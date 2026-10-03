package lib.graph.tree;

import lib.graph.*;

/**
 * 連結な木を保持し、木固有の基本処理を提供する。
 * <p>
 * 直径計算は {@code O(N)}。{@link #diameterCost()} は辺重みが非負であることを前提とする。
 */
@SuppressWarnings("unused")
public class Tree extends UndirectedGraph {

	/**
	 * {@code n} 頂点、{@code n - 1} 辺の木を保持する領域を確保する。
	 */
	public Tree(final int n) {
		super(n, n - 1);
	}

	/**
	 * 辺数を距離とする木の直径を返す。
	 */
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

	/**
	 * 非負の辺重みを持つ木の重み付き直径を返す。
	 */
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

	int[] dest() {
		return dest;
	}

	int[] next() {
		return next;
	}

	int[] first() {
		return first;
	}

	long[] cost() {
		return cost;
	}
}
