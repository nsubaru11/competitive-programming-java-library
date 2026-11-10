package example.util;

import java.util.*;

import lib.util.*;

public final class ConversionsExample {

	public static void main(final String[] args) {
		// 数字の文字配列から数値
		System.out.println(Conversions.toInt("00121".toCharArray()));
		System.out.println(Conversions.toInt("00121".toCharArray(), true));
		System.out.println(Conversions.toLong("9876543210".toCharArray()));
		System.out.println();

		// 数字の配列から数値
		System.out.println(Conversions.toInt(new int[]{1, 2, 3}));
		System.out.println(Conversions.toInt(new int[]{1, 2, 3}, true));
		System.out.println();

		// 数値から文字配列
		System.out.println(new String(Conversions.toCharArray(121)));
		System.out.println(new String(Conversions.toCharArray(121, 5)));
		System.out.println(new String(Conversions.toCharArray(121, 5, true)));
		System.out.println();

		// 文字配列と数字の配列
		System.out.println(Arrays.toString(Conversions.toIntArray("2026")));
		System.out.println(new String(Conversions.toCharArray(new int[]{4, 2})));
	}
}
