package lib.graph.tree;

import static java.util.Arrays.*;

import java.util.function.*;

@SuppressWarnings("unused")
public final class RootedTree {
	private final int[] dest, next, first, degree, depth, parent, size, in, out, top, rev;
	private final long[] cost, dist;
	private final int n, root;
	private int edgeCount = 0;
	private boolean init = false;

	public RootedTree(final int n, final int r) {
		this.n = n;
		this.root = r;
		int m = (n - 1) << 1;
		dest = new int[m];
		next = new int[m];
		first = new int[n];
		fill(first, -1);
		degree = new int[n];
		depth = new int[n];
		parent = new int[n];
		size = new int[n];
		in = new int[n];
		out = new int[n];
		top = new int[n];
		rev = new int[n];
		cost = new long[m];
		dist = new long[n];
	}

	public int n() {
		return n;
	}

	public int root() {
		return root;
	}

	public void add(final int i, final int j) {
		add(i, j, 1);
	}

	public void add(final int i, final int j, final long c) {
		dest[edgeCount] = j;
		next[edgeCount] = first[i];
		cost[edgeCount] = c;
		first[i] = edgeCount++;
		degree[i]++;

		dest[edgeCount] = i;
		next[edgeCount] = first[j];
		cost[edgeCount] = c;
		first[j] = edgeCount++;
		degree[j]++;
	}

	public void addAll(int m, final IntSupplier u, final IntSupplier v) {
		while (m-- > 0) add(u.getAsInt(), v.getAsInt());
	}

	public void addAll(int m, final IntSupplier u, final IntSupplier v, final LongSupplier cost) {
		while (m-- > 0) add(u.getAsInt(), v.getAsInt(), cost.getAsLong());
	}

	public int degree(final int i) {
		return degree[i];
	}

	public int parent(final int i) {
		if (!init) init();
		return parent[i];
	}

	public int depth(final int i) {
		if (!init) init();
		return depth[i];
	}

	public int size(final int i) {
		if (!init) init();
		return size[i];
	}

	public int lca(int u, int v) {
		if (!init) init();
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

	public long distance(final int u, final int v) {
		if (!init) init();
		final int lca = lca(u, v);
		return dist[u] + dist[v] - (dist[lca] << 1);
	}

	public int[] adj(final int u) {
		final int[] adj = new int[degree[u]];
		for (int e = first[u], i = 0; e != -1; e = next[e], i++) adj[i] = dest[e];
		return adj;
	}

	public int kthAncestor(int u, int k) {
		if (!init) init();
		if (depth[u] < k) return -1;
		while (true) {
			int len = depth[u] - depth[top[u]];
			if (k <= len) return rev[in[u] - k];
			k -= len + 1;
			u = parent[top[u]];
		}
	}

	public int jump(int u, int v, int k) {
		if (!init) init();
		int lca = lca(u, v);
		int du = depth[u] - depth[lca], dv = depth[v] - depth[lca];
		if (k <= du) return kthAncestor(u, k);
		else if (k <= du + dv) return kthAncestor(v, du + dv - k);
		else return -1;
	}

	public void updateSubtree(int u, final PathAction action) {
		if (!init) init();
		action.accept(in[u], out[u]);
	}

	public void updateEdge(int e, final IntConsumer action) {
		if (!init) init();
		final int u = dest[e << 1], v = dest[e << 1 | 1];
		action.accept(in[depth[u] > depth[v] ? u : v]);
	}

	public void updateEdge(int u, int v, final PathAction action) {
		if (!init) init();
		while (top[u] != top[v]) {
			if (depth[top[u]] < depth[top[v]]) {
				action.accept(in[top[v]], in[v] + 1);
				v = parent[top[v]];
			} else {
				action.accept(in[top[u]], in[u] + 1);
				u = parent[top[u]];
			}
		}
		if (in[u] > in[v]) {
			int tmp = u;
			u = v;
			v = tmp;
		}
		if (in[u] < in[v]) action.accept(in[u] + 1, in[v] + 1);
	}

	public void updateNode(int u, final IntConsumer action) {
		if (!init) init();
		action.accept(in[u]);
	}

	public void updateNode(int u, int v, final PathAction action) {
		if (!init) init();
		while (top[u] != top[v]) {
			if (depth[top[u]] < depth[top[v]]) {
				action.accept(in[top[v]], in[v] + 1);
				v = parent[top[v]];
			} else {
				action.accept(in[top[u]], in[u] + 1);
				u = parent[top[u]];
			}
		}
		if (in[u] < in[v]) action.accept(in[u], in[v] + 1);
		else action.accept(in[v], in[u] + 1);
	}

	public long querySubTree(int u, final LongBinaryOperator query) {
		if (!init) init();
		return query.applyAsLong(in[u], out[u]);
	}

	public long queryEdge(int u, int v, final long identity, final LongBinaryOperator query, final LongBinaryOperator op) {
		if (!init) init();
		long res = identity;
		while (top[u] != top[v]) {
			if (depth[top[u]] < depth[top[v]]) {
				res = op.applyAsLong(res, query.applyAsLong(in[top[v]], in[v] + 1));
				v = parent[top[v]];
			} else {
				res = op.applyAsLong(res, query.applyAsLong(in[top[u]], in[u] + 1));
				u = parent[top[u]];
			}
		}
		if (in[u] > in[v]) {
			int tmp = u;
			u = v;
			v = tmp;
		}
		return in[u] < in[v] ? op.applyAsLong(res, query.applyAsLong(in[u] + 1, in[v] + 1)) : res;
	}

	public long queryNode(int u, int v, final long identity, final LongBinaryOperator query, final LongBinaryOperator op) {
		if (!init) init();
		long res = identity;
		while (top[u] != top[v]) {
			if (depth[top[u]] < depth[top[v]]) {
				res = op.applyAsLong(res, query.applyAsLong(in[top[v]], in[v] + 1));
				v = parent[top[v]];
			} else {
				res = op.applyAsLong(res, query.applyAsLong(in[top[u]], in[u] + 1));
				u = parent[top[u]];
			}
		}
		if (in[u] > in[v]) {
			int tmp = u;
			u = v;
			v = tmp;
		}
		return op.applyAsLong(res, query.applyAsLong(in[u], in[v] + 1));
	}

	private void init() {
		init = true;
		parent[root] = root;
		dfs(root, root, 0, 0);
		hld(root, root, root, 0);
	}

	private int dfs(final int u, final int p, final int di, final long dc) {
		int s = 0;
		for (int e = first[u]; e != -1; e = next[e]) {
			final int v = dest[e];
			if (v == p) continue;
			s += dfs(v, parent[v] = u, depth[v] = di + 1, dist[v] = dc + cost[e]);
		}
		return size[u] = s + 1;
	}

	private void hld(final int u, final int p, final int t, final int i) {
		top[u] = t;
		in[u] = i;
		rev[i] = u;
		int mx = -1;
		for (int e = first[u]; e != -1; e = next[e]) {
			final int v = dest[e];
			if (v == p) continue;
			if (mx == -1 || size[mx] < size[v]) mx = v;
		}
		if (mx == -1) {
			out[u] = i + 1;
			return;
		}
		hld(mx, u, t, i + 1);
		for (int e = first[u], k = i + size[mx] + 1; e != -1; e = next[e]) {
			final int v = dest[e];
			if (v == p || v == mx) continue;
			hld(v, u, v, k);
			k += size[v];
		}
		out[u] = i + size[u];
	}

	public interface PathAction {
		void accept(final int l, final int r);
	}

}
