package lib.ds.arrays;

import java.util.*;
import java.util.function.*;

public final class LongPrefixSum implements LongArray {
	public final int length;
	private final long[] pref;

	public LongPrefixSum(final int n, final IntToLongFunction init) {
		length = n;
		pref = new long[length + 1];
		for (int i = 0; i < length; i++) pref[i + 1] = pref[i] + init.applyAsLong(i);
	}

	public LongPrefixSum(final long[] a) {
		length = a.length;
		pref = new long[length + 1];
		for (int i = 0; i < length; i++) pref[i + 1] = pref[i] + a[i];
	}

	public LongPrefixSum(final LongArray a) {
		length = a.size();
		pref = new long[length + 1];
		for (int i = 0; i < length; i++) pref[i + 1] = pref[i] + a.get(i);
	}

	public static LongPrefixSum generate(final int n, final LongSupplier init) {
		return new LongPrefixSum(n, _ -> init.getAsLong());
	}

	public long get(final int i) {
		return pref[i + 1] - pref[i];
	}

	public long sum() {
		return pref[length];
	}

	public long sum(final int i) {
		return pref[i + 1];
	}

	public long sum(final int i, final int j) {
		return pref[j + 1] - pref[i];
	}

	public int size() {
		return length;
	}

	public PrimitiveIterator.OfLong iterator() {
		return new PrimitiveIterator.OfLong() {
			private int i = 0;

			public long nextLong() {
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
		final StringBuilder sb = new StringBuilder(21 * length - 1);
		sb.append(get(0));
		for (int i = 1; i < length; i++) sb.append(' ').append(get(i));
		return sb.toString();
	}
}
