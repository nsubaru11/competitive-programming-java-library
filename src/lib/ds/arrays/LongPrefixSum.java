package lib.ds.arrays;

import java.util.*;
import java.util.function.*;

import lib.ds.*;

/**
 * long値の1次元累積和。構築後は変更できない。添字は0始まりとし、get は元の値を返す。
 * sum(i) は {@code [0, i]}、sum(i, j) は閉区間 {@code [i, j]} の和を返す。
 * 端点が範囲外なら IndexOutOfBoundsException を投げ、{@code i > j} の空区間は0を返す。和は long の範囲に収まるものとする。
 */
public final class LongPrefixSum implements LongArray {
	public final int size;
	private final long[] pref;

	public LongPrefixSum(final int n, final IntToLongFunction init) {
		this(n);
		for (int i = 0; i < n; i++) pref[i + 1] = init.applyAsLong(i);
		build();
	}

	public LongPrefixSum(final long[] a) {
		this(a.length);
		System.arraycopy(a, 0, pref, 1, size);
		build();
	}

	public LongPrefixSum(final LongCollection a) {
		this(a.size());
		final PrimitiveIterator.OfLong it = a.iterator();
		for (int i = 0; i < size; i++) pref[i + 1] = it.nextLong();
		build();
	}

	private LongPrefixSum(final int n) {
		size = n;
		pref = new long[n + 1];
	}

	public int size() {
		return size;
	}

	public long get(final int i) {
		return pref[i + 1] - pref[i];
	}

	public long sumAll() {
		return pref[size];
	}

	public long sum(final int i) {
		Objects.checkIndex(i, size);
		return pref[i + 1];
	}

	public long sum(final int i, final int j) {
		Objects.checkIndex(i, size);
		Objects.checkIndex(j, size);
		if (i > j) return 0;
		return pref[j + 1] - pref[i];
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
		for (int i = 0; i < size; i++) pref[i + 1] += pref[i];
	}
}
