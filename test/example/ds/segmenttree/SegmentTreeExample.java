package example.ds.segmenttree;

import lib.ds.segmenttree.*;

public final class SegmentTreeExample {

	public static void main(final String[] args) {
		// 文字列の連結
		SegmentTree<String> seg = new SegmentTree<>(new String[]{"a", "b", "c", "d", "e"}, String::concat, "");
		System.out.println(seg);
		System.out.println();

		// 区間の集計 [l, r)
		System.out.println(seg.query(1, 4));
		System.out.println(seg.queryAll());
		System.out.println();

		// 点更新
		seg.set(2, "X");
		seg.apply(0, "!", String::concat);
		System.out.println(seg.get(0));
		System.out.println(seg.queryAll());
		System.out.println();

		// 二分探索
		System.out.println(seg.maxRight(0, s -> s.length() <= 3));
		System.out.println(seg.minLeft(5, s -> !s.contains("X")));
		System.out.println();

		// 区間最大
		SegmentTree<Integer> max = new SegmentTree<>(5, Math::max, Integer.MIN_VALUE);
		max.setAll(i -> (i * 7) % 5);
		System.out.println(max);
		System.out.println(max.query(1, 3));
	}
}
