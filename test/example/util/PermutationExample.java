package example.util;

import java.util.*;

import lib.util.*;

public final class PermutationExample {

	public static void main(final String[] args) {
		// 全列挙
		int[] a = {1, 2, 3};
		do {
			System.out.println(Arrays.toString(a));
		} while (Permutation.next(a));
		System.out.println();

		// 辞書順位置
		int[] b = {3, 1, 2};
		System.out.println(Permutation.index(b));
		System.out.println();

		// 前の順列
		Permutation.prev(b);
		System.out.println(Arrays.toString(b));
		System.out.println();

		// 重複あり
		char[] c = "aab".toCharArray();
		do {
			System.out.println(new String(c));
		} while (Permutation.next(c));
	}
}
