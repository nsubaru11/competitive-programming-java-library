package unit.ds.fenwick;

import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import lib.ds.*;
import lib.ds.fenwick.*;

import static org.junit.jupiter.api.Assertions.*;

final class LongRangeBITTest {

	private static final long[] SAMPLE = {2, 0, 3, 1};

	@ParameterizedTest
	@CsvSource({
		"0, 0, 2",
		"0, 3, 6",
		"1, 2, 3",
		"3, 3, 1",
		"2, 1, 0",
	})
	void sumClosedRange(final int i, final int j, final long expected) {
		assertEquals(expected, new LongRangeBIT(SAMPLE).sum(i, j));
	}

	@Test
	void rangeAddUpdatesEveryElement() {
		final LongRangeBIT bit = new LongRangeBIT(SAMPLE);
		bit.add(1, 2, 5);
		assertArrayEquals(new long[]{2, 5, 8, 1}, bit.toArray());
		assertEquals(16L, bit.sumAll());
		assertEquals(15L, bit.sum(2));
		assertEquals(13L, bit.sum(1, 2));
	}

	@Test
	void emptyRangeAddDoesNothing() {
		final LongRangeBIT bit = new LongRangeBIT(SAMPLE);
		bit.add(2, 1, 100);
		assertArrayEquals(SAMPLE, bit.toArray());
		assertEquals(6L, bit.sumAll());
	}

	@Test
	void holdsValuesBeyondInt() {
		final long big = 1L << 40;
		final LongRangeBIT bit = new LongRangeBIT(new long[]{big, -big, big});
		bit.add(0, 2, big);
		assertArrayEquals(new long[]{2 * big, 0, 2 * big}, bit.toArray());
		assertEquals(4 * big, bit.sumAll());
		assertEquals(2 * big, bit.sum(1, 2));
	}

	@Test
	void updatesReturnDocumentedValues() {
		final LongRangeBIT bit = new LongRangeBIT(SAMPLE);
		assertEquals(3, bit.set(2, 5));
		assertEquals(7, bit.add(2, 2));
		assertEquals(14, bit.multiply(2, 2));
		assertEquals(29, bit.affine(2, 2, 1));
		assertEquals(30, bit.apply(2, 30, Math::max));
		assertEquals(33L, bit.sumAll());
	}

	@Test
	void outOfRangeIndexThrows() {
		final LongRangeBIT bit = new LongRangeBIT(SAMPLE);
		assertThrows(IndexOutOfBoundsException.class, () -> bit.get(4));
		assertThrows(IndexOutOfBoundsException.class, () -> bit.sum(-1));
		assertThrows(IndexOutOfBoundsException.class, () -> bit.sum(3, 4));
		assertThrows(IndexOutOfBoundsException.class, () -> bit.add(0, 4, 1));
		// 始点が終点を超えても、端点が範囲外なら投げる
		assertThrows(IndexOutOfBoundsException.class, () -> bit.sum(4, 0));
		assertThrows(IndexOutOfBoundsException.class, () -> bit.add(4, 0, 1));
	}

	@Test
	void constructorsKeepOrder() {
		final LongRangeBIT bit = new LongRangeBIT(SAMPLE);
		assertArrayEquals(new long[4], new LongRangeBIT(4).toArray());
		assertArrayEquals(SAMPLE, bit.toArray());
		assertArrayEquals(SAMPLE, new LongRangeBIT((LongCollection) bit).toArray());
	}

	@Test
	void copyIsIndependent() {
		final LongRangeBIT bit = new LongRangeBIT(SAMPLE);
		final LongRangeBIT copy = new LongRangeBIT(bit);
		copy.add(0, 3, 10);
		assertArrayEquals(SAMPLE, bit.toArray());
		assertEquals(6L, bit.sumAll());
		assertEquals(46L, copy.sumAll());
	}

	@Test
	void fillAndSetAllRebuild() {
		final LongRangeBIT bit = new LongRangeBIT(SAMPLE);
		assertSame(bit, bit.fill(3));
		assertArrayEquals(new long[]{3, 3, 3, 3}, bit.toArray());
		assertEquals(12L, bit.sumAll());
		assertSame(bit, bit.setAll(i -> i * i));
		assertArrayEquals(new long[]{0, 1, 4, 9}, bit.toArray());
		assertEquals(14L, bit.sumAll());
		assertEquals(14L, bit.sum(1, 3));
	}

	@Test
	void traversal() {
		final LongRangeBIT bit = new LongRangeBIT(SAMPLE);
		final PrimitiveIterator.OfLong it = bit.iterator();
		for (final long v : SAMPLE) assertEquals(v, it.nextLong());
		assertFalse(it.hasNext());
		assertThrows(NoSuchElementException.class, it::nextLong);
		assertEquals("2 0 3 1", bit.toString());
		assertEquals("", new LongRangeBIT(0).toString());
	}

	// 愚直解との照合: 配列に同じ操作を適用し、すべての取得結果を比べる
	@RepeatedTest(200)
	void matchesNaive(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final int n = 1 + random.nextInt(32);
		final long[] naive = new long[n];
		for (int i = 0; i < n; i++) naive[i] = random.nextLong(-1_000_000_000_000L, 1_000_000_000_001L);
		final LongRangeBIT bit = new LongRangeBIT(naive.clone());
		for (int op = 0; op < 500; op++) {
			final String at = "seed=" + seed + ", op=" + op;
			final int i = random.nextInt(n), j = random.nextInt(n);
			switch (random.nextInt(10)) {
				case 0 -> {
					final long v = random.nextLong(-1_000_000_000_000L, 1_000_000_000_001L);
					assertEquals(naive[i], bit.set(i, v), at);
					naive[i] = v;
				}
				case 1 -> {
					final long d = random.nextLong(-1_000_000_000_000L, 1_000_000_000_001L) - naive[i] / 2;
					naive[i] += d;
					assertEquals(naive[i], bit.add(i, d), at);
				}
				case 2 -> {
					// 区間加算で値が広がりすぎないよう、元の範囲へ戻す方向に加える
					final long d = random.nextLong(-1_000_000_000L, 1_000_000_001L) - Long.signum(naive[i]) * 500_000_000L;
					bit.add(i, j, d);
					for (int k = i; k <= j; k++) naive[k] += d;
				}
				case 3 -> {
					final long m = Math.abs(naive[i]) > 100_000 ? 0 : random.nextInt(5) - 2;
					naive[i] *= m;
					assertEquals(naive[i], bit.multiply(i, m), at);
				}
				case 4 -> {
					final long m = Math.abs(naive[i]) > 100_000 ? 0 : random.nextInt(5) - 2;
					final long d = random.nextInt(201) - 100;
					naive[i] = naive[i] * m + d;
					assertEquals(naive[i], bit.affine(i, m, d), at);
				}
				case 5 -> {
					final long x = random.nextLong(-1_000_000_000_000L, 1_000_000_000_001L);
					naive[i] = Math.min(naive[i], x);
					assertEquals(naive[i], bit.apply(i, x, Math::min), at);
				}
				case 6 -> assertEquals(naive[i], bit.get(i), at);
				case 7 -> {
					assertEquals(sum(naive, i, j), bit.sum(i, j), at);
					assertEquals(sum(naive, 0, i), bit.sum(i), at);
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
}
