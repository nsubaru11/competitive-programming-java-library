package lib.ds.arrays;

import java.util.*;
import java.util.function.*;

public final class IntPrefixSum implements IntArray {
	public final int length;
	private final int[] pref;

	public IntPrefixSum(final int n, final IntUnaryOperator init) {
		length = n;
		pref = new int[length + 1];
		for (int i = 0; i < length; i++) pref[i + 1] = pref[i] + init.applyAsInt(i);
	}

	public IntPrefixSum(final int[] a) {
		length = a.length;
		pref = new int[length + 1];
		for (int i = 0; i < length; i++) pref[i + 1] = pref[i] + a[i];
	}

	public IntPrefixSum(final IntArray a) {
		length = a.size();
		pref = new int[length + 1];
		for (int i = 0; i < length; i++) pref[i + 1] = pref[i] + a.get(i);
	}

	public static IntPrefixSum generate(final int n, final IntSupplier init) {
		return new IntPrefixSum(n, _ -> init.getAsInt());
	}

	public int get(final int i) {
		return pref[i + 1] - pref[i];
	}

	public int sum() {
		return pref[length];
	}

	public int sum(final int i) {
		return pref[i + 1];
	}

	public int sum(final int i, final int j) {
		return pref[j + 1] - pref[i];
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
		final StringBuilder sb = new StringBuilder(12 * length - 1);
		sb.append(get(0));
		for (int i = 1; i < length; i++) sb.append(' ').append(get(i));
		return sb.toString();
	}
}
