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
		int cnt = 0;
		if (n % 2 == 0) cnt++;
		while (n % 2 == 0) n /= 2;
		for (long i = 3; i * i <= n; i += 2) {
			if (n % i != 0) continue;
			cnt++;
			while (n % i == 0) n /= i;
		}
		if (n > 1) cnt++;
		return cnt;
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
		while (n % 2 == 0) {
			cnt++;
			n /= 2;
		}
		for (long i = 3; i * i <= n; i += 2) {
			while (n % i == 0) {
				cnt++;
				n /= i;
			}
		}
		if (n > 1) cnt++;
		return cnt;
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
		List<Integer> factors = primeFactors(n, () -> new ArrayList<Integer>(32));
		int[] result = new int[factors.size()];
		for (int i = 0; i < factors.size(); i++) {
			result[i] = factors.get(i);
		}
		return result;
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
		List<Long> factors = primeFactors(n, () -> new ArrayList<>(32));
		long[] result = new long[factors.size()];
		for (int i = 0; i < factors.size(); i++) {
			result[i] = factors.get(i);
		}
		return result;
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
		if (n <= 1) return supplier.get();
		T factors = supplier.get();
		while (n % 2 == 0) {
			factors.add(2);
			n /= 2;
		}
		for (int i = 3; i * i <= n; i += 2) {
			while (n % i == 0) {
				factors.add(i);
				n /= i;
			}
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
		if (n <= 1) return supplier.get();
		T factors = supplier.get();
		while (n % 2 == 0) {
			factors.add(2L);
			n /= 2;
		}
		for (long i = 3; i * i <= n; i += 2) {
			while (n % i == 0) {
				factors.add(i);
				n /= i;
			}
		}
		if (n > 1) factors.add(n);
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
	public static <T extends Map<Long, Integer>> T primeFactorsMap(long n, final Supplier<T> supplier) {
		if (n <= 1) return supplier.get();
		T factors = supplier.get();
		int cnt = 0;
		while (n % 2 == 0) {
			cnt++;
			n /= 2;
		}
		if (cnt > 0) factors.put(2L, cnt);
		for (long i = 3; i * i <= n; i += 2) {
			cnt = 0;
			while (n % i == 0) {
				cnt++;
				n /= i;
			}
			if (cnt > 0) factors.put(i, cnt);
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
		if (n <= 1) return supplier.get();
		T factors = supplier.get();
		int cnt = 0;
		while (n % 2 == 0) {
			cnt++;
			n /= 2;
		}
		if (cnt > 0) factors.put(2, cnt);
		for (int i = 3; i * i <= n; i += 2) {
			cnt = 0;
			while (n % i == 0) {
				cnt++;
				n /= i;
			}
			if (cnt > 0) factors.put(i, cnt);
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
