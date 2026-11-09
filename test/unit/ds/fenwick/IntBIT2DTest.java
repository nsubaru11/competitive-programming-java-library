package unit.ds.fenwick;

import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import lib.ds.fenwick.*;

import static org.junit.jupiter.api.Assertions.*;

final class IntBIT2DTest {

	// 2行3列。行優先で 1 2 3 / 4 5 6
	private static final int[][] SAMPLE = {{1, 2, 3}, {4, 5, 6}};

	@ParameterizedTest
	@CsvSource({
		"0, 0, 1",
		"0, 2, 6",
		"1, 0, 5",
		"1, 2, 21",
	})
	void prefixSum(final int i, final int j, final long expected) {
		assertEquals(expected, new IntBIT2D(SAMPLE).sum(i, j));
	}

	@ParameterizedTest
	@CsvSource({
		"0, 0, 1, 2, 21",
		"1, 1, 1, 2, 11",
		"0, 1, 1, 1, 7",
		"1, 2, 1, 2, 6",
		// 始点が終点を超える軸があれば0
		"1, 0, 0, 2, 0",
		"0, 2, 1, 1, 0",
	})
	void sumClosedRectangle(final int i1, final int j1, final int i2, final int j2, final long expected) {
		assertEquals(expected, new IntBIT2D(SAMPLE).sum(i1, j1, i2, j2));
	}

	@Test
	void sumIsLong() {
		final IntBIT2D bit = new IntBIT2D(new int[][]{{Integer.MAX_VALUE}, {Integer.MAX_VALUE}});
		assertEquals(2L * Integer.MAX_VALUE, bit.sumAll());
		assertEquals(2L * Integer.MAX_VALUE, bit.sum(1, 0));
		assertEquals(2L * Integer.MAX_VALUE, bit.sum(0, 0, 1, 0));
	}

	@Test
	void updatesReturnDocumentedValues() {
		final IntBIT2D bit = new IntBIT2D(SAMPLE);
		assertEquals(5, bit.set(1, 1, 7));
		assertEquals(9, bit.add(1, 1, 2));
		assertEquals(18, bit.multiply(1, 1, 2));
		assertEquals(37, bit.affine(1, 1, 2, 1));
		assertEquals(40, bit.apply(1, 1, 40, Math::max));
		assertEquals(56L, bit.sumAll());
		assertEquals(46L, bit.sum(1, 1, 1, 2));
	}

	@Test
	void outOfRangeIndexThrows() {
		final IntBIT2D bit = new IntBIT2D(SAMPLE);
		assertThrows(IndexOutOfBoundsException.class, () -> bit.get(2, 0));
		assertThrows(IndexOutOfBoundsException.class, () -> bit.get(0, 3));
		assertThrows(IndexOutOfBoundsException.class, () -> bit.get(-1, 0));
		assertThrows(IndexOutOfBoundsException.class, () -> bit.add(0, -1, 1));
		assertThrows(IndexOutOfBoundsException.class, () -> bit.sum(2, 0));
		assertThrows(IndexOutOfBoundsException.class, () -> bit.sum(0, 0, 1, 3));
		// 始点が終点を超えても、座標が範囲外なら投げる
		assertThrows(IndexOutOfBoundsException.class, () -> bit.sum(2, 0, 0, 0));
	}

	@Test
	void constructorsKeepLayout() {
		final IntBIT2D bit = new IntBIT2D(SAMPLE);
		final IntBIT2D empty = new IntBIT2D(2, 3);
		assertEquals(2, empty.h);
		assertEquals(3, empty.w);
		assertEquals(6, empty.size());
		assertArrayEquals(new int[6], empty.toArray());
		assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6}, bit.toArray());
		assertArrayEquals(SAMPLE, bit.toArray2D());
	}

	@Test
	void copyIsIndependent() {
		final IntBIT2D bit = new IntBIT2D(SAMPLE);
		final IntBIT2D copy = new IntBIT2D(bit);
		copy.add(0, 0, 10);
		assertEquals(1, bit.get(0, 0));
		assertEquals(21L, bit.sumAll());
		assertEquals(31L, copy.sumAll());
		assertEquals(11L, copy.sum(0, 0));
	}

	@Test
	void arraysAreIndependent() {
		final IntBIT2D bit = new IntBIT2D(SAMPLE);
		bit.toArray()[0] = 100;
		bit.toArray2D()[0][0] = 100;
		assertEquals(1, bit.get(0, 0));
	}

	@Test
	void fillAndSetAllRebuild() {
		final IntBIT2D bit = new IntBIT2D(SAMPLE);
		assertSame(bit, bit.fill(3));
		assertArrayEquals(new int[]{3, 3, 3, 3, 3, 3}, bit.toArray());
		assertEquals(18L, bit.sumAll());
		assertEquals(12L, bit.sum(0, 1, 1, 2));
		assertSame(bit, bit.setAll((i, j) -> 10 * i + j));
		assertArrayEquals(new int[][]{{0, 1, 2}, {10, 11, 12}}, bit.toArray2D());
		assertEquals(36L, bit.sumAll());
		assertEquals(23L, bit.sum(1, 1, 1, 2));
	}

	@Test
	void traversalIsRowMajor() {
		final IntBIT2D bit = new IntBIT2D(SAMPLE);
		final List<Integer> visited = new ArrayList<>();
		bit.forEachInt(visited::add);
		assertEquals(List.of(1, 2, 3, 4, 5, 6), visited);
		final PrimitiveIterator.OfInt it = bit.iterator();
		for (int v = 1; v <= 6; v++) assertEquals(v, it.nextInt());
		assertFalse(it.hasNext());
		assertThrows(NoSuchElementException.class, it::nextInt);
		assertEquals("1 2 3\n4 5 6", bit.toString());
		assertEquals("", new IntBIT2D(0, 0).toString());
	}

	// 愚直解との照合: 2次元配列に同じ操作を適用し、すべての取得結果を比べる
	@RepeatedTest(200)
	void matchesNaive(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final int h = 1 + random.nextInt(8), w = 1 + random.nextInt(8);
		final int[][] naive = new int[h][w];
		for (final int[] row : naive) for (int j = 0; j < w; j++) row[j] = random.nextInt(2001) - 1000;
		final IntBIT2D bit = new IntBIT2D(copy(naive));
		for (int op = 0; op < 500; op++) {
			final String at = "seed=" + seed + ", op=" + op;
			final int i = random.nextInt(h), j = random.nextInt(w);
			switch (random.nextInt(10)) {
				case 0 -> {
					final int v = random.nextInt(2001) - 1000;
					assertEquals(naive[i][j], bit.set(i, j, v), at);
					naive[i][j] = v;
				}
				case 1 -> {
					final int d = random.nextInt(2001) - 1000 - naive[i][j] / 2;
					naive[i][j] += d;
					assertEquals(naive[i][j], bit.add(i, j, d), at);
				}
				case 2 -> {
					final int m = Math.abs(naive[i][j]) > 100_000 ? 0 : random.nextInt(5) - 2;
					naive[i][j] *= m;
					assertEquals(naive[i][j], bit.multiply(i, j, m), at);
				}
				case 3 -> {
					final int m = Math.abs(naive[i][j]) > 100_000 ? 0 : random.nextInt(5) - 2;
					final int d = random.nextInt(201) - 100;
					naive[i][j] = naive[i][j] * m + d;
					assertEquals(naive[i][j], bit.affine(i, j, m, d), at);
				}
				case 4 -> {
					final int x = random.nextInt(2001) - 1000;
					naive[i][j] = Math.max(naive[i][j], x);
					assertEquals(naive[i][j], bit.apply(i, j, x, Math::max), at);
				}
				case 5 -> assertEquals(naive[i][j], bit.get(i, j), at);
				case 6 -> assertEquals(sum(naive, 0, 0, i, j), bit.sum(i, j), at);
				case 7 -> {
					final int i2 = random.nextInt(h), j2 = random.nextInt(w);
					assertEquals(sum(naive, i, j, i2, j2), bit.sum(i, j, i2, j2), at);
				}
				case 8 -> assertEquals(sum(naive, 0, 0, h - 1, w - 1), bit.sumAll(), at);
				default -> assertArrayEquals(naive, bit.toArray2D(), at);
			}
		}
	}

	private static int[][] copy(final int[][] a) {
		final int[][] res = new int[a.length][];
		for (int i = 0; i < a.length; i++) res[i] = a[i].clone();
		return res;
	}

	private static long sum(final int[][] a, final int i1, final int j1, final int i2, final int j2) {
		long s = 0;
		for (int i = i1; i <= i2; i++) for (int j = j1; j <= j2; j++) s += a[i][j];
		return s;
	}
}
