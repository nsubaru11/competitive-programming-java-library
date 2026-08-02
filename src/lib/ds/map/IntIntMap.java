package lib.ds.map;

import static java.lang.Math.*;

import java.util.function.*;

@SuppressWarnings("unused")
public final class IntIntMap {
	private int[] keys, values, stamps;
	private int defaultValue, stamp, size, capacity, resizeThreshold, mask;

	public IntIntMap() {
		this(1024, 0);
	}

	public IntIntMap(final int initialCapacity) {
		this(initialCapacity, 0);
	}

	public IntIntMap(final int initialCapacity, final int defaultValue) {
		this.defaultValue = defaultValue;
		capacity = normalizeCapacity(initialCapacity);
		size = 0;
		stamp = 1;
		resizeThreshold = capacity - (capacity >>> 2);
		stamps = new int[capacity];
		keys = new int[capacity];
		values = new int[capacity];
		mask = capacity - 1;
	}

	private static int normalizeCapacity(final int c) {
		final long required = (long) c * 4 / 3 + 1;
		int cap = max(16, (int) required);
		if ((cap & (cap - 1)) == 0) return cap;
		cap--;
		cap |= cap >>> 1;
		cap |= cap >>> 2;
		cap |= cap >>> 4;
		cap |= cap >>> 8;
		cap |= cap >>> 16;
		return cap + 1;
	}

	public int getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(final int defaultValue) {
		this.defaultValue = defaultValue;
	}

	public int get(final int key) {
		return getOrDefault(key, defaultValue);
	}

	public int getOrDefault(final int key, final int defaultValue) {
		for (int hash = hash(key); stamps[hash] == stamp; hash = (hash + 1) & mask) {
			if (keys[hash] == key) return values[hash];
		}
		return defaultValue;
	}

	public int increment(final int key) {
		return addOrDefault(key, 1, defaultValue + 1);
	}

	public int decrement(final int key) {
		return addOrDefault(key, -1, defaultValue - 1);
	}

	public int add(final int key, final int delta) {
		return addOrDefault(key, delta, defaultValue + delta);
	}

	public int addOrDefault(final int key, final int delta, final int defaultValue) {
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
		return values[hash] = defaultValue;
	}

	public int put(final int key, final int value) {
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

	public boolean remove(final int key) {
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

	public boolean containsKey(final int key) {
		for (int hash = hash(key); stamps[hash] == stamp; hash = (hash + 1) & mask) {
			if (keys[hash] == key) return true;
		}
		return false;
	}

	public int merge(final int key, final int value, final IntBinaryOperator op) {
		int hash = hash(key);
		for (; stamps[hash] == stamp; hash = (hash + 1) & mask) {
			if (keys[hash] == key) return values[hash] = op.applyAsInt(values[hash], value);
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

	public int putIfAbsent(final int key, final int value) {
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

	public void forEach(final IntIntConsumer action) {
		for (int i = 0; i < capacity; i++) {
			if (stamps[i] != stamp) continue;
			action.accept(keys[i], values[i]);
		}
	}

	public void forEachKey(final IntConsumer action) {
		for (int i = 0; i < capacity; i++) {
			if (stamps[i] != stamp) continue;
			action.accept(keys[i]);
		}
	}

	public void forEachValue(final IntConsumer action) {
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

	public int[] keys() {
		final int[] res = new int[size];
		for (int i = 0, idx = 0; i < capacity; i++) {
			if (stamps[i] != stamp) continue;
			res[idx++] = keys[i];
		}
		return res;
	}

	public int[] values() {
		final int[] res = new int[size];
		for (int i = 0, idx = 0; i < capacity; i++) {
			if (stamps[i] != stamp) continue;
			res[idx++] = values[i];
		}
		return res;
	}

	public int[][] entries() {
		final int[][] res = new int[2][size];
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
		final int[] oldKeys = keys, oldValues = values, oldStamps = stamps;
		capacity <<= 1;
		resizeThreshold = capacity - (capacity >>> 2);
		keys = new int[capacity];
		values = new int[capacity];
		stamps = new int[capacity];
		mask = capacity - 1;
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
		int h = key;
		h ^= h >>> 16;
		h *= 0x7feb352d;
		h ^= h >>> 15;
		h *= 0x846ca68b;
		h ^= h >>> 16;
		return h & mask;
	}

	public interface EntryToLongAccumulator {
		long apply(long accumulator, int key, int value);
	}

	public interface IntIntConsumer {
		void accept(int key, int value);
	}
}
