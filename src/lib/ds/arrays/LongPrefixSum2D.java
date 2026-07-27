package lib.ds.arrays;

import java.util.function.*;

/**
 * long値の2次元累積和を一次元配列で保持します。
 */
public final class LongPrefixSum2D {
	public final int h, w, length;
	private final int stride;
	private final long[] pref;

	/**
	 * {@code init(i, j)}で初期化します。
	 */
	public LongPrefixSum2D(final int h, final int w, final LongBinaryOperator init) {
		this.h = h;
		this.w = w;
		length = h * w;
		stride = w + 1;
		pref = new long[(h + 1) * stride];
		for (int i = 0; i < h; i++) {
			int p = (i + 1) * stride + 1;
			for (int j = 0; j < w; j++, p++) {
				pref[p] = init.applyAsLong(i, j) + pref[p - stride] + pref[p - 1] - pref[p - stride - 1];
			}
		}
	}

	/**
	 * 指定された配列から構築します。
	 */
	public LongPrefixSum2D(final long[][] a) {
		h = a.length;
		w = a[0].length;
		length = h * w;
		stride = w + 1;
		pref = new long[(h + 1) * stride];
		for (int i = 0; i < h; i++) {
			int p = (i + 1) * stride + 1;
			for (int j = 0; j < w; j++, p++) {
				pref[p] = a[i][j] + pref[p - stride] + pref[p - 1] - pref[p - stride - 1];
			}
		}
	}

	/**
	 * longのsupplierが生成する値を行優先順に使用して構築します。
	 */
	public static LongPrefixSum2D generate(final int h, final int w, final LongSupplier init) {
		return new LongPrefixSum2D(h, w, (_, _) -> init.getAsLong());
	}

	/**
	 * 指定位置の元の値を返します。
	 */
	public long get(final int i, final int j) {
		return sum(i, j, i, j);
	}

	/**
	 * 全要素の和を返します。
	 */
	public long sum() {
		return pref[pref.length - 1];
	}

	/**
	 * 閉矩形{@code [0, i] x [0, j]}の和を返します。
	 */
	public long sum(final int i, final int j) {
		return pref[(i + 1) * stride + j + 1];
	}

	/**
	 * 閉矩形{@code [i1, i2] x [j1, j2]}の和を返します。
	 */
	public long sum(final int i1, final int j1, final int i2, final int j2) {
		int p1 = i1 * stride;
		int p2 = (i2 + 1) * stride;
		return pref[p2 + j2 + 1] - pref[p1 + j2 + 1] - pref[p2 + j1] + pref[p1 + j1];
	}

	/**
	 * 各行を半角スペース区切り、行間を改行した文字列として返します。
	 */
	public String toString() {
		final StringBuilder sb = new StringBuilder(21 * length - 1);
		sb.append(get(0, 0));
		for (int j = 1; j < w; j++) sb.append(' ').append(get(0, j));
		for (int i = 1; i < h; i++) {
			sb.append('\n').append(get(i, 0));
			for (int j = 1; j < w; j++) sb.append(' ').append(get(i, j));
		}
		return sb.toString();
	}
}
