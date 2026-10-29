package lib.ds.arrays;

import java.util.*;
import java.util.function.*;

/**
 * int値の1次元累積和を保持する。構築後は更新しない。
 * 添字は0始まりとする。{@code sum(i)} は閉区間 {@code [0, i]}、{@code sum(i, j)} は閉区間 {@code [i, j]} の和を返し、
 * {@code i > j} は空区間として0を返す。端点が範囲外の場合は、{@code i > j} でも IndexOutOfBoundsException を投げる。get は元の値を返す。
 * 入力の int 値を long で累積する。init は添字の昇順に1回ずつ呼び、構築時に入力は変更しない。
 */
public final class IntPrefixSum implements IntArray {
	public final int size;
	private final long[] pref;

	public IntPrefixSum(final int n, final IntUnaryOperator init) {
		this(n);
		for (int i = 0; i < n; i++) pref[i + 1] = init.applyAsInt(i);
		build();
	}

	public IntPrefixSum(final int[] a) {
		this(a.length);
		for (int i = 0; i < size; i++) pref[i + 1] = a[i];
		build();
	}

	public IntPrefixSum(final IntArray a) {
		this(a.size());
		for (int i = 0; i < size; i++) pref[i + 1] = a.get(i);
		build();
	}

	private IntPrefixSum(final int n) {
		size = n;
		pref = new long[n + 1];
	}

	public int size() {
		return size;
	}

	public int get(final int i) {
		return (int) (pref[i + 1] - pref[i]);
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
		for (int i = 0; i < size; i++) pref[i + 1] += pref[i];
	}
}
