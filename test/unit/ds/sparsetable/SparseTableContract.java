package unit.ds.sparsetable;

import java.util.*;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SparseTable・IntSparseTable・LongSparseTable に共通する振る舞いのテスト。値は long で扱う。
 */
abstract class SparseTableContract {

	interface Target {
		long query(int l, int r);
	}

	/**
	 * max が true なら最大値、false なら最小値の Sparse Table を作る。
	 */
	abstract Target create(long[] a, boolean max);

	@Nested
	class Aggregation {

		@Test
		void queryHalfOpen() {
			final long[] a = {5, 2, 7, 1, 3, 9};
			final Target min = create(a, false), max = create(a, true);
			assertEquals(2L, min.query(0, 3));
			assertEquals(1L, min.query(1, 6));
			assertEquals(7L, min.query(2, 3), "長さ1の区間");
			assertEquals(9L, max.query(0, 6));
			assertEquals(7L, max.query(1, 5));
		}

		@Test
		void inputIsCopied() {
			final long[] a = {5, 2};
			final Target t = create(a, false);
			a[1] = 9;
			assertEquals(2L, t.query(0, 2));
		}

		@Test
		void singleElement() {
			assertEquals(4L, create(new long[]{4}, true).query(0, 1));
		}
	}

	@RepeatedTest(50)
	void matchesNaive(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final int n = 1 + random.nextInt(40);
		final long[] a = random.longs(n, -100, 100).toArray();
		final Target min = create(a, false), max = create(a, true);
		for (int l = 0; l < n; l++) {
			long mn = Long.MAX_VALUE, mx = Long.MIN_VALUE;
			for (int r = l + 1; r <= n; r++) {
				mn = Math.min(mn, a[r - 1]);
				mx = Math.max(mx, a[r - 1]);
				final String at = "seed=" + seed + ", l=" + l + ", r=" + r;
				assertEquals(mn, min.query(l, r), at);
				assertEquals(mx, max.query(l, r), at);
			}
		}
	}
}
