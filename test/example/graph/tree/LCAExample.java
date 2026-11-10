package example.graph.tree;

import lib.graph.tree.*;

public final class LCAExample {

	public static void main(final String[] args) {
		// 木
		RootedTree t = new RootedTree(7, 0);
		t.add(0, 1, 2);
		t.add(0, 2, 5);
		t.add(1, 3, 1);
		t.add(1, 4, 3);
		t.add(2, 5, 4);
		t.add(5, 6, 1);
		LCA lca = new LCA(t);

		// 最小共通祖先
		System.out.println(lca.lca(3, 4));
		System.out.println(lca.lca(3, 6));
		System.out.println(lca.lca(5, 6));
		System.out.println();

		// 重み付き距離
		System.out.println(lca.distance(3, 4));
		System.out.println(lca.distance(3, 6));
	}
}
