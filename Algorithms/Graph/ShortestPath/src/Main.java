import java.io.*;
import java.util.*;
import java.math.*;

import static java.lang.Math.*;
import static java.util.Arrays.*;

// AOJ GRL_1_A
public final class Main {

	private static void solve(final FastScanner sc, final FastPrinter out) {
		int v = sc.nextInt(), e = sc.nextInt(), r = sc.nextInt();
		Dijkstra di = new Dijkstra(v, e);
		while (e-- > 0) di.addEdge(sc.nextInt(), sc.nextInt(), sc.nextInt());
		for (int i = 0; i < v; i++) {
			long ans = di.solve(r, i);
			if (ans == Long.MAX_VALUE) out.println("INF");
			else out.println(ans);
		}
	}

	public static void main(String[] args) {
		try (final FastScanner sc = new FastScanner();
			 final FastPrinter out = new FastPrinter()) {
			solve(sc, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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
	private static final class Dijkstra {
		// -------------- フィールド --------------
		private static final long INF = Long.MAX_VALUE;
		private final IndexedPriorityQueue ans;
		private final int[] dest, next, first;
		private final long[] cost;
		private int used = -1;
		private int e;

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
			cost = new long[e];
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
				while (!ans.isEmpty()) {
					long c = ans.peek();
					int from = ans.pollNode();
					for (int e = first[from]; e != -1; e = next[e]) {
						int to = dest[e];
						long cost = this.cost[e];
						ans.relax(to, c + cost);
					}
				}
			}
			return ans.getCostOrDefault(j, INF);
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
				int log2N = 31 - Integer.numberOfLeadingZeros(size);
				int heapifyCost = size * 2 - 2 * log2N;
				int incrementalCost = unsortedCount <= 100 ? getIncrementalCostStrict() : getIncrementalCostApprox();
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
				int sortedSize = size - unsortedCount;
				for (int i = 1; i <= unsortedCount; i++) {
					int currentHeapSize = sortedSize + i;
					int depth = 31 - Integer.numberOfLeadingZeros(currentHeapSize);
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
				int sortedSize = size - unsortedCount;
				int avgHeapSize = sortedSize + (unsortedCount >> 1);
				if (avgHeapSize == 0) return 0;
				int depthOfAvgSize = 31 - Integer.numberOfLeadingZeros(avgHeapSize);
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
				long c = cost[node];
				while (i > 0) {
					int j = (i - 1) >> 1;
					int parent = heap[j];
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
				long c = cost[node];
				int half = size >> 1;
				while (i < half) {
					int child = (i << 1) + 1;
					child += child + 1 < size && cost[heap[child]] > cost[heap[child + 1]] ? 1 : 0;
					int childNode = heap[child];
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

	@SuppressWarnings("unused")
	private static final class FastScanner implements AutoCloseable {

		private static final int DEFAULT_BUFFER_SIZE = 65536;

		private final InputStream in;
		private final byte[] buffer;
		private int pos = 0;
		private int bufferLength = 0;

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
			int b = read();
			while (b <= 32) b = read();
			return b;
		}

		@Override
		public void close() throws IOException {
			if (in != System.in) in.close();
		}

		private int read() {
			if (pos >= bufferLength) {
				try {
					bufferLength = in.read(buffer, pos = 0, buffer.length);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				if (bufferLength <= 0) throw new RuntimeException(new EOFException());
			}
			return buffer[pos++] & 0xFF;
		}

		public int peek() {
			try {
				int b = skipSpaces();
				pos--;
				return b;
			} catch (RuntimeException e) {
				return 0;
			}
		}

		public boolean hasNext() {
			return peek() != 0;
		}

		public int nextInt() {
			int b = skipSpaces();
			boolean negative = false;
			if (b != '-') {
				// fall through
			} else {
				negative = true;
				b = read();
			}
			int result = 0;
			while ('0' <= b && b <= '9') {
				result = (result << 3) + (result << 1) + (b & 15);
				b = read();
			}
			return negative ? -result : result;
		}

		public long nextLong() {
			int b = skipSpaces();
			boolean negative = false;
			if (b != '-') {
				// fall through
			} else {
				negative = true;
				b = read();
			}
			long result = 0;
			while ('0' <= b && b <= '9') {
				result = (result << 3) + (result << 1) + (b & 15);
				b = read();
			}
			return negative ? -result : result;
		}

		public double nextDouble() {
			int b = skipSpaces();
			boolean negative = false;
			if (b != '-') {
				// fall through
			} else {
				negative = true;
				b = read();
			}
			long intPart = 0;
			while ('0' <= b && b <= '9') {
				intPart = (intPart << 3) + (intPart << 1) + (b & 15);
				b = read();
			}
			double result = intPart;
			if (b == '.') {
				b = read();
				double scale = 0.1;
				while ('0' <= b && b <= '9') {
					result += (b & 15) * scale;
					scale *= 0.1;
					b = read();
				}
			}
			return negative ? -result : result;
		}

		public char nextChar() {
			int b = skipSpaces();
			return (char) b;
		}

		public String next() {
			return nextStringBuilder().toString();
		}

		public StringBuilder nextStringBuilder() {
			final StringBuilder sb = new StringBuilder();
			int b = skipSpaces();
			while (b > 32) {
				sb.append((char) b);
				b = read();
			}
			return sb;
		}

		public String nextLine() {
			final StringBuilder sb = new StringBuilder();
			int b = read();
			while (b != 0 && b != '\n' && b != '\r') {
				sb.append((char) b);
				b = read();
			}
			if (b == '\r') {
				int c = read();
				if (c != '\n') pos--;
			}
			return sb.toString();
		}

		public BigInteger nextBigInteger() {
			return new BigInteger(next());
		}

		public BigDecimal nextBigDecimal() {
			return new BigDecimal(next());
		}
	}

	@SuppressWarnings("unused")
	private static final class FastPrinter implements AutoCloseable {

		private static final int MAX_INT_DIGITS = 11;
		private static final int MAX_LONG_DIGITS = 20;
		private static final long[] POW10 = {
				1, 10, 100, 1_000, 10_000, 100_000, 1_000_000, 10_000_000, 100_000_000,
				1_000_000_000, 10_000_000_000L, 100_000_000_000L, 1_000_000_000_000L,
				10_000_000_000_000L, 100_000_000_000_000L, 1_000_000_000_000_000L,
				10_000_000_000_000_000L, 100_000_000_000_000_000L, 1_000_000_000_000_000_000L
		};
		private static final int DEFAULT_BUFFER_SIZE = 65536;
		private static final byte[] DigitTens = {
				'0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
				'1', '1', '1', '1', '1', '1', '1', '1', '1', '1',
				'2', '2', '2', '2', '2', '2', '2', '2', '2', '2',
				'3', '3', '3', '3', '3', '3', '3', '3', '3', '3',
				'4', '4', '4', '4', '4', '4', '4', '4', '4', '4',
				'5', '5', '5', '5', '5', '5', '5', '5', '5', '5',
				'6', '6', '6', '6', '6', '6', '6', '6', '6', '6',
				'7', '7', '7', '7', '7', '7', '7', '7', '7', '7',
				'8', '8', '8', '8', '8', '8', '8', '8', '8', '8',
				'9', '9', '9', '9', '9', '9', '9', '9', '9', '9',
		};
		private static final byte[] DigitOnes = {
				'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
		};
		private static final byte[] TRUE_BYTES = {'Y', 'e', 's'};
		private static final byte[] FALSE_BYTES = {'N', 'o'};

		private final byte[] buffer;
		private final boolean autoFlush;
		private final int BUFFER_SIZE;
		private final OutputStream out;
		private int pos = 0;

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
			this.BUFFER_SIZE = max(bufferSize, 64);
			this.buffer = new byte[BUFFER_SIZE];
			this.autoFlush = autoFlush;
		}

		@Override
		public void close() throws IOException {
			flush();
			if (out != System.out) out.close();
		}

		public void flush() {
			try {
				if (pos > 0) out.write(buffer, 0, pos);
				out.flush();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			pos = 0;
		}

		public FastPrinter println() {
			ensureBufferSpace(1);
			buffer[pos++] = '\n';
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter println(final int i) {
			ensureBufferSpace(MAX_INT_DIGITS + 1);
			fillBuffer(i);
			buffer[pos++] = '\n';
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter println(final long l) {
			ensureBufferSpace(MAX_LONG_DIGITS + 1);
			fillBuffer(l);
			buffer[pos++] = '\n';
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter println(final double d) {
			return println(Double.toString(d));
		}

		public FastPrinter println(final char c) {
			ensureBufferSpace(2);
			buffer[pos++] = (byte) c;
			buffer[pos++] = '\n';
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter println(final boolean b) {
			ensureBufferSpace(4);
			fillBuffer(b);
			buffer[pos++] = '\n';
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter println(final String s) {
			final byte[] src = s.getBytes();
			fillBuffer(src, s.length());
			ensureBufferSpace(1);
			buffer[pos++] = '\n';
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter println(final StringBuilder s) {
			final byte[] src = s.toString().getBytes();
			final int len = s.length();
			fillBuffer(src, len);
			ensureBufferSpace(1);
			buffer[pos++] = '\n';
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter println(final BigInteger bi) {
			return println(bi.toString());
		}

		public FastPrinter println(final BigDecimal bd) {
			return println(bd.toString());
		}

		public FastPrinter print(final int i) {
			ensureBufferSpace(MAX_INT_DIGITS);
			fillBuffer(i);
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter print(final long l) {
			ensureBufferSpace(MAX_LONG_DIGITS);
			fillBuffer(l);
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter print(final double d) {
			return print(Double.toString(d));
		}

		public FastPrinter print(final char c) {
			ensureBufferSpace(1);
			buffer[pos++] = (byte) c;
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter print(final boolean b) {
			ensureBufferSpace(3);
			fillBuffer(b);
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter print(final String s) {
			final byte[] src = s.getBytes();
			fillBuffer(src, s.length());
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter print(final StringBuilder s) {
			final byte[] src = s.toString().getBytes();
			final int len = s.length();
			fillBuffer(src, len);
			if (autoFlush) flush();
			return this;
		}

		public FastPrinter print(final BigInteger bi) {
			return print(bi.toString());
		}

		public FastPrinter print(final BigDecimal bd) {
			return print(bd.toString());
		}

		public FastPrinter printf(final String format, final Object... args) {
			return print(String.format(format, args));
		}

		public FastPrinter printf(final Locale locale, final String format, final Object... args) {
			return print(String.format(locale, format, args));
		}

		private void ensureBufferSpace(final int size) {
			if (pos + size > buffer.length) {
				try {
					out.write(buffer, 0, pos);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				pos = 0;
			}
		}

		private void fillBuffer(final byte[] src, final int len) {
			for (int i = 0; i < len; ) {
				ensureBufferSpace(1);
				int limit = min(BUFFER_SIZE - pos, len - i);
				System.arraycopy(src, i, buffer, pos, limit);
				pos += limit;
				i += limit;
			}
		}

		private void fillBuffer(final boolean b) {
			if (b) {
				System.arraycopy(TRUE_BYTES, 0, buffer, pos, 3);
				pos += 3;
			} else {
				System.arraycopy(FALSE_BYTES, 0, buffer, pos, 2);
				pos += 2;
			}
		}

		private void fillBuffer(int i) {
			if (i >= 0) i = -i;
			else buffer[pos++] = '-';
			int quotient, remainder;
			final int numOfDigits = countDigits(i);
			int writePos = pos + numOfDigits;
			while (i <= -100) {
				quotient = i / 100;
				remainder = (quotient << 6) + (quotient << 5) + (quotient << 2) - i;
				buffer[--writePos] = DigitOnes[remainder];
				buffer[--writePos] = DigitTens[remainder];
				i = quotient;
			}
			quotient = i / 10;
			remainder = (quotient << 3) + (quotient << 1) - i;
			buffer[--writePos] = (byte) ('0' + remainder);
			if (quotient < 0) buffer[--writePos] = (byte) ('0' - quotient);
			pos += numOfDigits;
		}

		private void fillBuffer(long l) {
			if (l >= 0) l = -l;
			else buffer[pos++] = '-';
			long quotient;
			int remainder;
			final int numOfDigits = countDigits(l);
			int writePos = pos + numOfDigits;
			while (l <= -100) {
				quotient = l / 100;
				remainder = (int) ((quotient << 6) + (quotient << 5) + (quotient << 2) - l);
				buffer[--writePos] = DigitOnes[remainder];
				buffer[--writePos] = DigitTens[remainder];
				l = quotient;
			}
			quotient = l / 10;
			remainder = (int) ((quotient << 3) + (quotient << 1) - l);
			buffer[--writePos] = (byte) ('0' + remainder);
			if (quotient < 0) buffer[--writePos] = (byte) ('0' - quotient);
			pos += numOfDigits;
		}

		private int countDigits(int i) {
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

		private int countDigits(long l) {
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
	}
}
