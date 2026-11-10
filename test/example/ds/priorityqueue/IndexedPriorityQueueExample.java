package example.ds.priorityqueue;

import lib.ds.priorityqueue.*;

public final class IndexedPriorityQueueExample {

	public static void main(final String[] args) {
		// 追加
		IndexedPriorityQueue<String> pq = new IndexedPriorityQueue<>(4);
		pq.add(0, "pear");
		pq.add(1, "apple");
		pq.add(2, "fig");
		System.out.println(pq.size());
		System.out.println();

		// 先頭
		System.out.println(pq.peekIndex());
		System.out.println(pq.peek());
		System.out.println(pq.peekSecondIndex());
		System.out.println();

		// コストの更新
		pq.relax(0, "banana");
		pq.set(1, "kiwi");
		System.out.println(pq.get(0));
		System.out.println(pq.get(1));
		System.out.println();

		// 削除
		pq.remove(2);
		System.out.println(pq.containsIndex(2));
		System.out.println(pq.getCost(2));
		System.out.println();

		// 取り出し
		while (!pq.isEmpty()) {
			int i = pq.pollIndex();
			System.out.println(i + " " + pq.getCost(i));
		}
	}
}
