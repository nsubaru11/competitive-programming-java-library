package unit.graph.tree;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.graph.tree.*;

import static org.junit.jupiter.api.Assertions.*;

final class LCATest {

	@Nested
	class Search {

		@Test
		void smallTrees() {
			assertEquals(0, new LCA(new RootedTree(1, 0)).lca(0, 0));
			final RootedTree t = new RootedTree(3, 1);
			t.add(1, 0, 5);
			t.add(1, 2, 7);
			final LCA lca = new LCA(t);
			assertEquals(1, lca.lca(0, 2));
			assertEquals(0, lca.lca(0, 0));
			assertEquals(12L, lca.distance(0, 2));
			assertEquals(0L, lca.distance(2, 2));
		}

		@Test
		void longLine() {
			final int n = 3000;
			final RootedTree t = new RootedTree(n, 0);
			for (int i = 0; i + 1 < n; i++) t.add(i, i + 1, 1);
			final LCA lca = new LCA(t);
			assertEquals(1234, lca.lca(1234, 2999));
			assertEquals(1765L, lca.distance(1234, 2999));
		}
	}

	@RepeatedTest(200)
	void matchesNaive(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final int n = 1 + random.nextInt(seed % 10 == 0 ? 300 : 30), root = random.nextInt(n);
		final Naive naive = new Naive(random, n, root, 9);
		final LCA lca = new LCA(naive.rooted());
		for (int q = 0; q < 200; q++) {
			final int u = random.nextInt(n), v = random.nextInt(n);
			final String at = "seed=" + seed + ", u=" + u + ", v=" + v;
			assertEquals(naive.lca(u, v), lca.lca(u, v), at);
			assertEquals(naive.distance(u, v), lca.distance(u, v), at);
		}
	}
}
