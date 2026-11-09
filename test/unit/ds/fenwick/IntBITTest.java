package unit.ds.fenwick;

import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import lib.ds.*;
import lib.ds.fenwick.*;

import static org.junit.jupiter.api.Assertions.*;

final class IntBITTest {

	private static final int[] SAMPLE = {2, 0, 3, 1};

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
		assertEquals(expected, new IntBIT(SAMPLE).lowerBound(w));
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
		assertEquals(expected, new IntBIT(SAMPLE).upperBound(w));
	}

	@Test
	void boundsSkipLeadingZeros() {
		final IntBIT bit = new IntBIT(new int[]{0, 0, 1});
		assertEquals(0, bit.lowerBound(0));
		assertEquals(2, bit.upperBound(0));
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
		assertEquals(expected, new IntBIT(SAMPLE).sum(i, j));
	}

	@Test
	void sumIsLong() {
		final IntBIT bit = new IntBIT(new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE});
		assertEquals(2L * Integer.MAX_VALUE, bit.sumAll());
		assertEquals(2L * Integer.MAX_VALUE, bit.sum(1));
	}

	@Test
	void updatesReturnDocumentedValues() {
		final IntBIT bit = new IntBIT(SAMPLE);
		assertEquals(3, bit.set(2, 5));
		assertEquals(7, bit.add(2, 2));
		assertEquals(14, bit.multiply(2, 2));
		assertEquals(29, bit.affine(2, 2, 1));
		assertEquals(30, bit.apply(2, 30, Math::max));
		assertEquals(33L, bit.sumAll());
	}

	@Test
	void outOfRangeIndexThrows() {
		final IntBIT bit = new IntBIT(SAMPLE);
		assertThrows(IndexOutOfBoundsException.class, () -> bit.get(4));
		assertThrows(IndexOutOfBoundsException.class, () -> bit.sum(-1));
		assertThrows(IndexOutOfBoundsException.class, () -> bit.sum(3, 4));
		// 始点が終点を超えても、端点が範囲外なら投げる
		assertThrows(IndexOutOfBoundsException.class, () -> bit.sum(4, 0));
	}

	@Test
	void constructorsKeepOrder() {
		final IntBIT bit = new IntBIT(SAMPLE);
		assertArrayEquals(new int[4], new IntBIT(4).toArray());
		assertArrayEquals(SAMPLE, bit.toArray());
		assertArrayEquals(SAMPLE, new IntBIT((IntCollection) bit).toArray());
	}

	@Test
	void copyIsIndependent() {
		final IntBIT bit = new IntBIT(SAMPLE);
		final IntBIT copy = new IntBIT(bit);
		copy.add(0, 10);
		assertEquals(2, bit.get(0));
		assertEquals(6L, bit.sumAll());
		assertEquals(16L, copy.sumAll());
	}

	@Test
	void toArrayIsIndependent() {
		final IntBIT bit = new IntBIT(SAMPLE);
		bit.toArray()[0] = 100;
		assertEquals(2, bit.get(0));
	}

	@Test
	void fillAndSetAllRebuild() {
		final IntBIT bit = new IntBIT(SAMPLE);
		assertSame(bit, bit.fill(3));
		assertArrayEquals(new int[]{3, 3, 3, 3}, bit.toArray());
		assertEquals(12L, bit.sumAll());
		assertSame(bit, bit.setAll(i -> i * i));
		assertArrayEquals(new int[]{0, 1, 4, 9}, bit.toArray());
		assertEquals(14L, bit.sumAll());
	}

	@Test
	void traversal() {
		final IntBIT bit = new IntBIT(SAMPLE);
		final List<Integer> visited = new ArrayList<>();
		bit.forEachInt(visited::add);
		assertEquals(List.of(2, 0, 3, 1), visited);
		final PrimitiveIterator.OfInt it = bit.iterator();
		for (final int v : SAMPLE) assertEquals(v, it.nextInt());
		assertFalse(it.hasNext());
		assertThrows(NoSuchElementException.class, it::nextInt);
		assertEquals("2 0 3 1", bit.toString());
		assertEquals("", new IntBIT(0).toString());
	}

	// 愚直解との照合: 配列に同じ操作を適用し、すべての取得結果を比べる
	@RepeatedTest(200)
	void matchesNaive(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final int n = 1 + random.nextInt(32);
		final int[] naive = new int[n];
		for (int i = 0; i < n; i++) naive[i] = random.nextInt(1000);
		final IntBIT bit = new IntBIT(naive.clone());
		for (int op = 0; op < 500; op++) {
			final String at = "seed=" + seed + ", op=" + op;
			final int i = random.nextInt(n);
			switch (random.nextInt(10)) {
				case 0 -> {
					final int v = random.nextInt(1000);
					assertEquals(naive[i], bit.set(i, v), at);
					naive[i] = v;
				}
				case 1 -> {
					// 要素を非負に保ち、境界探索の前提を満たす
					final int d = random.nextInt(1000) - naive[i];
					naive[i] += d;
					assertEquals(naive[i], bit.add(i, d), at);
				}
				case 2 -> {
					final int m = naive[i] > 100_000 ? 0 : random.nextInt(3);
					naive[i] *= m;
					assertEquals(naive[i], bit.multiply(i, m), at);
				}
				case 3 -> {
					final int m = naive[i] > 100_000 ? 0 : random.nextInt(3);
					final int d = random.nextInt(100);
					naive[i] = naive[i] * m + d;
					assertEquals(naive[i], bit.affine(i, m, d), at);
				}
				case 4 -> {
					final int x = random.nextInt(1000);
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

	private static long sum(final int[] a, final int i, final int j) {
		long s = 0;
		for (int k = i; k <= j; k++) s += a[k];
		return s;
	}

	private static int lowerBound(final int[] a, final long w) {
		long s = 0;
		for (int k = 0; k < a.length; k++) {
			s += a[k];
			if (s >= w) return k;
		}
		return a.length;
	}

	private static int upperBound(final int[] a, final long w) {
		long s = 0;
		for (int k = 0; k < a.length; k++) {
			s += a[k];
			if (s > w) return k;
		}
		return a.length;
	}
}
