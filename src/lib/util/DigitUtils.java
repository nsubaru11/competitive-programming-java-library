package lib.util;

import java.util.*;

/**
 * 十進表現を対象とする桁操作を提供するユーティリティです。
 *
 * <p>数値を受け取るメソッドは非負整数を前提とします。
 */
public final class DigitUtils {
	private DigitUtils() {
	}

	/**
	 * {@code n} の二進表現の桁数を返します。{@code 0} の桁数は 1 とします。
	 */
	public static int digits2(int n) {
		if (n == 0) return 1;
		return 32 - Integer.numberOfLeadingZeros(n);
	}

	/**
	 * {@code n} の二進表現の桁数を返します。{@code 0} の桁数は 1 とします。
	 */
	public static int digits2(long n) {
		if (n == 0) return 1;
		return 64 - Long.numberOfLeadingZeros(n);
	}

	/**
	 * {@code n} の十進表現の桁数を返します。{@code 0} の桁数は 1 とします。
	 */
	public static int digits10(int n) {
		int digits = 1;
		for (; n >= 10; n /= 10) digits++;
		return digits;
	}

	/**
	 * {@code n} の十進表現の桁数を返します。{@code 0} の桁数は 1 とします。
	 */
	public static int digits10(long n) {
		int digits = 1;
		for (; n >= 10; n /= 10) digits++;
		return digits;
	}

	/**
	 * {@code n} の十進表現を反転した値を返します。
	 */
	public static int reverse(int n) {
		int result = 0;
		while (n > 0) {
			result = result * 10 + n % 10;
			n /= 10;
		}
		return result;
	}

	/**
	 * {@code n} の十進表現を反転した値を返します。
	 */
	public static long reverse(long n) {
		long result = 0;
		while (n > 0) {
			result = result * 10 + n % 10;
			n /= 10;
		}
		return result;
	}

	/**
	 * {@code n} の十進表現の各桁を昇順に並べた値を返します。
	 */
	public static int sort(final int n) {
		final long counts = digitCounts(n);
		int result = 0;
		for (int digit = 0; digit < 10; digit++) {
			int count = (int) (counts >>> (digit << 2)) & 15;
			while (count-- > 0) result = result * 10 + digit;
		}
		return result;
	}

	/**
	 * {@code n} の十進表現の各桁を昇順に並べた値を返します。
	 */
	public static long sort(final long n) {
		final long counts = digitCounts(n);
		long result = 0;
		for (int digit = 0; digit < 10; digit++) {
			int count = (int) (counts >>> (digit * 5)) & 31;
			while (count-- > 0) result = result * 10 + digit;
		}
		return result;
	}

	/**
	 * 文字列を文字コード順に昇順ソートした文字列を返します。
	 */
	public static String sort(final String s) {
		final char[] chars = s.toCharArray();
		Arrays.sort(chars);
		return new String(chars);
	}

	/**
	 * {@code n} の十進表現の各桁を降順に並べた値を返します。
	 */
	public static int descendingSort(final int n) {
		final long counts = digitCounts(n);
		int result = 0;
		for (int digit = 9; digit >= 0; digit--) {
			int count = (int) (counts >>> (digit << 2)) & 15;
			while (count-- > 0) result = result * 10 + digit;
		}
		return result;
	}

	/**
	 * {@code n} の十進表現の各桁を降順に並べた値を返します。
	 */
	public static long descendingSort(final long n) {
		final long counts = digitCounts(n);
		long result = 0;
		for (int digit = 9; digit >= 0; digit--) {
			int count = (int) (counts >>> (digit * 5)) & 31;
			while (count-- > 0) result = result * 10 + digit;
		}
		return result;
	}

	/**
	 * 文字列を文字コード順に降順ソートした文字列を返します。
	 */
	public static String descendingSort(final String s) {
		final char[] chars = s.toCharArray();
		ArrayUtils.descendingSort(chars);
		return new String(chars);
	}

	private static long digitCounts(int n) {
		long counts = 0;
		do {
			counts += 1L << (n % 10 << 2);
			n /= 10;
		} while (n > 0);
		return counts;
	}

	private static long digitCounts(long n) {
		long counts = 0;
		do {
			counts += 1L << (n % 10 * 5);
			n /= 10;
		} while (n > 0);
		return counts;
	}
}
