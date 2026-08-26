package lib.graph;

/**
 * 無向グラフの連結性解析。橋・関節点の検出を提供する。
 */
@SuppressWarnings("unused")
public final class Connectivity {
	// TODO: 二重辺連結成分分解（2-edge-connected components）の実装を行う
	// TODO: 二重頂点連結成分分解（2-vertex-connected components）の実装を行う
	private Connectivity() {
	}

	/**
	 * 無向グラフのlow-link値を計算し、橋と関節点を検出します。
	 * DFSは再帰を使わず、明示的なスタックで実行します。
	 *
	 * @param graph 解析対象の無向グラフ。自己ループを含まないこと
	 * @return DFS順、low-link値、橋および関節点の判定結果
	 */
	public static Result lowLink(final UndirectedGraph graph) {
		final int n = graph.n;
		final int[] dest = graph.dest, next = graph.next, edgeIter = graph.first.clone();
		final int[] stack = new int[n], parentEdge = new int[n], child = new int[n];
		final int[] ord = new int[n], low = new int[n];
		boolean[] bridge = new boolean[graph.edgeCount()], articulation = new boolean[n];
		for (int i = 0, order = 1; i < n; i++) {
			if (ord[i] != 0) continue;
			stack[0] = i;
			ord[i] = low[i] = order++;
			parentEdge[i] = -1;
			outer:
			for (int len = 1; len > 0; ) {
				final int u = stack[len - 1], pe = parentEdge[u];
				while (edgeIter[u] != -1) {
					final int e = edgeIter[u];
					edgeIter[u] = next[e];
					if (pe == e) continue;
					final int v = dest[e];
					if (ord[v] != 0) {
						if (ord[v] < low[u]) low[u] = ord[v];
						continue;
					}
					parentEdge[v] = e ^ 1;
					ord[v] = low[v] = order++;
					stack[len++] = v;
					continue outer;
				}
				len--;
				if (u == i) continue;
				final int pu = dest[pe];
				if (low[pu] > low[u]) low[pu] = low[u];
				if (low[u] > ord[pu]) bridge[pe >> 1] = true;
				if (low[u] >= ord[pu]) articulation[pu] = true;
				child[pu]++;
			}
			articulation[i] = child[i] >= 2;
		}
		return new Result(ord, low, bridge, articulation);
	}

	/** low-linkの計算結果。配列は結果を直接参照するために公開されています。 */
	public static class Result {
		/** 各頂点のDFS訪問順。未訪問の頂点はありません。 */
		public final int[] ord;
		/** 各頂点のlow-link値。 */
		public final int[] low;
		/** 論理辺IDごとの橋の判定。 */
		public final boolean[] bridge;
		/** 頂点番号ごとの関節点の判定。 */
		public final boolean[] articulation;

		private Result(final int[] ord, final int[] low, final boolean[] bridge, final boolean[] articulation) {
			this.ord = ord;
			this.low = low;
			this.bridge = bridge;
			this.articulation = articulation;
		}

		/**
		 * 指定した論理辺が橋か判定します。
		 *
		 * @param e {@link UndirectedGraph}の論理辺ID
		 * @return 橋なら{@code true}
		 */
		public boolean isBridge(final int e) {
			return bridge[e];
		}

		/**
		 * 指定した頂点が関節点か判定します。
		 *
		 * @param u 頂点番号
		 * @return 関節点なら{@code true}
		 */
		public boolean isArticulation(final int u) {
			return articulation[u];
		}
	}
}
