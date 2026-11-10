package example.math;

import lib.math.*;

public final class MathUtilsExample {

	public static void main(final String[] args) {
		// 累乗
		System.out.println(MathUtils.pow(2, 10));
		System.out.println(MathUtils.modPow(2, 100, 1_000_000_007));
		System.out.println(MathUtils.modInv(3, 1_000_000_007));
		System.out.println();

		// 平方根・対数
		System.out.println(MathUtils.floorSqrt(10));
		System.out.println(MathUtils.ceilSqrt(10));
		System.out.println(MathUtils.floorLog2(1000));
		System.out.println(MathUtils.ceilLog10(121));
		System.out.println();

		// 最大公約数・最小公倍数
		System.out.println(MathUtils.gcd(923490024, 825000390));
		System.out.println(MathUtils.lcm(12, 18));
		System.out.println();

		// 組み合わせ
		System.out.println(MathUtils.fact(10));
		System.out.println(MathUtils.nCr(10, 3));
		System.out.println(MathUtils.nCr(100, 50, 1_000_000_007L));
		System.out.println(MathUtils.nPr(10, 3));
		System.out.println();

		// 数論
		System.out.println(MathUtils.isPrime(97));
		System.out.println(MathUtils.eulerTotient(36));
	}
}
