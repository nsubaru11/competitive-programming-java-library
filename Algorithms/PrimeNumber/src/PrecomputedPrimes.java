import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.stream.LongStream;

import static java.util.Arrays.binarySearch;
import static java.util.Arrays.copyOf;

/**
 * エラトステネスの篩を用いて最大Integer.MAXVALUEの値
 */
@SuppressWarnings("unused")
public final class PrecomputedPrimes implements Iterable<Long> {
	private final long MAX_VALUE;
	private final long[] oddBits;
	private long[] primes;
	private int cnt;

	public PrecomputedPrimes(final long n) {
		cnt = 0;
		MAX_VALUE = n;
		oddBits = new long[(int) (((n >>> 1) + 63) >>> 6) + 1];
		sieve(n);
	}

	public boolean isPrime(final long n) {
		if (MAX_VALUE < n) throw new IllegalArgumentException();
		if ((n & 1) == 0) return n == 2;
		return !isCompositeOdd(n);
	}

	public int countPrimesUpTo(final long n) {
		if (MAX_VALUE < n) throw new IllegalArgumentException();
		int index = binarySearch(primes, n);
		return index < 0 ? ~index : index + 1;
	}

	public long ceilingPrime(final long n) {
		if (MAX_VALUE < n) throw new IllegalArgumentException();
		int index = binarySearch(primes, n);
		index = index < 0 ? ~index : index;
		return index >= cnt ? -1 : primes[index];
	}

	public long higherPrime(final long n) {
		return ceilingPrime(n + 1);
	}

	public long floorPrime(final long n) {
		if (MAX_VALUE < n) throw new IllegalArgumentException();
		int index = binarySearch(primes, n);
		index = index < 0 ? ~index - 1 : index;
		return index < 0 ? -1 : primes[index];
	}

	public long lowerPrime(final long n) {
		return floorPrime(n - 1);
	}

	public long kthPrime(final int i) {
		if (i < 0 || cnt <= i) throw new IllegalArgumentException();
		return primes[i];
	}

	public Map<Long, Integer> primeFactorize(long n) {
		if (n < 0) throw new IllegalArgumentException();
		Map<Long, Integer> factors = new HashMap<>();
		int i = 0;
		while (n > 1) {
			if (i >= cnt) throw new IllegalArgumentException();
			long prime = primes[i++];
			while (n % prime == 0) {
				factors.put(prime, factors.getOrDefault(prime, 0) + 1);
				n /= prime;
			}
		}
		return factors;
	}

	public PrimitiveIterator.OfLong iterator() {
		return new PrimitiveIterator.OfLong() {
			private int index = 0;

			@Override
			public boolean hasNext() {
				return index < cnt;
			}

			@Override
			public long nextLong() {
				if (!hasNext()) throw new NoSuchElementException();
				return primes[index++];
			}
		};
	}

	public LongStream stream() {
		return Arrays.stream(primes);
	}

	private void sieve(final long n) {
		int estimatedCapacity = n < 20 ? 10 : (int) (1.05 * n / (Math.log(n) - 1.0));
		primes = new long[estimatedCapacity];
		final long sqrtN = (long) Math.sqrt(n);

		if (2 <= n) primes[cnt++] = 2;
		if (3 <= n) primes[cnt++] = 3;

		for (long i = 9; i <= n; i += 6) setCompositeOdd(i);
		for (long i = 5; i <= n; i += 6) {
			for (long j = i; j <= i + 2 && j <= n; j += 2) {
				if (isCompositeOdd(j)) continue;
				primes[cnt++] = j;
				if (j > sqrtN) continue;
				final long step = j * 6;
				final long delta2 = j * 2;
				for (long k = j * j; k <= n; k += step) {
					setCompositeOdd(k);
					long k2 = k + delta2;
					if (k2 > n) break;
					setCompositeOdd(k2);
					long k4 = k2 + delta2;
					if (k4 > n) break;
					setCompositeOdd(k4);
				}
			}
		}
		primes = copyOf(primes, cnt);
	}

	private int bitIndex(final long n) {
		return (int) (n >>> 7);
	}

	private long bitMask(final long n) {
		return 1L << ((n >>> 1) & 63);
	}

	private void setCompositeOdd(final long n) {
		oddBits[bitIndex(n)] |= bitMask(n);
	}

	private boolean isCompositeOdd(final long n) {
		return (oddBits[bitIndex(n)] & bitMask(n)) != 0;
	}
}
