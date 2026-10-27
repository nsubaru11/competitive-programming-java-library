package lib.ds.arrays;

import java.util.*;
import java.util.function.*;

/**
 * long値の1次元累積和を保持する。構築後は更新しない。
 * 添字は0始まりとする。{@code sum(i)} は閉区間 {@code [0, i]}、{@code sum(i, j)} は閉区間 {@code [i, j]} の和を返し、
 * 範囲外の添字や {@code i > j} では IndexOutOfBoundsException を投げる。get は元の値を返す。
 * 各要素と和は long の範囲内とする。init は添字の昇順に1回ずつ呼び、構築時に入力は変更しない。
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
		if (size >= 0) System.arraycopy(a, 0, pref, 1, size);
		build();
	}

	public LongPrefixSum(final LongArray a) {
		this(a.size());
		for (int i = 0; i < size; i++) pref[i + 1] = a.get(i);
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

	public long sum() {
		return pref[size];
	}

	public long sum(final int i) {
		Objects.checkIndex(i, size);
		return pref[i + 1];
	}

	public long sum(final int i, final int j) {
		Objects.checkIndex(i, j + 1);
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
