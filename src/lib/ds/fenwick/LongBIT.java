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
	public final int n;
	private final int headBit;
	private final long[] tree, raw;

	/**
	 * サイズ n の BIT を構築する。初期値はすべて 0。
	 *
	 * @param n 要素数
	 */
	public LongBIT(final int n) {
		this.n = n;
		headBit = Integer.highestOneBit(n);
		tree = new long[n + 1];
		raw = new long[n];
	}

	/**
	 * 初期値関数を用いて BIT を O(N) で構築する。
	 *
	 * @param n    要素数
	 * @param init 初期値関数 (index -> value)
	 */
	public LongBIT(final int n, final IntToLongFunction init) {
		this(n);
		setAll(init);
	}

	public void fill(final long val) {
		tree[0] = 0;
		for (int i = 0; i < n; i++) raw[i] = tree[i + 1] = val;
		for (int i = 1; i <= n; i++) {
			int j = i + (i & -i);
			if (j <= n) tree[j] += tree[i];
		}
	}

	/**
	 * BIT の全要素を再構築する。
	 *
	 * @param init 初期値関数
	 */
	public void setAll(final IntToLongFunction init) {
		tree[0] = 0;
		for (int i = 0; i < n; i++) raw[i] = tree[i + 1] = init.applyAsLong(i);
		for (int i = 1; i <= n; i++) {
			int j = i + (i & -i);
			if (j <= n) tree[j] += tree[i];
		}
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
	 */
	public long set(final int i, final long v) {
		return add(i, v - raw[i]);
	}

	/**
	 * インデックス i (0-indexed) の要素に v を加算する。
	 *
	 * @param i インデックス
	 * @param v 加算する値
	 */
	public long add(final int i, final long v) {
		raw[i] += v;
		for (int cur = i + 1; cur <= n; cur += cur & -cur) tree[cur] += v;
		return raw[i];
	}

	public long multiply(final int i, final long a) {
		return add(i, raw[i] * (a - 1));
	}

	public long apply(final int i, final long a, final long b) {
		return add(i, raw[i] * (a - 1) + b);
	}

	public long apply(final int i, final long v, final LongBinaryOperator op) {
		return add(i, op.applyAsLong(raw[i], v) - raw[i]);
	}

	/**
	 * 閉区間 [0, r] の和を計算する。
	 *
	 * @param r 右端の境界 (includes)
	 * @return [0, r] の和
	 */
	public long sum(int r) {
		long s = 0;
		for (r++; r > 0; r -= r & -r) s += tree[r];
		return s;
	}

	/**
	 * 閉区間 [l, r] の和を計算する。
	 *
	 * @param l 左端の境界 (includes)
	 * @param r 右端の境界 (includes)
	 * @return [l, r] の和
	 */
	public long sum(final int l, final int r) {
		if (l > r) return 0;
		return sum(r) - sum(l - 1);
	}

	public long sumAll() {
		return sum(n - 1);
	}

	/**
	 * 累積和が w 以上となる最小のインデックス i (0-indexed) を返す。
	 * 全ての要素が非負であることを前提とする。
	 * 存在しない場合は n を返す。
	 *
	 * @param w ターゲットとなる累積和
	 * @return 累積和が w 以上となる最小のインデックス
	 */
	public int lowerBound(long w) {
		if (w <= 0) return 0;
		int i = 0;
		for (int k = Integer.highestOneBit(n); k > 0; k >>= 1) {
			if (i + k <= n && tree[i + k] < w) {
				w -= tree[i + k];
				i += k;
			}
		}
		return i;
	}

	/**
	 * 累積和が w より大きくなる最小のインデックス i (0-indexed) を返す。
	 * 全ての要素が非負であることを前提とする。
	 * 存在しない場合は n を返す。
	 *
	 * @param w ターゲットとなる累積和の上限
	 * @return 累積和が w より大きくなる最小のインデックス
	 */
	public int upperBound(long w) {
		if (w < 0) return 0;
		int i = 0;
		for (int k = headBit; k > 0; k >>= 1) {
			if (i + k <= n && tree[i + k] <= w) {
				w -= tree[i + k];
				i += k;
			}
		}
		return i;
	}

	public int size() {
		return n;
	}

	public PrimitiveIterator.OfLong iterator() {
		return new PrimitiveIterator.OfLong() {
			private int i = 0;

			public boolean hasNext() {
				return i < n;
			}

			public long nextLong() {
				if (!hasNext()) throw new NoSuchElementException();
				return raw[i++];
			}
		};
	}

	public String toString() {
		final StringBuilder s = new StringBuilder();
		s.append(raw[0]);
		for (int i = 1; i < n; i++) s.append(' ').append(raw[i]);
		return s.toString();
	}
}
