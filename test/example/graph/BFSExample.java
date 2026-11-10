package example.graph;

import java.util.*;

import lib.graph.*;

public final class BFSExample {

	public static void main(final String[] args) {
		// グラフ
		UndirectedGraph g = new UndirectedGraph(6, 6);
		g.add(0, 1, 1);
		g.add(1, 2, 1);
		g.add(2, 3, 1);
		g.add(0, 4, 1);
		g.add(4, 3, 1);
		g.add(3, 5, 1);

		// 単一始点
		PathResult r = BFS.solve(g, 0);
		System.out.println(Arrays.toString(r.dist));
		System.out.println(Arrays.toString(r.pathTo(5)));
		System.out.println();

		// 2点間
		System.out.println(BFS.dist(g, 1, 5));
		System.out.println(Arrays.toString(BFS.path(g, 1, 5)));
		System.out.println();

		// 複数始点
		System.out.println(Arrays.toString(BFS.dist(g, new int[]{0, 5})));
	}
}
