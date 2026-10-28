package lib.ds.segmenttree;

import java.util.*;
import java.util.function.*;

import lib.ds.*;

/**
 * int値の作用素を遅延伝播するセグメント木。区間作用・区間集計・二分探索を O(log n) で行う。
 * 添字は0始まりとする。{@code apply(l, r, f)} と {@code query(l, r)} は半開区間 {@code [l, r)} を扱い、{@code l >= r} は空区間とする。
 * 空区間の query は単位元を返し、空区間の apply は何もしない。それ以外の範囲外の添字・区間では IndexOutOfBoundsException を投げる。
 * operator は結合則を満たし identity はその単位元、{@code mapping(f, x)} は値 x に作用素 f を作用させた値、
 * {@code composition(f, g)} は g の後に f を作用させる作用素を返すものとする。作用素は区間長を受け取らない。
 * mapping と composition を省略した場合は区間代入として扱う。
 * set は作用を反映した更新前の値、{@code apply(i, f)} は更新後の値を返す。
 * maxRight は {@code tester(query(l, r))} が真となる最大の r、minLeft は {@code tester(query(l, r))} が真となる最小の l を返す。
 * tester は単位元に対して真となり、区間を広げると真から偽へ一度だけ変わるものとする。
 */
public final class IntLazySegmentTree implements IntCollection {
	private static final IntBinaryOperator DEFAULT_MAPPING = (f, _) -> f;
	private static final IntBinaryOperator DEFAULT_COMPOSITION = (f, _) -> f;
	private final int n, size, log, identity;
	private final IntBinaryOperator operator, mapping, composition;
	private final int[] tree, lazy;
	private final boolean[] hasLazy;

	public IntLazySegmentTree(final int n, final IntBinaryOperator operator, final int identity) {
		this(n, operator, identity, DEFAULT_MAPPING, DEFAULT_COMPOSITION);
	}

	public IntLazySegmentTree(final int n, final IntBinaryOperator operator, final int identity, final IntBinaryOperator mapping, final IntBinaryOperator composition) {
		this.n = n;
		size = n <= 1 ? 1 : Integer.highestOneBit(n - 1) << 1;
		log = Integer.numberOfTrailingZeros(size);
		this.identity = identity;
		this.operator = operator;
		this.mapping = mapping;
		this.composition = composition;
		tree = new int[size << 1];
		if (identity != 0) Arrays.fill(tree, identity);
		lazy = new int[size];
		hasLazy = new boolean[size];
	}

	public IntLazySegmentTree(final int[] a, final IntBinaryOperator operator, final int identity) {
		this(a, operator, identity, DEFAULT_MAPPING, DEFAULT_COMPOSITION);
	}

	public IntLazySegmentTree(final int[] a, final IntBinaryOperator operator, final int identity, final IntBinaryOperator mapping, final IntBinaryOperator composition) {
		this(a.length, operator, identity, mapping, composition);
		System.arraycopy(a, 0, tree, size, n);
		build();
	}

	public IntLazySegmentTree(final IntCollection a, final IntBinaryOperator operator, final int identity) {
		this(a, operator, identity, DEFAULT_MAPPING, DEFAULT_COMPOSITION);
	}

	public IntLazySegmentTree(final IntCollection a, final IntBinaryOperator operator, final int identity, final IntBinaryOperator mapping, final IntBinaryOperator composition) {
		this(a.size(), operator, identity, mapping, composition);
		final PrimitiveIterator.OfInt it = a.iterator();
		for (int i = 0, idx = size; i < n; i++, idx++) tree[idx] = it.nextInt();
		build();
	}

	public IntLazySegmentTree(final IntLazySegmentTree other) {
		n = other.n;
		size = other.size;
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
		return n;
	}

	public int get(final int i) {
		final int idx = size + Objects.checkIndex(i, n);
		pushPath(idx);
		return tree[idx];
	}

	public int set(final int i, final int v) {
		final int idx = size + Objects.checkIndex(i, n);
		pushPath(idx);
		final int prev = tree[idx];
		tree[idx] = v;
		updatePath(idx);
		return prev;
	}

	public int apply(final int i, final int f) {
		final int idx = size + Objects.checkIndex(i, n);
		pushPath(idx);
		final int v = tree[idx] = mapping.applyAsInt(f, tree[idx]);
		updatePath(idx);
		return v;
	}

	public void apply(int l, int r, final int f) {
		if (l >= r) return;
		Objects.checkFromToIndex(l, r, n);
		l += size;
		r += size;
		pushBoundaries(l, r);
		final int l2 = l, r2 = r;
		for (; l < r; l >>= 1, r >>= 1) {
			if ((l & 1) == 1) allApply(l++, f);
			if ((r & 1) == 1) allApply(--r, f);
		}
		updateBoundaries(l2, r2);
	}

	public IntLazySegmentTree fill(final int v) {
		Arrays.fill(tree, size, size + n, v);
		build();
		return this;
	}

	public IntLazySegmentTree setAll(final IntUnaryOperator init) {
		for (int i = 0, idx = size; i < n; i++, idx++) tree[idx] = init.applyAsInt(i);
		build();
		return this;
	}

	public int query(int l, int r) {
		if (l >= r) return identity;
		Objects.checkFromToIndex(l, r, n);
		l += size;
		r += size;
		pushBoundaries(l, r);
		int sml = identity, smr = identity;
		for (; l < r; l >>= 1, r >>= 1) {
			if ((l & 1) == 1) sml = operator.applyAsInt(sml, tree[l++]);
			if ((r & 1) == 1) smr = operator.applyAsInt(tree[--r], smr);
		}
		return operator.applyAsInt(sml, smr);
	}

	public int queryAll() {
		return tree[1];
	}

	public int maxRight(final int l, final IntPredicate tester) {
		Objects.checkIndex(l, n + 1);
		if (l == n) return n;
		int ans = identity;
		int cl = l + size;
		pushPath(cl);
		do {
			cl >>= Integer.numberOfTrailingZeros(cl);
			int combined = operator.applyAsInt(ans, tree[cl]);
			if (!tester.test(combined)) {
				while (cl < size) {
					push(cl);
					cl <<= 1;
					combined = operator.applyAsInt(ans, tree[cl]);
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

	public int minLeft(final int r, final IntPredicate tester) {
		Objects.checkIndex(r, n + 1);
		if (r == 0) return 0;
		int ans = identity;
		int cr = r + size;
		pushPath(cr - 1);
		do {
			cr--;
			while (cr > 1 && (cr & 1) == 1) cr >>= 1;
			int combined = operator.applyAsInt(tree[cr], ans);
			if (!tester.test(combined)) {
				while (cr < size) {
					push(cr);
					cr = (cr << 1) | 1;
					combined = operator.applyAsInt(tree[cr], ans);
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

	public int[] toArray() {
		pushAll();
		return Arrays.copyOfRange(tree, size, size + n);
	}

	public PrimitiveIterator.OfInt iterator() {
		pushAll();
		return new PrimitiveIterator.OfInt() {
			private int i = 0;

			public boolean hasNext() {
				return i < n;
			}

			public int nextInt() {
				if (!hasNext()) throw new NoSuchElementException();
				return tree[size + i++];
			}
		};
	}

	public String toString() {
		if (isEmpty()) return "";
		pushAll();
		final StringBuilder sb = new StringBuilder(12 * n - 1);
		sb.append(tree[size]);
		for (int i = 1; i < n; i++) sb.append(' ').append(tree[size + i]);
		return sb.toString();
	}

	private void build() {
		for (int i = size - 1; i > 0; i--) update(i);
		Arrays.fill(hasLazy, false);
	}

	private void update(final int i) {
		tree[i] = operator.applyAsInt(tree[i << 1], tree[i << 1 | 1]);
	}

	private void allApply(final int i, final int f) {
		tree[i] = mapping.applyAsInt(f, tree[i]);
		if (i >= size) return;
		lazy[i] = hasLazy[i] ? composition.applyAsInt(f, lazy[i]) : f;
		hasLazy[i] = true;
	}

	private void push(final int i) {
		if (!hasLazy[i]) return;
		final int f = lazy[i];
		allApply(i << 1, f);
		allApply(i << 1 | 1, f);
		hasLazy[i] = false;
	}

	private void pushAll() {
		for (int i = 1; i < size; i++) push(i);
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
