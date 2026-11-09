package unit.ds.priorityqueue;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.ds.arrays.*;
import lib.ds.priorityqueue.*;
import lib.util.function.*;

import static org.junit.jupiter.api.Assertions.*;

final class IndexedPriorityQueueTest {

	private record Job(int priority, String name) {}

	// 添字の状態: 未登録・登録中・取り出し済み（コストは残る）
	private static final int ABSENT = 0, QUEUED = 1, REMOVED = 2;

	@Test
	void intLifecycle() {
		final IntIndexedPriorityQueue q = new IntIndexedPriorityQueue(8);
		assertEquals(8, q.indexCount());
		assertEquals(100, q.peekOrDefault(100));
		assertEquals(-1, q.peekIndexOrDefault(-1));
		assertEquals(100, q.pollOrDefault(100));
		assertEquals(-1, q.pollIndexOrDefault(-1));
		assertFalse(q.remove(0));

		assertTrue(q.add(0, 5));
		assertFalse(q.add(0, 9));
		assertTrue(q.add(1, 3));
		assertTrue(q.add(2, 8));
		assertEquals(3, q.peek());
		assertEquals(1, q.peekIndex());
		assertEquals(5, q.get(0));

		assertFalse(q.set(0, 1));
		assertFalse(q.set(2, 10));
		assertEquals(1, q.peek());
		assertTrue(q.relax(2, 2));
		assertFalse(q.relax(2, 4));

		assertEquals(0, q.pollIndex());
		assertFalse(q.containsIndex(0));
		assertTrue(q.hasCost(0));
		assertEquals(1, q.getCost(0));
		assertFalse(q.relax(0, 0));
		assertTrue(q.add(0, 6));
		assertEquals(6, q.get(0));

		assertTrue(q.remove(2));
		assertFalse(q.remove(2));
		assertEquals(2, q.getCost(2));
	}

	@Test
	void intClearForgetsCosts() {
		final IntIndexedPriorityQueue q = new IntIndexedPriorityQueue(8);
		q.add(0, 1);
		q.pollIndex();
		q.clear();
		assertFalse(q.hasCost(0));
		assertTrue(q.add(0, 7));
		assertFalse(q.add(0, 9));
		assertTrue(q.add(3, 1));
		assertTrue(q.add(4, 5));
		assertEquals(7, q.get(0));
		assertEquals(1, q.poll());
		assertEquals(5, q.poll());
		assertEquals(7, q.poll());
	}

	@Test
	void intDescending() {
		final IntIndexedPriorityQueue q = new IntIndexedPriorityQueue(4, true);
		q.add(0, 2);
		q.add(1, 5);
		q.add(2, 3);
		assertEquals(5, q.peek());
		assertTrue(q.relax(0, 6));
		assertFalse(q.relax(2, 1));
		assertEquals(0, q.pollIndex());
		assertEquals(6, q.getCost(0));
	}

	@Test
	void longValues() {
		final LongIndexedPriorityQueue q = new LongIndexedPriorityQueue(4);
		q.add(0, 5_000_000_000L);
		assertTrue(q.set(1, 3_000_000_000L));
		assertTrue(q.relax(0, 2_000_000_000L));
		assertEquals(0, q.pollIndex());
		assertEquals(2_000_000_000L, q.getCost(0));
		assertEquals(-1, q.getOrDefault(0, -1));
		q.clear();
		assertEquals(-1, q.getCostOrDefault(0, -1));
	}

	@Test
	void genericValues() {
		final IndexedPriorityQueue<String> natural = new IndexedPriorityQueue<>(new String[]{"c", "a", "b"});
		assertEquals("a", natural.peek());
		assertEquals("b", natural.peekSecond());
		assertEquals(1, natural.pollIndex());
		assertEquals("a", natural.getCost(1));
		assertTrue(natural.add(1, "d"));
		assertFalse(natural.add(1, "e"));
		assertTrue(natural.remove(1));
		assertFalse(natural.remove(1));

		final IndexedPriorityQueue<Job> jobs = new IndexedPriorityQueue<>(3, Comparator.comparingInt(Job::priority));
		jobs.add(0, new Job(4, "d"));
		jobs.add(1, new Job(2, "b"));
		assertTrue(jobs.set(2, new Job(3, "c")));
		assertEquals(1, jobs.peekIndex());
		assertTrue(jobs.relax(0, new Job(1, "a")));
		assertEquals("a", jobs.poll().name());
		assertFalse(jobs.relax(0, new Job(0, "z")));
		assertNotNull(jobs.peekOrDefault(null));
		jobs.clear();
		assertEquals(-1, jobs.peekIndexOrDefault(-1));
	}

	@Test
	void primitiveComparators() {
		final IntComparator distanceFromTen = (a, b) -> Integer.compare(Math.abs(a - 10), Math.abs(b - 10));
		final IntIndexedPriorityQueue iq = new IntIndexedPriorityQueue(3, distanceFromTen);
		iq.add(0, 2);
		iq.add(1, 15);
		iq.add(2, 9);
		assertEquals(2, iq.pollIndex());

		final LongComparator lastDigit = (a, b) -> Long.compare(a % 10, b % 10);
		final LongIndexedPriorityQueue lq = new LongIndexedPriorityQueue(2, lastDigit);
		lq.add(0, 28);
		lq.add(1, 11);
		assertEquals(1, lq.peekIndex());
	}

	@Test
	void iteratorEnds() {
		final IntIndexedPriorityQueue q = new IntIndexedPriorityQueue(new int[]{7, 1, 5, 3});
		assertEquals(1, q.peek());
		final PrimitiveIterator.OfInt it = q.iterator();
		while (it.hasNext()) it.nextInt();
		assertThrows(NoSuchElementException.class, it::nextInt);
	}

	@Test
	void peekSecondDefaults() {
		final IntIndexedPriorityQueue ii = new IntIndexedPriorityQueue(3);
		ii.add(2, 5);
		assertEquals(-1, ii.peekSecondOrDefault(-1));
		assertEquals(-1, ii.peekSecondIndexOrDefault(-1));
		assertThrows(NoSuchElementException.class, ii::peekSecondIndex);
		ii.add(0, 7);
		assertEquals(0, ii.peekSecondIndex());
		assertEquals(7, ii.peekSecondOrDefault(-1));
		assertEquals(-1, new LongIndexedPriorityQueue(3).peekSecondIndexOrDefault(-1));

		final IndexedPriorityQueue<String> gi = new IndexedPriorityQueue<>(new String[]{"b", "a"});
		assertEquals(0, gi.peekSecondIndex());
		assertEquals(0, gi.peekSecondIndexOrDefault(-1));
		gi.pollIndex();
		assertEquals("z", gi.peekSecondOrDefault("z"));
	}

	// 同じコストの添字は小さい順に取り出す
	@Test
	void tiesPollSmallerIndexFirst() {
		final IntIndexedPriorityQueue built = new IntIndexedPriorityQueue(new int[]{3, 3, 3, 3, 3, 3, 3});
		for (int i = 0; i < 7; i++) assertEquals(i, built.pollIndex());

		final IntIndexedPriorityQueue q = new IntIndexedPriorityQueue(6);
		for (int i = 5; i >= 0; i--) q.add(i, 1);
		assertEquals(0, q.peekIndex());
		assertEquals(1, q.peekSecondIndex());
		assertEquals(0, q.pollIndex());
		q.set(1, 2);
		assertEquals(2, q.pollIndex());
		assertTrue(q.relax(1, 1));
		assertEquals(1, q.pollIndex());
		assertTrue(q.remove(3));
		assertEquals(4, q.pollIndex());
		assertEquals(5, q.pollIndex());

		final LongIndexedPriorityQueue max = new LongIndexedPriorityQueue(new long[]{5, 9, 9, 5, 9}, true);
		assertEquals(1, max.pollIndex());
		assertEquals(4, max.peekSecondIndex());
		assertEquals(2, max.pollIndex());
		assertEquals(4, max.pollIndex());
		assertEquals(0, max.pollIndex());
		assertEquals(3, max.pollIndex());

		final IndexedPriorityQueue<Job> jobs = new IndexedPriorityQueue<>(4, Comparator.comparingInt(Job::priority));
		for (int i = 3; i >= 0; i--) jobs.add(i, new Job(1, "j" + i));
		assertEquals(1, jobs.peekSecondIndex());
		for (int i = 0; i < 4; i++) assertEquals(i, jobs.pollIndex());
	}

	// 先頭を変える更新が、遅延している内部状態に正しく反映される
	@Test
	void updatesAroundTop() {
		final IntIndexedPriorityQueue iq = new IntIndexedPriorityQueue(4);
		iq.add(0, 1);
		iq.add(1, 2);
		iq.add(2, 3);
		assertEquals(1, iq.peek());
		iq.add(3, 0);
		assertFalse(iq.set(0, 10));
		assertEquals(0, iq.peek());
		assertEquals(10, iq.get(0));

		final LongIndexedPriorityQueue lq = new LongIndexedPriorityQueue(4, true);
		lq.add(0, 10);
		lq.add(1, 8);
		assertEquals(10, lq.peek());
		lq.add(2, 6);
		lq.add(3, 4);
		assertTrue(lq.relax(2, 12));
		assertEquals(12, lq.peek());

		final IntIndexedPriorityQueue uq = new IntIndexedPriorityQueue(4);
		uq.add(0, 5);
		assertEquals(5, uq.peek());
		uq.add(1, 1);
		uq.add(2, 3);
		uq.add(3, 2);
		assertTrue(uq.remove(1));
		assertTrue(uq.remove(3));
		assertFalse(uq.remove(3));
		assertEquals(1, uq.getCost(1));
		assertEquals(2, uq.size());
		assertEquals(3, uq.poll());
		assertEquals(5, uq.poll());
		assertTrue(uq.isEmpty());
	}

	@Test
	void containsChecksQueuedValues() {
		final IntIndexedPriorityQueue q = new IntIndexedPriorityQueue(4);
		q.add(0, 9);
		q.add(1, 3);
		assertTrue(q.contains(3));
		assertFalse(q.contains(0));
	}

	@Test
	void copiesAreIndependent() {
		final IntIndexedPriorityQueue ii = new IntIndexedPriorityQueue(4).setAll(i -> 10 - i);
		assertEquals(3, ii.peekIndex());
		final IntIndexedPriorityQueue iiCopy = new IntIndexedPriorityQueue(ii);
		assertEquals(3, iiCopy.pollIndex());
		assertTrue(ii.containsIndex(3));
		assertFalse(iiCopy.containsIndex(3));
		assertEquals(7, iiCopy.getCost(3));

		final LongIndexedPriorityQueue li = new LongIndexedPriorityQueue(3, true).setAll(i -> i * 1_000_000_000_000L);
		final LongIndexedPriorityQueue liCopy = new LongIndexedPriorityQueue(li);
		assertEquals(2_000_000_000_000L, liCopy.poll());
		assertEquals(2_000_000_000_000L, li.peek());

		final IndexedPriorityQueue<String> gi = new IndexedPriorityQueue<String>(3).setAll(i -> "c" + i);
		final IndexedPriorityQueue<String> giCopy = new IndexedPriorityQueue<>(gi);
		assertEquals(0, giCopy.pollIndex());
		assertEquals(0, gi.peekIndex());
		assertFalse(gi.isEmpty());
	}

	@Test
	void constructorsAndToString() {
		final IntIndexedPriorityQueue ii = new IntIndexedPriorityQueue(new IntArrayDeque(new int[]{7, 4, 9}));
		assertEquals(3, ii.indexCount);
		assertEquals(3, ii.indexCount());
		assertEquals(1, ii.peekIndex());
		assertEquals(3, new IntIndexedPriorityQueue(ii).indexCount);
		assertEquals(2, new IntIndexedPriorityQueue(new IntArrayDeque(new int[]{7, 4, 9}), true).peekIndex());
		assertEquals(2, new IntIndexedPriorityQueue(new IntArrayDeque(new int[]{7, 4, 9}), IntComparator.reverseOrder()).peekIndex());
		assertEquals("7 4 9", new IntIndexedPriorityQueue(new int[]{7, 4, 9}).toString());
		assertEquals("", new IntIndexedPriorityQueue(3).toString());

		final LongIndexedPriorityQueue li = new LongIndexedPriorityQueue(new LongArrayDeque(new long[]{7, 4, 9}));
		assertEquals(3, li.indexCount);
		assertEquals(1, li.peekIndex());
		assertEquals(2, new LongIndexedPriorityQueue(new LongArrayDeque(new long[]{7, 4, 9}), true).peekIndex());
		assertEquals(2, new LongIndexedPriorityQueue(new LongArrayDeque(new long[]{7, 4, 9}), LongComparator.reverseOrder()).peekIndex());
		assertEquals("7 4", new LongIndexedPriorityQueue(new long[]{7, 4}).toString());

		final IndexedPriorityQueue<Integer> gi = new IndexedPriorityQueue<>(List.of(7, 4, 9));
		assertEquals(3, gi.indexCount);
		assertEquals(1, gi.peekIndex());
		assertEquals(3, new IndexedPriorityQueue<>(gi).indexCount);
		assertEquals(2, new IndexedPriorityQueue<>(List.of(7, 4, 9), true).peekIndex());
		assertEquals(5, new IndexedPriorityQueue<Integer>(5).indexCount);
		assertEquals(1, new IndexedPriorityQueue<>(List.of("b", "a")).peekIndex());
		assertEquals(0, new IndexedPriorityQueue<>(List.of("b", "a"), true).peekIndex());
		assertEquals(1, new IndexedPriorityQueue<>(List.of("bb", "a"), Comparator.comparingInt(String::length)).peekIndex());
		assertEquals("x y", new IndexedPriorityQueue<>(new String[]{"x", "y"}).toString());
		assertEquals("", new IndexedPriorityQueue<String>(2).toString());
	}

	// 愚直解との照合: 添字ごとのコストと状態を配列で持ち、全添字の状態を比べる
	@RepeatedTest(20)
	void matchesNaive(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final boolean descending = (seed & 1) == 0;
		final int n = 1 + random.nextInt(32);
		// コストの種類を少なくし、同順位を多く発生させる
		final int costRange = random.nextBoolean() ? 4 : 1000;
		final IntIndexedPriorityQueue q = new IntIndexedPriorityQueue(n, descending);
		final int[] state = new int[n];
		final int[] cost = new int[n];
		for (int op = 0; op < 2000; op++) {
			final String at = "seed=" + seed + ", op=" + op;
			final int i = random.nextInt(n);
			final int c = random.nextInt(costRange);
			switch (random.nextInt(7)) {
				case 0 -> {
					assertEquals(state[i] != QUEUED, q.add(i, c), at);
					if (state[i] != QUEUED) {
						state[i] = QUEUED;
						cost[i] = c;
					}
				}
				case 1 -> {
					assertEquals(state[i] != QUEUED, q.set(i, c), at);
					state[i] = QUEUED;
					cost[i] = c;
				}
				case 2 -> {
					final boolean better = state[i] == QUEUED && (descending ? c > cost[i] : c < cost[i]);
					final boolean expected = state[i] == ABSENT || better;
					assertEquals(expected, q.relax(i, c), at);
					if (expected) {
						state[i] = QUEUED;
						cost[i] = c;
					}
				}
				case 3 -> {
					assertEquals(state[i] == QUEUED, q.remove(i), at);
					if (state[i] == QUEUED) state[i] = REMOVED;
				}
				case 4 -> {
					final int first = best(cost, state, descending, -1);
					assertEquals(first, q.peekIndexOrDefault(-1), at);
					assertEquals(first < 0 ? -1 : best(cost, state, descending, first), q.peekSecondIndexOrDefault(-1), at);
				}
				case 5 -> {
					final int first = best(cost, state, descending, -1);
					assertEquals(first, q.pollIndexOrDefault(-1), at);
					if (first >= 0) state[first] = REMOVED;
				}
				default -> {
					if (random.nextInt(50) == 0) {
						q.clear();
						Arrays.fill(state, ABSENT);
					}
				}
			}
			int size = 0;
			for (int j = 0; j < n; j++) {
				assertEquals(state[j] == QUEUED, q.containsIndex(j), at);
				assertEquals(state[j] != ABSENT, q.hasCost(j), at);
				assertEquals(state[j] == QUEUED ? cost[j] : -1, q.getOrDefault(j, -1), at);
				assertEquals(state[j] != ABSENT ? cost[j] : -1, q.getCostOrDefault(j, -1), at);
				if (state[j] == QUEUED) size++;
			}
			assertEquals(size, q.size(), at);
		}
	}

	// 登録中の添字のうち最良のものを返す。同順位は添字の小さい方を選ぶ
	private static int best(final int[] cost, final int[] state, final boolean descending, final int skip) {
		int result = -1;
		for (int i = 0; i < cost.length; i++) {
			if (state[i] != QUEUED || i == skip) continue;
			if (result < 0 || (descending ? cost[i] > cost[result] : cost[i] < cost[result])) result = i;
		}
		return result;
	}
}
