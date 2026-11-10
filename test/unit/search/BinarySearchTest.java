package unit.search;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.search.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 探索範囲は半開区間 [l, r)。比較関数は添字 i の値と目標値の大小を返す（値が大きければ正）。
 * normalSearch は一致する添字のいずれか、lowerBoundSearch は最初、upperBoundSearch は最後の一致を返し、
 * 見つからなければ -(挿入位置 + 1) を返す。
 */
final class BinarySearchTest {

	private static final long[] SORTED = {1, 3, 3, 3, 7};

	private static BinarySearch.CompareFunction against(final long target) {
		return i -> Long.compare(SORTED[(int) i], target);
	}

	@Nested
	class Search {

		@Test
		void intRange() {
			assertEquals(1, BinarySearch.lowerBoundSearch(0, 5, against(3)));
			assertEquals(3, BinarySearch.upperBoundSearch(0, 5, against(3)));
			final int any = BinarySearch.normalSearch(0, 5, against(3));
			assertTrue(1 <= any && any <= 3);
			assertEquals(-(4 + 1), BinarySearch.lowerBoundSearch(0, 5, against(5)));
			assertEquals(-(4 + 1), BinarySearch.upperBoundSearch(0, 5, against(5)));
			assertEquals(-(4 + 1), BinarySearch.normalSearch(0, 5, against(5)));
			assertEquals(-(0 + 1), BinarySearch.normalSearch(0, 5, against(0)));
			assertEquals(-(2 + 1), BinarySearch.lowerBoundSearch(2, 4, against(1)), "区間外の要素は見つからない");
		}

		@Test
		void longRange() {
			// f(i) = i^2 と 10^12 を比べる
			final BinarySearch.CompareFunction square = i -> Long.compare(i * i, 1_000_000_000_000L);
			assertEquals(1_000_000L, BinarySearch.normalSearch(0L, 2_000_000L, square));
			assertEquals(1_000_000L, BinarySearch.lowerBoundSearch(0L, 2_000_000L, square));
			assertEquals(1_000_000L, BinarySearch.upperBoundSearch(0L, 2_000_000L, square));
			final BinarySearch.CompareFunction odd = i -> Long.compare(i * i, 1_000_000_000_001L);
			assertEquals(-(1_000_001L + 1), BinarySearch.normalSearch(0L, 2_000_000L, odd));
			assertEquals(-(1_000_001L + 1), BinarySearch.lowerBoundSearch(0L, 2_000_000L, odd));
		}

		@Test
		void matchesNaive() {
			final Random random = new Random(1);
			for (int t = 0; t < 500; t++) {
				final long[] a = random.longs(random.nextInt(0, 15), -5, 6).sorted().toArray();
				final long v = random.nextLong(-6, 7);
				final BinarySearch.CompareFunction cmp = i -> Long.compare(a[(int) i], v);
				int first = -1, last = -1, less = 0;
				for (int i = 0; i < a.length; i++) {
					if (a[i] == v && first < 0) first = i;
					if (a[i] == v) last = i;
					if (a[i] < v) less = i + 1;
				}
				final String at = "a=" + Arrays.toString(a) + ", v=" + v;
				assertEquals(first >= 0 ? first : -(less + 1), BinarySearch.lowerBoundSearch(0, a.length, cmp), at);
				assertEquals(last >= 0 ? last : -(less + 1), BinarySearch.upperBoundSearch(0, a.length, cmp), at);
				assertEquals(first >= 0 ? first : -(less + 1), BinarySearch.lowerBoundSearch(0L, (long) a.length, cmp), at);
				final int normal = BinarySearch.normalSearch(0, a.length, cmp);
				if (first >= 0) assertTrue(first <= normal && normal <= last, at);
				else assertEquals(-(less + 1), normal, at);
				assertEquals(last >= 0 ? last : -(less + 1), BinarySearch.upperBoundSearch(0L, (long) a.length, cmp), at);
			}
		}
	}
}
