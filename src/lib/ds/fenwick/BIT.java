package lib.ds.fenwick;

import java.util.*;
import java.util.function.*;

/**
 * 演算・逆演算・単位元を指定する汎用 Binary Indexed Tree (Fenwick Tree)。点更新と区間集計を O(log n) で行う。
 * 添字は0始まりとする。{@code query(r)} は半開区間 {@code [0, r)}、{@code query(l, r)} は半開区間 {@code [l, r)} の集計結果を返し、
 * {@code l >= r} は空区間として単位元を返す。範囲外の区間では IndexOutOfBoundsException を投げる。
 * operator は結合則・可換性を満たし、identity はその単位元とする。set・apply と {@code query(l, r)} には operator の逆演算 inverse が必要である。
 * set は更新前の値、add は {@code operator(現在値, v)}、apply は {@code f(現在値, x)} へ更新した後の値を返す。
 * 配列・コレクション・コピーからの構築は内部配列を共有せず、要素オブジェクトは共有する。
 */
@SuppressWarnings("unchecked")
public final class BIT<T> implements Iterable<T> {
	public final int size;
	public final T identity;
	private final T[] raw, tree;
	private final BinaryOperator<T> operator, inverse;

	public BIT(final int n, final BinaryOperator<T> operator, final BinaryOperator<T> inverse, final T identity) {
		size = n;
		raw = (T[]) new Object[n];
		tree = (T[]) new Object[n + 1];
		if (identity != null) {
			Arrays.fill(raw, identity);
			Arrays.fill(tree, identity);
		}
		this.operator = operator;
		this.inverse = inverse;
		this.identity = identity;
	}

	public BIT(final T[] a, final BinaryOperator<T> operator, final BinaryOperator<T> inverse, final T identity) {
		size = a.length;
		raw = a.clone();
		tree = (T[]) new Object[size + 1];
		System.arraycopy(a, 0, tree, 1, size);
		this.operator = operator;
		this.inverse = inverse;
		this.identity = identity;
		build();
	}

	public BIT(final Collection<? extends T> a, final BinaryOperator<T> operator, final BinaryOperator<T> inverse, final T identity) {
		size = a.size();
		raw = (T[]) a.toArray();
		tree = (T[]) new Object[size + 1];
		System.arraycopy(raw, 0, tree, 1, size);
		this.operator = operator;
		this.inverse = inverse;
		this.identity = identity;
		build();
	}

	public BIT(final BIT<T> other) {
		size = other.size;
		raw = other.raw.clone();
		tree = other.tree.clone();
		operator = other.operator;
		inverse = other.inverse;
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
		add(i, inverse.apply(v, old));
		return old;
	}

	public T add(final int i, final T v) {
		raw[i] = operator.apply(raw[i], v);
		for (int cur = i + 1; cur <= size; cur += cur & -cur) tree[cur] = operator.apply(tree[cur], v);
		return raw[i];
	}

	public T apply(final int i, final T x, final BinaryOperator<T> f) {
		return add(i, inverse.apply(f.apply(raw[i], x), raw[i]));
	}

	public BIT<T> fill(final T v) {
		for (int i = 0; i < size; i++) tree[i + 1] = raw[i] = v;
		build();
		return this;
	}

	public BIT<T> setAll(final IntFunction<T> init) {
		for (int i = 0; i < size; i++) tree[i + 1] = raw[i] = init.apply(i);
		build();
		return this;
	}

	public T query(final int r) {
		return prefixQuery(Objects.checkIndex(r, size + 1));
	}

	public T query(final int l, final int r) {
		if (l >= r) return identity;
		Objects.checkFromToIndex(l, r, size);
		return inverse.apply(prefixQuery(r), prefixQuery(l));
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
			if (j <= size) tree[j] = operator.apply(tree[j], tree[i]);
		}
	}

	private T prefixQuery(int r) {
		T res = identity;
		for (; r > 0; r -= r & -r) res = operator.apply(res, tree[r]);
		return res;
	}
}
