package unit.math;

import java.math.*;
import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import lib.math.*;

import static org.junit.jupiter.api.Assertions.*;

final class MathUtilsTest {

	private static final long MOD = 998244353;

	private static BigInteger binom(final int n, final int r) {
		BigInteger c = BigInteger.ONE;
		for (int i = 0; i < r; i++) c = c.multiply(BigInteger.valueOf(n - i)).divide(BigInteger.valueOf(i + 1));
		return c;
	}

	@Nested
	class MinMax {

		@Test
		void allOverloads() {
			assertEquals(1, MathUtils.min(3, 1));
			assertEquals(1L, MathUtils.min(3L, 1L));
			assertEquals(-2, MathUtils.min(3, 1, -2));
			assertEquals(-2L, MathUtils.min(3L, 1L, -2L));
			assertEquals(-5, MathUtils.min(4, 2, -5, 9));
			assertEquals(-5L, MathUtils.min(4L, 2L, -5L, 9L));
			assertEquals(3, MathUtils.max(3, 1));
			assertEquals(3L, MathUtils.max(3L, 1L));
			assertEquals(7, MathUtils.max(3, 7, -2));
			assertEquals(7L, MathUtils.max(3L, 7L, -2L));
			assertEquals(9, MathUtils.max(4, 2, -5, 9));
			assertEquals(9L, MathUtils.max(4L, 2L, -5L, 9L));
			assertEquals(5, MathUtils.diff(2, 7));
			assertEquals(5L, MathUtils.diff(7L, 2L));
		}
	}

	@Nested
	class Power {

		@Test
		void powAndModPow() {
			final Random random = new Random(1);
			for (int t = 0; t < 300; t++) {
				final long a = random.nextLong(-30, 30);
				final int b = random.nextInt(10);
				assertEquals(BigInteger.valueOf(a).pow(b).longValue(), MathUtils.pow(a, b), a + "^" + b);
				final long x = random.nextLong(0, MOD), e = random.nextLong(0, 1L << 40);
				final long expected = BigInteger.valueOf(x).modPow(BigInteger.valueOf(e), BigInteger.valueOf(MOD)).longValue();
				assertEquals(expected, MathUtils.modPow(x, e, MOD));
				final int ie = random.nextInt(Integer.MAX_VALUE);
				final long intExpected = BigInteger.valueOf(x).modPow(BigInteger.valueOf(ie), BigInteger.valueOf(MOD)).longValue();
				assertEquals((int) intExpected, MathUtils.modPow((int) x, ie, (int) MOD));
			}
			assertEquals(1, MathUtils.modPow(5, 0, 7));
			assertEquals(2, MathUtils.modPow(2, 10, 7));
			assertEquals(1L, MathUtils.modPow(0L, 0L, 7L));
		}

		@Test
		void modInvWithPrimeMod() {
			for (int a = 1; a < 50; a++) {
				assertEquals(1L, (long) a * MathUtils.modInv(a, (int) MOD) % MOD);
				assertEquals(1L, a * MathUtils.modInv((long) a, MOD) % MOD);
			}
		}
	}

	@Nested
	class Roots {

		@ParameterizedTest
		@ValueSource(longs = {0, 1, 2, 3, 4, 8, 9, 15, 16, 26, 27, 28, 63, 64, 99, 100, 2147395599L, 2147395600L, Integer.MAX_VALUE})
		void sqrtAndCbrtOfInt(final long v) {
			final int n = (int) v;
			final int fs = MathUtils.floorSqrt(n), cs = MathUtils.ceilSqrt(n);
			assertTrue((long) fs * fs <= n && (long) (fs + 1) * (fs + 1) > n);
			assertTrue((long) cs * cs >= n && (cs == 0 || (long) (cs - 1) * (cs - 1) < n));
			final int fc = MathUtils.floorCbrt(n), cc = MathUtils.ceilCbrt(n);
			assertTrue((long) fc * fc * fc <= n && (long) (fc + 1) * (fc + 1) * (fc + 1) > n);
			assertTrue((long) cc * cc * cc >= n && (cc == 0 || (long) (cc - 1) * (cc - 1) * (cc - 1) < n));
			assertEquals((long) fs * fs == n, MathUtils.isSquare(n));
			assertEquals((long) fc * fc * fc == n, MathUtils.isCube(n));
		}

		@ParameterizedTest
		@ValueSource(longs = {0, 1, 3, 4, 1L << 40, (1L << 40) - 1, 999999999999999999L, 1000000000000000000L, 3037000499L * 3037000499L, Long.MAX_VALUE})
		void sqrtAndCbrtOfLong(final long n) {
			final BigInteger big = BigInteger.valueOf(n);
			final long fs = MathUtils.floorSqrt(n), cs = MathUtils.ceilSqrt(n);
			assertEquals(big.sqrt().longValue(), fs);
			assertEquals(big.sqrt().pow(2).equals(big) ? fs : fs + 1, cs);
			final long fc = MathUtils.floorCbrt(n), cc = MathUtils.ceilCbrt(n);
			assertTrue(BigInteger.valueOf(fc).pow(3).compareTo(big) <= 0 && BigInteger.valueOf(fc + 1).pow(3).compareTo(big) > 0);
			assertEquals(BigInteger.valueOf(fc).pow(3).equals(big) ? fc : fc + 1, cc);
			assertEquals(big.sqrt().pow(2).equals(big), MathUtils.isSquare(n));
			assertEquals(BigInteger.valueOf(fc).pow(3).equals(big), MathUtils.isCube(n));
		}
	}

	@Nested
	class Logs {

		@Test
		void log2AndLog10() {
			assertEquals(0, MathUtils.floorLog2(0));
			assertEquals(0, MathUtils.ceilLog2(-3));
			assertEquals(0, MathUtils.floorLog10(0L));
			assertEquals(0, MathUtils.ceilLog10(-1L));
			for (long n = 1; n < 5000; n += n / 7 + 1) {
				int f2 = 0, c2 = 0, f10 = 0, c10 = 0;
				while (1L << f2 + 1 <= n) f2++;
				while (1L << c2 < n) c2++;
				for (long p = 10; p <= n; p *= 10) f10++;
				for (long p = 1; p < n; p *= 10) c10++;
				assertEquals(f2, MathUtils.floorLog2((int) n), "n=" + n);
				assertEquals(c2, MathUtils.ceilLog2((int) n), "n=" + n);
				assertEquals(f10, MathUtils.floorLog10((int) n), "n=" + n);
				assertEquals(c10, MathUtils.ceilLog10((int) n), "n=" + n);
				assertEquals(f2, MathUtils.floorLog2(n));
				assertEquals(c2, MathUtils.ceilLog2(n));
				assertEquals(f10, MathUtils.floorLog10(n));
				assertEquals(c10, MathUtils.ceilLog10(n));
			}
			assertEquals(18, MathUtils.floorLog10(Long.MAX_VALUE));
			assertEquals(19, MathUtils.ceilLog10(Long.MAX_VALUE));
			assertEquals(62, MathUtils.floorLog2(Long.MAX_VALUE));
			assertEquals(63, MathUtils.ceilLog2(Long.MAX_VALUE));
		}
	}

	@Nested
	class GcdLcm {

		@Test
		void matchBigInteger() {
			final Random random = new Random(2);
			for (int t = 0; t < 500; t++) {
				final long a = random.nextLong(0, 1L << 40), b = random.nextLong(0, 1L << 40);
				final long g = BigInteger.valueOf(a).gcd(BigInteger.valueOf(b)).longValue();
				assertEquals(g, MathUtils.gcd(a, b));
				final int ia = random.nextInt(0, 100000), ib = random.nextInt(0, 100000);
				final int ig = BigInteger.valueOf(ia).gcd(BigInteger.valueOf(ib)).intValue();
				assertEquals(ig, MathUtils.gcd(ia, ib));
				if (ia > 0 && ib > 0) {
					assertEquals((long) ia / ig * ib, MathUtils.lcm((long) ia, ib));
					if ((long) ia / ig * ib <= Integer.MAX_VALUE) assertEquals(ia / ig * ib, MathUtils.lcm(ia, ib));
				}
			}
			assertEquals(5, MathUtils.gcd(0, 5));
			assertEquals(0L, MathUtils.gcd(0L, 0L));
		}

		@Test
		void exgcd() {
			final Random random = new Random(3);
			for (int t = 0; t < 300; t++) {
				final int a = random.nextInt(1, 100000), b = random.nextInt(1, 100000);
				final int[] x = new int[1], y = new int[1];
				final int g = MathUtils.exgcd(a, b, x, y);
				assertEquals(MathUtils.gcd(a, b), g);
				assertEquals(g, (long) a * x[0] + (long) b * y[0]);
				final long[] lx = new long[1], ly = new long[1];
				final long lg = MathUtils.exgcd((long) a * 7919, (long) b, lx, ly);
				assertEquals(MathUtils.gcd((long) a * 7919, b), lg);
				assertEquals(BigInteger.valueOf(lg), BigInteger.valueOf((long) a * 7919).multiply(BigInteger.valueOf(lx[0])).add(BigInteger.valueOf(b).multiply(BigInteger.valueOf(ly[0]))));
			}
		}
	}

	@Nested
	class Factorials {

		@Test
		void factAndModFact() {
			BigInteger f = BigInteger.ONE;
			for (int n = 0; n <= 20; n++) {
				if (n > 0) f = f.multiply(BigInteger.valueOf(n));
				assertEquals(f.longValue(), MathUtils.fact(n), "n=" + n);
			}
			long mf = 1;
			for (int n = 0; n < 200; n++) {
				if (n > 0) mf = mf * n % MOD;
				assertEquals(mf, MathUtils.modFact(n, MOD));
				assertEquals((int) mf, MathUtils.modFact(n, (int) MOD));
				assertEquals(1L, mf * MathUtils.invFact(n, MOD) % MOD);
				assertEquals(1L, mf * MathUtils.invFact(n, (int) MOD) % MOD);
			}
		}

		@Test
		void primeExponentIsLegendre() {
			for (int n = 0; n < 300; n++) {
				for (final int p : new int[]{2, 3, 5, 7, 13}) {
					int expected = 0;
					for (int k = 1; k <= n; k++) for (int x = k; x % p == 0; x /= p) expected++;
					assertEquals(expected, MathUtils.primeExponent(n, p), "n=" + n + ", p=" + p);
					assertEquals(expected, MathUtils.primeExponent((long) n, p));
				}
			}
		}
	}

	@Nested
	class Combinatorics {

		@Test
		void nCrNPrNHr() {
			for (int n = 0; n <= 30; n++) {
				for (int r = 0; r <= n; r++) {
					final BigInteger c = binom(n, r);
					assertEquals(c.longValue(), MathUtils.nCr(n, r), n + "C" + r);
					assertEquals(c.mod(BigInteger.valueOf(MOD)).longValue(), MathUtils.nCr(n, r, MOD));
					BigInteger p = BigInteger.ONE;
					for (int i = 0; i < r; i++) p = p.multiply(BigInteger.valueOf(n - i));
					if (p.bitLength() < 63) assertEquals(p.longValue(), MathUtils.nPr(n, r), n + "P" + r);
					assertEquals(p.mod(BigInteger.valueOf(MOD)).longValue(), MathUtils.nPr(n, r, MOD));
				}
				for (int r = 0; r <= 10 && n > 0; r++) {
					final BigInteger h = binom(n + r - 1, r);
					assertEquals(h.longValue(), MathUtils.nHr(n, r), n + "H" + r);
					assertEquals(h.mod(BigInteger.valueOf(MOD)).longValue(), MathUtils.nHr(n, r, MOD));
				}
			}
			assertEquals(0L, MathUtils.nCr(3, 5));
		}

		@Test
		void stirlingAndBell() {
			final long[][] s = new long[16][16];
			s[0][0] = 1;
			for (int n = 1; n < 16; n++) for (int k = 1; k <= n; k++) s[n][k] = k * s[n - 1][k] + s[n - 1][k - 1];
			for (int n = 0; n < 16; n++) {
				long bell = 0;
				for (int k = 0; k <= n; k++) {
					assertEquals(s[n][k], MathUtils.stirlingNumber2(n, k), "S(" + n + ", " + k + ")");
					bell += s[n][k];
				}
				assertEquals(bell, MathUtils.bellNumber(n), "B(" + n + ")");
			}
		}
	}

	@Nested
	class Primes {

		@Test
		void isPrimeMatchesBigInteger() {
			for (long n = 0; n < 2000; n++) {
				final boolean expected = n >= 2 && BigInteger.valueOf(n).isProbablePrime(50);
				assertEquals(expected, MathUtils.isPrime(n), "n=" + n);
				assertEquals(expected, MathUtils.isProbablePrime(n, 20), "n=" + n);
			}
			for (final long n : new long[]{998244353L, 1000000007L, 1000000007L * 998244353L, 2305843009213693951L, 3215031751L}) {
				assertEquals(BigInteger.valueOf(n).isProbablePrime(50), MathUtils.isPrime(n), "n=" + n);
			}
		}

		@Test
		void eulerTotient() {
			for (int n = 1; n < 1000; n++) {
				int expected = 0;
				for (int k = 1; k <= n; k++)
					if (BigInteger.valueOf(k).gcd(BigInteger.valueOf(n)).intValue() == 1) expected++;
				assertEquals(expected, MathUtils.eulerTotient(n), "n=" + n);
				assertEquals(expected, MathUtils.eulerTotient((long) n));
			}
			assertEquals(1000000006L * 998244352L, MathUtils.eulerTotient(1000000007L * 998244353L));
		}
	}
}
