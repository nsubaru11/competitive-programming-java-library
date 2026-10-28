package lib.ds.priorityqueue;

import static java.lang.Math.*;

import java.util.*;

import lib.ds.*;
import lib.util.function.*;

/**
 * long値の可変容量の優先度キュー。比較順で先頭となる値を最優先とし、同順位の値の取り出し順は規定しない。
 * 比較順は昇順・降順・LongComparator から選ぶ。初期容量は1未満なら1とし、容量不足時は2倍へ拡張する。
 * add・addAll・配列や LongCollection からの構築はヒープ化を遅延し、peek・poll など優先順が必要な操作の直前にまとめて復元する。
 * addAll は1件以上追加した場合に true を返す。clear は内部配列を再利用する。
 * 空の peek・poll・replaceTop は NoSuchElementException を投げ、OrDefault 系は状態を変えずに defaultValue を返す。
 * 走査はヒープを復元せず、内部配列の順に行う。
 */
public final class LongPriorityQueue implements LongCollection {
	private static final int DEFAULT_INITIAL_CAPACITY = 1024;
	private final boolean isDescendingOrder;
	private final LongComparator comparator;
	private long[] buf;
	private int size, unsortedCount;

	public LongPriorityQueue() {
		this(DEFAULT_INITIAL_CAPACITY, false, null);
	}

	public LongPriorityQueue(final int initialCapacity) {
		this(initialCapacity, false, null);
	}

	public LongPriorityQueue(final boolean isDescendingOrder) {
		this(DEFAULT_INITIAL_CAPACITY, isDescendingOrder, null);
	}

	public LongPriorityQueue(final int initialCapacity, final boolean isDescendingOrder) {
		this(initialCapacity, isDescendingOrder, null);
	}

	public LongPriorityQueue(final LongComparator comparator) {
		this(DEFAULT_INITIAL_CAPACITY, false, Objects.requireNonNull(comparator));
	}

	public LongPriorityQueue(final int initialCapacity, final LongComparator comparator) {
		this(initialCapacity, false, Objects.requireNonNull(comparator));
	}

	public LongPriorityQueue(final long[] values) {
		this(values, false);
	}

	public LongPriorityQueue(final long[] values, final boolean isDescendingOrder) {
		this(values.length, isDescendingOrder, null);
		addAll(values);
	}

	public LongPriorityQueue(final long[] values, final LongComparator comparator) {
		this(values.length, false, Objects.requireNonNull(comparator));
		addAll(values);
	}

	public LongPriorityQueue(final LongCollection values) {
		this(values, false);
	}

	public LongPriorityQueue(final LongCollection values, final boolean isDescendingOrder) {
		this(values.size(), isDescendingOrder, null);
		addAll(values);
	}

	public LongPriorityQueue(final LongCollection values, final LongComparator comparator) {
		this(values.size(), false, Objects.requireNonNull(comparator));
		addAll(values);
	}

	/**
	 * 比較順・要素・容量を引き継ぎ、内部配列を共有しない独立したコピーを構築する。
	 */
	public LongPriorityQueue(final LongPriorityQueue other) {
		isDescendingOrder = other.isDescendingOrder;
		comparator = other.comparator;
		buf = other.buf.clone();
		size = other.size;
		unsortedCount = other.unsortedCount;
	}

	private LongPriorityQueue(final int initialCapacity, final boolean isDescendingOrder, final LongComparator comparator) {
		this.isDescendingOrder = isDescendingOrder;
		this.comparator = comparator;
		buf = new long[max(1, initialCapacity)];
	}

	public boolean add(final long v) {
		ensureCapacity(size + 1);
		buf[size++] = v;
		unsortedCount++;
		return true;
	}

	public boolean addAll(final long[] values) {
		final int n = values.length;
		if (n == 0) return false;
		ensureCapacity(size + n);
		System.arraycopy(values, 0, buf, size, n);
		size += n;
		unsortedCount += n;
		return true;
	}

	public boolean addAll(final LongCollection values) {
		final int n = values.size();
		if (n == 0) return false;
		ensureCapacity(size + n);
		final PrimitiveIterator.OfLong it = values.iterator();
		for (int i = 0; i < n; i++) buf[size++] = it.nextLong();
		unsortedCount += n;
		return true;
	}

	public boolean addAll(final Iterable<Long> values) {
		if (values instanceof final LongCollection c) return addAll(c);
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

	public long peek() {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		return buf[0];
	}

	public long peekOrDefault(final long defaultValue) {
		return isEmpty() ? defaultValue : peek();
	}

	/**
	 * 2番目に優先される値を返す。要素数は2以上とする。
	 */
	public long peekSecond() {
		if (size < 2) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		if (size == 2) return buf[1];
		return compare(buf[1], buf[2]) <= 0 ? buf[1] : buf[2];
	}

	public long poll() {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		final long res = buf[0];
		if (--size > 0) siftDown(buf[size], 0);
		return res;
	}

	public long pollOrDefault(final long defaultValue) {
		return isEmpty() ? defaultValue : poll();
	}

	/**
	 * 最優先の値を v に置き換え、置き換え前の値を返す。poll と add を続けて行うより速い。
	 */
	public long replaceTop(final long v) {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		final long res = buf[0];
		siftDown(v, 0);
		return res;
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

	public void clear() {
		size = unsortedCount = 0;
	}

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
