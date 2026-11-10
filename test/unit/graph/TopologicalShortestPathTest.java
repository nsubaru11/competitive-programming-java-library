package unit.graph;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.graph.*;

import static org.junit.jupiter.api.Assertions.*;

final class TopologicalShortestPathTest {

	// 0 -> 1 (2), 0 -> 2 (5), 1 -> 2 (-4), 2 -> 3 (1)、4 は到達不能
	private static DirectedGraph sample() {
		final DirectedGraph g = new DirectedGraph(5, 4);
		g.add(0, 1, 2);
		g.add(0, 2, 5);
		g.add(1, 2, -4);
		g.add(2, 3, 1);
		return g;
	}

	@RepeatedTest(100)
	void matchesNaive(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final int n = 1 + random.nextInt(8);
		final List<Naive.Edge> edges = Naive.randomDagEdges(random, n, random.nextInt(14), () -> random.nextInt(-5, 10));
		final DirectedGraph g = Naive.directed(n, edges);
		final int s = random.nextInt(n), t = random.nextInt(n);
		final String at = "seed=" + seed + ", edges=" + edges + ", s=" + s + ", t=" + t;
		final long[] shortest = Naive.shortest(n, edges, s), longest = Naive.longest(n, edges, s);
		assertArrayEquals(shortest, TopologicalShortestPath.dist(g, s), at);
		assertArrayEquals(longest, TopologicalShortestPath.distLongest(g, s), at);
		if (shortest[t] != Long.MAX_VALUE) {
			assertTrue(Naive.isPath(TopologicalShortestPath.path(g, s, t), edges, s, t, shortest[t], false), at);
			assertTrue(Naive.isPath(TopologicalShortestPath.pathLongest(g, s, t), edges, s, t, longest[t], true), at);
		}
	}

	@Nested
	class Aggregation {

		@Test
		void shortestWithNegativeEdges() {
			final DirectedGraph g = sample();
			final PathResult r = TopologicalShortestPath.solve(g, 0);
			assertArrayEquals(new long[]{0, 2, -2, -1, Long.MAX_VALUE}, r.dist);
			assertArrayEquals(new int[]{0, 1, 2, 3}, r.pathTo(3));
			assertArrayEquals(new long[]{0, 2, -2, -1, Long.MAX_VALUE}, TopologicalShortestPath.dist(g, 0));
			assertEquals(-1L, TopologicalShortestPath.dist(g, 0, 3));
			assertArrayEquals(new int[]{0, 1, 2, 3}, TopologicalShortestPath.path(g, 0, 3));
			assertNull(TopologicalShortestPath.path(g, 0, 4));
		}

		@Test
		void longest() {
			final DirectedGraph g = sample();
			final PathResult r = TopologicalShortestPath.solveLongest(g, 0);
			assertArrayEquals(new long[]{0, 2, 5, 6, Long.MAX_VALUE}, r.dist);
			assertArrayEquals(new long[]{0, 2, 5, 6, Long.MAX_VALUE}, TopologicalShortestPath.distLongest(g, 0));
			assertEquals(6L, TopologicalShortestPath.distLongest(g, 0, 3));
			assertArrayEquals(new int[]{0, 2, 3}, TopologicalShortestPath.pathLongest(g, 0, 3));
		}

		@Test
		void multipleSources() {
			final DirectedGraph g = sample();
			assertArrayEquals(new long[]{0, 2, -2, -1, 0}, TopologicalShortestPath.dist(g, new int[]{0, 4}));
			assertArrayEquals(new long[]{Long.MAX_VALUE, 0, -4, -3, Long.MAX_VALUE}, TopologicalShortestPath.solve(g, new int[]{1}).dist);
			assertArrayEquals(new long[]{0, 2, 5, 6, 0}, TopologicalShortestPath.distLongest(g, new int[]{0, 4}));
			assertArrayEquals(new long[]{0, 2, 5, 6, 0}, TopologicalShortestPath.solveLongest(g, 0, 4).dist);
		}

		@Test
		void cycleThrows() {
			final DirectedGraph g = new DirectedGraph(2, 2);
			g.add(0, 1, 1);
			g.add(1, 0, 1);
			assertThrows(IllegalArgumentException.class, () -> TopologicalShortestPath.solve(g, 0));
			assertThrows(IllegalArgumentException.class, () -> TopologicalShortestPath.solveLongest(g, 0));
		}
	}
}
