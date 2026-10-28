package lib.ds.fenwick;

import java.util.*;
import java.util.function.*;

import lib.ds.*;

/**
 * long値の区間加算・区間和を O(log n) で行う Binary Indexed Tree。差分と添字で重み付けした差分を2本の BIT で保持する。
 * 添字は0始まりとする。{@code sum(i)} は閉区間 {@code [0, i]}、{@code sum(i, j)} と {@code add(i, j, d)} は閉区間 {@code [i, j]} を対象とし、
 * {@code i = -1}・{@code i > j} は空区間として扱う。範囲外の添字・区間では IndexOutOfBoundsException を投げる。
 * set は更新前の値を返し、add・multiply・affine・apply は値を返さない。
 * affine は {@code 現在値 * m + d}、apply は {@code f(現在値, x)} へ更新する。get は O(log n) である。
 * 各要素と計算途中の値は long の範囲内とする。
 */
public final class LongRangeBIT implements LongCollection {
	public final int size;
	private final long[] bit1, bit2;
	private long total;

	public LongRangeBIT(final int n) {
		size = n;
		bit1 = new long[n + 1];
		bit2 = new long[n + 1];
	}

	public LongRangeBIT(final long[] a) {
		this(a.length);
		System.arraycopy(a, 0, bit1, 1, size);
		for (final long v : a) total += v;
		build();
	}

	public LongRangeBIT(final LongCollection a) {
		this(a.size());
		final PrimitiveIterator.OfLong it = a.iterator();
		for (int i = 0; i < size; i++) total += bit1[i + 1] = it.nextLong();
		build();
	}

	public LongRangeBIT(final LongRangeBIT other) {
		size = other.size;
		bit1 = other.bit1.clone();
		bit2 = other.bit2.clone();
		total = other.total;
	}

	public int size() {
		return size;
	}

	public long get(final int i) {
		return prefixSum(bit1, Objects.checkIndex(i, size) + 1);
	}

	public long set(final int i, final long v) {
		final long old = get(i);
		addRange(i, i, v - old);
		return old;
	}

	public void add(final int i, final long d) {
		Objects.checkIndex(i, size);
		addRange(i, i, d);
	}

	public void add(final int i, final int j, final long d) {
		if (i > j) return;
		Objects.checkFromToIndex(i, j + 1, size);
		addRange(i, j, d);
	}

	public void multiply(final int i, final long m) {
		addRange(i, i, get(i) * (m - 1));
	}

	public void affine(final int i, final long m, final long d) {
		addRange(i, i, get(i) * (m - 1) + d);
	}

	public void apply(final int i, final long x, final LongBinaryOperator f) {
		final long old = get(i);
		addRange(i, i, f.applyAsLong(old, x) - old);
	}

	public LongRangeBIT fill(final long v) {
		total = size * v;
		Arrays.fill(bit1, 1, size + 1, v);
		build();
		return this;
	}

	public LongRangeBIT setAll(final IntToLongFunction init) {
		total = 0;
		for (int i = 0; i < size; i++) total += bit1[i + 1] = init.applyAsLong(i);
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

	public PrimitiveIterator.OfLong iterator() {
		return new PrimitiveIterator.OfLong() {
			private int i = 0;

			public boolean hasNext() {
				return i < size;
			}

			public long nextLong() {
				if (!hasNext()) throw new NoSuchElementException();
				return get(i++);
			}
		};
	}

	public String toString() {
		if (isEmpty()) return "";
		final StringBuilder sb = new StringBuilder(21 * size - 1);
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
