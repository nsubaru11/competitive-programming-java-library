package lib.ds.arrays;

import java.util.*;
import java.util.function.*;

/**
 * 固定長の回転可能なint配列である。
 * 長さは正、論理添字は {@code 0 <= i < size} とする。
 * 回転・resetRotation はこのインスタンスを変更し、自身を返す。
 */
public final class IntCircularArray implements IntMutableArray {
	public final int size;
	private final int[] arr;
	private long rotation;
	private int offset;

	/**
	 * 長さ n の0初期化配列を構築する。
	 */
	public IntCircularArray(final int n) {
		size = n;
		arr = new int[n];
	}

	/**
	 * 長さ n の全要素を v で初期化する。
	 */
	public IntCircularArray(final int n, final int v) {
		this(n);
		fill(v);
	}

	/**
	 * init を添字の昇順に1回ずつ呼び出して構築する。
	 */
	public IntCircularArray(final int n, final IntUnaryOperator init) {
		this(n);
		for (int i = 0; i < n; i++) arr[i] = init.applyAsInt(i);
	}

	/**
	 * 指定された配列の要素を同じ順序で保持する循環配列を構築する。
	 */
	public IntCircularArray(final int[] a) {
		size = a.length;
		arr = a.clone();
	}

	/**
	 * 指定された配列の論理順を保持する循環配列を構築する。
	 */
	public IntCircularArray(final IntArray a) {
		size = a.size();
		arr = a.toArray();
	}

	/**
	 * 全要素・回転状態・総回転数を保持する独立したコピーを構築する。
	 */
	public IntCircularArray(final IntCircularArray other) {
		size = other.size;
		arr = other.arr.clone();
		rotation = other.rotation;
		offset = other.offset;
	}

	public int size() {
		return size;
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
	 * @throws IndexOutOfBoundsException i が {@code 0 <= i < size} を満たさない場合
	 */
	public int get(final int i) {
		int p = offset + Objects.checkIndex(i, size);
		if (p >= size) p -= size;
		return arr[p];
	}

	/**
	 * 指定した有効な論理添字を更新し、更新前の値を返す。
	 *
	 * @throws IndexOutOfBoundsException i が {@code 0 <= i < size} を満たさない場合
	 */
	public int set(final int i, final int v) {
		int p = offset + Objects.checkIndex(i, size);
		if (p >= size) p -= size;
		final int old = arr[p];
		arr[p] = v;
		return old;
	}

	/**
	 * 全要素を指定値で更新し、回転状態と総回転数を0へ戻す。
	 */
	public void fill(final int v) {
		Arrays.fill(arr, v);
		rotation = 0;
		offset = 0;
	}

	/**
	 * init(i) で全要素を再構築し、回転状態と総回転数を0へ戻す。
	 * init は添字の昇順に呼ぶ。
	 */
	public void setAll(final IntUnaryOperator init) {
		for (int i = 0; i < size; i++) arr[i] = init.applyAsInt(i);
		rotation = 0;
		offset = 0;
	}

	/**
	 * 論理配列を左へ1要素回転する。
	 */
	public IntCircularArray rotateLeft() {
		offset++;
		if (offset == size) offset = 0;
		rotation++;
		return this;
	}

	/**
	 * 論理配列を左へn要素回転する。
	 */
	public IntCircularArray rotateLeft(final int n) {
		if (n < 0) return rotateRight(-n);
		offset += n % size;
		if (offset >= size) offset -= size;
		rotation += n;
		return this;
	}

	/**
	 * 論理配列を右へ1要素回転する。
	 */
	public IntCircularArray rotateRight() {
		offset--;
		if (offset == -1) offset = size - 1;
		rotation--;
		return this;
	}

	/**
	 * 論理配列を右へn要素回転する。
	 */
	public IntCircularArray rotateRight(final int n) {
		if (n < 0) return rotateLeft(-n);
		offset -= n % size;
		if (offset < 0) offset += size;
		rotation -= n;
		return this;
	}

	/**
	 * 回転状態を初期位置へ戻す。
	 */
	public IntCircularArray resetRotation() {
		rotation = 0;
		offset = 0;
		return this;
	}

	/**
	 * 現在の論理順で独立した配列へコピーする。
	 */
	public int[] toArray() {
		final int[] res = new int[size];
		final int n = size - offset;
		System.arraycopy(arr, offset, res, 0, n);
		System.arraycopy(arr, 0, res, n, offset);
		return res;
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
	 * 論理順の要素を半角スペースで区切った文字列として返す。
	 */
	public String toString() {
		if (isEmpty()) return "";
		final StringBuilder sb = new StringBuilder(12 * size - 1);
		sb.append(arr[offset]);
		for (int i = 1; i < size; i++) sb.append(' ').append(get(i));
		return sb.toString();
	}
}
