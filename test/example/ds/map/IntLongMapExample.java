package example.ds.map;

import java.util.*;

import lib.ds.map.*;

public final class IntLongMapExample {

	public static void main(final String[] args) {
		// 追加・取得
		IntLongMap map = new IntLongMap();
		map.put(1, 10);
		map.put(2, 20);
		System.out.println(map.get(1));
		System.out.println(map.get(3));
		System.out.println(map.containsKey(2));
		System.out.println();

		// 加算
		map.increment(5);
		map.increment(5);
		map.add(1, 5);
		System.out.println(map.get(5));
		System.out.println(map.get(1));
		System.out.println();

		// 最小・最大・合成
		map.mergeMin(2, 15);
		map.mergeMax(2, 12);
		map.merge(5, 3, (x, y) -> x * y);
		System.out.println(map.get(2));
		System.out.println(map.get(5));
		System.out.println();

		// 未登録のときだけ
		map.putIfAbsent(1, 99);
		map.computeIfAbsent(7, k -> 100);
		System.out.println(map.get(1));
		System.out.println(map.get(7));
		System.out.println();

		// 削除・走査
		System.out.println(map.remove(7));
		System.out.println(map.size());
		System.out.println(Arrays.deepToString(map.entries()));
		System.out.println(map.reduceValues(0, Long::sum));
	}
}
