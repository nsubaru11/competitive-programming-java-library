package example.ds.set;

import java.util.*;

import lib.ds.set.*;

public final class AVLMultisetExample {

	public static void main(final String[] args) {
		// 追加
		AVLMultiset<Integer> ms = new AVLMultiset<>();
		for (int v : new int[]{3, 1, 3, 2, 3}) {
			ms.add(v);
		}
		ms.add(5, 2);
		System.out.println(ms);
		System.out.println(ms.size());
		System.out.println(ms.uniqueSize());
		System.out.println();

		// 重複なし
		System.out.println(Arrays.toString(ms.toUniqueArray()));
		System.out.println();

		// 順位
		System.out.println(ms.getByIndex(3));
		System.out.println(ms.getByUniqueIndex(3));
		System.out.println(ms.indexOf(3));
		System.out.println();

		// 近傍
		System.out.println(ms.lower(3));
		System.out.println(ms.higher(3));
		System.out.println();

		// 削除
		ms.remove(3);
		System.out.println(ms);
		ms.removeAll(3);
		System.out.println(ms);
	}
}
