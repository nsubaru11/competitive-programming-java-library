package unit.ds.priorityqueue;

import java.util.*;
import java.util.function.*;

import org.junit.jupiter.api.*;

import unit.support.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * IndexedPriorityQueue・IntIndexedPriorityQueue・LongIndexedPriorityQueue に共通する振る舞いのテスト。コストは long で扱う。
 */
abstract class IndexedPriorityQueueContract {

	interface Target {
		Target copy();

		int indexCount();

		int size();

		boolean isEmpty();

		boolean containsIndex(int i);

		boolean hasCost(int i);

		long get(int i);

		long getOrDefault(int i, long defaultValue);

		long getCost(int i);

		long getCostOrDefault(int i, long defaultValue);

		long peek();

		long peekOrDefault(long defaultValue);

		int peekIndex();

		int peekIndexOrDefault(int defaultIndex);

		long peekSecond();

		long peekSecondOrDefault(long defaultValue);

		int peekSecondIndex();

		int peekSecondIndexOrDefault(int defaultIndex);

		boolean set(int i, long c);

		boolean add(int i, long c);

		boolean relax(int i, long c);

		long poll();

		long pollOrDefault(long defaultValue);

		int pollIndex();

		int pollIndexOrDefault(int defaultIndex);

		boolean remove(int i);

		Object setAll(IntToLongFunction init);

		void clear();

		Iterator<?> iterator();
	}

	abstract Target create(int n);

	abstract Target create(int n, boolean descending);

	/**
	 * 比較順を PriorityQueueContract.CUSTOM とする。
	 */
	abstract Target createWithComparator(int n);

	abstract Target create(long[] costs, boolean descending);

	abstract Target createWithComparator(long[] costs);

	abstract Target createFromCollection(long[] costs, boolean descending);

	static final Comparator<Long> CUSTOM = PriorityQueueContract.CUSTOM;

	@Nested
	class Construction {

		@Test
		void fromCostsPutsAllIndices() {
			final long[] costs = {5, 13, 2, 21, 8};
			assertPollIndices(create(costs, false), 2, 0, 4, 1, 3);
			assertPollIndices(create(costs, true), 3, 1, 4, 0, 2);
			// 10 で割った余りは 5 3 2 1 8
			assertPollIndices(createWithComparator(costs), 3, 2, 1, 0, 4);
			assertPollIndices(createFromCollection(costs, false), 2, 0, 4, 1, 3);
			assertPollIndices(createFromCollection(costs, true), 3, 1, 4, 0, 2);
		}

		@Test
		void emptyQueues() {
			for (final Target q : List.of(create(3), create(3, true), createWithComparator(3))) {
				assertEquals(3, q.indexCount());
				assertTrue(q.isEmpty());
				assertFalse(q.hasCost(0));
			}
		}

		@Test
		void copyIsIndependent() {
			final Target q = create(4);
			q.add(0, 5);
			q.add(1, 3);
			q.poll();
			final Target c = q.copy();
			c.add(2, 1);
			c.set(0, 9);
			assertEquals(1, q.size());
			assertEquals(5L, q.get(0));
			assertEquals(3L, c.getCost(1), "取り出し済みの状態も引き継ぐ");
			assertEquals(2, c.pollIndex());
		}
	}

	@Nested
	class Access {

		@Test
		void indexStates() {
			final Target q = create(3);
			q.add(0, 5);
			q.add(1, 3);
			assertEquals(1, q.pollIndex());
			// 0: キュー内、1: 取り出し済み、2: 未追加
			assertTrue(q.containsIndex(0));
			assertFalse(q.containsIndex(1));
			assertFalse(q.containsIndex(2));
			assertTrue(q.hasCost(0));
			assertTrue(q.hasCost(1));
			assertFalse(q.hasCost(2));
			assertEquals(5L, q.get(0));
			assertThrows(NoSuchElementException.class, () -> q.get(1));
			assertEquals(-1L, q.getOrDefault(1, -1));
			assertEquals(3L, q.getCost(1));
			assertThrows(NoSuchElementException.class, () -> q.getCost(2));
			assertEquals(-1L, q.getCostOrDefault(2, -1));
		}

		@Test
		void peekPrefersSmallerIndexOnTie() {
			final Target q = create(4);
			q.add(3, 1);
			q.add(1, 1);
			q.add(2, 0);
			q.add(0, 2);
			assertEquals(2, q.peekIndex());
			assertEquals(0L, q.peek());
			assertEquals(1, q.peekSecondIndex());
			assertEquals(1L, q.peekSecond());
		}

		@Test
		void emptyAndSingle() {
			final Target q = create(2);
			assertThrows(NoSuchElementException.class, q::peek);
			assertThrows(NoSuchElementException.class, q::peekIndex);
			assertEquals(-1L, q.peekOrDefault(-1));
			assertEquals(-1, q.peekIndexOrDefault(-1));
			q.add(1, 7);
			assertThrows(NoSuchElementException.class, q::peekSecond);
			assertThrows(NoSuchElementException.class, q::peekSecondIndex);
			assertEquals(-1L, q.peekSecondOrDefault(-1));
			assertEquals(-1, q.peekSecondIndexOrDefault(-1));
			q.add(0, 9);
			assertEquals(9L, q.peekSecondOrDefault(-1));
			assertEquals(0, q.peekSecondIndexOrDefault(-1));
		}
	}

	@Nested
	class Updates {

		@Test
		void setInsertsOrUpdates() {
			final Target q = create(3);
			assertTrue(q.set(0, 5), "未追加ならキューに入れて true");
			assertFalse(q.set(0, 1), "キュー内なら更新して false");
			assertEquals(1L, q.peek());
			assertFalse(q.set(0, 9));
			q.add(1, 4);
			assertEquals(1, q.peekIndex());
			q.poll();
			assertTrue(q.set(1, 2), "取り出し済みでも入れ直す");
		}

		@Test
		void addOnlyWhenNotInQueue() {
			final Target q = create(2);
			assertTrue(q.add(0, 5));
			assertFalse(q.add(0, 1));
			assertEquals(5L, q.get(0));
			q.poll();
			assertTrue(q.add(0, 3), "取り出し済みは入れ直す");
		}

		@Test
		void relax() {
			final Target q = create(3);
			assertTrue(q.relax(0, 5), "未追加ならキューに入れる");
			assertFalse(q.relax(0, 6), "優先されなければ更新しない");
			assertFalse(q.relax(0, 5));
			assertTrue(q.relax(0, 4));
			assertEquals(4L, q.get(0));
			q.poll();
			assertFalse(q.relax(0, 1), "取り出し済みは入れ直さない");
			assertFalse(q.containsIndex(0));
		}

		@Test
		void pollAndRemove() {
			final Target q = create(3);
			q.add(0, 5);
			q.add(1, 3);
			q.add(2, 4);
			assertTrue(q.remove(2));
			assertFalse(q.remove(2));
			assertFalse(q.remove(2));
			assertEquals(3L, q.poll());
			assertEquals(0, q.pollIndex());
			assertEquals(-1L, q.pollOrDefault(-1));
			assertEquals(-1, q.pollIndexOrDefault(-1));
			assertThrows(NoSuchElementException.class, q::poll);
			assertThrows(NoSuchElementException.class, q::pollIndex);
			assertEquals(4L, q.getCost(2), "remove した添字も取り出し済み");
		}
	}

	@Nested
	class Bulk {

		@Test
		void setAllAndClear() {
			final Target q = create(3);
			q.add(0, 9);
			q.poll();
			q.setAll(i -> 10 - i);
			assertEquals(3, q.size());
			assertPollIndices(q, 2, 1, 0);
			q.clear();
			assertTrue(q.isEmpty());
			assertFalse(q.hasCost(0), "clear で全添字が未追加に戻る");
		}
	}

	@Nested
	class Conversion {

		@Test
		void iteratorAndToStringContainCosts() {
			final Target q = create(3);
			q.add(0, 5);
			q.add(2, 3);
			final List<Long> seen = new ArrayList<>();
			q.iterator().forEachRemaining(v -> seen.add(((Number) v).longValue()));
			assertEquals(List.of(3L, 5L), seen.stream().sorted().toList());
			assertEquals(List.of(3L, 5L), Arrays.stream(q.toString().split(" ")).map(Long::parseLong).sorted().toList());
			assertEquals("", create(1).toString());
		}
	}

	@RepeatedTest(100)
	void matchesNaive(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final int n = 1 + random.nextInt(12), kind = random.nextInt(3);
		final Comparator<Long> order = kind == 0 ? Comparator.naturalOrder() : kind == 1 ? Comparator.reverseOrder() : CUSTOM;
		final Target q = kind == 2 ? createWithComparator(n) : create(n, kind == 1);
		// 0: 未追加、1: キュー内、2: 取り出し済み
		final int[] state = new int[n];
		final long[] cost = new long[n];
		new RandomOperations()
			.add("set", 2, (r, at) -> {
				final int i = r.nextInt(n);
				final long c = r.nextInt(-20, 20);
				assertEquals(state[i] != 1, q.set(i, c), at);
				state[i] = 1;
				cost[i] = c;
			})
			.add("add", 2, (r, at) -> {
				final int i = r.nextInt(n);
				final long c = r.nextInt(-20, 20);
				assertEquals(state[i] != 1, q.add(i, c), at);
				if (state[i] != 1) cost[i] = c;
				state[i] = 1;
			})
			.add("relax", 2, (r, at) -> {
				final int i = r.nextInt(n);
				final long c = r.nextInt(-20, 20);
				final boolean expected = state[i] == 0 || state[i] == 1 && order.compare(c, cost[i]) < 0;
				assertEquals(expected, q.relax(i, c), at);
				if (expected) {
					state[i] = 1;
					cost[i] = c;
				}
			})
			.add("poll", 2, (r, at) -> {
				final int best = best(state, cost, order, -1);
				assertEquals(best, q.pollIndexOrDefault(-1), at);
				if (best >= 0) state[best] = 2;
			})
			.add("remove", (r, at) -> {
				final int i = r.nextInt(n);
				assertEquals(state[i] == 1, q.remove(i), at);
				if (state[i] == 1) state[i] = 2;
			})
			.add("peek", 2, (r, at) -> {
				final int best = best(state, cost, order, -1);
				assertEquals(best, q.peekIndexOrDefault(-1), at);
				assertEquals(best(state, cost, order, best), q.peekSecondIndexOrDefault(-1), at);
				if (best >= 0) assertEquals(cost[best], q.peek(), at);
			})
			.add("get", (r, at) -> {
				final int i = r.nextInt(n);
				assertEquals(state[i] == 1 ? cost[i] : -999, q.getOrDefault(i, -999), at);
				assertEquals(state[i] != 0 ? cost[i] : -999, q.getCostOrDefault(i, -999), at);
				assertEquals(Arrays.stream(state).filter(s -> s == 1).count(), q.size(), at);
			})
			.run(seed, random, 400);
	}

	/**
	 * キュー内の添字のうち、except を除いて最も優先されるものを返す。コストが同順位なら添字の小さい方。なければ -1。
	 */
	private static int best(final int[] state, final long[] cost, final Comparator<Long> order, final int except) {
		int res = -1;
		for (int i = 0; i < state.length; i++) {
			if (state[i] != 1 || i == except) continue;
			if (res < 0 || order.compare(cost[i], cost[res]) < 0) res = i;
		}
		return res;
	}

	private static void assertPollIndices(final Target q, final int... expected) {
		for (final int i : expected) assertEquals(i, q.pollIndex());
		assertTrue(q.isEmpty());
	}
}
