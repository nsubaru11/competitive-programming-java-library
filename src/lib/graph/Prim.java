package lib.graph;

import static java.util.Arrays.*;

import lib.ds.priorityqueue.*;

/**
 * Prim法により無向グラフの最小または最大全域森を求めるユーティリティクラス。
 * <p>
 * 頂点番号が小さい未訪問頂点から各連結成分の探索を開始します。
 * グラフが連結なら結果は全域木、非連結なら各連結成分の全域木を合わせた全域森になります。
 * 辺IDを含む結果が必要な場合は {@link #minimum(UndirectedGraph)} または
 * {@link #maximum(UndirectedGraph)}、総コストだけが必要な場合は
 * {@link #minimumCost(UndirectedGraph)} または {@link #maximumCost(UndirectedGraph)} を使用します。
 * 計算量は {@code O((|V| + |E|) log |V|)}、追加メモリは {@code O(|V|)} です。
 */
public final class Prim {

	private Prim() {
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
	 * 採用辺IDと親辺の配列を確保しません。
	 *
	 * @param graph 対象の無向グラフ
	 * @return 最小全域森の総コスト
	 */
	public static long minimumCost(final UndirectedGraph graph) {
		return solveCost(graph, true);
	}

	/**
	 * 最大全域森の総コストだけを求めます。
	 * 採用辺IDと親辺の配列を確保しません。
	 *
	 * @param graph 対象の無向グラフ
	 * @return 最大全域森の総コスト
	 */
	public static long maximumCost(final UndirectedGraph graph) {
		return solveCost(graph, false);
	}

	private static SpanningForestResult solve(final UndirectedGraph graph, final boolean isMinimum) {
		int n = graph.n;
		int[] first = graph.first, next = graph.next, dest = graph.dest;
		long[] cost = graph.cost;

		int[] edgeIds = new int[n - 1], parentEdge = new int[n];
		fill(parentEdge, -1);

		int selected = 0, componentCount = 0;
		long total = 0;
		final LongIndexedPriorityQueue pq = new LongIndexedPriorityQueue(n, !isMinimum);
		for (int i = 0; i < n; i++) {
			if (pq.hasCost(i)) continue;
			componentCount++;
			pq.add(i, 0);
			while (!pq.isEmpty()) {
				final int u = pq.pollIndex();
				if (parentEdge[u] != -1) {
					total += pq.getLast(u);
					edgeIds[selected++] = parentEdge[u];
				}
				for (int e = first[u]; e != -1; e = next[e]) {
					final int v = dest[e];
					if (pq.relax(v, cost[e])) parentEdge[v] = e >> 1;
				}
			}
		}
		return new SpanningForestResult(total, selected == edgeIds.length ? edgeIds : copyOf(edgeIds, selected), componentCount);
	}

	private static long solveCost(final UndirectedGraph graph, final boolean isMinimum) {
		int n = graph.n;
		int[] first = graph.first, next = graph.next, dest = graph.dest;
		long[] cost = graph.cost;

		long total = 0;
		final LongIndexedPriorityQueue pq = new LongIndexedPriorityQueue(n, !isMinimum);
		for (int i = 0; i < n; i++) {
			if (pq.hasCost(i)) continue;
			pq.add(i, 0);
			while (!pq.isEmpty()) {
				final int u = pq.pollIndex();
				total += pq.getLast(u);
				for (int e = first[u]; e != -1; e = next[e]) {
					pq.relax(dest[e], cost[e]);
				}
			}
		}
		return total;
	}
}
