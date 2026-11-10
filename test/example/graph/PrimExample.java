package example.graph;

import lib.graph.*;

public final class PrimExample {

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
		System.out.println(Prim.minimum(g));
		System.out.println();

		// 最大全域木
		System.out.println(Prim.maximum(g));
		System.out.println();

		// コストのみ
		System.out.println(Prim.minimumCost(g));
		System.out.println(Prim.maximumCost(g));
	}
}
