package lib.math;

import static java.util.Arrays.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * エラトステネスの篩で構築した素数テーブルです。
 * 構築時に指定した上限以下の素数判定・検索を行えます。
 */
@SuppressWarnings("unused")
public final class PrimeTable implements Iterable<Long> {
	private final long maxValue;
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
		maxValue = n;
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
		if (maxValue < n) throw new IllegalArgumentException();
		if (n <= 1) return false;
		if ((n & 1) == 0) return n == 2;
		if (n % 3 == 0) return n == 3;
		return isNotCompositeOdd(n);
	}

	/**
	 * {@code n} 以下の素数の個数を返します。
	 *
	 * @param n 上限
	 * @return 素数の個数
	 */
	public int countPrimesUpTo(final long n) {
		if (maxValue < n) throw new IllegalArgumentException();
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
		if (maxValue < n) throw new IllegalArgumentException();
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
		if (maxValue < n) throw new IllegalArgumentException();
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
	 * 異なる素因数の個数を返します。
	 * 構築済みの素数列を利用するため、同じテーブルで複数回計算する場合に適しています。
	 * 構築上限が元の {@code n} の平方根以上であることを前提とします。
	 *
	 * @param n 素因数を数える整数
	 * @return 異なる素因数の個数。{@code n <= 1} では 0
	 */
	public int uniquePrimeFactorCount(long n) {
		if (n <= 1) return 0;
		int count = 0;
		for (int i = 0; i < this.cnt && n > 1; i++) {
			final long pi = primes[i];
			if (pi * pi > n) break;
			int e = 0;
			for (; n % pi == 0; n /= pi, e++) ;
			if (e > 0) count++;
		}
		return n > 1 ? count + 1 : count;
	}

	/**
	 * 重複を含む素因数の個数を返します。
	 * 構築上限が元の {@code n} の平方根以上であることを前提とします。
	 *
	 * @param n 素因数を数える整数
	 * @return 重複を含む素因数の個数。{@code n <= 1} では 0
	 */
	public int primeFactorCount(long n) {
		if (n <= 1) return 0;
		int count = 0;
		for (int i = 0; i < cnt && n > 1; i++) {
			final long pi = primes[i];
			if (pi * pi > n) break;
			for (; n % pi == 0; n /= pi, count++) ;
		}
		return n > 1 ? count + 1 : count;
	}

	/**
	 * 素因数を重複込みの昇順配列で返します。
	 * 構築上限が元の {@code n} の平方根以上であることを前提とします。
	 *
	 * @param n 分解する正の整数
	 * @return 素因数の昇順配列。{@code n <= 1} では空配列
	 */
	public int[] primeFactors(int n) {
		if (n <= 1) return new int[0];
		final int[] factors = new int[32];
		int size = 0;
		for (int i = 0; i < cnt && n > 1; i++) {
			final int pi = (int) primes[i];
			if (pi * pi > n) break;
			for (; n % pi == 0; n /= pi) factors[size++] = pi;
		}
		if (n > 1) factors[size++] = n;
		return copyOf(factors, size);
	}

	/**
	 * 素因数を重複込みの昇順配列で返します。
	 * 構築上限が元の {@code n} の平方根以上であることを前提とします。
	 *
	 * @param n 分解する正の整数
	 * @return 素因数の昇順配列。{@code n <= 1} では空配列
	 */
	public long[] primeFactors(long n) {
		if (n <= 1) return new long[0];
		final long[] factors = new long[64];
		int size = 0;
		for (int i = 0; i < cnt && n > 1; i++) {
			final long pi = primes[i];
			if (pi * pi > n) break;
			for (; n % pi == 0; n /= pi) factors[size++] = pi;
		}
		if (n > 1) factors[size++] = n;
		return copyOf(factors, size);
	}

	/**
	 * 素因数を重複込みの昇順で指定されたコレクションへ追加します。
	 * 構築上限が元の {@code n} の平方根以上であることを前提とします。
	 *
	 * @param n        分解する正の整数
	 * @param supplier 結果コレクションの生成器
	 * @param <T>      コレクション型
	 * @return 素因数を格納したコレクション。{@code n <= 1} では空
	 */
	public <T extends Collection<Integer>> T primeFactors(int n, final Supplier<T> supplier) {
		final T factors = supplier.get();
		if (n <= 1) return factors;
		for (int i = 0; i < cnt && n > 1; i++) {
			final int pi = (int) primes[i];
			if (pi * pi > n) break;
			for (; n % pi == 0; n /= pi) factors.add(pi);
		}
		if (n > 1) factors.add(n);
		return factors;
	}

	/**
	 * 素因数を重複込みの昇順で指定されたコレクションへ追加します。
	 * 構築上限が元の {@code n} の平方根以上であることを前提とします。
	 *
	 * @param n        分解する正の整数
	 * @param supplier 結果コレクションの生成器
	 * @param <T>      コレクション型
	 * @return 素因数を格納したコレクション。{@code n <= 1} では空
	 */
	public <T extends Collection<Long>> T primeFactors(long n, final Supplier<T> supplier) {
		final T factors = supplier.get();
		if (n <= 1) return factors;
		for (int i = 0; i < cnt && n > 1; i++) {
			final long pi = primes[i];
			if (pi * pi > n) break;
			for (; n % pi == 0; n /= pi) factors.add(pi);
		}
		if (n > 1) factors.add(n);
		return factors;
	}

	/**
	 * 素因数と指数の対応を2次元配列で返します。
	 * 構築上限が元の {@code n} の平方根以上であることを前提とします。
	 *
	 * @param n 分解する正の整数
	 * @return {@code [0]}に素因数、{@code [1]}に指数を格納した配列。{@code n <= 1} では {@code [2][0]}
	 */
	public int[][] primeFactors2D(int n) {
		if (n <= 1) return new int[2][0];
		final int[][] res = new int[2][10];
		int size = 0;
		for (int i = 0; i < cnt && n > 1; i++) {
			final int pi = (int) primes[i];
			if (pi * pi > n) break;
			int e = 0;
			for (; n % pi == 0; n /= pi, e++) ;
			if (e > 0) {
				res[0][size] = pi;
				res[1][size++] = e;
			}
		}
		if (n > 1) {
			res[0][size] = n;
			res[1][size++] = 1;
		}
		return size == res[0].length ? res : new int[][]{copyOf(res[0], size), copyOf(res[1], size)};
	}

	/**
	 * 素因数と指数の対応を2次元配列で返します。
	 * 構築上限が元の {@code n} の平方根以上であることを前提とします。
	 *
	 * @param n 分解する正の整数
	 * @return {@code [0]}に素因数、{@code [1]}に指数を格納した配列。{@code n <= 1} では {@code [2][0]}
	 */
	public long[][] primeFactors2D(long n) {
		if (n <= 1) return new long[2][0];
		final long[][] res = new long[2][16];
		int size = 0;
		for (int i = 0; i < cnt && n > 1; i++) {
			final long pi = primes[i];
			if (pi * pi > n) break;
			int e = 0;
			for (; n % pi == 0; n /= pi, e++) ;
			if (e > 0) {
				res[0][size] = pi;
				res[1][size++] = e;
			}
		}
		if (n > 1) {
			res[0][size] = n;
			res[1][size++] = 1;
		}
		return size == res[0].length ? res : new long[][]{copyOf(res[0], size), copyOf(res[1], size)};
	}

	/**
	 * 素因数と指数の対応を指定された Map に格納します。
	 * 構築上限が元の {@code n} の平方根以上であることを前提とします。
	 *
	 * @param n        分解する正の整数
	 * @param supplier 結果 Map の生成器
	 * @param <T>      Map 型
	 * @return 素因数をキー、指数を値とする Map
	 */
	public <T extends Map<Integer, Integer>> T primeFactorsMap(int n, final Supplier<T> supplier) {
		final T factors = supplier.get();
		if (n <= 1) return factors;
		for (int i = 0; i < cnt && n > 1; i++) {
			final int pi = (int) primes[i];
			if (pi * pi > n) break;
			int e = 0;
			for (; n % pi == 0; n /= pi, e++) ;
			if (e > 0) factors.put(pi, e);
		}
		if (n > 1) factors.put(n, 1);
		return factors;
	}

	/**
	 * 素因数と指数の対応を指定された Map に格納します。
	 * 構築上限が元の {@code n} の平方根以上であることを前提とします。
	 *
	 * @param n        分解する正の整数
	 * @param supplier 結果 Map の生成器
	 * @param <T>      Map 型
	 * @return 素因数をキー、指数を値とする Map
	 */
	public <T extends Map<Long, Integer>> T primeFactorsMap(long n, final Supplier<T> supplier) {
		final T factors = supplier.get();
		if (n <= 1) return factors;
		for (int i = 0; i < cnt && n > 1; i++) {
			final long pi = primes[i];
			if (pi * pi > n) break;
			int e = 0;
			for (; n % pi == 0; n /= pi, e++) ;
			if (e > 0) factors.put(pi, e);
		}
		if (n > 1) factors.put(n, 1);
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

		for (long i = 5; i <= n; i += 6) {
			if (isNotCompositeOdd(i)) {
				primes[cnt++] = i;
				if (i <= sqrtN) {
					final long stepI = i * 6;
					final long deltaI = i * 2;
					for (long k = i * i; k <= n; k += stepI) {
						setCompositeOdd(k);
						long k2 = k + deltaI;
						if (k2 > n) break;
						setCompositeOdd(k2);
					}
				}
			}
			final long j = i + 2;
			if (j > n) break;
			if (isNotCompositeOdd(j)) {
				primes[cnt++] = j;
				if (j <= sqrtN) {
					final long stepJ = j * 6;
					final long deltaJ = j * 2;
					for (long k = j * j - deltaJ; k <= n; k += stepJ) {
						setCompositeOdd(k);
						long k2 = k + deltaJ;
						if (k2 > n) break;
						setCompositeOdd(k2);
					}
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

	private boolean isNotCompositeOdd(final long n) {
		return (oddBits[bitIndex(n)] & bitMask(n)) == 0;
	}
}
