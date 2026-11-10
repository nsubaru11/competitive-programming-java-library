package unit.ds.priorityqueue;

import java.util.*;

import org.junit.jupiter.api.*;

import unit.support.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PriorityQueue・IntPriorityQueue・LongPriorityQueue に共通する振る舞いのテスト。値は long で扱う。
 */
abstract class PriorityQueueContract {

	static final Comparator<Long> CUSTOM = Comparator.<Long>comparingLong(v -> Math.floorMod(v, 10)).thenComparing(Comparator.reverseOrder());

	private static long second(final java.util.PriorityQueue<Long> naive, final Comparator<Long> order) {
		if (naive.size() < 2) return -999;
		return naive.stream().sorted(order).skip(1).findFirst().orElseThrow();
	}

	/**
	 * extra を追加したあと、全要素が order 順に取り出されることを確かめる。all はキューが既に持つ要素である。
	 */
	private static void assertPollOrder(final Target q, final long[] extra, final Comparator<Long> order, final long[]... all) {
		final List<Long> expected = new ArrayList<>();
		for (final long[] a : all) for (final long v : a) expected.add(v);
		for (final long v : extra) {
			q.add(v);
			expected.add(v);
		}
		expected.sort(order);
		final List<Long> actual = new ArrayList<>();
		while (!q.isEmpty()) actual.add(q.poll());
		assertEquals(expected, actual);
	}

	abstract Target create();

	abstract Target create(int initialCapacity);

	abstract Target create(boolean descending);

	abstract Target create(int initialCapacity, boolean descending);

	/**
	 * 比較関数を受け取るコンストラクタで作る。比較順は「10 で割った余りの昇順、同じなら値の降順」とする。
	 */
	abstract Target createWithComparator(int initialCapacity);

	abstract Target create(long[] values, boolean descending);

	abstract Target createWithComparator(long[] values);

	/**
	 * IntCollection・Collection などを受け取るコンストラクタで作る。
	 */
	abstract Target createFromCollection(long[] values, boolean descending);

	@RepeatedTest(100)
	void matchesNaive(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final int kind = random.nextInt(3);
		final Comparator<Long> order = kind == 0 ? Comparator.naturalOrder() : kind == 1 ? Comparator.reverseOrder() : CUSTOM;
		final Target q = kind == 2 ? createWithComparator(1) : create(1, kind == 1);
		final java.util.PriorityQueue<Long> naive = new java.util.PriorityQueue<>(order);
		new RandomOperations()
				.add("add", 4, (r, at) -> {
					final long v = r.nextInt(-50, 50);
					naive.add(v);
					q.add(v);
				})
				.add("addAll", (r, at) -> {
					final long[] a = r.longs(r.nextInt(5), -50, 50).toArray();
					for (final long v : a) naive.add(v);
					assertEquals(a.length > 0, q.addAll(a), at);
				})
				.add("poll", 3, (r, at) -> assertEquals(naive.isEmpty() ? -999L : naive.poll(), q.pollOrDefault(-999), at))
				.add("replaceTop", (r, at) -> {
					if (naive.isEmpty()) return;
					final long v = r.nextInt(-50, 50);
					assertEquals((long) naive.poll(), q.replaceTop(v), at);
					naive.add(v);
				})
				.add("peek", 2, (r, at) -> {
					assertEquals(naive.isEmpty() ? -999L : naive.peek(), q.peekOrDefault(-999), at);
					assertEquals(second(naive, order), q.peekSecondOrDefault(-999), at);
					assertEquals(naive.size(), q.size(), at);
				})
				.run(seed, random, 400);
	}

	interface Target {
		Target copy();

		int size();

		int capacity();

		boolean isEmpty();

		long peek();

		long peekOrDefault(long defaultValue);

		long peekSecond();

		long peekSecondOrDefault(long defaultValue);

		boolean add(long v);

		boolean addAll(long[] values);

		long poll();

		long pollOrDefault(long defaultValue);

		long replaceTop(long v);

		void clear();

		Iterator<?> iterator();
	}

	@Nested
	class Construction {

		@Test
		void orders() {
			final long[] a = {5, 13, 2, 21, 8};
			assertPollOrder(create(), a, Comparator.naturalOrder());
			assertPollOrder(create(1), a, Comparator.naturalOrder());
			assertPollOrder(create(false), a, Comparator.naturalOrder());
			assertPollOrder(create(true), a, Comparator.reverseOrder());
			assertPollOrder(create(0, true), a, Comparator.reverseOrder());
			assertPollOrder(createWithComparator(1), a, CUSTOM);
		}

		@Test
		void fromValues() {
			final long[] a = {5, 13, 2, 21, 8};
			assertPollOrder(create(a, false), new long[0], Comparator.naturalOrder(), a);
			assertPollOrder(create(a, true), new long[0], Comparator.reverseOrder(), a);
			assertPollOrder(createWithComparator(a), new long[0], CUSTOM, a);
			assertPollOrder(createFromCollection(a, false), new long[0], Comparator.naturalOrder(), a);
			assertPollOrder(createFromCollection(a, true), new long[0], Comparator.reverseOrder(), a);
			assertEquals(0, create(new long[0], false).size());
		}

		@Test
		void copyIsIndependentAndKeepsOrder() {
			final Target q = create(true);
			q.addAll(new long[]{3, 1, 4});
			final Target c = q.copy();
			c.add(9);
			assertEquals(4L, q.poll());
			assertEquals(9L, c.poll());
			assertEquals(4L, c.poll());
			assertEquals(2, q.size());
		}
	}

	@Nested
	class State {

		@Test
		void sizeAndCapacityGrow() {
			final Target q = create(1);
			assertTrue(q.isEmpty());
			for (int i = 0; i < 10; i++) q.add(i);
			assertEquals(10, q.size());
			assertTrue(q.capacity() >= 10);
			assertFalse(q.isEmpty());
		}
	}

	@Nested
	class Access {

		@Test
		void peekAndPeekSecond() {
			final Target q = create();
			q.addAll(new long[]{5, 3, 8, 3});
			assertEquals(3L, q.peek());
			assertEquals(3L, q.peekSecond(), "同順位の値も2番目になる");
			q.poll();
			assertEquals(3L, q.peek());
			assertEquals(5L, q.peekSecond());
			q.poll();
			q.poll();
			assertEquals(8L, q.peek());
			assertThrows(NoSuchElementException.class, q::peekSecond);
			assertEquals(-1L, q.peekSecondOrDefault(-1));
			q.add(1);
			assertEquals(8L, q.peekSecondOrDefault(-1), "要素数2では残りの1つが2番目");
		}

		@Test
		void emptyPeek() {
			final Target q = create();
			assertThrows(NoSuchElementException.class, q::peek);
			assertEquals(-1L, q.peekOrDefault(-1));
			assertEquals(-1L, q.peekSecondOrDefault(-1));
		}
	}

	@Nested
	class Updates {

		@Test
		void addAllReturnsWhetherChanged() {
			final Target q = create();
			assertFalse(q.addAll(new long[0]));
			assertTrue(q.addAll(new long[]{2, 1}));
			assertTrue(q.add(0));
			assertEquals(3, q.size());
		}

		@Test
		void pollAndReplaceTop() {
			final Target q = create();
			q.addAll(new long[]{5, 3, 8});
			assertEquals(3L, q.replaceTop(9), "置き換え前の値を返す");
			assertEquals(5L, q.poll());
			assertEquals(8L, q.poll());
			assertEquals(9L, q.pollOrDefault(-1));
			assertEquals(-1L, q.pollOrDefault(-1));
			assertThrows(NoSuchElementException.class, q::poll);
			assertThrows(NoSuchElementException.class, () -> q.replaceTop(1));
		}
	}

	@Nested
	class Bulk {

		@Test
		void clear() {
			final Target q = create();
			q.addAll(new long[]{3, 1});
			q.clear();
			assertTrue(q.isEmpty());
			q.add(7);
			assertEquals(7L, q.peek());
		}
	}

	@Nested
	class Conversion {

		@Test
		void iteratorAndToStringContainAllValues() {
			final Target q = create();
			q.addAll(new long[]{3, 1, 2});
			final List<Long> seen = new ArrayList<>();
			q.iterator().forEachRemaining(v -> seen.add(((Number) v).longValue()));
			assertEquals(List.of(1L, 2L, 3L), seen.stream().sorted().toList());
			final Iterator<?> it = q.iterator();
			for (int i = 0; i < 3; i++) it.next();
			assertThrows(NoSuchElementException.class, it::next);
			assertEquals(List.of(1L, 2L, 3L), Arrays.stream(q.toString().split(" ")).map(Long::parseLong).sorted().toList());
			assertEquals("", create().toString());
		}
	}
}
