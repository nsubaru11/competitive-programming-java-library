package lib.ds.fenwick;

import java.util.*;
import java.util.function.*;

import lib.ds.*;

/**
 * 競技プログラミング向け 2次元 Binary Indexed Tree (Fenwick Tree) の int 型特化実装。
 * 各操作後の要素は int の範囲内とし、更新差分と和は long で扱う。
 * 0-indexed での操作を提供し、点更新と矩形領域の和の計算を O(log H * log W) で行う。
 */
@SuppressWarnings("unused")
public final class IntBIT2D implements IntCollection {
	public final int h, w, size;
	private final int[] raw;
	private final long[] tree;
	private long total;

	/**
	 * サイズ h x w の 2次元 BIT を構築する。初期値はすべて 0。
	 *
	 * @param h 高さ
	 * @param w 幅
	 */
	public IntBIT2D(final int h, final int w) {
		this.h = h;
		this.w = w;
		this.size = h * w;
		raw = new int[size];
		tree = new long[(h + 1) * (w + 1)];
	}

	/**
	 * 初期値関数を用いて 2次元 BIT を O(HW) で構築する。
	 *
	 * @param h    高さ
	 * @param w    幅
	 * @param init 初期値関数 (row, col) -> value
	 */
	public IntBIT2D(final int h, final int w, final IntBinaryOperator init) {
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
	public int get(final int i, final int j) {
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
	public int set(final int i, final int j, final int v) {
		final int p = checkedIndex(i, j);
		final int old = raw[p];
		add(i, j, p, (long) v - old);
		return old;
	}

	/**
	 * インデックス (i, j) の要素に d を加算する。
	 *
	 * @param i 行インデックス (0-indexed)
	 * @param j 列インデックス (0-indexed)
	 * @param d 加算する値
	 */
	public int add(final int i, final int j, final int d) {
		return add(i, j, checkedIndex(i, j), d);
	}

	/**
	 * インデックス (i, j) の要素を m 倍する。
	 *
	 * @param i 行インデックス (0-indexed)
	 * @param j 列インデックス (0-indexed)
	 * @param m 乗算する値
	 * @return 更新後の値
	 */
	public int multiply(final int i, final int j, final int m) {
		final int p = checkedIndex(i, j);
		return add(i, j, p, raw[p] * (m - 1L));
	}

	/**
	 * インデックス (i, j) の要素に affine 変換 {@code x -> m * x + d} を適用する。
	 *
	 * @param i 行インデックス (0-indexed)
	 * @param j 列インデックス (0-indexed)
	 * @param m 乗数
	 * @param d 加算値
	 * @return 更新後の値
	 */
	public int affine(final int i, final int j, final int m, final int d) {
		final int p = checkedIndex(i, j);
		return add(i, j, p, raw[p] * (m - 1L) + d);
	}

	/**
	 * インデックス (i, j) の要素に二項演算を適用する。
	 *
	 * @param i 行インデックス (0-indexed)
	 * @param j 列インデックス (0-indexed)
	 * @param x 演算の第2引数
	 * @param f 演算関数 (current, x) -> newValue
	 * @return 更新後の値
	 */
	public int apply(final int i, final int j, final int x, final IntBinaryOperator f) {
		final int p = checkedIndex(i, j);
		return add(i, j, p, (long) f.applyAsInt(raw[p], x) - raw[p]);
	}

	public void fill(final int v) {
		total = (long) size * v;
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
	public void setAll(final IntBinaryOperator init) {
		total = 0;
		final int w1 = w + 1;
		for (int i = 0, p = 0; i < h; i++) {
			for (int j = 0; j < w; j++, p++) {
				total += tree[(i + 1) * w1 + (j + 1)] = raw[p] = init.applyAsInt(i, j);
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

	public int[] toArray() {
		return raw.clone();
	}

	public int[][] toArray2D() {
		final int[][] res = new int[h][w];
		for (int i = 0, p = 0; i < h; i++) {
			for (int j = 0; j < w; j++, p++) {
				res[i][j] = raw[p];
			}
		}
		return res;
	}

	public void forEachInt(final IntConsumer action) {
		for (int p = 0; p < size; p++) action.accept(raw[p]);
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
		for (int i = 0, p = 0; i < h; i++) {
			sb.append(raw[p++]);
			for (int j = 1; j < w; j++) {
				sb.append(' ').append(raw[p++]);
			}
			sb.append('\n');
		}
		return sb.toString();
	}

	private int add(final int i, final int j, final int p, final long d) {
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
