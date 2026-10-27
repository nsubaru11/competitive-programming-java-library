package lib.ds.arrays;

import java.util.*;
import java.util.function.*;

import lib.ds.*;
import lib.util.function.*;

/**
 * long 型の2次元配列。回転・転置・反転をデータ移動なしで行う。
 * 座標は0始まりの行・列で、走査と配列化は現在の論理状態の行優先順である。
 * 回転・転置・反転はこのインスタンスを変更し、自身を返す。
 */
public final class LongArray2D implements LongCollection {
	public final int size;
	private final long[] arr;
	private int h, w, base, di, dj;

	/**
	 * 高さ h、幅 w の0初期化配列を構築する。
	 */
	public LongArray2D(final int h, final int w) {
		this.h = h;
		this.w = w;
		di = w;
		dj = 1;
		size = h * w;
		arr = new long[size];
	}

	public LongArray2D(final int h, final int w, final long v) {
		this(h, w);
		fill(v);
	}

	/**
	 * 配列の先頭 {@code h * w} 要素を行優先順でコピーする。配列長は要素数以上とする。
	 */
	public LongArray2D(final int h, final int w, final int[] a) {
		this(h, w);
		for (int i = 0; i < size; i++) arr[i] = a[i];
	}

	/**
	 * 配列の先頭 {@code h * w} 要素を行優先順でコピーする。配列長は要素数以上とする。
	 */
	public LongArray2D(final int h, final int w, final long[] a) {
		this(h, w);
		System.arraycopy(a, 0, arr, 0, size);
	}

	/**
	 * 各座標に {@code init(i, j)} を行優先順で適用して構築する。
	 */
	public LongArray2D(final int h, final int w, final IntBinaryToLongFunction init) {
		this(h, w);
		for (int i = 0, p = 0; i < h; i++) {
			for (int j = 0; j < w; j++, p++) {
				arr[p] = init.applyAsLong(i, j);
			}
		}
	}

	/**
	 * 指定された2次元配列を行優先順でコピーする。
	 */
	public LongArray2D(final int[][] a) {
		this(a.length, a[0].length);
		for (int i = 0, p = 0; i < h; i++) {
			for (int j = 0; j < w; j++, p++) {
				arr[p] = a[i][j];
			}
		}
	}

	/**
	 * 指定された2次元配列を行優先順でコピーする。
	 */
	public LongArray2D(final long[][] a) {
		this(a.length, a[0].length);
		for (int i = 0, p = 0; i < h; i++, p += w) System.arraycopy(a[i], 0, arr, p, w);
	}

	/**
	 * 論理寸法・向き・全要素を保持する独立したコピーを構築する。
	 */
	public LongArray2D(final LongArray2D other) {
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
	public long get(final int i, final int j) {
		return arr[checkedIndex(i, j)];
	}

	/**
	 * 指定した論理位置の値を返す。範囲外の場合は defaultValue を返す。
	 */
	public long getOrDefault(final int i, final int j, final long defaultValue) {
		return inRange(i, j) ? arr[index(i, j)] : defaultValue;
	}

	/**
	 * 指定した論理位置を v に更新し、更新前の値を返す。
	 *
	 * @throws IndexOutOfBoundsException 論理添字が範囲外の場合
	 */
	public long set(final int i, final int j, final long v) {
		final int p = checkedIndex(i, j);
		final long old = arr[p];
		arr[p] = v;
		return old;
	}

	/**
	 * 指定した論理位置に d を加算し、更新後の値を返す。
	 *
	 * @throws IndexOutOfBoundsException 論理添字が範囲外の場合
	 */
	public long add(final int i, final int j, final long d) {
		return affine(i, j, 1, d);
	}

	/**
	 * 指定した論理位置を m 倍し、更新後の値を返す。
	 *
	 * @throws IndexOutOfBoundsException 論理添字が範囲外の場合
	 */
	public long multiply(final int i, final int j, final long m) {
		return affine(i, j, m, 0);
	}

	/**
	 * 指定した論理位置を {@code 現在値 * m + d} に更新し、更新後の値を返す。
	 *
	 * @throws IndexOutOfBoundsException 論理添字が範囲外の場合
	 */
	public long affine(final int i, final int j, final long m, final long d) {
		final int p = checkedIndex(i, j);
		return arr[p] = arr[p] * m + d;
	}

	/**
	 * 指定した論理位置を {@code f(現在値, x)} に更新し、更新後の値を返す。
	 *
	 * @throws IndexOutOfBoundsException 論理添字が範囲外の場合
	 */
	public long apply(final int i, final int j, final long x, final LongBinaryOperator f) {
		final int p = checkedIndex(i, j);
		return arr[p] = f.applyAsLong(arr[p], x);
	}

	/**
	 * 全要素を v に更新する。論理寸法と向きは維持する。
	 */
	public void fill(final long v) {
		Arrays.fill(arr, v);
	}

	/**
	 * 現在の論理座標に init を行優先順で適用して全要素を更新する。向きは維持する。
	 */
	public void setAll(final IntBinaryToLongFunction init) {
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				arr[index(i, j)] = init.applyAsLong(i, j);
			}
		}
	}

	/**
	 * 反時計回りに90度論理回転する。
	 */
	public LongArray2D rotateLeft() {
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
	public LongArray2D rotateLeft(final int distance) {
		for (int k = distance & 3; k > 0; k--) rotateLeft();
		return this;
	}

	/**
	 * 時計回りに90度論理回転する。
	 */
	public LongArray2D rotateRight() {
		base += (h - 1) * di;
		final int t = di;
		di = dj;
		dj = -t;
		final int s = h;
		h = w;
		w = s;
		return this;
	}

	/**
	 * 時計回りに90度単位で distance 回論理回転する。負数は逆方向を表す。
	 */
	public LongArray2D rotateRight(final int distance) {
		for (int k = distance & 3; k > 0; k--) rotateRight();
		return this;
	}

	/**
	 * 論理状態を転置し、高さと幅を入れ替える。
	 */
	public LongArray2D transpose() {
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
	public LongArray2D flipHorizontal() {
		base += (w - 1) * dj;
		dj = -dj;
		return this;
	}

	/**
	 * 論理状態を上下反転する。
	 */
	public LongArray2D flipVertical() {
		base += (h - 1) * di;
		di = -di;
		return this;
	}

	/**
	 * 現在の論理状態を行優先順で独立した2次元配列へコピーする。
	 */
	public long[][] toArray2D() {
		final long[][] res = new long[h][w];
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
	public PrimitiveIterator.OfLong iterator() {
		return new PrimitiveIterator.OfLong() {
			private int j = 0, k = 0, s = base, p = base;

			public boolean hasNext() {
				return k < size;
			}

			public long nextLong() {
				if (!hasNext()) throw new NoSuchElementException();
				k++;
				final long v = arr[p];
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

	/**
	 * 行優先順の ORDERED・SIZED・SUBSIZED を持つ spliterator を返す。
	 */
	public Spliterator.OfLong spliterator() {
		return Spliterators.spliterator(iterator(), size, Spliterator.ORDERED);
	}

	/**
	 * 論理状態の各行を半角スペース区切り、行間を改行した文字列として返す。要素がなければ空文字列を返す。
	 */
	public String toString() {
		if (size == 0) return "";
		final StringBuilder sb = new StringBuilder(21 * size - 1);
		sb.append(arr[index(0, 0)]);
		for (int j = 1; j < w; j++) sb.append(' ').append(arr[index(0, j)]);
		for (int i = 1; i < h; i++) {
			sb.append('\n').append(arr[index(i, 0)]);
			for (int j = 1; j < w; j++) {
				sb.append(' ').append(arr[index(i, j)]);
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
