package unit.graph;

import java.util.*;
import java.util.function.*;

import org.junit.jupiter.api.*;

import lib.graph.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 単一始点・複数始点最短路（BFS・Dijkstra・ZeroOneBFS・BellmanFord）に共通する振る舞いのテスト。
 * 各アルゴリズムは同じ名前の static メソッドを持つため、Solver で呼び分ける。
 */
abstract class ShortestPathContract {

	interface Solver {
		PathResult solve(Graph g, int s);

		PathResult solve(Graph g, int... s);

		long[] dist(Graph g, int s);

		long[] dist(Graph g, int... s);

		long dist(Graph g, int s, int t);

		int[] path(Graph g, int s, int t);
	}

	abstract Solver solver();

	/**
	 * そのアルゴリズムが扱える辺の重み。
	 */
	abstract long weight(Random random);

	/**
	 * 無向グラフを扱えるか。負辺を含む BellmanFord の無向グラフは負閉路になるため、テストで使わない。
	 */
	boolean supportsUndirected() {
		return true;
	}

	@Nested
	class Aggregation {

		@Test
		void unreachableIsMaxValue() {
			final DirectedGraph g = new DirectedGraph(3, 1);
			g.add(0, 1, 1);
			final PathResult r = solver().solve(g, 0);
			assertEquals(Long.MAX_VALUE, r.distTo(2));
			assertFalse(r.reachable(2));
			assertTrue(r.reachable(1));
			assertEquals(-1, r.parent(2));
			assertNull(r.pathTo(2));
			assertEquals(Long.MAX_VALUE, solver().dist(g, 0, 2));
			assertNull(solver().path(g, 0, 2));
			assertEquals(Long.MAX_VALUE, solver().dist(g, 0)[2]);
		}

		@Test
		void sourceItself() {
			final DirectedGraph g = new DirectedGraph(2, 1);
			g.add(0, 1, 1);
			final PathResult r = solver().solve(g, 1);
			assertEquals(0L, r.distTo(1));
			assertEquals(1, r.parent(1), "始点の親は始点");
			assertArrayEquals(new int[]{1}, r.pathTo(1));
			assertEquals(0L, solver().dist(g, 1, 1));
			assertArrayEquals(new int[]{1}, solver().path(g, 1, 1));
			assertArrayEquals(new int[]{1}, r.s);
			assertFalse(r.hasNegCycle);
		}

		@Test
		void multipleSources() {
			// 0 - 1 - 2 - 3 - 4 の一直線
			final DirectedGraph g = new DirectedGraph(5, 8);
			for (int i = 0; i + 1 < 5; i++) {
				g.add(i, i + 1, 1);
				g.add(i + 1, i, 1);
			}
			final PathResult r = solver().solve(g, 0, 4);
			assertArrayEquals(new long[]{0, 1, 2, 1, 0}, r.dist);
			assertArrayEquals(new long[]{0, 1, 2, 1, 0}, solver().dist(g, new int[]{0, 4}));
			assertArrayEquals(new int[]{0, 4}, r.s);
			assertEquals(4, r.pathTo(3)[0], "近い方の始点から復元する");
			assertTrue(r.toString().startsWith("s: [0, 4]\nhasNegCycle: false\ndist: [0, 1, 2, 1, 0]"));
		}
	}

	@RepeatedTest(100)
	void matchesNaive(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final int n = 1 + random.nextInt(8), m = random.nextInt(16);
		final boolean undirected = supportsUndirected() && random.nextBoolean();
		final List<Naive.Edge> edges = Naive.randomEdges(random, n, m, () -> weight(random), !undirected);
		final Graph g = undirected ? Naive.undirected(n, edges) : Naive.directed(n, edges);
		final List<Naive.Edge> arcs = Naive.arcs(edges, undirected);
		final int s = random.nextInt(n), t = random.nextInt(n);
		final String at = "seed=" + seed + ", undirected=" + undirected + ", edges=" + edges + ", s=" + s + ", t=" + t;
		final long[] expected = Naive.shortest(n, arcs, s);
		final PathResult r = solver().solve(g, s);
		assertArrayEquals(expected, r.dist, at);
		assertArrayEquals(expected, solver().dist(g, s), at);
		assertEquals(expected[t], solver().dist(g, s, t), at);
		if (expected[t] != Long.MAX_VALUE && expected[t] != Long.MIN_VALUE) {
			assertTrue(Naive.isPath(r.pathTo(t), arcs, s, t, expected[t], false), at + ", path=" + Arrays.toString(r.pathTo(t)));
			assertTrue(Naive.isPath(solver().path(g, s, t), arcs, s, t, expected[t], false), at);
		} else {
			assertNull(solver().path(g, s, t), at);
		}
		final int s2 = random.nextInt(n);
		assertArrayEquals(Naive.shortest(n, arcs, s, s2), solver().dist(g, new int[]{s, s2}), at + ", s2=" + s2);
	}

	/**
	 * static メソッドへの参照から Solver を作る。
	 */
	static Solver of(final BiFunction<Graph, Integer, PathResult> solve1, final BiFunction<Graph, int[], PathResult> solveN,
		final BiFunction<Graph, Integer, long[]> dist1, final BiFunction<Graph, int[], long[]> distN,
		final ToLongTriFunction dist, final TriFunction<int[]> path) {
		return new Solver() {
			public PathResult solve(final Graph g, final int s) {
				return solve1.apply(g, s);
			}

			public PathResult solve(final Graph g, final int... s) {
				return solveN.apply(g, s);
			}

			public long[] dist(final Graph g, final int s) {
				return dist1.apply(g, s);
			}

			public long[] dist(final Graph g, final int... s) {
				return distN.apply(g, s);
			}

			public long dist(final Graph g, final int s, final int t) {
				return dist.apply(g, s, t);
			}

			public int[] path(final Graph g, final int s, final int t) {
				return path.apply(g, s, t);
			}
		};
	}

	interface ToLongTriFunction {
		long apply(Graph g, int s, int t);
	}

	interface TriFunction<R> {
		R apply(Graph g, int s, int t);
	}
}
