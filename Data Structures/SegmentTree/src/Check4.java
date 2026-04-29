import static java.lang.Math.*;
import static java.util.Arrays.*;

import java.io.*;
import java.lang.invoke.*;
import java.math.*;
import java.nio.*;
import java.nio.charset.*;
import java.util.*;
import java.util.function.*;

// https://atcoder.jp/contests/practice2/tasks/practice2_j
public final class Check4 {

	// region < Constants & Globals >
	private static final boolean DEBUG = true;
	private static final int MOD = 998244353;
	// private static final int MOD = 1_000_000_007;
	private static final char[] op = new char[]{'L', 'U', 'R', 'D'};
	private static final int[] di = new int[]{0, -1, 0, 1, -1, -1, 1, 1};
	private static final int[] dj = new int[]{-1, 0, 1, 0, -1, 1, 1, -1};
	private static final FastScanner sc = new FastScanner();
	private static final FastPrinter out = new FastPrinter();
	// endregion

	private static void solve() {
		int n = sc.nextInt();
		int q = sc.nextInt();
		IntSegmentTree seg = new IntSegmentTree(n, Math::max, Integer.MIN_VALUE);
		seg.setAll(_ -> sc.nextInt());
		while (q-- > 0) {
			char t = sc.nextChar();
			int a = sc.nextInt0();
			int b = sc.nextInt();
			if (t == '1') {
				seg.set(a, b);
			} else if (t == '2') {
				out.println(seg.query(a, b));
			} else {
				out.println(seg.maxRight(a, v -> v < b) + 1);
			}
		}
	}

	// region < Utility Methods >
	private static boolean isValidRange(final int i, final int from, final int to) {
		return ((i - from) | (to - 1 - i)) >= 0;
	}

	private static boolean isValidRange(final int i, final int j, final int h, final int w) {
		return ((i | j | (h - 1 - i) | (w - 1 - j)) >>> 31) == 0;
	}

	private static void swap(final char[] a, final int i, final int j) {
		final char tmp = a[i];
		a[i] = a[j];
		a[j] = tmp;
	}

	private static void swap(final int[] a, final int i, final int j) {
		final int tmp = a[i];
		a[i] = a[j];
		a[j] = tmp;
	}

	private static void swap(final long[] a, final int i, final int j) {
		final long tmp = a[i];
		a[i] = a[j];
		a[j] = tmp;
	}

	private static boolean chmin(final char[] a, final int i, final char v) {
		if (a[i] <= v) return false;
		a[i] = v;
		return true;
	}

	private static boolean chmin(final int[] a, final int i, final int v) {
		if (a[i] <= v) return false;
		a[i] = v;
		return true;
	}

	private static boolean chmin(final long[] a, final int i, final long v) {
		if (a[i] <= v) return false;
		a[i] = v;
		return true;
	}

	private static boolean chmax(final char[] a, final int i, final char v) {
		if (a[i] >= v) return false;
		a[i] = v;
		return true;
	}

	private static boolean chmax(final int[] a, final int i, final int v) {
		if (a[i] >= v) return false;
		a[i] = v;
		return true;
	}

	private static boolean chmax(final long[] a, final int i, final long v) {
		if (a[i] >= v) return false;
		a[i] = v;
		return true;
	}

	private static int min(final int a, final int b) {
		return Math.min(a, b);
	}

	private static int min(final int a, final int b, final int c) {
		return Math.min(a, Math.min(b, c));
	}

	private static int min(int... a) {
		int len = a.length;
		int min = a[0];
		for (int i = 1; i < len; i++) if (min > a[i]) min = a[i];
		return min;
	}

	private static int max(final int a, final int b) {
		return Math.max(a, b);
	}

	private static int max(final int a, final int b, final int c) {
		return Math.max(a, Math.max(b, c));
	}

	private static int max(int... a) {
		int len = a.length;
		int max = a[0];
		for (int i = 1; i < len; i++) if (max < a[i]) max = a[i];
		return max;
	}

	private static long min(final long a, final long b) {
		return Math.min(a, b);
	}

	private static long min(final long a, final long b, final long c) {
		return Math.min(a, Math.min(b, c));
	}

	private static long min(long... a) {
		int len = a.length;
		long min = a[0];
		for (int i = 1; i < len; i++) if (min > a[i]) min = a[i];
		return min;
	}

	private static long max(final long a, final long b) {
		return Math.max(a, b);
	}

	private static long max(final long a, final long b, final long c) {
		return Math.max(a, Math.max(b, c));
	}

	private static long max(long... a) {
		int len = a.length;
		long max = a[0];
		for (int i = 1; i < len; i++) if (max < a[i]) max = a[i];
		return max;
	}

	private static double min(final double a, final double b) {
		return Math.min(a, b);
	}

	private static double min(final double a, final double b, final double c) {
		return Math.min(a, Math.min(b, c));
	}

	private static double min(double... a) {
		int len = a.length;
		double min = a[0];
		for (int i = 1; i < len; i++) if (min > a[i]) min = a[i];
		return min;
	}

	private static double max(final double a, final double b) {
		return Math.max(a, b);
	}

	private static double max(final double a, final double b, final double c) {
		return Math.max(a, Math.max(b, c));
	}

	private static double max(double... a) {
		int len = a.length;
		double max = a[0];
		for (int i = 1; i < len; i++) if (max < a[i]) max = a[i];
		return max;
	}

	private static long lModPow(long a, long b, final long mod) {
		if (b == 0) return 1;
		long ans = 1;
		for (a %= mod; b > 1; b >>= 1) {
			if ((b & 1) == 1) ans = ans * a % mod;
			a = a * a % mod;
		}
		return ans * a % mod;
	}

	private static int iModPow(int a, int b, final int mod) {
		if (b == 0) return 1;
		int ans = 1;
		for (a %= mod; b > 1; b >>= 1) {
			if ((b & 1) == 1) ans = (int) ((long) ans * a % mod);
			a = (int) ((long) a * a % mod);
		}
		return (int) ((long) ans * a % mod);
	}

	private static long lcmLong(final long x, final long y) {
		return x == 0 || y == 0 ? 0 : x / gcdLong(x, y) * y;
	}

	private static int lcmInt(final int x, final int y) {
		return x == 0 || y == 0 ? 0 : x / gcdInt(x, y) * y;
	}

	private static long gcdLong(long a, long b) {
		a = abs(a);
		b = abs(b);
		if (a == 0) return b;
		if (b == 0) return a;
		int commonShift = Long.numberOfTrailingZeros(a | b);
		a >>= Long.numberOfTrailingZeros(a);
		while (b != 0) {
			b >>= Long.numberOfTrailingZeros(b);
			if (a > b) {
				long tmp = a;
				a = b;
				b = tmp;
			}
			b -= a;
		}
		return a << commonShift;
	}

	private static int gcdInt(int a, int b) {
		a = abs(a);
		b = abs(b);
		if (a == 0) return b;
		if (b == 0) return a;
		int commonShift = Integer.numberOfTrailingZeros(a | b);
		a >>= Integer.numberOfTrailingZeros(a);
		while (b != 0) {
			b >>= Integer.numberOfTrailingZeros(b);
			if (a > b) {
				int tmp = a;
				a = b;
				b = tmp;
			}
			b -= a;
		}
		return a << commonShift;
	}

	private static void compression(final int[] a, final int len) {
		final int[] b = copyOf(a, len);
		sort(b);
		int n = 1;
		for (int i = 1; i < len; i++) {
			if (b[i] == b[i - 1]) continue;
			b[n++] = b[i];
		}
		for (int i = 0; i < len; i++) {
			a[i] = binarySearch(b, 0, n, a[i]);
		}
	}

	private static void compression(final int[][] a, final int n, final int m) {
		final int len = n * m;
		final int[] b = new int[len];
		for (int i = 0, j = 0; i < n; i++, j += m) {
			System.arraycopy(a[i], 0, b, j, m);
		}
		sort(b);
		int nm = 1;
		for (int i = 1; i < len; i++) {
			if (b[i] == b[i - 1]) continue;
			b[nm++] = b[i];
		}
		for (int i = 0; i < n; i++) {
			int[] ai = a[i];
			for (int j = 0; j < m; j++) {
				ai[j] = binarySearch(b, 0, nm, ai[j]);
			}
		}
	}

	private static void compression(final int[][] a, final int n) {
		int len = 0;
		for (int i = 0; i < n; i++) len += a[i].length;
		final int[] b = new int[len];
		for (int i = 0, j = 0; i < n; i++) {
			int m = a[i].length;
			System.arraycopy(a[i], 0, b, j, m);
			j += m;
		}
		sort(b);
		int nm = 1;
		for (int i = 1; i < len; i++) {
			if (b[i] == b[i - 1]) continue;
			b[nm++] = b[i];
		}
		for (int i = 0; i < n; i++) {
			int[] ai = a[i];
			for (int j = 0, m = ai.length; j < m; j++) {
				ai[j] = binarySearch(b, 0, nm, ai[j]);
			}
		}
	}
	// endregion

	// region < I/O & Debug >
	public static void main(final String[] args) {
		try {
			solve();
		} finally {
			out.close();
		}
	}

	private static void debug(final Object... args) {
		if (DEBUG) {
			out.flush();
			System.err.println(deepToString(args));
		}
	}

	@SuppressWarnings("unused")
	private static final class IntSegmentTree implements Iterable<Integer> {
		private final int n, size, identity;
		private final IntBinaryOperator operator;
		private final int[] tree;

		public IntSegmentTree(final int n, final IntBinaryOperator operator, final int identity) {
			this.n = n;
			size = n <= 1 ? 1 : Integer.highestOneBit(n - 1) << 1;
			this.identity = identity;
			this.operator = operator;
			tree = new int[size << 1];
			if (identity != 0) Arrays.fill(tree, identity);
		}

		public IntSegmentTree(final int[] data, final IntBinaryOperator operator, final int identity) {
			this.n = data.length;
			size = n <= 1 ? 1 : Integer.highestOneBit(n - 1) << 1;
			this.identity = identity;
			this.operator = operator;
			tree = new int[size << 1];
			System.arraycopy(data, 0, tree, size, n);
			if (identity != 0) Arrays.fill(tree, size + n, size << 1, identity);
			buildAll();
		}

		public int get(final int i) {
			return tree[size + i];
		}

		public int set(final int i, final int e) {
			return apply(i, 0, e);
		}

		public int add(final int i, final int d) {
			return apply(i, 1, d);
		}

		public int multiply(final int i, final int a) {
			return apply(i, a, 0);
		}

		public int apply(final int i, final int a, final int b) {
			final int idx = size + i;
			tree[idx] = tree[idx] * a + b;
			for (int j = idx >> 1; j > 0; j >>= 1) tree[j] = operator.applyAsInt(tree[j << 1], tree[(j << 1) | 1]);
			return tree[idx];
		}

		public int apply(final int i, final int v, final IntBinaryOperator op) {
			return apply(i, 0, op.applyAsInt(tree[size + i], v));
		}

		public void fill(final int val) {
			Arrays.fill(tree, size, size + n, val);
			buildAll();
		}

		public void setAll(final IntUnaryOperator func) {
			for (int i = 0, idx = size; i < n; i++, idx++) tree[idx] = func.applyAsInt(i);
			buildAll();
		}

		public int query(final int l, final int r) {
			if (l >= r) return identity;
			int sml = identity, smr = identity;
			for (int cl = l + size, cr = r + size; cl < cr; cl >>= 1, cr >>= 1) {
				if ((cl & 1) == 1) sml = operator.applyAsInt(sml, tree[cl++]);
				if ((cr & 1) == 1) smr = operator.applyAsInt(tree[--cr], smr);
			}
			return operator.applyAsInt(sml, smr);
		}

		public int queryAll() {
			return tree[1];
		}

		public int maxRight(final int l, final IntPredicate tester) {
			if (l == n) return n;
			int ans = identity;
			int cl = l + size;
			do {
				cl >>= Integer.numberOfTrailingZeros(cl);
				int combined = operator.applyAsInt(ans, tree[cl]);
				if (!tester.test(combined)) {
					while (cl < size) {
						cl <<= 1;
						combined = operator.applyAsInt(ans, tree[cl]);
						if (tester.test(combined)) {
							ans = combined;
							cl++;
						}
					}
					return cl - size;
				}
				ans = combined;
				cl++;
			} while ((cl & -cl) != cl);
			return n;
		}

		public int minLeft(final int r, final IntPredicate tester) {
			if (r == 0) return 0;
			int ans = identity;
			int cr = r + size - 1;
			do {
				while (cr > 1 && (cr & 1) == 1) cr >>= 1;
				int combined = operator.applyAsInt(tree[cr], ans);
				if (!tester.test(combined)) {
					while (cr < size) {
						cr = (cr << 1) | 1;
						combined = operator.applyAsInt(tree[cr], ans);
						if (tester.test(combined)) {
							ans = combined;
							cr--;
						}
					}
					return cr - size + 1;
				}
				ans = combined;
				cr--;
			} while ((cr & -cr) != cr);
			return 0;
		}

		public int size() {
			return n;
		}

		private void buildAll() {
			for (int i = size - 1; i > 0; i--) tree[i] = operator.applyAsInt(tree[i << 1], tree[(i << 1) | 1]);
		}

		public PrimitiveIterator.OfInt iterator() {
			return new PrimitiveIterator.OfInt() {
				private int idx = 0;

				public boolean hasNext() {
					return idx < n;
				}

				public int nextInt() {
					if (!hasNext()) throw new NoSuchElementException();
					return tree[size + idx++];
				}
			};
		}

		@Override
		public String toString() {
			final StringBuilder s = new StringBuilder();
			s.append(tree[size]);
			for (int i = size + 1; i < size + n; i++) s.append(' ').append(tree[i]);
			return s.toString();
		}

	}

	@SuppressWarnings("unused")
	private static final class FastScanner {
		private static final VarHandle LONG_HANDLE = MethodHandles.byteArrayViewVarHandle(long[].class, ByteOrder.LITTLE_ENDIAN);
		private final byte[] buffer;
		private final int bufferLength;
		private int pos = 0;

		public FastScanner() {
			this(System.in);
		}

		public FastScanner(final InputStream in) {
			try {
				int capacity = in.available() + 64;
				buffer = new byte[capacity];
				bufferLength = in.read(buffer, 0, capacity);
			} catch (final IOException e) {
				throw new RuntimeException(e);
			}
		}

		private int skipSpaces() {
			int p = pos, b;
			do {
				b = buffer[p++];
			} while (b <= 32);
			pos = p;
			return b;
		}

		public boolean hasNext() {
			while (pos < bufferLength) {
				if (buffer[pos] > 32) return true;
				pos++;
			}
			return false;
		}

		public char nextChar() {
			if (!hasNext()) throw new NoSuchElementException();
			return (char) buffer[pos++];
		}

		public int nextInt() {
			int b = skipSpaces();
			boolean negative = b == '-';
			final byte[] buf = buffer;
			int p = pos, n = 0;
			if (negative) b = buf[p++];
			long a = (long) LONG_HANDLE.get(buf, p - 1) ^ 0x3030303030303030L;
			final long check = a & 0xF0F0F0F0F0F0F0F0L;
			if (check == 0) {
				a = (a * 10 + (a >>> 8)) & 0x00FF00FF00FF00FFL;
				a = (a * 100 + (a >>> 16)) & 0x0000FFFF0000FFFFL;
				a = (a * 10000 + (a >>> 32)) & 0x00000000FFFFFFFFL;
				n = (int) a;
				p += 7;
				b = buf[p++];
			}
			while (b > 32) {
				n = (n << 3) + (n << 1) + (b & 15);
				b = buf[p++];
			}
			pos = p;
			return negative ? -n : n;
		}

		public long nextLong() {
			int b = skipSpaces();
			boolean negative = b == '-';
			final byte[] buf = buffer;
			int p = pos;
			if (negative) b = buf[p++];
			long n = 0;
			long a = (long) LONG_HANDLE.get(buf, p - 1) ^ 0x3030303030303030L;
			final long check = a & 0xF0F0F0F0F0F0F0F0L;
			if (check == 0) {
				a = (a * 10 + (a >>> 8)) & 0x00FF00FF00FF00FFL;
				a = (a * 100 + (a >>> 16)) & 0x0000FFFF0000FFFFL;
				a = (a * 10000 + (a >>> 32)) & 0x00000000FFFFFFFFL;
				n = a;
				p += 7;
				b = buf[p++];
				long a2 = (long) LONG_HANDLE.get(buf, p - 1) ^ 0x3030303030303030L;
				final long check2 = a2 & 0xF0F0F0F0F0F0F0F0L;
				if (check2 == 0) {
					a2 = (a2 * 10 + (a2 >>> 8)) & 0x00FF00FF00FF00FFL;
					a2 = (a2 * 100 + (a2 >>> 16)) & 0x0000FFFF0000FFFFL;
					a2 = (a2 * 10000 + (a2 >>> 32)) & 0x00000000FFFFFFFFL;
					n = n * 100000000L + a2;
					p += 7;
					b = buf[p++];
				}
			}
			while (b > 32) {
				n = (n << 3) + (n << 1) + (b & 15);
				b = buf[p++];
			}
			pos = p;
			return negative ? -n : n;
		}

		public double nextDouble() {
			int b = skipSpaces();
			final boolean negative = b == '-';
			final byte[] buf = buffer;
			int p = pos;
			if (negative) b = buf[p++];
			long intPart = 0;
			do {
				intPart = (intPart << 3) + (intPart << 1) + (b & 15);
				b = buf[p++];
			} while ('0' <= b && b <= '9');
			double result = intPart;
			if (b == '.') result += parseFracPart(p, buf);
			else pos = p;
			return negative ? -result : result;
		}

		private double parseFracPart(int p, final byte[] buf) {
			int b = buf[p++];
			long fracPart = 0, divisor = 1;
			do {
				fracPart = fracPart * 10 + (b & 15);
				divisor *= 10;
				b = buf[p++];
			} while ('0' <= b && b <= '9');
			pos = p;
			return (double) fracPart / divisor;
		}

		public String next() {
			skipSpaces();
			final byte[] buf = buffer;
			int p = pos;
			final int start = p - 1;
			while (buf[p] > 32) p++;
			final String s = new String(buf, start, p - start, StandardCharsets.US_ASCII);
			pos = p + 1;
			return s;
		}

		public StringBuilder nextStringBuilder() {
			return new StringBuilder(next());
		}

		public String nextLine() {
			final byte[] buf = buffer;
			int p = pos;
			final int start = p;
			while (p < bufferLength) {
				final int b = buf[p];
				if (b == '\n' || b == '\r') break;
				p++;
			}
			pos = p + (buf[p] == '\r' && buf[p + 1] == '\n' ? 2 : 1);
			return new String(buf, start, p - start, StandardCharsets.US_ASCII);
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

		public int nextInt0() {
			return nextInt() - 1;
		}

		public long nextLong0() {
			return nextLong() - 1;
		}

		public int[] nextInt0(final int n) {
			final int[] a = new int[n];
			for (int i = 0; i < n; i++) a[i] = nextInt() - 1;
			return a;
		}

		public long[] nextLong0(final int n) {
			final long[] a = new long[n];
			for (int i = 0; i < n; i++) a[i] = nextLong() - 1;
			return a;
		}

		public char[] nextChars() {
			skipSpaces();
			int p = pos;
			final int start = p - 1;
			while (buffer[p] > 32) p++;
			final int len = p - start;
			final char[] c = new char[len];
			for (int i = 0; i < len; i++) {
				c[i] = (char) buffer[start + i];
			}
			pos = p + 1;
			return c;
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
			for (int i = 0; i < n; i++) c[i] = nextChars();
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

		public ArrayList<Integer> nextIntAL(final int n) {
			return nextCollection(n, this::nextInt, () -> new ArrayList<>(n));
		}

		public HashSet<Integer> nextIntHS(final int n) {
			return nextCollection(n, this::nextInt, () -> new HashSet<>(n));
		}

		public TreeSet<Integer> nextIntTS(final int n) {
			return nextCollection(n, this::nextInt, TreeSet::new);
		}

		public ArrayList<Long> nextLongAL(final int n) {
			return nextCollection(n, this::nextLong, () -> new ArrayList<>(n));
		}

		public HashSet<Long> nextLongHS(final int n) {
			return nextCollection(n, this::nextLong, () -> new HashSet<>(n));
		}

		public TreeSet<Long> nextLongTS(final int n) {
			return nextCollection(n, this::nextLong, TreeSet::new);
		}

		public ArrayList<Character> nextCharacterAL(final int n) {
			return nextCollection(n, this::nextChar, () -> new ArrayList<>(n));
		}

		public HashSet<Character> nextCharacterHS(final int n) {
			return nextCollection(n, this::nextChar, () -> new HashSet<>(n));
		}

		public TreeSet<Character> nextCharacterTS(final int n) {
			return nextCollection(n, this::nextChar, TreeSet::new);
		}

		public ArrayList<String> nextStringAL(final int n) {
			return nextCollection(n, this::next, () -> new ArrayList<>(n));
		}

		public HashSet<String> nextStringHS(final int n) {
			return nextCollection(n, this::next, () -> new HashSet<>(n));
		}

		public TreeSet<String> nextStringTS(final int n) {
			return nextCollection(n, this::next, TreeSet::new);
		}

		private <S, T extends Collection<S>> T nextCollection(int n, final Supplier<S> input, final Supplier<T> collection) {
			final T t = collection.get();
			while (n-- > 0) t.add(input.get());
			return t;
		}

		public HashMap<Integer, Integer> nextIntMultisetHM(final int n) {
			return nextMultiset(n, this::nextInt, () -> new HashMap<>(n));
		}

		public TreeMap<Integer, Integer> nextIntMultisetTM(final int n) {
			return nextMultiset(n, this::nextInt, TreeMap::new);
		}

		public HashMap<Long, Integer> nextLongMultisetHM(final int n) {
			return nextMultiset(n, this::nextLong, () -> new HashMap<>(n));
		}

		public TreeMap<Long, Integer> nextLongMultisetTM(final int n) {
			return nextMultiset(n, this::nextLong, TreeMap::new);
		}

		public HashMap<Character, Integer> nextCharMultisetHM(final int n) {
			return nextMultiset(n, this::nextChar, () -> new HashMap<>(n));
		}

		public TreeMap<Character, Integer> nextCharMultisetTM(final int n) {
			return nextMultiset(n, this::nextChar, TreeMap::new);
		}

		public HashMap<String, Integer> nextStringMultisetHM(final int n) {
			return nextMultiset(n, this::next, () -> new HashMap<>(n));
		}

		public TreeMap<String, Integer> nextStringMultisetTM(final int n) {
			return nextMultiset(n, this::next, TreeMap::new);
		}

		private <S, T extends Map<S, Integer>> T nextMultiset(int n, final Supplier<S> input, final Supplier<T> map) {
			final T multiSet = map.get();
			while (n-- > 0) {
				final S i = input.get();
				multiSet.put(i, multiSet.getOrDefault(i, 0) + 1);
			}
			return multiSet;
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

	@SuppressWarnings("unused")
	private static final class FastPrinter implements AutoCloseable {
		private static final int MAX_INT_DIGITS = 11;
		private static final int MAX_LONG_DIGITS = 20;
		private static final int MAX_BOOL_DIGITS = 3;
		private static final int DEFAULT_BUFFER_SIZE = 1 << 20;
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
			final int p = write(s, pos);
			buffer[p] = LINE;
			pos = p + 1;
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter println(final StringBuilder s) {
			ensureCapacity(s.length() + 1);
			final int p = write(s, pos);
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
			pos = write(s, pos);
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter print(final StringBuilder s) {
			ensureCapacity(s.length());
			pos = write(s, pos);
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
			final int required = pos + additional;
			final int bufferLength = buffer.length;
			if (required <= bufferLength) return;
			flush();
			if (additional > bufferLength) buffer = new byte[bufferSize(additional)];
		}

		private int write(final boolean b, int p) {
			final byte[] src = b ? Cache.TRUE_BYTES : Cache.FALSE_BYTES;
			final int len = src.length;
			System.arraycopy(src, 0, buffer, p, len);
			return p + len;
		}

		private int write(int i, int p) {
			final byte[] buf = buffer;
			final VarHandle shortHandle = Cache.SHORT_HANDLE;
			final VarHandle intHandle = Cache.INT_HANDLE;
			final short[] digits2 = Cache.DIGITS_2;
			final int[] digits4 = Cache.DIGITS_4;
			if (i >= 0) i = -i;
			else buf[p++] = HYPHEN;
			final int digits = countDigits(i);
			int writePos = p + digits;
			if (i <= -100000000) {
				final int q = i / 100000000;
				final int r = (q * 100000000) - i;
				final int hi = r / 10000;
				intHandle.set(buf, writePos - 8, digits4[hi]);
				intHandle.set(buf, writePos - 4, digits4[r - hi * 10000]);
				writePos -= 8;
				i = q;
			}
			if (i <= -10000) {
				final int q = i / 10000;
				final int r = (q * 10000) - i;
				intHandle.set(buf, writePos - 4, digits4[r]);
				writePos -= 4;
				i = q;
			}
			if (i <= -100) {
				final int q = i / 100;
				final int r = (q * 100) - i;
				shortHandle.set(buf, writePos - 2, digits2[r]);
				writePos -= 2;
				i = q;
			}
			final int r = -i;
			if (r >= 10) shortHandle.set(buf, writePos - 2, digits2[r]);
			else buf[writePos - 1] = (byte) (r + ZERO);
			return p + digits;
		}

		private int write(long l, int p) {
			final byte[] buf = buffer;
			final VarHandle shortHandle = Cache.SHORT_HANDLE;
			final VarHandle intHandle = Cache.INT_HANDLE;
			final short[] digits2 = Cache.DIGITS_2;
			final int[] digits4 = Cache.DIGITS_4;
			if (l >= 0) l = -l;
			else buf[p++] = HYPHEN;
			final int digits = countDigits(l);
			int writePos = p + digits;
			if (l <= -100000000L) {
				long q = l / 100000000L;
				int r = (int) ((q * 100000000L) - l);
				int hi = r / 10000;
				intHandle.set(buf, writePos - 8, digits4[hi]);
				intHandle.set(buf, writePos - 4, digits4[r - hi * 10000]);
				writePos -= 8;
				l = q;
				if (l <= -100000000L) {
					q = l / 100000000L;
					r = (int) ((q * 100000000L) - l);
					hi = r / 10000;
					intHandle.set(buf, writePos - 8, digits4[hi]);
					intHandle.set(buf, writePos - 4, digits4[r - hi * 10000]);
					writePos -= 8;
					l = q;
				}
			}
			if (l <= -10000) {
				final long q = l / 10000;
				final int r = (int) ((q * 10000) - l);
				intHandle.set(buf, writePos - 4, digits4[r]);
				writePos -= 4;
				l = q;
			}
			if (l <= -100) {
				final long q = l / 100;
				final int r = (int) ((q * 100) - l);
				shortHandle.set(buf, writePos - 2, digits2[r]);
				writePos -= 2;
				l = q;
			}
			final int r = (int) -l;
			if (r >= 10) shortHandle.set(buf, writePos - 2, digits2[r]);
			else buf[writePos - 1] = (byte) (r + ZERO);
			return p + digits;
		}

		private int write(final CharSequence s, int p) {
			final int len = s.length();
			final byte[] buf = buffer;
			int i = 0;
			final int limit = len & ~7;
			while (i < limit) {
				buf[p] = (byte) s.charAt(i);
				buf[p + 1] = (byte) s.charAt(i + 1);
				buf[p + 2] = (byte) s.charAt(i + 2);
				buf[p + 3] = (byte) s.charAt(i + 3);
				buf[p + 4] = (byte) s.charAt(i + 4);
				buf[p + 5] = (byte) s.charAt(i + 5);
				buf[p + 6] = (byte) s.charAt(i + 6);
				buf[p + 7] = (byte) s.charAt(i + 7);
				p += 8;
				i += 8;
			}
			while (i < len) buf[p++] = (byte) s.charAt(i++);
			return p;
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
			int p = write(s, origPos);
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
			int p = write(s, origPos);
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
				pos = write(s, pos);
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
				pos = write(s, pos);
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
			pos = write(s0, pos);
			for (int i = len - 2; i >= 0; i--) {
				final String s = Double.toString(arr[i]);
				ensureCapacity(s.length() + 1);
				buffer[pos] = SPACE;
				pos = write(s, pos + 1);
			}
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter printReverse(final String[] arr) {
			final int len = arr.length;
			if (len == 0) return this;
			ensureCapacity(arr[len - 1].length());
			pos = write(arr[len - 1], pos);
			for (int i = len - 2; i >= 0; i--) {
				ensureCapacity(arr[i].length() + 1);
				buffer[pos] = SPACE;
				pos = write(arr[i], pos + 1);
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
			private static final byte[] TRUE_BYTES = {'Y', 'e', 's'};
			private static final byte[] FALSE_BYTES = {'N', 'o'};
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
			private static final int[] DIGITS_4 = new int[10000];
			private static final long[] POW10 = {
					1, 10, 100, 1_000, 10_000, 100_000, 1_000_000, 10_000_000, 100_000_000,
					1_000_000_000, 10_000_000_000L, 100_000_000_000L, 1_000_000_000_000L,
					10_000_000_000_000L, 100_000_000_000_000L, 1_000_000_000_000_000L,
					10_000_000_000_000_000L, 100_000_000_000_000_000L, 1_000_000_000_000_000_000L
			};

			static {
				int idx4 = 0;
				for (int i = 0; i < 100; i++) {
					final int hi = DIGITS_2[i] & 0xFFFF;
					for (int j = 0; j < 100; j++) {
						final int lo = DIGITS_2[j] & 0xFFFF;
						DIGITS_4[idx4++] = (lo << 16) | hi;
					}
				}
			}
		}
	}
	// endregion
}
