package verify.graph.connectivity;

import lib.graph.*;

public final class Test1 {

	public static void main(final String[] args) {
		testTriangleWithBridges();
		testParallelEdges();
	}

	private static void testTriangleWithBridges() {
		final UndirectedGraph graph = new UndirectedGraph(5, 5);
		graph.add(0, 1);
		graph.add(1, 2);
		graph.add(2, 0);
		graph.add(1, 3);
		graph.add(3, 4);

		final Connectivity.Result result = Connectivity.lowLink(graph);
		for (int e = 0; e < 3; e++) check(!result.isBridge(e));
		check(result.isBridge(3));
		check(result.isBridge(4));
		check(!result.isArticulation(0));
		check(result.isArticulation(1));
		check(!result.isArticulation(2));
		check(result.isArticulation(3));
		check(!result.isArticulation(4));
		for (int u = 0; u < graph.n; u++) check(result.ord[u] > 0 && result.low[u] <= result.ord[u]);
	}

	private static void testParallelEdges() {
		final UndirectedGraph graph = new UndirectedGraph(3, 3);
		graph.add(0, 1);
		graph.add(0, 1);
		graph.add(1, 2);

		final Connectivity.Result result = Connectivity.lowLink(graph);
		check(!result.isBridge(0));
		check(!result.isBridge(1));
		check(result.isBridge(2));
		check(result.isArticulation(1));
	}

	private static void check(final boolean condition) {
		if (!condition) throw new AssertionError();
	}
}
