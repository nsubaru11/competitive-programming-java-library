package unit.ds.fenwick;

import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import lib.ds.*;
import lib.ds.fenwick.*;

import static org.junit.jupiter.api.Assertions.*;

final class LongBITTest {

	private static final long[] SAMPLE = {2, 0, 3, 1};

	// 累積和は 2, 2, 5, 6
	@ParameterizedTest
	@CsvSource({
		"-1, 0",
		"0, 0",
		"1, 0",
		"2, 0",
		"3, 2",
		"6, 3",
		"7, 4",
	})
	void lowerBound(final long w, final int expected) {
		assertEquals(expected, new LongBIT(SAMPLE).lowerBound(w));
	}

	@ParameterizedTest
	@CsvSource({
		"-1, 0",
		"0, 0",
		"1, 0",
		"2, 2",
		"5, 3",
		"6, 4",
	})
	void upperBound(final long w, final int expected) {
		assertEquals(expected, new LongBIT(SAMPLE).upperBound(w));
	}

	@Test
	void boundsSkipLeadingZeros() {
		final LongBIT bit = new LongBIT(new long[]{0, 0, 1});
		assertEquals(0, bit.lowerBound(0L));
		assertEquals(2, bit.upperBound(0L));
	}

	@ParameterizedTest
	@CsvSource({
		"0, 0, 2",
		"0, 3, 6",
		"1, 2, 3",
		"3, 3, 1",
		"2, 1, 0",
	})
	void sumClosedRange(final int i, final int j, final long expected) {
		assertEquals(expected, new LongBIT(SAMPLE).sum(i, j));
	}

	@Test
	void holdsValuesBeyondIntRange() {
		final long big = 1L << 40;
		final LongBIT bit = new LongBIT(new long[]{big, big});
		assertEquals(big, bit.get(1));
		assertEquals(2 * big, bit.sumAll());
		assertEquals(1, bit.lowerBound(big + 1));
	}

	@Test
	void updatesReturnDocumentedValues() {
		final LongBIT bit = new LongBIT(SAMPLE);
		assertEquals(3L, bit.set(2, 5L));
		assertEquals(7L, bit.add(2, 2L));
		assertEquals(14L, bit.multiply(2, 2L));
		assertEquals(29L, bit.affine(2, 2L, 1L));
		assertEquals(30L, bit.apply(2, 30L, Math::max));
		assertEquals(33L, bit.sumAll());
	}

	@Test
	void outOfRangeIndexThrows() {
		final LongBIT bit = new LongBIT(SAMPLE);
		assertThrows(IndexOutOfBoundsException.class, () -> bit.get(4));
		assertThrows(IndexOutOfBoundsException.class, () -> bit.sum(-1));
		assertThrows(IndexOutOfBoundsException.class, () -> bit.sum(3, 4));
		// 始点が終点を超えても、端点が範囲外なら投げる
		assertThrows(IndexOutOfBoundsException.class, () -> bit.sum(4, 0));
	}

	@Test
	void constructorsKeepOrder() {
		final LongBIT bit = new LongBIT(SAMPLE);
		assertArrayEquals(new long[4], new LongBIT(4).toArray());
		assertArrayEquals(SAMPLE, bit.toArray());
		assertArrayEquals(SAMPLE, new LongBIT((LongCollection) bit).toArray());
	}

	@Test
	void copyIsIndependent() {
		final LongBIT bit = new LongBIT(SAMPLE);
		final LongBIT copy = new LongBIT(bit);
		copy.add(0, 10L);
		assertEquals(2L, bit.get(0));
		assertEquals(6L, bit.sumAll());
		assertEquals(16L, copy.sumAll());
	}

	@Test
	void toArrayIsIndependent() {
		final LongBIT bit = new LongBIT(SAMPLE);
		bit.toArray()[0] = 100;
		assertEquals(2L, bit.get(0));
	}

	@Test
	void fillAndSetAllRebuild() {
		final LongBIT bit = new LongBIT(SAMPLE);
		assertSame(bit, bit.fill(3L));
		assertArrayEquals(new long[]{3, 3, 3, 3}, bit.toArray());
		assertEquals(12L, bit.sumAll());
		assertSame(bit, bit.setAll(i -> (long) i * i));
		assertArrayEquals(new long[]{0, 1, 4, 9}, bit.toArray());
		assertEquals(14L, bit.sumAll());
	}

	@Test
	void traversal() {
		final LongBIT bit = new LongBIT(SAMPLE);
		final List<Long> visited = new ArrayList<>();
		bit.forEachLong(visited::add);
		assertEquals(List.of(2L, 0L, 3L, 1L), visited);
		final PrimitiveIterator.OfLong it = bit.iterator();
		for (final long v : SAMPLE) assertEquals(v, it.nextLong());
		assertFalse(it.hasNext());
		assertThrows(NoSuchElementException.class, it::nextLong);
		assertEquals("2 0 3 1", bit.toString());
		assertEquals("", new LongBIT(0).toString());
	}

	// 愚直解との照合: 配列に同じ操作を適用し、すべての取得結果を比べる
	@RepeatedTest(200)
	void matchesNaive(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final int n = 1 + random.nextInt(32);
		final long[] naive = new long[n];
		for (int i = 0; i < n; i++) naive[i] = random.nextLong(1L << 40);
		final LongBIT bit = new LongBIT(naive.clone());
		for (int op = 0; op < 500; op++) {
			final String at = "seed=" + seed + ", op=" + op;
			final int i = random.nextInt(n);
			switch (random.nextInt(10)) {
				case 0 -> {
					final long v = random.nextLong(1L << 40);
					assertEquals(naive[i], bit.set(i, v), at);
					naive[i] = v;
				}
				case 1 -> {
					// 要素を非負に保ち、境界探索の前提を満たす
					final long d = random.nextLong(1L << 40) - naive[i];
					naive[i] += d;
					assertEquals(naive[i], bit.add(i, d), at);
				}
				case 2 -> {
					final long m = naive[i] > 1L << 44 ? 0 : random.nextInt(3);
					naive[i] *= m;
					assertEquals(naive[i], bit.multiply(i, m), at);
				}
				case 3 -> {
					final long m = naive[i] > 1L << 44 ? 0 : random.nextInt(3);
					final long d = random.nextLong(1L << 20);
					naive[i] = naive[i] * m + d;
					assertEquals(naive[i], bit.affine(i, m, d), at);
				}
				case 4 -> {
					final long x = random.nextLong(1L << 40);
					naive[i] = Math.max(naive[i], x);
					assertEquals(naive[i], bit.apply(i, x, Math::max), at);
				}
				case 5 -> assertEquals(naive[i], bit.get(i), at);
				case 6 -> {
					final int j = random.nextInt(n);
					assertEquals(sum(naive, i, j), bit.sum(i, j), at);
					assertEquals(sum(naive, 0, i), bit.sum(i), at);
				}
				case 7 -> {
					final long w = random.nextLong(-1, sum(naive, 0, n - 1) + 2);
					assertEquals(lowerBound(naive, w), bit.lowerBound(w), at);
					assertEquals(upperBound(naive, w), bit.upperBound(w), at);
				}
				case 8 -> assertEquals(sum(naive, 0, n - 1), bit.sumAll(), at);
				default -> assertArrayEquals(naive, bit.toArray(), at);
			}
		}
	}

	private static long sum(final long[] a, final int i, final int j) {
		long s = 0;
		for (int k = i; k <= j; k++) s += a[k];
		return s;
	}

	private static int lowerBound(final long[] a, final long w) {
		long s = 0;
		for (int k = 0; k < a.length; k++) {
			s += a[k];
			if (s >= w) return k;
		}
		return a.length;
	}

	private static int upperBound(final long[] a, final long w) {
		long s = 0;
		for (int k = 0; k < a.length; k++) {
			s += a[k];
			if (s > w) return k;
		}
		return a.length;
	}
}
