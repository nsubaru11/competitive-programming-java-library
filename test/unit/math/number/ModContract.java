package unit.math.number;

import java.math.*;
import java.util.*;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ModInt・ModLong に共通する振る舞いのテスト。値と法は long で扱う。
 */
abstract class ModContract {

	abstract Target create(long value, long mod);

	/**
	 * ランダムな照合で使う素数の法。
	 */
	abstract long[] mods();

	interface Target {
		Object self();

		long value();

		long mod();

		Target add(Target other);

		Target sub(Target other);

		Target mul(Target other);

		Target div(Target other);

		Target pow(int exp);
	}

	@Nested
	class Access {

		@Test
		void valueModAndNumberConversions() {
			final Target a = create(5, 7);
			assertEquals(5L, a.value());
			assertEquals(7L, a.mod());
			final Number n = (Number) a.self();
			assertEquals(5, n.intValue());
			assertEquals(5L, n.longValue());
			assertEquals(5.0f, n.floatValue());
			assertEquals(5.0, n.doubleValue());
			assertEquals("5", a.self().toString());
		}

		@Test
		void equalsAndHashCode() {
			final Object a = create(3, 7).self(), b = create(3, 7).self(), c = create(3, 11).self(), d = create(4, 7).self();
			assertEquals(a, b);
			assertEquals(a.hashCode(), b.hashCode());
			assertNotEquals(a, c, "法が異なれば等しくない");
			assertNotEquals(a, d);
			assertNotEquals(a, "3");
		}
	}

	@Nested
	class Arithmetic {

		@Test
		void smallExample() {
			final Target a = create(5, 7), b = create(4, 7);
			assertEquals(2L, a.add(b).value());
			assertEquals(1L, a.sub(b).value());
			assertEquals(6L, b.sub(a).value());
			assertEquals(6L, a.mul(b).value());
			assertEquals(3L, a.div(b).value(), "5 / 4 = 5 * 2 = 10 = 3 (mod 7)");
			assertEquals(4L, a.pow(2).value());
			assertEquals(1L, a.pow(0).value());
			assertEquals(7L, a.add(b).mod());
		}

		@Test
		void matchesBigInteger() {
			final Random random = new Random(1);
			for (final long mod : mods()) {
				final BigInteger m = BigInteger.valueOf(mod);
				for (int t = 0; t < 300; t++) {
					final long x = random.nextLong(mod), y = random.nextLong(1, mod);
					final int e = random.nextInt(1000);
					final Target a = create(x, mod), b = create(y, mod);
					final BigInteger bx = BigInteger.valueOf(x), by = BigInteger.valueOf(y);
					final String at = "mod=" + mod + ", x=" + x + ", y=" + y;
					assertEquals(bx.add(by).mod(m).longValue(), a.add(b).value(), at);
					assertEquals(bx.subtract(by).mod(m).longValue(), a.sub(b).value(), at);
					assertEquals(bx.multiply(by).mod(m).longValue(), a.mul(b).value(), at);
					assertEquals(bx.multiply(by.modInverse(m)).mod(m).longValue(), a.div(b).value(), at);
					assertEquals(bx.modPow(BigInteger.valueOf(e), m).longValue(), a.pow(e).value(), at + ", e=" + e);
				}
			}
		}
	}
}
