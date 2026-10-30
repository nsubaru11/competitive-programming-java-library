package lib.ds.map;

import java.util.function.*;

import lib.util.function.*;

/**
 * int の3つ組 (a, b, c) をキー、long を値とするハッシュマップ。キーを1つの long にまとめて LongLongMap へ委譲する。
 * 各成分は {@code -2^20} 以上 {@code 2^20} 未満とする。範囲外の成分を渡すと別のキーと衝突する。
 * 既定値と戻り値の規約は LongLongMap と同じである。
 */
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

	public IntTripleLongMap(final IntTripleLongMap other) {
		baseMap = new LongLongMap(other.baseMap);
	}

	private static long pack(final int a, final int b, final int c) {
		return ((a + KEY_OFFSET) & KEY_MASK) << 42 | ((b + KEY_OFFSET) & KEY_MASK) << 21 | ((c + KEY_OFFSET) & KEY_MASK);
	}

	private static int unpack(final long key) {
		return (int) (key & KEY_MASK) - KEY_OFFSET;
	}

	public int size() {
		return baseMap.size();
	}

	public boolean isEmpty() {
		return baseMap.isEmpty();
	}

	public long getDefaultValue() {
		return baseMap.getDefaultValue();
	}

	public void setDefaultValue(final long defaultValue) {
		baseMap.setDefaultValue(defaultValue);
	}

	public boolean containsKey(final int a, final int b, final int c) {
		return baseMap.containsKey(pack(a, b, c));
	}

	public long get(final int a, final int b, final int c) {
		return baseMap.get(pack(a, b, c));
	}

	public long getOrDefault(final int a, final int b, final int c, final long defaultValue) {
		return baseMap.getOrDefault(pack(a, b, c), defaultValue);
	}

	public long put(final int a, final int b, final int c, final long value) {
		return baseMap.put(pack(a, b, c), value);
	}

	public long putIfAbsent(final int a, final int b, final int c, final long value) {
		return baseMap.putIfAbsent(pack(a, b, c), value);
	}

	public long computeIfAbsent(final int a, final int b, final int c, final IntTernaryToLongFunction f) {
		return baseMap.computeIfAbsent(pack(a, b, c), _ -> f.applyAsLong(a, b, c));
	}

	public long remove(final int a, final int b, final int c) {
		return baseMap.remove(pack(a, b, c));
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

	public long mergeMin(final int a, final int b, final int c, final long value) {
		return baseMap.mergeMin(pack(a, b, c), value);
	}

	public long mergeMax(final int a, final int b, final int c, final long value) {
		return baseMap.mergeMax(pack(a, b, c), value);
	}

	public long merge(final int a, final int b, final int c, final long value, final LongBinaryOperator f) {
		return baseMap.merge(pack(a, b, c), value, f);
	}

	public void clear() {
		baseMap.clear();
	}

	/**
	 * {@code [0]} に a、{@code [1]} に b、{@code [2]} に c を並べた配列を返す。
	 */
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

	/**
	 * {@code [0]} に a、{@code [1]} に b、{@code [2]} に c、{@code [3]} に値を並べた配列を返す。
	 */
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

	/**
	 * 現在の累積値と3成分のキーを受け取り、次の累積値を返す。
	 */
	public interface KeysToLongAccumulator {
		long apply(long accumulator, int key1, int key2, int key3);
	}

	/**
	 * 現在の累積値とエントリを受け取り、次の累積値を返す。
	 */
	public interface EntryToLongAccumulator {
		long apply(long accumulator, int key1, int key2, int key3, long value);
	}
}
