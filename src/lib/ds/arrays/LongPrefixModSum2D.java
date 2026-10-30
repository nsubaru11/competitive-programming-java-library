package lib.ds.arrays;

import java.util.*;

import lib.ds.*;
import lib.util.function.*;

/**
 * long値の2次元累積和を法 mod で保持する。構築後は変更できない。座標は0始まりの (行 i, 列 j) とし、各次元の長さは1以上とする。
 * sum(i, j) は {@code [0, i] x [0, j]}、sum(i1, j1, i2, j2) は閉矩形 {@code [i1, i2] x [j1, j2]} の和を返す。
 * 座標が範囲外なら IndexOutOfBoundsException を投げ、始点が終点を超える軸があれば0を返す。
 * mod は正、入力値は {@code [0, mod)} とし、結果も {@code [0, mod)} で返す。
 */
public final class LongPrefixModSum2D implements LongCollection {
	public final int h, w, size;
	public final long mod;
	private final int stride;
	private final long[] pref;

	public LongPrefixModSum2D(final int h, final int w, final long mod, final IntBinaryToLongFunction init) {
		this(h, w, mod);
		for (int i = 0; i < h; i++) {
			int p = (i + 1) * stride + 1;
			for (int j = 0; j < w; j++, p++) pref[p] = init.applyAsLong(i, j);
		}
		build();
	}

	public LongPrefixModSum2D(final long[][] a, final long mod) {
		this(a.length, a[0].length, mod);
		for (int i = 0; i < h; i++) {
			int p = (i + 1) * stride + 1;
			for (int j = 0; j < w; j++, p++) pref[p] = a[i][j];
		}
		build();
	}

	private LongPrefixModSum2D(final int h, final int w, final long mod) {
		this.h = h;
		this.w = w;
		this.mod = mod;
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
		final long res = (pref[p] - pref[p - stride] - pref[p - 1] + pref[p - stride - 1]) % mod;
		return res < 0 ? res + mod : res;
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
		final long res = (pref[p2 + j2 + 1] - pref[p1 + j2 + 1] - pref[p2 + j1] + pref[p1 + j1]) % mod;
		return res < 0 ? res + mod : res;
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
		final StringBuilder sb = new StringBuilder(20 * size - 1);
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
			for (int j = 0; j < w; j++, p++) {
				final long v = (pref[p] + pref[p - stride] + pref[p - 1] - pref[p - stride - 1]) % mod;
				pref[p] = v < 0 ? v + mod : v;
			}
		}
	}
}
