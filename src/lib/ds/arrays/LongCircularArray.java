package lib.ds.arrays;

import java.util.*;
import java.util.function.*;

/**
 * 固定長の回転可能なlong配列である。
 * 長さは正、論理添字は {@code 0 <= i < length} とする。
 */
@SuppressWarnings("unused")
public final class LongCircularArray implements LongMutableArray {
	public final int length;
	private final long[] arr;
	private long rotation;
	private int offset;

	/**
	 * 長さ n の0初期化配列を構築する。
	 */
	public LongCircularArray(final int n) {
		length = n;
		arr = new long[n];
	}

	/**
	 * 長さ n の全要素を initialValue で初期化する。
	 */
	public LongCircularArray(final int n, final long initialValue) {
		this(n);
		fill(initialValue);
	}

	/**
	 * init を添字の昇順に1回ずつ呼び出して構築する。
	 */
	public LongCircularArray(final int n, final IntToLongFunction init) {
		this(n);
		for (int i = 0; i < n; i++) arr[i] = init.applyAsLong(i);
	}

	/**
	 * 指定された配列の要素を同じ順序で保持する循環配列を構築する。
	 */
	public LongCircularArray(final long[] a) {
		length = a.length;
		arr = a.clone();
	}

	/**
	 * 指定された配列の論理順を保持する循環配列を構築する。
	 */
	public LongCircularArray(final LongArray a) {
		length = a.size();
		arr = new long[length];
		for (int i = 0; i < length; i++) arr[i] = a.get(i);
	}

	/**
	 * 全要素・回転状態・総回転数を保持する独立したコピーを構築する。
	 */
	public LongCircularArray(final LongCircularArray other) {
		length = other.length;
		arr = other.arr.clone();
		rotation = other.rotation;
		offset = other.offset;
	}

	public int size() {
		return length;
	}

	/**
	 * 左回転を正、右回転を負とする総回転数を返す。
	 */
	public long rotation() {
		return rotation;
	}

	/**
	 * 指定した有効な論理添字の値を返す。
	 *
	 * @throws IndexOutOfBoundsException i が {@code 0 <= i < length} を満たさない場合
	 */
	public long get(final int i) {
		int p = offset + Objects.checkIndex(i, length);
		if (p >= length) p -= length;
		return arr[p];
	}

	/**
	 * 指定した有効な論理添字を更新し、更新前の値を返す。
	 *
	 * @throws IndexOutOfBoundsException i が {@code 0 <= i < length} を満たさない場合
	 */
	public long set(final int i, final long v) {
		int p = offset + Objects.checkIndex(i, length);
		if (p >= length) p -= length;
		final long old = arr[p];
		arr[p] = v;
		return old;
	}

	/**
	 * 全要素を指定値で更新し、回転状態と総回転数を0へ戻す。
	 */
	public void fill(final long v) {
		Arrays.fill(arr, v);
		rotation = 0;
		offset = 0;
	}

	/**
	 * init(i) で全要素を再構築し、回転状態と総回転数を0へ戻す。
	 * init は添字の昇順に呼ぶ。
	 */
	public void setAll(final IntToLongFunction init) {
		for (int i = 0; i < length; i++) arr[i] = init.applyAsLong(i);
		rotation = 0;
		offset = 0;
	}

	/**
	 * 論理配列を左へ1要素回転する。
	 */
	public void shiftLeft() {
		offset++;
		if (offset == length) offset = 0;
		rotation++;
	}

	/**
	 * 論理配列を左へn要素回転する。
	 */
	public void shiftLeft(final int n) {
		if (n < 0) {
			ShiftRight(-n);
			return;
		}
		offset += n % length;
		if (offset >= length) offset -= length;
		rotation += n;
	}

	/**
	 * 論理配列を右へ1要素回転する。
	 */
	public void ShiftRight() {
		offset--;
		if (offset == -1) offset = length - 1;
		rotation--;
	}

	/**
	 * 論理配列を右へn要素回転する。
	 */
	public void ShiftRight(final int n) {
		if (n < 0) {
			shiftLeft(-n);
			return;
		}
		offset -= n % length;
		if (offset < 0) offset += length;
		rotation -= n;
	}

	/**
	 * 回転状態を初期位置へ戻す。
	 */
	public void resetRotation() {
		rotation = 0;
		offset = 0;
	}

	/**
	 * 現在の論理順で独立した配列へコピーする。
	 */
	public long[] toArray() {
		final long[] res = new long[length];
		final int n = length - offset;
		System.arraycopy(arr, offset, res, 0, n);
		System.arraycopy(arr, 0, res, n, offset);
		return res;
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
	 * 論理順の要素を半角スペースで区切った文字列として返す。
	 */
	public String toString() {
		final StringBuilder sb = new StringBuilder(21 * length - 1);
		sb.append(arr[offset]);
		for (int i = 1; i < length; i++) sb.append(' ').append(get(i));
		return sb.toString();
	}
}
