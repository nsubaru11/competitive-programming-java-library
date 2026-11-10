package unit.math;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.math.*;

import static org.junit.jupiter.api.Assertions.*;

final class FactorUtilsTest {

	/**
	 * 試し割りによる素因数（重複込み・昇順）。
	 */
	static List<Long> naiveFactors(long n) {
		final List<Long> res = new ArrayList<>();
		for (long d = 2; d * d <= n; d++) {
			while (n % d == 0) {
				res.add(d);
				n /= d;
			}
		}
		if (n > 1) res.add(n);
		return res;
	}

	static long[][] naive2D(final long n) {
		final TreeMap<Long, Long> m = new TreeMap<>();
		for (final long p : naiveFactors(n)) m.merge(p, 1L, Long::sum);
		return new long[][]{m.keySet().stream().mapToLong(Long::longValue).toArray(), m.values().stream().mapToLong(Long::longValue).toArray()};
	}

	@Test
	void matchesNaive() {
		final Random random = new Random(1);
		for (int t = 0; t < 3000; t++) {
			final long n = t < 2000 ? t + 1 : random.nextLong(2, 1L << 34);
			final List<Long> f = naiveFactors(n);
			final String at = "n=" + n;
			assertArrayEquals(f.stream().mapToLong(Long::longValue).toArray(), FactorUtils.primeFactors(n), at);
			assertArrayEquals(naive2D(n), FactorUtils.primeFactors2D(n), at);
			assertEquals(f.size(), FactorUtils.primeFactorCount(n), at);
			assertEquals(f.stream().distinct().count(), FactorUtils.uniquePrimeFactorCount(n), at);
			long divisorCount = 1;
			for (final long e : naive2D(n)[1]) divisorCount *= e + 1;
			assertEquals(divisorCount, FactorUtils.divisorCount(n), at);
			if (n <= 2000) {
				final int m = (int) n;
				assertArrayEquals(f.stream().mapToInt(Long::intValue).toArray(), FactorUtils.primeFactors(m), at);
				final long[] d = FactorUtils.divisors(n);
				assertEquals(divisorCount, d.length, at);
				for (int i = 0; i < d.length; i++) {
					assertEquals(0, n % d[i], at);
					if (i > 0) assertTrue(d[i - 1] < d[i], at);
				}
				assertArrayEquals(Arrays.stream(d).mapToInt(x -> (int) x).toArray(), FactorUtils.divisors(m), at);
			}
		}
	}

	@Nested
	class Aggregation {

		// 360 = 2^3 * 3^2 * 5
		@Test
		void counts() {
			assertEquals(3, FactorUtils.uniquePrimeFactorCount(360));
			assertEquals(6, FactorUtils.primeFactorCount(360));
			assertEquals(24, FactorUtils.divisorCount(360));
			assertEquals(0, FactorUtils.uniquePrimeFactorCount(1));
			assertEquals(0, FactorUtils.primeFactorCount(0));
			assertEquals(0, FactorUtils.divisorCount(0));
		}
	}

	@Nested
	class Conversion {

		@Test
		void factorizationOverloadsAgree() {
			assertArrayEquals(new int[]{2, 2, 2, 3, 3, 5}, FactorUtils.primeFactors(360));
			assertArrayEquals(new long[]{2, 2, 2, 3, 3, 5}, FactorUtils.primeFactors(360L));
			assertEquals(List.of(2, 2, 2, 3, 3, 5), FactorUtils.<ArrayList<Integer>>primeFactors(360, ArrayList::new));
			assertEquals(List.of(2L, 2L, 2L, 3L, 3L, 5L), FactorUtils.primeFactors(360L, ArrayList::new));
			assertArrayEquals(new int[][]{{2, 3, 5}, {3, 2, 1}}, FactorUtils.primeFactors2D(360));
			assertArrayEquals(new long[][]{{2, 3, 5}, {3, 2, 1}}, FactorUtils.primeFactors2D(360L));
			assertEquals(Map.of(2, 3, 3, 2, 5, 1), FactorUtils.<HashMap<Integer, Integer>>primeFactorsMap(360, HashMap::new));
			assertEquals(Map.of(2L, 3, 3L, 2, 5L, 1), FactorUtils.primeFactorsMap(360L, HashMap::new));
		}

		@Test
		void oneHasNoFactors() {
			assertArrayEquals(new int[0], FactorUtils.primeFactors(1));
			assertArrayEquals(new long[0], FactorUtils.primeFactors(0L));
			assertTrue(FactorUtils.<ArrayList<Integer>>primeFactors(1, ArrayList::new).isEmpty());
			assertTrue(FactorUtils.primeFactorsMap(1L, HashMap::new).isEmpty());
			assertTrue(FactorUtils.primeFactors2D(1).length == 0 || FactorUtils.primeFactors2D(1)[0].length == 0, "要素数0の配列");
			assertTrue(FactorUtils.primeFactors2D(1L).length == 0 || FactorUtils.primeFactors2D(1L)[0].length == 0);
		}

		@Test
		void divisorsOverloadsAgree() {
			assertArrayEquals(new int[]{1, 2, 3, 4, 6, 9, 12, 18, 36}, FactorUtils.divisors(36));
			assertArrayEquals(new long[]{1, 2, 3, 6}, FactorUtils.divisors(6L));
			assertEquals(List.of(1, 2, 3, 4, 6, 12), FactorUtils.<ArrayList<Integer>>divisors(12, ArrayList::new));
			assertEquals(List.of(1L, 7L), FactorUtils.divisors(7L, ArrayList::new));
		}
	}
}
