package lib.graph;

import static java.util.Arrays.*;

import java.util.function.*;

/**
 * 自己ループを含まない無向グラフ管理用ライブラリ
 * <p>
 * 無向辺は追加順に0始まりの辺IDが割り当てられます。
 * 内部表現では1本の無向辺を2本の内部辺として保持しますが、
 * 外部には無向辺IDとして公開します。
 * <p>
 * {@link #adjEdgeIds(int)} で取得した辺IDは {@link #to(int, int)} と
 * {@link #cost(int)} にそのまま渡せます。
 * {@link #to(int, int)} は「頂点 {@code u} から見た接続先頂点」を返します。
 */
@SuppressWarnings("unused")
public final class UndirectedGraph {
	private final int[] dest, next, first, degree;
	private final long[] cost;
	private final int n;
	private int edgeCount = 0;

	public UndirectedGraph(final int n, final int m) {
		this.n = n;
		final int m2 = m << 1;
		dest = new int[m2];
		next = new int[m2];
		first = new int[n];
		fill(first, -1);
		degree = new int[n];
		cost = new long[m2];
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

	public boolean isBipartite() {
		final int[] color = new int[n];
		final int[] q = new int[n];
		int tail = 0;
		for (int s = 0; s < n; s++) {
			if (color[s] != 0) continue;
			color[s] = 1;
			q[tail++] = s;
			for (int head = tail - 1; head < tail; head++) {
				final int u = q[head];
				for (int e = first[u]; e != -1; e = next[e]) {
					final int v = dest[e];
					if (color[v] == color[u]) return false;
					if (color[v] != 0) continue;
					color[v] = -color[u];
					q[tail++] = v;
				}
			}
		}
		return true;
	}

	public int to(final int u, final int e) {
		final int v1 = dest[e << 1];
		final int v2 = dest[e << 1 | 1];
		return u != v1 ? v1 : v2;
	}

	public long cost(final int e) {
		return cost[e << 1];
	}

	public int[] adj(final int u) {
		final int[] adj = new int[degree[u]];
		for (int e = first[u], i = 0; e != -1; e = next[e], i++) {
			adj[i] = dest[e];
		}
		return adj;
	}

	public int[] adjEdgeIds(final int u) {
		final int[] ids = new int[degree[u]];
		for (int e = first[u], i = 0; e != -1; e = next[e], i++) {
			ids[i] = e >> 1;
		}
		return ids;
	}

	public int[] bfs(final int s) {
		final boolean[] visited = new boolean[n];
		visited[s] = true;
		final int[] bfs = new int[n];
		fill(bfs, 1, n, -1);
		bfs[0] = s;
		for (int head = 0, tail = 1; head < tail; head++) {
			final int u = bfs[head];
			for (int e = first[u]; e != -1; e = next[e]) {
				final int v = dest[e];
				if (visited[v]) continue;
				bfs[tail++] = v;
				visited[v] = true;
			}
		}
		return bfs;
	}

	public int[] bfs(final int... s) {
		final boolean[] visited = new boolean[n];
		final int[] bfs = new int[n];
		int tail = 0;
		for (final int si : s) {
			bfs[tail++] = si;
			visited[si] = true;
		}
		fill(bfs, tail, n, -1);
		for (int head = 0; head < tail; head++) {
			final int u = bfs[head];
			for (int e = first[u]; e != -1; e = next[e]) {
				final int v = dest[e];
				if (visited[v]) continue;
				bfs[tail++] = v;
				visited[v] = true;
			}
		}
		return bfs;
	}

	public int[] dist(final int s) {
		final int[] dist = new int[n];
		fill(dist, -1);
		dist[s] = 0;
		final int[] q = new int[n];
		q[0] = s;
		for (int head = 0, tail = 1; head < tail; head++) {
			final int u = q[head];
			for (int e = first[u]; e != -1; e = next[e]) {
				final int v = dest[e];
				if (dist[v] != -1) continue;
				dist[v] = dist[u] + 1;
				q[tail++] = v;
			}
		}
		return dist;
	}

	public int[] dist(final int... s) {
		final int[] dist = new int[n];
		fill(dist, -1);
		final int[] q = new int[n];
		int tail = 0;
		for (final int s1 : s) {
			dist[s1] = 0;
			q[tail++] = s1;
		}
		for (int head = 0; head < tail; head++) {
			final int u = q[head];
			for (int e = first[u]; e != -1; e = next[e]) {
				final int v = dest[e];
				if (dist[v] != -1) continue;
				dist[v] = dist[u] + 1;
				q[tail++] = v;
			}
		}
		return dist;
	}

}
