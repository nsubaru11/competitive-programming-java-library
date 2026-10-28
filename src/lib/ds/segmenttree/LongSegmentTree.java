package lib.ds.segmenttree;

import java.util.*;
import java.util.function.*;

import lib.ds.*;

/**
 * long値のセグメント木。結合則を満たす演算と単位元を指定し、点更新・区間集計・二分探索を O(log n) で行う。
 * 添字は0始まりとする。{@code query(l, r)} は半開区間 {@code [l, r)} の集計結果を返し、{@code l >= r} は空区間として単位元を返す。
 * 範囲外の添字・区間では IndexOutOfBoundsException を投げる。set は更新前の値、add・multiply・affine・apply は更新後の値を返す。
 * affine は {@code 現在値 * m + d}、apply は {@code f(現在値, x)} へ更新する。
 * maxRight は {@code tester(query(l, r))} が真となる最大の r、minLeft は {@code tester(query(l, r))} が真となる最小の l を返す。
 * tester は単位元に対して真となり、区間を広げると真から偽へ一度だけ変わるものとする。
 */
public final class LongSegmentTree implements LongCollection {
	private final int n, size;
	private final long identity;
	private final LongBinaryOperator operator;
	private final long[] tree;

	public LongSegmentTree(final int n, final LongBinaryOperator operator, final long identity) {
		this.n = n;
		size = n <= 1 ? 1 : Integer.highestOneBit(n - 1) << 1;
		this.identity = identity;
		this.operator = operator;
		tree = new long[size << 1];
		if (identity != 0) Arrays.fill(tree, identity);
	}

	public LongSegmentTree(final long[] a, final LongBinaryOperator operator, final long identity) {
		this(a.length, operator, identity);
		System.arraycopy(a, 0, tree, size, n);
		build();
	}

	public LongSegmentTree(final LongCollection a, final LongBinaryOperator operator, final long identity) {
		this(a.size(), operator, identity);
		final PrimitiveIterator.OfLong it = a.iterator();
		for (int i = 0, idx = size; i < n; i++, idx++) tree[idx] = it.nextLong();
		build();
	}

	public LongSegmentTree(final LongSegmentTree other) {
		n = other.n;
		size = other.size;
		identity = other.identity;
		operator = other.operator;
		tree = other.tree.clone();
	}

	public int size() {
		return n;
	}

	public long get(final int i) {
		return tree[size + Objects.checkIndex(i, n)];
	}

	public long set(final int i, final long v) {
		final int idx = size + Objects.checkIndex(i, n);
		final long old = tree[idx];
		update(idx, v);
		return old;
	}

	public long add(final int i, final long d) {
		final int idx = size + Objects.checkIndex(i, n);
		return update(idx, tree[idx] + d);
	}

	public long multiply(final int i, final long m) {
		final int idx = size + Objects.checkIndex(i, n);
		return update(idx, tree[idx] * m);
	}

	public long affine(final int i, final long m, final long d) {
		final int idx = size + Objects.checkIndex(i, n);
		return update(idx, tree[idx] * m + d);
	}

	public long apply(final int i, final long x, final LongBinaryOperator f) {
		final int idx = size + Objects.checkIndex(i, n);
		return update(idx, f.applyAsLong(tree[idx], x));
	}

	public LongSegmentTree fill(final long v) {
		Arrays.fill(tree, size, size + n, v);
		build();
		return this;
	}

	public LongSegmentTree setAll(final IntToLongFunction init) {
		for (int i = 0, idx = size; i < n; i++, idx++) tree[idx] = init.applyAsLong(i);
		build();
		return this;
	}

	public long query(final int l, final int r) {
		if (l >= r) return identity;
		Objects.checkFromToIndex(l, r, n);
		long sml = identity, smr = identity;
		for (int cl = l + size, cr = r + size; cl < cr; cl >>= 1, cr >>= 1) {
			if ((cl & 1) == 1) sml = operator.applyAsLong(sml, tree[cl++]);
			if ((cr & 1) == 1) smr = operator.applyAsLong(tree[--cr], smr);
		}
		return operator.applyAsLong(sml, smr);
	}

	public long queryAll() {
		return tree[1];
	}

	public int maxRight(final int l, final LongPredicate tester) {
		Objects.checkIndex(l, n + 1);
		if (l == n) return n;
		long ans = identity;
		int cl = l + size;
		do {
			cl >>= Integer.numberOfTrailingZeros(cl);
			long combined = operator.applyAsLong(ans, tree[cl]);
			if (!tester.test(combined)) {
				while (cl < size) {
					cl <<= 1;
					combined = operator.applyAsLong(ans, tree[cl]);
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

	public int minLeft(final int r, final LongPredicate tester) {
		Objects.checkIndex(r, n + 1);
		if (r == 0) return 0;
		long ans = identity;
		int cr = r + size;
		do {
			cr--;
			while (cr > 1 && (cr & 1) == 1) cr >>= 1;
			long combined = operator.applyAsLong(tree[cr], ans);
			if (!tester.test(combined)) {
				while (cr < size) {
					cr = (cr << 1) | 1;
					combined = operator.applyAsLong(tree[cr], ans);
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

	public long[] toArray() {
		return Arrays.copyOfRange(tree, size, size + n);
	}

	public PrimitiveIterator.OfLong iterator() {
		return new PrimitiveIterator.OfLong() {
			private int i = 0;

			public boolean hasNext() {
				return i < n;
			}

			public long nextLong() {
				if (!hasNext()) throw new NoSuchElementException();
				return tree[size + i++];
			}
		};
	}

	public String toString() {
		if (isEmpty()) return "";
		final StringBuilder sb = new StringBuilder(21 * n - 1);
		sb.append(tree[size]);
		for (int i = 1; i < n; i++) sb.append(' ').append(tree[size + i]);
		return sb.toString();
	}

	private long update(final int idx, final long v) {
		tree[idx] = v;
		for (int j = idx >> 1; j > 0; j >>= 1) tree[j] = operator.applyAsLong(tree[j << 1], tree[(j << 1) | 1]);
		return v;
	}

	private void build() {
		for (int i = size - 1; i > 0; i--) tree[i] = operator.applyAsLong(tree[i << 1], tree[(i << 1) | 1]);
	}
}
