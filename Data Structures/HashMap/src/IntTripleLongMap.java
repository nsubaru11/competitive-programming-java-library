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

	public void forEach(IntTripleLongConsumer action) {
		baseMap.forEach((key, value) -> {
			int a = (int) (key >>> 42);
			int b = (int) ((key >>> 21) & MASK);
			int c = (int) (key & MASK);
			action.accept(a, b, c, value);
		});
	}

	public void forEachKey(IntTripleConsumer action) {
		baseMap.forEachKey(key -> {
			int a = (int) (key >>> 42);
			int b = (int) ((key >>> 21) & MASK);
			int c = (int) (key & MASK);
			action.accept(a, b, c);
		});
	}

	public void forEachValue(LongConsumer action) {
		baseMap.forEachValue(action);
	}

	public int[][] keys() {
		int[][] res = new int[3][baseMap.size()];
		long[] keys = baseMap.keys();
		for (int i = 0; i < keys.length; i++) {
			int a = (int) (keys[i] >>> 42), b = (int) ((keys[i] >>> 21) & MASK), c = (int) (keys[i] & MASK);
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
		long[][] res = new long[4][baseMap.size()];
		long[][] entries = baseMap.entries();
		for (int i = 0; i < baseMap.size(); i++) {
			int a = (int) (entries[0][i] >>> 42), b = (int) ((entries[0][i] >>> 21) & MASK), c = (int) (entries[0][i] & MASK);
			res[0][i] = a;
			res[1][i] = b;
			res[2][i] = c;
			res[3][i] = entries[1][i];
		}
		return res;
	}

	public interface IntTripleLongConsumer {
		void accept(int a, int b, int c, long value);
	}

	public interface IntTripleConsumer {
		void accept(int a, int b, int c);
	}
}
