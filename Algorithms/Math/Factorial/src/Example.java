public final class Example {

	public static void main(final String[] args) {
		final int max = args.length > 0 ? Integer.parseInt(args[0]) : 100000000;
		final int mod = 998244353;

		FactorialTable ft = new FactorialTable(max, mod);
		boolean valid = true;
		for (int i = 0; i < max; i++) {
			assert ft.fact(i) == FactorialNaive.modFact(i, mod);
			assert ft.invFact(i) == FactorialNaive.invFact(i, mod);
			if (valid && FactorialNaive.fact(i) < 0) {
				valid = false;
				System.out.println(i);
			}
		}
	}

}
