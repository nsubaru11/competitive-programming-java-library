package example.graph.tree;

import lib.graph.tree.*;

public final class RootedTreeExample {

	public static void main(final String[] args) {
		// 木
		RootedTree t = new RootedTree(7, 0);
		t.add(0, 1, 2);
		t.add(0, 2, 5);
		t.add(1, 3, 1);
		t.add(1, 4, 3);
		t.add(2, 5, 4);
		t.add(5, 6, 1);

		// 親・深さ・部分木
		for (int u = 0; u < t.n; u++) {
			System.out.println(u + " " + t.parent(u) + " " + t.depth(u) + " " + t.subtreeSize(u));
		}
		System.out.println();

		// 根からの距離
		System.out.println(t.rootDistance(6));
		System.out.println();

		// 先祖判定
		System.out.println(t.isAncestor(1, 4));
		System.out.println(t.isAncestor(2, 4));
		System.out.println();

		// 部分木の preorder 区間 [in, out)
		System.out.println(t.in(1) + " " + t.out(1));
		t.updateSubtree(2, (l, r) -> System.out.println(l + " " + r));
		System.out.println(t.preorderAt(t.in(5)));
	}
}
