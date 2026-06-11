public class Example {

	public static void main(String[] args) {
		int n = 20;
		IntPriorityQueue ipq = new IntPriorityQueue();
		LongPriorityQueue lqp = new LongPriorityQueue();
		IntPriorityQueue ipqd = new IntPriorityQueue(true);
		LongPriorityQueue lqpd = new LongPriorityQueue(true);
		PriorityQueue<Integer> pq = new PriorityQueue<>();
		PriorityQueue<Integer> pqd = new PriorityQueue<>(true);

		while (n-- > 0) {
			int r = (int) (Math.random() * 20);
			ipq.push(r);
			lqp.push(r);
			ipqd.push(r);
			lqpd.push(r);
			pq.push(r);
			pqd.push(r);
		}

		System.out.print("IntPriorityQueue:");
		while (!ipq.isEmpty()) {
			System.out.print(" " + ipq.poll());
		}
		System.out.print("\nLongPriorityQueue:");
		while (!lqp.isEmpty()) {
			System.out.print(" " + lqp.poll());
		}
		System.out.print("\nPriorityQueue:");
		while (!pq.isEmpty()) {
			System.out.print(" " + pq.poll());
		}
		System.out.print("\nIntPriorityQueueDescending:");
		while (!ipqd.isEmpty()) {
			System.out.print(" " + ipqd.poll());
		}
		System.out.print("\nLongPriorityQueueDescending:");
		while (!lqpd.isEmpty()) {
			System.out.print(" " + lqpd.poll());
		}
		System.out.print("\nPriorityQueueDescending:");
		while (!pqd.isEmpty()) {
			System.out.print(" " + pqd.poll());
		}
		System.out.println();
	}

}
