package unit.graph;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.graph.*;

import static org.junit.jupiter.api.Assertions.*;

final class DirectedGraphTest {

	static Set<Integer> toSet(final int[] a) {
		return GraphUtilsTest.set(a);
	}

	@Nested
	class Construction {

		@Test
		void sizes() {
			final DirectedGraph g = new DirectedGraph(3, 2);
			assertEquals(3, g.n);
			assertEquals(2, g.m);
			assertEquals(0, g.edgeCount());
		}
	}

	@Nested
	class Updates {

		@Test
		void addAssignsIdsInOrder() {
			final DirectedGraph g = new DirectedGraph(3, 3);
			g.add(0, 1);
			g.add(0, 2, 5);
			g.add(2, 2, -1);
			assertEquals(3, g.edgeCount());
			assertEquals(1L, g.cost(0), "重みを省略すると1");
			assertEquals(5L, g.cost(1));
			assertEquals(2, g.to(1));
			assertEquals(2, g.outDegree(0));
			assertEquals(0, g.inDegree(0));
			assertEquals(2, g.inDegree(2), "自己ループは入次数にも数える");
			assertEquals(1, g.outDegree(2));
			assertEquals(3, g.degree(2));
		}

		@Test
		void setAllReadsMEdgesAndClearReplaces() {
			final DirectedGraph g = new DirectedGraph(3, 2);
			g.add(1, 0);
			final Iterator<Integer> it = List.of(0, 1, 1, 2).iterator();
			g.setAll(it::next, it::next);
			assertEquals(2, g.edgeCount());
			assertEquals(0, g.inDegree(0), "setAll は既存の辺を消す");
			assertEquals(Set.of(1), toSet(g.adj(0)));
			final Iterator<Integer> it2 = List.of(2, 0, 0, 1).iterator();
			final Iterator<Long> cost = List.of(7L, 8L).iterator();
			g.setAll(it2::next, it2::next, cost::next);
			assertEquals(7L, g.cost(0));
			assertEquals(0, g.to(0));
			g.clear();
			assertEquals(0, g.edgeCount());
			assertEquals(0, g.degree(0));
			g.clear();
			assertEquals(0, g.adj(2).length);
		}

		@Test
		void addAllAppends() {
			final DirectedGraph g = new DirectedGraph(2, 4);
			g.add(0, 1);
			final Iterator<Integer> it = List.of(1, 0).iterator();
			g.addAll(1, it::next, it::next);
			final Iterator<Integer> it2 = List.of(1, 1).iterator();
			g.addAll(1, it2::next, it2::next, () -> 9L);
			assertEquals(3, g.edgeCount());
			assertEquals(9L, g.cost(2));
		}
	}

	@Nested
	class Conversion {

		@Test
		void adjacencyUsesOutgoingEdges() {
			final DirectedGraph g = new DirectedGraph(3, 3);
			g.add(0, 1);
			g.add(0, 2);
			g.add(1, 0);
			assertEquals(Set.of(1, 2), toSet(g.adj(0)));
			assertEquals(Set.of(0, 1), toSet(g.adjEdgeIds(0)));
			for (final int e : g.adjEdgeIds(0)) assertTrue(g.to(e) == 1 || g.to(e) == 2);
			assertEquals(0, g.adj(2).length);
		}
	}
}
