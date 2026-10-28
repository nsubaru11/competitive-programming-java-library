package lib.ds.segmenttree;

import java.util.*;
import java.util.function.*;

import lib.ds.*;

/**
 * int値の一次和と二乗和を法 mod で管理するセグメント木。区間アフィン変換・区間和・区間二乗和を O(log n) で行う。
 * 添字は0始まりとする。区間を受け取る操作は半開区間 {@code [l, r)} を扱い、{@code l >= r} は空区間とする。
 * 空区間の query・query2 は0を返し、空区間の更新は何もしない。それ以外の範囲外の添字・区間では IndexOutOfBoundsException を投げる。
 * affine は {@code 現在値 * m + d}、add は {@code 現在値 + d}、multiply は {@code 現在値 * m}、区間の set は v へ更新する。
 * 取得値と戻り値は {@code [0, mod)} に正規化する。単一点の set は作用を反映した更新前の値、単一点の add・multiply・affine は更新後の値を返す。
 * mod の既定値は998244353とし、{@code 1 <= mod <= 2 * 10^9} とする。
 */
public final class IntSquareSumSegmentTree implements IntCollection {
	private static final long IDENTITY_LAZY = 1L << 32;
	private static final int DEFAULT_MOD = 998244353;
	private final int n, size, log, mod;
	private final int[] tree, tree2;
	private final long[] lazy;

	public IntSquareSumSegmentTree(final int n) {
		this(n, DEFAULT_MOD);
	}

	public IntSquareSumSegmentTree(final int n, final int mod) {
		this.n = n;
		size = n <= 1 ? 1 : Integer.highestOneBit(n - 1) << 1;
		log = Integer.numberOfTrailingZeros(size);
		this.mod = mod;
		tree = new int[size << 1];
		tree2 = new int[size << 1];
		lazy = new long[size];
		Arrays.fill(lazy, IDENTITY_LAZY);
	}

	public IntSquareSumSegmentTree(final int[] a) {
		this(a, DEFAULT_MOD);
	}

	public IntSquareSumSegmentTree(final int[] a, final int mod) {
		this(a.length, mod);
		for (int i = 0, idx = size; i < n; i++, idx++) setLeaf(idx, a[i] % mod);
		build();
	}

	public IntSquareSumSegmentTree(final IntCollection a) {
		this(a, DEFAULT_MOD);
	}

	public IntSquareSumSegmentTree(final IntCollection a, final int mod) {
		this(a.size(), mod);
		final PrimitiveIterator.OfInt it = a.iterator();
		for (int i = 0, idx = size; i < n; i++, idx++) setLeaf(idx, it.nextInt() % mod);
		build();
	}

	public IntSquareSumSegmentTree(final IntSquareSumSegmentTree other) {
		n = other.n;
		size = other.size;
		log = other.log;
		mod = other.mod;
		tree = other.tree.clone();
		tree2 = other.tree2.clone();
		lazy = other.lazy.clone();
	}

	public int size() {
		return n;
	}

	public int get(final int i) {
		final int idx = size + Objects.checkIndex(i, n);
		pushPath(idx);
		return normalize(tree[idx]);
	}

	public int set(final int i, final int v) {
		final int idx = size + Objects.checkIndex(i, n);
		pushPath(idx);
		final int old = tree[idx];
		setLeaf(idx, v % mod);
		updatePath(idx);
		return normalize(old);
	}

	public void set(final int l, final int r, final int v) {
		affine(l, r, 0, v);
	}

	public int add(final int i, final int d) {
		return affine(i, 1, d);
	}

	public void add(final int l, final int r, final int d) {
		affine(l, r, 1, d);
	}

	public int multiply(final int i, final int m) {
		return affine(i, m, 0);
	}

	public void multiply(final int l, final int r, final int m) {
		affine(l, r, m, 0);
	}

	public int affine(final int i, final int m, final int d) {
		final int idx = size + Objects.checkIndex(i, n);
		pushPath(idx);
		final int v = (int) (((long) (m % mod) * tree[idx] + d % mod) % mod);
		setLeaf(idx, v);
		updatePath(idx);
		return normalize(v);
	}

	public void affine(int l, int r, final int m, final int d) {
		if (l >= r) return;
		Objects.checkFromToIndex(l, r, n);
		l += size;
		r += size;
		pushBoundaries(l, r);
		final int l2 = l, r2 = r;
		final int a = m % mod, b = d % mod, a2 = (int) ((long) a * a % mod), b2 = (int) ((long) b * b % mod), ab2 = (int) (((long) a * b << 1) % mod);
		for (int len = 1; l < r; l >>= 1, r >>= 1, len <<= 1) {
			if ((l & 1) == 1) allApply(l++, a, b, a2, b2, ab2, len);
			if ((r & 1) == 1) allApply(--r, a, b, a2, b2, ab2, len);
		}
		updateBoundaries(l2, r2);
	}

	public IntSquareSumSegmentTree fill(final int v) {
		final int x = v % mod, x2 = (int) ((long) x * x % mod);
		Arrays.fill(tree, size, size + n, x);
		Arrays.fill(tree2, size, size + n, x2);
		build();
		return this;
	}

	public IntSquareSumSegmentTree setAll(final IntUnaryOperator init) {
		for (int i = 0, idx = size; i < n; i++, idx++) setLeaf(idx, init.applyAsInt(i) % mod);
		build();
		return this;
	}

	public int query(int l, int r) {
		if (l >= r) return 0;
		Objects.checkFromToIndex(l, r, n);
		l += size;
		r += size;
		pushBoundaries(l, r);
		long ans = 0;
		for (; l < r; l >>= 1, r >>= 1) {
			if ((l & 1) == 1) ans += tree[l++];
			if ((r & 1) == 1) ans += tree[--r];
		}
		return normalize((int) (ans % mod));
	}

	public int query2(int l, int r) {
		if (l >= r) return 0;
		Objects.checkFromToIndex(l, r, n);
		l += size;
		r += size;
		pushBoundaries(l, r);
		long ans = 0;
		for (; l < r; l >>= 1, r >>= 1) {
			if ((l & 1) == 1) ans += tree2[l++];
			if ((r & 1) == 1) ans += tree2[--r];
		}
		return normalize((int) (ans % mod));
	}

	public int queryAll() {
		return normalize(tree[1]);
	}

	public int query2All() {
		return normalize(tree2[1]);
	}

	public int[] toArray() {
		pushAll();
		final int[] a = new int[n];
		for (int i = 0; i < n; i++) a[i] = normalize(tree[size + i]);
		return a;
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
				return normalize(tree[size + i++]);
			}
		};
	}

	public String toString() {
		if (isEmpty()) return "";
		pushAll();
		final StringBuilder sb = new StringBuilder(11 * n - 1);
		sb.append(normalize(tree[size]));
		for (int i = 1; i < n; i++) sb.append(' ').append(normalize(tree[size + i]));
		return sb.toString();
	}

	private int normalize(final int v) {
		return v < 0 ? v + mod : v;
	}

	private void setLeaf(final int idx, final int x) {
		tree[idx] = x;
		tree2[idx] = (int) ((long) x * x % mod);
	}

	private void build() {
		for (int i = size - 1; i > 0; i--) update(i);
		Arrays.fill(lazy, IDENTITY_LAZY);
	}

	private void update(final int i) {
		tree[i] = (int) (((long) tree[i << 1] + tree[i << 1 | 1]) % mod);
		tree2[i] = (int) (((long) tree2[i << 1] + tree2[i << 1 | 1]) % mod);
	}

	private void allApply(final int i, final int a, final int b, final int a2, final int b2, final int ab2, final int len) {
		final int old = tree[i];
		tree2[i] = (int) (((long) a2 * tree2[i] + (long) ab2 * old + (long) b2 * len) % mod);
		tree[i] = (int) (((long) a * old + (long) b * len) % mod);
		if (i >= size) return;
		final long f = lazy[i];
		final int oldA = (int) (f >>> 32), oldB = (int) f;
		final int newA = (int) ((long) a * oldA % mod), newB = (int) (((long) a * oldB + b) % mod);
		lazy[i] = ((long) newA << 32) | (newB & 0xFFFFFFFFL);
	}

	private void push(final int i, final int len) {
		final long f = lazy[i];
		if (f == IDENTITY_LAZY) return;
		final int a = (int) (f >>> 32), b = (int) f;
		final int a2 = (int) ((long) a * a % mod), b2 = (int) ((long) b * b % mod), ab2 = (int) (((long) a * b << 1) % mod);
		allApply(i << 1, a, b, a2, b2, ab2, len);
		allApply(i << 1 | 1, a, b, a2, b2, ab2, len);
		lazy[i] = IDENTITY_LAZY;
	}

	private void pushAll() {
		for (int i = 1; i < size; i++) push(i, size >> (32 - Integer.numberOfLeadingZeros(i)));
	}

	private void pushPath(final int idx) {
		for (int i = log; i >= 1; i--) push(idx >> i, 1 << (i - 1));
	}

	private void updatePath(final int idx) {
		for (int i = idx >> 1; i > 0; i >>= 1) update(i);
	}

	private void pushBoundaries(final int l, final int r) {
		for (int i = log; i >= 1; i--) {
			final int len = 1 << (i - 1);
			if (((l >> i) << i) != l) push(l >> i, len);
			if (((r >> i) << i) != r) push((r - 1) >> i, len);
		}
	}

	private void updateBoundaries(final int l, final int r) {
		for (int i = 1; i <= log; i++) {
			if (((l >> i) << i) != l) update(l >> i);
			if (((r >> i) << i) != r) update((r - 1) >> i);
		}
	}
}
