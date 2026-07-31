package verify.math.factorialtable;

import lib.math.*;

public final class Test {

	public static void main(final String[] args) {
		final int max = args.length > 0 ? Integer.parseInt(args[0]) : 100000;
		final int mod = 998244353;

		FactorialTable ft = new FactorialTable(0, mod);
		long expectedFact = 1;
		for (int i = 0; i <= max; i++) {
			if (i > 0) expectedFact = expectedFact * i % mod;
			if (ft.fact(i) != expectedFact) throw new AssertionError("fact: " + i);
			if ((long) ft.fact(i) * ft.invFact(i) % mod != 1) {
				throw new AssertionError("invFact: " + i);
			}
		}
		if (ft.nCr(10, 3) != 120) throw new AssertionError("nCr");
		if (ft.nPr(10, 3) != 720) throw new AssertionError("nPr");
	}

}
