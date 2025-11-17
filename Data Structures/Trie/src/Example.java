public final class Example {

	public static void main(final String[] args) {
		Trie trie = new Trie();
		trie.insert("prefix");
		trie.insert("previous");
		trie.insert("preset");
		trie.insert("precious");
		trie.insert("pretest");
		trie.insert("device");
		trie.insert("devious");
		System.out.println(trie.search("pre"));
		System.out.println(trie.search("prefix"));
		System.out.println(trie.search("previous"));
		System.out.println(trie.countPrefix("pre"));
		System.out.println(trie.countPrefix("dev"));
	}

}
