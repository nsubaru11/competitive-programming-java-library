package example.graph;

import java.util.*;

import lib.graph.*;

public final class BellmanFordExample {

	public static void main(final String[] args) {
		// 負辺
		DirectedGraph g = new DirectedGraph(4, 4);
		g.add(0, 1, 4);
		g.add(0, 2, 2);
		g.add(2, 1, -3);
		g.add(1, 3, 1);

		// 単一始点
		PathResult r = BellmanFord.solve(g, 0);
		System.out.println(Arrays.toString(r.dist));
		System.out.println(Arrays.toString(r.pathTo(3)));
		System.out.println(r.hasNegCycle);
		System.out.println();

		// 負閉路
		DirectedGraph h = new DirectedGraph(4, 4);
		h.add(0, 1, 1);
		h.add(1, 2, -2);
		h.add(2, 1, 1);
		h.add(3, 0, 1);
		PathResult s = BellmanFord.solve(h, 0);
		System.out.println(Arrays.toString(s.dist));
		System.out.println(s.hasNegCycle);
	}
}
