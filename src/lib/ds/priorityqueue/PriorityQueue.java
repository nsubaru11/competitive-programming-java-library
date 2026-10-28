package lib.ds.priorityqueue;

import static java.lang.Math.*;

import java.util.*;

/**
 * 可変容量の優先度キュー。Comparator の比較順で先頭となる要素を最優先とし、同順位の要素の取り出し順は規定しない。
 * Comparator を省略した場合は自然順序または逆順を使い、要素が相互に Comparable でなければ比較時に ClassCastException を投げる。
 * naturalOrder・reverseOrder ファクトリはこの前提を型で保証する。null は追加しない。
 * 初期容量は1未満なら1とし、容量不足時は2倍へ拡張する。
 * add・addAll・配列や Collection からの構築はヒープ化を遅延し、peek・poll など優先順が必要な操作の直前にまとめて復元する。
 * addAll は1件以上追加した場合に true を返す。clear は内部配列を再利用する。
 * 空の peek・poll・replaceTop は NoSuchElementException を投げ、OrDefault 系は状態を変えずに defaultValue を返す。
 * 走査はヒープを復元せず、内部配列の順に行う。
 *
 * @param <T> 要素型
 */
@SuppressWarnings("unchecked")
public final class PriorityQueue<T> implements Iterable<T> {
	private static final int DEFAULT_INITIAL_CAPACITY = 1024;
	private final Comparator<? super T> comparator;
	private T[] buf;
	private int size, unsortedCount;

	public PriorityQueue() {
		this(DEFAULT_INITIAL_CAPACITY, false);
	}

	public PriorityQueue(final int initialCapacity) {
		this(initialCapacity, false);
	}

	public PriorityQueue(final boolean isDescendingOrder) {
		this(DEFAULT_INITIAL_CAPACITY, isDescendingOrder);
	}

	public PriorityQueue(final int initialCapacity, final boolean isDescendingOrder) {
		this(initialCapacity, naturalComparator(isDescendingOrder));
	}

	public PriorityQueue(final Comparator<? super T> comparator) {
		this(DEFAULT_INITIAL_CAPACITY, comparator);
	}

	public PriorityQueue(final int initialCapacity, final Comparator<? super T> comparator) {
		this.comparator = Objects.requireNonNull(comparator);
		buf = (T[]) new Object[max(1, initialCapacity)];
	}

	public PriorityQueue(final T[] values) {
		this(values, false);
	}

	public PriorityQueue(final T[] values, final boolean isDescendingOrder) {
		this(values, naturalComparator(isDescendingOrder));
	}

	public PriorityQueue(final T[] values, final Comparator<? super T> comparator) {
		this(values.length, comparator);
		addAll(values);
	}

	/**
	 * values の全要素を自然順序で持つキューを構築する。values が比較順を持っていても引き継がない。
	 */
	public PriorityQueue(final Collection<? extends T> values) {
		this(values, false);
	}

	public PriorityQueue(final Collection<? extends T> values, final boolean isDescendingOrder) {
		this(values, naturalComparator(isDescendingOrder));
	}

	public PriorityQueue(final Collection<? extends T> values, final Comparator<? super T> comparator) {
		this(values.size(), comparator);
		addAll(values);
	}

	/**
	 * 比較順・要素・容量を引き継ぎ、内部配列を共有しない独立したコピーを構築する。要素自体は共有する。
	 */
	public PriorityQueue(final PriorityQueue<T> other) {
		comparator = other.comparator;
		buf = other.buf.clone();
		size = other.size;
		unsortedCount = other.unsortedCount;
	}

	public static <T extends Comparable<? super T>> PriorityQueue<T> naturalOrder() {
		return new PriorityQueue<>(Comparator.naturalOrder());
	}

	public static <T extends Comparable<? super T>> PriorityQueue<T> naturalOrder(final int initialCapacity) {
		return new PriorityQueue<>(initialCapacity, Comparator.naturalOrder());
	}

	public static <T extends Comparable<? super T>> PriorityQueue<T> naturalOrder(final T[] values) {
		return new PriorityQueue<>(values, Comparator.naturalOrder());
	}

	public static <T extends Comparable<? super T>> PriorityQueue<T> reverseOrder() {
		return new PriorityQueue<>(Comparator.reverseOrder());
	}

	public static <T extends Comparable<? super T>> PriorityQueue<T> reverseOrder(final int initialCapacity) {
		return new PriorityQueue<>(initialCapacity, Comparator.reverseOrder());
	}

	public static <T extends Comparable<? super T>> PriorityQueue<T> reverseOrder(final T[] values) {
		return new PriorityQueue<>(values, Comparator.reverseOrder());
	}

	private static <T> Comparator<? super T> naturalComparator(final boolean isDescendingOrder) {
		return isDescendingOrder ? (Comparator<? super T>) Comparator.reverseOrder() : (Comparator<? super T>) Comparator.naturalOrder();
	}

	public int size() {
		return size;
	}

	/**
	 * 内部配列の長さを返す。
	 */
	public int capacity() {
		return buf.length;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public T peek() {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		return buf[0];
	}

	public T peekOrDefault(final T defaultValue) {
		return isEmpty() ? defaultValue : peek();
	}

	/**
	 * 2番目に優先される要素を返す。要素数は2以上とする。
	 */
	public T peekSecond() {
		if (size < 2) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		if (size == 2) return buf[1];
		return comparator.compare(buf[1], buf[2]) <= 0 ? buf[1] : buf[2];
	}

	public boolean add(final T v) {
		ensureCapacity(size + 1);
		buf[size++] = v;
		unsortedCount++;
		return true;
	}

	public boolean addAll(final T[] values) {
		final int n = values.length;
		if (n == 0) return false;
		ensureCapacity(size + n);
		System.arraycopy(values, 0, buf, size, n);
		size += n;
		unsortedCount += n;
		return true;
	}

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

	public T poll() {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		final T res = buf[0];
		if (--size > 0) siftDown(buf[size], 0);
		return res;
	}

	public T pollOrDefault(final T defaultValue) {
		return isEmpty() ? defaultValue : poll();
	}

	/**
	 * 最優先の要素を v に置き換え、置き換え前の要素を返す。poll と add を続けて行うより速い。
	 */
	public T replaceTop(final T v) {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		final T res = buf[0];
		siftDown(v, 0);
		return res;
	}

	public void clear() {
		size = unsortedCount = 0;
	}

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
