package example.math.mathutils;

import lib.math.*;

import static java.lang.System.*;

public final class CombinatoricsExample {

	public static void main(String[] args) {
		out.println(MathUtils.nCr(10, 3));
		out.println(MathUtils.nCr(10, 3, 1_000_000_007L));
	}

}
