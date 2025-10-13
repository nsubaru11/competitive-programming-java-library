public final class Example {

	public static void main(String[] args) {
		PreComputedFactorials fact = new PreComputedFactorials(13);
		System.out.println(fact.factorial(10));
		System.out.println(FactorialUtils.factorial(5));
	}

}
