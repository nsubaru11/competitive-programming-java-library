package lib.ds.segmenttree;

import java.util.*;
import java.util.function.*;

import lib.ds.*;

/**
 * int値の作用素を遅延伝播するセグメント木。区間作用・区間集計・二分探索を O(log n) で行う。
 * 添字は0始まり、区間は半開区間 {@code [l, r)} とし、{@code l >= r} では query は単位元を返し、apply は何もしない。範囲外では IndexOutOfBoundsException を投げる。
 * {@code mapping(f, x)} は値 x に作用素 f を作用させた値、{@code composition(f, g)} は g の後に f を作用させる作用素を返す。
 * 作用素は区間長を受け取らない。mapping と composition を省略すると区間代入になる。set は更新前の値、単一点の apply は更新後の値を返す。
 */
public final class IntLazySegmentTree implements IntCollection {
	private static final IntBinaryOperator DEFAULT_MAPPING = (f, x) -> f;
	private static final IntBinaryOperator DEFAULT_COMPOSITION = (f, g) -> f;
	public final int size, identity;
	private final int leafSize, log;
	private final IntBinaryOperator operator, mapping, composition;
	private final int[] tree, lazy;
	private final boolean[] hasLazy;

	public IntLazySegmentTree(final int n, final IntBinaryOperator operator, final int identity) {
		this(n, operator, identity, DEFAULT_MAPPING, DEFAULT_COMPOSITION);
	}

	public IntLazySegmentTree(final int n, final IntBinaryOperator operator, final int identity, final IntBinaryOperator mapping, final IntBinaryOperator composition) {
		size = n;
		leafSize = n <= 1 ? 1 : Integer.highestOneBit(n - 1) << 1;
		log = Integer.numberOfTrailingZeros(leafSize);
		this.identity = identity;
		this.operator = operator;
		this.mapping = mapping;
		this.composition = composition;
		tree = new int[leafSize << 1];
		if (identity != 0) Arrays.fill(tree, identity);
		lazy = new int[leafSize];
		hasLazy = new boolean[leafSize];
	}

	public IntLazySegmentTree(final int[] a, final IntBinaryOperator operator, final int identity) {
		this(a, operator, identity, DEFAULT_MAPPING, DEFAULT_COMPOSITION);
	}

	public IntLazySegmentTree(final int[] a, final IntBinaryOperator operator, final int identity, final IntBinaryOperator mapping, final IntBinaryOperator composition) {
		this(a.length, operator, identity, mapping, composition);
		System.arraycopy(a, 0, tree, leafSize, size);
		build();
	}

	public IntLazySegmentTree(final IntCollection a, final IntBinaryOperator operator, final int identity) {
		this(a, operator, identity, DEFAULT_MAPPING, DEFAULT_COMPOSITION);
	}

	public IntLazySegmentTree(final IntCollection a, final IntBinaryOperator operator, final int identity, final IntBinaryOperator mapping, final IntBinaryOperator composition) {
		this(a.size(), operator, identity, mapping, composition);
		final PrimitiveIterator.OfInt it = a.iterator();
		for (int i = 0, idx = leafSize; i < size; i++, idx++) tree[idx] = it.nextInt();
		build();
	}

	public IntLazySegmentTree(final IntLazySegmentTree other) {
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

	public int get(final int i) {
		final int idx = leafSize + Objects.checkIndex(i, size);
		pushPath(idx);
		return tree[idx];
	}

	public int set(final int i, final int v) {
		final int idx = leafSize + Objects.checkIndex(i, size);
		pushPath(idx);
		final int old = tree[idx];
		tree[idx] = v;
		updatePath(idx);
		return old;
	}

	public int apply(final int i, final int f) {
		final int idx = leafSize + Objects.checkIndex(i, size);
		pushPath(idx);
		final int v = tree[idx] = mapping.applyAsInt(f, tree[idx]);
		updatePath(idx);
		return v;
	}

	public void apply(int l, int r, final int f) {
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

	public IntLazySegmentTree fill(final int v) {
		Arrays.fill(tree, leafSize, leafSize + size, v);
		build();
		return this;
	}

	public IntLazySegmentTree setAll(final IntUnaryOperator init) {
		for (int i = 0, idx = leafSize; i < size; i++, idx++) tree[idx] = init.applyAsInt(i);
		build();
		return this;
	}

	public int query(int l, int r) {
		if (l >= r) return identity;
		Objects.checkFromToIndex(l, r, size);
		l += leafSize;
		r += leafSize;
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

	/**
	 * {@code tester(query(l, r))} が真となる最大の r を返す。
	 * tester は単位元に対して真となり、r を大きくすると真から偽へ一度だけ変わるものとする。
	 */
	public int maxRight(final int l, final IntPredicate tester) {
		Objects.checkIndex(l, size + 1);
		if (l == size) return size;
		int ans = identity;
		int cl = l + leafSize;
		pushPath(cl);
		do {
			cl >>= Integer.numberOfTrailingZeros(cl);
			int combined = operator.applyAsInt(ans, tree[cl]);
			if (!tester.test(combined)) {
				while (cl < leafSize) {
					push(cl);
					cl <<= 1;
					combined = operator.applyAsInt(ans, tree[cl]);
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

	/**
	 * {@code tester(query(l, r))} が真となる最小の l を返す。
	 * tester は単位元に対して真となり、l を小さくすると真から偽へ一度だけ変わるものとする。
	 */
	public int minLeft(final int r, final IntPredicate tester) {
		Objects.checkIndex(r, size + 1);
		if (r == 0) return 0;
		int ans = identity;
		int cr = r + leafSize;
		pushPath(cr - 1);
		do {
			cr--;
			while (cr > 1 && (cr & 1) == 1) cr >>= 1;
			int combined = operator.applyAsInt(tree[cr], ans);
			if (!tester.test(combined)) {
				while (cr < leafSize) {
					push(cr);
					cr = (cr << 1) | 1;
					combined = operator.applyAsInt(tree[cr], ans);
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

	public int[] toArray() {
		pushAll();
		return Arrays.copyOfRange(tree, leafSize, leafSize + size);
	}

	public PrimitiveIterator.OfInt iterator() {
		pushAll();
		return new PrimitiveIterator.OfInt() {
			private int i = 0;

			public boolean hasNext() {
				return i < size;
			}

			public int nextInt() {
				if (!hasNext()) throw new NoSuchElementException();
				return tree[leafSize + i++];
			}
		};
	}

	public String toString() {
		if (isEmpty()) return "";
		pushAll();
		final StringBuilder sb = new StringBuilder(12 * size - 1);
		sb.append(tree[leafSize]);
		for (int i = 1; i < size; i++) sb.append(' ').append(tree[leafSize + i]);
		return sb.toString();
	}

	private void build() {
		for (int i = leafSize - 1; i > 0; i--) update(i);
		Arrays.fill(hasLazy, false);
	}

	private void update(final int i) {
		tree[i] = operator.applyAsInt(tree[i << 1], tree[i << 1 | 1]);
	}

	private void allApply(final int i, final int f) {
		tree[i] = mapping.applyAsInt(f, tree[i]);
		if (i >= leafSize) return;
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
