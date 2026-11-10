package example.ds.arrays;

import lib.ds.arrays.*;

public final class LongPrefixSumExample {

	public static void main(final String[] args) {
		// 構築
		LongPrefixSum ps = new LongPrefixSum(new long[]{3, 1, 4, 1, 5, 9});
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
		LongPrefixSum sq = new LongPrefixSum(5, i -> i * i);
		System.out.println(sq);
		System.out.println(sq.sum(1, 4));
	}
}
