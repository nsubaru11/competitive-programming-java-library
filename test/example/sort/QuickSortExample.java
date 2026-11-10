package example.sort;

import java.util.*;

import lib.sort.*;

public final class QuickSortExample {

	public static void main(final String[] args) {
		// 配列
		int[] a = {5, 2, 9, 1, 5, 6, 3};
		System.out.println(Arrays.toString(a));
		System.out.println();

		// 整列 [left, right]
		int[] b = a.clone();
		QuickSort.quickSort(b, 0, b.length - 1);
		System.out.println(Arrays.toString(b));
		System.out.println();

		// 一部だけ整列
		b = a.clone();
		QuickSort.quickSort(b, 2, 5);
		System.out.println(Arrays.toString(b));
	}
}
