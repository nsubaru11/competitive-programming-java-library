package example.ds.fenwick;

import lib.ds.fenwick.*;

public final class LongBITExample {

	public static void main(final String[] args) {
		// 構築
		LongBIT bit = new LongBIT(new long[]{3, 1, 4, 1, 5});
		System.out.println(bit);
		System.out.println();

		// 点更新
		bit.add(1, 10);
		bit.set(3, 2);
		System.out.println(bit.get(1));
		System.out.println(bit);
		System.out.println();

		// 区間和 [i, j]
		System.out.println(bit.sum(1, 3));
		System.out.println(bit.sum(2));
		System.out.println(bit.sumAll());
		System.out.println();

		// 乗算・一次関数・任意の演算
		bit.multiply(0, 2);
		bit.affine(4, 2, 1);
		bit.apply(2, 9, Math::max);
		System.out.println(bit);
		System.out.println();

		// 累積和の二分探索
		System.out.println(bit.lowerBound(20));
		System.out.println(bit.upperBound(20));
	}
}
