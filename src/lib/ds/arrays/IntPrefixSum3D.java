package lib.ds.arrays;

import java.util.*;

import lib.ds.*;
import lib.util.function.*;

/**
 * int値の3次元累積和。構築後は変更できない。座標は0始まりの (i, j, k) とし、各次元の長さは1以上とする。
 * sum(i, j, k) は {@code [0, i] x [0, j] x [0, k]}、sum(i1, j1, k1, i2, j2, k2) は閉直方体 {@code [i1, i2] x [j1, j2] x [k1, k2]} の和を返す。
 * 座標が範囲外なら IndexOutOfBoundsException を投げ、始点が終点を超える軸があれば0を返す。和は long で計算する。
 */
public final class IntPrefixSum3D implements IntCollection {
	public final int d, h, w, size;
	private final int strideI, strideJ;
	private final long[] pref;

	public IntPrefixSum3D(final int d, final int h, final int w, final IntTernaryOperator init) {
		this(d, h, w);
		for (int i = 0; i < d; i++) {
			for (int j = 0; j < h; j++) {
				int p = index(i + 1, j + 1, 1);
				for (int k = 0; k < w; k++, p++) pref[p] = init.applyAsInt(i, j, k);
			}
		}
		build();
	}

	public IntPrefixSum3D(final int[][][] a) {
		this(a.length, a[0].length, a[0][0].length);
		for (int i = 0; i < d; i++) {
			for (int j = 0; j < h; j++) {
				int p = index(i + 1, j + 1, 1);
				for (int k = 0; k < w; k++, p++) pref[p] = a[i][j][k];
			}
		}
		build();
	}

	private IntPrefixSum3D(final int d, final int h, final int w) {
		this.d = d;
		this.h = h;
		this.w = w;
		size = d * h * w;
		strideJ = w + 1;
		strideI = (h + 1) * strideJ;
		pref = new long[(d + 1) * strideI];
	}

	public int size() {
		return size;
	}

	public boolean inRange(final int i, final int j, final int k) {
		return (i | j | k | (d - 1 - i) | (h - 1 - j) | (w - 1 - k)) >= 0;
	}

	public int get(final int i, final int j, final int k) {
		Objects.checkIndex(j, h);
		Objects.checkIndex(k, w);
		final int p = index(i + 1, j + 1, k + 1);
		return (int) (pref[p] - pref[p - strideI] - pref[p - strideJ] - pref[p - 1] + pref[p - strideI - strideJ] + pref[p - strideI - 1] + pref[p - strideJ - 1] - pref[p - strideI - strideJ - 1]);
	}

	public int getOrDefault(final int i, final int j, final int k, final int defaultValue) {
		return inRange(i, j, k) ? get(i, j, k) : defaultValue;
	}

	public long sumAll() {
		return pref[pref.length - 1];
	}

	public long sum(final int i, final int j, final int k) {
		Objects.checkIndex(i, d);
		Objects.checkIndex(j, h);
		Objects.checkIndex(k, w);
		return pref[index(i + 1, j + 1, k + 1)];
	}

	public long sum(final int i1, final int j1, final int k1, final int i2, final int j2, final int k2) {
		Objects.checkIndex(i1, d);
		Objects.checkIndex(j1, h);
		Objects.checkIndex(k1, w);
		Objects.checkIndex(i2, d);
		Objects.checkIndex(j2, h);
		Objects.checkIndex(k2, w);
		if (i1 > i2 || j1 > j2 || k1 > k2) return 0;
		final int i = i2 + 1, j = j2 + 1, k = k2 + 1;
		return pref[index(i, j, k)] - pref[index(i1, j, k)] - pref[index(i, j1, k)] - pref[index(i, j, k1)] + pref[index(i1, j1, k)] + pref[index(i1, j, k1)] + pref[index(i, j1, k1)] - pref[index(i1, j1, k1)];
	}

	public PrimitiveIterator.OfInt iterator() {
		return new PrimitiveIterator.OfInt() {
			private int i = 0, j = 0, k = 0, s = 0;

			public boolean hasNext() {
				return s < size;
			}

			public int nextInt() {
				if (!hasNext()) throw new NoSuchElementException();
				final int v = get(i, j, k);
				s++;
				if (++k == w) {
					k = 0;
					if (++j == h) {
						j = 0;
						i++;
					}
				}
				return v;
			}
		};
	}

	public Spliterator.OfInt spliterator() {
		return Spliterators.spliterator(iterator(), size, Spliterator.ORDERED);
	}

	public String toString() {
		if (isEmpty()) return "";
		final StringBuilder sb = new StringBuilder(12 * size - 1);
		for (int i = 0; i < d; i++) {
			for (int j = 0; j < h; j++) {
				if (i > 0 || j > 0) sb.append('\n');
				sb.append(get(i, j, 0));
				for (int k = 1; k < w; k++) sb.append(' ').append(get(i, j, k));
			}
		}
		return sb.toString();
	}

	private void build() {
		for (int i = 1; i <= d; i++) {
			for (int j = 1; j <= h; j++) {
				int p = index(i, j, 1);
				for (int k = 0; k < w; k++, p++)
					pref[p] += pref[p - strideI] + pref[p - strideJ] + pref[p - 1] - pref[p - strideI - strideJ] - pref[p - strideI - 1] - pref[p - strideJ - 1] + pref[p - strideI - strideJ - 1];
			}
		}
	}

	private int index(final int i, final int j, final int k) {
		return i * strideI + j * strideJ + k;
	}
}
