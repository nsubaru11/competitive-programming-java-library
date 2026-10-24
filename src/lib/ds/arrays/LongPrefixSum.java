package lib.ds.arrays;

import java.util.*;
import java.util.function.*;

/**
 * long値の1次元累積和を保持する。
 * 添字は0始まり、区間和は両端を含む閉区間とする。
 */
public final class LongPrefixSum implements LongArray {
	public final int length;
	private final long[] pref;

	/**
	 * init を添字の昇順に1回ずつ呼び出して構築する。
	 */
	public LongPrefixSum(final int n, final IntToLongFunction init) {
		length = n;
		pref = new long[length + 1];
		for (int i = 0; i < length; i++) pref[i + 1] = pref[i] + init.applyAsLong(i);
	}

	/**
	 * 指定配列の論理順で構築する。入力は変更しない。
	 */
	public LongPrefixSum(final long[] a) {
		length = a.length;
		pref = new long[length + 1];
		for (int i = 0; i < length; i++) pref[i + 1] = pref[i] + a[i];
	}

	/**
	 * 指定配列の論理順で構築する。入力は変更しない。
	 */
	public LongPrefixSum(final LongArray a) {
		length = a.size();
		pref = new long[length + 1];
		for (int i = 0; i < length; i++) pref[i + 1] = pref[i] + a.get(i);
	}

	public int size() {
		return length;
	}

	/**
	 * 指定した有効な論理添字の値を返す。
	 *
	 * @throws IndexOutOfBoundsException 添字が範囲外の場合
	 */
	public long get(final int i) {
		Objects.checkIndex(i, length);
		return pref[i + 1] - pref[i];
	}

	public long sum() {
		return pref[length];
	}

	/**
	 * 閉区間 {@code [0, i]} の和を返す。
	 *
	 * @throws IndexOutOfBoundsException 添字が範囲外の場合
	 */
	public long sum(final int i) {
		Objects.checkIndex(i, length);
		return pref[i + 1];
	}

	/**
	 * 閉区間 {@code [i, j]} の和を返す。
	 *
	 * @throws IndexOutOfBoundsException 添字が範囲外、または閉区間の始点が終点を超える場合
	 */
	public long sum(final int i, final int j) {
		Objects.checkIndex(j, length);
		Objects.checkIndex(i, j + 1);
		return pref[j + 1] - pref[i];
	}

	/**
	 * 論理順に走査する iterator を返す。終端後の取得は NoSuchElementException を投げる。
	 */
	public PrimitiveIterator.OfLong iterator() {
		return new PrimitiveIterator.OfLong() {
			private int i = 0;

			public boolean hasNext() {
				return i < length;
			}

			public long nextLong() {
				if (!hasNext()) throw new NoSuchElementException();
				return get(i++);
			}
		};
	}

	/**
	 * 元の要素を半角スペースで区切った文字列として返す。
	 */
	public String toString() {
		if (length == 0) return "";
		final StringBuilder sb = new StringBuilder(21 * length - 1);
		sb.append(get(0));
		for (int i = 1; i < length; i++) sb.append(' ').append(get(i));
		return sb.toString();
	}
}
