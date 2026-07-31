package verify.math.factorutils;

import static java.lang.System.*;

import java.util.*;

import lib.math.*;

public final class Example {

	public static void main(String[] args) {
		out.println(FactorUtils.uniquePrimeFactorCount(360));
		out.println(FactorUtils.primeFactorCount(360));
		out.println(FactorUtils.divisorCount(360));
		out.println(Arrays.toString(FactorUtils.primeFactors(360)));
		out.println(Arrays.toString(FactorUtils.divisors(36)));

		List<Integer> divisors = FactorUtils.<ArrayList<Integer>>divisors(12, ArrayList::new);
		out.println(divisors);
	}

}
