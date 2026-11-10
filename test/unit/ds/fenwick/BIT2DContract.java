package unit.ds.fenwick;

import java.util.*;
import java.util.function.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import unit.support.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * IntBIT2D・LongBIT2D に共通する振る舞いのテスト。値は long で扱い、各版のテストが Target で型の違いを吸収する。
 */
abstract class BIT2DContract {

	// 2行3列。行優先で 1 2 3 / 4 5 6
	static final long[][] SAMPLE = {{1, 2, 3}, {4, 5, 6}};

	abstract Target create(int h, int w);

	abstract Target create(long[][] a);

	/**
	 * ランダムな照合で使う値の上限。その型で和や積があふれない大きさにする。
	 */
	abstract long bound();

	@RepeatedTest(200)
	void matchesNaive(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final long b = bound();
		final int h = 1 + random.nextInt(8), w = 1 + random.nextInt(8);
		final long[][] naive = new long[h][w];
		for (final long[] row : naive) for (int j = 0; j < w; j++) row[j] = random.nextLong(-b, b + 1);
		final long[][] init = new long[h][];
		for (int i = 0; i < h; i++) init[i] = naive[i].clone();
		final Target bit = create(init);
		new RandomOperations()
				.add("set", (r, at) -> {
					final int i = r.nextInt(h), j = r.nextInt(w);
					final long v = r.nextLong(-b, b + 1);
					assertEquals(naive[i][j], bit.set(i, j, v), at);
					naive[i][j] = v;
				})
				.add("add", (r, at) -> {
					final int i = r.nextInt(h), j = r.nextInt(w);
					final long d = r.nextLong(-b, b + 1) - naive[i][j];
					naive[i][j] += d;
					assertEquals(naive[i][j], bit.add(i, j, d), at);
				})
				.add("multiply", (r, at) -> {
					final int i = r.nextInt(h), j = r.nextInt(w);
					final long m = Math.abs(naive[i][j]) > b ? 0 : r.nextInt(5) - 2;
					naive[i][j] *= m;
					assertEquals(naive[i][j], bit.multiply(i, j, m), at);
				})
				.add("affine", (r, at) -> {
					final int i = r.nextInt(h), j = r.nextInt(w);
					final long m = Math.abs(naive[i][j]) > b ? 0 : r.nextInt(5) - 2, d = r.nextLong(-b / 10, b / 10 + 1);
					naive[i][j] = naive[i][j] * m + d;
					assertEquals(naive[i][j], bit.affine(i, j, m, d), at);
				})
				.add("apply", (r, at) -> {
					final int i = r.nextInt(h), j = r.nextInt(w);
					final long x = r.nextLong(-b, b + 1);
					naive[i][j] = Math.max(naive[i][j], x);
					assertEquals(naive[i][j], bit.apply(i, j, x, Math::max), at);
				})
				.add("get", (r, at) -> {
					final int i = r.nextInt(h), j = r.nextInt(w);
					assertEquals(naive[i][j], bit.get(i, j), at);
				})
				.add("sum", 2, (r, at) -> {
					final int i1 = r.nextInt(h), j1 = r.nextInt(w), i2 = r.nextInt(h), j2 = r.nextInt(w);
					assertEquals(Naive.sum(naive, i1, j1, i2, j2), bit.sum(i1, j1, i2, j2), at);
					assertEquals(Naive.sum(naive, 0, 0, i1, j1), bit.sum(i1, j1), at);
					assertEquals(Naive.sum(naive, 0, 0, h - 1, w - 1), bit.sumAll(), at);
				})
				.add("toArray2D", (r, at) -> assertArrayEquals(naive, bit.toArray2D(), at))
				.run(seed, random, 500);
	}

	/**
	 * テスト対象の操作を long で受け渡すアダプター。
	 */
	interface Target {
		Object self();

		Target copy();

		int h();

		int w();

		int size();

		long get(int i, int j);

		long set(int i, int j, long v);

		long add(int i, int j, long d);

		long multiply(int i, int j, long m);

		long affine(int i, int j, long m, long d);

		long apply(int i, int j, long x, LongBinaryOperator f);

		Object fill(long v);

		Object setAll(IntBinaryOperator init);

		long sum(int i, int j);

		long sum(int i1, int j1, int i2, int j2);

		long sumAll();

		long[] toArray();

		long[][] toArray2D();

		List<Long> forEachValue();

		Iterator<? extends Number> iterator();
	}

	@Nested
	class Construction {

		@Test
		void fromSizeIsZero() {
			final Target bit = create(2, 3);
			assertEquals(2, bit.h());
			assertEquals(3, bit.w());
			assertEquals(6, bit.size());
			assertArrayEquals(new long[6], bit.toArray());
		}

		@Test
		void fromArrayKeepsLayout() {
			final Target bit = create(SAMPLE);
			assertArrayEquals(new long[]{1, 2, 3, 4, 5, 6}, bit.toArray());
			assertArrayEquals(SAMPLE, bit.toArray2D());
		}

		@Test
		void copyIsIndependent() {
			final Target bit = create(SAMPLE);
			final Target copy = bit.copy();
			copy.add(0, 0, 10);
			assertEquals(1L, bit.get(0, 0));
			assertEquals(21L, bit.sumAll());
			assertEquals(31L, copy.sumAll());
			assertEquals(11L, copy.sum(0, 0));
		}
	}

	@Nested
	class Access {

		@Test
		void get() {
			final Target bit = create(SAMPLE);
			for (int i = 0; i < 2; i++) for (int j = 0; j < 3; j++) assertEquals(SAMPLE[i][j], bit.get(i, j));
		}

		@Test
		void outOfRangeThrows() {
			final Target bit = create(SAMPLE);
			assertThrows(IndexOutOfBoundsException.class, () -> bit.get(2, 0));
			assertThrows(IndexOutOfBoundsException.class, () -> bit.get(0, 3));
			assertThrows(IndexOutOfBoundsException.class, () -> bit.get(-1, 0));
			assertThrows(IndexOutOfBoundsException.class, () -> bit.add(0, -1, 1));
		}
	}

	@Nested
	class Updates {

		@Test
		void returnDocumentedValues() {
			final Target bit = create(SAMPLE);
			assertEquals(5L, bit.set(1, 1, 7), "set は更新前の値");
			assertEquals(9L, bit.add(1, 1, 2), "add は更新後の値");
			assertEquals(18L, bit.multiply(1, 1, 2));
			assertEquals(37L, bit.affine(1, 1, 2, 1), "affine は 現在値 * m + d");
			assertEquals(40L, bit.apply(1, 1, 40, Math::max), "apply は f(現在値, x)");
			assertEquals(56L, bit.sumAll());
			assertEquals(46L, bit.sum(1, 1, 1, 2));
		}
	}

	@Nested
	class Bulk {

		@Test
		void fillRebuilds() {
			final Target bit = create(SAMPLE);
			assertSame(bit.self(), bit.fill(3));
			assertArrayEquals(new long[]{3, 3, 3, 3, 3, 3}, bit.toArray());
			assertEquals(18L, bit.sumAll());
			assertEquals(12L, bit.sum(0, 1, 1, 2));
		}

		@Test
		void setAllRebuilds() {
			final Target bit = create(SAMPLE);
			assertSame(bit.self(), bit.setAll((i, j) -> 10 * i + j));
			assertArrayEquals(new long[][]{{0, 1, 2}, {10, 11, 12}}, bit.toArray2D());
			assertEquals(36L, bit.sumAll());
			assertEquals(23L, bit.sum(1, 1, 1, 2));
		}
	}

	@Nested
	class Aggregation {

		@ParameterizedTest
		@CsvSource({
				"0, 0, 1",
				"0, 2, 6",
				"1, 0, 5",
				"1, 2, 21",
		})
		void sumPrefix(final int i, final int j, final long expected) {
			assertEquals(expected, create(SAMPLE).sum(i, j));
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
			assertEquals(expected, create(SAMPLE).sum(i1, j1, i2, j2));
		}

		@Test
		void outOfRangeThrows() {
			final Target bit = create(SAMPLE);
			assertThrows(IndexOutOfBoundsException.class, () -> bit.sum(2, 0));
			assertThrows(IndexOutOfBoundsException.class, () -> bit.sum(0, 0, 1, 3));
			// 始点が終点を超えても、座標が範囲外なら投げる
			assertThrows(IndexOutOfBoundsException.class, () -> bit.sum(2, 0, 0, 0));
		}
	}

	@Nested
	class Conversion {

		@Test
		void arraysAreIndependent() {
			final Target bit = create(SAMPLE);
			bit.toArray()[0] = 100;
			bit.toArray2D()[0][0] = 100;
			assertEquals(1L, bit.get(0, 0));
		}

		@Test
		void traversalIsRowMajor() {
			final Target bit = create(SAMPLE);
			assertEquals(List.of(1L, 2L, 3L, 4L, 5L, 6L), bit.forEachValue());
			final Iterator<? extends Number> it = bit.iterator();
			for (long v = 1; v <= 6; v++) assertEquals(v, it.next().longValue());
			assertFalse(it.hasNext());
			assertThrows(NoSuchElementException.class, it::next);
		}

		@Test
		void toStringIsRowsOfSpaceSeparatedValues() {
			assertEquals("1 2 3\n4 5 6", create(SAMPLE).toString());
			assertEquals("", create(0, 0).toString());
		}
	}
}
