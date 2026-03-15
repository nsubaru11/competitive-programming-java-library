import java.util.*;

import static java.lang.Math.*;
import static java.util.Arrays.*;

/**
 * 無向辺・自己ループを含まない有向連結グラフ管理用ライブラリ
 * <p>
 * 辺は追加順に0始まりの辺IDが割り当てられます。
 * {@link #adjEdgeIds(int)} で取得した辺IDは {@link #to(int)} と {@link #cost(int)} にそのまま渡せます。
 */
@SuppressWarnings("unused")
public final class DirectedGraph {
	private final int[] dest, next, first, inDegree, outDegree;
	private final long[] cost;
	private final int n;
	private int edgeCount = 0;

	public DirectedGraph(final int n, final int m) {
		this.n = n;
		dest = new int[m];
		next = new int[m];
		first = new int[n];
		fill(first, -1);
		inDegree = new int[n];
		outDegree = new int[n];
		cost = new long[m];
	}

	public void add(final int i, final int j) {
		add(i, j, 1);
	}

	public void add(final int i, final int j, long c) {
		dest[edgeCount] = j;
		next[edgeCount] = first[i];
		cost[edgeCount] = c;
		first[i] = edgeCount++;
		outDegree[i]++;
		inDegree[j]++;
	}

	public int degree(final int i) {
		return inDegree[i] + outDegree[i];
	}

	public int inDegree(final int i) {
		return inDegree[i];
	}

	public int outDegree(final int i) {
		return outDegree[i];
	}

	public long cost(final int e) {
		return cost[e];
	}

	public int to(final int e) {
		return dest[e];
	}

	public int[] topologicalSort() {
		int[] degree = new int[n];
		System.arraycopy(inDegree, 0, degree, 0, n);
		int[] q = new int[n];
		int head = 0, tail = 0;
		for (int i = 0; i < n; i++) {
			if (degree[i] == 0) q[tail++] = i;
		}
		int[] res = new int[n];
		int idx = 0;
		while (head < tail) {
			int u = q[head++];
			res[idx++] = u;
			for (int e = first[u]; e != -1; e = next[e]) {
				int v = dest[e];
				degree[v]--;
				if (degree[v] == 0) q[tail++] = v;
			}
		}
		return idx == n ? res : null;
	}

	public boolean hasCycle() {
		int count = 0;
		int[] degree = new int[n];
		System.arraycopy(inDegree, 0, degree, 0, n);
		int[] q = new int[n];
		int head = 0, tail = 0;
		for (int i = 0; i < n; i++) {
			if (degree[i] == 0) q[tail++] = i;
		}
		while (head < tail) {
			int u = q[head++];
			count++;
			for (int e = first[u]; e != -1; e = next[e]) {
				int v = dest[e];
				if (--degree[v] == 0) q[tail++] = v;
			}
		}
		return count < n;
	}

	/**
	 * 強連結成分分解（SCC）を行います。
	 *
	 * @return 2次元配列 {@code sccs}。
	 * {@code sccs[i]} が i 番目の強連結成分に属する頂点配列を表します。
	 * 各頂点はちょうど1回だけいずれかの {@code sccs[i]} に現れます。
	 * 成分同士の並びは縮約グラフのトポロジカル順です。
	 * すなわち、異なる成分 {@code a -> b} への辺が存在するなら、返却配列では {@code a} の方が前に現れます。
	 * 各成分内の頂点順はDFSの探索順（辺の追加順と頂点番号順）に依存します。
	 */
	public int[][] scc() {
		int[] ord = new int[n];
		int[] low = new int[n];

		int[] edgeIter = new int[n];
		System.arraycopy(first, 0, edgeIter, 0, n);

		int[] stack = new int[n];
		int stackPtr = 0;

		int[] sccStack = new int[n];
		int sccPtr = 0;
		boolean[] onSccStack = new boolean[n];

		int timer = 1;

		int[] sccList = new int[n];
		int listPtr = 0;
		int[] sep = new int[n + 1];
		int sepPtr = 0;
		sep[sepPtr++] = 0;
		for (int i = 0; i < n; i++) if (ord[i] == 0) {
			stack[stackPtr++] = i;
			outer:
			while (stackPtr > 0) {
				int u = stack[stackPtr - 1];
				if (ord[u] == 0) {
					ord[u] = low[u] = timer++;
					sccStack[sccPtr++] = u;
					onSccStack[u] = true;
				}
				while (edgeIter[u] != -1) {
					int e = edgeIter[u];
					int v = dest[e];
					edgeIter[u] = next[e];
					if (ord[v] == 0) {
						stack[stackPtr++] = v;
						continue outer;
					} else if (onSccStack[v]) {
						low[u] = min(low[u], ord[v]);
					}
				}
				if (stackPtr > 1) {
					int p = stack[stackPtr - 2];
					low[p] = min(low[p], low[u]);
				}
				if (low[u] == ord[u]) {
					while (true) {
						int v = sccStack[--sccPtr];
						onSccStack[v] = false;
						sccList[listPtr++] = v;
						if (u == v) break;
					}
					sep[sepPtr++] = listPtr;
				}
				stackPtr--;
			}
		}
		int groupCount = sepPtr - 1;
		int[][] result = new int[groupCount][];
		for (int i = 0, end = sep[groupCount]; i < groupCount; i++) {
			int start = sep[groupCount - i - 1];
			int len = end - start;
			int[] grp = new int[len];
			System.arraycopy(sccList, start, grp, 0, len);
			result[i] = grp;
			end = start;
		}
		return result;
	}

	public int[] adj(final int u) {
		int[] adj = new int[outDegree[u]];
		for (int e = first[u], i = 0; e != -1; e = next[e], i++) {
			adj[i] = dest[e];
		}
		return adj;
	}

	public int[] adjEdgeIds(final int u) {
		int[] ids = new int[outDegree[u]];
		for (int e = first[u], i = 0; e != -1; e = next[e], i++) {
			ids[i] = e;
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
