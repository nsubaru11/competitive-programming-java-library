package lib.ds.arrays;

import java.util.*;
import java.util.function.*;

/**
 * long値の2次元累積和を法 mod で保持する。
 * 各次元の長さは正とし、指定区間は0始まりの有効な閉区間とする。法は正、入力値は {@code 0 <= v < mod} とする。
 * sum の結果は {@code [0, mod)} に正規化する。
 */
public final class LongPrefixModSum2D {
	public final int h, w, length;
	public final long mod;
	private final int stride;
	private final long[] pref;

	/**
	 * 各座標に {@code init(i, j)} を行優先順で適用して構築する。
	 */
	public LongPrefixModSum2D(final int h, final int w, final long mod, final LongBinaryOperator init) {
		this.h = h;
		this.w = w;
		this.mod = mod;
		length = h * w;
		stride = w + 1;
		pref = new long[(h + 1) * stride];
		for (int i = 0; i < h; i++) {
			int p = (i + 1) * stride + 1;
			for (int j = 0; j < w; j++, p++) {
				final long v = (init.applyAsLong(i, j) + pref[p - stride] + pref[p - 1] - pref[p - stride - 1]) % mod;
				pref[p] = v < 0 ? v + mod : v;
			}
		}
	}

	public LongPrefixModSum2D(final long[][] a, final long mod) {
		h = a.length;
		w = a[0].length;
		this.mod = mod;
		length = h * w;
		stride = w + 1;
		pref = new long[(h + 1) * stride];
		for (int i = 0; i < h; i++) {
			int p = (i + 1) * stride + 1;
			for (int j = 0; j < w; j++, p++) {
				final long v = (a[i][j] + pref[p - stride] + pref[p - 1] - pref[p - stride - 1]) % mod;
				pref[p] = v < 0 ? v + mod : v;
			}
		}
	}

	/**
	 * 指定位置の元の値を返す。
	 *
	 * @throws IndexOutOfBoundsException 添字が範囲外の場合
	 */
	public long get(final int i, final int j) {
		return sum(i, j, i, j);
	}

	/**
	 * 指定した座標の値を返す。いずれかの座標が範囲外の場合は defaultValue を返す。
	 */
	public long getOrDefault(final int i, final int j, final long defaultValue) {
		return 0 <= i && i < h && 0 <= j && j < w ? get(i, j) : defaultValue;
	}

	public long sum() {
		return pref[pref.length - 1];
	}

	/**
	 * 閉矩形{@code [0, i] x [0, j]}の和を返す。
	 *
	 * @throws IndexOutOfBoundsException 添字が範囲外の場合
	 */
	public long sum(final int i, final int j) {
		Objects.checkIndex(i, h);
		Objects.checkIndex(j, w);
		return pref[(i + 1) * stride + j + 1];
	}

	/**
	 * 閉矩形{@code [i1, i2] x [j1, j2]}の和を返す。
	 *
	 * @throws IndexOutOfBoundsException 添字が範囲外、または閉区間の始点が終点を超える場合
	 */
	public long sum(final int i1, final int j1, final int i2, final int j2) {
		Objects.checkIndex(i2, h);
		Objects.checkIndex(i1, i2 + 1);
		Objects.checkIndex(j2, w);
		Objects.checkIndex(j1, j2 + 1);
		final int p1 = i1 * stride;
		final int p2 = (i2 + 1) * stride;
		final long res = (pref[p2 + j2 + 1] - pref[p1 + j2 + 1] - pref[p2 + j1] + pref[p1 + j1]) % mod;
		return res < 0 ? res + mod : res;
	}

	/**
	 * 各行を半角スペース区切り、行間を改行した文字列として返す。
	 */
	public String toString() {
		final StringBuilder sb = new StringBuilder(20 * length - 1);
		sb.append(get(0, 0));
		for (int j = 1; j < w; j++) sb.append(' ').append(get(0, j));
		for (int i = 1; i < h; i++) {
			sb.append('\n').append(get(i, 0));
			for (int j = 1; j < w; j++) {
				sb.append(' ').append(get(i, j));
			}
		}
		return sb.toString();
	}
}
