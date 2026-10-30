package lib.ds.arrays;

import static java.lang.Math.*;

import java.util.*;
import java.util.function.*;

import lib.ds.*;

/**
 * long値の可変長の両端キュー。容量が不足すると内部配列を2倍に拡張する。
 * 添字は先頭を0とし、範囲外では IndexOutOfBoundsException を投げる。
 * 空の removeFirst・removeLast は NoSuchElementException を投げ、OrDefault 系は指定した既定値を返す。
 * set は更新前の値を返し、配列全体を変更するメソッドは自身を返す。
 */
public final class LongArrayDeque implements LongMutableArray {
	private long[] buf;
	private int head, size;

	public LongArrayDeque() {
		this(16);
	}

	/**
	 * 空の deque を構築する。初期容量は1以上の2のべき乗へ切り上げる。
	 */
	public LongArrayDeque(final int capacity) {
		buf = new long[1 << (32 - Integer.numberOfLeadingZeros(capacity - 1))];
	}

	public LongArrayDeque(final long[] a) {
		this(a.length);
		System.arraycopy(a, 0, buf, 0, a.length);
		size = a.length;
	}

	public LongArrayDeque(final LongCollection a) {
		this(a.size());
		size = a.size();
		final PrimitiveIterator.OfLong it = a.iterator();
		for (int i = 0; i < size; i++) buf[i] = it.nextLong();
	}

	public LongArrayDeque(final LongArrayDeque other) {
		buf = other.buf.clone();
		head = other.head;
		size = other.size;
	}

	public int size() {
		return size;
	}

	public long get(final int i) {
		return buf[physicalIndex(Objects.checkIndex(i, size))];
	}

	public long set(final int i, final long v) {
		final int p = physicalIndex(Objects.checkIndex(i, size));
		final long old = buf[p];
		buf[p] = v;
		return old;
	}

	public void addFirst(final long v) {
		if (size == buf.length) grow();
		head = physicalIndex(buf.length - 1);
		buf[head] = v;
		size++;
	}

	public void addLast(final long v) {
		if (size == buf.length) grow();
		buf[physicalIndex(size++)] = v;
	}

	public long removeFirst() {
		if (isEmpty()) throw new NoSuchElementException();
		final long first = buf[head];
		size--;
		head = physicalIndex(1);
		return first;
	}

	public long removeFirstOrDefault(final long defaultValue) {
		return isEmpty() ? defaultValue : removeFirst();
	}

	public long removeLast() {
		if (isEmpty()) throw new NoSuchElementException();
		return buf[physicalIndex(--size)];
	}

	public long removeLastOrDefault(final long defaultValue) {
		return isEmpty() ? defaultValue : removeLast();
	}

	public LongArrayDeque fill(final long v) {
		Arrays.fill(buf, 0, size, v);
		head = 0;
		return this;
	}

	public LongArrayDeque setAll(final IntToLongFunction init) {
		for (int i = 0; i < size; i++) buf[i] = init.applyAsLong(i);
		head = 0;
		return this;
	}

	public void clear() {
		head = size = 0;
	}

	public LongArrayDeque swap(final int i, final int j) {
		LongMutableArray.super.swap(i, j);
		return this;
	}

	public LongArrayDeque reverse() {
		return reverse(0, size);
	}

	public LongArrayDeque reverse(final int l, final int r) {
		LongMutableArray.super.reverse(l, r);
		return this;
	}

	public long[] toArray() {
		final long[] res = new long[size];
		final int n = min(size, buf.length - head);
		System.arraycopy(buf, head, res, 0, n);
		System.arraycopy(buf, 0, res, n, size - n);
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
				return buf[physicalIndex(i++)];
			}
		};
	}

	public String toString() {
		if (isEmpty()) return "";
		final StringBuilder sb = new StringBuilder(21 * size - 1);
		sb.append(buf[head]);
		for (int i = 1; i < size; i++) sb.append(' ').append(buf[physicalIndex(i)]);
		return sb.toString();
	}

	private int physicalIndex(final int i) {
		return (head + i) & (buf.length - 1);
	}

	private void grow() {
		final int n = buf.length;
		final long[] nb = new long[n << 1];
		System.arraycopy(buf, head, nb, 0, n - head);
		System.arraycopy(buf, 0, nb, n - head, head);
		buf = nb;
		head = 0;
	}
}
