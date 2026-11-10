package example.ds.trie;

import lib.ds.trie.*;

public final class TernarySearchTreeExample {

	public static void main(final String[] args) {
		// 挿入
		TernarySearchTree tst = new TernarySearchTree();
		tst.insert("cat");
		tst.insert("cap");
		tst.insert("car");
		tst.insert("dog");

		// 完全一致
		System.out.println(tst.search("cap"));
		System.out.println(tst.search("ca"));
		System.out.println();

		// 前方一致の件数
		System.out.println(tst.countPrefix("ca"));
		System.out.println(tst.countPrefix("d"));
		System.out.println(tst.countPrefix("e"));
	}
}
