package unit.ds;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.ds.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * IntCollection の既定メソッドを、配列で実装した最小のコレクションで検査する。
 */
final class IntCollectionTest {

	private static IntCollection of(final int... a) {
		return new IntCollection() {
			public int size() {
				return a.length;
			}

			public PrimitiveIterator.OfInt iterator() {
				return Arrays.stream(a).iterator();
			}
		};
	}

	@Nested
	class State {

		@Test
		void isEmpty() {
			assertTrue(of().isEmpty());
			assertFalse(of(1).isEmpty());
		}
	}

	@Nested
	class Search {

		@Test
		void contains() {
			final IntCollection c = of(3, 1, 4);
			assertTrue(c.contains(4));
			assertFalse(c.contains(2));
			assertFalse(of().contains(0));
		}
	}

	@Nested
	class Conversion {

		@Test
		void forEachInt() {
			final List<Integer> visited = new ArrayList<>();
			of(3, 1, 4).forEachInt(visited::add);
			assertEquals(List.of(3, 1, 4), visited);
		}

		@Test
		void spliteratorIsSized() {
			final Spliterator.OfInt s = of(3, 1, 4).spliterator();
			assertEquals(3, s.estimateSize());
			assertTrue(s.hasCharacteristics(Spliterator.SIZED));
		}

		@Test
		void streamListAndArray() {
			final IntCollection c = of(3, 1, 4);
			assertEquals(8, c.intStream().sum());
			assertEquals(List.of(3, 1, 4), c.toList());
			assertThrows(UnsupportedOperationException.class, () -> c.toList().add(1));
			assertArrayEquals(new int[]{3, 1, 4}, c.toArray());
			assertArrayEquals(new int[0], of().toArray());
		}
	}
}
