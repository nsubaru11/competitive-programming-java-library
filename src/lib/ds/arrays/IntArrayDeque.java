package lib.ds.arrays;

import static java.lang.Math.*;

import java.util.*;
import java.util.function.*;

import lib.ds.*;

/**
 * int値の可変長の両端キュー。容量が不足すると内部配列を2倍に拡張する。
 * 添字は先頭を0とし、範囲外では IndexOutOfBoundsException を投げる。
 * 空の removeFirst・removeLast は NoSuchElementException を投げ、OrDefault 系は指定した既定値を返す。
 * set は更新前の値を返し、配列全体を変更するメソッドは自身を返す。
 */
public final class IntArrayDeque implements IntMutableArray {
	private int[] buf;
	private int head, size;

	public IntArrayDeque() {
		this(16);
	}

	/**
	 * 空の deque を構築する。初期容量は1以上の2のべき乗へ切り上げる。
	 */
	public IntArrayDeque(final int capacity) {
		buf = new int[1 << (32 - Integer.numberOfLeadingZeros(capacity - 1))];
	}

	public IntArrayDeque(final int[] a) {
		this(a.length);
		System.arraycopy(a, 0, buf, 0, a.length);
		size = a.length;
	}

	public IntArrayDeque(final IntCollection a) {
		this(a.size());
		size = a.size();
		final PrimitiveIterator.OfInt it = a.iterator();
		for (int i = 0; i < size; i++) buf[i] = it.nextInt();
	}

	public IntArrayDeque(final IntArrayDeque other) {
		buf = other.buf.clone();
		head = other.head;
		size = other.size;
	}

	public int size() {
		return size;
	}

	public int get(final int i) {
		return buf[physicalIndex(Objects.checkIndex(i, size))];
	}

	public int set(final int i, final int v) {
		final int p = physicalIndex(Objects.checkIndex(i, size));
		final int old = buf[p];
		buf[p] = v;
		return old;
	}

	public void addFirst(final int v) {
		if (size == buf.length) grow();
		head = physicalIndex(buf.length - 1);
		buf[head] = v;
		size++;
	}

	public void addLast(final int v) {
		if (size == buf.length) grow();
		buf[physicalIndex(size++)] = v;
	}

	public int removeFirst() {
		if (isEmpty()) throw new NoSuchElementException();
		final int first = buf[head];
		size--;
		head = physicalIndex(1);
		return first;
	}

	public int removeFirstOrDefault(final int defaultValue) {
		return isEmpty() ? defaultValue : removeFirst();
	}

	public int removeLast() {
		if (isEmpty()) throw new NoSuchElementException();
		return buf[physicalIndex(--size)];
	}

	public int removeLastOrDefault(final int defaultValue) {
		return isEmpty() ? defaultValue : removeLast();
	}

	public IntArrayDeque fill(final int v) {
		Arrays.fill(buf, 0, size, v);
		head = 0;
		return this;
	}

	public IntArrayDeque setAll(final IntUnaryOperator init) {
		for (int i = 0; i < size; i++) buf[i] = init.applyAsInt(i);
		head = 0;
		return this;
	}

	public void clear() {
		head = size = 0;
	}

	public IntArrayDeque swap(final int i, final int j) {
		IntMutableArray.super.swap(i, j);
		return this;
	}

	public IntArrayDeque reverse() {
		return reverse(0, size);
	}

	public IntArrayDeque reverse(final int l, final int r) {
		IntMutableArray.super.reverse(l, r);
		return this;
	}

	public int[] toArray() {
		final int[] res = new int[size];
		final int n = min(size, buf.length - head);
		System.arraycopy(buf, head, res, 0, n);
		System.arraycopy(buf, 0, res, n, size - n);
		return res;
	}

	public PrimitiveIterator.OfInt iterator() {
		return new PrimitiveIterator.OfInt() {
			private int i = 0;

			public boolean hasNext() {
				return i < size;
			}

			public int nextInt() {
				if (!hasNext()) throw new NoSuchElementException();
				return buf[physicalIndex(i++)];
			}
		};
	}

	public String toString() {
		if (isEmpty()) return "";
		final StringBuilder sb = new StringBuilder(12 * size - 1);
		sb.append(buf[head]);
		for (int i = 1; i < size; i++) sb.append(' ').append(buf[physicalIndex(i)]);
		return sb.toString();
	}

	private int physicalIndex(final int i) {
		return (head + i) & (buf.length - 1);
	}

	private void grow() {
		final int n = buf.length;
		final int[] nb = new int[n << 1];
		System.arraycopy(buf, head, nb, 0, n - head);
		System.arraycopy(buf, 0, nb, n - head, head);
		buf = nb;
		head = 0;
	}
}
