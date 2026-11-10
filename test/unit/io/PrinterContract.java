package unit.io;

import java.io.*;
import java.math.*;
import java.nio.charset.*;
import java.util.*;
import java.util.function.*;

import org.junit.jupiter.api.*;

import unit.support.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * FastPrinter（Java 24 版と Java 17 互換版）に共通する振る舞いのテスト。期待値は FastPrinterGuide の記載に従う。
 */
public abstract class PrinterContract {

	static Object call(final Object p, final String name, final Object... args) {
		return Adapter.call(Object.class, p, name, args);
	}

	static List<String> tokens(final String s) {
		return Arrays.stream(s.trim().split("\\s+")).toList();
	}

	/**
	 * out へ書き込むプリンタを作る。
	 */
	protected abstract Object create(OutputStream out, int bufferSize, boolean autoFlush);

	/**
	 * body でプリンタへ書き込み、flush したあとの出力を返す。
	 */
	String output(final Consumer<Object> body) {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		final Object p = create(out, 1 << 10, false);
		body.accept(p);
		Adapter.call(Void.class, p, "flush");
		return out.toString(StandardCharsets.US_ASCII);
	}

	@RepeatedTest(20)
	void numbersMatchToString(final RepetitionInfo info) {
		final Random random = new Random(info.getCurrentRepetition());
		final long[] values = random.longs(200).map(v -> random.nextBoolean() ? v : v % 1000).toArray();
		final StringBuilder expected = new StringBuilder();
		for (final long v : values) expected.append(v).append('\n');
		assertEquals(expected.toString(), output(p -> {
			for (final long v : values) call(p, "println", v);
		}));
	}

	@Nested
	class Primitives {

		@Test
		void scalars() {
			assertEquals("-12 9000000000 x Yes No abc sb 123456789012345678901 1.25\n", output(p -> {
				call(p, "print", -12);
				call(p, "print", ' ');
				call(p, "print", 9_000_000_000L);
				call(p, "print", ' ');
				call(p, "print", 'x');
				call(p, "print", ' ');
				call(p, "print", true);
				call(p, "print", ' ');
				call(p, "print", false);
				call(p, "print", ' ');
				call(p, "print", "abc");
				call(p, "print", ' ');
				call(p, "print", new StringBuilder("sb"));
				call(p, "print", ' ');
				call(p, "print", new BigInteger("123456789012345678901"));
				call(p, "print", ' ');
				call(p, "print", new BigDecimal("1.25"));
				call(p, "println");
			}));
		}

		@Test
		void printlnAppendsLineBreak() {
			assertEquals("0\n-2147483648\n9223372036854775807\nYes\nq\nhi\n", output(p -> {
				call(p, "println", 0);
				call(p, "println", Integer.MIN_VALUE);
				call(p, "println", Long.MAX_VALUE);
				call(p, "println", true);
				call(p, "println", 'q');
				call(p, "println", "hi");
			}));
		}

		@Test
		void methodChainReturnsSelf() {
			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			final Object p = create(out, 16, false);
			assertSame(p, call(p, "print", 1));
			assertSame(p, call(p, "println", "x"));
		}

		@Test
		void pairsAndFixedDecimals() {
			// 2値の println は改行で区切る
			assertEquals("1\n2\n3\n4\n5,6\n1.50\n", output(p -> {
				call(p, "println", 1, 2);
				call(p, "println", 3L, 4L);
				call(p, "println", 5L, 6L, ',');
				call(p, "println", 1.5, 2);
			}));
		}

		@Test
		void printf() {
			assertEquals("x=007", output(p -> call(p, "printf", "x=%03d", new Object[]{7})));
		}
	}

	@Nested
	class Arrays1D {

		@Test
		void printUsesSpacesAndPrintlnUsesLineBreaks() {
			assertEquals("1 2 3", output(p -> call(p, "print", (Object) new int[]{1, 2, 3})));
			assertEquals("1\n2\n3\n", output(p -> call(p, "println", (Object) new int[]{1, 2, 3})), "println は各要素を改行区切り");
			assertEquals("4 5", output(p -> call(p, "print", (Object) new long[]{4, 5})));
			assertEquals("a b", output(p -> call(p, "print", (Object) new String[]{"a", "b"})));
			assertEquals("Yes No", output(p -> call(p, "print", (Object) new boolean[]{true, false})));
		}

		@Test
		void delimitersAndRanges() {
			assertEquals("1,2,3", output(p -> call(p, "print", new int[]{1, 2, 3}, ',')));
			assertEquals("2 3", output(p -> call(p, "print", new int[]{1, 2, 3, 4}, 1, 3)));
			assertEquals("2-3", output(p -> call(p, "print", new long[]{1, 2, 3, 4}, 1, 3, '-')));
		}

		@Test
		void functions() {
			assertEquals("2 4 6", output(p -> call(p, "print", new int[]{1, 2, 3}, (IntFunction<Integer>) x -> x * 2)));
			assertEquals("A\nB\n", output(p -> call(p, "println", List.of("a", "b"), (Function<String, String>) String::toUpperCase)));
		}

		@Test
		void iterables() {
			assertEquals("3 1 2", output(p -> call(p, "print", List.of(3, 1, 2))));
			assertEquals("3:1:2", output(p -> call(p, "print", List.of(3, 1, 2), ':')));
		}

		@Test
		void reverseRepeatAndChars() {
			assertEquals(List.of("3", "2", "1"), tokens(output(p -> call(p, "printReverse", (Object) new int[]{1, 2, 3}))));
			assertEquals(List.of("c", "b", "a"), tokens(output(p -> call(p, "printlnReverse", (Object) new String[]{"a", "b", "c"}))));
			assertEquals("xxx", output(p -> call(p, "printRepeat", 'x', 3)));
			assertEquals("abab", output(p -> call(p, "printRepeat", "ab", 2)));
			assertEquals(3, output(p -> call(p, "printlnRepeat", 'y', 3)).chars().filter(c -> c == 'y').count());
			assertEquals("abc", output(p -> call(p, "printChars", (Object) "abc".toCharArray())), "区切りなし");
			assertEquals("bc", output(p -> call(p, "printChars", "abcd".toCharArray(), 1, 3)));
			assertEquals("BCD", output(p -> call(p, "printChars", "abc".toCharArray(), (IntUnaryOperator) c -> c - 'a' + 'B')));
			assertEquals("ab\ncd\n", output(p -> call(p, "printChars", (Object) new char[][]{"ab".toCharArray(), "cd".toCharArray()})));
		}
	}

	@Nested
	class Arrays2D {

		@Test
		void matrixRowsOnLines() {
			assertEquals("1 2\n3 4\n", output(p -> call(p, "println", (Object) new int[][]{{1, 2}, {3, 4}})));
			assertEquals("1,2\n3,4\n", output(p -> call(p, "println", new long[][]{{1, 2}, {3, 4}}, ',')));
		}
	}

	@Nested
	class Buffering {

		@Test
		void outputBeyondBufferAndAutoFlush() {
			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			final Object p = create(out, 8, false);
			final String longText = "x".repeat(100);
			call(p, "print", longText);
			call(p, "print", 1234567890123L);
			Adapter.call(Void.class, p, "flush");
			assertEquals(longText + "1234567890123", out.toString(StandardCharsets.US_ASCII), "バッファより長い出力も欠けない");
			final ByteArrayOutputStream auto = new ByteArrayOutputStream();
			final Object q = create(auto, 1 << 10, true);
			call(q, "println", 42);
			assertEquals("42\n", auto.toString(StandardCharsets.US_ASCII), "autoFlush なら flush を呼ばなくても出力される");
		}

		@Test
		void closeFlushesAndClosesStream() {
			final boolean[] closed = {false};
			final ByteArrayOutputStream out = new ByteArrayOutputStream() {
				@Override
				public void close() {
					closed[0] = true;
				}
			};
			final Object p = create(out, 1 << 10, false);
			call(p, "print", "bye");
			Adapter.call(Void.class, p, "close");
			assertEquals("bye", out.toString(StandardCharsets.US_ASCII));
			assertTrue(closed[0]);
		}
	}
}
