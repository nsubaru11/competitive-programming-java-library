package lib.ds.priorityqueue;

import java.util.*;
import java.util.function.*;

import lib.ds.*;
import lib.util.function.*;

/**
 * 添字 {@code 0 <= i < n} に long のコストを対応付ける優先度キュー。n は構築時に固定し、添字の範囲は検査しない。
 * 比較順は昇順・降順・LongComparator から選び、同順位のコストを持つ添字の取り出し順は規定しない。
 * 各添字は「未追加」「active（キュー内）」「削除済み（poll・remove で取り出し済み）」のいずれかの状態を持ち、active な添字は高々1個のコストを持つ。
 * get 系は active な添字のコスト、getLast 系は active または削除済みの添字が最後に持ったコストを返す。clear は全添字を未追加に戻す。
 * 登録と配列からの構築はヒープ化を遅延し、peek・poll など優先順が必要な操作の直前にまとめて復元する。
 * 空の peek・poll 系と、該当する状態にない添字の get・getLast は NoSuchElementException を投げ、OrDefault 系は状態を変えずに既定値を返す。
 * 走査はヒープを復元せず、active な添字のコストを内部配列の順に返す。
 */
public final class LongIndexedPriorityQueue implements LongCollection {
	private final boolean isDescendingOrder;
	private final LongComparator comparator;
	private final int[] heap, position;
	private final long[] cost;
	private int size, unsortedCount, stamp;

	public LongIndexedPriorityQueue(final int n) {
		this(n, false, null);
	}

	public LongIndexedPriorityQueue(final int n, final boolean isDescendingOrder) {
		this(n, isDescendingOrder, null);
	}

	public LongIndexedPriorityQueue(final int n, final LongComparator comparator) {
		this(n, false, Objects.requireNonNull(comparator));
	}

	/**
	 * n = costs.length とし、costs[i] を添字 i のコストとして全添字を登録する。
	 */
	public LongIndexedPriorityQueue(final long[] costs) {
		this(costs, false);
	}

	public LongIndexedPriorityQueue(final long[] costs, final boolean isDescendingOrder) {
		this(costs.length, isDescendingOrder, null);
		System.arraycopy(costs, 0, cost, 0, costs.length);
		build();
	}

	public LongIndexedPriorityQueue(final long[] costs, final LongComparator comparator) {
		this(costs.length, false, Objects.requireNonNull(comparator));
		System.arraycopy(costs, 0, cost, 0, costs.length);
		build();
	}

	/**
	 * 比較順・状態・コストを引き継ぎ、内部配列を共有しない独立したコピーを構築する。
	 */
	public LongIndexedPriorityQueue(final LongIndexedPriorityQueue other) {
		isDescendingOrder = other.isDescendingOrder;
		comparator = other.comparator;
		heap = other.heap.clone();
		position = other.position.clone();
		cost = other.cost.clone();
		size = other.size;
		unsortedCount = other.unsortedCount;
		stamp = other.stamp;
	}

	private LongIndexedPriorityQueue(final int n, final boolean isDescendingOrder, final LongComparator comparator) {
		this.isDescendingOrder = isDescendingOrder;
		this.comparator = comparator;
		heap = new int[n];
		position = new int[n];
		cost = new long[n];
		stamp = -1;
	}

	/**
	 * active なら c へ更新して false、そうでなければ c で登録して true を返す。
	 */
	public boolean set(final int i, final long c) {
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
	 * 全添字の記録を消去し、init(i) を添字 i のコストとして全添字を登録して自身を返す。
	 */
	public LongIndexedPriorityQueue setAll(final IntToLongFunction init) {
		clear();
		for (int i = 0; i < cost.length; i++) cost[i] = init.applyAsLong(i);
		build();
		return this;
	}

	/**
	 * active でなければ c で登録して true、active なら何もせず false を返す。削除済みの添字も再登録する。
	 */
	public boolean add(final int i, final long c) {
		if (isActive(i)) return false;
		append(i, c);
		return true;
	}

	/**
	 * j 番目の組 (is[j], cs[j]) を順に add し、1件以上登録した場合に true を返す。is と cs は同じ長さとする。
	 */
	public boolean addAll(final int[] is, final long[] cs) {
		boolean changed = false;
		for (int j = 0; j < is.length; j++) {
			final int i = is[j];
			if (isActive(i)) continue;
			append(i, cs[j]);
			changed = true;
		}
		return changed;
	}

	/**
	 * 未追加なら登録し、active で c が現在のコストより優先されるなら更新する。登録・更新した場合に true を返す。
	 * 削除済みの添字は再登録せず false を返す。
	 */
	public boolean relax(final int i, final long c) {
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

	public long peek() {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		return cost[heap[0]];
	}

	public long peekOrDefault(final long defaultValue) {
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
	public long peekSecond() {
		if (size < 2) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		if (size == 2) return cost[heap[1]];
		return compare(cost[heap[1]], cost[heap[2]]) <= 0 ? cost[heap[1]] : cost[heap[2]];
	}

	public long poll() {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		final int i = heap[0];
		final long c = cost[i];
		position[i] = stamp;
		if (--size > 0) siftDown(heap[size], 0, size);
		return c;
	}

	public long pollOrDefault(final long defaultValue) {
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
	 * active なら削除済みにして true、そうでなければ false を返す。未整列領域の添字はヒープを復元せずに外す。
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

	public long get(final int i) {
		if (!isActive(i)) throw new NoSuchElementException();
		return cost[i];
	}

	public long getOrDefault(final int i, final long defaultValue) {
		return isActive(i) ? cost[i] : defaultValue;
	}

	public long getLast(final int i) {
		if (isUnseen(i)) throw new NoSuchElementException();
		return cost[i];
	}

	public long getLastOrDefault(final int i, final long defaultValue) {
		return isUnseen(i) ? defaultValue : cost[i];
	}

	/**
	 * active な添字の数を返す。
	 */
	public int size() {
		return size;
	}

	/**
	 * 使用できる添字の数 n を返す。
	 */
	public int indexCount() {
		return cost.length;
	}

	public void clear() {
		size = unsortedCount = 0;
		stamp--;
	}

	/**
	 * 添字が active か判定する。コストを探索する contains とは異なる。
	 */
	public boolean containsIndex(final int i) {
		return isActive(i);
	}

	/**
	 * 添字が active または削除済みか判定する。
	 */
	public boolean hasCost(final int i) {
		return !isUnseen(i);
	}

	public PrimitiveIterator.OfLong iterator() {
		return new PrimitiveIterator.OfLong() {
			private int i;

			public boolean hasNext() {
				return i < size;
			}

			public long nextLong() {
				if (!hasNext()) throw new NoSuchElementException();
				return cost[heap[i++]];
			}
		};
	}

	private int compare(final long a, final long b) {
		if (comparator != null) return comparator.compare(a, b);
		return isDescendingOrder ? Long.compare(b, a) : Long.compare(a, b);
	}

	private void build() {
		size = unsortedCount = cost.length;
		for (int i = 0; i < size; i++) heap[i] = position[i] = i;
	}

	private void append(final int i, final long c) {
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
		final long c = cost[i];
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
		final long c = cost[i];
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
