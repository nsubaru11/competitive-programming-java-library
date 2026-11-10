package example.util;

import java.math.*;

import lib.util.*;

public final class FormatUtilsExample {

	public static void main(final String[] args) {
		// 結合
		System.out.println(FormatUtils.join(new int[]{1, 2, 3}));
		System.out.println(FormatUtils.join(new long[]{1, 2, 3}, ","));
		System.out.println();

		// 2次元
		System.out.print(FormatUtils.joinLines(new int[][]{{1, 2}, {3, 4}}));
		System.out.println(FormatUtils.joinLines(new long[][]{{1, 2}, {3, 4}}, ",", ";"));
		System.out.println();

		// 小数
		System.out.println(FormatUtils.formatDouble(12, 3));
		System.out.println(FormatUtils.formatDouble(1.25, 1, RoundingMode.HALF_UP));
		System.out.println(FormatUtils.formatDouble(-1.25, 1, RoundingMode.FLOOR));
		System.out.println();

		// 桁揃え
		System.out.println(FormatUtils.toPaddedString(7, 3));
		System.out.println(FormatUtils.toPaddedString(31, 4, 16));
		System.out.println(FormatUtils.toPaddedString(31, 8, 2, '0'));
		System.out.println(FormatUtils.toPaddedString(7, 4, '_'));
	}
}
