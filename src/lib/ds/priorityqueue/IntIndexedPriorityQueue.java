package lib.ds.priorityqueue;

import java.util.*;
import java.util.function.*;

import lib.ds.*;
import lib.util.function.*;

/**
 * 添字 {@code 0 <= i < n} に int のコストを対応付ける優先度キュー。Dijkstra 法などで、添字ごとのコストを更新しながら優先されるものから取り出す用途に使う。
 * n は構築時に決め、添字の範囲は検査しない。比較順は昇順・降順・IntComparator から選ぶ。同順位のコストを持つ添字の取り出し順は規定しない。
 * 各添字は「未追加」「キュー内」「取り出し済み」のいずれかの状態を持つ。poll 系と remove で取り出した添字は取り出し済みになり、clear で全添字が未追加に戻る。
 * 空の peek・poll 系は NoSuchElementException を投げ、OrDefault 系は指定した既定値を返す。走査と toString はキュー内のコストを内部配列の順に返す。
 */
public final class IntIndexedPriorityQueue implements IntCollection {
	public final int indexCount;
	private final boolean isDescendingOrder;
	private final IntComparator comparator;
	private final int[] heap, position, cost;
	private int size, unsortedCount, stamp;

	public IntIndexedPriorityQueue(final int n) {
		this(n, false, null);
	}

	public IntIndexedPriorityQueue(final int n, final boolean isDescendingOrder) {
		this(n, isDescendingOrder, null);
	}

	public IntIndexedPriorityQueue(final int n, final IntComparator comparator) {
		this(n, false, Objects.requireNonNull(comparator));
	}

	/**
	 * costs[i] を添字 i のコストとして、全添字をキューに入れる。
	 */
	public IntIndexedPriorityQueue(final int[] costs) {
		this(costs, false);
	}

	public IntIndexedPriorityQueue(final int[] costs, final boolean isDescendingOrder) {
		this(costs.length, isDescendingOrder, null);
		System.arraycopy(costs, 0, cost, 0, costs.length);
		build();
	}

	public IntIndexedPriorityQueue(final int[] costs, final IntComparator comparator) {
		this(costs.length, false, Objects.requireNonNull(comparator));
		System.arraycopy(costs, 0, cost, 0, costs.length);
		build();
	}

	/**
	 * iterator の j 番目の値を添字 j のコストとして、全添字をキューに入れる。
	 */
	public IntIndexedPriorityQueue(final IntCollection costs) {
		this(costs, false);
	}

	public IntIndexedPriorityQueue(final IntCollection costs, final boolean isDescendingOrder) {
		this(costs.size(), isDescendingOrder, null);
		copyCosts(costs);
	}

	public IntIndexedPriorityQueue(final IntCollection costs, final IntComparator comparator) {
		this(costs.size(), false, Objects.requireNonNull(comparator));
		copyCosts(costs);
	}

	public IntIndexedPriorityQueue(final IntIndexedPriorityQueue other) {
		isDescendingOrder = other.isDescendingOrder;
		comparator = other.comparator;
		indexCount = other.indexCount;
		heap = other.heap.clone();
		position = other.position.clone();
		cost = other.cost.clone();
		size = other.size;
		unsortedCount = other.unsortedCount;
		stamp = other.stamp;
	}

	private IntIndexedPriorityQueue(final int n, final boolean isDescendingOrder, final IntComparator comparator) {
		this.isDescendingOrder = isDescendingOrder;
		this.comparator = comparator;
		indexCount = n;
		heap = new int[n];
		position = new int[n];
		cost = new int[n];
		stamp = -1;
	}

	/**
	 * キュー内にある添字の数を返す。
	 */
	public int size() {
		return size;
	}

	/**
	 * 使用できる添字の数 n を返す。
	 */
	public int indexCount() {
		return indexCount;
	}

	/**
	 * 添字 i がキュー内にあるか判定する。コストを探す contains とは異なる。
	 */
	public boolean containsIndex(final int i) {
		return isActive(i);
	}

	/**
	 * 添字 i がキュー内にあるか、取り出し済みか判定する。true なら getCost でコストを取得できる。
	 */
	public boolean hasCost(final int i) {
		return !isUnseen(i);
	}

	/**
	 * キュー内にある添字 i のコストを返す。キュー内になければ NoSuchElementException を投げる。
	 */
	public int get(final int i) {
		if (!isActive(i)) throw new NoSuchElementException();
		return cost[i];
	}

	public int getOrDefault(final int i, final int defaultValue) {
		return isActive(i) ? cost[i] : defaultValue;
	}

	/**
	 * 添字 i が最後に持っていたコストを返す。取り出し済みの添字にも使え、Dijkstra 法の確定距離の取得に使える。
	 * 未追加の添字では NoSuchElementException を投げる。
	 */
	public int getCost(final int i) {
		if (isUnseen(i)) throw new NoSuchElementException();
		return cost[i];
	}

	public int getCostOrDefault(final int i, final int defaultValue) {
		return isUnseen(i) ? defaultValue : cost[i];
	}

	public int peek() {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		return cost[heap[0]];
	}

	public int peekOrDefault(final int defaultValue) {
		return isEmpty() ? defaultValue : peek();
	}

	public int peekIndex() {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		return heap[0];
	}

	public int peekIndexOrDefault(final int defaultIndex) {
		return isEmpty() ? defaultIndex : peekIndex();
	}

	/**
	 * 2番目に優先されるコストを返す。要素数は2以上とする。
	 */
	public int peekSecond() {
		if (size < 2) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		if (size == 2) return cost[heap[1]];
		return compare(cost[heap[1]], cost[heap[2]]) <= 0 ? cost[heap[1]] : cost[heap[2]];
	}

	/**
	 * 添字 i のコストを c にする。キュー内になければ c でキューに入れる。キューに入れた場合に true を返す。
	 */
	public boolean set(final int i, final int c) {
		if (!isActive(i)) {
			append(i, c);
			return true;
		}
		final int order = compare(c, cost[i]);
		cost[i] = c;
		if (order == 0) return false;
		final int p = position[i];
		final int sortedSize = size - unsortedCount;
		if (p >= sortedSize) return false;
		if (order < 0) siftUp(i, p);
		else siftDown(i, p, sortedSize);
		return false;
	}

	/**
	 * 添字 i がキュー内になければ c でキューに入れて true を返す。キュー内なら何もせず false を返す。取り出し済みの添字も入れ直す。
	 */
	public boolean add(final int i, final int c) {
		if (isActive(i)) return false;
		append(i, c);
		return true;
	}

	/**
	 * c が添字 i の現在のコストより優先される場合だけ更新し、未追加の添字はキューに入れる。更新した場合に true を返す。
	 * 取り出し済みの添字は入れ直さずに false を返す。
	 */
	public boolean relax(final int i, final int c) {
		if (isRemoved(i)) return false;
		if (!isActive(i)) {
			append(i, c);
			return true;
		}
		if (compare(c, cost[i]) >= 0) return false;
		final int p = position[i];
		cost[i] = c;
		if (p < size - unsortedCount) siftUp(i, p);
		return true;
	}

	public int poll() {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		final int i = heap[0];
		final int c = cost[i];
		position[i] = stamp;
		if (--size > 0) siftDown(heap[size], 0, size);
		return c;
	}

	public int pollOrDefault(final int defaultValue) {
		return isEmpty() ? defaultValue : poll();
	}

	public int pollIndex() {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		final int i = heap[0];
		position[i] = stamp;
		if (--size > 0) siftDown(heap[size], 0, size);
		return i;
	}

	public int pollIndexOrDefault(final int defaultIndex) {
		return isEmpty() ? defaultIndex : pollIndex();
	}

	/**
	 * 添字 i がキュー内にあれば取り出し済みにして true、なければ false を返す。
	 */
	public boolean remove(final int i) {
		if (!isActive(i)) return false;
		if (position[i] >= size - unsortedCount) {
			removeUnsorted(i);
			return true;
		}
		if (unsortedCount > 0) ensureHeapProperty();
		final int p = position[i];
		position[i] = stamp;
		if (--size != p) {
			final int j = heap[size];
			if (compare(cost[j], cost[i]) < 0) siftUp(j, p);
			else siftDown(j, p, size);
		}
		return true;
	}

	/**
	 * 全添字の状態を消去し、{@code init(i)} を添字 i のコストとして全添字をキューに入れる。
	 */
	public IntIndexedPriorityQueue setAll(final IntUnaryOperator init) {
		clear();
		for (int i = 0; i < indexCount; i++) cost[i] = init.applyAsInt(i);
		build();
		return this;
	}

	public void clear() {
		size = unsortedCount = 0;
		stamp--;
	}

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

	public String toString() {
		if (isEmpty()) return "";
		final StringBuilder sb = new StringBuilder(12 * size - 1);
		sb.append(cost[heap[0]]);
		for (int i = 1; i < size; i++) sb.append(' ').append(cost[heap[i]]);
		return sb.toString();
	}

	private void build() {
		size = unsortedCount = indexCount;
		for (int i = 0; i < size; i++) heap[i] = position[i] = i;
	}

	private void copyCosts(final IntCollection costs) {
		final PrimitiveIterator.OfInt it = costs.iterator();
		for (int i = 0; i < indexCount; i++) cost[i] = it.nextInt();
		build();
	}

	private int compare(final int a, final int b) {
		if (comparator != null) return comparator.compare(a, b);
		return isDescendingOrder ? Integer.compare(b, a) : Integer.compare(a, b);
	}

	private void append(final int i, final int c) {
		cost[i] = c;
		heap[size] = i;
		position[i] = size++;
		unsortedCount++;
	}

	private void removeUnsorted(final int i) {
		final int p = position[i];
		position[i] = stamp;
		unsortedCount--;
		if (--size != p) {
			final int j = heap[size];
			heap[p] = j;
			position[j] = p;
		}
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
		for (int p = (size >> 1) - 1; p >= 0; p--) siftDown(heap[p], p, size);
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
