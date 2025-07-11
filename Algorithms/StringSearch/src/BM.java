import java.io.*;
import java.util.*;
import java.math.*;
import java.util.function.*;

import static java.lang.Math.*;
import static java.util.Arrays.*;

public class BM {

	private static void solve(final FastScanner sc, final FastPrinter out) {

	}

	public static void main(String[] args) {
		try (final FastScanner sc = new FastScanner();
			 final FastPrinter out = new FastPrinter()) {
			solve(sc, out);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unused")
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

		private static boolean isWhitespace(final int c) {
			return c == ' ' || c == '\n' || c == '\r' || c == '\t';
		}

		@Override
		public void close() throws IOException {
			if (in != System.in)
				in.close();
		}

		public byte read() {
			if (pos >= bufferLength) {
				try {
					bufferLength = in.read(buffer, pos = 0, buffer.length);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				if (bufferLength < 0)
					throw new RuntimeException(new IOException("End of input reached"));
			}
			return buffer[pos++];
		}

		public int nextInt() {
			int b = read();
			while (isWhitespace(b)) b = read();
			boolean negative = b == '-';
			if (negative) b = read();
			int result = 0;
			while ('0' <= b && b <= '9') {
				result = result * 10 + b - '0';
				b = read();
			}
			return negative ? -result : result;
		}

		public long nextLong() {
			int b = read();
			while (isWhitespace(b)) b = read();
			boolean negative = b == '-';
			if (negative) b = read();
			long result = 0;
			while ('0' <= b && b <= '9') {
				result = result * 10 + b - '0';
				b = read();
			}
			return negative ? -result : result;
		}

		public double nextDouble() {
			int b = read();
			while (isWhitespace(b)) b = read();
			boolean negative = b == '-';
			if (negative) b = read();
			double result = 0;
			while ('0' <= b && b <= '9') {
				result = result * 10 + b - '0';
				b = read();
			}
			if (b == '.') {
				b = read();
				double factor = 10;
				while ('0' <= b && b <= '9') {
					result += (b - '0') / factor;
					factor *= 10;
					b = read();
				}
			}
			return negative ? -result : result;
		}

		public char nextChar() {
			byte b = read();
			while (isWhitespace(b)) b = read();
			return (char) b;
		}

		public String next() {
			return nextStringBuilder().toString();
		}

		public StringBuilder nextStringBuilder() {
			StringBuilder sb = new StringBuilder();
			byte b = read();
			while (isWhitespace(b)) b = read();
			while (!isWhitespace(b)) {
				sb.appendCodePoint(b);
				b = read();
			}
			return sb;
		}

		public String nextLine() {
			StringBuilder sb = new StringBuilder();
			int b = read();
			while (b != 0 && b != '\r' && b != '\n') {
				sb.appendCodePoint(b);
				b = read();
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
			setAll(a, i -> nextInt());
			return a;
		}

		public long[] nextLong(final int n) {
			final long[] a = new long[n];
			setAll(a, i -> nextLong());
			return a;
		}

		public double[] nextDouble(final int n) {
			final double[] a = new double[n];
			setAll(a, i -> nextDouble());
			return a;
		}

		public char[] nextChars() {
			return next().toCharArray();
		}

		public char[] nextChars(final int n) {
			final char[] c = new char[n];
			for (int i = 0; i < n; i++)
				c[i] = nextChar();
			return c;
		}

		public String[] nextStrings(final int n) {
			final String[] s = new String[n];
			setAll(s, i -> next());
			return s;
		}

		public int[][] nextIntMat(final int h, final int w) {
			final int[][] a = new int[h][w];
			for (int i = 0; i < h; i++)
				setAll(a[i], j -> nextInt());
			return a;
		}

		public long[][] nextLongMat(final int h, final int w) {
			final long[][] a = new long[h][w];
			for (int i = 0; i < h; i++)
				setAll(a[i], j -> nextLong());
			return a;
		}

		public double[][] nextDoubleMat(final int h, final int w) {
			final double[][] a = new double[h][w];
			for (int i = 0; i < h; i++)
				setAll(a[i], j -> nextDouble());
			return a;
		}

		public char[][] nextCharMat(final int n) {
			final char[][] c = new char[n][];
			setAll(c, j -> nextChars());
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
				setAll(s[i], j -> next());
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
			setAll(ps, i -> i > 0 ? nextInt() + ps[i - 1] : nextInt());
			return ps;
		}

		public long[] nextLongPrefixSum(final int n) {
			final long[] ps = new long[n];
			setAll(ps, i -> i > 0 ? nextLong() + ps[i - 1] : nextLong());
			return ps;
		}

		public int[][] nextIntPrefixSum(final int h, final int w) {
			final int[][] ps = new int[h + 1][w + 1];
			for (int i = 1; i <= h; i++) {
				final int j = i;
				setAll(ps[i], k -> k > 0 ? nextInt() + ps[j - 1][k] + ps[j][k - 1] - ps[j - 1][k - 1] : 0);
			}
			return ps;
		}

		public long[][] nextLongPrefixSum(final int h, final int w) {
			final long[][] ps = new long[h + 1][w + 1];
			for (int i = 1; i <= h; i++) {
				final int j = i;
				setAll(ps[i], k -> k > 0 ? nextLong() + ps[j - 1][k] + ps[j][k - 1] - ps[j - 1][k - 1] : 0);
			}
			return ps;
		}

		public int[][][] nextIntPrefixSum(final int x, final int y, final int z) {
			final int[][][] ps = new int[x + 1][y + 1][z + 1];
			for (int a = 1; a <= x; a++)
				for (int b = 1; b <= y; b++) {
					final int A = a, B = b;
					setAll(ps[A][B], c -> c > 0 ? nextInt() + ps[A - 1][B][c] + ps[A][B - 1][c] + ps[A][B][c - 1]
							- ps[A - 1][B - 1][c] - ps[A - 1][B][c - 1] - ps[A][B - 1][c - 1] + ps[A - 1][B - 1][c - 1] : 0);
				}
			return ps;
		}

		public long[][][] nextLongPrefixSum(final int x, final int y, final int z) {
			final long[][][] ps = new long[x + 1][y + 1][z + 1];
			for (int a = 1; a <= x; a++)
				for (int b = 1; b <= y; b++) {
					final int A = a, B = b;
					setAll(ps[A][B], c -> c > 0 ? nextLong() + ps[A - 1][B][c] + ps[A][B - 1][c] + ps[A][B][c - 1]
							- ps[A - 1][B - 1][c] - ps[A - 1][B][c - 1] - ps[A][B - 1][c - 1] + ps[A - 1][B - 1][c - 1] : 0);
				}
			return ps;
		}

		public int[] nextIntInverseMapping(final int n) {
			final int[] inv = new int[n];
			for (int i = 0; i < n; i++)
				inv[nextInt() - 1] = i;
			return inv;
		}

		private <T extends Collection<Integer>> T nextIntCollection(int n, final Supplier<T> supplier) {
			final T collection = supplier.get();
			while (n-- > 0) {
				collection.add(nextInt());
			}
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
			while (n-- > 0) {
				collection.add(nextLong());
			}
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
			while (n-- > 0) {
				collection.add(nextChar());
			}
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
			while (n-- > 0) {
				collection.add(next());
			}
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
			final T collection = supplier.get();
			while (n-- > 0) {
				final int i = nextInt();
				collection.put(i, collection.getOrDefault(i, 0) + 1);
			}
			return collection;
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
				multiSet.put(next(), multiSet.getOrDefault(s, 0) + 1);
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
			for (int i = 0; i < n; i++) {
				final int value = nextInt() - 1;
				multiset[value] = multiset[value] + 1;
			}
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

	@SuppressWarnings("unused")
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
			print(Double.toString(d), true);
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
			print(s, true);
		}

		public void println(final Object o) {
			if (o == null) return;
			if (o instanceof String s) {
				print(s, true);
			} else if (o instanceof Long l) {
				println(l.longValue());
			} else if (o instanceof Integer i) {
				println(i.intValue());
			} else if (o instanceof Double d) {
				print(d.toString(), true);
			} else if (o instanceof Boolean b) {
				println(b.booleanValue());
			} else if (o instanceof Character c) {
				println(c.charValue());
			} else {
				print(o.toString(), true);
			}
		}

		public void println(final BigInteger bi) {
			print(bi.toString(), true);
		}

		public void println(final BigDecimal bd) {
			print(bd.toString(), true);
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
			print(Double.toString(d), false);
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
			print(s, false);
		}

		public void print(final Object o) {
			if (o == null) return;
			if (o instanceof String s) {
				print(s, false);
			} else if (o instanceof Long l) {
				print(l.longValue());
			} else if (o instanceof Integer i) {
				print(i.intValue());
			} else if (o instanceof Double d) {
				print(d.toString(), false);
			} else if (o instanceof Boolean b) {
				print(b.booleanValue());
			} else if (o instanceof Character c) {
				print(c.charValue());
			} else {
				print(o.toString(), false);
			}
		}

		public void print(final BigInteger bi) {
			print(bi.toString(), false);
		}

		public void print(final BigDecimal bd) {
			print(bd.toString(), false);
		}

		public void printf(final String format, final Object... args) {
			print(String.format(format, args), false);
		}

		public void printf(final Locale locale, final String format, final Object... args) {
			print(String.format(locale, format, args), false);
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

		private void print(final String s, final boolean newline) {
			fillBuffer(s);
			if (newline) {
				ensureBufferSpace(1);
				buffer[pos++] = '\n';
			}
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
			boolean negative = (i < 0);
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
			boolean negative = (l < 0);
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
			print(arr[0], false);
			for (int i = 1; i < len; i++) {
				print(delimiter);
				print(arr[i], false);
			}
		}

		public <T> void println(final int[] arr, final IntFunction<T> function) {
			if (arr == null) return;
			for (final int i : arr)
				println(function.apply(i));
		}

		public <T> void println(final long[] arr, final LongFunction<T> function) {
			if (arr == null) return;
			for (final long l : arr)
				println(function.apply(l));
		}

		public <T> void println(final char[] arr, final Function<Character, T> function) {
			if (arr == null) return;
			for (final char c : arr)
				println(function.apply(c));
		}

		public <T> void println(final boolean[] arr, final Function<Boolean, T> function) {
			if (arr == null) return;
			for (final boolean b : arr)
				println(function.apply(b));
		}

		public <T> void println(final String[] arr, final Function<String, T> function) {
			if (arr == null) return;
			for (final String s : arr)
				println(function.apply(s));
		}

		public <T> void print(final int[] arr, final IntFunction<T> function) {
			if (arr == null) return;
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
			for (final int[] arr : arr2d) {
				print(arr, function);
				println();
			}
		}

		public <T> void println(final long[][] arr2d, final LongFunction<T> function) {
			if (arr2d == null) return;
			for (final long[] arr : arr2d) {
				print(arr, function);
				println();
			}
		}

		public <T> void println(final char[][] arr2d, final LongFunction<T> function) {
			if (arr2d == null) return;
			for (final char[] arr : arr2d) {
				print(arr, function);
				println();
			}
		}

		public <T> void println(final boolean[][] arr2d, final Function<Boolean, T> function) {
			if (arr2d == null) return;
			for (final boolean[] arr : arr2d) {
				print(arr, function);
				println();
			}
		}

		public <T> void println(final String[][] arr2d, final Function<String, T> function) {
			if (arr2d == null) return;
			for (final String[] arr : arr2d) {
				print(arr, function);
				println();
			}
		}
	}
}
