package verify.ds.map;

import static java.lang.Math.*;

import java.util.*;

/**
 * 整数型Mapのハッシュ関数を、追加・取得・削除を含む同一ワークロードで比較します。
 */
public final class TestHash {
	private static final int QUERY_COUNT = 1_000_000;
	private static final int EXPECTED_SIZE = 100_000;
	private static final int WARMUP_ROUNDS = 2;
	private static final int MEASURE_ROUNDS = 5;

	private static final int SPLIT_MIX_32 = 0;
	private static final int IDENTITY_32 = 1;
	private static final int GOLDEN_RATIO_32 = 2;
	private static final int MURMUR_HASH_3_32 = 3;
	private static final int THOMAS_WANG_32 = 4;

	private static final int LIGHT_MURMUR_64 = 0;
	private static final int THOMAS_WANG_64 = 1;
	private static final int SPLIT_MIX_64 = 2;
	private static final int MURMUR_HASH_3_64 = 3;
	private static final int FIBONACCI_64 = 4;
	private static final int BENCHMARK_SALT_32 = 0x243f6a88;
	private static final long BENCHMARK_SALT_64 = 0x243f6a8885a308d3L;

	private static final String[] INT_HASH_NAMES = {"SplitMix32 Variant", "Identity Hash", "Golden Ratio", "MurmurHash3 32-bit", "Thomas Wang 32-bit"};
	private static final String[] LONG_HASH_NAMES = {"MurmurHash3系軽量", "Thomas Wang", "SplitMix64", "MurmurHash3 64-bit", "修正版フィボナッチ"};

	private static final int[] QUERY_INDICES = createQueryIndices();
	private static final int[] INT_KEYS = createIntKeys();
	private static final long[] LONG_KEYS = createLongKeys();
	private static final long[] PAIR_KEYS = createPairKeys();
	private static final long[] TRIPLE_KEYS = createTripleKeys();
	private static long blackhole;

	private TestHash() {
	}

	public static void main(final String[] args) {
		System.out.printf(Locale.ROOT, "queries=%,d, expectedSize=%,d, warmup=%d, measure=%d%n", QUERY_COUNT, EXPECTED_SIZE, WARMUP_ROUNDS, MEASURE_ROUNDS);
		System.out.println("operations: add/put=50%, get=25%, remove=25%");
		System.out.println("Pair/Tripleのパック済みキー生成は計測時間に含めません。");
		System.out.println("再現性を保つため、全方式に固定saltを適用します。");

		benchmark("IntIntMap", INT_HASH_NAMES, hashType -> measureIntInt(hashType, INT_KEYS));
		benchmark("IntLongMap", INT_HASH_NAMES, hashType -> measureIntLong(hashType, INT_KEYS));
		benchmark("LongIntMap", LONG_HASH_NAMES, hashType -> measureLongInt(hashType, LONG_KEYS));
		benchmark("LongLongMap", LONG_HASH_NAMES, hashType -> measureLongLong(hashType, LONG_KEYS));
		benchmark("IntPairIntMap", LONG_HASH_NAMES, hashType -> measureLongInt(hashType, PAIR_KEYS));
		benchmark("IntPairLongMap", LONG_HASH_NAMES, hashType -> measureLongLong(hashType, PAIR_KEYS));
		benchmark("IntTripleIntMap", LONG_HASH_NAMES, hashType -> measureLongInt(hashType, TRIPLE_KEYS));
		benchmark("IntTripleLongMap", LONG_HASH_NAMES, hashType -> measureLongLong(hashType, TRIPLE_KEYS));

		System.out.println("blackhole=" + blackhole);
	}

	private static void benchmark(final String className, final String[] hashNames, final Benchmark benchmark) {
		System.out.println();
		System.out.println("[" + className + "]");
		long expectedChecksum = 0;
		for (int hashType = 0; hashType < hashNames.length; hashType++) {
			final Result result = benchmark.run(hashType);
			if (hashType == 0) {
				expectedChecksum = result.checksum;
			} else if (result.checksum != expectedChecksum) {
				throw new AssertionError(className + ": checksum mismatch");
			}
			blackhole ^= result.checksum;
			System.out.printf(Locale.ROOT, "%-24s avg=%8.3f ms  best=%8.3f ms%n", hashNames[hashType], result.averageNanos / 1_000_000.0, result.bestNanos / 1_000_000.0);
		}
	}

	private static Result measureIntInt(final int hashType, final int[] keys) {
		final IntIntBenchmarkMap map = new IntIntBenchmarkMap(EXPECTED_SIZE, hashType);
		for (int i = 0; i < WARMUP_ROUNDS; i++) runIntInt(map, keys);
		long totalNanos = 0;
		long bestNanos = Long.MAX_VALUE;
		long checksum = 0;
		for (int i = 0; i < MEASURE_ROUNDS; i++) {
			final Round round = runIntInt(map, keys);
			totalNanos += round.nanos;
			bestNanos = min(bestNanos, round.nanos);
			if (i == 0) {
				checksum = round.checksum;
			} else if (round.checksum != checksum) {
				throw new AssertionError("IntIntMap: unstable checksum");
			}
		}
		return new Result(totalNanos / MEASURE_ROUNDS, bestNanos, checksum);
	}

	private static Result measureIntLong(final int hashType, final int[] keys) {
		final IntLongBenchmarkMap map = new IntLongBenchmarkMap(EXPECTED_SIZE, hashType);
		for (int i = 0; i < WARMUP_ROUNDS; i++) runIntLong(map, keys);
		long totalNanos = 0;
		long bestNanos = Long.MAX_VALUE;
		long checksum = 0;
		for (int i = 0; i < MEASURE_ROUNDS; i++) {
			final Round round = runIntLong(map, keys);
			totalNanos += round.nanos;
			bestNanos = min(bestNanos, round.nanos);
			if (i == 0) {
				checksum = round.checksum;
			} else if (round.checksum != checksum) {
				throw new AssertionError("IntLongMap: unstable checksum");
			}
		}
		return new Result(totalNanos / MEASURE_ROUNDS, bestNanos, checksum);
	}

	private static Result measureLongInt(final int hashType, final long[] keys) {
		final LongIntBenchmarkMap map = new LongIntBenchmarkMap(EXPECTED_SIZE, hashType);
		for (int i = 0; i < WARMUP_ROUNDS; i++) runLongInt(map, keys);
		long totalNanos = 0;
		long bestNanos = Long.MAX_VALUE;
		long checksum = 0;
		for (int i = 0; i < MEASURE_ROUNDS; i++) {
			final Round round = runLongInt(map, keys);
			totalNanos += round.nanos;
			bestNanos = min(bestNanos, round.nanos);
			if (i == 0) {
				checksum = round.checksum;
			} else if (round.checksum != checksum) {
				throw new AssertionError("LongIntMap: unstable checksum");
			}
		}
		return new Result(totalNanos / MEASURE_ROUNDS, bestNanos, checksum);
	}

	private static Result measureLongLong(final int hashType, final long[] keys) {
		final LongLongBenchmarkMap map = new LongLongBenchmarkMap(EXPECTED_SIZE, hashType);
		for (int i = 0; i < WARMUP_ROUNDS; i++) runLongLong(map, keys);
		long totalNanos = 0;
		long bestNanos = Long.MAX_VALUE;
		long checksum = 0;
		for (int i = 0; i < MEASURE_ROUNDS; i++) {
			final Round round = runLongLong(map, keys);
			totalNanos += round.nanos;
			bestNanos = min(bestNanos, round.nanos);
			if (i == 0) {
				checksum = round.checksum;
			} else if (round.checksum != checksum) {
				throw new AssertionError("LongLongMap: unstable checksum");
			}
		}
		return new Result(totalNanos / MEASURE_ROUNDS, bestNanos, checksum);
	}

	private static Round runIntInt(final IntIntBenchmarkMap map, final int[] keys) {
		map.clear();
		for (int i = 0; i < keys.length; i++) map.put(keys[i], i);
		long checksum = 0;
		final long start = System.nanoTime();
		for (int i = 0; i < QUERY_COUNT; i++) {
			final int key = keys[QUERY_INDICES[i]];
			switch (i & 7) {
				case 0, 4 -> checksum += map.add(key, 1);
				case 1, 5 -> checksum += map.get(key);
				case 2, 6 -> checksum += map.remove(key) ? 1 : 0;
				default -> checksum += map.put(key, i);
			}
		}
		final long nanos = System.nanoTime() - start;
		return new Round(nanos, checksum ^ map.size());
	}

	private static Round runIntLong(final IntLongBenchmarkMap map, final int[] keys) {
		map.clear();
		for (int i = 0; i < keys.length; i++) map.put(keys[i], i);
		long checksum = 0;
		final long start = System.nanoTime();
		for (int i = 0; i < QUERY_COUNT; i++) {
			final int key = keys[QUERY_INDICES[i]];
			switch (i & 7) {
				case 0, 4 -> checksum += map.add(key, 1);
				case 1, 5 -> checksum += map.get(key);
				case 2, 6 -> checksum += map.remove(key) ? 1 : 0;
				default -> checksum += map.put(key, i);
			}
		}
		final long nanos = System.nanoTime() - start;
		return new Round(nanos, checksum ^ map.size());
	}

	private static Round runLongInt(final LongIntBenchmarkMap map, final long[] keys) {
		map.clear();
		for (int i = 0; i < keys.length; i++) map.put(keys[i], i);
		long checksum = 0;
		final long start = System.nanoTime();
		for (int i = 0; i < QUERY_COUNT; i++) {
			final long key = keys[QUERY_INDICES[i]];
			switch (i & 7) {
				case 0, 4 -> checksum += map.add(key, 1);
				case 1, 5 -> checksum += map.get(key);
				case 2, 6 -> checksum += map.remove(key) ? 1 : 0;
				default -> checksum += map.put(key, i);
			}
		}
		final long nanos = System.nanoTime() - start;
		return new Round(nanos, checksum ^ map.size());
	}

	private static Round runLongLong(final LongLongBenchmarkMap map, final long[] keys) {
		map.clear();
		for (int i = 0; i < keys.length; i++) map.put(keys[i], i);
		long checksum = 0;
		final long start = System.nanoTime();
		for (int i = 0; i < QUERY_COUNT; i++) {
			final long key = keys[QUERY_INDICES[i]];
			switch (i & 7) {
				case 0, 4 -> checksum += map.add(key, 1);
				case 1, 5 -> checksum += map.get(key);
				case 2, 6 -> checksum += map.remove(key) ? 1 : 0;
				default -> checksum += map.put(key, i);
			}
		}
		final long nanos = System.nanoTime() - start;
		return new Round(nanos, checksum ^ map.size());
	}

	private static int[] createQueryIndices() {
		final int[] indices = new int[QUERY_COUNT];
		int state = 0x12345678;
		for (int i = 0; i < QUERY_COUNT; i++) {
			state ^= state << 13;
			state ^= state >>> 17;
			state ^= state << 5;
			indices[i] = (state >>> 1) % EXPECTED_SIZE;
		}
		return indices;
	}

	private static int[] createIntKeys() {
		final int[] keys = new int[EXPECTED_SIZE];
		for (int i = 0; i < EXPECTED_SIZE; i++) keys[i] = (i + 1) * 0x9e3779b9;
		return keys;
	}

	private static long[] createLongKeys() {
		final long[] keys = new long[EXPECTED_SIZE];
		for (int i = 0; i < EXPECTED_SIZE; i++) keys[i] = (i + 1L) * 0x9e3779b97f4a7c15L;
		return keys;
	}

	private static long[] createPairKeys() {
		final long[] keys = new long[EXPECTED_SIZE];
		final long keyMask = (1L << 32) - 1;
		for (int i = 0; i < EXPECTED_SIZE; i++) {
			final int a = i - EXPECTED_SIZE / 2;
			final int b = Integer.rotateLeft((i + 1) * 0x7feb352d, 13);
			keys[i] = ((long) a << 32) | (b & keyMask);
		}
		return keys;
	}

	private static long[] createTripleKeys() {
		final long[] keys = new long[EXPECTED_SIZE];
		final int keyOffset = 1 << 20;
		final long keyMask = (1L << 21) - 1;
		for (int i = 0; i < EXPECTED_SIZE; i++) {
			final int a = i - EXPECTED_SIZE / 2;
			final int b = (i * 31 + 7) % EXPECTED_SIZE - EXPECTED_SIZE / 2;
			final int c = (i * 73 + 11) % EXPECTED_SIZE - EXPECTED_SIZE / 2;
			keys[i] = ((((long) a + keyOffset) & keyMask) << 42) | ((((long) b + keyOffset) & keyMask) << 21) | (((long) c + keyOffset) & keyMask);
		}
		return keys;
	}

	private static int normalizeCapacity(final int expectedSize) {
		final long required = ((long) expectedSize * 4 + 2) / 3;
		int capacity = max(16, (int) required);
		if ((capacity & (capacity - 1)) == 0) return capacity;
		capacity--;
		capacity |= capacity >>> 1;
		capacity |= capacity >>> 2;
		capacity |= capacity >>> 4;
		capacity |= capacity >>> 8;
		capacity |= capacity >>> 16;
		return capacity + 1;
	}

	@FunctionalInterface
	private interface Benchmark {
		Result run(int hashType);
	}

	private record Result(long averageNanos, long bestNanos, long checksum) {}

	private record Round(long nanos, long checksum) {}

	/**
	 * ハッシュ方式以外の処理を完全に共通化するため、方式番号をfinalフィールドで保持します。
	 * switchのコストは全方式で共通です。
	 */
	private static final class IntIntBenchmarkMap {
		private final int hashType;
		private int[] keys, values, stamps;
		private int stamp, size, capacity, resizeThreshold, mask;

		private IntIntBenchmarkMap(final int expectedSize, final int hashType) {
			this.hashType = hashType;
			capacity = normalizeCapacity(expectedSize);
			resizeThreshold = capacity - (capacity >>> 2);
			mask = capacity - 1;
			stamp = 1;
			keys = new int[capacity];
			values = new int[capacity];
			stamps = new int[capacity];
		}

		private int get(final int key) {
			for (int hash = hash(key); stamps[hash] == stamp; hash = (hash + 1) & mask) {
				if (keys[hash] == key) return values[hash];
			}
			return 0;
		}

		private int add(final int key, final int delta) {
			int hash = hash(key);
			for (; stamps[hash] == stamp; hash = (hash + 1) & mask) {
				if (keys[hash] == key) return values[hash] += delta;
			}
			if (size >= resizeThreshold) {
				resize();
				hash = hash(key);
				while (stamps[hash] == stamp) hash = (hash + 1) & mask;
			}
			stamps[hash] = stamp;
			keys[hash] = key;
			size++;
			return values[hash] = delta;
		}

		private int put(final int key, final int value) {
			int hash = hash(key);
			for (; stamps[hash] == stamp; hash = (hash + 1) & mask) {
				if (keys[hash] == key) return values[hash] = value;
			}
			if (size >= resizeThreshold) {
				resize();
				hash = hash(key);
				while (stamps[hash] == stamp) hash = (hash + 1) & mask;
			}
			stamps[hash] = stamp;
			keys[hash] = key;
			size++;
			return values[hash] = value;
		}

		private boolean remove(final int key) {
			for (int hash = hash(key); stamps[hash] == stamp; hash = (hash + 1) & mask) {
				if (keys[hash] != key) continue;
				int hole = hash;
				for (int next = (hole + 1) & mask; stamps[next] == stamp; next = (next + 1) & mask) {
					final int home = hash(keys[next]);
					if (((next - home) & mask) >= ((next - hole) & mask)) {
						keys[hole] = keys[next];
						values[hole] = values[next];
						stamps[hole] = stamp;
						hole = next;
					}
				}
				stamps[hole] = 0;
				size--;
				return true;
			}
			return false;
		}

		private int size() {
			return size;
		}

		private void clear() {
			size = 0;
			stamp++;
		}

		private void resize() {
			final int oldCapacity = capacity;
			final int[] oldKeys = keys, oldValues = values, oldStamps = stamps;
			capacity <<= 1;
			resizeThreshold = capacity - (capacity >>> 2);
			mask = capacity - 1;
			keys = new int[capacity];
			values = new int[capacity];
			stamps = new int[capacity];
			for (int i = 0; i < oldCapacity; i++) {
				if (oldStamps[i] != stamp) continue;
				final int key = oldKeys[i];
				int hash = hash(key);
				while (stamps[hash] == stamp) hash = (hash + 1) & mask;
				stamps[hash] = stamp;
				keys[hash] = key;
				values[hash] = oldValues[i];
			}
		}

		private int hash(final int key) {
			int h = key ^ BENCHMARK_SALT_32;
			switch (hashType) {
				case SPLIT_MIX_32 -> {
					h ^= h >>> 16;
					h *= 0x7feb352d;
					h ^= h >>> 15;
					h *= 0x846ca68b;
					h ^= h >>> 16;
				}
				case IDENTITY_32 -> {
				}
				case GOLDEN_RATIO_32 -> {
					h ^= h >>> 16;
					h *= 0x9e3779b9;
					h ^= h >>> 16;
				}
				case MURMUR_HASH_3_32 -> {
					h ^= h >>> 16;
					h *= 0x85ebca6b;
					h ^= h >>> 13;
					h *= 0xc2b2ae35;
					h ^= h >>> 16;
				}
				case THOMAS_WANG_32 -> {
					h = (h ^ 61) ^ (h >>> 16);
					h += h << 3;
					h ^= h >>> 4;
					h *= 0x27d4eb2d;
					h ^= h >>> 15;
				}
				default -> throw new AssertionError(hashType);
			}
			return h & mask;
		}
	}

	private static final class IntLongBenchmarkMap {
		private final int hashType;
		private int[] keys, stamps;
		private long[] values;
		private int stamp, size, capacity, resizeThreshold, mask;

		private IntLongBenchmarkMap(final int expectedSize, final int hashType) {
			this.hashType = hashType;
			capacity = normalizeCapacity(expectedSize);
			resizeThreshold = capacity - (capacity >>> 2);
			mask = capacity - 1;
			stamp = 1;
			keys = new int[capacity];
			values = new long[capacity];
			stamps = new int[capacity];
		}

		private long get(final int key) {
			for (int hash = hash(key); stamps[hash] == stamp; hash = (hash + 1) & mask) {
				if (keys[hash] == key) return values[hash];
			}
			return 0;
		}

		private long add(final int key, final long delta) {
			int hash = hash(key);
			for (; stamps[hash] == stamp; hash = (hash + 1) & mask) {
				if (keys[hash] == key) return values[hash] += delta;
			}
			if (size >= resizeThreshold) {
				resize();
				hash = hash(key);
				while (stamps[hash] == stamp) hash = (hash + 1) & mask;
			}
			stamps[hash] = stamp;
			keys[hash] = key;
			size++;
			return values[hash] = delta;
		}

		private long put(final int key, final long value) {
			int hash = hash(key);
			for (; stamps[hash] == stamp; hash = (hash + 1) & mask) {
				if (keys[hash] == key) return values[hash] = value;
			}
			if (size >= resizeThreshold) {
				resize();
				hash = hash(key);
				while (stamps[hash] == stamp) hash = (hash + 1) & mask;
			}
			stamps[hash] = stamp;
			keys[hash] = key;
			size++;
			return values[hash] = value;
		}

		private boolean remove(final int key) {
			for (int hash = hash(key); stamps[hash] == stamp; hash = (hash + 1) & mask) {
				if (keys[hash] != key) continue;
				int hole = hash;
				for (int next = (hole + 1) & mask; stamps[next] == stamp; next = (next + 1) & mask) {
					final int home = hash(keys[next]);
					if (((next - home) & mask) >= ((next - hole) & mask)) {
						keys[hole] = keys[next];
						values[hole] = values[next];
						stamps[hole] = stamp;
						hole = next;
					}
				}
				stamps[hole] = 0;
				size--;
				return true;
			}
			return false;
		}

		private int size() {
			return size;
		}

		private void clear() {
			size = 0;
			stamp++;
		}

		private void resize() {
			final int oldCapacity = capacity;
			final int[] oldKeys = keys, oldStamps = stamps;
			final long[] oldValues = values;
			capacity <<= 1;
			resizeThreshold = capacity - (capacity >>> 2);
			mask = capacity - 1;
			keys = new int[capacity];
			values = new long[capacity];
			stamps = new int[capacity];
			for (int i = 0; i < oldCapacity; i++) {
				if (oldStamps[i] != stamp) continue;
				final int key = oldKeys[i];
				int hash = hash(key);
				while (stamps[hash] == stamp) hash = (hash + 1) & mask;
				stamps[hash] = stamp;
				keys[hash] = key;
				values[hash] = oldValues[i];
			}
		}

		private int hash(final int key) {
			int h = key ^ BENCHMARK_SALT_32;
			switch (hashType) {
				case SPLIT_MIX_32 -> {
					h ^= h >>> 16;
					h *= 0x7feb352d;
					h ^= h >>> 15;
					h *= 0x846ca68b;
					h ^= h >>> 16;
				}
				case IDENTITY_32 -> {
				}
				case GOLDEN_RATIO_32 -> {
					h ^= h >>> 16;
					h *= 0x9e3779b9;
					h ^= h >>> 16;
				}
				case MURMUR_HASH_3_32 -> {
					h ^= h >>> 16;
					h *= 0x85ebca6b;
					h ^= h >>> 13;
					h *= 0xc2b2ae35;
					h ^= h >>> 16;
				}
				case THOMAS_WANG_32 -> {
					h = (h ^ 61) ^ (h >>> 16);
					h += h << 3;
					h ^= h >>> 4;
					h *= 0x27d4eb2d;
					h ^= h >>> 15;
				}
				default -> throw new AssertionError(hashType);
			}
			return h & mask;
		}
	}

	private static final class LongIntBenchmarkMap {
		private final int hashType;
		private long[] keys;
		private int[] values, stamps;
		private int stamp, size, capacity, resizeThreshold, mask;

		private LongIntBenchmarkMap(final int expectedSize, final int hashType) {
			this.hashType = hashType;
			capacity = normalizeCapacity(expectedSize);
			resizeThreshold = capacity - (capacity >>> 2);
			mask = capacity - 1;
			stamp = 1;
			keys = new long[capacity];
			values = new int[capacity];
			stamps = new int[capacity];
		}

		private int get(final long key) {
			for (int hash = hash(key); stamps[hash] == stamp; hash = (hash + 1) & mask) {
				if (keys[hash] == key) return values[hash];
			}
			return 0;
		}

		private int add(final long key, final int delta) {
			int hash = hash(key);
			for (; stamps[hash] == stamp; hash = (hash + 1) & mask) {
				if (keys[hash] == key) return values[hash] += delta;
			}
			if (size >= resizeThreshold) {
				resize();
				hash = hash(key);
				while (stamps[hash] == stamp) hash = (hash + 1) & mask;
			}
			stamps[hash] = stamp;
			keys[hash] = key;
			size++;
			return values[hash] = delta;
		}

		private int put(final long key, final int value) {
			int hash = hash(key);
			for (; stamps[hash] == stamp; hash = (hash + 1) & mask) {
				if (keys[hash] == key) return values[hash] = value;
			}
			if (size >= resizeThreshold) {
				resize();
				hash = hash(key);
				while (stamps[hash] == stamp) hash = (hash + 1) & mask;
			}
			stamps[hash] = stamp;
			keys[hash] = key;
			size++;
			return values[hash] = value;
		}

		private boolean remove(final long key) {
			for (int hash = hash(key); stamps[hash] == stamp; hash = (hash + 1) & mask) {
				if (keys[hash] != key) continue;
				int hole = hash;
				for (int next = (hole + 1) & mask; stamps[next] == stamp; next = (next + 1) & mask) {
					final int home = hash(keys[next]);
					if (((next - home) & mask) >= ((next - hole) & mask)) {
						keys[hole] = keys[next];
						values[hole] = values[next];
						stamps[hole] = stamp;
						hole = next;
					}
				}
				stamps[hole] = 0;
				size--;
				return true;
			}
			return false;
		}

		private int size() {
			return size;
		}

		private void clear() {
			size = 0;
			stamp++;
		}

		private void resize() {
			final int oldCapacity = capacity;
			final long[] oldKeys = keys;
			final int[] oldValues = values, oldStamps = stamps;
			capacity <<= 1;
			resizeThreshold = capacity - (capacity >>> 2);
			mask = capacity - 1;
			keys = new long[capacity];
			values = new int[capacity];
			stamps = new int[capacity];
			for (int i = 0; i < oldCapacity; i++) {
				if (oldStamps[i] != stamp) continue;
				final long key = oldKeys[i];
				int hash = hash(key);
				while (stamps[hash] == stamp) hash = (hash + 1) & mask;
				stamps[hash] = stamp;
				keys[hash] = key;
				values[hash] = oldValues[i];
			}
		}

		private int hash(final long key) {
			long h = key ^ BENCHMARK_SALT_64;
			switch (hashType) {
				case LIGHT_MURMUR_64 -> {
					h ^= h >>> 33;
					h *= 0xff51afd7ed558ccdL;
					h ^= h >>> 33;
				}
				case THOMAS_WANG_64 -> {
					h ^= h >>> 21;
					h ^= h << 37;
					h ^= h >>> 28;
					h ^= h << 15;
					h ^= h >>> 32;
				}
				case SPLIT_MIX_64 -> {
					h ^= h >>> 30;
					h *= 0xbf58476d1ce4e5b9L;
					h ^= h >>> 27;
					h *= 0x94d049bb133111ebL;
					h ^= h >>> 31;
				}
				case MURMUR_HASH_3_64 -> {
					h ^= h >>> 33;
					h *= 0xff51afd7ed558ccdL;
					h ^= h >>> 33;
					h *= 0xc4ceb9fe1a85ec53L;
					h ^= h >>> 33;
				}
				case FIBONACCI_64 -> {
					h *= 0x9e3779b97f4a7c15L;
					return (int) (h >>> 32) & mask;
				}
				default -> throw new AssertionError(hashType);
			}
			return (int) h & mask;
		}
	}

	private static final class LongLongBenchmarkMap {
		private final int hashType;
		private long[] keys, values;
		private int[] stamps;
		private int stamp, size, capacity, resizeThreshold, mask;

		private LongLongBenchmarkMap(final int expectedSize, final int hashType) {
			this.hashType = hashType;
			capacity = normalizeCapacity(expectedSize);
			resizeThreshold = capacity - (capacity >>> 2);
			mask = capacity - 1;
			stamp = 1;
			keys = new long[capacity];
			values = new long[capacity];
			stamps = new int[capacity];
		}

		private long get(final long key) {
			for (int hash = hash(key); stamps[hash] == stamp; hash = (hash + 1) & mask) {
				if (keys[hash] == key) return values[hash];
			}
			return 0;
		}

		private long add(final long key, final long delta) {
			int hash = hash(key);
			for (; stamps[hash] == stamp; hash = (hash + 1) & mask) {
				if (keys[hash] == key) return values[hash] += delta;
			}
			if (size >= resizeThreshold) {
				resize();
				hash = hash(key);
				while (stamps[hash] == stamp) hash = (hash + 1) & mask;
			}
			stamps[hash] = stamp;
			keys[hash] = key;
			size++;
			return values[hash] = delta;
		}

		private long put(final long key, final long value) {
			int hash = hash(key);
			for (; stamps[hash] == stamp; hash = (hash + 1) & mask) {
				if (keys[hash] == key) return values[hash] = value;
			}
			if (size >= resizeThreshold) {
				resize();
				hash = hash(key);
				while (stamps[hash] == stamp) hash = (hash + 1) & mask;
			}
			stamps[hash] = stamp;
			keys[hash] = key;
			size++;
			return values[hash] = value;
		}

		private boolean remove(final long key) {
			for (int hash = hash(key); stamps[hash] == stamp; hash = (hash + 1) & mask) {
				if (keys[hash] != key) continue;
				int hole = hash;
				for (int next = (hole + 1) & mask; stamps[next] == stamp; next = (next + 1) & mask) {
					final int home = hash(keys[next]);
					if (((next - home) & mask) >= ((next - hole) & mask)) {
						keys[hole] = keys[next];
						values[hole] = values[next];
						stamps[hole] = stamp;
						hole = next;
					}
				}
				stamps[hole] = 0;
				size--;
				return true;
			}
			return false;
		}

		private int size() {
			return size;
		}

		private void clear() {
			size = 0;
			stamp++;
		}

		private void resize() {
			final int oldCapacity = capacity;
			final long[] oldKeys = keys, oldValues = values;
			final int[] oldStamps = stamps;
			capacity <<= 1;
			resizeThreshold = capacity - (capacity >>> 2);
			mask = capacity - 1;
			keys = new long[capacity];
			values = new long[capacity];
			stamps = new int[capacity];
			for (int i = 0; i < oldCapacity; i++) {
				if (oldStamps[i] != stamp) continue;
				final long key = oldKeys[i];
				int hash = hash(key);
				while (stamps[hash] == stamp) hash = (hash + 1) & mask;
				stamps[hash] = stamp;
				keys[hash] = key;
				values[hash] = oldValues[i];
			}
		}

		private int hash(final long key) {
			long h = key ^ BENCHMARK_SALT_64;
			switch (hashType) {
				case LIGHT_MURMUR_64 -> {
					h ^= h >>> 33;
					h *= 0xff51afd7ed558ccdL;
					h ^= h >>> 33;
				}
				case THOMAS_WANG_64 -> {
					h ^= h >>> 21;
					h ^= h << 37;
					h ^= h >>> 28;
					h ^= h << 15;
					h ^= h >>> 32;
				}
				case SPLIT_MIX_64 -> {
					h ^= h >>> 30;
					h *= 0xbf58476d1ce4e5b9L;
					h ^= h >>> 27;
					h *= 0x94d049bb133111ebL;
					h ^= h >>> 31;
				}
				case MURMUR_HASH_3_64 -> {
					h ^= h >>> 33;
					h *= 0xff51afd7ed558ccdL;
					h ^= h >>> 33;
					h *= 0xc4ceb9fe1a85ec53L;
					h ^= h >>> 33;
				}
				case FIBONACCI_64 -> {
					h *= 0x9e3779b97f4a7c15L;
					return (int) (h >>> 32) & mask;
				}
				default -> throw new AssertionError(hashType);
			}
			return (int) h & mask;
		}
	}
}
