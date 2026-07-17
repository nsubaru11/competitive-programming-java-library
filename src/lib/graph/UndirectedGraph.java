package lib.graph;

import static java.util.Arrays.*;

/**
 * 前方スター形式の隣接リストで、自己ループを含まない無向グラフを保持するクラス。
 * <p>
 * 無向辺は追加順に0始まりの辺IDが割り当てられます。
 * 内部表現では1本の無向辺を2本の内部辺として保持しますが、
 * 外部には無向辺IDとして公開します。
 * <p>
 * {@link #adjEdgeIds(int)} で取得した辺IDは {@link #to(int, int)} と
 * {@link #cost(int)} にそのまま渡せます。
 * {@link #to(int, int)} は「頂点 {@code u} から見た接続先頂点」を返します。
 */
public final class UndirectedGraph extends Graph {
	private final int[] degree;

	/**
	 * {@code n} 頂点、{@code m} 辺分の領域を確保します。
	 * 内部では各無向辺を2本の内部辺として保持します。
	 *
	 * @param n 頂点数
	 * @param m 入力辺数
	 */
	public UndirectedGraph(final int n, final int m) {
		super(n, m, m << 1);
		degree = new int[n];
	}

	public void add(final int u, final int v, final long c) {
		addEdge(u, v, c);
		degree[u]++;
		addEdge(v, u, c);
		degree[v]++;
	}

	/**
	 * 無向辺 {@code e} について、頂点 {@code u} と反対側の端点を返します。
	 *
	 * @param u 辺 {@code e} の一方の端点
	 * @param e 無向辺ID
	 * @return 反対側の端点
	 */
	public int to(final int u, final int e) {
		final int v1 = dest[e << 1];
		final int v2 = dest[e << 1 | 1];
		return u != v1 ? v1 : v2;
	}

	public int edgeCount() {
		return edgePtr >> 1;
	}

	public long cost(final int e) {
		return cost[e << 1];
	}

	public int degree(final int i) {
		return degree[i];
	}

	public int[] adj(final int u) {
		final int[] adj = new int[degree[u]];
		for (int e = first[u], i = 0; e != -1; e = next[e], i++) {
			adj[i] = dest[e];
		}
		return adj;
	}

	public int[] adjEdgeIds(final int u) {
		final int[] ids = new int[degree[u]];
		for (int e = first[u], i = 0; e != -1; e = next[e], i++) {
			ids[i] = e >> 1;
		}
		return ids;
	}

	public void clear() {
		if (edgePtr == 0) return;
		fill(degree, 0);
		super.clear();
	}
}
