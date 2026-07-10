package verify.math.combinatorics;

import lib.math.*;

import static java.lang.System.*;

public final class Example {

	public static void main(String[] args) {
		out.println(CombinatoricsUtils.comb(10, 3));
		out.println(CombinatoricsUtils.modComb(10, 3, 1_000_000_007L));
	}

}
