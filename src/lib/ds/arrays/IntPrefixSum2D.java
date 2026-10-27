package lib.ds.arrays;

import java.util.*;
import java.util.function.*;

import lib.ds.*;

/**
 * int値の2次元累積和を保持する。
 * 各次元の長さは正とし、指定区間は0始まりの有効な閉区間とする。
 */
public final class IntPrefixSum2D implements IntCollection {
	public final int h, w, size;
	private final int stride;
	private final long[] pref;

	/**
	 * 各座標に {@code init(i, j)} を行優先順で適用して構築する。
	 */
	public IntPrefixSum2D(final int h, final int w, final IntBinaryOperator init) {
		this.h = h;
		this.w = w;
		size = h * w;
		stride = w + 1;
		pref = new long[(h + 1) * stride];
		for (int i = 0; i < h; i++) {
			int p = (i + 1) * stride + 1;
			for (int j = 0; j < w; j++, p++) {
				pref[p] = init.applyAsInt(i, j) + pref[p - stride] + pref[p - 1] - pref[p - stride - 1];
			}
		}
	}

	public IntPrefixSum2D(final int[][] a) {
		h = a.length;
		w = a[0].length;
		size = h * w;
		stride = w + 1;
		pref = new long[(h + 1) * stride];
		for (int i = 0; i < h; i++) {
			int p = (i + 1) * stride + 1;
			for (int j = 0; j < w; j++, p++) {
				pref[p] = a[i][j] + pref[p - stride] + pref[p - 1] - pref[p - stride - 1];
			}
		}
	}

	public int size() {
		return size;
	}

	/**
	 * {@code 0 <= i < h} かつ {@code 0 <= j < w} かを返す。
	 */
	public boolean inRange(final int i, final int j) {
		return (i | j | (h - 1 - i) | (w - 1 - j)) >= 0;
	}

	/**
	 * 指定位置の元の値を返す。
	 *
	 * @throws IndexOutOfBoundsException 添字が範囲外の場合
	 */
	public int get(final int i, final int j) {
		Objects.checkIndex(j, w);
		final int p = (i + 1) * stride + j + 1;
		return (int) (pref[p] - pref[p - stride] - pref[p - 1] + pref[p - stride - 1]);
	}

	/**
	 * 指定した座標の値を返す。いずれかの座標が範囲外の場合は defaultValue を返す。
	 */
	public int getOrDefault(final int i, final int j, final int defaultValue) {
		return inRange(i, j) ? get(i, j) : defaultValue;
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
		return pref[p2 + j2 + 1] - pref[p1 + j2 + 1] - pref[p2 + j1] + pref[p1 + j1];
	}

	public PrimitiveIterator.OfInt iterator() {
		return new PrimitiveIterator.OfInt() {
			private int i = 0, j = 0, k = 0;

			public boolean hasNext() {
				return k < size;
			}

			public int nextInt() {
				if (!hasNext()) throw new NoSuchElementException();
				final int v = get(i, j);
				k++;
				if (++j == w) {
					j = 0;
					i++;
				}
				return v;
			}
		};
	}

	/**
	 * 行優先順の ORDERED・SIZED・SUBSIZED を持つ spliterator を返す。
	 */
	public Spliterator.OfInt spliterator() {
		return Spliterators.spliterator(iterator(), size, Spliterator.ORDERED);
	}

	/**
	 * 各行を半角スペース区切り、行間を改行した文字列として返す。
	 */
	public String toString() {
		final StringBuilder sb = new StringBuilder(12 * size - 1);
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
