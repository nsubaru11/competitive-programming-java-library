package example.ds.set;

import java.util.*;

import lib.ds.set.*;

public final class LongAVLMultisetExample {

	public static void main(final String[] args) {
		// 追加
		LongAVLMultiset ms = new LongAVLMultiset();
		for (long v : new long[]{3, 1, 3, 2, 3}) {
			ms.add(v);
		}
		ms.add(5, 2);
		System.out.println(Arrays.toString(ms.toArray()));
		System.out.println(ms.size());
		System.out.println(ms.uniqueSize());
		System.out.println();

		// 個数・和
		System.out.println(ms.count(3));
		System.out.println(ms.sum());
		System.out.println(ms.uniqueSum());
		System.out.println(ms.prefixSum(3));
		System.out.println();

		// 順位
		System.out.println(ms.getByIndex(3));
		System.out.println(ms.lowerBound(3));
		System.out.println(ms.upperBound(3));
		System.out.println();

		// 近傍
		System.out.println(ms.lower(3));
		System.out.println(ms.higher(3));
		System.out.println();

		// 削除
		ms.remove(3);
		System.out.println(Arrays.toString(ms.toArray()));
		ms.removeAll(3);
		System.out.println(Arrays.toString(ms.toUniqueArray()));
	}
}
