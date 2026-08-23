package lib.graph;

import static java.util.Arrays.*;

/**
 * トポロジカル順序に基づき、DAG（有向非巡回グラフ）の単一始点最短経路・最長経路を求めるユーティリティクラス。
 * <p>
 * 負辺を含んでいても正しく動作します。
 * 計算量は {@code O(n + m)}、追加メモリは {@code O(n)} です。
 */
public final class TopologicalShortestPath {
	private static final long INF = Long.MAX_VALUE;

	private TopologicalShortestPath() {
	}

	/**
	 * 始点 {@code s} から全頂点への最短距離と経路復元情報を計算します。
	 *
	 * @param graph 探索対象の有向グラフ
	 * @param s     始点（0-indexed）
	 * @return 計算結果
	 * @throws IllegalArgumentException graphに閉路が存在する場合
	 */
	public static PathResult solve(final DirectedGraph graph, final int s) {
		return solveInternal(graph, new int[]{s}, false);
	}

	/**
	 * 始点 {@code s} から全頂点への最短距離と経路復元情報を計算します。
	 *
	 * @param graph 探索対象の有向グラフ
	 * @param s     始点（0-indexed）
	 * @return 計算結果
	 * @throws IllegalArgumentException graphに閉路が存在する場合
	 */
	public static PathResult solve(final DirectedGraph graph, final int... s) {
		return solveInternal(graph, s, false);
	}

	/**
	 * 始点 {@code s} から全頂点への最長距離と経路復元情報を計算します。
	 *
	 * @param graph 探索対象の有向グラフ
	 * @param s     始点（0-indexed）
	 * @return 計算結果
	 * @throws IllegalArgumentException graphに閉路が存在する場合
	 */
	public static PathResult solveLongest(final DirectedGraph graph, final int s) {
		return solveInternal(graph, new int[]{s}, true);
	}

	/**
	 * 始点 {@code s} から全頂点への最長距離と経路復元情報を計算します。
	 *
	 * @param graph 探索対象の有向グラフ
	 * @param s     始点（0-indexed）
	 * @return 計算結果
	 * @throws IllegalArgumentException graphに閉路が存在する場合
	 */
	public static PathResult solveLongest(final DirectedGraph graph, final int... s) {
		return solveInternal(graph, s, true);
	}

	private static PathResult solveInternal(final DirectedGraph graph, final int[] s, final boolean longest) {
		final int[] order = GraphUtils.topologicalSort(graph);
		if (order == null) throw new IllegalArgumentException("graph has a cycle");

		final int n = graph.n;
		final int[] dest = graph.dest, next = graph.next, first = graph.first;
		final long[] cost = graph.cost;
		final int[] parent = new int[n];
		fill(parent, -1);
		final long[] dist = new long[n];
		fill(dist, INF);
		for (final int si : s) {
			parent[si] = si;
			dist[si] = 0;
		}

		for (final int u : order) {
			final long du = dist[u];
			if (du == INF) continue;
			for (int e = first[u]; e != -1; e = next[e]) {
				final int v = dest[e];
				final long dv = du + (longest ? -cost[e] : cost[e]);
				if (dv < dist[v]) {
					dist[v] = dv;
					parent[v] = u;
				}
			}
		}

		if (longest) {
			for (int v = 0; v < n; v++) {
				if (dist[v] != INF) dist[v] = -dist[v];
			}
		}
		return new PathResult(s, dist, parent);
	}

	/**
	 * 始点 {@code s} から全頂点への最短距離を返します。
	 * 到達不能な頂点の値は {@link Long#MAX_VALUE} です。
	 *
	 * @param graph 探索対象の有向グラフ
	 * @param s     始点（0-indexed）
	 * @return 最短距離の配列
	 * @throws IllegalArgumentException graphに閉路が存在する場合
	 */
	public static long[] dist(final DirectedGraph graph, final int s) {
		final PathResult result = solve(graph, s);
		return result.dist;
	}

	/**
	 * 始点 {@code s} から全頂点への最短距離を返します。
	 * 到達不能な頂点の値は {@link Long#MAX_VALUE} です。
	 *
	 * @param graph 探索対象の有向グラフ
	 * @param s     始点（0-indexed）
	 * @return 最短距離の配列
	 * @throws IllegalArgumentException graphに閉路が存在する場合
	 */
	public static long[] dist(final DirectedGraph graph, final int... s) {
		final PathResult result = solve(graph, s);
		return result.dist;
	}

	/**
	 * 始点 {@code s} から全頂点への最長距離を返します。
	 * 到達不能な頂点の値は {@link Long#MAX_VALUE} です。
	 *
	 * @param graph 探索対象の有向グラフ
	 * @param s     始点（0-indexed）
	 * @return 最長距離の配列
	 * @throws IllegalArgumentException graphに閉路が存在する場合
	 */
	public static long[] distLongest(final DirectedGraph graph, final int s) {
		final PathResult result = solveLongest(graph, s);
		return result.dist;
	}

	/**
	 * 始点 {@code s} から全頂点への最長距離を返します。
	 * 到達不能な頂点の値は {@link Long#MAX_VALUE} です。
	 *
	 * @param graph 探索対象の有向グラフ
	 * @param s     始点（0-indexed）
	 * @return 最長距離の配列
	 * @throws IllegalArgumentException graphに閉路が存在する場合
	 */
	public static long[] distLongest(final DirectedGraph graph, final int... s) {
		final PathResult result = solveLongest(graph, s);
		return result.dist;
	}

	/**
	 * 始点 s から終点 g への最短距離をトポロジカル順序を用いて計算します。
	 * 到達不能な頂点の値は {@link Long#MAX_VALUE} です。
	 *
	 * @param graph 探索対象の有向グラフ
	 * @param s     始点（0-indexed）
	 * @param g     終点（0-indexed）
	 * @return 始点から終点への最短距離
	 * @throws IllegalArgumentException graphに閉路が存在する場合
	 */
	public static long dist(final DirectedGraph graph, final int s, final int g) {
		final PathResult result = solve(graph, s);
		return result.dist[g];
	}

	/**
	 * 始点 s から終点 g への最長距離をトポロジカル順序を用いて計算します。
	 * 到達不能な頂点の値は {@link Long#MAX_VALUE} です。
	 *
	 * @param graph 探索対象の有向グラフ
	 * @param s     始点（0-indexed）
	 * @param g     終点（0-indexed）
	 * @return 始点から終点への最長距離
	 * @throws IllegalArgumentException graphに閉路が存在する場合
	 */
	public static long distLongest(final DirectedGraph graph, final int s, final int g) {
		final PathResult result = solveLongest(graph, s);
		return result.dist[g];
	}

	/**
	 * 始点 s から終点 g への最短経路をトポロジカル順序を用いて計算します。
	 *
	 * @param graph 探索対象の有向グラフ
	 * @param s     始点（0-indexed）
	 * @param g     終点（0-indexed）
	 * @return 始点から終点への最短経路
	 * @throws IllegalArgumentException graphに閉路が存在する場合
	 */
	public static int[] path(final DirectedGraph graph, final int s, final int g) {
		final PathResult result = solve(graph, s);
		return result.pathTo(g);
	}

	/**
	 * 始点 s から終点 g への最長経路をトポロジカル順序を用いて計算します。
	 *
	 * @param graph 探索対象の有向グラフ
	 * @param s     始点（0-indexed）
	 * @param g     終点（0-indexed）
	 * @return 始点から終点への最長経路
	 * @throws IllegalArgumentException graphに閉路が存在する場合
	 */
	public static int[] pathLongest(final DirectedGraph graph, final int s, final int g) {
		final PathResult result = solveLongest(graph, s);
		return result.pathTo(g);
	}
}
