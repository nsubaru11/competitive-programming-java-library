import java.io.*;
import java.lang.invoke.*;
import java.math.*;
import java.nio.*;
import java.nio.charset.*;
import java.util.*;

import static java.util.Arrays.*;

// https://onlinejudge.u-aizu.ac.jp/courses/library/5/GRL/2/GRL_2_A
public final class Check1 {

	// region < Constants & Globals >
	private static final FastScanner sc = new FastScanner();
	private static final FastPrinter out = new FastPrinter(64);
	// endregion

	private static void solve() {
		int v = sc.nextInt();
		int e = sc.nextInt();
		Kruskal solver = new Kruskal(v, e);
		while (e-- > 0) {
			int s = sc.nextInt();
			int t = sc.nextInt();
			int w = sc.nextInt();
			solver.addEdge(s, t, w);
		}
		out.println(solver.solve());
	}

	// region < I/O & Debug >
	public static void main(final String[] args) {
		try {
			solve();
		} finally {
			sc.close();
			out.close();
		}
	}

	/**
	 * Kruskal法を用いた全域木（MST）問題のソルバー。
	 * 最小全域木または最大全域木を求めることができます。
	 * 時間計算量: O(|E|log|E|)（Eは辺の数）
	 * 空間計算量: O(|V| + |E|)
	 */
	@SuppressWarnings("unused")
	private static final class Kruskal {
		private final int n;
		private final boolean isMinimum;
		private final int[] from, to, edges, path;
		private final long[] cost;
		private final UnionFind uf;
		private int edgeCnt = 0;
		private long ans = 0;
		private boolean solved = false;

		/**
		 * 指定された頂点数と最小/最大フラグでソルバーを初期化します。
		 *
		 * @param n         頂点数（0からv-1までの頂点番号が使用される）
		 * @param m         辺の数
		 * @param isMinimum trueの場合は最小全域木、falseの場合は最大全域木を求めます
		 */
		public Kruskal(final int n, final int m, final boolean isMinimum) {
			this.n = n;
			this.isMinimum = isMinimum;
			from = new int[m];
			to = new int[m];
			edges = new int[m];
			path = new int[n - 1];
			cost = new long[m];
			uf = new UnionFind(n);
		}

		/**
		 * 最小全域木を求めるソルバーを初期化します。
		 *
		 * @param n 頂点数（0からv-1までの頂点番号が使用される）
		 * @param m 辺の数
		 */
		public Kruskal(final int n, final int m) {
			this(n, m, true);
		}

		/**
		 * グラフに辺を追加します。
		 *
		 * @param u 辺の始点（0からv-1までの値）
		 * @param v 辺の終点（0からv-1までの値）
		 * @param c 辺の重み
		 */
		public void addEdge(final int u, final int v, final long c) {
			from[edgeCnt] = u;
			to[edgeCnt] = v;
			cost[edgeCnt] = isMinimum ? c : -c;
			edges[edgeCnt] = edgeCnt;
			edgeCnt++;
		}

		/**
		 * Kruskal法の辺インデックスを、対応するコストに基づいてソートします。
		 * Dual-Pivot Quicksortのエッセンスを採用したプリミティブ実装です。
		 */
		private static void sortEdges(int[] edges, long[] cost, int left, int right) {
			int size = right - left;

			if (size < 47) {
				for (int i = left + 1; i < right; i++) {
					int pivotEdge = edges[i];
					long pivotCost = cost[pivotEdge];
					int j = i - 1;
					while (j >= left && cost[edges[j]] > pivotCost) {
						edges[j + 1] = edges[j];
						j--;
					}
					edges[j + 1] = pivotEdge;
				}
				return;
			}

			int step = (size >> 3) * 3 + 3;
			int e1 = left + step;
			int e5 = right - 1 - step;
			int e3 = (left + right) >>> 1;
			int e2 = (e1 + e3) >>> 1;
			int e4 = (e3 + e5) >>> 1;

			if (cost[edges[e5]] < cost[edges[e2]]) swap(edges, e2, e5);
			if (cost[edges[e4]] < cost[edges[e1]]) swap(edges, e1, e4);
			if (cost[edges[e5]] < cost[edges[e4]]) swap(edges, e4, e5);
			if (cost[edges[e2]] < cost[edges[e1]]) swap(edges, e1, e2);
			if (cost[edges[e4]] < cost[edges[e2]]) swap(edges, e2, e4);

			int p1 = edges[e2], p2 = edges[e4];
			long v1 = cost[p1], v2 = cost[p2];

			int less = left, greater = right - 2;

			edges[e2] = edges[left];
			edges[e4] = edges[right - 1];
			edges[left] = p1;
			edges[right - 1] = p2;

			for (int k = left + 1; k <= greater; k++) {
				if (cost[edges[k]] < v1) {
					swap(edges, k, ++less);
				} else if (cost[edges[k]] > v2) {
					while (k < greater && cost[edges[greater]] > v2) greater--;
					swap(edges, k, greater--);
					if (cost[edges[k]] >= v1) continue;
					swap(edges, k, ++less);
				}
			}

			edges[left] = edges[less];
			edges[less] = p1;
			edges[right - 1] = edges[greater + 1];
			edges[greater + 1] = p2;

			sortEdges(edges, cost, left, less);
			if (v1 < v2) sortEdges(edges, cost, less + 1, greater + 1);
			sortEdges(edges, cost, greater + 2, right);
		}

		private static void swap(int[] a, int i, int j) {
			int t = a[i];
			a[i] = a[j];
			a[j] = t;
		}

		/**
		 * Kruskal法を実行し、全域木の総コストを計算します。
		 * 設定に応じて最小全域木または最大全域木を求めます。
		 * グラフが連結でない場合は-1を返します。
		 *
		 * @return 全域木の総コスト、または連結グラフでない場合は-1
		 */
		public long solve() {
			if (solved) return ans;
			int size = 0;
			sortEdges(edges, cost, 0, edgeCnt);
			for (int i = 0; size < n - 1 && i < edgeCnt; i++) {
				int e = edges[i], u = from[e], v = to[e];
				long c = cost[e];
				if (uf.union(u, v)) {
					path[size++] = e;
					ans += c;
				}
			}
			solved = true;
			return ans = size < n - 1 ? -1 : isMinimum ? ans : -ans;
		}

		/**
		 * 全域木を構成する辺のインデックスを取得します。
		 * インデックスは addEdge で追加された順序（0-indexed）に対応します。
		 *
		 * @return 全域木を構成する辺のインデックス配列
		 */
		public int[] solvePath() {
			if (!solved) solve();
			return path;
		}

		private static final class UnionFind {
			private final int[] root, rank;

			public UnionFind(final int size) {
				root = new int[size];
				rank = new int[size];
				setAll(root, i -> i);
			}

			public int find(final int x) {
				return x == root[x] ? x : (root[x] = find(root[x]));
			}

			public boolean union(int x, int y) {
				x = find(x);
				y = find(y);
				if (x == y) return false;
				if (rank[x] < rank[y]) {
					root[x] = y;
				} else {
					root[y] = x;
					if (rank[x] == rank[y]) rank[x]++;
				}
				return true;
			}
		}
	}

	@SuppressWarnings("unused")
	private static final class FastScanner implements AutoCloseable {
		private static final int DEFAULT_BUFFER_SIZE = 1 << 20;
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
			final byte[] buf = buffer;
			int p = pos, len = bufferLength, b;
			do {
				if (p >= len) {
					try {
						len = in.read(buf);
						p = 0;
					} catch (final IOException e) {
						throw new RuntimeException(e);
					}
					if (len <= 0) throw new NoSuchElementException();
					if (len < buf.length) buf[len] = 32;
				}
				b = buf[p++];
			} while (b <= 32);
			pos = p;
			bufferLength = len;
			return b;
		}

		@Override
		public void close() {
			try {
				in.close();
			} catch (final IOException e) {
				throw new RuntimeException(e);
			}
		}

		private boolean hasNextByte() {
			if (pos < bufferLength) return true;
			pos = 0;
			try {
				bufferLength = in.read(buffer);
				if (bufferLength > 0 && bufferLength < buffer.length) buffer[bufferLength] = 32;
			} catch (final IOException e) {
				throw new RuntimeException(e);
			}
			return bufferLength > 0;
		}

		public boolean hasNext() {
			while (hasNextByte()) {
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
			boolean negative = false;
			if (b == '-') {
				negative = true;
				if (pos == bufferLength && !hasNextByte()) throw new NoSuchElementException();
				b = buffer[pos++];
			}
			return pos + 10 <= bufferLength ? nextIntFast(b, negative) : nextIntSlow(b, negative);
		}

		private int nextIntFast(int b, final boolean negative) {
			final byte[] buf = buffer;
			int p = pos, n = 0;
			long a = (long) Handles.LONG_HANDLE.get(buf, p - 1) ^ 0x3030303030303030L;
			long check = a & 0xF0F0F0F0F0F0F0F0L;
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

		private int nextIntSlow(int b, final boolean negative) {
			int p = pos, len = bufferLength;
			int n = 0;
			do {
				n = (n << 3) + (n << 1) + (b & 15);
				if (p == len) {
					pos = p;
					if (!hasNextByte()) {
						p = pos;
						break;
					}
					p = pos;
					len = bufferLength;
				}
				b = buffer[p++];
			} while (b > 32);
			pos = p;
			return negative ? -n : n;
		}

		public long nextLong() {
			int b = skipSpaces();
			boolean negative = false;
			if (b == '-') {
				negative = true;
				if (pos == bufferLength && !hasNextByte()) throw new NoSuchElementException();
				b = buffer[pos++];
			}
			return pos + 20 <= bufferLength ? nextLongFast(b, negative) : nextLongSlow(b, negative);
		}

		private long nextLongFast(int b, final boolean negative) {
			final byte[] buf = buffer;
			int p = pos;
			long n = 0;
			long a = (long) Handles.LONG_HANDLE.get(buf, p - 1) ^ 0x3030303030303030L;
			long check = a & 0xF0F0F0F0F0F0F0F0L;
			if (check == 0) {
				a = (a * 10 + (a >>> 8)) & 0x00FF00FF00FF00FFL;
				a = (a * 100 + (a >>> 16)) & 0x0000FFFF0000FFFFL;
				a = (a * 10000 + (a >>> 32)) & 0x00000000FFFFFFFFL;
				n = a;
				p += 7;
				b = buf[p++];
				long a2 = (long) Handles.LONG_HANDLE.get(buf, p - 1) ^ 0x3030303030303030L;
				long check2 = a2 & 0xF0F0F0F0F0F0F0F0L;
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

		private long nextLongSlow(int b, final boolean negative) {
			int p = pos, len = bufferLength;
			long n = 0;
			do {
				n = (n << 3) + (n << 1) + (b & 15);
				if (p == len) {
					pos = p;
					if (!hasNextByte()) {
						p = pos;
						break;
					}
					p = pos;
					len = bufferLength;
				}
				b = buffer[p++];
			} while (b > 32);
			pos = p;
			return negative ? -n : n;
		}

		public double nextDouble() {
			int b = skipSpaces();
			boolean negative = false;
			if (b == '-') {
				negative = true;
				if (pos == bufferLength && !hasNextByte()) throw new NoSuchElementException();
				b = buffer[pos++];
			}
			return pos + 20 <= bufferLength ? nextDoubleFast(b, negative) : nextDoubleSlow(b, negative);
		}

		private double nextDoubleFast(int b, final boolean negative) {
			final byte[] buf = buffer;
			int p = pos, len = bufferLength;
			long intPart = 0;
			do {
				intPart = (intPart << 3) + (intPart << 1) + (b & 15);
				b = buf[p++];
			} while ('0' <= b && b <= '9');
			double result = intPart;
			if (b == '.') result += parseFracPart(p, len, buf);
			else pos = p;
			return negative ? -result : result;
		}

		private double nextDoubleSlow(int b, final boolean negative) {
			final byte[] buf = buffer;
			int p = pos, len = bufferLength;
			long intPart = 0;
			do {
				intPart = (intPart << 3) + (intPart << 1) + (b & 15);
				if (p == len) {
					pos = p;
					if (!hasNextByte()) {
						p = pos;
						b = -1;
						break;
					}
					p = pos;
					len = bufferLength;
				}
				b = buf[p++];
			} while ('0' <= b && b <= '9');

			double result = intPart;
			if (b == '.') result += parseFracPart(p, len, buf);
			else pos = p;
			return negative ? -result : result;
		}

		private double parseFracPart(int p, int len, final byte[] buf) {
			if (p == len) {
				pos = p;
				hasNextByte();
				p = pos;
				len = bufferLength;
			}
			int b = buf[p++];
			long fracPart = 0, divisor = 1;
			if (p + 20 <= len) {
				do {
					fracPart = fracPart * 10 + (b & 15);
					divisor *= 10;
					b = buf[p++];
				} while ('0' <= b && b <= '9');
			} else {
				do {
					fracPart = fracPart * 10 + (b & 15);
					divisor *= 10;
					if (p == len) {
						pos = p;
						if (!hasNextByte()) {
							p = pos;
							break;
						}
						p = pos;
						len = bufferLength;
					}
					b = buf[p++];
				} while ('0' <= b && b <= '9');
			}
			pos = p;
			return (double) fracPart / divisor;
		}

		public String next() {
			skipSpaces();
			final byte[] buf = buffer;
			int p = pos, len = bufferLength;
			final int start = p - 1;
			while (p < len && buf[p] > 32) p++;
			if (p < len) {
				final String s = new String(buf, start, p - start, StandardCharsets.US_ASCII);
				pos = p + 1;
				return s;
			}
			final StringBuilder sb = new StringBuilder(len - start + 16);
			for (int i = start; i < len; i++) sb.append((char) buf[i]);
			while (true) {
				if (p == len) {
					pos = p;
					if (!hasNextByte()) {
						p = pos;
						break;
					}
					p = pos;
					len = bufferLength;
				}
				final int b = buf[p++];
				if (b <= 32) break;
				sb.append((char) b);
			}
			pos = p;
			return sb.toString();
		}

		public StringBuilder nextStringBuilder() {
			final StringBuilder sb = new StringBuilder();
			int b = skipSpaces(), p = pos, len = bufferLength;
			do {
				sb.append((char) b);
				if (p == len) {
					pos = p;
					if (!hasNextByte()) {
						p = pos;
						break;
					}
					p = pos;
					len = bufferLength;
				}
				b = buffer[p++];
			} while (b > 32);
			pos = p;
			return sb;
		}

		public String nextLine() {
			if (pos == bufferLength && !hasNextByte()) throw new NoSuchElementException();
			final byte[] buf = buffer;
			int p = pos, len = bufferLength;
			final int start = p;
			while (p < len) {
				final int b = buf[p];
				if (b == '\n' || b == '\r') {
					final String s = new String(buf, start, p - start, StandardCharsets.US_ASCII);
					p++;
					if (b == '\r') {
						if (p == len) {
							pos = p;
							hasNextByte();
							p = pos;
							len = bufferLength;
						}
						if (p < len && buf[p] == '\n') p++;
					}
					pos = p;
					return s;
				}
				p++;
			}

			final StringBuilder sb = new StringBuilder();
			for (int i = start; i < len; i++) sb.append((char) buf[i]);
			while (true) {
				pos = len;
				if (!hasNextByte()) {
					pos = len;
					return sb.toString();
				}
				p = pos;
				len = bufferLength;
				while (p < len) {
					final int b = buf[p];
					if (b == '\n' || b == '\r') {
						p++;
						if (b == '\r') {
							if (p == len) {
								pos = p;
								hasNextByte();
								p = pos;
								len = bufferLength;
							}
							if (p < len && buf[p] == '\n') p++;
						}
						pos = p;
						return sb.toString();
					}
					sb.append((char) b);
					p++;
				}
			}
		}

		public BigInteger nextBigInteger() {
			return new BigInteger(next());
		}

		public BigDecimal nextBigDecimal() {
			return new BigDecimal(next());
		}

		private static final class Handles {
			private static final VarHandle LONG_HANDLE = MethodHandles.byteArrayViewVarHandle(long[].class, ByteOrder.LITTLE_ENDIAN);
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
			return println(o.toString());
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
			return print(o.toString());
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
