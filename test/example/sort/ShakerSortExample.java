package example.sort;

import java.util.*;

import lib.sort.*;

public final class ShakerSortExample {

	public static void main(final String[] args) {
		// 配列
		int[] a = {5, 2, 9, 1, 5, 6, 3};
		System.out.println(Arrays.toString(a));
		System.out.println();

		// 整列
		int[] b = a.clone();
		ShakerSort.shakerSort(b);
		System.out.println(Arrays.toString(b));
	}
}
