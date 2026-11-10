package example.ds.trie;

import lib.ds.trie.*;

public final class TrieExample {

	public static void main(final String[] args) {
		// 挿入
		Trie trie = new Trie();
		trie.insert("prefix").insert("previous").insert("preset").insert("device").insert("devious");

		// 完全一致
		System.out.println(trie.search("pre"));
		System.out.println(trie.search("prefix"));
		System.out.println();

		// 前方一致の件数
		System.out.println(trie.countPrefix("pre"));
		System.out.println(trie.countPrefix("dev"));
		System.out.println(trie.countPrefix("x"));
	}
}
