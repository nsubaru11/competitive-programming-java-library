package lib.math;

import static java.lang.Math.*;

/**
 * 整数型を対象とする数学ユーティリティです。
 *
 * <p>mod を受け取るメソッドは正の法を前提とします。逆元を利用するメソッドは、
 * 個別の JavaDoc に記載したとおり mod が素数であることを前提とします。
 */
@SuppressWarnings("unused")
public final class MathUtils {
	private static final long[] SMALL_FACTORIALS = {1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800, 39916800, 479001600, 6227020800L, 87178291200L, 1307674368000L, 20922789888000L, 355687428096000L, 6402373705728000L, 121645100408832000L, 2432902008176640000L};

	private MathUtils() {
	}

	/**
	 * 2つの {@code int} の最小値を返します。
	 */
	public static int min(final int a, final int b) {
		return Math.min(a, b);
	}

	/**
	 * 2つの {@code long} の最小値を返します。
	 */
	public static long min(final long a, final long b) {
		return Math.min(a, b);
	}

	/**
	 * 3つの {@code int} の最小値を返します。
	 */
	public static int min(final int a, final int b, final int c) {
		return Math.min(a, Math.min(b, c));
	}

	/**
	 * 3つの {@code long} の最小値を返します。
	 */
	public static long min(final long a, final long b, final long c) {
		return Math.min(a, Math.min(b, c));
	}

	/**
	 * 2つ以上の {@code int} の最小値を返します。
	 */
	public static int min(final int... a) {
		final int len = a.length;
		int min = a[0];
		for (int i = 1; i < len; i++) if (min > a[i]) min = a[i];
		return min;
	}

	/**
	 * 2つ以上の {@code long} の最小値を返します。
	 */
	public static long min(final long... a) {
		final int len = a.length;
		long min = a[0];
		for (int i = 1; i < len; i++) if (min > a[i]) min = a[i];
		return min;
	}

	/**
	 * 2つの {@code int} の最大値を返します。
	 */
	public static int max(final int a, final int b) {
		return Math.max(a, b);
	}

	/**
	 * 2つの {@code long} の最大値を返します。
	 */
	public static long max(final long a, final long b) {
		return Math.max(a, b);
	}

	/**
	 * 3つの {@code int} の最大値を返します。
	 */
	public static int max(final int a, final int b, final int c) {
		return Math.max(a, Math.max(b, c));
	}

	/**
	 * 3つの {@code long} の最大値を返します。
	 */
	public static long max(final long a, final long b, final long c) {
		return Math.max(a, Math.max(b, c));
	}

	/**
	 * 2つ以上の {@code int} の最大値を返します。
	 */
	public static int max(final int... a) {
		final int len = a.length;
		int max = a[0];
		for (int i = 1; i < len; i++) if (max < a[i]) max = a[i];
		return max;
	}

	/**
	 * 2つ以上の {@code long} の最大値を返します。
	 */
	public static long max(final long... a) {
		final int len = a.length;
		long max = a[0];
		for (int i = 1; i < len; i++) if (max < a[i]) max = a[i];
		return max;
	}

	/**
	 * 2つの {@code int} の差の絶対値を返します。
	 */
	public static int diff(final int a, final int b) {
		return a > b ? a - b : b - a;
	}

	/**
	 * 2つの {@code long} の差の絶対値を返します。
	 */
	public static long diff(final long a, final long b) {
		return a > b ? a - b : b - a;
	}

	/**
	 * 整数乗を返します。指数は非負であることを前提とします。
	 */
	public static long pow(long a, int b) {
		if (b == 0) return 1;
		long ans = 1;
		for (; b > 1; b >>= 1, a *= a) {
			if ((b & 1) == 1) ans *= a;
		}
		return ans * a;
	}

	/**
	 * {@code a^b mod mod} を返します。指数は非負であることを前提とします。
	 */
	public static int modPow(int a, int b, final int mod) {
		if (b == 0) return 1;
		int ans = 1;
		for (a %= mod; b > 1; b >>= 1) {
			if ((b & 1) == 1) ans = (int) ((long) ans * a % mod);
			a = (int) ((long) a * a % mod);
		}
		return (int) ((long) ans * a % mod);
	}

	/**
	 * {@code a^b mod mod} を返します。指数は非負であることを前提とします。
	 */
	public static long modPow(long a, long b, final long mod) {
		if (b == 0) return 1;
		long ans = 1;
		for (a %= mod; b > 1; b >>= 1) {
			if ((b & 1) == 1) ans = ans * a % mod;
			a = a * a % mod;
		}
		return ans * a % mod;
	}

	/**
	 * Fermat の小定理により {@code a} の mod 逆元を求めます。mod は素数を前提とします。
	 */
	public static int modInv(final int a, final int mod) {
		return modPow(a, mod - 2, mod);
	}

	/**
	 * Fermat の小定理により {@code a} の mod 逆元を求めます。mod は素数を前提とします。
	 */
	public static long modInv(final long a, final long mod) {
		return modPow(a, mod - 2, mod);
	}

	/**
	 * {@code n} 以下の最大の平方根を返します。{@code n <= 0} では 0 を返します。
	 */
	public static int sqrt(final int n) {
		if (n <= 0) return 0;
		return (int) Math.sqrt(n);
	}

	/**
	 * {@code n} 以下の最大の立方根を返します。{@code n <= 0} では 0 を返します。
	 */
	public static int cbrt(final int n) {
		if (n <= 0) return 0;
		return (int) Math.cbrt(n);
	}

	/**
	 * {@code n} 以下の最大の平方根を返します。{@code n <= 0} では 0 を返します。
	 */
	public static long sqrt(final long n) {
		if (n <= 0) return 0;
		long x = (long) Math.sqrt(n);
		if (x * x > n) x--;
		else if ((x + 1) * (x + 1) <= n) x++;
		return x;
	}

	/**
	 * {@code n} 以下の最大の立方根を返します。{@code n <= 0} では 0 を返します。
	 */
	public static int cbrt(final long n) {
		if (n <= 0) return 0;
		long x = (long) Math.cbrt(n);
		if (x * x * x > n) x--;
		else if ((x + 1) * (x + 1) * (x + 1) <= n) x++;
		return (int) x;
	}

	/**
	 * {@code n} が平方数か判定します。
	 */
	public static boolean isSquare(final int n) {
		int sqrt = sqrt(n);
		return n == sqrt * sqrt;
	}

	/**
	 * {@code n} が立方数か判定します。
	 */
	public static boolean isCube(final int n) {
		int cbrt = cbrt(n);
		return n == cbrt * cbrt * cbrt;
	}

	/**
	 * {@code n} が平方数か判定します。
	 */
	public static boolean isSquare(final long n) {
		long sqrt = sqrt(n);
		return n == sqrt * sqrt;
	}

	/**
	 * {@code n} が立方数か判定します。
	 */
	public static boolean isCube(final long n) {
		long cbrt = cbrt(n);
		return n == cbrt * cbrt * cbrt;
	}

	public static int digit2(long n) {
		if (n == 0) return 1;
		if (n == Long.MIN_VALUE) return 63;
		return 64 - Long.numberOfLeadingZeros(Math.abs(n));
	}

	public static int digit2(int n) {
		if (n == 0) return 1;
		if (n == Integer.MIN_VALUE) return 31;
		return 32 - Integer.numberOfLeadingZeros(Math.abs(n));
	}

	public static int digit10(long n) {
		if (n == Long.MIN_VALUE) return 19;
		if (n < 0) n = -n;
		int res = 0;
		do {
			res++;
			n /= 10;
		} while (n > 0);
		return res;
	}

	public static int digit10(int n) {
		if (n == Integer.MIN_VALUE) return 10;
		if (n < 0) n = -n;
		int res = 0;
		do {
			res++;
			n /= 10;
		} while (n > 0);
		return res;
	}

	/**
	 * 2つの {@code int} の最小公倍数を返します。
	 */
	public static int lcm(final int x, final int y) {
		return x == 0 || y == 0 ? 0 : x / gcd(x, y) * y;
	}

	/**
	 * 2つの {@code long} の最小公倍数を返します。
	 */
	public static long lcm(final long x, final long y) {
		return x == 0 || y == 0 ? 0 : x / gcd(x, y) * y;
	}

	/**
	 * binary GCD により2つの {@code int} の最大公約数を返します。
	 */
	public static int gcd(int a, int b) {
		a = abs(a);
		b = abs(b);
		if (a == 0) return b;
		if (b == 0) return a;
		int commonShift = Integer.numberOfTrailingZeros(a | b);
		a >>= Integer.numberOfTrailingZeros(a);
		while (b != 0) {
			b >>= Integer.numberOfTrailingZeros(b);
			if (a > b) {
				int tmp = a;
				a = b;
				b = tmp;
			}
			b -= a;
		}
		return a << commonShift;
	}

	/**
	 * binary GCD により2つの {@code long} の最大公約数を返します。
	 */
	public static long gcd(long a, long b) {
		a = abs(a);
		b = abs(b);
		if (a == 0) return b;
		if (b == 0) return a;
		int commonShift = Long.numberOfTrailingZeros(a | b);
		a >>= Long.numberOfTrailingZeros(a);
		while (b != 0) {
			b >>= Long.numberOfTrailingZeros(b);
			if (a > b) {
				long tmp = a;
				a = b;
				b = tmp;
			}
			b -= a;
		}
		return a << commonShift;
	}

	/**
	 * {@code n!} を返します。正確に表現できる範囲として {@code 0 <= n <= 20} を前提とします。
	 */
	public static long fact(final int n) {
		return SMALL_FACTORIALS[n];
	}

	/**
	 * {@code n! mod mod} を返します。
	 */
	public static int modFact(final int n, final int mod) {
		long ans = 1;
		for (int i = 1; i <= n; i++) ans = ans * i % mod;
		return (int) ans;
	}

	/**
	 * {@code n! mod mod} を返します。
	 */
	public static long modFact(final int n, final long mod) {
		long ans = 1;
		for (int i = 1; i <= n; i++) ans = ans * i % mod;
		return ans;
	}

	/**
	 * {@code n!} の mod 逆元を返します。mod は素数かつ {@code n < mod} を前提とします。
	 */
	public static int invFact(final int n, final int mod) {
		long ans = 1;
		for (int a = modFact(n, mod), b = mod - 2; b > 0; a = a * a % mod, b >>= 1) {
			if ((b & 1) == 1) ans = ans * a % mod;
		}
		return (int) ans;
	}

	/**
	 * {@code n!} の mod 逆元を返します。mod は素数かつ {@code n < mod} を前提とします。
	 */
	public static long invFact(final int n, final long mod) {
		long ans = 1;
		for (long a = modFact(n, mod), b = mod - 2; b > 0; a = a * a % mod, b >>= 1) {
			if ((b & 1) == 1) ans = ans * a % mod;
		}
		return ans;
	}

	/**
	 * 決定的な素数判定を行います。
	 */
	public static boolean isPrime(final long n) {
		return PrimeUtils.isPrime(n);
	}

	/**
	 * {@code k} 回相当の精度で確率的素数判定を行います。
	 */
	public static boolean isProbablePrime(long n, int k) {
		return PrimeUtils.isProbablePrime(n, k);
	}

	/**
	 * {@code n!} に含まれる {@code prime} の指数を返します。{@code prime} は素数を前提とします。
	 */
	public static int primeExponent(final int n, final int prime) {
		int cnt = 0;
		for (int p = prime; p <= n; p *= prime) {
			cnt += n / p;
		}
		return cnt;
	}

	/**
	 * {@code n!} に含まれる {@code prime} の指数を返します。{@code prime} は素数を前提とします。
	 */
	public static long primeExponent(final long n, final long prime) {
		long cnt = 0;
		for (long p = prime; p <= n; p *= prime) {
			cnt += n / p;
		}
		return cnt;
	}

	/**
	 * nCrを求めます。
	 *
	 * @param n 二項係数を求めるのに用いる値
	 * @param r 二項係数を求めるのに用いる値
	 * @return nCr
	 */
	public static long nCr(long n, int r) {
		if (r < 0 || r > n) return 0;
		r = (int) min(n - r, r);
		long numer = 1, denom = 1;
		for (int i = 1; i <= r; i++) {
			numer *= n - i + 1;
			denom *= i;
		}
		return numer / denom;
	}

	/**
	 * nCrをmodで割った余りを求めます。
	 * mod は素数であり、{@code r < mod} であることを前提とします。
	 *
	 * @param n   二項係数を求めるのに用いる値
	 * @param r   二項係数を求めるのに用いる値
	 * @param mod 法とする整数
	 * @return nCr % mod
	 */
	public static long nCr(long n, int r, final long mod) {
		if (r < 0 || r > n) return 0;
		r = (int) min(n - r, r);
		long ans = 1;
		for (int i = 1; i <= r; i++) {
			ans = ans * (n - i + 1) % mod;
		}
		return ans * MathUtils.invFact(r, mod) % mod;
	}

	/**
	 * nPrを求めます。
	 *
	 * @param n 順列を求めるのに用いる値
	 * @param r 順列を求めるのに用いる値
	 * @return nPr
	 */
	public static long nPr(long n, int r) {
		if (r < 0 || r > n) return 0;
		long result = 1;
		for (int i = 0; i < r; i++) {
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
	public static long nPr(long n, int r, final long mod) {
		if (r < 0 || r > n) return 0;
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
	public static long nHr(final long n, final int r) {
		if (n < 0 || r < 0) return 0;
		if (n == 0 && r == 0) return 1;
		return nCr(n + r - 1, r);
	}

	/**
	 * 重複組み合わせnHrをmodで割った余りを求めます。
	 * mod は素数であり、{@code r < mod} であることを前提とします。
	 *
	 * @param n   重複組み合わせを求めるのに用いる値
	 * @param r   重複組み合わせを求めるのに用いる値
	 * @param mod 法とする整数
	 * @return nHr % mod
	 */
	public static long nHr(final long n, final int r, final long mod) {
		if (n < 0 || r < 0) return 0;
		if (n == 0 && r == 0) return 1;
		return nCr(n + r - 1, r, mod) % mod;
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
		if (k == 0) return n == 0 ? 1 : 0;
		if (n == k || k == 1) return 1;
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

		return bell[n][0];
	}

	/**
	 * Euler のトーシェント関数 {@code φ(n)} を返します。
	 * {@code n >= 1} を前提とします。
	 *
	 * @param n 対象の正整数
	 * @return 1 以上 n 以下で n と互いに素な整数の個数
	 */
	public static int eulerTotient(int n) {
		int sum = 1;
		for (int i = 2; i <= 3; i++) {
			int temp = 1;
			while (n % i == 0) {
				n /= i;
				temp *= i;
			}
			sum *= temp - temp / i;
		}
		for (int i = 5; i * i <= n; i += 6) {
			for (int j = i; j <= i + 2; j += 2) {
				if (n % j == 0) {
					int temp = 1;
					while (n % j == 0) {
						n /= j;
						temp *= j;
					}
					sum *= temp - temp / j;
				}
			}
		}
		return n == 1 ? sum : sum * (n - 1);
	}

	/**
	 * Euler のトーシェント関数 {@code φ(n)} を返します。
	 * {@code n >= 1} を前提とします。
	 *
	 * @param n 対象の正整数
	 * @return 1 以上 n 以下で n と互いに素な整数の個数
	 */
	public static long eulerTotient(long n) {
		long sum = 1;
		for (int i = 2; i <= 3; i++) {
			long temp = 1;
			while (n % i == 0) {
				n /= i;
				temp *= i;
			}
			sum *= temp - temp / i;
		}
		for (int i = 5; (long) i * i <= n; i += 6) {
			for (int j = i; j <= i + 2; j += 2) {
				if (n % j == 0) {
					long temp = 1;
					while (n % j == 0) {
						n /= j;
						temp *= j;
					}
					sum *= temp - temp / j;
				}
			}
		}
		return n == 1 ? sum : sum * (n - 1);
	}

	/**
	 * 拡張 Euclid の互除法により {@code a*x[0] + b*y[0] = gcd(a, b)} を満たす係数を求めます。
	 *
	 * @param a 係数 a
	 * @param b 係数 b
	 * @param x 係数 x の出力先（長さ1以上）
	 * @param y 係数 y の出力先（長さ1以上）
	 * @return {@code gcd(a, b)}
	 */
	public static int exgcd(int a, int b, int[] x, int[] y) {
		int r0 = a, r1 = b;
		int x0 = 1, x1 = 0;
		int y0 = 0, y1 = 1;
		while (r1 != 0) {
			int q = r0 / r1;
			int nr = r0 % r1;
			r0 = r1;
			r1 = nr;
			int nx = x0 - q * x1;
			x0 = x1;
			x1 = nx;
			int ny = y0 - q * y1;
			y0 = y1;
			y1 = ny;
		}
		x[0] = x0;
		y[0] = y0;
		return r0;
	}

	/**
	 * 拡張 Euclid の互除法により {@code a*x[0] + b*y[0] = gcd(a, b)} を満たす係数を求めます。
	 *
	 * @param a 係数 a
	 * @param b 係数 b
	 * @param x 係数 x の出力先（長さ1以上）
	 * @param y 係数 y の出力先（長さ1以上）
	 * @return {@code gcd(a, b)}
	 */
	public static long exgcd(long a, long b, long[] x, long[] y) {
		long r0 = a, r1 = b;
		long x0 = 1, x1 = 0;
		long y0 = 0, y1 = 1;
		while (r1 != 0) {
			long q = r0 / r1;
			long nr = r0 % r1;
			r0 = r1;
			r1 = nr;
			long nx = x0 - q * x1;
			x0 = x1;
			x1 = nx;
			long ny = y0 - q * y1;
			y0 = y1;
			y1 = ny;
		}
		x[0] = x0;
		y[0] = y0;
		return r0;
	}

}
