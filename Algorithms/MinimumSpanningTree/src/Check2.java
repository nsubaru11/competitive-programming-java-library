import java.io.*;
import java.math.*;
import java.util.*;

import static java.util.Arrays.*;

// https://onlinejudge.u-aizu.ac.jp/courses/library/5/GRL/2/GRL_2_A
public final class Check2 {

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
		Prim solver = new Prim(v, e);
		while (e-- > 0) {
			int s = sc.nextInt();
			int t = sc.nextInt();
			int w = sc.nextInt();
			solver.addEdge(s, t, w);
		}
		out.println(solver.solve());
	}

	/**
	 * Prim法を用いた全域木（MST）問題のソルバー。
	 * 最小全域木または最大全域木を求めることができます。
	 * 時間計算量: O(|E|log|V|)
	 * 空間計算量: O(|V| + |E|)
	 */
	@SuppressWarnings("unused")
	private static final class Prim {
		private final int n;
		private final boolean isMinimum;
		private final int[] first, next, dest;
		private final long[] cost;
		private int edgeCnt = 0;

		/**
		 * 指定された頂点数と最小/最大フラグでソルバーを初期化します。
		 *
		 * @param n         頂点数（0からv-1までの頂点番号が使用される）
		 * @param m         辺の数
		 * @param isMinimum trueの場合は最小全域木、falseの場合は最大全域木を求めます
		 */
		public Prim(final int n, final int m, final boolean isMinimum) {
			this.n = n;
			this.isMinimum = isMinimum;
			this.first = new int[n];
			this.next = new int[m * 2];
			this.dest = new int[m * 2];
			this.cost = new long[m * 2];
			Arrays.fill(first, -1);
		}

		/**
		 * 最小全域木を求めるソルバーを初期化します。
		 *
		 * @param n 頂点数（0からv-1までの頂点番号が使用される）
		 * @param m 辺の数
		 */
		public Prim(final int n, final int m) {
			this(n, m, true);
		}

		/**
		 * グラフに無向辺を追加します。
		 *
		 * @param u 辺の始点（0からv-1までの値）
		 * @param v 辺の終点（0からv-1までの値）
		 * @param c 辺の重み
		 */
		public void addEdge(final int u, final int v, final long c) {
			next[edgeCnt] = first[u];
			first[u] = edgeCnt;
			dest[edgeCnt] = v;
			cost[edgeCnt] = isMinimum ? c : -c;
			edgeCnt++;
			next[edgeCnt] = first[v];
			first[v] = edgeCnt;
			dest[edgeCnt] = u;
			cost[edgeCnt] = isMinimum ? c : -c;
			edgeCnt++;
		}

		/**
		 * Prim法を実行し、全域木の総コストを計算します。
		 * 設定に応じて最小全域木または最大全域木を求めます。
		 * グラフが連結でない場合は-1を返します。
		 *
		 * @return 全域木の総コスト、または連結グラフでない場合は-1
		 */
		public long solve() {
			int u = 0;
			long ans = 0;
			int cnt = 0;
			final BitSet visited = new BitSet(n);
			final PriorityQueue<Integer> heap = new PriorityQueue<>((i, j) -> Long.compare(cost[i], cost[j]));
			visited.set(u);
			cnt++;
			for (int e = first[u]; e != -1; e = next[e]) {
				if (!visited.get(dest[e])) heap.add(e);
			}
			while (!heap.isEmpty() && cnt < n) {
				final int e = heap.poll();
				final int v = dest[e];
				if (visited.get(v)) continue;
				visited.set(v);
				cnt++;
				ans += cost[e];
				for (int e2 = first[v]; e2 != -1; e2 = next[e2]) {
					if (!visited.get(dest[e2])) heap.add(e2);
				}
			}
			return cnt == n ? (isMinimum ? ans : -ans) : -1;
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
