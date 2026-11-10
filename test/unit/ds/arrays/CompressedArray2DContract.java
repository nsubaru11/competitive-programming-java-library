package unit.ds.arrays;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.ds.arrays.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * IntCompressedArray2D・LongCompressedArray2D に共通する振る舞いのテスト。値は long、順位は int で扱う。
 */
abstract class CompressedArray2DContract {

	// 昇順は 10 20 20 30 40 50
	static final long[][] SAMPLE = {{30, 10, 20}, {50, 20, 40}};

	interface Target {
		Target copy();

		int h();

		int w();

		int size();

		int uniqueSize();

		boolean inRange(int i, int j);

		int get(int i, int j);

		int getOrDefault(int i, int j, int defaultValue);

		int rankOfValue(long v);

		long valueOfRank(int rank);

		boolean containsValue(long v);

		int count(long v);

		long[][] restore();

		int[][] toArray2D();

		long[] sorted();

		int[] ranks();

		Iterator<? extends Number> iterator();

		Spliterator<? extends Number> spliterator();
	}

	abstract Target create(long[][] a);

	abstract Target create(long[][] a, RankType type);

	abstract Target create(long[][] a, RankType type, boolean oneBased);

	@Nested
	class Construction {

		@Test
		void defaultIsDenseZeroBased() {
			final int[][] expected = {{2, 0, 1}, {4, 1, 3}};
			assertArrayEquals(expected, create(SAMPLE).toArray2D());
			assertArrayEquals(expected, create(SAMPLE, RankType.DENSE).toArray2D());
		}

		@Test
		void rankTypesAndOneBased() {
			assertArrayEquals(new int[][]{{4, 1, 2}, {6, 2, 5}}, create(SAMPLE, RankType.COMPETITION, true).toArray2D());
			assertArrayEquals(new int[][]{{3, 0, 2}, {5, 2, 4}}, create(SAMPLE, RankType.MODIFIED_COMPETITION, false).toArray2D());
		}

		@Test
		void inputIsCopiedAndCopyHasSameContents() {
			final long[][] a = {SAMPLE[0].clone(), SAMPLE[1].clone()};
			final Target c = create(a);
			a[0][0] = 0;
			assertArrayEquals(SAMPLE, c.restore());
			assertArrayEquals(c.toArray2D(), c.copy().toArray2D());
		}
	}

	@Nested
	class State {

		@Test
		void sizes() {
			final Target c = create(SAMPLE);
			assertEquals(2, c.h());
			assertEquals(3, c.w());
			assertEquals(6, c.size());
			assertEquals(5, c.uniqueSize());
		}
	}

	@Nested
	class Access {

		@Test
		void inRangeAndGet() {
			final Target c = create(SAMPLE);
			assertTrue(c.inRange(1, 2));
			assertFalse(c.inRange(2, 0));
			assertFalse(c.inRange(0, -1));
			assertEquals(4, c.get(1, 0));
			assertEquals(4, c.getOrDefault(1, 0, -1));
			assertEquals(-1, c.getOrDefault(0, 3, -1));
			assertThrows(IndexOutOfBoundsException.class, () -> c.get(0, 3));
			assertThrows(IndexOutOfBoundsException.class, () -> c.get(2, 0));
		}
	}

	@Nested
	class Search {

		@Test
		void rankValueContainsCount() {
			final Target c = create(SAMPLE);
			assertEquals(3, c.rankOfValue(40));
			assertEquals(40L, c.valueOfRank(3));
			assertTrue(c.containsValue(50));
			assertFalse(c.containsValue(35));
			assertEquals(2, c.count(20));
			assertEquals(0, c.count(35));
		}
	}

	@Nested
	class Conversion {

		@Test
		void sortedRanksAndTraversal() {
			final Target c = create(SAMPLE);
			assertArrayEquals(new long[]{10, 20, 20, 30, 40, 50}, c.sorted());
			assertArrayEquals(new int[]{0, 1, 1, 2, 3, 4}, c.ranks());
			final Iterator<? extends Number> it = c.iterator();
			for (final int v : new int[]{2, 0, 1, 4, 1, 3}) assertEquals(v, it.next().intValue());
			assertFalse(it.hasNext());
			assertThrows(NoSuchElementException.class, it::next);
			assertEquals(6, c.spliterator().estimateSize());
			assertEquals("2 0 1\n4 1 3", c.toString());
		}
	}

	@RepeatedTest(50)
	void matchesNaive(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final int h = 1 + random.nextInt(5), w = 1 + random.nextInt(5);
		final long[][] a = new long[h][w];
		final long[] flat = new long[h * w];
		for (int i = 0; i < h; i++) for (int j = 0; j < w; j++) flat[i * w + j] = a[i][j] = random.nextInt(-5, 5);
		for (final RankType type : RankType.values()) {
			final String at = "seed=" + seed + ", " + type;
			final Target c = create(a, type, true);
			for (int i = 0; i < h; i++) {
				for (int j = 0; j < w; j++) {
					assertEquals(CompressedArrayContract.rank(flat, a[i][j], type) + 1, c.get(i, j), at);
				}
			}
			assertArrayEquals(a, c.restore(), at);
		}
	}
}
