package lib.ds.segmenttree;

import java.util.*;
import java.util.function.*;

import lib.ds.*;

/**
 * int値のセグメント木。結合則を満たす演算と単位元を指定し、点更新・区間集計・二分探索を O(log n) で行う。
 * 添字は0始まりとする。{@code query(l, r)} は半開区間 {@code [l, r)} の集計結果を返し、{@code l >= r} は空区間として単位元を返す。
 * 範囲外の添字・区間では IndexOutOfBoundsException を投げる。set は更新前の値、add・multiply・affine・apply は更新後の値を返す。
 * affine は {@code 現在値 * m + d}、apply は {@code f(現在値, x)} へ更新する。
 * maxRight は {@code tester(query(l, r))} が真となる最大の r、minLeft は {@code tester(query(l, r))} が真となる最小の l を返す。
 * tester は単位元に対して真となり、区間を広げると真から偽へ一度だけ変わるものとする。
 */
public final class IntSegmentTree implements IntCollection {
	private final int n, size, identity;
	private final IntBinaryOperator operator;
	private final int[] tree;

	public IntSegmentTree(final int n, final IntBinaryOperator operator, final int identity) {
		this.n = n;
		size = n <= 1 ? 1 : Integer.highestOneBit(n - 1) << 1;
		this.identity = identity;
		this.operator = operator;
		tree = new int[size << 1];
		if (identity != 0) Arrays.fill(tree, identity);
	}

	public IntSegmentTree(final int[] a, final IntBinaryOperator operator, final int identity) {
		this(a.length, operator, identity);
		System.arraycopy(a, 0, tree, size, n);
		build();
	}

	public IntSegmentTree(final IntCollection a, final IntBinaryOperator operator, final int identity) {
		this(a.size(), operator, identity);
		final PrimitiveIterator.OfInt it = a.iterator();
		for (int i = 0, idx = size; i < n; i++, idx++) tree[idx] = it.nextInt();
		build();
	}

	public IntSegmentTree(final IntSegmentTree other) {
		n = other.n;
		size = other.size;
		identity = other.identity;
		operator = other.operator;
		tree = other.tree.clone();
	}

	public int size() {
		return n;
	}

	public int get(final int i) {
		return tree[size + Objects.checkIndex(i, n)];
	}

	public int set(final int i, final int v) {
		final int idx = size + Objects.checkIndex(i, n);
		final int prev = tree[idx];
		update(idx, v);
		return prev;
	}

	public int add(final int i, final int d) {
		final int idx = size + Objects.checkIndex(i, n);
		return update(idx, tree[idx] + d);
	}

	public int multiply(final int i, final int m) {
		final int idx = size + Objects.checkIndex(i, n);
		return update(idx, tree[idx] * m);
	}

	public int affine(final int i, final int m, final int d) {
		final int idx = size + Objects.checkIndex(i, n);
		return update(idx, tree[idx] * m + d);
	}

	public int apply(final int i, final int x, final IntBinaryOperator f) {
		final int idx = size + Objects.checkIndex(i, n);
		return update(idx, f.applyAsInt(tree[idx], x));
	}

	public IntSegmentTree fill(final int v) {
		Arrays.fill(tree, size, size + n, v);
		build();
		return this;
	}

	public IntSegmentTree setAll(final IntUnaryOperator init) {
		for (int i = 0, idx = size; i < n; i++, idx++) tree[idx] = init.applyAsInt(i);
		build();
		return this;
	}

	public int query(final int l, final int r) {
		if (l >= r) return identity;
		Objects.checkFromToIndex(l, r, n);
		int sml = identity, smr = identity;
		for (int cl = l + size, cr = r + size; cl < cr; cl >>= 1, cr >>= 1) {
			if ((cl & 1) == 1) sml = operator.applyAsInt(sml, tree[cl++]);
			if ((cr & 1) == 1) smr = operator.applyAsInt(tree[--cr], smr);
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
		do {
			cl >>= Integer.numberOfTrailingZeros(cl);
			int combined = operator.applyAsInt(ans, tree[cl]);
			if (!tester.test(combined)) {
				while (cl < size) {
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
		do {
			cr--;
			while (cr > 1 && (cr & 1) == 1) cr >>= 1;
			int combined = operator.applyAsInt(tree[cr], ans);
			if (!tester.test(combined)) {
				while (cr < size) {
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
		return Arrays.copyOfRange(tree, size, size + n);
	}

	public PrimitiveIterator.OfInt iterator() {
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
		final StringBuilder sb = new StringBuilder(12 * n - 1);
		sb.append(tree[size]);
		for (int i = 1; i < n; i++) sb.append(' ').append(tree[size + i]);
		return sb.toString();
	}

	private int update(final int idx, final int v) {
		tree[idx] = v;
		for (int j = idx >> 1; j > 0; j >>= 1) tree[j] = operator.applyAsInt(tree[j << 1], tree[(j << 1) | 1]);
		return v;
	}

	private void build() {
		for (int i = size - 1; i > 0; i--) tree[i] = operator.applyAsInt(tree[i << 1], tree[(i << 1) | 1]);
	}
}
