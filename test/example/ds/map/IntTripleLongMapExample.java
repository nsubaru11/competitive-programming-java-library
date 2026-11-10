package example.ds.map;

import java.util.*;

import lib.ds.map.*;

public final class IntTripleLongMapExample {

	public static void main(final String[] args) {
		// 追加・取得
		IntTripleLongMap map = new IntTripleLongMap();
		map.put(1, 2, 3, 10);
		map.put(-1, 0, 5, 20);
		System.out.println(map.get(1, 2, 3));
		System.out.println(map.get(3, 2, 1));
		System.out.println(map.containsKey(-1, 0, 5));
		System.out.println();

		// 加算
		map.increment(0, 0, 0);
		map.add(1, 2, 3, 5);
		System.out.println(map.get(0, 0, 0));
		System.out.println(map.get(1, 2, 3));
		System.out.println();

		// 最大・未登録のときだけ
		map.mergeMax(-1, 0, 5, 30);
		map.computeIfAbsent(2, 3, 4, (a, b, c) -> a + b + c);
		System.out.println(map.get(-1, 0, 5));
		System.out.println(map.get(2, 3, 4));
		System.out.println();

		// 削除
		System.out.println(map.remove(2, 3, 4));
		System.out.println(map.size());
		System.out.println();

		// 走査
		System.out.println(Arrays.deepToString(map.entries()));
		System.out.println(map.reduceValues(0, Long::sum));
	}
}
