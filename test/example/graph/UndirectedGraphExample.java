package example.graph;

import java.util.*;

import lib.graph.*;

public final class UndirectedGraphExample {

	public static void main(final String[] args) {
		// 初期化
		UndirectedGraph g = new UndirectedGraph(4, 4);
		g.add(0, 1, 3);
		g.add(0, 2, 1);
		g.add(1, 2, 4);
		g.add(2, 3, 2);
		System.out.println(g.edgeCount());
		System.out.println();

		// 次数
		for (int u = 0; u < g.n; u++) {
			System.out.println(g.degree(u));
		}
		System.out.println();

		// 隣接頂点
		System.out.println(Arrays.toString(g.adj(2)));
		System.out.println();

		// 辺の重み
		for (int e : g.adjEdgeIds(2)) {
			System.out.println(g.to(2, e) + " " + g.cost(e));
		}
	}
}
