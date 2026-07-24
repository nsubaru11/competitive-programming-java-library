package lib.ds.priorityqueue;

import static java.lang.Math.*;

import java.util.*;

import lib.ds.*;
import lib.util.function.*;

/**
 * 競技プログラミング向けのint型優先度キューです。
 */
@SuppressWarnings("unused")
public final class IntDoubleEndedPriorityQueue implements IntCollection {
	private static final int DEFAULT_INITIAL_CAPACITY = 1024;
	private final IntComparator comparator;
	private int[] buf;
	private int size, capacity, unsortedCount;

	/**
	 * 昇順のキューを構築します。
	 */
	public IntDoubleEndedPriorityQueue() {
		this(DEFAULT_INITIAL_CAPACITY, null);
	}

	/**
	 * 指定した初期容量で昇順のキューを構築します。
	 */
	public IntDoubleEndedPriorityQueue(final int initialCapacity) {
		this(initialCapacity, null);
	}

	/**
	 * 指定したComparatorでキューを構築します。
	 */
	public IntDoubleEndedPriorityQueue(final IntComparator comparator) {
		this(DEFAULT_INITIAL_CAPACITY, comparator);
	}

	/**
	 * 指定した初期容量とComparatorでキューを構築します。
	 */
	public IntDoubleEndedPriorityQueue(final int initialCapacity, final IntComparator comparator) {
		capacity = max(64, initialCapacity);
		this.comparator = comparator;
		buf = new int[capacity];
	}

	/**
	 * 配列の全要素を持つ昇順のキューを構築します。
	 */
	public IntDoubleEndedPriorityQueue(final int[] values) {
		this(values, null);
	}

	/**
	 * 配列の全要素を持つComparator順のキューを構築します。
	 */
	public IntDoubleEndedPriorityQueue(final int[] values, final IntComparator comparator) {
		this(values.length, comparator);
		System.arraycopy(values, 0, buf, 0, values.length);
		size = unsortedCount = values.length;
	}

	/**
	 * 要素を追加します。
	 */
	public boolean add(final int v) {
		ensureCapacity(size + 1);
		buf[size++] = v;
		unsortedCount++;
		return true;
	}

	/**
	 * 配列の全要素を追加します。
	 */
	public boolean addAll(final int[] values) {
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
	public boolean addAll(final Iterable<Integer> values) {
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

	/**
	 * 最優先要素を返します。
	 */
	public int peekFirst() {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		return buf[0];
	}

	/**
	 * 空ならdefaultValue、そうでなければ最優先要素を返します。
	 */
	public int peekFirstOrDefault(final int defaultValue) {
		return isEmpty() ? defaultValue : peekFirst();
	}

	/**
	 *
	 */
	public int peekLast() {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		return size > 1 ? buf[1] : buf[0];
	}

	/**
	 * 空ならdefaultValue、そうでなければ。
	 */
	public int peekLastOrDefault(final int defaultValue) {
		return isEmpty() ? defaultValue : peekLast();
	}

	/**
	 * 最優先要素を削除して返します。
	 */
	public int pollFirst() {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		final int res = buf[0];
		if (--size > 0) siftDown(buf[size], 0);
		return res;
	}

	/**
	 * 空ならdefaultValue、そうでなければ最優先要素を削除して返します。
	 */
	public int pollFirstOrDefault(final int defaultValue) {
		return isEmpty() ? defaultValue : pollFirst();
	}

	/**
	 *
	 */
	public int pollLast() {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		int i = size > 1 ? 1 : 0;
		final int res = buf[i];
		if (--size > 0) siftDown(buf[size], i);
		return res;
	}

	/**
	 * 空ならdefaultValue、
	 */
	public int pollLastOrDefault(final int defaultValue) {
		return isEmpty() ? defaultValue : pollLast();
	}

	/**
	 * 最優先要素を置き換え、置き換え前の値を返します。
	 */
	public int replaceFirst(final int v) {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		final int res = buf[0];
		buf[0] = v;
		siftDown(v, 0);
		return res;
	}

	public int replaceLast(final int v) {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		int i = size > 1 ? 1 : 0;
		final int res = buf[i];
		buf[i] = v;
		siftDown(v, i);
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

	/**
	 * 内部順で要素を走査するIteratorを返します。
	 */
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

	private int compare(final int a, final int b) {
		return comparator != null ? comparator.compare(a, b) : Integer.compare(a, b);
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

	public void siftDown(final int v, final int i) {
		if ((i & 1) == 0) {
			if (compare(v, buf[i + 1]) <= 0) {
				siftDownAsMinHeap(v, i);
			} else {
				buf[i] = buf[i + 1];
				siftDownAsMaxHeap(v, i + 1);
			}
		} else {
			if (compare(buf[i - 1], v) <= 0) {
				siftDownAsMaxHeap(v, i);
			} else {
				buf[i] = buf[i - 1];
				siftDownAsMinHeap(v, i - 1);
			}
		}
	}

	public void siftUp(final int v, final int i) {
		if ((i & 1) == 0) {
			if (i + 1 >= size) {
				siftUpAsMaxHeap(v, i);
				siftUpAsMinHeap(v, i);
			} else {
				if (compare(v, buf[i + 1]) <= 0) {
					siftUpAsMinHeap(v, i);
				} else {
					buf[i] = buf[i + 1];
					siftUpAsMaxHeap(v, i + 1);
				}
			}
		} else {
			if (compare(buf[i - 1], v) <= 0) {
				siftUpAsMaxHeap(v, i);
			} else {
				buf[i] = buf[i - 1];
				siftUpAsMinHeap(v, i - 1);
			}
		}
	}

	private void siftUpAsMaxHeap(final int v, int i) {
		while (i > 1) {
			final int p = ((i >> 1) - 1) | 1;
			if (compare(v, buf[p]) <= 0) break;
			buf[i] = buf[p];
			i = p;
		}
		buf[i] = v;
	}

	private int siftDownAsMaxHeap(final int v, int i) {
		final int half = size >> 1;
		while (i < half) {
			int child = (i << 1) + 1;
			if (child + 2 < size && compare(buf[child], buf[child + 2]) < 0) child += 2;
			if (compare(v, buf[child]) >= 0) break;
			buf[i] = buf[child];
			i = child;
		}
		buf[i] = v;
		return i;
	}

	private void siftUpAsMinHeap(final int v, int i) {
		while (i > 0) {
			final int p = ((i >> 1) - 1) & ~1;
			if (compare(v, buf[p]) >= 0) break;
			buf[i] = buf[p];
			i = p;
		}
		buf[i] = v;
	}

	private int siftDownAsMinHeap(final int v, int i) {
		final int half = size >> 1;
		while (i < half) {
			int child = (i << 1) + 2;
			if (child + 2 < size && compare(buf[child], buf[child + 2]) > 0) child += 2;
			if (compare(v, buf[child]) <= 0) break;
			buf[i] = buf[child];
			i = child;
		}
		buf[i] = v;
		return i;
	}
}
