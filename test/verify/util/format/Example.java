package verify.util.format;

import static java.lang.System.*;

import java.math.*;

import lib.util.*;

public final class Example {

	public static void main(String[] args) {
		out.println("===== join =====");
		out.println(FormatUtils.join(new int[]{1, 2, 3}));
		out.println(FormatUtils.join(new long[]{1, 2, 3}, ","));
		out.println("empty: [" + FormatUtils.join(new int[0]) + "]");

		out.println("===== joinLines =====");
		out.print(FormatUtils.joinLines(new int[][]{{1, 2}, {3, 4}}));
		out.println(FormatUtils.joinLines(new long[][]{{1, 2}, {3, 4}}, ",", ";"));
		out.println("empty row: " + FormatUtils.joinLines(new int[][]{{1}, {}, {2}}, ",", "|"));

		out.println("===== formatDouble =====");
		for (final RoundingMode mode : RoundingMode.values()) {
			try {
				out.println(mode + ": " + FormatUtils.formatDouble(1.25, 1, mode));
			} catch (ArithmeticException e) {
				out.println(mode + ": rounding required");
			}
		}
		out.println("fixed width: " + FormatUtils.formatDouble(12, 3));
		out.println("negative: " + FormatUtils.formatDouble(-1.25, 1, RoundingMode.FLOOR));

		out.println("===== toPaddedString =====");
		out.println(FormatUtils.toPaddedString(7, 3));
		out.println(FormatUtils.toPaddedString(-7, 4));
		out.println(FormatUtils.toPaddedString(31, 4, 16));
		out.println(FormatUtils.toPaddedString(31, 5, 2, '0'));
		out.println(FormatUtils.toPaddedString(7, 4, '_'));
	}

}
