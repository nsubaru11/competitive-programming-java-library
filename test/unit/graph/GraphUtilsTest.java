package unit.graph;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.graph.*;

import static org.junit.jupiter.api.Assertions.*;

final class GraphUtilsTest {

	@Nested
	class Search {

		@Test
		void bfsVisitsReachableVerticesInDistanceOrder() {
			final DirectedGraph g = new DirectedGraph(5, 3);
			g.add(0, 1);
			g.add(1, 2);
			g.add(0, 3);
			final int[] order = GraphUtils.bfs(g, 0);
			assertEquals(0, order[0]);
			assertEquals(Set.of(0, 1, 2, 3), set(order));
			assertEquals(2, order[3], "距離2の頂点は最後");
			assertArrayEquals(new int[]{4}, GraphUtils.bfs(g, 4));
		}

		@Test
		void bfsFromMultipleSourcesSkipsDuplicates() {
			final DirectedGraph g = new DirectedGraph(4, 1);
			g.add(2, 3);
			final int[] order = GraphUtils.bfs(g, 2, 0, 2);
			assertArrayEquals(new int[]{2, 0, 3}, order);
		}

		@Test
		void bipartite() {
			final UndirectedGraph even = new UndirectedGraph(4, 4), odd = new UndirectedGraph(3, 3);
			for (int i = 0; i < 4; i++) even.add(i, (i + 1) % 4);
			for (int i = 0; i < 3; i++) odd.add(i, (i + 1) % 3);
			assertTrue(GraphUtils.isBipartite(even));
			assertFalse(GraphUtils.isBipartite(odd));
			assertTrue(GraphUtils.isBipartite(new UndirectedGraph(3, 0)));
		}

		@Test
		void topologicalSortAndDirectedCycle() {
			final DirectedGraph dag = new DirectedGraph(3, 2);
			dag.add(2, 0);
			dag.add(0, 1);
			assertArrayEquals(new int[]{2, 0, 1}, GraphUtils.topologicalSort(dag));
			assertFalse(GraphUtils.hasCycle(dag));
			assertNull(GraphUtils.findCycle(dag));
			final DirectedGraph cyc = new DirectedGraph(3, 3);
			cyc.add(0, 1);
			cyc.add(1, 2);
			cyc.add(2, 1);
			assertNull(GraphUtils.topologicalSort(cyc));
			assertTrue(GraphUtils.hasCycle(cyc));
			final GraphUtils.Cycle c = GraphUtils.findCycle(cyc);
			assertEquals(2, c.size());
			assertEquals(Set.of(1, 2), set(c.vertices()));
			assertEquals(Set.of(1, 2), set(c.edges()));
		}

		@Test
		void undirectedCycle() {
			final UndirectedGraph tree = new UndirectedGraph(3, 2);
			tree.add(0, 1);
			tree.add(1, 2);
			assertFalse(GraphUtils.hasCycle(tree));
			assertNull(GraphUtils.findCycle(tree));
			final UndirectedGraph multi = new UndirectedGraph(2, 2);
			multi.add(0, 1);
			multi.add(0, 1);
			assertTrue(GraphUtils.hasCycle(multi), "多重辺は長さ2の閉路");
			final GraphUtils.Cycle c = GraphUtils.findCycle(multi);
			assertEquals(2, c.size());
			assertEquals(Set.of(0, 1), set(c.edges()));
		}

		@Test
		void sccInTopologicalOrder() {
			final DirectedGraph g = new DirectedGraph(5, 5);
			g.add(0, 1);
			g.add(1, 0);
			g.add(1, 2);
			g.add(2, 3);
			g.add(3, 2);
			final int[][] sccs = GraphUtils.scc(g);
			assertEquals(3, sccs.length);
			final List<Set<Integer>> comps = Arrays.stream(sccs).map(GraphUtilsTest::set).toList();
			assertTrue(comps.indexOf(Set.of(0, 1)) < comps.indexOf(Set.of(2, 3)));
			assertTrue(comps.contains(Set.of(4)));
		}
	}

	@RepeatedTest(100)
	void directedMatchesNaive(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final int n = 1 + random.nextInt(8);
		final List<Naive.Edge> edges = random.nextBoolean()
			? Naive.randomDagEdges(random, n, random.nextInt(12), () -> 1)
			: Naive.randomEdges(random, n, random.nextInt(12), () -> 1, true);
		final DirectedGraph g = Naive.directed(n, edges);
		final boolean[][] reach = Naive.reach(n, edges);
		final String at = "seed=" + seed + ", edges=" + edges;
		final boolean cyclic = edges.stream().anyMatch(e -> reach[e.v()][e.u()]);
		assertEquals(cyclic, GraphUtils.hasCycle(g), at);
		final int[] topo = GraphUtils.topologicalSort(g);
		if (cyclic) {
			assertNull(topo, at);
			final GraphUtils.Cycle c = GraphUtils.findCycle(g);
			assertNotNull(c, at);
			assertEquals(c.size(), set(c.vertices()).size(), at + ", 頂点は重複しない");
			for (int i = 0; i < c.size(); i++) {
				final Naive.Edge e = edges.get(c.edges()[i]);
				assertEquals(c.vertices()[i], e.u(), at);
				assertEquals(c.vertices()[(i + 1) % c.size()], e.v(), at);
			}
		} else {
			final int[] pos = new int[n];
			for (int i = 0; i < n; i++) pos[topo[i]] = i;
			assertEquals(n, set(topo).size(), at);
			for (final Naive.Edge e : edges) assertTrue(pos[e.u()] < pos[e.v()], at);
			assertNull(GraphUtils.findCycle(g), at);
		}
		final int[][] sccs = GraphUtils.scc(g);
		final int[] comp = new int[n];
		Arrays.fill(comp, -1);
		for (int i = 0; i < sccs.length; i++) for (final int v : sccs[i]) comp[v] = i;
		for (int u = 0; u < n; u++) {
			assertNotEquals(-1, comp[u], at);
			for (int v = 0; v < n; v++) assertEquals(reach[u][v] && reach[v][u], comp[u] == comp[v], at);
		}
		for (final Naive.Edge e : edges) assertTrue(comp[e.u()] <= comp[e.v()], at + ", 成分はトポロジカル順");
		final int s = random.nextInt(n);
		final int[] order = GraphUtils.bfs(g, s);
		final Set<Integer> expected = new HashSet<>();
		for (int v = 0; v < n; v++) if (reach[s][v]) expected.add(v);
		assertEquals(expected, set(order), at);
		final long[] dist = Naive.shortest(n, edges, s);
		for (int i = 0; i + 1 < order.length; i++) assertTrue(dist[order[i]] <= dist[order[i + 1]], at);
	}

	@RepeatedTest(100)
	void undirectedMatchesNaive(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final int n = 1 + random.nextInt(8);
		final List<Naive.Edge> edges = Naive.randomEdges(random, n, random.nextInt(10), () -> 1, false);
		final UndirectedGraph g = Naive.undirected(n, edges);
		final String at = "seed=" + seed + ", edges=" + edges;
		final boolean cyclic = edges.size() > n - Naive.components(n, edges);
		assertEquals(cyclic, GraphUtils.hasCycle(g), at);
		final GraphUtils.Cycle c = GraphUtils.findCycle(g);
		assertEquals(cyclic, c != null, at);
		if (c != null) {
			assertEquals(c.size(), set(c.vertices()).size(), at);
			assertEquals(c.size(), set(c.edges()).size(), at + ", 辺は重複しない");
			for (int i = 0; i < c.size(); i++) {
				final Naive.Edge e = edges.get(c.edges()[i]);
				final int a = c.vertices()[i], b = c.vertices()[(i + 1) % c.size()];
				assertTrue(e.u() == a && e.v() == b || e.u() == b && e.v() == a, at);
			}
		}
		boolean bipartite = false;
		for (int mask = 0; mask < 1 << n && !bipartite; mask++) {
			final int coloring = mask;
			bipartite = edges.stream().allMatch(e -> (coloring >> e.u() & 1) != (coloring >> e.v() & 1));
		}
		assertEquals(bipartite, GraphUtils.isBipartite(g), at);
	}

	static Set<Integer> set(final int[] a) {
		final Set<Integer> res = new HashSet<>();
		for (final int v : a) res.add(v);
		return res;
	}
}
