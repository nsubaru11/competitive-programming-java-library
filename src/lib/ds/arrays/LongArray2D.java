package lib.ds.arrays;

import java.util.*;
import java.util.function.*;

import lib.ds.*;
import lib.util.function.*;

/**
 * long値の2次元配列。回転・転置・反転をデータ移動なしで行う。
 * 座標は現在の論理状態の0始まりの行 i・列 j とし、範囲外では IndexOutOfBoundsException を投げる。getOrDefault は範囲外で defaultValue を返す。
 * set は更新前の値、add・multiply・affine・apply は更新後の値を返す。affine は {@code 現在値 * m + d}、apply は {@code f(現在値, x)} へ更新する。
 * 回転・転置・反転はこのインスタンスを変更して自身を返す。rotateLeft は反時計回り、rotateRight は時計回りに90度回転し、distance が負の場合は逆方向とする。
 * fill・setAll は論理寸法と向きを維持する。init は行優先順に呼び、走査・配列化・toString は現在の論理状態の行優先順とする。
 */
public final class LongArray2D implements LongCollection {
	public final int size;
	private final long[] arr;
	private int h, w, base, di, dj;

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
	public LongArray2D(final int h, final int w, final long[] a) {
		this(h, w);
		System.arraycopy(a, 0, arr, 0, size);
	}

	public LongArray2D(final int h, final int w, final IntBinaryToLongFunction init) {
		this(h, w);
		setAll(init);
	}

	public LongArray2D(final long[][] a) {
		this(a.length, a[0].length);
		for (int i = 0, p = 0; i < h; i++, p += w) System.arraycopy(a[i], 0, arr, p, w);
	}

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

	public int h() {
		return h;
	}

	public int w() {
		return w;
	}

	public boolean inRange(final int i, final int j) {
		return (i | j | (h - 1 - i) | (w - 1 - j)) >= 0;
	}

	public long get(final int i, final int j) {
		return arr[checkedIndex(i, j)];
	}

	public long getOrDefault(final int i, final int j, final long defaultValue) {
		return inRange(i, j) ? arr[index(i, j)] : defaultValue;
	}

	public long set(final int i, final int j, final long v) {
		final int p = checkedIndex(i, j);
		final long old = arr[p];
		arr[p] = v;
		return old;
	}

	public long add(final int i, final int j, final long d) {
		return affine(i, j, 1, d);
	}

	public long multiply(final int i, final int j, final long m) {
		return affine(i, j, m, 0);
	}

	public long affine(final int i, final int j, final long m, final long d) {
		final int p = checkedIndex(i, j);
		return arr[p] = arr[p] * m + d;
	}

	public long apply(final int i, final int j, final long x, final LongBinaryOperator f) {
		final int p = checkedIndex(i, j);
		return arr[p] = f.applyAsLong(arr[p], x);
	}

	public void fill(final long v) {
		Arrays.fill(arr, v);
	}

	public void setAll(final IntBinaryToLongFunction init) {
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				arr[index(i, j)] = init.applyAsLong(i, j);
			}
		}
	}

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

	public LongArray2D rotateLeft(final int distance) {
		for (int k = distance & 3; k > 0; k--) rotateLeft();
		return this;
	}

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

	public LongArray2D rotateRight(final int distance) {
		for (int k = distance & 3; k > 0; k--) rotateRight();
		return this;
	}

	public LongArray2D transpose() {
		int t = di;
		di = dj;
		dj = t;
		t = h;
		h = w;
		w = t;
		return this;
	}

	public LongArray2D flipHorizontal() {
		base += (w - 1) * dj;
		dj = -dj;
		return this;
	}

	public LongArray2D flipVertical() {
		base += (h - 1) * di;
		di = -di;
		return this;
	}

	public long[][] toArray2D() {
		final long[][] res = new long[h][w];
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				res[i][j] = arr[index(i, j)];
			}
		}
		return res;
	}

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

	public Spliterator.OfLong spliterator() {
		return Spliterators.spliterator(iterator(), size, Spliterator.ORDERED);
	}

	public String toString() {
		if (isEmpty()) return "";
		final StringBuilder sb = new StringBuilder(21 * size - 1);
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
