package unit.graph.tree;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.graph.tree.*;

import static org.junit.jupiter.api.Assertions.*;

final class LCATest {

	@Test
	void singleNode() {
		final RootedTree tree = new RootedTree(1, 0);
		assertEquals(0, new LCA(tree).lca(0, 0));
		assertEquals(0, new HLD(tree).lca(0, 0));
	}

	@Test
	void twoNodes() {
		final RootedTree tree = new RootedTree(2, 0);
		tree.add(0, 1, 10);
		final LCA lca = new LCA(tree);
		assertEquals(0, lca.lca(0, 1));
		assertEquals(0, lca.lca(1, 0));
		assertEquals(1, lca.lca(1, 1));
		assertEquals(10, lca.distance(0, 1));
		assertEquals(10, lca.distance(1, 0));
	}

	@Test
	void rootInMiddle() {
		final RootedTree tree = new RootedTree(3, 1);
		tree.add(1, 0, 5);
		tree.add(1, 2, 7);
		final LCA lca = new LCA(tree);
		assertEquals(1, lca.lca(0, 2));
		assertEquals(12, lca.distance(0, 2));
		assertMatchesHLD(tree);
	}

	@Test
	void completeBinaryTree() {
		final RootedTree tree = new RootedTree(7, 0);
		for (int v = 1; v < 7; v++) tree.add((v - 1) / 2, v);
		final LCA lca = new LCA(tree);
		assertEquals(1, lca.lca(3, 4));
		assertEquals(0, lca.lca(3, 6));
		assertEquals(2, lca.lca(2, 5));
		assertMatchesHLD(tree);
	}

	@Test
	void line() {
		final int n = 500;
		final RootedTree tree = new RootedTree(n, 0);
		for (int i = 0; i < n - 1; i++) tree.add(i, i + 1);
		final LCA lca = new LCA(tree);
		for (int u = 0; u < n; u += 7) {
			for (int v = 0; v < n; v += 11) assertEquals(Math.min(u, v), lca.lca(u, v), "u=" + u + ", v=" + v);
		}
	}

	@Test
	void star() {
		final int n = 200;
		final RootedTree tree = new RootedTree(n, 0);
		for (int i = 1; i < n; i++) tree.add(0, i);
		assertMatchesHLD(tree);
	}

	// HLD との照合: 根と形をランダムにした木で、全頂点対の LCA と距離を比べる
	@RepeatedTest(50)
	void matchesHLDOnRandomTree(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final int n = random.nextInt(1, 100);
		final RootedTree tree = new RootedTree(n, random.nextInt(n));
		for (int i = 1; i < n; i++) tree.add(i, random.nextInt(i), random.nextInt(1, 1000));
		assertMatchesHLD(tree, "seed=" + seed);
	}

	@Test
	void largeRandomTree() {
		final int n = 100_000;
		final Random random = new Random(98765);
		final RootedTree tree = new RootedTree(n, 0);
		for (int i = 1; i < n; i++) tree.add(i, random.nextInt(i));
		final LCA lca = new LCA(tree);
		final HLD hld = new HLD(tree);
		for (int q = 0; q < 100_000; q++) {
			final int u = random.nextInt(n);
			final int v = random.nextInt(n);
			assertEquals(hld.lca(u, v), lca.lca(u, v), "u=" + u + ", v=" + v);
		}
	}

	private static void assertMatchesHLD(final RootedTree tree) {
		assertMatchesHLD(tree, "");
	}

	private static void assertMatchesHLD(final RootedTree tree, final String at) {
		final LCA lca = new LCA(tree);
		final HLD hld = new HLD(tree);
		for (int u = 0; u < tree.n; u++) {
			for (int v = 0; v < tree.n; v++) {
				final String pair = at + " u=" + u + ", v=" + v;
				assertEquals(hld.lca(u, v), lca.lca(u, v), pair);
				assertEquals(hld.distance(u, v), lca.distance(u, v), pair);
			}
		}
	}
}
