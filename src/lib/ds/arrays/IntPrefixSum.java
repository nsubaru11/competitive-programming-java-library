package lib.ds.arrays;

import java.util.*;
import java.util.function.*;

/**
 * int値の1次元累積和を保持する。
 * 添字は0始まり、区間和は両端を含む閉区間とする。
 */
public final class IntPrefixSum implements IntArray {
	public final int size;
	private final long[] pref;

	/**
	 * init を添字の昇順に1回ずつ呼び出して構築する。
	 */
	public IntPrefixSum(final int n, final IntUnaryOperator init) {
		size = n;
		pref = new long[size + 1];
		for (int i = 0; i < size; i++) pref[i + 1] = pref[i] + init.applyAsInt(i);
	}

	/**
	 * 指定配列の論理順で構築する。入力は変更しない。
	 */
	public IntPrefixSum(final int[] a) {
		size = a.length;
		pref = new long[size + 1];
		for (int i = 0; i < size; i++) pref[i + 1] = pref[i] + a[i];
	}

	/**
	 * 指定配列の論理順で構築する。入力は変更しない。
	 */
	public IntPrefixSum(final IntArray a) {
		size = a.size();
		pref = new long[size + 1];
		for (int i = 0; i < size; i++) pref[i + 1] = pref[i] + a.get(i);
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
		return (int) (pref[i + 1] - pref[i]);
	}

	public long sum() {
		return pref[size];
	}

	/**
	 * 閉区間 {@code [0, i]} の和を返す。
	 *
	 * @throws IndexOutOfBoundsException 添字が範囲外の場合
	 */
	public long sum(final int i) {
		Objects.checkIndex(i, size);
		return pref[i + 1];
	}

	/**
	 * 閉区間 {@code [i, j]} の和を返す。
	 *
	 * @throws IndexOutOfBoundsException 添字が範囲外、または閉区間の始点が終点を超える場合
	 */
	public long sum(final int i, final int j) {
		Objects.checkIndex(i, j + 1);
		return pref[j + 1] - pref[i];
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
		final StringBuilder sb = new StringBuilder(12 * size - 1);
		sb.append(get(0));
		for (int i = 1; i < size; i++) sb.append(' ').append(get(i));
		return sb.toString();
	}
}
