package unit.string.palindrome;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.string.palindrome.*;

import unit.string.Naive;

import static org.junit.jupiter.api.Assertions.*;

final class ManacherTest {

	@Nested
	class Access {

		@Test
		void example() {
			final Manacher m = new Manacher("abaab");
			// 仮想中心は文字と文字の間を含めて 2n - 1 個
			assertArrayEquals(new int[]{1, 0, 3, 0, 1, 4, 1, 0, 1}, m.getPalindromeLengths());
			assertEquals(4, m.getLongestLen());
			assertEquals(3, m.getOddLongestLen());
			assertEquals(4, m.getEvenLongestLen());
			assertEquals("baab", m.getLongestPalindrome());
			assertEquals("aba", m.getLongestOddPalindrome());
			assertEquals("baab", m.getLongestEvenPalindrome());
			assertEquals(3, m.getOddPalindromeLengthAt(1));
			assertEquals(4, m.getEvenPalindromeLengthAt(2));
			assertEquals("aba", m.getOddPalindromeAt(1));
			assertEquals("baab", m.getEvenPalindromeAt(2));
			assertTrue(m.isPalindromeRange(1, 5));
			assertFalse(m.isPalindromeRange(0, 4));
			assertFalse(m.isPalindromeRange(2, 2), "空区間は false");
			assertFalse(m.isPalindromeRange(0, 6), "範囲外は false");
		}

		@Test
		void outOfRangeCenterThrows() {
			final Manacher m = new Manacher("abc".toCharArray());
			assertThrows(IllegalArgumentException.class, () -> m.getOddPalindromeLengthAt(3));
			assertThrows(IllegalArgumentException.class, () -> m.getEvenPalindromeLengthAt(2), "偶数長の中心は n - 2 まで");
			assertThrows(IllegalArgumentException.class, () -> m.getOddPalindromeAt(-1));
			assertThrows(IllegalArgumentException.class, () -> m.getEvenPalindromeAt(2));
		}
	}

	@RepeatedTest(200)
	void matchesNaive(final RepetitionInfo info) {
		final Random random = new Random(info.getCurrentRepetition());
		final String s = Naive.randomString(random, 20, 2);
		if (s.isEmpty()) return;
		final Manacher m = new Manacher(s);
		final String at = "s=" + s;
		assertEquals(Naive.longestPalindrome(s, true, true), m.getLongestLen(), at);
		assertEquals(Naive.longestPalindrome(s, true, false), m.getOddLongestLen(), at);
		assertEquals(Naive.longestPalindrome(s, false, true), m.getEvenLongestLen(), at);
		final String longest = m.getLongestPalindrome();
		assertEquals(m.getLongestLen(), longest.length(), at);
		assertTrue(s.contains(longest) && Naive.isPalindrome(longest, 0, longest.length()), at);
		for (int c = 0; c < s.length(); c++) {
			final int odd = Naive.palindromeAt(s, c, true);
			assertEquals(odd, m.getOddPalindromeLengthAt(c), at + ", c=" + c);
			assertEquals(s.substring(c - odd / 2, c + odd / 2 + 1), m.getOddPalindromeAt(c), at + ", c=" + c);
			if (c + 1 < s.length()) {
				final int even = Naive.palindromeAt(s, c, false);
				assertEquals(even, m.getEvenPalindromeLengthAt(c), at + ", c=" + c);
				assertEquals(s.substring(c + 1 - even / 2, c + 1 + even / 2), m.getEvenPalindromeAt(c), at + ", c=" + c);
			}
		}
		for (int l = 0; l <= s.length(); l++) {
			for (int r = l; r <= s.length(); r++) {
				assertEquals(l < r && Naive.isPalindrome(s, l, r), m.isPalindromeRange(l, r), at + ", [" + l + ", " + r + ")");
			}
		}
	}
}
