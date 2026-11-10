package example.ds.sparsetable;

import lib.ds.sparsetable.*;

public final class SparseTableExample {

	public static void main(final String[] args) {
		// 辞書順最大
		String[] a = {"pear", "apple", "fig", "kiwi", "banana"};
		SparseTable<String> max = new SparseTable<>(a, (x, y) -> x.compareTo(y) >= 0 ? x : y);

		// 区間の集計 [l, r)
		System.out.println(max.query(0, 2));
		System.out.println(max.query(1, 4));
		System.out.println(max.query(4, 5));
		System.out.println();

		// 最大公約数
		Integer[] b = {12, 18, 24, 9, 30};
		SparseTable<Integer> gcd = new SparseTable<>(b, SparseTableExample::gcd);
		System.out.println(gcd.query(0, 3));
		System.out.println(gcd.query(0, 4));
	}

	private static int gcd(final int a, final int b) {
		return b == 0 ? a : gcd(b, a % b);
	}
}
