package example.ds.arrays;

import lib.ds.arrays.*;

public final class IntPrefixSum2DExample {

	public static void main(final String[] args) {
		// 構築
		int[][] a = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
		IntPrefixSum2D ps = new IntPrefixSum2D(a);
		System.out.println(ps);
		System.out.println();

		// 矩形和 [i1, i2] × [j1, j2]
		System.out.println(ps.sum(1, 1, 2, 2));
		System.out.println(ps.sum(0, 0, 0, 2));
		System.out.println();

		// 左上からの和
		System.out.println(ps.sum(1, 1));
		System.out.println(ps.sumAll());
		System.out.println();

		// 元の値
		System.out.println(ps.get(2, 1));
		System.out.println(ps.inRange(3, 0));
		System.out.println(ps.getOrDefault(3, 0, -1));
		System.out.println();

		// 初期化関数
		IntPrefixSum2D ones = new IntPrefixSum2D(3, 4, (i, j) -> 1);
		System.out.println(ones.sum(0, 1, 2, 3));
	}
}
