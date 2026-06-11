public final class Example {

	public static void main(final String[] args) {
		testTrie();
		testSuffixTrie();
	}

	private static void testTrie() {
		Trie pre = new Trie();
		pre.insert("prefix").insert("previous").insert("preset").insert("precious").insert("pretest").insert("device").insert("devious");
		System.out.println(pre.search("pre"));
		System.out.println(pre.search("prefix"));
		System.out.println(pre.search("previous"));
		System.out.println(pre.countPrefix("pre"));
		System.out.println(pre.countPrefix("dev"));
	}

	private static void testSuffixTrie() {
		SuffixTrie suf = new SuffixTrie();
		suf.insert("prefix").insert("previous").insert("preset").insert("precious").insert("pretest").insert("device").insert("devious");
		System.out.println(suf.search("pre"));
		System.out.println(suf.search("prefix"));
		System.out.println(suf.search("previous"));
		System.out.println(suf.countPrefix("ous"));
		System.out.println(suf.countPrefix("t"));
	}
}
