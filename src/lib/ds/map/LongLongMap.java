package lib.ds.map;

import static java.lang.Math.*;

import java.util.concurrent.*;
import java.util.function.*;

import lib.util.function.*;

/**
 * {@code long} 型のキーと値を保持する、オープンアドレス方式の高速マップです。
 * 線形探索、backward-shift deletion、世代番号による {@code O(1)} の {@link #clear()} を使用し、
 * 未存在キーの取得では設定済みの既定値を返します。
 */
@SuppressWarnings("unused")
public final class LongLongMap {
	private static final long SALT64 = ThreadLocalRandom.current().nextLong();

	long[] keys, values;
	int[] stamps;
	private long defaultValue;
	private int stamp, size, capacity, resizeThreshold, mask;

	public LongLongMap() {
		this(1024, 0);
	}

	public LongLongMap(final int expectedSize) {
		this(expectedSize, 0);
	}

	public LongLongMap(final int expectedSize, final long defaultValue) {
		this.defaultValue = defaultValue;
		capacity = normalizeCapacity(expectedSize);
		size = 0;
		stamp = 1;
		resizeThreshold = capacity - (capacity >>> 2);
		stamps = new int[capacity];
		keys = new long[capacity];
		values = new long[capacity];
		mask = capacity - 1;
	}

	private static int normalizeCapacity(final int expectedSize) {
		final int required = (expectedSize * 4 + 2) / 3;
		if (required <= 16) return 16;
		int cap = required;
		if ((cap & (cap - 1)) == 0) return cap;
		cap |= cap >>> 1;
		cap |= cap >>> 2;
		cap |= cap >>> 4;
		cap |= cap >>> 8;
		cap |= cap >>> 16;
		return cap + 1;
	}

	public long getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(final long defaultValue) {
		this.defaultValue = defaultValue;
	}

	public long get(final long key) {
		return getOrDefault(key, defaultValue);
	}

	public long getOrDefault(final long key, final long defaultValue) {
		for (int hash = hash(key); stamps[hash] == stamp; hash = (hash + 1) & mask) {
			if (keys[hash] == key) return values[hash];
		}
		return defaultValue;
	}

	public long increment(final long key) {
		return addOrDefault(key, 1, defaultValue + 1);
	}

	public long decrement(final long key) {
		return addOrDefault(key, -1, defaultValue - 1);
	}

	public long add(final long key, final long delta) {
		return addOrDefault(key, delta, defaultValue + delta);
	}

	public long addOrDefault(final long key, final long delta, final long absentValue) {
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
		return values[hash] = absentValue;
	}

	public long put(final long key, final long value) {
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

	public boolean remove(final long key) {
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

	public boolean containsKey(final long key) {
		for (int hash = hash(key); stamps[hash] == stamp; hash = (hash + 1) & mask) {
			if (keys[hash] == key) return true;
		}
		return false;
	}

	public long merge(final long key, final long value, final LongBinaryOperator op) {
		int hash = hash(key);
		for (; stamps[hash] == stamp; hash = (hash + 1) & mask) {
			if (keys[hash] == key) return values[hash] = op.applyAsLong(values[hash], value);
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

	public long putIfAbsent(final long key, final long value) {
		int hash = hash(key);
		for (; stamps[hash] == stamp; hash = (hash + 1) & mask) {
			if (keys[hash] == key) return values[hash];
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

	public long computeIfAbsent(final long key, final LongUnaryOperator op) {
		int hash = hash(key);
		for (; stamps[hash] == stamp; hash = (hash + 1) & mask) {
			if (keys[hash] == key) return values[hash];
		}
		return putIfAbsent(key, op.applyAsLong(key));
	}

	public long computeMin(final long key, final long value) {
		int hash = hash(key);
		for (; stamps[hash] == stamp; hash = (hash + 1) & mask) {
			if (keys[hash] == key) return values[hash] = min(values[hash], value);
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

	public long computeMax(final long key, final long value) {
		int hash = hash(key);
		for (; stamps[hash] == stamp; hash = (hash + 1) & mask) {
			if (keys[hash] == key) return values[hash] = max(values[hash], value);
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

	public void clear() {
		size = 0;
		stamp++;
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	int currentStamp() {
		return stamp;
	}

	public void forEach(final LongBinaryConsumer action) {
		for (int i = 0; i < capacity; i++) {
			if (stamps[i] != stamp) continue;
			action.accept(keys[i], values[i]);
		}
	}

	public void forEachKey(final LongConsumer action) {
		for (int i = 0; i < capacity; i++) {
			if (stamps[i] != stamp) continue;
			action.accept(keys[i]);
		}
	}

	public void forEachValue(final LongConsumer action) {
		for (int i = 0; i < capacity; i++) {
			if (stamps[i] != stamp) continue;
			action.accept(values[i]);
		}
	}

	public long reduce(final long identity, final EntryToLongAccumulator accumulator) {
		long result = identity;
		for (int i = 0; i < capacity; i++) {
			if (stamps[i] != stamp) continue;
			result = accumulator.apply(result, keys[i], values[i]);
		}
		return result;
	}

	public long reduceKeys(final long identity, final LongBinaryOperator accumulator) {
		long result = identity;
		for (int i = 0; i < capacity; i++) {
			if (stamps[i] != stamp) continue;
			result = accumulator.applyAsLong(result, keys[i]);
		}
		return result;
	}

	public long reduceValues(final long identity, final LongBinaryOperator accumulator) {
		long result = identity;
		for (int i = 0; i < capacity; i++) {
			if (stamps[i] != stamp) continue;
			result = accumulator.applyAsLong(result, values[i]);
		}
		return result;
	}

	public long[] keys() {
		final long[] res = new long[size];
		for (int i = 0, idx = 0; i < capacity; i++) {
			if (stamps[i] != stamp) continue;
			res[idx++] = keys[i];
		}
		return res;
	}

	public long[] values() {
		final long[] res = new long[size];
		for (int i = 0, idx = 0; i < capacity; i++) {
			if (stamps[i] != stamp) continue;
			res[idx++] = values[i];
		}
		return res;
	}

	public long[][] entries() {
		final long[][] res = new long[2][size];
		for (int i = 0, idx = 0; i < capacity; i++) {
			if (stamps[i] != stamp) continue;
			res[0][idx] = keys[i];
			res[1][idx] = values[i];
			idx++;
		}
		return res;
	}

	private void resize() {
		final int oldCapacity = capacity;
		final long[] oldKeys = keys, oldValues = values;
		final int[] oldStamps = stamps;
		capacity <<= 1;
		resizeThreshold = capacity - (capacity >>> 2);
		keys = new long[capacity];
		values = new long[capacity];
		stamps = new int[capacity];
		mask = capacity - 1;
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
		long h = key ^ SALT64;
		h ^= h >>> 33;
		h *= 0xff51afd7ed558ccdL;
		h ^= h >>> 33;
		return (int) h & mask;
	}

	/**
	 * 現在の累積値とエントリを受け取り、次の累積値を返します。
	 */
	public interface EntryToLongAccumulator {
		/**
		 * @param accumulator 現在の累積値
		 * @param key         キー
		 * @param value       値
		 * @return 次の累積値
		 */
		long apply(long accumulator, long key, long value);
	}
}
