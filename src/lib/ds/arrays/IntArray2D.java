package lib.ds.arrays;

import java.util.*;
import java.util.function.*;

import lib.ds.*;

/**
 * int値の2次元配列。回転・転置・反転を要素の移動なしで行う。
 * 座標は現在の向きでの0始まりの (行 i, 列 j) とし、範囲外では IndexOutOfBoundsException を投げる。
 * set は更新前の値、add・multiply・affine・apply は更新後の値を返し、配列全体を変更するメソッドは自身を返す。
 * rotateLeft は反時計回り、rotateRight は時計回りに90度ずつ回転する。走査は現在の向きの行優先順とする。
 */
public final class IntArray2D implements IntCollection {
	public final int size;
	private final int[] arr;
	private int h, w, base, di, dj;

	public IntArray2D(final int h, final int w) {
		this.h = h;
		this.w = w;
		di = w;
		dj = 1;
		size = h * w;
		arr = new int[size];
	}

	public IntArray2D(final int h, final int w, final int v) {
		this(h, w);
		fill(v);
	}

	/**
	 * 配列の先頭 {@code h * w} 要素を行優先順でコピーする。
	 */
	public IntArray2D(final int h, final int w, final int[] a) {
		this(h, w);
		System.arraycopy(a, 0, arr, 0, size);
	}

	public IntArray2D(final int[][] a) {
		this(a.length, a[0].length);
		for (int i = 0, p = 0; i < h; i++, p += w) System.arraycopy(a[i], 0, arr, p, w);
	}

	public IntArray2D(final IntArray2D other) {
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

	public int get(final int i, final int j) {
		return arr[checkedIndex(i, j)];
	}

	public int getOrDefault(final int i, final int j, final int defaultValue) {
		return inRange(i, j) ? arr[index(i, j)] : defaultValue;
	}

	public int set(final int i, final int j, final int v) {
		final int p = checkedIndex(i, j);
		final int old = arr[p];
		arr[p] = v;
		return old;
	}

	public int add(final int i, final int j, final int d) {
		return affine(i, j, 1, d);
	}

	public int multiply(final int i, final int j, final int m) {
		return affine(i, j, m, 0);
	}

	public int affine(final int i, final int j, final int m, final int d) {
		final int p = checkedIndex(i, j);
		return arr[p] = arr[p] * m + d;
	}

	public int apply(final int i, final int j, final int x, final IntBinaryOperator f) {
		final int p = checkedIndex(i, j);
		return arr[p] = f.applyAsInt(arr[p], x);
	}

	public IntArray2D fill(final int v) {
		Arrays.fill(arr, v);
		return this;
	}

	public IntArray2D setAll(final IntBinaryOperator init) {
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				arr[index(i, j)] = init.applyAsInt(i, j);
			}
		}
		return this;
	}

	public IntArray2D rotateLeft() {
		base += (w - 1) * dj;
		final int t = di;
		di = -dj;
		dj = t;
		final int s = h;
		h = w;
		w = s;
		return this;
	}

	public IntArray2D rotateLeft(final int distance) {
		for (int k = distance & 3; k > 0; k--) rotateLeft();
		return this;
	}

	public IntArray2D rotateRight() {
		base += (h - 1) * di;
		final int t = di;
		di = dj;
		dj = -t;
		final int s = h;
		h = w;
		w = s;
		return this;
	}

	public IntArray2D rotateRight(final int distance) {
		for (int k = distance & 3; k > 0; k--) rotateRight();
		return this;
	}

	public IntArray2D transpose() {
		int t = di;
		di = dj;
		dj = t;
		t = h;
		h = w;
		w = t;
		return this;
	}

	public IntArray2D flipHorizontal() {
		base += (w - 1) * dj;
		dj = -dj;
		return this;
	}

	public IntArray2D flipVertical() {
		base += (h - 1) * di;
		di = -di;
		return this;
	}

	public int[][] toArray2D() {
		final int[][] res = new int[h][w];
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
		final StringBuilder sb = new StringBuilder(12 * size - 1);
		for (int i = 0; i < h; i++) {
			if (i > 0) sb.append('\n');
			sb.append(arr[index(i, 0)]);
			for (int j = 1; j < w; j++) sb.append(' ').append(arr[index(i, j)]);
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
