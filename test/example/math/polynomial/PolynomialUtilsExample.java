package example.math.polynomial;

import java.util.*;

import lib.math.polynomial.*;

public final class PolynomialUtilsExample {

	public static void main(final String[] args) {
		// 係数
		double[] a = {1, 2, 3};
		double[] b = {4, 5};

		// 四則演算
		System.out.println(Arrays.toString(PolynomialUtils.add(a, b)));
		System.out.println(Arrays.toString(PolynomialUtils.subtract(a, b)));
		System.out.println(Arrays.toString(PolynomialUtils.multiply(a, b)));
		System.out.println();

		// 微分・積分
		System.out.println(Arrays.toString(PolynomialUtils.differentiate(a)));
		System.out.println(Arrays.toString(PolynomialUtils.integrate(a, 0)));
		System.out.println();

		// 評価・次数
		System.out.println(PolynomialUtils.evaluate(a, 2));
		System.out.println(PolynomialUtils.degree(a));
	}
}
