package lib.math;

import static java.util.Arrays.*;

import java.util.*;
import java.util.stream.*;

/**
 * エラトステネスの篩で構築した素数テーブルです。
 * 構築時に指定した上限以下の素数判定・検索を行えます。
 */
@SuppressWarnings("unused")
public final class PrimeTable implements Iterable<Long> {
	private final long MAX_VALUE;
	private final long[] oddBits;
	private long[] primes;
	private int cnt;

	/**
	 * {@code 0} 以上 {@code n} 以下の素数テーブルを構築します。
	 *
	 * @param n テーブルの上限
	 */
	public PrimeTable(final long n) {
		cnt = 0;
		MAX_VALUE = n;
		oddBits = new long[(int) (((n >>> 1) + 63) >>> 6) + 1];
		sieve(n);
	}

	/**
	 * テーブル範囲内の値が素数か判定します。
	 * {@code 0 <= n <= MAX_VALUE} を前提とします。
	 *
	 * @param n 判定する値
	 * @return 素数なら {@code true}
	 */
	public boolean isPrime(final long n) {
		if (MAX_VALUE < n) throw new IllegalArgumentException();
		if (n == 1) return false;
		if ((n & 1) == 0) return n == 2;
		return !isCompositeOdd(n);
	}

	/**
	 * {@code n} 以下の素数の個数を返します。
	 *
	 * @param n 上限
	 * @return 素数の個数
	 */
	public int countPrimesUpTo(final long n) {
		if (MAX_VALUE < n) throw new IllegalArgumentException();
		int index = binarySearch(primes, n);
		return index < 0 ? ~index : index + 1;
	}

	/**
	 * {@code n} 以上で最小の素数を返します。
	 *
	 * @param n 下限
	 * @return 該当する素数。存在しない場合は {@code -1}
	 */
	public long ceilingPrime(final long n) {
		if (MAX_VALUE < n) throw new IllegalArgumentException();
		int index = binarySearch(primes, n);
		index = index < 0 ? ~index : index;
		return index >= cnt ? -1 : primes[index];
	}

	/**
	 * {@code n} より大きい最小の素数を返します。
	 *
	 * @param n 基準値
	 * @return 該当する素数。存在しない場合は {@code -1}
	 */
	public long higherPrime(final long n) {
		return ceilingPrime(n + 1);
	}

	/**
	 * {@code n} 以下で最大の素数を返します。
	 *
	 * @param n 上限
	 * @return 該当する素数。存在しない場合は {@code -1}
	 */
	public long floorPrime(final long n) {
		if (MAX_VALUE < n) throw new IllegalArgumentException();
		int index = binarySearch(primes, n);
		index = index < 0 ? ~index - 1 : index;
		return index < 0 ? -1 : primes[index];
	}

	/**
	 * {@code n} より小さい最大の素数を返します。
	 *
	 * @param n 基準値
	 * @return 該当する素数。存在しない場合は {@code -1}
	 */
	public long lowerPrime(final long n) {
		return floorPrime(n - 1);
	}

	/**
	 * 0 始まりで {@code i} 番目の素数を返します。
	 *
	 * @param i 素数の添字
	 * @return {@code i} 番目の素数
	 */
	public long kthPrime(final int i) {
		if (i < 0 || cnt <= i) throw new IllegalArgumentException();
		return primes[i];
	}

	/**
	 * 構築済みの素数列を利用して素因数分解します。
	 * 既に列挙済みの素数を利用するため、同じテーブルで複数回分解する場合に適しています。
	 * 構築上限が元の {@code n} の平方根以上であることを前提とします。
	 *
	 * @param n 分解する非負整数
	 * @return 素因数をキー、指数を値とする Map
	 */
	public Map<Long, Integer> primeFactorize(long n) {
		if (n < 0) throw new IllegalArgumentException();
		Map<Long, Integer> factors = new HashMap<>();
		boolean covered = false;
		for (int i = 0; i < cnt && n > 1; i++) {
			final long prime = primes[i];
			if (prime * prime > n) {
				covered = true;
				break;
			}
			while (n % prime == 0) {
				factors.merge(prime, 1, Integer::sum);
				n /= prime;
			}
		}
		if (n > 1) {
			if (!covered && n / MAX_VALUE > MAX_VALUE) throw new IllegalArgumentException();
			factors.merge(n, 1, Integer::sum);
		}
		return factors;
	}

	/**
	 * テーブル内の素数を昇順に走査する iterator を返します。
	 *
	 * @return 素数を昇順に返す iterator
	 */
	public PrimitiveIterator.OfLong iterator() {
		return new PrimitiveIterator.OfLong() {
			private int index = 0;

			public boolean hasNext() {
				return index < cnt;
			}

			public long nextLong() {
				if (!hasNext()) throw new NoSuchElementException();
				return primes[index++];
			}
		};
	}

	/**
	 * テーブル内の素数を昇順の {@link LongStream} として返します。
	 *
	 * @return 素数の昇順 stream
	 */
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
