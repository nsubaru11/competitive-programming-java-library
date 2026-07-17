package verify.ds.pq;

import java.util.*;

import lib.ds.*;
import lib.ds.priorityqueue.*;
import lib.ds.priorityqueue.PriorityQueue;

public final class Test {

	public static void main(final String[] args) {
		checkPriorityQueues();
		checkIntIndexed();
		checkLongIndexed();
		checkSetAllAndSecond();
		checkLazyUpdateBoundary();
		checkCollections();
		checkRandom(false);
		checkRandom(true);
	}

	private static void checkPriorityQueues() {
		final IntPriorityQueue iq = new IntPriorityQueue();
		iq.addAll(new int[]{5, 1});
		iq.addAll(List.of(4, 2));
		check(iq.replaceTop(3) == 1);
		check(iq.poll() == 2);
		check(iq.poll() == 3);
		check(iq.poll() == 4);
		check(iq.poll() == 5);

		final LongPriorityQueue lq = new LongPriorityQueue(true);
		lq.addAll(new long[]{2, 7});
		final Iterable<Long> values = () -> List.of(5L, 3L).iterator();
		lq.addAll(values);
		check(lq.poll() == 7);
		check(lq.poll() == 5);
		check(lq.poll() == 3);
		check(lq.poll() == 2);

		final PriorityQueue<String> q = new PriorityQueue<>(Comparator.comparingInt(String::length));
		q.addAll(new String[]{"five", "a"});
		q.addAll(List.of("three", "to"));
		check(q.poll().equals("a"));
		check(q.poll().equals("to"));
	}

	private static void checkIntIndexed() {
		final IntIndexedPriorityQueue q = new IntIndexedPriorityQueue(8);
		check(!q.containsIndex(0));
		check(!q.hasCost(0));
		check(q.getOrDefault(0, 100) == 100);
		check(q.getLastOrDefault(0, 100) == 100);

		q.push(0, 5);
		q.push(1, 3);
		q.push(2, 8);
		check(q.peek() == 3);
		check(q.peekIndex() == 1);
		check(q.get(0) == 5);
		check(q.getLast(0) == 5);

		q.set(0, 1);
		q.set(2, 10);
		check(q.peek() == 1);
		check(q.relax(2, 2));
		check(!q.relax(2, 4));
		check(q.get(2) == 2);

		check(q.pollIndex() == 0);
		check(!q.containsIndex(0));
		check(q.hasCost(0));
		check(q.getOrDefault(0, 100) == 100);
		check(q.getLast(0) == 1);
		check(!q.relax(0, 0));

		q.set(0, 6);
		check(q.containsIndex(0));
		check(q.get(0) == 6);
		q.remove(2);
		check(!q.containsIndex(2));
		check(q.getLast(2) == 2);

		q.clear();
		check(q.isEmpty());
		check(!q.containsIndex(0));
		check(!q.hasCost(0));
		check(q.getLastOrDefault(0, 100) == 100);
		q.pushAll(new int[]{0, 3, 4}, new int[]{7, 1, 5});
		check(q.peekIndex() == 3);
		check(q.poll() == 1);
		check(q.poll() == 5);
		check(q.poll() == 7);

		final IntIndexedPriorityQueue max = new IntIndexedPriorityQueue(4, true);
		max.push(0, 2);
		max.push(1, 5);
		max.push(2, 3);
		check(max.peek() == 5);
		check(max.relax(0, 6));
		check(!max.relax(2, 1));
		check(max.pollIndex() == 0);
		check(max.getLast(0) == 6);
	}

	private static void checkLongIndexed() {
		final LongIndexedPriorityQueue q = new LongIndexedPriorityQueue(4);
		q.push(0, 5_000_000_000L);
		q.set(1, 3_000_000_000L);
		check(q.relax(0, 2_000_000_000L));
		check(q.pollIndex() == 0);
		check(q.getLast(0) == 2_000_000_000L);
		check(q.getOrDefault(0, -1) == -1);
		q.clear();
		check(q.getLastOrDefault(0, -1) == -1);
	}

	private static void checkSetAllAndSecond() {
		final int[] a = {7, 1, 5, 3};
		final IntIndexedPriorityQueue iq = new IntIndexedPriorityQueue(a.length);
		iq.setAll(i -> a[i]);
		check(iq.size() == a.length);
		for (int i = 0; i < a.length; i++) {
			check(iq.containsIndex(i));
			check(iq.get(i) == a[i]);
		}
		check(iq.peek() == 1);
		check(iq.peekSecond() == 3);

		final IntIndexedPriorityQueue imax = new IntIndexedPriorityQueue(2, true);
		imax.setAll(i -> i == 0 ? 2 : 5);
		check(imax.peek() == 5);
		check(imax.peekSecond() == 2);
		final var iit = imax.iterator();
		while (iit.hasNext()) iit.nextInt();
		expectNoSuchElement(iit::nextInt);

		final LongIndexedPriorityQueue lmax = new LongIndexedPriorityQueue(2, true);
		lmax.setAll(i -> i == 0 ? 2_000_000_000L : 5_000_000_000L);
		check(lmax.peek() == 5_000_000_000L);
		check(lmax.peekSecond() == 2_000_000_000L);
		final var lit = lmax.iterator();
		while (lit.hasNext()) lit.nextLong();
		expectNoSuchElement(lit::nextLong);
	}

	private static void checkLazyUpdateBoundary() {
		final IntIndexedPriorityQueue iq = new IntIndexedPriorityQueue(4);
		iq.push(0, 1);
		iq.push(1, 2);
		iq.push(2, 3);
		check(iq.peek() == 1);
		iq.push(3, 0);
		iq.set(0, 10);
		check(iq.peek() == 0);
		check(iq.get(0) == 10);

		final LongIndexedPriorityQueue lq = new LongIndexedPriorityQueue(4, true);
		lq.push(0, 10);
		lq.push(1, 8);
		check(lq.peek() == 10);
		lq.push(2, 6);
		lq.push(3, 4);
		check(lq.relax(2, 12));
		check(lq.peek() == 12);
	}

	private static void checkCollections() {
		final IntPriorityQueue iq = new IntPriorityQueue();
		iq.push(4);
		iq.push(2);
		final IntCollection ints = iq;
		check(ints.contains(2));
		check(ints.toArray().length == 2);

		final LongPriorityQueue lq = new LongPriorityQueue();
		lq.push(4);
		lq.push(2);
		final LongCollection longs = lq;
		check(longs.contains(2));
		check(longs.toArray().length == 2);

		final IntIndexedPriorityQueue indexed = new IntIndexedPriorityQueue(4);
		indexed.push(0, 9);
		indexed.push(1, 3);
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
					if (state[i] == 2) {
						expected = false;
					} else if (state[i] == 0) {
						expected = true;
						state[i] = 1;
						cost[i] = c;
					} else if (descending ? c > cost[i] : c < cost[i]) {
						expected = true;
						cost[i] = c;
					} else {
						expected = false;
					}
					check(q.relax(i, c) == expected);
				}
				case 2 -> {
					if (state[i] == 0) {
						q.push(i, c);
						state[i] = 1;
						cost[i] = c;
					}
				}
				case 3 -> {
					if (state[i] == 1) {
						q.remove(i);
						state[i] = 2;
					}
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
}
