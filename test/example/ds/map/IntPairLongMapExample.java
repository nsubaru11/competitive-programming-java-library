package example.ds.map;

import java.util.*;

import lib.ds.map.*;

public final class IntPairLongMapExample {

	public static void main(final String[] args) {
		// 追加・取得
		IntPairLongMap map = new IntPairLongMap();
		map.put(1, 2, 10);
		map.put(-3, 4, 20);
		System.out.println(map.get(1, 2));
		System.out.println(map.get(2, 1));
		System.out.println(map.containsKey(-3, 4));
		System.out.println();

		// 加算
		map.increment(0, 0);
		map.increment(0, 0);
		map.add(1, 2, 5);
		System.out.println(map.get(0, 0));
		System.out.println(map.get(1, 2));
		System.out.println();

		// 最小・未登録のときだけ
		map.mergeMin(-3, 4, 7);
		map.computeIfAbsent(5, 6, (a, b) -> a * b);
		System.out.println(map.get(-3, 4));
		System.out.println(map.get(5, 6));
		System.out.println();

		// 削除
		System.out.println(map.remove(5, 6));
		System.out.println(map.size());
		System.out.println();

		// 走査
		System.out.println(Arrays.deepToString(map.keys()));
		System.out.println(Arrays.toString(map.values()));
		map.forEach((a, b, v) -> System.out.println(a + " " + b + " " + v));
	}
}
