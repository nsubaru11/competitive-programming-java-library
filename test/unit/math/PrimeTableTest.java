package unit.math;

import java.util.*;
import java.util.function.*;

import org.junit.jupiter.api.*;

import lib.math.*;

import static org.junit.jupiter.api.Assertions.*;

final class PrimeTableTest {

	private static PrimeTable table;

	@BeforeAll
	static void build() {
		table = new PrimeTable(1_000_000);
	}

	@Test
	void smallTable() {
		final PrimeTable small = new PrimeTable(40);
		assertEquals(12, small.countPrimesUpTo(40));
		assertEquals(37, small.get(11));
		assertTrue(small.isPrime(37));
	}

	@Test
	void countAndLargestPrime() {
		assertEquals(78_498, table.countPrimesUpTo(1_000_000));
		assertTrue(table.isPrime(999_983));
	}

	// 愚直解との照合: 試し割りによる素数判定と比べる
	@Test
	void isPrimeMatchesTrialDivision() {
		for (int n = 0; n <= 10_000; n++) assertEquals(isPrimeNaive(n), table.isPrime(n), "n=" + n);
	}

	// 360 = 2^3 * 3^2 * 5
	@Test
	void factorCounts() {
		assertEquals(3, table.uniquePrimeFactorCount(360));
		assertEquals(6, table.primeFactorCount(360));
	}

	@Test
	void primeFactorArrays() {
		assertArrayEquals(new int[]{2, 2, 2, 3, 3, 5}, table.primeFactors(360));
		assertArrayEquals(new long[]{2, 2, 2, 3, 3, 5}, table.primeFactors(360L));
		assertArrayEquals(new int[][]{{2, 3, 5}, {3, 2, 1}}, table.primeFactors2D(360));
		assertArrayEquals(new long[][]{{2, 3, 5}, {3, 2, 1}}, table.primeFactors2D(360L));
	}

	@Test
	void primeFactorCollections() {
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
	}

	@Test
	void numbersWithoutPrimeFactors() {
		assertEquals(2, table.primeFactors2D(1).length);
		assertEquals(0, table.primeFactors2D(1)[0].length);
		assertEquals(0, table.primeFactors(-1).length);
		assertTrue(table.<HashMap<Integer, Integer>>primeFactorsMap(-1, HashMap::new).isEmpty());
	}

	private static boolean isPrimeNaive(final int n) {
		if (n < 2) return false;
		for (int d = 2; d * d <= n; d++) if (n % d == 0) return false;
		return true;
	}
}
