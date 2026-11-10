package example.ds.fenwick;

import lib.ds.fenwick.*;

public final class IntRangeBITExample {

	public static void main(final String[] args) {
		// 構築
		IntRangeBIT bit = new IntRangeBIT(new int[]{1, 2, 3, 4, 5});
		System.out.println(bit);
		System.out.println();

		// 区間加算 [i, j]
		bit.add(1, 3, 10);
		System.out.println(bit);
		System.out.println();

		// 区間和 [i, j]
		System.out.println(bit.sum(1, 2));
		System.out.println(bit.sum(3));
		System.out.println(bit.sumAll());
		System.out.println();

		// 点更新
		bit.set(0, 100);
		bit.add(4, -5);
		System.out.println(bit.get(0));
		System.out.println(bit.get(4));
		System.out.println(bit);
	}
}
