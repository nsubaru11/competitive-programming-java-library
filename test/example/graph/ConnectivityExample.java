package example.graph;

import lib.graph.*;

public final class ConnectivityExample {

	public static void main(final String[] args) {
		// 三角形 0-1-2 と、橋でつながる 2-3-4
		UndirectedGraph g = new UndirectedGraph(5, 5);
		g.add(0, 1, 1);
		g.add(1, 2, 1);
		g.add(2, 0, 1);
		g.add(2, 3, 1);
		g.add(3, 4, 1);

		// 橋・関節点の検出
		Connectivity.Result r = Connectivity.lowLink(g);

		// 橋
		for (int e = 0; e < g.edgeCount(); e++) {
			System.out.println(e + " " + r.isBridge(e));
		}
		System.out.println();

		// 関節点
		for (int u = 0; u < g.n; u++) {
			System.out.println(u + " " + r.isArticulation(u));
		}
	}
}
