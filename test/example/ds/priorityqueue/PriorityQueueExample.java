package example.ds.priorityqueue;

import lib.ds.priorityqueue.*;

public final class PriorityQueueExample {

	public static void main(final String[] args) {
		// 自然順序
		PriorityQueue<String> pq = new PriorityQueue<>(new String[]{"pear", "apple", "fig"});
		System.out.println(pq.peek());
		System.out.println(pq.peekSecond());
		System.out.println();

		// 追加・取り出し
		pq.add("banana");
		System.out.println(pq.poll());
		System.out.println(pq.poll());
		System.out.println(pq.size());
		System.out.println();

		// 先頭の置換
		System.out.println(pq.replaceTop("kiwi"));
		System.out.println(pq.peek());
		System.out.println();

		// 比較関数
		PriorityQueue<int[]> byCost = new PriorityQueue<>((a, b) -> Integer.compare(a[1], b[1]));
		byCost.add(new int[]{0, 30});
		byCost.add(new int[]{1, 10});
		byCost.add(new int[]{2, 20});
		while (!byCost.isEmpty()) {
			int[] e = byCost.poll();
			System.out.println(e[0] + " " + e[1]);
		}
		System.out.println();

		// 空のとき
		System.out.println(byCost.pollOrDefault(null));
	}
}
