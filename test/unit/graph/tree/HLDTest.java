package unit.graph.tree;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.graph.tree.*;

import static org.junit.jupiter.api.Assertions.*;

final class HLDTest {

	@Nested
	class Search {

		@Test
		void ancestorsAndJump() {
			// 0 - 1 - 2 - 3 と 1 - 4
			final RootedTree t = new RootedTree(5, 0);
			t.add(0, 1, 1);
			t.add(1, 2, 1);
			t.add(2, 3, 1);
			t.add(1, 4, 5);
			final HLD hld = new HLD(t);
			assertEquals(1, hld.lca(3, 4));
			assertEquals(7L, hld.distance(3, 4));
			assertEquals(1, hld.kthAncestor(3, 2));
			assertEquals(3, hld.kthAncestor(3, 0));
			assertEquals(-1, hld.kthAncestor(3, 4), "存在しなければ -1");
			assertEquals(2, hld.jump(3, 4, 1));
			assertEquals(4, hld.jump(3, 4, 3));
			assertEquals(-1, hld.jump(3, 4, 4), "パス外なら -1");
		}
	}

	@RepeatedTest(100)
	void matchesNaive(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final int n = 1 + random.nextInt(seed % 10 == 0 ? 200 : 20), root = random.nextInt(n);
		final Naive naive = new Naive(random, n, root, 9);
		final RootedTree t = naive.rooted();
		final HLD hld = new HLD(t);
		final String at = "seed=" + seed;
		// 位置 p に頂点 vertexAt(p) の値を置き、区間和で集約を照合する
		final long[] value = new long[n], posValue = new long[n];
		for (int u = 0; u < n; u++) value[u] = random.nextInt(100);
		for (int p = 0; p < n; p++) posValue[p] = value[hld.vertexAt(p)];
		for (int u = 0; u < n; u++) {
			assertEquals(u, hld.vertexAt(hld.enter(u)), at);
			assertEquals(hld.enter(u) + naive.subtreeSize(u), hld.exit(u), at);
			assertTrue(naive.isAncestor(hld.top(u), u), at + ", chain の先頭は祖先");
			long subtree = 0;
			for (int v = 0; v < n; v++) if (naive.isAncestor(u, v)) subtree += value[v];
			final int uu = u;
			assertEquals(subtree, hld.querySubtree(u, (l, r) -> sum(posValue, l, r)), at);
			final int[] range = new int[2];
			hld.updateSubtree(u, (l, r) -> {
				range[0] = l;
				range[1] = r;
			});
			assertArrayEquals(new int[]{hld.enter(uu), hld.exit(uu)}, range, at);
			final int[] node = {-1};
			hld.updateNode(u, p -> node[0] = p);
			assertEquals(hld.enter(u), node[0], at);
			for (int k = 0; k <= naive.depth[u] + 1; k++) {
				int expected = u;
				for (int i = 0; i < k && expected != -1; i++) expected = expected == root ? -1 : naive.parent[expected];
				assertEquals(expected, hld.kthAncestor(u, k), at + ", u=" + u + ", k=" + k);
			}
		}
		for (int e = 0; e < n - 1; e++) {
			final int deeper = naive.depth[naive.eu[e]] > naive.depth[naive.ev[e]] ? naive.eu[e] : naive.ev[e];
			final int[] pos = {-1};
			hld.updateEdge(e, p -> pos[0] = p);
			assertEquals(hld.enter(deeper), pos[0], at + ", 辺は深い側の頂点位置");
		}
		for (int q = 0; q < 50; q++) {
			final int u = random.nextInt(n), v = random.nextInt(n);
			final String qat = at + ", u=" + u + ", v=" + v;
			final List<Integer> path = naive.path(u, v);
			final int w = naive.lca(u, v);
			assertEquals(w, hld.lca(u, v), qat);
			assertEquals(naive.distance(u, v), hld.distance(u, v), qat);
			final long nodeSum = path.stream().mapToLong(x -> value[x]).sum();
			assertEquals(nodeSum, hld.queryNode(u, v, 0, (l, r) -> sum(posValue, l, r), Long::sum), qat);
			assertEquals(nodeSum - value[w], hld.queryEdge(u, v, 0, (l, r) -> sum(posValue, l, r), Long::sum), qat);
			assertEquals(positions(path, hld, -1), covered(n, c -> hld.updateNode(u, v, c)), qat);
			assertEquals(positions(path, hld, w), covered(n, c -> hld.updateEdge(u, v, c)), qat + ", LCA を除く");
			final int k = random.nextInt(path.size() + 1);
			assertEquals(k < path.size() ? path.get(k) : -1, hld.jump(u, v, k), qat + ", k=" + k);
		}
	}

	private static long sum(final long[] a, final int l, final int r) {
		long s = 0;
		for (int i = l; i < r; i++) s += a[i];
		return s;
	}

	private static Set<Integer> positions(final List<Integer> path, final HLD hld, final int except) {
		final Set<Integer> res = new HashSet<>();
		for (final int x : path) if (x != except) res.add(hld.enter(x));
		return res;
	}

	/**
	 * 渡された区間の和集合を返す。区間が重なれば失敗させる。
	 */
	private static Set<Integer> covered(final int n, final java.util.function.Consumer<lib.util.function.IntBinaryConsumer> call) {
		final Set<Integer> res = new HashSet<>();
		call.accept((l, r) -> {
			for (int i = l; i < r; i++) assertTrue(res.add(i), "区間が重なる");
		});
		return res;
	}
}
