package lib.ds.fenwick;

import java.util.*;
import java.util.function.*;

import lib.ds.*;

/**
 * 競技プログラミング向け 2次元 Binary Indexed Tree (Fenwick Tree) の int 型特化実装。
 * <p>
 * 0-indexed での操作を提供し、点更新と矩形領域の和の計算を O(log H * log W) で行う。
 */
@SuppressWarnings("unused")
public final class IntBIT2D implements IntCollection {
	public final int h, w;
	private final int hw;
	private final int[] tree, raw;
	private int total;

	/**
	 * サイズ h x w の 2次元 BIT を構築する。初期値はすべて 0。
	 *
	 * @param h 高さ
	 * @param w 幅
	 */
	public IntBIT2D(final int h, final int w) {
		this.h = h;
		this.w = w;
		this.hw = h * w;
		tree = new int[(h + 1) * (w + 1)];
		raw = new int[hw];
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

	public void fill(final int val) {
		total = hw * val;
		int w1 = w + 1;
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				raw[i * w + j] = tree[(i + 1) * w1 + (j + 1)] = val;
			}
		}
		for (int i = 1; i <= h; i++) {
			int idx = i * w1;
			for (int j = 1; j <= w; j++) {
				int nj = j + (j & -j);
				if (nj <= w) tree[idx + nj] += tree[idx + j];
			}
		}
		for (int i = 1; i <= h; i++) {
			int ni = i + (i & -i);
			if (ni <= h) {
				int cIdx = i * w1, nIdx = ni * w1;
				for (int j = 1; j <= w; j++) {
					tree[nIdx + j] += tree[cIdx + j];
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
		int w1 = w + 1;
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				total += raw[i * w + j] = tree[(i + 1) * w1 + (j + 1)] = init.applyAsInt(i, j);
			}
		}
		for (int i = 1; i <= h; i++) {
			int idx = i * w1;
			for (int j = 1; j <= w; j++) {
				int nj = j + (j & -j);
				if (nj <= w) tree[idx + nj] += tree[idx + j];
			}
		}
		for (int i = 1; i <= h; i++) {
			int ni = i + (i & -i);
			if (ni <= h) {
				int cIdx = i * w1, nIdx = ni * w1;
				for (int j = 1; j <= w; j++) {
					tree[nIdx + j] += tree[cIdx + j];
				}
			}
		}
	}

	/**
	 * インデックス (i, j) の現在の値を取得する。
	 *
	 * @param i 行インデックス (0-indexed)
	 * @param j 列インデックス (0-indexed)
	 * @return 現在の値
	 */
	public int get(final int i, final int j) {
		return raw[i * w + j];
	}

	/**
	 * インデックス (i, j) の要素を v に更新する。
	 *
	 * @param i 行インデックス (0-indexed)
	 * @param j 列インデックス (0-indexed)
	 * @param v 更新後の値
	 */
	public int set(final int i, final int j, final int v) {
		return add(i, j, v - raw[i * w + j]);
	}

	/**
	 * インデックス (i, j) の要素に v を加算する。
	 *
	 * @param i 行インデックス (0-indexed)
	 * @param j 列インデックス (0-indexed)
	 * @param v 加算する値
	 */
	public int add(final int i, final int j, final int v) {
		int ij = i * w + j;
		raw[ij] += v;
		total += v;
		int w1 = w + 1;
		for (int i2 = i + 1; i2 <= h; i2 += i2 & -i2) {
			int idx = i2 * w1;
			for (int j2 = j + 1; j2 <= w; j2 += j2 & -j2) {
				tree[idx + j2] += v;
			}
		}
		return raw[ij];
	}

	/**
	 * インデックス (i, j) の要素を a 倍する。
	 *
	 * @param i 行インデックス (0-indexed)
	 * @param j 列インデックス (0-indexed)
	 * @param a 乗算する値
	 * @return 更新後の値
	 */
	public int multiply(final int i, final int j, final int a) {
		return add(i, j, raw[i * w + j] * (a - 1));
	}

	/**
	 * インデックス (i, j) の要素に affine 変換 {@code x -> a * x + b} を適用する。
	 *
	 * @param i 行インデックス (0-indexed)
	 * @param j 列インデックス (0-indexed)
	 * @param a 乗数
	 * @param b 加算値
	 * @return 更新後の値
	 */
	public int apply(final int i, final int j, final int a, final int b) {
		return add(i, j, raw[i * w + j] * (a - 1) + b);
	}

	/**
	 * インデックス (i, j) の要素に二項演算を適用する。
	 *
	 * @param i  行インデックス (0-indexed)
	 * @param j  列インデックス (0-indexed)
	 * @param v  演算の第2引数
	 * @param op 演算関数 (current, v) -> newValue
	 * @return 更新後の値
	 */
	public int apply(final int i, final int j, final int v, final IntBinaryOperator op) {
		int ij = i * w + j;
		return add(i, j, op.applyAsInt(raw[ij], v) - raw[ij]);
	}

	/**
	 * 矩形領域 [0, i] x [0, j] の和を計算する。
	 *
	 * @param i 下端の境界 (includes)
	 * @param j 右端の境界 (includes)
	 * @return 領域内の和
	 */
	public int sum(final int i, final int j) {
		int s = 0;
		int w1 = w + 1;
		for (int i2 = i + 1; i2 > 0; i2 -= i2 & -i2) {
			int idx = i2 * w1;
			for (int j2 = j + 1; j2 > 0; j2 -= j2 & -j2) s += tree[idx + j2];
		}
		return s;
	}

	/**
	 * 矩形領域 [i1, i2] x [j1, j2] の和を計算する。
	 *
	 * @param i1 上端の境界 (includes)
	 * @param i2 下端の境界 (includes)
	 * @param j1 左端の境界 (includes)
	 * @param j2 右端の境界 (includes)
	 * @return 領域内の和
	 */
	public int sum(final int i1, final int j1, final int i2, final int j2) {
		if (i1 > i2 || j1 > j2) return 0;
		return sum(i2, j2) - sum(i1 - 1, j2) - sum(i2, j1 - 1) + sum(i1 - 1, j1 - 1);
	}

	/**
	 * 全要素の和を O(1) で返す。
	 *
	 * @return 全要素の和
	 */
	public int sumAll() {
		return total;
	}

	public int size() {
		return hw;
	}

	public PrimitiveIterator.OfInt iterator() {
		return new PrimitiveIterator.OfInt() {
			private int i = 0;

			public boolean hasNext() {
				return i < hw;
			}

			public int nextInt() {
				if (!hasNext()) throw new NoSuchElementException();
				return get(i / w, i++ % w);
			}
		};
	}

	public String toString() {
		final StringBuilder s = new StringBuilder();
		for (int i = 0, ij = 0; i < h; i++) {
			s.append(raw[ij++]);
			for (int j = 1; j < w; j++) {
				s.append(' ').append(raw[ij++]);
			}
			s.append('\n');
		}
		return s.toString();
	}
}
