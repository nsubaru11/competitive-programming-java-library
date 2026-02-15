import java.util.*;

import static java.lang.Math.*;
import static java.util.Arrays.*;

/**
 * グラフアルゴリズム特化高性能インデックス付き優先度キュー
 * <p>
 * 遅延ヒープ構築により、複数の要素追加後に一度だけヒープ化を行うことで高速化を実現。
 * インデックスベースの操作により、特定ノードへの O(1) アクセスが可能。
 * グラフの最短経路問題（Dijkstra法など）での使用に最適化されている。
 */
@SuppressWarnings("unused")
public final class IndexedPriorityQueue implements Iterable<Long> {
	// -------------- フィールド --------------
	private final boolean isDescendingOrder;
	private final long[] cost;
	private final int[] heap, position;
	private int size, unsortedCount;

	// -------------- コンストラクタ --------------

	/**
	 * コンストラクタ（最小値優先）
	 *
	 * @param n 頂点数
	 */
	public IndexedPriorityQueue(final int n) {
		this(n, false);
	}

	/**
	 * コンストラクタ
	 *
	 * @param n                 頂点数
	 * @param isDescendingOrder true の場合は最大値優先（降順）、false の場合は最小値優先（昇順）
	 */
	public IndexedPriorityQueue(final int n, final boolean isDescendingOrder) {
		this.isDescendingOrder = isDescendingOrder;
		cost = new long[n];
		heap = new int[n];
		position = new int[n];
		fill(position, -2);
		size = 0;
		unsortedCount = 0;
	}

	// -------------- 公開メソッド --------------

	/**
	 * 要素を追加する
	 *
	 * @param node 追加するノード
	 * @param c    追加するノードのコスト
	 */
	public void push(final int node, long c) {
		if (position[node] != -2) throw new IllegalArgumentException();
		if (isDescendingOrder) c = -c;
		cost[node] = c;
		heap[size] = node;
		position[node] = size;
		size++;
		unsortedCount++;
	}

	/**
	 * 全ての要素を追加する
	 *
	 * @param nodes 追加するノードの配列
	 * @param costs 追加するコストの配列
	 */
	public void pushAll(final int[] nodes, final long[] costs) {
		final int n = nodes.length;
		int s = size;
		final long[] cArr = cost;
		final int[] hArr = heap;
		final int[] pArr = position;
		if (isDescendingOrder) {
			for (int i = 0; i < n; i++) {
				final int node = nodes[i];
				if (pArr[node] != -2) throw new IllegalArgumentException();
				cArr[node] = -costs[i];
				hArr[s] = node;
				pArr[node] = s++;
			}
		} else {
			for (int i = 0; i < n; i++) {
				final int node = nodes[i];
				if (pArr[node] != -2) throw new IllegalArgumentException();
				cArr[node] = costs[i];
				hArr[s] = node;
				pArr[node] = s++;
			}
		}
		size = s;
		unsortedCount += n;
	}

	/**
	 * 指定したノードが存在しなければ追加し、存在すれば更新する
	 *
	 * @param node ノード
	 * @param cost ノードのコスト
	 */
	public void pushOrUpdate(final int node, final long cost) {
		if (position[node] < 0) {
			push(node, cost);
		} else {
			updateCost(node, cost);
		}
	}

	/**
	 * 指定したノードのコストを更新する
	 *
	 * @param node    ノード
	 * @param newCost 新しいコスト
	 */
	public void updateCost(final int node, long newCost) {
		if (position[node] < 0) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		if (isDescendingOrder) newCost = -newCost;
		long oldCost = cost[node];
		cost[node] = newCost;
		int pos = position[node];
		if (newCost < oldCost) siftUp(node, pos);
		else siftDown(node, pos);
	}

	/**
	 * relax操作（Dijkstra法などで使用）
	 *
	 * @param node ノード
	 * @param cost 新しいコスト
	 * @return 更新が行われた場合はtrue
	 */
	public boolean relax(final int node, long cost) {
		if (position[node] == -1) return false;
		if (position[node] == -2) {
			push(node, cost);
			return true;
		}
		if (isDescendingOrder) cost = -cost;
		if (this.cost[node] > cost) {
			this.cost[node] = cost;
			siftUp(node, position[node]);
			return true;
		}
		return false;
	}

	/**
	 * ヒープの先頭要素のコストを取得する
	 *
	 * @return ヒープの先頭要素のコスト（昇順時は最小、降順時は最大）
	 */
	public long peek() {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		return isDescendingOrder ? -cost[heap[0]] : cost[heap[0]];
	}

	/**
	 * ヒープの先頭ノードを取得する
	 *
	 * @return ヒープの先頭ノード（昇順時は最小コスト、降順時は最大コストのノード）
	 */
	public int peekNode() {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		return heap[0];
	}

	/**
	 * 2番目の要素のコストを取得する
	 *
	 * @return 2番目の要素のコスト（昇順時は2番目に小さい、降順時は2番目に大きい）
	 */
	public long peekSecond() {
		if (size < 2) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		if (size == 2) {
			return isDescendingOrder ? -cost[heap[1]] : cost[heap[1]];
		}
		long c1 = cost[heap[1]];
		long c2 = cost[heap[2]];
		return isDescendingOrder ? -min(c1, c2) : min(c1, c2);
	}

	/**
	 * ヒープの先頭要素を削除し、そのコストを返す
	 *
	 * @return 削除された要素のコスト（昇順時は最小、降順時は最大）
	 */
	public long poll() {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		int node = heap[0];
		long c = isDescendingOrder ? -cost[node] : cost[node];
		position[node] = -1;
		size--;
		if (size > 0) {
			int lastNode = heap[size];
			siftDown(lastNode, 0);
		}
		return c;
	}

	/**
	 * ヒープの先頭ノードを削除し、そのノードを返す
	 *
	 * @return 削除されたノード（昇順時は最小コスト、降順時は最大コストのノード）
	 */
	public int pollNode() {
		if (isEmpty()) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		int node = heap[0];
		position[node] = -1;
		size--;
		if (size > 0) {
			int lastNode = heap[size];
			siftDown(lastNode, 0);
		}
		return node;
	}

	/**
	 * 指定したノードを削除する
	 *
	 * @param node 削除するノード
	 */
	public void remove(final int node) {
		if (position[node] < 0) throw new NoSuchElementException();
		if (unsortedCount > 0) ensureHeapProperty();
		int pos = position[node];
		position[node] = -1;
		size--;
		if (pos < size) {
			int lastNode = heap[size];
			long lastCost = cost[lastNode];
			long removedCost = cost[node];
			if (lastCost < removedCost) siftUp(lastNode, pos);
			else siftDown(lastNode, pos);
		}
	}

	/**
	 * 指定したノードのコストを取得する（存在しない場合は例外をスロー）
	 *
	 * @param node 対象ノード
	 * @return コスト
	 */
	public long getCost(int node) {
		if (position[node] == -2) throw new NoSuchElementException();
		return isDescendingOrder ? -cost[node] : cost[node];
	}

	/**
	 * 指定したノードのコストを取得する（存在しない場合はデフォルト値を返す）
	 *
	 * @param node         対象ノード
	 * @param defaultValue デフォルト値
	 * @return コストまたはデフォルト値
	 */
	public long getCostOrDefault(final int node, final long defaultValue) {
		return position[node] == -2 ? defaultValue : isDescendingOrder ? -cost[node] : cost[node];
	}

	/**
	 * 要素数を取得する
	 *
	 * @return 要素数
	 */
	public int size() {
		return size;
	}

	/**
	 * ヒープをクリアする
	 */
	public void clear() {
		size = 0;
		unsortedCount = 0;
		fill(position, -2);
	}

	/**
	 * ヒープが空かどうかを判定する
	 *
	 * @return 空の場合はtrue
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * 指定したノードが存在するかどうかを判定する
	 *
	 * @param node 対象ノード
	 * @return 存在する場合はtrue
	 */
	public boolean contains(int node) {
		return position[node] >= 0;
	}

	/**
	 * 要素のイテレータを取得する（ヒープ順序）
	 *
	 * @return イテレータ
	 */
	@Override
	public PrimitiveIterator.OfLong iterator() {
		if (unsortedCount > 0) ensureHeapProperty();
		return new PrimitiveIterator.OfLong() {
			int i = 0;

			@Override
			public boolean hasNext() {
				return i < size;
			}

			@Override
			public long nextLong() {
				if (!hasNext()) throw new NoSuchElementException();
				long c = cost[heap[i++]];
				return isDescendingOrder ? -c : c;
			}
		};
	}

	// -------------- ヒープ構築（遅延評価） --------------

	/**
	 * 遅延評価された未ソート要素をヒープ化し、ヒーププロパティを復元する
	 * <p>
	 * このメソッドは、未ソート要素が存在する場合に最適なアルゴリズムを自動選択して実行します
	 * <p><b>分岐点の決定：</b>
	 * 両アルゴリズムの最大比較回数を計算し、コストが小さい方を実行する
	 * (heapifyCost < incrementalCost なら heapify を選択)
	 */
	private void ensureHeapProperty() {
		final int log2N = 31 - Integer.numberOfLeadingZeros(size);
		final int heapifyCost = size * 2 - 2 * log2N;
		final int incrementalCost = unsortedCount <= 100 ? getIncrementalCostStrict() : getIncrementalCostApprox();
		if (heapifyCost < incrementalCost) {
			heapify();
		} else {
			for (int i = size - unsortedCount; i < size; i++) siftUp(heap[i], i);
		}
		unsortedCount = 0;
	}

	/**
	 * インクリメンタル構築の最大比較回数を厳密に計算する
	 *
	 * @return 最大比較回数の合計
	 */
	private int getIncrementalCostStrict() {
		int totalCost = 0;
		final int sortedSize = size - unsortedCount;
		for (int i = 1; i <= unsortedCount; i++) {
			final int currentHeapSize = sortedSize + i;
			final int depth = 31 - Integer.numberOfLeadingZeros(currentHeapSize);
			totalCost += depth;
		}
		return totalCost;
	}

	/**
	 * インクリメンタル構築の最大比較回数を高速に近似計算する
	 * <p>コスト ≈ k * floor(log₂(平均ヒープサイズ))
	 *
	 * @return 最大比較回数の近似値
	 */
	private int getIncrementalCostApprox() {
		final int sortedSize = size - unsortedCount;
		final int avgHeapSize = sortedSize + (unsortedCount >> 1);
		if (avgHeapSize == 0) return 0;
		final int depthOfAvgSize = 31 - Integer.numberOfLeadingZeros(avgHeapSize);
		return unsortedCount * depthOfAvgSize;
	}

	/**
	 * Bottom-up heapify (Floyd's algorithm)
	 */
	private void heapify() {
		for (int i = (size >> 1) - 1; i >= 0; i--) siftDown(heap[i], i);
	}

	// -------------- ヒープ操作（基本） --------------

	/**
	 * siftUp操作
	 *
	 * @param node 移動させるノード
	 * @param i    ノードの現在位置
	 */
	private void siftUp(final int node, int i) {
		final long[] cArr = cost;
		final int[] hArr = heap;
		final int[] pArr = position;
		final long c = cArr[node];
		while (i > 0) {
			final int j = (i - 1) >> 1;
			final int parent = hArr[j];
			if (c >= cArr[parent]) break;
			hArr[i] = parent;
			pArr[parent] = i;
			i = j;
		}
		hArr[i] = node;
		pArr[node] = i;
	}

	/**
	 * siftDown操作
	 *
	 * @param node 移動させるノード
	 * @param i    ノードの現在位置
	 */
	private void siftDown(final int node, int i) {
		final long[] cArr = cost;
		final int[] hArr = heap;
		final int[] pArr = position;
		final int n = size;
		final long c = cArr[node];
		final int half = n >> 1;
		while (i < half) {
			int child = (i << 1) + 1;
			if (child + 1 < n && cArr[hArr[child]] > cArr[hArr[child + 1]]) child++;
			final int childNode = hArr[child];
			if (c <= cArr[childNode]) break;
			hArr[i] = childNode;
			pArr[childNode] = i;
			i = child;
		}
		hArr[i] = node;
		pArr[node] = i;
	}
}
