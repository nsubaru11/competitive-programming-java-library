package lib.ds.fenwick;

import java.util.*;
import java.util.function.*;

/**
 * 競技プログラミング向け Binary Indexed Tree（Fenwick Tree）
 * <p>
 * 1点更新と半開区間 [l, r) の集計結果（演算適用結果）を O(log N) で提供。
 */
@SuppressWarnings({"unused", "unchecked"})
public final class BIT<T> implements Iterable<T> {
	public final int size;
	public final T identity;
	private final T[] raw, tree;
	private final BinaryOperator<T> op, inv;

	/**
	 * サイズ size の BIT を構築する。
	 *
	 * @param size     要素数
	 * @param op       演算 (例: (a, b) -> a + b)
	 * @param inv      逆演算 (例: (a, b) -> a - b)
	 * @param identity 単位元 (例: 0)
	 */
	public BIT(final int size, final BinaryOperator<T> op, final BinaryOperator<T> inv, final T identity) {
		this.size = size;
		raw = (T[]) new Object[size];
		tree = (T[]) new Object[size + 1];
		if (identity != null) {
			Arrays.fill(raw, identity);
			Arrays.fill(tree, identity);
		}
		this.op = op;
		this.inv = inv;
		this.identity = identity;
	}

	/**
	 * 初期値関数を用いて BIT を O(N) で構築する。
	 *
	 * @param size     要素数
	 * @param op       演算
	 * @param inv      逆演算
	 * @param init     初期値関数 (index -> value)
	 * @param identity 単位元
	 */
	public BIT(final int size, final BinaryOperator<T> op, final BinaryOperator<T> inv, final IntFunction<T> init, final T identity) {
		this.size = size;
		raw = (T[]) new Object[size];
		tree = (T[]) new Object[size + 1];
		this.op = op;
		this.inv = inv;
		this.identity = identity;
		setAll(init);
	}

	public int size() {
		return size;
	}

	/**
	 * インデックス i (0-indexed) の現在の値を取得する。
	 *
	 * @param i インデックス
	 * @return 現在の値
	 */
	public T get(final int i) {
		return raw[i];
	}

	/**
	 * インデックス i (0-indexed) の要素を v に更新する。
	 * 逆演算 inv が提供されている必要がある。
	 *
	 * @param i インデックス
	 * @param v 更新後の値
	 * @return 更新前の値
	 */
	public T set(final int i, final T v) {
		final T old = raw[i];
		apply(i, inv.apply(v, old));
		return old;
	}

	/**
	 * インデックス i (0-indexed) の要素に v を作用させる。
	 *
	 * @param i インデックス
	 * @param v 作用させる値
	 */
	public T apply(final int i, final T v) {
		raw[i] = op.apply(raw[i], v);
		for (int cur = i + 1; cur <= size; cur += cur & -cur) tree[cur] = op.apply(tree[cur], v);
		return raw[i];
	}

	public void fill(final T v) {
		tree[0] = identity;
		for (int i = 0; i < size; i++) tree[i + 1] = raw[i] = v;
		for (int i = 1; i <= size; i++) {
			final int j = i + (i & -i);
			if (j <= size) tree[j] = op.apply(tree[j], tree[i]);
		}
	}

	public void setAll(final IntFunction<T> init) {
		tree[0] = identity;
		for (int i = 0; i < size; i++) tree[i + 1] = raw[i] = init.apply(i);
		for (int i = 1; i <= size; i++) {
			final int j = i + (i & -i);
			if (j <= size) tree[j] = op.apply(tree[j], tree[i]);
		}
	}

	/**
	 * 半開区間 [0, r) の集計結果を計算する。r = 0 は空区間として扱う。
	 *
	 * @param r 右端の境界 (excludes)
	 * @return [0, r) の集計結果
	 */
	public T query(final int r) {
		return prefixQuery(Objects.checkIndex(r, size + 1));
	}

	/**
	 * 半開区間 [l, r) の集計結果を計算する。l >= r は空区間として扱う。
	 * 逆演算 inv が提供されている必要がある。
	 *
	 * @param l 左端の境界 (includes)
	 * @param r 右端の境界 (excludes)
	 * @return [l, r) の集計結果
	 */
	public T query(final int l, final int r) {
		if (l >= r) return identity;
		Objects.checkFromToIndex(l, r, size);
		return inv.apply(prefixQuery(r), prefixQuery(l));
	}

	public T queryAll() {
		return prefixQuery(size);
	}

	public Iterator<T> iterator() {
		return new Iterator<>() {
			private int i = 0;

			public boolean hasNext() {
				return i < size;
			}

			public T next() {
				if (!hasNext()) throw new NoSuchElementException();
				return raw[i++];
			}
		};
	}

	public String toString() {
		if (size == 0) return "";
		final StringBuilder sb = new StringBuilder();
		sb.append(raw[0]);
		for (int i = 1; i < size; i++) sb.append(' ').append(raw[i]);
		return sb.toString();
	}

	private T prefixQuery(int r) {
		T res = identity;
		for (; r > 0; r -= r & -r) res = op.apply(res, tree[r]);
		return res;
	}
}
