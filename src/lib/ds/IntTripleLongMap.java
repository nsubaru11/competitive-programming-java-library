package lib.ds;

import java.util.function.*;

@SuppressWarnings("unused")
public final class IntTripleLongMap {
	private static final long MASK = 0x1FFFFFL;
	private final BaseLongLongMap baseMap;

	public IntTripleLongMap(final int initialCapacity) {
		baseMap = new BaseLongLongMap(initialCapacity);
	}

	private static long pack(final int a, final int b, final int c) {
		return (((long) a & MASK) << 42) | (((long) b & MASK) << 21) | (c & MASK);
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
		return addOrDefault(a, b, c, 1, 1);
	}

	public long decrement(final int a, final int b, final int c) {
		return addOrDefault(a, b, c, -1, -1);
	}

	public long add(final int a, final int b, final int c, final long delta) {
		return addOrDefault(a, b, c, delta, delta);
	}

	public long addOrDefault(final int a, final int b, final int c, final long delta, final long defaultValue) {
		return baseMap.addOrDefault(pack(a, b, c), delta, defaultValue);
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

	public void clear() {
		baseMap.clear();
	}

	public int size() {
		return baseMap.size();
	}

	public boolean isEmpty() {
		return baseMap.isEmpty();
	}

	public void forEach(final IntTripleLongConsumer action) {
		baseMap.forEach((key, value) -> {
			final int a = (int) (key >>> 42);
			final int b = (int) (key >>> 21 & MASK);
			final int c = (int) (key & MASK);
			action.accept(a, b, c, value);
		});
	}

	public void forEachKey(final IntTripleConsumer action) {
		baseMap.forEachKey(key -> {
			final int a = (int) (key >>> 42);
			final int b = (int) (key >>> 21 & MASK);
			final int c = (int) (key & MASK);
			action.accept(a, b, c);
		});
	}

	public void forEachValue(final LongConsumer action) {
		baseMap.forEachValue(action);
	}

	public long reduce(final long identity, final EntryToLongAccumulator accumulator) {
		return baseMap.reduce(identity, (acc, key, value) -> {
			final int a = (int) (key >>> 42);
			final int b = (int) (key >>> 21 & MASK);
			final int c = (int) (key & MASK);
			return accumulator.apply(acc, a, b, c, value);
		});
	}

	public long reduceKeys(final long identity, final KeysToLongAccumulator accumulator) {
		return baseMap.reduceKeys(identity, (acc, key) -> {
			final int a = (int) (key >>> 42);
			final int b = (int) (key >>> 21 & MASK);
			final int c = (int) (key & MASK);
			return accumulator.apply(acc, a, b, c);
		});
	}

	public long reduceValues(final long identity, final LongBinaryOperator accumulator) {
		return baseMap.reduceValues(identity, accumulator);
	}

	public int[][] keys() {
		final int size = baseMap.size();
		final int[][] res = new int[3][size];
		final long[] keys = baseMap.keys();
		for (int i = 0; i < size; i++) {
			final long key = keys[i];
			final int a = (int) (key >>> 42);
			final int b = (int) (key >>> 21 & MASK);
			final int c = (int) (key & MASK);
			res[0][i] = a;
			res[1][i] = b;
			res[2][i] = c;
		}
		return res;
	}

	public long[] values() {
		return baseMap.values();
	}

	public long[][] entries() {
		final int size = baseMap.size();
		final long[][] res = new long[4][size];
		final long[][] entries = baseMap.entries();
		for (int i = 0; i < size; i++) {
			final long key = entries[0][i], value = entries[1][i];
			final int a = (int) (key >>> 42);
			final int b = (int) (key >>> 21 & MASK);
			final int c = (int) (key & MASK);
			res[0][i] = a;
			res[1][i] = b;
			res[2][i] = c;
			res[3][i] = value;
		}
		return res;
	}

	public interface KeysToLongAccumulator {
		long apply(long accumulator, int key1, int key2, int key3);
	}

	public interface EntryToLongAccumulator {
		long apply(long accumulator, int key1, int key2, int key3, long value);
	}

	public interface IntTripleLongConsumer {
		void accept(int a, int b, int c, long value);
	}

	public interface IntTripleConsumer {
		void accept(int a, int b, int c);
	}
}
