package example.util;

import lib.util.*;

public final class DigitUtilsExample {

	public static void main(final String[] args) {
		// 桁数
		System.out.println(DigitUtils.digits10(12345));
		System.out.println(DigitUtils.digits10(9_876_543_210L));
		System.out.println(DigitUtils.digits2(8));
		System.out.println();

		// 反転
		System.out.println(DigitUtils.reverse(12340));
		System.out.println(DigitUtils.reverse(1_234_567_890_123L));
		System.out.println();

		// 桁の並べ替え
		System.out.println(DigitUtils.sort(31415));
		System.out.println(DigitUtils.descendingSort(31415));
		System.out.println(DigitUtils.sort("31415"));
		System.out.println(DigitUtils.descendingSort(1_000_000_007L));
	}
}
