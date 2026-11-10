package example.ds.fenwick;

import lib.ds.fenwick.*;

public final class BITExample {

	public static void main(final String[] args) {
		// XOR の BIT
		BIT<Integer> bit = new BIT<>(new Integer[]{3, 5, 6, 1}, (a, b) -> a ^ b, (a, b) -> a ^ b, 0);
		System.out.println(bit);
		System.out.println();

		// 区間の集計 [l, r)
		System.out.println(bit.query(1, 3));
		System.out.println(bit.query(2));
		System.out.println(bit.queryAll());
		System.out.println();

		// 点更新
		bit.add(0, 7);
		bit.set(3, 8);
		System.out.println(bit.get(0));
		System.out.println(bit);
		System.out.println();

		// 和の BIT
		BIT<Long> sum = new BIT<>(5, Long::sum, (a, b) -> a - b, 0L);
		sum.setAll(i -> (long) i * i);
		System.out.println(sum.query(1, 4));
	}
}
