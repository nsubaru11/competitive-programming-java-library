import java.util.function.*;

@SuppressWarnings("unused")
public final class IntPairLongMap {
	private static final long MASK = 0xFFFFFFFFL;
	private final BaseLongLongMap baseMap;

	public IntPairLongMap(final int initialCapacity) {
		baseMap = new BaseLongLongMap(initialCapacity);
	}

	private static long pack(final int a, final int b) {
		return ((long) a << 32) | (b & MASK);
	}

	public long put(final int a, final int b, final long value) {
		return baseMap.put(pack(a, b), value);
	}

	public long get(final int a, final int b) {
		return baseMap.get(pack(a, b));
	}

	public long getOrDefault(final int a, final int b, final long defaultValue) {
		return baseMap.getOrDefault(pack(a, b), defaultValue);
	}

	public long increment(final int a, final int b) {
		return addOrDefault(a, b, 1, 1);
	}

	public long decrement(final int a, final int b) {
		return addOrDefault(a, b, -1, -1);
	}

	public long add(final int a, final int b, final long delta) {
		return addOrDefault(a, b, delta, delta);
	}

	public long addOrDefault(final int a, final int b, final long delta, final long defaultValue) {
		return baseMap.addOrDefault(pack(a, b), delta, defaultValue);
	}

	public boolean remove(final int a, final int b) {
		return baseMap.remove(pack(a, b));
	}

	public boolean containsKey(final int a, final int b) {
		return baseMap.containsKey(pack(a, b));
	}

	public long merge(final int a, final int b, final long value, final LongBinaryOperator op) {
		return baseMap.merge(pack(a, b), value, op);
	}

	public long putIfAbsent(final int a, final int b, final long value) {
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

	public void forEach(IntPairLongConsumer action) {
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

	public void forEachValue(LongConsumer action) {
		baseMap.forEachValue(action);
	}

	public long[][] keys() {
		long[][] res = new long[2][baseMap.size()];
		long[] keys = baseMap.keys();
		for (int i = 0; i < keys.length; i++) {
			int a = (int) (keys[i] >>> 32), b = (int) keys[i];
			res[0][i] = a;
			res[1][i] = b;
		}
		return res;
	}

	public long[] values() {
		return baseMap.values();
	}

	public long[][] entries() {
		long[][] res = new long[3][baseMap.size()];
		long[][] entries = baseMap.entries();
		for (int i = 0; i < baseMap.size(); i++) {
			int a = (int) (entries[0][i] >>> 32), b = (int) entries[0][i];
			res[0][i] = a;
			res[1][i] = b;
			res[2][i] = entries[1][i];
		}
		return res;
	}

	public interface IntPairLongConsumer {
		void accept(int a, int b, long value);
	}

	public interface IntPairConsumer {
		void accept(int a, int b);
	}

}
