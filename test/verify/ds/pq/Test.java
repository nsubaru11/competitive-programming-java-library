package verify.ds.pq;

import java.util.*;

import lib.ds.*;
import lib.ds.priorityqueue.*;
import lib.ds.priorityqueue.PriorityQueue;
import lib.util.function.*;

public final class Test {

	public static void main(final String[] args) {
		checkPriorityQueues();
		checkPrimitiveComparators();
		checkIntIndexed();
		checkLongIndexed();
		checkGenericIndexed();
		checkConstructorsAndDefaults();
		checkLazyUpdateBoundary();
		checkCollections();
		checkRandom(false);
		checkRandom(true);
	}

	private static void checkPriorityQueues() {
		final IntPriorityQueue iq = new IntPriorityQueue(2);
		check(iq.capacity() == 2);
		check(iq.addAll(new int[]{5, 1}));
		check(iq.addAll(List.of(4, 2)));
		check(iq.peek() == 1);
		check(iq.peekSecond() == 2);
		check(iq.replaceTop(3) == 1);
		check(iq.poll() == 2);
		check(iq.poll() == 3);
		check(iq.poll() == 4);
		check(iq.poll() == 5);
		check(iq.pollOrDefault(-1) == -1);

		final LongPriorityQueue lq = new LongPriorityQueue(true);
		check(lq.addAll(new long[]{2, 7}));
		final Iterable<Long> values = () -> List.of(5L, 3L).iterator();
		check(lq.addAll(values));
		check(lq.peekSecond() == 5);
		check(lq.poll() == 7);
		check(lq.poll() == 5);
		check(lq.poll() == 3);
		check(lq.poll() == 2);
		check(lq.peekOrDefault(-1) == -1);

		final PriorityQueue<Integer> natural = new PriorityQueue<>();
		natural.addAll(new Integer[]{5, 1, 3});
		check(natural.peekSecond() == 3);
		check(natural.poll() == 1);

		final PriorityQueue<Integer> reverse = new PriorityQueue<>(new Integer[]{2, 7, 4}, true);
		check(reverse.poll() == 7);

		final PriorityQueue<String> q = new PriorityQueue<>(Comparator.comparingInt(String::length));
		q.addAll(new String[]{"five", "a"});
		q.addAll(List.of("three", "to"));
		check(q.poll().equals("a"));
		check(q.poll().equals("to"));
		q.clear();
		check(q.isEmpty());

		final PriorityQueue<Job> jobs = new PriorityQueue<>(Comparator.comparingInt(Job::priority));
		jobs.add(new Job(3, "c"));
		jobs.add(new Job(1, "a"));
		check(jobs.poll().name().equals("a"));

		final PriorityQueue<Job> incomparable = new PriorityQueue<>();
		incomparable.add(new Job(2, "b"));
		incomparable.add(new Job(1, "a"));
		expectClassCast(incomparable::peek);
	}

	private static void checkPrimitiveComparators() {
		final IntComparator distanceFromTen = (a, b) -> Integer.compare(Math.abs(a - 10), Math.abs(b - 10));
		final IntPriorityQueue iq = new IntPriorityQueue(new int[]{2, 15, 9}, distanceFromTen);
		check(iq.poll() == 9);
		check(iq.poll() == 15);
		check(iq.poll() == 2);

		final LongComparator lastDigit = (a, b) -> Long.compare(a % 10, b % 10);
		final LongPriorityQueue lq = new LongPriorityQueue(2, lastDigit.reversed());
		lq.add(12);
		lq.add(29);
		check(lq.poll() == 29);

		final IntIndexedPriorityQueue indexed = new IntIndexedPriorityQueue(3, distanceFromTen);
		indexed.add(0, 2);
		indexed.add(1, 15);
		indexed.add(2, 9);
		check(indexed.pollIndex() == 2);

		final LongIndexedPriorityQueue longIndexed = new LongIndexedPriorityQueue(2, lastDigit);
		longIndexed.add(0, 28);
		longIndexed.add(1, 11);
		check(longIndexed.peekIndex() == 1);
	}

	private static void checkIntIndexed() {
		final IntIndexedPriorityQueue q = new IntIndexedPriorityQueue(8);
		check(q.indexCount() == 8);
		check(q.peekOrDefault(100) == 100);
		check(q.peekIndexOrDefault(-1) == -1);
		check(q.pollOrDefault(100) == 100);
		check(q.pollIndexOrDefault(-1) == -1);
		check(!q.remove(0));

		check(q.add(0, 5));
		check(!q.add(0, 9));
		check(q.add(1, 3));
		check(q.add(2, 8));
		check(q.peek() == 3);
		check(q.peekIndex() == 1);
		check(q.get(0) == 5);

		q.set(0, 1);
		q.set(2, 10);
		check(q.peek() == 1);
		check(q.relax(2, 2));
		check(!q.relax(2, 4));

		check(q.pollIndex() == 0);
		check(!q.containsIndex(0));
		check(q.hasCost(0));
		check(q.getLast(0) == 1);
		check(!q.relax(0, 0));
		check(q.add(0, 6));
		check(q.get(0) == 6);

		check(q.remove(2));
		check(!q.remove(2));
		check(q.getLast(2) == 2);

		q.clear();
		check(!q.hasCost(0));
		check(q.addAll(new int[]{0, 0, 3, 4}, new int[]{7, 9, 1, 5}));
		check(q.get(0) == 7);
		check(q.poll() == 1);
		check(q.poll() == 5);
		check(q.poll() == 7);
		check(!q.addAll(new int[0], new int[0]));

		final IntIndexedPriorityQueue max = new IntIndexedPriorityQueue(4, true);
		max.add(0, 2);
		max.add(1, 5);
		max.add(2, 3);
		check(max.peek() == 5);
		check(max.relax(0, 6));
		check(!max.relax(2, 1));
		check(max.pollIndex() == 0);
		check(max.getLast(0) == 6);
	}

	private static void checkLongIndexed() {
		final LongIndexedPriorityQueue q = new LongIndexedPriorityQueue(4);
		q.add(0, 5_000_000_000L);
		q.set(1, 3_000_000_000L);
		check(q.relax(0, 2_000_000_000L));
		check(q.pollIndex() == 0);
		check(q.getLast(0) == 2_000_000_000L);
		check(q.getOrDefault(0, -1) == -1);
		q.clear();
		check(q.getLastOrDefault(0, -1) == -1);

		final LongIndexedPriorityQueue generated = LongIndexedPriorityQueue.generate(3, i -> (long) i * i, true);
		check(generated.peek() == 4);
		check(generated.indexCount() == 3);
	}

	private static void checkGenericIndexed() {
		final IndexedPriorityQueue<String> natural = new IndexedPriorityQueue<>(new String[]{"c", "a", "b"});
		check(natural.peek().equals("a"));
		check(natural.peekSecond().equals("b"));
		check(natural.pollIndex() == 1);
		check(natural.getLast(1).equals("a"));
		check(natural.add(1, "d"));
		check(!natural.add(1, "e"));
		check(natural.remove(1));
		check(!natural.remove(1));

		final IndexedPriorityQueue<Job> jobs = new IndexedPriorityQueue<>(3, Comparator.comparingInt(Job::priority));
		jobs.add(0, new Job(4, "d"));
		jobs.add(1, new Job(2, "b"));
		jobs.set(2, new Job(3, "c"));
		check(jobs.peekIndex() == 1);
		check(jobs.relax(0, new Job(1, "a")));
		check(jobs.poll().name().equals("a"));
		check(!jobs.relax(0, new Job(0, "z")));
		check(jobs.peekOrDefault(null) != null);
		jobs.clear();
		check(jobs.peekIndexOrDefault(-1) == -1);

		final IndexedPriorityQueue<Integer> generated = IndexedPriorityQueue.generate(4, i -> i * i, true);
		check(generated.peek() == 9);
	}

	private static void checkConstructorsAndDefaults() {
		final IntPriorityQueue iq = new IntPriorityQueue(new int[]{7, 1, 5, 3});
		check(iq.peek() == 1);
		check(iq.peekSecond() == 3);

		final IntIndexedPriorityQueue indexed = IntIndexedPriorityQueue.generate(4, i -> i * 10, true);
		check(indexed.peekIndex() == 3);
		check(indexed.peek() == 30);

		final IntIndexedPriorityQueue fromArray = new IntIndexedPriorityQueue(new int[]{7, 1, 5, 3});
		check(fromArray.peek() == 1);
		final var it = fromArray.iterator();
		while (it.hasNext()) it.nextInt();
		expectNoSuchElement(it::nextInt);
	}

	private static void checkLazyUpdateBoundary() {
		final IntIndexedPriorityQueue iq = new IntIndexedPriorityQueue(4);
		iq.add(0, 1);
		iq.add(1, 2);
		iq.add(2, 3);
		check(iq.peek() == 1);
		iq.add(3, 0);
		iq.set(0, 10);
		check(iq.peek() == 0);
		check(iq.get(0) == 10);

		final LongIndexedPriorityQueue lq = new LongIndexedPriorityQueue(4, true);
		lq.add(0, 10);
		lq.add(1, 8);
		check(lq.peek() == 10);
		lq.add(2, 6);
		lq.add(3, 4);
		check(lq.relax(2, 12));
		check(lq.peek() == 12);
	}

	private static void checkCollections() {
		final IntPriorityQueue iq = new IntPriorityQueue();
		iq.add(4);
		iq.add(2);
		final IntCollection ints = iq;
		check(ints.contains(2));
		check(ints.toArray().length == 2);

		final LongPriorityQueue lq = new LongPriorityQueue();
		lq.add(4);
		lq.add(2);
		final LongCollection longs = lq;
		check(longs.contains(2));
		check(longs.toArray().length == 2);

		final IntIndexedPriorityQueue indexed = new IntIndexedPriorityQueue(4);
		indexed.add(0, 9);
		indexed.add(1, 3);
		final IntCollection costs = indexed;
		check(costs.contains(3));
		check(!costs.contains(0));
	}

	private static void checkRandom(final boolean descending) {
		final int n = 32;
		final IntIndexedPriorityQueue q = new IntIndexedPriorityQueue(n, descending);
		final int[] state = new int[n];
		final int[] cost = new int[n];
		final Random random = new Random(descending ? 2 : 1);
		for (int t = 0; t < 20_000; t++) {
			final int i = random.nextInt(n);
			final int c = random.nextInt(1_000);
			switch (random.nextInt(6)) {
				case 0 -> {
					q.set(i, c);
					state[i] = 1;
					cost[i] = c;
				}
				case 1 -> {
					final boolean expected;
					if (state[i] == 2) expected = false;
					else if (state[i] == 0) {
						expected = true;
						state[i] = 1;
						cost[i] = c;
					} else if (descending ? c > cost[i] : c < cost[i]) {
						expected = true;
						cost[i] = c;
					} else expected = false;
					check(q.relax(i, c) == expected);
				}
				case 2 -> {
					final boolean expected = state[i] != 1;
					check(q.add(i, c) == expected);
					if (expected) {
						state[i] = 1;
						cost[i] = c;
					}
				}
				case 3 -> {
					final boolean expected = state[i] == 1;
					check(q.remove(i) == expected);
					if (expected) state[i] = 2;
				}
				case 4 -> {
					if (!q.isEmpty()) {
						final int best = best(cost, state, descending);
						check(q.peek() == best);
						final int j = q.pollIndex();
						check(state[j] == 1);
						check(cost[j] == best);
						state[j] = 2;
					}
				}
				default -> {
					if ((t & 255) == 0) {
						q.clear();
						Arrays.fill(state, 0);
					}
				}
			}
			int size = 0;
			for (int j = 0; j < n; j++) {
				check(q.containsIndex(j) == (state[j] == 1));
				check(q.hasCost(j) == (state[j] != 0));
				check(q.getOrDefault(j, -1) == (state[j] == 1 ? cost[j] : -1));
				check(q.getLastOrDefault(j, -1) == (state[j] != 0 ? cost[j] : -1));
				if (state[j] == 1) size++;
			}
			check(q.size() == size);
		}
	}

	private static int best(final int[] cost, final int[] state, final boolean descending) {
		int best = 0;
		boolean found = false;
		for (int i = 0; i < cost.length; i++) {
			if (state[i] != 1) continue;
			if (!found || (descending ? cost[i] > best : cost[i] < best)) {
				best = cost[i];
				found = true;
			}
		}
		return best;
	}

	private static void check(final boolean condition) {
		if (!condition) throw new AssertionError();
	}

	private static void expectNoSuchElement(final Runnable action) {
		try {
			action.run();
			throw new AssertionError();
		} catch (final NoSuchElementException expected) {
			// expected
		}
	}

	private static void expectClassCast(final Runnable action) {
		try {
			action.run();
			throw new AssertionError();
		} catch (final ClassCastException expected) {
			// expected
		}
	}

	private record Job(int priority, String name) {
	}
}
