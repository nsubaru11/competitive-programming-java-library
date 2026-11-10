package unit.math;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.math.*;

import static org.junit.jupiter.api.Assertions.*;

final class NumberPredicatesTest {

	@Nested
	class Search {

		@Test
		void perfectNumbers() {
			final Set<Long> perfect = Set.of(6L, 28L, 496L, 8128L);
			for (long n = -2; n <= 10000; n++) {
				long sum = 0;
				for (long d = 1; d < n; d++) if (n % d == 0) sum += d;
				assertEquals(n > 1 && sum == n, NumberPredicates.isPerfectNumber(n), "n=" + n);
				assertEquals(perfect.contains(n), NumberPredicates.isPerfectNumber(n), "n=" + n);
			}
			assertTrue(NumberPredicates.isPerfectNumber(33550336L));
		}

		@Test
		void palindromes() {
			for (long n = -5; n <= 20000; n++) {
				final String s = Long.toString(n);
				assertEquals(n >= 0 && new StringBuilder(s).reverse().toString().equals(s), NumberPredicates.isPalindrome(n), "n=" + n);
			}
		}

		@Test
		void fibonacci() {
			final Set<Long> fib = new HashSet<>();
			for (long a = 0, b = 1; a <= 1_358_187_913L; ) {
				fib.add(a);
				final long c = a + b;
				a = b;
				b = c;
			}
			for (long n = 0; n <= 5000; n++) assertEquals(fib.contains(n), NumberPredicates.isFibonacci(n), "n=" + n);
			assertTrue(NumberPredicates.isFibonacci(1_134_903_170L));
			assertFalse(NumberPredicates.isFibonacci(1_134_903_171L));
		}

		@Test
		void armstrongNumbers() {
			for (long n = -1; n <= 100000; n++) {
				final String s = Long.toString(n);
				long sum = 0;
				for (final char c : s.toCharArray()) if (c != '-') sum += (long) Math.pow(c - '0', s.length());
				assertEquals(n >= 0 && sum == n, NumberPredicates.isArmstrong(n), "n=" + n);
			}
		}

		@Test
		void happyNumbers() {
			for (long n = -1; n <= 2000; n++) {
				long x = n;
				final Set<Long> seen = new HashSet<>();
				while (x > 1 && seen.add(x)) {
					long s = 0;
					for (long y = x; y > 0; y /= 10) s += (y % 10) * (y % 10);
					x = s;
				}
				assertEquals(n > 0 && x == 1, NumberPredicates.isHappyNumber(n), "n=" + n);
			}
		}
	}
}
