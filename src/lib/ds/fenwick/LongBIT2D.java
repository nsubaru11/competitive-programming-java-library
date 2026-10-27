package lib.ds.fenwick;

import java.util.*;
import java.util.function.*;

import lib.ds.*;
import lib.util.function.*;

/**
 * 競技プログラミング向け 2次元 Binary Indexed Tree (Fenwick Tree) の long 型特化実装。
 * <p>
 * 0-indexed での操作を提供し、点更新と矩形領域の和の計算を O(log H * log W) で行う。
 */
@SuppressWarnings("unused")
public final class LongBIT2D implements LongCollection {
	public final int h, w, size;
	private final long[] raw, tree;
	private long total;

	/**
	 * サイズ h x w の 2次元 BIT を構築する。初期値はすべて 0。
	 *
	 * @param h 高さ
	 * @param w 幅
	 */
	public LongBIT2D(final int h, final int w) {
		this.h = h;
		this.w = w;
		this.size = h * w;
		tree = new long[(h + 1) * (w + 1)];
		raw = new long[size];
	}

	/**
	 * 初期値関数を用いて 2次元 BIT を O(HW) で構築する。
	 *
	 * @param h    高さ
	 * @param w    幅
	 * @param init 初期値関数 (row, col) -> value
	 */
	public LongBIT2D(final int h, final int w, final IntBinaryToLongFunction init) {
		this(h, w);
		setAll(init);
	}

	public int size() {
		return size;
	}

	/**
	 * インデックス (i, j) の現在の値を取得する。
	 *
	 * @param i 行インデックス (0-indexed)
	 * @param j 列インデックス (0-indexed)
	 * @return 現在の値
	 */
	public long get(final int i, final int j) {
		return raw[checkedIndex(i, j)];
	}

	/**
	 * インデックス (i, j) の要素を v に更新する。
	 *
	 * @param i 行インデックス (0-indexed)
	 * @param j 列インデックス (0-indexed)
	 * @param v 更新後の値
	 * @return 更新前の値
	 */
	public long set(final int i, final int j, final long v) {
		final int p = checkedIndex(i, j);
		final long old = raw[p];
		add(i, j, p, v - old);
		return old;
	}

	/**
	 * インデックス (i, j) の要素に d を加算する。
	 *
	 * @param i 行インデックス (0-indexed)
	 * @param j 列インデックス (0-indexed)
	 * @param d 加算する値
	 */
	public long add(final int i, final int j, final long d) {
		return add(i, j, checkedIndex(i, j), d);
	}

	public long multiply(final int i, final int j, final long m) {
		final int p = checkedIndex(i, j);
		return add(i, j, p, raw[p] * (m - 1));
	}

	/**
	 * 指定位置を {@code 現在値 * m + d} に更新する。
	 */
	public long affine(final int i, final int j, final long m, final long d) {
		final int p = checkedIndex(i, j);
		return add(i, j, p, raw[p] * (m - 1) + d);
	}

	public long apply(final int i, final int j, final long x, final LongBinaryOperator f) {
		final int p = checkedIndex(i, j);
		return add(i, j, p, f.applyAsLong(raw[p], x) - raw[p]);
	}

	public void fill(final long v) {
		total = size * v;
		final int w1 = w + 1;
		for (int i = 0, p = 0; i < h; i++) {
			for (int j = 0; j < w; j++, p++) {
				tree[(i + 1) * w1 + (j + 1)] = raw[p] = v;
			}
		}
		for (int i = 1; i <= h; i++) {
			final int idx = i * w1;
			for (int j = 1; j <= w; j++) {
				final int nj = j + (j & -j);
				if (nj <= w) tree[idx + nj] += tree[idx + j];
			}
		}
		for (int i = 1; i <= h; i++) {
			final int ni = i + (i & -i);
			if (ni <= h) {
				final int p = i * w1, q = ni * w1;
				for (int j = 1; j <= w; j++) {
					tree[q + j] += tree[p + j];
				}
			}
		}
	}

	/**
	 * BIT の全要素を再構築する。
	 *
	 * @param init 初期値関数
	 */
	public void setAll(final IntBinaryToLongFunction init) {
		total = 0;
		final int w1 = w + 1;
		for (int i = 0, p = 0; i < h; i++) {
			for (int j = 0; j < w; j++, p++) {
				total += tree[(i + 1) * w1 + (j + 1)] = raw[p] = init.applyAsLong(i, j);
			}
		}
		for (int i = 1; i <= h; i++) {
			final int idx = i * w1;
			for (int j = 1; j <= w; j++) {
				final int nj = j + (j & -j);
				if (nj <= w) tree[idx + nj] += tree[idx + j];
			}
		}
		for (int i = 1; i <= h; i++) {
			final int ni = i + (i & -i);
			if (ni <= h) {
				final int p = i * w1, q = ni * w1;
				for (int j = 1; j <= w; j++) {
					tree[q + j] += tree[p + j];
				}
			}
		}
	}

	/**
	 * 矩形領域 [0, i] x [0, j] の和を計算する。終端 -1 は空領域を表す。
	 *
	 * @param i 下端の境界 (includes)
	 * @param j 右端の境界 (includes)
	 * @return 領域内の和
	 */
	public long sum(final int i, final int j) {
		Objects.checkIndex(i + 1, h + 1);
		Objects.checkIndex(j + 1, w + 1);
		return prefixSum(i + 1, j + 1);
	}

	/**
	 * 矩形領域 [i1, i2] x [j1, j2] の和を計算する。
	 *
	 * @param i1 上端の境界 (includes)
	 * @param j1 左端の境界 (includes)
	 * @param i2 下端の境界 (includes)
	 * @param j2 右端の境界 (includes)
	 * @return 領域内の和
	 */
	public long sum(final int i1, final int j1, final int i2, final int j2) {
		if (i1 > i2 || j1 > j2) return 0;
		Objects.checkFromToIndex(i1, i2 + 1, h);
		Objects.checkFromToIndex(j1, j2 + 1, w);
		return prefixSum(i2 + 1, j2 + 1) - prefixSum(i1, j2 + 1) - prefixSum(i2 + 1, j1) + prefixSum(i1, j1);
	}

	/**
	 * 全要素の和を O(1) で返す。
	 *
	 * @return 全要素の和
	 */
	public long sumAll() {
		return total;
	}

	public long[] toArray() {
		return raw.clone();
	}

	public long[][] toArray2D() {
		final long[][] res = new long[h][w];
		for (int i = 0, p = 0; i < h; i++) {
			for (int j = 0; j < w; j++, p++) {
				res[i][j] = raw[p];
			}
		}
		return res;
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
		for (int i = 0, p = 0; i < h; i++) {
			sb.append(raw[p++]);
			for (int j = 1; j < w; j++) {
				sb.append(' ').append(raw[p++]);
			}
			sb.append('\n');
		}
		return sb.toString();
	}

	private long add(final int i, final int j, final int p, final long d) {
		raw[p] += d;
		total += d;
		final int w1 = w + 1;
		for (int i2 = i + 1; i2 <= h; i2 += i2 & -i2) {
			final int idx = i2 * w1;
			for (int j2 = j + 1; j2 <= w; j2 += j2 & -j2) {
				tree[idx + j2] += d;
			}
		}
		return raw[p];
	}

	private int checkedIndex(final int i, final int j) {
		return i * w + Objects.checkIndex(j, w);
	}

	private long prefixSum(final int i, final int j) {
		long s = 0;
		final int w1 = w + 1;
		for (int i2 = i; i2 > 0; i2 -= i2 & -i2) {
			final int p = i2 * w1;
			for (int j2 = j; j2 > 0; j2 -= j2 & -j2) {
				s += tree[p + j2];
			}
		}
		return s;
	}
}
