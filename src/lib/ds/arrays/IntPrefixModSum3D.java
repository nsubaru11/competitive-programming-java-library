package lib.ds.arrays;

import java.util.function.*;

/**
 * int値の3次元累積和を法{@code mod}で保持します。
 */
public final class IntPrefixModSum3D {
	public final int d, h, w, length, mod;
	private final int strideI, strideJ;
	private final int[] pref;

	/**
	 * supplierが生成する値をi、j、kの順に使用して初期化します。
	 */
	public IntPrefixModSum3D(final int d, final int h, final int w, final int mod, final IntSupplier init) {
		this.d = d;
		this.h = h;
		this.w = w;
		this.mod = mod;
		length = d * h * w;
		strideJ = w + 1;
		strideI = (h + 1) * strideJ;
		pref = new int[(d + 1) * strideI];
		for (int i = 0; i < d; i++) {
			for (int j = 0; j < h; j++) {
				int p = (i + 1) * strideI + (j + 1) * strideJ + 1;
				for (int k = 0; k < w; k++, p++) {
					int v = (int) (((long) init.getAsInt() + pref[p - strideI] + pref[p - strideJ] + pref[p - 1] - pref[p - strideI - strideJ] - pref[p - strideI - 1] - pref[p - strideJ - 1] + pref[p - strideI - strideJ - 1]) % mod);
					pref[p] = v < 0 ? v + mod : v;
				}
			}
		}
	}

	/**
	 * 指定された配列から構築します。
	 */
	public IntPrefixModSum3D(final int[][][] a, final int mod) {
		d = a.length;
		h = a[0].length;
		w = a[0][0].length;
		this.mod = mod;
		length = d * h * w;
		strideJ = w + 1;
		strideI = (h + 1) * strideJ;
		pref = new int[(d + 1) * strideI];
		for (int i = 0; i < d; i++) {
			for (int j = 0; j < h; j++) {
				int p = (i + 1) * strideI + (j + 1) * strideJ + 1;
				for (int k = 0; k < w; k++, p++) {
					int v = (int) (((long) a[i][j][k] + pref[p - strideI] + pref[p - strideJ] + pref[p - 1] - pref[p - strideI - strideJ] - pref[p - strideI - 1] - pref[p - strideJ - 1] + pref[p - strideI - strideJ - 1]) % mod);
					pref[p] = v < 0 ? v + mod : v;
				}
			}
		}
	}

	/**
	 * supplierが生成する値をi、j、kの順に使用して構築します。
	 */
	public static IntPrefixModSum3D generate(final int d, final int h, final int w, final int mod, final IntSupplier init) {
		return new IntPrefixModSum3D(d, h, w, mod, init);
	}

	/**
	 * 指定位置の元の値を返します。
	 */
	public int get(final int i, final int j, final int k) {
		return sum(i, j, k, i, j, k);
	}

	/**
	 * 全要素の和を法{@code mod}で返します。
	 */
	public int sum() {
		return pref[pref.length - 1];
	}

	/**
	 * 閉直方体{@code [0, i] x [0, j] x [0, k]}の和を法{@code mod}で返します。
	 */
	public int sum(final int i, final int j, final int k) {
		return pref[index(i + 1, j + 1, k + 1)];
	}

	/**
	 * 閉直方体{@code [i1, i2] x [j1, j2] x [k1, k2]}の和を法{@code mod}で返します。
	 */
	public int sum(final int i1, final int j1, final int k1, final int i2, final int j2, final int k2) {
		int ii = i2 + 1;
		int jj = j2 + 1;
		int kk = k2 + 1;
		int res = (int) (((long) pref[index(ii, jj, kk)] - pref[index(i1, jj, kk)] - pref[index(ii, j1, kk)] - pref[index(ii, jj, k1)] + pref[index(i1, j1, kk)] + pref[index(i1, jj, k1)] + pref[index(ii, j1, k1)] - pref[index(i1, j1, k1)]) % mod);
		return res < 0 ? res + mod : res;
	}

	private int index(final int i, final int j, final int k) {
		return i * strideI + j * strideJ + k;
	}

	/**
	 * 最終軸の要素を半角スペースで区切り、各行を改行した文字列として返します。
	 */
	public String toString() {
		final StringBuilder sb = new StringBuilder(11 * length - 1);
		for (int i = 0; i < d; i++) {
			for (int j = 0; j < h; j++) {
				if (i != 0 || j != 0) sb.append('\n');
				sb.append(get(i, j, 0));
				for (int k = 1; k < w; k++) sb.append(' ').append(get(i, j, k));
			}
		}
		return sb.toString();
	}
}
