package lib.ds.arrays;

import java.util.function.*;

/**
 * long値の2次元累積和を法{@code mod}で保持します。
 */
public final class LongPrefixModSum2D {
	public final int h, w, length;
	public final long mod;
	private final int stride;
	private final long[] pref;

	/**
	 * {@code init(i, j)}で初期化します。
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
				long v = (init.applyAsLong(i, j) + pref[p - stride] + pref[p - 1] - pref[p - stride - 1]) % mod;
				pref[p] = v < 0 ? v + mod : v;
			}
		}
	}

	/**
	 * 指定された配列から構築します。
	 */
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
				long v = (a[i][j] + pref[p - stride] + pref[p - 1] - pref[p - stride - 1]) % mod;
				pref[p] = v < 0 ? v + mod : v;
			}
		}
	}

	/**
	 * supplierが生成する値を行優先順に使用して構築します。
	 */
	public static LongPrefixModSum2D generate(final int h, final int w, final long mod, final LongSupplier init) {
		return new LongPrefixModSum2D(h, w, mod, (_, _) -> init.getAsLong());
	}

	/**
	 * 指定位置の元の値を返します。
	 */
	public long get(final int i, final int j) {
		return sum(i, j, i, j);
	}

	/**
	 * 全要素の和を法{@code mod}で返します。
	 */
	public long sum() {
		return pref[pref.length - 1];
	}

	/**
	 * 閉矩形{@code [0, i] x [0, j]}の和を法{@code mod}で返します。
	 */
	public long sum(final int i, final int j) {
		return pref[(i + 1) * stride + j + 1];
	}

	/**
	 * 閉矩形{@code [i1, i2] x [j1, j2]}の和を法{@code mod}で返します。
	 */
	public long sum(final int i1, final int j1, final int i2, final int j2) {
		int p1 = i1 * stride;
		int p2 = (i2 + 1) * stride;
		long res = (pref[p2 + j2 + 1] - pref[p1 + j2 + 1] - pref[p2 + j1] + pref[p1 + j1]) % mod;
		return res < 0 ? res + mod : res;
	}

	/**
	 * 各行を半角スペース区切り、行間を改行した文字列として返します。
	 */
	public String toString() {
		final StringBuilder sb = new StringBuilder(20 * length - 1);
		sb.append(get(0, 0));
		for (int j = 1; j < w; j++) sb.append(' ').append(get(0, j));
		for (int i = 1; i < h; i++) {
			sb.append('\n').append(get(i, 0));
			for (int j = 1; j < w; j++) sb.append(' ').append(get(i, j));
		}
		return sb.toString();
	}
}
