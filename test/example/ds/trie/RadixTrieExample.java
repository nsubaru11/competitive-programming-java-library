package example.ds.trie;

import lib.ds.trie.*;

public final class RadixTrieExample {

	public static void main(final String[] args) {
		// 挿入
		RadixTrie trie = new RadixTrie();
		trie.insert("romane");
		trie.insert("romanus");
		trie.insert("romulus");
		trie.insert("rubens");

		// 完全一致
		System.out.println(trie.search("romanus"));
		System.out.println(trie.search("rubens"));
		System.out.println();

		// 接頭辞・未登録
		System.out.println(trie.search("roman"));
		System.out.println(trie.search("ruber"));
	}
}
