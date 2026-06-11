import java.util.*;
import java.util.function.*;

@SuppressWarnings("unused")
public final class LongSegmentTree implements Iterable<Long> {
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

	public LongSegmentTree(final long[] data, final LongBinaryOperator operator, final long identity) {
		this.n = data.length;
		size = n <= 1 ? 1 : Integer.highestOneBit(n - 1) << 1;
		this.identity = identity;
		this.operator = operator;
		tree = new long[size << 1];
		System.arraycopy(data, 0, tree, size, n);
		if (identity != 0) Arrays.fill(tree, size + n, size << 1, identity);
		buildAll();
	}

	public long get(final int i) {
		return tree[size + i];
	}

	public long set(final int i, final long e) {
		return apply(i, 0, e);
	}

	public long add(final int i, final long d) {
		return apply(i, 1, d);
	}

	public long multiply(final int i, final long a) {
		return apply(i, a, 0);
	}

	public long apply(final int i, final long a, final long b) {
		final int idx = size + i;
		tree[idx] = tree[idx] * a + b;
		for (int j = idx >> 1; j > 0; j >>= 1) tree[j] = operator.applyAsLong(tree[j << 1], tree[(j << 1) | 1]);
		return tree[idx];
	}

	public long apply(final int i, final long v, final LongBinaryOperator op) {
		return apply(i, 0, op.applyAsLong(tree[size + i], v));
	}

	public void fill(final long val) {
		Arrays.fill(tree, size, size + n, val);
		buildAll();
	}

	public void setAll(final LongUnaryOperator func) {
		for (int i = 0, idx = size; i < n; i++, idx++) tree[idx] = func.applyAsLong(i);
		buildAll();
	}

	public long query(final int l, final int r) {
		if (l >= r) return identity;
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
		if (r == 0) return 0;
		long ans = identity;
		int cr = r + size - 1;
		do {
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
			cr--;
		} while ((cr & -cr) != cr);
		return 0;
	}

	public int size() {
		return n;
	}

	private void buildAll() {
		for (int i = size - 1; i > 0; i--) tree[i] = operator.applyAsLong(tree[i << 1], tree[(i << 1) | 1]);
	}

	public PrimitiveIterator.OfLong iterator() {
		return new PrimitiveIterator.OfLong() {
			private int idx = 0;

			public boolean hasNext() {
				return idx < n;
			}

			public long nextLong() {
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
