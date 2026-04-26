import java.util.function.*;

@SuppressWarnings("unused")
public final class IntPairIntMap {
	private static final long MASK = 0xFFFFFFFFL;
	private final BaseLongIntMap baseMap;

	public IntPairIntMap(final int initialCapacity) {
		baseMap = new BaseLongIntMap(initialCapacity);
	}

	private static long pack(final int a, final int b) {
		return ((long) a << 32) | (b & MASK);
	}

	public int put(final int a, final int b, final int value) {
		return baseMap.put(pack(a, b), value);
	}

	public int get(final int a, final int b) {
		return baseMap.get(pack(a, b));
	}

	public int getOrDefault(final int a, final int b, final int defaultValue) {
		return baseMap.getOrDefault(pack(a, b), defaultValue);
	}

	public int increment(final int a, final int b) {
		return addOrDefault(a, b, 1, 1);
	}

	public int decrement(final int a, final int b) {
		return addOrDefault(a, b, -1, -1);
	}

	public int add(final int a, final int b, final int delta) {
		return addOrDefault(a, b, delta, delta);
	}

	public int addOrDefault(final int a, final int b, final int delta, final int defaultValue) {
		return baseMap.addOrDefault(pack(a, b), delta, defaultValue);
	}

	public boolean remove(final int a, final int b) {
		return baseMap.remove(pack(a, b));
	}

	public boolean containsKey(final int a, final int b) {
		return baseMap.containsKey(pack(a, b));
	}

	public int merge(final int a, final int b, final int value, final IntBinaryOperator op) {
		return baseMap.merge(pack(a, b), value, op);
	}

	public int putIfAbsent(final int a, final int b, final int value) {
		return baseMap.putIfAbsent(pack(a, b), value);
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

	public void forEach(IntPairIntConsumer action) {
		baseMap.forEach((key, value) -> {
			int a = (int) (key >>> 32);
			int b = (int) key;
			action.accept(a, b, value);
		});
	}

	public void forEachKey(IntPairConsumer action) {
		baseMap.forEachKey(key -> {
			int a = (int) (key >>> 32);
			int b = (int) key;
			action.accept(a, b);
		});
	}

	public void forEachValue(IntConsumer action) {
		baseMap.forEachValue(action);
	}

	public int[][] keys() {
		int[][] res = new int[2][baseMap.size()];
		long[] keys = baseMap.keys();
		for (int i = 0; i < keys.length; i++) {
			int a = (int) (keys[i] >>> 32), b = (int) keys[i];
			res[0][i] = a;
			res[1][i] = b;
		}
		return res;
	}

	public int[] values() {
		return baseMap.values();
	}

	public int[][] entries() {
		int[][] res = new int[3][baseMap.size()];
		long[][] entries = baseMap.entries();
		for (int i = 0; i < baseMap.size(); i++) {
			int a = (int) (entries[0][i] >>> 32), b = (int) entries[0][i];
			res[0][i] = a;
			res[1][i] = b;
			res[2][i] = (int) entries[1][i];
		}
		return res;
	}

	public interface IntPairIntConsumer {
		void accept(int a, int b, int value);
	}

	public interface IntPairConsumer {
		void accept(int a, int b);
	}
}
