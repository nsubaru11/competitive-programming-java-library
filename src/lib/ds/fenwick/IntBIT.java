package lib.ds.fenwick;

import java.util.*;
import java.util.function.*;

import lib.ds.*;

/**
 * 競技プログラミング向け Binary Indexed Tree (Fenwick Tree) の int 型特化実装。
 * 各操作後の要素は int の範囲内とし、更新差分と和は long で扱う。
 * 0-indexed の点更新・区間和・累積和境界探索を O(log N) で行う。
 */
@SuppressWarnings("unused")
public final class IntBIT implements IntCollection {
	public final int size;
	private final int headBit;
	private final int[] raw;
	private final long[] tree;
	private long total;

	/**
	 * サイズ size の BIT を構築する。初期値はすべて 0。
	 *
	 * @param size 要素数
	 */
	public IntBIT(final int size) {
		this.size = size;
		headBit = Integer.highestOneBit(size);
		raw = new int[size];
		tree = new long[size + 1];
	}

	/**
	 * 初期値関数を用いて BIT を O(N) で構築する。
	 *
	 * @param size 要素数
	 * @param init 初期値関数 (index -> value)
	 */
	public IntBIT(final int size, final IntUnaryOperator init) {
		this(size);
		setAll(init);
	}

	public int size() {
		return size;
	}

	/**
	 * インデックス i (0-indexed) の現在の値を取得する。
	 *
	 * @param i インデックス
	 * @return 現在の値
	 */
	public int get(final int i) {
		return raw[i];
	}

	/**
	 * インデックス i (0-indexed) の要素を v に更新する。
	 *
	 * @param i インデックス
	 * @param v 更新後の値
	 * @return 更新前の値
	 */
	public int set(final int i, final int v) {
		final int old = raw[i];
		add(i, (long) v - old);
		return old;
	}

	/**
	 * インデックス i (0-indexed) の要素に d を加算する。
	 *
	 * @param i インデックス
	 * @param d 加算する値
	 */
	public int add(final int i, final int d) {
		return add(i, (long) d);
	}

	public int multiply(final int i, final int m) {
		return add(i, raw[i] * (m - 1L));
	}

	/**
	 * 指定位置を {@code 現在値 * m + d} に更新する。
	 */
	public int affine(final int i, final int m, final int d) {
		return add(i, raw[i] * (m - 1L) + d);
	}

	public int apply(final int i, final int x, final IntBinaryOperator f) {
		return add(i, (long) f.applyAsInt(raw[i], x) - raw[i]);
	}

	public void fill(final int v) {
		total = (long) size * v;
		tree[0] = 0;
		for (int i = 0; i < size; i++) tree[i + 1] = raw[i] = v;
		for (int i = 1; i <= size; i++) {
			final int j = i + (i & -i);
			if (j <= size) tree[j] += tree[i];
		}
	}

	/**
	 * BIT の全要素を再構築する。
	 *
	 * @param init 初期値関数
	 */
	public void setAll(final IntUnaryOperator init) {
		total = 0;
		tree[0] = 0;
		for (int i = 0; i < size; i++) total += tree[i + 1] = raw[i] = init.applyAsInt(i);
		for (int i = 1; i <= size; i++) {
			final int j = i + (i & -i);
			if (j <= size) tree[j] += tree[i];
		}
	}

	/**
	 * 閉区間 [0, r] の和を計算する。r = -1 は空区間として扱う。
	 *
	 * @param r 右端の境界 (includes)
	 * @return [0, r] の和
	 */
	public long sum(final int r) {
		return prefixSum(Objects.checkIndex(r + 1, size + 1));
	}

	/**
	 * 閉区間 [l, r] の和を計算する。l > r は空区間として扱う。
	 *
	 * @param l 左端の境界 (includes)
	 * @param r 右端の境界 (includes)
	 * @return [l, r] の和
	 */
	public long sum(final int l, final int r) {
		if (l > r) return 0;
		Objects.checkFromToIndex(l, r + 1, size);
		return prefixSum(r + 1) - prefixSum(l);
	}

	/**
	 * 全要素の和を O(1) で返す。
	 *
	 * @return 全要素の和
	 */
	public long sumAll() {
		return total;
	}

	/**
	 * 累積和が w 以上となる最小のインデックス i (0-indexed) を返す。
	 * 全ての要素が非負であることを前提とする。
	 * 存在しない場合は size を返す。
	 *
	 * @param w ターゲットとなる累積和
	 * @return 累積和が w 以上となる最小のインデックス
	 */
	public int lowerBound(long w) {
		if (w <= 0) return 0;
		int i = 0;
		for (int k = headBit; k > 0; k >>= 1) {
			if (i + k <= size && tree[i + k] < w) {
				w -= tree[i + k];
				i += k;
			}
		}
		return i;
	}

	/**
	 * 累積和が w より大きくなる最小のインデックス i (0-indexed) を返す。
	 * 全ての要素が非負であることを前提とする。
	 * 存在しない場合は size を返す。
	 *
	 * @param w ターゲットとなる累積和の上限
	 * @return 累積和が w より大きくなる最小のインデックス
	 */
	public int upperBound(long w) {
		if (w < 0) return 0;
		int i = 0;
		for (int k = headBit; k > 0; k >>= 1) {
			if (i + k <= size && tree[i + k] <= w) {
				w -= tree[i + k];
				i += k;
			}
		}
		return i;
	}

	public int[] toArray() {
		return raw.clone();
	}

	public void forEachInt(final IntConsumer action) {
		for (int i = 0; i < size; i++) action.accept(raw[i]);
	}

	public PrimitiveIterator.OfInt iterator() {
		return new PrimitiveIterator.OfInt() {
			private int i = 0;

			public boolean hasNext() {
				return i < size;
			}

			public int nextInt() {
				if (!hasNext()) throw new NoSuchElementException();
				return raw[i++];
			}
		};
	}

	public String toString() {
		if (isEmpty()) return "";
		final StringBuilder sb = new StringBuilder();
		sb.append(raw[0]);
		for (int i = 1; i < size; i++) sb.append(' ').append(raw[i]);
		return sb.toString();
	}

	private int add(final int i, final long d) {
		raw[i] += d;
		total += d;
		for (int cur = i + 1; cur <= size; cur += cur & -cur) tree[cur] += d;
		return raw[i];
	}

	private long prefixSum(int r) {
		long s = 0;
		for (; r > 0; r -= r & -r) s += tree[r];
		return s;
	}
}
