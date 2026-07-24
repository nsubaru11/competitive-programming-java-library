package verify.math.polynomial.polynomialutils;

import static java.lang.System.*;

import lib.math.polynomial.*;

public final class Example {

	public static void main(String[] args) {
		double[] a = {1, 2, 3};
		double[] b = {4, 5};
		out.println(java.util.Arrays.toString(PolynomialUtils.add(a, b)));
		out.println(java.util.Arrays.toString(PolynomialUtils.multiply(a, b)));
	}

}
