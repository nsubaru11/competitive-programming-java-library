package unit.graph;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.graph.*;

import static org.junit.jupiter.api.Assertions.*;

final class UndirectedGraphTest {

	@Nested
	class Updates {

		@Test
		void eachEdgeHasOneId() {
			final UndirectedGraph g = new UndirectedGraph(3, 2);
			g.add(0, 1, 4);
			g.add(2, 1);
			assertEquals(2, g.edgeCount());
			assertEquals(4L, g.cost(0));
			assertEquals(1L, g.cost(1));
			assertEquals(1, g.to(0, 0));
			assertEquals(0, g.to(1, 0), "反対側の端点");
			assertEquals(2, g.degree(1));
			assertEquals(1, g.degree(2));
		}

		@Test
		void setAllAndClear() {
			final UndirectedGraph g = new UndirectedGraph(3, 1);
			final Iterator<Integer> it = List.of(0, 2).iterator();
			g.setAll(it::next, it::next, () -> 3L);
			assertEquals(1, g.edgeCount());
			assertEquals(3L, g.cost(0));
			g.clear();
			assertEquals(0, g.degree(0));
			assertEquals(0, g.edgeCount());
		}
	}

	@Nested
	class Conversion {

		@Test
		void adjacencyIncludesBothDirections() {
			final UndirectedGraph g = new UndirectedGraph(3, 2);
			g.add(0, 1);
			g.add(1, 2);
			assertEquals(Set.of(0, 2), GraphUtilsTest.set(g.adj(1)));
			assertEquals(Set.of(0, 1), GraphUtilsTest.set(g.adjEdgeIds(1)), "論理辺IDを返す");
			for (final int e : g.adjEdgeIds(1)) assertNotEquals(1, g.to(1, e));
		}
	}
}
