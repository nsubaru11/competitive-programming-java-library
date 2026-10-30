package lib.ds.fenwick;

import java.util.*;
import java.util.function.*;

import lib.ds.*;

/**
 * int値の Binary Indexed Tree (Fenwick Tree)。点更新・区間和・累積和の二分探索を O(log n) で行う。
 * 添字は0始まり、sum(i) は {@code [0, i]}、sum(i, j) は閉区間 {@code [i, j]} の和を返す。
 * 端点が範囲外なら IndexOutOfBoundsException を投げ、{@code i > j} の空区間は0を返す。
 * set は更新前の値、add・multiply・affine・apply は更新後の値を返す。affine は {@code 現在値 * m + d}、apply は {@code f(現在値, x)} へ更新する。
 * 要素は int の範囲に収まるものとし、和は long で返す。
 */
public final class IntBIT implements IntCollection {
	public final int size;
	private final int headBit;
	private final int[] raw;
	private final long[] tree;
	private long total;

	public IntBIT(final int n) {
		size = n;
		headBit = Integer.highestOneBit(n);
		raw = new int[n];
		tree = new long[n + 1];
	}

	public IntBIT(final int[] a) {
		this(a.length);
		for (int i = 0; i < size; i++) total += tree[i + 1] = raw[i] = a[i];
		build();
	}

	public IntBIT(final IntCollection a) {
		this(a.size());
		final PrimitiveIterator.OfInt it = a.iterator();
		for (int i = 0; i < size; i++) total += tree[i + 1] = raw[i] = it.nextInt();
		build();
	}

	public IntBIT(final IntBIT other) {
		size = other.size;
		headBit = other.headBit;
		raw = other.raw.clone();
		tree = other.tree.clone();
		total = other.total;
	}

	public int size() {
		return size;
	}

	public int get(final int i) {
		return raw[i];
	}

	public int set(final int i, final int v) {
		final int old = raw[i];
		add(i, (long) v - old);
		return old;
	}

	public int add(final int i, final int d) {
		return add(i, (long) d);
	}

	public int multiply(final int i, final int m) {
		return add(i, raw[i] * (m - 1L));
	}

	public int affine(final int i, final int m, final int d) {
		return add(i, raw[i] * (m - 1L) + d);
	}

	public int apply(final int i, final int x, final IntBinaryOperator f) {
		return add(i, (long) f.applyAsInt(raw[i], x) - raw[i]);
	}

	public IntBIT fill(final int v) {
		total = (long) size * v;
		for (int i = 0; i < size; i++) tree[i + 1] = raw[i] = v;
		build();
		return this;
	}

	public IntBIT setAll(final IntUnaryOperator init) {
		total = 0;
		for (int i = 0; i < size; i++) total += tree[i + 1] = raw[i] = init.applyAsInt(i);
		build();
		return this;
	}

	public long sum(final int i) {
		return prefixSum(Objects.checkIndex(i, size) + 1);
	}

	public long sum(final int i, final int j) {
		Objects.checkIndex(i, size);
		Objects.checkIndex(j, size);
		if (i > j) return 0;
		return prefixSum(j + 1) - prefixSum(i);
	}

	public long sumAll() {
		return total;
	}

	/**
	 * 累積和が w 以上となる最小の添字を返す。全要素が非負であることを前提とする。
	 * {@code w <= 0} なら0、該当する添字がなければ size を返す。
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
	 * 累積和が w を超える最小の添字を返す。全要素が非負であることを前提とする。
	 * {@code w < 0} なら0、該当する添字がなければ size を返す。
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

	public int[] toArray() {
		return raw.clone();
	}

	public void forEachInt(final IntConsumer action) {
		for (int i = 0; i < size; i++) action.accept(raw[i]);
	}

	public PrimitiveIterator.OfInt iterator() {
		return new PrimitiveIterator.OfInt() {
			private int i = 0;

			public boolean hasNext() {
				return i < size;
			}

			public int nextInt() {
				if (!hasNext()) throw new NoSuchElementException();
				return raw[i++];
			}
		};
	}

	public String toString() {
		if (isEmpty()) return "";
		final StringBuilder sb = new StringBuilder(12 * size - 1);
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

	private int add(final int i, final long d) {
		raw[i] += (int) d;
		total += d;
		for (int cur = i + 1; cur <= size; cur += cur & -cur) tree[cur] += d;
		return raw[i];
	}

	private long prefixSum(int r) {
		long s = 0;
		for (; r > 0; r -= r & -r) s += tree[r];
		return s;
	}
}
