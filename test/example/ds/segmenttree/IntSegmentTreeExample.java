package example.ds.segmenttree;

import lib.ds.segmenttree.*;

public final class IntSegmentTreeExample {

	public static void main(final String[] args) {
		// 区間最小
		IntSegmentTree seg = new IntSegmentTree(new int[]{5, 3, 7, 9, 1, 4}, Math::min, Integer.MAX_VALUE);
		System.out.println(seg);
		System.out.println();

		// 区間の集計 [l, r)
		System.out.println(seg.query(0, 3));
		System.out.println(seg.query(2, 6));
		System.out.println(seg.queryAll());
		System.out.println();

		// 点更新
		seg.set(4, 8);
		seg.add(1, 10);
		System.out.println(seg.get(1));
		System.out.println(seg);
		System.out.println();

		// 二分探索
		System.out.println(seg.maxRight(0, x -> x >= 5));
		System.out.println(seg.minLeft(6, x -> x >= 4));
		System.out.println();

		// 区間和
		IntSegmentTree sum = new IntSegmentTree(5, (a, b) -> a + b, 0);
		sum.setAll(i -> i + 1);
		System.out.println(sum.query(1, 4));
	}
}
