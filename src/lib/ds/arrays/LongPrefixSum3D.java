package lib.ds.arrays;

import java.util.*;
import java.util.function.*;

/**
 * long値の3次元累積和を保持する。
 * 各次元の長さは正とし、指定区間は0始まりの有効な閉区間とする。
 */
public final class LongPrefixSum3D {
	public final int d, h, w, length;
	private final int strideI, strideJ;
	private final long[] pref;

	/**
	 * 各座標で init を1回呼び、k、j、i の順に添字を進めて構築する。
	 */
	public LongPrefixSum3D(final int d, final int h, final int w, final LongSupplier init) {
		this.d = d;
		this.h = h;
		this.w = w;
		length = d * h * w;
		strideJ = w + 1;
		strideI = (h + 1) * strideJ;
		pref = new long[(d + 1) * strideI];
		for (int i = 0; i < d; i++) {
			for (int j = 0; j < h; j++) {
				int p = (i + 1) * strideI + (j + 1) * strideJ + 1;
				for (int k = 0; k < w; k++, p++) {
					pref[p] = init.getAsLong() + pref[p - strideI] + pref[p - strideJ] + pref[p - 1] - pref[p - strideI - strideJ] - pref[p - strideI - 1] - pref[p - strideJ - 1] + pref[p - strideI - strideJ - 1];
				}
			}
		}
	}

	public LongPrefixSum3D(final long[][][] a) {
		d = a.length;
		h = a[0].length;
		w = a[0][0].length;
		length = d * h * w;
		strideJ = w + 1;
		strideI = (h + 1) * strideJ;
		pref = new long[(d + 1) * strideI];
		for (int i = 0; i < d; i++) {
			for (int j = 0; j < h; j++) {
				int p = (i + 1) * strideI + (j + 1) * strideJ + 1;
				for (int k = 0; k < w; k++, p++) {
					pref[p] = a[i][j][k] + pref[p - strideI] + pref[p - strideJ] + pref[p - 1] - pref[p - strideI - strideJ] - pref[p - strideI - 1] - pref[p - strideJ - 1] + pref[p - strideI - strideJ - 1];
				}
			}
		}
	}

	/**
	 * 指定位置の元の値を返す。
	 *
	 * @throws IndexOutOfBoundsException 添字が範囲外の場合
	 */
	public long get(final int i, final int j, final int k) {
		return sum(i, j, k, i, j, k);
	}

	/**
	 * 指定した座標の値を返す。いずれかの座標が範囲外の場合は defaultValue を返す。
	 */
	public long getOrDefault(final int i, final int j, final int k, final long defaultValue) {
		return 0 <= i && i < d && 0 <= j && j < h && 0 <= k && k < w ? get(i, j, k) : defaultValue;
	}

	public long sum() {
		return pref[pref.length - 1];
	}

	/**
	 * 閉直方体{@code [0, i] x [0, j] x [0, k]}の和を返す。
	 *
	 * @throws IndexOutOfBoundsException 添字が範囲外の場合
	 */
	public long sum(final int i, final int j, final int k) {
		Objects.checkIndex(i, d);
		Objects.checkIndex(j, h);
		Objects.checkIndex(k, w);
		return pref[index(i + 1, j + 1, k + 1)];
	}

	/**
	 * 閉直方体{@code [i1, i2] x [j1, j2] x [k1, k2]}の和を返す。
	 *
	 * @throws IndexOutOfBoundsException 添字が範囲外、または閉区間の始点が終点を超える場合
	 */
	public long sum(final int i1, final int j1, final int k1, final int i2, final int j2, final int k2) {
		Objects.checkIndex(i2, d);
		Objects.checkIndex(i1, i2 + 1);
		Objects.checkIndex(j2, h);
		Objects.checkIndex(j1, j2 + 1);
		Objects.checkIndex(k2, w);
		Objects.checkIndex(k1, k2 + 1);
		final int i = i2 + 1, j = j2 + 1, k = k2 + 1;
		return pref[index(i, j, k)] - pref[index(i1, j, k)] - pref[index(i, j1, k)] - pref[index(i, j, k1)] + pref[index(i1, j1, k)] + pref[index(i1, j, k1)] + pref[index(i, j1, k1)] - pref[index(i1, j1, k1)];
	}

	/**
	 * 最終軸の要素を半角スペースで区切り、各行を改行した文字列として返す。
	 */
	public String toString() {
		final StringBuilder sb = new StringBuilder(21 * length - 1);
		for (int i = 0; i < d; i++) {
			for (int j = 0; j < h; j++) {
				if (i != 0 || j != 0) sb.append('\n');
				sb.append(get(i, j, 0));
				for (int k = 1; k < w; k++) {
					sb.append(' ').append(get(i, j, k));
				}
			}
		}
		return sb.toString();
	}

	private int index(final int i, final int j, final int k) {
		return i * strideI + j * strideJ + k;
	}
}
