import java.util.*;

import static java.util.Arrays.*;

/**
 * 自己ループを含まない連結無向グラフ管理用ライブラリ
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
		int m2 = m * 2;
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

	public int degree(final int i) {
		return degree[i];
	}

	public boolean isBipartite() {
		int[] color = new int[n];
		int[] q = new int[n];
		int head = 0, tail = 0;
		color[0] = 1;
		q[tail++] = 0;
		while (head < tail) {
			int u = q[head++];
			for (int e = first[u]; e != -1; e = next[e]) {
				int v = dest[e];
				if (color[v] == color[u]) return false;
				if (color[v] != 0) continue;
				color[v] = -color[u];
				q[tail++] = v;
			}
		}
		return true;
	}

	public int to(final int u, final int e) {
		int v1 = dest[e << 1];
		int v2 = dest[e << 1 | 1];
		return u != v1 ? v1 : v2;
	}

	public long cost(final int e) {
		return cost[e << 1];
	}

	public int[] adj(final int u) {
		int[] adj = new int[degree[u]];
		for (int e = first[u], i = 0; e != -1; e = next[e], i++) {
			adj[i] = dest[e];
		}
		return adj;
	}

	public int[] adjEdgeIds(final int u) {
		int[] ids = new int[degree[u]];
		for (int e = first[u], i = 0; e != -1; e = next[e], i++) {
			ids[i] = e >> 1;
		}
		return ids;
	}

	public Iterable<Integer> bfs(final int s) {
		return () -> new PrimitiveIterator.OfInt() {
			private final int[] q = new int[n];
			private final boolean[] visited = new boolean[n];
			private int head, tail;

			{
				q[tail++] = s;
				visited[s] = true;
			}

			@Override
			public boolean hasNext() {
				return head < tail;
			}

			@Override
			public int nextInt() {
				int u = q[head++];
				for (int e = first[u]; e != -1; e = next[e]) {
					int v = dest[e];
					if (visited[v]) continue;
					q[tail++] = v;
					visited[v] = true;
				}
				return u;
			}
		};
	}

	public Iterable<Integer> bfs(final int... s) {
		return () -> new PrimitiveIterator.OfInt() {
			private final int[] q = new int[n];
			private final boolean[] visited = new boolean[n];
			private int head, tail;

			{
				for (int s1 : s) {
					q[tail++] = s1;
					visited[s1] = true;
				}
			}

			@Override
			public boolean hasNext() {
				return head < tail;
			}

			@Override
			public int nextInt() {
				int u = q[head++];
				for (int e = first[u]; e != -1; e = next[e]) {
					int v = dest[e];
					if (visited[v]) continue;
					q[tail++] = v;
					visited[v] = true;
				}
				return u;
			}
		};
	}

	public int[] dist(final int s) {
		int[] dist = new int[n];
		fill(dist, -1);
		dist[s] = 0;
		int[] q = new int[n];
		int head = 0, tail = 0;
		q[tail++] = s;
		while (head < tail) {
			int u = q[head++];
			for (int e = first[u]; e != -1; e = next[e]) {
				int v = dest[e];
				if (dist[v] != -1) continue;
				dist[v] = dist[u] + 1;
				q[tail++] = v;
			}
		}
		return dist;
	}

	public int[] dist(final int... s) {
		int[] dist = new int[n];
		fill(dist, -1);
		int[] q = new int[n];
		int head = 0, tail = 0;
		for (int s1 : s) {
			dist[s1] = 0;
			q[tail++] = s1;
		}
		while (head < tail) {
			int u = q[head++];
			for (int e = first[u]; e != -1; e = next[e]) {
				int v = dest[e];
				if (dist[v] != -1) continue;
				dist[v] = dist[u] + 1;
				q[tail++] = v;
			}
		}
		return dist;
	}

}
