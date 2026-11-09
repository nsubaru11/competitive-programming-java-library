package unit.ds.fenwick;

import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import lib.ds.*;
import lib.ds.fenwick.*;

import static org.junit.jupiter.api.Assertions.*;

final class IntRangeBITTest {

	private static final int[] SAMPLE = {2, 0, 3, 1};

	@ParameterizedTest
	@CsvSource({
		"0, 0, 2",
		"0, 3, 6",
		"1, 2, 3",
		"3, 3, 1",
		"2, 1, 0",
	})
	void sumClosedRange(final int i, final int j, final long expected) {
		assertEquals(expected, new IntRangeBIT(SAMPLE).sum(i, j));
	}

	@Test
	void rangeAddUpdatesEveryElement() {
		final IntRangeBIT bit = new IntRangeBIT(SAMPLE);
		bit.add(1, 2, 5);
		assertArrayEquals(new int[]{2, 5, 8, 1}, bit.toArray());
		assertEquals(16L, bit.sumAll());
		assertEquals(15L, bit.sum(2));
		assertEquals(13L, bit.sum(1, 2));
	}

	@Test
	void emptyRangeAddDoesNothing() {
		final IntRangeBIT bit = new IntRangeBIT(SAMPLE);
		bit.add(2, 1, 100);
		assertArrayEquals(SAMPLE, bit.toArray());
		assertEquals(6L, bit.sumAll());
	}

	@Test
	void sumIsLong() {
		final IntRangeBIT bit = new IntRangeBIT(new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE});
		assertEquals(2L * Integer.MAX_VALUE, bit.sumAll());
		assertEquals(2L * Integer.MAX_VALUE, bit.sum(1));
		assertEquals(2L * Integer.MAX_VALUE, bit.sum(0, 1));
	}

	@Test
	void updatesReturnDocumentedValues() {
		final IntRangeBIT bit = new IntRangeBIT(SAMPLE);
		assertEquals(3, bit.set(2, 5));
		assertEquals(7, bit.add(2, 2));
		assertEquals(14, bit.multiply(2, 2));
		assertEquals(29, bit.affine(2, 2, 1));
		assertEquals(30, bit.apply(2, 30, Math::max));
		assertEquals(33L, bit.sumAll());
	}

	@Test
	void outOfRangeIndexThrows() {
		final IntRangeBIT bit = new IntRangeBIT(SAMPLE);
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
		final IntRangeBIT bit = new IntRangeBIT(SAMPLE);
		assertArrayEquals(new int[4], new IntRangeBIT(4).toArray());
		assertArrayEquals(SAMPLE, bit.toArray());
		assertArrayEquals(SAMPLE, new IntRangeBIT((IntCollection) bit).toArray());
	}

	@Test
	void copyIsIndependent() {
		final IntRangeBIT bit = new IntRangeBIT(SAMPLE);
		final IntRangeBIT copy = new IntRangeBIT(bit);
		copy.add(0, 3, 10);
		assertArrayEquals(SAMPLE, bit.toArray());
		assertEquals(6L, bit.sumAll());
		assertEquals(46L, copy.sumAll());
	}

	@Test
	void fillAndSetAllRebuild() {
		final IntRangeBIT bit = new IntRangeBIT(SAMPLE);
		assertSame(bit, bit.fill(3));
		assertArrayEquals(new int[]{3, 3, 3, 3}, bit.toArray());
		assertEquals(12L, bit.sumAll());
		assertSame(bit, bit.setAll(i -> i * i));
		assertArrayEquals(new int[]{0, 1, 4, 9}, bit.toArray());
		assertEquals(14L, bit.sumAll());
		assertEquals(14L, bit.sum(1, 3));
	}

	@Test
	void traversal() {
		final IntRangeBIT bit = new IntRangeBIT(SAMPLE);
		final PrimitiveIterator.OfInt it = bit.iterator();
		for (final int v : SAMPLE) assertEquals(v, it.nextInt());
		assertFalse(it.hasNext());
		assertThrows(NoSuchElementException.class, it::nextInt);
		assertEquals("2 0 3 1", bit.toString());
		assertEquals("", new IntRangeBIT(0).toString());
	}

	// 愚直解との照合: 配列に同じ操作を適用し、すべての取得結果を比べる
	@RepeatedTest(200)
	void matchesNaive(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final int n = 1 + random.nextInt(32);
		final int[] naive = new int[n];
		for (int i = 0; i < n; i++) naive[i] = random.nextInt(2001) - 1000;
		final IntRangeBIT bit = new IntRangeBIT(naive.clone());
		for (int op = 0; op < 500; op++) {
			final String at = "seed=" + seed + ", op=" + op;
			final int i = random.nextInt(n), j = random.nextInt(n);
			switch (random.nextInt(10)) {
				case 0 -> {
					final int v = random.nextInt(2001) - 1000;
					assertEquals(naive[i], bit.set(i, v), at);
					naive[i] = v;
				}
				case 1 -> {
					final int d = random.nextInt(2001) - 1000;
					naive[i] += d;
					assertEquals(naive[i], bit.add(i, d), at);
				}
				case 2 -> {
					// 区間加算で値が広がりすぎないよう、元の範囲へ戻す方向に加える
					final int d = random.nextInt(201) - 100 - Integer.signum(naive[i]) * 50;
					bit.add(i, j, d);
					for (int k = i; k <= j; k++) naive[k] += d;
				}
				case 3 -> {
					final int m = Math.abs(naive[i]) > 100_000 ? 0 : random.nextInt(5) - 2;
					naive[i] *= m;
					assertEquals(naive[i], bit.multiply(i, m), at);
				}
				case 4 -> {
					final int m = Math.abs(naive[i]) > 100_000 ? 0 : random.nextInt(5) - 2;
					final int d = random.nextInt(201) - 100;
					naive[i] = naive[i] * m + d;
					assertEquals(naive[i], bit.affine(i, m, d), at);
				}
				case 5 -> {
					final int x = random.nextInt(2001) - 1000;
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

	private static long sum(final int[] a, final int i, final int j) {
		long s = 0;
		for (int k = i; k <= j; k++) s += a[k];
		return s;
	}
}
