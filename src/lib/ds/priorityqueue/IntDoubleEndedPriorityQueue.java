package lib.ds.priorityqueue;

import static java.lang.Math.*;

import java.util.*;

import lib.ds.*;
import lib.util.function.*;

/**
 * int値の両端優先度キュー（interval heap）。最小値と最大値の両方を取り出す。
 * 開発中であり、pollFirst・pollLast が誤った値を返すことがあるため使用しない。
 */
public final class IntDoubleEndedPriorityQueue implements IntCollection {
	// TODO: 開発中。interval heap がヒープ性を保てず pollFirst・pollLast が誤った値を返すため、完成まで使用しない
	// TODO: 要素数が奇数の末尾ノードを考慮して siftUp・siftDown・heapify を実装する
	// TODO: Comparator の null 検査・Guide・Test・Long 版を整備する
	private static final int DEFAULT_INITIAL_CAPACITY = 1024;
	private final IntComparator comparator;
	private int[] buf;
	private int size, capacity, unsortedCount;

	public IntDoubleEndedPriorityQueue() {
		this(DEFAULT_INITIAL_CAPACITY, null);
	}

	public IntDoubleEndedPriorityQueue(final int initialCapacity) {
		this(initialCapacity, null);
	}

	public IntDoubleEndedPriorityQueue(final IntComparator comparator) {
		this(DEFAULT_INITIAL_CAPACITY, comparator);
	}

	public IntDoubleEndedPriorityQueue(final int initialCapacity, final IntComparator comparator) {
		capacity = max(64, initialCapacity);
		this.comparator = comparator;
		buf = new int[capacity];
	}

	public IntDoubleEndedPriorityQueue(final int[] values) {
		this(values, null);
	}

	public IntDoubleEndedPriorityQueue(final int[] values, final IntComparator comparator) {
		this(values.length, comparator);
		System.arraycopy(values, 0, buf, 0, values.length);
		size = unsortedCount = values.length;
	}

	public int size() {
		return size;
	}

	public int capacity() {
		return capacity;
	}

	public int peekFirst() {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		return buf[0];
	}

	public int peekFirstOrDefault(final int defaultValue) {
		return isEmpty() ? defaultValue : peekFirst();
	}

	public int peekLast() {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		return size > 1 ? buf[1] : buf[0];
	}

	public int peekLastOrDefault(final int defaultValue) {
		return isEmpty() ? defaultValue : peekLast();
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

	public int pollFirst() {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		final int res = buf[0];
		if (--size > 0) siftDown(buf[size], 0);
		return res;
	}

	public int pollFirstOrDefault(final int defaultValue) {
		return isEmpty() ? defaultValue : pollFirst();
	}

	public int pollLast() {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		int i = size > 1 ? 1 : 0;
		final int res = buf[i];
		if (--size > 0) siftDown(buf[size], i);
		return res;
	}

	public int pollLastOrDefault(final int defaultValue) {
		return isEmpty() ? defaultValue : pollLast();
	}

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

	private void siftDown(final int v, final int i) {
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

	private void siftUp(final int v, final int i) {
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
