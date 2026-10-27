package lib.ds.fenwick;

import java.util.*;
import java.util.function.*;

/**
 * 演算・逆演算・単位元を指定する汎用 Binary Indexed Tree (Fenwick Tree)。点更新と区間集計を O(log n) で行う。
 * 添字は0始まりとする。{@code query(r)} は半開区間 {@code [0, r)}、{@code query(l, r)} は半開区間 {@code [l, r)} の集計結果を返し、
 * {@code l >= r} は空区間として単位元を返す。範囲外の区間では IndexOutOfBoundsException を投げる。
 * op は結合則・可換性を満たし、identity はその単位元とする。set と {@code query(l, r)} には op の逆演算 inv が必要である。
 * set は更新前の値、apply は op を作用させた更新後の値を返す。配列とコピーからの構築は内部配列を共有せず、要素オブジェクトは共有する。
 */
@SuppressWarnings("unchecked")
public final class BIT<T> implements Iterable<T> {
	public final int size;
	public final T identity;
	private final T[] raw, tree;
	private final BinaryOperator<T> op, inv;

	public BIT(final int n, final BinaryOperator<T> op, final BinaryOperator<T> inv, final T identity) {
		size = n;
		raw = (T[]) new Object[n];
		tree = (T[]) new Object[n + 1];
		if (identity != null) {
			Arrays.fill(raw, identity);
			Arrays.fill(tree, identity);
		}
		this.op = op;
		this.inv = inv;
		this.identity = identity;
	}

	public BIT(final int n, final BinaryOperator<T> op, final BinaryOperator<T> inv, final IntFunction<T> init, final T identity) {
		size = n;
		raw = (T[]) new Object[n];
		tree = (T[]) new Object[n + 1];
		this.op = op;
		this.inv = inv;
		this.identity = identity;
		setAll(init);
	}

	public BIT(final T[] a, final BinaryOperator<T> op, final BinaryOperator<T> inv, final T identity) {
		size = a.length;
		raw = a.clone();
		tree = (T[]) new Object[size + 1];
		System.arraycopy(a, 0, tree, 1, size);
		this.op = op;
		this.inv = inv;
		this.identity = identity;
		build();
	}

	public BIT(final BIT<T> other) {
		size = other.size;
		raw = other.raw.clone();
		tree = other.tree.clone();
		op = other.op;
		inv = other.inv;
		identity = other.identity;
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public T get(final int i) {
		return raw[i];
	}

	public T set(final int i, final T v) {
		final T old = raw[i];
		apply(i, inv.apply(v, old));
		return old;
	}

	public T apply(final int i, final T v) {
		raw[i] = op.apply(raw[i], v);
		for (int cur = i + 1; cur <= size; cur += cur & -cur) tree[cur] = op.apply(tree[cur], v);
		return raw[i];
	}

	public void fill(final T v) {
		for (int i = 0; i < size; i++) tree[i + 1] = raw[i] = v;
		build();
	}

	public void setAll(final IntFunction<T> init) {
		for (int i = 0; i < size; i++) tree[i + 1] = raw[i] = init.apply(i);
		build();
	}

	public T query(final int r) {
		return prefixQuery(Objects.checkIndex(r, size + 1));
	}

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
		if (isEmpty()) return "";
		final StringBuilder sb = new StringBuilder();
		sb.append(raw[0]);
		for (int i = 1; i < size; i++) sb.append(' ').append(raw[i]);
		return sb.toString();
	}

	private void build() {
		for (int i = 1; i <= size; i++) {
			final int j = i + (i & -i);
			if (j <= size) tree[j] = op.apply(tree[j], tree[i]);
		}
	}

	private T prefixQuery(int r) {
		T res = identity;
		for (; r > 0; r -= r & -r) res = op.apply(res, tree[r]);
		return res;
	}
}
