import sun.misc.Unsafe;

import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.*;

import static java.lang.Math.*;
import static java.util.Arrays.sort;

public class TestOutputStrings {

	public static void main(String[] args) throws FileNotFoundException {
		int len = args.length;
		final int which = len >= 1 ? Integer.parseInt(args[0]) : 0;
		final int buf = len >= 2 ? Integer.parseInt(args[1]) : 65536;
		FastScanner sc = new FastScanner();
		ArrayList<String> data = new ArrayList<>();
		while (sc.hasNext()) data.add(sc.next());
		if (which == 0) {
			double ms = benchFastPrinter0From(buf, data);
			PrintStream out = new PrintStream(new FileOutputStream("Output\\output4.txt", true));
			System.setOut(out);
			System.out.printf("FastPrinter0: %.3f ms (buf=%d)%n", ms, buf);
		} else {
			double ms = benchFastPrinter1From(buf, data);
			PrintStream out = new PrintStream(new FileOutputStream("Output\\output4.txt", true));
			System.setOut(out);
			System.out.printf("FastPrinter1: %.3f ms (buf=%d)%n", ms, buf);
		}
	}

	private static double benchFastPrinter0From(final int bufferSize, ArrayList<String> data) {
		final long t0 = System.nanoTime();
		try (final FastPrinter0 out = new FastPrinter0(bufferSize)) {
			for (String s : data) out.println(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
		final long t1 = System.nanoTime();
		return (t1 - t0) / 1_000_000.0;
	}

	private static double benchFastPrinter1From(final int bufferSize, ArrayList<String> data) {
		final long t0 = System.nanoTime();
		try (final FastPrinter1 out = new FastPrinter1(bufferSize)) {
			for (String s : data) out.println(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
		final long t1 = System.nanoTime();
		return (t1 - t0) / 1_000_000.0;
	}

	@SuppressWarnings("unused")
	private static final class FastPrinter0 implements AutoCloseable {
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
		private final byte[] buffer;
		private final boolean autoFlush;
		private final OutputStream out;
		private int pos = 0;

		public FastPrinter0() {
			this(System.out, DEFAULT_BUFFER_SIZE, false);
		}

		public FastPrinter0(final OutputStream out) {
			this(out, DEFAULT_BUFFER_SIZE, false);
		}

		public FastPrinter0(final int bufferSize) {
			this(System.out, bufferSize, false);
		}

		public FastPrinter0(final boolean autoFlush) {
			this(System.out, DEFAULT_BUFFER_SIZE, autoFlush);
		}

		public FastPrinter0(final OutputStream out, final boolean autoFlush) {
			this(out, DEFAULT_BUFFER_SIZE, autoFlush);
		}

		public FastPrinter0(final int bufferSize, final boolean autoFlush) {
			this(System.out, bufferSize, autoFlush);
		}

		public FastPrinter0(final OutputStream out, final int bufferSize) {
			this(out, bufferSize, false);
		}

		public FastPrinter0(final OutputStream out, final int bufferSize, final boolean autoFlush) {
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

		private void fillBuffer(long l) {
			if (l < 0) {
				buffer[pos++] = '-';
			} else {
				l = -l;
			}
			long quotient;
			int remainder;
			final int numOfDigits = countDigits(l);
			int writePos = pos + numOfDigits;
			while (l <= -100) {
				quotient = l / 100;
				remainder = (int) (quotient * 100 - l);
				buffer[--writePos] = DigitOnes[remainder];
				buffer[--writePos] = DigitTens[remainder];
				l = quotient;
			}
			quotient = l / 10;
			remainder = (int) (quotient * 10 - l);
			buffer[--writePos] = (byte) ('0' + remainder);
			if (quotient < 0) {
				buffer[--writePos] = (byte) ('0' - quotient);
			}
			pos += numOfDigits;
		}

		private int countDigits(final long l) {
			if (l > -10) return 1;
			if (l > -100) return 2;
			if (l > -1000) return 3;
			if (l > -10000) return 4;
			if (l > -100000) return 5;
			if (l > -1000000) return 6;
			if (l > -10000000) return 7;
			if (l > -100000000) return 8;
			if (l > -1000000000) return 9;
			if (l > -10000000000L) return 10;
			if (l > -100000000000L) return 11;
			if (l > -1000000000000L) return 12;
			if (l > -10000000000000L) return 13;
			if (l > -100000000000000L) return 14;
			if (l > -1000000000000000L) return 15;
			if (l > -10000000000000000L) return 16;
			if (l > -100000000000000000L) return 17;
			if (l > -1000000000000000000L) return 18;
			return 19;
		}

	}

	@SuppressWarnings("unused")
	private static final class FastPrinter1 implements AutoCloseable {
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
		private static final byte[] TRUE_BYTES = {89, 101, 115};
		private static final byte[] FALSE_BYTES = {78, 111};
		private static final Unsafe UNSAFE;
		private static final long STRING_VALUE_OFFSET;
		private static final long ABSTRACT_STRING_BUILDER_VALUE_OFFSET;
		private static final long ABSTRACT_STRING_BUILDER_COUNT_OFFSET;

		static {
			try {
				Field f = Unsafe.class.getDeclaredField("theUnsafe");
				f.setAccessible(true);
				UNSAFE = (Unsafe) f.get(null);
				STRING_VALUE_OFFSET = UNSAFE.objectFieldOffset(String.class.getDeclaredField("value"));
				Class<?> asbClass = Class.forName("java.lang.AbstractStringBuilder");
				ABSTRACT_STRING_BUILDER_VALUE_OFFSET = UNSAFE.objectFieldOffset(asbClass.getDeclaredField("value"));
				ABSTRACT_STRING_BUILDER_COUNT_OFFSET = UNSAFE.objectFieldOffset(asbClass.getDeclaredField("count"));
			} catch (Exception e) {
				throw new RuntimeException("Unsafe initialization failed. Check Java version and environment.", e);
			}
		}

		private final byte[] buffer;
		private final boolean autoFlush;
		private final OutputStream out;
		private final int BUFFER_SIZE;
		private int pos = 0;

		public FastPrinter1() {
			this(System.out, DEFAULT_BUFFER_SIZE, false);
		}

		public FastPrinter1(final OutputStream out) {
			this(out, DEFAULT_BUFFER_SIZE, false);
		}

		public FastPrinter1(final int bufferSize) {
			this(System.out, bufferSize, false);
		}

		public FastPrinter1(final boolean autoFlush) {
			this(System.out, DEFAULT_BUFFER_SIZE, autoFlush);
		}

		public FastPrinter1(final OutputStream out, final boolean autoFlush) {
			this(out, DEFAULT_BUFFER_SIZE, autoFlush);
		}

		public FastPrinter1(final int bufferSize, final boolean autoFlush) {
			this(System.out, bufferSize, autoFlush);
		}

		public FastPrinter1(final OutputStream out, final int bufferSize) {
			this(out, bufferSize, false);
		}

		public FastPrinter1(final OutputStream out, final int bufferSize, final boolean autoFlush) {
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

		public FastPrinter1 println() {
			ensureBufferSpace(1);
			buffer[pos++] = '\n';
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter1 println(final int i) {
			ensureBufferSpace(MAX_INT_DIGITS + 1);
			fillBuffer(i);
			buffer[pos++] = '\n';
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter1 println(final long l) {
			ensureBufferSpace(MAX_LONG_DIGITS + 1);
			fillBuffer(l);
			buffer[pos++] = '\n';
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter1 println(final double d) {
			return println(Double.toString(d));
		}

		public FastPrinter1 println(final char c) {
			ensureBufferSpace(2);
			buffer[pos++] = (byte) c;
			buffer[pos++] = '\n';
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter1 println(final boolean b) {
			ensureBufferSpace(4);
			fillBuffer(b);
			buffer[pos++] = '\n';
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter1 println(final String s) {
			final byte[] src = (byte[]) UNSAFE.getObject(s, STRING_VALUE_OFFSET);
			fillBuffer(src, s.length());
			ensureBufferSpace(1);
			buffer[pos++] = '\n';
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter1 println(final StringBuilder s) {
			final byte[] src = (byte[]) UNSAFE.getObject(s, ABSTRACT_STRING_BUILDER_VALUE_OFFSET);
			final int len = UNSAFE.getInt(s, ABSTRACT_STRING_BUILDER_COUNT_OFFSET);
			fillBuffer(src, len);
			ensureBufferSpace(1);
			buffer[pos++] = '\n';
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter1 println(final Object o) {
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

		public FastPrinter1 println(final BigInteger bi) {
			return println(bi.toString());
		}

		public FastPrinter1 println(final BigDecimal bd) {
			return println(bd.toString());
		}

		public FastPrinter1 print(final int i) {
			ensureBufferSpace(MAX_INT_DIGITS);
			fillBuffer(i);
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter1 print(final long l) {
			ensureBufferSpace(MAX_LONG_DIGITS);
			fillBuffer(l);
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter1 print(final double d) {
			return print(Double.toString(d));
		}

		public FastPrinter1 print(final char c) {
			ensureBufferSpace(1);
			buffer[pos++] = (byte) c;
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter1 print(final boolean b) {
			ensureBufferSpace(3);
			fillBuffer(b);
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter1 print(final String s) {
			final byte[] src = (byte[]) UNSAFE.getObject(s, STRING_VALUE_OFFSET);
			fillBuffer(src, s.length());
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter1 print(final StringBuilder s) {
			final byte[] src = (byte[]) UNSAFE.getObject(s, ABSTRACT_STRING_BUILDER_VALUE_OFFSET);
			final int len = UNSAFE.getInt(s, ABSTRACT_STRING_BUILDER_COUNT_OFFSET);
			fillBuffer(src, len);
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter1 print(final Object o) {
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

		public FastPrinter1 print(final BigInteger bi) {
			return print(bi.toString());
		}

		public FastPrinter1 print(final BigDecimal bd) {
			return print(bd.toString());
		}

		public FastPrinter1 printf(final String format, final Object... args) {
			return print(String.format(format, args));
		}

		public FastPrinter1 printf(final Locale locale, final String format, final Object... args) {
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
						return l > -1000L ? 3 : 4;
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

		public FastPrinter1 println(final int a, final int b) {
			return println(a, b, '\n');
		}

		public FastPrinter1 println(final int a, final long b) {
			return println(a, b, '\n');
		}

		public FastPrinter1 println(final long a, final int b) {
			return println(a, b, '\n');
		}

		public FastPrinter1 println(final long a, final long b) {
			return println(a, b, '\n');
		}

		public FastPrinter1 println(final long a, final long b, final char delimiter) {
			ensureBufferSpace((MAX_LONG_DIGITS << 1) + 2);
			fillBuffer(a);
			buffer[pos++] = (byte) delimiter;
			fillBuffer(b);
			buffer[pos++] = '\n';
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter1 print(final int a, final int b) {
			return print(a, b, ' ');
		}

		public FastPrinter1 print(final int a, final long b) {
			return print(a, b, ' ');
		}

		public FastPrinter1 print(final long a, final int b) {
			return print(a, b, ' ');
		}

		public FastPrinter1 print(final long a, final long b) {
			return print(a, b, ' ');
		}

		public FastPrinter1 print(final long a, final long b, final char delimiter) {
			ensureBufferSpace((MAX_LONG_DIGITS << 1) + 1);
			fillBuffer(a);
			buffer[pos++] = (byte) delimiter;
			fillBuffer(b);
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter1 println(final double d, final int n) {
			return print(d, n).println();
		}

		public FastPrinter1 print(double d, int n) {
			if (n <= 0) return print(round(d));
			if (d >= 0) {
			} else {
				ensureBufferSpace(1);
				buffer[pos++] = '-';
				d = -d;
			}
			if (n > 18) n = 18;
			long scale = POW10[n];
			long scaled = (long) floor(d * scale);
			long intPart = scaled / scale;
			long fracPart = scaled - intPart * scale;
			print(intPart);
			ensureBufferSpace(n + 1);
			buffer[pos++] = '.';
			int digits = 1;
			for (long t = fracPart; t >= 10; t /= 10) digits++;
			for (int pad = n - digits; pad > 0; pad--) buffer[pos++] = '0';
			fillBuffer(fracPart);
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter1 println(final int[] arr) {
			return println(arr, '\n');
		}

		public FastPrinter1 println(final long[] arr) {
			return println(arr, '\n');
		}

		public FastPrinter1 println(final double[] arr) {
			return println(arr, '\n');
		}

		public FastPrinter1 println(final char[] arr) {
			return println(arr, '\n');
		}

		public FastPrinter1 println(final boolean[] arr) {
			return println(arr, '\n');
		}

		public FastPrinter1 println(final String[] arr) {
			return println(arr, '\n');
		}

		public FastPrinter1 println(final Object... arr) {
			for (final Object o : arr) println(o);
			return this;
		}

		public FastPrinter1 println(final int[] arr, final char delimiter) {
			return print(arr, delimiter).println();
		}

		public FastPrinter1 println(final long[] arr, final char delimiter) {
			return print(arr, delimiter).println();
		}

		public FastPrinter1 println(final double[] arr, final char delimiter) {
			return print(arr, delimiter).println();
		}

		public FastPrinter1 println(final char[] arr, final char delimiter) {
			return print(arr, delimiter).println();
		}

		public FastPrinter1 println(final boolean[] arr, final char delimiter) {
			return print(arr, delimiter).println();
		}

		public FastPrinter1 println(final String[] arr, final char delimiter) {
			return print(arr, delimiter).println();
		}

		public FastPrinter1 print(final int[] arr) {
			return print(arr, ' ');
		}

		public FastPrinter1 print(final long[] arr) {
			return print(arr, ' ');
		}

		public FastPrinter1 print(final double[] arr) {
			return print(arr, ' ');
		}

		public FastPrinter1 print(final char[] arr) {
			return print(arr, ' ');
		}

		public FastPrinter1 print(final boolean[] arr) {
			return print(arr, ' ');
		}

		public FastPrinter1 print(final String[] arr) {
			return print(arr, ' ');
		}

		public FastPrinter1 print(final Object... arr) {
			final int len = arr.length;
			if (len > 0) print(arr[0]);
			for (int i = 1; i < len; i++) {
				ensureBufferSpace(1);
				buffer[pos++] = ' ';
				print(arr[i]);
			}
			return this;
		}

		public FastPrinter1 print(final int[] arr, final char delimiter) {
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

		public FastPrinter1 print(final long[] arr, final char delimiter) {
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

		public FastPrinter1 print(final double[] arr, final char delimiter) {
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

		public FastPrinter1 print(final char[] arr, final char delimiter) {
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

		public FastPrinter1 print(final boolean[] arr, final char delimiter) {
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

		public FastPrinter1 print(final String[] arr, final char delimiter) {
			final int len = arr.length;
			if (len > 0) print(arr[0]);
			for (int i = 1; i < len; i++) print(delimiter).print(arr[i]);
			return this;
		}

		public <T> FastPrinter1 println(final int[] arr, final IntFunction<T> function) {
			for (final int i : arr) println(function.apply(i));
			return this;
		}

		public <T> FastPrinter1 println(final long[] arr, final LongFunction<T> function) {
			for (final long l : arr) println(function.apply(l));
			return this;
		}

		public <T> FastPrinter1 println(final double[] arr, final DoubleFunction<T> function) {
			for (final double l : arr) println(function.apply(l));
			return this;
		}

		public <T> FastPrinter1 println(final char[] arr, final IntFunction<T> function) {
			for (final char c : arr) println(function.apply(c));
			return this;
		}

		public <T> FastPrinter1 println(final boolean[] arr, final Function<Boolean, T> function) {
			for (final boolean b : arr) println(function.apply(b));
			return this;
		}

		public <T> FastPrinter1 println(final String[] arr, final Function<String, T> function) {
			for (final String s : arr) println(function.apply(s));
			return this;
		}

		public <T> FastPrinter1 print(final int[] arr, final IntFunction<T> function) {
			final int len = arr.length;
			if (len > 0) print(function.apply(arr[0]));
			for (int i = 1; i < len; i++) {
				ensureBufferSpace(1);
				buffer[pos++] = ' ';
				print(function.apply(arr[i]));
			}
			return this;
		}

		public <T> FastPrinter1 print(final long[] arr, final LongFunction<T> function) {
			final int len = arr.length;
			if (len > 0) print(function.apply(arr[0]));
			for (int i = 1; i < len; i++) {
				ensureBufferSpace(1);
				buffer[pos++] = ' ';
				print(function.apply(arr[i]));
			}
			return this;
		}

		public <T> FastPrinter1 print(final double[] arr, final DoubleFunction<T> function) {
			final int len = arr.length;
			if (len > 0) print(function.apply(arr[0]));
			for (int i = 1; i < len; i++) {
				ensureBufferSpace(1);
				buffer[pos++] = ' ';
				print(function.apply(arr[i]));
			}
			return this;
		}

		public <T> FastPrinter1 print(final char[] arr, final IntFunction<T> function) {
			final int len = arr.length;
			if (len > 0) print(function.apply(arr[0]));
			for (int i = 1; i < len; i++) {
				ensureBufferSpace(1);
				buffer[pos++] = ' ';
				print(function.apply(arr[i]));
			}
			return this;
		}

		public <T> FastPrinter1 print(final boolean[] arr, final Function<Boolean, T> function) {
			final int len = arr.length;
			if (len > 0) print(function.apply(arr[0]));
			for (int i = 1; i < len; i++) {
				ensureBufferSpace(1);
				buffer[pos++] = ' ';
				print(function.apply(arr[i]));
			}
			return this;
		}

		public <T> FastPrinter1 print(final String[] arr, final Function<String, T> function) {
			final int len = arr.length;
			if (len > 0) print(function.apply(arr[0]));
			for (int i = 1; i < len; i++) {
				ensureBufferSpace(1);
				buffer[pos++] = ' ';
				print(function.apply(arr[i]));
			}
			return this;
		}

		public FastPrinter1 println(final int[][] arr2d) {
			return println(arr2d, ' ');
		}

		public FastPrinter1 println(final long[][] arr2d) {
			return println(arr2d, ' ');
		}

		public FastPrinter1 println(final double[][] arr2d) {
			return println(arr2d, ' ');
		}

		public FastPrinter1 println(final char[][] arr2d) {
			return println(arr2d, ' ');
		}

		public FastPrinter1 println(final boolean[][] arr2d) {
			return println(arr2d, ' ');
		}

		public FastPrinter1 println(final String[][] arr2d) {
			return println(arr2d, ' ');
		}

		public FastPrinter1 println(final Object[][] arr2d) {
			return println(arr2d, ' ');
		}

		public FastPrinter1 println(final int[][] arr2d, final char delimiter) {
			for (final int[] arr : arr2d) println(arr, delimiter);
			return this;
		}

		public FastPrinter1 println(final long[][] arr2d, final char delimiter) {
			for (final long[] arr : arr2d) println(arr, delimiter);
			return this;
		}

		public FastPrinter1 println(final double[][] arr2d, final char delimiter) {
			for (final double[] arr : arr2d) println(arr, delimiter);
			return this;
		}

		public FastPrinter1 println(final char[][] arr2d, final char delimiter) {
			for (final char[] arr : arr2d) println(arr, delimiter);
			return this;
		}

		public FastPrinter1 println(final boolean[][] arr2d, final char delimiter) {
			for (final boolean[] arr : arr2d) println(arr, delimiter);
			return this;
		}

		public FastPrinter1 println(final String[][] arr2d, final char delimiter) {
			for (final String[] arr : arr2d) println(arr, delimiter);
			return this;
		}

		public FastPrinter1 println(final Object[][] arr2d, final char delimiter) {
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

		public <T> FastPrinter1 println(final int[][] arr2d, final IntFunction<T> function) {
			for (final int[] arr : arr2d) print(arr, function).println();
			return this;
		}

		public <T> FastPrinter1 println(final long[][] arr2d, final LongFunction<T> function) {
			for (final long[] arr : arr2d) print(arr, function).println();
			return this;
		}

		public <T> FastPrinter1 println(final double[][] arr2d, final DoubleFunction<T> function) {
			for (final double[] arr : arr2d) print(arr, function).println();
			return this;
		}

		public <T> FastPrinter1 println(final char[][] arr2d, final IntFunction<T> function) {
			for (final char[] arr : arr2d) print(arr, function).println();
			return this;
		}

		public <T> FastPrinter1 println(final boolean[][] arr2d, final Function<Boolean, T> function) {
			for (final boolean[] arr : arr2d) print(arr, function).println();
			return this;
		}

		public <T> FastPrinter1 println(final String[][] arr2d, final Function<String, T> function) {
			for (final String[] arr : arr2d) print(arr, function).println();
			return this;
		}

		public FastPrinter1 printChars(final char[] arr) {
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

		public FastPrinter1 printChars(final char[] arr, final IntUnaryOperator function) {
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

		public FastPrinter1 printChars(final char[][] arr2d) {
			for (final char[] arr : arr2d) printChars(arr).println();
			return this;
		}

		public FastPrinter1 printChars(final char[][] arr2d, final IntUnaryOperator function) {
			for (final char[] arr : arr2d) printChars(arr, function).println();
			return this;
		}

		public <T> FastPrinter1 println(final Iterable<T> iter) {
			return print(iter, '\n').println();
		}

		public <T> FastPrinter1 println(final Iterable<T> iter, final char delimiter) {
			return print(iter, delimiter).println();
		}

		public <T, U> FastPrinter1 println(final Iterable<T> iter, final Function<T, U> function) {
			return print(iter, function, '\n').println();
		}

		public <T> FastPrinter1 print(final Iterable<T> iter) {
			return print(iter, ' ');
		}

		public <T> FastPrinter1 print(final Iterable<T> iter, final char delimiter) {
			final Iterator<T> it = iter.iterator();
			if (it.hasNext()) print(it.next());
			while (it.hasNext()) {
				ensureBufferSpace(1);
				buffer[pos++] = (byte) delimiter;
				print(it.next());
			}
			return this;
		}

		public <T, U> FastPrinter1 print(final Iterable<T> iter, final Function<T, U> function) {
			return print(iter, function, ' ');
		}

		public <T, U> FastPrinter1 print(final Iterable<T> iter, final Function<T, U> function, final char delimiter) {
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

	private static final class FastScanner implements AutoCloseable {
		private static final int DEFAULT_BUFFER_SIZE = 65536;
		private final InputStream in;
		private final byte[] buffer;
		private int pos = 0, bufferLength = 0;

		public FastScanner() {
			this(System.in, DEFAULT_BUFFER_SIZE);
		}

		public FastScanner(final InputStream in) {
			this(in, DEFAULT_BUFFER_SIZE);
		}

		public FastScanner(final int bufferSize) {
			this(System.in, bufferSize);
		}

		public FastScanner(final InputStream in, final int bufferSize) {
			this.in = in;
			this.buffer = new byte[bufferSize];
		}

		private int skipSpaces() {
			int b = read();
			while (b <= 32) b = read();
			return b;
		}

		@Override
		public void close() throws IOException {
			if (in != System.in) in.close();
		}

		private int read() {
			if (pos >= bufferLength) {
				try {
					bufferLength = in.read(buffer, pos = 0, buffer.length);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				if (bufferLength <= 0) return 0;
			}
			return buffer[pos++] & 0xFF;
		}

		public int peek() {
			if (pos < bufferLength) {
				return buffer[pos] & 0xFF;
			} else {
				int b = read();
				if (b != 0) pos--;
				return b;
			}
		}

		public boolean hasNext() {
			while (true) {
				int b = peek();
				if (b == 0) return false;
				if (b <= 32) read();
				else return true;
			}
		}

		public int nextInt() {
			int b = skipSpaces();
			boolean negative = false;
			if (b != '-') {
				// fall through
			} else {
				negative = true;
				b = read();
			}
			int result = 0;
			while ('0' <= b && b <= '9') {
				result = (result << 3) + (result << 1) + (b & 15);
				b = read();
			}
			return negative ? -result : result;
		}

		public long nextLong() {
			int b = skipSpaces();
			boolean negative = false;
			if (b != '-') {
				// fall through
			} else {
				negative = true;
				b = read();
			}
			long result = 0;
			while ('0' <= b && b <= '9') {
				result = (result << 3) + (result << 1) + (b & 15);
				b = read();
			}
			return negative ? -result : result;
		}

		public double nextDouble() {
			int b = skipSpaces();
			boolean negative = false;
			if (b != '-') {
			} else {
				negative = true;
				b = read();
			}
			double result = 0;
			while ('0' <= b && b <= '9') {
				result = ((long) result << 3) + ((long) result << 1) + (b & 15);
				b = read();
			}
			if (b == '.') {
				b = read();
				double scale = 0.1;
				while ('0' <= b && b <= '9') {
					result += (b & 15) * scale;
					scale *= 0.1;
					b = read();
				}
			}
			return negative ? -result : result;
		}

		public char nextChar() {
			int b = skipSpaces();
			return (char) b;
		}

		public String next() {
			return nextStringBuilder().toString();
		}

		public StringBuilder nextStringBuilder() {
			final StringBuilder sb = new StringBuilder();
			int b = skipSpaces();
			while (b > 32) {
				sb.append((char) b);
				b = read();
			}
			return sb;
		}

		public String nextLine() {
			final StringBuilder sb = new StringBuilder();
			int b = read();
			while (b != 0 && b != '\n' && b != '\r') {
				sb.append((char) b);
				b = read();
			}
			if (b == '\r') {
				int c = read();
				if (c != '\n') pos--;
			}
			return sb.toString();
		}

		public BigInteger nextBigInteger() {
			return new BigInteger(next());
		}

		public BigDecimal nextBigDecimal() {
			return new BigDecimal(next());
		}

		public int[] nextInt(final int n) {
			final int[] a = new int[n];
			for (int i = 0; i < n; i++) a[i] = nextInt();
			return a;
		}

		public long[] nextLong(final int n) {
			final long[] a = new long[n];
			for (int i = 0; i < n; i++) a[i] = nextLong();
			return a;
		}

		public double[] nextDouble(final int n) {
			final double[] a = new double[n];
			for (int i = 0; i < n; i++) a[i] = nextDouble();
			return a;
		}

		public char[] nextChars() {
			return next().toCharArray();
		}

		public char[] nextChars(final int n) {
			final char[] c = new char[n];
			for (int i = 0; i < n; i++) c[i] = nextChar();
			return c;
		}

		public String[] nextStrings(final int n) {
			final String[] s = new String[n];
			for (int i = 0; i < n; i++) s[i] = next();
			return s;
		}

		public int[][] nextIntMat(final int h, final int w) {
			final int[][] a = new int[h][w];
			for (int i = 0; i < h; i++)
				for (int j = 0; j < w; j++)
					a[i][j] = nextInt();
			return a;
		}

		public long[][] nextLongMat(final int h, final int w) {
			final long[][] a = new long[h][w];
			for (int i = 0; i < h; i++)
				for (int j = 0; j < w; j++)
					a[i][j] = nextLong();
			return a;
		}

		public double[][] nextDoubleMat(final int h, final int w) {
			final double[][] a = new double[h][w];
			for (int i = 0; i < h; i++)
				for (int j = 0; j < w; j++)
					a[i][j] = nextDouble();
			return a;
		}

		public char[][] nextCharMat(final int n) {
			final char[][] c = new char[n][];
			for (int i = 0; i < n; i++) c[i] = nextChars(nextInt());
			return c;
		}

		public char[][] nextCharMat(final int h, final int w) {
			final char[][] c = new char[h][w];
			for (int i = 0; i < h; i++)
				for (int j = 0; j < w; j++)
					c[i][j] = nextChar();
			return c;
		}

		public String[][] nextStringMat(final int h, final int w) {
			final String[][] s = new String[h][w];
			for (int i = 0; i < h; i++)
				for (int j = 0; j < w; j++)
					s[i][j] = next();
			return s;
		}

		public int[][][] nextInt3D(final int x, final int y, final int z) {
			final int[][][] a = new int[x][y][z];
			for (int i = 0; i < x; i++)
				for (int j = 0; j < y; j++)
					for (int k = 0; k < z; k++)
						a[i][j][k] = nextInt();
			return a;
		}

		public long[][][] nextLong3D(final int x, final int y, final int z) {
			final long[][][] a = new long[x][y][z];
			for (int i = 0; i < x; i++)
				for (int j = 0; j < y; j++)
					for (int k = 0; k < z; k++)
						a[i][j][k] = nextLong();
			return a;
		}

		public int[] nextSortedInt(final int n) {
			final int[] a = nextInt(n);
			sort(a);
			return a;
		}

		public long[] nextSortedLong(final int n) {
			final long[] a = nextLong(n);
			sort(a);
			return a;
		}

		public double[] nextSortedDouble(final int n) {
			final double[] a = nextDouble(n);
			sort(a);
			return a;
		}

		public char[] nextSortedChars() {
			final char[] c = nextChars();
			sort(c);
			return c;
		}

		public char[] nextSortedChars(final int n) {
			final char[] c = nextChars(n);
			sort(c);
			return c;
		}

		public String[] nextSortedStrings(final int n) {
			final String[] s = nextStrings(n);
			sort(s);
			return s;
		}

		public int[] nextIntPrefixSum(final int n) {
			final int[] ps = new int[n];
			ps[0] = nextInt();
			for (int i = 1; i < n; i++) ps[i] = nextInt() + ps[i - 1];
			return ps;
		}

		public long[] nextLongPrefixSum(final int n) {
			final long[] ps = new long[n];
			ps[0] = nextLong();
			for (int i = 1; i < n; i++) ps[i] = nextLong() + ps[i - 1];
			return ps;
		}

		public int[][] nextIntPrefixSum(final int h, final int w) {
			final int[][] ps = new int[h + 1][w + 1];
			for (int i = 1; i <= h; i++)
				for (int j = 1; j <= w; j++)
					ps[i][j] = nextInt() + ps[i - 1][j] + ps[i][j - 1] - ps[i - 1][j - 1];
			return ps;
		}

		public long[][] nextLongPrefixSum(final int h, final int w) {
			final long[][] ps = new long[h + 1][w + 1];
			for (int i = 1; i <= h; i++)
				for (int j = 1; j <= w; j++)
					ps[i][j] = nextLong() + ps[i - 1][j] + ps[i][j - 1] - ps[i - 1][j - 1];
			return ps;
		}

		public int[][][] nextIntPrefixSum(final int x, final int y, final int z) {
			final int[][][] ps = new int[x + 1][y + 1][z + 1];
			for (int a = 1; a <= x; a++)
				for (int b = 1; b <= y; b++)
					for (int c = 1; c <= z; c++)
						ps[a][b][c] = nextInt() + ps[a - 1][b][c] + ps[a][b - 1][c] + ps[a][b][c - 1] - ps[a - 1][b - 1][c]
								- ps[a - 1][b][c - 1] - ps[a][b - 1][c - 1] + ps[a - 1][b - 1][c - 1];
			return ps;
		}

		public long[][][] nextLongPrefixSum(final int x, final int y, final int z) {
			final long[][][] ps = new long[x + 1][y + 1][z + 1];
			for (int a = 1; a <= x; a++)
				for (int b = 1; b <= y; b++)
					for (int c = 1; c <= z; c++)
						ps[a][b][c] = nextLong() + ps[a - 1][b][c] + ps[a][b - 1][c] + ps[a][b][c - 1] - ps[a - 1][b - 1][c]
								- ps[a - 1][b][c - 1] - ps[a][b - 1][c - 1] + ps[a - 1][b - 1][c - 1];
			return ps;
		}

		public int[] nextIntInverseMapping(final int n) {
			final int[] inv = new int[n];
			for (int i = 0; i < n; i++) inv[nextInt() - 1] = i;
			return inv;
		}

		private <T extends Collection<Integer>> T nextIntCollection(int n, final Supplier<T> supplier) {
			final T collection = supplier.get();
			while (n-- > 0) collection.add(nextInt());
			return collection;
		}

		public ArrayList<Integer> nextIntAL(final int n) {
			return nextIntCollection(n, () -> new ArrayList<>(n));
		}

		public HashSet<Integer> nextIntHS(final int n) {
			return nextIntCollection(n, () -> new HashSet<>(n));
		}

		public TreeSet<Integer> nextIntTS(final int n) {
			return nextIntCollection(n, TreeSet::new);
		}

		private <T extends Collection<Long>> T nextLongCollection(int n, final Supplier<T> supplier) {
			final T collection = supplier.get();
			while (n-- > 0) collection.add(nextLong());
			return collection;
		}

		public ArrayList<Long> nextLongAL(final int n) {
			return nextLongCollection(n, () -> new ArrayList<>(n));
		}

		public HashSet<Long> nextLongHS(final int n) {
			return nextLongCollection(n, () -> new HashSet<>(n));
		}

		public TreeSet<Long> nextLongTS(final int n) {
			return nextLongCollection(n, TreeSet::new);
		}

		private <T extends Collection<Character>> T nextCharacterCollection(int n, final Supplier<T> supplier) {
			final T collection = supplier.get();
			while (n-- > 0) collection.add(nextChar());
			return collection;
		}

		public ArrayList<Character> nextCharacterAL(final int n) {
			return nextCharacterCollection(n, () -> new ArrayList<>(n));
		}

		public HashSet<Character> nextCharacterHS(final int n) {
			return nextCharacterCollection(n, () -> new HashSet<>(n));
		}

		public TreeSet<Character> nextCharacterTS(final int n) {
			return nextCharacterCollection(n, TreeSet::new);
		}

		private <T extends Collection<String>> T nextStringCollection(int n, final Supplier<T> supplier) {
			final T collection = supplier.get();
			while (n-- > 0) collection.add(next());
			return collection;
		}

		public ArrayList<String> nextStringAL(final int n) {
			return nextStringCollection(n, () -> new ArrayList<>(n));
		}

		public HashSet<String> nextStringHS(final int n) {
			return nextStringCollection(n, () -> new HashSet<>(n));
		}

		public TreeSet<String> nextStringTS(final int n) {
			return nextStringCollection(n, TreeSet::new);
		}

		private <T extends Map<Integer, Integer>> T nextIntMultiset(int n, final Supplier<T> supplier) {
			final T multiSet = supplier.get();
			while (n-- > 0) {
				final int i = nextInt();
				multiSet.put(i, multiSet.getOrDefault(i, 0) + 1);
			}
			return multiSet;
		}

		public HashMap<Integer, Integer> nextIntMultisetHM(final int n) {
			return nextIntMultiset(n, () -> new HashMap<>(n));
		}

		public TreeMap<Integer, Integer> nextIntMultisetTM(final int n) {
			return nextIntMultiset(n, TreeMap::new);
		}

		private <T extends Map<Long, Integer>> T nextLongMultiset(int n, final Supplier<T> supplier) {
			final T multiSet = supplier.get();
			while (n-- > 0) {
				final long l = nextLong();
				multiSet.put(l, multiSet.getOrDefault(l, 0) + 1);
			}
			return multiSet;
		}

		public HashMap<Long, Integer> nextLongMultisetHM(final int n) {
			return nextLongMultiset(n, () -> new HashMap<>(n));
		}

		public TreeMap<Long, Integer> nextLongMultisetTM(final int n) {
			return nextLongMultiset(n, TreeMap::new);
		}

		private <T extends Map<Character, Integer>> T nextCharMultiset(int n, final Supplier<T> supplier) {
			final T multiSet = supplier.get();
			while (n-- > 0) {
				final char c = nextChar();
				multiSet.put(c, multiSet.getOrDefault(c, 0) + 1);
			}
			return multiSet;
		}

		public HashMap<Character, Integer> nextCharMultisetHM(final int n) {
			return nextCharMultiset(n, () -> new HashMap<>(n));
		}

		public TreeMap<Character, Integer> nextCharMultisetTM(final int n) {
			return nextCharMultiset(n, TreeMap::new);
		}

		private <T extends Map<String, Integer>> T nextStringMultiset(int n, final Supplier<T> supplier) {
			final T multiSet = supplier.get();
			while (n-- > 0) {
				final String s = next();
				multiSet.put(s, multiSet.getOrDefault(s, 0) + 1);
			}
			return multiSet;
		}

		public HashMap<String, Integer> nextStringMultisetHM(final int n) {
			return nextStringMultiset(n, () -> new HashMap<>(n));
		}

		public TreeMap<String, Integer> nextStringMultisetTM(final int n) {
			return nextStringMultiset(n, TreeMap::new);
		}

		public int[] nextIntMultiset(final int n, final int m) {
			final int[] multiset = new int[m];
			for (int i = 0; i < n; i++) multiset[nextInt() - 1]++;
			return multiset;
		}

		public int[] nextUpperCharMultiset(final int n) {
			return nextCharMultiset(n, 'A', 'Z');
		}

		public int[] nextLowerCharMultiset(final int n) {
			return nextCharMultiset(n, 'a', 'z');
		}

		public int[] nextCharMultiset(int n, final char l, final char r) {
			final int[] multiset = new int[r - l + 1];
			while (n-- > 0) {
				final int c = nextChar() - l;
				multiset[c]++;
			}
			return multiset;
		}
	}
}
