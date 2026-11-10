package example.graph;

import lib.graph.*;

public final class KruskalExample {

	public static void main(final String[] args) {
		// グラフ
		UndirectedGraph g = new UndirectedGraph(5, 8);
		g.add(0, 1, 3);
		g.add(0, 4, 1);
		g.add(1, 4, 4);
		g.add(2, 3, 2);
		g.add(3, 4, 7);
		g.add(1, 2, 5);
		g.add(2, 4, 6);
		g.add(3, 1, 2);

		// 最小全域木
		System.out.println(Kruskal.minimum(g));
		System.out.println();

		// 最大全域木
		System.out.println(Kruskal.maximum(g));
		System.out.println();

		// コストのみ
		System.out.println(Kruskal.minimumCost(g));
		System.out.println(Kruskal.maximumCost(g));
	}
}
