package unit.ds.trie;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.ds.trie.*;

import static org.junit.jupiter.api.Assertions.*;

final class DoubleArrayTrieTest extends StringSetContract {

	Search build(final List<String> words) {
		final DoubleArrayTrie t = new DoubleArrayTrie(4);
		t.build(words.toArray(new String[0]));
		return t::search;
	}

	@Test
	void emptyBuild() {
		final DoubleArrayTrie t = new DoubleArrayTrie(16);
		t.build(new String[0]);
		assertFalse(t.search("a"));
	}
}
