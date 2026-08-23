package lib.graph;

/**
 * 単一始点または複数始点最短経路の計算結果。
 * <p>
 * 負閉路を扱わないアルゴリズム（BFS・Dijkstra・0-1BFS等）では {@link #hasNegCycle} は常に
 * {@code false} で、負閉路の影響を受ける頂点は現れません。
 * Bellman-Ford法など負閉路を検出するアルゴリズムでは、到達不能な頂点の距離は
 * {@link Long#MAX_VALUE}、到達可能な負閉路の影響を受ける頂点は {@link Long#MIN_VALUE} です。
 */
public final class PathResult {
	private static final long INF = Long.MAX_VALUE;
	private static final long NINF = Long.MIN_VALUE;

	public final int[] s;
	public final boolean hasNegCycle;
	public final long[] dist;
	public final int[] parent;

	PathResult(final int s, final long[] dist, final int[] parent) {
		this(new int[]{s}, false, dist, parent);
	}

	PathResult(final int[] s, final long[] dist, final int[] parent) {
		this(s, false, dist, parent);
	}

	PathResult(final int s, final boolean hasNegCycle, final long[] dist, final int[] parent) {
		this(new int[]{s}, hasNegCycle, dist, parent);
	}

	PathResult(final int[] s, final boolean hasNegCycle, final long[] dist, final int[] parent) {
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
		final long d = distTo(v);
		if (d == INF || d == NINF) return null;
		final int[] temp = new int[dist.length];
		int len = 0, p = v;
		for (; p != parent[p]; p = parent[p]) temp[len++] = p;
		temp[len++] = p;
		final int[] res = new int[len];
		for (int i = 0; i < len; i++) res[i] = temp[len - i - 1];
		return res;
	}
}
