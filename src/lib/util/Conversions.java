package lib.util;

/**
 * 数値、文字列、数字配列の相互変換を提供するユーティリティです。
 *
 * <p>数値を受け取るメソッドは非負整数を前提とします。エンディアンを指定しない
 * メソッドはビッグエンディアンとして扱います。
 */
public final class Conversions {
	private Conversions() {
	}

	/**
	 * {@code char[]} を {@code int} に変換します。
	 */
	public static int toInt(final char[] arr) {
		return toInt(arr, false);
	}

	/**
	 * {@code char[]} を {@code int} に変換します。
	 *
	 * <p>ビッグエンディアンでは {@code "00121"} は {@code 121}、
	 * リトルエンディアンでは {@code 12100} になります。
	 *
	 * @param arr          十進数字の配列
	 * @param littleEndian リトルエンディアンで解釈するか
	 */
	public static int toInt(final char[] arr, final boolean littleEndian) {
		int result = 0;
		if (littleEndian) {
			for (int i = arr.length; i-- > 0; ) result = result * 10 + arr[i] - '0';
		} else {
			for (final char c : arr) result = result * 10 + c - '0';
		}
		return result;
	}

	/**
	 * {@code char[]} を {@code long} に変換します。
	 */
	public static long toLong(final char[] arr) {
		return toLong(arr, false);
	}

	/**
	 * {@code char[]} を {@code long} に変換します。
	 *
	 * <p>ビッグエンディアンでは {@code "00121"} は {@code 121}、
	 * リトルエンディアンでは {@code 12100} になります。
	 *
	 * @param arr          十進数字の配列
	 * @param littleEndian リトルエンディアンで解釈するか
	 */
	public static long toLong(final char[] arr, final boolean littleEndian) {
		long result = 0;
		if (littleEndian) {
			for (int i = arr.length; i-- > 0; ) result = result * 10 + arr[i] - '0';
		} else {
			for (final char c : arr) result = result * 10 + c - '0';
		}
		return result;
	}

	/**
	 * 数字配列を {@code int} に変換します。
	 */
	public static int toInt(final int[] arr) {
		return toInt(arr, false);
	}

	/**
	 * 数字配列を {@code int} に変換します。
	 *
	 * <p>ビッグエンディアンでは {@code {0, 0, 1, 2, 1}} は {@code 121}、
	 * リトルエンディアンでは {@code 12100} になります。
	 *
	 * @param arr          {@code 0} 以上 {@code 9} 以下の数字配列
	 * @param littleEndian リトルエンディアンで解釈するか
	 */
	public static int toInt(final int[] arr, final boolean littleEndian) {
		int result = 0;
		if (littleEndian) {
			for (int i = arr.length; i-- > 0; ) result = result * 10 + arr[i];
		} else {
			for (final int v : arr) result = result * 10 + v;
		}
		return result;
	}

	/**
	 * 数字配列を {@code long} に変換します。
	 */
	public static long toLong(final int[] arr) {
		return toLong(arr, false);
	}

	/**
	 * 数字配列を {@code long} に変換します。
	 *
	 * <p>ビッグエンディアンでは {@code {0, 0, 1, 2, 1}} は {@code 121}、
	 * リトルエンディアンでは {@code 12100} になります。
	 *
	 * @param arr          {@code 0} 以上 {@code 9} 以下の数字配列
	 * @param littleEndian リトルエンディアンで解釈するか
	 */
	public static long toLong(final int[] arr, final boolean littleEndian) {
		long result = 0;
		if (littleEndian) {
			for (int i = arr.length; i-- > 0; ) result = result * 10 + arr[i];
		} else {
			for (final int v : arr) result = result * 10 + v;
		}
		return result;
	}

	/**
	 * {@code int} をビッグエンディアンの {@code char[]} に変換します。
	 */
	public static char[] toCharArray(final int n) {
		return toCharArray(n, DigitUtils.digits10(n));
	}

	/**
	 * {@code int} を指定桁数のビッグエンディアンの {@code char[]} に変換します。
	 * 不足分は {@code 0} で埋め、超過分は上位桁から切り捨てます。
	 */
	public static char[] toCharArray(final int n, final int length) {
		return toCharArray(n, length, false);
	}

	/**
	 * {@code int} を指定桁数の {@code char[]} に変換します。
	 *
	 * <p>{@code n = 121, length = 5} のとき、ビッグエンディアンでは
	 * {@code "00121"}、リトルエンディアンでは {@code "12100"} になります。
	 *
	 * @param n            変換する非負整数
	 * @param length       配列の長さ
	 * @param littleEndian リトルエンディアンで出力するか
	 */
	public static char[] toCharArray(int n, final int length, final boolean littleEndian) {
		final char[] chars = new char[length];
		if (littleEndian) {
			for (int i = 0; i < length; i++) {
				chars[i] = (char) (n % 10 + '0');
				n /= 10;
			}
		} else {
			for (int i = length; i-- > 0; ) {
				chars[i] = (char) (n % 10 + '0');
				n /= 10;
			}
		}
		return chars;
	}

	/**
	 * {@code long} をビッグエンディアンの {@code char[]} に変換します。
	 */
	public static char[] toCharArray(final long n) {
		return toCharArray(n, DigitUtils.digits10(n));
	}

	/**
	 * {@code long} を指定桁数のビッグエンディアンの {@code char[]} に変換します。
	 * 不足分は {@code 0} で埋め、超過分は上位桁から切り捨てます。
	 */
	public static char[] toCharArray(final long n, final int length) {
		return toCharArray(n, length, false);
	}

	/**
	 * {@code long} を指定桁数の {@code char[]} に変換します。
	 *
	 * <p>{@code n = 121, length = 5} のとき、ビッグエンディアンでは
	 * {@code "00121"}、リトルエンディアンでは {@code "12100"} になります。
	 *
	 * @param n            変換する非負整数
	 * @param length       配列の長さ
	 * @param littleEndian リトルエンディアンで出力するか
	 */
	public static char[] toCharArray(long n, final int length, final boolean littleEndian) {
		final char[] chars = new char[length];
		if (littleEndian) {
			for (int i = 0; i < length; i++) {
				chars[i] = (char) (n % 10 + '0');
				n /= 10;
			}
		} else {
			for (int i = length; i-- > 0; ) {
				chars[i] = (char) (n % 10 + '0');
				n /= 10;
			}
		}
		return chars;
	}

	/**
	 * 数字配列をビッグエンディアンの {@code char[]} に変換します。
	 */
	public static char[] toCharArray(final int[] arr) {
		return toCharArray(arr, false);
	}

	/**
	 * 数字配列を {@code char[]} に変換します。
	 *
	 * @param arr          {@code 0} 以上 {@code 9} 以下の数字配列
	 * @param littleEndian リトルエンディアンで出力するか
	 */
	public static char[] toCharArray(final int[] arr, final boolean littleEndian) {
		final int len = arr.length;
		final char[] chars = new char[len];
		if (littleEndian) {
			for (int i = 0; i < len; i++) chars[i] = (char) (arr[len - 1 - i] + '0');
		} else {
			for (int i = 0; i < len; i++) chars[i] = (char) (arr[i] + '0');
		}
		return chars;
	}

	/**
	 * {@code char[]} を各桁の値を格納した {@code int[]} に変換します。
	 */
	public static int[] toIntArray(final char[] arr) {
		final int len = arr.length;
		final int[] result = new int[len];
		for (int i = 0; i < len; i++) result[i] = arr[i] - '0';
		return result;
	}

	/**
	 * {@link String} を各桁の値を格納した {@code int[]} に変換します。
	 */
	public static int[] toIntArray(final String s) {
		final int len = s.length();
		final int[] result = new int[len];
		for (int i = 0; i < len; i++) result[i] = s.charAt(i) - '0';
		return result;
	}
}
