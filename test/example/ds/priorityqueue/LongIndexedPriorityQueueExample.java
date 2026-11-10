package example.ds.priorityqueue;

import lib.ds.priorityqueue.*;

public final class LongIndexedPriorityQueueExample {

	public static void main(final String[] args) {
		// 追加
		LongIndexedPriorityQueue pq = new LongIndexedPriorityQueue(5);
		pq.add(0, 30);
		pq.add(1, 10);
		pq.add(2, 20);
		System.out.println(pq.size());
		System.out.println();

		// 先頭
		System.out.println(pq.peekIndex());
		System.out.println(pq.peek());
		System.out.println();

		// コストの更新
		pq.relax(0, 5);
		pq.relax(2, 25);
		pq.set(3, 15);
		System.out.println(pq.get(0));
		System.out.println(pq.get(2));
		System.out.println();

		// 添字の状態
		System.out.println(pq.containsIndex(4));
		System.out.println(pq.hasCost(3));
		System.out.println();

		// 取り出し
		while (!pq.isEmpty()) {
			int i = pq.pollIndex();
			System.out.println(i + " " + pq.getCost(i));
		}
		System.out.println();

		// 取り出し済み
		System.out.println(pq.containsIndex(1));
		System.out.println(pq.hasCost(1));
	}
}
