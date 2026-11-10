package example.math;

import java.util.*;

import lib.math.*;

public final class FactorUtilsExample {

	public static void main(final String[] args) {
		// 素因数の個数
		System.out.println(FactorUtils.uniquePrimeFactorCount(360));
		System.out.println(FactorUtils.primeFactorCount(360));
		System.out.println();

		// 素因数分解
		System.out.println(Arrays.toString(FactorUtils.primeFactors(360)));
		System.out.println(Arrays.deepToString(FactorUtils.primeFactors2D(360)));
		Map<Long, Integer> map = FactorUtils.primeFactorsMap(360L, TreeMap::new);
		System.out.println(map);
		System.out.println();

		// 約数
		System.out.println(FactorUtils.divisorCount(36));
		System.out.println(Arrays.toString(FactorUtils.divisors(36)));
		List<Integer> list = FactorUtils.<ArrayList<Integer>>divisors(12, ArrayList::new);
		System.out.println(list);
	}
}
