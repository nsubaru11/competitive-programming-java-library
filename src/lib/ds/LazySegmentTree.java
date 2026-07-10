package lib.ds;

import java.util.*;
import java.util.function.*;

@SuppressWarnings({"unused", "unchecked"})
public final class LazySegmentTree<T> implements Iterable<T> {
	private final int n, size, log;
	private final T identity;
	private final BinaryOperator<T> operator, composition;
	private final BiFunction<T, T, T> mapping;
	private final T[] tree, lazy;
	private final boolean[] hasLazy;

	public LazySegmentTree(final int n, final BinaryOperator<T> operator, final T identity) {
		this(n, operator, identity, (f, x) -> f, (f, g) -> f);
	}

	public LazySegmentTree(final int n, final BinaryOperator<T> operator, final T identity,
	                       final BiFunction<T, T, T> mapping, final BinaryOperator<T> composition) {
		this.n = n;
		size = n <= 1 ? 1 : Integer.highestOneBit(n - 1) << 1;
		this.identity = identity;
		this.operator = operator;
		this.mapping = mapping;
		this.composition = composition;
		log = Integer.numberOfTrailingZeros(size);
		tree = (T[]) new Object[size << 1];
		if (identity != null) Arrays.fill(tree, identity);
		lazy = (T[]) new Object[size << 1];
		hasLazy = new boolean[size << 1];
	}

	public LazySegmentTree(final T[] data, final BinaryOperator<T> operator, final T identity) {
		this(data, operator, identity, (f, x) -> f, (f, g) -> f);
	}

	public LazySegmentTree(final T[] data, final BinaryOperator<T> operator, final T identity,
	                       final BiFunction<T, T, T> mapping, final BinaryOperator<T> composition) {
		this(data.length, operator, identity, mapping, composition);
		System.arraycopy(data, 0, tree, size, n);
		buildAll();
	}

	public T get(final int i) {
		final int idx = size + i;
		pushPath(idx);
		return tree[idx];
	}

	public void set(final int i, final T v) {
		final int idx = size + i;
		pushPath(idx);
		tree[idx] = v;
		for (int j = idx >> 1; j > 0; j >>= 1) update(j);
	}

	public void apply(final int i, final T v) {
		apply(i, i + 1, v);
	}

	public void apply(int l, int r, final T v) {
		if (l >= r) return;
		l += size;
		r += size;
		pushBoundaries(l, r);
		final int l2 = l, r2 = r;
		for (; l < r; l >>= 1, r >>= 1) {
			if ((l & 1) == 1) allApply(l++, v);
			if ((r & 1) == 1) allApply(--r, v);
		}
		updateBoundaries(l2, r2);
	}

	public void fill(final T val) {
		Arrays.fill(tree, size, size + n, val);
		buildAll();
	}

	public void setAll(final IntFunction<T> func) {
		for (int i = 0, idx = size; i < n; i++, idx++) tree[idx] = func.apply(i);
		buildAll();
	}

	public T query(int l, int r) {
		if (l >= r) return identity;
		l += size;
		r += size;
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
		if (l == n) return n;
		T ans = identity;
		int cl = l + size;
		pushPath(cl);
		do {
			cl >>= Integer.numberOfTrailingZeros(cl);
			T combined = operator.apply(ans, tree[cl]);
			if (!tester.test(combined)) {
				while (cl < size) {
					push(cl);
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
		if (r == 0) return 0;
		T ans = identity;
		int cr = r + size - 1;
		pushPath(cr);
		do {
			while (cr > 1 && (cr & 1) == 1) cr >>= 1;
			T combined = operator.apply(tree[cr], ans);
			if (!tester.test(combined)) {
				while (cr < size) {
					push(cr);
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
			cr--;
		} while ((cr & -cr) != cr);
		return 0;
	}

	public int size() {
		return n;
	}

	private void buildAll() {
		for (int i = size - 1; i > 0; i--) tree[i] = operator.apply(tree[i << 1], tree[i << 1 | 1]);
		Arrays.fill(hasLazy, false);
	}

	private void update(final int i) {
		tree[i] = operator.apply(tree[i << 1], tree[i << 1 | 1]);
	}

	private void push(final int i) {
		if (!hasLazy[i]) return;
		final T f = lazy[i];
		allApply(i << 1, f);
		allApply(i << 1 | 1, f);
		hasLazy[i] = false;
	}

	private void allApply(final int i, final T f) {
		tree[i] = mapping.apply(f, tree[i]);
		if (i >= size) return;
		lazy[i] = hasLazy[i] ? composition.apply(f, lazy[i]) : f;
		hasLazy[i] = true;
	}

	private void pushPath(final int idx) {
		for (int i = log; i >= 1; i--) push(idx >> i);
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

	public Iterator<T> iterator() {
		return new Iterator<>() {
			private int idx = 0;

			public boolean hasNext() {
				return idx < n;
			}

			public T next() {
				if (!hasNext()) throw new NoSuchElementException();
				return get(idx++);
			}
		};
	}

	@Override
	public String toString() {
		final StringBuilder s = new StringBuilder();
		s.append(get(0));
		for (int i = 1; i < n; i++) s.append(' ').append(get(i));
		return s.toString();
	}
}
