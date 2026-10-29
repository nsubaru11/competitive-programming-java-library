package lib.ds.fenwick;

import java.util.*;
import java.util.function.*;

import lib.ds.*;

/**
 * int値の区間加算・区間和を O(log n) で行う Binary Indexed Tree。差分と添字で重み付けした差分を2本の BIT で保持する。
 * 添字は0始まりとする。{@code sum(i)} は閉区間 {@code [0, i]}、{@code sum(i, j)} と {@code add(i, j, d)} は閉区間 {@code [i, j]} を対象とし、
 * {@code i = -1}・{@code i > j} は空区間として扱う。範囲外の添字・区間では IndexOutOfBoundsException を投げる。
 * set は更新前の値、単一点の add・multiply・affine・apply は更新後の値を返し、区間の add は値を返さない。
 * affine は {@code 現在値 * m + d}、apply は {@code f(現在値, x)} へ更新する。get は O(log n) である。
 * 各操作後の要素は int の範囲内とし、更新差分と和は long で扱う。
 */
public final class IntRangeBIT implements IntCollection {
	public final int size;
	private final long[] bit1, bit2;
	private long total;

	public IntRangeBIT(final int n) {
		size = n;
		bit1 = new long[n + 1];
		bit2 = new long[n + 1];
	}

	public IntRangeBIT(final int[] a) {
		this(a.length);
		for (int i = 0; i < size; i++) total += bit1[i + 1] = a[i];
		build();
	}

	public IntRangeBIT(final IntCollection a) {
		this(a.size());
		final PrimitiveIterator.OfInt it = a.iterator();
		for (int i = 0; i < size; i++) total += bit1[i + 1] = it.nextInt();
		build();
	}

	public IntRangeBIT(final IntRangeBIT other) {
		size = other.size;
		bit1 = other.bit1.clone();
		bit2 = other.bit2.clone();
		total = other.total;
	}

	public int size() {
		return size;
	}

	public int get(final int i) {
		return (int) prefixSum(bit1, Objects.checkIndex(i, size) + 1);
	}

	public int set(final int i, final int v) {
		final int old = get(i);
		addRange(i, i, (long) v - old);
		return old;
	}

	public int add(final int i, final int d) {
		final int v = get(i) + d;
		addRange(i, i, d);
		return v;
	}

	public void add(final int i, final int j, final int d) {
		if (i > j) return;
		Objects.checkFromToIndex(i, j + 1, size);
		addRange(i, j, d);
	}

	public int multiply(final int i, final int m) {
		final int old = get(i);
		addRange(i, i, old * (m - 1L));
		return old * m;
	}

	public int affine(final int i, final int m, final int d) {
		final int old = get(i);
		addRange(i, i, old * (m - 1L) + d);
		return old * m + d;
	}

	public int apply(final int i, final int x, final IntBinaryOperator f) {
		final int old = get(i), v = f.applyAsInt(old, x);
		addRange(i, i, (long) v - old);
		return v;
	}

	public IntRangeBIT fill(final int v) {
		total = (long) size * v;
		Arrays.fill(bit1, 1, size + 1, v);
		build();
		return this;
	}

	public IntRangeBIT setAll(final IntUnaryOperator init) {
		total = 0;
		for (int i = 0; i < size; i++) total += bit1[i + 1] = init.applyAsInt(i);
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

	public PrimitiveIterator.OfInt iterator() {
		return new PrimitiveIterator.OfInt() {
			private int i = 0;

			public boolean hasNext() {
				return i < size;
			}

			public int nextInt() {
				if (!hasNext()) throw new NoSuchElementException();
				return get(i++);
			}
		};
	}

	public String toString() {
		if (isEmpty()) return "";
		final StringBuilder sb = new StringBuilder(12 * size - 1);
		sb.append(get(0));
		for (int i = 1; i < size; i++) sb.append(' ').append(get(i));
		return sb.toString();
	}

	private void build() {
		for (int i = size; i > 0; i--) {
			bit1[i] -= bit1[i - 1];
			bit2[i] = bit1[i] * (i - 1);
		}
		for (int i = 1; i <= size; i++) {
			final int j = i + (i & -i);
			if (j <= size) {
				bit1[j] += bit1[i];
				bit2[j] += bit2[i];
			}
		}
	}

	private void addRange(final int i, final int j, final long d) {
		total += (j - i + 1L) * d;
		add(bit1, i, d);
		add(bit1, j + 1, -d);
		add(bit2, i, d * i);
		add(bit2, j + 1, -d * (j + 1));
	}

	private void add(final long[] bit, int i, final long v) {
		for (i++; i <= size; i += i & -i) bit[i] += v;
	}

	private long prefixSum(final long[] bit, int r) {
		long s = 0;
		for (; r > 0; r -= r & -r) s += bit[r];
		return s;
	}

	private long prefixSum(final int r) {
		return prefixSum(bit1, r) * r - prefixSum(bit2, r);
	}
}
