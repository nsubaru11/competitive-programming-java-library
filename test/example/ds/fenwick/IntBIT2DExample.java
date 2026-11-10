package example.ds.fenwick;

import java.util.*;

import lib.ds.fenwick.*;

public final class IntBIT2DExample {

	public static void main(final String[] args) {
		// 構築
		IntBIT2D bit = new IntBIT2D(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
		System.out.println(bit);
		System.out.println();

		// 点更新
		bit.add(0, 0, 10);
		bit.set(2, 2, 0);
		System.out.println(bit.get(0, 0));
		System.out.println(bit.get(2, 2));
		System.out.println();

		// 矩形和 [i1, i2] × [j1, j2]
		System.out.println(bit.sum(1, 1, 2, 2));
		System.out.println(bit.sum(1, 1));
		System.out.println(bit.sumAll());
		System.out.println();

		// 一括更新
		bit.setAll((i, j) -> i * 3 + j);
		System.out.println(Arrays.deepToString(bit.toArray2D()));
	}
}
