package example.ds.arrays;

import lib.ds.arrays.*;

public final class LongArrayDequeExample {

	public static void main(final String[] args) {
		// 初期化
		LongArrayDeque dq = new LongArrayDeque();
		System.out.println(dq.isEmpty());
		System.out.println();

		// 追加
		dq.addLast(10L);
		dq.addLast(20L);
		dq.addLast(30L);
		dq.addFirst(5L);
		System.out.println(dq);
		System.out.println(dq.size());
		System.out.println();

		// 先頭・末尾
		System.out.println(dq.getFirst());
		System.out.println(dq.getLast());
		System.out.println();

		// 削除
		System.out.println(dq.removeFirst());
		System.out.println(dq.removeLast());
		System.out.println(dq);
		System.out.println();

		// 添字アクセス
		dq.set(0, 99L);
		System.out.println(dq.get(0));
		System.out.println(dq);
		System.out.println();

		// 反転
		dq.reverse();
		System.out.println(dq);
		System.out.println();

		// 全削除
		dq.clear();
		System.out.println(dq.removeFirstOrDefault(-1L));
	}
}
