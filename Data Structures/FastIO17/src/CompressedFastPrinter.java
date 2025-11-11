import sun.misc.Unsafe;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.Locale;
import java.util.function.*;

import static java.lang.Math.*;

@SuppressWarnings("unused")
// FastPrinterとContestPrinterを統合し、コメントを削除した圧縮版のクラスです。
// コメントが必要なく、文字数を気にするなら、こちらを使用して下さい。
public class CompressedFastPrinter {

	public static void main(String[] args) {
		try (FastPrinter out = new FastPrinter(System.out)) {
			System.err.println("--- FastIO17 CompressedFastPrinter Benchmark ---");
			long totalStartTime = System.nanoTime();

			final int N = 10_000_000;

			// --- Int Test ---
			long startTime = System.nanoTime();
			for (int i = 0; i < N; i++) {
				out.println(i);
			}
			long endTime = System.nanoTime();
			System.err.println("  - Int test (2,500,000 ops): " + (endTime - startTime) / 1_000_000 + " ms");

			// --- Long Test ---
			startTime = System.nanoTime();
			for (int i = 0; i < N; i++) {
				out.println((long) i * 1000);
			}
			endTime = System.nanoTime();
			System.err.println("  - Long test (2,500,000 ops): " + (endTime - startTime) / 1_000_000 + " ms");

			// --- Double Test ---
			startTime = System.nanoTime();
			for (int i = 0; i < N; i++) {
				out.println(i / 100.0);
			}
			endTime = System.nanoTime();
			System.err.println("  - Double test (2,500,000 ops): " + (endTime - startTime) / 1_000_000 + " ms");

			// --- Short String Test ---
			startTime = System.nanoTime();
			for (int i = 0; i < N; i++) {
				out.println("String" + i);
			}
			endTime = System.nanoTime();
			System.err.println("  - Short String test (2,500,000 ops): " + (endTime - startTime) / 1_000_000 + " ms");

			// --- Long String Test ---
			String longString = "0123456789".repeat(10_000);
			int longStringLoop = 1000;

			startTime = System.nanoTime();
			for (int i = 0; i < longStringLoop; i++) {
				out.println(longString);
			}
			endTime = System.nanoTime();
			System.err.println("  - Long String test (100,000 chars x " + longStringLoop + " ops): " + (endTime - startTime) / 1_000_000 + " ms");

			long totalEndTime = System.nanoTime();
			System.err.println("Total execution time: " + (totalEndTime - totalStartTime) / 1_000_000 + " ms");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static final class FastPrinter implements AutoCloseable {
		private static final int MAX_INT_DIGITS = 11;
		private static final int MAX_LONG_DIGITS = 20;
		private static final int DEFAULT_BUFFER_SIZE = 65536;
		private static final byte[] DigitTens = {
				'0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
				'1', '1', '1', '1', '1', '1', '1', '1', '1', '1',
				'2', '2', '2', '2', '2', '2', '2', '2', '2', '2',
				'3', '3', '3', '3', '3', '3', '3', '3', '3', '3',
				'4', '4', '4', '4', '4', '4', '4', '4', '4', '4',
				'5', '5', '5', '5', '5', '5', '5', '5', '5', '5',
				'6', '6', '6', '6', '6', '6', '6', '6', '6', '6',
				'7', '7', '7', '7', '7', '7', '7', '7', '7', '7',
				'8', '8', '8', '8', '8', '8', '8', '8', '8', '8',
				'9', '9', '9', '9', '9', '9', '9', '9', '9', '9',
		};
		private static final byte[] DigitOnes = {
				'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
		};
		private static final long[] POW10 = {
				1, 10, 100, 1_000, 10_000, 100_000, 1_000_000, 10_000_000, 100_000_000,
				1_000_000_000, 10_000_000_000L, 100_000_000_000L, 1_000_000_000_000L,
				10_000_000_000_000L, 100_000_000_000_000L, 1_000_000_000_000_000L,
				10_000_000_000_000_000L, 100_000_000_000_000_000L, 1_000_000_000_000_000_000L
		};
		private static final byte[] TRUE_BYTES = {'Y', 'e', 's'};
		private static final byte[] FALSE_BYTES = {'N', 'o'};
		private static final Unsafe UNSAFE;
		private static final long STRING_VALUE_OFFSET;
		private static final long ABSTRACT_STRING_BUILDER_VALUE_OFFSET;

		static {
			try {
				Field f = Unsafe.class.getDeclaredField("theUnsafe");
				f.setAccessible(true);
				UNSAFE = (Unsafe) f.get(null);
				STRING_VALUE_OFFSET = UNSAFE.objectFieldOffset(String.class.getDeclaredField("value"));
				Class<?> asbClass = Class.forName("java.lang.AbstractStringBuilder");
				ABSTRACT_STRING_BUILDER_VALUE_OFFSET = UNSAFE.objectFieldOffset(asbClass.getDeclaredField("value"));
			} catch (Exception e) {
				throw new RuntimeException("Unsafe initialization failed. Check Java version and environment.", e);
			}
		}

		private final byte[] buffer;
		private final boolean autoFlush;
		private final OutputStream out;
		private final int BUFFER_SIZE;
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
			this.BUFFER_SIZE = max(bufferSize, 64);
			this.buffer = new byte[BUFFER_SIZE];
			this.autoFlush = autoFlush;
		}

		@Override
		public void close() throws IOException {
			flush();
			if (out != System.out) out.close();
		}

		public void flush() {
			try {
				if (pos > 0) out.write(buffer, 0, pos);
				out.flush();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			pos = 0;
		}

		public FastPrinter println() {
			ensureBufferSpace(1);
			buffer[pos++] = '\n';
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter println(final int i) {
			ensureBufferSpace(MAX_INT_DIGITS + 1);
			fillBuffer(i);
			buffer[pos++] = '\n';
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter println(final long l) {
			ensureBufferSpace(MAX_LONG_DIGITS + 1);
			fillBuffer(l);
			buffer[pos++] = '\n';
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter println(final double d) {
			return println(Double.toString(d));
		}

		public FastPrinter println(final char c) {
			ensureBufferSpace(2);
			buffer[pos++] = (byte) c;
			buffer[pos++] = '\n';
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter println(final boolean b) {
			ensureBufferSpace(4);
			fillBuffer(b);
			buffer[pos++] = '\n';
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter println(final String s) {
			final byte[] src = (byte[]) UNSAFE.getObject(s, STRING_VALUE_OFFSET);
			fillBuffer(src, s.length());
			ensureBufferSpace(1);
			buffer[pos++] = '\n';
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter println(final StringBuilder s) {
			final byte[] src = (byte[]) UNSAFE.getObject(s, ABSTRACT_STRING_BUILDER_VALUE_OFFSET);
			final int len = s.length();
			fillBuffer(src, len);
			ensureBufferSpace(1);
			buffer[pos++] = '\n';
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter println(final Object o) {
			if (o == null) return this;
			if (o instanceof String s) {
				return println(s);
			} else if (o instanceof Long l) {
				return println(l.longValue());
			} else if (o instanceof Integer i) {
				return println(i.intValue());
			} else if (o instanceof Double d) {
				return println(d.toString());
			} else if (o instanceof Boolean b) {
				return println(b.booleanValue());
			} else if (o instanceof Character c) {
				return println(c.charValue());
			} else if (o instanceof int[] arr) {
				return println(arr);
			} else if (o instanceof long[] arr) {
				return println(arr);
			} else if (o instanceof double[] arr) {
				return println(arr);
			} else if (o instanceof boolean[] arr) {
				return println(arr);
			} else if (o instanceof char[] arr) {
				return println(arr);
			} else if (o instanceof String[] arr) {
				return println(arr);
			} else if (o instanceof Object[] arr) {
				return println(arr);
			} else {
				return println(o.toString());
			}
		}

		public FastPrinter println(final BigInteger bi) {
			return println(bi.toString());
		}

		public FastPrinter println(final BigDecimal bd) {
			return println(bd.toString());
		}

		public FastPrinter print(final int i) {
			ensureBufferSpace(MAX_INT_DIGITS);
			fillBuffer(i);
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter print(final long l) {
			ensureBufferSpace(MAX_LONG_DIGITS);
			fillBuffer(l);
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter print(final double d) {
			return print(Double.toString(d));
		}

		public FastPrinter print(final char c) {
			ensureBufferSpace(1);
			buffer[pos++] = (byte) c;
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter print(final boolean b) {
			ensureBufferSpace(3);
			fillBuffer(b);
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter print(final String s) {
			final byte[] src = (byte[]) UNSAFE.getObject(s, STRING_VALUE_OFFSET);
			fillBuffer(src, s.length());
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter print(final StringBuilder s) {
			final byte[] src = (byte[]) UNSAFE.getObject(s, ABSTRACT_STRING_BUILDER_VALUE_OFFSET);
			final int len = s.length();
			fillBuffer(src, len);
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter print(final Object o) {
			if (o == null) return this;
			if (o instanceof String s) {
				return print(s);
			} else if (o instanceof Long l) {
				return print(l.longValue());
			} else if (o instanceof Integer i) {
				return print(i.intValue());
			} else if (o instanceof Double d) {
				return print(d.toString());
			} else if (o instanceof Boolean b) {
				return print(b.booleanValue());
			} else if (o instanceof Character c) {
				return print(c.charValue());
			} else if (o instanceof int[] arr) {
				return print(arr);
			} else if (o instanceof long[] arr) {
				return print(arr);
			} else if (o instanceof double[] arr) {
				return print(arr);
			} else if (o instanceof boolean[] arr) {
				return print(arr);
			} else if (o instanceof char[] arr) {
				return print(arr);
			} else if (o instanceof String[] arr) {
				return print(arr);
			} else if (o instanceof Object[] arr) {
				return print(arr);
			} else {
				return print(o.toString());
			}
		}

		public FastPrinter print(final BigInteger bi) {
			return print(bi.toString());
		}

		public FastPrinter print(final BigDecimal bd) {
			return print(bd.toString());
		}

		public FastPrinter printf(final String format, final Object... args) {
			return print(String.format(format, args));
		}

		public FastPrinter printf(final Locale locale, final String format, final Object... args) {
			return print(String.format(locale, format, args));
		}

		private void ensureBufferSpace(final int size) {
			if (pos + size > BUFFER_SIZE) {
				try {
					out.write(buffer, 0, pos);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				pos = 0;
			}
		}

		private void fillBuffer(final byte[] src, final int len) {
			for (int i = 0; i < len; ) {
				ensureBufferSpace(1);
				int limit = min(BUFFER_SIZE - pos, len - i);
				System.arraycopy(src, i, buffer, pos, limit);
				pos += limit;
				i += limit;
			}
		}

		private void fillBuffer(final boolean b) {
			if (b) {
				System.arraycopy(TRUE_BYTES, 0, buffer, pos, 3);
				pos += 3;
			} else {
				System.arraycopy(FALSE_BYTES, 0, buffer, pos, 2);
				pos += 2;
			}
		}

		private void fillBuffer(int i) {
			if (i >= 0) i = -i;
			else buffer[pos++] = '-';
			int quotient, remainder;
			final int numOfDigits = countDigits(i);
			int writePos = pos + numOfDigits;
			while (i <= -100) {
				quotient = i / 100;
				remainder = (quotient << 6) + (quotient << 5) + (quotient << 2) - i;
				buffer[--writePos] = DigitOnes[remainder];
				buffer[--writePos] = DigitTens[remainder];
				i = quotient;
			}
			quotient = i / 10;
			remainder = (quotient << 3) + (quotient << 1) - i;
			buffer[--writePos] = (byte) ('0' + remainder);
			if (quotient < 0) buffer[--writePos] = (byte) ('0' - quotient);
			pos += numOfDigits;
		}

		private void fillBuffer(long l) {
			if (l >= 0) l = -l;
			else buffer[pos++] = '-';
			long quotient;
			int remainder;
			final int numOfDigits = countDigits(l);
			int writePos = pos + numOfDigits;
			while (l <= -100) {
				quotient = l / 100;
				remainder = (int) ((quotient << 6) + (quotient << 5) + (quotient << 2) - l);
				buffer[--writePos] = DigitOnes[remainder];
				buffer[--writePos] = DigitTens[remainder];
				l = quotient;
			}
			quotient = l / 10;
			remainder = (int) ((quotient << 3) + (quotient << 1) - l);
			buffer[--writePos] = (byte) ('0' + remainder);
			if (quotient < 0) buffer[--writePos] = (byte) ('0' - quotient);
			pos += numOfDigits;
		}

		private int countDigits(int i) {
			if (i > -100000) {
				if (i > -100) {
					return i > -10 ? 1 : 2;
				} else {
					if (i > -10000) return i > -1000 ? 3 : 4;
					else return 5;
				}
			} else {
				if (i > -10000000) {
					return i > -1000000 ? 6 : 7;
				} else {
					if (i > -1000000000) return i > -100000000 ? 8 : 9;
					else return 10;
				}
			}
		}

		private int countDigits(long l) {
			if (l > -1000000000) {
				if (l > -10000) {
					if (l > -100) {
						return l > -10 ? 1 : 2;
					} else {
						return l > -1000 ? 3 : 4;
					}
				} else {
					if (l > -1000000) {
						return l > -100000 ? 5 : 6;
					} else {
						if (l > -100000000) return l > -10000000 ? 7 : 8;
						else return 9;
					}
				}
			} else {
				if (l > -10000000000000L) {
					if (l > -100000000000L) {
						return l > -10000000000L ? 10 : 11;
					} else {
						return l > -1000000000000L ? 12 : 13;
					}
				} else {
					if (l > -10000000000000000L) {
						if (l > -1000000000000000L) return l > -100000000000000L ? 14 : 15;
						else return 16;
					} else {
						if (l > -1000000000000000000L) return l > -100000000000000000L ? 17 : 18;
						else return 19;
					}
				}
			}
		}

		public FastPrinter println(final int a, final int b) {
			return println(a, b, '\n');
		}

		public FastPrinter println(final int a, final long b) {
			return println(a, b, '\n');
		}

		public FastPrinter println(final long a, final int b) {
			return println(a, b, '\n');
		}

		public FastPrinter println(final long a, final long b) {
			return println(a, b, '\n');
		}

		public FastPrinter println(final long a, final long b, final char delimiter) {
			ensureBufferSpace((MAX_LONG_DIGITS << 1) + 2);
			fillBuffer(a);
			buffer[pos++] = (byte) delimiter;
			fillBuffer(b);
			buffer[pos++] = '\n';
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter print(final int a, final int b) {
			return print(a, b, ' ');
		}

		public FastPrinter print(final int a, final long b) {
			return print(a, b, ' ');
		}

		public FastPrinter print(final long a, final int b) {
			return print(a, b, ' ');
		}

		public FastPrinter print(final long a, final long b) {
			return print(a, b, ' ');
		}

		public FastPrinter print(final long a, final long b, final char delimiter) {
			ensureBufferSpace((MAX_LONG_DIGITS << 1) + 1);
			fillBuffer(a);
			buffer[pos++] = (byte) delimiter;
			fillBuffer(b);
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter println(final double d, final int n) {
			return print(d, n).println();
		}

		public FastPrinter print(double d, int n) {
			if (n <= 0) return print(round(d));
			if (d >= 0) {
			} else {
				ensureBufferSpace(1);
				buffer[pos++] = '-';
				d = -d;
			}
			if (n > 18) n = 18;
			long intPart = (long) d;
			long fracPart = (long) ((d - intPart) * POW10[n]);
			print(intPart);
			int digits = n - countDigits(-fracPart);
			ensureBufferSpace(digits + 1);
			buffer[pos++] = '.';
			while (digits-- > 0) buffer[pos++] = '0';
			print(fracPart);
			return this;
		}

		public FastPrinter println(final int[] arr) {
			return println(arr, '\n');
		}

		public FastPrinter println(final long[] arr) {
			return println(arr, '\n');
		}

		public FastPrinter println(final double[] arr) {
			return println(arr, '\n');
		}

		public FastPrinter println(final char[] arr) {
			return println(arr, '\n');
		}

		public FastPrinter println(final boolean[] arr) {
			return println(arr, '\n');
		}

		public FastPrinter println(final String[] arr) {
			return println(arr, '\n');
		}

		public FastPrinter println(final Object... arr) {
			for (final Object o : arr) println(o);
			return this;
		}

		public FastPrinter println(final int[] arr, final char delimiter) {
			return print(arr, delimiter).println();
		}

		public FastPrinter println(final long[] arr, final char delimiter) {
			return print(arr, delimiter).println();
		}

		public FastPrinter println(final double[] arr, final char delimiter) {
			return print(arr, delimiter).println();
		}

		public FastPrinter println(final char[] arr, final char delimiter) {
			return print(arr, delimiter).println();
		}

		public FastPrinter println(final boolean[] arr, final char delimiter) {
			return print(arr, delimiter).println();
		}

		public FastPrinter println(final String[] arr, final char delimiter) {
			return print(arr, delimiter).println();
		}

		public FastPrinter print(final int[] arr) {
			return print(arr, ' ');
		}

		public FastPrinter print(final long[] arr) {
			return print(arr, ' ');
		}

		public FastPrinter print(final double[] arr) {
			return print(arr, ' ');
		}

		public FastPrinter print(final char[] arr) {
			return print(arr, ' ');
		}

		public FastPrinter print(final boolean[] arr) {
			return print(arr, ' ');
		}

		public FastPrinter print(final String[] arr) {
			return print(arr, ' ');
		}

		public FastPrinter print(final Object... arr) {
			final int len = arr.length;
			if (len > 0) print(arr[0]);
			for (int i = 1; i < len; i++) {
				ensureBufferSpace(1);
				buffer[pos++] = ' ';
				print(arr[i]);
			}
			return this;
		}

		public FastPrinter print(final int[] arr, final char delimiter) {
			final int len = arr.length;
			if (len > 0) print(arr[0]);
			for (int i = 1; i < len; i++) {
				ensureBufferSpace(MAX_INT_DIGITS + 1);
				buffer[pos++] = (byte) delimiter;
				fillBuffer(arr[i]);
			}
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter print(final long[] arr, final char delimiter) {
			final int len = arr.length;
			if (len > 0) print(arr[0]);
			for (int i = 1; i < len; i++) {
				ensureBufferSpace(MAX_LONG_DIGITS + 1);
				buffer[pos++] = (byte) delimiter;
				fillBuffer(arr[i]);
			}
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter print(final double[] arr, final char delimiter) {
			final int len = arr.length;
			if (len > 0) print(arr[0], 16);
			for (int i = 1; i < len; i++) {
				ensureBufferSpace(1);
				buffer[pos++] = (byte) delimiter;
				print(arr[i], 16);
			}
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter print(final char[] arr, final char delimiter) {
			final int len = arr.length;
			if (len > 0) print(arr[0]);
			int i = 1;
			while (i < len) {
				ensureBufferSpace(2);
				int limit = min((BUFFER_SIZE - pos) >> 1, len - i);
				while (limit-- > 0) {
					buffer[pos++] = (byte) delimiter;
					buffer[pos++] = (byte) arr[i++];
				}
			}
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter print(final boolean[] arr, final char delimiter) {
			final int len = arr.length;
			if (len > 0) print(arr[0]);
			for (int i = 1; i < len; i++) {
				ensureBufferSpace(4);
				buffer[pos++] = (byte) delimiter;
				fillBuffer(arr[i]);
			}
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter print(final String[] arr, final char delimiter) {
			final int len = arr.length;
			if (len > 0) print(arr[0]);
			for (int i = 1; i < len; i++) print(delimiter).print(arr[i]);
			return this;
		}

		public <T> FastPrinter println(final int[] arr, final IntFunction<T> function) {
			for (final int i : arr) println(function.apply(i));
			return this;
		}

		public <T> FastPrinter println(final long[] arr, final LongFunction<T> function) {
			for (final long l : arr) println(function.apply(l));
			return this;
		}

		public <T> FastPrinter println(final double[] arr, final DoubleFunction<T> function) {
			for (final double l : arr) println(function.apply(l));
			return this;
		}

		public <T> FastPrinter println(final char[] arr, final IntFunction<T> function) {
			for (final char c : arr) println(function.apply(c));
			return this;
		}

		public <T> FastPrinter println(final boolean[] arr, final Function<Boolean, T> function) {
			for (final boolean b : arr) println(function.apply(b));
			return this;
		}

		public <T> FastPrinter println(final String[] arr, final Function<String, T> function) {
			for (final String s : arr) println(function.apply(s));
			return this;
		}

		public <T> FastPrinter print(final int[] arr, final IntFunction<T> function) {
			final int len = arr.length;
			if (len > 0) print(function.apply(arr[0]));
			for (int i = 1; i < len; i++) {
				ensureBufferSpace(1);
				buffer[pos++] = ' ';
				print(function.apply(arr[i]));
			}
			return this;
		}

		public <T> FastPrinter print(final long[] arr, final LongFunction<T> function) {
			final int len = arr.length;
			if (len > 0) print(function.apply(arr[0]));
			for (int i = 1; i < len; i++) {
				ensureBufferSpace(1);
				buffer[pos++] = ' ';
				print(function.apply(arr[i]));
			}
			return this;
		}

		public <T> FastPrinter print(final double[] arr, final DoubleFunction<T> function) {
			final int len = arr.length;
			if (len > 0) print(function.apply(arr[0]));
			for (int i = 1; i < len; i++) {
				ensureBufferSpace(1);
				buffer[pos++] = ' ';
				print(function.apply(arr[i]));
			}
			return this;
		}

		public <T> FastPrinter print(final char[] arr, final IntFunction<T> function) {
			final int len = arr.length;
			if (len > 0) print(function.apply(arr[0]));
			for (int i = 1; i < len; i++) {
				ensureBufferSpace(1);
				buffer[pos++] = ' ';
				print(function.apply(arr[i]));
			}
			return this;
		}

		public <T> FastPrinter print(final boolean[] arr, final Function<Boolean, T> function) {
			final int len = arr.length;
			if (len > 0) print(function.apply(arr[0]));
			for (int i = 1; i < len; i++) {
				ensureBufferSpace(1);
				buffer[pos++] = ' ';
				print(function.apply(arr[i]));
			}
			return this;
		}

		public <T> FastPrinter print(final String[] arr, final Function<String, T> function) {
			final int len = arr.length;
			if (len > 0) print(function.apply(arr[0]));
			for (int i = 1; i < len; i++) {
				ensureBufferSpace(1);
				buffer[pos++] = ' ';
				print(function.apply(arr[i]));
			}
			return this;
		}

		public FastPrinter println(final int[][] arr2d) {
			return println(arr2d, ' ');
		}

		public FastPrinter println(final long[][] arr2d) {
			return println(arr2d, ' ');
		}

		public FastPrinter println(final double[][] arr2d) {
			return println(arr2d, ' ');
		}

		public FastPrinter println(final char[][] arr2d) {
			return println(arr2d, ' ');
		}

		public FastPrinter println(final boolean[][] arr2d) {
			return println(arr2d, ' ');
		}

		public FastPrinter println(final String[][] arr2d) {
			return println(arr2d, ' ');
		}

		public FastPrinter println(final Object[][] arr2d) {
			return println(arr2d, ' ');
		}

		public FastPrinter println(final int[][] arr2d, final char delimiter) {
			for (final int[] arr : arr2d) println(arr, delimiter);
			return this;
		}

		public FastPrinter println(final long[][] arr2d, final char delimiter) {
			for (final long[] arr : arr2d) println(arr, delimiter);
			return this;
		}

		public FastPrinter println(final double[][] arr2d, final char delimiter) {
			for (final double[] arr : arr2d) println(arr, delimiter);
			return this;
		}

		public FastPrinter println(final char[][] arr2d, final char delimiter) {
			for (final char[] arr : arr2d) println(arr, delimiter);
			return this;
		}

		public FastPrinter println(final boolean[][] arr2d, final char delimiter) {
			for (final boolean[] arr : arr2d) println(arr, delimiter);
			return this;
		}

		public FastPrinter println(final String[][] arr2d, final char delimiter) {
			for (final String[] arr : arr2d) println(arr, delimiter);
			return this;
		}

		public FastPrinter println(final Object[][] arr2d, final char delimiter) {
			for (final Object[] arr : arr2d) {
				int len = arr.length;
				if (len > 0) print(arr[0]);
				for (int i = 1; i < len; i++) {
					ensureBufferSpace(1);
					buffer[pos++] = (byte) delimiter;
					print(arr[i]);
				}
				println();
			}
			return this;
		}

		public <T> FastPrinter println(final int[][] arr2d, final IntFunction<T> function) {
			for (final int[] arr : arr2d) print(arr, function).println();
			return this;
		}

		public <T> FastPrinter println(final long[][] arr2d, final LongFunction<T> function) {
			for (final long[] arr : arr2d) print(arr, function).println();
			return this;
		}

		public <T> FastPrinter println(final double[][] arr2d, final DoubleFunction<T> function) {
			for (final double[] arr : arr2d) print(arr, function).println();
			return this;
		}

		public <T> FastPrinter println(final char[][] arr2d, final IntFunction<T> function) {
			for (final char[] arr : arr2d) print(arr, function).println();
			return this;
		}

		public <T> FastPrinter println(final boolean[][] arr2d, final Function<Boolean, T> function) {
			for (final boolean[] arr : arr2d) print(arr, function).println();
			return this;
		}

		public <T> FastPrinter println(final String[][] arr2d, final Function<String, T> function) {
			for (final String[] arr : arr2d) print(arr, function).println();
			return this;
		}

		public FastPrinter printChars(final char[] arr) {
			int i = 0;
			final int len = arr.length;
			while (i < len) {
				ensureBufferSpace(1);
				int limit = min(BUFFER_SIZE - pos, len - i);
				while (limit-- > 0) buffer[pos++] = (byte) arr[i++];
			}
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter printChars(final char[] arr, final IntUnaryOperator function) {
			int i = 0;
			final int len = arr.length;
			while (i < len) {
				ensureBufferSpace(1);
				int limit = min(BUFFER_SIZE - pos, len - i);
				while (limit-- > 0) buffer[pos++] = (byte) function.applyAsInt(arr[i++]);
			}
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter printChars(final char[][] arr2d) {
			for (final char[] arr : arr2d) printChars(arr).println();
			return this;
		}

		public FastPrinter printChars(final char[][] arr2d, final IntUnaryOperator function) {
			for (final char[] arr : arr2d) printChars(arr, function).println();
			return this;
		}

		public <T> FastPrinter println(final Iterable<T> iter) {
			return print(iter, '\n').println();
		}

		public <T> FastPrinter println(final Iterable<T> iter, final char delimiter) {
			return print(iter, delimiter).println();
		}

		public <T, U> FastPrinter println(final Iterable<T> iter, final Function<T, U> function) {
			return print(iter, function, '\n').println();
		}

		public <T> FastPrinter print(final Iterable<T> iter) {
			return print(iter, ' ');
		}

		public <T> FastPrinter print(final Iterable<T> iter, final char delimiter) {
			final Iterator<T> it = iter.iterator();
			if (it.hasNext()) print(it.next());
			while (it.hasNext()) {
				ensureBufferSpace(1);
				buffer[pos++] = (byte) delimiter;
				print(it.next());
			}
			return this;
		}

		public <T, U> FastPrinter print(final Iterable<T> iter, final Function<T, U> function) {
			return print(iter, function, ' ');
		}

		public <T, U> FastPrinter print(final Iterable<T> iter, final Function<T, U> function, final char delimiter) {
			final Iterator<T> it = iter.iterator();
			if (it.hasNext()) print(function.apply(it.next()));
			while (it.hasNext()) {
				ensureBufferSpace(1);
				buffer[pos++] = (byte) delimiter;
				print(function.apply(it.next()));
			}
			return this;
		}
	}
}
