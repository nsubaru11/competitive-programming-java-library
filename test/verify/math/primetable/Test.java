package verify.math.primetable;

import lib.math.*;

public final class Test {

	public static void main(String[] args) {
		PrimeTable primes = new PrimeTable(100000000);
		long sum = 0;
		for (long p : primes) {
			sum += p;
			System.out.println(p);
		}
		System.out.println(sum);
	}
}
