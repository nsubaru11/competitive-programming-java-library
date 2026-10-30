package lib.ds.priorityqueue;

import static java.lang.Math.*;

import java.util.*;

import lib.ds.*;
import lib.util.function.*;

/**
 * int値の可変長の優先度キュー（二分ヒープ）。比較順で先頭となる値を優先し、同順位の値の取り出し順は規定しない。
 * 比較順は昇順・降順・IntComparator から選ぶ。
 * 追加時のヒープ化は遅延し、peek・poll などの直前にまとめて行う。空の peek・poll・replaceTop は NoSuchElementException を投げ、OrDefault 系は指定した既定値を返す。
 * 走査と toString は優先順ではなく内部配列の順に行う。
 */
public final class IntPriorityQueue implements IntCollection {
	private static final int DEFAULT_INITIAL_CAPACITY = 1024;
	private final boolean isDescendingOrder;
	private final IntComparator comparator;
	private int[] buf;
	private int size, unsortedCount;

	public IntPriorityQueue() {
		this(DEFAULT_INITIAL_CAPACITY, false, null);
	}

	public IntPriorityQueue(final int initialCapacity) {
		this(initialCapacity, false, null);
	}

	public IntPriorityQueue(final boolean isDescendingOrder) {
		this(DEFAULT_INITIAL_CAPACITY, isDescendingOrder, null);
	}

	public IntPriorityQueue(final int initialCapacity, final boolean isDescendingOrder) {
		this(initialCapacity, isDescendingOrder, null);
	}

	public IntPriorityQueue(final IntComparator comparator) {
		this(DEFAULT_INITIAL_CAPACITY, false, Objects.requireNonNull(comparator));
	}

	public IntPriorityQueue(final int initialCapacity, final IntComparator comparator) {
		this(initialCapacity, false, Objects.requireNonNull(comparator));
	}

	public IntPriorityQueue(final int[] values) {
		this(values, false);
	}

	public IntPriorityQueue(final int[] values, final boolean isDescendingOrder) {
		this(values.length, isDescendingOrder, null);
		addAll(values);
	}

	public IntPriorityQueue(final int[] values, final IntComparator comparator) {
		this(values.length, false, Objects.requireNonNull(comparator));
		addAll(values);
	}

	public IntPriorityQueue(final IntCollection values) {
		this(values, false);
	}

	public IntPriorityQueue(final IntCollection values, final boolean isDescendingOrder) {
		this(values.size(), isDescendingOrder, null);
		addAll(values);
	}

	public IntPriorityQueue(final IntCollection values, final IntComparator comparator) {
		this(values.size(), false, Objects.requireNonNull(comparator));
		addAll(values);
	}

	public IntPriorityQueue(final IntPriorityQueue other) {
		isDescendingOrder = other.isDescendingOrder;
		comparator = other.comparator;
		buf = other.buf.clone();
		size = other.size;
		unsortedCount = other.unsortedCount;
	}

	private IntPriorityQueue(final int initialCapacity, final boolean isDescendingOrder, final IntComparator comparator) {
		this.isDescendingOrder = isDescendingOrder;
		this.comparator = comparator;
		buf = new int[max(1, initialCapacity)];
	}

	public int size() {
		return size;
	}

	public int capacity() {
		return buf.length;
	}

	public int peek() {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		return buf[0];
	}

	public int peekOrDefault(final int defaultValue) {
		return isEmpty() ? defaultValue : peek();
	}

	/**
	 * 2番目に優先される値を返す。要素数は2以上とする。
	 */
	public int peekSecond() {
		if (size < 2) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		if (size == 2) return buf[1];
		return compare(buf[1], buf[2]) <= 0 ? buf[1] : buf[2];
	}

	public boolean add(final int v) {
		ensureCapacity(size + 1);
		buf[size++] = v;
		unsortedCount++;
		return true;
	}

	public boolean addAll(final int[] values) {
		final int n = values.length;
		if (n == 0) return false;
		ensureCapacity(size + n);
		System.arraycopy(values, 0, buf, size, n);
		size += n;
		unsortedCount += n;
		return true;
	}

	public boolean addAll(final IntCollection values) {
		final int n = values.size();
		if (n == 0) return false;
		ensureCapacity(size + n);
		final PrimitiveIterator.OfInt it = values.iterator();
		for (int i = 0; i < n; i++) buf[size++] = it.nextInt();
		unsortedCount += n;
		return true;
	}

	public boolean addAll(final Iterable<Integer> values) {
		if (values instanceof final IntCollection c) return addAll(c);
		if (values instanceof final Collection<Integer> c) {
			final int n = c.size();
			if (n == 0) return false;
			ensureCapacity(size + n);
			for (final int v : c) buf[size++] = v;
			unsortedCount += n;
			return true;
		}
		boolean changed = false;
		for (final int v : values) {
			add(v);
			changed = true;
		}
		return changed;
	}

	public int poll() {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		final int res = buf[0];
		if (--size > 0) siftDown(buf[size], 0);
		return res;
	}

	public int pollOrDefault(final int defaultValue) {
		return isEmpty() ? defaultValue : poll();
	}

	/**
	 * 先頭の値を v に置き換え、置き換え前の値を返す。poll と add を続けて呼ぶより速い。
	 */
	public int replaceTop(final int v) {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		final int res = buf[0];
		siftDown(v, 0);
		return res;
	}

	public void clear() {
		size = unsortedCount = 0;
	}

	public PrimitiveIterator.OfInt iterator() {
		return new PrimitiveIterator.OfInt() {
			private int i;

			public boolean hasNext() {
				return i < size;
			}

			public int nextInt() {
				if (!hasNext()) throw new NoSuchElementException();
				return buf[i++];
			}
		};
	}

	public String toString() {
		if (isEmpty()) return "";
		final StringBuilder sb = new StringBuilder(12 * size - 1);
		sb.append(buf[0]);
		for (int i = 1; i < size; i++) sb.append(' ').append(buf[i]);
		return sb.toString();
	}

	private int compare(final int a, final int b) {
		if (comparator != null) return comparator.compare(a, b);
		return isDescendingOrder ? Integer.compare(b, a) : Integer.compare(a, b);
	}

	private void ensureCapacity(final int required) {
		if (required <= buf.length) return;
		int newCapacity = buf.length;
		while (newCapacity < required) newCapacity <<= 1;
		buf = Arrays.copyOf(buf, newCapacity);
	}

	private void ensureHeapProperty() {
		final int log2N = 31 - Integer.numberOfLeadingZeros(size);
		final int heapifyCost = size * 2 - 2 * log2N;
		final int incrementalCost = unsortedCount <= 100 ? getIncrementalCostStrict() : getIncrementalCostApprox();
		if (heapifyCost < incrementalCost) heapify();
		else for (int i = size - unsortedCount; i < size; i++) siftUp(buf[i], i);
		unsortedCount = 0;
	}

	private int getIncrementalCostStrict() {
		int cost = 0;
		final int sortedSize = size - unsortedCount;
		for (int i = 1; i <= unsortedCount; i++) cost += 31 - Integer.numberOfLeadingZeros(sortedSize + i);
		return cost;
	}

	private int getIncrementalCostApprox() {
		final int averageSize = size - unsortedCount + (unsortedCount >> 1);
		if (averageSize == 0) return 0;
		return unsortedCount * (31 - Integer.numberOfLeadingZeros(averageSize));
	}

	private void heapify() {
		for (int i = (size >> 1) - 1; i >= 0; i--) siftDown(buf[i], i);
	}

	private void siftUp(final int v, int i) {
		while (i > 0) {
			final int p = (i - 1) >> 1;
			if (compare(v, buf[p]) >= 0) break;
			buf[i] = buf[p];
			i = p;
		}
		buf[i] = v;
	}

	private void siftDown(final int v, int i) {
		final int half = size >> 1;
		while (i < half) {
			int child = (i << 1) + 1;
			if (child + 1 < size && compare(buf[child], buf[child + 1]) > 0) child++;
			if (compare(v, buf[child]) <= 0) break;
			buf[i] = buf[child];
			i = child;
		}
		buf[i] = v;
	}
}
