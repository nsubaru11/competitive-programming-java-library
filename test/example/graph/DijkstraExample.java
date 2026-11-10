package example.graph;

import java.util.*;

import lib.graph.*;

public final class DijkstraExample {

	public static void main(final String[] args) {
		// グラフ
		DirectedGraph g = new DirectedGraph(5, 6);
		g.add(0, 1, 7);
		g.add(0, 2, 2);
		g.add(2, 1, 1);
		g.add(1, 4, 9);
		g.add(2, 4, 10);
		g.add(3, 0, 7);

		// 単一始点
		PathResult r = Dijkstra.solve(g, 0);
		System.out.println(r.distTo(4));
		System.out.println(Arrays.toString(r.pathTo(4)));
		System.out.println(r.reachable(3));
		System.out.println();

		// 距離の配列
		System.out.println(Arrays.toString(Dijkstra.dist(g, 0)));
		System.out.println();

		// 2点間
		System.out.println(Dijkstra.dist(g, 0, 1));
		System.out.println(Arrays.toString(Dijkstra.path(g, 0, 1)));
	}
}
