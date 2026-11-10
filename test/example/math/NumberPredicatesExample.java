package example.math;

import lib.math.*;

public final class NumberPredicatesExample {

	public static void main(final String[] args) {
		// 回文数
		System.out.println(NumberPredicates.isPalindrome(12321));
		System.out.println(NumberPredicates.isPalindrome(12345));
		System.out.println();

		// 完全数
		System.out.println(NumberPredicates.isPerfectNumber(28));
		System.out.println();

		// フィボナッチ数
		System.out.println(NumberPredicates.isFibonacci(21));
		System.out.println(NumberPredicates.isFibonacci(22));
		System.out.println();

		// アームストロング数
		System.out.println(NumberPredicates.isArmstrong(153));
		System.out.println();

		// ハッピー数
		System.out.println(NumberPredicates.isHappyNumber(19));
		System.out.println(NumberPredicates.isHappyNumber(4));
	}
}
