package unit.graph;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.graph.*;

import static org.junit.jupiter.api.Assertions.*;

final class WarshallFloydTest {

	@RepeatedTest(100)
	void matchesNaive(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final int n = 1 + random.nextInt(7);
		final boolean undirected = random.nextBoolean();
		final List<Naive.Edge> edges = Naive.randomEdges(random, n, random.nextInt(14), () -> undirected ? random.nextInt(10) : random.nextInt(-3, 10), !undirected);
		final Graph g = undirected ? Naive.undirected(n, edges) : Naive.directed(n, edges);
		final List<Naive.Edge> arcs = Naive.arcs(edges, undirected);
		final WarshallFloyd.Result r = WarshallFloyd.solve(g);
		boolean neg = false;
		for (int u = 0; u < n; u++) {
			final long[] expected = Naive.shortest(n, arcs, u);
			neg |= expected[u] == Long.MIN_VALUE;
			assertArrayEquals(expected, r.dist[u], "seed=" + seed + ", edges=" + edges + ", u=" + u);
		}
		assertEquals(neg, r.hasNegCycle, "seed=" + seed);
	}

	@Nested
	class Aggregation {

		@Test
		void allPairs() {
			final DirectedGraph g = new DirectedGraph(3, 3);
			g.add(0, 1, 4);
			g.add(1, 2, -1);
			g.add(0, 2, 5);
			final WarshallFloyd.Result r = WarshallFloyd.solve(g);
			assertFalse(r.hasNegCycle);
			assertEquals(3L, r.dist(0, 2));
			assertEquals(0L, r.dist(1, 1));
			assertEquals(Long.MAX_VALUE, r.dist(2, 0));
			assertFalse(r.reachable(2, 0));
			assertTrue(r.reachable(0, 2));
			assertEquals(3L, r.dist[0][2]);
		}

		@Test
		void negativeCycle() {
			final DirectedGraph g = new DirectedGraph(3, 3);
			g.add(0, 1, 1);
			g.add(1, 0, -2);
			g.add(1, 2, 1);
			final WarshallFloyd.Result r = WarshallFloyd.solve(g);
			assertTrue(r.hasNegCycle);
			assertEquals(Long.MIN_VALUE, r.dist(0, 2));
			assertTrue(r.reachable(0, 2));
			assertEquals(Long.MAX_VALUE, r.dist(2, 0));
		}
	}
}
