package verify.math.factorutils;

import java.util.*;

import lib.math.*;

public final class Test {

	public static void main(String[] args) {
		if (FactorUtils.uniquePrimeFactorCount(360) != 3) throw new AssertionError();
		if (FactorUtils.primeFactorCount(360) != 6) throw new AssertionError();
		if (FactorUtils.divisorCount(360) != 24) throw new AssertionError();
		if (!Arrays.equals(FactorUtils.primeFactors(360), new int[]{2, 2, 2, 3, 3, 5})) {
			throw new AssertionError();
		}
		if (!Arrays.deepEquals(FactorUtils.primeFactors2D(360), new int[][]{{2, 3, 5}, {3, 2, 1}})) {
			throw new AssertionError();
		}
		if (!Arrays.deepEquals(FactorUtils.primeFactors2D(360L), new long[][]{{2, 3, 5}, {3, 2, 1}})) {
			throw new AssertionError();
		}
		if (!Arrays.equals(FactorUtils.divisors(36), new int[]{1, 2, 3, 4, 6, 9, 12, 18, 36})) {
			throw new AssertionError();
		}
		List<Integer> divisors = FactorUtils.<ArrayList<Integer>>divisors(12, ArrayList::new);
		if (!divisors.equals(List.of(1, 2, 3, 4, 6, 12))) throw new AssertionError();
	}

}
