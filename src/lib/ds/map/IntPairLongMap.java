package lib.ds.map;

import java.util.function.*;

import lib.util.function.*;

/**
 * int の組 (a, b) をキー、long を値とするハッシュマップ。
 * キーを a を上位32bit、b を下位32bitとする long へ可逆にパックし、LongLongMap へ委譲する。
 * 戻り値・既定値・走査順の仕様は LongLongMap と同じである。
 */
public final class IntPairLongMap {
	private static final long KEY_MASK = (1L << 32) - 1;
	private final LongLongMap baseMap;

	public IntPairLongMap() {
		baseMap = new LongLongMap();
	}

	public IntPairLongMap(final int expectedSize) {
		baseMap = new LongLongMap(expectedSize);
	}

	public IntPairLongMap(final int expectedSize, final long defaultValue) {
		baseMap = new LongLongMap(expectedSize, defaultValue);
	}

	/**
	 * 既定値・エントリ・容量を引き継ぎ、内部配列を共有しない独立したコピーを構築する。
	 */
	public IntPairLongMap(final IntPairLongMap other) {
		baseMap = new LongLongMap(other.baseMap);
	}

	private static long pack(final int a, final int b) {
		return ((long) a << 32) | (b & KEY_MASK);
	}

	public int size() {
		return baseMap.size();
	}

	public boolean isEmpty() {
		return baseMap.isEmpty();
	}

	public long getDefaultValue() {
		return baseMap.getDefaultValue();
	}

	public void setDefaultValue(final long defaultValue) {
		baseMap.setDefaultValue(defaultValue);
	}

	public boolean containsKey(final int a, final int b) {
		return baseMap.containsKey(pack(a, b));
	}

	public long get(final int a, final int b) {
		return baseMap.get(pack(a, b));
	}

	public long getOrDefault(final int a, final int b, final long defaultValue) {
		return baseMap.getOrDefault(pack(a, b), defaultValue);
	}

	public long put(final int a, final int b, final long value) {
		return baseMap.put(pack(a, b), value);
	}

	public long putIfAbsent(final int a, final int b, final long value) {
		return baseMap.putIfAbsent(pack(a, b), value);
	}

	public long computeIfAbsent(final int a, final int b, final IntBinaryToLongFunction f) {
		return baseMap.computeIfAbsent(pack(a, b), _ -> f.applyAsLong(a, b));
	}

	public long remove(final int a, final int b) {
		return baseMap.remove(pack(a, b));
	}

	public long increment(final int a, final int b) {
		return baseMap.increment(pack(a, b));
	}

	public long decrement(final int a, final int b) {
		return baseMap.decrement(pack(a, b));
	}

	public long add(final int a, final int b, final long delta) {
		return baseMap.add(pack(a, b), delta);
	}

	public long addOrDefault(final int a, final int b, final long delta, final long absentValue) {
		return baseMap.addOrDefault(pack(a, b), delta, absentValue);
	}

	public long mergeMin(final int a, final int b, final long value) {
		return baseMap.mergeMin(pack(a, b), value);
	}

	public long mergeMax(final int a, final int b, final long value) {
		return baseMap.mergeMax(pack(a, b), value);
	}

	public long merge(final int a, final int b, final long value, final LongBinaryOperator f) {
		return baseMap.merge(pack(a, b), value, f);
	}

	public void clear() {
		baseMap.clear();
	}

	public int[][] keys() {
		final int[][] res = new int[2][baseMap.size()];
		final int stamp = baseMap.currentStamp();
		for (int i = 0, idx = 0; i < baseMap.keys.length; i++) {
			if (baseMap.stamps[i] != stamp) continue;
			final long key = baseMap.keys[i];
			res[0][idx] = (int) (key >>> 32);
			res[1][idx] = (int) key;
			idx++;
		}
		return res;
	}

	public long[] values() {
		return baseMap.values();
	}

	public long[][] entries() {
		final long[][] res = new long[3][baseMap.size()];
		final int stamp = baseMap.currentStamp();
		for (int i = 0, idx = 0; i < baseMap.keys.length; i++) {
			if (baseMap.stamps[i] != stamp) continue;
			final long key = baseMap.keys[i];
			res[0][idx] = (int) (key >>> 32);
			res[1][idx] = (int) key;
			res[2][idx] = baseMap.values[i];
			idx++;
		}
		return res;
	}

	public void forEach(final IntBinaryLongConsumer action) {
		baseMap.forEach((key, value) -> action.accept((int) (key >>> 32), (int) key, value));
	}

	public void forEachKey(final IntBinaryConsumer action) {
		baseMap.forEachKey(key -> action.accept((int) (key >>> 32), (int) key));
	}

	public void forEachValue(final LongConsumer action) {
		baseMap.forEachValue(action);
	}

	public long reduce(final long identity, final EntryToLongAccumulator accumulator) {
		return baseMap.reduce(identity, (acc, key, value) -> accumulator.apply(acc, (int) (key >>> 32), (int) key, value));
	}

	public long reduceKeys(final long identity, final KeysToLongAccumulator accumulator) {
		return baseMap.reduceKeys(identity, (acc, key) -> accumulator.apply(acc, (int) (key >>> 32), (int) key));
	}

	public long reduceValues(final long identity, final LongBinaryOperator accumulator) {
		return baseMap.reduceValues(identity, accumulator);
	}

	/**
	 * 現在の累積値と2成分のキーを受け取り、次の累積値を返す。
	 */
	public interface KeysToLongAccumulator {
		long apply(long accumulator, int key1, int key2);
	}

	/**
	 * 現在の累積値とエントリを受け取り、次の累積値を返す。
	 */
	public interface EntryToLongAccumulator {
		long apply(long accumulator, int key1, int key2, long value);
	}
}
