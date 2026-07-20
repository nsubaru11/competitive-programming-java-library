package lib.ds.priorityqueue;

import static java.lang.Math.*;

import java.util.*;

import lib.ds.*;
import lib.util.function.*;

/**
 * 競技プログラミング向けのlong型優先度キューです。
 */
@SuppressWarnings("unused")
public final class LongPriorityQueue implements LongCollection {
	private static final int DEFAULT_INITIAL_CAPACITY = 1024;
	private final boolean isDescendingOrder;
	private final LongComparator comparator;
	private long[] buf;
	private int size, capacity, unsortedCount;

	/**
	 * 昇順のキューを構築します。
	 */
	public LongPriorityQueue() {
		this(DEFAULT_INITIAL_CAPACITY, false, null);
	}

	/**
	 * 指定した初期容量で昇順のキューを構築します。
	 */
	public LongPriorityQueue(final int initialCapacity) {
		this(initialCapacity, false, null);
	}

	/**
	 * 昇順または降順のキューを構築します。
	 */
	public LongPriorityQueue(final boolean isDescendingOrder) {
		this(DEFAULT_INITIAL_CAPACITY, isDescendingOrder, null);
	}

	/**
	 * 指定した初期容量で昇順または降順のキューを構築します。
	 */
	public LongPriorityQueue(final int initialCapacity, final boolean isDescendingOrder) {
		this(initialCapacity, isDescendingOrder, null);
	}

	/**
	 * 指定したComparatorでキューを構築します。
	 */
	public LongPriorityQueue(final LongComparator comparator) {
		this(DEFAULT_INITIAL_CAPACITY, false, Objects.requireNonNull(comparator));
	}

	/**
	 * 指定した初期容量とComparatorでキューを構築します。
	 */
	public LongPriorityQueue(final int initialCapacity, final LongComparator comparator) {
		this(initialCapacity, false, Objects.requireNonNull(comparator));
	}

	/**
	 * 配列の全要素を持つ昇順のキューを構築します。
	 */
	public LongPriorityQueue(final long[] values) {
		this(values, false);
	}

	/**
	 * 配列の全要素を持つ昇順または降順のキューを構築します。
	 */
	public LongPriorityQueue(final long[] values, final boolean isDescendingOrder) {
		this(max(1, values.length), isDescendingOrder, null);
		System.arraycopy(values, 0, buf, 0, values.length);
		size = unsortedCount = values.length;
	}

	/**
	 * 配列の全要素を持つComparator順のキューを構築します。
	 */
	public LongPriorityQueue(final long[] values, final LongComparator comparator) {
		this(max(1, values.length), false, Objects.requireNonNull(comparator));
		System.arraycopy(values, 0, buf, 0, values.length);
		size = unsortedCount = values.length;
	}

	private LongPriorityQueue(final int initialCapacity, final boolean isDescendingOrder, final LongComparator comparator) {
		capacity = max(1, initialCapacity);
		this.isDescendingOrder = isDescendingOrder;
		this.comparator = comparator;
		buf = new long[capacity];
	}

	/**
	 * 要素を追加します。
	 */
	public boolean add(final long v) {
		ensureCapacity(size + 1);
		buf[size++] = v;
		unsortedCount++;
		return true;
	}

	/**
	 * 配列の全要素を追加します。
	 */
	public boolean addAll(final long[] values) {
		final int n = values.length;
		if (n == 0) return false;
		ensureCapacity(size + n);
		System.arraycopy(values, 0, buf, size, n);
		size += n;
		unsortedCount += n;
		return true;
	}

	/**
	 * Iterableの全要素を追加します。
	 */
	public boolean addAll(final Iterable<Long> values) {
		if (values instanceof final Collection<Long> c) {
			final int n = c.size();
			if (n == 0) return false;
			ensureCapacity(size + n);
			for (final long v : c) buf[size++] = v;
			unsortedCount += n;
			return true;
		}
		boolean changed = false;
		for (final long v : values) {
			add(v);
			changed = true;
		}
		return changed;
	}

	/** 最優先要素を返します。 */
	public long peek() {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		return buf[0];
	}

	/**
	 * 空ならdefaultValue、そうでなければ最優先要素を返します。
	 */
	public long peekOrDefault(final long defaultValue) {
		return isEmpty() ? defaultValue : peek();
	}

	/**
	 * 2番目に優先される要素を返します。
	 */
	public long peekSecond() {
		if (size < 2) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		if (size == 2) return buf[1];
		return compare(buf[1], buf[2]) <= 0 ? buf[1] : buf[2];
	}

	/** 最優先要素を削除して返します。 */
	public long poll() {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		final long res = buf[0];
		if (--size > 0) siftDown(buf[size], 0);
		return res;
	}

	/**
	 * 空ならdefaultValue、そうでなければ最優先要素を削除して返します。
	 */
	public long pollOrDefault(final long defaultValue) {
		return isEmpty() ? defaultValue : poll();
	}

	/** 最優先要素を置き換え、置き換え前の値を返します。 */
	public long replaceTop(final long v) {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		final long res = buf[0];
		buf[0] = v;
		siftDown(v, 0);
		return res;
	}

	/**
	 * 現在の要素数を返します。
	 */
	public int size() {
		return size;
	}

	/**
	 * 現在の内部配列容量を返します。
	 */
	public int capacity() {
		return capacity;
	}

	/**
	 * 全要素を削除します。
	 */
	public void clear() {
		size = unsortedCount = 0;
	}

	/** 内部順で要素を走査するIteratorを返します。 */
	public PrimitiveIterator.OfLong iterator() {
		return new PrimitiveIterator.OfLong() {
			private int i;

			public boolean hasNext() {
				return i < size;
			}

			public long nextLong() {
				if (!hasNext()) throw new NoSuchElementException();
				return buf[i++];
			}
		};
	}

	private int compare(final long a, final long b) {
		if (comparator != null) return comparator.compare(a, b);
		return isDescendingOrder ? Long.compare(b, a) : Long.compare(a, b);
	}

	private void ensureCapacity(final int required) {
		if (required <= capacity) return;
		int newCapacity = capacity;
		while (newCapacity < required) newCapacity <<= 1;
		buf = Arrays.copyOf(buf, capacity = newCapacity);
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

	private void siftUp(final long v, int i) {
		while (i > 0) {
			final int p = (i - 1) >> 1;
			if (compare(v, buf[p]) >= 0) break;
			buf[i] = buf[p];
			i = p;
		}
		buf[i] = v;
	}

	private void siftDown(final long v, int i) {
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
