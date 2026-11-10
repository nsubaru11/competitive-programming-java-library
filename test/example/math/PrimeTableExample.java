package example.math;

import java.util.*;

import lib.math.*;

public final class PrimeTableExample {

	public static void main(final String[] args) {
		// 構築
		PrimeTable pt = new PrimeTable(100);
		System.out.println(pt.getPrimeCount());
		System.out.println(pt.get(0));
		System.out.println();

		// 判定・個数
		System.out.println(pt.isPrime(97));
		System.out.println(pt.countPrimesUpTo(50));
		System.out.println();

		// 近傍
		System.out.println(pt.floorPrime(50));
		System.out.println(pt.ceilingPrime(50));
		System.out.println();

		// 素因数分解
		System.out.println(Arrays.toString(pt.primeFactors(360)));
		System.out.println(Arrays.deepToString(pt.primeFactors2D(360)));
		System.out.println();

		// 列挙
		long sum = 0;
		for (long p : pt) {
			sum += p;
		}
		System.out.println(sum);
	}
}
