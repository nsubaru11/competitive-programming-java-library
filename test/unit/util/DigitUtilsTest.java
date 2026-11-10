package unit.util;

import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import lib.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 数値を受け取るメソッドは非負整数を前提とする。
 */
final class DigitUtilsTest {

	@Nested
	class Aggregation {

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
			"1000, 4",
			"9223372036854775807, 19",
		})
		void decimalDigits(final long n, final int expected) {
			assertEquals(expected, DigitUtils.digits10(n));
			if (n <= Integer.MAX_VALUE) assertEquals(expected, DigitUtils.digits10((int) n));
		}
	}

	@Nested
	class Transformation {

		// 末尾の 0 は反転後の先頭に来るため消える
		@Test
		void reverse() {
			assertEquals(21, DigitUtils.reverse(1_200));
			assertEquals(21L, DigitUtils.reverse(1_200L));
			assertEquals(0, DigitUtils.reverse(0));
		}

		// 昇順では 0 が先頭に来て消える
		@Test
		void sortDigitsAndStrings() {
			assertEquals(112234579, DigitUtils.sort(417253219));
			assertEquals(975432211, DigitUtils.descendingSort(417253219));
			assertEquals(1, DigitUtils.sort(100));
			assertEquals(100, DigitUtils.descendingSort(100));
			assertEquals(123_456_789L, DigitUtils.sort(9_876_543_210L));
			assertEquals(9_876_543_210L, DigitUtils.descendingSort(9_876_543_210L));
			assertEquals("01abc", DigitUtils.sort("cab01"));
			assertEquals("cba10", DigitUtils.descendingSort("cab01"));
		}
	}

	@RepeatedTest(200)
	void matchesStringOperations(final RepetitionInfo info) {
		final Random random = new Random(info.getCurrentRepetition());
		final long v = random.nextBoolean() ? random.nextInt(Integer.MAX_VALUE) : random.nextLong(Long.MAX_VALUE);
		final String s = Long.toString(v);
		final String at = "v=" + v;
		assertEquals(Long.toBinaryString(v).length(), DigitUtils.digits2(v), at);
		assertEquals(s.length(), DigitUtils.digits10(v), at);
		final char[] asc = s.toCharArray();
		Arrays.sort(asc);
		final String ascending = new String(asc), descending = new StringBuilder(ascending).reverse().toString();
		assertEquals(Long.parseLong(ascending), DigitUtils.sort(v), at);
		// 降順に並べた値が long に収まる場合だけ比べる
		if (descending.length() < 19 || descending.compareTo(Long.toString(Long.MAX_VALUE)) <= 0) {
			assertEquals(Long.parseLong(descending), DigitUtils.descendingSort(v), at);
		}
		final String reversed = new StringBuilder(s).reverse().toString();
		if (reversed.length() < 19 || reversed.compareTo(Long.toString(Long.MAX_VALUE)) <= 0) {
			assertEquals(Long.parseLong(reversed), DigitUtils.reverse(v), at);
		}
		if (v <= Integer.MAX_VALUE) {
			final int iv = (int) v;
			assertEquals(Integer.toBinaryString(iv).length(), DigitUtils.digits2(iv), at);
			assertEquals(s.length(), DigitUtils.digits10(iv), at);
			assertEquals(Long.parseLong(ascending), DigitUtils.sort(iv), at);
			if (Long.parseLong(descending) <= Integer.MAX_VALUE) assertEquals(Integer.parseInt(descending), DigitUtils.descendingSort(iv), at);
			if (Long.parseLong(reversed) <= Integer.MAX_VALUE) assertEquals(Integer.parseInt(reversed), DigitUtils.reverse(iv), at);
		}
		final String text = random.ints(random.nextInt(10), 'a', 'z' + 1).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
		final char[] t = text.toCharArray();
		Arrays.sort(t);
		assertEquals(new String(t), DigitUtils.sort(text), at);
		assertEquals(new StringBuilder(new String(t)).reverse().toString(), DigitUtils.descendingSort(text), at);
	}
}
