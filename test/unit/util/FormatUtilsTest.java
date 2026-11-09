package unit.util;

import java.math.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import lib.util.*;

import static org.junit.jupiter.api.Assertions.*;

final class FormatUtilsTest {

	@Test
	void join() {
		assertEquals("1 2 3", FormatUtils.join(new int[]{1, 2, 3}));
		assertEquals("1,2,3", FormatUtils.join(new long[]{1, 2, 3}, ","));
		assertEquals("", FormatUtils.join(new int[0]));
		assertEquals("-1 0 9223372036854775807", FormatUtils.join(new long[]{-1, 0, Long.MAX_VALUE}));
	}

	// 各行の末尾に行区切りを付ける。空の行は空文字列になる
	@Test
	void joinLines() {
		final int[][] ints = {{1, 2}, {}, {3, 4}};
		final long[][] longs = {{1, 2}, {3, 4}};
		assertEquals("1 2\n\n3 4\n", FormatUtils.joinLines(ints));
		assertEquals("1,2\n\n3,4\n", FormatUtils.joinLines(ints, ","));
		assertEquals("1,2;;3,4;", FormatUtils.joinLines(ints, ",", ";"));
		assertEquals("1|2/3|4/", FormatUtils.joinLines(longs, "|", "/"));
		assertEquals("", FormatUtils.joinLines(new int[0][]));
	}

	@ParameterizedTest
	@CsvSource({
		"1.25, UP, 1.3",
		"1.25, DOWN, 1.2",
		"1.25, CEILING, 1.3",
		"1.25, FLOOR, 1.2",
		"1.25, HALF_UP, 1.3",
		"1.25, HALF_DOWN, 1.2",
		"1.25, HALF_EVEN, 1.2",
		"1.35, HALF_EVEN, 1.4",
		"-1.25, CEILING, -1.2",
		"-1.25, FLOOR, -1.3",
		"-1.25, DOWN, -1.2",
		"-1.25, UP, -1.3",
		"1.2, UNNECESSARY, 1.2",
	})
	void formatDoubleWithRounding(final double value, final RoundingMode mode, final String expected) {
		assertEquals(expected, FormatUtils.formatDouble(value, 1, mode));
	}

	@Test
	void formatDoubleDefaultsToHalfUp() {
		assertEquals("1.3", FormatUtils.formatDouble(1.25, 1));
		assertEquals("12", FormatUtils.formatDouble(12, 0));
		assertEquals("12.000", FormatUtils.formatDouble(12, 3));
		assertEquals("10.00", FormatUtils.formatDouble(9.999, 2));
		assertEquals("-10.00", FormatUtils.formatDouble(-9.999, 2));
	}

	@Test
	void unnecessaryRoundingThrows() {
		assertThrows(ArithmeticException.class, () -> FormatUtils.formatDouble(1.25, 1, RoundingMode.UNNECESSARY));
	}

	// 符号は先頭に置き、その後ろを埋める
	@Test
	void paddedString() {
		assertEquals("007", FormatUtils.toPaddedString(7, 3));
		assertEquals("-007", FormatUtils.toPaddedString(-7, 4));
		assertEquals("001f", FormatUtils.toPaddedString(31, 4, 16));
		assertEquals("11111", FormatUtils.toPaddedString(31, 5, 2));
		assertEquals("0001f", FormatUtils.toPaddedString(31, 5, 16, '0'));
		assertEquals("___7", FormatUtils.toPaddedString(7, 4, '_'));
		assertEquals("-__7", FormatUtils.toPaddedString(-7, 4, '_'));
		assertEquals("-001f", FormatUtils.toPaddedString(-31, 5, 16));
		assertEquals("12345", FormatUtils.toPaddedString(12345, 3));
	}
}
