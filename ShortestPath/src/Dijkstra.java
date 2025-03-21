import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import static java.util.Arrays.fill;

/**
 * Dijkstra アルゴリズムを実装するクラス。
 * 非負重み付きグラフに対して、単一始点から各頂点への最短経路を求める。
 * 優先度付きキューを用いて効率的な探索を実現している。
 * <p>
 * ※ 同一始点での複数回呼び出しのためにキャッシュ機構（used フィールド）を用いているが、
 * 辺の追加後はキャッシュを無効化する必要がある。
 */
public final class Dijkstra {
	private static final long INF = Long.MAX_VALUE;
	private final int v;
	private final long[] ans;
	private final List<List<Edge>> edges;
	private int used = -1;

	/**
	 * コンストラクタ
	 *
	 * @param v 頂点数
	 */
	public Dijkstra(int v) {
		this.v = v;
		edges = new ArrayList<>(v);
		ans = new long[v];
		for (int i = 0; i < v; i++)
			edges.add(new ArrayList<>());
	}

	/**
	 * 有向辺を追加する。
	 *
	 * @param i    辺の始点（0-indexed）
	 * @param j    辺の終点（0-indexed）
	 * @param cost 辺の重み
	 */
	public void addEdge(int i, int j, long cost) {
		edges.get(i).add(new Edge(i, j, cost));
		used = -1;
	}

	/**
	 * 始点 i から終点 j への最短経路の重みを返す。
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
			PriorityQueue<Vertex> pq = new PriorityQueue<>();
			pq.add(new Vertex(i, ans[i]));
			while (!pq.isEmpty()) {
				Vertex vertex = pq.poll();
				int from = vertex.index;
				if (ans[from] < vertex.cost)
					continue;  // すでにより良い経路が見つかっている場合はスキップ
				for (Edge e : edges.get(from)) {
					long cost = e.cost;
					int to = e.to;
					if (ans[from] != INF && ans[to] > ans[from] + cost) {
						ans[to] = ans[from] + cost;
						pq.add(new Vertex(to, ans[to]));
					}
				}
			}
		}
		return ans[j];
	}

	/**
	 * 辺を表すレコードクラス。
	 * 辺の始点、終点、重みを保持する。
	 */
	private record Edge(int from, int to, long cost) {
	}

	/**
	 * 優先度付きキューで使用するノードを表現するレコードクラス。
	 * 各頂点のインデックスと、始点からその頂点までの現在の推定距離を保持する。
	 */
	private record Vertex(int index, long cost) implements Comparable<Vertex> {
		@Override
		public int compareTo(Vertex o) {
			return Long.compare(cost, o.cost);
		}
	}
}
