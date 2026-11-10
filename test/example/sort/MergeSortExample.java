package example.sort;

import java.util.*;

import lib.sort.*;

public final class MergeSortExample {

	public static void main(final String[] args) {
		// 配列
		int[] a = {5, 2, 9, 1, 5, 6, 3};
		System.out.println(Arrays.toString(a));
		System.out.println();

		// 整列（新しい配列を返す）
		int[] b = a.clone();
		b = MergeSort.mergeSort(b, b.length);
		System.out.println(Arrays.toString(b));
	}
}
