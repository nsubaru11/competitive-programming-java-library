package example.search;

import lib.search.*;

public final class BinarySearchExample {

	public static void main(final String[] args) {
		// 比較関数
		BinarySearch.CompareFunction cmp = x -> {
			long k = x * x;
			return k < 100 ? -1 : k > 400 ? 1 : 0;
		};

		// 条件を満たす値
		System.out.println(BinarySearch.normalSearch(0, 100, cmp));
		System.out.println();

		// 最小・最大
		System.out.println(BinarySearch.lowerBoundSearch(0, 100, cmp));
		System.out.println(BinarySearch.upperBoundSearch(0, 100, cmp));
		System.out.println();

		// long の範囲
		System.out.println(BinarySearch.lowerBoundSearch(0L, 1L << 40, x -> Long.compare(x, 123_456_789_012L)));
		System.out.println();

		// 見つからないとき
		int i = BinarySearch.normalSearch(0, 100, x -> Long.compare(x * x, 50));
		System.out.println(i);
		System.out.println(~i);
	}
}
