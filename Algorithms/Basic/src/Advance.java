import java.util.HashSet;
import java.util.TreeSet;

import static java.lang.Math.sqrt;

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
	public static long modPow(long n, long k, long mod) {
		long modPow = 1;
		for (n %= mod, k %= mod; k > 0; k >>= 1, n = n * n % mod) {
			if ((k & 1) == 1) modPow = modPow * n % mod;
		}
		return modPow;
	}

	/* -------------- gcd -------------- */

	/**
	 * 最大公約数
	 */
	public static int gcd(int a, int b) {
		return b > 0 ? gcd(b, a % b) : a;
	}

	/**
	 * 最大公約数
	 */
	public static long gcd(long a, long b) {
		return b > 0 ? gcd(b, a % b) : a;
	}

	/**
	 * 最大公約数
	 */
	public static int gcd(int[] arr) {
		int iGcd = arr[0];
		int len = arr.length;
		for (int i = 1; i < len; i++) {
			iGcd = gcd(iGcd, arr[i]);
		}
		return iGcd;
	}

	/**
	 * 最大公約数
	 */
	public static long gcd(long[] arr) {
		long lGcd = arr[0];
		int len = arr.length;
		for (int i = 1; i < len; i++) {
			lGcd = gcd(lGcd, arr[i]);
		}
		return lGcd;
	}

	/* -------------- lcm -------------- */

	/**
	 * 最小公倍数
	 */
	public static long lcm(int a, int b) {
		return (long) a / gcd(a, b) * b;
	}

	/**
	 * 最小公倍数
	 */
	public static long lcm(long a, long b) {
		return a / gcd(a, b) * b;
	}

	/**
	 * 最小公倍数
	 */
	public static long lcm(int[] arr) {
		long lcm = arr[0];
		int len = arr.length;
		for (int i = 1; i < len; i++) {
			lcm = lcm(lcm, arr[i]);
		}
		return lcm;
	}

	/**
	 * 最小公倍数
	 */
	public static long lcm(long[] arr) {
		long lcm = arr[0];
		int len = arr.length;
		for (int i = 1; i < len; i++) {
			lcm = lcm(lcm, arr[i]);
		}
		return lcm;
	}

	/* -------------- sum, min, max -------------- */

	/**
	 * int配列の合計
	 */
	public static long sum(int[] arr) {
		long sum = 0;
		for (int x : arr) sum += x;
		return sum;
	}

	/**
	 * long配列の合計
	 */
	public static long sum(long[] arr) {
		long sum = 0;
		for (long x : arr) sum += x;
		return sum;
	}

	/**
	 * int配列の最小値
	 */
	public static int min(int[] arr) {
		int min = arr[0];
		int len = arr.length;
		for (int i = 1; i < len; i++) {
			min = Math.min(min, arr[i]);
		}
		return min;
	}

	/**
	 * long配列の最小値
	 */
	public static long min(long[] arr) {
		long min = arr[0];
		int len = arr.length;
		for (int i = 1; i < len; i++) {
			min = Math.min(min, arr[i]);
		}
		return min;
	}

	/**
	 * int配列の最大値
	 */
	public static int max(int[] arr) {
		int max = arr[0];
		int len = arr.length;
		for (int i = 1; i < len; i++) {
			max = Math.max(max, arr[i]);
		}
		return max;
	}

	/**
	 * long配列の最大値
	 */
	public static long max(long[] arr) {
		long max = arr[0];
		int len = arr.length;
		for (int i = 1; i < len; i++) {
			max = Math.max(max, arr[i]);
		}
		return max;
	}

	/* -------------- factorsSet -------------- */

	/**
	 * 約数のHashSet
	 */
	public static HashSet<Integer> factorsHashSet(long n) {
		HashSet<Integer> hs = new HashSet<>();
		int sqrt = (int) sqrt(n) + 1;
		for (int i = 1; i < sqrt; i++) {
			if (n % i == 0) {
				hs.add(i);
				hs.add((int) (n / i));
			}
		}
		return hs;
	}

	/**
	 * 約数のTreeSet
	 */
	public static TreeSet<Integer> factorsTreeSet(long n) {
		TreeSet<Integer> hs = new TreeSet<>();
		int sqrt = (int) sqrt(n) + 1;
		for (int i = 1; i < sqrt; i++) {
			if (n % i == 0) {
				hs.add(i);
				hs.add((int) (n / i));
			}
		}
		return hs;
	}
}
