package verify.graph.tree.lca;

import java.util.*;
import lib.graph.tree.*;

public final class TestLCA {

	public static void main(final String[] args) {
		testSingleNode();
		testTwoNodes();
		testThreeNodes();
		testSmallTree();
		testLineTree();
		testStarTree();
		testRandomTrees();
		testWeightedTree();
		testLargeTree();
		System.out.println("All LCA tests passed successfully!");
	}

	private static void testTwoNodes() {
		final RootedTree tree = new RootedTree(2, 0);
		tree.add(0, 1, 10);
		final LCA lca = new LCA(tree);
		final HLD hld = new HLD(tree);
		check(lca.lca(0, 1) == 0);
		check(lca.lca(1, 0) == 0);
		check(lca.lca(1, 1) == 1);
		check(lca.distance(0, 1) == 10);
		check(lca.distance(1, 0) == 10);
	}

	private static void testThreeNodes() {
		final RootedTree tree = new RootedTree(3, 1);
		tree.add(1, 0, 5);
		tree.add(1, 2, 7);
		final LCA lca = new LCA(tree);
		final HLD hld = new HLD(tree);
		for (int u = 0; u < 3; u++) {
			for (int v = 0; v < 3; v++) {
				check(lca.lca(u, v) == hld.lca(u, v));
				check(lca.distance(u, v) == hld.distance(u, v));
			}
		}
	}

	private static void testLargeTree() {
		final int n = 100_000;
		final Random rnd = new Random(98765);
		final RootedTree tree = new RootedTree(n, 0);
		for (int i = 1; i < n; i++) {
			final int p = rnd.nextInt(i);
			tree.add(i, p);
		}

		final LCA lca = new LCA(tree);
		final HLD hld = new HLD(tree);

		for (int q = 0; q < 100_000; q++) {
			final int u = rnd.nextInt(n);
			final int v = rnd.nextInt(n);
			final int expected = hld.lca(u, v);
			final int actual = lca.lca(u, v);
			if (actual != expected) {
				throw new AssertionError("Mismatch in large tree query " + q + ": " + u + ", " + v);
			}
		}
	}

	private static void testSingleNode() {
		final RootedTree tree = new RootedTree(1, 0);
		final LCA lca = new LCA(tree);
		final HLD hld = new HLD(tree);
		check(lca.lca(0, 0) == 0);
		check(lca.lca(0, 0) == hld.lca(0, 0));
	}

	private static void testSmallTree() {
		final RootedTree tree = new RootedTree(7, 0);
		tree.add(0, 1);
		tree.add(0, 2);
		tree.add(1, 3);
		tree.add(1, 4);
		tree.add(2, 5);
		tree.add(2, 6);

		final LCA lca = new LCA(tree);
		final HLD hld = new HLD(tree);

		for (int u = 0; u < 7; u++) {
			for (int v = 0; v < 7; v++) {
				check(lca.lca(u, v) == hld.lca(u, v));
			}
		}
	}

	private static void testLineTree() {
		final int n = 500;
		final RootedTree tree = new RootedTree(n, 0);
		for (int i = 0; i < n - 1; i++) {
			tree.add(i, i + 1);
		}

		final LCA lca = new LCA(tree);
		final HLD hld = new HLD(tree);

		for (int u = 0; u < n; u += 7) {
			for (int v = 0; v < n; v += 11) {
				check(lca.lca(u, v) == hld.lca(u, v));
			}
		}
	}

	private static void testStarTree() {
		final int n = 200;
		final RootedTree tree = new RootedTree(n, 0);
		for (int i = 1; i < n; i++) {
			tree.add(0, i);
		}

		final LCA lca = new LCA(tree);
		final HLD hld = new HLD(tree);

		for (int u = 0; u < n; u++) {
			for (int v = 0; v < n; v++) {
				check(lca.lca(u, v) == hld.lca(u, v));
				check(lca.distance(u, v) == hld.distance(u, v));
			}
		}
	}

	private static void testRandomTrees() {
		final Random rnd = new Random(42);
		for (int iter = 0; iter < 50; iter++) {
			final int n = rnd.nextInt(1, 300);
			final int root = rnd.nextInt(n);
			final RootedTree tree = new RootedTree(n, root);
			for (int i = 1; i < n; i++) {
				final int p = rnd.nextInt(i);
				tree.add(i, p);
			}

			final LCA lca = new LCA(tree);
			final HLD hld = new HLD(tree);

			for (int q = 0; q < 500; q++) {
				final int u = rnd.nextInt(n);
				final int v = rnd.nextInt(n);
				final int expectedLca = hld.lca(u, v);
				final int actualLca = lca.lca(u, v);
				if (actualLca != expectedLca) {
					throw new AssertionError(String.format("Mismatch on iter %d: u=%d, v=%d, expected=%d, actual=%d",
							iter, u, v, expectedLca, actualLca));
				}
			}
		}
	}

	private static void testWeightedTree() {
		final int n = 50;
		final Random rnd = new Random(12345);
		final RootedTree tree = new RootedTree(n, 0);
		for (int i = 1; i < n; i++) {
			final int p = rnd.nextInt(i);
			final long w = rnd.nextInt(1, 1000);
			tree.add(i, p, w);
		}

		final LCA lca = new LCA(tree);
		final HLD hld = new HLD(tree);

		for (int u = 0; u < n; u++) {
			for (int v = 0; v < n; v++) {
				check(lca.lca(u, v) == hld.lca(u, v));
				check(lca.distance(u, v) == hld.distance(u, v));
			}
		}
	}

	private static void check(final boolean condition) {
		if (!condition) throw new AssertionError();
	}
}
