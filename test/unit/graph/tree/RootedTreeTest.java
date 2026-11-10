package unit.graph.tree;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.graph.tree.*;

import static org.junit.jupiter.api.Assertions.*;

final class RootedTreeTest {

	@RepeatedTest(100)
	void matchesNaive(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final int n = 1 + random.nextInt(12), root = random.nextInt(n);
		final Naive naive = new Naive(random, n, root, 9);
		final RootedTree t = naive.rooted();
		final String at = "seed=" + seed;
		final boolean[] used = new boolean[n];
		for (int u = 0; u < n; u++) {
			assertEquals(naive.parent[u], t.parent(u), at);
			assertEquals(naive.parentEdge[u], t.parentEdge(u), at);
			assertEquals(naive.depth[u], t.depth(u), at);
			assertEquals(naive.dist[u], t.rootDistance(u), at);
			assertEquals(naive.subtreeSize(u), t.subtreeSize(u), at);
			assertEquals(u, t.preorderAt(t.in(u)), at);
			assertEquals(t.in(u) + t.subtreeSize(u), t.out(u), at);
			used[t.in(u)] = true;
			for (int v = 0; v < n; v++) {
				final boolean anc = naive.isAncestor(u, v);
				assertEquals(anc, t.isAncestor(u, v), at);
				assertEquals(anc, t.in(u) <= t.in(v) && t.in(v) < t.out(u), at + ", 部分木区間は子孫だけを含む");
			}
		}
		for (final boolean b : used) assertTrue(b, at);
		assertEquals(root, t.preorderAt(0), at);
	}

	@Nested
	class Access {

		@Test
		void rootProperties() {
			final RootedTree t = new RootedTree(3, 1);
			t.add(0, 1, 4);
			t.add(1, 2, 6);
			assertEquals(1, t.root);
			assertEquals(1, t.parent(1), "根の親は根自身");
			assertEquals(-1, t.parentEdge(1));
			assertEquals(0, t.depth(1));
			assertEquals(3, t.subtreeSize(1));
			assertEquals(1, t.preorderAt(0));
			assertEquals(0, t.in(1));
			assertEquals(3, t.out(1));
			assertEquals(6L, t.rootDistance(2));
			assertTrue(t.isAncestor(1, 1));
			assertFalse(t.isAncestor(0, 2));
		}

		@Test
		void subtreeIntervalIsPassedToCallbacks() {
			final RootedTree t = new RootedTree(3, 0);
			t.add(0, 1);
			t.add(1, 2);
			final int[] seen = new int[2];
			t.updateSubtree(1, (l, r) -> {
				seen[0] = l;
				seen[1] = r;
			});
			assertArrayEquals(new int[]{t.in(1), t.out(1)}, seen);
			assertEquals(100L * t.in(1) + t.out(1), t.querySubtree(1, (l, r) -> 100L * l + r));
		}
	}
}
