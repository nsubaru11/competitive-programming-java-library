import java.util.*;

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
	private final int n;
	private final int[] dest, next, first;
	private final long[] cost;
	private final long[] dist;
	private int edgeCount = 0;
	private int cache = -1;
	private boolean hasNegCycle = false;

	/**
	 * @param n 頂点数
	 * @param m 辺の最大数（確保するメモリ量）
	 */
	public BellmanFord(final int n, final int m) {
		this.n = n;
		dist = new long[n];
		dest = new int[m];
		next = new int[m];
		cost = new long[m];
		first = new int[n];
		Arrays.fill(first, -1);
	}

	/**
	 * 有向辺を追加する
	 */
	public void addEdge(final int from, final int to, final long c) {
		dest[edgeCount] = to;
		cost[edgeCount] = c;
		next[edgeCount] = first[from];
		first[from] = edgeCount++;
		cache = -1;
	}

	/**
	 * 始点 i から終点 j への最短距離を求める。
	 * 始点から到達可能な負閉路が存在する場合は -INF (Long.MIN_VALUE) を返す。
	 * 到達不能な場合は INF (Long.MAX_VALUE) を返す。
	 */
	public long solve(final int i, final int j) {
		if (cache == i) {
			if (hasNegCycle) return Long.MIN_VALUE;
			return dist[j];
		}

		cache = i;
		hasNegCycle = false;
		Arrays.fill(dist, INF);
		dist[i] = 0;

		boolean updated = false;
		for (int k = 1; k <= n; k++) {
			updated = false;
			for (int u = 0; u < n; u++) {
				if (dist[u] == INF) continue;
				for (int e = first[u]; e != -1; e = next[e]) {
					int v = dest[e];
					long c = cost[e];
					if (dist[v] > dist[u] + c) {
						dist[v] = dist[u] + c;
						updated = true;
						if (k == n) {
							hasNegCycle = true;
							return Long.MIN_VALUE;
						}
					}
				}
			}
			if (!updated) break;
		}
		return dist[j];
	}

	/**
	 * 直前の solve() 呼び出しにおいて、到達可能な負閉路が検出されたかを返す
	 */
	public boolean hasNegativeCycle() {
		return hasNegCycle;
	}

	/**
	 * 直前の solve() の始点からの距離配列を取得する
	 */
	public long[] getDist() {
		return dist;
	}
}
