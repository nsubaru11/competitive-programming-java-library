package unit.ds.trie;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.ds.trie.*;

import static org.junit.jupiter.api.Assertions.*;

final class TrieTest extends StringSetContract {

	Search build(final List<String> words) {
		final Trie t = new Trie();
		for (final String w : words) t.insert(w);
		return t::search;
	}

	@Test
	void countPrefixCountsInsertions() {
		final Trie t = new Trie().insert("apple").insert("app").insert("apple").insert("bat");
		assertEquals(3, t.countPrefix("app"));
		assertEquals(2, t.countPrefix("apple"));
		assertEquals(0, t.countPrefix("c"));
	}
}
