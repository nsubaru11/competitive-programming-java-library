package unit.graph.tree;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.graph.tree.*;

import static org.junit.jupiter.api.Assertions.*;

final class TreePathMoTest {

	@Test
	void noQueries() {
		final RootedTree t = new RootedTree(2, 0);
		t.add(0, 1);
		TreePathMo.run(t, new int[][]{{}, {}}, v -> fail(), v -> fail(), q -> fail());
	}

	// パス上の異なる色の数を数える
	@RepeatedTest(100)
	void distinctColorsOnPathMatchNaive(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final int n = 1 + random.nextInt(20), q = 1 + random.nextInt(30);
		final Naive naive = new Naive(random, n, random.nextInt(n), 1);
		final int[] color = new int[n];
		for (int i = 0; i < n; i++) color[i] = random.nextInt(4);
		final int[][] uv = new int[2][q];
		for (int i = 0; i < q; i++) {
			uv[0][i] = random.nextInt(n);
			uv[1][i] = random.nextInt(n);
		}
		final int[] count = new int[4], answer = new int[q];
		final int[] distinct = {0};
		final boolean[] inSet = new boolean[n];
		TreePathMo.run(naive.rooted(), uv, v -> {
			assertFalse(inSet[v], "seed=" + seed + ", 同じ頂点を二重に追加しない");
			inSet[v] = true;
			if (count[color[v]]++ == 0) distinct[0]++;
		}, v -> {
			assertTrue(inSet[v], "seed=" + seed);
			inSet[v] = false;
			if (--count[color[v]] == 0) distinct[0]--;
		}, i -> answer[i] = distinct[0]);
		for (int i = 0; i < q; i++) {
			final long expected = naive.path(uv[0][i], uv[1][i]).stream().map(v -> color[v]).distinct().count();
			assertEquals(expected, answer[i], "seed=" + seed + ", query=" + i);
		}
	}
}
