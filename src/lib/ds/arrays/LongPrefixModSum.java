package lib.ds.arrays;

import java.util.*;
import java.util.function.*;

/**
 * long値の1次元累積和を法 mod で保持する。構築後は更新しない。法は正、入力値は {@code 0 <= v < mod} とし、get・sum の結果は {@code [0, mod)} とする。
 * 添字は0始まりとする。{@code sum(i)} は閉区間 {@code [0, i]}、{@code sum(i, j)} は閉区間 {@code [i, j]} の和を返し、
 * 範囲外の添字や {@code i > j} では IndexOutOfBoundsException を投げる。get は元の値を返す。
 * init は添字の昇順に1回ずつ呼び、構築時に入力は変更しない。
 */
public final class LongPrefixModSum implements LongArray {
	public final int size;
	public final long mod;
	private final long[] pref;

	public LongPrefixModSum(final int n, final long mod, final IntToLongFunction init) {
		this(n, mod);
		for (int i = 0; i < n; i++) pref[i + 1] = init.applyAsLong(i);
		build();
	}

	public LongPrefixModSum(final long[] a, final long mod) {
		this(a.length, mod);
		if (size >= 0) System.arraycopy(a, 0, pref, 1, size);
		build();
	}

	public LongPrefixModSum(final LongArray a, final long mod) {
		this(a.size(), mod);
		for (int i = 0; i < size; i++) pref[i + 1] = a.get(i);
		build();
	}

	private LongPrefixModSum(final int n, final long mod) {
		size = n;
		this.mod = mod;
		pref = new long[n + 1];
	}

	public int size() {
		return size;
	}

	public long get(final int i) {
		final long res = pref[i + 1] - pref[i];
		return res < 0 ? res + mod : res;
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
		final long res = pref[j + 1] - pref[i];
		return res < 0 ? res + mod : res;
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
		final StringBuilder sb = new StringBuilder(20 * size - 1);
		sb.append(get(0));
		for (int i = 1; i < size; i++) sb.append(' ').append(get(i));
		return sb.toString();
	}

	private void build() {
		for (int i = 0; i < size; i++) {
			final long v = pref[i] + pref[i + 1];
			pref[i + 1] = v >= mod ? v - mod : v;
		}
	}
}
