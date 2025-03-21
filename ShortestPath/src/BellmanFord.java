import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.fill;

/**
 * Bellman-Ford アルゴリズムを実装するクラス。
 * 単一始点からの最短経路を求めることができ、負閉路が存在する場合は -INF を返す。
 * すべての辺リストに対して反復緩和を行い、最短距離を計算する。
 * <p>
 * ※ 同一始点での複数回呼び出しのためにキャッシュ機構（used フィールド）を用いているが、
 * 複雑な利用ではキャッシュの無効化や再計算のタイミングに注意する必要がある。
 */
public final class BellmanFord {
	private static final long INF = Long.MAX_VALUE;
	private final int v;
	private final long[] ans;
	private final List<Edge> edges;
	private int used = -1;

	/**
	 * コンストラクタ
	 *
	 * @param v 頂点数
	 * @param e 辺の数（初期容量として使用）
	 */
	public BellmanFord(int v, int e) {
		this.v = v;
		ans = new long[v];
		edges = new ArrayList<>(e);
	}

	/**
	 * 有向辺を追加する。
	 *
	 * @param i    辺の始点（0-indexed）
	 * @param j    辺の終点（0-indexed）
	 * @param cost 辺の重み
	 */
	public void addEdge(int i, int j, long cost) {
		edges.add(new Edge(i, j, cost));
		used = -1;
	}

	/**
	 * 始点 i から終点 j への最短経路の重みを返す。
	 * 負閉路が存在する場合は -INF を返す。
	 *
	 * @param i 始点
	 * @param j 終点
	 * @return 始点から終点への最短経路の重み（経路が存在しない場合は INF）
	 */
	public long getShortestPathWeight(int i, int j) {
		if (used != i) {
			used = i;
			fill(ans, INF);
			ans[i] = 0;
			boolean update = false;
			for (int vtx = 0; vtx < v; vtx++) {
				update = false;
				for (Edge e : edges) {
					int from = e.from;
					int to = e.to;
					long cost = e.cost;
					if (ans[from] != INF && ans[to] > ans[from] + cost) {
						ans[to] = ans[from] + cost;
						update = true;
					}
				}
				if (!update) break;
			}
			if (update) return -INF;
		}
		return ans[j];
	}

	/**
	 * 辺を表すレコードクラス。
	 * 辺の始点、終点、重みを保持する。
	 */
	private static record Edge(int from, int to, long cost) {
	}
}
