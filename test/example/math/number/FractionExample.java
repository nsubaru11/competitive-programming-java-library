package example.math.number;

import lib.math.number.*;

public final class FractionExample {

	public static void main(final String[] args) {
		// 生成
		Fraction a = new Fraction(1, 3);
		Fraction b = new Fraction(2, 4);
		System.out.println(a);
		System.out.println(b);
		System.out.println();

		// 四則演算
		System.out.println(a.add(b));
		System.out.println(a.subtract(b));
		System.out.println(a.multiply(b));
		System.out.println(a.divide(b));
		System.out.println(a.add(2));
		System.out.println();

		// 分子・分母・逆数
		System.out.println(b.getNumerator() + " " + b.getDenominator());
		System.out.println(b.getInverse());
		System.out.println(a.getDecimal());
		System.out.println();

		// 累乗（破壊的）
		Fraction c = new Fraction(a);
		c.pow(3);
		System.out.println(c);
		System.out.println(a.equals(new Fraction(2, 6)));
	}
}
