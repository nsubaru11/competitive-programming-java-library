package unit.graph.tree;

import java.util.*;

import lib.graph.tree.*;

/**
 * graph.tree のテストで使うランダムな木と愚直解。辺 i は頂点 i + 1 と親候補 p[i] を結ぶ。
 */
final class Naive {

	final int n, root;
	final int[] eu, ev;
	final long[] ec;
	final List<List<int[]>> adj = new ArrayList<>();
	final int[] parent, depth, parentEdge;
	final long[] dist;

	/**
	 * n 頂点のランダムな木を作る。頂点番号はランダムに並べ替え、辺の重みは [0, maxCost] とする。
	 */
	Naive(final Random random, final int n, final int root, final int maxCost) {
		this.n = n;
		this.root = root;
		final int[] perm = new int[n];
		for (int i = 0; i < n; i++) perm[i] = i;
		for (int i = n - 1; i > 0; i--) {
			final int j = random.nextInt(i + 1), t = perm[i];
			perm[i] = perm[j];
			perm[j] = t;
		}
		eu = new int[n - 1];
		ev = new int[n - 1];
		ec = new long[n - 1];
		for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
		for (int i = 1; i < n; i++) {
			final int p = random.nextInt(i);
			// 辺の向きもランダムにする
			final boolean flip = random.nextBoolean();
			eu[i - 1] = perm[flip ? i : p];
			ev[i - 1] = perm[flip ? p : i];
			ec[i - 1] = random.nextInt(maxCost + 1);
			adj.get(eu[i - 1]).add(new int[]{ev[i - 1], i - 1});
			adj.get(ev[i - 1]).add(new int[]{eu[i - 1], i - 1});
		}
		parent = new int[n];
		depth = new int[n];
		parentEdge = new int[n];
		dist = new long[n];
		parent[root] = root;
		parentEdge[root] = -1;
		final Deque<Integer> stack = new ArrayDeque<>(List.of(root));
		final boolean[] seen = new boolean[n];
		seen[root] = true;
		while (!stack.isEmpty()) {
			final int u = stack.pop();
			for (final int[] e : adj.get(u)) {
				if (seen[e[0]]) continue;
				seen[e[0]] = true;
				parent[e[0]] = u;
				parentEdge[e[0]] = e[1];
				depth[e[0]] = depth[u] + 1;
				dist[e[0]] = dist[u] + ec[e[1]];
				stack.push(e[0]);
			}
		}
	}

	RootedTree rooted() {
		final RootedTree t = new RootedTree(n, root);
		for (int i = 0; i < n - 1; i++) t.add(eu[i], ev[i], ec[i]);
		return t;
	}

	Tree tree() {
		final Tree t = new Tree(n);
		for (int i = 0; i < n - 1; i++) t.add(eu[i], ev[i], ec[i]);
		return t;
	}

	boolean isAncestor(final int u, int v) {
		while (v != u && v != root) v = parent[v];
		return v == u;
	}

	int lca(int u, int v) {
		while (depth[u] > depth[v]) u = parent[u];
		while (depth[v] > depth[u]) v = parent[v];
		while (u != v) {
			u = parent[u];
			v = parent[v];
		}
		return u;
	}

	long distance(final int u, final int v) {
		return dist[u] + dist[v] - 2 * dist[lca(u, v)];
	}

	/**
	 * u から v へのパス上の頂点を順に返す。
	 */
	List<Integer> path(int u, int v) {
		final int w = lca(u, v);
		final List<Integer> left = new ArrayList<>(), right = new ArrayList<>();
		for (; u != w; u = parent[u]) left.add(u);
		for (; v != w; v = parent[v]) right.add(v);
		left.add(w);
		Collections.reverse(right);
		left.addAll(right);
		return left;
	}

	int subtreeSize(final int u) {
		int s = 0;
		for (int v = 0; v < n; v++) if (isAncestor(u, v)) s++;
		return s;
	}
}
