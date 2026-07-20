package lib.ds.priorityqueue;

import java.util.*;
import java.util.function.*;

import lib.ds.*;
import lib.util.function.*;

/**
 * int型コストを扱うインデックス付き優先度キューです。
 */
public final class IntIndexedPriorityQueue implements IntCollection {
	private final boolean isDescendingOrder;
	private final IntComparator comparator;
	private final int[] heap, position, cost;
	private int size, unsortedCount, stamp;

	/**
	 * 指定したindex数で昇順のキューを構築します。
	 */
	public IntIndexedPriorityQueue(final int n) {
		this(n, false, null);
	}

	/**
	 * 指定したindex数で昇順または降順のキューを構築します。
	 */
	public IntIndexedPriorityQueue(final int n, final boolean isDescendingOrder) {
		this(n, isDescendingOrder, null);
	}

	/**
	 * 指定したindex数とComparatorでキューを構築します。
	 */
	public IntIndexedPriorityQueue(final int n, final IntComparator comparator) {
		this(n, false, Objects.requireNonNull(comparator));
	}

	private IntIndexedPriorityQueue(final int n, final boolean isDescendingOrder, final IntComparator comparator) {
		this.isDescendingOrder = isDescendingOrder;
		this.comparator = comparator;
		cost = new int[n];
		heap = new int[n];
		position = new int[n];
		stamp = -1;
	}

	/**
	 * costs[i]をindex iの昇順コストとして登録したキューを構築します。
	 */
	public IntIndexedPriorityQueue(final int[] costs) {
		this(costs, false);
	}

	/**
	 * costs[i]をindex iの昇順または降順コストとして登録したキューを構築します。
	 */
	public IntIndexedPriorityQueue(final int[] costs, final boolean isDescendingOrder) {
		this(costs.length, isDescendingOrder, null);
		initialize(costs);
	}

	/**
	 * costs[i]をindex iのコストとしてComparator順で登録したキューを構築します。
	 */
	public IntIndexedPriorityQueue(final int[] costs, final IntComparator comparator) {
		this(costs.length, false, Objects.requireNonNull(comparator));
		initialize(costs);
	}

	/**
	 * init(i)をindex iのコストとして持つ昇順キューを返します。
	 */
	public static IntIndexedPriorityQueue generate(final int n, final IntUnaryOperator init) {
		final IntIndexedPriorityQueue q = new IntIndexedPriorityQueue(n);
		q.setAll(init);
		return q;
	}

	/**
	 * init(i)をindex iの昇順または降順コストとして持つキューを返します。
	 */
	public static IntIndexedPriorityQueue generate(final int n, final IntUnaryOperator init, final boolean isDescendingOrder) {
		final IntIndexedPriorityQueue q = new IntIndexedPriorityQueue(n, isDescendingOrder);
		q.setAll(init);
		return q;
	}

	/**
	 * init(i)をindex iのComparator順コストとして持つキューを返します。
	 */
	public static IntIndexedPriorityQueue generate(final int n, final IntUnaryOperator init, final IntComparator comparator) {
		final IntIndexedPriorityQueue q = new IntIndexedPriorityQueue(n, comparator);
		q.setAll(init);
		return q;
	}

	/**
	 * activeなら更新し、そうでなければ登録します。
	 */
	public void set(final int i, final int c) {
		if (!isActive(i)) {
			cost[i] = c;
			heap[size] = i;
			position[i] = size++;
			unsortedCount++;
			return;
		}
		final int old = cost[i];
		final int order = compare(c, old);
		cost[i] = c;
		if (order == 0) return;
		final int p = position[i];
		final int sortedSize = size - unsortedCount;
		if (p >= sortedSize) return;
		if (order < 0) siftUp(i, p);
		else siftDown(i, p, sortedSize);
	}

	/**
	 * 全indexをinit(i)のコストで登録します。
	 */
	public void setAll(final IntUnaryOperator init) {
		clear();
		size = cost.length;
		for (int i = 0; i < size; i++) {
			cost[i] = init.applyAsInt(i);
			heap[i] = position[i] = i;
		}
		unsortedCount = size;
	}

	/**
	 * inactiveなindexを追加します。
	 */
	public boolean add(final int i, final int c) {
		if (isActive(i)) return false;
		cost[i] = c;
		heap[size] = i;
		position[i] = size++;
		unsortedCount++;
		return true;
	}

	/**
	 * inactiveなindexをまとめて追加します。
	 */
	public boolean addAll(final int[] is, final int[] cs) {
		boolean changed = false;
		for (int j = 0; j < is.length; j++) {
			final int i = is[j];
			if (isActive(i)) continue;
			cost[i] = cs[j];
			heap[size] = i;
			position[i] = size++;
			unsortedCount++;
			changed = true;
		}
		return changed;
	}

	/**
	 * コストがより優先される場合だけ登録または更新します。
	 */
	public boolean relax(final int i, final int c) {
		if (isRemoved(i)) return false;
		if (!isActive(i)) return add(i, c);
		if (compare(c, cost[i]) >= 0) return false;
		final int p = position[i];
		cost[i] = c;
		if (p < size - unsortedCount) siftUp(i, p);
		return true;
	}

	/**
	 * 最優先コストを返します。
	 */
	public int peek() {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		return cost[heap[0]];
	}

	/**
	 * 空ならdefaultValue、そうでなければ最優先コストを返します。
	 */
	public int peekOrDefault(final int defaultValue) {
		return isEmpty() ? defaultValue : peek();
	}

	/** 最優先indexを返します。 */
	public int peekIndex() {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		return heap[0];
	}

	/**
	 * 空ならdefaultIndex、そうでなければ最優先indexを返します。
	 */
	public int peekIndexOrDefault(final int defaultIndex) {
		return isEmpty() ? defaultIndex : peekIndex();
	}

	/** 2番目に優先されるコストを返します。 */
	public int peekSecond() {
		if (size < 2) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		if (size == 2) return cost[heap[1]];
		return compare(cost[heap[1]], cost[heap[2]]) <= 0 ? cost[heap[1]] : cost[heap[2]];
	}

	/** 最優先要素を削除してコストを返します。 */
	public int poll() {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		final int i = heap[0];
		final int c = cost[i];
		position[i] = stamp;
		if (--size > 0) siftDown(heap[size], 0);
		return c;
	}

	/**
	 * 空ならdefaultValue、そうでなければ最優先コストを削除して返します。
	 */
	public int pollOrDefault(final int defaultValue) {
		return isEmpty() ? defaultValue : poll();
	}

	/** 最優先要素を削除してindexを返します。 */
	public int pollIndex() {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		final int i = heap[0];
		position[i] = stamp;
		if (--size > 0) siftDown(heap[size], 0);
		return i;
	}

	/**
	 * 空ならdefaultIndex、そうでなければ最優先indexを削除して返します。
	 */
	public int pollIndexOrDefault(final int defaultIndex) {
		return isEmpty() ? defaultIndex : pollIndex();
	}

	/**
	 * activeなindexを削除します。
	 */
	public boolean remove(final int i) {
		if (!isActive(i)) return false;
		if (unsortedCount > 0) ensureHeapProperty();
		final int p = position[i];
		position[i] = stamp;
		if (--size != p) {
			final int j = heap[size];
			if (compare(cost[j], cost[i]) < 0) siftUp(j, p);
			else siftDown(j, p);
		}
		return true;
	}

	/** activeなindexのコストを返します。 */
	public int get(final int i) {
		if (!isActive(i)) throw new NoSuchElementException();
		return cost[i];
	}

	/**
	 * activeでなければdefaultValueを返します。
	 */
	public int getOrDefault(final int i, final int defaultValue) {
		return isActive(i) ? cost[i] : defaultValue;
	}

	/** 現在世代で最後に記録したコストを返します。 */
	public int getLast(final int i) {
		if (isUnseen(i)) throw new NoSuchElementException();
		return cost[i];
	}

	/** 現在世代で未追加ならdefaultValueを返します。 */
	public int getLastOrDefault(final int i, final int defaultValue) {
		return isUnseen(i) ? defaultValue : cost[i];
	}

	/**
	 * 現在の要素数を返します。
	 */
	public int size() {
		return size;
	}

	/**
	 * 使用可能なindex数を返します。
	 */
	public int indexCount() {
		return cost.length;
	}

	/** キューと現在世代のコスト記録を論理的に削除します。 */
	public void clear() {
		size = unsortedCount = 0;
		stamp--;
	}

	/**
	 * indexがactiveか判定します。
	 */
	public boolean containsIndex(final int i) {
		return isActive(i);
	}

	/** indexに現在世代のコスト記録があるか判定します。 */
	public boolean hasCost(final int i) {
		return !isUnseen(i);
	}

	/** activeなコストを内部順で走査するIteratorを返します。 */
	public PrimitiveIterator.OfInt iterator() {
		return new PrimitiveIterator.OfInt() {
			private int i;

			public boolean hasNext() {
				return i < size;
			}

			public int nextInt() {
				if (!hasNext()) throw new NoSuchElementException();
				return cost[heap[i++]];
			}
		};
	}

	private void initialize(final int[] costs) {
		System.arraycopy(costs, 0, cost, 0, costs.length);
		size = unsortedCount = costs.length;
		for (int i = 0; i < size; i++) heap[i] = position[i] = i;
	}

	private int compare(final int a, final int b) {
		if (comparator != null) return comparator.compare(a, b);
		return isDescendingOrder ? Integer.compare(b, a) : Integer.compare(a, b);
	}

	private boolean isActive(final int i) {
		final int p = position[i];
		return p >= 0 && p < size && heap[p] == i;
	}

	private boolean isRemoved(final int i) {
		return position[i] == stamp;
	}

	private boolean isUnseen(final int i) {
		return !isActive(i) && !isRemoved(i);
	}

	private void ensureHeapProperty() {
		final int log2N = 31 - Integer.numberOfLeadingZeros(size);
		final int heapifyCost = size * 2 - 2 * log2N;
		final int incrementalCost = unsortedCount <= 100 ? getIncrementalCostStrict() : getIncrementalCostApprox();
		if (heapifyCost < incrementalCost) heapify();
		else for (int p = size - unsortedCount; p < size; p++) siftUp(heap[p], p);
		unsortedCount = 0;
	}

	private int getIncrementalCostStrict() {
		int c = 0;
		final int sortedSize = size - unsortedCount;
		for (int i = 1; i <= unsortedCount; i++) c += 31 - Integer.numberOfLeadingZeros(sortedSize + i);
		return c;
	}

	private int getIncrementalCostApprox() {
		final int averageSize = size - unsortedCount + (unsortedCount >> 1);
		if (averageSize == 0) return 0;
		return unsortedCount * (31 - Integer.numberOfLeadingZeros(averageSize));
	}

	private void heapify() {
		for (int p = (size >> 1) - 1; p >= 0; p--) siftDown(heap[p], p);
	}

	private void siftUp(final int i, int p) {
		final int c = cost[i];
		while (p > 0) {
			final int q = (p - 1) >> 1;
			final int j = heap[q];
			if (compare(c, cost[j]) >= 0) break;
			heap[p] = j;
			position[j] = p;
			p = q;
		}
		heap[p] = i;
		position[i] = p;
	}

	private void siftDown(final int i, final int p) {
		siftDown(i, p, size);
	}

	private void siftDown(final int i, int p, final int n) {
		final int c = cost[i];
		final int half = n >> 1;
		while (p < half) {
			int q = (p << 1) + 1;
			if (q + 1 < n && compare(cost[heap[q]], cost[heap[q + 1]]) > 0) q++;
			final int j = heap[q];
			if (compare(c, cost[j]) <= 0) break;
			heap[p] = j;
			position[j] = p;
			p = q;
		}
		heap[p] = i;
		position[i] = p;
	}
}
