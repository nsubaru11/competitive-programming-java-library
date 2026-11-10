package unit.math.number;

import java.math.*;
import java.util.*;

import org.junit.jupiter.api.*;

import lib.math.number.*;

import static org.junit.jupiter.api.Assertions.*;

final class FractionTest {

	private static Fraction reduce(final long n, final long d) {
		final long g = BigInteger.valueOf(n).gcd(BigInteger.valueOf(d)).longValue();
		final Fraction f = new Fraction(n, d);
		assertEquals(Math.abs(d / g), f.denominator, "既約分数");
		return f;
	}

	@Nested
	class Construction {

		@Test
		void reducesAndNormalizesSign() {
			final Fraction f = new Fraction(6, -4);
			assertEquals(-3L, f.numerator);
			assertEquals(2L, f.denominator);
			assertEquals(-3L, f.getNumerator());
			assertEquals(2L, f.getDenominator());
			assertEquals("-3/2", f.toString());
			assertEquals("5", new Fraction(5).toString());
			assertEquals("0", new Fraction(0, 7).toString());
			assertThrows(ArithmeticException.class, () -> new Fraction(1, 0));
		}

		@Test
		void copy() {
			final Fraction f = new Fraction(2, 3), c = new Fraction(f);
			c.pow(2);
			assertEquals(new Fraction(2, 3), f);
			assertEquals(new Fraction(4, 9), c);
		}
	}

	@Nested
	class Arithmetic {

		@Test
		void examples() {
			final Fraction a = new Fraction(1, 2), b = new Fraction(1, 3);
			assertEquals(new Fraction(5, 6), a.add(b));
			assertEquals(new Fraction(1, 6), a.subtract(b));
			assertEquals(new Fraction(1, 6), a.multiply(b));
			assertEquals(new Fraction(3, 2), a.divide(b));
			assertEquals(new Fraction(5, 2), a.add(2));
			assertEquals(new Fraction(-3, 2), a.subtract(2));
			assertEquals(new Fraction(3, 2), a.multiply(3));
			assertEquals(new Fraction(1, 8), a.divide(4));
			assertEquals(new Fraction(2, 1), a.getInverse());
			assertEquals(new Fraction(1, 2), a, "演算は元の値を変えない");
		}

		@Test
		void powIsInPlace() {
			final Fraction f = new Fraction(-2, 3);
			f.pow(3);
			assertEquals(new Fraction(-8, 27), f);
			f.pow(0);
			assertEquals(new Fraction(1), f);
		}

		@Test
		void matchesBigIntegerRationals() {
			final Random random = new Random(1);
			for (int t = 0; t < 500; t++) {
				final long an = random.nextLong(-1000, 1000), ad = random.nextLong(1, 1000), bn = random.nextLong(-1000, 1000), bd = random.nextLong(1, 1000);
				final Fraction a = new Fraction(an, ad), b = new Fraction(bn, bd);
				final String at = an + "/" + ad + ", " + bn + "/" + bd;
				assertEquals(reduce(an * bd + bn * ad, ad * bd), a.add(b), at);
				assertEquals(reduce(an * bd - bn * ad, ad * bd), a.subtract(b), at);
				assertEquals(reduce(an * bn, ad * bd), a.multiply(b), at);
				if (bn != 0) assertEquals(reduce(an * bd, ad * bn), a.divide(b), at);
			}
		}
	}

	@Nested
	class Conversion {

		@Test
		void decimalAndEquality() {
			assertEquals(String.format("%.5f", 1.0 / 3), new Fraction(1, 3).getDecimal());
			assertEquals(new Fraction(0, 3), new Fraction(0, -5), "0 はすべて等しい");
			assertNotEquals(new Fraction(1, 2), new Fraction(1, 3));
			assertNotEquals(new Fraction(1, 2), "1/2");
		}
	}
}
