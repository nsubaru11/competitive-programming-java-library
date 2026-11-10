package example.ds.set;

import lib.ds.set.*;

public final class LongAVLSetExample {

	public static void main(final String[] args) {
		// 追加
		LongAVLSet set = new LongAVLSet();
		for (long v : new long[]{5, 1, 4, 1, 3, 9}) {
			set.add(v);
		}
		System.out.println(set);
		System.out.println(set.size());
		System.out.println();

		// 検索
		System.out.println(set.contains(4));
		System.out.println(set.contains(2));
		System.out.println();

		// 近傍
		System.out.println(set.lower(4));
		System.out.println(set.floor(4));
		System.out.println(set.ceiling(6));
		System.out.println(set.higher(9));
		System.out.println();

		// 順位
		System.out.println(set.getByIndex(2));
		System.out.println(set.indexOf(9));
		System.out.println(set.rank(4));
		System.out.println();

		// 削除
		set.remove(4);
		System.out.println(set.pollFirst());
		System.out.println(set.pollLast());
		System.out.println(set);
	}
}
