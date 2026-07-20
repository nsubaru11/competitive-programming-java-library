package lib.graph;

import static java.util.Arrays.*;

import lib.ds.priorityqueue.*;

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
	private final int[] first, next, dest, path;
	private final long[] cost;
	private int edgeCnt = 0;
	private long ans = 0;
	private boolean solved = false;

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
		first = new int[n];
		next = new int[m * 2];
		dest = new int[m * 2];
		path = new int[n - 1];
		cost = new long[m * 2];
		fill(first, -1);
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
		if (solved) return ans;
		solved = true;
		int cnt = 0;
		ans = 0;
		final LongIndexedPriorityQueue pq = new LongIndexedPriorityQueue(n);
		pq.add(0, 0);
		final int[] parentEdge = new int[n];
		while (!pq.isEmpty()) {
			final int u = pq.pollIndex();
			final long c = pq.getLast(u);
			if (cnt > 0) {
				ans += c;
				path[cnt - 1] = parentEdge[u];
			}
			cnt++;
			if (cnt == n) break;
			for (int e = first[u]; e != -1; e = next[e]) {
				final int v = dest[e];
				if (pq.relax(v, cost[e])) parentEdge[v] = e >> 1;
			}
		}
		return ans = cnt < n ? -1 : isMinimum ? ans : -ans;
	}

	/**
	 * 全域木を構成する辺のインデックスを取得します。
	 * インデックスは addEdge で追加された順序（0-indexed）に対応します。
	 *
	 * @return 全域木を構成する辺のインデックス配列
	 */
	public int[] solvePath() {
		if (!solved) solve();
		return path;
	}
}
