import sun.misc.*;

import java.io.*;
import java.lang.reflect.*;
import java.math.*;
import java.util.*;
import java.util.function.*;

import static java.lang.Math.*;
import static java.util.Arrays.*;

// https://judge.yosupo.jp/problem/static_range_sum
public final class Check1 {

	// region < Constants & Globals >
	private static final boolean DEBUG;
	private static final int MOD;
	private static final int[] di;
	private static final int[] dj;
	private static final FastScanner sc;
	private static final FastPrinter out;

	static {
		DEBUG = true;
		MOD = 998244353;
		// MOD = 1_000_000_007;
		di = new int[]{0, -1, 0, 1, -1, -1, 1, 1};
		dj = new int[]{-1, 0, 1, 0, -1, 1, 1, -1};
		sc = new FastScanner(System.in);
		out = new FastPrinter(System.out);
	}
	// endregion

	private static void solve() {
		int n = sc.nextInt();
		int q = sc.nextInt();
		IntArray1D s = new IntArray1D(n, i -> sc.nextInt());
		while (q-- > 0) {
			int l = sc.nextInt();
			int r = sc.nextInt();
			out.println(s.sum(l, r - 1));
		}
	}

	/**
	 * 不変長整数配列 + 高速回転操作
	 */
	@SuppressWarnings("unused")
	private static final class IntArray1D implements Iterable<Integer> {
		public final long sum;
		public final int size, max, min;
		private final int[] arr;
		private final long[] ps;
		private int offset = 0;

		public IntArray1D(final int n, final IntUnaryOperator init) {
			size = n;
			arr = new int[2 * n];
			ps = new long[2 * n + 1];
			int v = init.applyAsInt(0);
			arr[0] = v;
			int mx, mn;
			long s = mx = mn = v;
			for (int i = 1; i < n; i++) {
				v = init.applyAsInt(i);
				arr[i] = v;
				s += v;
				if (v > mx) mx = v;
				if (v < mn) mn = v;
			}
			sum = s;
			max = mx;
			min = mn;
			System.arraycopy(arr, 0, arr, n, n);
			for (int i = 0; i < 2 * n; i++) {
				ps[i + 1] = ps[i] + arr[i];
			}
		}

		public int get(final int i) {
			return arr[offset + i];
		}

		public long sum(final int l, final int r) {
			return ps[offset + r + 1] - ps[offset + l];
		}

		public long sum() {
			return sum;
		}

		public int getMax() {
			return max;
		}

		public int getMin() {
			return min;
		}

		public void lShift() {
			offset++;
			if (offset == size) offset = 0;
		}

		public void rShift() {
			offset--;
			if (offset == -1) offset = size - 1;
		}

		public void lShift(final int n) {
			if (n < 0) rShift(-n);
			offset += n % size;
			if (offset >= size) offset -= size;
		}

		public void rShift(final int n) {
			if (n < 0) lShift(-n);
			offset -= n % size;
			if (offset < 0) offset += size;
		}

		public void resetRotation() {
			offset = 0;
		}

		/**
		 * 極大値（両隣より大きい）の個数
		 *
		 * @return 極大値の個数
		 */
		public int localMaxCnt() {
			if (size < 3) return 0;
			int cnt = 0, l = arr[offset], m = arr[offset + 1];
			for (int i = 2; i < size; i++) {
				final int r = arr[i + offset];
				if (l < m && m > r) cnt++;
				l = m;
				m = r;
			}
			return cnt;
		}

		/**
		 * 極小値の個数
		 *
		 * @return 極小値の個数
		 */
		public int localMinCnt() {
			if (size < 3) return 0;
			int cnt = 0, l = arr[offset], m = arr[offset + 1];
			for (int i = 2; i < size; i++) {
				final int r = arr[offset + i];
				if (l > m && m < r) cnt++;
				l = m;
				m = r;
			}
			return cnt;
		}

		/**
		 * 同じ要素が連続する最長の長さを返します
		 *
		 * @return 同じ要素が連続する最長の長さ
		 */
		public int runLen() {
			if (size == 1) return 1;
			int len = 1, cnt = 1, prev = arr[offset];
			for (int i = 1; i < size; i++) {
				final int cur = arr[offset + i];
				if (prev == cur) {
					if (len < ++cnt) len = cnt;
				} else {
					cnt = 1;
					prev = cur;
				}
			}
			return len;
		}

		/**
		 * 長さ k の連続部分列の最大和
		 *
		 * @param k 長さ
		 * @return 最大連続部分和
		 */
		public long maxWin(final int k) {
			if (k == size) return sum;
			long win = 0;
			for (int i = 0; i < k; i++) win += arr[offset + i];
			long res = win;
			for (int i = k; i < size; i++) {
				win += arr[offset + i] - arr[offset + i - k];
				if (win > res) res = win;
			}
			return res;
		}

		/**
		 * 長さ k の連続部分列の最小和
		 *
		 * @param k 長さ
		 * @return 最小連続部分和
		 */
		public long minWin(final int k) {
			if (k == size) return sum;
			long win = 0;
			for (int i = 0; i < k; i++) win += arr[offset + i];
			long res = win;
			for (int i = k; i < size; i++) {
				win += arr[offset + i] - arr[offset + i - k];
				if (win < res) res = win;
			}
			return res;
		}

		/**
		 * 長さ k のスライディングウィンドウにおいて、
		 * ある要素が最大値として連続して存在する最長期間
		 *
		 * @param k ウィンドウサイズ
		 * @return 最大値が連続する最長期間
		 */
		public int winMaxLen(final int k) {
			if (size < k) return -1;
			final ArrayDeque<Integer> dq = new ArrayDeque<>(k);
			for (int i = 0; i < k; i++) {
				final int v = arr[offset + i];
				while (!dq.isEmpty() && arr[offset + dq.peekLast()] < v) dq.pollLast();
				dq.addLast(i);
			}
			int len = 1, maxIdx = dq.peekFirst();
			for (int i = k; i < size; i++) {
				if (!dq.isEmpty() && dq.peekFirst() <= i - k) dq.pollFirst();
				final int v = arr[offset + i];
				while (!dq.isEmpty() && arr[offset + dq.peekLast()] < v) dq.pollLast();
				dq.addLast(i);
				if (dq.peek() == maxIdx) len++;
				else {
					maxIdx = dq.peekFirst();
					len = 1;
				}
			}
			return len;
		}

		/**
		 * 長さ k のスライディングウィンドウにおいて、
		 * ある要素が最小値として連続して存在する最長期間
		 *
		 * @param k ウィンドウサイズ
		 * @return 最小値が連続する最長期間
		 */
		public int winMinLen(final int k) {
			if (size < k) return -1;
			final ArrayDeque<Integer> dq = new ArrayDeque<>(k);
			for (int i = 0; i < k; i++) {
				final int v = arr[offset + i];
				while (!dq.isEmpty() && arr[offset + dq.peekLast()] > v) dq.pollLast();
				dq.addLast(i);
			}
			int len = 1, minIdx = dq.peekFirst();
			for (int i = k; i < size; i++) {
				final int v = arr[offset + i];
				if (!dq.isEmpty() && dq.peekFirst() <= i - k) dq.pollFirst();
				while (!dq.isEmpty() && arr[offset + dq.peekLast()] > v) dq.pollLast();
				dq.addLast(i);
				if (dq.peek() == minIdx) len++;
				else {
					minIdx = dq.peekFirst();
					len = 1;
				}
			}
			return len;
		}

		/**
		 * 最長増加部分列の長さ（狭義単調増加）
		 *
		 * @return LIS の長さ
		 */
		public int lis() {
			if (size <= 1) return size;
			final int[] dp = new int[size];
			int len = 0;
			for (int i = 0; i < size; i++) {
				final int v = arr[offset + i];
				int pos = bs(dp, len, v);
				if (pos < 0) {
					pos = ~pos;
					dp[pos] = v;
				}
				if (pos == len) len++;
			}
			return len;
		}

		/**
		 * 最長増加部分列の長さ（広義単調増加）
		 *
		 * @return LIS の長さ
		 */
		public int lnds() {
			if (size <= 1) return size;
			final int[] dp = new int[size];
			int len = 0;
			for (int i = 0; i < size; i++) {
				final int v = arr[offset + i];
				int pos = bsUpper(dp, len, v);
				pos = pos < 0 ? ~pos : pos + 1;
				dp[pos] = v;
				if (pos == len) len++;
			}
			return len;
		}

		/**
		 * 最長減少部分列の長さ（狭義単調減少）
		 *
		 * @return LDS の長さ
		 */
		public int lds() {
			if (size <= 1) return size;
			final int[] dp = new int[size];
			int len = 0;
			for (int i = 0; i < size; i++) {
				final int v = -arr[offset + i];
				int pos = bs(dp, len, v);
				if (pos < 0) {
					pos = ~pos;
					dp[pos] = v;
				}
				if (pos == len) len++;
			}
			return len;
		}

		/**
		 * 最長減少部分列の長さ（広義単調減少）
		 *
		 * @return LDS の長さ
		 */
		public int lnis() {
			if (size <= 1) return size;
			final int[] dp = new int[size];
			int len = 0;
			for (int i = 0; i < size; i++) {
				final int v = -arr[offset + i];
				int pos = bsUpper(dp, len, v);
				pos = pos < 0 ? ~pos : pos + 1;
				dp[pos] = v;
				if (pos == len) len++;
			}
			return len;
		}

		/**
		 * 任意個選択の部分和判定
		 *
		 * @param target 目標和
		 * @return 部分集合の和が target になるか
		 * @throws UnsupportedOperationException n > 60 の場合
		 */
		public boolean subset(final long target) {
			if (size == 1) return target == arr[0] || target == 0;
			if (target == 0 || target == sum) return true;
			if (target < 0 || target > sum) return false;
			return subsetMitm(target);
		}

		/**
		 * ちょうど k 個選択の部分和判定
		 *
		 * @param target 目標和
		 * @param k      選択個数
		 * @return k 個選んで和が target になるか
		 */
		public boolean subset(final long target, final int k) {
			if (size == 1) return target == arr[0];
			return subsetRec(target, k, size - 1);
		}

		private boolean subsetMitm(final long t) {
			final int half = size >> 1;
			final HashSet<Long> set = new HashSet<>();
			for (int m = 0; m < 1 << half; m++) {
				long s = 0;
				for (int i = 0; i < half; i++) {
					if ((m & (1 << i)) != 0) s += arr[i];
				}
				if (s == t) return true;
				set.add(s);
			}
			for (int m = 0; m < (1 << (size - half)); m++) {
				long s = 0;
				for (int i = 0, x = half; i < size - half; i++, x++) {
					if ((m & (1 << i)) != 0) s += arr[x];
				}
				if (set.contains(t - s)) return true;
			}
			return false;
		}

		public boolean subsetRec(final long target, final int k, final int idx) {
			if (k == 0) return target == 0;
			if (idx < 0 || idx + 1 < k) return false;
			if (idx == k - 1) return target == ps[idx + 1];
			if (ps[idx + 1] < target) return false;
			if (subsetRec(target - arr[idx], k - 1, idx - 1)) return true;
			return subsetRec(target, k, idx - 1);
		}

		private int bs(final int[] a, final int len, final int t) {
			int l = 0, r = len - 1;
			while (l <= r) {
				final int m = l + ((r - l) >>> 1);
				if (a[m] > t) r = m - 1;
				else if (a[m] < t) l = m + 1;
				else return m;
			}
			return ~l;
		}

		private int bsUpper(final int[] a, final int len, final int t) {
			int ans = -1;
			int l = 0, r = len - 1;
			while (l <= r) {
				final int m = l + ((r - l) >>> 1);
				if (a[m] > t) r = m - 1;
				else {
					if (a[m] == t) ans = m;
					l = m + 1;
				}
			}
			return ans == -1 ? ~l : ans;
		}

		public PrimitiveIterator.OfInt iterator() {
			return new PrimitiveIterator.OfInt() {
				private int idx = 0;

				public boolean hasNext() {
					return idx < size;
				}

				public int nextInt() {
					return arr[offset + idx++];
				}
			};
		}

		public String toString() {
			final StringBuilder sb = new StringBuilder();
			final PrimitiveIterator.OfInt it = iterator();
			sb.append(it.nextInt());
			while (it.hasNext()) sb.append(' ').append(it.nextInt());
			return sb.toString();
		}
	}

	// region < Utility Methods >
	private static boolean isValidRange(final int i, final int j, final int h, final int w) {
		return 0 <= i && i < h && 0 <= j && j < w;
	}

	private static long modPow(long a, long b, final long mod) {
		long ans = 1;
		for (a %= mod; b > 0; a = a * a % mod, b >>= 1) {
			if ((b & 1) == 1) ans = ans * a % mod;
		}
		return ans;
	}

	private static long floorLong(final long a, final long b) {
		return a < 0 ? (a - b + 1) / b : a / b;
	}

	private static int floorInt(final int a, final int b) {
		return a < 0 ? (a - b + 1) / b : a / b;
	}

	private static long ceilLong(final long a, final long b) {
		return a < 0 ? a / b : (a + b - 1) / b;
	}

	private static long ceilInt(final int a, final int b) {
		return a < 0 ? a / b : (a + b - 1) / b;
	}

	private static long LCM(final long x, final long y) {
		return x == 0 || y == 0 ? 0 : x * (y / GCD(x, y));
	}

	public static long GCD(long a, long b) {
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
	// endregion

	// region < I/O & Debug >
	public static void main(final String[] args) {
		try {
			solve();
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			sc.close();
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
		public void close() {
			try {
				if (in != System.in) in.close();
				pos = 0;
				bufferLength = 0;
			} catch (final IOException e) {
				throw new RuntimeException(e);
			}
		}

		private int read() {
			if (pos >= bufferLength) {
				try {
					bufferLength = in.read(buffer, pos = 0, buffer.length);
				} catch (final IOException e) {
					throw new RuntimeException(e);
				}
				if (bufferLength <= 0) throw new RuntimeException(new EOFException());
			}
			return buffer[pos++] & 0xFF;
		}

		public int peek() {
			try {
				int b = skipSpaces();
				pos--;
				return b;
			} catch (final RuntimeException e) {
				return 0;
			}
		}

		public boolean hasNext() {
			return peek() != 0;
		}

		public int nextInt() {
			int b = skipSpaces();
			boolean negative = false;
			if (b == '-') {
				negative = true;
				b = read();
			}
			int result = 0;
			do {
				result = (result << 3) + (result << 1) + (b & 15);
				b = read();
			} while (b >= '0' && b <= '9');
			return negative ? -result : result;
		}

		public long nextLong() {
			int b = skipSpaces();
			boolean negative = false;
			if (b == '-') {
				negative = true;
				b = read();
			}
			long result = 0;
			do {
				result = (result << 3) + (result << 1) + (b & 15);
				b = read();
			} while (b >= '0' && b <= '9');
			return negative ? -result : result;
		}

		public double nextDouble() {
			int b = skipSpaces();
			boolean negative = false;
			if (b == '-') {
				negative = true;
				b = read();
			}
			long intPart = 0;
			do {
				intPart = (intPart << 3) + (intPart << 1) + (b & 15);
				b = read();
			} while (b >= '0' && b <= '9');
			double result = intPart;
			if (b == '.') {
				b = read();
				double scale = 0.1;
				do {
					result += (b & 15) * scale;
					scale *= 0.1;
					b = read();
				} while (b >= '0' && b <= '9');
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
			do {
				sb.append((char) b);
				b = read();
			} while (b > 32);
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
		private static final int DEFAULT_BUFFER_SIZE = 65536;
		private static final byte LINE = '\n';
		private static final byte SPACE = ' ';
		private static final byte HYPHEN = '-';
		private static final byte PERIOD = '.';
		private static final byte ZERO = '0';
		private static final byte[] TRUE_BYTES = {'Y', 'e', 's'};
		private static final byte[] FALSE_BYTES = {'N', 'o'};
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
		private static final long[] POW10 = {
				1, 10, 100, 1_000, 10_000, 100_000, 1_000_000, 10_000_000, 100_000_000,
				1_000_000_000, 10_000_000_000L, 100_000_000_000L, 1_000_000_000_000L,
				10_000_000_000_000L, 100_000_000_000_000L, 1_000_000_000_000_000L,
				10_000_000_000_000_000L, 100_000_000_000_000_000L, 1_000_000_000_000_000_000L
		};
		private static final Unsafe UNSAFE;
		private static final long STRING_VALUE_OFFSET;
		private static final long ABSTRACT_STRING_BUILDER_VALUE_OFFSET;


		static {
			try {
				final Field f = Unsafe.class.getDeclaredField("theUnsafe");
				f.setAccessible(true);
				UNSAFE = (Unsafe) f.get(null);
				STRING_VALUE_OFFSET = UNSAFE.objectFieldOffset(String.class.getDeclaredField("value"));
				final Class<?> asbClass = Class.forName("java.lang.AbstractStringBuilder");
				ABSTRACT_STRING_BUILDER_VALUE_OFFSET = UNSAFE.objectFieldOffset(asbClass.getDeclaredField("value"));
			} catch (final Exception e) {
				throw new RuntimeException("Unsafe initialization failed. Check Java version and environment.", e);
			}
		}

		private final OutputStream out;
		private final boolean autoFlush;
		private byte[] buffer;
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
			this.buffer = new byte[max(64, roundUpToPowerOfTwo(bufferSize))];
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

		private static int roundUpToPowerOfTwo(int x) {
			if (x <= 1) return 1;
			x--;
			x |= x >>> 1;
			x |= x >>> 2;
			x |= x >>> 4;
			x |= x >>> 8;
			x |= x >>> 16;
			return x + 1;
		}

		@Override
		public void close() {
			try {
				flush();
				if (out != System.out) out.close();
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
			write(b);
			ensureCapacity(1);
			buffer[pos++] = LINE;
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter println(final byte b) {
			ensureCapacity(2);
			buffer[pos++] = b;
			buffer[pos++] = LINE;
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter println(final char c) {
			ensureCapacity(2);
			buffer[pos++] = (byte) c;
			buffer[pos++] = LINE;
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter println(final int i) {
			ensureCapacity(MAX_INT_DIGITS + 1);
			write(i);
			buffer[pos++] = LINE;
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter println(final long l) {
			ensureCapacity(MAX_LONG_DIGITS + 1);
			write(l);
			buffer[pos++] = LINE;
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
			write(s);
			buffer[pos++] = LINE;
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter println(final StringBuilder s) {
			ensureCapacity(s.length() + 1);
			write(s);
			buffer[pos++] = LINE;
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter println(final Object o) {
			if (o == null) return println();
			else if (o instanceof Boolean b) {
				return println(b.booleanValue());
			} else if (o instanceof Byte b) {
				return println(b.byteValue());
			} else if (o instanceof Character c) {
				return println(c.charValue());
			} else if (o instanceof Integer i) {
				return println(i.intValue());
			} else if (o instanceof Long l) {
				return println(l.longValue());
			} else if (o instanceof Double d) {
				return println(d.toString());
			} else if (o instanceof BigInteger bi) {
				return println(bi.toString());
			} else if (o instanceof BigDecimal bd) {
				return println(bd.toString());
			} else if (o instanceof String s) {
				return println(s);
			} else if (o instanceof boolean[] arr) {
				return println(arr);
			} else if (o instanceof char[] arr) {
				return println(arr);
			} else if (o instanceof int[] arr) {
				return println(arr);
			} else if (o instanceof long[] arr) {
				return println(arr);
			} else if (o instanceof double[] arr) {
				return println(arr);
			} else if (o instanceof String[] arr) {
				return println(arr);
			} else if (o instanceof Object[] arr) {
				return println(arr);
			} else {
				return println(o.toString());
			}
		}

		public FastPrinter print(final boolean b) {
			write(b);
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
			write(i);
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter print(final long l) {
			ensureCapacity(MAX_LONG_DIGITS);
			write(l);
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
			write(s);
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter print(final StringBuilder s) {
			ensureCapacity(s.length());
			write(s);
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter print(final Object o) {
			if (o == null) return this;
			else if (o instanceof Boolean b) {
				return print(b.booleanValue());
			} else if (o instanceof Byte b) {
				return print(b.byteValue());
			} else if (o instanceof Character c) {
				return print(c.charValue());
			} else if (o instanceof Integer i) {
				return print(i.intValue());
			} else if (o instanceof Long l) {
				return print(l.longValue());
			} else if (o instanceof Double d) {
				return print(d.toString());
			} else if (o instanceof BigInteger bi) {
				return print(bi.toString());
			} else if (o instanceof BigDecimal bd) {
				return print(bd.toString());
			} else if (o instanceof String s) {
				return print(s);
			} else if (o instanceof boolean[] arr) {
				return print(arr);
			} else if (o instanceof char[] arr) {
				return print(arr);
			} else if (o instanceof int[] arr) {
				return print(arr);
			} else if (o instanceof long[] arr) {
				return print(arr);
			} else if (o instanceof double[] arr) {
				return print(arr);
			} else if (o instanceof String[] arr) {
				return print(arr);
			} else if (o instanceof Object[] arr) {
				return print(arr);
			} else {
				return print(o.toString());
			}
		}

		public FastPrinter printf(final String format, final Object... args) {
			return print(String.format(format, args));
		}

		public FastPrinter printf(final Locale locale, final String format, final Object... args) {
			return print(String.format(locale, format, args));
		}

		private void ensureCapacity(final int additional) {
			final int required = pos + additional;
			if (required <= buffer.length) return;
			if (required <= 1_000_000_000) {
				buffer = copyOf(buffer, roundUpToPowerOfTwo(required));
			} else {
				flush();
			}
		}

		private void write(final boolean b) {
			final byte[] src = b ? TRUE_BYTES : FALSE_BYTES;
			final int len = src.length;
			ensureCapacity(len);
			System.arraycopy(src, 0, buffer, pos, len);
			pos += len;
		}

		private void write(int i) {
			final byte[] buf = buffer;
			int p = pos;
			if (i >= 0) i = -i;
			else buf[p++] = HYPHEN;
			final int digits = countDigits(i);
			int writePos = p + digits;
			while (i <= -100) {
				final int q = i / 100;
				final int r = (q << 6) + (q << 5) + (q << 2) - i;
				buf[--writePos] = DigitOnes[r];
				buf[--writePos] = DigitTens[r];
				i = q;
			}
			final int r = -i;
			buf[--writePos] = DigitOnes[r];
			if (r >= 10) buf[--writePos] = DigitTens[r];
			pos = p + digits;
		}

		private void write(long l) {
			final byte[] buf = buffer;
			int p = pos;
			if (l >= 0) l = -l;
			else buf[p++] = HYPHEN;
			final int digits = countDigits(l);
			int writePos = p + digits;
			while (l <= -100) {
				final long q = l / 100;
				final int r = (int) ((q << 6) + (q << 5) + (q << 2) - l);
				buf[--writePos] = DigitOnes[r];
				buf[--writePos] = DigitTens[r];
				l = q;
			}
			final int r = (int) -l;
			buf[--writePos] = DigitOnes[r];
			if (r >= 10) buf[--writePos] = DigitTens[r];
			pos = p + digits;
		}

		private void write(final String s) {
			final byte[] src = (byte[]) UNSAFE.getObject(s, STRING_VALUE_OFFSET);
			final int len = s.length();
			System.arraycopy(src, 0, buffer, pos, len);
			pos += len;
		}

		private void write(final StringBuilder s) {
			final byte[] src = (byte[]) UNSAFE.getObject(s, ABSTRACT_STRING_BUILDER_VALUE_OFFSET);
			final int len = s.length();
			System.arraycopy(src, 0, buffer, pos, len);
			pos += len;
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
			write(a);
			buffer[pos++] = (byte) delimiter;
			write(b);
			buffer[pos++] = LINE;
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
			write(a);
			buffer[pos++] = (byte) delimiter;
			write(b);
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter println(final double d, final int n) {
			return print(d, n).println();
		}

		public FastPrinter print(double d, int n) {
			if (n <= 0) return print(round(d));
			if (d < 0) {
				ensureCapacity(1);
				buffer[pos++] = HYPHEN;
				d = -d;
			}
			if (n > 18) n = 18;
			final long intPart = (long) d;
			final long fracPart = (long) ((d - intPart) * POW10[n]);
			print(intPart);
			int leadingZeros = n - countDigits(-fracPart);
			ensureCapacity(leadingZeros + 1);
			buffer[pos++] = PERIOD;
			while (leadingZeros-- > 0) buffer[pos++] = ZERO;
			print(fracPart);
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
			print(arr[from]);
			for (int i = from + 1; i < to; i++) {
				ensureCapacity(1);
				buffer[pos++] = (byte) delimiter;
				write(arr[i]);
			}
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter print(final char[] arr, final int from, final int to, final char delimiter) {
			if (from >= to) return this;
			ensureCapacity(((to - from) << 1) - 1);
			byte[] buf = buffer;
			int p = pos;
			buf[p++] = (byte) arr[from];
			for (int i = from + 1; i < to; i++) {
				buf[p++] = (byte) delimiter;
				buf[p++] = (byte) arr[i];
			}
			pos = p;
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter print(final int[] arr, final int from, final int to, final char delimiter) {
			if (from >= to) return this;
			final int len = to - from;
			ensureCapacity(len * (MAX_INT_DIGITS + 1));
			write(arr[from]);
			for (int i = from + 1; i < to; i++) {
				buffer[pos++] = (byte) delimiter;
				write(arr[i]);
			}
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter print(final long[] arr, final int from, final int to, final char delimiter) {
			if (from >= to) return this;
			final int len = to - from;
			ensureCapacity(len * (MAX_LONG_DIGITS + 1));
			write(arr[from]);
			for (int i = from + 1; i < to; i++) {
				buffer[pos++] = (byte) delimiter;
				write(arr[i]);
			}
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter print(final double[] arr, final int from, final int to, final char delimiter) {
			if (from >= to) return this;
			print(arr[from]);
			for (int i = from + 1; i < to; i++) {
				String s = Double.toString(arr[i]);
				ensureCapacity(s.length() + 1);
				buffer[pos++] = (byte) delimiter;
				write(s);
			}
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter print(final String[] arr, final int from, final int to, final char delimiter) {
			if (from >= to) return this;
			print(arr[from]);
			for (int i = from + 1; i < to; i++) {
				ensureCapacity(arr[i].length() + 1);
				buffer[pos++] = (byte) delimiter;
				write(arr[i]);
			}
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
				ensureCapacity(1);
				buffer[pos++] = SPACE;
				print(function.apply(arr[i]));
			}
			return this;
		}

		public <T> FastPrinter print(final char[] arr, final IntFunction<T> function) {
			final int len = arr.length;
			if (len > 0) print(function.apply(arr[0]));
			for (int i = 1; i < len; i++) {
				ensureCapacity(1);
				buffer[pos++] = SPACE;
				print(function.apply(arr[i]));
			}
			return this;
		}

		public <T> FastPrinter print(final int[] arr, final IntFunction<T> function) {
			final int len = arr.length;
			if (len > 0) print(function.apply(arr[0]));
			for (int i = 1; i < len; i++) {
				ensureCapacity(1);
				buffer[pos++] = SPACE;
				print(function.apply(arr[i]));
			}
			return this;
		}

		public <T> FastPrinter print(final long[] arr, final LongFunction<T> function) {
			final int len = arr.length;
			if (len > 0) print(function.apply(arr[0]));
			for (int i = 1; i < len; i++) {
				ensureCapacity(1);
				buffer[pos++] = SPACE;
				print(function.apply(arr[i]));
			}
			return this;
		}

		public <T> FastPrinter print(final double[] arr, final DoubleFunction<T> function) {
			final int len = arr.length;
			if (len > 0) print(function.apply(arr[0]));
			for (int i = 1; i < len; i++) {
				ensureCapacity(1);
				buffer[pos++] = SPACE;
				print(function.apply(arr[i]));
			}
			return this;
		}

		public <T> FastPrinter print(final String[] arr, final Function<String, T> function) {
			final int len = arr.length;
			if (len > 0) print(function.apply(arr[0]));
			for (int i = 1; i < len; i++) {
				ensureCapacity(1);
				buffer[pos++] = SPACE;
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
					ensureCapacity(1);
					buffer[pos++] = (byte) delimiter;
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
				buf[p++] = (byte) arr[i++];
				buf[p++] = (byte) arr[i++];
				buf[p++] = (byte) arr[i++];
				buf[p++] = (byte) arr[i++];
				buf[p++] = (byte) arr[i++];
				buf[p++] = (byte) arr[i++];
				buf[p++] = (byte) arr[i++];
				buf[p++] = (byte) arr[i++];
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
				buf[p++] = (byte) function.applyAsInt(arr[i++]);
				buf[p++] = (byte) function.applyAsInt(arr[i++]);
				buf[p++] = (byte) function.applyAsInt(arr[i++]);
				buf[p++] = (byte) function.applyAsInt(arr[i++]);
				buf[p++] = (byte) function.applyAsInt(arr[i++]);
				buf[p++] = (byte) function.applyAsInt(arr[i++]);
				buf[p++] = (byte) function.applyAsInt(arr[i++]);
				buf[p++] = (byte) function.applyAsInt(arr[i++]);
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
				ensureCapacity(1);
				buffer[pos++] = (byte) delimiter;
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
				ensureCapacity(1);
				buffer[pos++] = (byte) delimiter;
				print(function.apply(it.next()));
			}
			return this;
		}

		public FastPrinter printRepeat(final char c, final int cnt) {
			if (cnt <= 0) return this;
			ensureCapacity(cnt);
			final byte[] buf = buffer;
			final byte b = (byte) c;
			int p = pos;
			buf[p++] = b;
			int copied = 1;
			while (copied << 1 <= cnt) {
				System.arraycopy(buf, pos, buf, p, copied);
				p += copied;
				copied <<= 1;
			}
			final int remain = cnt - copied;
			if (remain > 0) {
				System.arraycopy(buf, pos, buf, p, remain);
				p += remain;
			}
			pos = p;
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
			int p = pos, i = 0;
			final int limit8 = len & ~7;
			while (i < limit8) {
				buf[p++] = (byte) s.charAt(i++);
				buf[p++] = (byte) s.charAt(i++);
				buf[p++] = (byte) s.charAt(i++);
				buf[p++] = (byte) s.charAt(i++);
				buf[p++] = (byte) s.charAt(i++);
				buf[p++] = (byte) s.charAt(i++);
				buf[p++] = (byte) s.charAt(i++);
				buf[p++] = (byte) s.charAt(i++);
			}
			while (i < len) buf[p++] = (byte) s.charAt(i++);
			int copied = 1;
			while (copied << 1 <= cnt) {
				System.arraycopy(buf, pos, buf, p, copied * len);
				p += copied * len;
				copied <<= 1;
			}
			final int remain = cnt - copied;
			if (remain > 0) {
				System.arraycopy(buf, pos, buf, p, remain * len);
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
			int p = pos;
			buf[p++] = b;
			buf[p++] = LINE;
			int copied = 2;
			while (copied << 1 <= total) {
				System.arraycopy(buf, pos, buf, p, copied);
				p += copied;
				copied <<= 1;
			}
			final int remain = total - copied;
			if (remain > 0) {
				System.arraycopy(buf, pos, buf, p, remain);
				p += remain;
			}
			pos = p;
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter printlnRepeat(final String s, final int cnt) {
			if (cnt <= 0) return this;
			final int sLen = s.length();
			if (sLen == 0) return this;
			final int unit = sLen + 1;
			final int total = unit * cnt;
			ensureCapacity(total);
			final byte[] buf = buffer;
			int p = pos;
			int i = 0;
			final int limit8 = sLen & ~7;
			while (i < limit8) {
				buf[p++] = (byte) s.charAt(i++);
				buf[p++] = (byte) s.charAt(i++);
				buf[p++] = (byte) s.charAt(i++);
				buf[p++] = (byte) s.charAt(i++);
				buf[p++] = (byte) s.charAt(i++);
				buf[p++] = (byte) s.charAt(i++);
				buf[p++] = (byte) s.charAt(i++);
				buf[p++] = (byte) s.charAt(i++);
			}
			while (i < sLen) buf[p++] = (byte) s.charAt(i++);
			buf[p++] = LINE;
			int copied = 1;
			while (copied << 1 <= cnt) {
				System.arraycopy(buf, pos, buf, p, copied * unit);
				p += copied * unit;
				copied <<= 1;
			}
			final int remain = cnt - copied;
			if (remain > 0) {
				System.arraycopy(buf, pos, buf, p, remain * unit);
				p += remain * unit;
			}
			pos = p;
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter printlnReverse(final boolean[] arr) {
			final int len = arr.length;
			final byte[] buf = buffer;
			for (int i = len - 1; i >= 0; i--) {
				write(arr[i]);
				ensureCapacity(1);
				buf[pos++] = LINE;
			}
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter printlnReverse(final char[] arr) {
			final int len = arr.length;
			ensureCapacity(len << 1);
			final byte[] buf = buffer;
			int p = pos;
			for (int i = len - 1; i >= 0; i--) {
				buf[p++] = (byte) arr[i];
				buf[p++] = LINE;
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
			for (int i = len - 1; i >= 0; i--) {
				write(arr[i]);
				buf[pos++] = LINE;
			}
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter printlnReverse(final long[] arr) {
			final int len = arr.length;
			if (len == 0) return this;
			ensureCapacity(len * (MAX_LONG_DIGITS + 1));
			final byte[] buf = buffer;
			for (int i = len - 1; i >= 0; i--) {
				write(arr[i]);
				buf[pos++] = LINE;
			}
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter printlnReverse(final double[] arr) {
			final int len = arr.length;
			final byte[] buf = buffer;
			for (int i = len - 1; i >= 0; i--) {
				String s = Double.toString(arr[i]);
				ensureCapacity(s.length() + 1);
				write(s);
				buf[pos++] = LINE;
			}
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter printlnReverse(final String[] arr) {
			final int len = arr.length;
			final byte[] buf = buffer;
			for (int i = len - 1; i >= 0; i--) {
				String s = arr[i];
				ensureCapacity(s.length() + 1);
				write(s);
				buf[pos++] = LINE;
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
			final byte[] buf = buffer;
			write(arr[len - 1]);
			for (int i = len - 2; i >= 0; i--) {
				ensureCapacity(1);
				buf[pos++] = SPACE;
				write(arr[i]);
			}
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
				buf[p++] = SPACE;
				buf[p++] = (byte) arr[i];
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
			write(arr[len - 1]);
			for (int i = len - 2; i >= 0; i--) {
				buf[pos++] = SPACE;
				write(arr[i]);
			}
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter printReverse(final long[] arr) {
			final int len = arr.length;
			if (len == 0) return this;
			ensureCapacity(len * (MAX_LONG_DIGITS + 1) - 1);
			final byte[] buf = buffer;
			write(arr[len - 1]);
			for (int i = len - 2; i >= 0; i--) {
				buf[pos++] = SPACE;
				write(arr[i]);
			}
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter printReverse(final double[] arr) {
			final int len = arr.length;
			if (len == 0) return this;
			final byte[] buf = buffer;
			print(arr[len - 1]);
			for (int i = len - 2; i >= 0; i--) {
				String s = Double.toString(arr[i]);
				ensureCapacity(s.length() + 1);
				buf[pos++] = SPACE;
				write(s);
			}
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter printReverse(final String[] arr) {
			final int len = arr.length;
			if (len == 0) return this;
			final byte[] buf = buffer;
			ensureCapacity(arr[len - 1].length());
			write(arr[len - 1]);
			for (int i = len - 2; i >= 0; i--) {
				ensureCapacity(arr[i].length() + 1);
				buf[pos++] = SPACE;
				write(arr[i]);
			}
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter printReverse(final Object[] arr) {
			final int len = arr.length;
			if (len == 0) return this;
			final byte[] buf = buffer;
			print(arr[len - 1]);
			for (int i = len - 2; i >= 0; i--) {
				ensureCapacity(1);
				buf[pos++] = SPACE;
				print(arr[i]);
			}
			if (autoFlush) flush();
			return this;
		}
	}
	// endregion
}
