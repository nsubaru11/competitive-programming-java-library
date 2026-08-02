package verify.math.mathutils;

import static java.lang.System.*;

import lib.math.*;

public final class Example {

	public static void main(String[] args) {
		out.println(MathUtils.pow(2, 10));
		out.println(MathUtils.modPow(2, 10, 1_000_000_007));
		out.println(MathUtils.floorSqrt(10));
		out.println(MathUtils.ceilSqrt(10));
		out.println(MathUtils.floorLog10(121));
		out.println(MathUtils.ceilLog10(121));
	}

}
