package example.graph.tree;

import lib.graph.tree.*;

public final class HLDExample {

	public static void main(final String[] args) {
		// 木
		RootedTree t = new RootedTree(7, 0);
		t.add(0, 1, 2);
		t.add(0, 2, 5);
		t.add(1, 3, 1);
		t.add(1, 4, 3);
		t.add(2, 5, 4);
		t.add(5, 6, 1);
		HLD hld = new HLD(t);

		// LCA・距離
		System.out.println(hld.lca(4, 6));
		System.out.println(hld.distance(4, 6));
		System.out.println();

		// 祖先・パス上の移動
		System.out.println(hld.kthAncestor(6, 2));
		System.out.println(hld.jump(3, 6, 2));
		System.out.println();

		// HLD 順の値（頂点 u の値は u + 1）
		long[] val = new long[t.n];
		for (int u = 0; u < t.n; u++) {
			val[hld.enter(u)] = u + 1;
		}

		// パス上の頂点和
		System.out.println(hld.queryNode(3, 6, 0, (l, r) -> sum(val, l, r), Long::sum));
		System.out.println();

		// 部分木の頂点和
		System.out.println(hld.querySubtree(2, (l, r) -> sum(val, l, r)));
	}

	private static long sum(final long[] a, final int l, final int r) {
		long s = 0;
		for (int i = l; i < r; i++) s += a[i];
		return s;
	}
}
