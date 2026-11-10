package example.math;

import java.util.*;

import lib.math.*;

public final class PrimeUtilsExample {

	public static void main(final String[] args) {
		// 素数判定
		System.out.println(PrimeUtils.isPrime(97));
		System.out.println(PrimeUtils.isPrime(1_000_000_007L));
		System.out.println(PrimeUtils.isProbablePrime(998_244_353L, 10));
		System.out.println();

		// 素数の個数
		System.out.println(PrimeUtils.primeCount(100));
		System.out.println(PrimeUtils.primeCount(50, 100));
		System.out.println();

		// 素数の列挙
		System.out.println(Arrays.toString(PrimeUtils.eratosthenes(10, 50)));
		List<Integer> list = PrimeUtils.eratosthenes(90, 110, ArrayList::new);
		System.out.println(list);
	}
}
