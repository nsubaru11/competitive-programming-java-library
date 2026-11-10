package example.ds.sparsetable;

import lib.ds.sparsetable.*;

public final class LongSparseTableExample {

	public static void main(final String[] args) {
		// 構築
		long[] a = {5, 3, 7, 9, 1, 4};
		LongSparseTable min = new LongSparseTable(a, Math::min);
		LongSparseTable max = new LongSparseTable(a, Math::max);

		// 区間最小 [l, r)
		System.out.println(min.query(0, 3));
		System.out.println(min.query(2, 6));
		System.out.println();

		// 区間最大 [l, r)
		System.out.println(max.query(0, 3));
		System.out.println(max.query(4, 6));
		System.out.println();

		// 区間 OR
		LongSparseTable or = new LongSparseTable(new long[]{1, 2, 4, 8}, (x, y) -> x | y);
		System.out.println(or.query(1, 3));
		System.out.println(or.query(0, 4));
	}
}
