import java.util.function.*;

/**
 * 競技プログラミング向け 2次元 Binary Indexed Tree (Fenwick Tree) の int 型特化実装。
 * <p>
 * 0-indexed での操作を提供し、点更新と矩形領域の和の計算を O(log H * log W) で行う。
 */
@SuppressWarnings("unused")
public final class IntBIT2D {
	private final int h, w;
	private final int[] tree, raw;

	/**
	 * サイズ h x w の 2次元 BIT を構築する。初期値はすべて 0。
	 *
	 * @param h 高さ
	 * @param w 幅
	 */
	public IntBIT2D(final int h, final int w) {
		this.h = h;
		this.w = w;
		tree = new int[(h + 1) * (w + 1)];
		raw = new int[h * w];
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

	/**
	 * BIT の全要素を再構築する。
	 *
	 * @param init 初期値関数
	 */
	public void setAll(final IntBinaryOperator init) {
		int w1 = w + 1;
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				raw[i * w + j] = tree[(i + 1) * w1 + (j + 1)] = init.applyAsInt(i, j);
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
	 * インデックス (i, j) の要素に v を加算する。
	 *
	 * @param i 行インデックス (0-indexed)
	 * @param j 列インデックス (0-indexed)
	 * @param v 加算する値
	 */
	public void apply(final int i, final int j, final int v) {
		raw[i * w + j] += v;
		int w1 = w + 1;
		for (int i2 = i + 1; i2 <= h; i2 += i2 & -i2) {
			int idx = i2 * w1;
			for (int j2 = j + 1; j2 <= w; j2 += j2 & -j2) {
				tree[idx + j2] += v;
			}
		}
	}

	/**
	 * インデックス (i, j) の要素を v に更新する。
	 *
	 * @param i 行インデックス (0-indexed)
	 * @param j 列インデックス (0-indexed)
	 * @param v 更新後の値
	 */
	public void set(final int i, final int j, final int v) {
		apply(i, j, v - raw[i * w + j]);
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
	 * 矩形領域 [0, i] x [0, j] の和を計算する。
	 *
	 * @param i 下端の境界 (includes)
	 * @param j 右端の境界 (includes)
	 * @return 領域内の和
	 */
	public int query(int i, int j) {
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
	public int query(final int i1, final int j1, final int i2, final int j2) {
		if (i1 > i2 || j1 > j2) return 0;
		return query(i2, j2) - query(i1 - 1, j2) - query(i2, j1 - 1) + query(i1 - 1, j1 - 1);
	}

	/**
	 * BIT の高さを返す。
	 *
	 * @return 高さ
	 */
	public int height() {
		return h;
	}

	/**
	 * BIT の幅を返す。
	 *
	 * @return 幅
	 */
	public int width() {
		return w;
	}
}
