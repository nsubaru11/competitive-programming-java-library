package example.util;

import java.util.*;

import lib.util.*;

public final class ArrayUtilsExample {

	public static void main(final String[] args) {
		// 配列
		int[] a = {3, 1, 4, 1, 5, 9, 2, 6};

		// 集計
		System.out.println(ArrayUtils.sum(a));
		System.out.println(ArrayUtils.min(a) + " " + ArrayUtils.max(a));
		System.out.println(ArrayUtils.argMin(a) + " " + ArrayUtils.argMax(a));
		System.out.println(ArrayUtils.count(a, 1));
		System.out.println(ArrayUtils.uniqueSize(a));
		System.out.println();

		// 判定
		System.out.println(ArrayUtils.isSorted(a));
		System.out.println(ArrayUtils.allEqual(a));
		System.out.println(ArrayUtils.anyMatch(a, x -> x > 8));
		System.out.println();

		// 頻度・mex
		System.out.println(Arrays.toString(ArrayUtils.freq(a, 10)));
		System.out.println(ArrayUtils.mex(a));
		System.out.println();

		// 部分列・窓
		System.out.println(ArrayUtils.lis(a));
		System.out.println(ArrayUtils.lnds(a));
		System.out.println(ArrayUtils.maxWin(a, 3));
		System.out.println();

		// 部分和の個数
		System.out.println(ArrayUtils.subsetMitm(a, 10));
		System.out.println();

		// 反転
		int[] b = a.clone();
		ArrayUtils.reverse(b);
		System.out.println(Arrays.toString(b));
		ArrayUtils.reverse(b, 0, 3);
		System.out.println(Arrays.toString(b));
	}
}
