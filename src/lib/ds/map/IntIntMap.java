package lib.ds.map;

import static java.lang.Math.*;

import java.util.concurrent.*;
import java.util.function.*;

import lib.util.function.*;

/**
 * int キー・int 値のオープンアドレス法（線形探索）によるハッシュマップ。
 * 削除は backward-shift deletion で行い、clear は世代番号の更新により O(1) で行う。
 * expectedSize はリサイズせずに保持できる要素数の目安であり、要素数が容量の 3/4 に達した状態で新しいキーを挿入すると容量を2倍へ拡張する。
 * put・putIfAbsent・remove は操作前の値を返し、キーが存在しなかった場合は defaultValue を返す。存在の区別には containsKey を使う。
 * その他の更新系メソッドは操作後にキーへ対応する値を返し、computeIfAbsent はキーが存在すれば既存の値を返す。
 * 未存在キーの get は defaultValue を返し、キーを挿入しない。add・increment・decrement は未存在キーを defaultValue に増分を加えた値で挿入し、
 * merge・mergeMin・mergeMax は value で、addOrDefault は absentValue で挿入する。
 * computeIfAbsent の関数が同じマップを構造変更した場合は、関数の適用後にキーを再探索する。
 * 走査はハッシュ配置順に行い、挿入順ではない。走査中にマップを変更しない。
 */
public final class IntIntMap {
	private static final int DEFAULT_EXPECTED_SIZE = 1024;
	private static final int SALT32 = ThreadLocalRandom.current().nextInt();
	private int[] keys, values, stamps;
	private int defaultValue, stamp, size, capacity, resizeThreshold, mask, modCount;

	public IntIntMap() {
		this(DEFAULT_EXPECTED_SIZE, 0);
	}

	public IntIntMap(final int expectedSize) {
		this(expectedSize, 0);
	}

	public IntIntMap(final int expectedSize, final int defaultValue) {
		this.defaultValue = defaultValue;
		capacity = normalizeCapacity(expectedSize);
		stamp = 1;
		resizeThreshold = capacity - (capacity >>> 2);
		mask = capacity - 1;
		keys = new int[capacity];
		values = new int[capacity];
		stamps = new int[capacity];
	}

	/**
	 * 既定値・エントリ・容量を引き継ぎ、内部配列を共有しない独立したコピーを構築する。
	 */
	public IntIntMap(final IntIntMap other) {
		defaultValue = other.defaultValue;
		stamp = other.stamp;
		size = other.size;
		capacity = other.capacity;
		resizeThreshold = other.resizeThreshold;
		mask = other.mask;
		keys = other.keys.clone();
		values = other.values.clone();
		stamps = other.stamps.clone();
	}

	private static int normalizeCapacity(final int expectedSize) {
		final int required = (expectedSize * 4 + 2) / 3;
		if (required <= 16) return 16;
		int cap = required;
		if ((cap & (cap - 1)) == 0) return cap;
		cap |= cap >>> 1;
		cap |= cap >>> 2;
		cap |= cap >>> 4;
		cap |= cap >>> 8;
		cap |= cap >>> 16;
		return cap + 1;
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public int getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(final int defaultValue) {
		this.defaultValue = defaultValue;
	}

	public boolean containsKey(final int key) {
		for (int hash = hash(key); stamps[hash] == stamp; hash = (hash + 1) & mask) {
			if (keys[hash] == key) return true;
		}
		return false;
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

	public int put(final int key, final int value) {
		int hash = hash(key);
		for (; stamps[hash] == stamp; hash = (hash + 1) & mask) {
			if (keys[hash] == key) {
				final int old = values[hash];
				values[hash] = value;
				return old;
			}
		}
		if (size >= resizeThreshold) {
			resize();
			hash = hash(key);
			while (stamps[hash] == stamp) hash = (hash + 1) & mask;
		}
		stamps[hash] = stamp;
		keys[hash] = key;
		size++;
		modCount++;
		values[hash] = value;
		return defaultValue;
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
		modCount++;
		values[hash] = value;
		return defaultValue;
	}

	public int computeIfAbsent(final int key, final IntUnaryOperator f) {
		int hash = hash(key);
		for (; stamps[hash] == stamp; hash = (hash + 1) & mask) {
			if (keys[hash] == key) return values[hash];
		}
		final int expectedModCount = modCount;
		final int value = f.applyAsInt(key);
		if (modCount != expectedModCount) {
			putIfAbsent(key, value);
			return get(key);
		}
		if (size >= resizeThreshold) {
			resize();
			hash = hash(key);
			while (stamps[hash] == stamp) hash = (hash + 1) & mask;
		}
		stamps[hash] = stamp;
		keys[hash] = key;
		size++;
		modCount++;
		return values[hash] = value;
	}

	public int remove(final int key) {
		for (int hash = hash(key); stamps[hash] == stamp; hash = (hash + 1) & mask) {
			if (keys[hash] != key) continue;
			final int old = values[hash];
			int hole = hash;
			for (int next = (hole + 1) & mask; stamps[next] == stamp; next = (next + 1) & mask) {
				final int home = hash(keys[next]);
				if (((next - home) & mask) >= ((next - hole) & mask)) {
					keys[hole] = keys[next];
					values[hole] = values[next];
					hole = next;
				}
			}
			stamps[hole] = 0;
			size--;
			modCount++;
			return old;
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

	public int addOrDefault(final int key, final int delta, final int absentValue) {
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
		modCount++;
		return values[hash] = absentValue;
	}

	public int mergeMin(final int key, final int value) {
		int hash = hash(key);
		for (; stamps[hash] == stamp; hash = (hash + 1) & mask) {
			if (keys[hash] == key) return values[hash] = min(values[hash], value);
		}
		if (size >= resizeThreshold) {
			resize();
			hash = hash(key);
			while (stamps[hash] == stamp) hash = (hash + 1) & mask;
		}
		stamps[hash] = stamp;
		keys[hash] = key;
		size++;
		modCount++;
		return values[hash] = value;
	}

	public int mergeMax(final int key, final int value) {
		int hash = hash(key);
		for (; stamps[hash] == stamp; hash = (hash + 1) & mask) {
			if (keys[hash] == key) return values[hash] = max(values[hash], value);
		}
		if (size >= resizeThreshold) {
			resize();
			hash = hash(key);
			while (stamps[hash] == stamp) hash = (hash + 1) & mask;
		}
		stamps[hash] = stamp;
		keys[hash] = key;
		size++;
		modCount++;
		return values[hash] = value;
	}

	public int merge(final int key, final int value, final IntBinaryOperator f) {
		int hash = hash(key);
		for (; stamps[hash] == stamp; hash = (hash + 1) & mask) {
			if (keys[hash] == key) return values[hash] = f.applyAsInt(values[hash], value);
		}
		if (size >= resizeThreshold) {
			resize();
			hash = hash(key);
			while (stamps[hash] == stamp) hash = (hash + 1) & mask;
		}
		stamps[hash] = stamp;
		keys[hash] = key;
		size++;
		modCount++;
		return values[hash] = value;
	}

	public void clear() {
		size = 0;
		stamp++;
		modCount++;
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

	public void forEach(final IntBinaryConsumer action) {
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
		int h = key ^ SALT32;
		h ^= h >>> 16;
		h *= 0x7feb352d;
		h ^= h >>> 15;
		h *= 0x846ca68b;
		h ^= h >>> 16;
		return h & mask;
	}

	/**
	 * 現在の累積値とエントリを受け取り、次の累積値を返す。
	 */
	public interface EntryToLongAccumulator {
		long apply(long accumulator, int key, int value);
	}
}
