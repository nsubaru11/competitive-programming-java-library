package lib.ds.arrays;

import java.util.*;
import java.util.function.*;

/**
 * 固定長の回転可能な long 配列。長さは正とし、論理添字 {@code 0 <= i < size} の範囲外では IndexOutOfBoundsException を投げる。
 * 回転はデータを移動せず、rotateLeft・rotateRight・resetRotation はこのインスタンスを変更して自身を返す。
 * distance が負の回転は逆方向として扱う。rotation は左回転を正、右回転を負とする総回転数を返す。
 * fill・setAll は回転状態と総回転数を0へ戻す。走査・配列化・toString は現在の論理順とする。
 */
public final class LongCircularArray implements LongMutableArray {
	public final int size;
	private final long[] arr;
	private long rotation;
	private int offset;

	public LongCircularArray(final int n) {
		size = n;
		arr = new long[n];
	}

	public LongCircularArray(final int n, final long v) {
		this(n);
		fill(v);
	}

	public LongCircularArray(final int n, final IntToLongFunction init) {
		this(n);
		setAll(init);
	}

	public LongCircularArray(final long[] a) {
		size = a.length;
		arr = a.clone();
	}

	public LongCircularArray(final LongArray a) {
		size = a.size();
		arr = a.toArray();
	}

	public LongCircularArray(final LongCircularArray other) {
		size = other.size;
		arr = other.arr.clone();
		rotation = other.rotation;
		offset = other.offset;
	}

	public int size() {
		return size;
	}

	public long rotation() {
		return rotation;
	}

	public long get(final int i) {
		int p = offset + Objects.checkIndex(i, size);
		if (p >= size) p -= size;
		return arr[p];
	}

	public long set(final int i, final long v) {
		int p = offset + Objects.checkIndex(i, size);
		if (p >= size) p -= size;
		final long old = arr[p];
		arr[p] = v;
		return old;
	}

	public void fill(final long v) {
		Arrays.fill(arr, v);
		rotation = 0;
		offset = 0;
	}

	public void setAll(final IntToLongFunction init) {
		for (int i = 0; i < size; i++) arr[i] = init.applyAsLong(i);
		rotation = 0;
		offset = 0;
	}

	public LongCircularArray rotateLeft() {
		offset++;
		if (offset == size) offset = 0;
		rotation++;
		return this;
	}

	public LongCircularArray rotateLeft(final int distance) {
		if (distance < 0) return rotateRight(-distance);
		offset += distance % size;
		if (offset >= size) offset -= size;
		rotation += distance;
		return this;
	}

	public LongCircularArray rotateRight() {
		offset--;
		if (offset == -1) offset = size - 1;
		rotation--;
		return this;
	}

	public LongCircularArray rotateRight(final int distance) {
		if (distance < 0) return rotateLeft(-distance);
		offset -= distance % size;
		if (offset < 0) offset += size;
		rotation -= distance;
		return this;
	}

	public LongCircularArray resetRotation() {
		rotation = 0;
		offset = 0;
		return this;
	}

	public long[] toArray() {
		final long[] res = new long[size];
		final int n = size - offset;
		System.arraycopy(arr, offset, res, 0, n);
		System.arraycopy(arr, 0, res, n, offset);
		return res;
	}

	public PrimitiveIterator.OfLong iterator() {
		return new PrimitiveIterator.OfLong() {
			private int i = 0;

			public boolean hasNext() {
				return i < size;
			}

			public long nextLong() {
				if (!hasNext()) throw new NoSuchElementException();
				return get(i++);
			}
		};
	}

	public String toString() {
		if (isEmpty()) return "";
		final StringBuilder sb = new StringBuilder(21 * size - 1);
		sb.append(arr[offset]);
		for (int i = 1; i < size; i++) sb.append(' ').append(get(i));
		return sb.toString();
	}
}
