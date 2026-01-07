import static java.util.Arrays.fill;

/**
 * Bellman-Ford アルゴリズムを実装するクラス。
 * 単一始点からの最短経路を求めることができ、負閉路が存在する場合は -INF を返す。
 * すべての辺リストに対して反復緩和を行い、最短距離を計算する。
 * <p>
 * ※ 同一始点での複数回呼び出しのためにキャッシュ機構（used フィールド）を用いているが、
 * 複雑な利用ではキャッシュの無効化や再計算のタイミングに注意する必要がある。
 */
@SuppressWarnings("unused")
public final class BellmanFord {
	private static final long INF = Long.MAX_VALUE;
	private static final long NEG_INF = Long.MIN_VALUE;

	private final int v;
	private final long[] ans;
	private final int[] dest, next, first;
	private final long[] cost;
	private int e;
	private int used = -1;

	/**
	 * コンストラクタ
	 *
	 * @param v 頂点数
	 * @param e 辺数の最大値
	 */
	public BellmanFord(final int v, final int e) {
		this.v = v;
		ans = new long[v];
		dest = new int[e];
		next = new int[e];
		first = new int[v];
		cost = new long[e];
		fill(first, -1);
		this.e = 0;
	}

	/**
	 * 有向辺を追加する。
	 *
	 * @param i 辺の始点（0-indexed）
	 * @param j 辺の終点（0-indexed）
	 * @param c 辺の重み
	 */
	public void addEdge(final int i, final int j, final long c) {
		dest[e] = j;
		cost[e] = c;
		next[e] = first[i];
		first[i] = e;
		e++;
		used = -1;
	}

	/**
	 * 始点 i から終点 j への最短経路の重みを返す
	 * 負閉路の影響を受ける場合は -INF を返す
	 *
	 * @param i 始点
	 * @param j 終点
	 * @return 始点から終点への最短経路の重み（到達不可能なら INF、負閉路影響下なら -INF）
	 */
	public long solve(final int i, final int j) {
		if (used != i) {
			used = i;
			fill(ans, INF);
			ans[i] = 0;
			boolean update = false;
			for (int vtx = 0; vtx < v; vtx++) {
				update = false;
				for (int from = 0; from < v; from++) {
					long d = ans[from];
					if (d == INF || d == NEG_INF) continue;

					for (int edge = first[from]; edge != -1; edge = next[edge]) {
						int to = dest[edge];
						long c = cost[edge];
						long newDist = d + c;

						if (ans[to] > newDist) {
							ans[to] = newDist;
							update = true;
						}
					}
					if (!update) break;
				}
				if (update) return -INF;
			}
			return ans[j];
		}
		return ans[j];
	}

}