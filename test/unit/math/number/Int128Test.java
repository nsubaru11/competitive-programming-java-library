package unit.math.number;

import java.math.*;
import java.util.*;

import org.junit.jupiter.api.*;

import lib.math.number.*;

import static org.junit.jupiter.api.Assertions.*;

final class Int128Test {

	private static final BigInteger TWO_64 = BigInteger.ONE.shiftLeft(64);

	private static Int128 of(final BigInteger v) {
		return Int128.of(v.toString());
	}

	private static BigInteger big(final Int128 v) {
		return new BigInteger(v.toString());
	}

	@Nested
	class Construction {

		@Test
		void fromPartsAndStrings() {
			assertEquals("0", new Int128().toString());
			assertEquals("123", new Int128(123).toString());
			assertEquals("123", Int128.of(123).toString());
			assertEquals(TWO_64.add(BigInteger.valueOf(5)).toString(), Int128.of(1, 5).toString(), "hi * 2^64 + lo");
			assertEquals(TWO_64.add(BigInteger.valueOf(5)).toString(), new Int128(1, 5).toString());
			assertEquals("-170141183460469231731687303715884105728", Int128.of("-170141183460469231731687303715884105728").toString());
			assertEquals("170141183460469231731687303715884105727", new Int128("170141183460469231731687303715884105727").toString());
			assertEquals("-1", Int128.of("-1").toString());
			assertEquals("-3", Int128.of(-3).toString(), "負の long は符号を広げる");
			assertEquals("-3", new Int128(-3).toString());
			assertThrows(NumberFormatException.class, () -> Int128.of(""));
		}
	}

	@Nested
	class State {

		@Test
		void flagsAndBits() {
			assertTrue(Int128.of("0").isZero());
			assertFalse(Int128.of("1").isZero());
			assertTrue(Int128.of("-5").isNegative());
			assertFalse(Int128.of("5").isNegative());
			final Int128 v = Int128.of(1, 6);
			assertTrue(v.getBit(64));
			assertTrue(v.getBit(1));
			assertFalse(v.getBit(0));
			assertThrows(IndexOutOfBoundsException.class, () -> v.getBit(128));
			assertThrows(IndexOutOfBoundsException.class, () -> v.getBit(-1));
		}

		@Test
		void numberConversions() {
			final Int128 v = Int128.of(1, 7);
			assertEquals(7, v.intValue());
			assertEquals(7L, v.longValue(), "下位64ビット");
			assertEquals(Math.pow(2, 64) + 7, v.doubleValue(), 1e3);
			assertEquals((float) (Math.pow(2, 64) + 7), v.floatValue(), 1e6);
			assertEquals(-3.0, Int128.of(-3).doubleValue(), "負の値でも桁が落ちない");
			assertEquals(-(Math.pow(2, 64) + 7), Int128.of(-2, -7).doubleValue(), 1e3);
		}
	}

	@Nested
	class Arithmetic {

		@Test
		void operationsUpdateInPlace() {
			final Int128 a = Int128.of(10);
			assertSame(a, a.add(Int128.of(5)));
			assertEquals("15", a.toString());
			assertSame(a, a.sub(Int128.of(20)));
			assertEquals("-5", a.toString());
			assertSame(a, a.mul(Int128.of("-3")));
			assertEquals("15", a.toString());
			assertSame(a, a.div(Int128.of(4)));
			assertEquals("3", a.toString());
			assertThrows(ArithmeticException.class, () -> a.div(Int128.of(0)));
		}

		@Test
		void matchesBigInteger() {
			final Random random = new Random(1);
			for (int t = 0; t < 2000; t++) {
				final BigInteger x = new BigInteger(random.nextInt(1, 63), random).multiply(BigInteger.valueOf(random.nextBoolean() ? 1 : -1));
				final BigInteger y = new BigInteger(random.nextInt(1, 63), random).multiply(BigInteger.valueOf(random.nextBoolean() ? 1 : -1));
				final String at = "x=" + x + ", y=" + y;
				assertEquals(x.add(y), big(of(x).add(of(y))), at);
				assertEquals(x.subtract(y), big(of(x).sub(of(y))), at);
				assertEquals(x.multiply(y), big(of(x).mul(of(y))), at);
				if (y.signum() != 0)
					assertEquals(x.multiply(x).divide(y), big(of(x.multiply(x)).div(of(y))), at + ", 0方向へ切り捨て");
				assertEquals(Integer.signum(x.compareTo(y)), Integer.signum(of(x).compareTo(of(y))), at);
			}
		}

		@Test
		void unsignedMultiplyHigh() {
			final Random random = new Random(2);
			for (int t = 0; t < 1000; t++) {
				final long x = random.nextLong(), y = random.nextLong();
				final BigInteger p = new BigInteger(Long.toUnsignedString(x)).multiply(new BigInteger(Long.toUnsignedString(y)));
				assertEquals(p.shiftRight(64).longValue(), Int128.unsignedMultiplyHigh(x, y));
			}
		}
	}
}
