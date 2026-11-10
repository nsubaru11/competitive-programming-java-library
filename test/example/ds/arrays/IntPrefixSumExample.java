package example.ds.arrays;

import lib.ds.arrays.*;

public final class IntPrefixSumExample {

	public static void main(final String[] args) {
		// 構築
		IntPrefixSum ps = new IntPrefixSum(new int[]{3, 1, 4, 1, 5, 9});
		System.out.println(ps);
		System.out.println(ps.size());
		System.out.println();

		// 区間和 [i, j]
		System.out.println(ps.sum(1, 3));
		System.out.println(ps.sum(2));
		System.out.println(ps.sumAll());
		System.out.println();

		// 元の値
		System.out.println(ps.get(4));
		System.out.println();

		// 初期化関数
		IntPrefixSum sq = new IntPrefixSum(5, i -> i * i);
		System.out.println(sq);
		System.out.println(sq.sum(1, 4));
	}
}
