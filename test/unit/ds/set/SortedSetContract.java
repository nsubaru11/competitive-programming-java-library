package unit.ds.set;

import java.util.*;
import java.util.stream.*;

import org.junit.jupiter.api.*;

import unit.support.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 順序付き集合（IntAVLSet・LongAVLSet・AVLSet）に共通する振る舞いのテスト。値は long で扱う。
 * 期待値は Guide の記載に従う。
 */
abstract class SortedSetContract {

	abstract Target create();

	/**
	 * 対象の要素型（Integer・Long）のコレクションを作る。
	 */
	abstract Collection<?> collection(long... values);

	Target of(final long... values) {
		final Target s = create();
		for (final long v : values) s.add(v);
		return s;
	}

	@RepeatedTest(100)
	void matchesNaive(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final Target s = create();
		final TreeSet<Long> naive = new TreeSet<>();
		new RandomOperations()
				.add("add", 4, (r, at) -> {
					final long v = r.nextInt(-30, 30);
					assertEquals(naive.add(v), s.add(v), at);
				})
				.add("remove", 2, (r, at) -> {
					final long v = r.nextInt(-30, 30);
					assertEquals(naive.remove(v), s.remove(v), at);
				})
				.add("removeAt", (r, at) -> {
					if (naive.isEmpty()) return;
					final int i = r.nextInt(naive.size());
					final long v = naive.stream().skip(i).findFirst().orElseThrow();
					naive.remove(v);
					assertTrue(s.removeAt(i), at);
				})
				.add("poll", (r, at) -> {
					if (naive.isEmpty()) return;
					if (r.nextBoolean()) assertEquals((long) naive.pollFirst(), s.pollFirst(), at);
					else assertEquals((long) naive.pollLast(), s.pollLast(), at);
				})
				.add("navigation", 2, (r, at) -> {
					final long k = r.nextInt(-35, 35);
					assertEquals(naive.higher(k), s.higher(k), at);
					assertEquals(naive.ceiling(k), s.ceiling(k), at);
					assertEquals(naive.lower(k), s.lower(k), at);
					assertEquals(naive.floor(k), s.floor(k), at);
					final int less = naive.headSet(k).size();
					assertEquals(naive.contains(k) ? less : ~less, s.rank(k), at);
					assertEquals(naive.contains(k) ? naive.headSet(k).size() : -1, s.indexOf(k), at);
				})
				.add("index", (r, at) -> {
					assertEquals(naive.size(), s.size(), at);
					if (naive.isEmpty()) return;
					final int i = r.nextInt(naive.size());
					assertEquals((long) naive.stream().skip(i).findFirst().orElseThrow(), s.getByIndex(i), at);
					assertEquals((long) naive.first(), s.first(), at);
					assertEquals((long) naive.last(), s.last(), at);
				})
				.add("toArray", (r, at) -> assertArrayEquals(naive.stream().mapToLong(Long::longValue).toArray(), s.toArray(), at))
				.run(seed, random, 400);
	}

	interface Target {
		int size();

		boolean isEmpty();

		void clear();

		boolean contains(long t);

		boolean containsAll(Collection<?> c);

		boolean add(long t);

		boolean addAll(Collection<?> c);

		boolean remove(long t);

		boolean removeAll(Collection<?> c);

		boolean removeAt(int index);

		long[] toArray();

		BaseStream<?, ?> stream();

		Iterator<?> iterator();

		long getByIndex(int index);

		int indexOf(long t);

		int rank(long t);

		Long higher(long key);

		Long ceiling(long key);

		Long lower(long key);

		Long floor(long key);

		long first();

		long last();

		long pollFirst();

		long pollLast();
	}

	@Nested
	class State {

		@Test
		void sizeIsEmptyAndClear() {
			final Target s = of(3, 1, 3, 2);
			assertEquals(3, s.size(), "重複は無視する");
			assertFalse(s.isEmpty());
			s.clear();
			assertTrue(s.isEmpty());
			assertEquals(0, s.size());
			s.add(5);
			assertEquals(5L, s.first());
		}
	}

	@Nested
	class Access {

		@Test
		void containsAndIndex() {
			final Target s = of(30, 10, 20);
			assertTrue(s.contains(20));
			assertFalse(s.contains(25));
			assertFalse(create().contains(1));
			assertTrue(s.containsAll(collection(10, 30)));
			assertFalse(s.containsAll(collection(10, 25)));
			assertTrue(create().containsAll(collection()));
			assertFalse(create().containsAll(collection(1)));
			assertEquals(20L, s.getByIndex(1));
			assertThrows(IndexOutOfBoundsException.class, () -> s.getByIndex(3));
			assertThrows(IndexOutOfBoundsException.class, () -> s.getByIndex(-1));
			assertEquals(10L, s.first());
			assertEquals(30L, s.last());
		}
	}

	@Nested
	class Updates {

		@Test
		void addAndRemoveReturnWhetherChanged() {
			final Target s = create();
			assertTrue(s.add(2));
			assertFalse(s.add(2));
			assertTrue(s.addAll(collection(1, 2, 3)));
			assertFalse(s.addAll(collection(1, 3)));
			assertTrue(s.remove(1));
			assertFalse(s.remove(1));
			assertFalse(create().remove(1));
			assertTrue(s.removeAll(collection(3, 9)));
			assertFalse(s.removeAll(collection(9)));
			assertFalse(create().removeAll(collection(1)));
			assertArrayEquals(new long[]{2}, s.toArray());
		}

		@Test
		void removeUpdatesEndpoints() {
			final Target s = of(1, 2, 3, 4);
			s.remove(1);
			s.remove(4);
			assertEquals(2L, s.first());
			assertEquals(3L, s.last());
			assertTrue(s.removeAt(0));
			assertEquals(3L, s.first());
			assertThrows(IndexOutOfBoundsException.class, () -> s.removeAt(1));
		}

		@Test
		void poll() {
			final Target s = of(5, 1, 3);
			assertEquals(1L, s.pollFirst());
			assertEquals(5L, s.pollLast());
			assertEquals(3L, s.pollFirst());
			assertThrows(RuntimeException.class, s::pollFirst, "空なら例外");
			assertThrows(RuntimeException.class, s::pollLast);
		}
	}

	@Nested
	class Search {

		@Test
		void indexOfAndRank() {
			final Target s = of(10, 20, 30);
			assertEquals(1, s.indexOf(20));
			assertEquals(-1, s.indexOf(25), "存在しなければ -1");
			assertEquals(-1, create().indexOf(1));
			assertEquals(1, s.rank(20), "存在すればそのインデックス");
			assertEquals(~2, s.rank(25), "存在しなければ ~挿入位置");
			assertEquals(~0, s.rank(5));
			assertEquals(~3, s.rank(35));
		}

		@Test
		void navigation() {
			final Target s = of(10, 20, 30);
			assertEquals(20L, s.higher(10));
			assertEquals(10L, s.ceiling(10));
			assertEquals(20L, s.ceiling(15));
			assertNull(s.higher(30));
			assertEquals(10L, s.higher(5));
			assertEquals(20L, s.lower(30));
			assertEquals(30L, s.floor(30));
			assertEquals(20L, s.floor(25));
			assertNull(s.lower(10));
			assertEquals(30L, s.lower(99));
			assertNull(create().ceiling(1));
		}
	}

	@Nested
	class Conversion {

		@Test
		void ascendingTraversal() {
			final Target s = of(3, -1, 2);
			assertArrayEquals(new long[]{-1, 2, 3}, s.toArray());
			assertArrayEquals(new long[0], create().toArray());
			final List<Long> streamed = new ArrayList<>();
			s.stream().iterator().forEachRemaining(v -> streamed.add(((Number) v).longValue()));
			assertEquals(List.of(-1L, 2L, 3L), streamed);
			final Iterator<?> it = s.iterator();
			for (int i = 0; i < 3; i++) it.next();
			assertFalse(it.hasNext());
			assertThrows(NoSuchElementException.class, it::next);
			assertEquals("[-1, 2, 3]", s.toString());
			assertEquals("[]", create().toString());
		}
	}
}
