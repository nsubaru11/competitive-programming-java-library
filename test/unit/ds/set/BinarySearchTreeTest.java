package unit.ds.set;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.ds.set.*;

import unit.support.*;

import static org.junit.jupiter.api.Assertions.*;

final class BinarySearchTreeTest {

	private static BinarySearchTree<Integer> of(final Integer... values) {
		final BinarySearchTree<Integer> t = new BinarySearchTree<>();
		t.addAll(List.of(values));
		return t;
	}

	@RepeatedTest(100)
	void matchesNaive(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final BinarySearchTree<Integer> t = new BinarySearchTree<>();
		final TreeSet<Integer> naive = new TreeSet<>();
		new RandomOperations()
				.add("add", 3, (r, at) -> {
					final int v = r.nextInt(-20, 20);
					assertEquals(naive.add(v), t.add(v), at);
				})
				.add("remove", 2, (r, at) -> {
					final int v = r.nextInt(-20, 20);
					assertEquals(naive.remove(v), t.remove(v), at);
				})
				.add("navigation", 2, (r, at) -> {
					final int k = r.nextInt(-22, 22);
					assertEquals(naive.higher(k), t.higher(k), at);
					assertEquals(naive.floor(k), t.floor(k), at);
					assertEquals(naive.contains(k), t.contains(k), at);
				})
				.add("iterate", (r, at) -> {
					assertEquals(List.copyOf(naive), List.copyOf(t), at);
					assertEquals(naive.size(), t.size(), at);
				})
				.run(seed, random, 300);
	}

	@Nested
	class State {

		@Test
		void sizeIsEmptyAndClear() {
			final BinarySearchTree<Integer> t = of(3, 1, 3);
			assertEquals(2, t.size());
			assertFalse(t.isEmpty());
			t.clear();
			assertTrue(t.isEmpty());
		}
	}

	@Nested
	class Access {

		@Test
		void contains() {
			final BinarySearchTree<Integer> t = of(3, 1, 2);
			assertTrue(t.contains(2));
			assertFalse(t.contains(5));
			assertFalse(t.contains(null));
			assertFalse(t.contains("x"), "型が違う値は含まない");
			assertTrue(t.containsAll(List.of(1, 3)));
			assertFalse(t.containsAll(List.of(1, 4)));
		}
	}

	@Nested
	class Updates {

		@Test
		void addAndRemove() {
			final BinarySearchTree<Integer> t = new BinarySearchTree<>();
			assertTrue(t.add(2));
			assertFalse(t.add(2));
			assertFalse(t.add(null));
			assertTrue(t.addAll(List.of(1, 3)));
			assertFalse(t.addAll(List.of(1)));
			assertTrue(t.remove(2));
			assertFalse(t.remove(2));
			assertFalse(t.remove(null));
			assertTrue(t.remove(1));
			assertTrue(t.remove(3));
			assertTrue(t.isEmpty());
		}

		@Test
		void removeAllAndRetainAll() {
			final BinarySearchTree<Integer> t = of(1, 2, 3, 4, 5);
			assertTrue(t.removeAll(List.of(2, 4, 9)));
			assertEquals(List.of(1, 3, 5), List.copyOf(t));
			assertFalse(t.removeAll(List.of(9)));
			assertTrue(t.retainAll(List.of(3, 5, 7)));
			assertEquals(List.of(3, 5), List.copyOf(t));
			assertFalse(t.retainAll(List.of(3, 5)));
			assertTrue(t.retainAll(List.of()));
			assertTrue(t.isEmpty());
			assertThrows(NullPointerException.class, () -> t.removeAll(null));
		}
	}

	@Nested
	class Search {

		@Test
		void navigation() {
			final BinarySearchTree<Integer> t = of(10, 20, 30);
			assertEquals(20, t.higher(10));
			assertEquals(10, t.ceiling(10));
			assertEquals(20, t.lower(30));
			assertEquals(20, t.floor(25));
			assertNull(t.higher(30));
			assertNull(t.lower(10));
			assertNull(t.floor(null));
		}
	}

	@Nested
	class Conversion {

		@Test
		void ascendingArraysAndStream() {
			final BinarySearchTree<Integer> t = of(3, 1, 2);
			assertArrayEquals(new Object[]{1, 2, 3}, t.toArray());
			assertArrayEquals(new Integer[]{1, 2, 3}, t.toArray(new Integer[0]));
			final Integer[] big = {9, 9, 9, 9, 9};
			assertSame(big, t.toArray(big));
			assertArrayEquals(new Integer[]{1, 2, 3, null, 9}, big, "要素数の次に null を置く");
			assertEquals(List.of(1, 2, 3), t.stream().toList());
		}

		@Test
		void comparator() {
			final BinarySearchTree<Integer> t = new BinarySearchTree<Integer>(Comparator.reverseOrder());
			t.addAll(List.of(1, 3, 2));
			assertEquals(List.of(3, 2, 1), List.copyOf(t));
			assertEquals(1, t.higher(2));
		}
	}
}
