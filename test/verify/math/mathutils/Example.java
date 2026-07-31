package verify.math.mathutils;

import static java.lang.System.*;

import lib.math.*;

public final class Example {

	public static void main(String[] args) {
		out.println(MathUtils.pow(2, 10));
		out.println(MathUtils.modPow(2, 10, 1_000_000_007));
	}

}
