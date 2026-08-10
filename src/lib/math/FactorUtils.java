package lib.math;

import java.util.*;
import java.util.function.*;

/**
 * 素因数分解と約数列挙を提供するユーティリティです。
 * 配列版と順序を保持するコレクション版では、素因数を昇順で返します。
 */
public final class FactorUtils {
	private FactorUtils() {
	}

	// region <prime factorize>

	/**
	 * 異なる素因数の個数を返します。
	 *
	 * @param n 素因数を数える整数
	 * @return 異なる素因数の個数。{@code n <= 1} では 0
	 */
	public static int uniquePrimeFactorCount(long n) {
		if (n <= 1) return 0;
		int cnt = 0, e = 0;
		for (; n % 2 == 0; n /= 2, e++) ;
		if (e > 0) cnt++;
		e = 0;
		for (; n % 3 == 0; n /= 3, e++) ;
		if (e > 0) cnt++;
		for (long i = 5, j = 7; i * i <= n; i += 6, j += 6) {
			e = 0;
			for (; n % i == 0; n /= i, e++) ;
			if (e > 0) cnt++;
			if (j * j > n) break;
			e = 0;
			for (; n % j == 0; n /= j, e++) ;
			if (e > 0) cnt++;
		}
		return n > 1 ? cnt + 1 : cnt;
	}

	/**
	 * 重複を含む素因数の個数を返します。
	 *
	 * @param n 素因数を数える整数
	 * @return 重複を含む素因数の個数。{@code n <= 1} では 0
	 */
	public static int primeFactorCount(long n) {
		if (n <= 1) return 0;
		int cnt = 0;
		for (; n % 2 == 0; n /= 2, cnt++) ;
		for (; n % 3 == 0; n /= 3, cnt++) ;
		for (long i = 5, j = 7; i * i <= n; i += 6, j += 6) {
			for (; n % i == 0; n /= i, cnt++) ;
			if (j * j > n) break;
			for (; n % j == 0; n /= j, cnt++) ;
		}
		return n > 1 ? cnt + 1 : cnt;
	}

	/**
	 * 素因数分解を行い、素因数を昇順の配列で返します。
	 * {@code n <= 1} の場合は空配列を返します。
	 *
	 * @param n 分解する正の整数
	 * @return 素因数の昇順配列
	 */
	public static int[] primeFactors(final int n) {
		if (n <= 1) return new int[0];
		final List<Integer> factors = primeFactors(n, () -> new ArrayList<Integer>(32));
		final int size = factors.size();
		final int[] res = new int[size];
		for (int i = 0; i < size; i++) res[i] = factors.get(i);
		return res;
	}

	/**
	 * 素因数分解を行い、素因数を昇順の配列で返します。
	 * {@code n <= 1} の場合は空配列を返します。
	 *
	 * @param n 分解する正の整数
	 * @return 素因数の昇順配列
	 */
	public static long[] primeFactors(final long n) {
		if (n <= 1) return new long[0];
		final List<Long> factors = primeFactors(n, () -> new ArrayList<>(32));
		final int size = factors.size();
		final long[] res = new long[size];
		for (int i = 0; i < size; i++) res[i] = factors.get(i);
		return res;
	}

	/**
	 * 素因数を昇順で指定されたコレクションへ追加します。
	 * 同じ素因数は重複して追加されます。重複と挿入順を保持する
	 * {@link List} などのコレクションを指定してください。
	 *
	 * @param n        分解する正の整数
	 * @param supplier 結果コレクションの生成器
	 * @param <T>      コレクション型
	 * @return 素因数を格納したコレクション。{@code n <= 1} では空
	 */
	public static <T extends Collection<Integer>> T primeFactors(int n, final Supplier<T> supplier) {
		final T factors = supplier.get();
		if (n <= 1) return factors;
		for (; n % 2 == 0; n /= 2) factors.add(2);
		for (; n % 3 == 0; n /= 3) factors.add(3);
		for (int i = 5, j = 7; i * i <= n; i += 6, j += 6) {
			for (; n % i == 0; n /= i) factors.add(i);
			if (j * j > n) break;
			for (; n % j == 0; n /= j) factors.add(j);
		}
		if (n > 1) factors.add(n);
		return factors;
	}

	/**
	 * 素因数を昇順で指定されたコレクションへ追加します。
	 * 同じ素因数は重複して追加されます。重複と挿入順を保持する
	 * {@link List} などのコレクションを指定してください。
	 *
	 * @param n        分解する正の整数
	 * @param supplier 結果コレクションの生成器
	 * @param <T>      コレクション型
	 * @return 素因数を格納したコレクション。{@code n <= 1} では空
	 */
	public static <T extends Collection<Long>> T primeFactors(long n, final Supplier<T> supplier) {
		final T factors = supplier.get();
		if (n <= 1) return factors;
		for (; n % 2 == 0; n /= 2) factors.add(2L);
		for (; n % 3 == 0; n /= 3) factors.add(3L);
		for (long i = 5, j = 7; i * i <= n; i += 6, j += 6) {
			for (; n % i == 0; n /= i) factors.add(i);
			if (j * j > n) break;
			for (; n % j == 0; n /= j) factors.add(j);
		}
		if (n > 1) factors.add(n);
		return factors;
	}

	/**
	 * 素因数分解を行い、素因数と指数の対応を 2xN の2次元配列で返します。
	 * result[0] には昇順にソートされた素因数が、
	 * result[1] には対応する指数が格納されます。
	 *
	 * @param n 分解する正の整数
	 * @return {素因数の配列, 指数の配列} の2次元配列。 n <= 1 の場合は要素数0の配列を返す。
	 */
	public static int[][] primeFactors2D(int n) {
		if (n <= 1) return new int[2][0];
		final int count = uniquePrimeFactorCount(n);
		final int[][] res = new int[2][count];
		int idx = 0, e = 0;
		for (; n % 2 == 0; n /= 2, e++) ;
		if (e > 0) {
			res[0][idx] = 2;
			res[1][idx++] = e;
		}
		e = 0;
		for (; n % 3 == 0; n /= 3, e++) ;
		if (e > 0) {
			res[0][idx] = 3;
			res[1][idx++] = e;
		}
		for (int i = 5, j = 7; i * i <= n; i += 6, j += 6) {
			e = 0;
			for (; n % i == 0; n /= i, e++) ;
			if (e > 0) {
				res[0][idx] = i;
				res[1][idx++] = e;
			}
			if (j * j > n) break;
			e = 0;
			for (; n % j == 0; n /= j, e++) ;
			if (e > 0) {
				res[0][idx] = j;
				res[1][idx++] = e;
			}
		}
		if (n > 1) {
			res[0][idx] = n;
			res[1][idx] = 1;
		}
		return res;
	}

	/**
	 * 素因数分解を行い、素因数と指数の対応を 2xN の2次元配列で返します。
	 * result[0] には昇順にソートされた素因数が、
	 * result[1] には対応する指数が格納されます。
	 *
	 * @param n 分解する正の整数
	 * @return {素因数の配列, 指数の配列} の2次元配列。 n <= 1 の場合は要素数0の配列を返す。
	 */
	public static long[][] primeFactors2D(long n) {
		if (n <= 1) return new long[2][0];
		final int count = uniquePrimeFactorCount(n);
		final long[][] res = new long[2][count];
		int idx = 0, e = 0;
		for (; n % 2 == 0; n /= 2, e++) ;
		if (e > 0) {
			res[0][idx] = 2;
			res[1][idx++] = e;
		}
		e = 0;
		for (; n % 3 == 0; n /= 3, e++) ;
		if (e > 0) {
			res[0][idx] = 3;
			res[1][idx++] = e;
		}
		for (long i = 5, j = 7; i * i <= n; i += 6, j += 6) {
			e = 0;
			for (; n % i == 0; n /= i, e++) ;
			if (e > 0) {
				res[0][idx] = i;
				res[1][idx++] = e;
			}
			if (j * j > n) break;
			e = 0;
			for (; n % j == 0; n /= j, e++) ;
			if (e > 0) {
				res[0][idx] = j;
				res[1][idx++] = e;
			}
		}
		if (n > 1) {
			res[0][idx] = n;
			res[1][idx] = 1;
		}
		return res;
	}

	/**
	 * 素因数と指数の対応を指定された Map に格納します。
	 * 素因数は昇順に処理されますが、反復順序は指定された Map に従います。
	 *
	 * @param n        分解する正の整数
	 * @param supplier 結果 Map の生成器
	 * @param <T>      Map 型
	 * @return 素因数をキー、指数を値とする Map
	 */
	public static <T extends Map<Long, Integer>> T primeFactorsMap(long n, final Supplier<T> supplier) {
		final T factors = supplier.get();
		if (n <= 1) return factors;
		int e = 0;
		for (; n % 2 == 0; n /= 2, e++) ;
		if (e > 0) factors.put(2L, e);
		e = 0;
		for (; n % 3 == 0; n /= 3, e++) ;
		if (e > 0) factors.put(3L, e);
		for (long i = 5, j = 7; i * i <= n; i += 6, j += 6) {
			e = 0;
			for (; n % i == 0; n /= i, e++) ;
			if (e > 0) factors.put(i, e);
			if (j * j > n) break;
			e = 0;
			for (; n % j == 0; n /= j, e++) ;
			if (e > 0) factors.put(j, e);
		}
		if (n > 1) factors.put(n, 1);
		return factors;
	}

	/**
	 * 素因数と指数の対応を指定された Map に格納します。
	 * 素因数は昇順に処理されますが、反復順序は指定された Map に従います。
	 *
	 * @param n        分解する正の整数
	 * @param supplier 結果 Map の生成器
	 * @param <T>      Map 型
	 * @return 素因数をキー、指数を値とする Map
	 */
	public static <T extends Map<Integer, Integer>> T primeFactorsMap(int n, final Supplier<T> supplier) {
		final T factors = supplier.get();
		if (n <= 1) return factors;
		int e = 0;
		for (; n % 2 == 0; n /= 2, e++) ;
		if (e > 0) factors.put(2, e);
		e = 0;
		for (; n % 3 == 0; n /= 3, e++) ;
		if (e > 0) factors.put(3, e);
		for (int i = 5, j = 7; i * i <= n; i += 6, j += 6) {
			e = 0;
			for (; n % i == 0; n /= i, e++) ;
			if (e > 0) factors.put(i, e);
			if (j * j > n) break;
			e = 0;
			for (; n % j == 0; n /= j, e++) ;
			if (e > 0) factors.put(j, e);
		}
		if (n > 1) factors.put(n, 1);
		return factors;
	}
	// endregion

	// region <divisors>

	/**
	 * 正の約数の個数を返します。
	 *
	 * @param n 約数を数える整数
	 * @return 正の約数の個数。{@code n <= 0} では 0
	 */
	public static int divisorCount(final long n) {
		int cnt = 0;
		long p = 1;
		for (; p * p < n; p++) {
			if (n % p == 0) cnt += 2;
		}
		if (p * p == n) cnt++;
		return cnt;
	}

	/**
	 * 正の整数の約数を昇順の配列で返します。
	 *
	 * @param n 約数を列挙する正の整数。{@code n > 0} を前提とします
	 * @return 約数の昇順配列
	 */
	public static int[] divisors(final int n) {
		final ArrayList<Integer> values = divisorsInner(n);
		int[] divisors = new int[values.size()];
		for (int i = 0; i < values.size(); i++) {
			divisors[i] = values.get(i);
		}
		return divisors;
	}

	/**
	 * 正の整数の約数を昇順の配列で返します。
	 *
	 * @param n 約数を列挙する正の整数。{@code n > 0} を前提とします
	 * @return 約数の昇順配列
	 */
	public static long[] divisors(final long n) {
		final ArrayList<Long> values = divisorsInner(n);
		long[] divisors = new long[values.size()];
		for (int i = 0; i < values.size(); i++) {
			divisors[i] = values.get(i);
		}
		return divisors;
	}

	/**
	 * 約数を昇順で指定されたコレクションへ追加します。
	 * {@link List} や {@link LinkedHashSet} など挿入順を保持するコレクションでは
	 * 昇順になります。{@link HashSet} では反復順序は保証されません。
	 *
	 * @param n        約数を列挙する正の整数。{@code n > 0} を前提とします
	 * @param supplier 結果コレクションの生成器
	 * @param <T>      コレクション型
	 * @return 約数を格納したコレクション
	 */
	public static <T extends Collection<Integer>> T divisors(final int n, final Supplier<T> supplier) {
		final T collection = supplier.get();
		collection.addAll(divisorsInner(n));
		return collection;
	}

	/**
	 * 約数を昇順で指定されたコレクションへ追加します。
	 * {@link List} や {@link LinkedHashSet} など挿入順を保持するコレクションでは
	 * 昇順になります。{@link HashSet} では反復順序は保証されません。
	 *
	 * @param n        約数を列挙する正の整数。{@code n > 0} を前提とします
	 * @param supplier 結果コレクションの生成器
	 * @param <T>      コレクション型
	 * @return 約数を格納したコレクション
	 */
	public static <T extends Collection<Long>> T divisors(final long n, final Supplier<T> supplier) {
		final T collection = supplier.get();
		collection.addAll(divisorsInner(n));
		return collection;
	}

	private static ArrayList<Integer> divisorsInner(final int n) {
		final ArrayList<Integer> lo = new ArrayList<>(32), hi = new ArrayList<>(32);
		int p = 1;
		for (; p * p < n; p++) {
			if (n % p == 0) {
				lo.add(p);
				hi.add(n / p);
			}
		}
		if (p * p == n) lo.add(p);
		for (int i = hi.size() - 1; i >= 0; i--) lo.add(hi.get(i));
		return lo;
	}

	private static ArrayList<Long> divisorsInner(final long n) {
		final ArrayList<Long> lo = new ArrayList<>(32), hi = new ArrayList<>(32);
		long p = 1;
		for (; p * p < n; p++) {
			if (n % p == 0) {
				lo.add(p);
				hi.add(n / p);
			}
		}
		if (p * p == n) lo.add(p);
		for (int i = hi.size() - 1; i >= 0; i--) lo.add(hi.get(i));
		return lo;
	}
	// endregion
}
