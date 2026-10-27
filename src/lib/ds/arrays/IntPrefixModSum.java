package lib.ds.arrays;

import java.util.*;
import java.util.function.*;

/**
 * int値の1次元累積和を法 mod で保持する。
 * 添字は0始まり、区間和は両端を含む閉区間とする。法は正、入力値は {@code 0 <= v < mod} とする。
 * sum の結果は {@code [0, mod)} に正規化する。
 */
public final class IntPrefixModSum implements IntArray {
	public final int size, mod;
	private final int[] pref;

	/**
	 * init を添字の昇順に1回ずつ呼び出して構築する。
	 */
	public IntPrefixModSum(final int n, final int mod, final IntUnaryOperator init) {
		size = n;
		this.mod = mod;
		pref = new int[size + 1];
		for (int i = 0; i < size; i++) {
			final int v = pref[i] + init.applyAsInt(i);
			pref[i + 1] = v >= mod ? v - mod : v;
		}
	}

	/**
	 * 指定配列の論理順で構築する。入力は変更しない。
	 */
	public IntPrefixModSum(final int[] a, final int mod) {
		size = a.length;
		this.mod = mod;
		pref = new int[size + 1];
		for (int i = 0; i < size; i++) {
			final int v = pref[i] + a[i];
			pref[i + 1] = v >= mod ? v - mod : v;
		}
	}

	/**
	 * 指定配列の論理順で構築する。入力は変更しない。
	 */
	public IntPrefixModSum(final IntArray a, final int mod) {
		size = a.size();
		this.mod = mod;
		pref = new int[size + 1];
		for (int i = 0; i < size; i++) {
			final int v = pref[i] + a.get(i);
			pref[i + 1] = v >= mod ? v - mod : v;
		}
	}

	public int size() {
		return size;
	}

	/**
	 * 指定した有効な論理添字の値を返す。
	 *
	 * @throws IndexOutOfBoundsException 添字が範囲外の場合
	 */
	public int get(final int i) {
		final int res = pref[i + 1] - pref[i];
		return res < 0 ? res + mod : res;
	}

	public int sum() {
		return pref[size];
	}

	/**
	 * 閉区間 {@code [0, i]} の和を返す。
	 *
	 * @throws IndexOutOfBoundsException 添字が範囲外の場合
	 */
	public int sum(final int i) {
		Objects.checkIndex(i, size);
		return pref[i + 1];
	}

	/**
	 * 閉区間 {@code [i, j]} の和を返す。
	 *
	 * @throws IndexOutOfBoundsException 添字が範囲外、または閉区間の始点が終点を超える場合
	 */
	public int sum(final int i, final int j) {
		Objects.checkIndex(i, j + 1);
		final int res = pref[j + 1] - pref[i];
		return res < 0 ? res + mod : res;
	}

	/**
	 * 論理順に走査する iterator を返す。終端後の取得は NoSuchElementException を投げる。
	 */
	public PrimitiveIterator.OfInt iterator() {
		return new PrimitiveIterator.OfInt() {
			private int i = 0;

			public boolean hasNext() {
				return i < size;
			}

			public int nextInt() {
				if (!hasNext()) throw new NoSuchElementException();
				return get(i++);
			}
		};
	}

	/**
	 * 元の要素を半角スペースで区切った文字列として返す。
	 */
	public String toString() {
		if (size == 0) return "";
		final StringBuilder sb = new StringBuilder(11 * size - 1);
		sb.append(get(0));
		for (int i = 1; i < size; i++) sb.append(' ').append(get(i));
		return sb.toString();
	}
}
