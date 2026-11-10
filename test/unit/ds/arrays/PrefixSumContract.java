package unit.ds.arrays;

import java.util.*;

import org.junit.jupiter.api.*;

import unit.support.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 1〜3次元の累積和（int・long 版、法あり・なし）に共通する振る舞いのテスト。
 * 次元によって引数の個数が変わるため、座標は配列で受け渡し、{@link Adapter#call} で呼び出す。値は long で扱う。
 */
abstract class PrefixSumContract {

	/**
	 * 次元数。1〜3。
	 */
	abstract int dims();

	/**
	 * 法。法を取らない版は0。
	 */
	abstract long mod();

	/**
	 * 配列を受け取るコンストラクタで作る。flat は行優先順に並べた値である。
	 */
	abstract Object create(int[] shape, long[] flat);

	/**
	 * 初期化関数を受け取るコンストラクタで作る。
	 */
	abstract Object createByInit(int[] shape, long[] flat);

	/**
	 * 値の上限（含まない）。法を取る版は法、取らない版はその型で和があふれない大きさにする。
	 */
	long bound() {
		return mod() > 0 ? mod() : 1000;
	}

	@Nested
	class Construction {

		@Test
		void arrayAndInitGiveSameContents() {
			final Random random = new Random(1);
			final int[] shape = shape(random);
			final long[] flat = values(random, size(shape));
			final Object a = create(shape, flat), b = createByInit(shape, flat);
			assertArrayEquals(flat, toArray(a));
			assertArrayEquals(flat, toArray(b));
			assertEquals(a.toString(), b.toString());
		}
	}

	@Nested
	class Access {

		@Test
		void getAndDefault() {
			final int[] shape = fill(dims(), 2);
			final long[] flat = values(new Random(2), size(shape));
			final Object a = create(shape, flat);
			for (int p = 0; p < flat.length; p++) {
				assertEquals(flat[p], get(a, coordinate(shape, p)));
				assertEquals(flat[p], getOrDefault(a, coordinate(shape, p), -1));
			}
			for (int axis = 0; axis < dims(); axis++) {
				final int[] out = new int[dims()];
				out[axis] = 2;
				assertEquals(-1L, getOrDefault(a, out, -1));
				out[axis] = -1;
				assertEquals(-1L, getOrDefault(a, out, -1));
			}
		}

		@Test
		void outOfRangeGetThrows() {
			final int[] shape = fill(dims(), 2);
			final Object a = create(shape, new long[size(shape)]);
			for (int axis = 0; axis < dims(); axis++) {
				final int[] out = new int[dims()];
				out[axis] = 2;
				assertThrows(IndexOutOfBoundsException.class, () -> get(a, out));
			}
		}
	}

	@Nested
	class Aggregation {

		@Test
		void allRangesMatchNaive() {
			final Random random = new Random(3);
			for (int t = 0; t < 20; t++) {
				final int[] shape = shape(random);
				final long[] flat = values(random, size(shape));
				final Object a = create(shape, flat);
				assertEquals(naiveSum(shape, flat, new int[dims()], last(shape)), (long) Adapter.call(Long.class, a, "sumAll"));
				for (int x = 0; x < 30; x++) {
					final int[] lo = point(random, shape), hi = point(random, shape);
					final String at = "shape=" + Arrays.toString(shape) + ", lo=" + Arrays.toString(lo) + ", hi=" + Arrays.toString(hi);
					assertEquals(naiveSum(shape, flat, lo, hi), sum(a, lo, hi), at);
					assertEquals(naiveSum(shape, flat, new int[dims()], hi), sum(a, hi), at);
				}
			}
		}

		@Test
		void emptyRangeIsZero() {
			final int[] shape = fill(dims(), 3);
			final Object a = create(shape, values(new Random(4), size(shape)));
			for (int axis = 0; axis < dims(); axis++) {
				final int[] lo = new int[dims()], hi = fill(dims(), 2);
				lo[axis] = 2;
				hi[axis] = 1;
				assertEquals(0L, sum(a, lo, hi));
			}
		}

		@Test
		void outOfRangeSumThrows() {
			final int[] shape = fill(dims(), 2);
			final Object a = create(shape, new long[size(shape)]);
			for (int axis = 0; axis < dims(); axis++) {
				final int[] out = new int[dims()];
				out[axis] = 2;
				assertThrows(IndexOutOfBoundsException.class, () -> sum(a, out));
				assertThrows(IndexOutOfBoundsException.class, () -> sum(a, new int[dims()], out));
				// 始点が終点を超えても、座標が範囲外なら投げる
				assertThrows(IndexOutOfBoundsException.class, () -> sum(a, out, new int[dims()]));
			}
		}
	}

	@Nested
	class Conversion {

		@Test
		void iteratorIsRowMajor() {
			final int[] shape = fill(dims(), 2);
			final long[] flat = values(new Random(5), size(shape));
			final Object a = create(shape, flat);
			final Iterator<?> it = ((Iterable<?>) a).iterator();
			for (final long v : flat) assertEquals(v, ((Number) it.next()).longValue());
			assertFalse(it.hasNext());
			assertThrows(NoSuchElementException.class, it::next);
			final Spliterator<?> s = ((Iterable<?>) a).spliterator();
			assertEquals(flat.length, s.estimateSize());
			assertTrue(s.hasCharacteristics(Spliterator.ORDERED));
		}

		@Test
		void toStringPutsEachLastAxisRowOnALine() {
			final int[] shape = fill(dims(), 2);
			final long[] flat = new long[size(shape)];
			for (int p = 0; p < flat.length; p++) flat[p] = p;
			final StringBuilder expected = new StringBuilder();
			for (int p = 0; p < flat.length; p++) {
				if (p > 0) expected.append(p % 2 == 0 ? '\n' : ' ');
				expected.append(p);
			}
			assertEquals(expected.toString(), create(shape, flat).toString());
		}
	}

	// ---- 次元によらない呼び出し ----

	static long get(final Object a, final int[] p) {
		return Adapter.call(Long.class, a, "get", box(p));
	}

	static long getOrDefault(final Object a, final int[] p, final long defaultValue) {
		final Object[] args = Arrays.copyOf(box(p), p.length + 1);
		args[p.length] = defaultValue;
		return Adapter.call(Long.class, a, "getOrDefault", args);
	}

	static long sum(final Object a, final int[] hi) {
		return Adapter.call(Long.class, a, "sum", box(hi));
	}

	static long sum(final Object a, final int[] lo, final int[] hi) {
		final Object[] args = new Object[lo.length * 2];
		for (int k = 0; k < lo.length; k++) {
			args[k] = lo[k];
			args[lo.length + k] = hi[k];
		}
		return Adapter.call(Long.class, a, "sum", args);
	}

	long[] toArray(final Object a) {
		final List<Long> res = new ArrayList<>();
		((Iterable<?>) a).forEach(v -> res.add(((Number) v).longValue()));
		return res.stream().mapToLong(Long::longValue).toArray();
	}

	// ---- 愚直解と入力の生成 ----

	long naiveSum(final int[] shape, final long[] flat, final int[] lo, final int[] hi) {
		long s = 0;
		for (int p = 0; p < flat.length; p++) {
			final int[] c = coordinate(shape, p);
			boolean in = true;
			for (int k = 0; k < c.length; k++) in &= lo[k] <= c[k] && c[k] <= hi[k];
			// 法を取る版は足すたびに剰余を取り、long のあふれを避ける
			if (in) s = mod() > 0 ? (s + flat[p]) % mod() : s + flat[p];
		}
		return s;
	}

	int[] shape(final Random random) {
		final int[] s = new int[dims()];
		for (int k = 0; k < s.length; k++) s[k] = 1 + random.nextInt(dims() == 1 ? 12 : 4);
		return s;
	}

	long[] values(final Random random, final int n) {
		final long[] res = new long[n];
		final boolean signed = mod() == 0;
		for (int i = 0; i < n; i++) res[i] = signed ? random.nextLong(-bound() + 1, bound()) : random.nextLong(bound());
		return res;
	}

	static int[] point(final Random random, final int[] shape) {
		final int[] p = new int[shape.length];
		for (int k = 0; k < p.length; k++) p[k] = random.nextInt(shape[k]);
		return p;
	}

	static int[] coordinate(final int[] shape, int p) {
		final int[] c = new int[shape.length];
		for (int k = shape.length - 1; k >= 0; k--) {
			c[k] = p % shape[k];
			p /= shape[k];
		}
		return c;
	}

	static int size(final int[] shape) {
		int n = 1;
		for (final int s : shape) n *= s;
		return n;
	}

	static int[] last(final int[] shape) {
		final int[] res = new int[shape.length];
		for (int k = 0; k < res.length; k++) res[k] = shape[k] - 1;
		return res;
	}

	static int[] fill(final int n, final int v) {
		final int[] res = new int[n];
		Arrays.fill(res, v);
		return res;
	}

	static long[][] reshape2(final int[] s, final long[] flat) {
		final long[][] res = new long[s[0]][s[1]];
		for (int i = 0; i < s[0]; i++) System.arraycopy(flat, i * s[1], res[i], 0, s[1]);
		return res;
	}

	static long[][][] reshape3(final int[] s, final long[] flat) {
		final long[][][] res = new long[s[0]][s[1]][s[2]];
		for (int i = 0; i < s[0]; i++) for (int j = 0; j < s[1]; j++) System.arraycopy(flat, (i * s[1] + j) * s[2], res[i][j], 0, s[2]);
		return res;
	}

	private static Object[] box(final int[] p) {
		final Object[] res = new Object[p.length];
		for (int k = 0; k < p.length; k++) res[k] = p[k];
		return res;
	}
}
