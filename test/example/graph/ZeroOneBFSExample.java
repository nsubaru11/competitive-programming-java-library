package example.graph;

import java.util.*;

import lib.graph.*;

public final class ZeroOneBFSExample {

	public static void main(final String[] args) {
		// 重み0・1のグラフ
		DirectedGraph g = new DirectedGraph(5, 6);
		g.add(0, 1, 1);
		g.add(0, 2, 0);
		g.add(2, 1, 0);
		g.add(1, 3, 1);
		g.add(2, 3, 1);
		g.add(3, 4, 0);

		// 単一始点
		PathResult r = ZeroOneBFS.solve(g, 0);
		System.out.println(Arrays.toString(r.dist));
		System.out.println(Arrays.toString(r.pathTo(4)));
		System.out.println();

		// 2点間
		System.out.println(ZeroOneBFS.dist(g, 0, 1));
		System.out.println(Arrays.toString(ZeroOneBFS.path(g, 0, 1)));
		System.out.println();

		// 複数始点
		System.out.println(Arrays.toString(ZeroOneBFS.dist(g, new int[]{1, 2})));
	}
}
