package lib.ds.arrays;

import static java.lang.Math.*;

import java.util.*;
import java.util.function.*;

import lib.ds.*;

/**
 * char 型の2次元配列。回転・転置・反転をデータ移動なしで行う。
 * 座標は0始まりで、走査順は現在の論理状態の行優先順とする。
 * 回転・転置・反転はこのインスタンスを変更し、自身を返す。
 */
@SuppressWarnings("unused")
public final class CharArray2D implements IntCollection {
	public final int size;
	private final char[] arr;
	private int h, w, base, di, dj;

	/**
	 * 高さ h、幅 w の0初期化配列を構築する。
	 */
	public CharArray2D(final int h, final int w) {
		this.h = h;
		this.w = w;
		di = w;
		dj = 1;
		size = h * w;
		arr = new char[size];
	}

	public CharArray2D(final int h, final int w, final char initialValue) {
		this(h, w);
		fill(initialValue);
	}

	/**
	 * 配列の先頭 {@code h * w} 文字を行優先順でコピーする。配列長は要素数以上とする。
	 */
	public CharArray2D(final int h, final int w, final char[] a) {
		this(h, w);
		System.arraycopy(a, 0, arr, 0, size);
	}

	/**
	 * init(i, j) を行優先順に呼び、結果を char に変換して構築する。
	 */
	public CharArray2D(final int h, final int w, final IntBinaryOperator init) {
		this(h, w);
		for (int i = 0, p = 0; i < h; i++) {
			for (int j = 0; j < w; j++, p++) {
				arr[p] = (char) init.applyAsInt(i, j);
			}
		}
	}

	/**
	 * 指定された2次元配列を行優先順でコピーする。
	 */
	public CharArray2D(final char[][] a) {
		this(a.length, a[0].length);
		for (int i = 0, p = 0; i < h; i++, p += w) System.arraycopy(a[i], 0, arr, p, w);
	}

	/**
	 * 論理寸法・向き・全要素を保持する独立したコピーを構築する。
	 */
	public CharArray2D(final CharArray2D other) {
		h = other.h;
		w = other.w;
		base = other.base;
		di = other.di;
		dj = other.dj;
		size = other.size;
		arr = other.arr.clone();
	}

	public int size() {
		return size;
	}

	/**
	 * 現在の論理状態の高さを返す。
	 */
	public int h() {
		return h;
	}

	/**
	 * 現在の論理状態の幅を返す。
	 */
	public int w() {
		return w;
	}

	/**
	 * {@code 0 <= i < h()} かつ {@code 0 <= j < w()} かを返す。
	 */
	public boolean inRange(final int i, final int j) {
		return (i | j | (h - 1 - i) | (w - 1 - j)) >= 0;
	}

	/**
	 * 指定した論理位置の値を返す。
	 *
	 * @throws IndexOutOfBoundsException 論理添字が範囲外の場合
	 */
	public char get(final int i, final int j) {
		return arr[checkedIndex(i, j)];
	}

	/**
	 * 指定した論理位置の値を返す。範囲外の場合は defaultValue を返す。
	 */
	public char getOrDefault(final int i, final int j, final char defaultValue) {
		return inRange(i, j) ? arr[index(i, j)] : defaultValue;
	}

	/**
	 * 指定した論理位置を v に更新し、更新前の値を返す。
	 *
	 * @throws IndexOutOfBoundsException 論理添字が範囲外の場合
	 */
	public char set(final int i, final int j, final char v) {
		final int p = checkedIndex(i, j);
		final char old = arr[p];
		arr[p] = v;
		return old;
	}

	/**
	 * 指定した論理位置を {@code (char) f(現在値, x)} に更新し、更新後の値を返す。
	 *
	 * @throws IndexOutOfBoundsException 論理添字が範囲外の場合
	 */
	public char apply(final int i, final int j, final char x, final IntBinaryOperator f) {
		final int p = checkedIndex(i, j);
		return arr[p] = (char) f.applyAsInt(arr[p], x);
	}

	/**
	 * 全要素を v に更新する。論理寸法と向きは維持する。
	 */
	public void fill(final char v) {
		Arrays.fill(arr, v);
	}

	/**
	 * 現在の論理座標に init を行優先順で適用して全要素を更新する。関数の結果は char に変換する。向きは維持する。
	 */
	public void setAll(final IntBinaryOperator init) {
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				arr[index(i, j)] = (char) init.applyAsInt(i, j);
			}
		}
	}

	/**
	 * 反時計回りに90度論理回転する。
	 */
	public CharArray2D rotateLeft() {
		base += (w - 1) * dj;
		final int t = di;
		di = -dj;
		dj = t;
		final int s = h;
		h = w;
		w = s;
		return this;
	}

	/**
	 * 反時計回りに90度単位で distance 回論理回転する。負数は逆方向を表す。
	 */
	public CharArray2D rotateLeft(final int distance) {
		for (int k = floorMod(distance, 4); k > 0; k--) rotateLeft();
		return this;
	}

	/**
	 * 時計回りに90度論理回転する。
	 */
	public CharArray2D rotateRight() {
		return rotateLeft(-1);
	}

	/**
	 * 時計回りに90度単位で distance 回論理回転する。負数は逆方向を表す。
	 */
	public CharArray2D rotateRight(final int distance) {
		return rotateLeft(-distance);
	}

	/**
	 * 論理状態を転置し、高さと幅を入れ替える。
	 */
	public CharArray2D transpose() {
		int t = di;
		di = dj;
		dj = t;
		t = h;
		h = w;
		w = t;
		return this;
	}

	/**
	 * 論理状態を左右反転する。
	 */
	public CharArray2D flipHorizontal() {
		base += (w - 1) * dj;
		dj = -dj;
		return this;
	}

	/**
	 * 論理状態を上下反転する。
	 */
	public CharArray2D flipVertical() {
		base += (h - 1) * di;
		di = -di;
		return this;
	}

	/**
	 * 現在の論理状態を行優先順で独立した2次元配列へコピーする。
	 */
	public char[][] toArray2D() {
		final char[][] res = new char[h][w];
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				res[i][j] = arr[index(i, j)];
			}
		}
		return res;
	}

	/**
	 * 現在の論理状態の行優先順に走査する iterator を返す。終端後の取得は NoSuchElementException を投げる。
	 */
	public PrimitiveIterator.OfInt iterator() {
		return new PrimitiveIterator.OfInt() {
			private int i = 0, j = 0, k = 0;

			public boolean hasNext() {
				return k < size;
			}

			public int nextInt() {
				if (!hasNext()) throw new NoSuchElementException();
				k++;
				final int v = arr[index(i, j)];
				if (++j == w) {
					j = 0;
					i++;
				}
				return v;
			}
		};
	}

	/**
	 * 行優先順の ORDERED・SIZED・SUBSIZED を持つ spliterator を返す。
	 */
	public Spliterator.OfInt spliterator() {
		return Spliterators.spliterator(iterator(), size, Spliterator.ORDERED);
	}

	/**
	 * 各行の文字を区切りなしで連結し、行間を改行した文字列を返す。
	 */
	public String toString() {
		if (size == 0) return "";
		final StringBuilder sb = new StringBuilder(size + h - 1);
		for (int j = 0; j < w; j++) sb.append(arr[index(0, j)]);
		for (int i = 1; i < h; i++) {
			sb.append('\n');
			for (int j = 0; j < w; j++) {
				sb.append(arr[index(i, j)]);
			}
		}
		return sb.toString();
	}

	private int checkedIndex(final int i, final int j) {
		Objects.checkIndex(i, h);
		Objects.checkIndex(j, w);
		return index(i, j);
	}

	private int index(final int i, final int j) {
		return base + i * di + j * dj;
	}
}
