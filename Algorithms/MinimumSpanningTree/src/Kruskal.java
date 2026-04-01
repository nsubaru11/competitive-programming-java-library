import static java.util.Arrays.*;

/**
 * Kruskal法を用いた全域木（MST）問題のソルバー。
 * 最小全域木または最大全域木を求めることができます。
 * 時間計算量: O(|E|log|E|)（Eは辺の数）
 * 空間計算量: O(|V| + |E|)
 */
@SuppressWarnings("unused")
public final class Kruskal {
	private final int n;
	private final boolean isMinimum;
	private final int[] from, to, edges, path;
	private final long[] cost;
	private final UnionFind uf;
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
	public Kruskal(final int n, final int m, final boolean isMinimum) {
		this.n = n;
		this.isMinimum = isMinimum;
		from = new int[m];
		to = new int[m];
		edges = new int[m];
		path = new int[n - 1];
		cost = new long[m];
		uf = new UnionFind(n);
	}

	/**
	 * 最小全域木を求めるソルバーを初期化します。
	 *
	 * @param n 頂点数（0からv-1までの頂点番号が使用される）
	 * @param m 辺の数
	 */
	public Kruskal(final int n, final int m) {
		this(n, m, true);
	}

	/**
	 * グラフに辺を追加します。
	 *
	 * @param u 辺の始点（0からv-1までの値）
	 * @param v 辺の終点（0からv-1までの値）
	 * @param c 辺の重み
	 */
	public void addEdge(final int u, final int v, final long c) {
		from[edgeCnt] = u;
		to[edgeCnt] = v;
		cost[edgeCnt] = isMinimum ? c : -c;
		edges[edgeCnt] = edgeCnt;
		edgeCnt++;
	}

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

	/**
	 * Kruskal法を実行し、全域木の総コストを計算します。
	 * 設定に応じて最小全域木または最大全域木を求めます。
	 * グラフが連結でない場合は-1を返します。
	 *
	 * @return 全域木の総コスト、または連結グラフでない場合は-1
	 */
	public long solve() {
		if (solved) return ans;
		int size = 0;
		sortEdges(edges, cost, 0, edgeCnt);
		for (int i = 0; size < n - 1 && i < edgeCnt; i++) {
			int e = edges[i], u = from[e], v = to[e];
			long c = cost[e];
			if (uf.union(u, v)) {
				path[size++] = e;
				ans += c;
			}
		}
		solved = true;
		return ans = size < n - 1 ? -1 : isMinimum ? ans : -ans;
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

	private static final class UnionFind {
		private final int[] root, rank;

		public UnionFind(final int size) {
			root = new int[size];
			rank = new int[size];
			setAll(root, i -> i);
		}

		public int find(final int x) {
			return x == root[x] ? x : (root[x] = find(root[x]));
		}

		public boolean union(int x, int y) {
			x = find(x);
			y = find(y);
			if (x == y) return false;
			if (rank[x] < rank[y]) {
				root[x] = y;
			} else {
				root[y] = x;
				if (rank[x] == rank[y]) rank[x]++;
			}
			return true;
		}
	}
}
