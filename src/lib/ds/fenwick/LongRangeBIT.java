package lib.ds.fenwick;

import java.util.*;
import java.util.function.*;

import lib.ds.*;

/**
 * 競技プログラミング向け Range Update Range Query (区間加算・区間和取得) を提供する long 型特化 BIT。
 * <p>
 * 2つの BIT を内部で管理することで、区間加算と区間和の計算を共に O(log N) で行う。
 */
@SuppressWarnings("unused")
public final class LongRangeBIT implements LongCollection {
	public final int size;
	private final long[] bit1, bit2;
	private long total;

	/**
	 * サイズ size の BIT を構築する。初期値はすべて 0。
	 *
	 * @param size 要素数
	 */
	public LongRangeBIT(final int size) {
		this.size = size;
		bit1 = new long[size + 1];
		bit2 = new long[size + 1];
	}

	/**
	 * 初期値関数を用いて BIT を O(N) で構築する。
	 *
	 * @param size 要素数
	 * @param init 初期値関数 (index -> value)
	 */
	public LongRangeBIT(final int size, final IntToLongFunction init) {
		this(size);
		setAll(init);
	}

	/**
	 * BIT のサイズを返す。
	 *
	 * @return サイズ
	 */
	public int size() {
		return size;
	}

	/**
	 * インデックス i (0-indexed) の現在の値を取得する。
	 *
	 * @param i インデックス
	 * @return 現在の値
	 */
	public long get(final int i) {
		return prefixSum(bit1, Objects.checkIndex(i, size) + 1);
	}

	/**
	 * インデックス i (0-indexed) の要素を v に更新する。
	 *
	 * @param i インデックス
	 * @param v 更新後の値
	 * @return 更新前の値
	 */
	public long set(final int i, final long v) {
		final long old = get(i);
		addRange(i, i, v - old);
		return old;
	}

	/**
	 * インデックス i (0-indexed) の要素に d を加算する。
	 *
	 * @param i インデックス
	 * @param d 加算する値
	 */
	public void add(final int i, final long d) {
		Objects.checkIndex(i, size);
		addRange(i, i, d);
	}

	/**
	 * 閉区間 [l, r] に d を加算する。l > r の場合は何もしない。
	 *
	 * @param l 左端の境界 (includes)
	 * @param r 右端の境界 (includes)
	 * @param d 加算する値
	 */
	public void add(final int l, final int r, final long d) {
		if (l > r) return;
		Objects.checkFromToIndex(l, r + 1, size);
		addRange(l, r, d);
	}

	public void multiply(final int i, final long m) {
		addRange(i, i, get(i) * (m - 1));
	}

	/**
	 * 指定位置を {@code 現在値 * m + d} に更新する。
	 */
	public void affine(final int i, final long m, final long d) {
		addRange(i, i, get(i) * (m - 1) + d);
	}

	public void apply(final int i, final long x, final LongBinaryOperator f) {
		final long old = get(i);
		addRange(i, i, f.applyAsLong(old, x) - old);
	}

	public void fill(final long v) {
		total = size * v;
		bit1[0] = bit2[0] = 0;
		long prev = 0;
		for (int i = 0; i < size; i++) {
			final long diff = v - prev;
			bit1[i + 1] = diff;
			bit2[i + 1] = 0;
			prev = v;
		}
		for (int i = 1; i <= size; i++) {
			final int j = i + (i & -i);
			if (j <= size) {
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
		total = 0;
		bit1[0] = bit2[0] = 0;
		long prev = 0;
		for (int i = 0; i < size; i++) {
			final long cur = init.applyAsLong(i), diff = cur - prev;
			total += cur;
			bit1[i + 1] = diff;
			bit2[i + 1] = diff * i;
			prev = cur;
		}
		for (int i = 1; i <= size; i++) {
			final int j = i + (i & -i);
			if (j <= size) {
				bit1[j] += bit1[i];
				bit2[j] += bit2[i];
			}
		}
	}

	/**
	 * 閉区間 [0, r] の和を計算する。r = -1 は空区間として扱う。
	 *
	 * @param r 右端の境界 (includes)
	 * @return [0, r] の和
	 */
	public long sum(final int r) {
		return prefixSum(Objects.checkIndex(r + 1, size + 1));
	}

	/**
	 * 閉区間 [l, r] の和を計算する。l > r は空区間として扱う。
	 *
	 * @param l 左端の境界 (includes)
	 * @param r 右端の境界 (includes)
	 * @return [l, r] の和
	 */
	public long sum(final int l, final int r) {
		if (l > r) return 0;
		Objects.checkFromToIndex(l, r + 1, size);
		return prefixSum(r + 1) - prefixSum(l);
	}

	/**
	 * 全要素の和を O(1) で返す。
	 *
	 * @return 全要素の和
	 */
	public long sumAll() {
		return total;
	}

	public PrimitiveIterator.OfLong iterator() {
		return new PrimitiveIterator.OfLong() {
			private int i = 0;

			public boolean hasNext() {
				return i < size;
			}

			public long nextLong() {
				if (!hasNext()) throw new NoSuchElementException();
				return get(i++);
			}
		};
	}

	public String toString() {
		if (isEmpty()) return "";
		final StringBuilder sb = new StringBuilder();
		sb.append(get(0));
		for (int i = 1; i < size; i++) sb.append(' ').append(get(i));
		return sb.toString();
	}

	private void addRange(final int l, final int r, final long d) {
		total += (r - l + 1L) * d;
		add(bit1, l, d);
		add(bit1, r + 1, -d);
		add(bit2, l, d * l);
		add(bit2, r + 1, -d * (r + 1));
	}

	private void add(final long[] bit, int i, final long v) {
		for (i++; i <= size; i += i & -i) bit[i] += v;
	}

	private long prefixSum(final long[] bit, int r) {
		long s = 0;
		for (; r > 0; r -= r & -r) s += bit[r];
		return s;
	}

	private long prefixSum(final int r) {
		return prefixSum(bit1, r) * r - prefixSum(bit2, r);
	}
}
