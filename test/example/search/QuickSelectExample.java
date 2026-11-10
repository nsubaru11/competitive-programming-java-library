package example.search;

import lib.search.*;

public final class QuickSelectExample {

	public static void main(final String[] args) {
		// 配列
		int[] a = {9, 1, 8, 2, 7, 3, 6, 4, 5};

		// k 番目に小さい値
		System.out.println(QuickSelect.select(a.clone(), 0));
		System.out.println(QuickSelect.select(a.clone(), 4));
		System.out.println(QuickSelect.select(a.clone(), 8));
	}
}
