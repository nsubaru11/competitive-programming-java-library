package lib.graph;

import static java.util.Arrays.*;

/**
 * 前方スター形式の隣接リストで有向グラフを保持するクラス。
 * <p>
 * 辺は追加順に0始まりの辺IDが割り当てられます。
 * {@link #adjEdgeIds(int)} で取得した辺IDは {@link #to(int)} と {@link #cost(int)} にそのまま渡せます。
 */
public final class DirectedGraph extends Graph {
	final int[] inDegree, outDegree;

	/**
	 * {@code n} 頂点、{@code m} 辺分の領域を確保します。
	 * {@code m} は {@code setAll} が読み込む辺数でもあります。
	 *
	 * @param n 頂点数
	 * @param m 入力辺数
	 */
	public DirectedGraph(final int n, final int m) {
		super(n, m, m);
		inDegree = new int[n];
		outDegree = new int[n];
	}

	public void add(final int u, final int v, final long c) {
		addEdge(u, v, c);
		outDegree[u]++;
		inDegree[v]++;
	}

	/**
	 * 指定した辺の終点を返します。
	 *
	 * @param e 辺ID
	 * @return 辺の終点
	 */
	public int to(final int e) {
		return dest[e];
	}

	public int edgeCount() {
		return edgePtr;
	}

	public long cost(final int e) {
		return cost[e];
	}

	/**
	 * 指定した頂点の入次数と出次数の合計を返します。
	 * 自己ループは入次数と出次数にそれぞれ1本ずつ数えます。
	 *
	 * @param i 頂点
	 * @return 入次数と出次数の合計
	 */
	public int degree(final int i) {
		return inDegree[i] + outDegree[i];
	}

	/**
	 * 指定した頂点の入次数を返します。
	 *
	 * @param i 頂点
	 * @return 入次数
	 */
	public int inDegree(final int i) {
		return inDegree[i];
	}

	/**
	 * 指定した頂点の出次数を返します。
	 *
	 * @param i 頂点
	 * @return 出次数
	 */
	public int outDegree(final int i) {
		return outDegree[i];
	}

	public int[] adj(final int u) {
		final int[] adj = new int[outDegree[u]];
		for (int e = first[u], i = 0; e != -1; e = next[e], i++) {
			adj[i] = dest[e];
		}
		return adj;
	}

	public int[] adjEdgeIds(final int u) {
		final int[] ids = new int[outDegree[u]];
		for (int e = first[u], i = 0; e != -1; e = next[e], i++) {
			ids[i] = e;
		}
		return ids;
	}

	public void clear() {
		if (edgePtr == 0) return;
		fill(inDegree, 0);
		fill(outDegree, 0);
		super.clear();
	}
}
