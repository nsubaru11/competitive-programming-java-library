import java.io.*;
import java.lang.invoke.*;
import java.math.*;
import java.nio.*;
import java.nio.charset.*;
import java.util.*;

import static java.lang.Math.*;
import static java.util.Arrays.*;

// https://onlinejudge.u-aizu.ac.jp/courses/library/5/GRL/2/GRL_2_A
public final class Check2 {

	// region < Constants & Globals >
	private static final FastScanner sc = new FastScanner();
	private static final FastPrinter out = new FastPrinter(64);
	// endregion

	private static void solve() {
		int v = sc.nextInt();
		int e = sc.nextInt();
		Prim solver = new Prim(v, e);
		while (e-- > 0) {
			int s = sc.nextInt();
			int t = sc.nextInt();
			int w = sc.nextInt();
			solver.addEdge(s, t, w);
		}
		out.println(solver.solve());
	}

	// region < I/O & Debug >
	public static void main(final String[] args) {
		try {
			solve();
		} finally {
			sc.close();
			out.close();
		}
	}

	/**
	 * Prim法を用いた全域木（MST）問題のソルバー。
	 * 最小全域木または最大全域木を求めることができます。
	 * 時間計算量: O(|E|log|V|)
	 * 空間計算量: O(|V| + |E|)
	 */
	@SuppressWarnings("unused")
	private static final class Prim {
		private final int n;
		private final boolean isMinimum;
		private final int[] first, next, dest, path;
		private final long[] cost;
		private int edgeCnt = 0;
		private long ans = 0;
		private boolean solved = false;

		/**
		 * 指定された頂点数と最小/最大フラグでソルバーを初期化します。
		 *
		 * @param n         頂点数（0からv-1までの頂点番号が使用される）
		 * @param m         辺の数
		 * @param isMinimum trueの場合は最小全域木、falseの場合は最大全域木を求めます
		 */
		public Prim(final int n, final int m, final boolean isMinimum) {
			this.n = n;
			this.isMinimum = isMinimum;
			first = new int[n];
			next = new int[m * 2];
			dest = new int[m * 2];
			path = new int[n - 1];
			cost = new long[m * 2];
			fill(first, -1);
		}

		/**
		 * 最小全域木を求めるソルバーを初期化します。
		 *
		 * @param n 頂点数（0からv-1までの頂点番号が使用される）
		 * @param m 辺の数
		 */
		public Prim(final int n, final int m) {
			this(n, m, true);
		}

		/**
		 * グラフに無向辺を追加します。
		 *
		 * @param u 辺の始点（0からv-1までの値）
		 * @param v 辺の終点（0からv-1までの値）
		 * @param c 辺の重み
		 */
		public void addEdge(final int u, final int v, final long c) {
			next[edgeCnt] = first[u];
			first[u] = edgeCnt;
			dest[edgeCnt] = v;
			cost[edgeCnt] = isMinimum ? c : -c;
			edgeCnt++;
			next[edgeCnt] = first[v];
			first[v] = edgeCnt;
			dest[edgeCnt] = u;
			cost[edgeCnt] = isMinimum ? c : -c;
			edgeCnt++;
		}

		/**
		 * Prim法を実行し、全域木の総コストを計算します。
		 * 設定に応じて最小全域木または最大全域木を求めます。
		 * グラフが連結でない場合は-1を返します。
		 *
		 * @return 全域木の総コスト、または連結グラフでない場合は-1
		 */
		public long solve() {
			if (solved) return ans;
			solved = true;
			int cnt = 0;
			ans = 0;
			IndexedPriorityQueue pq = new IndexedPriorityQueue(n);
			pq.push(0, 0);
			int[] parentEdge = new int[n];
			while (!pq.isEmpty()) {
				final int u = pq.peekNode();
				final long c = pq.poll();
				if (cnt > 0) {
					ans += c;
					path[cnt - 1] = parentEdge[u];
				}
				cnt++;
				if (cnt == n) break;
				for (int e2 = first[u]; e2 != -1; e2 = next[e2]) {
					int v = dest[e2];
					if (pq.relax(v, cost[e2])) parentEdge[v] = e2 >> 1;
				}
			}
			return ans = cnt < n ? -1 : isMinimum ? ans : -ans;
		}

		/**
		 * 全域木を構成する辺のインデックスを取得します。
		 * インデックスは addEdge で追加された順序（0-indexed）に対応します。
		 *
		 * @return 全域木を構成する辺のインデックス配列
		 */
		public int[] solvePath() {
			if (!solved) solve();
			return path;
		}

		/**
		 * グラフアルゴリズム特化高性能インデックス付き優先度キュー
		 * <p>
		 * 遅延ヒープ構築により、複数の要素追加後に一度だけヒープ化を行うことで高速化を実現。
		 * インデックスベースの操作により、特定ノードへの O(1) アクセスが可能。
		 * グラフの最短経路問題（Dijkstra法など）での使用に最適化されている。
		 */
		@SuppressWarnings("unused")
		private static final class IndexedPriorityQueue implements Iterable<Long> {
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
	}

	@SuppressWarnings("unused")
	private static final class FastScanner implements AutoCloseable {
		private static final int DEFAULT_BUFFER_SIZE = 1 << 20;
		private final InputStream in;
		private final byte[] buffer;
		private int pos = 0, bufferLength = 0;

		public FastScanner() {
			this(System.in, DEFAULT_BUFFER_SIZE);
		}

		public FastScanner(final InputStream in) {
			this(in, DEFAULT_BUFFER_SIZE);
		}

		public FastScanner(final int bufferSize) {
			this(System.in, bufferSize);
		}

		public FastScanner(final InputStream in, final int bufferSize) {
			this.in = in;
			this.buffer = new byte[bufferSize];
		}

		private int skipSpaces() {
			final byte[] buf = buffer;
			int p = pos, len = bufferLength, b;
			do {
				if (p >= len) {
					try {
						len = in.read(buf);
						p = 0;
					} catch (final IOException e) {
						throw new RuntimeException(e);
					}
					if (len <= 0) throw new NoSuchElementException();
					if (len < buf.length) buf[len] = 32;
				}
				b = buf[p++];
			} while (b <= 32);
			pos = p;
			bufferLength = len;
			return b;
		}

		@Override
		public void close() {
			try {
				in.close();
			} catch (final IOException e) {
				throw new RuntimeException(e);
			}
		}

		private boolean hasNextByte() {
			if (pos < bufferLength) return true;
			pos = 0;
			try {
				bufferLength = in.read(buffer);
				if (bufferLength > 0 && bufferLength < buffer.length) buffer[bufferLength] = 32;
			} catch (final IOException e) {
				throw new RuntimeException(e);
			}
			return bufferLength > 0;
		}

		public boolean hasNext() {
			while (hasNextByte()) {
				if (buffer[pos] > 32) return true;
				pos++;
			}
			return false;
		}

		public char nextChar() {
			if (!hasNext()) throw new NoSuchElementException();
			return (char) buffer[pos++];
		}

		public int nextInt() {
			int b = skipSpaces();
			boolean negative = false;
			if (b == '-') {
				negative = true;
				if (pos == bufferLength && !hasNextByte()) throw new NoSuchElementException();
				b = buffer[pos++];
			}
			return pos + 10 <= bufferLength ? nextIntFast(b, negative) : nextIntSlow(b, negative);
		}

		private int nextIntFast(int b, final boolean negative) {
			final byte[] buf = buffer;
			int p = pos, n = 0;
			long a = (long) Handles.LONG_HANDLE.get(buf, p - 1) ^ 0x3030303030303030L;
			long check = a & 0xF0F0F0F0F0F0F0F0L;
			if (check == 0) {
				a = (a * 10 + (a >>> 8)) & 0x00FF00FF00FF00FFL;
				a = (a * 100 + (a >>> 16)) & 0x0000FFFF0000FFFFL;
				a = (a * 10000 + (a >>> 32)) & 0x00000000FFFFFFFFL;
				n = (int) a;
				p += 7;
				b = buf[p++];
			}
			while (b > 32) {
				n = (n << 3) + (n << 1) + (b & 15);
				b = buf[p++];
			}
			pos = p;
			return negative ? -n : n;
		}

		private int nextIntSlow(int b, final boolean negative) {
			int p = pos, len = bufferLength;
			int n = 0;
			do {
				n = (n << 3) + (n << 1) + (b & 15);
				if (p == len) {
					pos = p;
					if (!hasNextByte()) {
						p = pos;
						break;
					}
					p = pos;
					len = bufferLength;
				}
				b = buffer[p++];
			} while (b > 32);
			pos = p;
			return negative ? -n : n;
		}

		public long nextLong() {
			int b = skipSpaces();
			boolean negative = false;
			if (b == '-') {
				negative = true;
				if (pos == bufferLength && !hasNextByte()) throw new NoSuchElementException();
				b = buffer[pos++];
			}
			return pos + 20 <= bufferLength ? nextLongFast(b, negative) : nextLongSlow(b, negative);
		}

		private long nextLongFast(int b, final boolean negative) {
			final byte[] buf = buffer;
			int p = pos;
			long n = 0;
			long a = (long) Handles.LONG_HANDLE.get(buf, p - 1) ^ 0x3030303030303030L;
			long check = a & 0xF0F0F0F0F0F0F0F0L;
			if (check == 0) {
				a = (a * 10 + (a >>> 8)) & 0x00FF00FF00FF00FFL;
				a = (a * 100 + (a >>> 16)) & 0x0000FFFF0000FFFFL;
				a = (a * 10000 + (a >>> 32)) & 0x00000000FFFFFFFFL;
				n = a;
				p += 7;
				b = buf[p++];
				long a2 = (long) Handles.LONG_HANDLE.get(buf, p - 1) ^ 0x3030303030303030L;
				long check2 = a2 & 0xF0F0F0F0F0F0F0F0L;
				if (check2 == 0) {
					a2 = (a2 * 10 + (a2 >>> 8)) & 0x00FF00FF00FF00FFL;
					a2 = (a2 * 100 + (a2 >>> 16)) & 0x0000FFFF0000FFFFL;
					a2 = (a2 * 10000 + (a2 >>> 32)) & 0x00000000FFFFFFFFL;
					n = n * 100000000L + a2;
					p += 7;
					b = buf[p++];
				}
			}
			while (b > 32) {
				n = (n << 3) + (n << 1) + (b & 15);
				b = buf[p++];
			}
			pos = p;
			return negative ? -n : n;
		}

		private long nextLongSlow(int b, final boolean negative) {
			int p = pos, len = bufferLength;
			long n = 0;
			do {
				n = (n << 3) + (n << 1) + (b & 15);
				if (p == len) {
					pos = p;
					if (!hasNextByte()) {
						p = pos;
						break;
					}
					p = pos;
					len = bufferLength;
				}
				b = buffer[p++];
			} while (b > 32);
			pos = p;
			return negative ? -n : n;
		}

		public double nextDouble() {
			int b = skipSpaces();
			boolean negative = false;
			if (b == '-') {
				negative = true;
				if (pos == bufferLength && !hasNextByte()) throw new NoSuchElementException();
				b = buffer[pos++];
			}
			return pos + 20 <= bufferLength ? nextDoubleFast(b, negative) : nextDoubleSlow(b, negative);
		}

		private double nextDoubleFast(int b, final boolean negative) {
			final byte[] buf = buffer;
			int p = pos, len = bufferLength;
			long intPart = 0;
			do {
				intPart = (intPart << 3) + (intPart << 1) + (b & 15);
				b = buf[p++];
			} while ('0' <= b && b <= '9');
			double result = intPart;
			if (b == '.') result += parseFracPart(p, len, buf);
			else pos = p;
			return negative ? -result : result;
		}

		private double nextDoubleSlow(int b, final boolean negative) {
			final byte[] buf = buffer;
			int p = pos, len = bufferLength;
			long intPart = 0;
			do {
				intPart = (intPart << 3) + (intPart << 1) + (b & 15);
				if (p == len) {
					pos = p;
					if (!hasNextByte()) {
						p = pos;
						b = -1;
						break;
					}
					p = pos;
					len = bufferLength;
				}
				b = buf[p++];
			} while ('0' <= b && b <= '9');

			double result = intPart;
			if (b == '.') result += parseFracPart(p, len, buf);
			else pos = p;
			return negative ? -result : result;
		}

		private double parseFracPart(int p, int len, final byte[] buf) {
			if (p == len) {
				pos = p;
				hasNextByte();
				p = pos;
				len = bufferLength;
			}
			int b = buf[p++];
			long fracPart = 0, divisor = 1;
			if (p + 20 <= len) {
				do {
					fracPart = fracPart * 10 + (b & 15);
					divisor *= 10;
					b = buf[p++];
				} while ('0' <= b && b <= '9');
			} else {
				do {
					fracPart = fracPart * 10 + (b & 15);
					divisor *= 10;
					if (p == len) {
						pos = p;
						if (!hasNextByte()) {
							p = pos;
							break;
						}
						p = pos;
						len = bufferLength;
					}
					b = buf[p++];
				} while ('0' <= b && b <= '9');
			}
			pos = p;
			return (double) fracPart / divisor;
		}

		public String next() {
			skipSpaces();
			final byte[] buf = buffer;
			int p = pos, len = bufferLength;
			final int start = p - 1;
			while (p < len && buf[p] > 32) p++;
			if (p < len) {
				final String s = new String(buf, start, p - start, StandardCharsets.US_ASCII);
				pos = p + 1;
				return s;
			}
			final StringBuilder sb = new StringBuilder(len - start + 16);
			for (int i = start; i < len; i++) sb.append((char) buf[i]);
			while (true) {
				if (p == len) {
					pos = p;
					if (!hasNextByte()) {
						p = pos;
						break;
					}
					p = pos;
					len = bufferLength;
				}
				final int b = buf[p++];
				if (b <= 32) break;
				sb.append((char) b);
			}
			pos = p;
			return sb.toString();
		}

		public StringBuilder nextStringBuilder() {
			final StringBuilder sb = new StringBuilder();
			int b = skipSpaces(), p = pos, len = bufferLength;
			do {
				sb.append((char) b);
				if (p == len) {
					pos = p;
					if (!hasNextByte()) {
						p = pos;
						break;
					}
					p = pos;
					len = bufferLength;
				}
				b = buffer[p++];
			} while (b > 32);
			pos = p;
			return sb;
		}

		public String nextLine() {
			if (pos == bufferLength && !hasNextByte()) throw new NoSuchElementException();
			final byte[] buf = buffer;
			int p = pos, len = bufferLength;
			final int start = p;
			while (p < len) {
				final int b = buf[p];
				if (b == '\n' || b == '\r') {
					final String s = new String(buf, start, p - start, StandardCharsets.US_ASCII);
					p++;
					if (b == '\r') {
						if (p == len) {
							pos = p;
							hasNextByte();
							p = pos;
							len = bufferLength;
						}
						if (p < len && buf[p] == '\n') p++;
					}
					pos = p;
					return s;
				}
				p++;
			}

			final StringBuilder sb = new StringBuilder();
			for (int i = start; i < len; i++) sb.append((char) buf[i]);
			while (true) {
				pos = len;
				if (!hasNextByte()) {
					pos = len;
					return sb.toString();
				}
				p = pos;
				len = bufferLength;
				while (p < len) {
					final int b = buf[p];
					if (b == '\n' || b == '\r') {
						p++;
						if (b == '\r') {
							if (p == len) {
								pos = p;
								hasNextByte();
								p = pos;
								len = bufferLength;
							}
							if (p < len && buf[p] == '\n') p++;
						}
						pos = p;
						return sb.toString();
					}
					sb.append((char) b);
					p++;
				}
			}
		}

		public BigInteger nextBigInteger() {
			return new BigInteger(next());
		}

		public BigDecimal nextBigDecimal() {
			return new BigDecimal(next());
		}

		private static final class Handles {
			private static final VarHandle LONG_HANDLE = MethodHandles.byteArrayViewVarHandle(long[].class, ByteOrder.LITTLE_ENDIAN);
		}
	}

	@SuppressWarnings("unused")
	private static final class FastPrinter implements AutoCloseable {
		private static final int MAX_INT_DIGITS = 11;
		private static final int MAX_LONG_DIGITS = 20;
		private static final int MAX_BOOL_DIGITS = 3;
		private static final int DEFAULT_BUFFER_SIZE = 1 << 20;
		private static final byte LINE = '\n';
		private static final byte SPACE = ' ';
		private static final byte HYPHEN = '-';
		private static final byte PERIOD = '.';
		private static final byte ZERO = '0';
		private final OutputStream out;
		private final boolean autoFlush;
		private byte[] buffer;
		private int pos;

		public FastPrinter() {
			this(System.out, DEFAULT_BUFFER_SIZE, false);
		}

		public FastPrinter(final OutputStream out) {
			this(out, DEFAULT_BUFFER_SIZE, false);
		}

		public FastPrinter(final int bufferSize) {
			this(System.out, bufferSize, false);
		}

		public FastPrinter(final boolean autoFlush) {
			this(System.out, DEFAULT_BUFFER_SIZE, autoFlush);
		}

		public FastPrinter(final OutputStream out, final boolean autoFlush) {
			this(out, DEFAULT_BUFFER_SIZE, autoFlush);
		}

		public FastPrinter(final int bufferSize, final boolean autoFlush) {
			this(System.out, bufferSize, autoFlush);
		}

		public FastPrinter(final OutputStream out, final int bufferSize) {
			this(out, bufferSize, false);
		}

		public FastPrinter(final OutputStream out, final int bufferSize, final boolean autoFlush) {
			this.out = out;
			this.buffer = new byte[bufferSize(bufferSize)];
			this.autoFlush = autoFlush;
		}

		private static int countDigits(final int i) {
			if (i > -100000) {
				if (i > -100) {
					return i > -10 ? 1 : 2;
				} else {
					if (i > -10000) return i > -1000 ? 3 : 4;
					else return 5;
				}
			} else {
				if (i > -10000000) {
					return i > -1000000 ? 6 : 7;
				} else {
					if (i > -1000000000) return i > -100000000 ? 8 : 9;
					else return 10;
				}
			}
		}

		private static int countDigits(final long l) {
			if (l > -1000000000) {
				if (l > -10000) {
					if (l > -100) {
						return l > -10 ? 1 : 2;
					} else {
						return l > -1000 ? 3 : 4;
					}
				} else {
					if (l > -1000000) {
						return l > -100000 ? 5 : 6;
					} else {
						if (l > -100000000) return l > -10000000 ? 7 : 8;
						else return 9;
					}
				}
			} else {
				if (l > -10000000000000L) {
					if (l > -100000000000L) {
						return l > -10000000000L ? 10 : 11;
					} else {
						return l > -1000000000000L ? 12 : 13;
					}
				} else {
					if (l > -10000000000000000L) {
						if (l > -1000000000000000L) return l > -100000000000000L ? 14 : 15;
						else return 16;
					} else {
						if (l > -1000000000000000000L) return l > -100000000000000000L ? 17 : 18;
						else return 19;
					}
				}
			}
		}

		private static int bufferSize(int x) {
			return x <= 64 ? 64 : 1 << (32 - Integer.numberOfLeadingZeros(x - 1));
		}

		@Override
		public void close() {
			try {
				flush();
				out.close();
			} catch (final IOException e) {
				throw new RuntimeException(e);
			}
		}

		public void flush() {
			if (pos == 0) return;
			try {
				out.write(buffer, 0, pos);
				pos = 0;
			} catch (final IOException e) {
				throw new RuntimeException(e);
			}
		}

		public FastPrinter println() {
			ensureCapacity(1);
			buffer[pos++] = LINE;
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter println(final boolean b) {
			ensureCapacity(MAX_BOOL_DIGITS + 1);
			final int p = write(b, pos);
			buffer[p] = LINE;
			pos = p + 1;
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter println(final byte b) {
			ensureCapacity(2);
			final byte[] buf = buffer;
			final int p = pos;
			buf[p] = b;
			buf[p + 1] = LINE;
			pos = p + 2;
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter println(final char c) {
			ensureCapacity(2);
			final byte[] buf = buffer;
			final int p = pos;
			buf[p] = (byte) c;
			buf[p + 1] = LINE;
			pos = p + 2;
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter println(final int i) {
			ensureCapacity(MAX_INT_DIGITS + 1);
			final int p = write(i, pos);
			buffer[p] = LINE;
			pos = p + 1;
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter println(final long l) {
			ensureCapacity(MAX_LONG_DIGITS + 1);
			final int p = write(l, pos);
			buffer[p] = LINE;
			pos = p + 1;
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter println(final double d) {
			return println(Double.toString(d));
		}

		public FastPrinter println(final BigInteger bi) {
			return println(bi.toString());
		}

		public FastPrinter println(final BigDecimal bd) {
			return println(bd.toString());
		}

		public FastPrinter println(final String s) {
			ensureCapacity(s.length() + 1);
			final int p = write(s, pos);
			buffer[p] = LINE;
			pos = p + 1;
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter println(final StringBuilder s) {
			ensureCapacity(s.length() + 1);
			final int p = write(s, pos);
			buffer[p] = LINE;
			pos = p + 1;
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter println(final Object o) {
			return println(o.toString());
		}

		public FastPrinter print(final boolean b) {
			ensureCapacity(MAX_BOOL_DIGITS);
			pos = write(b, pos);
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter print(final byte b) {
			ensureCapacity(1);
			buffer[pos++] = b;
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter print(final char c) {
			ensureCapacity(1);
			buffer[pos++] = (byte) c;
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter print(final int i) {
			ensureCapacity(MAX_INT_DIGITS);
			pos = write(i, pos);
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter print(final long l) {
			ensureCapacity(MAX_LONG_DIGITS);
			pos = write(l, pos);
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter print(final double d) {
			return print(Double.toString(d));
		}

		public FastPrinter print(final BigInteger bi) {
			return print(bi.toString());
		}

		public FastPrinter print(final BigDecimal bd) {
			return print(bd.toString());
		}

		public FastPrinter print(final String s) {
			ensureCapacity(s.length());
			pos = write(s, pos);
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter print(final StringBuilder s) {
			ensureCapacity(s.length());
			pos = write(s, pos);
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter print(final Object o) {
			return print(o.toString());
		}

		public FastPrinter printf(final String format, final Object... args) {
			return print(String.format(format, args));
		}

		public FastPrinter printf(final Locale locale, final String format, final Object... args) {
			return print(String.format(locale, format, args));
		}

		private void ensureCapacity(final int additional) {
			final int required = pos + additional;
			final int bufferLength = buffer.length;
			if (required <= bufferLength) return;
			flush();
			if (additional > bufferLength) buffer = new byte[bufferSize(additional)];
		}

		private int write(final boolean b, int p) {
			final byte[] src = b ? Cache.TRUE_BYTES : Cache.FALSE_BYTES;
			final int len = src.length;
			System.arraycopy(src, 0, buffer, p, len);
			return p + len;
		}

		private int write(int i, int p) {
			final byte[] buf = buffer;
			final VarHandle shortHandle = Cache.SHORT_HANDLE;
			final VarHandle intHandle = Cache.INT_HANDLE;
			final short[] digits2 = Cache.DIGITS_2;
			final int[] digits4 = Cache.DIGITS_4;
			if (i >= 0) i = -i;
			else buf[p++] = HYPHEN;
			final int digits = countDigits(i);
			int writePos = p + digits;
			if (i <= -100000000) {
				final int q = i / 100000000;
				final int r = (q * 100000000) - i;
				final int hi = r / 10000;
				intHandle.set(buf, writePos - 8, digits4[hi]);
				intHandle.set(buf, writePos - 4, digits4[r - hi * 10000]);
				writePos -= 8;
				i = q;
			}
			if (i <= -10000) {
				final int q = i / 10000;
				final int r = (q * 10000) - i;
				intHandle.set(buf, writePos - 4, digits4[r]);
				writePos -= 4;
				i = q;
			}
			if (i <= -100) {
				final int q = i / 100;
				final int r = (q * 100) - i;
				shortHandle.set(buf, writePos - 2, digits2[r]);
				writePos -= 2;
				i = q;
			}
			final int r = -i;
			if (r >= 10) shortHandle.set(buf, writePos - 2, digits2[r]);
			else buf[writePos - 1] = (byte) (r + ZERO);
			return p + digits;
		}

		private int write(long l, int p) {
			final byte[] buf = buffer;
			final VarHandle shortHandle = Cache.SHORT_HANDLE;
			final VarHandle intHandle = Cache.INT_HANDLE;
			final short[] digits2 = Cache.DIGITS_2;
			final int[] digits4 = Cache.DIGITS_4;
			if (l >= 0) l = -l;
			else buf[p++] = HYPHEN;
			final int digits = countDigits(l);
			int writePos = p + digits;
			if (l <= -100000000L) {
				long q = l / 100000000L;
				int r = (int) ((q * 100000000L) - l);
				int hi = r / 10000;
				intHandle.set(buf, writePos - 8, digits4[hi]);
				intHandle.set(buf, writePos - 4, digits4[r - hi * 10000]);
				writePos -= 8;
				l = q;
				if (l <= -100000000L) {
					q = l / 100000000L;
					r = (int) ((q * 100000000L) - l);
					hi = r / 10000;
					intHandle.set(buf, writePos - 8, digits4[hi]);
					intHandle.set(buf, writePos - 4, digits4[r - hi * 10000]);
					writePos -= 8;
					l = q;
				}
			}
			if (l <= -10000) {
				final long q = l / 10000;
				final int r = (int) ((q * 10000) - l);
				intHandle.set(buf, writePos - 4, digits4[r]);
				writePos -= 4;
				l = q;
			}
			if (l <= -100) {
				final long q = l / 100;
				final int r = (int) ((q * 100) - l);
				shortHandle.set(buf, writePos - 2, digits2[r]);
				writePos -= 2;
				l = q;
			}
			final int r = (int) -l;
			if (r >= 10) shortHandle.set(buf, writePos - 2, digits2[r]);
			else buf[writePos - 1] = (byte) (r + ZERO);
			return p + digits;
		}

		private int write(final CharSequence s, int p) {
			final int len = s.length();
			final byte[] buf = buffer;
			int i = 0;
			final int limit = len & ~7;
			while (i < limit) {
				buf[p] = (byte) s.charAt(i);
				buf[p + 1] = (byte) s.charAt(i + 1);
				buf[p + 2] = (byte) s.charAt(i + 2);
				buf[p + 3] = (byte) s.charAt(i + 3);
				buf[p + 4] = (byte) s.charAt(i + 4);
				buf[p + 5] = (byte) s.charAt(i + 5);
				buf[p + 6] = (byte) s.charAt(i + 6);
				buf[p + 7] = (byte) s.charAt(i + 7);
				p += 8;
				i += 8;
			}
			while (i < len) buf[p++] = (byte) s.charAt(i++);
			return p;
		}

		private static final class Cache {
			private static final VarHandle SHORT_HANDLE = MethodHandles.byteArrayViewVarHandle(short[].class, ByteOrder.LITTLE_ENDIAN);
			private static final VarHandle INT_HANDLE = MethodHandles.byteArrayViewVarHandle(int[].class, ByteOrder.LITTLE_ENDIAN);
			private static final byte[] TRUE_BYTES = {'Y', 'e', 's'};
			private static final byte[] FALSE_BYTES = {'N', 'o'};
			private static final short[] DIGITS_2 = {
					12336, 12592, 12848, 13104, 13360, 13616, 13872, 14128, 14384, 14640,
					12337, 12593, 12849, 13105, 13361, 13617, 13873, 14129, 14385, 14641,
					12338, 12594, 12850, 13106, 13362, 13618, 13874, 14130, 14386, 14642,
					12339, 12595, 12851, 13107, 13363, 13619, 13875, 14131, 14387, 14643,
					12340, 12596, 12852, 13108, 13364, 13620, 13876, 14132, 14388, 14644,
					12341, 12597, 12853, 13109, 13365, 13621, 13877, 14133, 14389, 14645,
					12342, 12598, 12854, 13110, 13366, 13622, 13878, 14134, 14390, 14646,
					12343, 12599, 12855, 13111, 13367, 13623, 13879, 14135, 14391, 14647,
					12344, 12600, 12856, 13112, 13368, 13624, 13880, 14136, 14392, 14648,
					12345, 12601, 12857, 13113, 13369, 13625, 13881, 14137, 14393, 14649,
			};
			private static final int[] DIGITS_4 = new int[10000];
			private static final long[] POW10 = {
					1, 10, 100, 1_000, 10_000, 100_000, 1_000_000, 10_000_000, 100_000_000,
					1_000_000_000, 10_000_000_000L, 100_000_000_000L, 1_000_000_000_000L,
					10_000_000_000_000L, 100_000_000_000_000L, 1_000_000_000_000_000L,
					10_000_000_000_000_000L, 100_000_000_000_000_000L, 1_000_000_000_000_000_000L
			};

			static {
				int idx4 = 0;
				for (int i = 0; i < 100; i++) {
					final int hi = DIGITS_2[i] & 0xFFFF;
					for (int j = 0; j < 100; j++) {
						final int lo = DIGITS_2[j] & 0xFFFF;
						DIGITS_4[idx4++] = (lo << 16) | hi;
					}
				}
			}
		}
	}
	// endregion
}
