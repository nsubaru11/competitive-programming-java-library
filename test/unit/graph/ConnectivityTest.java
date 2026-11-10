package unit.graph;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.graph.*;

import static org.junit.jupiter.api.Assertions.*;

final class ConnectivityTest {

	@Nested
	class Search {

		@Test
		void bridgesAndArticulations() {
			// 三角形 0-1-2 に、2-3 と 3-4 がぶら下がる
			final UndirectedGraph g = new UndirectedGraph(5, 5);
			g.add(0, 1);
			g.add(1, 2);
			g.add(2, 0);
			g.add(2, 3);
			g.add(3, 4);
			final Connectivity.Result r = Connectivity.lowLink(g);
			assertFalse(r.isBridge(0));
			assertTrue(r.isBridge(3));
			assertTrue(r.isBridge(4));
			assertTrue(r.isArticulation(2));
			assertTrue(r.isArticulation(3));
			assertFalse(r.isArticulation(0));
			assertFalse(r.isArticulation(4));
		}

		@Test
		void parallelEdgesAreNotBridges() {
			final UndirectedGraph g = new UndirectedGraph(2, 2);
			g.add(0, 1);
			g.add(1, 0);
			final Connectivity.Result r = Connectivity.lowLink(g);
			assertFalse(r.isBridge(0));
			assertFalse(r.isBridge(1));
		}
	}

	@RepeatedTest(100)
	void matchesNaive(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final int n = 1 + random.nextInt(9);
		final List<Naive.Edge> edges = Naive.randomEdges(random, n, random.nextInt(14), () -> 1, false);
		final Connectivity.Result r = Connectivity.lowLink(Naive.undirected(n, edges));
		final int base = Naive.components(n, edges);
		final String at = "seed=" + seed + ", edges=" + edges;
		for (int e = 0; e < edges.size(); e++) {
			final List<Naive.Edge> rest = new ArrayList<>(edges);
			rest.remove(e);
			assertEquals(Naive.components(n, rest) > base, r.isBridge(e), at + ", e=" + e);
		}
		for (int u = 0; u < n; u++) {
			final int removed = u;
			final List<Naive.Edge> rest = edges.stream().filter(x -> x.u() != removed && x.v() != removed).toList();
			// u を除いた頂点の成分数（u 自身の分を引く）が増えれば関節点
			assertEquals(Naive.components(n, rest) - 1 > base - (isolated(u, edges) ? 1 : 0), r.isArticulation(u), at + ", u=" + u);
		}
	}

	private static boolean isolated(final int u, final List<Naive.Edge> edges) {
		return edges.stream().noneMatch(e -> e.u() == u || e.v() == u);
	}
}
