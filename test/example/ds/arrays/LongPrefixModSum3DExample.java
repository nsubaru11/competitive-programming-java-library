package example.ds.arrays;

import lib.ds.arrays.*;

public final class LongPrefixModSum3DExample {

	public static void main(final String[] args) {
		// 構築
		long[][][] a = {{{1, 2}, {3, 4}}, {{5, 6}, {7, 8}}};
		LongPrefixModSum3D ps = new LongPrefixModSum3D(a, 10);
		System.out.println(ps.size());
		System.out.println();

		// 直方体の和の剰余
		System.out.println(ps.sum(0, 0, 1, 1, 1, 1));
		System.out.println(ps.sum(1, 0, 0, 1, 1, 1));
		System.out.println();

		// 原点からの和の剰余
		System.out.println(ps.sum(0, 1, 1));
		System.out.println(ps.sumAll());
		System.out.println();

		// 元の値
		System.out.println(ps.get(1, 1, 0));
		System.out.println(ps.getOrDefault(2, 0, 0, -1));
		System.out.println();

		// 初期化関数
		LongPrefixModSum3D big = new LongPrefixModSum3D(2, 2, 2, 1_000_000_007, (i, j, k) -> 1_000_000_000);
		System.out.println(big.sumAll());
	}
}
