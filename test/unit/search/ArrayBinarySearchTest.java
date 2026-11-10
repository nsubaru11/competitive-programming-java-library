package unit.search;

import java.util.*;
import java.util.function.*;

import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import lib.ds.arrays.*;
import lib.search.*;

import unit.support.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * lowerBoundSearch は最初の一致位置、upperBoundSearch は最後の一致位置を返し、見つからなければ ~(挿入位置) を返す。
 * 区間版は半開区間 [l, r) を探索する。配列の型ごとのオーバーロードを同じ期待値で検査する。
 */
final class ArrayBinarySearchTest {

	/**
	 * 配列の型。昇順の long[] から対象の配列と探索値を作る。
	 */
	enum Kind {
		INT(a -> Arrays.stream(a).mapToInt(Math::toIntExact).toArray(), v -> (int) v),
		LONG(long[]::clone, v -> v),
		DOUBLE(a -> Arrays.stream(a).asDoubleStream().toArray(), v -> (double) v),
		COMPARABLE(a -> Arrays.stream(a).boxed().toArray(Long[]::new), v -> v),
		INT_ARRAY(a -> new IntArrayDeque(Arrays.stream(a).mapToInt(Math::toIntExact).toArray()), v -> (int) v),
		LONG_ARRAY(a -> new LongArrayDeque(a), v -> v);

		final Function<long[], Object> array;
		final LongFunction<Object> value;

		Kind(final Function<long[], Object> array, final LongFunction<Object> value) {
			this.array = array;
			this.value = value;
		}
	}

	@ParameterizedTest
	@EnumSource(Kind.class)
	void example(final Kind kind) {
		final Object a = kind.array.apply(new long[]{1, 3, 3, 3, 7});
		assertEquals(1, call(kind, "lowerBoundSearch", a, kind.value.apply(3)));
		assertEquals(3, call(kind, "upperBoundSearch", a, kind.value.apply(3)));
		assertEquals(~4, call(kind, "lowerBoundSearch", a, kind.value.apply(5)), "挿入位置4");
		assertEquals(~4, call(kind, "upperBoundSearch", a, kind.value.apply(5)));
		assertEquals(~0, call(kind, "lowerBoundSearch", a, kind.value.apply(0)));
		assertEquals(~5, call(kind, "upperBoundSearch", a, kind.value.apply(8)));
		assertEquals(3, call(kind, "count", a, kind.value.apply(3)));
		assertEquals(0, call(kind, "count", a, kind.value.apply(4)));
		assertTrue((boolean) Adapter.callStatic(Boolean.class, ArrayBinarySearch.class, "contains", a, kind.value.apply(7)));
		assertFalse((boolean) Adapter.callStatic(Boolean.class, ArrayBinarySearch.class, "contains", a, kind.value.apply(2)));
		// 区間 [2, 4) は 3 3
		assertEquals(2, call(kind, "lowerBoundSearch", a, 2, 4, kind.value.apply(3)));
		assertEquals(3, call(kind, "upperBoundSearch", a, 2, 4, kind.value.apply(3)));
		assertEquals(~4, call(kind, "lowerBoundSearch", a, 2, 4, kind.value.apply(7)), "区間外の要素は見つからない");
		assertEquals(2, call(kind, "count", a, 2, 4, kind.value.apply(3)));
	}

	@ParameterizedTest
	@EnumSource(Kind.class)
	void matchesNaive(final Kind kind) {
		final Random random = new Random(kind.ordinal());
		for (int t = 0; t < 200; t++) {
			final long[] sorted = random.longs(random.nextInt(0, 15), -5, 6).sorted().toArray();
			final Object a = kind.array.apply(sorted);
			final int n = sorted.length, l = random.nextInt(n + 1), r = l + random.nextInt(n - l + 1);
			final long v = random.nextLong(-6, 7);
			final String at = "a=" + Arrays.toString(sorted) + ", v=" + v + ", [" + l + ", " + r + ")";
			assertEquals(naiveLower(sorted, 0, n, v), call(kind, "lowerBoundSearch", a, kind.value.apply(v)), at);
			assertEquals(naiveUpper(sorted, 0, n, v), call(kind, "upperBoundSearch", a, kind.value.apply(v)), at);
			assertEquals(naiveCount(sorted, 0, n, v), call(kind, "count", a, kind.value.apply(v)), at);
			assertEquals(naiveCount(sorted, 0, n, v) > 0, Adapter.callStatic(Boolean.class, ArrayBinarySearch.class, "contains", a, kind.value.apply(v)), at);
			assertEquals(naiveLower(sorted, l, r, v), call(kind, "lowerBoundSearch", a, l, r, kind.value.apply(v)), at);
			assertEquals(naiveUpper(sorted, l, r, v), call(kind, "upperBoundSearch", a, l, r, kind.value.apply(v)), at);
			assertEquals(naiveCount(sorted, l, r, v), call(kind, "count", a, l, r, kind.value.apply(v)), at);
			assertEquals(naiveCount(sorted, l, r, v) > 0, Adapter.callStatic(Boolean.class, ArrayBinarySearch.class, "contains", a, l, r, kind.value.apply(v)), at);
		}
	}

	private static int call(final Kind kind, final String name, final Object... args) {
		return Adapter.callStatic(Integer.class, ArrayBinarySearch.class, name, args);
	}

	private static int naiveLower(final long[] a, final int l, final int r, final long v) {
		int less = l;
		for (int i = l; i < r; i++) {
			if (a[i] == v) return i;
			if (a[i] < v) less = i + 1;
		}
		return ~less;
	}

	private static int naiveUpper(final long[] a, final int l, final int r, final long v) {
		int less = l;
		for (int i = r - 1; i >= l; i--) if (a[i] == v) return i;
		for (int i = l; i < r; i++) if (a[i] < v) less = i + 1;
		return ~less;
	}

	private static int naiveCount(final long[] a, final int l, final int r, final long v) {
		int c = 0;
		for (int i = l; i < r; i++) if (a[i] == v) c++;
		return c;
	}
}
