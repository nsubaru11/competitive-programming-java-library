package unit.ds.segmenttree;

import java.util.*;
import java.util.function.*;

import org.junit.jupiter.api.*;

import unit.support.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * LazySegmentTree・IntLazySegmentTree・LongLazySegmentTree に共通する振る舞いのテスト。値は long で扱う。
 * 演算は最小値、作用素は ADD（区間加算）または既定の区間代入を使う。
 */
abstract class LazySegmentTreeContract {

	/**
	 * ADD は mapping・composition に加算を渡し、ASSIGN は省略して既定の区間代入にする。
	 */
	enum Act {
		ADD, ASSIGN
	}

	static final long[] SAMPLE = {5, 2, 7, 1, 3};

	interface Target {
		Target copy();

		int size();

		long identity();

		long get(int i);

		long set(int i, long v);

		long apply(int i, long f);

		void apply(int l, int r, long f);

		Object fill(long v);

		Object setAll(IntToLongFunction init);

		long query(int l, int r);

		long queryAll();

		int maxRight(int l, LongPredicate tester);

		int minLeft(int r, LongPredicate tester);

		Iterator<?> iterator();
	}

	abstract Target create(int n, Act act);

	abstract Target create(long[] a, Act act);

	abstract Target createFromCollection(long[] a, Act act);

	/**
	 * 最小値の単位元（その型の最大値）。
	 */
	abstract long inf();

	@Nested
	class Construction {

		@Test
		void fromSizeAndCollection() {
			final Target t = create(3, Act.ADD);
			assertEquals(inf(), t.identity());
			assertEquals(inf(), t.get(1));
			assertEquals(inf(), t.queryAll());
			assertEquals(1L, createFromCollection(SAMPLE, Act.ADD).queryAll());
			assertEquals(1L, createFromCollection(SAMPLE, Act.ASSIGN).queryAll());
		}

		@Test
		void copyKeepsPendingActsAndIsIndependent() {
			final Target t = create(SAMPLE, Act.ADD);
			t.apply(0, 5, 10);
			final Target c = t.copy();
			c.apply(0, 2, 100);
			assertEquals(11L, t.queryAll());
			assertEquals(112L, c.query(0, 2));
			assertEquals(17L, c.get(2));
		}
	}

	@Nested
	class Access {

		@Test
		void getAfterRangeAct() {
			final Target t = create(SAMPLE, Act.ADD);
			t.apply(1, 4, 10);
			assertArrayEquals(new long[]{5, 12, 17, 11, 3}, values(t));
			assertThrows(IndexOutOfBoundsException.class, () -> t.get(5));
		}
	}

	@Nested
	class Updates {

		@Test
		void pointUpdatesReturnDocumentedValues() {
			final Target t = create(SAMPLE, Act.ADD);
			assertEquals(7L, t.set(2, 4), "set は更新前の値");
			assertEquals(9L, t.apply(2, 5), "単一点の apply は更新後の値");
			assertEquals(1L, t.queryAll());
		}

		@Test
		void rangeAssignIsDefault() {
			final Target t = create(SAMPLE, Act.ASSIGN);
			t.apply(1, 4, 6);
			assertArrayEquals(new long[]{5, 6, 6, 6, 3}, values(t));
			t.apply(0, 2, 9);
			assertEquals(3L, t.queryAll());
			assertEquals(6L, t.query(0, 4));
		}

		@Test
		void emptyRangeActDoesNothing() {
			final Target t = create(SAMPLE, Act.ADD);
			t.apply(3, 3, 100);
			t.apply(4, 2, 100);
			assertArrayEquals(SAMPLE, values(t));
		}

		@Test
		void outOfRangeThrows() {
			final Target t = create(SAMPLE, Act.ADD);
			assertThrows(IndexOutOfBoundsException.class, () -> t.apply(0, 6, 1));
			assertThrows(IndexOutOfBoundsException.class, () -> t.set(5, 1));
			assertThrows(IndexOutOfBoundsException.class, () -> t.query(-1, 2));
		}
	}

	@Nested
	class Bulk {

		@Test
		void fillAndSetAllDiscardPendingActs() {
			final Target t = create(SAMPLE, Act.ADD);
			t.apply(0, 5, 100);
			t.fill(4);
			assertArrayEquals(new long[]{4, 4, 4, 4, 4}, values(t));
			t.apply(0, 5, 100);
			t.setAll(i -> 10L - i);
			assertArrayEquals(new long[]{10, 9, 8, 7, 6}, values(t));
			assertEquals(6L, t.queryAll());
		}
	}

	@Nested
	class Aggregation {

		@Test
		void queryHalfOpen() {
			final Target t = create(SAMPLE, Act.ADD);
			assertEquals(2L, t.query(0, 3));
			assertEquals(1L, t.query(2, 5));
			assertEquals(inf(), t.query(2, 2), "空区間は単位元");
		}
	}

	@Nested
	class Search {

		@Test
		void maxRightAndMinLeftAfterAct() {
			final Target t = create(SAMPLE, Act.ADD);
			t.apply(3, 4, 10);
			// 5 2 7 11 3
			assertEquals(1, t.maxRight(0, m -> m >= 3));
			assertEquals(4, t.maxRight(2, m -> m >= 7));
			assertEquals(5, t.maxRight(5, m -> false), "l = size なら size");
			assertEquals(2, t.minLeft(4, m -> m >= 7));
			assertEquals(0, t.minLeft(0, m -> false), "r = 0 なら0");
		}
	}

	@Nested
	class Conversion {

		@Test
		void iteratorAndToString() {
			final Target t = create(SAMPLE, Act.ADD);
			t.apply(0, 2, 1);
			assertEquals("6 3 7 1 3", t.toString());
			final Iterator<?> it = t.iterator();
			for (int i = 0; i < 5; i++) it.next();
			assertThrows(NoSuchElementException.class, it::next);
			assertEquals("", create(0, Act.ADD).toString());
		}
	}

	@RepeatedTest(100)
	void matchesNaive(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final int n = 1 + random.nextInt(20);
		final Act act = random.nextBoolean() ? Act.ADD : Act.ASSIGN;
		final long[] naive = random.longs(n, 0, 100).toArray();
		final Target t = create(naive.clone(), act);
		new RandomOperations()
			.add("applyRange", 3, (r, at) -> {
				final int l = r.nextInt(n + 1), rr = r.nextInt(n + 1);
				final long f = act == Act.ADD ? r.nextInt(-10, 11) : r.nextInt(100);
				t.apply(l, rr, f);
				for (int i = l; i < rr; i++) naive[i] = act == Act.ADD ? naive[i] + f : f;
			})
			.add("applyPoint", (r, at) -> {
				final int i = r.nextInt(n);
				final long f = act == Act.ADD ? r.nextInt(-10, 11) : r.nextInt(100);
				naive[i] = act == Act.ADD ? naive[i] + f : f;
				assertEquals(naive[i], t.apply(i, f), at);
			})
			.add("set", (r, at) -> {
				final int i = r.nextInt(n);
				final long v = r.nextInt(100);
				assertEquals(naive[i], t.set(i, v), at);
				naive[i] = v;
			})
			.add("query", 3, (r, at) -> {
				final int l = r.nextInt(n + 1), rr = r.nextInt(n + 1);
				assertEquals(min(naive, l, rr), t.query(l, rr), at);
				assertEquals(min(naive, 0, n), t.queryAll(), at);
			})
			.add("maxRight", (r, at) -> {
				final int l = r.nextInt(n + 1);
				final long w = r.nextInt(-50, 150);
				int expected = l;
				while (expected < n && naive[expected] >= w) expected++;
				assertEquals(expected, t.maxRight(l, m -> m >= w), at);
			})
			.add("minLeft", (r, at) -> {
				final int rr = r.nextInt(n + 1);
				final long w = r.nextInt(-50, 150);
				int expected = rr;
				while (expected > 0 && naive[expected - 1] >= w) expected--;
				assertEquals(expected, t.minLeft(rr, m -> m >= w), at);
			})
			.add("get", (r, at) -> {
				final int i = r.nextInt(n);
				assertEquals(naive[i], t.get(i), at);
			})
			.run(seed, random, 300);
	}

	long min(final long[] a, final int l, final int r) {
		long res = inf();
		for (int i = l; i < r; i++) res = Math.min(res, a[i]);
		return res;
	}

	static long[] values(final Target t) {
		final long[] res = new long[t.size()];
		for (int i = 0; i < res.length; i++) res[i] = t.get(i);
		return res;
	}
}
