public final class Example {

	public static void main(String[] args) {
		PrecomputedPrimes primes = new PrecomputedPrimes(100000000);
		long sum = 0;
		for (long p : primes) {
			sum += p;
			System.out.println(p);
		}
		System.out.println(sum);
	}
}
