package unit.ds.trie;

import java.util.*;

import lib.ds.trie.*;

final class RadixTrieTest extends StringSetContract {

	Search build(final List<String> words) {
		final RadixTrie t = new RadixTrie();
		for (final String w : words) t.insert(w);
		return t::search;
	}
}
