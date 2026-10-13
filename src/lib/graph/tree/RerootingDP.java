package lib.graph.tree;

import lib.util.function.*;

/**
 * 全方位木DPで使う、子から親・親から子への走査を提供する。
 * <p>
 * DP状態の結合や辺をまたぐ変換は呼び出し側で行う。
 */
@SuppressWarnings("unused")
public final class RerootingDP {
	private RerootingDP() {}

	/**
	 * 全方位木DP用の2方向の走査を実行する。
	 * <p>
	 * {@code bottomUpConsumer} は根以外の各頂点について、子孫から親へ処理できる順序で
	 * {@code (頂点, 親, 親辺の論理ID)} を受け取る。その後 {@code topDownConsumer} は
	 * 各親子辺について、親から子へ処理できる順序で {@code (親, 子, 辺の論理ID)} を受け取る。
	 *
	 * @param tree             対象の根付き木
	 * @param bottomUpConsumer 子から親への遷移を処理するコールバック
	 * @param topDownConsumer  親から子への遷移を処理するコールバック
	 */
	public static void run(final RootedTree tree, final IntTernaryConsumer bottomUpConsumer, final IntTernaryConsumer topDownConsumer) {
		tree.ensureBuild();
		bottomUp(tree, bottomUpConsumer);
		topDown(tree, topDownConsumer);
	}

	private static void bottomUp(final RootedTree tree, final IntTernaryConsumer bottomUpConsumer) {
		final int[] preorder = tree.preorder, parent = tree.parent, parentEdge = tree.parentEdge;
		for (int i = tree.n - 1; i > 0; i--) {
			final int u = preorder[i];
			bottomUpConsumer.accept(u, parent[u], parentEdge[u]);
		}
	}

	private static void topDown(final RootedTree tree, final IntTernaryConsumer topDownConsumer) {
		final int[] preorder = tree.preorder, dest = tree.dest(), next = tree.next(), first = tree.first(), parent = tree.parent;
		for (int i = 0; i < tree.n; i++) {
			final int u = preorder[i], p = parent[u];
			for (int e = first[u]; e != -1; e = next[e]) {
				final int v = dest[e];
				if (v == p) continue;
				topDownConsumer.accept(u, v, e >> 1);
			}
		}
	}
}
