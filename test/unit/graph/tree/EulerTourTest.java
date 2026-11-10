package unit.graph.tree;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.graph.tree.*;

import static org.junit.jupiter.api.Assertions.*;

final class EulerTourTest {

	@RepeatedTest(100)
	void matchesNaive(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final int n = 1 + random.nextInt(12), root = random.nextInt(n);
		final Naive naive = new Naive(random, n, root, 1);
		final EulerTour tour = new EulerTour(naive.rooted());
		final String at = "seed=" + seed;
		assertEquals(2 * n, tour.length(), at);
		final int[] count = new int[n];
		for (int i = 0; i < 2 * n; i++) count[tour.eventAt(i)]++;
		for (int u = 0; u < n; u++) {
			assertEquals(2, count[u], at + ", 各頂点は入場と退場の2回現れる");
			assertEquals(u, tour.eventAt(tour.enter(u)), at);
			assertEquals(u, tour.eventAt(tour.exit(u)), at);
			for (int v = 0; v < n; v++) {
				final boolean nested = tour.enter(u) <= tour.enter(v) && tour.exit(v) <= tour.exit(u);
				assertEquals(naive.isAncestor(u, v), nested, at);
			}
		}
	}

	@Nested
	class Access {

		@Test
		void events() {
			final RootedTree t = new RootedTree(3, 0);
			t.add(0, 1);
			t.add(1, 2);
			final EulerTour tour = new EulerTour(t);
			assertEquals(6, tour.length());
			assertArrayEquals(new int[]{0, 1, 2, 2, 1, 0}, new int[]{tour.eventAt(0), tour.eventAt(1), tour.eventAt(2), tour.eventAt(3), tour.eventAt(4), tour.eventAt(5)});
			assertEquals(1, tour.enter(1));
			assertEquals(4, tour.exit(1));
		}
	}
}
