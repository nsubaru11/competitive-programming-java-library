package lib.graph.tree;

import java.util.function.*;

import lib.util.function.*;

/**
 * 根付き木をHeavy-Light Decompositionで線形化し、部分木・パスを配列区間に分解する。
 * <p>
 * 構築は {@code O(N)}。{@code enter/exit} はHLD順の部分木区間 {@code [enter(u), exit(u))}、
 * {@code vertexAt} はその逆写像を表す。LCAとパス分解は {@code O(log N)} 個のchain区間を処理する。
 */
@SuppressWarnings("unused")
public final class HLD {
	private final RootedTree tree;
	private final int[] enter, exit, top, rev;

	/**
	 * 根付き木のDFS情報を使ってHLD順を構築する。
	 */
	public HLD(final RootedTree tree) {
		tree.ensureBuild();
		this.tree = tree;
		final int n = tree.n, root = tree.root;

		enter = new int[n];
		exit = new int[n];
		top = new int[n];
		rev = new int[n];
		hld(root, root, root, 0);
	}

	/**
	 * HLD順の位置 {@code i} にある頂点を返す。
	 */
	public int vertexAt(final int i) {
		return rev[i];
	}

	/**
	 * 頂点 {@code u} のHLD順の位置を返す。
	 */
	public int enter(final int u) {
		return enter[u];
	}

	/**
	 * HLD順で頂点 {@code u} の部分木区間が終わる位置（含まない）を返す。
	 */
	public int exit(final int u) {
		return exit[u];
	}

	/**
	 * 頂点 {@code u} が属するheavy chainの先頭を返す。
	 */
	public int top(final int u) {
		return top[u];
	}

	/**
	 * 2頂点の最小共通祖先を返す。
	 */
	public int lca(int u, int v) {
		final int[] depth = tree.depth, parent = tree.parent;
		while (top[u] != top[v]) {
			if (depth[top[u]] < depth[top[v]]) {
				int tmp = u;
				u = v;
				v = tmp;
			}
			u = parent[top[u]];
		}
		return depth[u] < depth[v] ? u : v;
	}

	/**
	 * 根からの重み付き距離とLCAから2頂点間の距離を返す。
	 */
	public long distance(final int u, final int v) {
		final long[] rootDistance = tree.rootDistance;
		return rootDistance[u] + rootDistance[v] - (rootDistance[lca(u, v)] << 1);
	}

	/**
	 * 頂点 {@code u} から {@code k} 個上の祖先を返す。存在しない場合は {@code -1}。
	 */
	public int kthAncestor(int u, int k) {
		if (tree.depth[u] < k) return -1;
		while (true) {
			final int len = tree.depth[u] - tree.depth[top[u]];
			if (k <= len) return rev[enter[u] - k];
			k -= len + 1;
			u = tree.parent[top[u]];
		}
	}

	/**
	 * 頂点 {@code u} から {@code v} へ {@code k} 辺進んだ頂点を返す。パス外なら {@code -1}。
	 */
	public int jump(final int u, final int v, final int k) {
		final int[] depth = tree.depth;
		final int lcaDepth = depth[lca(u, v)];
		final int du = depth[u] - lcaDepth, dv = depth[v] - lcaDepth;
		if (k <= du) return kthAncestor(u, k);
		else if (k <= du + dv) return kthAncestor(v, du + dv - k);
		else return -1;
	}

	/**
	 * 部分木のHLD区間 {@code [enter(u), exit(u))} を処理に渡す。
	 */
	public void updateSubtree(final int u, final IntBinaryConsumer action) {
		action.accept(enter[u], exit[u]);
	}

	/**
	 * 論理辺 {@code e} を深い側の頂点位置に対応づけ、その位置を処理に渡す。
	 */
	public void updateEdge(final int e, final IntConsumer action) {
		final int[] dest = tree.dest(), depth = tree.depth;
		final int u = dest[e << 1], v = dest[e << 1 | 1];
		action.accept(enter[depth[u] > depth[v] ? u : v]);
	}

	/**
	 * 2頂点間のパス上の辺を、LCAを除くHLD区間に分けて処理に渡す。
	 */
	public void updateEdge(int u, int v, final IntBinaryConsumer action) {
		final int[] depth = tree.depth, parent = tree.parent;
		while (top[u] != top[v]) {
			if (depth[top[u]] < depth[top[v]]) {
				action.accept(enter[top[v]], enter[v] + 1);
				v = parent[top[v]];
			} else {
				action.accept(enter[top[u]], enter[u] + 1);
				u = parent[top[u]];
			}
		}
		if (enter[u] > enter[v]) {
			final int tmp = u;
			u = v;
			v = tmp;
		}
		if (enter[u] < enter[v]) action.accept(enter[u] + 1, enter[v] + 1);
	}

	/**
	 * 頂点 {@code u} のHLD位置を処理に渡す。
	 */
	public void updateNode(final int u, final IntConsumer action) {
		action.accept(enter[u]);
	}

	/**
	 * 2頂点間のパス上の頂点を、両端を含むHLD区間に分けて処理に渡す。
	 */
	public void updateNode(int u, int v, final IntBinaryConsumer action) {
		final int[] depth = tree.depth, parent = tree.parent;
		while (top[u] != top[v]) {
			if (depth[top[u]] < depth[top[v]]) {
				action.accept(enter[top[v]], enter[v] + 1);
				v = parent[top[v]];
			} else {
				action.accept(enter[top[u]], enter[u] + 1);
				u = parent[top[u]];
			}
		}
		if (enter[u] < enter[v]) action.accept(enter[u], enter[v] + 1);
		else action.accept(enter[v], enter[u] + 1);
	}

	/**
	 * 部分木のHLD区間 {@code [enter(u), exit(u))} に問い合わせる。
	 */
	public long querySubtree(final int u, final LongBinaryOperator query) {
		return query.applyAsLong(enter[u], exit[u]);
	}

	/**
	 * パス上の辺集約を返す。{@code op} は結合的かつ可換、{@code identity} はその単位元とする。
	 * 区間問い合わせ {@code query} は半開区間の集約値を返す。
	 */
	public long queryEdge(int u, int v, final long identity, final LongBinaryOperator query, final LongBinaryOperator op) {
		final int[] depth = tree.depth, parent = tree.parent;
		long res = identity;
		while (top[u] != top[v]) {
			if (depth[top[u]] < depth[top[v]]) {
				res = op.applyAsLong(res, query.applyAsLong(enter[top[v]], enter[v] + 1));
				v = parent[top[v]];
			} else {
				res = op.applyAsLong(res, query.applyAsLong(enter[top[u]], enter[u] + 1));
				u = parent[top[u]];
			}
		}
		if (enter[u] > enter[v]) {
			int tmp = u;
			u = v;
			v = tmp;
		}
		return enter[u] < enter[v] ? op.applyAsLong(res, query.applyAsLong(enter[u] + 1, enter[v] + 1)) : res;
	}

	/**
	 * パス上の頂点集約を返す。{@code op} は結合的かつ可換、{@code identity} はその単位元とする。
	 * 区間問い合わせ {@code query} は半開区間の集約値を返す。
	 */
	public long queryNode(int u, int v, final long identity, final LongBinaryOperator query, final LongBinaryOperator op) {
		final int[] depth = tree.depth, parent = tree.parent;
		long res = identity;
		while (top[u] != top[v]) {
			if (depth[top[u]] < depth[top[v]]) {
				res = op.applyAsLong(res, query.applyAsLong(enter[top[v]], enter[v] + 1));
				v = parent[top[v]];
			} else {
				res = op.applyAsLong(res, query.applyAsLong(enter[top[u]], enter[u] + 1));
				u = parent[top[u]];
			}
		}
		if (enter[u] > enter[v]) {
			final int tmp = u;
			u = v;
			v = tmp;
		}
		return op.applyAsLong(res, query.applyAsLong(enter[u], enter[v] + 1));
	}

	private void hld(final int u, final int p, final int t, final int i) {
		final int[] dest = tree.dest(), next = tree.next(), first = tree.first(), subtreeSize = tree.subtreeSize;
		top[u] = t;
		enter[u] = i;
		rev[i] = u;
		int mx = -1;
		for (int e = first[u]; e != -1; e = next[e]) {
			final int v = dest[e];
			if (v == p) continue;
			if (mx == -1 || subtreeSize[mx] < subtreeSize[v]) mx = v;
		}
		if (mx == -1) {
			exit[u] = i + 1;
			return;
		}
		hld(mx, u, t, i + 1);
		for (int e = first[u], k = i + subtreeSize[mx] + 1; e != -1; e = next[e]) {
			final int v = dest[e];
			if (v == p || v == mx) continue;
			hld(v, u, v, k);
			k += subtreeSize[v];
		}
		exit[u] = i + subtreeSize[u];
	}
}
