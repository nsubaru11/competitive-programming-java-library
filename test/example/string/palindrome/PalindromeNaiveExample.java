package example.string.palindrome;

import lib.string.palindrome.*;

public final class PalindromeNaiveExample {

	public static void main(final String[] args) {
		// 判定
		System.out.println(PalindromeNaive.isPalindrome("racecar"));
		System.out.println(PalindromeNaive.isPalindrome("hello"));
		System.out.println();

		// 区間判定
		String s = "levelupracecar";
		System.out.println(PalindromeNaive.isPalindromeRange(s, 0, 5));
		System.out.println(PalindromeNaive.isPalindromeRange(s, 7, 14));
		System.out.println();

		// 最長回文
		System.out.println(PalindromeNaive.findLongestPalindrome("babad"));
		System.out.println();

		// 回文化
		System.out.println(PalindromeNaive.makePalindrome("abc"));
		System.out.println(PalindromeNaive.makePalindrome("abb"));
	}
}
