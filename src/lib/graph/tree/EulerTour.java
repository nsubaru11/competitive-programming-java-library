package lib.graph.tree;

/**
 * 根付き木の各頂点を入場時と退場時に記録した長さ {@code 2N} のイベント列を提供する。
 * <p>
 * {@code eventAt(i)} はイベント位置の頂点を返し、{@code enter/exit} は頂点ごとのイベント位置を返す。
 * 構築は {@code O(N)}、追加領域は {@code O(N)}。LCA用のDFS往復列とは異なる。
 */
public final class EulerTour {
	final int[] event;
	private final RootedTree tree;

	/**
	 * RootedTreeのpreorder情報から入退場イベント列を構築する。
	 */
	public EulerTour(final RootedTree tree) {
		tree.ensureBuild();
		this.tree = tree;
		final int n = tree.n;
		event = new int[n << 1];
		final int[] in = tree.in, out = tree.out, depth = tree.depth;
		for (final int u : tree.preorder) {
			event[(in[u] << 1) - depth[u]] = u;
			event[(out[u] << 1) - depth[u] - 1] = u;
		}
	}

	/**
	 * イベント列の長さを返す。
	 */
	public int length() {
		return event.length;
	}

	/**
	 * イベント位置 {@code i} に記録された頂点を返す。
	 */
	public int eventAt(final int i) {
		return event[i];
	}

	/**
	 * 頂点 {@code u} の入場イベント位置を返す。
	 */
	public int enter(final int u) {
		return (tree.in[u] << 1) - tree.depth[u];
	}

	/**
	 * 頂点 {@code u} の退場イベント位置を返す。
	 */
	public int exit(final int u) {
		return (tree.out[u] << 1) - tree.depth[u] - 1;
	}
}
