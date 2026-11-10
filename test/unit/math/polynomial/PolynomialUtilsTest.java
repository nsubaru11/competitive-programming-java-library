package unit.math.polynomial;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.math.polynomial.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 係数は最高次の項から並べる。例えば {1, -3, 2} は x^2 - 3x + 2 を表す。
 */
final class PolynomialUtilsTest {

	private static final double EPS = 1e-9;

	@Nested
	class Arithmetic {

		@Test
		void addAndSubtractAlignLowestTerms() {
			assertArrayEquals(new double[]{1, 4, 6}, PolynomialUtils.add(new double[]{1, 2, 3}, new double[]{2, 3}), EPS);
			assertArrayEquals(new double[]{1, 0, 0}, PolynomialUtils.subtract(new double[]{1, 2, 3}, new double[]{2, 3}), EPS);
			assertArrayEquals(new double[]{-1, -2}, PolynomialUtils.subtract(new double[]{1}, new double[]{1, 3}), EPS);
		}

		@Test
		void multiply() {
			// (x - 1)(x - 2) = x^2 - 3x + 2
			assertArrayEquals(new double[]{1, -3, 2}, PolynomialUtils.multiply(new double[]{1, -1}, new double[]{1, -2}), EPS);
		}

		@Test
		void differentiateAndIntegrate() {
			assertArrayEquals(new double[]{3, 4, 1}, PolynomialUtils.differentiate(new double[]{1, 2, 1, 7}), EPS);
			assertArrayEquals(new double[0], PolynomialUtils.differentiate(new double[]{5}), EPS);
			assertArrayEquals(new double[]{1, 1, 1, 5}, PolynomialUtils.integrate(new double[]{3, 2, 1}, 5), EPS);
			final double[] p = {2, -1, 4};
			assertArrayEquals(p, PolynomialUtils.differentiate(PolynomialUtils.integrate(p, 9)), EPS);
		}
	}

	@Nested
	class Aggregation {

		@Test
		void evaluateAndDegree() {
			assertEquals(0.0, PolynomialUtils.evaluate(new double[]{1, -3, 2}, 2), EPS);
			assertEquals(6.0, PolynomialUtils.evaluate(new double[]{1, -3, 2}, 4), EPS);
			assertEquals(2, PolynomialUtils.degree(new double[]{1, -3, 2}));
			assertEquals(1, PolynomialUtils.degree(new double[]{0, 0, 3, 1}), "先頭の0は次数に数えない");
			assertEquals(-1, PolynomialUtils.degree(new double[]{0, 0}), "零多項式");
		}

		@Test
		void gcdHasCommonRoots() {
			// (x - 1)(x - 2) と (x - 1)(x + 3) の最大公約数は x - 1 の定数倍
			final double[] g = PolynomialUtils.gcd(new double[]{1, -3, 2}, new double[]{1, 2, -3});
			assertEquals(1, PolynomialUtils.degree(g));
			assertEquals(0.0, PolynomialUtils.evaluate(g, 1), EPS);
			assertNotEquals(0.0, PolynomialUtils.evaluate(g, 2), EPS);
		}
	}

	@RepeatedTest(50)
	void matchesNaive(final RepetitionInfo info) {
		final Random random = new Random(info.getCurrentRepetition());
		final double[] a = random.ints(1 + random.nextInt(6), -5, 6).asDoubleStream().toArray(), b = random.ints(1 + random.nextInt(6), -5, 6).asDoubleStream().toArray();
		final double x = random.nextInt(-3, 4);
		final double va = PolynomialUtils.evaluate(a, x), vb = PolynomialUtils.evaluate(b, x);
		double naive = 0;
		for (final double c : a) naive = naive * x + c;
		assertEquals(naive, va, EPS);
		assertEquals(va + vb, PolynomialUtils.evaluate(PolynomialUtils.add(a, b), x), EPS);
		assertEquals(va - vb, PolynomialUtils.evaluate(PolynomialUtils.subtract(a, b), x), EPS);
		assertEquals(va * vb, PolynomialUtils.evaluate(PolynomialUtils.multiply(a, b), x), EPS);
	}
}
