import java.util.*;

/**
 * Prim法を用いた全域木（MST）問題のソルバー。
 * 最小全域木または最大全域木を求めることができます。
 * 時間計算量: O(|E|log|V|)
 * 空間計算量: O(|V| + |E|)
 */
public final class Prim {
	private final int v;
	private final boolean isMinimum;
	private final List<List<Edge>> edges;

	/**
	 * 指定された頂点数と最小/最大フラグでソルバーを初期化します。
	 *
	 * @param v         頂点数（0からv-1までの頂点番号が使用される）
	 * @param isMinimum trueの場合は最小全域木、falseの場合は最大全域木を求めます
	 */
	public Prim(int v, final boolean isMinimum) {
		this.v = v;
		this.isMinimum = isMinimum;
		this.edges = new ArrayList<>(v);
		while (v-- > 0) {
			edges.add(new ArrayList<>());
		}
	}

	/**
	 * 最小全域木を求めるソルバーを初期化します。
	 *
	 * @param v 頂点数（0からv-1までの頂点番号が使用される）
	 */
	public Prim(final int v) {
		this(v, true);
	}

	/**
	 * グラフに無向辺を追加します。
	 *
	 * @param u    辺の始点（0からv-1までの値）
	 * @param v    辺の終点（0からv-1までの値）
	 * @param cost 辺の重み
	 */
	public void addEdge(final int u, final int v, final long cost) {
		edges.get(u).add(new Edge(u, v, cost));
		edges.get(v).add(new Edge(v, u, cost));
	}

	/**
	 * Prim法を実行し、全域木の総コストを計算します。
	 * 設定に応じて最小全域木または最大全域木を求めます。
	 * グラフが連結でない場合は-1を返します。
	 *
	 * @return 全域木の総コスト、または連結グラフでない場合は-1
	 */
	public long solve() {
		int start = 0;
		long ans = 0;
		int size = 0;
		final BitSet visited = new BitSet(v);
		final Comparator<Edge> comparator = isMinimum
				? Comparator.comparingLong(Edge::cost)
				: Comparator.comparingLong(Edge::cost).reversed();
		final PriorityQueue<Edge> heap = new PriorityQueue<>(comparator);
		visited.set(start);
		size++;
		heap.addAll(edges.get(start));
		while (!heap.isEmpty() && size < v) {
			final Edge e = heap.poll();
			final int v = e.v();
			if (visited.get(v)) continue;
			visited.set(v);
			size++;
			ans += e.cost();
			for (final Edge edge : edges.get(v)) {
				if (!visited.get(edge.v())) heap.add(edge);
			}
		}
		return size == v ? ans : -1;
	}

	/**
	 * グラフの辺を表すレコード。
	 */
	private record Edge(int u, int v, long cost) {
		// コンストラクタはrecordにより自動生成されます
	}
}
