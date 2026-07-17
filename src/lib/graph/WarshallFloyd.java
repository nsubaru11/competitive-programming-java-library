package lib.graph;

import static java.util.Arrays.*;

/**
 * Warshall-Floyd法により全頂点対間の最短距離を求めるユーティリティクラス。
 * <p>
 * 到達不能な頂点対は {@link Long#MAX_VALUE}、負閉路の影響を受ける頂点対は
 * {@link Long#MIN_VALUE} です。
 * 計算量は {@code O(n^3)}、追加メモリは {@code O(n^2)} です。
 */
public final class WarshallFloyd {
	private static final long INF = Long.MAX_VALUE;
	private static final long NINF = Long.MIN_VALUE;

	private WarshallFloyd() {
	}

	/**
	 * 全頂点対間の最短距離を計算します。
	 *
	 * @param graph 探索対象のグラフ
	 * @return 計算結果
	 */
	public static Result solve(final Graph graph) {
		int n = graph.n;
		long[][] dist = new long[n][n];
		for (int u = 0; u < n; u++) {
			fill(dist[u], INF);
			dist[u][u] = 0;
		}
		final int[] first = graph.first, next = graph.next, dest = graph.dest;
		final long[] cost = graph.cost;
		for (int u = 0; u < n; u++) {
			for (int e = first[u]; e != -1; e = next[e]) {
				final int v = dest[e];
				final long c = cost[e];
				if (c < dist[u][v]) dist[u][v] = c;
			}
		}
		for (int via = 0; via < n; via++) {
			final long[] viaRow = dist[via];
			for (int from = 0; from < n; from++) {
				final long[] fromRow = dist[from];
				final long fromToVia = fromRow[via];
				if (fromToVia == INF) continue;
				for (int to = 0; to < n; to++) {
					final long viaToTo = viaRow[to];
					if (viaToTo == INF) continue;
					final long candidate = fromToVia + viaToTo;
					if (candidate < fromRow[to]) fromRow[to] = candidate;
				}
			}
		}
		boolean[] negative = new boolean[n];
		boolean hasNegCycle = false;
		for (int v = 0; v < n; v++) {
			if (dist[v][v] < 0) {
				negative[v] = true;
				hasNegCycle = true;
			}
		}
		for (int via = 0; via < n; via++) {
			if (!negative[via]) continue;
			long[] viaRow = dist[via];
			for (int from = 0; from < n; from++) {
				long[] fromRow = dist[from];
				if (fromRow[via] == INF) continue;
				for (int to = 0; to < n; to++) {
					if (viaRow[to] != INF) fromRow[to] = NINF;
				}
			}
		}
		return new Result(hasNegCycle, dist);
	}

	/**
	 * Warshall-Floyd法による全頂点対最短距離の計算結果。
	 */
	public static final class Result {
		public final boolean hasNegCycle;
		public final long[][] dist;

		private Result(final boolean hasNegCycle, final long[][] dist) {
			this.hasNegCycle = hasNegCycle;
			this.dist = dist;
		}

		/**
		 * 頂点 {@code u} から頂点 {@code v} への距離を返します。
		 *
		 * @param u 始点
		 * @param v 終点
		 * @return 距離。到達不能は {@link Long#MAX_VALUE}、負閉路の影響下は {@link Long#MIN_VALUE}
		 */
		public long dist(final int u, final int v) {
			return dist[u][v];
		}

		/**
		 * 頂点 {@code u} から頂点 {@code v} へ到達可能かを返します。
		 * 負閉路の影響下でも到達可能なら {@code true} です。
		 *
		 * @param u 始点
		 * @param v 終点
		 * @return 到達可能なら {@code true}
		 */
		public boolean reachable(final int u, final int v) {
			return dist[u][v] != INF;
		}

	}
}
