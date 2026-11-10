package example.ds.set;

import lib.ds.set.*;

public final class BinarySearchTreeExample {

	public static void main(final String[] args) {
		// 追加
		BinarySearchTree<Integer> bst = new BinarySearchTree<>();
		for (int v : new int[]{50, 30, 70, 20, 40, 60}) {
			bst.add(v);
		}
		System.out.println(bst.stream().toList());
		System.out.println(bst.size());
		System.out.println();

		// 検索
		System.out.println(bst.contains(40));
		System.out.println(bst.contains(45));
		System.out.println();

		// 近傍
		System.out.println(bst.lower(50));
		System.out.println(bst.floor(45));
		System.out.println(bst.ceiling(45));
		System.out.println(bst.higher(70));
		System.out.println();

		// 削除
		bst.remove(30);
		System.out.println(bst.stream().toList());
	}
}
