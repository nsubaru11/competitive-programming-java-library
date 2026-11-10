package unit.graph;

import java.util.*;
import java.util.function.*;

import org.junit.jupiter.api.*;

import lib.graph.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 最小・最大全域森（Kruskal・Prim）に共通する振る舞いのテスト。
 */
abstract class SpanningForestContract {

	/**
	 * 重みの順に辺を見て、閉路を作らない辺を採用する。
	 */
	static long naiveForestCost(final int n, final List<Naive.Edge> edges, final boolean max) {
		final List<Naive.Edge> sorted = new ArrayList<>(edges);
		sorted.sort(Comparator.comparingLong(e -> max ? -e.c() : e.c()));
		final List<Naive.Edge> chosen = new ArrayList<>();
		long cost = 0;
		for (final Naive.Edge e : sorted) {
			chosen.add(e);
			if (Naive.components(n, chosen) == n - chosen.size()) {
				cost += e.c();
			} else {
				chosen.removeLast();
			}
		}
		return cost;
	}

	static Set<Integer> toSet(final int[] a) {
		final Set<Integer> res = new HashSet<>();
		for (final int v : a) res.add(v);
		return res;
	}

	abstract Function<UndirectedGraph, SpanningForestResult> minimum();

	abstract Function<UndirectedGraph, SpanningForestResult> maximum();

	abstract ToLongFunction<UndirectedGraph> minimumCost();

	abstract ToLongFunction<UndirectedGraph> maximumCost();

	@RepeatedTest(100)
	void matchesNaive(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final int n = 1 + random.nextInt(8);
		final List<Naive.Edge> edges = Naive.randomEdges(random, n, random.nextInt(14), () -> random.nextInt(-5, 10), false);
		final UndirectedGraph g = Naive.undirected(n, edges);
		final String at = "seed=" + seed + ", edges=" + edges;
		final int components = Naive.components(n, edges);
		for (final boolean max : new boolean[]{false, true}) {
			final long expected = naiveForestCost(n, edges, max);
			final SpanningForestResult r = (max ? maximum() : minimum()).apply(g);
			assertEquals(expected, r.cost, at + ", max=" + max);
			assertEquals(expected, (max ? maximumCost() : minimumCost()).applyAsLong(g), at);
			assertEquals(components, r.componentCount, at);
			assertEquals(n - components, r.edgeCount(), at);
			final List<Naive.Edge> chosen = Arrays.stream(r.edgeIds).mapToObj(edges::get).toList();
			assertEquals(expected, chosen.stream().mapToLong(Naive.Edge::c).sum(), at);
			assertEquals(components, Naive.components(n, chosen), at + ", 採用辺で同じ連結成分になる");
		}
	}

	@Nested
	class Aggregation {

		// 0-1 (1), 1-2 (2), 0-2 (3), 3-4 (5)。成分は {0, 1, 2} と {3, 4}、5 は孤立点
		private UndirectedGraph sample() {
			final UndirectedGraph g = new UndirectedGraph(6, 4);
			g.add(0, 1, 1);
			g.add(1, 2, 2);
			g.add(0, 2, 3);
			g.add(3, 4, 5);
			return g;
		}

		@Test
		void minimumForest() {
			final SpanningForestResult r = minimum().apply(sample());
			assertEquals(8L, r.cost);
			assertEquals(Set.of(0, 1, 3), toSet(r.edgeIds));
			assertEquals(3, r.edgeCount());
			assertEquals(3, r.componentCount);
			assertFalse(r.isSpanningTree());
			assertEquals(8L, minimumCost().applyAsLong(sample()));
			assertTrue(r.toString().startsWith("cost: 8\nedgeIds: "));
		}

		@Test
		void maximumForest() {
			final SpanningForestResult r = maximum().apply(sample());
			assertEquals(10L, r.cost);
			assertEquals(Set.of(1, 2, 3), toSet(r.edgeIds));
			assertEquals(10L, maximumCost().applyAsLong(sample()));
		}

		@Test
		void connectedGraphIsSpanningTree() {
			final UndirectedGraph g = new UndirectedGraph(3, 3);
			g.add(0, 1, 1);
			g.add(1, 2, 1);
			g.add(2, 0, 1);
			final SpanningForestResult r = minimum().apply(g);
			assertTrue(r.isSpanningTree());
			assertEquals(2, r.edgeCount());
		}

		@Test
		void emptyGraph() {
			final SpanningForestResult r = minimum().apply(new UndirectedGraph(2, 0));
			assertEquals(0L, r.cost);
			assertEquals(0, r.edgeCount());
			assertEquals(2, r.componentCount);
		}
	}
}
