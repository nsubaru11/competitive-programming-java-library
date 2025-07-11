/**
 * 累乗・階乗関連のユーティリティクラス
 */
@SuppressWarnings("unused")
public class PowerUtils {

	/**
	 * a ^ b % mod を求めます。
	 *
	 * @param a   底
	 * @param b   指数
	 * @param mod 割る値(素数)
	 * @return a ^ b % mod
	 */
	public static long modPow(long a, long b, long mod) {
		long ans = 1;
		b %= mod - 1;
		for (; b > 0; a = a * a % mod, b >>= 1) {
			if ((b & 1) == 1) ans = ans * a % mod;
		}
		return ans;
	}

	/**
	 * aのb乗を求めます
	 *
	 * @param a 底
	 * @param b 指数
	 * @return a ^ b
	 */
	public static long pow(long a, int b) {
		long ans = 1;
		for (; b > 0; a *= a, b >>= 1) {
			if ((b & 1) == 1) ans *= a;
		}
		return ans;
	}

	/**
	 * nが20以下の階乗を返します。
	 *
	 * @param n int
	 * @return n!
	 */
	public static long factorial_max20(int n) {
		long[] SmallFactorials = {1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800, 39916800, 479001600, 6227020800L, 87178291200L, 1307674368000L, 20922789888000L, 355687428096000L, 6402373705728000L, 121645100408832000L, 2432902008176640000L};
		return SmallFactorials[n];
	}

	/**
	 * nの階乗をmodで割った余りを計算します。
	 *
	 * @param n   int
	 * @param mod long
	 * @return n! % mod
	 */
	public static long modFactorial(int n, long mod) {
		if (n <= 20) return factorial_max20(n) % mod;
		if (n >= mod) return 0;
		if (n == mod - 1) return n;
		int twoExponent = n - Integer.bitCount(n);
		long[] oddPart = new long[(n >> 1) + 1];
		oddPart[0] = 1;
		for (int i = 3; i <= n; i += 2) {
			int idx = i >> 1;
			oddPart[idx] = oddPart[idx - 1] * i % mod;
		}
		long ans = oddPart[(n - 1) >> 1];
		for (int i = 1; n >> i > 1; i++) {
			int k = n;
			if ((k >>= i) % 2 == 1) {
				ans = ans * oddPart[k >> 1] % mod;
			} else {
				ans = ans * oddPart[(k - 1) >> 1] % mod;
			}
		}
		long twoExp = (1L << 62) % mod;
		while (twoExponent > 62) {
			ans = ans * twoExp % mod;
			twoExponent -= 62;
		}
		return ans * ((1L << twoExponent) % mod) % mod;
	}

} 