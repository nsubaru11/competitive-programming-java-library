package example.ds.segmenttree;

import lib.ds.segmenttree.*;

public final class IntSquareSumSegmentTreeExample {

	public static void main(final String[] args) {
		// 構築
		IntSquareSumSegmentTree seg = new IntSquareSumSegmentTree(new int[]{1, 2, 3, 4, 5});
		System.out.println(seg);
		System.out.println();

		// 区間和・区間二乗和 [l, r)
		System.out.println(seg.query(0, 3));
		System.out.println(seg.query2(0, 3));
		System.out.println(seg.queryAll());
		System.out.println(seg.query2All());
		System.out.println();

		// 区間更新
		seg.add(0, 2, 10);
		seg.multiply(2, 4, 2);
		seg.affine(4, 5, 3, 1);
		System.out.println(seg);
		System.out.println();

		// 区間代入
		seg.set(1, 4, 0);
		System.out.println(seg);
		System.out.println(seg.query2All());
		System.out.println();

		// 法の指定
		IntSquareSumSegmentTree mod = new IntSquareSumSegmentTree(new int[]{6, 7}, 10);
		System.out.println(mod.queryAll());
		System.out.println(mod.query2All());
	}
}
