package example.sort;

import java.util.*;

import lib.sort.*;

public final class GnomeSortExample {

	public static void main(final String[] args) {
		// 配列
		int[] a = {5, 2, 9, 1, 5, 6, 3};
		System.out.println(Arrays.toString(a));
		System.out.println();

		// 基本形
		int[] b = a.clone();
		GnomeSort.basicGnomeSort(b);
		System.out.println(Arrays.toString(b));
		System.out.println();

		// 改良版
		b = a.clone();
		GnomeSort.gnomeSort(b);
		System.out.println(Arrays.toString(b));
	}
}
