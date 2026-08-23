package lib.graph;

import static java.util.Arrays.*;

import lib.ds.arrays.*;

/**
 * 0-1 BFS。辺重みが0または1のグラフの単一始点・複数始点最短路を O(V + E) で求める。
 */
public final class ZeroOneBFS {
	private static final long INF = Long.MAX_VALUE;

	private ZeroOneBFS() {
	}

	/**
	 * 始点 {@code s} から全頂点への最短距離と経路復元情報を計算します。
	 *
	 * @param graph 探索対象のグラフ
	 * @param s     始点（0-indexed）
	 * @return 計算結果
	 */
	public static PathResult solve(final Graph graph, final int s) {
		return solveInternal(graph, new int[]{s});
	}

	/**
	 * 始点 {@code s} から全頂点への最短距離と経路復元情報を計算します。
	 *
	 * @param graph 探索対象のグラフ
	 * @param s     始点（0-indexed）
	 * @return 計算結果
	 */
	public static PathResult solve(final Graph graph, final int... s) {
		return solveInternal(graph, s);
	}

	private static PathResult solveInternal(final Graph graph, final int[] s) {
		final int n = graph.n;
		final int[] dest = graph.dest, next = graph.next, first = graph.first;
		final long[] cost = graph.cost;
		final int[] parent = new int[n];
		fill(parent, -1);
		final long[] dist = new long[n];
		fill(dist, INF);
		final IntArrayDeque dq = new IntArrayDeque(n);
		for (final int si : s) {
			parent[si] = si;
			dist[si] = 0;
			dq.addLast(si);
		}
		final boolean[] visited = new boolean[n];

		while (!dq.isEmpty()) {
			final int u = dq.pollFirst();
			if (visited[u]) continue;
			visited[u] = true;
			final long du = dist[u];
			for (int e = first[u]; e != -1; e = next[e]) {
				final int v = dest[e];
				final long c = cost[e];
				if (dist[v] > du + c) {
					dist[v] = du + c;
					parent[v] = u;
					if (c == 0) dq.addFirst(v);
					else dq.addLast(v);
				}
			}
		}
		return new PathResult(s, dist, parent);
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
		final PathResult result = solve(graph, s);
		return result.dist;
	}

	/**
	 * 始点 {@code s} から全頂点への最短距離を返します。
	 * 到達不能な頂点の値は {@link Long#MAX_VALUE} です。
	 *
	 * @param graph 探索対象のグラフ
	 * @param s     始点（0-indexed）
	 * @return 最短距離の配列
	 */
	public static long[] dist(final Graph graph, final int... s) {
		final PathResult result = solve(graph, s);
		return result.dist;
	}

	/**
	 * 始点 s から終点 g への最短経路のコストを01BFSを用いて計算します。
	 *
	 * @param graph 探索対象のグラフ
	 * @param s     始点（0-indexed）
	 * @param g     終点（0-indexed）
	 * @return 始点から終点への最短距離。到達不能な場合は {@link Long#MAX_VALUE}
	 */
	public static long dist(final Graph graph, final int s, final int g) {
		final int n = graph.n;
		final int[] dest = graph.dest, next = graph.next, first = graph.first;
		final long[] cost = graph.cost;
		final long[] dist = new long[n];
		fill(dist, INF);
		dist[s] = 0;
		final IntArrayDeque dq = new IntArrayDeque(n);
		dq.addLast(s);
		final boolean[] visited = new boolean[n];

		while (!dq.isEmpty()) {
			final int u = dq.pollFirst();
			if (u == g) break;
			if (visited[u]) continue;
			visited[u] = true;
			final long du = dist[u];
			for (int e = first[u]; e != -1; e = next[e]) {
				final int v = dest[e];
				final long c = cost[e];
				if (dist[v] > du + c) {
					dist[v] = du + c;
					if (c == 0) dq.addFirst(v);
					else dq.addLast(v);
				}
			}
		}
		return dist[g];
	}

	/**
	 * 始点 s から終点 g への最短経路を01BFSを用いて計算します。
	 *
	 * @param graph 探索対象のグラフ
	 * @param s     始点（0-indexed）
	 * @param g     終点（0-indexed）
	 * @return 始点から終点への最短経路（経路が存在しない場合は null）
	 */
	public static int[] path(final Graph graph, final int s, final int g) {
		final PathResult result = solve(graph, s);
		return result.pathTo(g);
	}
}
