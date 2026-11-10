package example.ds.arrays;

import lib.ds.arrays.*;

public final class LongPrefixModSumExample {

	public static void main(final String[] args) {
		// 構築
		LongPrefixModSum ps = new LongPrefixModSum(new long[]{5, 6, 3, 4, 2}, 7);
		System.out.println(ps);
		System.out.println();

		// 区間和 [i, j] の剰余
		System.out.println(ps.sum(1, 3));
		System.out.println(ps.sum(2));
		System.out.println(ps.sumAll());
		System.out.println();

		// 元の値
		System.out.println(ps.get(1));
		System.out.println();

		// 初期化関数
		LongPrefixModSum pow = new LongPrefixModSum(4, 1_000_000_007, i -> 1_000_000_000);
		System.out.println(pow.sumAll());
	}
}
