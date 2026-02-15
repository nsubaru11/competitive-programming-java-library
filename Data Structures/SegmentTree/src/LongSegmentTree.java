import java.util.*;
import java.util.function.*;

@SuppressWarnings("unused")
public final class LongSegmentTree implements Iterable<Long> {
	private final int elementCount, leafStart;
	private final long identity;
	private final LongBinaryOperator operator;
	private final long[] tree;
	private final int[] updateQueue;
	private final boolean[] isPending;
	private int queueHead, pendingCount;

	public LongSegmentTree(final int elementCount, final LongBinaryOperator operator, final long identity) {
		this.elementCount = elementCount;
		leafStart = elementCount == 1 ? 0 : Integer.highestOneBit((elementCount - 1) << 1) - 1;
		this.identity = identity;
		this.operator = operator;
		tree = new long[leafStart + elementCount];
		if (identity != 0) Arrays.fill(tree, leafStart, leafStart + elementCount, identity);
		updateQueue = new int[leafStart + 1];
		isPending = new boolean[leafStart];
	}

	public LongSegmentTree(final long[] data, final LongBinaryOperator operator, final long identity) {
		this.elementCount = data.length;
		leafStart = elementCount == 1 ? 0 : Integer.highestOneBit((elementCount - 1) << 1) - 1;
		this.identity = identity;
		this.operator = operator;
		tree = new long[leafStart + elementCount];
		System.arraycopy(data, 0, tree, leafStart, elementCount);
		updateQueue = new int[leafStart + 1];
		isPending = new boolean[leafStart];
		buildAll();
	}

	public long get(final int i) {
		return tree[leafStart + i];
	}

	public void set(final int i, final long e) {
		final int ls = leafStart, idx = ls + i;
		final long[] t = tree;
		final long prev = t[idx];
		t[idx] = e;
		final int p = (idx - 1) >> 1;
		final boolean[] pending = isPending;
		if (prev != e && p >= 0 && !pending[p]) {
			pending[p] = true;
			updateQueue[(queueHead + pendingCount++) & ls] = idx - ((idx & 1) ^ 1);
		}
	}

	public void update(final int i, final LongUnaryOperator func) {
		set(i, func.applyAsLong(tree[leafStart + i]));
	}

	public void fill(final long val) {
		Arrays.fill(tree, leafStart, leafStart + elementCount, val);
		buildAll();
	}

	public void setAll(final LongUnaryOperator func) {
		final long[] t = tree;
		for (int i = 0, idx = leafStart; i < elementCount; i++, idx++) t[idx] = func.applyAsLong(t[idx]);
		buildAll();
	}

	public long query(final int l, final int r) {
		final long id = identity;
		if (l > r) return id;
		if (pendingCount > 0) build();
		final long[] t = tree;
		final int ls = leafStart;
		if (l == r) return t[ls + l];
		int cl = l + ls, cr = r + ls;
		long ans = id;
		final LongBinaryOperator op = operator;
		while (cl <= cr) {
			if ((cl & 1) == 0) ans = op.applyAsLong(ans, t[cl]);
			if ((cr & 1) == 1) ans = op.applyAsLong(ans, t[cr]);
			cl >>= 1;
			cr = (cr - 2) >> 1;
		}
		return ans;
	}

	public long queryAll() {
		if (pendingCount > 0) build();
		return tree[0];
	}

	public int maxRight(final int l, final LongPredicate tester) {
		if (pendingCount > 0) build();
		final int n = elementCount;
		if (l == n) return n;
		final long[] t = tree;
		final int ls = leafStart;
		final LongBinaryOperator op = operator;
		long ans = identity;
		int cl = l + ls;
		do {
			while ((cl & 1) == 1) cl = (cl - 1) >> 1;
			long combined = op.applyAsLong(ans, t[cl]);
			if (!tester.test(combined)) {
				while (cl < ls) {
					cl = (cl << 1) + 1;
					combined = op.applyAsLong(ans, t[cl]);
					if (tester.test(combined)) {
						ans = combined;
						cl++;
					}
				}
				return cl - ls;
			}
			ans = combined;
			cl++;
		} while ((cl & (cl - 1)) != 0);
		return n;
	}

	public int minLeft(final int r, final LongPredicate tester) {
		if (pendingCount > 0) build();
		if (r == 0) return 0;
		final long[] t = tree;
		final int ls = leafStart;
		final LongBinaryOperator op = operator;
		long ans = identity;
		int cr = r + ls - 1;
		do {
			while (cr > 0 && (cr & 1) == 0) cr = (cr - 2) >> 1;
			long combined = op.applyAsLong(t[cr], ans);
			if (!tester.test(combined)) {
				while (cr < ls) {
					cr = (cr << 1) + 2;
					combined = op.applyAsLong(t[cr], ans);
					if (tester.test(combined)) {
						ans = combined;
						cr--;
					}
				}
				return cr - ls + 1;
			}
			ans = combined;
			cr--;
		} while (((cr + 1) & cr) != 0);
		return 0;
	}

	public int size() {
		return elementCount;
	}

	private void build() {
		final long[] t = tree;
		final int ls = leafStart;
		final int[] q = updateQueue;
		final boolean[] pending = isPending;
		final int treeLen = t.length;
		int pc = pendingCount, qh = queueHead;
		while (pc-- > 0) {
			final int pos = qh++ & ls;
			final int left = q[pos], right = left + 1;
			final int parent = left >> 1;
			final long old = t[parent];
			if (right < treeLen) {
				t[parent] = operator.applyAsLong(t[left], t[right]);
			} else {
				t[parent] = t[left];
			}
			pending[parent] = false;
			if (parent > 0 && t[parent] != old) {
				final int p = (parent - 1) >> 1;
				if (!pending[p]) {
					pending[p] = true;
					q[(qh + pc++) & ls] = parent - ((parent & 1) ^ 1);
				}
			}
		}
		pendingCount = 0;
		queueHead = qh;
	}

	private void buildAll() {
		final long[] t = tree;
		final int len = t.length;
		for (int i = leafStart - 1; i >= 0; i--) {
			final int l = (i << 1) + 1, r = l + 1;
			if (r < len) {
				t[i] = operator.applyAsLong(t[l], t[r]);
			} else if (l < len) {
				t[i] = t[l];
			} else {
				t[i] = identity;
			}
		}
		if (pendingCount > 0) {
			queueHead = 0;
			pendingCount = 0;
			Arrays.fill(isPending, false);
		}
	}

	public PrimitiveIterator.OfLong iterator() {
		return new PrimitiveIterator.OfLong() {
			private int idx = 0;

			public boolean hasNext() {
				return idx < elementCount;
			}

			public long nextLong() {
				if (!hasNext()) throw new NoSuchElementException();
				return tree[leafStart + idx++];
			}
		};
	}

	@Override
	public String toString() {
		final StringBuilder s = new StringBuilder();
		s.append(tree[leafStart]);
		for (int i = leafStart + 1; i < tree.length; i++) s.append(' ').append(tree[i]);
		return s.toString();
	}

}
