package example.string.palindrome;

import java.util.*;

import lib.string.palindrome.*;

public final class ManacherExample {

	public static void main(final String[] args) {
		// 構築
		Manacher mc = new Manacher("abacabaxyyx");
		System.out.println(Arrays.toString(mc.getPalindromeLengths()));
		System.out.println();

		// 最長回文
		System.out.println(mc.getLongestLen());
		System.out.println(mc.getLongestPalindrome());
		System.out.println(mc.getLongestEvenPalindrome());
		System.out.println();

		// 中心指定
		System.out.println(mc.getOddPalindromeLengthAt(3));
		System.out.println(mc.getOddPalindromeAt(3));
		System.out.println(mc.getEvenPalindromeAt(8));
		System.out.println();

		// 区間判定
		System.out.println(mc.isPalindromeRange(0, 7));
		System.out.println(mc.isPalindromeRange(0, 4));
	}
}
