package lib.ds.segmenttree;

import java.util.*;
import java.util.function.*;

/**
 * 結合則を満たす演算と単位元を指定する汎用セグメント木。点更新・区間集計・二分探索を O(log n) で行う。
 * 添字は0始まりとする。{@code query(l, r)} は半開区間 {@code [l, r)} の集計結果を返し、{@code l >= r} は空区間として単位元を返す。
 * 範囲外の添字・区間では IndexOutOfBoundsException を投げる。set は更新前の値、apply は {@code f(現在値, x)} へ更新した後の値を返す。
 * maxRight は {@code tester(query(l, r))} が真となる最大の r、minLeft は {@code tester(query(l, r))} が真となる最小の l を返す。
 * tester は単位元に対して真となり、区間を広げると真から偽へ一度だけ変わるものとする。
 * identity が null の場合、operator は null を受け付けること。配列とコピーからの構築は内部配列を共有せず、要素オブジェクトは共有する。
 */
@SuppressWarnings("unchecked")
public final class SegmentTree<T> implements Iterable<T> {
	private final int n, size;
	private final T identity;
	private final BinaryOperator<T> operator;
	private final T[] tree;

	public SegmentTree(final int n, final BinaryOperator<T> operator, final T identity) {
		this.n = n;
		size = n <= 1 ? 1 : Integer.highestOneBit(n - 1) << 1;
		this.identity = identity;
		this.operator = operator;
		tree = (T[]) new Object[size << 1];
		if (identity != null) Arrays.fill(tree, identity);
	}

	public SegmentTree(final T[] a, final BinaryOperator<T> operator, final T identity) {
		this(a.length, operator, identity);
		System.arraycopy(a, 0, tree, size, n);
		build();
	}

	public SegmentTree(final SegmentTree<T> other) {
		n = other.n;
		size = other.size;
		identity = other.identity;
		operator = other.operator;
		tree = other.tree.clone();
	}

	public int size() {
		return n;
	}

	public boolean isEmpty() {
		return n == 0;
	}

	public T get(final int i) {
		return tree[size + Objects.checkIndex(i, n)];
	}

	public T set(final int i, final T v) {
		final int idx = size + Objects.checkIndex(i, n);
		final T prev = tree[idx];
		update(idx, v);
		return prev;
	}

	public T apply(final int i, final T x, final BinaryOperator<T> f) {
		final int idx = size + Objects.checkIndex(i, n);
		return update(idx, f.apply(tree[idx], x));
	}

	public SegmentTree<T> fill(final T v) {
		Arrays.fill(tree, size, size + n, v);
		build();
		return this;
	}

	public SegmentTree<T> setAll(final IntFunction<T> init) {
		for (int i = 0, idx = size; i < n; i++, idx++) tree[idx] = init.apply(i);
		build();
		return this;
	}

	public T query(final int l, final int r) {
		if (l >= r) return identity;
		Objects.checkFromToIndex(l, r, n);
		T sml = identity, smr = identity;
		for (int cl = l + size, cr = r + size; cl < cr; cl >>= 1, cr >>= 1) {
			if ((cl & 1) == 1) sml = operator.apply(sml, tree[cl++]);
			if ((cr & 1) == 1) smr = operator.apply(tree[--cr], smr);
		}
		return operator.apply(sml, smr);
	}

	public T queryAll() {
		return tree[1];
	}

	public int maxRight(final int l, final Predicate<T> tester) {
		Objects.checkIndex(l, n + 1);
		if (l == n) return n;
		T ans = identity;
		int cl = l + size;
		do {
			cl >>= Integer.numberOfTrailingZeros(cl);
			T combined = operator.apply(ans, tree[cl]);
			if (!tester.test(combined)) {
				while (cl < size) {
					cl <<= 1;
					combined = operator.apply(ans, tree[cl]);
					if (tester.test(combined)) {
						ans = combined;
						cl++;
					}
				}
				return cl - size;
			}
			ans = combined;
			cl++;
		} while ((cl & -cl) != cl);
		return n;
	}

	public int minLeft(final int r, final Predicate<T> tester) {
		Objects.checkIndex(r, n + 1);
		if (r == 0) return 0;
		T ans = identity;
		int cr = r + size;
		do {
			cr--;
			while (cr > 1 && (cr & 1) == 1) cr >>= 1;
			T combined = operator.apply(tree[cr], ans);
			if (!tester.test(combined)) {
				while (cr < size) {
					cr = (cr << 1) | 1;
					combined = operator.apply(tree[cr], ans);
					if (tester.test(combined)) {
						ans = combined;
						cr--;
					}
				}
				return cr - size + 1;
			}
			ans = combined;
		} while ((cr & -cr) != cr);
		return 0;
	}

	public Iterator<T> iterator() {
		return new Iterator<>() {
			private int i = 0;

			public boolean hasNext() {
				return i < n;
			}

			public T next() {
				if (!hasNext()) throw new NoSuchElementException();
				return tree[size + i++];
			}
		};
	}

	public String toString() {
		if (isEmpty()) return "";
		final StringBuilder sb = new StringBuilder();
		sb.append(tree[size]);
		for (int i = 1; i < n; i++) sb.append(' ').append(tree[size + i]);
		return sb.toString();
	}

	private T update(final int idx, final T v) {
		tree[idx] = v;
		for (int j = idx >> 1; j > 0; j >>= 1) tree[j] = operator.apply(tree[j << 1], tree[(j << 1) | 1]);
		return v;
	}

	private void build() {
		for (int i = size - 1; i > 0; i--) tree[i] = operator.apply(tree[i << 1], tree[(i << 1) | 1]);
	}
}
