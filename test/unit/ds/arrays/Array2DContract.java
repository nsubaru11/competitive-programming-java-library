package unit.ds.arrays;

import java.util.*;
import java.util.function.*;

import org.junit.jupiter.api.*;

import unit.support.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * IntArray2D・LongArray2D・CharArray2D に共通する振る舞いのテスト。値は long で扱う。
 * 四則演算の更新は NumericArray2DContract で検査する。
 */
abstract class Array2DContract {

	// 2行3列。行優先で 1 2 3 / 4 5 6
	static final long[][] SAMPLE = {{1, 2, 3}, {4, 5, 6}};

	/**
	 * 反時計回りに90度回転した新しい配列を返す。
	 */
	private static long[][] rotateLeft(final long[][] m) {
		final int h = m.length, w = m[0].length;
		final long[][] res = new long[w][h];
		for (int i = 0; i < w; i++) for (int j = 0; j < h; j++) res[i][j] = m[j][w - 1 - i];
		return res;
	}

	private static long[][] transpose(final long[][] m) {
		final int h = m.length, w = m[0].length;
		final long[][] res = new long[w][h];
		for (int i = 0; i < w; i++) for (int j = 0; j < h; j++) res[i][j] = m[j][i];
		return res;
	}

	abstract Target create(int h, int w);

	abstract Target create(int h, int w, long v);

	abstract Target create(int h, int w, long[] rowMajor);

	abstract Target create(long[][] a);

	@RepeatedTest(100)
	void matchesNaive(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final int h = 1 + random.nextInt(5), w = 1 + random.nextInt(5);
		final long[][][] naive = {new long[h][w]};
		for (final long[] row : naive[0]) for (int j = 0; j < w; j++) row[j] = random.nextInt(100);
		final Target a = create(naive[0]);
		new RandomOperations()
				.add("rotateLeft", (r, at) -> {
					final int d = r.nextInt(-4, 5);
					a.rotateLeft(d);
					for (int k = Math.floorMod(d, 4); k > 0; k--) naive[0] = rotateLeft(naive[0]);
				})
				.add("rotateRight", (r, at) -> {
					a.rotateRight();
					for (int k = 0; k < 3; k++) naive[0] = rotateLeft(naive[0]);
				})
				.add("transpose", (r, at) -> {
					a.transpose();
					naive[0] = transpose(naive[0]);
				})
				.add("flipHorizontal", (r, at) -> {
					a.flipHorizontal();
					for (final long[] row : naive[0])
						for (int i = 0, j = row.length - 1; i < j; i++, j--) {
							final long t = row[i];
							row[i] = row[j];
							row[j] = t;
						}
				})
				.add("flipVertical", (r, at) -> {
					a.flipVertical();
					final long[][] m = naive[0];
					for (int i = 0, j = m.length - 1; i < j; i++, j--) {
						final long[] t = m[i];
						m[i] = m[j];
						m[j] = t;
					}
				})
				.add("set", 2, (r, at) -> {
					final long[][] m = naive[0];
					final int i = r.nextInt(m.length), j = r.nextInt(m[0].length);
					final long v = r.nextInt(100);
					assertEquals(m[i][j], a.set(i, j, v), at);
					m[i][j] = v;
				})
				.add("toArray2D", 2, (r, at) -> {
					assertEquals(naive[0].length, a.h(), at);
					assertArrayEquals(naive[0], a.toArray2D(), at);
				})
				.run(seed, random, 200);
	}

	interface Target {
		Object self();

		Target copy();

		int size();

		int h();

		int w();

		boolean inRange(int i, int j);

		long get(int i, int j);

		long getOrDefault(int i, int j, long defaultValue);

		long set(int i, int j, long v);

		long apply(int i, int j, long x, LongBinaryOperator f);

		Object fill(long v);

		Object setAll(IntBinaryOperator init);

		Object rotateLeft();

		Object rotateLeft(int distance);

		Object rotateRight();

		Object rotateRight(int distance);

		Object transpose();

		Object flipHorizontal();

		Object flipVertical();

		long[][] toArray2D();

		Iterator<? extends Number> iterator();

		Spliterator<? extends Number> spliterator();
	}

	@Nested
	class Construction {

		@Test
		void fromSizeValueAndRowMajor() {
			assertArrayEquals(new long[2][3], create(2, 3).toArray2D());
			assertArrayEquals(new long[][]{{7, 7}, {7, 7}}, create(2, 2, 7).toArray2D());
			// 先頭 h * w 要素だけを使う
			assertArrayEquals(SAMPLE, create(2, 3, new long[]{1, 2, 3, 4, 5, 6, 7}).toArray2D());
			assertArrayEquals(SAMPLE, create(SAMPLE).toArray2D());
		}

		@Test
		void copyKeepsOrientationAndIsIndependent() {
			final Target a = create(SAMPLE);
			a.rotateRight();
			final Target copy = a.copy();
			assertArrayEquals(a.toArray2D(), copy.toArray2D());
			copy.set(0, 0, 9);
			assertEquals(4L, a.get(0, 0));
		}
	}

	@Nested
	class State {

		@Test
		void dimensionsFollowRotation() {
			final Target a = create(SAMPLE);
			assertEquals(6, a.size());
			assertEquals(2, a.h());
			assertEquals(3, a.w());
			a.rotateLeft();
			assertEquals(3, a.h());
			assertEquals(2, a.w());
		}
	}

	@Nested
	class Access {

		@Test
		void inRangeAndGet() {
			final Target a = create(SAMPLE);
			assertTrue(a.inRange(1, 2));
			assertFalse(a.inRange(2, 0));
			assertFalse(a.inRange(0, 3));
			assertFalse(a.inRange(-1, 0));
			assertEquals(6L, a.get(1, 2));
			assertEquals(6L, a.getOrDefault(1, 2, 0));
			assertEquals(9L, a.getOrDefault(2, 0, 9));
		}

		@Test
		void outOfRangeThrows() {
			final Target a = create(SAMPLE);
			assertThrows(IndexOutOfBoundsException.class, () -> a.get(2, 0));
			assertThrows(IndexOutOfBoundsException.class, () -> a.get(0, 3));
			assertThrows(IndexOutOfBoundsException.class, () -> a.set(-1, 0, 0));
		}
	}

	@Nested
	class Updates {

		@Test
		void setAndApply() {
			final Target a = create(SAMPLE);
			assertEquals(5L, a.set(1, 1, 8), "set は更新前の値");
			assertEquals(9L, a.apply(1, 1, 9, Math::max), "apply は f(現在値, x)");
			assertEquals(9L, a.get(1, 1));
		}
	}

	@Nested
	class Bulk {

		@Test
		void fillAndSetAllFollowOrientation() {
			final Target a = create(SAMPLE);
			assertSame(a.self(), a.fill(3));
			assertArrayEquals(new long[][]{{3, 3, 3}, {3, 3, 3}}, a.toArray2D());
			a.transpose();
			assertSame(a.self(), a.setAll((i, j) -> 10 * i + j));
			assertArrayEquals(new long[][]{{0, 1}, {10, 11}, {20, 21}}, a.toArray2D());
		}
	}

	@Nested
	class Transformation {

		@Test
		void rotateLeftIsCounterclockwise() {
			final Target a = create(SAMPLE);
			assertSame(a.self(), a.rotateLeft());
			assertArrayEquals(new long[][]{{3, 6}, {2, 5}, {1, 4}}, a.toArray2D());
		}

		@Test
		void rotateRightIsClockwise() {
			final Target a = create(SAMPLE);
			assertSame(a.self(), a.rotateRight());
			assertArrayEquals(new long[][]{{4, 1}, {5, 2}, {6, 3}}, a.toArray2D());
		}

		@Test
		void rotateByDistanceUsesModuloFour() {
			final Target a = create(SAMPLE);
			assertSame(a.self(), a.rotateLeft(5));
			assertArrayEquals(new long[][]{{3, 6}, {2, 5}, {1, 4}}, a.toArray2D());
			assertSame(a.self(), a.rotateRight(2));
			assertArrayEquals(new long[][]{{4, 1}, {5, 2}, {6, 3}}, a.toArray2D());
			a.rotateRight(-1);
			assertArrayEquals(SAMPLE, a.toArray2D());
		}

		@Test
		void transposeAndFlip() {
			final Target a = create(SAMPLE);
			assertSame(a.self(), a.transpose());
			assertArrayEquals(new long[][]{{1, 4}, {2, 5}, {3, 6}}, a.toArray2D());
			a.transpose();
			assertSame(a.self(), a.flipHorizontal());
			assertArrayEquals(new long[][]{{3, 2, 1}, {6, 5, 4}}, a.toArray2D());
			assertSame(a.self(), a.flipVertical());
			assertArrayEquals(new long[][]{{6, 5, 4}, {3, 2, 1}}, a.toArray2D());
		}
	}

	@Nested
	class Conversion {

		@Test
		void traversalIsRowMajorInCurrentOrientation() {
			final Target a = create(SAMPLE);
			a.rotateRight();
			final Iterator<? extends Number> it = a.iterator();
			for (final long v : new long[]{4, 1, 5, 2, 6, 3}) assertEquals(v, it.next().longValue());
			assertFalse(it.hasNext());
			assertThrows(NoSuchElementException.class, it::next);
			final Spliterator<? extends Number> s = a.spliterator();
			assertEquals(6, s.estimateSize());
			assertTrue(s.hasCharacteristics(Spliterator.ORDERED));
		}

		@Test
		void toArray2DIsIndependent() {
			final Target a = create(SAMPLE);
			a.toArray2D()[0][0] = 9;
			assertEquals(1L, a.get(0, 0));
		}
	}
}
