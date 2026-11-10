package unit.ds.arrays;

import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import lib.ds.arrays.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * IntArray と IntMutableArray の既定メソッドを、配列で実装した最小の配列で検査する。
 */
final class IntMutableArrayTest {

	static IntMutableArray of(final int... a) {
		final int[] arr = a.clone();
		return new IntMutableArray() {
			public int size() {
				return arr.length;
			}

			public int get(final int i) {
				return arr[i];
			}

			public int set(final int i, final int v) {
				final int old = arr[i];
				arr[i] = v;
				return old;
			}

			public PrimitiveIterator.OfInt iterator() {
				return Arrays.stream(arr).iterator();
			}
		};
	}

	@Nested
	class Access {

		@ParameterizedTest
		@CsvSource({
				"-1, -9",
				"0, 3",
				"2, 4",
				"3, -9",
		})
		void getOrDefault(final int i, final int expected) {
			assertEquals(expected, of(3, 1, 4).getOrDefault(i, -9));
		}

		@Test
		void firstAndLast() {
			final IntMutableArray a = of(3, 1, 4);
			assertEquals(3, a.getFirst());
			assertEquals(4, a.getLast());
			assertEquals(3, a.getFirstOrDefault(-9));
			assertEquals(4, a.getLastOrDefault(-9));
		}

		@Test
		void firstAndLastOfEmpty() {
			final IntMutableArray a = of();
			assertThrows(NoSuchElementException.class, a::getFirst);
			assertThrows(NoSuchElementException.class, a::getLast);
			assertEquals(-9, a.getFirstOrDefault(-9));
			assertEquals(-9, a.getLastOrDefault(-9));
		}
	}

	@Nested
	class Updates {

		@Test
		void returnDocumentedValues() {
			final IntMutableArray a = of(3, 1, 4);
			assertEquals(1, a.set(1, 5), "set は更新前の値");
			assertEquals(7, a.add(1, 2), "add は更新後の値");
			assertEquals(14, a.multiply(1, 2));
			assertEquals(29, a.affine(1, 2, 1), "affine は 現在値 * m + d");
			assertEquals(30, a.apply(1, 30, Math::max), "apply は f(現在値, x)");
			assertArrayEquals(new int[]{3, 30, 4}, a.toArray());
		}
	}

	@Nested
	class Bulk {

		@Test
		void fillAndSetAll() {
			final IntMutableArray a = of(3, 1, 4);
			assertSame(a, a.fill(7));
			assertArrayEquals(new int[]{7, 7, 7}, a.toArray());
			assertSame(a, a.setAll(i -> i * i));
			assertArrayEquals(new int[]{0, 1, 4}, a.toArray());
		}
	}

	@Nested
	class Search {

		@Test
		void indexOf() {
			final IntMutableArray a = of(3, 1, 3, 4);
			assertEquals(0, a.indexOf(3));
			assertEquals(2, a.lastIndexOf(3));
			assertEquals(-1, a.indexOf(2));
			assertEquals(-1, a.lastIndexOf(2));
		}
	}

	@Nested
	class Transformation {

		@Test
		void swap() {
			final IntMutableArray a = of(3, 1, 4);
			assertSame(a, a.swap(0, 2));
			assertArrayEquals(new int[]{4, 1, 3}, a.toArray());
		}

		@Test
		void reverse() {
			final IntMutableArray a = of(1, 2, 3, 4, 5);
			assertSame(a, a.reverse(1, 4));
			assertArrayEquals(new int[]{1, 4, 3, 2, 5}, a.toArray());
			a.reverse(2, 2);
			assertArrayEquals(new int[]{1, 4, 3, 2, 5}, a.toArray());
			assertSame(a, a.reverse());
			assertArrayEquals(new int[]{5, 2, 3, 4, 1}, a.toArray());
			assertThrows(IndexOutOfBoundsException.class, () -> a.reverse(0, 6));
			assertThrows(IndexOutOfBoundsException.class, () -> a.reverse(3, 2));
		}
	}

	@Nested
	class Conversion {

		@Test
		void spliteratorIsOrdered() {
			final Spliterator.OfInt s = of(3, 1, 4).spliterator();
			assertTrue(s.hasCharacteristics(Spliterator.ORDERED));
			assertTrue(s.hasCharacteristics(Spliterator.SIZED));
			assertEquals(3, s.estimateSize());
		}
	}
}
