import java.util.*;

/**
 * 素数に関するクラス
 */
@SuppressWarnings("unused")
public class PrecomputedPrimes implements Iterable<Integer> {
	private final int MAX_VALUE;
	private final BitSet table;
	private final ArrayList<Integer> primes;

	public PrecomputedPrimes(int n) {
		MAX_VALUE = n;
		primes = new ArrayList<>();
		table = new BitSet(n + 1);
		int index = 1;
		if (2 <= n) primes.add(2);
		if (3 <= n) primes.add(3);
		for (int i = 4; i <= n; i += 2) table.set(i);
		for (int i = 9; i <= n; i += 6) table.set(i);
		for (int i = 5; i <= n; i += 6) {
			for (int j = i; j <= i + 2 && j <= n; j += 2) {
				if (!table.get(j)) {
					primes.add(j);
					for (long k = (long) j * j; k <= n; k += (long) j + j) {
						table.set((int) k);
					}
				}
			}
		}
	}

	public boolean isPrime(int n) {
		if (MAX_VALUE < n) throw new IllegalArgumentException();
		return !table.get(n);
	}

	public int countPrimesUpTo(int n) {
		if (MAX_VALUE < n) throw new IllegalArgumentException();
		int index = Collections.binarySearch(primes, n);
		return index < 0 ? ~index : index + 1;
	}

	public int ceilingPrime(int n) {
		if (MAX_VALUE < n) throw new IllegalArgumentException();
		int index = Collections.binarySearch(primes, n);
		return primes.get(index < 0 ? ~index : index);
	}


	public int higherPrime(int n) {
		return ceilingPrime(n + 1);
	}

	public int floorPrime(int n) {
		if (MAX_VALUE < n) throw new IllegalArgumentException();
		int index = Collections.binarySearch(primes, n);
		return primes.get(index < 0 ? ~index - 1 : index);
	}

	public int lowerPrime(int n) {
		return floorPrime(n - 1);
	}

	public int kthPrime(int i) {
		if (primes.size() < i) throw new IllegalArgumentException();
		return primes.get(i);
	}

	public Iterator<Integer> iterator() {
		return new Iterator<>() {
			private int index = 0;

			public boolean hasNext() {
				return index < primes.size();
			}

			public Integer next() {
				if (!hasNext())
					throw new NoSuchElementException();
				return primes.get(index++);
			}
		};
	}

}
