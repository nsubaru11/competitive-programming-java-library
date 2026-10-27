package lib.ds.fenwick;

import java.util.*;
import java.util.function.*;

import lib.ds.*;

/**
 * 競技プログラミング向け Binary Indexed Tree (Fenwick Tree) の long 型特化実装。
 * <p>
 * 0-indexed での操作を提供し、点更新と区間和の計算を O(log N) で行う。
 * また、BIT上の二分探索による lowerBound を O(log N) で提供する。
 */
@SuppressWarnings("unused")
public final class LongBIT implements LongCollection {
	public final int size;
	private final int headBit;
	private final long[] raw, tree;
	private long total;

	/**
	 * サイズ size の BIT を構築する。初期値はすべて 0。
	 *
	 * @param size 要素数
	 */
	public LongBIT(final int size) {
		this.size = size;
		headBit = Integer.highestOneBit(size);
		raw = new long[size];
		tree = new long[size + 1];
	}

	/**
	 * 初期値関数を用いて BIT を O(N) で構築する。
	 *
	 * @param size 要素数
	 * @param init 初期値関数 (index -> value)
	 */
	public LongBIT(final int size, final IntToLongFunction init) {
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
	public long get(final int i) {
		return raw[i];
	}

	/**
	 * インデックス i (0-indexed) の要素を v に更新する。
	 *
	 * @param i インデックス
	 * @param v 更新後の値
	 * @return 更新前の値
	 */
	public long set(final int i, final long v) {
		final long old = raw[i];
		add(i, v - old);
		return old;
	}

	/**
	 * インデックス i (0-indexed) の要素に d を加算する。
	 *
	 * @param i インデックス
	 * @param d 加算する値
	 */
	public long add(final int i, final long d) {
		raw[i] += d;
		total += d;
		for (int cur = i + 1; cur <= size; cur += cur & -cur) tree[cur] += d;
		return raw[i];
	}

	public long multiply(final int i, final long m) {
		return add(i, raw[i] * (m - 1));
	}

	/**
	 * 指定位置を {@code 現在値 * m + d} に更新する。
	 */
	public long affine(final int i, final long m, final long d) {
		return add(i, raw[i] * (m - 1) + d);
	}

	public long apply(final int i, final long x, final LongBinaryOperator f) {
		return add(i, f.applyAsLong(raw[i], x) - raw[i]);
	}

	public void fill(final long v) {
		total = size * v;
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
	public void setAll(final IntToLongFunction init) {
		total = 0;
		tree[0] = 0;
		for (int i = 0; i < size; i++) total += tree[i + 1] = raw[i] = init.applyAsLong(i);
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

	public long[] toArray() {
		return raw.clone();
	}

	public void forEachLong(final LongConsumer action) {
		for (int i = 0; i < size; i++) action.accept(raw[i]);
	}

	public PrimitiveIterator.OfLong iterator() {
		return new PrimitiveIterator.OfLong() {
			private int i = 0;

			public boolean hasNext() {
				return i < size;
			}

			public long nextLong() {
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

	private long prefixSum(int r) {
		long s = 0;
		for (; r > 0; r -= r & -r) s += tree[r];
		return s;
	}
}
