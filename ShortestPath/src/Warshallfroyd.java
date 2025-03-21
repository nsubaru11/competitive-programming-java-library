import static java.lang.Math.min;
import static java.util.Arrays.fill;

/**
 * Warshall-Floyd アルゴリズムを実装するクラス。
 * 全点対間の最短経路を計算する。負閉路が存在する場合は、
 * getShortestPathWeight() は -INF を返し、getAllShortestPathWeight() は null を返す。
 * <p>
 * ※ 隣接行列を用いるため、頂点数が大きい場合はメモリや計算量の観点で注意が必要。
 */
public final class Warshallfroyd {
	private static final long INF = Long.MAX_VALUE;
	private final int v;
	private final long[][] dist;
	private boolean update = false;
	private boolean isNegative = false;

	/**
	 * コンストラクタ
	 *
	 * @param v 頂点数
	 */
	public Warshallfroyd(int v) {
		this.v = v;
		dist = new long[v][v];
		for (int i = 0; i < v; i++) {
			fill(dist[i], INF);
			dist[i][i] = 0;
		}
	}

	/**
	 * 有向辺を追加する。
	 * 既に辺が存在する場合は、より小さい重みを採用する。
	 *
	 * @param from 開始点（0-indexed）
	 * @param to   到着点（0-indexed）
	 * @param cost 辺の重み
	 */
	public void addEdge(int from, int to, long cost) {
		dist[from][to] = min(dist[from][to], cost);
		update = true;
		isNegative = false;
	}

	/**
	 * 始点 from から終点 to への最短経路の重みを返す。
	 * 負閉路が存在する場合は -INF を返す。
	 *
	 * @param from 始点（0-indexed）
	 * @param to   終点（0-indexed）
	 * @return 始点から終点への最短経路の重み（到達不可能なら INF）
	 */
	public long getShortestPathWeight(int from, int to) {
		if (update) dist();
		return isNegative ? -INF : dist[from][to];
	}

	/**
	 * 全点対間の最短経路の重みを含む隣接行列を返す。
	 * 負閉路が存在する場合は null を返す。
	 *
	 * @return 全点対間の最短経路行列（負閉路がある場合は null）
	 */
	public long[][] getAllShortestPathWeight() {
		if (update) dist();
		return isNegative ? null : dist;
	}

	/**
	 * ワーシャルフロイド法により、全点対間の最短経路を計算する。
	 * 辺の緩和を全頂点対で行い、負閉路が検出された場合は isNegative を true にする。
	 */
	private void dist() {
		update = false;
		for (int via = 0; via < v; via++) {
			for (int from = 0; from < v; from++) {
				if (dist[from][via] == INF) continue;
				for (int to = 0; to < v; to++) {
					if (dist[via][to] == INF) continue;
					dist[from][to] = min(dist[from][to], dist[from][via] + dist[via][to]);
					if (from == to && dist[from][to] < 0) {
						isNegative = true;
						return;
					}
				}
			}
		}
	}
}
