package unit.math;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.math.*;

import static org.junit.jupiter.api.Assertions.*;

final class FactorUtilsTest {

	// 360 = 2^3 * 3^2 * 5
	@Test
	void counts() {
		assertEquals(3, FactorUtils.uniquePrimeFactorCount(360));
		assertEquals(6, FactorUtils.primeFactorCount(360));
		assertEquals(24, FactorUtils.divisorCount(360));
	}

	@Test
	void primeFactors() {
		assertArrayEquals(new int[]{2, 2, 2, 3, 3, 5}, FactorUtils.primeFactors(360));
		assertArrayEquals(new int[][]{{2, 3, 5}, {3, 2, 1}}, FactorUtils.primeFactors2D(360));
		assertArrayEquals(new long[][]{{2, 3, 5}, {3, 2, 1}}, FactorUtils.primeFactors2D(360L));
	}

	@Test
	void divisors() {
		assertArrayEquals(new int[]{1, 2, 3, 4, 6, 9, 12, 18, 36}, FactorUtils.divisors(36));
		assertEquals(List.of(1, 2, 3, 4, 6, 12), FactorUtils.<ArrayList<Integer>>divisors(12, ArrayList::new));
	}

	// 愚直解との照合: 試し割りで数えた約数と比べる
	@Test
	void divisorsMatchNaive() {
		for (int n = 1; n <= 2000; n++) {
			final ArrayList<Integer> expected = new ArrayList<>();
			for (int d = 1; d <= n; d++) if (n % d == 0) expected.add(d);
			assertArrayEquals(expected.stream().mapToInt(Integer::intValue).toArray(), FactorUtils.divisors(n), "n=" + n);
			assertEquals(expected.size(), FactorUtils.divisorCount(n), "n=" + n);
		}
	}
}
