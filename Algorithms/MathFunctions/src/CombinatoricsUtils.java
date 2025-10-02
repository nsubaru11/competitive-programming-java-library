import static java.lang.Math.min;

/**
 * 組み合わせ論関連のユーティリティクラス
 */
@SuppressWarnings("unused")
public final class CombinatoricsUtils {

	/**
	 * nCrを求めます。
	 *
	 * @param n 二項係数を求めるのに用いる値
	 * @param r 二項係数を求めるのに用いる値
	 * @return nCr
	 */
	public static long comb(int n, int r, final long mod) {
		if (n < r || n < 0 || r < 0) return 0;
		r = min(n - r, r);
		long ans = 1;
		long[] x = new long[1], y = new long[1];
		for (int i = 1; i <= r; i++) {
			ans = ans * (n - i + 1) % mod;
			if (NumberTheoryUtils.GCD(i, mod) != 1) {
				NumberTheoryUtils.exGCD(i, mod, x, y);
				ans = ans * ((x[0] % mod + mod) % mod) % mod;
			} else {
				ans = ans * (PowerUtils.modPow(i, mod - 2, mod)) % mod;
			}
		}
		return ans;
	}

	/**
	 * nCrをmodで割った余りを求めます。
	 *
	 * @param n   二項係数を求めるのに用いる値
	 * @param r   二項係数を求めるのに用いる値
	 * @param mod 法とする整数
	 * @return nCr % mod
	 */
	public static long modComb(int n, int r, final long mod) {
		if (n < r || n < 0 || r < 0) return 0;
		r = min(n - r, r);
		long numer = 1, denom = 1;
		for (int i = 1; i <= r; i++) {
			numer *= n - i + 1;
			denom *= i;
		}
		return (numer / denom) % mod;
	}

	/**
	 * nPrを求めます。
	 *
	 * @param n 順列を求めるのに用いる値
	 * @param r 順列を求めるのに用いる値
	 * @return nPr
	 */
	public static long perm(long n, long r) {
		if (n < 0 || r < 0) return 0;
		if (r > n) return 0;
		if (r > Integer.MAX_VALUE) throw new ArithmeticException("数が膨大すぎます。");
		long result = 1;
		for (long i = 0; i < r; i++) {
			result *= (n - i);
		}
		return result;
	}

	/**
	 * nPrをmodで割った余りを求めます。
	 *
	 * @param n   順列を求めるのに用いる値
	 * @param r   順列を求めるのに用いる値
	 * @param mod 法とする整数
	 * @return nPr % mod
	 */
	public static long modPerm(long n, long r, final long mod) {
		if (n < 0 || r < 0) return 0;
		if (r > n) return 0;
		if (r > Integer.MAX_VALUE)
			throw new ArithmeticException("数が膨大すぎます。");
		long result = 1;
		for (long i = 0; i < r; i++) {
			result = (result * (n - i)) % mod;
		}
		return result;
	}

	/**
	 * 重複組み合わせnHrを求めます。
	 *
	 * @param n 重複組み合わせを求めるのに用いる値
	 * @param r 重複組み合わせを求めるのに用いる値
	 * @return nHr
	 */
	public static long multiComb(final int n, final int r, final long mod) {
		return comb(n + r - 1, r, mod);
	}

	/**
	 * 重複組み合わせnHrをmodで割った余りを求めます。
	 *
	 * @param n   重複組み合わせを求めるのに用いる値
	 * @param r   重複組み合わせを求めるのに用いる値
	 * @param mod 法とする整数
	 * @return nHr % mod
	 */
	public static long modMultiComb(final int n, final int r, final long mod) {
		return comb(n + r - 1, r, mod) % mod;
	}

	/**
	 * スターリング数（第2種）を求めます。
	 *
	 * @param n スターリング数を求めるのに用いる値
	 * @param k スターリング数を求めるのに用いる値
	 * @return S(n, k)
	 */
	public static long stirlingNumber2(int n, int k) {
		if (n < k || k < 0) return 0;
		if (n == k || k == 0 || k == 1) return 1;
		long[] stirling = new long[n * (n + 1) >> 1];
		for (int i = 0, c = 0; i < n; i++, c += i) {
			stirling[c] = stirling[c + i] = 1;
			for (int j = 1; j < min(i, k); j++) {
				stirling[c + j] = stirling[c + j - i - 1] + (j + 1) * stirling[c + j - i];
			}
		}
		return stirling[((n - 1) * n >> 1) + k - 1];
	}

	/**
	 * ベル数を求めます。
	 *
	 * @param n ベル数を求めるのに用いる値
	 * @return B(n)
	 */
	public static long bellNumber(int n) {
		if (n <= 0) return 1;

		long[][] bell = new long[n + 1][n + 1];
		bell[0][0] = 1;

		for (int i = 1; i <= n; i++) {
			bell[i][0] = bell[i - 1][i - 1];
			for (int j = 1; j <= i; j++) {
				bell[i][j] = bell[i - 1][j - 1] + bell[i][j - 1];
			}
		}

//		return bell[n][0];
		return (long) Math.pow(Math.E, Math.pow(Math.E, n) - 1);
	}
} 