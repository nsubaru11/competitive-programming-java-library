package lib.ds.priorityqueue;

import static java.lang.Math.*;

import java.util.*;
import java.util.function.*;

import lib.ds.*;

/**
 * long型のコストを扱うインデックス付き優先度キューです。
 * <p>
 * 各インデックスは現在のキュー内に高々1個だけ存在します。取り出しまたは削除された
 * インデックスの最後のコストは、次に {@link #clear()} されるまで保持されます。
 */
public final class LongIndexedPriorityQueue implements LongCollection {
	private final boolean isDescendingOrder;
	private final long[] cost;
	private final int[] heap, position;
	private int size, unsortedCount, stamp;

	/**
	 * 最小値優先のキューを構築します。
	 *
	 * @param n 使用できるインデックス数
	 */
	public LongIndexedPriorityQueue(final int n) {
		this(n, false);
	}

	/**
	 * キューを構築します。
	 *
	 * @param n                 使用できるインデックス数
	 * @param isDescendingOrder 最大値優先ならtrue、最小値優先ならfalse
	 */
	public LongIndexedPriorityQueue(final int n, final boolean isDescendingOrder) {
		this.isDescendingOrder = isDescendingOrder;
		cost = new long[n];
		heap = new int[n];
		position = new int[n];
		stamp = -1;
	}

	/**
	 * 指定したインデックスのコストを設定します。
	 * <p>
	 * activeならコストを更新し、未追加または削除済みならキューへ登録します。
	 *
	 * @param i インデックス
	 * @param c コスト
	 */
	public void set(final int i, long c) {
		if (isDescendingOrder) c = -c;
		if (!isActive(i)) {
			cost[i] = c;
			heap[size] = i;
			position[i] = size++;
			unsortedCount++;
			return;
		}
		final long old = cost[i];
		if (c == old) return;
		final int p = position[i];
		final int sortedSize = size - unsortedCount;
		cost[i] = c;
		if (p >= sortedSize) return;
		if (c < old) siftUp(i, p);
		else siftDown(i, p, sortedSize);
	}

	/**
	 * 全indexを{@code init(i)}のコストで登録します。
	 *
	 * @param init indexからコストを生成する関数
	 */
	public void setAll(final IntToLongFunction init) {
		clear();
		size = cost.length;
		for (int i = 0; i < size; i++) {
			final long c = init.applyAsLong(i);
			cost[i] = isDescendingOrder ? -c : c;
			heap[i] = i;
			position[i] = i;
		}
		heapify();
	}

	/**
	 * 現在世代で未追加のインデックスを追加します。
	 *
	 * @param i インデックス
	 * @param c コスト
	 */
	public void push(final int i, final long c) {
		if (!isUnseen(i)) throw new IllegalArgumentException();
		cost[i] = isDescendingOrder ? -c : c;
		heap[size] = i;
		position[i] = size++;
		unsortedCount++;
	}

	/**
	 * 現在世代で未追加のインデックスをまとめて追加します。
	 *
	 * @param is インデックス配列
	 * @param cs コスト配列
	 */
	public void pushAll(final int[] is, final long[] cs) {
		final int n = is.length;
		final int k = isDescendingOrder ? -1 : 1;
		for (int j = 0; j < n; j++) {
			final int i = is[j];
			if (!isUnseen(i)) throw new IllegalArgumentException();
			cost[i] = cs[j] * k;
			heap[size] = i;
			position[i] = size++;
		}
		unsortedCount += n;
	}

	/**
	 * 指定したインデックスのコストを、より優先される場合だけ更新します。
	 * <p>
	 * 未追加なら登録し、削除済みなら何も行いません。
	 *
	 * @param i インデックス
	 * @param c コスト
	 * @return 登録または更新した場合はtrue
	 */
	public boolean relax(final int i, long c) {
		if (isRemoved(i)) return false;
		if (!isActive(i)) {
			push(i, c);
			return true;
		}
		if (isDescendingOrder) c = -c;
		if (cost[i] <= c) return false;
		final int p = position[i];
		cost[i] = c;
		if (p < size - unsortedCount) siftUp(i, p);
		return true;
	}

	/**
	 * 最優先コストを返します。
	 *
	 * @return 最優先コスト
	 */
	public long peek() {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		final long c = cost[heap[0]];
		return isDescendingOrder ? -c : c;
	}

	/**
	 * 最優先インデックスを返します。
	 *
	 * @return 最優先インデックス
	 */
	public int peekIndex() {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		return heap[0];
	}

	/**
	 * 2番目に優先されるコストを返します。
	 *
	 * @return 2番目に優先されるコスト
	 */
	public long peekSecond() {
		if (size < 2) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		if (size == 2) return isDescendingOrder ? -cost[heap[1]] : cost[heap[1]];
		final long c = min(cost[heap[1]], cost[heap[2]]);
		return isDescendingOrder ? -c : c;
	}

	/**
	 * 最優先要素を削除して、そのコストを返します。
	 *
	 * @return 削除した要素のコスト
	 */
	public long poll() {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		final int i = heap[0];
		final long c = cost[i];
		position[i] = stamp;
		if (--size > 0) siftDown(heap[size], 0);
		return isDescendingOrder ? -c : c;
	}

	/**
	 * 最優先要素を削除して、そのインデックスを返します。
	 *
	 * @return 削除した要素のインデックス
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
	 * 指定したインデックスを削除します。
	 *
	 * @param i インデックス
	 */
	public void remove(final int i) {
		if (!isActive(i)) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		final int p = position[i];
		position[i] = stamp;
		if (--size == p) return;
		final int j = heap[size];
		if (cost[j] < cost[i]) siftUp(j, p);
		else siftDown(j, p);
	}

	/**
	 * activeなインデックスのコストを返します。
	 *
	 * @param i インデックス
	 * @return コスト
	 */
	public long get(final int i) {
		if (!isActive(i)) throw new NoSuchElementException();
		final long c = cost[i];
		return isDescendingOrder ? -c : c;
	}

	/**
	 * activeなインデックスのコストを返します。
	 *
	 * @param i            インデックス
	 * @param defaultValue activeでない場合の値
	 * @return コスト、またはdefaultValue
	 */
	public long getOrDefault(final int i, final long defaultValue) {
		if (!isActive(i)) return defaultValue;
		final long c = cost[i];
		return isDescendingOrder ? -c : c;
	}

	/**
	 * 現在世代で最後に記録したコストを返します。
	 * <p>
	 * 削除済みのインデックスについても最後のコストを返します。
	 *
	 * @param i インデックス
	 * @return 最後に記録したコスト
	 */
	public long getLast(final int i) {
		if (isUnseen(i)) throw new NoSuchElementException();
		final long c = cost[i];
		return isDescendingOrder ? -c : c;
	}

	/**
	 * 現在世代で最後に記録したコストを返します。
	 *
	 * @param i            インデックス
	 * @param defaultValue 現在世代で未追加の場合の値
	 * @return 最後に記録したコスト、またはdefaultValue
	 */
	public long getLastOrDefault(final int i, final long defaultValue) {
		if (isUnseen(i)) return defaultValue;
		final long c = cost[i];
		return isDescendingOrder ? -c : c;
	}

	/**
	 * 現在の要素数を返します。
	 *
	 * @return 要素数
	 */
	public int size() {
		return size;
	}

	/**
	 * キューと現在世代のコスト記録を論理的に消去します。
	 */
	public void clear() {
		size = 0;
		unsortedCount = 0;
		stamp--;
	}

	/**
	 * 指定したインデックスがactiveか判定します。
	 *
	 * @param i インデックス
	 * @return active なら true
	 */
	public boolean containsIndex(final int i) {
		return isActive(i);
	}

	/**
	 * 指定したインデックスに現在世代のコスト記録があるか判定します。
	 *
	 * @param i インデックス
	 * @return active または削除済みなら true
	 */
	public boolean hasCost(final int i) {
		return !isUnseen(i);
	}

	/**
	 * activeな要素のコストを内部ヒープ順に走査します。
	 *
	 * @return コストの iterator
	 */
	public PrimitiveIterator.OfLong iterator() {
		if (unsortedCount > 0) ensureHeapProperty();
		return new PrimitiveIterator.OfLong() {
			private int i;

			public boolean hasNext() {
				return i < size;
			}

			public long nextLong() {
				if (!hasNext()) throw new NoSuchElementException();
				final long c = cost[heap[i++]];
				return isDescendingOrder ? -c : c;
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
		if (heapifyCost < incrementalCost) {
			heapify();
		} else {
			for (int p = size - unsortedCount; p < size; p++) siftUp(heap[p], p);
		}
		unsortedCount = 0;
	}

	private int getIncrementalCostStrict() {
		int c = 0;
		final int sortedSize = size - unsortedCount;
		for (int i = 1; i <= unsortedCount; i++) {
			final int currentSize = sortedSize + i;
			c += 31 - Integer.numberOfLeadingZeros(currentSize);
		}
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
		final long c = cost[i];
		while (p > 0) {
			final int q = (p - 1) >> 1;
			final int j = heap[q];
			if (c >= cost[j]) break;
			heap[p] = j;
			position[j] = p;
			p = q;
		}
		heap[p] = i;
		position[i] = p;
	}

	private void siftDown(final int i, int p) {
		siftDown(i, p, size);
	}

	private void siftDown(final int i, int p, final int n) {
		final long c = cost[i];
		final int half = n >> 1;
		while (p < half) {
			int q = (p << 1) + 1;
			if (q + 1 < n && cost[heap[q]] > cost[heap[q + 1]]) q++;
			final int j = heap[q];
			if (c <= cost[j]) break;
			heap[p] = j;
			position[j] = p;
			p = q;
		}
		heap[p] = i;
		position[i] = p;
	}
}
