package example.graph;

import java.util.*;

import lib.graph.*;

public final class GraphUtilsExample {

	public static void main(final String[] args) {
		// 二部グラフ判定・BFS の訪問順
		UndirectedGraph u = new UndirectedGraph(4, 4);
		u.add(0, 1, 1);
		u.add(1, 2, 1);
		u.add(2, 3, 1);
		u.add(3, 0, 1);
		System.out.println(GraphUtils.isBipartite(u));
		System.out.println(Arrays.toString(GraphUtils.bfs(u, 0)));
		System.out.println();

		// トポロジカルソート
		DirectedGraph dag = new DirectedGraph(4, 4);
		dag.add(2, 0, 1);
		dag.add(0, 1, 1);
		dag.add(2, 3, 1);
		dag.add(3, 1, 1);
		System.out.println(Arrays.toString(GraphUtils.topologicalSort(dag)));
		System.out.println(GraphUtils.hasCycle(dag));
		System.out.println();

		// 閉路
		DirectedGraph g = new DirectedGraph(5, 5);
		g.add(0, 1, 1);
		g.add(1, 2, 1);
		g.add(2, 0, 1);
		g.add(2, 3, 1);
		g.add(3, 4, 1);
		System.out.println(Arrays.toString(GraphUtils.findCycle(g).vertices()));
		System.out.println();

		// 強連結成分
		System.out.println(Arrays.deepToString(GraphUtils.scc(g)));
	}
}
