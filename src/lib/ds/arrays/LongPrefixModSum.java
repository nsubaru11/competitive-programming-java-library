package lib.ds.arrays;

import java.util.*;
import java.util.function.*;

public final class LongPrefixModSum implements LongArray {
	public final int length;
	public final long mod;
	private final long[] pref;

	public LongPrefixModSum(final int n, final long mod, final IntToLongFunction init) {
		length = n;
		this.mod = mod;
		pref = new long[length + 1];
		for (int i = 0; i < length; i++) {
			long v = pref[i] + init.applyAsLong(i);
			pref[i + 1] = v >= mod ? v - mod : v;
		}
	}

	public LongPrefixModSum(final long[] a, final long mod) {
		length = a.length;
		this.mod = mod;
		pref = new long[length + 1];
		for (int i = 0; i < length; i++) {
			long v = pref[i] + a[i];
			pref[i + 1] = v >= mod ? v - mod : v;
		}
	}

	public LongPrefixModSum(final LongArray a, final long mod) {
		length = a.size();
		this.mod = mod;
		pref = new long[length + 1];
		for (int i = 0; i < length; i++) {
			long v = pref[i] + a.get(i);
			pref[i + 1] = v >= mod ? v - mod : v;
		}
	}

	public static LongPrefixModSum generate(final int n, final long mod, final LongSupplier init) {
		return new LongPrefixModSum(n, mod, _ -> init.getAsLong());
	}

	public long get(final int i) {
		long res = pref[i + 1] - pref[i];
		return res < 0 ? res + mod : res;
	}

	public long sum() {
		return pref[length];
	}

	public long sum(final int i) {
		return pref[i + 1];
	}

	public long sum(final int i, final int j) {
		long res = pref[j + 1] - pref[i];
		return res < 0 ? res + mod : res;
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
		final StringBuilder sb = new StringBuilder(20 * length - 1);
		sb.append(get(0));
		for (int i = 1; i < length; i++) sb.append(' ').append(get(i));
		return sb.toString();
	}
}
