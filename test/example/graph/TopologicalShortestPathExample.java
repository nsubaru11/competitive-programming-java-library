package example.graph;

import java.util.*;

import lib.graph.*;

public final class TopologicalShortestPathExample {

	public static void main(final String[] args) {
		// 負辺を含む DAG
		DirectedGraph g = new DirectedGraph(5, 6);
		g.add(0, 1, 2);
		g.add(0, 2, 6);
		g.add(1, 2, -3);
		g.add(1, 3, 4);
		g.add(2, 3, 1);
		g.add(3, 4, 2);

		// 最短経路
		System.out.println(Arrays.toString(TopologicalShortestPath.dist(g, 0)));
		System.out.println(Arrays.toString(TopologicalShortestPath.path(g, 0, 4)));
		System.out.println();

		// 最長経路
		System.out.println(Arrays.toString(TopologicalShortestPath.distLongest(g, 0)));
		System.out.println(Arrays.toString(TopologicalShortestPath.pathLongest(g, 0, 4)));
		System.out.println();

		// 2点間
		System.out.println(TopologicalShortestPath.dist(g, 1, 4));
		System.out.println(TopologicalShortestPath.distLongest(g, 1, 4));
	}
}
