package verify.io.bench17;

import static java.lang.Math.*;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import lib.io.compat17.*;
import sun.misc.*;

public class TestOutputDoubleNumbers {

	public static void main(String[] args) throws FileNotFoundException {
		int len = args.length;
		final int which = len >= 1 ? Integer.parseInt(args[0]) : 0;
		final int buf = len >= 2 ? Integer.parseInt(args[1]) : 65536;
		FastScanner sc = new FastScanner();
		int n = sc.nextInt();
		double[] data = sc.nextDouble(n);
		if (which == 0) {
			double ms = benchFastPrinterFrom(buf, data);
			PrintStream out = new PrintStream(new FileOutputStream("Output\\output6_1.txt", true));
			System.setOut(out);
			System.out.printf("FastPrinter1: %.3f ms (buf=%d)%n", ms, buf);
		} else if (which == 1) {
			double ms = benchFastPrinter2From(buf, data);
			PrintStream out = new PrintStream(new FileOutputStream("Output\\output6_1.txt", true));
			System.setOut(out);
			System.out.printf("FastPrinter2: %.3f ms (buf=%d)%n", ms, buf);
		} else if (which == 2) {
			double ms = benchFastPrinter3From(buf, data);
			PrintStream out = new PrintStream(new FileOutputStream("Output\\output6_2.txt", true));
			System.setOut(out);
			System.out.printf("FastPrinter1: %.3f ms (buf=%d)%n", ms, buf);
		} else if (which == 3) {
			double ms = benchFastPrinter4From(buf, data);
			PrintStream out = new PrintStream(new FileOutputStream("Output\\output6_2.txt", true));
			System.setOut(out);
			System.out.printf("FastPrinter2: %.3f ms (buf=%d)%n", ms, buf);
		}
	}

	private static double benchFastPrinterFrom(final int bufferSize, final double[] data) {
		final long t0 = System.nanoTime();
		try (final FastPrinter1 out = new FastPrinter1(System.out, bufferSize)) {
			for (double s : data) {
				out.println(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		final long t1 = System.nanoTime();
		return (t1 - t0) / 1_000_000.0;
	}

	private static double benchFastPrinter2From(final int bufferSize, final double[] data) {
		final long t0 = System.nanoTime();
		try (final FastPrinter2 out = new FastPrinter2(System.out, bufferSize)) {
			for (double s : data) {
				out.println(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		final long t1 = System.nanoTime();
		return (t1 - t0) / 1_000_000.0;
	}

	private static double benchFastPrinter3From(final int bufferSize, final double[] data) {
		int n = data.length;
		final long seed = 123456789;
		SplittableRandom rnd = new SplittableRandom(seed);
		int[] random = new int[n];
		for (int i = 0; i < n; i++) {
			random[i] = rnd.nextInt(1, 18);
		}
		final long t0 = System.nanoTime();
		try (final FastPrinter1 out = new FastPrinter1(System.out, bufferSize)) {
			for (int i = 0; i < n; i++) {
				out.println(data[i], random[i]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		final long t1 = System.nanoTime();
		return (t1 - t0) / 1_000_000.0;
	}

	private static double benchFastPrinter4From(final int bufferSize, final double[] data) {
		int n = data.length;
		final long seed = 123456789;
		SplittableRandom rnd = new SplittableRandom(seed);
		int[] random = new int[n];
		for (int i = 0; i < n; i++) {
			random[i] = rnd.nextInt(1, 18);
		}
		final long t0 = System.nanoTime();
		try (final FastPrinter2 out = new FastPrinter2(System.out, bufferSize)) {
			for (int i = 0; i < n; i++) {
				out.println(data[i], random[i]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		final long t1 = System.nanoTime();
		return (t1 - t0) / 1_000_000.0;
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
		private final byte[] buffer;
		private final boolean autoFlush;
		private final OutputStream out;
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

		public void println(final double d) {
			println(Double.toString(d));
		}

		public void println(final String s) {
			fillBuffer(s);
			ensureBufferSpace(1);
			buffer[pos++] = '\n';
			if (autoFlush) flush();
		}

		public void print(final long l) {
			ensureBufferSpace(MAX_LONG_DIGITS);
			fillBuffer(l);
			if (autoFlush) flush();
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
	private static final class FastPrinter2 implements AutoCloseable {
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

		public FastPrinter2() {
			this(System.out, DEFAULT_BUFFER_SIZE, false);
		}

		public FastPrinter2(final OutputStream out) {
			this(out, DEFAULT_BUFFER_SIZE, false);
		}

		public FastPrinter2(final int bufferSize) {
			this(System.out, bufferSize, false);
		}

		public FastPrinter2(final boolean autoFlush) {
			this(System.out, DEFAULT_BUFFER_SIZE, autoFlush);
		}

		public FastPrinter2(final OutputStream out, final boolean autoFlush) {
			this(out, DEFAULT_BUFFER_SIZE, autoFlush);
		}

		public FastPrinter2(final int bufferSize, final boolean autoFlush) {
			this(System.out, bufferSize, autoFlush);
		}

		public FastPrinter2(final OutputStream out, final int bufferSize) {
			this(out, bufferSize, false);
		}

		public FastPrinter2(final OutputStream out, final int bufferSize, final boolean autoFlush) {
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

		public FastPrinter2 println() {
			ensureBufferSpace(1);
			buffer[pos++] = '\n';
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter2 println(final long l) {
			ensureBufferSpace(MAX_LONG_DIGITS + 1);
			fillBuffer(l);
			buffer[pos++] = '\n';
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter2 println(final double d) {
			return println(Double.toString(d));
		}

		public FastPrinter2 println(final String s) {
			final byte[] src = (byte[]) UNSAFE.getObject(s, STRING_VALUE_OFFSET);
			fillBuffer(src, s.length());
			ensureBufferSpace(1);
			buffer[pos++] = '\n';
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter2 print(final long l) {
			ensureBufferSpace(MAX_LONG_DIGITS);
			fillBuffer(l);
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter2 println(final double d, final int n) {
			return print(d, n).println();
		}

		public FastPrinter2 print(double d, int n) {
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

		private void fillBuffer(final byte[] src, final int len) {
			for (int i = 0; i < len; ) {
				ensureBufferSpace(1);
				int limit = min(BUFFER_SIZE - pos, len - i);
				System.arraycopy(src, i, buffer, pos, limit);
				pos += limit;
				i += limit;
			}
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

	}

}
