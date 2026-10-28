package lib.ds.fenwick;

import java.util.*;
import java.util.function.*;

import lib.ds.*;

/**
 * long値の Binary Indexed Tree (Fenwick Tree)。点更新・区間和・累積和の境界探索を O(log n) で行う。
 * 添字は0始まりとする。{@code sum(i)} は閉区間 {@code [0, i]}、{@code sum(i, j)} は閉区間 {@code [i, j]} の和を返し、
 * {@code i = -1}・{@code i > j} は空区間として0を返す。範囲外の添字・区間では IndexOutOfBoundsException を投げる。
 * set は更新前の値、add・multiply・affine・apply は更新後の値を返す。
 * affine は {@code 現在値 * m + d}、apply は {@code f(現在値, x)} へ更新する。
 * 各要素と計算途中の値は long の範囲内とする。
 */
public final class LongBIT implements LongCollection {
	public final int size;
	private final int headBit;
	private final long[] raw, tree;
	private long total;

	public LongBIT(final int n) {
		size = n;
		headBit = Integer.highestOneBit(n);
		raw = new long[n];
		tree = new long[n + 1];
	}

	public LongBIT(final long[] a) {
		this(a.length);
		System.arraycopy(a, 0, raw, 0, size);
		System.arraycopy(a, 0, tree, 1, size);
		for (final long v : a) total += v;
		build();
	}

	public LongBIT(final LongCollection a) {
		this(a.size());
		final PrimitiveIterator.OfLong it = a.iterator();
		for (int i = 0; i < size; i++) total += tree[i + 1] = raw[i] = it.nextLong();
		build();
	}

	public LongBIT(final LongBIT other) {
		size = other.size;
		headBit = other.headBit;
		raw = other.raw.clone();
		tree = other.tree.clone();
		total = other.total;
	}

	public int size() {
		return size;
	}

	public long get(final int i) {
		return raw[i];
	}

	public long set(final int i, final long v) {
		final long old = raw[i];
		add(i, v - old);
		return old;
	}

	public long add(final int i, final long d) {
		raw[i] += d;
		total += d;
		for (int cur = i + 1; cur <= size; cur += cur & -cur) tree[cur] += d;
		return raw[i];
	}

	public long multiply(final int i, final long m) {
		return add(i, raw[i] * (m - 1));
	}

	public long affine(final int i, final long m, final long d) {
		return add(i, raw[i] * (m - 1) + d);
	}

	public long apply(final int i, final long x, final LongBinaryOperator f) {
		return add(i, f.applyAsLong(raw[i], x) - raw[i]);
	}

	public LongBIT fill(final long v) {
		total = size * v;
		for (int i = 0; i < size; i++) tree[i + 1] = raw[i] = v;
		build();
		return this;
	}

	public LongBIT setAll(final IntToLongFunction init) {
		total = 0;
		for (int i = 0; i < size; i++) total += tree[i + 1] = raw[i] = init.applyAsLong(i);
		build();
		return this;
	}

	public long sum(final int i) {
		return prefixSum(Objects.checkIndex(i + 1, size + 1));
	}

	public long sum(final int i, final int j) {
		if (i > j) return 0;
		Objects.checkFromToIndex(i, j + 1, size);
		return prefixSum(j + 1) - prefixSum(i);
	}

	public long sumAll() {
		return total;
	}

	/**
	 * 累積和が w 以上となる最小の添字を返す。全要素が非負であることを前提とする。
	 * {@code w <= 0} は0、該当する添字がなければ size を返す。
	 */
	public int lowerBound(long w) {
		if (w <= 0) return 0;
		int i = 0;
		for (int k = headBit; k > 0; k >>= 1) {
			if (i + k <= size && tree[i + k] < w) {
				w -= tree[i + k];
				i += k;
			}
		}
		return i;
	}

	/**
	 * 累積和が w より大きくなる最小の添字を返す。全要素が非負であることを前提とする。
	 * {@code w < 0} は0、該当する添字がなければ size を返す。
	 */
	public int upperBound(long w) {
		if (w < 0) return 0;
		int i = 0;
		for (int k = headBit; k > 0; k >>= 1) {
			if (i + k <= size && tree[i + k] <= w) {
				w -= tree[i + k];
				i += k;
			}
		}
		return i;
	}

	public long[] toArray() {
		return raw.clone();
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
		sb.append(raw[0]);
		for (int i = 1; i < size; i++) sb.append(' ').append(raw[i]);
		return sb.toString();
	}

	private void build() {
		for (int i = 1; i <= size; i++) {
			final int j = i + (i & -i);
			if (j <= size) tree[j] += tree[i];
		}
	}

	private long prefixSum(int r) {
		long s = 0;
		for (; r > 0; r -= r & -r) s += tree[r];
		return s;
	}
}
