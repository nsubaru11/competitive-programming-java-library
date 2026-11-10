package unit.util;

import java.util.*;
import java.util.function.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import lib.ds.arrays.*;
import lib.util.*;

import unit.support.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ArrayUtils の型ごとのオーバーロード（char[]・int[]・long[]・IntArray・LongArray）を同じ愚直解で検査する。
 * その型にないメソッドは検査しない。winMaxLen・winMinLen は返り値の定義が Javadoc から一意に読み取れないため検査しない。
 */
final class ArrayUtilsTest {

	/**
	 * 型に対応するオーバーロードがあれば呼び出し、なければ empty を返す。
	 */
	private static <R> Optional<R> call(final Class<R> type, final String name, final Object... args) {
		try {
			return Optional.ofNullable(Adapter.callStatic(type, ArrayUtils.class, name, args));
		} catch (final IllegalArgumentException e) {
			if (e.getMessage() != null && e.getMessage().endsWith("が見つからない")) return Optional.empty();
			throw e;
		}
	}

	private static void expect(final String at, final Object expected, final Optional<?> actual) {
		actual.ifPresent(v -> assertEquals(expected, v, at));
	}

	// long 版は変換しても同じ配列が返るため、書き換えられないよう深いコピーを渡す
	private static long[][] copy(final long[][] a) {
		return Arrays.stream(a).map(long[]::clone).toArray(long[][]::new);
	}

	private static long[] back(final Object a) {
		return (long[]) Adapter.convert(a, long[].class);
	}

	private static long[] reversed(final long[] a, final int l, final int r) {
		final long[] res = a.clone();
		for (int i = l, j = r - 1; i < j; i++, j--) {
			res[i] = a[j];
			res[j] = a[i];
		}
		return res;
	}

	private static int indexOf(final long[] a, final long v) {
		for (int i = 0; i < a.length; i++) if (a[i] == v) return i;
		return -1;
	}

	private static int lastIndexOf(final long[] a, final long v) {
		for (int i = a.length - 1; i >= 0; i--) if (a[i] == v) return i;
		return -1;
	}

	private static boolean isSorted(final long[] a) {
		for (int i = 1; i < a.length; i++) if (a[i - 1] > a[i]) return false;
		return true;
	}

	private static int maxRun(final long[] a) {
		int best = 0;
		for (int i = 0; i < a.length; ) {
			int j = i;
			while (j < a.length && a[j] == a[i]) j++;
			best = Math.max(best, j - i);
			i = j;
		}
		return best;
	}

	/**
	 * sign が 1 なら両隣より真に大きい内部要素、-1 なら真に小さい内部要素を数える。
	 */
	private static int localCount(final long[] a, final int sign) {
		int c = 0;
		for (int i = 1; i + 1 < a.length; i++)
			if (Long.compare(a[i], a[i - 1]) == sign && Long.compare(a[i], a[i + 1]) == sign) c++;
		return c;
	}

	private static int longest(final long[] a, final Rel rel) {
		final int[] dp = new int[a.length];
		int best = 0;
		for (int i = 0; i < a.length; i++) {
			dp[i] = 1;
			for (int j = 0; j < i; j++) if (rel.test(a[j], a[i])) dp[i] = Math.max(dp[i], dp[j] + 1);
			best = Math.max(best, dp[i]);
		}
		return best;
	}

	@ParameterizedTest
	@EnumSource(Kind.class)
	void statisticsMatchNaive(final Kind kind) {
		final Random random = new Random(kind.ordinal());
		for (int t = 0; t < 300; t++) {
			final long[] a = random.longs(random.nextInt(1, 12), 0, 5).map(x -> x + kind.offset).toArray();
			final Object arr = kind.array.apply(a);
			final String at = kind + " " + Arrays.toString(a);
			final LongSummaryStatistics s = Arrays.stream(a).summaryStatistics();
			expect(at, s.getSum(), call(Long.class, "sum", arr));
			expect(at, s.getMin(), call(Long.class, "min", arr));
			expect(at, s.getMax(), call(Long.class, "max", arr));
			expect(at, (long) indexOf(a, s.getMin()), call(Long.class, "argMin", arr));
			expect(at, (long) indexOf(a, s.getMax()), call(Long.class, "argMax", arr));
			expect(at, Arrays.stream(a).distinct().count(), call(Long.class, "uniqueSize", arr));
			final long v = a[random.nextInt(a.length)], w = kind.offset + 7;
			expect(at, Arrays.stream(a).filter(x -> x == v).count(), call(Long.class, "count", arr, v));
			expect(at, 0L, call(Long.class, "count", arr, w));
			expect(at, (long) indexOf(a, v), call(Long.class, "indexOf", arr, v));
			expect(at, (long) lastIndexOf(a, v), call(Long.class, "lastIndexOf", arr, v));
			expect(at, isSorted(a), call(Boolean.class, "isSorted", arr));
			expect(at, Arrays.stream(a).distinct().count() == 1, call(Boolean.class, "allEqual", arr));
			final long threshold = kind.offset + 2;
			final boolean all = Arrays.stream(a).allMatch(x -> x >= threshold), any = Arrays.stream(a).anyMatch(x -> x >= threshold);
			if (kind == Kind.LONG || kind == Kind.LONG_ARRAY) {
				expect(at, all, call(Boolean.class, "allMatch", arr, (LongPredicate) x -> x >= threshold));
				expect(at, any, call(Boolean.class, "anyMatch", arr, (LongPredicate) x -> x >= threshold));
			} else {
				expect(at, all, call(Boolean.class, "allMatch", arr, (IntPredicate) x -> x >= threshold));
				expect(at, any, call(Boolean.class, "anyMatch", arr, (IntPredicate) x -> x >= threshold));
			}
			if (kind.offset == 0) {
				final long[] freq = new long[6];
				for (final long x : a) freq[(int) x]++;
				call(int[].class, "freq", arr, 6).ifPresent(f -> assertArrayEquals(freq, Arrays.stream(f).asLongStream().toArray(), at));
				long mex = 0;
				while (indexOf(a, mex) >= 0) mex++;
				expect(at, mex, call(Long.class, "mex", arr));
			}
		}
	}

	@ParameterizedTest
	@EnumSource(Kind.class)
	void sequencesMatchNaive(final Kind kind) {
		final Random random = new Random(10 + kind.ordinal());
		for (int t = 0; t < 300; t++) {
			final long[] a = random.longs(random.nextInt(1, 12), 0, 4).map(x -> x + kind.offset).toArray();
			final Object arr = kind.array.apply(a);
			final int len = random.nextInt(1, a.length + 1);
			final long[] head = Arrays.copyOf(a, len);
			final String at = kind + " " + Arrays.toString(a) + ", len=" + len;
			expect(at, (long) maxRun(a), call(Long.class, "maxRunLen", arr));
			expect(at, (long) maxRun(head), call(Long.class, "maxRunLen", arr, len));
			expect(at, (long) localCount(a, 1), call(Long.class, "localMaxCnt", arr));
			expect(at, (long) localCount(head, 1), call(Long.class, "localMaxCnt", arr, len));
			expect(at, (long) localCount(a, -1), call(Long.class, "localMinCnt", arr));
			expect(at, (long) localCount(head, -1), call(Long.class, "localMinCnt", arr, len));
			expect(at, (long) longest(a, (x, y) -> x < y), call(Long.class, "lis", arr));
			expect(at, (long) longest(head, (x, y) -> x < y), call(Long.class, "lis", arr, len));
			expect(at, (long) longest(a, (x, y) -> x <= y), call(Long.class, "lnds", arr));
			expect(at, (long) longest(head, (x, y) -> x <= y), call(Long.class, "lnds", arr, len));
			expect(at, (long) longest(a, (x, y) -> x > y), call(Long.class, "lds", arr));
			expect(at, (long) longest(head, (x, y) -> x > y), call(Long.class, "lds", arr, len));
			expect(at, (long) longest(a, (x, y) -> x >= y), call(Long.class, "lnis", arr));
			expect(at, (long) longest(head, (x, y) -> x >= y), call(Long.class, "lnis", arr, len));
			final int k = random.nextInt(1, a.length + 1);
			long best = Long.MIN_VALUE, worst = Long.MAX_VALUE;
			for (int i = 0; i + k <= a.length; i++) {
				long sum = 0;
				for (int j = i; j < i + k; j++) sum += a[j];
				best = Math.max(best, sum);
				worst = Math.min(worst, sum);
			}
			expect(at + ", k=" + k, best, call(Long.class, "maxWin", arr, k));
			expect(at + ", k=" + k, worst, call(Long.class, "minWin", arr, k));
		}
	}

	@ParameterizedTest
	@EnumSource(value = Kind.class, names = {"CHAR", "INT", "LONG"})
	void inPlaceOperations(final Kind kind) {
		final Random random = new Random(20 + kind.ordinal());
		for (int t = 0; t < 200; t++) {
			final long[] a = random.longs(random.nextInt(1, 10), 0, 9).map(x -> x + kind.offset).toArray();
			final String at = kind + " " + Arrays.toString(a);
			final Object r1 = kind.array.apply(a);
			Adapter.callStatic(Void.class, ArrayUtils.class, "reverse", r1);
			assertArrayEquals(reversed(a, 0, a.length), back(r1), at);
			final int l = random.nextInt(a.length + 1), r = l + random.nextInt(a.length - l + 1);
			final Object r2 = kind.array.apply(a);
			Adapter.callStatic(Void.class, ArrayUtils.class, "reverse", r2, l, r);
			assertArrayEquals(reversed(a, l, r), back(r2), at + ", [" + l + ", " + r + ")");
			final int i = random.nextInt(a.length), j = random.nextInt(a.length);
			final Object s = kind.array.apply(a);
			Adapter.callStatic(Void.class, ArrayUtils.class, "swap", s, i, j);
			final long[] swapped = a.clone();
			swapped[i] = a[j];
			swapped[j] = a[i];
			assertArrayEquals(swapped, back(s), at);
			final Object d = kind.array.apply(a);
			Adapter.callStatic(Void.class, ArrayUtils.class, "descendingSort", d);
			assertArrayEquals(Arrays.stream(a).boxed().sorted(Comparator.reverseOrder()).mapToLong(Long::longValue).toArray(), back(d), at);
			// 2次元配列は行単位で反転・交換する
			final long[][] grid = new long[a.length][];
			for (int k = 0; k < a.length; k++) grid[k] = new long[]{a[k], k + kind.offset};
			final Class<?> type2D = kind == Kind.CHAR ? char[][].class : kind == Kind.INT ? int[][].class : long[][].class;
			final Object g1 = Adapter.convert(copy(grid), type2D);
			Adapter.callStatic(Void.class, ArrayUtils.class, "reverse", g1, l, r);
			final long[][] g1Back = (long[][]) Adapter.convert(g1, long[][].class);
			for (int k = 0; k < a.length; k++)
				assertArrayEquals(grid[k >= l && k < r ? l + r - 1 - k : k], g1Back[k], at);
			final Object g2 = Adapter.convert(copy(grid), type2D);
			Adapter.callStatic(Void.class, ArrayUtils.class, "reverse", g2);
			assertArrayEquals(grid[0], ((long[][]) Adapter.convert(g2, long[][].class))[a.length - 1], at);
			final Object g3 = Adapter.convert(copy(grid), type2D);
			Adapter.callStatic(Void.class, ArrayUtils.class, "swapRow", g3, i, j);
			assertArrayEquals(grid[j], ((long[][]) Adapter.convert(g3, long[][].class))[i], at);
		}
	}

	enum Kind {
		CHAR('a', a -> Adapter.convert(a, char[].class)),
		INT(0, a -> Adapter.convert(a, int[].class)),
		LONG(0, long[]::clone),
		INT_ARRAY(0, a -> new IntArrayDeque((int[]) Adapter.convert(a, int[].class))),
		LONG_ARRAY(0, LongArrayDeque::new);

		/**
		 * 値に足すずれ。char 版は英小文字にする。
		 */
		final long offset;
		final Function<long[], Object> array;

		Kind(final long offset, final Function<long[], Object> array) {
			this.offset = offset;
			this.array = array;
		}
	}

	private interface Rel {
		boolean test(long prev, long next);
	}

	@Nested
	class Characters {

		@Test
		void frequencyAndMexOfLetters() {
			final char[] s = "abza".toCharArray(), t = "AZB".toCharArray();
			final int[] lower = ArrayUtils.freqLower(s), upper = ArrayUtils.freqUpper(t);
			assertEquals(26, lower.length);
			assertEquals(2, lower[0]);
			assertEquals(1, lower[25]);
			assertEquals(1, upper[0]);
			assertEquals(1, upper[25]);
			assertEquals('c', ArrayUtils.mexLower(s));
			assertEquals('C', ArrayUtils.mexUpper(t));
			assertEquals('\0', ArrayUtils.mexLower("abcdefghijklmnopqrstuvwxyz".toCharArray()), "全小文字を含めば '\\0'");
			assertEquals('\0', ArrayUtils.mexUpper("ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()));
		}
	}

	@Nested
	class Subsets {

		@RepeatedTest(50)
		void countsMatchBitmask(final RepetitionInfo info) {
			final Random random = new Random(info.getCurrentRepetition());
			final int[] a = random.ints(random.nextInt(1, 12), -3, 6).toArray();
			final long target = random.nextInt(-3, 12);
			final int k = random.nextInt(0, a.length + 1);
			int any = 0, sized = 0;
			for (int mask = 0; mask < 1 << a.length; mask++) {
				long sum = 0;
				for (int i = 0; i < a.length; i++) if ((mask >> i & 1) == 1) sum += a[i];
				if (sum != target) continue;
				any++;
				if (Integer.bitCount(mask) == k) sized++;
			}
			final String at = Arrays.toString(a) + ", t=" + target + ", k=" + k;
			assertEquals(any, ArrayUtils.subsetMitm(a, target), at);
			assertEquals(any, ArrayUtils.subsetDp(a, target), at);
			assertEquals(sized, ArrayUtils.subsetRecursion(a, target, k), at);
			final long[] la = Arrays.stream(a).asLongStream().toArray();
			expect(at, (long) any, call(Long.class, "subsetMitm", la, target));
			expect(at, (long) any, call(Long.class, "subsetDp", la, target));
			expect(at, (long) sized, call(Long.class, "subsetRecursion", la, target, k));
		}
	}
}
