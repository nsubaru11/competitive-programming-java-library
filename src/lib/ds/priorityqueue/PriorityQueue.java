package lib.ds.priorityqueue;

import static java.lang.Math.*;

import java.util.*;

/**
 * 競技プログラミング向けのgeneric優先度キューです。
 * Comparatorを省略した場合は自然順序を使用し、要素が相互にComparableでなければ比較時に{@link ClassCastException}となります。
 *
 * @param <T> 要素型
 */
@SuppressWarnings({"unchecked", "unused"})
public final class PriorityQueue<T> implements Iterable<T> {
	private static final int DEFAULT_INITIAL_CAPACITY = 1024;
	private final Comparator<? super T> comparator;
	private T[] buf;
	private int size, capacity, unsortedCount;

	/**
	 * 自然順序のキューを構築します。
	 */
	public PriorityQueue() {
		this(DEFAULT_INITIAL_CAPACITY, false);
	}

	/**
	 * 指定した初期容量で自然順序のキューを構築します。
	 */
	public PriorityQueue(final int initialCapacity) {
		this(initialCapacity, false);
	}

	/**
	 * 自然順序または逆順のキューを構築します。
	 */
	public PriorityQueue(final boolean isDescendingOrder) {
		this(DEFAULT_INITIAL_CAPACITY, isDescendingOrder);
	}

	/**
	 * 指定した初期容量で自然順序または逆順のキューを構築します。
	 */
	public PriorityQueue(final int initialCapacity, final boolean isDescendingOrder) {
		this(initialCapacity, naturalComparator(isDescendingOrder));
	}

	/**
	 * 指定したComparatorでキューを構築します。
	 */
	public PriorityQueue(final Comparator<? super T> comparator) {
		this(DEFAULT_INITIAL_CAPACITY, comparator);
	}

	/**
	 * 指定した初期容量とComparatorでキューを構築します。
	 */
	public PriorityQueue(final int initialCapacity, final Comparator<? super T> comparator) {
		capacity = max(1, initialCapacity);
		this.comparator = Objects.requireNonNull(comparator);
		buf = (T[]) new Object[capacity];
	}

	/**
	 * 配列の全要素を持つ自然順序のキューを構築します。
	 */
	public PriorityQueue(final T[] values) {
		this(values, false);
	}

	/**
	 * 配列の全要素を持つ自然順序または逆順のキューを構築します。
	 */
	public PriorityQueue(final T[] values, final boolean isDescendingOrder) {
		this(values, naturalComparator(isDescendingOrder));
	}

	/**
	 * 配列の全要素を持つComparator順のキューを構築します。
	 */
	public PriorityQueue(final T[] values, final Comparator<? super T> comparator) {
		this(max(1, values.length), comparator);
		System.arraycopy(values, 0, buf, 0, values.length);
		size = unsortedCount = values.length;
	}

	/**
	 * Comparable要素を自然順序で扱うキューを返します。
	 */
	public static <T extends Comparable<? super T>> PriorityQueue<T> naturalOrder() {
		return new PriorityQueue<>(Comparator.naturalOrder());
	}

	/**
	 * Comparable要素を自然順序で扱うキューを返します。
	 */
	public static <T extends Comparable<? super T>> PriorityQueue<T> naturalOrder(final int initialCapacity) {
		return new PriorityQueue<>(initialCapacity, Comparator.naturalOrder());
	}

	/**
	 * Comparable要素を逆順で扱うキューを返します。
	 */
	public static <T extends Comparable<? super T>> PriorityQueue<T> reverseOrder() {
		return new PriorityQueue<>(Comparator.reverseOrder());
	}

	/**
	 * Comparable要素を逆順で扱うキューを返します。
	 */
	public static <T extends Comparable<? super T>> PriorityQueue<T> reverseOrder(final int initialCapacity) {
		return new PriorityQueue<>(initialCapacity, Comparator.reverseOrder());
	}

	/**
	 * 配列の全要素を自然順序で扱うキューを返します。
	 */
	public static <T extends Comparable<? super T>> PriorityQueue<T> naturalOrder(final T[] values) {
		return new PriorityQueue<>(values, Comparator.naturalOrder());
	}

	/**
	 * 配列の全要素を逆順で扱うキューを返します。
	 */
	public static <T extends Comparable<? super T>> PriorityQueue<T> reverseOrder(final T[] values) {
		return new PriorityQueue<>(values, Comparator.reverseOrder());
	}

	private static <T> Comparator<? super T> naturalComparator(final boolean isDescendingOrder) {
		return isDescendingOrder ? (Comparator<? super T>) Comparator.reverseOrder() : (Comparator<? super T>) Comparator.naturalOrder();
	}

	/**
	 * 要素を追加します。
	 */
	public boolean add(final T v) {
		ensureCapacity(size + 1);
		buf[size++] = v;
		unsortedCount++;
		return true;
	}

	/**
	 * 配列の全要素を追加します。
	 */
	public boolean addAll(final T[] values) {
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
	public boolean addAll(final Iterable<? extends T> values) {
		if (values instanceof final Collection<? extends T> c) {
			final int n = c.size();
			if (n == 0) return false;
			ensureCapacity(size + n);
			for (final T v : c) buf[size++] = v;
			unsortedCount += n;
			return true;
		}
		boolean changed = false;
		for (final T v : values) {
			add(v);
			changed = true;
		}
		return changed;
	}

	/**
	 * 最優先要素を返します。
	 */
	public T peek() {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		return buf[0];
	}

	/**
	 * 空ならdefaultValue、そうでなければ最優先要素を返します。
	 */
	public T peekOrDefault(final T defaultValue) {
		return isEmpty() ? defaultValue : peek();
	}

	/**
	 * 2番目に優先される要素を返します。
	 */
	public T peekSecond() {
		if (size < 2) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		if (size == 2) return buf[1];
		return comparator.compare(buf[1], buf[2]) <= 0 ? buf[1] : buf[2];
	}

	/**
	 * 最優先要素を削除して返します。
	 */
	public T poll() {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		final T res = buf[0];
		if (--size > 0) siftDown(buf[size], 0);
		return res;
	}

	/**
	 * 空ならdefaultValue、そうでなければ最優先要素を削除して返します。
	 */
	public T pollOrDefault(final T defaultValue) {
		return isEmpty() ? defaultValue : poll();
	}

	/** 最優先要素を置き換え、置き換え前の値を返します。 */
	public T replaceTop(final T v) {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		final T res = buf[0];
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

	/** 現在の内部配列容量を返します。 */
	public int capacity() {
		return capacity;
	}

	/**
	 * キューをO(1)で論理的に空にします。
	 */
	public void clear() {
		size = unsortedCount = 0;
	}

	/**
	 * キューが空か判定します。
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/** 内部順で要素を走査するIteratorを返します。 */
	public Iterator<T> iterator() {
		return new Iterator<>() {
			private int i;

			public boolean hasNext() {
				return i < size;
			}

			public T next() {
				if (!hasNext()) throw new NoSuchElementException();
				return buf[i++];
			}
		};
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

	private void siftUp(final T v, int i) {
		while (i > 0) {
			final int p = (i - 1) >> 1;
			if (comparator.compare(v, buf[p]) >= 0) break;
			buf[i] = buf[p];
			i = p;
		}
		buf[i] = v;
	}

	private void siftDown(final T v, int i) {
		final int half = size >> 1;
		while (i < half) {
			int child = (i << 1) + 1;
			if (child + 1 < size && comparator.compare(buf[child], buf[child + 1]) > 0) child++;
			if (comparator.compare(v, buf[child]) <= 0) break;
			buf[i] = buf[child];
			i = child;
		}
		buf[i] = v;
	}
}
