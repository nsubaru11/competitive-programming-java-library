package unit.graph.tree;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.graph.tree.*;

import static org.junit.jupiter.api.Assertions.*;

final class TreeTest {

	@Nested
	class Aggregation {

		@Test
		void diameterOfPathAndStar() {
			final Tree path = new Tree(4);
			path.add(0, 1, 5);
			path.add(1, 2, 1);
			path.add(2, 3, 1);
			assertEquals(3, path.diameter());
			assertEquals(7L, path.diameterCost());
			final Tree star = new Tree(4);
			star.add(0, 1, 1);
			star.add(0, 2, 9);
			star.add(0, 3, 4);
			assertEquals(2, star.diameter());
			assertEquals(13L, star.diameterCost());
			assertEquals(0, new Tree(1).diameter());
		}
	}

	@RepeatedTest(100)
	void matchesNaive(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final int n = 1 + random.nextInt(12);
		final Naive naive = new Naive(random, n, 0, 9);
		final Tree t = naive.tree();
		int hops = 0;
		long cost = 0;
		for (int u = 0; u < n; u++) {
			for (int v = 0; v < n; v++) {
				hops = Math.max(hops, naive.path(u, v).size() - 1);
				cost = Math.max(cost, naive.distance(u, v));
			}
		}
		final String at = "seed=" + seed;
		assertEquals(hops, t.diameter(), at);
		assertEquals(cost, t.diameterCost(), at);
	}
}
