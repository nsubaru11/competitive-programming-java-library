package unit.ds.trie;

import java.util.*;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 文字列の集合として使う trie（Trie・RadixTrie・TernarySearchTree・SuffixTrie・DoubleArrayTrie）に共通する振る舞いのテスト。
 * 単語は1文字以上の英小文字とする。
 */
abstract class StringSetContract {

	/**
	 * words をすべて登録した集合を作り、search を返す。
	 */
	abstract Search build(List<String> words);

	interface Search {
		boolean search(String word);
	}

	@Nested
	class Access {

		@Test
		void exactMatchOnly() {
			final Search s = build(List.of("test", "team", "toast", "te"));
			assertTrue(s.search("test"));
			assertTrue(s.search("te"));
			assertTrue(s.search("toast"));
			assertFalse(s.search("t"), "登録した単語の接頭辞");
			assertFalse(s.search("tes"));
			assertFalse(s.search("tests"), "登録した単語より長い");
			assertFalse(s.search("tea"));
			assertFalse(s.search("x"));
		}

		@Test
		void duplicatesAndPrefixChains() {
			final Search s = build(List.of("a", "ab", "abc", "ab", "abcd"));
			for (final String w : List.of("a", "ab", "abc", "abcd")) assertTrue(s.search(w), w);
			assertFalse(s.search("abcde"));
			assertFalse(s.search("b"));
		}
	}

	@RepeatedTest(100)
	void matchesNaive(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final List<String> words = new ArrayList<>();
		for (int i = random.nextInt(1, 30); i > 0; i--) words.add(randomWord(random));
		final Set<String> naive = new HashSet<>(words);
		final Search s = build(words);
		for (int q = 0; q < 200; q++) {
			final String w = q % 3 == 0 ? words.get(random.nextInt(words.size())) : randomWord(random);
			assertEquals(naive.contains(w), s.search(w), "seed=" + seed + ", words=" + words + ", query=" + w);
		}
	}

	static String randomWord(final Random random) {
		final StringBuilder sb = new StringBuilder();
		for (int i = random.nextInt(1, 6); i > 0; i--) sb.append((char) ('a' + random.nextInt(3)));
		return sb.toString();
	}
}
