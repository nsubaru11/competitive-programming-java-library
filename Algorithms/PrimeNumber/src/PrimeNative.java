import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

/**
 * 素数に関するクラス
 */
@SuppressWarnings("unused")
public final class PrimeNative {
	private static final int INF = Integer.MAX_VALUE;

	/**
	 * 素数判定
	 *
	 * @param n long
	 * @return {@code true}なら素数、{@code false}なら合成数
	 */
	public static boolean isPrime(final long n) {
		if (n <= 1) return false;
		if (n == 2 || n == 3) return true;
		if (n % 2L == 0 || n % 3L == 0) return false;
		for (long i = 5L; i * i <= n; i += 6L) {
			if (n % i == 0 || n % (i + 2L) == 0) return false;
		}
		return true;
	}

	/**
	 * 素数の可能性が高いかを判定
	 * {@code k}の値が大きいほど精度が上がります。
	 * 合成数であることが確定している場合は{@code false}を返します。
	 *
	 * @param n 判定する数
	 * @param k 精度を決めるパラメータ
	 * @return {@code true}なら素数の可能性が高く、{@code false}なら合成数
	 */
	public static boolean isProbablePrime(final long n, final int k) {
		BigInteger a = BigInteger.valueOf(n);
		return (a.isProbablePrime(k));
	}

	/**
	 * 2以上n以下の素数の個数を返します。
	 *
	 * @param n 上限値
	 * @return 素数の個数
	 */
	public static int elements(final int n) {
		return elements(2, n);
	}

	/**
	 * n以上k以下の素数の個数を返します。
	 *
	 * @param min 下限値
	 * @param max 上限値
	 * @return 素数の個数
	 */
	public static int elements(final int min, final int max) {
		final BitSet table = new BitSet(max + 1);
		int count = 0;
		if (2 > min) count++;
		if (3 > min) count++;
		for (int i = 4; i <= max; i += 2) table.set(i);
		for (int i = 9; i <= max; i += 6) table.set(i);
		for (int i = 5; i <= max; i += 6) {
			for (int j = i; j <= i + 2 && j <= max; j += 2) {
				if (!table.get(j)) {
					if (min <= j) count++;
					for (long k = (long) j * j; k <= max; k += (long) j + j) {
						table.set((int) k);
					}
				}
			}
		}
		return count;
	}

	/**
	 * 2以上n以下の素数をセットで返します。
	 *
	 * @param n 上限値
	 * @return 素数のセット
	 */
	public static Set<Integer> getPrimeSet(final int n) {
		return Eratosthenes(2, n, HashSet::new);
	}

	/**
	 * n以上k以下の素数をセットで返します。
	 *
	 * @param min 下限値
	 * @param max 上限値
	 * @return 素数のセット
	 */
	public static Set<Integer> getPrimeSet(final int min, final int max) {
		return Eratosthenes(min, max, HashSet::new);
	}

	/**
	 * 2以上n以下の素数をリストで返します。
	 *
	 * @param n 上限値
	 * @return 素数のリスト
	 */
	public static List<Integer> getPrimeList(final int n) {
		return Eratosthenes(2, n, ArrayList::new);
	}

	/**
	 * n以上k以下の素数をリストで返します。
	 *
	 * @param min 下限値
	 * @param max 上限値
	 * @return 素数のリスト
	 */
	public static List<Integer> getPrimeList(final int min, final int max) {
		return Eratosthenes(min, max, ArrayList::new);
	}

	private static <T extends Collection<Integer>> T Eratosthenes(final int min, final int max, final Supplier<T> supplier) {
		final T primeNum = supplier.get();
		final BitSet table = new BitSet(max + 1);
		if (min <= 2) primeNum.add(2);
		if (min <= 3) primeNum.add(3);
		for (int i = 4; i <= max; i += 2) table.set(i);
		for (int i = 9; i <= max; i += 6) table.set(i);
		for (int i = 5; i <= max; i += 6) {
			for (int j = i; j <= i + 2 && j <= max; j += 2) {
				if (!table.get(j)) {
					if (min <= j) primeNum.add(j);
					for (long k = (long) j * j; k <= max; k += (long) j + j) {
						table.set((int) k);
					}
				}
			}
		}
		return primeNum;
	}
}
