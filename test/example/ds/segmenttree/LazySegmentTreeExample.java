package example.ds.segmenttree;

import lib.ds.segmenttree.*;

public final class LazySegmentTreeExample {

	public static void main(final String[] args) {
		// 区間加算・区間最大
		Long[] a = {5L, 3L, 7L, 9L, 1L};
		LazySegmentTree<Long> seg = new LazySegmentTree<>(a, Math::max, Long.MIN_VALUE, (f, x) -> f + x, (f, g) -> f + g);
		System.out.println(seg);
		System.out.println();

		// 区間作用 [l, r)
		seg.apply(3, 5, 10L);
		System.out.println(seg);
		System.out.println(seg.query(0, 3));
		System.out.println(seg.queryAll());
		System.out.println();

		// 点更新
		seg.set(0, 100L);
		System.out.println(seg.get(0));
		System.out.println();

		// 二分探索
		System.out.println(seg.minLeft(5, x -> x <= 19));
		System.out.println();

		// 区間代入（作用素を省略）
		LazySegmentTree<String> s = new LazySegmentTree<>(new String[]{"a", "b", "c", "d"}, String::concat, "");
		s.apply(1, 3, "z");
		System.out.println(s.queryAll());
	}
}
