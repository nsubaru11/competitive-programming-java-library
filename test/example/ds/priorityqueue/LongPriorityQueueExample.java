package example.ds.priorityqueue;

import lib.ds.priorityqueue.*;

public final class LongPriorityQueueExample {

	public static void main(final String[] args) {
		// 昇順
		LongPriorityQueue min = new LongPriorityQueue(new long[]{5, 1, 4, 2, 3});
		System.out.println(min.peek());
		System.out.println(min.peekSecond());
		System.out.println();

		// 追加・取り出し
		min.add(0);
		System.out.println(min.poll());
		System.out.println(min.poll());
		System.out.println(min.size());
		System.out.println();

		// 先頭の置換
		System.out.println(min.replaceTop(10));
		System.out.println(min.peek());
		System.out.println();

		// 降順
		LongPriorityQueue max = new LongPriorityQueue(true);
		max.addAll(new long[]{3, 1, 2});
		while (!max.isEmpty()) {
			System.out.println(max.poll());
		}
		System.out.println();

		// 空のとき
		System.out.println(max.pollOrDefault(-1));
	}
}
