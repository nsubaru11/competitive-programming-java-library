package unit.graph;

import org.junit.jupiter.api.*;

import lib.graph.*;

import static org.junit.jupiter.api.Assertions.*;

final class ConnectivityTest {

	// 三角形 0-1-2 に、1-3-4 の道が付いたグラフ
	@Test
	void triangleWithBridges() {
		final UndirectedGraph graph = new UndirectedGraph(5, 5);
		graph.add(0, 1);
		graph.add(1, 2);
		graph.add(2, 0);
		graph.add(1, 3);
		graph.add(3, 4);

		final Connectivity.Result result = Connectivity.lowLink(graph);
		for (int e = 0; e < 3; e++) assertFalse(result.isBridge(e), "edge=" + e);
		assertTrue(result.isBridge(3));
		assertTrue(result.isBridge(4));
		assertFalse(result.isArticulation(0));
		assertTrue(result.isArticulation(1));
		assertFalse(result.isArticulation(2));
		assertTrue(result.isArticulation(3));
		assertFalse(result.isArticulation(4));
		for (int u = 0; u < graph.n; u++) {
			assertTrue(result.ord[u] > 0, "u=" + u);
			assertTrue(result.low[u] <= result.ord[u], "u=" + u);
		}
	}

	// 多重辺は橋にならない
	@Test
	void parallelEdgesAreNotBridges() {
		final UndirectedGraph graph = new UndirectedGraph(3, 3);
		graph.add(0, 1);
		graph.add(0, 1);
		graph.add(1, 2);

		final Connectivity.Result result = Connectivity.lowLink(graph);
		assertFalse(result.isBridge(0));
		assertFalse(result.isBridge(1));
		assertTrue(result.isBridge(2));
		assertTrue(result.isArticulation(1));
	}
}
