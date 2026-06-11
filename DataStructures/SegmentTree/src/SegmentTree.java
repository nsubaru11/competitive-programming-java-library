import java.util.*;
import java.util.function.*;

@SuppressWarnings({"unused", "unchecked"})
public final class SegmentTree<T> implements Iterable<T> {
	private final int n, size;
	private final T identity;
	private final BinaryOperator<T> operator;
	private final T[] tree;

	public SegmentTree(final int n, final BinaryOperator<T> operator, final T identity) {
		this.n = n;
		size = n <= 1 ? 1 : Integer.highestOneBit(n - 1) << 1;
		this.identity = identity;
		this.operator = operator;
		tree = (T[]) new Object[size << 1];
		if (identity != null) Arrays.fill(tree, identity);
	}

	public SegmentTree(final T[] data, final BinaryOperator<T> operator, final T identity) {
		this.n = data.length;
		size = n <= 1 ? 1 : Integer.highestOneBit(n - 1) << 1;
		this.identity = identity;
		this.operator = operator;
		tree = (T[]) new Object[size << 1];
		System.arraycopy(data, 0, tree, size, n);
		if (identity != null) Arrays.fill(tree, size + n, size << 1, identity);
		buildAll();
	}

	public T get(final int i) {
		return tree[size + i];
	}

	public T set(final int i, final T e) {
		final int idx = size + i;
		if (Objects.equals(tree[idx], e)) return e;
		tree[idx] = e;
		for (int j = idx >> 1; j > 0; j >>= 1) tree[j] = operator.apply(tree[j << 1], tree[(j << 1) | 1]);
		return tree[idx];
	}

	public T apply(final int i, final T v, final BinaryOperator<T> op) {
		return set(i, op.apply(tree[size + i], v));
	}

	public void fill(final T val) {
		Arrays.fill(tree, size, size + n, val);
		buildAll();
	}

	public void setAll(final IntFunction<T> func) {
		for (int i = 0, idx = size; i < n; i++, idx++) tree[idx] = func.apply(i);
		buildAll();
	}

	public T query(final int l, final int r) {
		if (l >= r) return identity;
		T sml = identity, smr = identity;
		for (int cl = l + size, cr = r + size; cl < cr; cl >>= 1, cr >>= 1) {
			if ((cl & 1) == 1) sml = operator.apply(sml, tree[cl++]);
			if ((cr & 1) == 1) smr = operator.apply(tree[--cr], smr);
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
		do {
			cl >>= Integer.numberOfTrailingZeros(cl);
			T combined = operator.apply(ans, tree[cl]);
			if (!tester.test(combined)) {
				while (cl < size) {
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
		do {
			while (cr > 1 && (cr & 1) == 1) cr >>= 1;
			T combined = operator.apply(tree[cr], ans);
			if (!tester.test(combined)) {
				while (cr < size) {
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
		for (int i = size - 1; i > 0; i--) tree[i] = operator.apply(tree[i << 1], tree[(i << 1) | 1]);
	}

	public Iterator<T> iterator() {
		return new Iterator<>() {
			private int idx = 0;

			public boolean hasNext() {
				return idx < n;
			}

			public T next() {
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
