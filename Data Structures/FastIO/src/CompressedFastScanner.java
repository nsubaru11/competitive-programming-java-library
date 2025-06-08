import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Supplier;

import static java.util.Arrays.setAll;
import static java.util.Arrays.sort;

@SuppressWarnings("unused")
// FastScannerとContestScannerを統合し、コメントを削除した圧縮版のクラスです。
// コメントが必要なく、文字数を気にするなら、こちらを使用して下さい。
public class CompressedFastScanner {

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
			final StringBuilder sb = new StringBuilder();
			byte b = read();
			while (isWhitespace(b)) b = read();
			while (!isWhitespace(b)) {
				sb.appendCodePoint(b);
				b = read();
			}
			return sb;
		}

		public String nextLine() {
			final StringBuilder sb = new StringBuilder();
			int b = read();
			while (b != 0 && b != '\r' && b != '\n') {
				sb.appendCodePoint(b);
				b = read();
			}
			read();
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
				for (int b = 1, A = a, B = b; b <= y; b++)
					setAll(ps[A][B], c -> c > 0 ? nextLong() + ps[A - 1][B][c] + ps[A][B - 1][c] + ps[A][B][c - 1]
							- ps[A - 1][B - 1][c] - ps[A - 1][B][c - 1] - ps[A][B - 1][c - 1] + ps[A - 1][B - 1][c - 1] : 0);
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
			for (int i = 0; i < n; i++) {
				multiset[nextInt() - 1]++;
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
}
