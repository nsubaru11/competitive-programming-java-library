import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Locale;
import java.util.function.DoubleFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.LongFunction;

import static java.lang.Math.*;

@SuppressWarnings("unused")
// FastPrinterとContestPrinterを統合し、コメントを削除した圧縮版のクラスです。
// コメントが必要なく、文字数を気にするなら、こちらを使用して下さい。
public class CompressedFastPrinter {

	private static final class FastPrinter implements AutoCloseable {
		private static final int MAX_INT_DIGITS = 11;
		private static final int MAX_LONG_DIGITS = 20;
		private static final int DEFAULT_BUFFER_SIZE = 65536;
		private static final byte[] TWO_DIGIT_NUMBERS = new byte[200];

		static {
			byte tens = '0', ones = '0';
			for (int i = 0; i < 100; i++) {
				TWO_DIGIT_NUMBERS[i << 1] = tens;
				TWO_DIGIT_NUMBERS[(i << 1) + 1] = ones;
				if (++ones > '9') {
					ones = '0';
					tens++;
				}
			}
		}

		private final byte[] buffer;
		private final boolean autoFlush;
		private final OutputStream out;
		private int pos = 0;

		public FastPrinter() {
			this(System.out, DEFAULT_BUFFER_SIZE, false);
		}

		public FastPrinter(final OutputStream out) {
			this(out, DEFAULT_BUFFER_SIZE, false);
		}

		public FastPrinter(final int bufferSize) {
			this(System.out, bufferSize, false);
		}

		public FastPrinter(final boolean autoFlush) {
			this(System.out, DEFAULT_BUFFER_SIZE, autoFlush);
		}

		public FastPrinter(final OutputStream out, final boolean autoFlush) {
			this(out, DEFAULT_BUFFER_SIZE, autoFlush);
		}

		public FastPrinter(final int bufferSize, final boolean autoFlush) {
			this(System.out, bufferSize, autoFlush);
		}

		public FastPrinter(final OutputStream out, final int bufferSize) {
			this(out, bufferSize, false);
		}

		public FastPrinter(final OutputStream out, final int bufferSize, final boolean autoFlush) {
			this.out = out;
			this.buffer = new byte[max(bufferSize, 64)];
			this.autoFlush = autoFlush;
		}

		@Override
		public void close() throws IOException {
			flush();
			if (out != System.out)
				out.close();
		}

		public void flush() {
			try {
				if (pos > 0)
					out.write(buffer, 0, pos);
				out.flush();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			pos = 0;
		}

		public void println() {
			ensureBufferSpace(1);
			buffer[pos++] = '\n';
			if (autoFlush) flush();
		}

		public void println(final int i) {
			ensureBufferSpace(MAX_INT_DIGITS + 1);
			fillBuffer(i);
			buffer[pos++] = '\n';
			if (autoFlush) flush();
		}

		public void println(final long l) {
			ensureBufferSpace(MAX_LONG_DIGITS + 1);
			fillBuffer(l);
			buffer[pos++] = '\n';
			if (autoFlush) flush();
		}

		public void println(final double d) {
			fillBuffer(Double.toString(d));
			ensureBufferSpace(1);
			buffer[pos++] = '\n';
			if (autoFlush) flush();
		}

		public void println(final char c) {
			ensureBufferSpace(2);
			buffer[pos++] = (byte) c;
			buffer[pos++] = '\n';
			if (autoFlush) flush();
		}

		public void println(final boolean b) {
			ensureBufferSpace(4);
			fillBuffer(b);
			buffer[pos++] = '\n';
			if (autoFlush) flush();
		}

		public void println(final String s) {
			fillBuffer(s);
			ensureBufferSpace(1);
			buffer[pos++] = '\n';
			if (autoFlush) flush();
		}

		public void println(final Object o) {
			if (o == null) return;
			if (o instanceof String s) {
				println(s);
			} else if (o instanceof Long l) {
				println(l.longValue());
			} else if (o instanceof Integer i) {
				println(i.intValue());
			} else if (o instanceof Double d) {
				println(d.toString());
			} else if (o instanceof Boolean b) {
				println(b.booleanValue());
			} else if (o instanceof Character c) {
				println(c.charValue());
			} else if (o instanceof int[] arr) {
				println(arr);
			} else if (o instanceof long[] arr) {
				println(arr);
			} else if (o instanceof double[] arr) {
				println(arr);
			} else if (o instanceof boolean[] arr) {
				println(arr);
			} else if (o instanceof char[] arr) {
				println(arr);
			} else if (o instanceof String[] arr) {
				println(arr);
			} else if (o instanceof Object[] arr) {
				println(arr);
			} else {
				println(o.toString());
			}
		}

		public void println(final BigInteger bi) {
			println(bi.toString());
		}

		public void println(final BigDecimal bd) {
			println(bd.toString());
		}

		public void print(final int i) {
			ensureBufferSpace(MAX_INT_DIGITS);
			fillBuffer(i);
			if (autoFlush) flush();
		}

		public void print(final long l) {
			ensureBufferSpace(MAX_LONG_DIGITS);
			fillBuffer(l);
			if (autoFlush) flush();
		}

		public void print(final double d) {
			print(Double.toString(d));
		}

		public void print(final char c) {
			ensureBufferSpace(1);
			buffer[pos++] = (byte) c;
			if (autoFlush) flush();
		}

		public void print(final boolean b) {
			ensureBufferSpace(3);
			fillBuffer(b);
			if (autoFlush) flush();
		}

		public void print(final String s) {
			fillBuffer(s);
			if (autoFlush) flush();
		}

		public void print(final Object o) {
			if (o == null) return;
			if (o instanceof String s) {
				print(s);
			} else if (o instanceof Long l) {
				print(l.longValue());
			} else if (o instanceof Integer i) {
				print(i.intValue());
			} else if (o instanceof Double d) {
				print(d.toString());
			} else if (o instanceof Boolean b) {
				print(b.booleanValue());
			} else if (o instanceof Character c) {
				print(c.charValue());
			} else if (o instanceof int[] arr) {
				print(arr);
			} else if (o instanceof long[] arr) {
				print(arr);
			} else if (o instanceof double[] arr) {
				print(arr);
			} else if (o instanceof boolean[] arr) {
				print(arr);
			} else if (o instanceof char[] arr) {
				print(arr);
			} else if (o instanceof String[] arr) {
				print(arr);
			} else if (o instanceof Object[] arr) {
				print(arr);
			} else {
				print(o.toString());
			}
		}

		public void print(final BigInteger bi) {
			print(bi.toString());
		}

		public void print(final BigDecimal bd) {
			print(bd.toString());
		}

		public void printf(final String format, final Object... args) {
			print(String.format(format, args));
		}

		public void printf(final Locale locale, final String format, final Object... args) {
			print(String.format(locale, format, args));
		}

		private void ensureBufferSpace(final int size) {
			if (pos + size > buffer.length) {
				try {
					out.write(buffer, 0, pos);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				pos = 0;
			}
		}

		private void fillBuffer(final String s) {
			if (s == null) return;
			final int len = s.length();
			for (int i = 0; i < len; ) {
				ensureBufferSpace(1);
				int limit = min(buffer.length - pos, len - i);
				while (limit-- > 0) {
					buffer[pos++] = (byte) s.charAt(i++);
				}
			}
		}

		private void fillBuffer(final boolean b) {
			if (b) {
				buffer[pos++] = 'Y';
				buffer[pos++] = 'e';
				buffer[pos++] = 's';
			} else {
				buffer[pos++] = 'N';
				buffer[pos++] = 'o';
			}
		}

		private void fillBuffer(int i) {
			if (i == Integer.MIN_VALUE) {
				buffer[pos++] = '-';
				buffer[pos++] = '2';
				buffer[pos++] = '1';
				buffer[pos++] = '4';
				buffer[pos++] = '7';
				buffer[pos++] = '4';
				buffer[pos++] = '8';
				buffer[pos++] = '3';
				buffer[pos++] = '6';
				buffer[pos++] = '4';
				buffer[pos++] = '8';
				return;
			}
			boolean negative = i < 0;
			if (negative) {
				i = -i;
				buffer[pos++] = '-';
			}
			final int numOfDigits = countDigits(i);
			int writePos = pos + numOfDigits;
			while (i >= 100) {
				int quotient = i / 100;
				int remainder = (i - quotient * 100) << 1;
				buffer[--writePos] = TWO_DIGIT_NUMBERS[remainder + 1];
				buffer[--writePos] = TWO_DIGIT_NUMBERS[remainder];
				i = quotient;
			}
			if (i < 10) {
				buffer[--writePos] = (byte) ('0' + i);
			} else {
				buffer[--writePos] = TWO_DIGIT_NUMBERS[(i << 1) + 1];
				buffer[--writePos] = TWO_DIGIT_NUMBERS[i << 1];
			}
			pos += numOfDigits;
		}

		private void fillBuffer(long l) {
			if ((int) l == l) {
				fillBuffer((int) l);
				return;
			}
			if (l == Long.MIN_VALUE) {
				buffer[pos++] = '-';
				buffer[pos++] = '9';
				buffer[pos++] = '2';
				buffer[pos++] = '2';
				buffer[pos++] = '3';
				buffer[pos++] = '3';
				buffer[pos++] = '7';
				buffer[pos++] = '2';
				buffer[pos++] = '0';
				buffer[pos++] = '3';
				buffer[pos++] = '6';
				buffer[pos++] = '8';
				buffer[pos++] = '5';
				buffer[pos++] = '4';
				buffer[pos++] = '7';
				buffer[pos++] = '7';
				buffer[pos++] = '5';
				buffer[pos++] = '8';
				buffer[pos++] = '0';
				buffer[pos++] = '8';
				return;
			}
			boolean negative = l < 0;
			if (negative) {
				l = -l;
				buffer[pos++] = '-';
			}
			final int numOfDigits = countDigits(l);
			int writePos = pos + numOfDigits;
			while (l >= 100) {
				long quotient = l / 100;
				int remainder = (int) (l - quotient * 100) << 1;
				buffer[--writePos] = TWO_DIGIT_NUMBERS[remainder + 1];
				buffer[--writePos] = TWO_DIGIT_NUMBERS[remainder];
				l = quotient;
			}
			if (l < 10) {
				buffer[--writePos] = (byte) ('0' + l);
			} else {
				buffer[--writePos] = TWO_DIGIT_NUMBERS[(int) (l << 1) + 1];
				buffer[--writePos] = TWO_DIGIT_NUMBERS[(int) l << 1];
			}
			pos += numOfDigits;
		}

		private int countDigits(final int i) {
			if (i < 10) return 1;
			if (i < 100) return 2;
			if (i < 1000) return 3;
			if (i < 10000) return 4;
			if (i < 100000) return 5;
			if (i < 1000000) return 6;
			if (i < 10000000) return 7;
			if (i < 100000000) return 8;
			if (i < 1000000000) return 9;
			return 10;
		}

		private int countDigits(final long l) {
			if (l < 10000000000L) return 10;
			if (l < 100000000000L) return 11;
			if (l < 1000000000000L) return 12;
			if (l < 10000000000000L) return 13;
			if (l < 100000000000000L) return 14;
			if (l < 1000000000000000L) return 15;
			if (l < 10000000000000000L) return 16;
			if (l < 100000000000000000L) return 17;
			if (l < 1000000000000000000L) return 18;
			return 19;
		}

		public void println(final int a, final int b) {
			println(a, b, '\n');
		}

		public void println(final int a, final long b) {
			println(a, b, '\n');
		}

		public void println(final long a, final int b) {
			println(a, b, '\n');
		}

		public void println(final long a, final long b) {
			println(a, b, '\n');
		}

		public void println(final long a, final long b, final char delimiter) {
			ensureBufferSpace((MAX_LONG_DIGITS << 1) + 2);
			fillBuffer(a);
			buffer[pos++] = (byte) delimiter;
			fillBuffer(b);
			buffer[pos++] = '\n';
			if (autoFlush) flush();
		}

		public void print(final int a, final int b) {
			print(a, b, ' ');
		}

		public void print(final int a, final long b) {
			print(a, b, ' ');
		}

		public void print(final long a, final int b) {
			print(a, b, ' ');
		}

		public void print(final long a, final long b) {
			print(a, b, ' ');
		}

		public void print(final long a, final long b, final char delimiter) {
			ensureBufferSpace((MAX_LONG_DIGITS << 1) + 1);
			fillBuffer(a);
			buffer[pos++] = (byte) delimiter;
			fillBuffer(b);
			if (autoFlush) flush();
		}

		public void println(final double d, final int n) {
			print(d, n);
			println();
		}

		public void print(double d, int n) {
			if (n == 0) {
				print(round(d));
				return;
			}
			if (d < 0) {
				ensureBufferSpace(1);
				buffer[pos++] = '-';
				d = -d;
			}
			d += pow(10, -n) / 2;
			print((long) d);
			ensureBufferSpace(n + 1);
			buffer[pos++] = '.';
			d -= (long) d;
			while (n-- > 0) {
				d *= 10;
				buffer[pos++] = (byte) ((int) d + '0');
				d -= (int) d;
			}
			if (autoFlush) flush();
		}

		public void println(final int[] arr) {
			println(arr, '\n');
		}

		public void println(final long[] arr) {
			println(arr, '\n');
		}

		public void println(final double[] arr) {
			println(arr, '\n');
		}

		public void println(final char[] arr) {
			println(arr, '\n');
		}

		public void println(final boolean[] arr) {
			println(arr, '\n');
		}

		public void println(final String[] arr) {
			println(arr, '\n');
		}

		public void println(final Object... arr) {
			if (arr == null) return;
			for (final Object o : arr)
				println(o);
		}

		public void println(final int[] arr, final char delimiter) {
			print(arr, delimiter);
			println();
		}

		public void println(final long[] arr, final char delimiter) {
			print(arr, delimiter);
			println();
		}

		public void println(final double[] arr, final char delimiter) {
			print(arr, delimiter);
			println();
		}

		public void println(final char[] arr, final char delimiter) {
			print(arr, delimiter);
			println();
		}

		public void println(final boolean[] arr, final char delimiter) {
			print(arr, delimiter);
			println();
		}

		public void println(final String[] arr, final char delimiter) {
			print(arr, delimiter);
			println();
		}

		public void print(final int[] arr) {
			print(arr, ' ');
		}

		public void print(final long[] arr) {
			print(arr, ' ');
		}

		public void print(final double[] arr) {
			print(arr, ' ');
		}

		public void print(final char[] arr) {
			print(arr, ' ');
		}

		public void print(final boolean[] arr) {
			print(arr, ' ');
		}

		public void print(final String[] arr) {
			print(arr, ' ');
		}

		public void print(final Object... arr) {
			if (arr == null) return;
			final int len = arr.length;
			if (len == 0) return;
			print(arr[0]);
			for (int i = 1; i < len; i++) {
				print(' ');
				print(arr[i]);
			}
		}

		public void print(final int[] arr, final char delimiter) {
			if (arr == null) return;
			final int len = arr.length;
			if (len == 0) return;
			print(arr[0]);
			for (int i = 1; i < len; i++) {
				ensureBufferSpace(MAX_INT_DIGITS + 1);
				buffer[pos++] = (byte) delimiter;
				fillBuffer(arr[i]);
			}
			if (autoFlush) flush();
		}

		public void print(final long[] arr, final char delimiter) {
			if (arr == null) return;
			final int len = arr.length;
			if (len == 0) return;
			print(arr[0]);
			for (int i = 1; i < len; i++) {
				ensureBufferSpace(MAX_LONG_DIGITS + 1);
				buffer[pos++] = (byte) delimiter;
				fillBuffer(arr[i]);
			}
			if (autoFlush) flush();
		}

		public void print(final double[] arr, final char delimiter) {
			if (arr == null) return;
			final int len = arr.length;
			if (len == 0) return;
			print(arr[0], 16);
			for (int i = 1; i < len; i++) {
				ensureBufferSpace(1);
				buffer[pos++] = (byte) delimiter;
				print(arr[i], 16);
			}
			if (autoFlush) flush();
		}

		public void print(final char[] arr, final char delimiter) {
			if (arr == null) return;
			final int len = arr.length;
			if (len == 0) return;
			print(arr[0]);
			int i = 1;
			while (i < len) {
				ensureBufferSpace(2);
				int limit = min(buffer.length - pos, len - i);
				while (limit-- > 0) {
					buffer[pos++] = (byte) delimiter;
					buffer[pos++] = (byte) arr[i++];
				}
			}
			if (autoFlush) flush();
		}

		public void print(final boolean[] arr, final char delimiter) {
			if (arr == null) return;
			final int len = arr.length;
			if (len == 0) return;
			print(arr[0]);
			for (int i = 1; i < len; i++) {
				ensureBufferSpace(4);
				buffer[pos++] = (byte) delimiter;
				fillBuffer(arr[i]);
			}
			if (autoFlush) flush();
		}

		public void print(final String[] arr, final char delimiter) {
			if (arr == null) return;
			final int len = arr.length;
			if (len == 0) return;
			print(arr[0]);
			for (int i = 1; i < len; i++) {
				print(delimiter);
				print(arr[i]);
			}
		}

		public <T> void println(final int[] arr, final IntFunction<T> function) {
			if (arr == null) return;
			if (function == null) {
				println(arr);
				return;
			}
			for (final int i : arr)
				println(function.apply(i));
		}

		public <T> void println(final long[] arr, final LongFunction<T> function) {
			if (arr == null) return;
			if (function == null) {
				println(arr);
				return;
			}
			for (final long l : arr)
				println(function.apply(l));
		}

		public <T> void println(final double[] arr, final DoubleFunction<T> function) {
			if (arr == null) return;
			if (function == null) {
				println(arr);
				return;
			}
			for (final double l : arr)
				println(function.apply(l));
		}

		public <T> void println(final char[] arr, final Function<Character, T> function) {
			if (arr == null) return;
			if (function == null) {
				println(arr);
				return;
			}
			for (final char c : arr)
				println(function.apply(c));
		}

		public <T> void println(final boolean[] arr, final Function<Boolean, T> function) {
			if (arr == null) return;
			if (function == null) {
				println(arr);
				return;
			}
			for (final boolean b : arr)
				println(function.apply(b));
		}

		public <T> void println(final String[] arr, final Function<String, T> function) {
			if (arr == null) return;
			if (function == null) {
				println(arr);
				return;
			}
			for (final String s : arr)
				println(function.apply(s));
		}

		public <T> void print(final int[] arr, final IntFunction<T> function) {
			if (arr == null) return;
			if (function == null) {
				print(arr, ' ');
				return;
			}
			final int len = arr.length;
			if (len == 0) return;
			print(function.apply(arr[0]));
			for (int i = 1; i < len; i++) {
				print(' ');
				print(function.apply(arr[i]));
			}
		}

		public <T> void print(final long[] arr, final LongFunction<T> function) {
			if (arr == null) return;
			if (function == null) {
				print(arr, ' ');
				return;
			}
			final int len = arr.length;
			if (len == 0) return;
			print(function.apply(arr[0]));
			for (int i = 1; i < len; i++) {
				print(' ');
				print(function.apply(arr[i]));
			}
		}

		public <T> void print(final double[] arr, final DoubleFunction<T> function) {
			if (arr == null) return;
			if (function == null) {
				print(arr, ' ');
				return;
			}
			final int len = arr.length;
			if (len == 0) return;
			print(function.apply(arr[0]));
			for (int i = 1; i < len; i++) {
				print(' ');
				print(function.apply(arr[i]));
			}
		}

		public <T> void print(final char[] arr, final Function<Character, T> function) {
			if (arr == null) return;
			if (function == null) {
				print(arr, ' ');
				return;
			}
			final int len = arr.length;
			if (len == 0) return;
			print(function.apply(arr[0]));
			for (int i = 1; i < len; i++) {
				print(' ');
				print(function.apply(arr[i]));
			}
		}

		public <T> void print(final boolean[] arr, final Function<Boolean, T> function) {
			if (arr == null) return;
			if (function == null) {
				print(arr, ' ');
				return;
			}
			final int len = arr.length;
			if (len == 0) return;
			print(function.apply(arr[0]));
			for (int i = 1; i < len; i++) {
				print(' ');
				print(function.apply(arr[i]));
			}
		}

		public <T> void print(final String[] arr, final Function<String, T> function) {
			if (arr == null) return;
			if (function == null) {
				print(arr, ' ');
				return;
			}
			final int len = arr.length;
			if (len == 0) return;
			print(function.apply(arr[0]));
			for (int i = 1; i < len; i++) {
				print(' ');
				print(function.apply(arr[i]));
			}
		}

		public void println(final int[][] arr2d) {
			println(arr2d, ' ');
		}

		public void println(final long[][] arr2d) {
			println(arr2d, ' ');
		}

		public void println(final double[][] arr2d) {
			println(arr2d, ' ');
		}

		public void println(final char[][] arr2d) {
			println(arr2d, ' ');
		}

		public void println(final boolean[][] arr2d) {
			println(arr2d, ' ');
		}

		public void println(final String[][] arr2d) {
			println(arr2d, ' ');
		}

		public void println(final Object[][] arr2d) {
			println(arr2d, ' ');
		}

		public void println(final int[][] arr2d, final char delimiter) {
			if (arr2d == null) return;
			for (final int[] arr : arr2d)
				println(arr, delimiter);
		}

		public void println(final long[][] arr2d, final char delimiter) {
			if (arr2d == null) return;
			for (final long[] arr : arr2d)
				println(arr, delimiter);
		}

		public void println(final double[][] arr2d, final char delimiter) {
			if (arr2d == null) return;
			for (final double[] arr : arr2d)
				println(arr, delimiter);
		}

		public void println(final char[][] arr2d, final char delimiter) {
			if (arr2d == null) return;
			for (final char[] arr : arr2d)
				println(arr, delimiter);
		}

		public void println(final boolean[][] arr2d, final char delimiter) {
			if (arr2d == null) return;
			for (final boolean[] arr : arr2d)
				println(arr, delimiter);
		}

		public void println(final String[][] arr2d, final char delimiter) {
			if (arr2d == null) return;
			for (final String[] arr : arr2d)
				println(arr, delimiter);
		}

		public void println(final Object[][] arr2d, final char delimiter) {
			if (arr2d == null) return;
			for (final Object[] arr : arr2d) {
				print(arr[0]);
				for (int i = 1; i < arr.length; i++) {
					print(delimiter);
					print(arr[i]);
				}
				println();
			}
		}

		public <T> void println(final int[][] arr2d, final IntFunction<T> function) {
			if (arr2d == null) return;
			if (function == null) {
				println(arr2d);
				return;
			}
			for (final int[] arr : arr2d) {
				print(arr, function);
				println();
			}
		}

		public <T> void println(final long[][] arr2d, final LongFunction<T> function) {
			if (arr2d == null) return;
			if (function == null) {
				println(arr2d);
				return;
			}
			for (final long[] arr : arr2d) {
				print(arr, function);
				println();
			}
		}

		public <T> void println(final double[][] arr2d, final DoubleFunction<T> function) {
			if (arr2d == null) return;
			if (function == null) {
				println(arr2d);
				return;
			}
			for (final double[] arr : arr2d) {
				print(arr, function);
				println();
			}
		}

		public <T> void println(final char[][] arr2d, final LongFunction<T> function) {
			if (arr2d == null) return;
			if (function == null) {
				println(arr2d);
				return;
			}
			for (final char[] arr : arr2d) {
				print(arr, function);
				println();
			}
		}

		public <T> void println(final boolean[][] arr2d, final Function<Boolean, T> function) {
			if (arr2d == null) return;
			if (function == null) {
				println(arr2d);
				return;
			}
			for (final boolean[] arr : arr2d) {
				print(arr, function);
				println();
			}
		}

		public <T> void println(final String[][] arr2d, final Function<String, T> function) {
			if (arr2d == null) return;
			if (function == null) {
				println(arr2d);
				return;
			}
			for (final String[] arr : arr2d) {
				print(arr, function);
				println();
			}
		}

		public void printChars(final char[] arr) {
			if (arr == null) return;
			int i = 0;
			final int len = arr.length;
			while (i < len) {
				ensureBufferSpace(1);
				int limit = min(buffer.length - pos, len - i);
				while (limit-- > 0) {
					buffer[pos++] = (byte) arr[i++];
				}
			}
			if (autoFlush) flush();
		}

		public void printChars(final char[] arr, final Function<Character, Character> function) {
			if (arr == null) return;
			if (function == null) {
				printChars(arr);
				return;
			}
			int i = 0;
			final int len = arr.length;
			while (i < len) {
				ensureBufferSpace(1);
				int limit = min(buffer.length - pos, len - i);
				while (limit-- > 0) {
					buffer[pos++] = (byte) function.apply(arr[i++]).charValue();
				}
			}
			if (autoFlush) flush();
		}

		public void printChars(final char[][] arr2d) {
			if (arr2d == null) return;
			for (final char[] arr : arr2d) {
				printChars(arr);
				println();
			}
		}

		public void printChars(final char[][] arr2d, final Function<Character, Character> function) {
			if (arr2d == null) return;
			if (function == null) {
				printChars(arr2d);
				return;
			}
			for (final char[] arr : arr2d) {
				printChars(arr, function);
				println();
			}
		}

		public <T> void println(final Iterable<T> iter) {
			print(iter, '\n');
			println();
		}

		public <T> void println(final Iterable<T> iter, final char delimiter) {
			print(iter, delimiter);
			println();
		}

		public <T, U> void println(final Iterable<T> iter, final Function<T, U> function) {
			print(iter, function, '\n');
			println();
		}

		public <T> void print(final Iterable<T> iter) {
			print(iter, ' ');
		}

		public <T> void print(final Iterable<T> iter, final char delimiter) {
			if (iter == null) return;
			boolean first = true;
			for (final T t : iter) {
				if (first) {
					first = false;
				} else {
					ensureBufferSpace(1);
					buffer[pos++] = (byte) delimiter;
				}
				print(t);
			}
		}

		public <T, U> void print(final Iterable<T> iter, final Function<T, U> function) {
			print(iter, function, ' ');
		}

		public <T, U> void print(final Iterable<T> iter, final Function<T, U> function, final char delimiter) {
			if (iter == null) return;
			if (function == null) {
				print(iter, delimiter);
				return;
			}
			boolean first = true;
			for (final T t : iter) {
				if (first) {
					first = false;
				} else {
					ensureBufferSpace(1);
					buffer[pos++] = (byte) delimiter;
				}
				print(function.apply(t));
			}
		}
	}
}
