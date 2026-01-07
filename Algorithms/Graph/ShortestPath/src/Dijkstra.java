import java.util.*;

import static java.util.Arrays.*;

/**
 * Dijkstra アルゴリズムを実装するクラス
 * <p>
 * 非負重み付きグラフに対して、単一始点から各頂点への最短経路（または最長経路）を求める。
 * 専用の優先度付きキューを用いて効率的な探索を実現している。
 * <p>
 * 同一始点での複数回呼び出しに対応するためキャッシュ機構を用いている。
 * 辺の追加後はキャッシュが自動的に無効化される。
 */
@SuppressWarnings("unused")
public final class Dijkstra {
	// -------------- フィールド --------------
	private static final long INF = Long.MAX_VALUE;
	private final IndexedPriorityQueue ans;
	private final int[] dest, next, first, path;
	private final long[] cost;
	private final int v;
	private int used = -1, e;

	// -------------- コンストラクタ --------------

	/**
	 * コンストラクタ
	 *
	 * @param v 頂点数
	 * @param e 辺数の最大値
	 */
	public Dijkstra(final int v, final int e) {
		this(v, e, false);
	}

	/**
	 * コンストラクタ
	 *
	 * @param v     頂点数
	 * @param e     辺数の最大値
	 * @param isMax true の場合は最長経路を求める（最大値優先）、false の場合は最短経路を求める（最小値優先）
	 */
	public Dijkstra(final int v, final int e, final boolean isMax) {
		dest = new int[e];
		next = new int[e];
		first = new int[v];
		path = new int[v];
		cost = new long[e];
		this.v = v;
		fill(first, -1);
		this.e = 0;
		ans = new IndexedPriorityQueue(v, isMax);
	}

	// -------------- グラフ構築 --------------

	/**
	 * 有向辺を追加する
	 *
	 * @param i 辺の始点（0-indexed）
	 * @param j 辺の終点（0-indexed）
	 * @param c 辺の重み
	 */
	public void addEdge(final int i, final int j, final long c) {
		dest[e] = j;
		cost[e] = c;
		next[e] = first[i];
		first[i] = e;
		e++;
		used = -1;
	}

	// -------------- 最短経路探索 --------------

	/**
	 * 始点 i から終点 j への最短経路の重みを返す
	 *
	 * @param i 始点
	 * @param j 終点
	 * @return 始点から終点への最短経路の重み（経路が存在しない場合は INF）
	 */
	public long solve(final int i, final int j) {
		if (used != i) {
			used = i;
			ans.clear();
			ans.push(i, 0);
			path[i] = i;
			while (!ans.isEmpty()) {
				final long c = ans.peek();
				final int from = ans.pollNode();
				for (int e = first[from]; e != -1; e = next[e]) {
					final int to = dest[e];
					final long cost = this.cost[e];
					if (ans.relax(to, c + cost)) path[to] = from;
				}
			}
		}
		return ans.getCostOrDefault(j, INF);
	}

	/**
	 * 始点 i から終点 j への最短経路を返す
	 *
	 * @param i 始点
	 * @param j 終点
	 * @return 始点から終点への最短経路（経路が存在しない場合は null）
	 */
	public ArrayList<Integer> getPath(final int i, final int j) {
		if (used != i) solve(i, j);
		if (ans.getCostOrDefault(j, INF) == INF) return null;
		final ArrayList<Integer> path = new ArrayList<>(v);
		for (int p = j; p != i; p = this.path[p]) path.add(p);
		path.add(i);
		for (int k = 0, size = path.size(); k < size / 2; k++) {
			path.set(k, path.set(size - k - 1, path.get(k)));
		}
		return path;
	}

	// -------------- 内部クラス：IndexedPriorityQueue --------------

	/**
	 * Dijkstra法特化インデックス付き優先度キュー
	 * <p>
	 * 遅延ヒープ構築により効率的な探索を実現する内部クラス。
	 */
	private static final class IndexedPriorityQueue {
		// -------------- フィールド --------------
		private final boolean isDescendingOrder;
		private final long[] cost;
		private final int[] heap, position;
		private int size, unsortedCount;

		// -------------- コンストラクタ --------------

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
		 * ヒープの先頭ノードを削除し、そのノードを返す
		 *
		 * @return 削除されたノード（昇順時は最小コスト、降順時は最大コストのノード）
		 */
		public int pollNode() {
			if (isEmpty()) throw new NoSuchElementException();
			if (unsortedCount > 0) ensureHeapProperty();
			final int node = heap[0];
			position[node] = -1;
			size--;
			if (size > 0) {
				int lastNode = heap[size];
				siftDown(lastNode, 0);
			}
			return node;
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

		// -------------- ヒープ構築（遅延評価） --------------

		/**
		 * 遅延評価された未ソート要素をヒープ化し、ヒーププロパティを復元する。
		 * <p>
		 * このメソッドは、未ソート要素が存在する場合に最適なアルゴリズムを自動選択して実行します。
		 * <p><b>分岐点の決定：</b>
		 * 両アルゴリズムの最大比較回数を計算し、コストが小さい方を実行する。
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
		 * インクリメンタル構築の最大比較回数を厳密に計算する。
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
		 * インクリメンタル構築の最大比較回数を高速に近似計算する。
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
			final long c = cost[node];
			while (i > 0) {
				final int j = (i - 1) >> 1;
				final int parent = heap[j];
				if (c >= cost[parent]) break;
				heap[i] = parent;
				position[parent] = i;
				i = j;
			}
			heap[i] = node;
			position[node] = i;
		}

		/**
		 * siftDown操作
		 *
		 * @param node 移動させるノード
		 * @param i    ノードの現在位置
		 */
		private void siftDown(final int node, int i) {
			final long c = cost[node];
			final int half = size >> 1;
			while (i < half) {
				int child = (i << 1) + 1;
				child += child + 1 < size && cost[heap[child]] > cost[heap[child + 1]] ? 1 : 0;
				final int childNode = heap[child];
				if (c <= cost[childNode]) break;
				heap[i] = childNode;
				position[childNode] = i;
				i = child;
			}
			heap[i] = node;
			position[node] = i;
		}
	}
}
