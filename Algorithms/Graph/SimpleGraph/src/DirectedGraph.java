import static java.lang.Math.*;
import static java.util.Arrays.*;

import java.util.function.*;

/**
 * 無向辺・自己ループを含まない有向グラフ管理用ライブラリ
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

	public void add(final int i, final int j, final long c) {
		dest[edgeCount] = j;
		next[edgeCount] = first[i];
		cost[edgeCount] = c;
		first[i] = edgeCount++;
		outDegree[i]++;
		inDegree[j]++;
	}

	public void addAll(int m, final IntSupplier u, final IntSupplier v) {
		while (m-- > 0) add(u.getAsInt(), v.getAsInt());
	}

	public void addAll(int m, final IntSupplier u, final IntSupplier v, final LongSupplier cost) {
		while (m-- > 0) add(u.getAsInt(), v.getAsInt(), cost.getAsLong());
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
		final int[] degree = new int[n];
		System.arraycopy(inDegree, 0, degree, 0, n);
		final int[] q = new int[n];
		int tail = 0;
		for (int i = 0; i < n; i++) if (degree[i] == 0) q[tail++] = i;
		for (int head = 0; head < tail; head++) {
			final int u = q[head];
			for (int e = first[u]; e != -1; e = next[e]) {
				final int v = dest[e];
				if (--degree[v] == 0) q[tail++] = v;
			}
		}
		return tail == n ? q : null;
	}

	public boolean hasCycle() {
		final int[] degree = new int[n];
		System.arraycopy(inDegree, 0, degree, 0, n);
		final int[] q = new int[n];
		int tail = 0;
		for (int i = 0; i < n; i++) if (degree[i] == 0) q[tail++] = i;
		for (int head = 0; head < tail; head++) {
			final int u = q[head];
			for (int e = first[u]; e != -1; e = next[e]) {
				final int v = dest[e];
				if (--degree[v] == 0) q[tail++] = v;
			}
		}
		return tail < n;
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
		final int[] ord = new int[n];
		final int[] low = new int[n];
		final int[] edgeIter = new int[n];
		System.arraycopy(first, 0, edgeIter, 0, n);
		final int[] stack = new int[n];
		final int[] sccStack = new int[n];
		final boolean[] onSccStack = new boolean[n];
		final int[] sccList = new int[n];
		final int[] sep = new int[n + 1];
		int sepPtr = 1;
		for (int i = 0, stackPtr = 0, sccPtr = 0, timer = 1, listPtr = 0; i < n; i++)
			if (ord[i] == 0) {
			stack[stackPtr++] = i;
			outer:
			while (stackPtr > 0) {
				final int u = stack[stackPtr - 1];
				if (ord[u] == 0) {
					ord[u] = low[u] = timer++;
					sccStack[sccPtr++] = u;
					onSccStack[u] = true;
				}
				while (edgeIter[u] != -1) {
					final int e = edgeIter[u];
					final int v = dest[e];
					edgeIter[u] = next[e];
					if (ord[v] == 0) {
						stack[stackPtr++] = v;
						continue outer;
					} else if (onSccStack[v]) {
						low[u] = min(low[u], ord[v]);
					}
				}
				if (stackPtr > 1) {
					final int p = stack[stackPtr - 2];
					low[p] = min(low[p], low[u]);
				}
				if (low[u] == ord[u]) {
					while (true) {
						final int v = sccStack[--sccPtr];
						onSccStack[v] = false;
						sccList[listPtr++] = v;
						if (u == v) break;
					}
					sep[sepPtr++] = listPtr;
				}
				stackPtr--;
			}
		}
		final int groupCount = sepPtr - 1;
		final int[][] result = new int[groupCount][];
		for (int i = 0, end = sep[groupCount]; i < groupCount; i++) {
			final int start = sep[groupCount - i - 1];
			final int len = end - start;
			final int[] grp = new int[len];
			System.arraycopy(sccList, start, grp, 0, len);
			result[i] = grp;
			end = start;
		}
		return result;
	}

	public int[] adj(final int u) {
		final int[] adj = new int[outDegree[u]];
		for (int e = first[u], i = 0; e != -1; e = next[e], i++) {
			adj[i] = dest[e];
		}
		return adj;
	}

	public int[] adjEdgeIds(final int u) {
		final int[] ids = new int[outDegree[u]];
		for (int e = first[u], i = 0; e != -1; e = next[e], i++) {
			ids[i] = e;
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
