package lib.ds.arrays;

import java.util.*;
import java.util.function.*;

import lib.ds.*;

/**
 * int値の1次元累積和を法 mod で保持する。構築後は変更できない。添字は0始まりとし、get は元の値を返す。
 * sum(i) は {@code [0, i]}、sum(i, j) は閉区間 {@code [i, j]} の和を返す。
 * 端点が範囲外なら IndexOutOfBoundsException を投げ、{@code i > j} の空区間は0を返す。
 * mod は正、入力値は {@code [0, mod)} とし、結果も {@code [0, mod)} で返す。
 */
public final class IntPrefixModSum implements IntArray {
	public final int size, mod;
	private final int[] pref;

	public IntPrefixModSum(final int n, final int mod, final IntUnaryOperator init) {
		this(n, mod);
		for (int i = 0; i < n; i++) pref[i + 1] = init.applyAsInt(i);
		build();
	}

	public IntPrefixModSum(final int[] a, final int mod) {
		this(a.length, mod);
		System.arraycopy(a, 0, pref, 1, size);
		build();
	}

	public IntPrefixModSum(final IntCollection a, final int mod) {
		this(a.size(), mod);
		final PrimitiveIterator.OfInt it = a.iterator();
		for (int i = 0; i < size; i++) pref[i + 1] = it.nextInt();
		build();
	}

	private IntPrefixModSum(final int n, final int mod) {
		size = n;
		this.mod = mod;
		pref = new int[n + 1];
	}

	public int size() {
		return size;
	}

	public int get(final int i) {
		final int res = pref[i + 1] - pref[i];
		return res < 0 ? res + mod : res;
	}

	public int sumAll() {
		return pref[size];
	}

	public int sum(final int i) {
		Objects.checkIndex(i, size);
		return pref[i + 1];
	}

	public int sum(final int i, final int j) {
		Objects.checkIndex(i, size);
		Objects.checkIndex(j, size);
		if (i > j) return 0;
		final int res = pref[j + 1] - pref[i];
		return res < 0 ? res + mod : res;
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
		final StringBuilder sb = new StringBuilder(11 * size - 1);
		sb.append(get(0));
		for (int i = 1; i < size; i++) sb.append(' ').append(get(i));
		return sb.toString();
	}

	private void build() {
		for (int i = 0; i < size; i++) {
			final int v = pref[i] + pref[i + 1];
			pref[i + 1] = v >= mod ? v - mod : v;
		}
	}
}
