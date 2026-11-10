package example.graph.tree;

import lib.graph.tree.*;

public final class TreeExample {

	public static void main(final String[] args) {
		// 木
		Tree t = new Tree(7);
		t.add(0, 1, 2);
		t.add(0, 2, 5);
		t.add(1, 3, 1);
		t.add(1, 4, 3);
		t.add(2, 5, 4);
		t.add(5, 6, 1);

		// 直径（辺数）
		System.out.println(t.diameter());
		System.out.println();

		// 重み付き直径
		System.out.println(t.diameterCost());
	}
}
