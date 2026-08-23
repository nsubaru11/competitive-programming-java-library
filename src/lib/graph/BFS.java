package lib.graph;

import static java.util.Arrays.*;

/**
 * BFS。辺重みが1のグラフの単一始点・複数始点最短路を O(V + E) で求める。
 */
public final class BFS {
	private static final long INF = Long.MAX_VALUE;

	private BFS() {
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
		final int[] parent = new int[n];
		fill(parent, -1);
		final long[] dist = new long[n];
		fill(dist, INF);
		final int[] dq = new int[n];
		int tail = 0;
		for (final int si : s) {
			parent[si] = si;
			dist[si] = 0;
			dq[tail++] = si;
		}

		for (int head = 0; head < tail; head++) {
			final int u = dq[head];
			final long du = dist[u];
			for (int e = first[u]; e != -1; e = next[e]) {
				final int v = dest[e];
				if (dist[v] != INF) continue;
				dist[v] = du + 1;
				parent[v] = u;
				dq[tail++] = v;
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
	 * 始点 s から終点 g への最短経路のコストをBFSを用いて計算します。
	 *
	 * @param graph 探索対象のグラフ
	 * @param s     始点（0-indexed）
	 * @param g     終点（0-indexed）
	 * @return 始点から終点への最短距離。到達不能な場合は {@link Long#MAX_VALUE}
	 */
	public static long dist(final Graph graph, final int s, final int g) {
		if (s == g) return 0;
		final int n = graph.n;
		final int[] dest = graph.dest, next = graph.next, first = graph.first;
		final long[] dist = new long[n];
		fill(dist, INF);
		dist[s] = 0;
		final int[] dq = new int[n];
		dq[0] = s;

		for (int head = 0, tail = 1; head < tail; head++) {
			final int u = dq[head];
			final long du = dist[u];
			for (int e = first[u]; e != -1; e = next[e]) {
				final int v = dest[e];
				if (dist[v] != INF) continue;
				dist[v] = du + 1;
				if (v == g) return dist[v];
				dq[tail++] = v;
			}
		}
		return dist[g];
	}

	/**
	 * 始点 s から終点 g への最短経路をBFSを用いて計算します。
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
