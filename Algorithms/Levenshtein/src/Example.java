import static java.lang.System.out;

/**
 * Levenshteinクラスの使用例を示すサンプルコードです。
 * 文字列、文字配列、整数配列に対する編集距離計算の様々なケースを実演します。
 */
public class Example {

	public static void main(String[] args) {
		// 文字列の編集距離計算の例
		demonstrateStringEditDistance();

		// 文字配列の編集距離計算の例
		demonstrateCharArrayEditDistance();

		// 整数配列の編集距離計算の例
		demonstrateIntArrayEditDistance();
	}

	/**
	 * 文字列間の編集距離計算の例を示します。
	 */
	private static void demonstrateStringEditDistance() {
		out.println("===== 文字列の編集距離計算 =====");

		// 基本的な編集距離の計算
		String s1 = "kitten";
		String s2 = "sitting";
		int distance = LevenshteinDP.computeEditDistance(s1, s2);
		out.println("「" + s1 + "」と「" + s2 + "」の編集距離: " + distance);

		// 同一文字列の編集距離
		String s3 = "hello";
		String s4 = "hello";
		out.println("「" + s3 + "」と「" + s4 + "」の編集距離: " + LevenshteinDP.computeEditDistance(s3, s4));

		// 1文字違いの編集距離
		String s5 = "hello";
		String s6 = "hallo";
		out.println("「" + s5 + "」と「" + s6 + "」の編集距離: " + LevenshteinDP.computeEditDistance(s5, s6));

		// 最大許容距離を指定した編集距離の計算
		out.println("\n----- 最大許容距離を指定した場合 -----");

		// 距離が許容範囲内の場合
		int maxDistance1 = 3;
		int result1 = LevenshteinDP.computeEditDistance(s1, s2, maxDistance1);
		out.println("「" + s1 + "」と「" + s2 + "」の編集距離 (最大許容距離=" + maxDistance1 + "): " + result1);

		// 距離が許容範囲を超える場合
		int maxDistance2 = 2;
		int result2 = LevenshteinDP.computeEditDistance(s1, s2, maxDistance2);
		out.println("「" + s1 + "」と「" + s2 + "」の編集距離 (最大許容距離=" + maxDistance2 + "): " + result2);

		// 長さの差が許容範囲を超える場合
		String s7 = "a";
		String s8 = "abcdefg";
		int maxDistance3 = 3;
		int result3 = LevenshteinDP.computeEditDistance(s7, s8, maxDistance3);
		out.println("「" + s7 + "」と「" + s8 + "」の編集距離 (最大許容距離=" + maxDistance3 + "): " + result3);

		out.println();
	}

	/**
	 * 文字配列間の編集距離計算の例を示します。
	 */
	private static void demonstrateCharArrayEditDistance() {
		out.println("===== 文字配列の編集距離計算 =====");

		// 基本的な編集距離の計算
		char[] c1 = {'a', 'c', 'e', 'b', 'd', 'a', 'b', 'e', 'd'};
		char[] c2 = {'a', 'c', 'b', 'd', 'e', 'a', 'c', 'b', 'e', 'd'};
		int distance = LevenshteinDP.computeEditDistance(c1, c2);
		int distance2 = Wu.computeEditDistance(c2, c1);
		out.println("文字配列1と文字配列2の編集距離: " + distance + " " + distance2);

		// 複数の編集操作が必要な場合
		char[] c3 = {'w', 'o', 'r', 'l', 'd'};
		char[] c4 = {'w', 'e', 'a', 'r'};
		out.println("文字配列3と文字配列4の編集距離: " + LevenshteinDP.computeEditDistance(c3, c4));

		// 最大許容距離を指定した編集距離の計算
		out.println("\n----- 最大許容距離を指定した場合 -----");

		// 距離が許容範囲内の場合
		int maxDistance1 = 1;
		int result1 = LevenshteinDP.computeEditDistance(c1, c2, maxDistance1);
		out.println("文字配列1と文字配列2の編集距離 (最大許容距離=" + maxDistance1 + "): " + result1);

		// 距離が許容範囲を超える場合
		int maxDistance2 = 2;
		int result2 = LevenshteinDP.computeEditDistance(c3, c4, maxDistance2);
		out.println("文字配列3と文字配列4の編集距離 (最大許容距離=" + maxDistance2 + "): " + result2);

		out.println();
	}

	/**
	 * 整数配列間の編集距離計算の例を示します。
	 */
	private static void demonstrateIntArrayEditDistance() {
		out.println("===== 整数配列の編集距離計算 =====");

		// 基本的な編集距離の計算
		int[] a1 = {1, 2, 3, 4, 5};
		int[] a2 = {1, 3, 4, 5, 6};
		int distance = LevenshteinDP.computeEditDistance(a1, a2);
		out.println("整数配列1と整数配列2の編集距離: " + distance);

		// 同一配列の編集距離
		int[] a3 = {10, 20, 30, 40};
		int[] a4 = {10, 20, 30, 40};
		out.println("整数配列3と整数配列4の編集距離: " + LevenshteinDP.computeEditDistance(a3, a4));

		// 複数の編集操作が必要な場合
		int[] a5 = {5, 10, 15, 20, 25};
		int[] a6 = {5, 15, 25, 35};
		out.println("整数配列5と整数配列6の編集距離: " + LevenshteinDP.computeEditDistance(a5, a6));

		// 最大許容距離を指定した編集距離の計算
		out.println("\n----- 最大許容距離を指定した場合 -----");

		// 距離が許容範囲内の場合
		int maxDistance1 = 2;
		int result1 = LevenshteinDP.computeEditDistance(a1, a2, maxDistance1);
		out.println("整数配列1と整数配列2の編集距離 (最大許容距離=" + maxDistance1 + "): " + result1);

		// 距離が許容範囲を超える場合
		int maxDistance2 = 2;
		int result2 = LevenshteinDP.computeEditDistance(a5, a6, maxDistance2);
		out.println("整数配列5と整数配列6の編集距離 (最大許容距離=" + maxDistance2 + "): " + result2);

		out.println();
	}
}
