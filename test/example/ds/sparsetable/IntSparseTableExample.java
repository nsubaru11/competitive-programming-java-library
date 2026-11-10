package example.ds.sparsetable;

import lib.ds.sparsetable.*;

public final class IntSparseTableExample {

	public static void main(final String[] args) {
		// 構築
		int[] a = {5, 3, 7, 9, 1, 4};
		IntSparseTable min = new IntSparseTable(a, Math::min);
		IntSparseTable max = new IntSparseTable(a, Math::max);

		// 区間最小 [l, r)
		System.out.println(min.query(0, 3));
		System.out.println(min.query(2, 6));
		System.out.println();

		// 区間最大 [l, r)
		System.out.println(max.query(0, 3));
		System.out.println(max.query(4, 6));
		System.out.println();

		// 区間 OR
		IntSparseTable or = new IntSparseTable(new int[]{1, 2, 4, 8}, (x, y) -> x | y);
		System.out.println(or.query(1, 3));
		System.out.println(or.query(0, 4));
	}
}
