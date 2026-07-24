package verify.ds.priorityqueue;

import lib.ds.priorityqueue.IntPriorityQueue;

public final class Example {

	public static void main(String[] args) {
		IntPriorityQueue queue = new IntPriorityQueue();
		queue.add(3);
		queue.add(1);
		queue.add(2);

		while (!queue.isEmpty()) {
			System.out.println(queue.poll());
		}
	}

}
