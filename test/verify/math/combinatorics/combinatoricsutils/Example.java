package verify.math.combinatorics.combinatoricsutils;

import static java.lang.System.*;

import lib.math.combinatorics.*;

public final class Example {

	public static void main(String[] args) {
		out.println(CombinatoricsUtils.nCr(10, 3));
		out.println(CombinatoricsUtils.nCr(10, 3, 1_000_000_007L));
	}

}
