/**
 * 数論関連のユーティリティクラス
 */
@SuppressWarnings("unused")
public class NumberTheoryUtils {

	/**
	 * 最小公倍数を求めます
	 *
	 * @param x long
	 * @param y long
	 * @return x * (y / GCD(x, y))
	 */
	public static long LCM(long x, long y) {
		return x == 0 || y == 0 ? 0 : x * (y / GCD(x, y));
	}

	/**
	 * 最大公約数を求めます
	 *
	 * @param x long
	 * @param y long
	 * @return GCD(x, y)
	 */
	public static long GCD(long x, long y) {
		return y > 0 ? GCD(y, x % y) : x;
	}

	/**
	 * ax + by = GCD(x, y)となるx, yの組を見つけます。
	 *
	 * @param a  long
	 * @param b  long
	 * @param xy long[]
	 * @return |x| + |y|の最小値
	 */
	public static long exGCD(long a, long b, long[] xy) {
		if (b == 0) {
			xy[0] = 1;
			xy[1] = 0;
			return a;
		}
		long[] tmp = new long[2];
		long d = exGCD(b, a % b, tmp);
		xy[0] = tmp[0];
		xy[1] = tmp[1] - (a / b) * tmp[0];
		return d;
	}

	/**
	 * オイラーのトーシェント関数に基づき1からnまでのnと互いに素な数字の個数を調べます。
	 *
	 * @param n long
	 * @return long
	 */
	public static long EulerTotientFunction(long n) {
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
	 * 素数判定を行います。
	 *
	 * @param n 判定する数値
	 * @return 素数かどうか
	 */
	public static boolean isPrime(long n) {
		if (n <= 1) return false;
		if (n <= 3) return true;
		if (n % 2 == 0 || n % 3 == 0) return false;

		for (long i = 5; i * i <= n; i += 6) {
			if (n % i == 0 || n % (i + 2) == 0) return false;
		}
		return true;
	}

	/**
	 * 素因数分解を行います。
	 *
	 * @param n 分解する数値
	 * @return 素因数の配列
	 */
	public static long[] primeFactors(long n) {
		if (n <= 1) return new long[0];

		java.util.List<Long> factors = new java.util.ArrayList<>();
		while (n % 2 == 0) {
			factors.add(2L);
			n /= 2;
		}

		for (long i = 3; i * i <= n; i += 2) {
			while (n % i == 0) {
				factors.add(i);
				n /= i;
			}
		}

		if (n > 1) factors.add(n);

		long[] result = new long[factors.size()];
		for (int i = 0; i < factors.size(); i++) {
			result[i] = factors.get(i);
		}
		return result;
	}

	/**
	 * モジュラ逆数を計算します。
	 *
	 * @param a 元の数値
	 * @param m 法
	 * @return a^(-1) mod m
	 */
	public static long modInverse(long a, long m) {
		long[] xy = new long[2];
		long gcd = exGCD(a, m, xy);
		if (gcd != 1) return -1; // 逆数が存在しない
		return (xy[0] % m + m) % m;
	}
} 