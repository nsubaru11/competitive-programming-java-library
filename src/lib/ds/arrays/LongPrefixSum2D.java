package lib.ds.arrays;

import java.util.*;

import lib.ds.*;
import lib.util.function.*;

/**
 * long値の2次元累積和を保持する。構築後は更新しない。各要素と和は long の範囲内とする。
 * 座標は0始まりの行 i・列 j とし、各次元の長さは正とする。{@code sum(i, j)} は閉矩形 {@code [0, i] x [0, j]}、
 * {@code sum(i1, j1, i2, j2)} は閉矩形 {@code [i1, i2] x [j1, j2]} の和を返す。
 * 始点が終点を超える軸がある区間は空領域として0を返す。座標が範囲外の場合は、空領域でも IndexOutOfBoundsException を投げる。
 * get は元の値を返し、getOrDefault は範囲外で defaultValue を返す。init は行優先順に1回ずつ呼び、走査と toString は行優先順とする。
 */
public final class LongPrefixSum2D implements LongCollection {
	public final int h, w, size;
	private final int stride;
	private final long[] pref;

	public LongPrefixSum2D(final int h, final int w, final IntBinaryToLongFunction init) {
		this(h, w);
		for (int i = 0; i < h; i++) {
			int p = (i + 1) * stride + 1;
			for (int j = 0; j < w; j++, p++) pref[p] = init.applyAsLong(i, j);
		}
		build();
	}

	public LongPrefixSum2D(final long[][] a) {
		this(a.length, a[0].length);
		for (int i = 0; i < h; i++) {
			int p = (i + 1) * stride + 1;
			for (int j = 0; j < w; j++, p++) pref[p] = a[i][j];
		}
		build();
	}

	private LongPrefixSum2D(final int h, final int w) {
		this.h = h;
		this.w = w;
		size = h * w;
		stride = w + 1;
		pref = new long[(h + 1) * stride];
	}

	public int size() {
		return size;
	}

	public boolean inRange(final int i, final int j) {
		return (i | j | (h - 1 - i) | (w - 1 - j)) >= 0;
	}

	public long get(final int i, final int j) {
		Objects.checkIndex(j, w);
		final int p = (i + 1) * stride + j + 1;
		return pref[p] - pref[p - stride] - pref[p - 1] + pref[p - stride - 1];
	}

	public long getOrDefault(final int i, final int j, final long defaultValue) {
		return inRange(i, j) ? get(i, j) : defaultValue;
	}

	public long sumAll() {
		return pref[pref.length - 1];
	}

	public long sum(final int i, final int j) {
		Objects.checkIndex(i, h);
		Objects.checkIndex(j, w);
		return pref[(i + 1) * stride + j + 1];
	}

	public long sum(final int i1, final int j1, final int i2, final int j2) {
		Objects.checkIndex(i1, h);
		Objects.checkIndex(j1, w);
		Objects.checkIndex(i2, h);
		Objects.checkIndex(j2, w);
		if (i1 > i2 || j1 > j2) return 0;
		final int p1 = i1 * stride;
		final int p2 = (i2 + 1) * stride;
		return pref[p2 + j2 + 1] - pref[p1 + j2 + 1] - pref[p2 + j1] + pref[p1 + j1];
	}

	public PrimitiveIterator.OfLong iterator() {
		return new PrimitiveIterator.OfLong() {
			private int i = 0, j = 0, k = 0;

			public boolean hasNext() {
				return k < size;
			}

			public long nextLong() {
				if (!hasNext()) throw new NoSuchElementException();
				final long v = get(i, j);
				k++;
				if (++j == w) {
					j = 0;
					i++;
				}
				return v;
			}
		};
	}

	public Spliterator.OfLong spliterator() {
		return Spliterators.spliterator(iterator(), size, Spliterator.ORDERED);
	}

	public String toString() {
		if (isEmpty()) return "";
		final StringBuilder sb = new StringBuilder(21 * size - 1);
		for (int i = 0; i < h; i++) {
			if (i > 0) sb.append('\n');
			sb.append(get(i, 0));
			for (int j = 1; j < w; j++) sb.append(' ').append(get(i, j));
		}
		return sb.toString();
	}

	private void build() {
		for (int i = 1; i <= h; i++) {
			int p = i * stride + 1;
			for (int j = 0; j < w; j++, p++) pref[p] += pref[p - stride] + pref[p - 1] - pref[p - stride - 1];
		}
	}
}
