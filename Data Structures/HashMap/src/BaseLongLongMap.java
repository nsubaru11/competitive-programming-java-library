import static java.lang.Math.*;

import java.util.*;
import java.util.function.*;

@SuppressWarnings("unused")
public final class BaseLongLongMap {
	private long[] keys, values;
	private int[] stamps;
	private int stamp, size, capacity, occupied, resizeThreshold, mask;

	public BaseLongLongMap(final int initialCapacity) {
		capacity = normalizeCapacity(initialCapacity);
		size = 0;
		occupied = 0;
		stamp = 1;
		resizeThreshold = capacity - (capacity >>> 2);
		stamps = new int[capacity];
		keys = new long[capacity];
		values = new long[capacity];
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

	public long get(final long key) {
		for (int hash = hash(key), s = stamps[hash]; s == stamp || s == -stamp; hash = (hash + 1) & mask, s = stamps[hash]) {
			if (s < 0 || keys[hash] != key) continue;
			return values[hash];
		}
		throw new NoSuchElementException();
	}

	public long getOrDefault(final long key, final long defaultValue) {
		for (int hash = hash(key), s = stamps[hash]; s == stamp || s == -stamp; hash = (hash + 1) & mask, s = stamps[hash]) {
			if (s < 0 || keys[hash] != key) continue;
			return values[hash];
		}
		return defaultValue;
	}

	public long increment(final long key) {
		return addOrDefault(key, 1, 1);
	}

	public long decrement(final long key) {
		return addOrDefault(key, -1, -1);
	}

	public long add(final long key, final long delta) {
		return addOrDefault(key, delta, delta);
	}

	public long addOrDefault(final long key, final long delta, final long defaultValue) {
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

	public long put(final long key, final long value) {
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

	public boolean remove(final long key) {
		for (int hash = hash(key), s = stamps[hash]; s == stamp || s == -stamp; hash = (hash + 1) & mask, s = stamps[hash]) {
			if (s < 0 || keys[hash] != key) continue;
			stamps[hash] = -stamp;
			size--;
			return true;
		}
		return false;
	}

	public boolean containsKey(final long key) {
		for (int hash = hash(key), s = stamps[hash]; s == stamp || s == -stamp; hash = (hash + 1) & mask, s = stamps[hash]) {
			if (s < 0 || keys[hash] != key) continue;
			return true;
		}
		return false;
	}

	public long merge(final long key, final long value, final LongBinaryOperator op) {
		if (occupied >= resizeThreshold) resize();
		int pos = -1;
		int hash = hash(key);
		for (int s = stamps[hash]; s == stamp || s == -stamp; hash = (hash + 1) & mask, s = stamps[hash]) {
			if (s == -stamp) {
				if (pos == -1) pos = hash;
				continue;
			} else if (keys[hash] != key) continue;
			return values[hash] = op.applyAsLong(values[hash], value);
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

	public long putIfAbsent(final long key, final long value) {
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
		final long[] oldKeys = keys, oldValues = values;
		final int[] oldStamps = stamps;
		capacity <<= 1;
		resizeThreshold = capacity - (capacity >>> 2);
		keys = new long[capacity];
		values = new long[capacity];
		stamps = new int[capacity];
		occupied = 0;
		mask = capacity - 1;
		for (int i = 0; i < oldCapacity; i++) {
			if (oldStamps[i] != stamp) continue;
			final long key = oldKeys[i];
			int hash = hash(key);
			while (stamps[hash] == stamp) hash = (hash + 1) & mask;
			stamps[hash] = stamp;
			keys[hash] = key;
			values[hash] = oldValues[i];
			occupied++;
		}
	}

	private int hash(final long key) {
		long h = key;
		h ^= h >>> 33;
		h *= 0xff51afd7ed558ccdL;
		h ^= h >>> 33;
		return (int) h & mask;
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

	public void forEach(LongLongConsumer action) {
		for (int i = 0; i < capacity; i++) {
			if (stamps[i] != stamp) continue;
			action.accept(keys[i], values[i]);
		}
	}

	public void forEachKey(LongConsumer action) {
		for (int i = 0; i < capacity; i++) {
			if (stamps[i] != stamp) continue;
			action.accept(keys[i]);
		}
	}

	public void forEachValue(LongConsumer action) {
		for (int i = 0; i < capacity; i++) {
			if (stamps[i] != stamp) continue;
			action.accept(values[i]);
		}
	}

	public long[] keys() {
		long[] res = new long[size];
		for (int i = 0, idx = 0; i < capacity; i++) {
			if (stamps[i] != stamp) continue;
			res[idx++] = keys[i];
		}
		return res;
	}

	public long[] values() {
		long[] res = new long[size];
		for (int i = 0, idx = 0; i < capacity; i++) {
			if (stamps[i] != stamp) continue;
			res[idx++] = values[i];
		}
		return res;
	}

	public long[][] entries() {
		long[][] res = new long[2][size];
		for (int i = 0, idx = 0; i < capacity; i++) {
			if (stamps[i] != stamp) continue;
			res[0][idx] = keys[i];
			res[1][idx] = values[i];
			idx++;
		}
		return res;
	}

	public interface LongLongConsumer {
		void accept(long key, long value);
	}
}
