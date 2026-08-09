package lib.ds.map;

import java.util.function.*;

import lib.util.function.*;

/**
 * 3つの {@code int} をキー、{@code long} を値として保持する高速マップです。
 * 各キー成分をオフセット付きの符号付き21bitとして1つの {@code long} に圧縮し、
 * {@link LongLongMap} に処理を委譲します。各成分の有効範囲は
 * {@code -1_048_576} 以上 {@code 1_048_575} 以下です。
 */
@SuppressWarnings("unused")
public final class IntTripleLongMap {
	private static final int KEY_OFFSET = 1 << 20;
	private static final long KEY_MASK = (1L << 21) - 1;
	private final LongLongMap baseMap;

	public IntTripleLongMap() {
		baseMap = new LongLongMap();
	}

	public IntTripleLongMap(final int expectedSize) {
		baseMap = new LongLongMap(expectedSize);
	}

	public IntTripleLongMap(final int expectedSize, final long defaultValue) {
		baseMap = new LongLongMap(expectedSize, defaultValue);
	}

	private static long pack(final int a, final int b, final int c) {
		return ((((long) a + KEY_OFFSET) & KEY_MASK) << 42) | (((long) b + KEY_OFFSET) & KEY_MASK) << 21 | (((long) c + KEY_OFFSET) & KEY_MASK);
	}

	private static int unpack(final long key) {
		return (int) (key & KEY_MASK) - KEY_OFFSET;
	}

	public long getDefaultValue() {
		return baseMap.getDefaultValue();
	}

	public void setDefaultValue(final long defaultValue) {
		baseMap.setDefaultValue(defaultValue);
	}

	public long put(final int a, final int b, final int c, final long value) {
		return baseMap.put(pack(a, b, c), value);
	}

	public long get(final int a, final int b, final int c) {
		return baseMap.get(pack(a, b, c));
	}

	public long getOrDefault(final int a, final int b, final int c, final long defaultValue) {
		return baseMap.getOrDefault(pack(a, b, c), defaultValue);
	}

	public long increment(final int a, final int b, final int c) {
		return baseMap.increment(pack(a, b, c));
	}

	public long decrement(final int a, final int b, final int c) {
		return baseMap.decrement(pack(a, b, c));
	}

	public long add(final int a, final int b, final int c, final long delta) {
		return baseMap.add(pack(a, b, c), delta);
	}

	public long addOrDefault(final int a, final int b, final int c, final long delta, final long absentValue) {
		return baseMap.addOrDefault(pack(a, b, c), delta, absentValue);
	}

	public boolean remove(final int a, final int b, final int c) {
		return baseMap.remove(pack(a, b, c));
	}

	public boolean containsKey(final int a, final int b, final int c) {
		return baseMap.containsKey(pack(a, b, c));
	}

	public long merge(final int a, final int b, final int c, final long value, final LongBinaryOperator op) {
		return baseMap.merge(pack(a, b, c), value, op);
	}

	public long putIfAbsent(final int a, final int b, final int c, final long value) {
		return baseMap.putIfAbsent(pack(a, b, c), value);
	}

	public long computeIfAbsent(final int a, final int b, final int c, final IntTernaryToLongFunction op) {
		return baseMap.computeIfAbsent(pack(a, b, c), _ -> op.applyAsLong(a, b, c));
	}

	public long mergeMin(final int a, final int b, final int c, final long value) {
		return baseMap.mergeMin(pack(a, b, c), value);
	}

	public long mergeMax(final int a, final int b, final int c, final long value) {
		return baseMap.mergeMax(pack(a, b, c), value);
	}

	public void clear() {
		baseMap.clear();
	}

	public int size() {
		return baseMap.size();
	}

	public boolean isEmpty() {
		return baseMap.isEmpty();
	}

	public void forEach(final IntTernaryLongConsumer action) {
		baseMap.forEach((key, value) -> {
			final int a = unpack(key >>> 42);
			final int b = unpack(key >>> 21);
			final int c = unpack(key);
			action.accept(a, b, c, value);
		});
	}

	public void forEachKey(final IntTernaryConsumer action) {
		baseMap.forEachKey(key -> {
			final int a = unpack(key >>> 42);
			final int b = unpack(key >>> 21);
			final int c = unpack(key);
			action.accept(a, b, c);
		});
	}

	public void forEachValue(final LongConsumer action) {
		baseMap.forEachValue(action);
	}

	public long reduce(final long identity, final EntryToLongAccumulator accumulator) {
		return baseMap.reduce(identity, (acc, key, value) -> {
			final int a = unpack(key >>> 42);
			final int b = unpack(key >>> 21);
			final int c = unpack(key);
			return accumulator.apply(acc, a, b, c, value);
		});
	}

	public long reduceKeys(final long identity, final KeysToLongAccumulator accumulator) {
		return baseMap.reduceKeys(identity, (acc, key) -> {
			final int a = unpack(key >>> 42);
			final int b = unpack(key >>> 21);
			final int c = unpack(key);
			return accumulator.apply(acc, a, b, c);
		});
	}

	public long reduceValues(final long identity, final LongBinaryOperator accumulator) {
		return baseMap.reduceValues(identity, accumulator);
	}

	public int[][] keys() {
		final int[][] res = new int[3][baseMap.size()];
		final int stamp = baseMap.currentStamp();
		for (int i = 0, idx = 0; i < baseMap.keys.length; i++) {
			if (baseMap.stamps[i] != stamp) continue;
			final long key = baseMap.keys[i];
			res[0][idx] = unpack(key >>> 42);
			res[1][idx] = unpack(key >>> 21);
			res[2][idx] = unpack(key);
			idx++;
		}
		return res;
	}

	public long[] values() {
		return baseMap.values();
	}

	public long[][] entries() {
		final long[][] res = new long[4][baseMap.size()];
		final int stamp = baseMap.currentStamp();
		for (int i = 0, idx = 0; i < baseMap.keys.length; i++) {
			if (baseMap.stamps[i] != stamp) continue;
			final long key = baseMap.keys[i];
			res[0][idx] = unpack(key >>> 42);
			res[1][idx] = unpack(key >>> 21);
			res[2][idx] = unpack(key);
			res[3][idx] = baseMap.values[i];
			idx++;
		}
		return res;
	}

	/**
	 * 現在の累積値と3成分のキーを受け取り、次の累積値を返します。
	 */
	public interface KeysToLongAccumulator {
		/**
		 * @param accumulator 現在の累積値
		 * @param key1        キー1
		 * @param key2        キー2
		 * @param key3        キー3
		 * @return 次の累積値
		 */
		long apply(long accumulator, int key1, int key2, int key3);
	}

	/**
	 * 現在の累積値とエントリを受け取り、次の累積値を返します。
	 */
	public interface EntryToLongAccumulator {
		/**
		 * @param accumulator 現在の累積値
		 * @param key1        キー1
		 * @param key2        キー2
		 * @param key3        キー3
		 * @param value       値
		 * @return 次の累積値
		 */
		long apply(long accumulator, int key1, int key2, int key3, long value);
	}
}
