package unit.ds.trie;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.ds.trie.*;

import static org.junit.jupiter.api.Assertions.*;

final class TernarySearchTreeTest extends StringSetContract {

	Search build(final List<String> words) {
		final TernarySearchTree t = new TernarySearchTree();
		for (final String w : words) t.insert(w);
		return t::search;
	}

	@Test
	void countPrefixCountsDistinctWords() {
		final TernarySearchTree t = new TernarySearchTree();
		for (final String w : List.of("apple", "app", "apple", "apply", "bat")) t.insert(w);
		assertEquals(3, t.countPrefix("app"));
		assertEquals(1, t.countPrefix("apple"));
		assertEquals(0, t.countPrefix("c"));
	}
}
