package example.sort;

import java.util.*;

import lib.sort.*;

public final class SelectionSortExample {

	public static void main(final String[] args) {
		// 配列
		int[] a = {5, 2, 9, 1, 5, 6, 3};
		System.out.println(Arrays.toString(a));
		System.out.println();

		// 片側から選択
		int[] b = a.clone();
		SelectionSort.selectionSort(b);
		System.out.println(Arrays.toString(b));
		System.out.println();

		// 両側から選択
		b = a.clone();
		SelectionSort.doubleSelectionSort(b);
		System.out.println(Arrays.toString(b));
	}
}
