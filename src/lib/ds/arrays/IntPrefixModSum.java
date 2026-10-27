package lib.ds.arrays;

import java.util.*;
import java.util.function.*;

/**
 * int値の1次元累積和を法 mod で保持する。構築後は更新しない。法は正、入力値は {@code 0 <= v < mod} とし、get・sum の結果は {@code [0, mod)} とする。
 * 添字は0始まりとする。{@code sum(i)} は閉区間 {@code [0, i]}、{@code sum(i, j)} は閉区間 {@code [i, j]} の和を返し、
 * 範囲外の添字や {@code i > j} では IndexOutOfBoundsException を投げる。get は元の値を返す。
 * init は添字の昇順に1回ずつ呼び、構築時に入力は変更しない。
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
		if (size >= 0) System.arraycopy(a, 0, pref, 1, size);
		build();
	}

	public IntPrefixModSum(final IntArray a, final int mod) {
		this(a.size(), mod);
		for (int i = 0; i < size; i++) pref[i + 1] = a.get(i);
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

	public int sum() {
		return pref[size];
	}

	public int sum(final int i) {
		Objects.checkIndex(i, size);
		return pref[i + 1];
	}

	public int sum(final int i, final int j) {
		Objects.checkIndex(i, j + 1);
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
