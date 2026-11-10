package unit.math;

import java.math.*;
import java.util.*;

import org.junit.jupiter.api.*;

import lib.math.*;

import static org.junit.jupiter.api.Assertions.*;

final class PrimeUtilsTest {

	private static boolean naive(final long n) {
		if (n < 2) return false;
		for (long d = 2; d * d <= n; d++) if (n % d == 0) return false;
		return true;
	}

	@Nested
	class Search {

		@Test
		void isPrimeAndProbablePrime() {
			for (long n = 0; n <= 5000; n++) {
				assertEquals(naive(n), PrimeUtils.isPrime(n), "n=" + n);
				assertEquals(naive(n), PrimeUtils.isProbablePrime(n, 30), "n=" + n);
			}
			assertTrue(PrimeUtils.isPrime(1_000_000_007L));
			assertFalse(PrimeUtils.isPrime(1_000_000_007L * 3));
		}
	}

	@Nested
	class Aggregation {

		@Test
		void primeCount() {
			for (int n = 0; n <= 1000; n++) {
				int expected = 0;
				for (int k = 2; k <= n; k++) if (naive(k)) expected++;
				assertEquals(expected, PrimeUtils.primeCount(n), "n=" + n);
			}
			assertEquals(78498, PrimeUtils.primeCount(1_000_000));
		}

		@Test
		void primeCountInRange() {
			final Random random = new Random(1);
			for (int t = 0; t < 300; t++) {
				final int min = random.nextInt(0, 500), max = random.nextInt(0, 600);
				int expected = 0;
				for (int k = min; k <= max; k++) if (naive(k)) expected++;
				assertEquals(expected, PrimeUtils.primeCount(min, max), "min=" + min + ", max=" + max);
			}
		}
	}

	@Nested
	class Conversion {

		@Test
		void eratosthenes() {
			final Random random = new Random(2);
			for (int t = 0; t < 300; t++) {
				final int min = random.nextInt(0, 500), max = random.nextInt(0, 600);
				final List<Integer> expected = new ArrayList<>();
				for (int k = min; k <= max; k++) if (naive(k)) expected.add(k);
				assertArrayEquals(expected.stream().mapToInt(Integer::intValue).toArray(), PrimeUtils.eratosthenes(min, max), "min=" + min + ", max=" + max);
				assertEquals(new TreeSet<>(expected), PrimeUtils.eratosthenes(min, max, TreeSet::new));
			}
		}
	}
}
