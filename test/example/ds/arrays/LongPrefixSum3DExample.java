package example.ds.arrays;

import lib.ds.arrays.*;

public final class LongPrefixSum3DExample {

	public static void main(final String[] args) {
		// 構築
		long[][][] a = {{{1, 2}, {3, 4}}, {{5, 6}, {7, 8}}};
		LongPrefixSum3D ps = new LongPrefixSum3D(a);
		System.out.println(ps.size());
		System.out.println();

		// 直方体の和 [i1, i2] × [j1, j2] × [k1, k2]
		System.out.println(ps.sum(0, 0, 1, 1, 1, 1));
		System.out.println(ps.sum(1, 0, 0, 1, 1, 1));
		System.out.println();

		// 原点からの和
		System.out.println(ps.sum(0, 1, 1));
		System.out.println(ps.sumAll());
		System.out.println();

		// 元の値
		System.out.println(ps.get(1, 1, 0));
		System.out.println(ps.getOrDefault(2, 0, 0, -1));
		System.out.println();

		// 初期化関数
		LongPrefixSum3D cube = new LongPrefixSum3D(3, 3, 3, (i, j, k) -> i + j + k);
		System.out.println(cube.sum(1, 1, 1, 2, 2, 2));
	}
}
