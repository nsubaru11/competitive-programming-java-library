package example.graph;

import lib.graph.*;

public final class WarshallFloydExample {

	public static void main(final String[] args) {
		// グラフ
		DirectedGraph g = new DirectedGraph(4, 5);
		g.add(0, 1, 3);
		g.add(1, 2, 1);
		g.add(0, 2, 5);
		g.add(2, 3, 2);
		g.add(3, 1, 4);

		// 全点対
		WarshallFloyd.Result r = WarshallFloyd.solve(g);
		for (int u = 0; u < g.n; u++) {
			StringBuilder sb = new StringBuilder();
			for (int v = 0; v < g.n; v++) {
				sb.append(r.reachable(u, v) ? r.dist(u, v) : "INF").append(' ');
			}
			System.out.println(sb.toString().trim());
		}
		System.out.println();

		// 到達判定
		System.out.println(r.reachable(0, 3));
		System.out.println(r.reachable(3, 0));
	}
}
