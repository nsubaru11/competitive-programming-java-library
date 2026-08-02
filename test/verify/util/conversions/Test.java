package verify.util.conversions;

import static java.lang.System.*;

import java.util.*;

import lib.util.*;

public final class Test {

	public static void main(String[] args) {
		// 文字配列・文字列から整数への変換テスト
		testToInt();

		// エンディアン指定テスト
		testEndian();

		// 整数から文字配列への変換テスト
		testToCharArray();

		// 整数のソートテスト
		testSort();

		// 配列変換と操作のテスト
		testArrayOperations();

		// 境界値・API仕様のテスト
		testContracts();
	}

	private static void testToInt() {
		out.println("===== 文字から整数への変換 =====");
		out.print("char[] -> int: ");
		out.println(Conversions.toInt("1234567890".toCharArray()));
		out.println();
	}

	private static void testToCharArray() {
		out.println("===== 整数から文字配列への変換 =====");
		out.print("int: " + 1234567890 + " -> char[]: ");
		out.println(Conversions.toCharArray(1234567890));

		out.print("int: " + 1234567890 + " -> char[] (桁数 = 8): ");
		out.println(Conversions.toCharArray(1234567890, 8));
		out.print("int: " + 123 + " -> char[] (桁数 = 8): ");
		out.println(Conversions.toCharArray(123, 8));
		out.println();
	}

	private static void testSort() {
		out.println("===== 整数のソート =====");
		int example = 417253219;
		out.println(example + " ~ sort ~ " + DigitUtils.sort(example));
		out.println(example + " ~ descendingSort ~ " + DigitUtils.descendingSort(example));
		out.println();
	}

	private static void testEndian() {
		out.println("===== エンディアン指定 =====");
		char[] chars = {'0', '0', '1', '2', '1'};
		out.println("big endian: " + Conversions.toInt(chars));
		out.println("little endian: " + Conversions.toInt(chars, true));
		out.println("big endian: " + new String(Conversions.toCharArray(121, 5)));
		out.println("little endian: " + new String(Conversions.toCharArray(121, 5, true)));
		out.println();
	}

	private static void testArrayOperations() {
		out.println("===== 配列操作 =====");

		// 文字配列から整数配列への変換
		char[] c = {'1', '2', '3', '4'};
		int[] ia = Conversions.toIntArray(c);
		out.println("char[] -> int[]: " + Arrays.toString(ia));

		// 整数配列から文字配列への変換
		char[] c2 = Conversions.toCharArray(ia);
		out.println("int[] -> char[]: " + Arrays.toString(c2));

		// 文字列から整数配列への変換
		int[] ia2 = Conversions.toIntArray("5678");
		out.println("String -> int[]: " + Arrays.toString(ia2));

		// 整数配列から文字列への変換
		out.println("int[] -> String: " + FormatUtils.join(ia2));

		// 配列の反転
		out.println("\n配列の反転テスト:");
		out.print("int[] 反転前: " + Arrays.toString(ia2) + " -> 反転後: ");
		ArrayUtils.reverse(ia2);
		out.println(Arrays.toString(ia2));

		out.print("char[] 反転前: " + Arrays.toString(c2) + " -> 反転後: ");
		ArrayUtils.reverse(c2);
		out.println(Arrays.toString(c2));

		long[] la = {1, 2, 3, 4, 5};
		out.print("long[] 反転前: " + Arrays.toString(la) + " -> 反転後: ");
		ArrayUtils.reverse(la);
		out.println(Arrays.toString(la));
	}

	private static void testContracts() {
		final char[] chars = "00121".toCharArray();
		final int[] digits = {0, 0, 1, 2, 1};

		check(Conversions.toLong(chars) == 121, "big-endian char[] -> long");
		check(Conversions.toLong(chars, true) == 12100, "little-endian char[] -> long");
		check(Conversions.toInt(digits) == 121, "big-endian int[] -> int");
		check(Conversions.toInt(digits, true) == 12100, "little-endian int[] -> int");
		check("00121".equals(new String(Conversions.toCharArray(121, 5))), "big-endian int -> char[]");
		check("12100".equals(new String(Conversions.toCharArray(121, 5, true))), "little-endian int -> char[]");
		check("21".equals(new String(Conversions.toCharArray(121, 2))), "char[] truncation");
		check("12".equals(new String(Conversions.toCharArray(121, 2, true))), "little-endian truncation");
		check("0".equals(new String(Conversions.toCharArray(0))), "zero conversion");
		check(Arrays.equals(Conversions.toIntArray("00121"), digits), "String -> int[]");

		check(DigitUtils.digits2(0) == 1, "binary digits of zero");
		check(DigitUtils.digits2(Integer.MAX_VALUE) == 31, "binary digits of int max");
		check(DigitUtils.digits2(Long.MAX_VALUE) == 63, "binary digits of long max");
		check(DigitUtils.digits10(0) == 1, "decimal digits of zero");
		check(DigitUtils.digits10(1_000) == 4, "decimal digits at power of ten");
		check(DigitUtils.digits10(Long.MAX_VALUE) == 19, "decimal digits of long max");
		check(DigitUtils.reverse(1_200) == 21, "int reverse");
		check(DigitUtils.reverse(1_200L) == 21, "long reverse");
		check(DigitUtils.sort(100) == 1, "int ascending digit sort");
		check(DigitUtils.descendingSort(100) == 100, "int descending digit sort");
		check(DigitUtils.sort(9_876_543_210L) == 123_456_789L, "long ascending digit sort");
		check(DigitUtils.descendingSort(9_876_543_210L) == 9_876_543_210L, "long descending digit sort");
		check("01abc".equals(DigitUtils.sort("cab01")), "String ascending sort");
		check("cba10".equals(DigitUtils.descendingSort("cab01")), "String descending sort");
		out.println("contract checks: OK");
	}

	private static void check(final boolean condition, final String message) {
		if (!condition) throw new AssertionError(message);
	}
}
