package unit.ds.arrays;

import java.util.*;
import java.util.function.*;

import org.junit.jupiter.api.*;

import unit.support.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * IntArrayDeque・LongArrayDeque に共通する振る舞いのテスト。値は long で扱う。
 */
abstract class ArrayDequeContract {

	interface Target {
		Object self();

		Target copy();

		Target fromCollection();

		int size();

		long get(int i);

		long set(int i, long v);

		void addFirst(long v);

		void addLast(long v);

		long removeFirst();

		long removeFirstOrDefault(long defaultValue);

		long removeLast();

		long removeLastOrDefault(long defaultValue);

		Object fill(long v);

		Object setAll(IntToLongFunction init);

		void clear();

		Object swap(int i, int j);

		Object reverse();

		Object reverse(int l, int r);

		long[] toArray();

		Iterator<? extends Number> iterator();
	}

	abstract Target create();

	abstract Target create(int capacity);

	abstract Target create(long[] a);

	/**
	 * 先頭側から追加して head を配列の途中へ動かした deque を作る。要素は a の順に並ぶ。
	 */
	Target wrapped(final long[] a) {
		final Target dq = create(4);
		for (int i = a.length - 1; i >= 0; i--) dq.addFirst(a[i]);
		return dq;
	}

	@Nested
	class Construction {

		@Test
		void emptyAndCapacity() {
			assertEquals(0, create().size());
			assertEquals(0, create(1).size());
			assertEquals(0, create(5).size());
		}

		@Test
		void fromArrayCollectionAndCopy() {
			final long[] a = {3, 1, 4};
			final Target dq = create(a);
			assertArrayEquals(a, dq.toArray());
			assertArrayEquals(a, dq.fromCollection().toArray());
			final Target copy = dq.copy();
			copy.addLast(9);
			copy.set(0, 7);
			assertArrayEquals(a, dq.toArray());
			assertArrayEquals(new long[]{7, 1, 4, 9}, copy.toArray());
		}

		@Test
		void fromEmptyArray() {
			final Target dq = create(new long[0]);
			assertEquals(0, dq.size());
			dq.addLast(1);
			assertArrayEquals(new long[]{1}, dq.toArray());
		}
	}

	@Nested
	class Access {

		@Test
		void getAndSetFollowLogicalOrder() {
			final Target dq = wrapped(new long[]{3, 1, 4, 1, 5});
			assertEquals(3L, dq.get(0));
			assertEquals(5L, dq.get(4));
			assertEquals(4L, dq.set(2, 9), "set は更新前の値");
			assertArrayEquals(new long[]{3, 1, 9, 1, 5}, dq.toArray());
		}

		@Test
		void outOfRangeThrows() {
			final Target dq = create(new long[]{3, 1});
			assertThrows(IndexOutOfBoundsException.class, () -> dq.get(2));
			assertThrows(IndexOutOfBoundsException.class, () -> dq.get(-1));
			assertThrows(IndexOutOfBoundsException.class, () -> dq.set(2, 0));
		}
	}

	@Nested
	class Updates {

		@Test
		void addAndRemoveAtBothEnds() {
			final Target dq = create(2);
			dq.addLast(2);
			dq.addFirst(1);
			dq.addLast(3);
			dq.addFirst(0);
			assertArrayEquals(new long[]{0, 1, 2, 3}, dq.toArray());
			assertEquals(0L, dq.removeFirst());
			assertEquals(3L, dq.removeLast());
			assertArrayEquals(new long[]{1, 2}, dq.toArray());
		}

		@Test
		void removeFromEmpty() {
			final Target dq = create();
			assertThrows(NoSuchElementException.class, dq::removeFirst);
			assertThrows(NoSuchElementException.class, dq::removeLast);
			assertEquals(-9L, dq.removeFirstOrDefault(-9));
			assertEquals(-9L, dq.removeLastOrDefault(-9));
			dq.addLast(5);
			assertEquals(5L, dq.removeLastOrDefault(-9));
			dq.addLast(6);
			assertEquals(6L, dq.removeFirstOrDefault(-9));
		}

		@Test
		void growKeepsOrderWhenWrapped() {
			final Target dq = create(4);
			for (int v = 0; v < 3; v++) dq.addLast(v);
			dq.removeFirst();
			dq.removeFirst();
			for (int v = 3; v < 20; v++) dq.addLast(v);
			dq.addFirst(1);
			final long[] expected = new long[19];
			for (int i = 0; i < 19; i++) expected[i] = i + 1;
			assertArrayEquals(expected, dq.toArray());
		}
	}

	@Nested
	class Bulk {

		@Test
		void fillAndSetAllWhenWrapped() {
			final Target dq = wrapped(new long[]{3, 1, 4});
			assertSame(dq.self(), dq.fill(7));
			assertArrayEquals(new long[]{7, 7, 7}, dq.toArray());
			final Target dq2 = wrapped(new long[]{3, 1, 4});
			assertSame(dq2.self(), dq2.setAll(i -> 10L * i));
			assertArrayEquals(new long[]{0, 10, 20}, dq2.toArray());
			dq2.addFirst(-1);
			assertArrayEquals(new long[]{-1, 0, 10, 20}, dq2.toArray());
		}

		@Test
		void clear() {
			final Target dq = create(new long[]{3, 1, 4});
			dq.clear();
			assertEquals(0, dq.size());
			dq.addFirst(2);
			assertArrayEquals(new long[]{2}, dq.toArray());
		}
	}

	@Nested
	class Transformation {

		@Test
		void swapAndReverse() {
			final Target dq = wrapped(new long[]{1, 2, 3, 4, 5});
			assertSame(dq.self(), dq.swap(0, 4));
			assertArrayEquals(new long[]{5, 2, 3, 4, 1}, dq.toArray());
			assertSame(dq.self(), dq.reverse(1, 4));
			assertArrayEquals(new long[]{5, 4, 3, 2, 1}, dq.toArray());
			assertSame(dq.self(), dq.reverse());
			assertArrayEquals(new long[]{1, 2, 3, 4, 5}, dq.toArray());
		}
	}

	@Nested
	class Conversion {

		@Test
		void iteratorAndToString() {
			final Target dq = wrapped(new long[]{3, 1, 4});
			final Iterator<? extends Number> it = dq.iterator();
			for (final long v : new long[]{3, 1, 4}) assertEquals(v, it.next().longValue());
			assertFalse(it.hasNext());
			assertThrows(NoSuchElementException.class, it::next);
			assertEquals("3 1 4", dq.toString());
			assertEquals("", create().toString());
		}
	}

	@RepeatedTest(100)
	void matchesNaive(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final ArrayDeque<Long> naive = new ArrayDeque<>();
		final Target dq = create(1 + random.nextInt(4));
		new RandomOperations()
			.add("addFirst", 3, (r, at) -> {
				final long v = r.nextInt(1000);
				naive.addFirst(v);
				dq.addFirst(v);
			})
			.add("addLast", 3, (r, at) -> {
				final long v = r.nextInt(1000);
				naive.addLast(v);
				dq.addLast(v);
			})
			.add("removeFirst", 2, (r, at) -> assertEquals(naive.isEmpty() ? -1L : naive.removeFirst(), dq.removeFirstOrDefault(-1), at))
			.add("removeLast", 2, (r, at) -> assertEquals(naive.isEmpty() ? -1L : naive.removeLast(), dq.removeLastOrDefault(-1), at))
			.add("getSet", 2, (r, at) -> {
				if (naive.isEmpty()) return;
				final long[] a = toArray(naive);
				final int i = r.nextInt(a.length);
				assertEquals(a[i], dq.get(i), at);
				final long v = r.nextInt(1000);
				assertEquals(a[i], dq.set(i, v), at);
				a[i] = v;
				naive.clear();
				for (final long x : a) naive.addLast(x);
			})
			.add("toArray", (r, at) -> {
				assertEquals(naive.size(), dq.size(), at);
				assertArrayEquals(toArray(naive), dq.toArray(), at);
			})
			.run(seed, random, 400);
	}

	private static long[] toArray(final Deque<Long> d) {
		return d.stream().mapToLong(Long::longValue).toArray();
	}
}
