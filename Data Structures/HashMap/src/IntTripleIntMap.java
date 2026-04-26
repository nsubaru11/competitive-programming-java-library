import java.util.function.*;

@SuppressWarnings("unused")
public final class IntTripleIntMap {
	private static final long MASK = 0x1FFFFFL;
	private final BaseLongIntMap baseMap;

	public IntTripleIntMap(final int initialCapacity) {
		baseMap = new BaseLongIntMap(initialCapacity);
	}

	private static long pack(final int a, final int b, final int c) {
		return (((long) a & MASK) << 42) | (((long) b & MASK) << 21) | (c & MASK);
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
		return addOrDefault(a, b, c, 1, 1);
	}

	public int decrement(final int a, final int b, final int c) {
		return addOrDefault(a, b, c, -1, -1);
	}

	public int add(final int a, final int b, final int c, final int delta) {
		return addOrDefault(a, b, c, delta, delta);
	}

	public int addOrDefault(final int a, final int b, final int c, final int delta, final int defaultValue) {
		return baseMap.addOrDefault(pack(a, b, c), delta, defaultValue);
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

	public void forEach(IntTripleIntConsumer action) {
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

	public void forEachValue(IntConsumer action) {
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

	public int[] values() {
		return baseMap.values();
	}

	public int[][] entries() {
		int[][] res = new int[4][baseMap.size()];
		long[][] entries = baseMap.entries();
		for (int i = 0; i < baseMap.size(); i++) {
			int a = (int) (entries[0][i] >>> 42), b = (int) ((entries[0][i] >>> 21) & MASK), c = (int) (entries[0][i] & MASK);
			res[0][i] = a;
			res[1][i] = b;
			res[2][i] = c;
			res[3][i] = (int) entries[1][i];
		}
		return res;
	}

	public interface IntTripleIntConsumer {
		void accept(int a, int b, int c, int value);
	}

	public interface IntTripleConsumer {
		void accept(int a, int b, int c);
	}

}
