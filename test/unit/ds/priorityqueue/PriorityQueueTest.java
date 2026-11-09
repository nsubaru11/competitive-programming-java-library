package unit.ds.priorityqueue;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.ds.*;
import lib.ds.priorityqueue.*;
import lib.ds.priorityqueue.PriorityQueue;
import lib.util.function.*;

import static org.junit.jupiter.api.Assertions.*;

final class PriorityQueueTest {

	private record Job(int priority, String name) {}

	@Test
	void intPollsInOrder() {
		final IntPriorityQueue q = new IntPriorityQueue(2);
		assertEquals(2, q.capacity());
		assertTrue(q.addAll(new int[]{5, 1}));
		assertTrue(q.addAll(List.of(4, 2)));
		assertEquals(1, q.peek());
		assertEquals(2, q.peekSecond());
		assertEquals(1, q.replaceTop(3));
		assertEquals(2, q.poll());
		assertEquals(3, q.poll());
		assertEquals(4, q.poll());
		assertEquals(5, q.poll());
		assertEquals(-1, q.pollOrDefault(-1));
	}

	@Test
	void longDescending() {
		final LongPriorityQueue q = new LongPriorityQueue(true);
		assertTrue(q.addAll(new long[]{2, 7}));
		final Iterable<Long> values = () -> List.of(5L, 3L).iterator();
		assertTrue(q.addAll(values));
		assertEquals(5, q.peekSecond());
		assertEquals(7, q.poll());
		assertEquals(5, q.poll());
		assertEquals(3, q.poll());
		assertEquals(2, q.poll());
		assertEquals(-1, q.peekOrDefault(-1));
	}

	@Test
	void genericOrders() {
		final PriorityQueue<Integer> natural = new PriorityQueue<>();
		natural.addAll(new Integer[]{5, 1, 3});
		assertEquals(3, natural.peekSecond());
		assertEquals(1, natural.poll());

		assertEquals(7, new PriorityQueue<>(new Integer[]{2, 7, 4}, true).poll());

		final PriorityQueue<String> byLength = new PriorityQueue<>(Comparator.comparingInt(String::length));
		byLength.addAll(new String[]{"five", "a"});
		byLength.addAll(List.of("three", "to"));
		assertEquals("a", byLength.poll());
		assertEquals("to", byLength.poll());
		byLength.clear();
		assertTrue(byLength.isEmpty());

		final PriorityQueue<Job> jobs = new PriorityQueue<>(Comparator.comparingInt(Job::priority));
		jobs.add(new Job(3, "c"));
		jobs.add(new Job(1, "a"));
		assertEquals("a", jobs.poll().name());
	}

	@Test
	void naturalOrderRequiresComparable() {
		final PriorityQueue<Job> q = new PriorityQueue<>();
		q.add(new Job(2, "b"));
		q.add(new Job(1, "a"));
		assertThrows(ClassCastException.class, q::peek);
	}

	@Test
	void primitiveComparators() {
		final IntComparator distanceFromTen = (a, b) -> Integer.compare(Math.abs(a - 10), Math.abs(b - 10));
		final IntPriorityQueue iq = new IntPriorityQueue(new int[]{2, 15, 9}, distanceFromTen);
		assertEquals(9, iq.poll());
		assertEquals(15, iq.poll());
		assertEquals(2, iq.poll());

		final LongComparator lastDigit = (a, b) -> Long.compare(a % 10, b % 10);
		final LongPriorityQueue lq = new LongPriorityQueue(2, lastDigit.reversed());
		lq.add(12);
		lq.add(29);
		assertEquals(29, lq.poll());
	}

	@Test
	void constructFromArray() {
		final IntPriorityQueue q = new IntPriorityQueue(new int[]{7, 1, 5, 3});
		assertEquals(1, q.peek());
		assertEquals(3, q.peekSecond());
	}

	@Test
	void peekSecondDefaults() {
		final IntPriorityQueue iq = new IntPriorityQueue(new int[]{4});
		assertEquals(-1, iq.peekSecondOrDefault(-1));
		iq.add(6);
		assertEquals(6, iq.peekSecondOrDefault(-1));
		assertEquals(-1, new LongPriorityQueue(new long[]{4}).peekSecondOrDefault(-1));
		assertEquals("z", new PriorityQueue<>(new String[]{"a"}).peekSecondOrDefault("z"));
	}

	@Test
	void collectionViews() {
		final IntPriorityQueue iq = new IntPriorityQueue();
		iq.add(4);
		iq.add(2);
		assertTrue(iq.contains(2));
		assertEquals(2, iq.toArray().length);

		final LongPriorityQueue lq = new LongPriorityQueue();
		lq.add(4);
		lq.add(2);
		assertTrue(lq.contains(2));
		assertEquals(2, lq.toArray().length);
	}

	@Test
	void intCopiesAndAddAll() {
		final IntPriorityQueue q = new IntPriorityQueue(new int[]{4, 1, 3});
		final IntPriorityQueue copy = new IntPriorityQueue(q);
		assertEquals(1, copy.poll());
		assertEquals(3, q.size());
		assertEquals(q.capacity(), copy.capacity());
		assertEquals(4, new IntPriorityQueue(q, true).peek());
		assertEquals(4, new IntPriorityQueue(q, IntComparator.reverseOrder()).peek());
		assertTrue(q.addAll(q));
		assertEquals(6, q.size());
		assertTrue(q.addAll((Iterable<Integer>) copy));
		assertEquals(8, q.size());
		assertFalse(q.addAll(new IntPriorityQueue()));
	}

	@Test
	void longCopiesAndAddAll() {
		final LongPriorityQueue q = new LongPriorityQueue(new long[]{4, 1, 3}, true);
		final LongPriorityQueue copy = new LongPriorityQueue(q);
		assertEquals(4, copy.poll());
		assertEquals(4, q.peek());
		assertEquals(1, new LongPriorityQueue((LongCollection) q).peek());
		assertEquals(4, new LongPriorityQueue(q, true).peek());
		assertEquals(1, new LongPriorityQueue(q, LongComparator.naturalOrder()).peek());
		assertTrue(q.addAll(copy));
		assertEquals(5, q.size());
	}

	@Test
	void genericCopiesAndConstructors() {
		final PriorityQueue<Integer> q = new PriorityQueue<>(List.of(5, 2, 8));
		assertEquals(2, q.peek());
		assertEquals(8, new PriorityQueue<>(List.of(5, 2, 8), true).peek());
		assertEquals("a", new PriorityQueue<>(List.of("bb", "a"), Comparator.comparingInt(String::length)).peek());
		final PriorityQueue<Integer> copy = new PriorityQueue<>(q);
		assertEquals(2, copy.poll());
		assertEquals(3, q.size());
		assertEquals(4, new PriorityQueue<Integer>(4).capacity());
		assertEquals(3, new PriorityQueue<>(new Integer[]{1, 3}, true).peek());
	}

	@Test
	void toStringKeepsHeapOrder() {
		assertEquals("", new IntPriorityQueue().toString());
		assertEquals("3 1 2", new IntPriorityQueue(new int[]{3, 1, 2}).toString());
		assertEquals("5000000000 4", new LongPriorityQueue(new long[]{5_000_000_000L, 4}).toString());
		assertEquals("b a", new PriorityQueue<>(List.of("b", "a")).toString());
	}

	// 愚直解との照合: 整列済みリストと同じ操作を適用し、先頭を比べる
	@RepeatedTest(20)
	void matchesSortedList(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final boolean descending = (seed & 1) == 0;
		final IntPriorityQueue q = new IntPriorityQueue(1, descending);
		final ArrayList<Integer> naive = new ArrayList<>();
		final Comparator<Integer> order = descending ? Comparator.reverseOrder() : Comparator.naturalOrder();
		for (int op = 0; op < 2000; op++) {
			final String at = "seed=" + seed + ", op=" + op;
			naive.sort(order);
			switch (random.nextInt(4)) {
				case 0, 1 -> {
					final int v = random.nextInt(100);
					q.add(v);
					naive.add(v);
				}
				case 2 -> {
					if (naive.isEmpty()) assertEquals(-1, q.pollOrDefault(-1), at);
					else assertEquals(naive.removeFirst(), q.poll(), at);
				}
				default -> {
					assertEquals(naive.isEmpty() ? -1 : naive.getFirst(), q.peekOrDefault(-1), at);
					assertEquals(naive.size() < 2 ? -1 : naive.get(1), q.peekSecondOrDefault(-1), at);
				}
			}
			assertEquals(naive.size(), q.size(), at);
		}
	}
}
