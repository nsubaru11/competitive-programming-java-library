import static java.lang.Math.*;

import java.util.*;
import java.util.function.*;

@SuppressWarnings("unused")
public final class BaseIntIntMap {
	private int[] keys, values, stamps;
	private int stamp, size, capacity, occupied, resizeThreshold, mask;

	public BaseIntIntMap(final int initialCapacity) {
		capacity = normalizeCapacity(initialCapacity);
		size = 0;
		occupied = 0;
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

	public int get(final int key) {
		for (int hash = hash(key), s = stamps[hash]; s == stamp || s == -stamp; hash = (hash + 1) & mask, s = stamps[hash]) {
			if (s < 0 || keys[hash] != key) continue;
			return values[hash];
		}
		throw new NoSuchElementException();
	}

	public int getOrDefault(final int key, final int defaultValue) {
		for (int hash = hash(key), s = stamps[hash]; s == stamp || s == -stamp; hash = (hash + 1) & mask, s = stamps[hash]) {
			if (s < 0 || keys[hash] != key) continue;
			return values[hash];
		}
		return defaultValue;
	}

	public int increment(final int key) {
		return addOrDefault(key, 1, 1);
	}

	public int decrement(final int key) {
		return addOrDefault(key, -1, -1);
	}

	public int add(final int key, final int delta) {
		return addOrDefault(key, delta, delta);
	}

	public int addOrDefault(final int key, final int delta, final int defaultValue) {
		if (occupied >= resizeThreshold) resize();
		int pos = -1;
		int hash = hash(key);
		for (int s = stamps[hash]; s == stamp || s == -stamp; hash = (hash + 1) & mask, s = stamps[hash]) {
			if (s == -stamp) {
				if (pos == -1) pos = hash;
				continue;
			} else if (keys[hash] != key) continue;
			return values[hash] += delta;
		}
		if (pos == -1) {
			pos = hash;
			occupied++;
		}
		stamps[pos] = stamp;
		keys[pos] = key;
		size++;
		return values[pos] = defaultValue;
	}

	public int put(final int key, final int value) {
		if (occupied >= resizeThreshold) resize();
		int pos = -1;
		int hash = hash(key);
		for (int s = stamps[hash]; s == stamp || s == -stamp; hash = (hash + 1) & mask, s = stamps[hash]) {
			if (s == -stamp) {
				if (pos == -1) pos = hash;
				continue;
			} else if (keys[hash] != key) continue;
			return values[hash] = value;
		}
		if (pos == -1) {
			pos = hash;
			occupied++;
		}
		stamps[pos] = stamp;
		keys[pos] = key;
		size++;
		return values[pos] = value;
	}

	public boolean remove(final int key) {
		for (int hash = hash(key), s = stamps[hash]; s == stamp || s == -stamp; hash = (hash + 1) & mask, s = stamps[hash]) {
			if (s < 0 || keys[hash] != key) continue;
			stamps[hash] = -stamp;
			size--;
			return true;
		}
		return false;
	}

	public boolean containsKey(final int key) {
		for (int hash = hash(key), s = stamps[hash]; s == stamp || s == -stamp; hash = (hash + 1) & mask, s = stamps[hash]) {
			if (s < 0 || keys[hash] != key) continue;
			return true;
		}
		return false;
	}

	public int merge(final int key, final int value, final IntBinaryOperator op) {
		if (occupied >= resizeThreshold) resize();
		int pos = -1;
		int hash = hash(key);
		for (int s = stamps[hash]; s == stamp || s == -stamp; hash = (hash + 1) & mask, s = stamps[hash]) {
			if (s == -stamp) {
				if (pos == -1) pos = hash;
				continue;
			} else if (keys[hash] != key) continue;
			return values[hash] = op.applyAsInt(values[hash], value);
		}
		if (pos == -1) {
			pos = hash;
			occupied++;
		}
		stamps[pos] = stamp;
		keys[pos] = key;
		size++;
		return values[pos] = value;
	}

	public int putIfAbsent(final int key, final int value) {
		if (occupied >= resizeThreshold) resize();
		int pos = -1;
		int hash = hash(key);
		for (int s = stamps[hash]; s == stamp || s == -stamp; hash = (hash + 1) & mask, s = stamps[hash]) {
			if (s == -stamp) {
				if (pos == -1) pos = hash;
				continue;
			} else if (keys[hash] != key) continue;
			return values[hash];
		}
		if (pos == -1) {
			pos = hash;
			occupied++;
		}
		stamps[pos] = stamp;
		keys[pos] = key;
		size++;
		return values[pos] = value;
	}

	private void resize() {
		final int oldCapacity = capacity;
		final int[] oldKeys = keys, oldValues = values, oldStamps = stamps;
		capacity <<= 1;
		resizeThreshold = capacity - (capacity >>> 2);
		keys = new int[capacity];
		values = new int[capacity];
		stamps = new int[capacity];
		occupied = 0;
		mask = capacity - 1;
		for (int i = 0; i < oldCapacity; i++) {
			if (oldStamps[i] != stamp) continue;
			final int key = oldKeys[i];
			int hash = hash(key);
			while (stamps[hash] == stamp) hash = (hash + 1) & mask;
			stamps[hash] = stamp;
			keys[hash] = key;
			values[hash] = oldValues[i];
			occupied++;
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

	public void clear() {
		size = 0;
		occupied = 0;
		stamp++;
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public void forEach(IntIntConsumer action) {
		for (int i = 0; i < capacity; i++) {
			if (stamps[i] != stamp) continue;
			action.accept(keys[i], values[i]);
		}
	}

	public void forEachKey(IntConsumer action) {
		for (int i = 0; i < capacity; i++) {
			if (stamps[i] != stamp) continue;
			action.accept(keys[i]);
		}
	}

	public void forEachValue(IntConsumer action) {
		for (int i = 0; i < capacity; i++) {
			if (stamps[i] != stamp) continue;
			action.accept(values[i]);
		}
	}

	public int[] keys() {
		int[] res = new int[size];
		for (int i = 0, idx = 0; i < capacity; i++) {
			if (stamps[i] != stamp) continue;
			res[idx++] = keys[i];
		}
		return res;
	}

	public int[] values() {
		int[] res = new int[size];
		for (int i = 0, idx = 0; i < capacity; i++) {
			if (stamps[i] != stamp) continue;
			res[idx++] = values[i];
		}
		return res;
	}

	public int[][] entries() {
		int[][] res = new int[2][size];
		for (int i = 0, idx = 0; i < capacity; i++) {
			if (stamps[i] != stamp) continue;
			res[0][idx] = keys[i];
			res[1][idx] = values[i];
			idx++;
		}
		return res;
	}

	public interface IntIntConsumer {
		void accept(int key, int value);
	}
}
