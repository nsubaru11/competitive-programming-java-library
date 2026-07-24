package verify.math.numbertheory.numbertheoryutils;

import static java.lang.System.*;

import lib.math.numbertheory.*;

public final class Example {

	public static void main(String[] args) {
		out.println(NumberTheoryUtils.gcd(923490024, 825000390));
		out.println(NumberTheoryUtils.fastGcd(923490024, 825000390));
		out.println(NumberTheoryUtils.lcm(12, 18));
	}

}
