package verify.util.format;

import java.math.*;

import lib.util.*;

public final class Test {

	public static void main(String[] args) {
		testJoin();
		testJoinLines();
		testFormatDouble();
		testPaddedString();
	}

	private static void testJoin() {
		check("1 2 3", FormatUtils.join(new int[]{1, 2, 3}));
		check("1,2,3", FormatUtils.join(new long[]{1, 2, 3}, ","));
		check("", FormatUtils.join(new int[0]));
		check("-1 0 9223372036854775807", FormatUtils.join(new long[]{-1, 0, Long.MAX_VALUE}));
	}

	private static void testJoinLines() {
		final int[][] ints = {{1, 2}, {}, {3, 4}};
		final long[][] longs = {{1, 2}, {3, 4}};
		check("1 2\n\n3 4\n", FormatUtils.joinLines(ints));
		check("1,2\n\n3,4\n", FormatUtils.joinLines(ints, ","));
		check("1,2;;3,4;", FormatUtils.joinLines(ints, ",", ";"));
		check("1|2/3|4/", FormatUtils.joinLines(longs, "|", "/"));
		check("", FormatUtils.joinLines(new int[0][]));
	}

	private static void testFormatDouble() {
		check("1.3", FormatUtils.formatDouble(1.25, 1));
		check("1.3", FormatUtils.formatDouble(1.25, 1, RoundingMode.UP));
		check("1.2", FormatUtils.formatDouble(1.25, 1, RoundingMode.DOWN));
		check("1.3", FormatUtils.formatDouble(1.25, 1, RoundingMode.CEILING));
		check("1.2", FormatUtils.formatDouble(1.25, 1, RoundingMode.FLOOR));
		check("1.3", FormatUtils.formatDouble(1.25, 1, RoundingMode.HALF_UP));
		check("1.2", FormatUtils.formatDouble(1.25, 1, RoundingMode.HALF_DOWN));
		check("1.2", FormatUtils.formatDouble(1.25, 1, RoundingMode.HALF_EVEN));
		check("1.4", FormatUtils.formatDouble(1.35, 1, RoundingMode.HALF_EVEN));
		check("-1.2", FormatUtils.formatDouble(-1.25, 1, RoundingMode.CEILING));
		check("-1.3", FormatUtils.formatDouble(-1.25, 1, RoundingMode.FLOOR));
		check("-1.2", FormatUtils.formatDouble(-1.25, 1, RoundingMode.DOWN));
		check("-1.3", FormatUtils.formatDouble(-1.25, 1, RoundingMode.UP));
		check("12", FormatUtils.formatDouble(12, 0));
		check("12.000", FormatUtils.formatDouble(12, 3));
		check("10.00", FormatUtils.formatDouble(9.999, 2));
		check("-10.00", FormatUtils.formatDouble(-9.999, 2));
		check("1.2", FormatUtils.formatDouble(1.2, 1, RoundingMode.UNNECESSARY));
		try {
			FormatUtils.formatDouble(1.25, 1, RoundingMode.UNNECESSARY);
			throw new AssertionError("UNNECESSARY did not throw");
		} catch (ArithmeticException expected) {
		}
	}

	private static void testPaddedString() {
		check("007", FormatUtils.toPaddedString(7, 3));
		check("-007", FormatUtils.toPaddedString(-7, 4));
		check("001f", FormatUtils.toPaddedString(31, 4, 16));
		check("11111", FormatUtils.toPaddedString(31, 5, 2));
		check("0001f", FormatUtils.toPaddedString(31, 5, 16, '0'));
		check("___7", FormatUtils.toPaddedString(7, 4, '_'));
		check("-__7", FormatUtils.toPaddedString(-7, 4, '_'));
		check("-001f", FormatUtils.toPaddedString(-31, 5, 16));
		check(Long.toString(Long.MIN_VALUE), FormatUtils.toPaddedString(Long.MIN_VALUE, 20));
	}

	private static void check(final String expected, final String actual) {
		if (!expected.equals(actual)) throw new AssertionError(expected + " != " + actual);
	}
}
