package lib.ds.map;

import java.util.function.*;

/**
 * 3つの {@code int} をキー、{@code int} を値として保持する高速マップです。
 * 各キー成分をオフセット付きの符号付き21bitとして1つの {@code long} に圧縮し、
 * {@link LongIntMap} に処理を委譲します。各成分の有効範囲は
 * {@code -1_048_576} 以上 {@code 1_048_575} 以下です。
 */
@SuppressWarnings("unused")
public final class IntTripleIntMap {
	private static final int KEY_OFFSET = 1 << 20;
	private static final long KEY_MASK = (1L << 21) - 1;
	private final LongIntMap baseMap;

	public IntTripleIntMap() {
		baseMap = new LongIntMap();
	}

	public IntTripleIntMap(final int expectedSize) {
		baseMap = new LongIntMap(expectedSize);
	}

	public IntTripleIntMap(final int expectedSize, final int defaultValue) {
		baseMap = new LongIntMap(expectedSize, defaultValue);
	}

	private static long pack(final int a, final int b, final int c) {
		return ((((long) a + KEY_OFFSET) & KEY_MASK) << 42) | (((long) b + KEY_OFFSET) & KEY_MASK) << 21 | (((long) c + KEY_OFFSET) & KEY_MASK);
	}

	private static int unpack(final long key) {
		return (int) (key & KEY_MASK) - KEY_OFFSET;
	}

	public int getDefaultValue() {
		return baseMap.getDefaultValue();
	}

	public void setDefaultValue(final int defaultValue) {
		baseMap.setDefaultValue(defaultValue);
	}

	public int put(final int a, final int b, final int c, final int value) {
		return baseMap.put(pack(a, b, c), value);
	}

	public int get(final int a, final int b, final int c) {
		return baseMap.get(pack(a, b, c));
	}

	public int getOrDefault(final int a, final int b, final int c, final int defaultValue) {
		return baseMap.getOrDefault(pack(a, b, c), defaultValue);
	}

	public int increment(final int a, final int b, final int c) {
		return baseMap.increment(pack(a, b, c));
	}

	public int decrement(final int a, final int b, final int c) {
		return baseMap.decrement(pack(a, b, c));
	}

	public int add(final int a, final int b, final int c, final int delta) {
		return baseMap.add(pack(a, b, c), delta);
	}

	public int addOrDefault(final int a, final int b, final int c, final int delta, final int absentValue) {
		return baseMap.addOrDefault(pack(a, b, c), delta, absentValue);
	}

	public boolean remove(final int a, final int b, final int c) {
		return baseMap.remove(pack(a, b, c));
	}

	public boolean containsKey(final int a, final int b, final int c) {
		return baseMap.containsKey(pack(a, b, c));
	}

	public int merge(final int a, final int b, final int c, final int value, final IntBinaryOperator op) {
		return baseMap.merge(pack(a, b, c), value, op);
	}

	public int putIfAbsent(final int a, final int b, final int c, final int value) {
		return baseMap.putIfAbsent(pack(a, b, c), value);
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

	public void forEach(final IntTripleIntConsumer action) {
		baseMap.forEach((key, value) -> {
			final int a = unpack(key >>> 42);
			final int b = unpack(key >>> 21);
			final int c = unpack(key);
			action.accept(a, b, c, value);
		});
	}

	public void forEachKey(final IntTripleConsumer action) {
		baseMap.forEachKey(key -> {
			final int a = unpack(key >>> 42);
			final int b = unpack(key >>> 21);
			final int c = unpack(key);
			action.accept(a, b, c);
		});
	}

	public void forEachValue(final IntConsumer action) {
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

	public int[] values() {
		return baseMap.values();
	}

	public int[][] entries() {
		final int[][] res = new int[4][baseMap.size()];
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

	public interface KeysToLongAccumulator {
		long apply(long accumulator, int key1, int key2, int key3);
	}

	public interface EntryToLongAccumulator {
		long apply(long accumulator, int key1, int key2, int key3, int value);
	}

	public interface IntTripleIntConsumer {
		void accept(int a, int b, int c, int value);
	}

	public interface IntTripleConsumer {
		void accept(int a, int b, int c);
	}
}
