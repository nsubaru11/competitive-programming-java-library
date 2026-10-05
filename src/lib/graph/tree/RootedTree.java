package lib.graph.tree;

import static java.util.Arrays.*;

import java.util.function.*;

import lib.util.function.*;

/**
 * 根を固定した木のDFS情報を保持する。
 * <p>
 * 最初の情報参照時に {@code O(N)} で前処理する。{@code in/out} は通常のDFS preorder上の部分木区間
 * {@code [in(u), out(u))} を表す。全辺を追加してから使用し、前処理後は木を変更しないこと。
 */
@SuppressWarnings("unused")
public final class RootedTree extends Tree {
	public final int root;
	int[] depth, parent, subtreeSize, preorder, in, out;
	long[] rootDistance;
	private boolean init = false;

	/**
	 * {@code n} 頂点の木を根 {@code r} で根付き木として扱う。
	 */
	public RootedTree(final int n, final int r) {
		super(n);
		this.root = r;
	}

	/**
	 * 頂点 {@code u} の親を返す。根の親は根自身。
	 */
	public int parent(final int u) {
		ensureBuild();
		return parent[u];
	}

	/**
	 * 頂点 {@code u} の深さを返す。根の深さは {@code 0}。
	 */
	public int depth(final int u) {
		ensureBuild();
		return depth[u];
	}

	/**
	 * 頂点 {@code u} の部分木サイズを返す。
	 */
	public int subtreeSize(final int u) {
		ensureBuild();
		return subtreeSize[u];
	}

	/**
	 * preorderの位置 {@code i} にある頂点を返す。
	 */
	public int preorderAt(final int i) {
		ensureBuild();
		return preorder[i];
	}

	/**
	 * preorder上の頂点 {@code u} の位置を返す。
	 */
	public int in(final int u) {
		ensureBuild();
		return in[u];
	}

	/**
	 * preorder上で頂点 {@code u} の部分木区間が終わる位置（含まない）を返す。
	 */
	public int out(final int u) {
		ensureBuild();
		return out[u];
	}

	/**
	 * 根から頂点 {@code u} までの重み付き距離を返す。
	 */
	public long rootDistance(final int u) {
		ensureBuild();
		return rootDistance[u];
	}

	/**
	 * {@code u} が {@code v} の祖先であるかを判定する。{@code u == v} の場合は {@code true}。
	 */
	public boolean isAncestor(final int u, final int v) {
		ensureBuild();
		return in[u] <= in[v] && in[v] < out[u];
	}

	/**
	 * 部分木をpreorder区間 {@code [in(u), out(u))} として処理に渡す。
	 */
	public void updateSubtree(final int u, final IntBinaryConsumer action) {
		ensureBuild();
		action.accept(in[u], out[u]);
	}

	/**
	 * 部分木のpreorder区間 {@code [in(u), out(u))} に問い合わせを行う。
	 */
	public long querySubtree(final int u, final LongBinaryOperator query) {
		ensureBuild();
		return query.applyAsLong(in[u], out[u]);
	}

	void ensureBuild() {
		if (init) return;
		depth = new int[n];
		parent = new int[n];
		subtreeSize = new int[n];
		in = new int[n];
		out = new int[n];
		preorder = new int[n];
		rootDistance = new long[n];
		dfs();
		init = true;
	}

	private void dfs() {
		int[] stack = new int[n], edgeIter = first.clone();
		stack[0] = root;
		fill(in, -1);
		fill(parent, -1);
		fill(subtreeSize, 1);
		parent[root] = root;
		outer:
		for (int len = 1, counter = 0; len > 0; ) {
			final int u = stack[len - 1];
			if (in[u] == -1) preorder[in[u] = counter++] = u;
			while (edgeIter[u] != -1) {
				final int e = edgeIter[u], v = dest[e];
				edgeIter[u] = next[e];
				if (v == parent[u]) continue;
				parent[v] = u;
				depth[v] = depth[u] + 1;
				rootDistance[v] = rootDistance[u] + cost[e];
				stack[len++] = v;
				continue outer;
			}
			out[u] = counter;
			len--;
		}
		for (int i = n - 1; i > 0; i--) {
			final int u = preorder[i];
			subtreeSize[parent[u]] += subtreeSize[u];
		}
	}
}
