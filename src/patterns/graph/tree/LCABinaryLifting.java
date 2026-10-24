package patterns.graph.tree;

import lib.graph.tree.*;

/**
 * バイナリリフティングによる最小共通祖先（LCA）の実装例。
 * <p>
 * {@link RootedTree} の親・深さ情報を使い、各頂点の 2 の冪個上の祖先を前処理します。
 * 前処理は {@code O(N log N)}、LCA クエリは {@code O(log N)} です。
 */
public final class LCABinaryLifting {
	private final RootedTree tree;
	private final int[][] ancestor;
	private final int[] depth;

	/**
	 * 根付き木から祖先表を構築します。全辺を追加してから呼び出してください。
	 *
	 * @param tree 根付き木
	 */
	public LCABinaryLifting(final RootedTree tree) {
		this.tree = tree;
		final int n = tree.n;
		depth = new int[n];
		final int levels = 32 - Integer.numberOfLeadingZeros(n);
		ancestor = new int[levels][n];
		for (int v = 0; v < n; v++) {
			ancestor[0][v] = tree.parent(v);
			depth[v] = tree.depth(v);
		}
		for (int k = 1; k < levels; k++) {
			for (int v = 0; v < n; v++) {
				ancestor[k][v] = ancestor[k - 1][ancestor[k - 1][v]];
			}
		}
	}

	/**
	 * 頂点 {@code u} と {@code v} の最小共通祖先を返します。
	 *
	 * @param u 頂点番号
	 * @param v 頂点番号
	 * @return 最小共通祖先
	 */
	public int lca(int u, int v) {
		if (depth[u] < depth[v]) {
			final int t = u;
			u = v;
			v = t;
		}
		int diff = depth[u] - depth[v];
		for (int k = 0; diff != 0; k++, diff >>>= 1) {
			if ((diff & 1) != 0) u = ancestor[k][u];
		}
		if (u == v) return u;
		for (int k = ancestor.length - 1; k >= 0; k--) {
			if (ancestor[k][u] != ancestor[k][v]) {
				u = ancestor[k][u];
				v = ancestor[k][v];
			}
		}
		return ancestor[0][u];
	}

	/**
	 * 頂点 {@code u} の {@code k} 個上の祖先を返します。
	 *
	 * @param u 頂点番号
	 * @param k 根方向へ上る辺数（{@code 0 <= k <= depth(u)}）
	 * @return {@code k} 個上の祖先
	 */
	public int kthAncestor(int u, int k) {
		for (int bit = 0; k != 0; bit++, k >>>= 1) {
			if ((k & 1) != 0) u = ancestor[bit][u];
		}
		return u;
	}

	/**
	 * 頂点 {@code u} と {@code v} の重み付き距離を返します。
	 */
	public long distance(final int u, final int v) {
		final int p = lca(u, v);
		return tree.rootDistance(u) + tree.rootDistance(v) - 2L * tree.rootDistance(p);
	}
}
