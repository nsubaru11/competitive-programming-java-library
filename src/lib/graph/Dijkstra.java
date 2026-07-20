package lib.graph;

import static java.util.Arrays.*;

import lib.ds.priorityqueue.*;

/**
 * Dijkstra法により、非負重み付きグラフの単一始点最短経路を求めるユーティリティクラス。
 * <p>
 * 計算量は {@code O((n + m) log n)}、追加メモリは {@code O(n)} です。
 * 辺の重みはすべて非負であることを前提とします。
 */
public final class Dijkstra {
	private static final long INF = Long.MAX_VALUE;

	private Dijkstra() {
	}

	/**
	 * 始点 {@code s} から全頂点への最短距離と経路復元情報を計算します。
	 *
	 * @param graph 探索対象のグラフ
	 * @param s 始点（0-indexed）
	 * @return 計算結果
	 */
	public static Result solve(final Graph graph, final int s) {
		LongIndexedPriorityQueue ans = new LongIndexedPriorityQueue(graph.n);
		int[] parent = new int[graph.n];
		fill(parent, -1);
		int[] next = graph.next, first = graph.first, dest = graph.dest;
		long[] cost = graph.cost;
		ans.add(s, 0);
		parent[s] = s;
		while (!ans.isEmpty()) {
			final long cu = ans.peek();
			final int u = ans.pollIndex();
			for (int e = first[u]; e != -1; e = next[e]) {
				final int v = dest[e];
				final long cv = cost[e];
				if (ans.relax(v, cu + cv)) parent[v] = u;
			}
		}
		long[] dist = new long[graph.n];
		setAll(dist, i -> ans.getLastOrDefault(i, INF));
		return new Result(s, dist, parent);
	}

	/**
	 * 始点 {@code s} から全頂点への最短距離を返します。
	 * 到達不能な頂点の値は {@link Long#MAX_VALUE} です。
	 *
	 * @param graph 探索対象のグラフ
	 * @param s     始点（0-indexed）
	 * @return 最短距離の配列
	 */
	public static long[] dist(final Graph graph, final int s) {
		Result result = solve(graph, s);
		return result.dist;
	}

	/**
	 * 始点 s から終点 g への最短経路のコストをダイクストラ法を用いて計算します。
	 *
	 * @param graph 探索対象のグラフ
	 * @param s 始点（0-indexed）
	 * @param g 終点（0-indexed）
	 * @return 始点から終点への最短距離。到達不能な場合は {@link Long#MAX_VALUE}
	 */
	public static long dist(final Graph graph, final int s, final int g) {
		LongIndexedPriorityQueue ans = new LongIndexedPriorityQueue(graph.n);
		int[] next = graph.next, first = graph.first, dest = graph.dest;
		long[] cost = graph.cost;
		ans.add(s, 0);
		while (!ans.isEmpty()) {
			final long cu = ans.peek();
			final int u = ans.pollIndex();
			if (u == g) break;
			for (int e = first[u]; e != -1; e = next[e]) {
				final int v = dest[e];
				final long cv = cost[e];
				ans.relax(v, cu + cv);
			}
		}
		return ans.getLastOrDefault(g, INF);
	}

	/**
	 * 始点 s から終点 g への最短経路をダイクストラ法を用いて計算します。
	 *
	 * @param graph 探索対象のグラフ
	 * @param s 始点（0-indexed）
	 * @param g 終点（0-indexed）
	 * @return 始点から終点への最短経路（経路が存在しない場合は null）
	 */
	public static int[] path(final Graph graph, final int s, final int g) {
		Result result = solve(graph, s);
		return result.pathTo(g);
	}

	/**
	 * Dijkstra法による単一始点最短経路の計算結果。
	 */
	public static final class Result {
		public final int s;
		public final long[] dist;
		public final int[] parent;

		private Result(final int s, final long[] dist, final int[] parent) {
			this.s = s;
			this.dist = dist;
			this.parent = parent;
		}

		/**
		 * 指定した頂点への最短距離を返します。
		 *
		 * @param v 終点
		 * @return 最短距離。到達不能な場合は {@link Long#MAX_VALUE}
		 */
		public long distTo(final int v) {
			return dist[v];
		}

		/**
		 * 指定した頂点へ到達可能かを返します。
		 *
		 * @param v 終点
		 * @return 到達可能なら {@code true}
		 */
		public boolean reachable(final int v) {
			return dist[v] != INF;
		}

		/**
		 * 最短経路木上の親を返します。
		 *
		 * @param v 頂点
		 * @return 親。始点自身は始点、到達不能な頂点は {@code -1}
		 */
		public int parent(final int v) {
			return parent[v];
		}

		/**
		 * 始点から指定した頂点までの最短経路を頂点列として返します。
		 *
		 * @param v 終点
		 * @return 始点と終点を含む頂点列。到達不能な場合は {@code null}
		 */
		public int[] pathTo(final int v) {
			if (distTo(v) == INF) return null;
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
