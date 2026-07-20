package lib.ds.priorityqueue;

import java.util.*;
import java.util.function.*;

/**
 * genericコストを扱うインデックス付き優先度キューです。
 * Comparatorを省略した場合は自然順序を使用し、costが相互にComparableでなければ比較時に{@link ClassCastException}となります。
 *
 * @param <T> コスト型
 */
@SuppressWarnings({"unchecked", "unused"})
public final class IndexedPriorityQueue<T> implements Iterable<T> {
	private final Comparator<? super T> comparator;
	private final int[] heap, position;
	private final T[] cost;
	private int size, unsortedCount, stamp;

	/**
	 * 指定したindex数で自然順序のキューを構築します。
	 */
	public IndexedPriorityQueue(final int n) {
		this(n, false);
	}

	/**
	 * 指定したindex数で自然順序または逆順のキューを構築します。
	 */
	public IndexedPriorityQueue(final int n, final boolean isDescendingOrder) {
		this(n, naturalComparator(isDescendingOrder));
	}

	/**
	 * 指定したindex数とComparatorでキューを構築します。
	 */
	public IndexedPriorityQueue(final int n, final Comparator<? super T> comparator) {
		this.comparator = Objects.requireNonNull(comparator);
		cost = (T[]) new Object[n];
		heap = new int[n];
		position = new int[n];
		stamp = -1;
	}

	/**
	 * costs[i]をindex iの自然順序コストとして登録したキューを構築します。
	 */
	public IndexedPriorityQueue(final T[] costs) {
		this(costs, false);
	}

	/**
	 * costs[i]をindex iの自然順序または逆順コストとして登録したキューを構築します。
	 */
	public IndexedPriorityQueue(final T[] costs, final boolean isDescendingOrder) {
		this(costs, naturalComparator(isDescendingOrder));
	}

	/**
	 * costs[i]をindex iのコストとしてComparator順で登録したキューを構築します。
	 */
	public IndexedPriorityQueue(final T[] costs, final Comparator<? super T> comparator) {
		this(costs.length, comparator);
		System.arraycopy(costs, 0, cost, 0, costs.length);
		size = unsortedCount = costs.length;
		for (int i = 0; i < size; i++) heap[i] = position[i] = i;
	}

	/**
	 * init(i)をindex iの自然順序コストとして持つキューを返します。
	 */
	public static <T extends Comparable<? super T>> IndexedPriorityQueue<T> generate(final int n, final IntFunction<? extends T> init) {
		final IndexedPriorityQueue<T> q = new IndexedPriorityQueue<>(n, Comparator.naturalOrder());
		q.setAll(init);
		return q;
	}

	/**
	 * init(i)をindex iの自然順序または逆順コストとして持つキューを返します。
	 */
	public static <T extends Comparable<? super T>> IndexedPriorityQueue<T> generate(
			final int n, final IntFunction<? extends T> init, final boolean isDescendingOrder) {
		final IndexedPriorityQueue<T> q = new IndexedPriorityQueue<>(n, naturalComparator(isDescendingOrder));
		q.setAll(init);
		return q;
	}

	/**
	 * init(i)をindex iのComparator順コストとして持つキューを返します。
	 */
	public static <T> IndexedPriorityQueue<T> generate(
			final int n, final IntFunction<? extends T> init, final Comparator<? super T> comparator) {
		final IndexedPriorityQueue<T> q = new IndexedPriorityQueue<>(n, comparator);
		q.setAll(init);
		return q;
	}

	private static <T> Comparator<? super T> naturalComparator(final boolean isDescendingOrder) {
		return isDescendingOrder ? (Comparator<? super T>) Comparator.reverseOrder() : (Comparator<? super T>) Comparator.naturalOrder();
	}

	/**
	 * activeなら更新し、そうでなければ登録します。
	 */
	public void set(final int i, final T c) {
		if (!isActive(i)) {
			cost[i] = c;
			heap[size] = i;
			position[i] = size++;
			unsortedCount++;
			return;
		}
		final T old = cost[i];
		final int order = comparator.compare(c, old);
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
	public void setAll(final IntFunction<? extends T> init) {
		clear();
		size = cost.length;
		for (int i = 0; i < size; i++) {
			cost[i] = init.apply(i);
			heap[i] = position[i] = i;
		}
		unsortedCount = size;
	}

	/**
	 * inactiveなindexを追加します。
	 */
	public boolean add(final int i, final T c) {
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
	public boolean addAll(final int[] is, final T[] cs) {
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
	public boolean relax(final int i, final T c) {
		if (isRemoved(i)) return false;
		if (!isActive(i)) return add(i, c);
		if (comparator.compare(c, cost[i]) >= 0) return false;
		final int p = position[i];
		cost[i] = c;
		if (p < size - unsortedCount) siftUp(i, p);
		return true;
	}

	/**
	 * 最優先コストを返します。
	 */
	public T peek() {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		return cost[heap[0]];
	}

	/**
	 * 空ならdefaultValue、そうでなければ最優先コストを返します。
	 */
	public T peekOrDefault(final T defaultValue) {
		return isEmpty() ? defaultValue : peek();
	}

	/**
	 * 最優先indexを返します。
	 */
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

	/**
	 * 2番目に優先されるコストを返します。
	 */
	public T peekSecond() {
		if (size < 2) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		if (size == 2) return cost[heap[1]];
		return comparator.compare(cost[heap[1]], cost[heap[2]]) <= 0 ? cost[heap[1]] : cost[heap[2]];
	}

	/**
	 * 最優先要素を削除してコストを返します。
	 */
	public T poll() {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		final int i = heap[0];
		final T c = cost[i];
		position[i] = stamp;
		if (--size > 0) siftDown(heap[size], 0);
		return c;
	}

	/**
	 * 空ならdefaultValue、そうでなければ最優先コストを削除して返します。
	 */
	public T pollOrDefault(final T defaultValue) {
		return isEmpty() ? defaultValue : poll();
	}

	/**
	 * 最優先要素を削除してindexを返します。
	 */
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
			if (comparator.compare(cost[j], cost[i]) < 0) siftUp(j, p);
			else siftDown(j, p);
		}
		return true;
	}

	/**
	 * activeなindexのコストを返します。
	 */
	public T get(final int i) {
		if (!isActive(i)) throw new NoSuchElementException();
		return cost[i];
	}

	/**
	 * activeでなければdefaultValueを返します。
	 */
	public T getOrDefault(final int i, final T defaultValue) {
		return isActive(i) ? cost[i] : defaultValue;
	}

	/**
	 * 現在世代で最後に記録したコストを返します。
	 */
	public T getLast(final int i) {
		if (isUnseen(i)) throw new NoSuchElementException();
		return cost[i];
	}

	/**
	 * 現在世代で未追加ならdefaultValueを返します。
	 */
	public T getLastOrDefault(final int i, final T defaultValue) {
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

	/**
	 * キューと現在世代のコスト記録をO(1)で論理的に削除します。
	 */
	public void clear() {
		size = unsortedCount = 0;
		stamp--;
	}

	/**
	 * キューが空か判定します。
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * indexがactiveか判定します。
	 */
	public boolean containsIndex(final int i) {
		return isActive(i);
	}

	/**
	 * indexに現在世代のコスト記録があるか判定します。
	 */
	public boolean hasCost(final int i) {
		return !isUnseen(i);
	}

	/**
	 * activeなコストを内部順で走査するIteratorを返します。
	 */
	public Iterator<T> iterator() {
		return new Iterator<>() {
			private int i;

			public boolean hasNext() {
				return i < size;
			}

			public T next() {
				if (!hasNext()) throw new NoSuchElementException();
				return cost[heap[i++]];
			}
		};
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
		final T c = cost[i];
		while (p > 0) {
			final int q = (p - 1) >> 1;
			final int j = heap[q];
			if (comparator.compare(c, cost[j]) >= 0) break;
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
		final T c = cost[i];
		final int half = n >> 1;
		while (p < half) {
			int q = (p << 1) + 1;
			if (q + 1 < n && comparator.compare(cost[heap[q]], cost[heap[q + 1]]) > 0) q++;
			final int j = heap[q];
			if (comparator.compare(c, cost[j]) <= 0) break;
			heap[p] = j;
			position[j] = p;
			p = q;
		}
		heap[p] = i;
		position[i] = p;
	}
}
