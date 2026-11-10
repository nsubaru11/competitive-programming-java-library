package unit.ds.arrays;

import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import lib.ds.arrays.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * IntCompressedArray・LongCompressedArray に共通する振る舞いのテスト。値は long、順位は int で扱う。
 */
abstract class CompressedArrayContract {

	// 昇順は 10 20 20 30
	static final long[] SAMPLE = {30, 10, 20, 20};

	/**
	 * 順位の定義どおりに数える。DENSE は v 未満の値の種類数、COMPETITION は v 未満の個数、MODIFIED_COMPETITION は v 以下の個数 - 1。
	 */
	static int rank(final long[] a, final long v, final RankType type) {
		return switch (type) {
			case DENSE -> (int) Arrays.stream(a).filter(x -> x < v).distinct().count();
			case COMPETITION -> (int) Arrays.stream(a).filter(x -> x < v).count();
			case MODIFIED_COMPETITION -> (int) Arrays.stream(a).filter(x -> x <= v).count() - 1;
		};
	}

	abstract Target create(long[] a);

	abstract Target create(long[] a, RankType type);

	abstract Target create(long[] a, RankType type, boolean oneBased);

	/**
	 * IntCollection・LongCollection を受け取るコンストラクタで作る。
	 */
	abstract Target createFromCollection(long[] a, RankType type, boolean oneBased);

	@RepeatedTest(100)
	void matchesNaive(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final int n = 1 + random.nextInt(30);
		final long[] a = new long[n];
		for (int i = 0; i < n; i++) a[i] = random.nextInt(-10, 10);
		for (final RankType type : RankType.values()) {
			for (final boolean oneBased : new boolean[]{false, true}) {
				final String at = "seed=" + seed + ", " + type + ", oneBased=" + oneBased;
				final Target c = create(a, type, oneBased);
				final int[] expected = new int[n];
				for (int i = 0; i < n; i++) expected[i] = rank(a, a[i], type) + (oneBased ? 1 : 0);
				assertArrayEquals(expected, c.toArray(), at);
				assertEquals(Arrays.stream(a).distinct().count(), c.uniqueSize(), at);
				assertArrayEquals(a, c.restore(), at);
				for (int i = 0; i < n; i++) {
					assertEquals(expected[i], c.rankOfValue(a[i]), at);
					assertEquals(a[i], c.valueOfRank(expected[i]), at);
				}
			}
		}
	}

	interface Target {
		Target copy();

		int size();

		int uniqueSize();

		int get(int i);

		int getOrDefault(int i, int defaultValue);

		int rankOfValue(long v);

		long valueOfRank(int rank);

		boolean containsValue(long v);

		int count(long v);

		long[] restore();

		int[] toArray();

		long[] sorted();

		int[] ranks();

		Iterator<? extends Number> iterator();

		Spliterator<? extends Number> spliterator();
	}

	@Nested
	class Construction {

		@Test
		void defaultIsDenseZeroBased() {
			assertArrayEquals(new int[]{2, 0, 1, 1}, create(SAMPLE).toArray());
			assertArrayEquals(new int[]{2, 0, 1, 1}, create(SAMPLE, RankType.DENSE).toArray());
		}

		@ParameterizedTest
		@CsvSource({
				"DENSE, false, '2 0 1 1'",
				"DENSE, true, '3 1 2 2'",
				"COMPETITION, false, '3 0 1 1'",
				"COMPETITION, true, '4 1 2 2'",
				"MODIFIED_COMPETITION, false, '3 0 2 2'",
				"MODIFIED_COMPETITION, true, '4 1 3 3'",
		})
		void rankTypes(final RankType type, final boolean oneBased, final String expected) {
			assertEquals(expected, create(SAMPLE, type, oneBased).toString());
			assertEquals(expected, createFromCollection(SAMPLE, type, oneBased).toString());
		}

		@Test
		void inputIsCopied() {
			final long[] a = SAMPLE.clone();
			final Target c = create(a);
			a[0] = 0;
			assertArrayEquals(SAMPLE, c.restore());
		}

		@Test
		void empty() {
			final Target c = create(new long[0]);
			assertEquals(0, c.size());
			assertEquals(0, c.uniqueSize());
			assertEquals("", c.toString());
		}

		@Test
		void copyHasSameContents() {
			final Target c = create(SAMPLE, RankType.COMPETITION, true);
			final Target copy = c.copy();
			assertArrayEquals(c.toArray(), copy.toArray());
			assertArrayEquals(c.ranks(), copy.ranks());
			assertEquals(c.uniqueSize(), copy.uniqueSize());
		}
	}

	@Nested
	class State {

		@Test
		void sizes() {
			final Target c = create(SAMPLE);
			assertEquals(4, c.size());
			assertEquals(3, c.uniqueSize());
		}
	}

	@Nested
	class Access {

		@Test
		void getAndDefault() {
			final Target c = create(SAMPLE);
			assertEquals(2, c.get(0));
			assertEquals(1, c.getOrDefault(3, -1));
			assertEquals(-1, c.getOrDefault(4, -1));
		}
	}

	@Nested
	class Search {

		@Test
		void rankAndValue() {
			final Target c = create(SAMPLE, RankType.COMPETITION, true);
			assertEquals(1, c.rankOfValue(10));
			assertEquals(2, c.rankOfValue(20));
			assertEquals(4, c.rankOfValue(30));
			assertEquals(20L, c.valueOfRank(2));
			assertEquals(30L, c.valueOfRank(4));
		}

		@Test
		void containsAndCount() {
			final Target c = create(SAMPLE);
			assertTrue(c.containsValue(20));
			assertFalse(c.containsValue(25));
			assertEquals(2, c.count(20));
			assertEquals(1, c.count(30));
			assertEquals(0, c.count(25));
		}
	}

	@Nested
	class Conversion {

		@Test
		void restoreSortedAndRanks() {
			final Target c = create(SAMPLE, RankType.MODIFIED_COMPETITION, false);
			assertArrayEquals(SAMPLE, c.restore());
			assertArrayEquals(new long[]{10, 20, 20, 30}, c.sorted());
			assertArrayEquals(new int[]{0, 2, 2, 3}, c.ranks());
		}

		@Test
		void arraysAreIndependent() {
			final Target c = create(SAMPLE);
			c.toArray()[0] = 9;
			c.sorted()[0] = 9;
			c.ranks()[0] = 9;
			assertEquals(2, c.get(0));
			assertArrayEquals(new long[]{10, 20, 20, 30}, c.sorted());
			assertArrayEquals(new int[]{0, 1, 1, 2}, c.ranks());
		}

		@Test
		void iteratorAndSpliterator() {
			final Target c = create(SAMPLE);
			final Iterator<? extends Number> it = c.iterator();
			for (final int v : new int[]{2, 0, 1, 1}) assertEquals(v, it.next().intValue());
			assertFalse(it.hasNext());
			assertThrows(NoSuchElementException.class, it::next);
			final Spliterator<? extends Number> s = c.spliterator();
			assertEquals(4, s.estimateSize());
			assertTrue(s.hasCharacteristics(Spliterator.ORDERED));
		}
	}
}
