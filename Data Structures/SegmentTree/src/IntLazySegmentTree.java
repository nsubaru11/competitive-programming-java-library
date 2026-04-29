import java.util.*;
import java.util.function.*;

@SuppressWarnings("unused")
public final class IntLazySegmentTree implements Iterable<Integer> {
	private static final IntBinaryOperator DEFAULT_MAPPING = (f, x) -> f;
	private static final IntBinaryOperator DEFAULT_COMPOSITION = (f, g) -> f;
	private final int n, size, log, identity;
	private final IntBinaryOperator operator, mapping, composition;
	private final int[] tree, lazy;
	private final boolean[] hasLazy;

	public IntLazySegmentTree(final int n, final IntBinaryOperator operator, final int identity) {
		this(n, operator, identity, DEFAULT_MAPPING, DEFAULT_COMPOSITION);
	}

	public IntLazySegmentTree(final int n, final IntBinaryOperator operator, final int identity,
	                          final IntBinaryOperator mapping, final IntBinaryOperator composition) {
		this.n = n;
		size = n <= 1 ? 1 : Integer.highestOneBit(n - 1) << 1;
		this.identity = identity;
		this.operator = operator;
		this.mapping = mapping;
		this.composition = composition;
		log = Integer.numberOfTrailingZeros(size);
		tree = new int[size << 1];
		if (identity != 0) Arrays.fill(tree, identity);
		lazy = new int[size << 1];
		hasLazy = new boolean[size << 1];
	}

	public IntLazySegmentTree(final int[] data, final IntBinaryOperator operator, final int identity) {
		this(data, operator, identity, DEFAULT_MAPPING, DEFAULT_COMPOSITION);
	}

	public IntLazySegmentTree(final int[] data, final IntBinaryOperator operator, final int identity,
	                          final IntBinaryOperator mapping, final IntBinaryOperator composition) {
		this(data.length, operator, identity, mapping, composition);
		System.arraycopy(data, 0, tree, size, n);
		buildAll();
	}

	public int get(final int i) {
		final int idx = size + i;
		pushPath(idx);
		return tree[idx];
	}

	public void set(final int i, final int v) {
		final int idx = size + i;
		pushPath(idx);
		tree[idx] = v;
		for (int j = idx >> 1; j > 0; j >>= 1) update(j);
	}

	public void apply(final int i, final int v) {
		apply(i, i + 1, v);
	}

	public void apply(int l, int r, final int v) {
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

	public void fill(final int val) {
		Arrays.fill(tree, size, size + n, val);
		buildAll();
	}

	public void setAll(final IntUnaryOperator func) {
		for (int i = 0, idx = size; i < n; i++, idx++) tree[idx] = func.applyAsInt(i);
		buildAll();
	}

	public int query(int l, int r) {
		if (l >= r) return identity;
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
		if (r == 0) return 0;
		int ans = identity;
		int cr = r + size - 1;
		pushPath(cr);
		do {
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
			cr--;
		} while ((cr & -cr) != cr);
		return 0;
	}

	public int size() {
		return n;
	}

	private void buildAll() {
		for (int i = size - 1; i > 0; i--) tree[i] = operator.applyAsInt(tree[i << 1], tree[i << 1 | 1]);
		Arrays.fill(hasLazy, false);
	}

	private void allApply(final int i, final int f) {
		tree[i] = mapping.applyAsInt(f, tree[i]);
		if (i >= size) return;
		lazy[i] = hasLazy[i] ? composition.applyAsInt(f, lazy[i]) : f;
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

	private void push(final int i) {
		if (!hasLazy[i]) return;
		final int f = lazy[i];
		allApply(i << 1, f);
		allApply(i << 1 | 1, f);
		hasLazy[i] = false;
	}

	private void updateBoundaries(final int l, final int r) {
		for (int i = 1; i <= log; i++) {
			if (((l >> i) << i) != l) update(l >> i);
			if (((r >> i) << i) != r) update((r - 1) >> i);
		}
	}

	private void update(final int i) {
		tree[i] = operator.applyAsInt(tree[i << 1], tree[i << 1 | 1]);
	}

	public PrimitiveIterator.OfInt iterator() {
		return new PrimitiveIterator.OfInt() {
			private int idx = 0;

			public boolean hasNext() {
				return idx < n;
			}

			public int nextInt() {
				if (!hasNext()) throw new NoSuchElementException();
				return tree[size + idx++];
			}
		};
	}

	@Override
	public String toString() {
		final StringBuilder s = new StringBuilder();
		s.append(tree[size]);
		for (int i = size + 1; i < size + n; i++) s.append(' ').append(tree[i]);
		return s.toString();
	}
}
