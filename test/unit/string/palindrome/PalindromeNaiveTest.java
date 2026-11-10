package unit.string.palindrome;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.string.palindrome.*;

import unit.string.Naive;

import static org.junit.jupiter.api.Assertions.*;

final class PalindromeNaiveTest {

	@Nested
	class Access {

		@Test
		void examples() {
			assertTrue(PalindromeNaive.isPalindrome("racecar"));
			assertTrue(PalindromeNaive.isPalindrome("abba".toCharArray()));
			assertFalse(PalindromeNaive.isPalindrome("ab"));
			assertTrue(PalindromeNaive.isPalindromeRange("xabay", 1, 4), "終了は含まない");
			assertTrue(PalindromeNaive.isPalindromeRange("xabay".toCharArray(), 1, 4));
			assertFalse(PalindromeNaive.isPalindromeRange("xabay", 0, 4));
			assertFalse(PalindromeNaive.isPalindromeRange("abc", 1, 1), "空区間は false");
			assertFalse(PalindromeNaive.isPalindromeRange("abc", 0, 4), "範囲外は false");
			assertEquals("aba", PalindromeNaive.findLongestPalindrome("xabay"));
			assertEquals("", PalindromeNaive.findLongestPalindrome(""));
			assertEquals("abcba", PalindromeNaive.makePalindrome("abc"));
			assertEquals("ababa", PalindromeNaive.makePalindrome("abab"));
			assertEquals("aba", PalindromeNaive.makePalindrome("aba"));
		}
	}

	@RepeatedTest(200)
	void matchesNaive(final RepetitionInfo info) {
		final Random random = new Random(info.getCurrentRepetition());
		final String s = Naive.randomString(random, 12, 2);
		final String at = "s=" + s;
		assertEquals(Naive.isPalindrome(s, 0, s.length()), PalindromeNaive.isPalindrome(s), at);
		assertEquals(Naive.isPalindrome(s, 0, s.length()), PalindromeNaive.isPalindrome(s.toCharArray()), at);
		final String longest = PalindromeNaive.findLongestPalindrome(s);
		assertEquals(Naive.longestPalindrome(s, true, true), longest.length(), at);
		assertTrue(s.contains(longest), at);
		for (int l = 0; l <= s.length(); l++) {
			for (int r = l; r <= s.length(); r++) assertEquals(l < r && Naive.isPalindrome(s, l, r), PalindromeNaive.isPalindromeRange(s, l, r), at);
		}
		if (s.isEmpty()) return;
		final String made = PalindromeNaive.makePalindrome(s);
		assertTrue(made.startsWith(s) && Naive.isPalindrome(made, 0, made.length()), at + ", made=" + made);
		// 末尾に足す文字数は最小
		for (int k = 0; k < made.length() - s.length(); k++) {
			final String shorter = s + new StringBuilder(s.substring(0, k)).reverse();
			assertFalse(Naive.isPalindrome(shorter, 0, shorter.length()), at + ", k=" + k);
		}
	}
}
