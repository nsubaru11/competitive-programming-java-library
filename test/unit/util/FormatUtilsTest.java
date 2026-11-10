package unit.util;

import java.math.*;
import java.util.*;
import java.util.stream.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import lib.util.*;

import static org.junit.jupiter.api.Assertions.*;

final class FormatUtilsTest {

	@RepeatedTest(200)
	void matchesJdkFormatting(final RepetitionInfo info) {
		final Random random = new Random(info.getCurrentRepetition());
		final long[] a = random.longs(random.nextInt(6), -1000, 1000).toArray();
		final String at = "a=" + Arrays.toString(a);
		assertEquals(Arrays.stream(a).mapToObj(Long::toString).collect(Collectors.joining(" ")), FormatUtils.join(a), at);
		assertEquals(Arrays.stream(a).mapToObj(Long::toString).collect(Collectors.joining("--")), FormatUtils.join(Arrays.stream(a).mapToInt(x -> (int) x).toArray(), "--"), at);
		// 高速化のため double で 10^digits 倍してから丸めるので、境界付近では誤差が入る。
		// x を 1/8 の倍数にして、10^digits 倍しても誤差が出ない値だけで比べる
		final double x = random.nextInt(-1_000_000, 1_000_000) / 8.0;
		final int digits = random.nextInt(0, 6);
		for (final RoundingMode mode : new RoundingMode[]{RoundingMode.HALF_UP, RoundingMode.FLOOR, RoundingMode.CEILING}) {
			final BigDecimal expected = new BigDecimal(x).setScale(digits, mode);
			// 負の値が 0 に丸められる場合の符号は Guide に定めがないので比べない
			if (x < 0 && expected.signum() == 0) continue;
			assertEquals(expected.toPlainString(), FormatUtils.formatDouble(x, digits, mode), "x=" + x + ", digits=" + digits + ", " + mode);
		}
		final long v = random.nextLong(-100000, 100000);
		final int width = random.nextInt(0, 10), radix = random.nextInt(2, 37);
		final String body = Long.toString(Math.abs(v), radix), sign = v < 0 ? "-" : "";
		final String expected = sign + "0".repeat(Math.max(0, width - sign.length() - body.length())) + body;
		assertEquals(expected, FormatUtils.toPaddedString(v, width, radix), "v=" + v + ", width=" + width + ", radix=" + radix);
	}

	@Nested
	class Join {

		@Test
		void arrays() {
			assertEquals("1 2 3", FormatUtils.join(new int[]{1, 2, 3}));
			assertEquals("1,2,3", FormatUtils.join(new int[]{1, 2, 3}, ","));
			assertEquals("-1 0 9223372036854775807", FormatUtils.join(new long[]{-1, 0, Long.MAX_VALUE}));
			assertEquals("1, 2", FormatUtils.join(new long[]{1, 2}, ", "));
			assertEquals("", FormatUtils.join(new int[0]));
		}

		// 各行の末尾に行区切りを付ける。空の行は空文字列になる
		@Test
		void lines() {
			final int[][] ints = {{1, 2}, {}, {3, 4}};
			final long[][] longs = {{1, 2}, {3, 4}};
			assertEquals("1 2\n\n3 4\n", FormatUtils.joinLines(ints));
			assertEquals("1,2\n\n3,4\n", FormatUtils.joinLines(ints, ","));
			assertEquals("1,2;;3,4;", FormatUtils.joinLines(ints, ",", ";"));
			assertEquals("1 2\n3 4\n", FormatUtils.joinLines(longs));
			assertEquals("1|2\n3|4\n", FormatUtils.joinLines(longs, "|"));
			assertEquals("1|2/3|4/", FormatUtils.joinLines(longs, "|", "/"));
			assertEquals("", FormatUtils.joinLines(new int[0][]));
		}
	}

	@Nested
	class FormatDouble {

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
				"1.2, UNNECESSARY, 1.2",
		})
		void roundingModes(final double value, final RoundingMode mode, final String expected) {
			assertEquals(expected, FormatUtils.formatDouble(value, 1, mode));
		}

		@Test
		void defaultIsHalfUpAndFixedDigits() {
			assertEquals("1.3", FormatUtils.formatDouble(1.25, 1));
			assertEquals("12", FormatUtils.formatDouble(12, 0));
			assertEquals("12.000", FormatUtils.formatDouble(12, 3));
			assertEquals("10.00", FormatUtils.formatDouble(9.999, 2));
			assertEquals("-10.00", FormatUtils.formatDouble(-9.999, 2));
			assertThrows(ArithmeticException.class, () -> FormatUtils.formatDouble(1.25, 1, RoundingMode.UNNECESSARY));
		}
	}

	@Nested
	class Padding {

		// 符号は先頭に置き、その後ろを埋める。幅を超える値は切り詰めない
		@Test
		void examples() {
			assertEquals("007", FormatUtils.toPaddedString(7, 3));
			assertEquals("-007", FormatUtils.toPaddedString(-7, 4));
			assertEquals("___7", FormatUtils.toPaddedString(7, 4, '_'));
			assertEquals("-__7", FormatUtils.toPaddedString(-7, 4, '_'));
			assertEquals("001f", FormatUtils.toPaddedString(31, 4, 16));
			assertEquals("-001f", FormatUtils.toPaddedString(-31, 5, 16));
			assertEquals("11111", FormatUtils.toPaddedString(31, 5, 2));
			assertEquals("**1f", FormatUtils.toPaddedString(31, 4, 16, '*'));
			assertEquals("12345", FormatUtils.toPaddedString(12345, 3));
		}
	}
}
