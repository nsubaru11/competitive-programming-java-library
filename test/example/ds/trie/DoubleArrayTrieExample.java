package example.ds.trie;

import lib.ds.trie.*;

public final class DoubleArrayTrieExample {

	public static void main(final String[] args) {
		// 構築
		DoubleArrayTrie trie = new DoubleArrayTrie(1 << 12);
		trie.build(new String[]{"apple", "apply", "banana", "band"});

		// 完全一致
		System.out.println(trie.search("apple"));
		System.out.println(trie.search("band"));
		System.out.println();

		// 接頭辞・未登録
		System.out.println(trie.search("app"));
		System.out.println(trie.search("bandana"));
	}
}
