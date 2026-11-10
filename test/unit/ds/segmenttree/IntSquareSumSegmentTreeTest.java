package unit.ds.segmenttree;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.ds.arrays.*;
import lib.ds.segmenttree.*;

import unit.support.*;

import static org.junit.jupiter.api.Assertions.*;

final class IntSquareSumSegmentTreeTest {

	private static final int[] SAMPLE = {1, 2, 3, 4};

	private static long affine(final long x, final long m, final long d, final long mod) {
		return Math.floorMod(Math.floorMod(m, mod) * x % mod + d, mod);
	}

	@RepeatedTest(100)
	void matchesNaive(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final int n = 1 + random.nextInt(20);
		final int mod = random.nextBoolean() ? 998244353 : random.nextBoolean() ? 2_000_000_000 : 1 + random.nextInt(20);
		final long[] naive = new long[n];
		final int[] init = new int[n];
		for (int i = 0; i < n; i++) {
			init[i] = random.nextInt(-mod + 1, mod);
			naive[i] = Math.floorMod(init[i], mod);
		}
		final IntSquareSumSegmentTree t = new IntSquareSumSegmentTree(init, mod);
		new RandomOperations()
				.add("affineRange", 3, (r, at) -> {
					final int l = r.nextInt(n + 1), rr = r.nextInt(n + 1), m = r.nextInt(-mod + 1, mod), d = r.nextInt(-mod + 1, mod);
					t.affine(l, rr, m, d);
					for (int i = l; i < rr; i++) naive[i] = affine(naive[i], m, d, mod);
				})
				.add("affinePoint", (r, at) -> {
					final int i = r.nextInt(n), m = r.nextInt(-mod + 1, mod), d = r.nextInt(-mod + 1, mod);
					naive[i] = affine(naive[i], m, d, mod);
					assertEquals(naive[i], t.affine(i, m, d), at);
				})
				.add("set", (r, at) -> {
					final int i = r.nextInt(n), v = r.nextInt(mod);
					assertEquals(naive[i], t.set(i, v), at);
					naive[i] = v;
				})
				.add("query", 3, (r, at) -> {
					final int l = r.nextInt(n + 1), rr = r.nextInt(n + 1);
					long s = 0, s2 = 0;
					for (int i = l; i < rr; i++) {
						s = (s + naive[i]) % mod;
						s2 = (s2 + naive[i] * naive[i] % mod) % mod;
					}
					assertEquals(s, t.query(l, rr), at);
					assertEquals(s2, t.query2(l, rr), at);
				})
				.add("toArray", (r, at) -> assertArrayEquals(naive, Arrays.stream(t.toArray()).asLongStream().toArray(), at))
				.run(seed, random, 300);
	}

	@Nested
	class Construction {

		@Test
		void defaultModAndConstructors() {
			assertEquals(998244353, new IntSquareSumSegmentTree(3).mod);
			assertEquals(4, new IntSquareSumSegmentTree(4).size());
			assertEquals(0, new IntSquareSumSegmentTree(4).queryAll());
			assertEquals(30, new IntSquareSumSegmentTree(SAMPLE).query2All());
			assertEquals(10, new IntSquareSumSegmentTree(new IntArrayDeque(SAMPLE)).queryAll());
			assertEquals(30 % 7, new IntSquareSumSegmentTree(new IntArrayDeque(SAMPLE), 7).query2All());
			assertEquals(10 % 7, new IntSquareSumSegmentTree(SAMPLE, 7).queryAll());
			assertEquals(7, new IntSquareSumSegmentTree(2, 7).mod);
		}

		@Test
		void copyIsIndependent() {
			final IntSquareSumSegmentTree t = new IntSquareSumSegmentTree(SAMPLE);
			t.add(0, 4, 1);
			final IntSquareSumSegmentTree c = new IntSquareSumSegmentTree(t);
			c.add(0, 4, 1);
			assertEquals(14, t.queryAll());
			assertEquals(18, c.queryAll());
		}

		@Test
		void inputIsNormalized() {
			final IntSquareSumSegmentTree t = new IntSquareSumSegmentTree(new int[]{-1, 9}, 7);
			assertEquals(6, t.get(0));
			assertEquals(2, t.get(1));
			assertEquals(1, t.queryAll());
			assertEquals((1 + 4) % 7, t.query2All());
		}
	}

	@Nested
	class Access {

		@Test
		void getAndOutOfRange() {
			final IntSquareSumSegmentTree t = new IntSquareSumSegmentTree(SAMPLE);
			assertEquals(3, t.get(2));
			assertThrows(IndexOutOfBoundsException.class, () -> t.get(4));
		}
	}

	@Nested
	class Updates {

		@Test
		void pointUpdatesReturnDocumentedValues() {
			final IntSquareSumSegmentTree t = new IntSquareSumSegmentTree(SAMPLE);
			assertEquals(3, t.set(2, 5), "set は更新前の値");
			assertEquals(7, t.add(2, 2), "add は更新後の値");
			assertEquals(14, t.multiply(2, 2));
			assertEquals(29, t.affine(2, 2, 1));
			assertEquals(1 + 4 + 29 * 29 + 16, t.query2All());
		}

		@Test
		void rangeUpdates() {
			final IntSquareSumSegmentTree t = new IntSquareSumSegmentTree(SAMPLE);
			t.add(1, 3, 10);
			assertEquals("1 12 13 4", t.toString());
			t.multiply(0, 2, 3);
			assertEquals("3 36 13 4", t.toString());
			t.affine(2, 4, 2, 1);
			assertEquals("3 36 27 9", t.toString());
			t.set(0, 3, 5);
			assertEquals("5 5 5 9", t.toString());
			assertEquals(75 + 81, t.query2(0, 4));
		}

		@Test
		void emptyRangeDoesNothingAndOutOfRangeThrows() {
			final IntSquareSumSegmentTree t = new IntSquareSumSegmentTree(SAMPLE);
			t.add(2, 2, 100);
			t.affine(3, 1, 0, 0);
			assertEquals("1 2 3 4", t.toString());
			assertEquals(0, t.query(3, 1));
			assertEquals(0, t.query2(3, 3));
			assertThrows(IndexOutOfBoundsException.class, () -> t.add(0, 5, 1));
			assertThrows(IndexOutOfBoundsException.class, () -> t.query2(-1, 1));
		}
	}

	@Nested
	class Bulk {

		@Test
		void fillAndSetAll() {
			final IntSquareSumSegmentTree t = new IntSquareSumSegmentTree(SAMPLE, 7);
			t.add(0, 4, 3);
			assertSame(t, t.fill(3));
			assertEquals(12 % 7, t.queryAll());
			assertEquals(36 % 7, t.query2All());
			assertSame(t, t.setAll(i -> i + 5));
			assertEquals("5 6 0 1", t.toString());
		}
	}

	@Nested
	class Conversion {

		@Test
		void toArrayIteratorAndToString() {
			final IntSquareSumSegmentTree t = new IntSquareSumSegmentTree(SAMPLE);
			t.add(0, 2, 1);
			assertArrayEquals(new int[]{2, 3, 3, 4}, t.toArray());
			final PrimitiveIterator.OfInt it = t.iterator();
			for (final int v : new int[]{2, 3, 3, 4}) assertEquals(v, it.nextInt());
			assertFalse(it.hasNext());
			assertThrows(NoSuchElementException.class, it::nextInt);
			assertEquals("", new IntSquareSumSegmentTree(0).toString());
		}
	}
}
