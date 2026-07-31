package lib.math;

import java.util.*;

/**
 * 数値判定関連のユーティリティクラス
 */
@SuppressWarnings("unused")
public final class NumberPredicates {
	private NumberPredicates() {
	}

	/**
	 * 完全数かどうかの判定
	 *
	 * @param n 判定する対象
	 * @return 完全数かどうか
	 */
	public static boolean isPerfectNumber(long n) {
		if (n <= 1) return false;
		long sum = 1;
		for (long i = 2; i * i <= n; i++) {
			if (n % i == 0) {
				sum += i;
				if (i * i != n) {
					sum += n / i;
				}
			}
		}
		return sum == n;
	}

	/**
	 * 回文数かどうかの判定
	 *
	 * @param n 判定する対象
	 * @return 回文数かどうか
	 */
	public static boolean isPalindrome(long n) {
		if (n < 0) return false;
		long original = n;
		long reversed = 0;
		while (n > 0) {
			reversed = reversed * 10 + n % 10;
			n /= 10;
		}
		return original == reversed;
	}

	/**
	 * フィボナッチ数かどうかの判定
	 * {@code 0 <= n <= 1_358_187_913} を前提とします。
	 *
	 * @param n 判定する対象
	 * @return フィボナッチ数かどうか
	 */
	public static boolean isFibonacci(long n) {
		return MathUtils.isSquare(5 * n * n + 4) || MathUtils.isSquare(5 * n * n - 4);
	}

	/**
	 * アームストロング数かどうかの判定
	 *
	 * @param n 判定する対象
	 * @return アームストロング数かどうか
	 */
	public static boolean isArmstrong(long n) {
		if (n < 0) return false;
		long original = n;
		int digits = 0;
		long temp = n;
		while (temp > 0) {
			digits++;
			temp /= 10;
		}
		long sum = 0;
		while (n > 0) {
			sum += MathUtils.pow(n % 10, digits);
			n /= 10;
		}
		return sum == original;
	}

	/**
	 * ハッピー数かどうかの判定
	 *
	 * @param n 判定する対象
	 * @return ハッピー数かどうか
	 */
	public static boolean isHappyNumber(long n) {
		if (n <= 0) return false;
		Set<Long> seen = new HashSet<>();
		while (n != 1 && !seen.contains(n)) {
			seen.add(n);
			long sum = 0;
			while (n > 0) {
				sum += (n % 10) * (n % 10);
				n /= 10;
			}
			n = sum;
		}
		return n == 1;
	}
}
