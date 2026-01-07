import java.util.*;

import static java.util.Arrays.*;

/**
 * 自己ループを含まない連結無向グラフ管理用ライブラリ
 */
@SuppressWarnings("unused")
public final class UndirectedGraph {
	// -------------- フィールド --------------
	private static final long INF = Long.MAX_VALUE;
	private final int[] dest, next, first, degree;
	private final int n, m;
	private int edgeCount = 0;

	public UndirectedGraph(final int n, final int m) {
		this.n = n;
		int m2 = m * 2;
		this.m = m2;
		dest = new int[m2];
		next = new int[m2];
		first = new int[n];
		fill(first, -1);
		degree = new int[n];
	}

	public void add(final int i, final int j) {
		dest[edgeCount] = j;
		next[edgeCount] = first[i];
		first[i] = edgeCount++;
		degree[i]++;

		dest[edgeCount] = i;
		next[edgeCount] = first[j];
		first[j] = edgeCount++;
		degree[j]++;
	}

	public int getDegree(final int i) {
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
				color[v] = -color[u];
				q[tail++] = v;
			}
		}
		return true;
	}

	public Iterable<Integer> adj(final int u) {
		return () -> new PrimitiveIterator.OfInt() {
			private int e = first[u];

			@Override
			public boolean hasNext() {
				return e != -1;
			}

			@Override
			public int nextInt() {
				int v = dest[e];
				e = next[e];
				return v;
			}
		};
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
