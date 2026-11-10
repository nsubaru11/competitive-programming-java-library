package example.string.approximate;

import lib.string.approximate.*;

public final class LevenshteinDPExample {

	public static void main(final String[] args) {
		// 文字列
		System.out.println(LevenshteinDP.computeEditDistance("kitten", "sitting"));
		System.out.println(LevenshteinDP.computeEditDistance("hello", "hello"));
		System.out.println();

		// 上限付き
		System.out.println(LevenshteinDP.computeEditDistance("kitten", "sitting", 3));
		System.out.println(LevenshteinDP.computeEditDistance("kitten", "sitting", 2));
		System.out.println();

		// 文字配列
		char[] c = "world".toCharArray();
		char[] d = "wear".toCharArray();
		System.out.println(LevenshteinDP.computeEditDistance(c, d));
		System.out.println();

		// 整数配列
		int[] a = {1, 2, 3, 4, 5};
		int[] b = {1, 3, 4, 5, 6};
		System.out.println(LevenshteinDP.computeEditDistance(a, b));
	}
}
