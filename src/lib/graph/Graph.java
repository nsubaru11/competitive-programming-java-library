package lib.graph;

import static java.util.Arrays.*;

import java.util.function.*;

/**
 * 前方スター形式の隣接リストを保持するグラフの基底クラス。
 * <p>
 * 辺の終点・重み・次の辺番号をプリミティブ配列に保持します。
 * 無向グラフでは、1本の無向辺を向きの異なる2本の内部辺として保持します。
 * 配列の容量は構築時に固定され、辺追加時の自動拡張は行いません。
 * <p>
 * {@link #m} は現在の辺数ではなく、辺数引数を持たない {@link #setAll(IntSupplier, IntSupplier)}
 * および {@link #setAll(IntSupplier, IntSupplier, LongSupplier)} が読み込む入力辺数です。
 * 現在の論理辺数は {@link #edgeCount()} で取得します。
 */
public abstract class Graph {
	public final int n, m;
	protected final int[] dest, next, first;
	protected final long[] cost;
	protected int edgePtr = 0;

	/**
	 * グラフの内部配列を確保します。
	 *
	 * @param n            頂点数
	 * @param m            {@code setAll} が読み込む入力辺数
	 * @param edgeCapacity 内部辺の最大数。有向グラフでは論理辺数、無向グラフではその2倍が必要
	 */
	protected Graph(final int n, final int m, final int edgeCapacity) {
		this.n = n;
		this.m = m;
		dest = new int[edgeCapacity];
		next = new int[edgeCapacity];
		first = new int[n];
		fill(first, -1);
		cost = new long[edgeCapacity];
	}

	/**
	 * 現在の辺をすべて削除し、重み1の辺を {@link #m} 本読み込みます。
	 *
	 * @param u 辺の始点を返すサプライヤ
	 * @param v 辺の終点を返すサプライヤ
	 */
	public void setAll(final IntSupplier u, final IntSupplier v) {
		clear();
		addAll(m, u, v);
	}

	/**
	 * 現在の辺をすべて削除し、重み付き辺を {@link #m} 本読み込みます。
	 *
	 * @param u 辺の始点を返すサプライヤ
	 * @param v 辺の終点を返すサプライヤ
	 * @param c 辺の重みを返すサプライヤ
	 */
	public void setAll(final IntSupplier u, final IntSupplier v, final LongSupplier c) {
		clear();
		addAll(m, u, v, c);
	}

	/**
	 * 重み1の辺を1本追加します。
	 *
	 * @param u 辺の始点
	 * @param v 辺の終点
	 */
	public void add(final int u, final int v) {
		add(u, v, 1);
	}

	/**
	 * 重み付き辺を1本追加します。
	 *
	 * @param u 辺の始点
	 * @param v 辺の終点
	 * @param c 辺の重み
	 */
	public abstract void add(final int u, final int v, final long c);

	/**
	 * 前方スター形式の内部辺を1本追加します。
	 *
	 * @param u 内部辺の始点
	 * @param v 内部辺の終点
	 * @param c 内部辺の重み
	 */
	protected final void addEdge(final int u, final int v, final long c) {
		dest[edgePtr] = v;
		next[edgePtr] = first[u];
		cost[edgePtr] = c;
		first[u] = edgePtr++;
	}

	/**
	 * 現在の辺に続けて、重み1の辺を {@code count} 本追加します。
	 *
	 * @param count 追加する辺数
	 * @param u     辺の始点を返すサプライヤ
	 * @param v     辺の終点を返すサプライヤ
	 */
	public void addAll(int count, final IntSupplier u, final IntSupplier v) {
		while (count-- > 0) add(u.getAsInt(), v.getAsInt(), 1);
	}

	/**
	 * 現在の辺に続けて、重み付き辺を {@code count} 本追加します。
	 *
	 * @param count 追加する辺数
	 * @param u     辺の始点を返すサプライヤ
	 * @param v     辺の終点を返すサプライヤ
	 * @param c     辺の重みを返すサプライヤ
	 */
	public void addAll(int count, final IntSupplier u, final IntSupplier v, final LongSupplier c) {
		while (count-- > 0) add(u.getAsInt(), v.getAsInt(), c.getAsLong());
	}

	/**
	 * 現在保持している辺をすべて削除します。
	 */
	public void clear() {
		if (edgePtr == 0) return;
		fill(first, -1);
		edgePtr = 0;
	}

	/**
	 * 現在保持している論理辺数を返します。
	 *
	 * @return 論理辺数
	 */
	public abstract int edgeCount();

	/**
	 * 指定した論理辺の重みを返します。
	 *
	 * @param e 辺ID
	 * @return 辺の重み
	 */
	public abstract long cost(final int e);

	/**
	 * 指定した頂点に接続する辺数を返します。
	 * 有向グラフでは入次数と出次数の合計です。
	 *
	 * @param v 頂点
	 * @return 次数
	 */
	public abstract int degree(final int v);

	/**
	 * 指定した頂点から隣接する頂点を返します。
	 * 有向グラフでは出辺の終点のみを返します。
	 *
	 * @param u 頂点
	 * @return 隣接頂点の配列
	 */
	public abstract int[] adj(final int u);

	/**
	 * 指定した頂点に接続する辺IDを返します。
	 * 有向グラフでは出辺のIDのみを返します。
	 *
	 * @param u 頂点
	 * @return 辺IDの配列
	 */
	public abstract int[] adjEdgeIds(final int u);
}
