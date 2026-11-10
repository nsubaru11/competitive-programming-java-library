package unit.ds.segmenttree;

import java.util.*;
import java.util.function.*;

import org.junit.jupiter.api.*;

import unit.support.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SegmentTree・IntSegmentTree・LongSegmentTree に共通する振る舞いのテスト。値は long で扱う。
 * 四則演算の更新は NumericSegmentTreeContract で検査する。
 */
abstract class SegmentTreeContract {

	static final long[] SAMPLE = {5, 2, 7, 1, 3};

	abstract Target create(int n, Op op);

	abstract Target create(long[] a, Op op);

	/**
	 * IntCollection・Collection などを受け取るコンストラクタで作る。
	 */
	abstract Target createFromCollection(long[] a, Op op);

	/**
	 * Op.MIN の単位元。
	 */
	abstract long inf();

	@RepeatedTest(100)
	void matchesNaive(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final int n = 1 + random.nextInt(20);
		final Op op = random.nextBoolean() ? Op.SUM : Op.MIN;
		final long[] naive = random.longs(n, 0, 100).toArray();
		final Target t = create(naive.clone(), op);
		new RandomOperations()
				.add("set", 2, (r, at) -> {
					final int i = r.nextInt(n);
					final long v = r.nextInt(100);
					assertEquals(naive[i], t.set(i, v), at);
					naive[i] = v;
				})
				.add("apply", (r, at) -> {
					final int i = r.nextInt(n);
					final long x = r.nextInt(100);
					naive[i] = Math.min(naive[i], x);
					assertEquals(naive[i], t.apply(i, x, Math::min), at);
				})
				.add("query", 3, (r, at) -> {
					final int l = r.nextInt(n + 1), rr = r.nextInt(n + 1);
					assertEquals(fold(naive, l, rr, op), t.query(l, rr), at);
					assertEquals(fold(naive, 0, n, op), t.queryAll(), at);
				})
				.add("maxRight", 2, (r, at) -> {
					final int l = r.nextInt(n + 1);
					final long w = r.nextInt(300);
					final LongPredicate tester = op == Op.SUM ? s -> s <= w : s -> s >= w % 100;
					int expected = l;
					while (expected < n && tester.test(fold(naive, l, expected + 1, op))) expected++;
					assertEquals(expected, t.maxRight(l, tester), at);
				})
				.add("minLeft", 2, (r, at) -> {
					final int rr = r.nextInt(n + 1);
					final long w = r.nextInt(300);
					final LongPredicate tester = op == Op.SUM ? s -> s <= w : s -> s >= w % 100;
					int expected = rr;
					while (expected > 0 && tester.test(fold(naive, expected - 1, rr, op))) expected--;
					assertEquals(expected, t.minLeft(rr, tester), at);
				})
				.add("get", (r, at) -> {
					final int i = r.nextInt(n);
					assertEquals(naive[i], t.get(i), at);
				})
				.run(seed, random, 300);
	}

	long fold(final long[] a, final int l, final int r, final Op op) {
		long res = op == Op.SUM ? 0 : inf();
		for (int i = l; i < r; i++) res = op == Op.SUM ? res + a[i] : Math.min(res, a[i]);
		return res;
	}

	/**
	 * テストで使う演算。SUM は和（単位元0）、MIN は最小値（単位元はその型の最大値）。
	 */
	enum Op {
		SUM, MIN
	}

	interface Target {
		Target copy();

		int size();

		long identity();

		long get(int i);

		long set(int i, long v);

		long apply(int i, long x, LongBinaryOperator f);

		Object fill(long v);

		Object setAll(IntToLongFunction init);

		long query(int l, int r);

		long queryAll();

		int maxRight(int l, LongPredicate tester);

		int minLeft(int r, LongPredicate tester);

		Iterator<?> iterator();
	}

	@Nested
	class Construction {

		@Test
		void fromSizeIsIdentity() {
			final Target sum = create(3, Op.SUM), min = create(3, Op.MIN);
			assertEquals(0L, sum.identity());
			assertEquals(inf(), min.identity());
			for (int i = 0; i < 3; i++) {
				assertEquals(0L, sum.get(i));
				assertEquals(inf(), min.get(i));
			}
			assertEquals(inf(), min.queryAll());
		}

		@Test
		void collectionAndCopy() {
			final Target t = create(SAMPLE, Op.SUM);
			assertEquals(18L, createFromCollection(SAMPLE, Op.SUM).queryAll());
			assertEquals(1L, createFromCollection(SAMPLE, Op.MIN).queryAll());
			final Target c = t.copy();
			c.set(0, 100);
			assertEquals(18L, t.queryAll());
			assertEquals(113L, c.queryAll());
		}
	}

	@Nested
	class Access {

		@Test
		void getAndOutOfRange() {
			final Target t = create(SAMPLE, Op.SUM);
			for (int i = 0; i < SAMPLE.length; i++) assertEquals(SAMPLE[i], t.get(i));
			assertThrows(IndexOutOfBoundsException.class, () -> t.get(5));
			assertThrows(IndexOutOfBoundsException.class, () -> t.set(-1, 0));
		}
	}

	@Nested
	class Updates {

		@Test
		void setAndApply() {
			final Target t = create(SAMPLE, Op.MIN);
			assertEquals(7L, t.set(2, 0), "set は更新前の値");
			assertEquals(0L, t.queryAll());
			assertEquals(4L, t.apply(2, 4, Math::max), "apply は f(現在値, x)");
			assertEquals(1L, t.queryAll());
		}
	}

	@Nested
	class Bulk {

		@Test
		void fillAndSetAllRebuild() {
			final Target t = create(SAMPLE, Op.SUM);
			t.fill(2);
			assertEquals(10L, t.queryAll());
			assertEquals(4L, t.query(1, 3));
			t.setAll(i -> i * 10L);
			assertEquals(100L, t.queryAll());
			assertEquals(30L, t.query(0, 3));
		}
	}

	@Nested
	class Aggregation {

		@Test
		void queryHalfOpen() {
			final Target sum = create(SAMPLE, Op.SUM), min = create(SAMPLE, Op.MIN);
			assertEquals(9L, sum.query(1, 3));
			assertEquals(2L, min.query(0, 2));
			assertEquals(1L, min.query(1, 5));
			assertEquals(0L, sum.query(3, 3), "空区間は単位元");
			assertEquals(inf(), min.query(4, 2));
			assertEquals(18L, sum.queryAll());
		}

		@Test
		void outOfRangeThrows() {
			final Target t = create(SAMPLE, Op.SUM);
			assertThrows(IndexOutOfBoundsException.class, () -> t.query(0, 6));
			assertThrows(IndexOutOfBoundsException.class, () -> t.query(-1, 2));
		}
	}

	@Nested
	class Search {

		// 累積和は 5 7 14 15 18
		@Test
		void maxRight() {
			final Target t = create(SAMPLE, Op.SUM);
			assertEquals(2, t.maxRight(0, s -> s <= 13));
			assertEquals(3, t.maxRight(0, s -> s <= 14));
			assertEquals(5, t.maxRight(0, s -> s <= 18));
			assertEquals(0, t.maxRight(0, s -> s <= 4));
			assertEquals(4, t.maxRight(2, s -> s <= 9));
			assertEquals(5, t.maxRight(5, s -> s <= 0), "l = size なら size");
			assertThrows(IndexOutOfBoundsException.class, () -> t.maxRight(6, s -> true));
		}

		@Test
		void minLeft() {
			final Target t = create(SAMPLE, Op.SUM);
			assertEquals(3, t.minLeft(5, s -> s <= 4));
			assertEquals(2, t.minLeft(5, s -> s <= 11));
			assertEquals(0, t.minLeft(5, s -> s <= 18));
			assertEquals(5, t.minLeft(5, s -> s <= 2));
			assertEquals(0, t.minLeft(0, s -> s <= 0), "r = 0 なら0");
			assertThrows(IndexOutOfBoundsException.class, () -> t.minLeft(6, s -> true));
		}
	}

	@Nested
	class Conversion {

		@Test
		void iteratorAndToString() {
			final Target t = create(SAMPLE, Op.SUM);
			final Iterator<?> it = t.iterator();
			for (final long v : SAMPLE) assertEquals(v, ((Number) it.next()).longValue());
			assertFalse(it.hasNext());
			assertThrows(NoSuchElementException.class, it::next);
			assertEquals("5 2 7 1 3", t.toString());
			assertEquals("", create(0, Op.SUM).toString());
		}
	}
}
