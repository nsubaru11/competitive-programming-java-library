package example.ds.unionfind;

import lib.ds.unionfind.*;

public final class UnionFindExample {

	public static void main(final String[] args) {
		// 併合
		UnionFind uf = new UnionFind(6);
		System.out.println(uf.union(0, 1));
		System.out.println(uf.union(1, 2));
		System.out.println(uf.union(0, 2));
		uf.union(3, 4);
		System.out.println();

		// 連結判定
		System.out.println(uf.isConnected(0, 2));
		System.out.println(uf.isConnected(2, 3));
		System.out.println(uf.find(2) == uf.find(0));
		System.out.println();

		// グループの情報
		System.out.println(uf.groupCount());
		System.out.println(uf.size(0));
		System.out.println(uf.pathCount(0));
		System.out.println();

		// グループの一覧
		System.out.println(uf.groups());
	}
}
