import java.util.Arrays;

import static java.lang.System.out;

public class Example {

	public static void main(String[] args) {
		// 文字配列・文字列から整数への変換テスト
		testToInt();

		// 整数から文字配列への変換テスト
		testToCharArray();

		// 整数から文字列への変換テスト
		testToString();

		// 整数のソートテスト
		testSort();

		// 配列変換と操作のテスト
		testArrayOperations();
	}

	private static void testToInt() {
		out.println("===== 文字から整数への変換 =====");
		out.print("char[] -> int: ");
		out.println(Master.toInt("1234567890".toCharArray()));
		out.print("String -> int: ");
		out.println(Master.toInt("1234567890"));
		out.println();
	}

	private static void testToCharArray() {
		out.println("===== 整数から文字配列への変換 =====");
		out.print("int: " + 1234567890 + " -> char[]: ");
		out.println(Master.toCharArray(1234567890));

		out.print("int: " + 1234567890 + " -> char[] (桁数 = 8): ");
		out.println(Master.toCharArray(1234567890, 8));
		out.print("int: " + 123 + " -> char[] (桁数 = 8): ");
		out.println(Master.toCharArray(123, 8));
		out.println();
	}

	private static void testToString() {
		out.println("===== 整数から文字列への変換 =====");
		out.print("int -> String :");
		out.println(Master.toString(1234567890));
		out.println();
	}

	private static void testSort() {
		out.println("===== 整数のソート =====");
		int example = 417253219;
		out.println(example + " ~ sort ~ " + Master.sort(example));
		out.println(example + " ~ descendingSort ~ " + Master.descendingSort(example));
		out.println();
	}

	private static void testArrayOperations() {
		out.println("===== 配列操作 =====");

		// 文字配列から整数配列への変換
		char[] c = {'1', '2', '3', '4'};
		int[] ia = Master.toIntArray(c);
		out.println("char[] -> int[]: " + Arrays.toString(ia));

		// 整数配列から文字配列への変換
		char[] c2 = Master.toCharArray(ia);
		out.println("int[] -> char[]: " + Arrays.toString(c2));

		// 文字列から整数配列への変換
		int[] ia2 = Master.toIntArray("5678");
		out.println("String -> int[]: " + Arrays.toString(ia2));

		// 整数配列から文字列への変換
		out.println("int[] -> String: " + Master.toString(ia2));

		// 配列の反転
		out.println("\n配列の反転テスト:");
		out.print("int[] 反転前: " + Arrays.toString(ia2) + " -> 反転後: ");
		Master.reverse(ia2);
		out.println(Arrays.toString(ia2));

		out.print("char[] 反転前: " + Arrays.toString(c2) + " -> 反転後: ");
		Master.reverse(c2);
		out.println(Arrays.toString(c2));

		long[] la = {1, 2, 3, 4, 5};
		out.print("long[] 反転前: " + Arrays.toString(la) + " -> 反転後: ");
		Master.reverse(la);
		out.println(Arrays.toString(la));
	}
}
