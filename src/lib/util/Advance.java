package lib.util;

import java.util.*;

@SuppressWarnings("unused")
public final class Advance {

	/* -------------- pow -------------- */

	/**
	 * long値で返します。
	 */
	public static long pow(long n, int k) {
		long pow = 1;
		for (; k > 0; k >>= 1, n *= n) {
			if ((k & 1) == 1) pow *= n;
		}
		return pow;
	}

	/* -------------- modPow -------------- */

	/**
	 * n ^ k % mod
	 */
	public static long modPow(long n, long k, final long mod) {
		long modPow = 1;
		for (n %= mod; k > 0; k >>= 1, n = n * n % mod) {
			if ((k & 1) == 1) modPow = modPow * n % mod;
		}
		return modPow;
	}

	/* -------------- gcd -------------- */

	/**
	 * 最大公約数
	 */
	public static int gcd(final int a, final int b) {
		return b > 0 ? gcd(b, a % b) : a;
	}

	/**
	 * 最大公約数
	 */
	public static long gcd(final long a, final long b) {
		return b > 0 ? gcd(b, a % b) : a;
	}

	/**
	 * 最大公約数
	 */
	public static int gcd(final int[] arr) {
		int iGcd = arr[0];
		for (int i = 1, len = arr.length; i < len; i++) {
			iGcd = gcd(iGcd, arr[i]);
		}
		return iGcd;
	}

	/**
	 * 最大公約数
	 */
	public static long gcd(final long[] arr) {
		long lGcd = arr[0];
		for (int i = 1, len = arr.length; i < len; i++) {
			lGcd = gcd(lGcd, arr[i]);
		}
		return lGcd;
	}

	/* -------------- lcm -------------- */

	/**
	 * 最小公倍数
	 */
	public static long lcm(final int a, final int b) {
		return (long) a / gcd(a, b) * b;
	}

	/**
	 * 最小公倍数
	 */
	public static long lcm(final long a, final long b) {
		return a / gcd(a, b) * b;
	}

	/**
	 * 最小公倍数
	 */
	public static long lcm(final int[] arr) {
		long lcm = arr[0];
		for (int i = 1, len = arr.length; i < len; i++) {
			lcm = lcm(lcm, arr[i]);
		}
		return lcm;
	}

	/**
	 * 最小公倍数
	 */
	public static long lcm(final long[] arr) {
		long lcm = arr[0];
		for (int i = 1, len = arr.length; i < len; i++) {
			lcm = lcm(lcm, arr[i]);
		}
		return lcm;
	}

	/* -------------- sum, min, max -------------- */

	/**
	 * int配列の合計
	 */
	public static long sum(final int[] arr) {
		long sum = 0;
		for (int x : arr) sum += x;
		return sum;
	}

	/**
	 * long配列の合計
	 */
	public static long sum(final long[] arr) {
		long sum = 0;
		for (long x : arr) sum += x;
		return sum;
	}

	/**
	 * int配列の最小値
	 */
	public static int min(final int[] arr) {
		int min = arr[0];
		for (int i = 1, len = arr.length; i < len; i++) {
			min = Math.min(min, arr[i]);
		}
		return min;
	}

	/**
	 * long配列の最小値
	 */
	public static long min(final long[] arr) {
		long min = arr[0];
		for (int i = 1, len = arr.length; i < len; i++) {
			min = Math.min(min, arr[i]);
		}
		return min;
	}

	/**
	 * int配列の最大値
	 */
	public static int max(final int[] arr) {
		int max = arr[0];
		for (int i = 1, len = arr.length; i < len; i++) {
			max = Math.max(max, arr[i]);
		}
		return max;
	}

	/**
	 * long配列の最大値
	 */
	public static long max(final long[] arr) {
		long max = arr[0];
		for (int i = 1, len = arr.length; i < len; i++) {
			max = Math.max(max, arr[i]);
		}
		return max;
	}

	/* -------------- factorsSet -------------- */

	/**
	 * 約数のHashSet
	 */
	public static HashSet<Long> factorsHashSet(final long n) {
		final HashSet<Long> hs = new HashSet<>();
		for (long i = 1; i * i <= n; i++) {
			if (n % i == 0) {
				hs.add(i);
				hs.add(n / i);
			}
		}
		return hs;
	}

	/**
	 * 約数のTreeSet
	 */
	public static TreeSet<Long> factorsTreeSet(final long n) {
		final TreeSet<Long> hs = new TreeSet<>();
		for (long i = 1; i * i <= n; i++) {
			if (n % i == 0) {
				hs.add(i);
				hs.add(n / i);
			}
		}
		return hs;
	}
}
