package example.ds.segmenttree;

import lib.ds.segmenttree.*;

public final class LongLazySegmentTreeExample {

	public static void main(final String[] args) {
		// 区間加算・区間最小
		long[] a = {5, 3, 7, 9, 1, 4};
		LongLazySegmentTree seg = new LongLazySegmentTree(a, Math::min, Long.MAX_VALUE, (f, x) -> f + x, (f, g) -> f + g);
		System.out.println(seg);
		System.out.println();

		// 区間作用 [l, r)
		seg.apply(0, 3, 10);
		System.out.println(seg);
		System.out.println(seg.query(0, 4));
		System.out.println(seg.queryAll());
		System.out.println();

		// 点更新
		seg.set(4, 20);
		seg.apply(5, -2);
		System.out.println(seg.get(5));
		System.out.println();

		// 二分探索
		System.out.println(seg.maxRight(0, x -> x >= 9));
		System.out.println();

		// 区間代入（作用素を省略）
		LongLazySegmentTree assign = new LongLazySegmentTree(a, Math::max, Long.MIN_VALUE);
		assign.apply(1, 4, 0);
		System.out.println(assign);
		System.out.println(assign.queryAll());
	}
}
