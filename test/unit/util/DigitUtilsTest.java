package unit.util;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import lib.util.*;

import static org.junit.jupiter.api.Assertions.*;

final class DigitUtilsTest {

	@ParameterizedTest
	@CsvSource({
		"0, 1",
		"1, 1",
		"2, 2",
		"2147483647, 31",
		"9223372036854775807, 63",
	})
	void binaryDigits(final long n, final int expected) {
		assertEquals(expected, DigitUtils.digits2(n));
		if (n <= Integer.MAX_VALUE) assertEquals(expected, DigitUtils.digits2((int) n));
	}

	@ParameterizedTest
	@CsvSource({
		"0, 1",
		"9, 1",
		"10, 2",
		"999, 3",
		"1000, 4",
		"9223372036854775807, 19",
	})
	void decimalDigits(final long n, final int expected) {
		assertEquals(expected, DigitUtils.digits10(n));
		if (n <= Integer.MAX_VALUE) assertEquals(expected, DigitUtils.digits10((int) n));
	}

	// 末尾の 0 は反転後の先頭に来るため消える
	@Test
	void reverse() {
		assertEquals(21, DigitUtils.reverse(1_200));
		assertEquals(21L, DigitUtils.reverse(1_200L));
		assertEquals(4321, DigitUtils.reverse(1234));
	}

	// 昇順では 0 が先頭に来て消える
	@Test
	void sortDigits() {
		assertEquals(112234579, DigitUtils.sort(417253219));
		assertEquals(975432211, DigitUtils.descendingSort(417253219));
		assertEquals(1, DigitUtils.sort(100));
		assertEquals(100, DigitUtils.descendingSort(100));
		assertEquals(123_456_789L, DigitUtils.sort(9_876_543_210L));
		assertEquals(9_876_543_210L, DigitUtils.descendingSort(9_876_543_210L));
	}

	@Test
	void sortString() {
		assertEquals("01abc", DigitUtils.sort("cab01"));
		assertEquals("cba10", DigitUtils.descendingSort("cab01"));
	}
}
