package lib.graph;

import static java.lang.Math.*;
import static java.util.Arrays.*;

import lib.ds.unionfind.*;

/**
 * Kruskal法により無向グラフの最小または最大全域森を求めるユーティリティクラス。
 * <p>
 * グラフが連結なら結果は全域木、非連結なら各連結成分の全域木を合わせた全域森になります。
 * 辺IDを含む結果が必要な場合は {@link #minimum(UndirectedGraph)} または
 * {@link #maximum(UndirectedGraph)}、総コストだけが必要な場合は
 * {@link #minimumCost(UndirectedGraph)} または {@link #maximumCost(UndirectedGraph)} を使用します。
 * 計算量は {@code O(|E| log |E|)}、追加メモリは {@code O(|V| + |E|)} です。
 */
public final class Kruskal {
	/**
	 * Kruskal法の辺インデックスを、対応するコストに基づいてソートします。
	 * Dual-Pivot Quicksortのエッセンスを採用したプリミティブ実装です。
	 */
	private static void sortEdges(int[] edges, long[] cost, int left, int right) {
		int size = right - left;

		if (size < 47) {
			for (int i = left + 1; i < right; i++) {
				int pivotEdge = edges[i];
				long pivotCost = cost[pivotEdge];
				int j = i - 1;
				while (j >= left && cost[edges[j]] > pivotCost) {
					edges[j + 1] = edges[j];
					j--;
				}
				edges[j + 1] = pivotEdge;
			}
			return;
		}

		int step = (size >> 3) * 3 + 3;
		int e1 = left + step;
		int e5 = right - 1 - step;
		int e3 = (left + right) >>> 1;
		int e2 = (e1 + e3) >>> 1;
		int e4 = (e3 + e5) >>> 1;

		if (cost[edges[e5]] < cost[edges[e2]]) swap(edges, e2, e5);
		if (cost[edges[e4]] < cost[edges[e1]]) swap(edges, e1, e4);
		if (cost[edges[e5]] < cost[edges[e4]]) swap(edges, e4, e5);
		if (cost[edges[e2]] < cost[edges[e1]]) swap(edges, e1, e2);
		if (cost[edges[e4]] < cost[edges[e2]]) swap(edges, e2, e4);

		int p1 = edges[e2], p2 = edges[e4];
		long v1 = cost[p1], v2 = cost[p2];

		int less = left, greater = right - 2;

		edges[e2] = edges[left];
		edges[e4] = edges[right - 1];
		edges[left] = p1;
		edges[right - 1] = p2;

		for (int k = left + 1; k <= greater; k++) {
			if (cost[edges[k]] < v1) {
				swap(edges, k, ++less);
			} else if (cost[edges[k]] > v2) {
				while (k < greater && cost[edges[greater]] > v2) greater--;
				swap(edges, k, greater--);
				if (cost[edges[k]] >= v1) continue;
				swap(edges, k, ++less);
			}
		}

		edges[left] = edges[less];
		edges[less] = p1;
		edges[right - 1] = edges[greater + 1];
		edges[greater + 1] = p2;

		sortEdges(edges, cost, left, less);
		if (v1 < v2) sortEdges(edges, cost, less + 1, greater + 1);
		sortEdges(edges, cost, greater + 2, right);
	}

	private static void swap(int[] a, int i, int j) {
		int t = a[i];
		a[i] = a[j];
		a[j] = t;
	}

	private Kruskal() {
	}

	/**
	 * 最小全域森を求めます。
	 *
	 * @param graph 対象の無向グラフ
	 * @return 最小全域森の総コスト、採用辺ID、連結成分数
	 */
	public static SpanningForestResult minimum(final UndirectedGraph graph) {
		return solve(graph, true);
	}

	/**
	 * 最大全域森を求めます。
	 *
	 * @param graph 対象の無向グラフ
	 * @return 最大全域森の総コスト、採用辺ID、連結成分数
	 */
	public static SpanningForestResult maximum(final UndirectedGraph graph) {
		return solve(graph, false);
	}

	/**
	 * 最小全域森の総コストだけを求めます。
	 * 採用辺IDの配列を確保しません。
	 *
	 * @param graph 対象の無向グラフ
	 * @return 最小全域森の総コスト
	 */
	public static long minimumCost(final UndirectedGraph graph) {
		return solveCost(graph, true);
	}

	/**
	 * 最大全域森の総コストだけを求めます。
	 * 採用辺IDの配列を確保しません。
	 *
	 * @param graph 対象の無向グラフ
	 * @return 最大全域森の総コスト
	 */
	public static long maximumCost(final UndirectedGraph graph) {
		return solveCost(graph, false);
	}

	private static SpanningForestResult solve(final UndirectedGraph graph, final boolean isMinimum) {
		int n = graph.n, edgeCnt = graph.edgeCount();
		long[] cost = graph.cost;
		int[] dest = graph.dest;

		int[] edges = new int[edgeCnt];
		setAll(edges, i -> i << 1);
		sortEdges(edges, cost, 0, edgeCnt);

		UnionFind uf = new UnionFind(n);
		int[] edgeIds = new int[min(n - 1, edgeCnt)];
		int selected = 0;
		long total = 0;
		for (int i = 0; selected < n - 1 && i < edgeCnt; i++) {
			int j = isMinimum ? i : edgeCnt - i - 1;
			int e = edges[j], u = dest[e], v = dest[e + 1];
			if (uf.union(u, v)) {
				edgeIds[selected++] = e >> 1;
				total += cost[e];
			}
		}
		return new SpanningForestResult(total, selected == edgeIds.length ? edgeIds : copyOf(edgeIds, selected), uf.groupCount());
	}

	private static long solveCost(final UndirectedGraph graph, final boolean isMinimum) {
		int n = graph.n, edgeCnt = graph.edgeCount();
		long[] cost = graph.cost;
		int[] dest = graph.dest;

		int[] edges = new int[edgeCnt];
		setAll(edges, i -> i << 1);
		sortEdges(edges, cost, 0, edgeCnt);

		UnionFind uf = new UnionFind(n);
		int selected = 0;
		long total = 0;
		for (int i = 0; selected < n - 1 && i < edgeCnt; i++) {
			int j = isMinimum ? i : edgeCnt - i - 1;
			int e = edges[j], u = dest[e], v = dest[e + 1];
			if (uf.union(u, v)) {
				total += cost[e];
				selected++;
			}
		}
		return total;
	}
}
