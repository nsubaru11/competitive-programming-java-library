import java.util.*;

/**
 * Prim法を用いた全域木（MST）問題のソルバー。
 * 最小全域木または最大全域木を求めることができます。
 * 時間計算量: O(|E|log|V|)
 * 空間計算量: O(|V| + |E|)
 */
@SuppressWarnings("unused")
public final class Prim {
	private final int n;
	private final boolean isMinimum;
	private final int[] first, next, dest;
	private final long[] cost;
	private int edgeCnt = 0;

	/**
	 * 指定された頂点数と最小/最大フラグでソルバーを初期化します。
	 *
	 * @param n         頂点数（0からv-1までの頂点番号が使用される）
	 * @param m         辺の数
	 * @param isMinimum trueの場合は最小全域木、falseの場合は最大全域木を求めます
	 */
	public Prim(final int n, final int m, final boolean isMinimum) {
		this.n = n;
		this.isMinimum = isMinimum;
		this.first = new int[n];
		this.next = new int[m * 2];
		this.dest = new int[m * 2];
		this.cost = new long[m * 2];
		Arrays.fill(first, -1);
	}

	/**
	 * 最小全域木を求めるソルバーを初期化します。
	 *
	 * @param n 頂点数（0からv-1までの頂点番号が使用される）
	 * @param m 辺の数
	 */
	public Prim(final int n, final int m) {
		this(n, m, true);
	}

	/**
	 * グラフに無向辺を追加します。
	 *
	 * @param u 辺の始点（0からv-1までの値）
	 * @param v 辺の終点（0からv-1までの値）
	 * @param c 辺の重み
	 */
	public void addEdge(final int u, final int v, final long c) {
		next[edgeCnt] = first[u];
		first[u] = edgeCnt;
		dest[edgeCnt] = v;
		cost[edgeCnt] = isMinimum ? c : -c;
		edgeCnt++;
		next[edgeCnt] = first[v];
		first[v] = edgeCnt;
		dest[edgeCnt] = u;
		cost[edgeCnt] = isMinimum ? c : -c;
		edgeCnt++;
	}

	/**
	 * Prim法を実行し、全域木の総コストを計算します。
	 * 設定に応じて最小全域木または最大全域木を求めます。
	 * グラフが連結でない場合は-1を返します。
	 *
	 * @return 全域木の総コスト、または連結グラフでない場合は-1
	 */
	public long solve() {
		int u = 0;
		long ans = 0;
		int cnt = 0;
		final BitSet visited = new BitSet(n);
		final PriorityQueue<Integer> heap = new PriorityQueue<>((i, j) -> Long.compare(cost[i], cost[j]));
		visited.set(u);
		cnt++;
		for (int e = first[u]; e != -1; e = next[e]) {
			if (!visited.get(dest[e])) heap.add(e);
		}
		while (!heap.isEmpty() && cnt < n) {
			final int e = heap.poll();
			final int v = dest[e];
			if (visited.get(v)) continue;
			visited.set(v);
			cnt++;
			ans += cost[e];
			for (int e2 = first[v]; e2 != -1; e2 = next[e2]) {
				if (!visited.get(dest[e2])) heap.add(e2);
			}
		}
		return cnt == n ? (isMinimum ? ans : -ans) : -1;
	}
}
