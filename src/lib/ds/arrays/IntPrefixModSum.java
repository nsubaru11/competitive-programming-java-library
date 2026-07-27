package lib.ds.arrays;

import java.util.*;
import java.util.function.*;

public final class IntPrefixModSum implements IntArray {
	public final int length, mod;
	private final int[] pref;

	public IntPrefixModSum(final int n, final int mod, final IntUnaryOperator init) {
		length = n;
		this.mod = mod;
		pref = new int[length + 1];
		for (int i = 0; i < length; i++) {
			int v = pref[i] + init.applyAsInt(i);
			pref[i + 1] = v >= mod ? v - mod : v;
		}
	}

	public IntPrefixModSum(final int[] a, final int mod) {
		length = a.length;
		this.mod = mod;
		pref = new int[length + 1];
		for (int i = 0; i < length; i++) {
			int v = pref[i] + a[i];
			pref[i + 1] = v >= mod ? v - mod : v;
		}
	}

	public IntPrefixModSum(final IntArray a, final int mod) {
		length = a.size();
		this.mod = mod;
		pref = new int[length + 1];
		for (int i = 0; i < length; i++) {
			int v = pref[i] + a.get(i);
			pref[i + 1] = v >= mod ? v - mod : v;
		}
	}

	public static IntPrefixModSum generate(final int n, final int mod, final IntSupplier init) {
		return new IntPrefixModSum(n, mod, _ -> init.getAsInt());
	}

	public int get(final int i) {
		int res = pref[i + 1] - pref[i];
		return res < 0 ? res + mod : res;
	}

	public int sum() {
		return pref[length];
	}

	public int sum(final int i) {
		return pref[i + 1];
	}

	public int sum(final int i, final int j) {
		int res = pref[j + 1] - pref[i];
		return res < 0 ? res + mod : res;
	}

	public int size() {
		return length;
	}

	public PrimitiveIterator.OfInt iterator() {
		return new PrimitiveIterator.OfInt() {
			private int i = 0;

			public int nextInt() {
				return get(i++);
			}

			public boolean hasNext() {
				return i < length;
			}
		};
	}

	/**
	 * 元の要素を半角スペースで区切った文字列として返します。
	 */
	public String toString() {
		final StringBuilder sb = new StringBuilder(11 * length - 1);
		sb.append(get(0));
		for (int i = 1; i < length; i++) sb.append(' ').append(get(i));
		return sb.toString();
	}
}
