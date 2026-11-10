package unit.ds.set;

import java.util.*;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * IntAVLMultiset・LongAVLMultiset の和に関する操作を、MultisetContract に加えて検査する。期待値は Guide の記載に従う。
 */
abstract class NumericMultisetContract extends MultisetContract {

	interface Numeric {
		boolean add(long t);

		long sum();

		long uniqueSum();

		long prefixSum(int i);

		long prefixUniqueSum(int i);

		long suffixSum(int i);

		long suffixUniqueSum(int i);

		long sumRange(int l, int r);

		long uniqueSumRange(int l, int r);
	}

	abstract Numeric createNumeric();

	// 昇順は 10 20 20 20 30
	Numeric numericSample() {
		final Numeric s = createNumeric();
		for (final long v : new long[]{20, 10, 20, 30, 20}) s.add(v);
		return s;
	}

	@Nested
	class Sums {

		@Test
		void totals() {
			final Numeric s = numericSample();
			assertEquals(100L, s.sum());
			assertEquals(60L, s.uniqueSum());
		}

		@Test
		void prefixAndSuffix() {
			final Numeric s = numericSample();
			final long[] prefix = {10, 30, 50, 70, 100};
			for (int i = 0; i < 5; i++) {
				assertEquals(prefix[i], s.prefixSum(i), "インデックス0から" + i + "まで");
				assertEquals(100 - (i == 0 ? 0 : prefix[i - 1]), s.suffixSum(i), "インデックス" + i + "から最後まで");
			}
			assertEquals(30L, s.prefixUniqueSum(1));
			assertEquals(50L, s.suffixUniqueSum(1));
			assertThrows(IndexOutOfBoundsException.class, () -> s.prefixSum(5));
			assertThrows(IndexOutOfBoundsException.class, () -> s.suffixUniqueSum(3));
		}

		@Test
		void ranges() {
			final Numeric s = numericSample();
			assertEquals(40L, s.sumRange(1, 3), "l を除き r を含む");
			assertEquals(0L, s.sumRange(2, 2));
			assertEquals(50L, s.uniqueSumRange(0, 2));
			assertThrows(IndexOutOfBoundsException.class, () -> s.sumRange(3, 1));
		}
	}

	@RepeatedTest(50)
	void sumsMatchNaive(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final Numeric s = createNumeric();
		final List<Long> all = new ArrayList<>();
		for (int k = random.nextInt(1, 40); k > 0; k--) {
			final long v = random.nextInt(-20, 20);
			all.add(v);
			s.add(v);
		}
		Collections.sort(all);
		final List<Long> unique = all.stream().distinct().toList();
		final String at = "seed=" + seed + ", values=" + all;
		for (int i = 0; i < all.size(); i++) {
			assertEquals(all.subList(0, i + 1).stream().mapToLong(Long::longValue).sum(), s.prefixSum(i), at + ", i=" + i);
			assertEquals(all.subList(i, all.size()).stream().mapToLong(Long::longValue).sum(), s.suffixSum(i), at + ", i=" + i);
		}
		for (int i = 0; i < unique.size(); i++) {
			assertEquals(unique.subList(0, i + 1).stream().mapToLong(Long::longValue).sum(), s.prefixUniqueSum(i), at + ", i=" + i);
			assertEquals(unique.subList(i, unique.size()).stream().mapToLong(Long::longValue).sum(), s.suffixUniqueSum(i), at + ", i=" + i);
		}
	}
}
