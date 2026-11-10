package example.graph;

import java.util.*;

import lib.graph.*;

public final class DirectedGraphExample {

	public static void main(final String[] args) {
		// 初期化
		DirectedGraph g = new DirectedGraph(4, 5);
		g.add(0, 1, 3);
		g.add(0, 2, 1);
		g.add(1, 2, 4);
		g.add(2, 3, 2);
		g.add(3, 0, 5);
		System.out.println(g.edgeCount());
		System.out.println();

		// 次数
		for (int u = 0; u < g.n; u++) {
			System.out.println(g.inDegree(u) + " " + g.outDegree(u));
		}
		System.out.println();

		// 隣接頂点
		System.out.println(Arrays.toString(g.adj(0)));
		System.out.println();

		// 辺の行き先と重み
		for (int e : g.adjEdgeIds(0)) {
			System.out.println(g.to(e) + " " + g.cost(e));
		}
	}
}
