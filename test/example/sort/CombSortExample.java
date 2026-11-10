package example.sort;

import java.util.*;

import lib.sort.*;

public final class CombSortExample {

	public static void main(final String[] args) {
		// 配列
		int[] a = {5, 2, 9, 1, 5, 6, 3};
		System.out.println(Arrays.toString(a));
		System.out.println();

		// 標準形
		int[] b = a.clone();
		CombSort.standardCombSort(b);
		System.out.println(Arrays.toString(b));
		System.out.println();

		// 改良版
		b = a.clone();
		CombSort.optimizedCombSort(b);
		System.out.println(Arrays.toString(b));
	}
}
