package lib.graph.tree;

import static java.util.Arrays.*;

import java.util.function.*;

@SuppressWarnings("unused")
public final class Tree {
	private final int[] dest, next, first, degree;
	private final long[] cost;
	private final int n;
	private int edgeCount = 0;

	public Tree(final int n) {
		this.n = n;
		int m = (n - 1) << 1;
		dest = new int[m];
		next = new int[m];
		first = new int[n];
		fill(first, -1);
		degree = new int[n];
		cost = new long[m];
	}

	public int n() {
		return n;
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
		for (int e = first[u], i = 0; e != -1; e = next[e], i++) adj[i] = dest[e];
		return adj;
	}

	public int[] adjEdgeIds(final int u) {
		final int[] ids = new int[degree[u]];
		for (int e = first[u], i = 0; e != -1; e = next[e], i++) {
			ids[i] = e >> 1;
		}
		return ids;
	}

	public int diameter() {
		final int[] qV = new int[n], qL = new int[n], qF = new int[n];
		for (int head = 0, tail = 1; tail < n; head++) {
			final int u = qV[head], nl = qL[head] + 1, from = qF[head];
			for (int e = first[u]; e != -1; e = next[e]) {
				final int v = dest[e];
				if (from == v) continue;
				qV[tail] = v;
				qL[tail] = nl;
				qF[tail++] = u;
			}
		}
		qV[0] = qV[n - 1];
		qL[0] = 0;
		qF[0] = -1;
		for (int head = 0, tail = 1; tail < n; head++) {
			final int u = qV[head], nl = qL[head] + 1, from = qF[head];
			for (int e = first[u]; e != -1; e = next[e]) {
				final int v = dest[e];
				if (from == v) continue;
				qV[tail] = v;
				qL[tail] = nl;
				qF[tail++] = u;
			}
		}
		return qL[n - 1];
	}

	public long diameterCost() {
		final int[] qV = new int[n], qF = new int[n];
		final long[] qL = new long[n];
		int mx = 0;
		for (int head = 0, tail = 1; tail < n; head++) {
			final int u = qV[head], from = qF[head];
			final long l = qL[head];
			for (int e = first[u]; e != -1; e = next[e]) {
				final int v = dest[e];
				if (from == v) continue;
				qV[tail] = v;
				qL[tail] = l + cost[e];
				if (qL[tail] > qL[mx]) mx = tail;
				qF[tail++] = u;
			}
		}
		qV[0] = qV[mx];
		qL[0] = 0;
		qF[0] = -1;
		mx = 0;
		for (int head = 0, tail = 1; tail < n; head++) {
			final int u = qV[head], from = qF[head];
			final long l = qL[head];
			for (int e = first[u]; e != -1; e = next[e]) {
				final int v = dest[e];
				if (from == v) continue;
				qV[tail] = v;
				qL[tail] = l + cost[e];
				if (qL[tail] > qL[mx]) mx = tail;
				qF[tail++] = u;
			}
		}
		return qL[mx];
	}

}
