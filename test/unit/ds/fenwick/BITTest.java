package unit.ds.fenwick;

import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import lib.ds.fenwick.*;

import static org.junit.jupiter.api.Assertions.*;

final class BITTest {

	private static final List<Long> SAMPLE = List.of(2L, 0L, 3L, 1L);

	private static BIT<Long> sample() {
		return new BIT<>(SAMPLE, Long::sum, (a, b) -> a - b, 0L);
	}

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
	void fieldsAndState() {
		final BIT<Long> bit = sample();
		assertEquals(4, bit.size);
		assertEquals(4, bit.size());
		assertEquals(0L, bit.identity);
		assertFalse(bit.isEmpty());
		assertTrue(new BIT<>(0, Long::sum, (a, b) -> a - b, 0L).isEmpty());
	}

	@Test
	void updatesReturnDocumentedValues() {
		final BIT<Long> bit = sample();
		assertEquals(3L, bit.set(2, 5L));
		assertEquals(7L, bit.add(2, 2L));
		assertEquals(10L, bit.apply(2, 10L, Math::max));
		assertEquals(13L, bit.queryAll());
	}

	@Test
	void outOfRangeIndexThrows() {
		final BIT<Long> bit = sample();
		assertThrows(IndexOutOfBoundsException.class, () -> bit.get(4));
		assertThrows(IndexOutOfBoundsException.class, () -> bit.query(5));
		assertThrows(IndexOutOfBoundsException.class, () -> bit.query(-1, 2));
		assertThrows(IndexOutOfBoundsException.class, () -> bit.query(2, 5));
	}

	@Test
	void constructorsKeepOrder() {
		final BIT<Long> fromSize = new BIT<>(3, Long::sum, (a, b) -> a - b, 0L);
		assertEquals("0 0 0", fromSize.toString());
		final BIT<Long> fromArray = new BIT<>(SAMPLE.toArray(new Long[0]), Long::sum, (a, b) -> a - b, 0L);
		assertEquals("2 0 3 1", fromArray.toString());
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

	@Test
	void fillAndSetAllRebuild() {
		final BIT<Long> bit = sample();
		assertSame(bit, bit.fill(3L));
		assertEquals(12L, bit.queryAll());
		assertSame(bit, bit.setAll(i -> (long) i * i));
		assertEquals("0 1 4 9", bit.toString());
		assertEquals(14L, bit.queryAll());
	}

	@Test
	void traversal() {
		final BIT<Long> bit = sample();
		final List<Long> visited = new ArrayList<>();
		bit.forEach(visited::add);
		assertEquals(SAMPLE, visited);
		final Iterator<Long> it = bit.iterator();
		for (final long v : SAMPLE) assertEquals(v, it.next());
		assertFalse(it.hasNext());
		assertThrows(NoSuchElementException.class, it::next);
		assertEquals("", new BIT<>(0, Long::sum, (a, b) -> a - b, 0L).toString());
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

	// 愚直解との照合: 配列に同じ操作を適用し、すべての取得結果を比べる
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
		for (int op = 0; op < 500; op++) {
			final String at = "seed=" + seed + ", op=" + op;
			final int i = random.nextInt(n);
			switch (random.nextInt(7)) {
				case 0 -> {
					final long v = random.nextLong(-1000, 1000);
					assertEquals(naive[i], bit.set(i, v), at);
					naive[i] = v;
				}
				case 1 -> {
					final long d = random.nextLong(-1000, 1000);
					naive[i] += d;
					assertEquals(naive[i], bit.add(i, d), at);
				}
				case 2 -> {
					final long x = random.nextLong(-1000, 1000);
					naive[i] = Math.min(naive[i], x);
					assertEquals(naive[i], bit.apply(i, x, Math::min), at);
				}
				case 3 -> assertEquals(naive[i], bit.get(i), at);
				case 4 -> {
					final int l = random.nextInt(n + 1);
					final int r = random.nextInt(n + 1);
					assertEquals(sum(naive, l, r), bit.query(l, r), at);
					assertEquals(sum(naive, 0, r), bit.query(r), at);
				}
				case 5 -> assertEquals(sum(naive, 0, n), bit.queryAll(), at);
				default -> {
					int k = 0;
					for (final long v : bit) assertEquals(naive[k++], v, at);
					assertEquals(n, k, at);
				}
			}
		}
	}

	private static long sum(final long[] a, final int l, final int r) {
		long s = 0;
		for (int k = l; k < r; k++) s += a[k];
		return s;
	}
}
