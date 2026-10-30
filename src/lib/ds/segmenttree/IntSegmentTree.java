package lib.ds.segmenttree;

import java.util.*;
import java.util.function.*;

import lib.ds.*;

/**
 * int値のセグメント木。結合則を満たす演算と単位元を指定し、点更新・区間集計・二分探索を O(log n) で行う。
 * 添字は0始まり、query は半開区間 {@code [l, r)} の集計結果を返し、{@code l >= r} なら単位元を返す。範囲外では IndexOutOfBoundsException を投げる。
 * set は更新前の値、add・multiply・affine・apply は更新後の値を返す。affine は {@code 現在値 * m + d}、apply は {@code f(現在値, x)} へ更新する。
 */
public final class IntSegmentTree implements IntCollection {
	public final int size, identity;
	private final int leafSize;
	private final IntBinaryOperator operator;
	private final int[] tree;

	public IntSegmentTree(final int n, final IntBinaryOperator operator, final int identity) {
		size = n;
		leafSize = n <= 1 ? 1 : Integer.highestOneBit(n - 1) << 1;
		this.identity = identity;
		this.operator = operator;
		tree = new int[leafSize << 1];
		if (identity != 0) Arrays.fill(tree, identity);
	}

	public IntSegmentTree(final int[] a, final IntBinaryOperator operator, final int identity) {
		this(a.length, operator, identity);
		System.arraycopy(a, 0, tree, leafSize, size);
		build();
	}

	public IntSegmentTree(final IntCollection a, final IntBinaryOperator operator, final int identity) {
		this(a.size(), operator, identity);
		final PrimitiveIterator.OfInt it = a.iterator();
		for (int i = 0, idx = leafSize; i < size; i++, idx++) tree[idx] = it.nextInt();
		build();
	}

	public IntSegmentTree(final IntSegmentTree other) {
		size = other.size;
		leafSize = other.leafSize;
		identity = other.identity;
		operator = other.operator;
		tree = other.tree.clone();
	}

	public int size() {
		return size;
	}

	public int get(final int i) {
		return tree[leafSize + Objects.checkIndex(i, size)];
	}

	public int set(final int i, final int v) {
		final int idx = leafSize + Objects.checkIndex(i, size);
		final int old = tree[idx];
		update(idx, v);
		return old;
	}

	public int add(final int i, final int d) {
		final int idx = leafSize + Objects.checkIndex(i, size);
		return update(idx, tree[idx] + d);
	}

	public int multiply(final int i, final int m) {
		final int idx = leafSize + Objects.checkIndex(i, size);
		return update(idx, tree[idx] * m);
	}

	public int affine(final int i, final int m, final int d) {
		final int idx = leafSize + Objects.checkIndex(i, size);
		return update(idx, tree[idx] * m + d);
	}

	public int apply(final int i, final int x, final IntBinaryOperator f) {
		final int idx = leafSize + Objects.checkIndex(i, size);
		return update(idx, f.applyAsInt(tree[idx], x));
	}

	public IntSegmentTree fill(final int v) {
		Arrays.fill(tree, leafSize, leafSize + size, v);
		build();
		return this;
	}

	public IntSegmentTree setAll(final IntUnaryOperator init) {
		for (int i = 0, idx = leafSize; i < size; i++, idx++) tree[idx] = init.applyAsInt(i);
		build();
		return this;
	}

	public int query(final int l, final int r) {
		if (l >= r) return identity;
		Objects.checkFromToIndex(l, r, size);
		int sml = identity, smr = identity;
		for (int cl = l + leafSize, cr = r + leafSize; cl < cr; cl >>= 1, cr >>= 1) {
			if ((cl & 1) == 1) sml = operator.applyAsInt(sml, tree[cl++]);
			if ((cr & 1) == 1) smr = operator.applyAsInt(tree[--cr], smr);
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
		do {
			cl >>= Integer.numberOfTrailingZeros(cl);
			int combined = operator.applyAsInt(ans, tree[cl]);
			if (!tester.test(combined)) {
				while (cl < leafSize) {
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
		do {
			cr--;
			while (cr > 1 && (cr & 1) == 1) cr >>= 1;
			int combined = operator.applyAsInt(tree[cr], ans);
			if (!tester.test(combined)) {
				while (cr < leafSize) {
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
		return Arrays.copyOfRange(tree, leafSize, leafSize + size);
	}

	public PrimitiveIterator.OfInt iterator() {
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
		final StringBuilder sb = new StringBuilder(12 * size - 1);
		sb.append(tree[leafSize]);
		for (int i = 1; i < size; i++) sb.append(' ').append(tree[leafSize + i]);
		return sb.toString();
	}

	private void build() {
		for (int i = leafSize - 1; i > 0; i--) tree[i] = operator.applyAsInt(tree[i << 1], tree[(i << 1) | 1]);
	}

	private int update(final int idx, final int v) {
		tree[idx] = v;
		for (int j = idx >> 1; j > 0; j >>= 1) tree[j] = operator.applyAsInt(tree[j << 1], tree[(j << 1) | 1]);
		return v;
	}
}
