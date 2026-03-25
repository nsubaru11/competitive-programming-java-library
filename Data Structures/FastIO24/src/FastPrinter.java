import java.io.*;
import java.lang.invoke.*;
import java.math.*;
import java.nio.*;
import java.nio.charset.*;
import java.util.*;
import java.util.function.*;

import static java.lang.Math.*;
import static java.util.Arrays.*;

@SuppressWarnings("unused")
public final class FastPrinter implements AutoCloseable {
	private static final int MAX_INT_DIGITS = 11;
	private static final int MAX_LONG_DIGITS = 20;
	private static final int MAX_BOOL_DIGITS = 3;
	private static final int DEFAULT_BUFFER_SIZE = 1 << 20;
	private static final Charset ASCII = StandardCharsets.US_ASCII;
	private static final int YES = 0x736559;
	private static final short NO = 0x6F4E;
	private static final byte LINE = '\n';
	private static final byte SPACE = ' ';
	private static final byte HYPHEN = '-';
	private static final byte PERIOD = '.';
	private static final byte ZERO = '0';
	private final OutputStream out;
	private final boolean autoFlush;
	private byte[] buffer;
	private int pos;

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
		this.buffer = new byte[bufferSize(bufferSize)];
		this.autoFlush = autoFlush;
	}

	private static int countDigits(final int i) {
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

	private static int countDigits(final long l) {
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

	private static int bufferSize(int x) {
		return x <= 64 ? 64 : 1 << (32 - Integer.numberOfLeadingZeros(x - 1));
	}

	@Override
	public void close() {
		try {
			flush();
			out.close();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void flush() {
		if (pos == 0) return;
		try {
			out.write(buffer, 0, pos);
			pos = 0;
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	public FastPrinter println() {
		ensureCapacity(1);
		buffer[pos++] = LINE;
		if (autoFlush) flush();
		return this;
	}

	public FastPrinter println(final boolean b) {
		ensureCapacity(MAX_BOOL_DIGITS + 1);
		final int p = write(b, pos);
		buffer[p] = LINE;
		pos = p + 1;
		if (autoFlush) flush();
		return this;
	}

	public FastPrinter println(final byte b) {
		ensureCapacity(2);
		final byte[] buf = buffer;
		final int p = pos;
		buf[p] = b;
		buf[p + 1] = LINE;
		pos = p + 2;
		if (autoFlush) flush();
		return this;
	}

	public FastPrinter println(final char c) {
		ensureCapacity(2);
		final byte[] buf = buffer;
		final int p = pos;
		buf[p] = (byte) c;
		buf[p + 1] = LINE;
		pos = p + 2;
		if (autoFlush) flush();
		return this;
	}

	public FastPrinter println(final int i) {
		ensureCapacity(MAX_INT_DIGITS + 1);
		final int p = write(i, pos);
		buffer[p] = LINE;
		pos = p + 1;
		if (autoFlush) flush();
		return this;
	}

	public FastPrinter println(final long l) {
		ensureCapacity(MAX_LONG_DIGITS + 1);
		final int p = write(l, pos);
		buffer[p] = LINE;
		pos = p + 1;
		if (autoFlush) flush();
		return this;
	}

	public FastPrinter println(final double d) {
		return println(Double.toString(d));
	}

	public FastPrinter println(final BigInteger bi) {
		return println(bi.toString());
	}

	public FastPrinter println(final BigDecimal bd) {
		return println(bd.toString());
	}

	public FastPrinter println(final String s) {
		ensureCapacity(s.length() + 1);
		final int p = write(s.getBytes(ASCII), pos);
		buffer[p] = LINE;
		pos = p + 1;
		if (autoFlush) flush();
		return this;
	}

	public FastPrinter println(final StringBuilder s) {
		ensureCapacity(s.length() + 1);
		final int p = write(s.toString().getBytes(ASCII), pos);
		buffer[p] = LINE;
		pos = p + 1;
		if (autoFlush) flush();
		return this;
	}

	public FastPrinter println(final Object o) {
		return switch (o) {
			case null -> println();
			case Boolean b -> println(b.booleanValue());
			case Byte b -> println(b.byteValue());
			case Character c -> println(c.charValue());
			case Integer i -> println(i.intValue());
			case Long l -> println(l.longValue());
			case Double d -> println(d.toString());
			case BigInteger bi -> println(bi.toString());
			case BigDecimal bd -> println(bd.toString());
			case String s -> println(s);
			case boolean[] arr -> println(arr);
			case char[] arr -> println(arr);
			case int[] arr -> println(arr);
			case long[] arr -> println(arr);
			case double[] arr -> println(arr);
			case String[] arr -> println(arr);
			case Object[] arr -> println(arr);
			default -> println(o.toString());
		};
	}

	public FastPrinter print(final boolean b) {
		ensureCapacity(MAX_BOOL_DIGITS);
		pos = write(b, pos);
		if (autoFlush) flush();
		return this;
	}

	public FastPrinter print(final byte b) {
		ensureCapacity(1);
		buffer[pos++] = b;
		if (autoFlush) flush();
		return this;
	}

	public FastPrinter print(final char c) {
		ensureCapacity(1);
		buffer[pos++] = (byte) c;
		if (autoFlush) flush();
		return this;
	}

	public FastPrinter print(final int i) {
		ensureCapacity(MAX_INT_DIGITS);
		pos = write(i, pos);
		if (autoFlush) flush();
		return this;
	}

	public FastPrinter print(final long l) {
		ensureCapacity(MAX_LONG_DIGITS);
		pos = write(l, pos);
		if (autoFlush) flush();
		return this;
	}

	public FastPrinter print(final double d) {
		return print(Double.toString(d));
	}

	public FastPrinter print(final BigInteger bi) {
		return print(bi.toString());
	}

	public FastPrinter print(final BigDecimal bd) {
		return print(bd.toString());
	}

	public FastPrinter print(final String s) {
		ensureCapacity(s.length());
		pos = write(s.getBytes(ASCII), pos);
		if (autoFlush) flush();
		return this;
	}

	public FastPrinter print(final StringBuilder s) {
		ensureCapacity(s.length());
		pos = write(s.toString().getBytes(ASCII), pos);
		if (autoFlush) flush();
		return this;
	}

	public FastPrinter print(final Object o) {
		return switch (o) {
			case null -> this;
			case Boolean b -> print(b.booleanValue());
			case Byte b -> print(b.byteValue());
			case Character c -> print(c.charValue());
			case Integer i -> print(i.intValue());
			case Long l -> print(l.longValue());
			case Double d -> print(d.toString());
			case BigInteger bi -> print(bi.toString());
			case BigDecimal bd -> print(bd.toString());
			case String s -> print(s);
			case boolean[] arr -> print(arr);
			case char[] arr -> print(arr);
			case int[] arr -> print(arr);
			case long[] arr -> print(arr);
			case double[] arr -> print(arr);
			case String[] arr -> print(arr);
			case Object[] arr -> print(arr);
			default -> print(o.toString());
		};
	}

	public FastPrinter printf(final String format, final Object... args) {
		return print(String.format(format, args));
	}

	public FastPrinter printf(final Locale locale, final String format, final Object... args) {
		return print(String.format(locale, format, args));
	}

	private void ensureCapacity(final int additional) {
		final int required = pos + additional + 4;
		final int bufferLength = buffer.length;
		if (required <= bufferLength) return;
		flush();
		if (additional + 4 > bufferLength) buffer = new byte[bufferSize(additional + 4)];
	}

	private int write(final boolean b, int p) {
		final byte[] buf = buffer;
		if (b) {
			Cache.INT_HANDLE.set(buf, p, YES);
			return p + 3;
		}
		Cache.SHORT_HANDLE.set(buf, p, NO);
		return p + 2;
	}

	private int write(int i, int p) {
		final byte[] buf = buffer;
		final VarHandle shortHandle = Cache.SHORT_HANDLE;
		final VarHandle intHandle = Cache.INT_HANDLE;
		final short[] digits2 = Cache.DIGITS_2;
		final int[] digits3 = Cache.DIGITS_3;
		if (i >= 0) i = -i;
		else buf[p++] = HYPHEN;
		final int digits = countDigits(i);
		int writePos = p;

		int c1 = -1, c2 = -1, c3 = -1;
		if (i <= -1000) {
			final int q = i / 1000;
			c1 = (q * 1000) - i;
			i = q;
		}
		if (i <= -1000) {
			final int q = i / 1000;
			c2 = (q * 1000) - i;
			i = q;
		}
		if (i <= -1000) {
			final int q = i / 1000;
			c3 = (q * 1000) - i;
			i = q;
		}

		i = -i;
		if (i >= 100) {
			intHandle.set(buf, writePos, digits3[i]);
			writePos += 3;
		} else if (i >= 10) {
			shortHandle.set(buf, writePos, digits2[i]);
			writePos += 2;
		} else {
			buf[writePos++] = (byte) (i + ZERO);
		}

		if (c3 != -1) {
			intHandle.set(buf, writePos, digits3[c3]);
			writePos += 3;
		}
		if (c2 != -1) {
			intHandle.set(buf, writePos, digits3[c2]);
			writePos += 3;
		}
		if (c1 != -1) intHandle.set(buf, writePos, digits3[c1]);
		return p + digits;
	}

	private int write(long l, int p) {
		final byte[] buf = buffer;
		final VarHandle shortHandle = Cache.SHORT_HANDLE;
		final VarHandle intHandle = Cache.INT_HANDLE;
		final short[] digits2 = Cache.DIGITS_2;
		final int[] digits3 = Cache.DIGITS_3;
		if (l >= 0) l = -l;
		else buf[p++] = HYPHEN;
		final int digits = countDigits(l);
		int writePos = p;

		int c1 = -1, c2 = -1, c3 = -1, c4 = -1, c5 = -1, c6 = -1;
		if (l <= -1000L) {
			final long q = l / 1000L;
			c1 = (int) ((q * 1000L) - l);
			l = q;
		}
		if (l <= -1000L) {
			final long q = l / 1000L;
			c2 = (int) ((q * 1000L) - l);
			l = q;
		}
		if (l <= -1000L) {
			final long q = l / 1000L;
			c3 = (int) ((q * 1000L) - l);
			l = q;
		}
		if (l <= -1000L) {
			final long q = l / 1000L;
			c4 = (int) ((q * 1000L) - l);
			l = q;
		}
		if (l <= -1000L) {
			final long q = l / 1000L;
			c5 = (int) ((q * 1000L) - l);
			l = q;
		}
		if (l <= -1000L) {
			final long q = l / 1000L;
			c6 = (int) ((q * 1000L) - l);
			l = q;
		}

		final int rem = (int) -l;
		if (rem >= 100) {
			intHandle.set(buf, writePos, digits3[rem]);
			writePos += 3;
		} else if (rem >= 10) {
			shortHandle.set(buf, writePos, digits2[rem]);
			writePos += 2;
		} else {
			buf[writePos++] = (byte) (rem + ZERO);
		}

		if (c6 != -1) {
			intHandle.set(buf, writePos, digits3[c6]);
			writePos += 3;
		}
		if (c5 != -1) {
			intHandle.set(buf, writePos, digits3[c5]);
			writePos += 3;
		}
		if (c4 != -1) {
			intHandle.set(buf, writePos, digits3[c4]);
			writePos += 3;
		}
		if (c3 != -1) {
			intHandle.set(buf, writePos, digits3[c3]);
			writePos += 3;
		}
		if (c2 != -1) {
			intHandle.set(buf, writePos, digits3[c2]);
			writePos += 3;
		}
		if (c1 != -1) intHandle.set(buf, writePos, digits3[c1]);
		return p + digits;
	}

	private int write(final byte[] s, int p) {
		final int len = s.length;
		final byte[] buf = buffer;
		System.arraycopy(s, 0, buf, p, len);
		return p + len;
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
		ensureCapacity((MAX_LONG_DIGITS << 1) + 2);
		final byte[] buf = buffer;
		int p = pos;
		p = write(a, p);
		buf[p] = (byte) delimiter;
		p = write(b, p + 1);
		buf[p] = LINE;
		pos = p + 1;
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
		ensureCapacity((MAX_LONG_DIGITS << 1) + 1);
		final byte[] buf = buffer;
		int p = pos;
		p = write(a, p);
		buf[p] = (byte) delimiter;
		pos = write(b, p + 1);
		if (autoFlush) flush();
		return this;
	}

	public FastPrinter println(final double d, final int n) {
		return print(d, n).println();
	}

	public FastPrinter print(double d, int n) {
		if (n <= 0) return print(round(d));
		ensureCapacity(MAX_LONG_DIGITS + n + 2);
		final byte[] buf = buffer;
		int p = pos;
		if (d < 0) {
			buf[p++] = HYPHEN;
			d = -d;
		}
		if (n > 18) n = 18;
		final long intPart = (long) d;
		final long fracPart = (long) ((d - intPart) * Cache.POW10[n]);
		p = write(intPart, p);
		buf[p++] = PERIOD;
		int leadingZeros = n - countDigits(-fracPart);
		fill(buf, p, p + leadingZeros, ZERO);
		pos = write(fracPart, p + leadingZeros);
		if (autoFlush) flush();
		return this;
	}

	public FastPrinter println(final boolean[] arr) {
		return print(arr, 0, arr.length, '\n').println();
	}

	public FastPrinter println(final char[] arr) {
		return print(arr, 0, arr.length, '\n').println();
	}

	public FastPrinter println(final int[] arr) {
		return print(arr, 0, arr.length, '\n').println();
	}

	public FastPrinter println(final long[] arr) {
		return print(arr, 0, arr.length, '\n').println();
	}

	public FastPrinter println(final double[] arr) {
		return print(arr, 0, arr.length, '\n').println();
	}

	public FastPrinter println(final String[] arr) {
		return print(arr, 0, arr.length, '\n').println();
	}

	public FastPrinter println(final Object... arr) {
		for (final Object o : arr) println(o);
		return this;
	}

	public FastPrinter println(final boolean[] arr, final char delimiter) {
		return print(arr, 0, arr.length, delimiter).println();
	}

	public FastPrinter println(final char[] arr, final char delimiter) {
		return print(arr, 0, arr.length, delimiter).println();
	}

	public FastPrinter println(final int[] arr, final char delimiter) {
		return print(arr, 0, arr.length, delimiter).println();
	}

	public FastPrinter println(final long[] arr, final char delimiter) {
		return print(arr, 0, arr.length, delimiter).println();
	}

	public FastPrinter println(final double[] arr, final char delimiter) {
		return print(arr, 0, arr.length, delimiter).println();
	}

	public FastPrinter println(final String[] arr, final char delimiter) {
		return print(arr, 0, arr.length, delimiter).println();
	}

	public FastPrinter println(final boolean[] arr, final int from, final int to) {
		return print(arr, from, to, '\n').println();
	}

	public FastPrinter println(final char[] arr, final int from, final int to) {
		return print(arr, from, to, '\n').println();
	}

	public FastPrinter println(final int[] arr, final int from, final int to) {
		return print(arr, from, to, '\n').println();
	}

	public FastPrinter println(final long[] arr, final int from, final int to) {
		return print(arr, from, to, '\n').println();
	}

	public FastPrinter println(final double[] arr, final int from, final int to) {
		return print(arr, from, to, '\n').println();
	}

	public FastPrinter println(final String[] arr, final int from, final int to) {
		return print(arr, from, to, '\n').println();
	}

	public FastPrinter println(final boolean[] arr, final int from, final int to, final char delimiter) {
		return print(arr, from, to, delimiter).println();
	}

	public FastPrinter println(final char[] arr, final int from, final int to, final char delimiter) {
		return print(arr, from, to, delimiter).println();
	}

	public FastPrinter println(final int[] arr, final int from, final int to, final char delimiter) {
		return print(arr, from, to, delimiter).println();
	}

	public FastPrinter println(final long[] arr, final int from, final int to, final char delimiter) {
		return print(arr, from, to, delimiter).println();
	}

	public FastPrinter println(final double[] arr, final int from, final int to, final char delimiter) {
		return print(arr, from, to, delimiter).println();
	}

	public FastPrinter println(final String[] arr, final int from, final int to, final char delimiter) {
		return print(arr, from, to, delimiter).println();
	}

	public FastPrinter print(final boolean[] arr) {
		return print(arr, 0, arr.length, ' ');
	}

	public FastPrinter print(final char[] arr) {
		return print(arr, 0, arr.length, ' ');
	}

	public FastPrinter print(final int[] arr) {
		return print(arr, 0, arr.length, ' ');
	}

	public FastPrinter print(final long[] arr) {
		return print(arr, 0, arr.length, ' ');
	}

	public FastPrinter print(final double[] arr) {
		return print(arr, 0, arr.length, ' ');
	}

	public FastPrinter print(final String[] arr) {
		return print(arr, 0, arr.length, ' ');
	}

	public FastPrinter print(final Object... arr) {
		final int len = arr.length;
		if (len > 0) print(arr[0]);
		for (int i = 1; i < len; i++) {
			ensureCapacity(1);
			buffer[pos++] = SPACE;
			print(arr[i]);
		}
		return this;
	}

	public FastPrinter print(final boolean[] arr, final char delimiter) {
		return print(arr, 0, arr.length, delimiter);
	}

	public FastPrinter print(final char[] arr, final char delimiter) {
		return print(arr, 0, arr.length, delimiter);
	}

	public FastPrinter print(final int[] arr, final char delimiter) {
		return print(arr, 0, arr.length, delimiter);
	}

	public FastPrinter print(final long[] arr, final char delimiter) {
		return print(arr, 0, arr.length, delimiter);
	}

	public FastPrinter print(final double[] arr, final char delimiter) {
		return print(arr, 0, arr.length, delimiter);
	}

	public FastPrinter print(final String[] arr, final char delimiter) {
		return print(arr, 0, arr.length, delimiter);
	}

	public FastPrinter print(final boolean[] arr, final int from, final int to) {
		return print(arr, from, to, ' ');
	}

	public FastPrinter print(final char[] arr, final int from, final int to) {
		return print(arr, from, to, ' ');
	}

	public FastPrinter print(final int[] arr, final int from, final int to) {
		return print(arr, from, to, ' ');
	}

	public FastPrinter print(final long[] arr, final int from, final int to) {
		return print(arr, from, to, ' ');
	}

	public FastPrinter print(final double[] arr, final int from, final int to) {
		return print(arr, from, to, ' ');
	}

	public FastPrinter print(final String[] arr, final int from, final int to) {
		return print(arr, from, to, ' ');
	}

	public FastPrinter print(final boolean[] arr, final int from, final int to, final char delimiter) {
		if (from >= to) return this;
		ensureCapacity((to - from) * (MAX_BOOL_DIGITS + 1));
		final byte[] buf = buffer;
		int p = pos;
		p = write(arr[from], p);
		final byte d = (byte) delimiter;
		for (int i = from + 1; i < to; i++) {
			buf[p] = d;
			p = write(arr[i], p + 1);
		}
		pos = p;
		if (autoFlush) flush();
		return this;
	}

	public FastPrinter print(final char[] arr, final int from, final int to, final char delimiter) {
		if (from >= to) return this;
		ensureCapacity(((to - from) << 1) - 1);
		final byte[] buf = buffer;
		int p = pos;
		buf[p++] = (byte) arr[from];
		final byte d = (byte) delimiter;
		for (int i = from + 1; i < to; i++) {
			buf[p] = d;
			buf[p + 1] = (byte) arr[i];
			p += 2;
		}
		pos = p;
		if (autoFlush) flush();
		return this;
	}

	public FastPrinter print(final int[] arr, final int from, final int to, final char delimiter) {
		if (from >= to) return this;
		ensureCapacity((to - from) * (MAX_INT_DIGITS + 1));
		final byte[] buf = buffer;
		int p = pos;
		p = write(arr[from], p);
		final byte d = (byte) delimiter;
		for (int i = from + 1; i < to; i++) {
			buf[p] = d;
			p = write(arr[i], p + 1);
		}
		pos = p;
		if (autoFlush) flush();
		return this;
	}

	public FastPrinter print(final long[] arr, final int from, final int to, final char delimiter) {
		if (from >= to) return this;
		ensureCapacity((to - from) * (MAX_LONG_DIGITS + 1));
		final byte[] buf = buffer;
		int p = pos;
		p = write(arr[from], p);
		final byte d = (byte) delimiter;
		for (int i = from + 1; i < to; i++) {
			buf[p] = d;
			p = write(arr[i], p + 1);
		}
		pos = p;
		if (autoFlush) flush();
		return this;
	}

	public FastPrinter print(final double[] arr, final int from, final int to, final char delimiter) {
		if (from >= to) return this;
		print(arr[from]);
		final byte d = (byte) delimiter;
		for (int i = from + 1; i < to; i++) {
			print(d);
			print(arr[i]);
		}
		if (autoFlush) flush();
		return this;
	}

	public FastPrinter print(final String[] arr, final int from, final int to, final char delimiter) {
		if (from >= to) return this;
		int totalLen = 0;
		for (int i = from; i < to; i++) totalLen += arr[i].length();
		ensureCapacity(totalLen + (to - from - 1));

		final byte[] buf = buffer;
		int p = pos;
		p = write(arr[from].getBytes(ASCII), p);
		final byte d = (byte) delimiter;
		for (int i = from + 1; i < to; i++) {
			buf[p] = d;
			p = write(arr[i].getBytes(ASCII), p + 1);
		}
		pos = p;
		if (autoFlush) flush();
		return this;
	}

	public <T> FastPrinter println(final boolean[] arr, final Function<Boolean, T> function) {
		for (final boolean b : arr) println(function.apply(b));
		return this;
	}

	public <T> FastPrinter println(final char[] arr, final IntFunction<T> function) {
		for (final char c : arr) println(function.apply(c));
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

	public <T> FastPrinter println(final String[] arr, final Function<String, T> function) {
		for (final String s : arr) println(function.apply(s));
		return this;
	}

	public <T> FastPrinter print(final boolean[] arr, final Function<Boolean, T> function) {
		final int len = arr.length;
		if (len > 0) print(function.apply(arr[0]));
		for (int i = 1; i < len; i++) {
			print(SPACE);
			print(function.apply(arr[i]));
		}
		return this;
	}

	public <T> FastPrinter print(final char[] arr, final IntFunction<T> function) {
		final int len = arr.length;
		if (len > 0) print(function.apply(arr[0]));
		for (int i = 1; i < len; i++) {
			print(SPACE);
			print(function.apply(arr[i]));
		}
		return this;
	}

	public <T> FastPrinter print(final int[] arr, final IntFunction<T> function) {
		final int len = arr.length;
		if (len > 0) print(function.apply(arr[0]));
		for (int i = 1; i < len; i++) {
			print(SPACE);
			print(function.apply(arr[i]));
		}
		return this;
	}

	public <T> FastPrinter print(final long[] arr, final LongFunction<T> function) {
		final int len = arr.length;
		if (len > 0) print(function.apply(arr[0]));
		for (int i = 1; i < len; i++) {
			print(SPACE);
			print(function.apply(arr[i]));
		}
		return this;
	}

	public <T> FastPrinter print(final double[] arr, final DoubleFunction<T> function) {
		final int len = arr.length;
		if (len > 0) print(function.apply(arr[0]));
		for (int i = 1; i < len; i++) {
			print(SPACE);
			print(function.apply(arr[i]));
		}
		return this;
	}

	public <T> FastPrinter print(final String[] arr, final Function<String, T> function) {
		final int len = arr.length;
		if (len > 0) print(function.apply(arr[0]));
		for (int i = 1; i < len; i++) {
			print(SPACE);
			print(function.apply(arr[i]));
		}
		return this;
	}

	public FastPrinter println(final boolean[][] arr2d) {
		return println(arr2d, ' ');
	}

	public FastPrinter println(final char[][] arr2d) {
		return println(arr2d, ' ');
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

	public FastPrinter println(final String[][] arr2d) {
		return println(arr2d, ' ');
	}

	public FastPrinter println(final Object[][] arr2d) {
		return println(arr2d, ' ');
	}

	public FastPrinter println(final boolean[][] arr2d, final char delimiter) {
		for (final boolean[] arr : arr2d) println(arr, delimiter);
		return this;
	}

	public FastPrinter println(final char[][] arr2d, final char delimiter) {
		for (final char[] arr : arr2d) println(arr, delimiter);
		return this;
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

	public FastPrinter println(final String[][] arr2d, final char delimiter) {
		for (final String[] arr : arr2d) println(arr, delimiter);
		return this;
	}

	public FastPrinter println(final Object[][] arr2d, final char delimiter) {
		for (final Object[] arr : arr2d) {
			final int len = arr.length;
			if (len > 0) print(arr[0]);
			for (int i = 1; i < len; i++) {
				print(delimiter);
				print(arr[i]);
			}
			println();
		}
		return this;
	}

	public <T> FastPrinter println(final boolean[][] arr2d, final Function<Boolean, T> function) {
		for (final boolean[] arr : arr2d) print(arr, function).println();
		return this;
	}

	public <T> FastPrinter println(final char[][] arr2d, final IntFunction<T> function) {
		for (final char[] arr : arr2d) print(arr, function).println();
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

	public <T> FastPrinter println(final String[][] arr2d, final Function<String, T> function) {
		for (final String[] arr : arr2d) print(arr, function).println();
		return this;
	}

	public FastPrinter printChars(final char[] arr) {
		return printChars(arr, 0, arr.length);
	}

	public FastPrinter printChars(final char[] arr, final int from, final int to) {
		final int len = to - from;
		ensureCapacity(len);
		final byte[] buf = buffer;
		int p = pos, i = from;
		final int limit8 = from + (len & ~7);
		while (i < limit8) {
			buf[p] = (byte) arr[i];
			buf[p + 1] = (byte) arr[i + 1];
			buf[p + 2] = (byte) arr[i + 2];
			buf[p + 3] = (byte) arr[i + 3];
			buf[p + 4] = (byte) arr[i + 4];
			buf[p + 5] = (byte) arr[i + 5];
			buf[p + 6] = (byte) arr[i + 6];
			buf[p + 7] = (byte) arr[i + 7];
			p += 8;
			i += 8;
		}
		while (i < to) buf[p++] = (byte) arr[i++];
		pos = p;
		if (autoFlush) flush();
		return this;
	}

	public FastPrinter printChars(final char[] arr, final IntUnaryOperator function) {
		final int len = arr.length;
		ensureCapacity(len);
		final byte[] buf = buffer;
		int p = pos, i = 0;
		final int limit = len & ~7;
		while (i < limit) {
			buf[p] = (byte) function.applyAsInt(arr[i]);
			buf[p + 1] = (byte) function.applyAsInt(arr[i + 1]);
			buf[p + 2] = (byte) function.applyAsInt(arr[i + 2]);
			buf[p + 3] = (byte) function.applyAsInt(arr[i + 3]);
			buf[p + 4] = (byte) function.applyAsInt(arr[i + 4]);
			buf[p + 5] = (byte) function.applyAsInt(arr[i + 5]);
			buf[p + 6] = (byte) function.applyAsInt(arr[i + 6]);
			buf[p + 7] = (byte) function.applyAsInt(arr[i + 7]);
			p += 8;
			i += 8;
		}
		while (i < len) buf[p++] = (byte) function.applyAsInt(arr[i++]);
		pos = p;
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

	public <T> FastPrinter print(final Iterable<T> iter) {
		return print(iter, ' ');
	}

	public <T> FastPrinter print(final Iterable<T> iter, final char delimiter) {
		final Iterator<T> it = iter.iterator();
		if (it.hasNext()) print(it.next());
		while (it.hasNext()) {
			print(delimiter);
			print(it.next());
		}
		return this;
	}

	public <T, U> FastPrinter println(final Iterable<T> iter, final Function<T, U> function) {
		return print(iter, function, '\n').println();
	}

	public <T, U> FastPrinter println(final Iterable<T> iter, final Function<T, U> function, final char delimiter) {
		return print(iter, function, delimiter).println();
	}

	public <T, U> FastPrinter print(final Iterable<T> iter, final Function<T, U> function) {
		return print(iter, function, ' ');
	}

	public <T, U> FastPrinter print(final Iterable<T> iter, final Function<T, U> function, final char delimiter) {
		final Iterator<T> it = iter.iterator();
		if (it.hasNext()) print(function.apply(it.next()));
		while (it.hasNext()) {
			print(delimiter);
			print(function.apply(it.next()));
		}
		return this;
	}

	public FastPrinter printRepeat(final char c, final int cnt) {
		if (cnt <= 0) return this;
		ensureCapacity(cnt);
		final byte[] buf = buffer;
		final int p = pos;
		fill(buf, p, p + cnt, (byte) c);
		pos = p + cnt;
		if (autoFlush) flush();
		return this;
	}

	public FastPrinter printRepeat(final String s, final int cnt) {
		if (cnt <= 0) return this;
		final int len = s.length();
		if (len == 0) return this;
		final int total = len * cnt;
		ensureCapacity(total);
		final byte[] buf = buffer;
		final int origPos = pos;
		int p = write(s.getBytes(ASCII), origPos);
		int copied = 1;
		while (copied << 1 <= cnt) {
			System.arraycopy(buf, origPos, buf, p, copied * len);
			p += copied * len;
			copied <<= 1;
		}
		final int remain = cnt - copied;
		if (remain > 0) {
			System.arraycopy(buf, origPos, buf, p, remain * len);
			p += remain * len;
		}
		pos = p;
		if (autoFlush) flush();
		return this;
	}

	public FastPrinter printlnRepeat(final char c, final int cnt) {
		if (cnt <= 0) return this;
		final int total = cnt << 1;
		ensureCapacity(total);
		final byte[] buf = buffer;
		final byte b = (byte) c;
		final int origPos = pos;
		buf[origPos] = b;
		buf[origPos + 1] = LINE;
		int p = origPos + 2;
		int copied = 2;
		while (copied << 1 <= total) {
			System.arraycopy(buf, origPos, buf, p, copied);
			p += copied;
			copied <<= 1;
		}
		final int remain = total - copied;
		if (remain > 0) {
			System.arraycopy(buf, origPos, buf, p, remain);
			p += remain;
		}
		pos = p;
		if (autoFlush) flush();
		return this;
	}

	public FastPrinter printlnRepeat(final String s, final int cnt) {
		if (cnt <= 0) return this;
		final int len = s.length();
		if (len == 0) return this;
		final int unitLen = len + 1;
		ensureCapacity(unitLen * cnt);
		final byte[] buf = buffer;
		final int origPos = pos;
		int p = write(s.getBytes(ASCII), origPos);
		buf[p++] = LINE;
		int copied = 1;
		while (copied << 1 <= cnt) {
			System.arraycopy(buf, origPos, buf, p, copied * unitLen);
			p += copied * unitLen;
			copied <<= 1;
		}
		final int remain = cnt - copied;
		if (remain > 0) {
			System.arraycopy(buf, origPos, buf, p, remain * unitLen);
			p += remain * unitLen;
		}
		pos = p;
		if (autoFlush) flush();
		return this;
	}

	public FastPrinter printlnReverse(final boolean[] arr) {
		final int len = arr.length;
		if (len == 0) return this;
		ensureCapacity(len * (MAX_BOOL_DIGITS + 1));
		final byte[] buf = buffer;
		int p = pos;
		for (int i = len - 1; i >= 0; i--) {
			p = write(arr[i], p);
			buf[p++] = LINE;
		}
		pos = p;
		if (autoFlush) flush();
		return this;
	}

	public FastPrinter printlnReverse(final char[] arr) {
		final int len = arr.length;
		if (len == 0) return this;
		ensureCapacity(len << 1);
		final byte[] buf = buffer;
		int p = pos;
		for (int i = len - 1; i >= 0; i--) {
			buf[p] = (byte) arr[i];
			buf[p + 1] = LINE;
			p += 2;
		}
		pos = p;
		if (autoFlush) flush();
		return this;
	}

	public FastPrinter printlnReverse(final int[] arr) {
		final int len = arr.length;
		if (len == 0) return this;
		ensureCapacity(len * (MAX_INT_DIGITS + 1));
		final byte[] buf = buffer;
		int p = pos;
		for (int i = len - 1; i >= 0; i--) {
			p = write(arr[i], p);
			buf[p++] = LINE;
		}
		pos = p;
		if (autoFlush) flush();
		return this;
	}

	public FastPrinter printlnReverse(final long[] arr) {
		final int len = arr.length;
		if (len == 0) return this;
		ensureCapacity(len * (MAX_LONG_DIGITS + 1));
		final byte[] buf = buffer;
		int p = pos;
		for (int i = len - 1; i >= 0; i--) {
			p = write(arr[i], p);
			buf[p++] = LINE;
		}
		pos = p;
		if (autoFlush) flush();
		return this;
	}

	public FastPrinter printlnReverse(final double[] arr) {
		final int len = arr.length;
		for (int i = len - 1; i >= 0; i--) {
			final String s = Double.toString(arr[i]);
			ensureCapacity(s.length() + 1);
			pos = write(s.getBytes(ASCII), pos);
			buffer[pos++] = LINE;
		}
		if (autoFlush) flush();
		return this;
	}

	public FastPrinter printlnReverse(final String[] arr) {
		final int len = arr.length;
		for (int i = len - 1; i >= 0; i--) {
			final String s = arr[i];
			ensureCapacity(s.length() + 1);
			pos = write(s.getBytes(ASCII), pos);
			buffer[pos++] = LINE;
		}
		if (autoFlush) flush();
		return this;
	}

	public FastPrinter printlnReverse(final Object[] arr) {
		final int len = arr.length;
		for (int i = len - 1; i >= 0; i--) println(arr[i]);
		if (autoFlush) flush();
		return this;
	}

	public FastPrinter printReverse(final boolean[] arr) {
		final int len = arr.length;
		if (len == 0) return this;
		ensureCapacity(len * (MAX_BOOL_DIGITS + 1) - 1);
		final byte[] buf = buffer;
		int p = pos;
		p = write(arr[len - 1], p);
		for (int i = len - 2; i >= 0; i--) {
			buf[p] = SPACE;
			p = write(arr[i], p + 1);
		}
		pos = p;
		if (autoFlush) flush();
		return this;
	}

	public FastPrinter printReverse(final char[] arr) {
		final int len = arr.length;
		if (len == 0) return this;
		ensureCapacity((len << 1) - 1);
		final byte[] buf = buffer;
		int p = pos;
		buf[p++] = (byte) arr[len - 1];
		for (int i = len - 2; i >= 0; i--) {
			buf[p] = SPACE;
			buf[p + 1] = (byte) arr[i];
			p += 2;
		}
		pos = p;
		if (autoFlush) flush();
		return this;
	}

	public FastPrinter printReverse(final int[] arr) {
		final int len = arr.length;
		if (len == 0) return this;
		ensureCapacity(len * (MAX_INT_DIGITS + 1) - 1);
		final byte[] buf = buffer;
		int p = pos;
		p = write(arr[len - 1], p);
		for (int i = len - 2; i >= 0; i--) {
			buf[p] = SPACE;
			p = write(arr[i], p + 1);
		}
		pos = p;
		if (autoFlush) flush();
		return this;
	}

	public FastPrinter printReverse(final long[] arr) {
		final int len = arr.length;
		if (len == 0) return this;
		ensureCapacity(len * (MAX_LONG_DIGITS + 1) - 1);
		final byte[] buf = buffer;
		int p = pos;
		p = write(arr[len - 1], p);
		for (int i = len - 2; i >= 0; i--) {
			buf[p] = SPACE;
			p = write(arr[i], p + 1);
		}
		pos = p;
		if (autoFlush) flush();
		return this;
	}

	public FastPrinter printReverse(final double[] arr) {
		final int len = arr.length;
		if (len == 0) return this;
		final String s0 = Double.toString(arr[len - 1]);
		ensureCapacity(s0.length());
		pos = write(s0.getBytes(ASCII), pos);
		for (int i = len - 2; i >= 0; i--) {
			final String s = Double.toString(arr[i]);
			ensureCapacity(s.length() + 1);
			buffer[pos] = SPACE;
			pos = write(s.getBytes(ASCII), pos + 1);
		}
		if (autoFlush) flush();
		return this;
	}

	public FastPrinter printReverse(final String[] arr) {
		final int len = arr.length;
		if (len == 0) return this;
		ensureCapacity(arr[len - 1].length());
		pos = write(arr[len - 1].getBytes(ASCII), pos);
		for (int i = len - 2; i >= 0; i--) {
			ensureCapacity(arr[i].length() + 1);
			buffer[pos] = SPACE;
			pos = write(arr[i].getBytes(ASCII), pos + 1);
		}
		if (autoFlush) flush();
		return this;
	}

	public FastPrinter printReverse(final Object[] arr) {
		final int len = arr.length;
		if (len == 0) return this;
		print(arr[len - 1]);
		for (int i = len - 2; i >= 0; i--) {
			ensureCapacity(1);
			buffer[pos++] = SPACE;
			print(arr[i]);
		}
		if (autoFlush) flush();
		return this;
	}

	private static final class Cache {
		private static final VarHandle SHORT_HANDLE = MethodHandles.byteArrayViewVarHandle(short[].class, ByteOrder.LITTLE_ENDIAN);
		private static final VarHandle INT_HANDLE = MethodHandles.byteArrayViewVarHandle(int[].class, ByteOrder.LITTLE_ENDIAN);
		private static final short[] DIGITS_2 = {
				12336, 12592, 12848, 13104, 13360, 13616, 13872, 14128, 14384, 14640,
				12337, 12593, 12849, 13105, 13361, 13617, 13873, 14129, 14385, 14641,
				12338, 12594, 12850, 13106, 13362, 13618, 13874, 14130, 14386, 14642,
				12339, 12595, 12851, 13107, 13363, 13619, 13875, 14131, 14387, 14643,
				12340, 12596, 12852, 13108, 13364, 13620, 13876, 14132, 14388, 14644,
				12341, 12597, 12853, 13109, 13365, 13621, 13877, 14133, 14389, 14645,
				12342, 12598, 12854, 13110, 13366, 13622, 13878, 14134, 14390, 14646,
				12343, 12599, 12855, 13111, 13367, 13623, 13879, 14135, 14391, 14647,
				12344, 12600, 12856, 13112, 13368, 13624, 13880, 14136, 14392, 14648,
				12345, 12601, 12857, 13113, 13369, 13625, 13881, 14137, 14393, 14649,
		};
		private static final int[] DIGITS_3 = new int[1000];
		private static final long[] POW10 = {
				1, 10, 100, 1_000, 10_000, 100_000, 1_000_000, 10_000_000, 100_000_000,
				1_000_000_000, 10_000_000_000L, 100_000_000_000L, 1_000_000_000_000L,
				10_000_000_000_000L, 100_000_000_000_000L, 1_000_000_000_000_000L,
				10_000_000_000_000_000L, 100_000_000_000_000_000L, 1_000_000_000_000_000_000L
		};

		static {
			int idx = 0;
			for (int i = 0; i < 100; i++) {
				final int base = DIGITS_2[i];
				for (int j = 0; j < 10; j++) {
					DIGITS_3[idx++] = base | ((j + '0') << 16);
				}
			}
		}
	}
}
