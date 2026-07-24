package verify.math.number;

import static java.lang.System.*;

import lib.math.number.*;

public final class Example {

	public static void main(String[] args) {
		out.println(PowerUtils.pow(2, 10));
		out.println(PowerUtils.modPow(2, 10, 1_000_000_007L));
		out.println(NumberPredicates.isPalindrome(12321));
	}

}
