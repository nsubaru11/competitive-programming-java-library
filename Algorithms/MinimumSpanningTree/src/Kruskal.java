import java.util.*;

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
	private final int[] from, to;
	private final long[] cost;
	private final Integer[] edges;
	private final UnionFind uf;
	private int edgeCnt = 0;

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
		cost = new long[m];
		uf = new UnionFind(n);
		edges = new Integer[m];
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
	 * Kruskal法を実行し、全域木の総コストを計算します。
	 * 設定に応じて最小全域木または最大全域木を求めます。
	 * グラフが連結でない場合は-1を返します。
	 *
	 * @return 全域木の総コスト、または連結グラフでない場合は-1
	 */
	public long solve() {
		long ans = 0;
		int size = n;
		Arrays.sort(edges, 0, edgeCnt, (i, j) -> Long.compare(cost[i], cost[j]));
		for (int i = 0; i < edgeCnt; i++) {
			int e = edges[i];
			int u = from[e];
			int v = to[e];
			long c = cost[e];
			if (uf.find(u) != uf.find(v)) {
				uf.union(u, v);
				ans += c;
				size--;
			}
		}
		return size > 1 ? -1 : isMinimum ? ans : -ans;
	}

	private static final class UnionFind {
		private final int[] root, rank;

		public UnionFind(final int size) {
			root = new int[size];
			rank = new int[size];
			Arrays.setAll(root, i -> i);
		}

		public int find(final int x) {
			int root = x;
			while (root != this.root[root]) {
				root = this.root[root];
			}
			int cur = x;
			while (cur != root) {
				int next = this.root[cur];
				this.root[cur] = root;
				cur = next;
			}
			return root;
		}

		public void union(int x, int y) {
			x = find(x);
			y = find(y);
			if (x != y) {
				if (rank[x] > rank[y]) {
					root[y] = x;
				} else if (rank[x] < rank[y]) {
					root[x] = y;
				} else {
					root[y] = x;
					rank[x] += 1;
				}
			}
		}
	}
}
