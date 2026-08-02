package lib.util;

import java.math.*;

/**
 * 数値や配列を出力用の文字列へ変換するユーティリティです。
 */
public final class FormatUtils {
	private static final long[] POW10 = {1, 10, 100, 1_000, 10_000, 100_000, 1_000_000, 10_000_000, 100_000_000, 1_000_000_000, 10_000_000_000L, 100_000_000_000L, 1_000_000_000_000L, 10_000_000_000_000L, 100_000_000_000_000L, 1_000_000_000_000_000L, 10_000_000_000_000_000L, 100_000_000_000_000_000L, 1_000_000_000_000_000_000L};

	private FormatUtils() {
	}

	// region join

	/**
	 * {@code int[]} を半角スペース区切りの文字列へ変換します。
	 */
	public static String join(final int[] arr) {
		return join(arr, " ");
	}

	/**
	 * {@code int[]} を指定した区切り文字列で連結します。
	 */
	public static String join(final int[] arr, final String delimiter) {
		final int len = arr.length;
		if (len == 0) return "";
		final StringBuilder sb = new StringBuilder(len * 11 + delimiter.length() * (len - 1));
		sb.append(arr[0]);
		for (int i = 1; i < len; i++) sb.append(delimiter).append(arr[i]);
		return sb.toString();
	}

	/**
	 * {@code long[]} を半角スペース区切りの文字列へ変換します。
	 */
	public static String join(final long[] arr) {
		return join(arr, " ");
	}

	/**
	 * {@code long[]} を指定した区切り文字列で連結します。
	 */
	public static String join(final long[] arr, final String delimiter) {
		final int len = arr.length;
		if (len == 0) return "";
		final StringBuilder sb = new StringBuilder(len * 20 + delimiter.length() * (len - 1));
		sb.append(arr[0]);
		for (int i = 1; i < len; i++) sb.append(delimiter).append(arr[i]);
		return sb.toString();
	}

	/**
	 * {@code int[][]} を各行の半角スペース区切り・改行区切りの文字列へ変換します。
	 */
	public static String joinLines(final int[][] arr) {
		return joinLines(arr, " ", "\n");
	}

	/**
	 * {@code int[][]} を指定した要素間区切り・改行区切りの文字列へ変換します。
	 */
	public static String joinLines(final int[][] arr, final String delimiter) {
		return joinLines(arr, delimiter, "\n");
	}

	/**
	 * {@code int[][]} を指定した要素間区切り・行間区切りの文字列へ変換します。
	 *
	 * @param arr           連結する配列
	 * @param delimiter     同じ行の要素間に挿入する文字列
	 * @param lineDelimiter 行間に挿入する文字列
	 */
	public static String joinLines(final int[][] arr, final String delimiter, final String lineDelimiter) {
		final StringBuilder sb = new StringBuilder();
		for (final int[] row : arr) {
			if (row.length > 0) {
				sb.append(row[0]);
				for (int i = 1; i < row.length; i++) sb.append(delimiter).append(row[i]);
			}
			sb.append(lineDelimiter);
		}
		return sb.toString();
	}

	/**
	 * {@code long[][]} を各行の半角スペース区切り・改行区切りの文字列へ変換します。
	 */
	public static String joinLines(final long[][] arr) {
		return joinLines(arr, " ", "\n");
	}

	/**
	 * {@code long[][]} を指定した要素間区切り・改行区切りの文字列へ変換します。
	 */
	public static String joinLines(final long[][] arr, final String delimiter) {
		return joinLines(arr, delimiter, "\n");
	}

	/**
	 * {@code long[][]} を指定した要素間区切り・行間区切りの文字列へ変換します。
	 *
	 * @param arr           連結する配列
	 * @param delimiter     同じ行の要素間に挿入する文字列
	 * @param lineDelimiter 行間に挿入する文字列
	 */
	public static String joinLines(final long[][] arr, final String delimiter, final String lineDelimiter) {
		final StringBuilder sb = new StringBuilder();
		for (final long[] row : arr) {
			if (row.length > 0) {
				sb.append(row[0]);
				for (int i = 1; i < row.length; i++) sb.append(delimiter).append(row[i]);
			}
			sb.append(lineDelimiter);
		}
		return sb.toString();
	}
	// endregion

	// region format double

	/**
	 * {@code double} を小数点以下 {@code digits} 桁の固定形式へ変換します。
	 * {@link RoundingMode#HALF_UP} で丸めます。
	 *
	 * <p>{@link #formatDouble(double, int, RoundingMode)} の簡略版です。
	 */
	public static String formatDouble(double x, int digits) {
		return formatDouble(x, digits, RoundingMode.HALF_UP);
	}

	/**
	 * {@code double} を小数点以下 {@code digits} 桁の固定形式へ変換します。
	 *
	 * <p>{@code digits} は {@code 0} 以上 {@code 18} 以下、{@code x} は有限値、
	 * 丸め後の値は {@code long} で表現可能であることを前提とします。
	 * {@code HALF_UP} は、ちょうど中間の値を絶対値が大きくなる方向へ丸めます。
	 * {@code UNNECESSARY} で丸めが必要になる場合は {@link ArithmeticException} を投げます。
	 *
	 * @param x            変換する値
	 * @param digits       小数点以下の桁数
	 * @param roundingMode 丸め方法
	 */
	public static String formatDouble(final double x, final int digits, final RoundingMode roundingMode) {
		final long scale = POW10[digits];
		final boolean negative = x < 0;
		final double scaledValue = (negative ? -x : x) * scale;
		long scaled = (long) scaledValue;
		final double fraction = scaledValue - scaled;
		if (shouldIncrement(fraction, scaled, negative, roundingMode)) scaled++;

		final long integral = scaled / scale;
		final long fractionDigits = scaled % scale;
		final StringBuilder sb = new StringBuilder(digits + 24);
		if (negative) sb.append('-');
		sb.append(integral);
		if (digits > 0) {
			sb.append('.');
			final String fractionString = Long.toString(fractionDigits);
			sb.repeat("0", Math.max(0, digits - fractionString.length()));
			sb.append(fractionString);
		}
		return sb.toString();
	}

	private static boolean shouldIncrement(final double fraction, final long scaled, final boolean negative, final RoundingMode roundingMode) {
		if (fraction == 0) return false;
		return switch (roundingMode) {
			case DOWN -> false;
			case UP -> true;
			case CEILING -> !negative;
			case FLOOR -> negative;
			case HALF_UP -> fraction >= 0.5;
			case HALF_DOWN -> fraction > 0.5;
			case HALF_EVEN -> fraction > 0.5 || fraction == 0.5 && (scaled & 1) != 0;
			case UNNECESSARY -> throw new ArithmeticException("Rounding necessary");
		};
	}
	// endregion

	// region to padded string

	/**
	 * 数値を指定した最小幅まで左側から指定文字で埋めた文字列へ変換します。
	 * 符号付きの場合、符号は先頭に残します。指定幅を超える値は切り詰めません。
	 */
	public static String toPaddedString(final long num, final int width) {
		return toPaddedString(num, width, 10, '0');
	}

	/**
	 * 数値を指定した最小幅まで左側から指定文字で埋めた文字列へ変換します。
	 */
	public static String toPaddedString(final long num, final int width, final char padding) {
		return toPaddedString(num, width, 10, padding);
	}

	/**
	 * 数値を指定した基数の文字列へ変換し、指定した最小幅まで {@code 0} で埋めます。
	 * 基数は {@code 2} 以上 {@code 36} 以下を前提とします。
	 */
	public static String toPaddedString(final long num, final int width, final int radix) {
		return toPaddedString(num, width, radix, '0');
	}

	/**
	 * 数値を指定した基数の文字列へ変換し、指定した最小幅まで指定文字で埋めます。
	 * 符号付きの場合、符号は先頭に残します。指定幅を超える値は切り詰めません。
	 * 基数は {@code 2} 以上 {@code 36} 以下を前提とします。
	 */
	public static String toPaddedString(final long num, final int width, final int radix, final char padding) {
		final String str = Long.toString(num, radix);
		final int paddingLength = width - str.length();
		if (paddingLength <= 0) return str;
		final int signLength = num < 0 ? 1 : 0;
		final StringBuilder sb = new StringBuilder(width);
		if (signLength != 0) sb.append('-');
		sb.repeat(String.valueOf(padding), paddingLength);
		sb.append(str, signLength, str.length());
		return sb.toString();
	}
	// endregion
}
