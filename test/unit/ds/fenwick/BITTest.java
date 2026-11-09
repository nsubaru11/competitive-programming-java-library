package unit.ds.fenwick;

import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import lib.ds.fenwick.*;

import unit.support.*;

import static org.junit.jupiter.api.Assertions.*;

final class BITTest {

	private static final List<Long> SAMPLE = List.of(2L, 0L, 3L, 1L);

	private static BIT<Long> sample() {
		return new BIT<>(SAMPLE, Long::sum, (a, b) -> a - b, 0L);
	}

	private static BIT<Long> ofSize(final int n) {
		return new BIT<>(n, Long::sum, (a, b) -> a - b, 0L);
	}

	@Nested
	class Construction {

		@Test
		void fromSizeIsIdentity() {
			assertEquals("0 0 0", ofSize(3).toString());
		}

		@Test
		void fromArrayAndCollectionKeepOrder() {
			final BIT<Long> fromArray = new BIT<>(SAMPLE.toArray(new Long[0]), Long::sum, (a, b) -> a - b, 0L);
			assertEquals("2 0 3 1", fromArray.toString());
			assertEquals(6L, fromArray.queryAll());
			assertEquals("2 0 3 1", sample().toString());
		}

		@Test
		void copyIsIndependent() {
			final BIT<Long> bit = sample();
			final BIT<Long> copy = new BIT<>(bit);
			copy.add(0, 10L);
			assertEquals(2L, bit.get(0));
			assertEquals(6L, bit.queryAll());
			assertEquals(16L, copy.queryAll());
		}
	}

	@Nested
	class State {

		@Test
		void sizeAndIdentity() {
			final BIT<Long> bit = sample();
			assertEquals(4, bit.size);
			assertEquals(4, bit.size());
			assertEquals(0L, bit.identity);
			assertFalse(bit.isEmpty());
			assertTrue(ofSize(0).isEmpty());
		}
	}

	@Nested
	class Access {

		@Test
		void get() {
			final BIT<Long> bit = sample();
			for (int i = 0; i < SAMPLE.size(); i++) assertEquals(SAMPLE.get(i), bit.get(i));
			assertThrows(IndexOutOfBoundsException.class, () -> bit.get(4));
		}
	}

	@Nested
	class Updates {

		@Test
		void returnDocumentedValues() {
			final BIT<Long> bit = sample();
			assertEquals(3L, bit.set(2, 5L), "set は更新前の値");
			assertEquals(7L, bit.add(2, 2L), "add は operator で合成した更新後の値");
			assertEquals(10L, bit.apply(2, 10L, Math::max), "apply は f(現在値, x)");
			assertEquals(13L, bit.queryAll());
		}
	}

	@Nested
	class Bulk {

		@Test
		void fillAndSetAllRebuild() {
			final BIT<Long> bit = sample();
			assertSame(bit, bit.fill(3L));
			assertEquals(12L, bit.queryAll());
			assertSame(bit, bit.setAll(i -> (long) i * i));
			assertEquals("0 1 4 9", bit.toString());
			assertEquals(14L, bit.queryAll());
			assertEquals(13L, bit.query(2, 4));
		}
	}

	@Nested
	class Aggregation {

		// 半開区間 [l, r)。l >= r は単位元
		@ParameterizedTest
		@CsvSource({
			"0, 0, 0",
			"0, 1, 2",
			"0, 4, 6",
			"1, 3, 3",
			"3, 4, 1",
			"3, 1, 0",
		})
		void queryHalfOpen(final int l, final int r, final long expected) {
			assertEquals(expected, sample().query(l, r));
		}

		@ParameterizedTest
		@CsvSource({
			"0, 0",
			"1, 2",
			"3, 5",
			"4, 6",
		})
		void queryPrefix(final int r, final long expected) {
			assertEquals(expected, sample().query(r));
		}

		@Test
		void outOfRangeThrows() {
			final BIT<Long> bit = sample();
			assertThrows(IndexOutOfBoundsException.class, () -> bit.query(5));
			assertThrows(IndexOutOfBoundsException.class, () -> bit.query(-1, 2));
			assertThrows(IndexOutOfBoundsException.class, () -> bit.query(2, 5));
		}

		// 和以外の可換な演算でも動くことを確かめる
		@Test
		void xorOperator() {
			final BIT<Integer> bit = new BIT<>(List.of(5, 3, 6), (a, b) -> a ^ b, (a, b) -> a ^ b, 0);
			assertEquals(5 ^ 3 ^ 6, bit.queryAll());
			assertEquals(3 ^ 6, bit.query(1, 3));
			assertEquals(3, bit.set(1, 7));
			assertEquals(5 ^ 7, bit.query(2));
		}
	}

	@Nested
	class Conversion {

		@Test
		void forEachAndIterator() {
			final BIT<Long> bit = sample();
			final List<Long> visited = new ArrayList<>();
			bit.forEach(visited::add);
			assertEquals(SAMPLE, visited);
			final Iterator<Long> it = bit.iterator();
			for (final long v : SAMPLE) assertEquals(v, it.next());
			assertFalse(it.hasNext());
			assertThrows(NoSuchElementException.class, it::next);
		}

		@Test
		void toStringJoinsWithSpace() {
			assertEquals("2 0 3 1", sample().toString());
			assertEquals("", ofSize(0).toString());
		}
	}

	@RepeatedTest(200)
	void matchesNaive(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final int n = 1 + random.nextInt(32);
		final long[] naive = new long[n];
		final List<Long> init = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			naive[i] = random.nextLong(-1000, 1000);
			init.add(naive[i]);
		}
		final BIT<Long> bit = new BIT<>(init, Long::sum, (a, b) -> a - b, 0L);
		new RandomOperations()
			.add("set", (r, at) -> {
				final int i = r.nextInt(n);
				final long v = r.nextLong(-1000, 1000);
				assertEquals(naive[i], bit.set(i, v), at);
				naive[i] = v;
			})
			.add("add", (r, at) -> {
				final int i = r.nextInt(n);
				final long d = r.nextLong(-1000, 1000);
				naive[i] += d;
				assertEquals(naive[i], bit.add(i, d), at);
			})
			.add("apply", (r, at) -> {
				final int i = r.nextInt(n);
				final long x = r.nextLong(-1000, 1000);
				naive[i] = Math.min(naive[i], x);
				assertEquals(naive[i], bit.apply(i, x, Math::min), at);
			})
			.add("get", (r, at) -> {
				final int i = r.nextInt(n);
				assertEquals(naive[i], bit.get(i), at);
			})
			.add("query", 2, (r, at) -> {
				// 半開区間 [l, r) を閉区間 [l, r - 1] の愚直解と比べる
				final int l = r.nextInt(n + 1), rr = r.nextInt(n + 1);
				assertEquals(Naive.sum(naive, l, rr - 1), bit.query(l, rr), at);
				assertEquals(Naive.sum(naive, 0, rr - 1), bit.query(rr), at);
				assertEquals(Naive.sum(naive, 0, n - 1), bit.queryAll(), at);
			})
			.add("iterator", (r, at) -> {
				int k = 0;
				for (final long v : bit) assertEquals(naive[k++], v, at);
				assertEquals(n, k, at);
			})
			.run(seed, random, 500);
	}
}
