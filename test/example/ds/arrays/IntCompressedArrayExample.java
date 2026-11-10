package example.ds.arrays;

import java.util.*;

import lib.ds.arrays.*;

public final class IntCompressedArrayExample {

	public static void main(final String[] args) {
		// 圧縮
		int[] a = {50, 10, 30, 10, 90};
		IntCompressedArray ca = new IntCompressedArray(a);
		System.out.println(ca);
		System.out.println(ca.uniqueSize);
		System.out.println();

		// 値と順位
		System.out.println(ca.rankOfValue(30));
		System.out.println(ca.valueOfRank(3));
		System.out.println(ca.containsValue(20));
		System.out.println(ca.count(10));
		System.out.println();

		// 復元
		System.out.println(Arrays.toString(ca.restore()));
		System.out.println(Arrays.toString(ca.sorted()));
		System.out.println();

		// 順位の付け方
		System.out.println(new IntCompressedArray(a, RankType.COMPETITION, true));
		System.out.println(new IntCompressedArray(a, RankType.MODIFIED_COMPETITION, true));
	}
}
