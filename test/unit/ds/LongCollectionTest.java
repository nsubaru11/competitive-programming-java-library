package unit.ds;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.ds.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * LongCollection の既定メソッドを、配列で実装した最小のコレクションで検査する。
 */
final class LongCollectionTest {

	private static LongCollection of(final long... a) {
		return new LongCollection() {
			public int size() {
				return a.length;
			}

			public PrimitiveIterator.OfLong iterator() {
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
			final LongCollection c = of(3, 1, 4);
			assertTrue(c.contains(4));
			assertFalse(c.contains(2));
			assertFalse(of().contains(0));
		}
	}

	@Nested
	class Conversion {

		@Test
		void forEachLong() {
			final List<Long> visited = new ArrayList<>();
			of(3, 1, 4).forEachLong(visited::add);
			assertEquals(List.of(3L, 1L, 4L), visited);
		}

		@Test
		void spliteratorIsSized() {
			final Spliterator.OfLong s = of(3, 1, 4).spliterator();
			assertEquals(3, s.estimateSize());
			assertTrue(s.hasCharacteristics(Spliterator.SIZED));
		}

		@Test
		void streamListAndArray() {
			final LongCollection c = of(3, 1, 4);
			assertEquals(8L, c.longStream().sum());
			assertEquals(List.of(3L, 1L, 4L), c.toList());
			assertThrows(UnsupportedOperationException.class, () -> c.toList().add(1L));
			assertArrayEquals(new long[]{3, 1, 4}, c.toArray());
			assertArrayEquals(new long[0], of().toArray());
		}
	}
}
