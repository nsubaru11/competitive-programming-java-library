package unit.ds.trie;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.ds.trie.*;

import static org.junit.jupiter.api.Assertions.*;

final class SuffixTrieTest extends StringSetContract {

	Search build(final List<String> words) {
		final SuffixTrie t = new SuffixTrie();
		for (final String w : words) t.insert(w);
		return t::search;
	}

	@Test
	void countPrefixCountsWordsEndingWithSuffix() {
		final SuffixTrie t = new SuffixTrie().insert("running").insert("sing").insert("song").insert("sing");
		assertEquals(3, t.countPrefix("ing"));
		assertEquals(1, t.countPrefix("ong"));
		assertEquals(0, t.countPrefix("x"));
	}
}
