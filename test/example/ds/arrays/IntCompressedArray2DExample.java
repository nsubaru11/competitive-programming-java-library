package example.ds.arrays;

import java.util.*;

import lib.ds.arrays.*;

public final class IntCompressedArray2DExample {

	public static void main(final String[] args) {
		// 圧縮
		int[][] a = {{50, 10, 30}, {10, 90, 30}};
		IntCompressedArray2D ca = new IntCompressedArray2D(a);
		System.out.println(ca);
		System.out.println();

		// 値と順位
		System.out.println(ca.get(1, 1));
		System.out.println(ca.rankOfValue(30));
		System.out.println(ca.valueOfRank(0));
		System.out.println(ca.count(30));
		System.out.println();

		// 復元
		System.out.println(Arrays.deepToString(ca.restore()));
		System.out.println(Arrays.toString(ca.sorted()));
		System.out.println();

		// 1始まり
		System.out.println(new IntCompressedArray2D(a, RankType.DENSE, true));
	}
}
