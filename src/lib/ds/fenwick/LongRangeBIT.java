package lib.ds.fenwick;

import lib.ds.LongCollection;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.function.IntToLongFunction;
import java.util.function.LongBinaryOperator;

/**
 * 競技プログラミング向け Range Update Range Query (区間加算・区間和取得) を提供する long 型特化 BIT。
 * <p>
 * 2つの BIT を内部で管理することで、区間加算と区間和の計算を共に O(log N) で行う。
 */
@SuppressWarnings("unused")
public final class LongRangeBIT implements LongCollection {
	public final int n;
	private final long[] bit1, bit2;

	/**
	 * サイズ n の BIT を構築する。初期値はすべて 0。
	 *
	 * @param n 要素数
	 */
	public LongRangeBIT(final int n) {
		this.n = n;
		bit1 = new long[n + 1];
		bit2 = new long[n + 1];
	}

	/**
	 * 初期値関数を用いて BIT を O(N) で構築する。
	 *
	 * @param n    要素数
	 * @param init 初期値関数 (index -> value)
	 */
	public LongRangeBIT(final int n, final IntToLongFunction init) {
		this(n);
		setAll(init);
	}

	public void fill(final long val) {
		bit1[0] = bit2[0] = 0;
		long prev = 0;
		for (int i = 0; i < n; i++) {
			long diff = val - prev;
			bit1[i + 1] = diff;
			bit2[i + 1] = 0;
			prev = val;
		}
		for (int i = 1; i <= n; i++) {
			int j = i + (i & -i);
			if (j <= n) {
				bit1[j] += bit1[i];
				bit2[j] += bit2[i];
			}
		}
	}

	/**
	 * BIT の全要素を再構築する。
	 *
	 * @param init 初期値関数
	 */
	public void setAll(final IntToLongFunction init) {
		bit1[0] = bit2[0] = 0;
		long prev = 0;
		for (int i = 0; i < n; i++) {
			long cur = init.applyAsLong(i);
			long diff = cur - prev;
			bit1[i + 1] = diff;
			bit2[i + 1] = diff * i;
			prev = cur;
		}
		for (int i = 1; i <= n; i++) {
			int j = i + (i & -i);
			if (j <= n) {
				bit1[j] += bit1[i];
				bit2[j] += bit2[i];
			}
		}
	}

	/**
	 * インデックス i (0-indexed) の現在の値を取得する。
	 *
	 * @param i インデックス
	 * @return 現在の値
	 */
	public long get(final int i) {
		return sum(bit1, i + 1);
	}

	/**
	 * インデックス i (0-indexed) の要素を v に更新する。
	 *
	 * @param i インデックス
	 * @param v 更新後の値
	 */
	public void set(final int i, final long v) {
		final long delta = v - get(i);
		add(bit1, i, delta);
		add(bit1, i + 1, -delta);
		add(bit2, i, delta * i);
		add(bit2, i + 1, -delta * (i + 1));
	}

	/**
	 * 閉区間 [l, r] に v を加算する。
	 *
	 * @param l 左端の境界 (includes)
	 * @param r 右端の境界 (includes)
	 * @param v 加算する値
	 */
	public void add(final int l, final int r, final long v) {
		if (l > r) return;
		add(bit1, l, v);
		add(bit1, r + 1, -v);
		add(bit2, l, v * l);
		add(bit2, r + 1, -v * (r + 1));
	}

	/**
	 * インデックス i (0-indexed) の要素に v を加算する。
	 *
	 * @param i インデックス
	 * @param v 加算する値
	 */
	public void add(final int i, final long v) {
		add(i, i, v);
	}

	public void multiply(final int i, final long a) {
		add(i, i, get(i) * (a - 1));
	}

	public void apply(final int i, final long a, final long b) {
		add(i, i, get(i) * (a - 1) + b);
	}

	public void apply(final int i, final long v, final LongBinaryOperator op) {
		final long ai = get(i);
		add(i, i, op.applyAsLong(ai, v) - ai);
	}

	/**
	 * 閉区間 [0, r] の和を計算する。
	 *
	 * @param r 右端の境界 (includes)
	 * @return [0, r] の和
	 */
	public long sum(final int r) {
		if (r < 0) return 0;
		return sum(bit1, r + 1) * (r + 1) - sum(bit2, r + 1);
	}

	/**
	 * 閉区間 [l, r] の和を計算する。
	 *
	 * @param l 左端の境界 (includes)
	 * @param r 右端の境界 (includes)
	 * @return [l, r] の和
	 */
	public long sum(final int l, final int r) {
		if (l > r) return 0;
		return sum(r) - sum(l - 1);
	}

	public long sumAll() {
		return sum(n - 1);
	}

	/**
	 * BIT のサイズを返す。
	 *
	 * @return サイズ
	 */
	public int size() {
		return n;
	}

	public PrimitiveIterator.OfLong iterator() {
		return new PrimitiveIterator.OfLong() {
			private int i = 0;

			public boolean hasNext() {
				return i < n;
			}

			public long nextLong() {
				if (!hasNext()) throw new NoSuchElementException();
				return get(i++);
			}
		};
	}

	public String toString() {
		final StringBuilder s = new StringBuilder();
		s.append(get(0));
		for (int i = 1; i < n; i++) s.append(' ').append(get(i));
		return s.toString();
	}

	private void add(final long[] bit, int i, final long v) {
		for (i++; i <= n; i += i & -i) bit[i] += v;
	}

	private long sum(final long[] bit, int r) {
		long s = 0;
		for (; r > 0; r -= r & -r) s += bit[r];
		return s;
	}
}
