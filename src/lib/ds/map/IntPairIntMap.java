package lib.ds.map;

import java.util.function.*;

import lib.util.function.*;

/**
 * 2つの {@code int} をキー、{@code int} を値として保持する高速マップです。
 * キーの組を1つの {@code long} に可逆圧縮し、{@link LongIntMap} に処理を委譲します。
 * 未存在キーの取得では設定済みの既定値を返します。
 */
@SuppressWarnings("unused")
public final class IntPairIntMap {
	private static final long KEY_MASK = (1L << 32) - 1;
	private final LongIntMap baseMap;

	public IntPairIntMap() {
		baseMap = new LongIntMap();
	}

	public IntPairIntMap(final int expectedSize) {
		baseMap = new LongIntMap(expectedSize);
	}

	public IntPairIntMap(final int expectedSize, final int defaultValue) {
		baseMap = new LongIntMap(expectedSize, defaultValue);
	}

	private static long pack(final int a, final int b) {
		return ((long) a << 32) | (b & KEY_MASK);
	}

	public int getDefaultValue() {
		return baseMap.getDefaultValue();
	}

	public void setDefaultValue(final int defaultValue) {
		baseMap.setDefaultValue(defaultValue);
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
		return baseMap.increment(pack(a, b));
	}

	public int decrement(final int a, final int b) {
		return baseMap.decrement(pack(a, b));
	}

	public int add(final int a, final int b, final int delta) {
		return baseMap.add(pack(a, b), delta);
	}

	public int addOrDefault(final int a, final int b, final int delta, final int absentValue) {
		return baseMap.addOrDefault(pack(a, b), delta, absentValue);
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

	public int computeIfAbsent(final int a, final int b, final IntBinaryOperator op) {
		return baseMap.computeIfAbsent(pack(a, b), _ -> op.applyAsInt(a, b));
	}

	public int mergeMin(final int a, final int b, final int value) {
		return baseMap.mergeMin(pack(a, b), value);
	}

	public int mergeMax(final int a, final int b, final int value) {
		return baseMap.mergeMax(pack(a, b), value);
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

	public void forEach(final IntTernaryConsumer action) {
		baseMap.forEach((key, value) -> action.accept((int) (key >>> 32), (int) key, value));
	}

	public void forEachKey(final IntBinaryConsumer action) {
		baseMap.forEachKey(key -> action.accept((int) (key >>> 32), (int) key));
	}

	public void forEachValue(final IntConsumer action) {
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

	public int[] values() {
		return baseMap.values();
	}

	public int[][] entries() {
		final int[][] res = new int[3][baseMap.size()];
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

	/**
	 * 現在の累積値と2成分のキーを受け取り、次の累積値を返します。
	 */
	public interface KeysToLongAccumulator {
		/**
		 * @param accumulator 現在の累積値
		 * @param key1        キー1
		 * @param key2        キー2
		 * @return 次の累積値
		 */
		long apply(long accumulator, int key1, int key2);
	}

	/**
	 * 現在の累積値とエントリを受け取り、次の累積値を返します。
	 */
	public interface EntryToLongAccumulator {
		/**
		 * @param accumulator 現在の累積値
		 * @param key1        キー1
		 * @param key2        キー2
		 * @param value       値
		 * @return 次の累積値
		 */
		long apply(long accumulator, int key1, int key2, int value);
	}
}
