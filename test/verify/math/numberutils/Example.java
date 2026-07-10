package verify.math.numberutils;

import lib.math.*;

import static java.lang.System.*;

public final class Example {

	public static void main(String[] args) {
		out.println(PowerUtils.pow(2, 10));
		out.println(PowerUtils.modPow(2, 10, 1_000_000_007L));
		out.println(NumberFormatUtils.toPaddedString(7, 3));
		out.println(NumberPredicates.isPalindrome(12321));
	}

}
