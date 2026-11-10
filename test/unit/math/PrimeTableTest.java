package unit.math;

import java.util.*;
import java.util.function.*;

import org.junit.jupiter.api.*;

import lib.math.*;

import static org.junit.jupiter.api.Assertions.*;

final class PrimeTableTest {

	private static final int LIMIT = 1_000_000;
	private static PrimeTable table;

	@BeforeAll
	static void build() {
		table = new PrimeTable(LIMIT);
	}

	@Nested
	class State {

		@Test
		void limitAndCount() {
			assertEquals(LIMIT, table.getLimitValue());
			assertEquals(78_498, table.getPrimeCount());
			final PrimeTable small = new PrimeTable(40);
			assertEquals(40, small.getLimitValue());
			assertEquals(12, small.getPrimeCount());
		}
	}

	@Nested
	class Access {

		@Test
		void isPrimeAndGet() {
			for (int n = 0; n <= 10_000; n++) assertEquals(isPrimeNaive(n), table.isPrime(n), "n=" + n);
			assertTrue(table.isPrime(999_983));
			assertEquals(2, table.get(0));
			assertEquals(37, new PrimeTable(40).get(11));
			assertEquals(999_983, table.get(table.getPrimeCount() - 1));
		}
	}

	@Nested
	class Search {

		@Test
		void neighborsAndCount() {
			final PrimeTable small = new PrimeTable(40);
			assertEquals(12, small.countPrimesUpTo(40));
			assertEquals(4, small.countPrimesUpTo(10));
			assertEquals(0, small.countPrimesUpTo(1));
			assertEquals(11, small.ceilingPrime(11));
			assertEquals(13, small.higherPrime(11));
			assertEquals(11, small.floorPrime(12));
			assertEquals(7, small.lowerPrime(11));
			assertEquals(-1, small.lowerPrime(2), "存在しなければ -1");
			assertEquals(-1, small.floorPrime(1));
			assertEquals(-1, small.higherPrime(37));
			assertEquals(-1, small.ceilingPrime(38));
		}

		@Test
		void matchesNaive() {
			final PrimeTable small = new PrimeTable(500);
			for (int n = 0; n <= 500; n++) {
				int count = 0, ceiling = -1, higher = -1, floor = -1, lower = -1;
				for (int k = 2; k <= 500; k++) {
					if (!isPrimeNaive(k)) continue;
					if (k <= n) count++;
					if (k >= n && ceiling == -1) ceiling = k;
					if (k > n && higher == -1) higher = k;
					if (k <= n) floor = k;
					if (k < n) lower = k;
				}
				assertEquals(count, small.countPrimesUpTo(n), "n=" + n);
				assertEquals(ceiling, small.ceilingPrime(n), "n=" + n);
				// 構築上限を超える値は探さない
				if (n < 500) assertEquals(higher, small.higherPrime(n), "n=" + n);
				assertEquals(floor, small.floorPrime(n), "n=" + n);
				assertEquals(lower, small.lowerPrime(n), "n=" + n);
			}
		}
	}

	@Nested
	class Conversion {

		// 360 = 2^3 * 3^2 * 5
		@Test
		void factorization() {
			assertEquals(3, table.uniquePrimeFactorCount(360));
			assertEquals(6, table.primeFactorCount(360));
			assertArrayEquals(new int[]{2, 2, 2, 3, 3, 5}, table.primeFactors(360));
			assertArrayEquals(new long[]{2, 2, 2, 3, 3, 5}, table.primeFactors(360L));
			assertArrayEquals(new int[][]{{2, 3, 5}, {3, 2, 1}}, table.primeFactors2D(360));
			assertArrayEquals(new long[][]{{2, 3, 5}, {3, 2, 1}}, table.primeFactors2D(360L));
			assertEquals(Map.of(2, 3, 3, 2, 5, 1), table.<LinkedHashMap<Integer, Integer>>primeFactorsMap(360, LinkedHashMap::new));
			assertEquals(Map.of(2L, 3, 3L, 2, 5L, 1), table.primeFactorsMap(360L, LinkedHashMap::new));
			assertEquals(Map.of(2, 3, 3, 2, 5, 1), table.primeFactorsMap(360, (Supplier<HashMap<Integer, Integer>>) HashMap::new));
			assertEquals(List.of(2, 2, 2, 3, 3, 5), table.<ArrayList<Integer>>primeFactors(360, ArrayList::new));
			assertEquals(List.of(2L, 2L, 2L, 3L, 3L, 5L), table.primeFactors(360L, ArrayList::new));
		}

		// 構築上限を超える long も、上限が平方根以上なら分解できる（sqrt(600851475143) ≒ 775146）
		@Test
		void factorizeBeyondSieve() {
			assertArrayEquals(new long[]{71, 839, 1471, 6857}, table.primeFactors(600_851_475_143L));
			assertEquals(4, table.uniquePrimeFactorCount(600_851_475_143L));
		}

		@Test
		void numbersWithoutPrimeFactors() {
			assertEquals(0, table.uniquePrimeFactorCount(1));
			assertEquals(0, table.primeFactorCount(0));
			assertEquals(2, table.primeFactors2D(1).length);
			assertEquals(0, table.primeFactors2D(1)[0].length);
			assertEquals(2, table.primeFactors2D(1L).length);
			assertEquals(0, table.primeFactors(-1).length);
			assertEquals(0, table.primeFactors(1L).length);
			assertTrue(table.<ArrayList<Integer>>primeFactors(1, ArrayList::new).isEmpty());
			assertTrue(table.<HashMap<Integer, Integer>>primeFactorsMap(-1, HashMap::new).isEmpty());
		}

		@Test
		void factorizationMatchesNaive() {
			final Random random = new Random(1);
			for (int t = 0; t < 2000; t++) {
				final long n = t < 1000 ? t + 2 : random.nextLong(2, 10_000_000_000L);
				final String at = "n=" + n;
				assertArrayEquals(FactorUtilsTest.naiveFactors(n).stream().mapToLong(Long::longValue).toArray(), table.primeFactors(n), at);
				assertArrayEquals(FactorUtilsTest.naive2D(n), table.primeFactors2D(n), at);
			}
		}

		@Test
		void iteratorAndStream() {
			final PrimeTable small = new PrimeTable(30);
			final List<Long> it = new ArrayList<>();
			small.iterator().forEachRemaining((LongConsumer) it::add);
			assertEquals(List.of(2L, 3L, 5L, 7L, 11L, 13L, 17L, 19L, 23L, 29L), it);
			assertArrayEquals(new long[]{2, 3, 5, 7, 11, 13, 17, 19, 23, 29}, small.stream().toArray());
			final PrimitiveIterator.OfLong iterator = small.iterator();
			for (int i = 0; i < 10; i++) iterator.nextLong();
			assertFalse(iterator.hasNext());
			assertThrows(NoSuchElementException.class, iterator::nextLong);
		}
	}

	private static boolean isPrimeNaive(final long n) {
		if (n < 2) return false;
		for (long d = 2; d * d <= n; d++) if (n % d == 0) return false;
		return true;
	}
}
