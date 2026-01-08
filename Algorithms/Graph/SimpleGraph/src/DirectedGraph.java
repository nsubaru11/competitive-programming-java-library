import java.util.*;

import static java.lang.Math.*;
import static java.util.Arrays.*;

/**
 * 無向辺・自己ループを含まない有向連結グラフ管理用ライブラリ
 */
@SuppressWarnings("unused")
public final class DirectedGraph {
	// -------------- フィールド --------------
	private final int[] dest, next, first, inDegree, outDegree;
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
	}

	public void add(final int i, final int j) {
		dest[edgeCount] = j;
		next[edgeCount] = first[i];
		first[i] = edgeCount++;
		outDegree[i]++;
		inDegree[j]++;
	}

	public int getDegree(final int i) {
		return inDegree[i] + outDegree[i];
	}

	public int getInDegree(final int i) {
		return inDegree[i];
	}

	public int getOutDegree(final int i) {
		return outDegree[i];
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
