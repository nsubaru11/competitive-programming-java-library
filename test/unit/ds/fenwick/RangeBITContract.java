package unit.ds.fenwick;

import java.util.*;
import java.util.function.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import unit.support.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * IntRangeBIT・LongRangeBIT に共通する振る舞いのテスト。値は long で扱い、各版のテストが Target で型の違いを吸収する。
 */
abstract class RangeBITContract {

	static final long[] SAMPLE = {2, 0, 3, 1};

	/**
	 * テスト対象の操作を long で受け渡すアダプター。
	 */
	interface Target {
		Object self();

		Target copy();

		Target fromCollection();

		int size();

		long get(int i);

		long set(int i, long v);

		long add(int i, long d);

		void add(int i, int j, long d);

		long multiply(int i, long m);

		long affine(int i, long m, long d);

		long apply(int i, long x, LongBinaryOperator f);

		Object fill(long v);

		Object setAll(IntToLongFunction init);

		long sum(int i);

		long sum(int i, int j);

		long sumAll();

		long[] toArray();

		Iterator<? extends Number> iterator();
	}

	abstract Target create(int n);

	abstract Target create(long[] a);

	/**
	 * ランダムな照合で使う値の上限。その型で和や積があふれない大きさにする。
	 */
	abstract long bound();

	@Nested
	class Construction {

		@Test
		void fromSizeIsZero() {
			assertArrayEquals(new long[4], create(4).toArray());
			assertEquals(4, create(4).size());
		}

		@Test
		void fromArrayAndCollectionKeepOrder() {
			final Target bit = create(SAMPLE);
			assertArrayEquals(SAMPLE, bit.toArray());
			assertArrayEquals(SAMPLE, bit.fromCollection().toArray());
			assertEquals(6L, bit.fromCollection().sumAll());
		}

		@Test
		void copyIsIndependent() {
			final Target bit = create(SAMPLE);
			final Target copy = bit.copy();
			copy.add(0, 3, 10);
			assertArrayEquals(SAMPLE, bit.toArray());
			assertEquals(6L, bit.sumAll());
			assertEquals(46L, copy.sumAll());
		}
	}

	@Nested
	class Access {

		@Test
		void get() {
			final Target bit = create(SAMPLE);
			for (int i = 0; i < SAMPLE.length; i++) assertEquals(SAMPLE[i], bit.get(i));
		}

		@Test
		void outOfRangeThrows() {
			final Target bit = create(SAMPLE);
			assertThrows(IndexOutOfBoundsException.class, () -> bit.get(4));
			assertThrows(IndexOutOfBoundsException.class, () -> bit.get(-1));
		}
	}

	@Nested
	class Updates {

		@Test
		void pointUpdatesReturnDocumentedValues() {
			final Target bit = create(SAMPLE);
			assertEquals(3L, bit.set(2, 5), "set は更新前の値");
			assertEquals(7L, bit.add(2, 2), "add は更新後の値");
			assertEquals(14L, bit.multiply(2, 2));
			assertEquals(29L, bit.affine(2, 2, 1), "affine は 現在値 * m + d");
			assertEquals(30L, bit.apply(2, 30, Math::max), "apply は f(現在値, x)");
			assertEquals(33L, bit.sumAll());
		}

		@Test
		void rangeAddUpdatesEveryElement() {
			final Target bit = create(SAMPLE);
			bit.add(1, 2, 5);
			assertArrayEquals(new long[]{2, 5, 8, 1}, bit.toArray());
			assertEquals(16L, bit.sumAll());
			assertEquals(15L, bit.sum(2));
			assertEquals(13L, bit.sum(1, 2));
		}

		@Test
		void emptyRangeAddDoesNothing() {
			final Target bit = create(SAMPLE);
			bit.add(2, 1, 100);
			assertArrayEquals(SAMPLE, bit.toArray());
			assertEquals(6L, bit.sumAll());
		}

		@Test
		void outOfRangeThrows() {
			final Target bit = create(SAMPLE);
			assertThrows(IndexOutOfBoundsException.class, () -> bit.add(0, 4, 1));
			// 始点が終点を超えても、端点が範囲外なら投げる
			assertThrows(IndexOutOfBoundsException.class, () -> bit.add(4, 0, 1));
		}
	}

	@Nested
	class Bulk {

		@Test
		void fillRebuilds() {
			final Target bit = create(SAMPLE);
			assertSame(bit.self(), bit.fill(3));
			assertArrayEquals(new long[]{3, 3, 3, 3}, bit.toArray());
			assertEquals(12L, bit.sumAll());
			assertEquals(6L, bit.sum(1, 2));
		}

		@Test
		void setAllRebuilds() {
			final Target bit = create(SAMPLE);
			assertSame(bit.self(), bit.setAll(i -> (long) i * i));
			assertArrayEquals(new long[]{0, 1, 4, 9}, bit.toArray());
			assertEquals(14L, bit.sumAll());
			assertEquals(14L, bit.sum(1, 3));
		}
	}

	@Nested
	class Aggregation {

		@ParameterizedTest
		@CsvSource({
			"0, 0, 2",
			"0, 3, 6",
			"1, 2, 3",
			"3, 3, 1",
			"2, 1, 0",
		})
		void sumClosedRange(final int i, final int j, final long expected) {
			assertEquals(expected, create(SAMPLE).sum(i, j));
		}

		@ParameterizedTest
		@CsvSource({
			"0, 2",
			"1, 2",
			"2, 5",
			"3, 6",
		})
		void sumPrefix(final int i, final long expected) {
			assertEquals(expected, create(SAMPLE).sum(i));
		}

		@Test
		void outOfRangeThrows() {
			final Target bit = create(SAMPLE);
			assertThrows(IndexOutOfBoundsException.class, () -> bit.sum(-1));
			assertThrows(IndexOutOfBoundsException.class, () -> bit.sum(4));
			assertThrows(IndexOutOfBoundsException.class, () -> bit.sum(3, 4));
			assertThrows(IndexOutOfBoundsException.class, () -> bit.sum(4, 0));
		}
	}

	@Nested
	class Conversion {

		@Test
		void iterator() {
			final Iterator<? extends Number> it = create(SAMPLE).iterator();
			for (final long v : SAMPLE) assertEquals(v, it.next().longValue());
			assertFalse(it.hasNext());
			assertThrows(NoSuchElementException.class, it::next);
		}

		@Test
		void toStringJoinsWithSpace() {
			assertEquals("2 0 3 1", create(SAMPLE).toString());
			assertEquals("", create(0).toString());
		}
	}

	@RepeatedTest(200)
	void matchesNaive(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final long b = bound();
		final int n = 1 + random.nextInt(32);
		final long[] naive = new long[n];
		for (int i = 0; i < n; i++) naive[i] = random.nextLong(-b, b + 1);
		final Target bit = create(naive.clone());
		new RandomOperations()
			.add("set", (r, at) -> {
				final int i = r.nextInt(n);
				final long v = r.nextLong(-b, b + 1);
				assertEquals(naive[i], bit.set(i, v), at);
				naive[i] = v;
			})
			.add("add", (r, at) -> {
				final int i = r.nextInt(n);
				final long d = r.nextLong(-b, b + 1) - naive[i];
				naive[i] += d;
				assertEquals(naive[i], bit.add(i, d), at);
			})
			.add("addRange", 2, (r, at) -> {
				// 値が広がりすぎないよう、0 へ戻す向きに寄せて加える
				final int i = r.nextInt(n), j = r.nextInt(n);
				final long d = r.nextLong(-b / 10, b / 10 + 1) - Long.signum(naive[i]) * (b / 20);
				bit.add(i, j, d);
				for (int k = i; k <= j; k++) naive[k] += d;
			})
			.add("multiply", (r, at) -> {
				final int i = r.nextInt(n);
				final long m = Math.abs(naive[i]) > b ? 0 : r.nextInt(5) - 2;
				naive[i] *= m;
				assertEquals(naive[i], bit.multiply(i, m), at);
			})
			.add("affine", (r, at) -> {
				final int i = r.nextInt(n);
				final long m = Math.abs(naive[i]) > b ? 0 : r.nextInt(5) - 2, d = r.nextLong(-b / 10, b / 10 + 1);
				naive[i] = naive[i] * m + d;
				assertEquals(naive[i], bit.affine(i, m, d), at);
			})
			.add("apply", (r, at) -> {
				final int i = r.nextInt(n);
				final long x = r.nextLong(-b, b + 1);
				naive[i] = Math.min(naive[i], x);
				assertEquals(naive[i], bit.apply(i, x, Math::min), at);
			})
			.add("get", (r, at) -> {
				final int i = r.nextInt(n);
				assertEquals(naive[i], bit.get(i), at);
			})
			.add("sum", 2, (r, at) -> {
				final int i = r.nextInt(n), j = r.nextInt(n);
				assertEquals(Naive.sum(naive, i, j), bit.sum(i, j), at);
				assertEquals(Naive.sum(naive, 0, i), bit.sum(i), at);
				assertEquals(Naive.sum(naive, 0, n - 1), bit.sumAll(), at);
			})
			.add("toArray", (r, at) -> assertArrayEquals(naive, bit.toArray(), at))
			.run(seed, random, 500);
	}
}
