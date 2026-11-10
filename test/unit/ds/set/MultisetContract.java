package unit.ds.set;

import java.util.*;
import java.util.stream.*;

import org.junit.jupiter.api.*;

import unit.support.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 多重集合（IntAVLMultiset・LongAVLMultiset・AVLMultiset）に共通する振る舞いのテスト。値は long で扱う。
 * 期待値は Guide の記載に従う。インデックスは重複を含めた昇順の位置、ユニークインデックスは重複を除いた位置である。
 */
abstract class MultisetContract {

	static List<Long> collect(final Iterator<?> it) {
		final List<Long> res = new ArrayList<>();
		it.forEachRemaining(v -> res.add(((Number) v).longValue()));
		return res;
	}

	abstract Target create();

	abstract Collection<?> collection(long... values);

	Target of(final long... values) {
		final Target s = create();
		for (final long v : values) s.add(v);
		return s;
	}

	// 昇順は 10 20 20 20 30
	Target sample() {
		return of(20, 10, 20, 30, 20);
	}

	@RepeatedTest(100)
	void matchesNaive(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final Target s = create();
		final TreeMap<Long, Long> naive = new TreeMap<>();
		new RandomOperations()
				.add("add", 4, (r, at) -> {
					final long v = r.nextInt(-10, 10), c = r.nextInt(1, 3);
					naive.merge(v, c, Long::sum);
					assertTrue(s.add(v, c), at);
				})
				.add("remove", 2, (r, at) -> {
					final long v = r.nextInt(-10, 10);
					final boolean had = naive.containsKey(v);
					if (had && naive.merge(v, -1L, Long::sum) == 0) naive.remove(v);
					assertEquals(had, s.remove(v), at);
				})
				.add("removeAll", (r, at) -> {
					final long v = r.nextInt(-10, 10);
					assertEquals(naive.remove(v) != null, s.removeAll(v), at);
				})
				.add("query", 3, (r, at) -> {
					final long k = r.nextInt(-12, 12);
					final long less = naive.headMap(k).values().stream().mapToLong(Long::longValue).sum();
					assertEquals(naive.getOrDefault(k, 0L), s.count(k), at);
					final boolean has = naive.containsKey(k);
					final long uniqueLess = naive.headMap(k).size();
					assertEquals(has ? less : ~less, s.rank(k), at);
					assertEquals(has ? uniqueLess : ~uniqueLess, s.uniqueRank(k), at);
					assertEquals(has ? less : ~less, s.lowerBound(k), at);
					assertEquals(has ? less + naive.get(k) - 1 : ~less, s.upperBound(k), at);
					assertEquals(naive.containsKey(k) ? less : -1, s.indexOf(k), at);
					assertEquals(naive.higherKey(k), s.higher(k), at);
					assertEquals(naive.floorKey(k), s.floor(k), at);
				})
				.add("toArray", (r, at) -> {
					final List<Long> expected = new ArrayList<>();
					naive.forEach((v, c) -> {
						for (long i = 0; i < c; i++) expected.add(v);
					});
					assertEquals(expected, collect(s.iterator()), at);
					assertEquals(expected.size(), s.size(), at);
					assertEquals(naive.size(), s.uniqueSize(), at);
					if (!expected.isEmpty()) {
						final int i = r.nextInt(expected.size());
						assertEquals((long) expected.get(i), s.getByIndex(i), at);
						assertEquals((long) naive.firstKey(), s.first(), at);
						assertEquals((long) naive.lastKey(), s.last(), at);
					}
				})
				.run(seed, random, 400);
	}

	interface Target {
		long size();

		int uniqueSize();

		boolean isEmpty();

		void clear();

		boolean contains(long t);

		boolean containsAll(Collection<?> c);

		boolean add(long t);

		boolean add(long t, long cnt);

		boolean addAll(Collection<?> c);

		boolean remove(long t);

		boolean remove(long t, long cnt);

		boolean removeAll(long t);

		boolean removeAll(Collection<?> c);

		boolean removeAt(long index);

		boolean removeUniqueAt(int index);

		long count(long t);

		long getByIndex(long index);

		long getByUniqueIndex(int index);

		long indexOf(long t);

		int uniqueIndexOf(long t);

		long rank(long t);

		long uniqueRank(long t);

		long lowerBound(long t);

		long upperBound(long t);

		Long higher(long key);

		Long ceiling(long key);

		Long lower(long key);

		Long floor(long key);

		long first();

		long last();

		long pollFirst();

		long pollLast();

		long pollFirstAll();

		long pollLastAll();

		long[] toArray();

		long[] toUniqueArray();

		BaseStream<?, ?> stream();

		BaseStream<?, ?> uniqueStream();

		Iterator<?> iterator();

		Iterator<?> uniqueIterator();
	}

	@Nested
	class State {

		@Test
		void sizes() {
			final Target s = sample();
			assertEquals(5L, s.size());
			assertEquals(3, s.uniqueSize());
			assertFalse(s.isEmpty());
			s.clear();
			assertTrue(s.isEmpty());
			assertEquals(0, s.uniqueSize());
		}
	}

	@Nested
	class Access {

		@Test
		void containsCountAndIndex() {
			final Target s = sample();
			assertTrue(s.contains(20));
			assertFalse(s.contains(25));
			assertTrue(s.containsAll(collection(10, 30)));
			assertFalse(s.containsAll(collection(10, 25)));
			assertEquals(3L, s.count(20));
			assertEquals(0L, s.count(25));
			assertEquals(20L, s.getByIndex(3));
			assertEquals(30L, s.getByIndex(4));
			assertEquals(30L, s.getByUniqueIndex(2));
			assertThrows(IndexOutOfBoundsException.class, () -> s.getByIndex(5));
			assertThrows(IndexOutOfBoundsException.class, () -> s.getByUniqueIndex(3));
			assertEquals(10L, s.first());
			assertEquals(30L, s.last());
		}
	}

	@Nested
	class Updates {

		@Test
		void addAndRemoveCounts() {
			final Target s = create();
			assertTrue(s.add(5, 3));
			assertTrue(s.add(5));
			assertEquals(4L, s.count(5));
			assertTrue(s.remove(5));
			assertTrue(s.remove(5, 2));
			assertEquals(1L, s.count(5));
			assertFalse(s.remove(6));
			assertTrue(s.removeAll(5));
			assertFalse(s.contains(5));
			assertThrows(IllegalArgumentException.class, () -> s.add(1, 0));
			assertThrows(IllegalArgumentException.class, () -> s.remove(1, 0));
		}

		@Test
		void collections() {
			final Target s = create();
			assertTrue(s.addAll(collection(1, 1, 2, 3)));
			assertEquals(4L, s.size());
			assertTrue(s.removeAll(collection(1, 3)), "値ごとにすべて削除する");
			assertArrayEquals(new long[]{2}, s.toArray());
			assertFalse(s.removeAll(collection(9)));
		}

		@Test
		void removeByIndex() {
			final Target s = sample();
			assertTrue(s.removeAt(0));
			assertEquals(20L, s.first());
			assertTrue(s.removeUniqueAt(0), "ユニークインデックスの値をすべて削除する");
			assertArrayEquals(new long[]{30}, s.toArray());
			assertThrows(IndexOutOfBoundsException.class, () -> s.removeAt(1));
			assertThrows(IndexOutOfBoundsException.class, () -> s.removeUniqueAt(1));
		}

		@Test
		void poll() {
			final Target s = sample();
			assertEquals(10L, s.pollFirst());
			assertEquals(30L, s.pollLast());
			assertEquals(20L, s.pollFirst());
			assertEquals(2L, s.count(20));
			assertEquals(20L, s.pollLastAll());
			assertTrue(s.isEmpty());
			final Target t = sample();
			assertEquals(10L, t.pollFirstAll());
			assertEquals(20L, t.pollFirstAll());
			assertArrayEquals(new long[]{30}, t.toArray());
			assertThrows(RuntimeException.class, create()::pollFirst, "空なら例外");
			assertThrows(RuntimeException.class, create()::pollLastAll);
		}
	}

	@Nested
	class Search {

		@Test
		void indexAndRank() {
			final Target s = sample();
			assertEquals(1L, s.indexOf(20), "最初の要素のインデックス");
			assertEquals(-1L, s.indexOf(25));
			assertEquals(1, s.uniqueIndexOf(20));
			assertEquals(-1, s.uniqueIndexOf(25));
			assertEquals(1L, s.rank(20), "存在すれば最初の要素のインデックス");
			assertEquals(~4L, s.rank(25), "存在しなければ ~挿入位置");
			assertEquals(1L, s.uniqueRank(20));
			assertEquals(~2L, s.uniqueRank(25));
		}

		@Test
		void bounds() {
			final Target s = sample();
			// ArrayBinarySearch と同じく、一致する左端・右端、なければ ~挿入位置
			assertEquals(1L, s.lowerBound(20), "一致する左端");
			assertEquals(3L, s.upperBound(20), "一致する右端");
			assertEquals(~4L, s.lowerBound(25));
			assertEquals(~4L, s.upperBound(25));
			assertEquals(~0L, s.lowerBound(5));
			assertEquals(4L, s.upperBound(30));
		}

		@Test
		void navigation() {
			final Target s = sample();
			assertEquals(30L, s.higher(20));
			assertEquals(20L, s.ceiling(20));
			assertEquals(10L, s.lower(20));
			assertEquals(20L, s.floor(25));
			assertNull(s.higher(30));
			assertNull(s.lower(10));
		}
	}

	@Nested
	class Conversion {

		@Test
		void arraysStreamsAndIterators() {
			final Target s = sample();
			assertArrayEquals(new long[]{10, 20, 20, 20, 30}, s.toArray());
			assertArrayEquals(new long[]{10, 20, 30}, s.toUniqueArray());
			assertEquals(List.of(10L, 20L, 20L, 20L, 30L), collect(s.stream().iterator()));
			assertEquals(List.of(10L, 20L, 30L), collect(s.uniqueStream().iterator()));
			assertEquals(List.of(10L, 20L, 20L, 20L, 30L), collect(s.iterator()));
			assertEquals(List.of(10L, 20L, 30L), collect(s.uniqueIterator()));
			assertEquals("[10, 20, 20, 20, 30]", s.toString());
		}
	}
}
