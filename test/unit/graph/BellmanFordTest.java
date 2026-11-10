package unit.graph;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.graph.*;

import static org.junit.jupiter.api.Assertions.*;

final class BellmanFordTest extends ShortestPathContract {

	Solver solver() {
		return of(BellmanFord::solve, BellmanFord::solve, BellmanFord::dist, BellmanFord::dist, BellmanFord::dist, BellmanFord::path);
	}

	// 負辺と負閉路を含む有向グラフで照合する
	long weight(final Random random) {
		return random.nextInt(-3, 10);
	}

	boolean supportsUndirected() {
		return false;
	}

	@Test
	void negativeCycleAffectsReachableVertices() {
		// 0 -> 1 -> 2 -> 1（負閉路）、2 -> 3、4 は 0 から到達不能
		final DirectedGraph g = new DirectedGraph(5, 4);
		g.add(0, 1, 1);
		g.add(1, 2, -3);
		g.add(2, 1, 1);
		g.add(2, 3, 1);
		final PathResult r = BellmanFord.solve(g, 0);
		assertTrue(r.hasNegCycle);
		assertEquals(0L, r.distTo(0));
		assertEquals(Long.MIN_VALUE, r.distTo(3));
		assertTrue(r.reachable(3), "負閉路の影響下でも到達可能");
		assertEquals(-1, r.parent(3));
		assertNull(r.pathTo(3));
		assertEquals(Long.MAX_VALUE, r.distTo(4));
		assertNull(BellmanFord.path(g, 0, 2));
	}

	@Test
	void unreachableNegativeCycleDoesNotMatter() {
		final DirectedGraph g = new DirectedGraph(3, 2);
		g.add(1, 2, -1);
		g.add(2, 1, -1);
		final PathResult r = BellmanFord.solve(g, 0);
		assertFalse(r.hasNegCycle);
		assertFalse(r.reachable(1));
	}
}
