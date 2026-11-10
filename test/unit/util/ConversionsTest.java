package unit.util;

import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import lib.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ビッグエンディアンは先頭が最上位の桁、リトルエンディアンは先頭が最下位の桁である。"00121" は前者で 121、後者で 12100 を表す。
 */
final class ConversionsTest {

	@Nested
	class ToNumber {

		@Test
		void charsByEndian() {
			final char[] chars = "00121".toCharArray();
			assertEquals(121, Conversions.toInt(chars));
			assertEquals(121, Conversions.toInt(chars, false));
			assertEquals(12100, Conversions.toInt(chars, true));
			assertEquals(121L, Conversions.toLong(chars));
			assertEquals(12100L, Conversions.toLong(chars, true));
			assertEquals(1234567890123456789L, Conversions.toLong("1234567890123456789".toCharArray()));
		}

		@Test
		void digitsByEndian() {
			final int[] digits = {0, 0, 1, 2, 1};
			assertEquals(121, Conversions.toInt(digits));
			assertEquals(12100, Conversions.toInt(digits, true));
			assertEquals(121L, Conversions.toLong(digits));
			assertEquals(12100L, Conversions.toLong(digits, true));
		}
	}

	@Nested
	class ToChars {

		// 桁数が足りなければ上位を 0 で埋め、超えれば上位の桁から切り捨てる
		@ParameterizedTest
		@CsvSource({
			"121, 5, false, 00121",
			"121, 5, true, 12100",
			"121, 2, false, 21",
			"121, 2, true, 12",
			"1234567890, 8, false, 34567890",
		})
		void fixedWidth(final int value, final int width, final boolean littleEndian, final String expected) {
			assertEquals(expected, new String(Conversions.toCharArray(value, width, littleEndian)));
			assertEquals(expected, new String(Conversions.toCharArray((long) value, width, littleEndian)));
			if (!littleEndian) {
				assertEquals(expected, new String(Conversions.toCharArray(value, width)));
				assertEquals(expected, new String(Conversions.toCharArray((long) value, width)));
			}
		}

		@Test
		void naturalWidth() {
			assertEquals("0", new String(Conversions.toCharArray(0)));
			assertEquals("1234567890", new String(Conversions.toCharArray(1234567890)));
			assertEquals("9223372036854775807", new String(Conversions.toCharArray(Long.MAX_VALUE)));
		}

		@Test
		void digitArrays() {
			assertArrayEquals("1234".toCharArray(), Conversions.toCharArray(new int[]{1, 2, 3, 4}));
			assertArrayEquals("4321".toCharArray(), Conversions.toCharArray(new int[]{1, 2, 3, 4}, true));
			assertArrayEquals("1234".toCharArray(), Conversions.toCharArray(new int[]{1, 2, 3, 4}, false));
			assertArrayEquals(new int[]{1, 2, 3, 4}, Conversions.toIntArray("1234".toCharArray()));
			assertArrayEquals(new int[]{0, 0, 1, 2, 1}, Conversions.toIntArray("00121"));
		}
	}

	@RepeatedTest(100)
	void roundTripsMatchStrings(final RepetitionInfo info) {
		final Random random = new Random(info.getCurrentRepetition());
		final long v = random.nextBoolean() ? random.nextInt(Integer.MAX_VALUE) : random.nextLong(Long.MAX_VALUE);
		final String s = Long.toString(v), reversed = new StringBuilder(s).reverse().toString();
		final String at = "v=" + v;
		assertEquals(s, new String(Conversions.toCharArray(v)), at);
		assertEquals(v, Conversions.toLong(s.toCharArray()), at);
		assertEquals(v, Conversions.toLong(reversed.toCharArray(), true), at);
		final int[] digits = Conversions.toIntArray(s);
		assertEquals(v, Conversions.toLong(digits), at);
		assertArrayEquals(s.toCharArray(), Conversions.toCharArray(digits), at);
		final int width = random.nextInt(1, 25);
		final String padded = width >= s.length() ? "0".repeat(width - s.length()) + s : s.substring(s.length() - width);
		assertEquals(padded, new String(Conversions.toCharArray(v, width)), at + ", width=" + width);
		assertEquals(new StringBuilder(padded).reverse().toString(), new String(Conversions.toCharArray(v, width, true)), at);
		if (v <= Integer.MAX_VALUE) {
			final int iv = (int) v;
			assertEquals(iv, Conversions.toInt(s.toCharArray()), at);
			assertEquals(iv, Conversions.toInt(digits), at);
			assertEquals(s, new String(Conversions.toCharArray(iv)), at);
			assertEquals(padded, new String(Conversions.toCharArray(iv, width)), at);
		}
	}
}
