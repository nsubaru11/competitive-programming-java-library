package lib.graph;

import static java.util.Arrays.*;

/**
 * Bellman-Ford法により、負辺を含むグラフの単一始点・複数始点最短経路を求めるユーティリティクラス。
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
	 * @param s     始点（0-indexed）
	 * @return 計算結果
	 */
	public static PathResult solve(final Graph graph, final int s) {
		return solveInternal(graph, new int[]{s});
	}

	/**
	 * 始点 {@code s} から全頂点への最短距離を計算します。
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
		for (final int si : s) {
			parent[si] = si;
			dist[si] = 0;
		}

		for (int k = 1; k < n; k++) {
			boolean updated = false;
			for (int u = 0; u < n; u++) {
				final long du = dist[u];
				if (du == INF) continue;
				for (int e = first[u]; e != -1; e = next[e]) {
					final int v = dest[e];
					final long c = cost[e];
					if (dist[v] > du + c) {
						dist[v] = du + c;
						parent[v] = u;
						updated = true;
					}
				}
			}
			if (!updated) break;
		}

		final boolean[] affected = new boolean[n];
		final int[] q = new int[n];
		int tail = 0;
		for (int u = 0; u < n; u++) {
			final long du = dist[u];
			if (du == INF) continue;
			for (int e = first[u]; e != -1; e = next[e]) {
				final int v = dest[e];
				if (dist[v] > du + cost[e] && !affected[v]) {
					affected[v] = true;
					q[tail++] = v;
				}
			}
		}

		for (int head = 0; head < tail; head++) {
			final int u = q[head];
			for (int e = first[u]; e != -1; e = next[e]) {
				final int v = dest[e];
				if (affected[v]) continue;
				affected[v] = true;
				q[tail++] = v;
			}
		}

		for (int i = 0; i < tail; i++) {
			final int v = q[i];
			dist[v] = NINF;
			parent[v] = -1;
		}
		return new PathResult(s, tail > 0, dist, parent);
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
	 * 始点 s から終点 g への最短経路のコストをベルマンフォード法を用いて計算します。
	 *
	 * @param graph 探索対象のグラフ
	 * @param s     始点（0-indexed）
	 * @param g     終点（0-indexed）
	 * @return 始点から終点への最短距離。到達不能な場合は {@link Long#MAX_VALUE}
	 */
	public static long dist(final Graph graph, final int s, final int g) {
		final PathResult result = solve(graph, s);
		return result.dist[g];
	}

	/**
	 * 始点 s から終点 g への最短経路をベルマンフォード法を用いて計算します。
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
