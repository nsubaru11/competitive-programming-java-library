package example.ds.trie;

import lib.ds.trie.*;

public final class SuffixTrieExample {

	public static void main(final String[] args) {
		// 挿入
		SuffixTrie trie = new SuffixTrie();
		trie.insert("previous").insert("precious").insert("pretest").insert("devious");

		// 完全一致
		System.out.println(trie.search("ous"));
		System.out.println(trie.search("pretest"));
		System.out.println();

		// 後方一致の件数
		System.out.println(trie.countPrefix("ous"));
		System.out.println(trie.countPrefix("ious"));
		System.out.println(trie.countPrefix("t"));
	}
}
