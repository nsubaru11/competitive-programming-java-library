package lib.ds.segmenttree;

import java.util.*;
import java.util.function.*;

/**
 * 作用素を遅延伝播する汎用セグメント木。区間作用・区間集計・二分探索を O(log n) で行う。
 * 添字は0始まりとする。{@code apply(l, r, f)} と {@code query(l, r)} は半開区間 {@code [l, r)} を扱い、{@code l >= r} は空区間とする。
 * 空区間の query は単位元を返し、空区間の apply は何もしない。それ以外の範囲外の添字・区間では IndexOutOfBoundsException を投げる。
 * operator は結合則を満たし identity はその単位元、{@code mapping(f, x)} は値 x に作用素 f を作用させた値、
 * {@code composition(f, g)} は g の後に f を作用させる作用素を返すものとする。作用素は区間長を受け取らない。
 * mapping と composition を省略した場合は区間代入として扱う。
 * set は作用を反映した更新前の値、{@code apply(i, f)} は更新後の値を返す。
 * maxRight は {@code tester(query(l, r))} が真となる最大の r、minLeft は {@code tester(query(l, r))} が真となる最小の l を返す。
 * tester は単位元に対して真となり、区間を広げると真から偽へ一度だけ変わるものとする。
 * identity が null の場合、operator は null を受け付けること。配列・コレクション・コピーからの構築は内部配列を共有せず、要素オブジェクトは共有する。
 */
@SuppressWarnings("unchecked")
public final class LazySegmentTree<T> implements Iterable<T> {
	private static final BiFunction<?, ?, ?> DEFAULT_MAPPING = (f, x) -> f;
	private static final BinaryOperator<?> DEFAULT_COMPOSITION = (f, g) -> f;
	public final int size;
	public final T identity;
	private final int leafSize, log;
	private final BinaryOperator<T> operator, composition;
	private final BiFunction<T, T, T> mapping;
	private final T[] tree, lazy;
	private final boolean[] hasLazy;

	public LazySegmentTree(final int n, final BinaryOperator<T> operator, final T identity) {
		this(n, operator, identity, (BiFunction<T, T, T>) DEFAULT_MAPPING, (BinaryOperator<T>) DEFAULT_COMPOSITION);
	}

	public LazySegmentTree(final int n, final BinaryOperator<T> operator, final T identity, final BiFunction<T, T, T> mapping, final BinaryOperator<T> composition) {
		size = n;
		leafSize = n <= 1 ? 1 : Integer.highestOneBit(n - 1) << 1;
		log = Integer.numberOfTrailingZeros(leafSize);
		this.identity = identity;
		this.operator = operator;
		this.mapping = mapping;
		this.composition = composition;
		tree = (T[]) new Object[leafSize << 1];
		if (identity != null) Arrays.fill(tree, identity);
		lazy = (T[]) new Object[leafSize];
		hasLazy = new boolean[leafSize];
	}

	public LazySegmentTree(final T[] a, final BinaryOperator<T> operator, final T identity) {
		this(a, operator, identity, (BiFunction<T, T, T>) DEFAULT_MAPPING, (BinaryOperator<T>) DEFAULT_COMPOSITION);
	}

	public LazySegmentTree(final T[] a, final BinaryOperator<T> operator, final T identity, final BiFunction<T, T, T> mapping, final BinaryOperator<T> composition) {
		this(a.length, operator, identity, mapping, composition);
		System.arraycopy(a, 0, tree, leafSize, size);
		build();
	}

	public LazySegmentTree(final Collection<? extends T> a, final BinaryOperator<T> operator, final T identity) {
		this(a, operator, identity, (BiFunction<T, T, T>) DEFAULT_MAPPING, (BinaryOperator<T>) DEFAULT_COMPOSITION);
	}

	public LazySegmentTree(final Collection<? extends T> a, final BinaryOperator<T> operator, final T identity, final BiFunction<T, T, T> mapping, final BinaryOperator<T> composition) {
		this(a.size(), operator, identity, mapping, composition);
		final Iterator<? extends T> it = a.iterator();
		for (int i = 0, idx = leafSize; i < size; i++, idx++) tree[idx] = it.next();
		build();
	}

	public LazySegmentTree(final LazySegmentTree<T> other) {
		size = other.size;
		leafSize = other.leafSize;
		log = other.log;
		identity = other.identity;
		operator = other.operator;
		mapping = other.mapping;
		composition = other.composition;
		tree = other.tree.clone();
		lazy = other.lazy.clone();
		hasLazy = other.hasLazy.clone();
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public T get(final int i) {
		final int idx = leafSize + Objects.checkIndex(i, size);
		pushPath(idx);
		return tree[idx];
	}

	public T set(final int i, final T v) {
		final int idx = leafSize + Objects.checkIndex(i, size);
		pushPath(idx);
		final T old = tree[idx];
		tree[idx] = v;
		updatePath(idx);
		return old;
	}

	public T apply(final int i, final T f) {
		final int idx = leafSize + Objects.checkIndex(i, size);
		pushPath(idx);
		final T v = tree[idx] = mapping.apply(f, tree[idx]);
		updatePath(idx);
		return v;
	}

	public void apply(int l, int r, final T f) {
		if (l >= r) return;
		Objects.checkFromToIndex(l, r, size);
		l += leafSize;
		r += leafSize;
		pushBoundaries(l, r);
		final int l2 = l, r2 = r;
		for (; l < r; l >>= 1, r >>= 1) {
			if ((l & 1) == 1) allApply(l++, f);
			if ((r & 1) == 1) allApply(--r, f);
		}
		updateBoundaries(l2, r2);
	}

	public LazySegmentTree<T> fill(final T v) {
		Arrays.fill(tree, leafSize, leafSize + size, v);
		build();
		return this;
	}

	public LazySegmentTree<T> setAll(final IntFunction<T> init) {
		for (int i = 0, idx = leafSize; i < size; i++, idx++) tree[idx] = init.apply(i);
		build();
		return this;
	}

	public T query(int l, int r) {
		if (l >= r) return identity;
		Objects.checkFromToIndex(l, r, size);
		l += leafSize;
		r += leafSize;
		pushBoundaries(l, r);
		T sml = identity, smr = identity;
		for (; l < r; l >>= 1, r >>= 1) {
			if ((l & 1) == 1) sml = operator.apply(sml, tree[l++]);
			if ((r & 1) == 1) smr = operator.apply(tree[--r], smr);
		}
		return operator.apply(sml, smr);
	}

	public T queryAll() {
		return tree[1];
	}

	public int maxRight(final int l, final Predicate<T> tester) {
		Objects.checkIndex(l, size + 1);
		if (l == size) return size;
		T ans = identity;
		int cl = l + leafSize;
		pushPath(cl);
		do {
			cl >>= Integer.numberOfTrailingZeros(cl);
			T combined = operator.apply(ans, tree[cl]);
			if (!tester.test(combined)) {
				while (cl < leafSize) {
					push(cl);
					cl <<= 1;
					combined = operator.apply(ans, tree[cl]);
					if (tester.test(combined)) {
						ans = combined;
						cl++;
					}
				}
				return cl - leafSize;
			}
			ans = combined;
			cl++;
		} while ((cl & -cl) != cl);
		return size;
	}

	public int minLeft(final int r, final Predicate<T> tester) {
		Objects.checkIndex(r, size + 1);
		if (r == 0) return 0;
		T ans = identity;
		int cr = r + leafSize;
		pushPath(cr - 1);
		do {
			cr--;
			while (cr > 1 && (cr & 1) == 1) cr >>= 1;
			T combined = operator.apply(tree[cr], ans);
			if (!tester.test(combined)) {
				while (cr < leafSize) {
					push(cr);
					cr = (cr << 1) | 1;
					combined = operator.apply(tree[cr], ans);
					if (tester.test(combined)) {
						ans = combined;
						cr--;
					}
				}
				return cr - leafSize + 1;
			}
			ans = combined;
		} while ((cr & -cr) != cr);
		return 0;
	}

	public Iterator<T> iterator() {
		pushAll();
		return new Iterator<>() {
			private int i = 0;

			public boolean hasNext() {
				return i < size;
			}

			public T next() {
				if (!hasNext()) throw new NoSuchElementException();
				return tree[leafSize + i++];
			}
		};
	}

	public String toString() {
		if (isEmpty()) return "";
		pushAll();
		final StringBuilder sb = new StringBuilder();
		sb.append(tree[leafSize]);
		for (int i = 1; i < size; i++) sb.append(' ').append(tree[leafSize + i]);
		return sb.toString();
	}

	private void build() {
		for (int i = leafSize - 1; i > 0; i--) update(i);
		Arrays.fill(hasLazy, false);
	}

	private void update(final int i) {
		tree[i] = operator.apply(tree[i << 1], tree[i << 1 | 1]);
	}

	private void allApply(final int i, final T f) {
		tree[i] = mapping.apply(f, tree[i]);
		if (i >= leafSize) return;
		lazy[i] = hasLazy[i] ? composition.apply(f, lazy[i]) : f;
		hasLazy[i] = true;
	}

	private void push(final int i) {
		if (!hasLazy[i]) return;
		final T f = lazy[i];
		allApply(i << 1, f);
		allApply(i << 1 | 1, f);
		hasLazy[i] = false;
	}

	private void pushAll() {
		for (int i = 1; i < leafSize; i++) push(i);
	}

	private void pushPath(final int idx) {
		for (int i = log; i >= 1; i--) push(idx >> i);
	}

	private void updatePath(final int idx) {
		for (int i = idx >> 1; i > 0; i >>= 1) update(i);
	}

	private void pushBoundaries(final int l, final int r) {
		for (int i = log; i >= 1; i--) {
			if (((l >> i) << i) != l) push(l >> i);
			if (((r >> i) << i) != r) push((r - 1) >> i);
		}
	}

	private void updateBoundaries(final int l, final int r) {
		for (int i = 1; i <= log; i++) {
			if (((l >> i) << i) != l) update(l >> i);
			if (((r >> i) << i) != r) update((r - 1) >> i);
		}
	}
}
