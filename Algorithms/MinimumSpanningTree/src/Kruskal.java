import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Kruskal法を用いた全域木（MST）問題のソルバー。
 * 最小全域木または最大全域木を求めることができます。
 * 時間計算量: O(|E|log|E|)（Eは辺の数）
 * 空間計算量: O(|V| + |E|)
 */
public class Kruskal {
	private final int v;
	private final UnionFind uf;
	private final PriorityQueue<Edge> edges;

	/**
	 * 指定された頂点数と最小/最大フラグでソルバーを初期化します。
	 *
	 * @param v         頂点数（0からv-1までの頂点番号が使用される）
	 * @param isMinimum trueの場合は最小全域木、falseの場合は最大全域木を求めます
	 */
	public Kruskal(final int v, final boolean isMinimum) {
		this.v = v;
		uf = new UnionFind(v);
		Comparator<Edge> comparator = isMinimum
				? Comparator.comparingLong(Edge::cost)
				: Comparator.comparingLong(Edge::cost).reversed();
		edges = new PriorityQueue<>(comparator);
	}

	/**
	 * 最小全域木を求めるソルバーを初期化します。
	 *
	 * @param v 頂点数（0からv-1までの頂点番号が使用される）
	 */
	public Kruskal(final int v) {
		this(v, true);
	}

	/**
	 * グラフに辺を追加します。
	 *
	 * @param u    辺の始点（0からv-1までの値）
	 * @param v    辺の終点（0からv-1までの値）
	 * @param cost 辺の重み
	 */
	public void addEdge(final int u, final int v, final long cost) {
		edges.add(new Edge(u, v, cost));
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
		int size = v;
		while (!edges.isEmpty()) {
			Edge e = edges.poll();
			int u = e.u();
			int v = e.v();
			long cost = e.cost();
			if (uf.find(u) != uf.find(v)) {
				uf.union(u, v);
				ans += cost;
				size--;
			}
		}
		return size > 1 ? -1 : ans;
	}

	/**
	 * グラフの辺を表すレコード。
	 */
	private record Edge(int u, int v, long cost) {
		// コンストラクタはrecordにより自動生成されます
	}

	/**
	 * Union-Find（素集合データ構造）の実装。
	 * 経路圧縮とランクによる最適化を行っています。
	 */
	private static final class UnionFind {
		/**
		 * 各要素の親要素を格納する配列
		 */
		private final int[] root;
		/**
		 * 各要素がルートの場合のランク（木の高さの上限）を格納する配列
		 */
		private final int[] rank;

		/**
		 * 指定されたサイズでUnion-Findデータ構造を初期化します。
		 * 初期状態では、各要素は独立した集合です。
		 *
		 * @param size 要素数
		 */
		public UnionFind(final int size) {
			root = new int[size];
			rank = new int[size];
			Arrays.setAll(root, i -> i);
		}

		/**
		 * 要素xが属する集合の代表元を見つけます。
		 * 経路圧縮を行い、効率的な検索を可能にします。
		 *
		 * @param x 検索する要素
		 * @return xが属する集合の代表元
		 */
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

		/**
		 * 要素xを含む集合と要素yを含む集合を統合します。
		 * ランクに基づく最適化を行い、木の高さを最小に保ちます。
		 *
		 * @param x 最初の集合に属する要素
		 * @param y 2番目の集合に属する要素
		 */
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