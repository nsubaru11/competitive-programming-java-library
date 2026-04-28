import java.util.*;
import java.util.function.*;

@SuppressWarnings("unused")
public final class IntSegmentTree implements Iterable<Integer> {
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

	public IntSegmentTree(final int[] data, final IntBinaryOperator operator, final int identity) {
		this.n = data.length;
		size = n <= 1 ? 1 : Integer.highestOneBit(n - 1) << 1;
		this.identity = identity;
		this.operator = operator;
		tree = new int[size << 1];
		System.arraycopy(data, 0, tree, size, n);
		if (identity != 0) Arrays.fill(tree, size + n, size << 1, identity);
		buildAll();
	}

	public int get(final int i) {
		return tree[size + i];
	}

	public void set(final int i, final int e) {
		final int idx = size + i;
		if (tree[idx] == e) return;
		tree[idx] = e;
		for (int j = idx >> 1; j > 0; j >>= 1) tree[j] = operator.applyAsInt(tree[j << 1], tree[(j << 1) | 1]);
	}

	public void apply(final int i, final int v, final IntBinaryOperator op) {
		set(i, op.applyAsInt(tree[size + i], v));
	}

	public void fill(final int val) {
		Arrays.fill(tree, size, size + n, val);
		buildAll();
	}

	public void setAll(final IntUnaryOperator func) {
		for (int i = 0, idx = size; i < n; i++, idx++) tree[idx] = func.applyAsInt(i);
		buildAll();
	}

	public int query(final int l, final int r) {
		if (l >= r) return identity;
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
		if (r == 0) return 0;
		int ans = identity;
		int cr = r + size - 1;
		do {
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
			cr--;
		} while ((cr & -cr) != cr);
		return 0;
	}

	public int size() {
		return n;
	}

	private void buildAll() {
		for (int i = size - 1; i > 0; i--) tree[i] = operator.applyAsInt(tree[i << 1], tree[(i << 1) | 1]);
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
