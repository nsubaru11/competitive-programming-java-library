import java.io.*;
import java.math.*;
import java.util.*;

import static java.util.Arrays.*;

// https://onlinejudge.u-aizu.ac.jp/courses/library/5/GRL/2/GRL_2_A
public final class Check1 {

	// region < Constants & Globals >
	private static final boolean DEBUG;
	private static final int MOD;
	private static final int[] di;
	private static final int[] dj;
	private static final FastScanner sc;
	private static final PrintWriter out;

	static {
		DEBUG = true;
		MOD = 998244353;
		// MOD = 1_000_000_007;
		di = new int[]{0, -1, 0, 1, -1, -1, 1, 1};
		dj = new int[]{-1, 0, 1, 0, -1, 1, 1, -1};
		sc = new FastScanner(System.in);
		out = new PrintWriter(System.out, false);
	}
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
		private final int[] from, to;
		private final long[] cost;
		private final Integer[] edges;
		private final UnionFind uf;
		private int edgeCnt = 0;

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
			cost = new long[m];
			uf = new UnionFind(n);
			edges = new Integer[m];
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
		 * Kruskal法を実行し、全域木の総コストを計算します。
		 * 設定に応じて最小全域木または最大全域木を求めます。
		 * グラフが連結でない場合は-1を返します。
		 *
		 * @return 全域木の総コスト、または連結グラフでない場合は-1
		 */
		public long solve() {
			long ans = 0;
			int size = n;
			Arrays.sort(edges, 0, edgeCnt, (i, j) -> Long.compare(cost[i], cost[j]));
			for (int i = 0; i < edgeCnt; i++) {
				int e = edges[i];
				int u = from[e];
				int v = to[e];
				long c = cost[e];
				if (uf.find(u) != uf.find(v)) {
					uf.union(u, v);
					ans += c;
					size--;
				}
			}
			return size > 1 ? -1 : isMinimum ? ans : -ans;
		}

		private static final class UnionFind {
			private final int[] root, rank;

			public UnionFind(final int size) {
				root = new int[size];
				rank = new int[size];
				Arrays.setAll(root, i -> i);
			}

			public int find(final int x) {
				int root = x;
				while (root != this.root[root]) {
					root = this.root[root];
				}
				int cur = x;
				while (cur != root) {
					int next = this.root[cur];
					this.root[cur] = root;
					cur = next;
				}
				return root;
			}

			public void union(int x, int y) {
				x = find(x);
				y = find(y);
				if (x != y) {
					if (rank[x] > rank[y]) {
						root[y] = x;
					} else if (rank[x] < rank[y]) {
						root[x] = y;
					} else {
						root[y] = x;
						rank[x] += 1;
					}
				}
			}
		}
	}

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
	}
	// endregion
}
