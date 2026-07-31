package lib.math;

import java.math.*;
import java.util.*;
import java.util.function.*;

/**
 * 一回限りの素数判定・素数列挙を提供するユーティリティです。
 * 大量の問い合わせで篩を再利用する場合は {@link PrimeTable} を使用します。
 */
@SuppressWarnings("unused")
public final class PrimeUtils {
	private PrimeUtils() {
	}

	/**
	 * 試し割りにより素数判定を行います。
	 *
	 * @param n 判定する整数
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
		return a.isProbablePrime(k);
	}

	/**
	 * 2以上n以下の素数の個数を返します。
	 *
	 * @param n 上限値
	 * @return 素数の個数
	 */
	public static int primeCount(final int n) {
		return primeCount(2, n);
	}

	/**
	 * {@code min} 以上 {@code max} 以下の素数の個数を返します。
	 *
	 * @param min 下限値
	 * @param max 上限値
	 * @return 素数の個数
	 */
	public static int primeCount(final int min, final int max) {
		final BitSet table = new BitSet(max + 1);
		int count = 0;
		if (min <= 2 && 2 <= max) count++;
		if (min <= 3 && 3 <= max) count++;
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
	 * エラトステネスの篩により、指定範囲の素数を昇順で列挙します。
	 *
	 * @param min 下限値
	 * @param max 上限値
	 * @return {@code min} 以上 {@code max} 以下の素数配列
	 */
	public static int[] eratosthenes(final int min, final int max) {
		ArrayList<Integer> al = eratosthenes(min, max, ArrayList::new);
		int[] primeNum = new int[al.size()];
		for (int i = 0; i < al.size(); i++) {
			primeNum[i] = al.get(i);
		}
		return primeNum;
	}

	/**
	 * エラトステネスの篩により、指定範囲の素数を指定されたコレクションへ追加します。
	 *
	 * @param min      下限値
	 * @param max      上限値
	 * @param supplier 結果コレクションの生成器
	 * @param <T>      コレクション型
	 * @return 素数を格納したコレクション
	 */
	public static <T extends Collection<Integer>> T eratosthenes(final int min, final int max, final Supplier<T> supplier) {
		final T primeNum = supplier.get();
		final BitSet table = new BitSet(max + 1);
		if (min <= 2 && 2 <= max) primeNum.add(2);
		if (min <= 3 && 3 <= max) primeNum.add(3);
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
