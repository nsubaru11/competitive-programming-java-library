package lib.graph;

import static java.util.Arrays.*;

import lib.ds.priorityqueue.*;

/**
 * Dijkstra法により、非負重み付きグラフの単一始点・複数始点最短経路を求めるユーティリティクラス。
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
		final LongIndexedPriorityQueue dist = new LongIndexedPriorityQueue(n);
		for (final int si : s) {
			parent[si] = si;
			dist.add(si, 0);
		}

		while (!dist.isEmpty()) {
			final int u = dist.peekIndex();
			final long du = dist.poll();
			for (int e = first[u]; e != -1; e = next[e]) {
				final int v = dest[e];
				final long c = cost[e];
				if (dist.relax(v, du + c)) parent[v] = u;
			}
		}
		final long[] res = new long[graph.n];
		setAll(res, i -> dist.getLastOrDefault(i, INF));
		return new PathResult(s, res, parent);
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
	 * 始点 s から終点 g への最短経路のコストをダイクストラ法を用いて計算します。
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
		final LongIndexedPriorityQueue dist = new LongIndexedPriorityQueue(n);
		dist.add(s, 0);

		while (!dist.isEmpty()) {
			final int u = dist.peekIndex();
			if (u == g) break;
			final long du = dist.poll();
			for (int e = first[u]; e != -1; e = next[e]) {
				final int v = dest[e];
				final long c = cost[e];
				dist.relax(v, du + c);
			}
		}
		return dist.getLastOrDefault(g, INF);
	}

	/**
	 * 始点 s から終点 g への最短経路をダイクストラ法を用いて計算します。
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
