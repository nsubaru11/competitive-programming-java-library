package lib.graph;

import static java.lang.Math.*;
import static java.util.Arrays.*;

/**
 * グラフに対する基本的な探索・判定アルゴリズムを提供するユーティリティクラス。
 */
public final class GraphUtils {

	private GraphUtils() {
	}

	/**
	 * 無向グラフが二部グラフかを判定します。
	 *
	 * @param graph 判定する無向グラフ
	 * @return 二部グラフなら {@code true}
	 */
	public static boolean isBipartite(final UndirectedGraph graph) {
		int n = graph.n;
		int[] first = graph.first, next = graph.next, dest = graph.dest;
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

	/**
	 * 始点 {@code s} から幅優先探索し、頂点の訪問順を返します。
	 * 到達不能な頂点に対応する末尾要素は {@code -1} です。
	 *
	 * @param graph 探索対象のグラフ
	 * @param s     始点
	 * @return 長さ {@code graph.n} の訪問順配列
	 */
	public static int[] bfs(final Graph graph, final int s) {
		int n = graph.n;
		int[] first = graph.first, next = graph.next, dest = graph.dest;
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

	/**
	 * 複数の始点から幅優先探索し、頂点の訪問順を返します。
	 * 始点は互いに異なることを前提とします。
	 * 到達不能な頂点に対応する末尾要素は {@code -1} です。
	 *
	 * @param graph 探索対象のグラフ
	 * @param s     始点列
	 * @return 長さ {@code graph.n} の訪問順配列
	 */
	public static int[] bfs(final Graph graph, final int... s) {
		int n = graph.n;
		int[] first = graph.first, next = graph.next, dest = graph.dest;
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

	/**
	 * 重みを無視し、始点 {@code s} から各頂点までに通る最小辺数を返します。
	 *
	 * @param graph 探索対象のグラフ
	 * @param s     始点
	 * @return 最小辺数の配列。到達不能な頂点は {@code -1}
	 */
	public static int[] dist(final Graph graph, final int s) {
		int n = graph.n;
		int[] first = graph.first, next = graph.next, dest = graph.dest;
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

	/**
	 * 重みを無視し、複数の始点のいずれかから各頂点までに通る最小辺数を返します。
	 * 始点は互いに異なることを前提とします。
	 *
	 * @param graph 探索対象のグラフ
	 * @param s     始点列
	 * @return 最小辺数の配列。到達不能な頂点は {@code -1}
	 */
	public static int[] dist(final Graph graph, final int... s) {
		int n = graph.n;
		int[] first = graph.first, next = graph.next, dest = graph.dest;
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

	/**
	 * 有向グラフをトポロジカルソートします。
	 *
	 * @param graph 対象の有向グラフ
	 * @return トポロジカル順。閉路が存在する場合は {@code null}
	 */
	public static int[] topologicalSort(final DirectedGraph graph) {
		int n = graph.n;
		int[] first = graph.first, next = graph.next, dest = graph.dest, inDegree = graph.inDegree;
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

	/**
	 * 有向グラフに閉路が存在するかを判定します。
	 *
	 * @param graph 対象の有向グラフ
	 * @return 閉路が存在する場合は {@code true}
	 */
	public static boolean hasCycle(final DirectedGraph graph) {
		int n = graph.n;
		int[] first = graph.first, next = graph.next, dest = graph.dest, inDegree = graph.inDegree;
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
	 * @param graph 対象の有向グラフ
	 * @return 2次元配列 {@code sccs}。
	 * {@code sccs[i]} が i 番目の強連結成分に属する頂点配列を表します。
	 * 各頂点はちょうど1回だけいずれかの {@code sccs[i]} に現れます。
	 * 成分同士の並びは縮約グラフのトポロジカル順です。
	 * すなわち、異なる成分 {@code a -> b} への辺が存在するなら、返却配列では {@code a} の方が前に現れます。
	 * 各成分内の頂点順はDFSの探索順（辺の追加順と頂点番号順）に依存します。
	 */
	public static int[][] scc(final DirectedGraph graph) {
		int n = graph.n;
		int[] first = graph.first, next = graph.next, dest = graph.dest;
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

}
