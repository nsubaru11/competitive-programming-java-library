package unit.util;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import lib.util.*;

import static org.junit.jupiter.api.Assertions.*;

final class ConversionsTest {

	// ビッグエンディアンは先頭が最上位。"00121" は 121、リトルエンディアンでは 12100
	@Test
	void charsToNumberByEndian() {
		final char[] chars = "00121".toCharArray();
		assertEquals(121, Conversions.toInt(chars));
		assertEquals(12100, Conversions.toInt(chars, true));
		assertEquals(121, Conversions.toLong(chars));
		assertEquals(12100, Conversions.toLong(chars, true));
		assertEquals(1234567890, Conversions.toInt("1234567890".toCharArray()));
	}

	@Test
	void digitsToNumberByEndian() {
		final int[] digits = {0, 0, 1, 2, 1};
		assertEquals(121, Conversions.toInt(digits));
		assertEquals(12100, Conversions.toInt(digits, true));
	}

	// 桁数が足りなければ上位を 0 で埋め、超えれば下位の桁だけを残す
	@ParameterizedTest
	@CsvSource({
		"121, 5, false, 00121",
		"121, 5, true, 12100",
		"121, 2, false, 21",
		"121, 2, true, 12",
		"123, 8, false, 00000123",
		"1234567890, 8, false, 34567890",
	})
	void numberToFixedWidthChars(final int value, final int width, final boolean littleEndian, final String expected) {
		assertEquals(expected, new String(Conversions.toCharArray(value, width, littleEndian)));
	}

	@Test
	void numberToChars() {
		assertEquals("0", new String(Conversions.toCharArray(0)));
		assertEquals("1234567890", new String(Conversions.toCharArray(1234567890)));
		assertEquals("00121", new String(Conversions.toCharArray(121, 5)));
	}

	@Test
	void digitArrays() {
		assertArrayEquals(new int[]{1, 2, 3, 4}, Conversions.toIntArray("1234".toCharArray()));
		assertArrayEquals(new int[]{0, 0, 1, 2, 1}, Conversions.toIntArray("00121"));
		assertArrayEquals("1234".toCharArray(), Conversions.toCharArray(new int[]{1, 2, 3, 4}));
	}
}
