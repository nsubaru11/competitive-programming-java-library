package example.search;

import lib.search.*;

public final class ArrayBinarySearchExample {

	public static void main(final String[] args) {
		// ソート済み配列
		int[] a = {10, 20, 30, 30, 50, 70, 70, 70};

		// 下限・上限
		System.out.println(ArrayBinarySearch.lowerBoundSearch(a, 30));
		System.out.println(ArrayBinarySearch.upperBoundSearch(a, 30));
		System.out.println(ArrayBinarySearch.upperBoundSearch(a, 70));
		System.out.println();

		// 見つからないとき
		int i = ArrayBinarySearch.lowerBoundSearch(a, 40);
		System.out.println(i);
		System.out.println(~i);
		System.out.println();

		// 区間指定
		System.out.println(ArrayBinarySearch.lowerBoundSearch(a, 4, 8, 70));
		System.out.println();

		// 包含・個数
		System.out.println(ArrayBinarySearch.contains(a, 50));
		System.out.println(ArrayBinarySearch.count(a, 70));
		System.out.println(ArrayBinarySearch.count(a, 40));
	}
}
