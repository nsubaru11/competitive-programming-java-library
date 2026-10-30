package lib.ds.arrays;

import java.util.*;
import java.util.function.*;

import lib.ds.*;

/**
 * char値の2次元配列。回転・転置・反転を要素の移動なしで行う。
 * 座標は現在の向きでの0始まりの (行 i, 列 j) とし、範囲外では IndexOutOfBoundsException を投げる。
 * set は更新前の値、apply は更新後の値を返し、配列全体を変更するメソッドは自身を返す。
 * rotateLeft は反時計回り、rotateRight は時計回りに90度ずつ回転する。toString は各行の文字を区切らずに連結し、行を改行で区切る。
 */
public final class CharArray2D implements IntCollection {
	public final int size;
	private final char[] arr;
	private int h, w, base, di, dj;

	public CharArray2D(final int h, final int w) {
		this.h = h;
		this.w = w;
		di = w;
		dj = 1;
		size = h * w;
		arr = new char[size];
	}

	public CharArray2D(final int h, final int w, final char v) {
		this(h, w);
		fill(v);
	}

	/**
	 * 配列の先頭 {@code h * w} 文字を行優先順でコピーする。
	 */
	public CharArray2D(final int h, final int w, final char[] a) {
		this(h, w);
		System.arraycopy(a, 0, arr, 0, size);
	}

	public CharArray2D(final char[][] a) {
		this(a.length, a[0].length);
		for (int i = 0, p = 0; i < h; i++, p += w) System.arraycopy(a[i], 0, arr, p, w);
	}

	/**
	 * 各文字列を1行としてコピーする。各文字列の長さは先頭の文字列の長さ以上とする。
	 */
	public CharArray2D(final String[] s) {
		this(s.length, s[0].length());
		for (int i = 0, p = 0; i < h; i++, p += w) s[i].getChars(0, w, arr, p);
	}

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

	public int h() {
		return h;
	}

	public int w() {
		return w;
	}

	public boolean inRange(final int i, final int j) {
		return (i | j | (h - 1 - i) | (w - 1 - j)) >= 0;
	}

	public char get(final int i, final int j) {
		return arr[checkedIndex(i, j)];
	}

	public char getOrDefault(final int i, final int j, final char defaultValue) {
		return inRange(i, j) ? arr[index(i, j)] : defaultValue;
	}

	public char set(final int i, final int j, final char v) {
		final int p = checkedIndex(i, j);
		final char old = arr[p];
		arr[p] = v;
		return old;
	}

	/**
	 * {@code f(現在値, x)} を char に変換して格納し、更新後の値を返す。
	 */
	public char apply(final int i, final int j, final char x, final IntBinaryOperator f) {
		final int p = checkedIndex(i, j);
		return arr[p] = (char) f.applyAsInt(arr[p], x);
	}

	public CharArray2D fill(final char v) {
		Arrays.fill(arr, v);
		return this;
	}

	/**
	 * 各座標を {@code init(i, j)} を char に変換した値にする。
	 */
	public CharArray2D setAll(final IntBinaryOperator init) {
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				arr[index(i, j)] = (char) init.applyAsInt(i, j);
			}
		}
		return this;
	}

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

	public CharArray2D rotateLeft(final int distance) {
		for (int k = distance & 3; k > 0; k--) rotateLeft();
		return this;
	}

	public CharArray2D rotateRight() {
		base += (h - 1) * di;
		final int t = di;
		di = dj;
		dj = -t;
		final int s = h;
		h = w;
		w = s;
		return this;
	}

	public CharArray2D rotateRight(final int distance) {
		for (int k = distance & 3; k > 0; k--) rotateRight();
		return this;
	}

	public CharArray2D transpose() {
		int t = di;
		di = dj;
		dj = t;
		t = h;
		h = w;
		w = t;
		return this;
	}

	public CharArray2D flipHorizontal() {
		base += (w - 1) * dj;
		dj = -dj;
		return this;
	}

	public CharArray2D flipVertical() {
		base += (h - 1) * di;
		di = -di;
		return this;
	}

	public char[][] toArray2D() {
		final char[][] res = new char[h][w];
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				res[i][j] = arr[index(i, j)];
			}
		}
		return res;
	}

	public PrimitiveIterator.OfInt iterator() {
		return new PrimitiveIterator.OfInt() {
			private int j = 0, k = 0, s = base, p = base;

			public boolean hasNext() {
				return k < size;
			}

			public int nextInt() {
				if (!hasNext()) throw new NoSuchElementException();
				k++;
				final int v = arr[p];
				if (++j == w) {
					j = 0;
					s += di;
					p = s;
				} else {
					p += dj;
				}
				return v;
			}
		};
	}

	public Spliterator.OfInt spliterator() {
		return Spliterators.spliterator(iterator(), size, Spliterator.ORDERED);
	}

	public String toString() {
		if (isEmpty()) return "";
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
