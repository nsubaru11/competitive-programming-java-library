package lib.graph;

import static java.util.Arrays.*;

/**
 * Bellman-Ford法により、負辺を含むグラフの単一始点最短経路を求めるユーティリティクラス。
 * <p>
 * 到達不能な頂点の距離は {@link Long#MAX_VALUE}、到達可能な負閉路の影響を受ける頂点は
 * {@link Long#MIN_VALUE} です。
 * 計算量は {@code O(nm)}、追加メモリは {@code O(n)} です。
 */
public final class BellmanFord {
	private static final long INF = Long.MAX_VALUE;
	private static final long NINF = Long.MIN_VALUE;

	private BellmanFord() {
	}

	/**
	 * 始点 {@code s} から全頂点への最短距離を計算します。
	 *
	 * @param graph 探索対象のグラフ
	 * @param s 始点（0-indexed）
	 * @return 計算結果
	 */
	public static Result solve(final Graph graph, final int s) {
		int n = graph.n;
		int[] first = graph.first, next = graph.next, dest = graph.dest;
		long[] dist = new long[n], cost = graph.cost;
		int[] parent = new int[n];
		fill(dist, INF);
		fill(parent, -1);
		dist[s] = 0;
		parent[s] = s;

		for (int k = 1; k < n; k++) {
			boolean updated = false;
			for (int u = 0; u < n; u++) {
				long du = dist[u];
				if (du == INF) continue;
				for (int e = first[u]; e != -1; e = next[e]) {
					int v = dest[e];
					long c = cost[e];
					if (dist[v] > du + c) {
						dist[v] = du + c;
						parent[v] = u;
						updated = true;
					}
				}
			}
			if (!updated) break;
		}

		boolean[] affected = new boolean[n];
		int[] q = new int[n];
		int tail = 0;
		for (int u = 0; u < n; u++) {
			long du = dist[u];
			if (du == INF) continue;
			for (int e = first[u]; e != -1; e = next[e]) {
				int v = dest[e];
				if (dist[v] > du + cost[e] && !affected[v]) {
					affected[v] = true;
					q[tail++] = v;
				}
			}
		}
		for (int head = 0; head < tail; head++) {
			int u = q[head];
			for (int e = first[u]; e != -1; e = next[e]) {
				int v = dest[e];
				if (affected[v]) continue;
				affected[v] = true;
				q[tail++] = v;
			}
		}
		for (int i = 0; i < tail; i++) {
			int v = q[i];
			dist[v] = NINF;
			parent[v] = -1;
		}
		return new Result(s, tail > 0, dist, parent);
	}

	/**
	 * Bellman-Ford法による単一始点最短経路の計算結果。
	 */
	public static final class Result {
		public final boolean hasNegCycle;
		public final int s;
		public final long[] dist;
		public final int[] parent;

		private Result(final int s, boolean hasNegCycle, final long[] dist, final int[] parent) {
			this.s = s;
			this.hasNegCycle = hasNegCycle;
			this.dist = dist;
			this.parent = parent;
		}

		/**
		 * 指定した頂点への距離を返します。
		 *
		 * @param v 終点
		 * @return 距離。到達不能は {@link Long#MAX_VALUE}、負閉路の影響下は {@link Long#MIN_VALUE}
		 */
		public long distTo(final int v) {
			return dist[v];
		}

		/**
		 * 指定した頂点へ到達可能かを返します。
		 * 負閉路の影響下でも到達可能なら {@code true} です。
		 *
		 * @param v 終点
		 * @return 到達可能なら {@code true}
		 */
		public boolean reachable(final int v) {
			return dist[v] != INF;
		}

		/**
		 * 最短経路上の親を返します。
		 *
		 * @param v 頂点
		 * @return 親。始点自身は始点、到達不能または負閉路の影響下では {@code -1}
		 */
		public int parent(final int v) {
			return parent[v];
		}

		/**
		 * 始点から指定した頂点までの最短経路を頂点列として返します。
		 *
		 * @param v 終点
		 * @return 始点と終点を含む頂点列。到達不能または負閉路の影響下では {@code null}
		 */
		public int[] pathTo(final int v) {
			long d = distTo(v);
			if (d == INF || d == NINF) return null;
			final int[] temp = new int[dist.length];
			int len = 0;
			for (int p = v; p != s; p = parent[p]) temp[len++] = p;
			temp[len++] = s;
			int[] res = new int[len];
			for (int i = 0; i < len; i++) res[i] = temp[len - i - 1];
			return res;
		}
	}
}
