package unit.math;

import java.math.*;

import org.junit.jupiter.api.*;

import lib.math.*;

import static org.junit.jupiter.api.Assertions.*;

final class FactorialTableTest {

	private static final int MOD = 998244353;
	private static final BigInteger BIG_MOD = BigInteger.valueOf(MOD);

	@Nested
	class Construction {

		@Test
		void defaultModAndGrowth() {
			assertEquals(MOD, new FactorialTable().mod);
			assertEquals(MOD, new FactorialTable(10).mod);
			final FactorialTable small = new FactorialTable(0, MOD);
			// 容量を超える添字でも拡張して求める
			long expected = 1;
			for (int i = 0; i <= 5000; i++) {
				if (i > 0) expected = expected * i % MOD;
				assertEquals(expected, small.fact(i), "i=" + i);
				assertEquals(1L, (long) small.fact(i) * small.invFact(i) % MOD, "i=" + i);
				if (i > 0) assertEquals(1L, (long) i * small.inv(i) % MOD, "i=" + i);
			}
		}

		@Test
		void smallPrimeMod() {
			final FactorialTable t = new FactorialTable(20, 7);
			assertEquals(7, t.mod);
			assertEquals(6, t.fact(3));
			assertEquals(0, t.fact(7), "n >= mod では0");
			assertEquals(0, t.fact(10));
			assertEquals(15 % 7, t.nCr(6, 2));
		}
	}

	@Nested
	class Combinatorics {

		@Test
		void nCrNPrNHr() {
			final FactorialTable t = new FactorialTable();
			for (int n = 0; n <= 40; n++) {
				for (int r = 0; r <= n; r++) {
					assertEquals(mod(binom(n, r)), t.nCr(n, r), n + "C" + r);
					BigInteger p = BigInteger.ONE;
					for (int i = 0; i < r; i++) p = p.multiply(BigInteger.valueOf(n - i));
					assertEquals(mod(p), t.nPr(n, r), n + "P" + r);
					if (n > 0) assertEquals(mod(binom(n + r - 1, r)), t.nHr(n, r), n + "H" + r);
				}
			}
		}

		@Test
		void namedNumbers() {
			final FactorialTable t = new FactorialTable();
			final long[] catalan = {1, 1, 2, 5, 14, 42, 132, 429, 1430, 4862};
			for (int n = 0; n < catalan.length; n++) assertEquals(catalan[n], t.catalan(n), "C_" + n);
			for (int n = 1; n <= 12; n++) {
				for (int k = 1; k <= n; k++) {
					// Lah 数 L(n, k) = C(n - 1, k - 1) * n! / k!
					assertEquals(mod(binom(n - 1, k - 1).multiply(fact(n)).divide(fact(k))), t.lah(n, k), "L(" + n + ", " + k + ")");
					// ナラヤナ数 N(n, k) = C(n, k) * C(n, k - 1) / n
					assertEquals(mod(binom(n, k).multiply(binom(n, k - 1)).divide(BigInteger.valueOf(n))), t.narayana(n, k), "N(" + n + ", " + k + ")");
				}
			}
			for (int n = 0; n <= 12; n++) {
				for (int k = 0; k <= n; k++) {
					// 対角線を越えない経路数 C(n + k, k) - C(n + k, k - 1)
					final BigInteger ballot = binom(n + k, k).subtract(k == 0 ? BigInteger.ZERO : binom(n + k, k - 1));
					assertEquals(mod(ballot), t.ballotTheorem(n, k), "(" + n + ", " + k + ")");
				}
			}
		}
	}

	private static long mod(final BigInteger v) {
		return v.mod(BIG_MOD).longValue();
	}

	private static BigInteger binom(final int n, final int r) {
		BigInteger c = BigInteger.ONE;
		for (int i = 0; i < r; i++) c = c.multiply(BigInteger.valueOf(n - i)).divide(BigInteger.valueOf(i + 1));
		return c;
	}

	private static BigInteger fact(final int n) {
		BigInteger f = BigInteger.ONE;
		for (int i = 2; i <= n; i++) f = f.multiply(BigInteger.valueOf(i));
		return f;
	}
}
