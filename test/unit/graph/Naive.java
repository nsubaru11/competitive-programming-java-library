package unit.graph;

import java.util.*;
import java.util.function.*;

import lib.graph.*;

/**
 * graph のテストで使う辺リストと愚直解。
 */
final class Naive {

	static final long INF = Long.MAX_VALUE, NINF = Long.MIN_VALUE;

	private Naive() {
	}

	/**
	 * n 頂点・m 本の辺をランダムに作る。cost が辺の重みを返す。selfLoop が false なら自己ループを作らない。
	 */
	static List<Edge> randomEdges(final Random random, final int n, final int m, final LongSupplier cost, final boolean selfLoop) {
		final List<Edge> edges = new ArrayList<>();
		// 1頂点で自己ループを禁止すると作れる辺がない
		if (!selfLoop && n < 2) return edges;
		while (edges.size() < m) {
			final int u = random.nextInt(n), v = random.nextInt(n);
			if (!selfLoop && u == v) continue;
			edges.add(new Edge(u, v, cost.getAsLong()));
		}
		return edges;
	}

	/**
	 * u < v となる辺だけからなる DAG の辺をランダムに作る。
	 */
	static List<Edge> randomDagEdges(final Random random, final int n, final int m, final LongSupplier cost) {
		final List<Edge> edges = new ArrayList<>();
		if (n < 2) return edges;
		while (edges.size() < m) {
			final int u = random.nextInt(n), v = random.nextInt(n);
			if (u < v) edges.add(new Edge(u, v, cost.getAsLong()));
		}
		return edges;
	}

	static DirectedGraph directed(final int n, final List<Edge> edges) {
		final DirectedGraph g = new DirectedGraph(n, edges.size());
		for (final Edge e : edges) g.add(e.u, e.v, e.c);
		return g;
	}

	static UndirectedGraph undirected(final int n, final List<Edge> edges) {
		final UndirectedGraph g = new UndirectedGraph(n, edges.size());
		for (final Edge e : edges) g.add(e.u, e.v, e.c);
		return g;
	}

	/**
	 * 有向として扱う辺リスト。無向グラフは両向きの辺に展開する。
	 */
	static List<Edge> arcs(final List<Edge> edges, final boolean undirected) {
		if (!undirected) return edges;
		final List<Edge> res = new ArrayList<>();
		for (final Edge e : edges) {
			res.add(e);
			res.add(new Edge(e.v, e.u, e.c));
		}
		return res;
	}

	/**
	 * 多始点最短距離。到達不能は INF、到達可能な負閉路の影響下は NINF。
	 */
	static long[] shortest(final int n, final List<Edge> arcs, final int... s) {
		final long[] d = new long[n];
		Arrays.fill(d, INF);
		for (final int v : s) d[v] = 0;
		for (int k = 0; k < n; k++) {
			for (final Edge e : arcs) {
				if (d[e.u] != INF && d[e.u] + e.c < d[e.v]) d[e.v] = d[e.u] + e.c;
			}
		}
		final boolean[] neg = new boolean[n];
		for (final Edge e : arcs) if (d[e.u] != INF && d[e.u] + e.c < d[e.v]) neg[e.v] = true;
		for (int k = 0; k < n; k++) for (final Edge e : arcs) if (neg[e.u]) neg[e.v] = true;
		for (int v = 0; v < n; v++) if (neg[v]) d[v] = NINF;
		return d;
	}

	/**
	 * DAG の多始点最長距離。到達不能は INF。
	 */
	static long[] longest(final int n, final List<Edge> arcs, final int... s) {
		final long[] d = new long[n];
		Arrays.fill(d, INF);
		for (final int v : s) d[v] = 0;
		for (int k = 0; k < n; k++) {
			for (final Edge e : arcs) {
				if (d[e.u] != INF && (d[e.v] == INF || d[e.u] + e.c > d[e.v])) d[e.v] = d[e.u] + e.c;
			}
		}
		return d;
	}

	/**
	 * path が s から始まり g で終わり、各区間に辺があり、重みの合計が expected になることを確かめる。
	 * 重みは、隣り合う頂点を結ぶ辺のうち最も小さい（maximize なら大きい）ものを使う。
	 */
	static boolean isPath(final int[] path, final List<Edge> arcs, final int s, final int g, final long expected, final boolean maximize) {
		if (path == null || path.length == 0 || path[0] != s) return false;
		if (path[path.length - 1] != g) return false;
		long total = 0;
		for (int i = 0; i + 1 < path.length; i++) {
			final int a = path[i], b = path[i + 1];
			final OptionalLong w = maximize
					? arcs.stream().filter(e -> e.u == a && e.v == b).mapToLong(Edge::c).max()
					: arcs.stream().filter(e -> e.u == a && e.v == b).mapToLong(Edge::c).min();
			if (w.isEmpty()) return false;
			total += w.getAsLong();
		}
		return total == expected;
	}

	/**
	 * 連結成分の数。
	 */
	static int components(final int n, final List<Edge> edges) {
		final int[] label = new int[n];
		for (int i = 0; i < n; i++) label[i] = i;
		for (final Edge e : edges) {
			final int a = label[e.u], b = label[e.v];
			if (a == b) continue;
			for (int i = 0; i < n; i++) if (label[i] == b) label[i] = a;
		}
		return (int) Arrays.stream(label).distinct().count();
	}

	/**
	 * 到達可能性の推移閉包。
	 */
	static boolean[][] reach(final int n, final List<Edge> arcs) {
		final boolean[][] r = new boolean[n][n];
		for (int i = 0; i < n; i++) r[i][i] = true;
		for (final Edge e : arcs) r[e.u][e.v] = true;
		for (int k = 0; k < n; k++)
			for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) r[i][j] |= r[i][k] && r[k][j];
		return r;
	}

	record Edge(int u, int v, long c) {
	}
}
