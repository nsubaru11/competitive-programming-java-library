package lib.ds.fenwick;

import java.util.*;
import java.util.function.*;

import lib.ds.*;
import lib.util.function.*;

/**
 * long値の2次元 Binary Indexed Tree (Fenwick Tree)。点更新と矩形和を O(log h log w) で行う。
 * 座標は0始まりの行 i・列 j とする。{@code sum(i, j)} は閉矩形 {@code [0, i] x [0, j]}、
 * {@code sum(i1, j1, i2, j2)} は閉矩形 {@code [i1, i2] x [j1, j2]} の和を返す。
 * 終端 -1 や始点が終点を超える区間は空領域として0を返す。範囲外の座標・区間では IndexOutOfBoundsException を投げる。
 * set は更新前の値、add・multiply・affine・apply は更新後の値を返す。
 * affine は {@code 現在値 * m + d}、apply は {@code f(現在値, x)} へ更新する。
 * 各要素と計算途中の値は long の範囲内とする。走査は行優先順とする。
 */
public final class LongBIT2D implements LongCollection {
	public final int h, w, size;
	private final long[] raw, tree;
	private long total;

	public LongBIT2D(final int h, final int w) {
		this.h = h;
		this.w = w;
		size = h * w;
		raw = new long[size];
		tree = new long[(h + 1) * (w + 1)];
	}

	public LongBIT2D(final long[][] a) {
		this(a.length, a[0].length);
		final int w1 = w + 1;
		for (int i = 0, p = 0; i < h; i++) {
			for (int j = 0; j < w; j++, p++) {
				total += tree[(i + 1) * w1 + (j + 1)] = raw[p] = a[i][j];
			}
		}
		build();
	}

	public LongBIT2D(final LongBIT2D other) {
		h = other.h;
		w = other.w;
		size = other.size;
		raw = other.raw.clone();
		tree = other.tree.clone();
		total = other.total;
	}

	public int size() {
		return size;
	}

	public long get(final int i, final int j) {
		return raw[checkedIndex(i, j)];
	}

	public long set(final int i, final int j, final long v) {
		final int p = checkedIndex(i, j);
		final long old = raw[p];
		add(i, j, p, v - old);
		return old;
	}

	public long add(final int i, final int j, final long d) {
		return add(i, j, checkedIndex(i, j), d);
	}

	public long multiply(final int i, final int j, final long m) {
		final int p = checkedIndex(i, j);
		return add(i, j, p, raw[p] * (m - 1));
	}

	public long affine(final int i, final int j, final long m, final long d) {
		final int p = checkedIndex(i, j);
		return add(i, j, p, raw[p] * (m - 1) + d);
	}

	public long apply(final int i, final int j, final long x, final LongBinaryOperator f) {
		final int p = checkedIndex(i, j);
		return add(i, j, p, f.applyAsLong(raw[p], x) - raw[p]);
	}

	public LongBIT2D fill(final long v) {
		total = size * v;
		final int w1 = w + 1;
		for (int i = 0, p = 0; i < h; i++) {
			for (int j = 0; j < w; j++, p++) {
				tree[(i + 1) * w1 + (j + 1)] = raw[p] = v;
			}
		}
		build();
		return this;
	}

	public LongBIT2D setAll(final IntBinaryToLongFunction init) {
		total = 0;
		final int w1 = w + 1;
		for (int i = 0, p = 0; i < h; i++) {
			for (int j = 0; j < w; j++, p++) {
				total += tree[(i + 1) * w1 + (j + 1)] = raw[p] = init.applyAsLong(i, j);
			}
		}
		build();
		return this;
	}

	public long sum(final int i, final int j) {
		Objects.checkIndex(i + 1, h + 1);
		Objects.checkIndex(j + 1, w + 1);
		return prefixSum(i + 1, j + 1);
	}

	public long sum(final int i1, final int j1, final int i2, final int j2) {
		if (i1 > i2 || j1 > j2) return 0;
		Objects.checkFromToIndex(i1, i2 + 1, h);
		Objects.checkFromToIndex(j1, j2 + 1, w);
		return prefixSum(i2 + 1, j2 + 1) - prefixSum(i1, j2 + 1) - prefixSum(i2 + 1, j1) + prefixSum(i1, j1);
	}

	public long sumAll() {
		return total;
	}

	public long[] toArray() {
		return raw.clone();
	}

	public long[][] toArray2D() {
		final long[][] res = new long[h][w];
		for (int i = 0; i < h; i++) System.arraycopy(raw, i * w, res[i], 0, w);
		return res;
	}

	public void forEachLong(final LongConsumer action) {
		for (int i = 0; i < size; i++) action.accept(raw[i]);
	}

	public PrimitiveIterator.OfLong iterator() {
		return new PrimitiveIterator.OfLong() {
			private int i = 0;

			public boolean hasNext() {
				return i < size;
			}

			public long nextLong() {
				if (!hasNext()) throw new NoSuchElementException();
				return raw[i++];
			}
		};
	}

	public String toString() {
		if (isEmpty()) return "";
		final StringBuilder sb = new StringBuilder(21 * size - 1);
		for (int i = 0, p = 0; i < h; i++) {
			if (i > 0) sb.append('\n');
			sb.append(raw[p++]);
			for (int j = 1; j < w; j++) sb.append(' ').append(raw[p++]);
		}
		return sb.toString();
	}

	private void build() {
		final int w1 = w + 1;
		for (int i = 1; i <= h; i++) {
			final int p = i * w1;
			for (int j = 1; j <= w; j++) {
				final int nj = j + (j & -j);
				if (nj <= w) tree[p + nj] += tree[p + j];
			}
		}
		for (int i = 1; i <= h; i++) {
			final int ni = i + (i & -i);
			if (ni > h) continue;
			final int p = i * w1, q = ni * w1;
			for (int j = 1; j <= w; j++) tree[q + j] += tree[p + j];
		}
	}

	private long add(final int i, final int j, final int p, final long d) {
		raw[p] += d;
		total += d;
		final int w1 = w + 1;
		for (int ci = i + 1; ci <= h; ci += ci & -ci) {
			final int q = ci * w1;
			for (int cj = j + 1; cj <= w; cj += cj & -cj) tree[q + cj] += d;
		}
		return raw[p];
	}

	private int checkedIndex(final int i, final int j) {
		return i * w + Objects.checkIndex(j, w);
	}

	private long prefixSum(final int i, final int j) {
		long s = 0;
		final int w1 = w + 1;
		for (int ci = i; ci > 0; ci -= ci & -ci) {
			final int p = ci * w1;
			for (int cj = j; cj > 0; cj -= cj & -cj) s += tree[p + cj];
		}
		return s;
	}
}
