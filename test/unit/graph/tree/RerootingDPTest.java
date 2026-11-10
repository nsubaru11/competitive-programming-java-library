package unit.graph.tree;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.graph.tree.*;

import static org.junit.jupiter.api.Assertions.*;

final class RerootingDPTest {

	// 全方位木DPで、各頂点から全頂点への距離の和を求める
	@RepeatedTest(100)
	void sumOfDistancesMatchesNaive(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final int n = 1 + random.nextInt(15), root = random.nextInt(n);
		final Naive naive = new Naive(random, n, root, 9);
		final RootedTree t = naive.rooted();
		final long[] size = new long[n], down = new long[n], all = new long[n];
		Arrays.fill(size, 1);
		final boolean[] doneUp = new boolean[n];
		RerootingDP.run(t, (v, p, e) -> {
			// 子はすべて処理済み
			for (int c = 0; c < n; c++)
				if (c != v && naive.parent[c] == v && c != root) assertTrue(doneUp[c], "seed=" + seed);
			assertEquals(naive.parent[v], p);
			assertEquals(naive.parentEdge[v], e);
			doneUp[v] = true;
			size[p] += size[v];
			down[p] += down[v] + size[v] * naive.ec[e];
		}, (p, c, e) -> {
			// 親の値は確定済み
			if (p == root) all[p] = down[p];
			assertEquals(naive.parent[c], p);
			all[c] = all[p] + (n - 2 * size[c]) * naive.ec[e];
		});
		if (n == 1) all[root] = down[root];
		for (int v = 0; v < n; v++) {
			if (v != root) assertTrue(doneUp[v], "seed=" + seed + ", 根以外はすべて1回処理する");
			long expected = 0;
			for (int u = 0; u < n; u++) expected += naive.distance(u, v);
			assertEquals(expected, all[v], "seed=" + seed + ", v=" + v);
		}
	}
}
