import java.util.*;
import java.util.function.*;

@SuppressWarnings("unused")
public final class IntSegmentTree implements Iterable<Integer> {
	private final int elementCount, leafStart, identity;
	private final IntBinaryOperator operator;
	private final int[] tree, updateQueue;
	private final boolean[] isPending;
	private int queueHead, pendingCount;

	public IntSegmentTree(final int elementCount, final IntBinaryOperator operator, final int identity) {
		this.elementCount = elementCount;
		leafStart = elementCount == 1 ? 0 : Integer.highestOneBit((elementCount - 1) << 1) - 1;
		this.identity = identity;
		this.operator = operator;
		tree = new int[leafStart + elementCount];
		if (identity != 0) Arrays.fill(tree, leafStart, leafStart + elementCount, identity);
		updateQueue = new int[leafStart + 1];
		isPending = new boolean[leafStart];
	}

	public IntSegmentTree(final int[] data, final IntBinaryOperator operator, final int identity) {
		this.elementCount = data.length;
		leafStart = elementCount == 1 ? 0 : Integer.highestOneBit((elementCount - 1) << 1) - 1;
		this.identity = identity;
		this.operator = operator;
		tree = new int[leafStart + elementCount];
		System.arraycopy(data, 0, tree, leafStart, elementCount);
		updateQueue = new int[leafStart + 1];
		isPending = new boolean[leafStart];
		buildAll();
	}

	public int get(final int i) {
		return tree[leafStart + i];
	}

	public void set(final int i, final int e) {
		final int ls = leafStart, idx = ls + i;
		final int[] t = tree;
		final int prev = t[idx];
		t[idx] = e;
		final int p = (idx - 1) >> 1;
		final boolean[] pending = isPending;
		if (prev != e && p >= 0 && !pending[p]) {
			pending[p] = true;
			updateQueue[(queueHead + pendingCount++) & ls] = idx - ((idx & 1) ^ 1);
		}
	}

	public void update(final int i, final IntUnaryOperator func) {
		set(i, func.applyAsInt(tree[leafStart + i]));
	}

	public void fill(final int val) {
		Arrays.fill(tree, leafStart, leafStart + elementCount, val);
		buildAll();
	}

	public void setAll(final IntUnaryOperator func) {
		final int[] t = tree;
		for (int i = 0, idx = leafStart; i < elementCount; i++, idx++) t[idx] = func.applyAsInt(t[idx]);
		buildAll();
	}

	public int query(final int l, final int r) {
		final int id = identity;
		if (l > r) return id;
		if (pendingCount > 0) build();
		final int[] t = tree;
		final int ls = leafStart;
		if (l == r) return t[ls + l];
		int cl = l + ls, cr = r + ls;
		int ans = id;
		final IntBinaryOperator op = operator;
		while (cl <= cr) {
			if ((cl & 1) == 0) ans = op.applyAsInt(ans, t[cl]);
			if ((cr & 1) == 1) ans = op.applyAsInt(ans, t[cr]);
			cl >>= 1;
			cr = (cr - 2) >> 1;
		}
		return ans;
	}

	public int queryAll() {
		if (pendingCount > 0) build();
		return tree[0];
	}

	public int maxRight(final int l, final IntPredicate tester) {
		if (pendingCount > 0) build();
		final int n = elementCount;
		if (l == n) return n;
		final int[] t = tree;
		final int ls = leafStart;
		final IntBinaryOperator op = operator;
		int ans = identity;
		int cl = l + ls;
		do {
			while ((cl & 1) == 1) cl = (cl - 1) >> 1;
			int combined = op.applyAsInt(ans, t[cl]);
			if (!tester.test(combined)) {
				while (cl < ls) {
					cl = (cl << 1) + 1;
					combined = op.applyAsInt(ans, t[cl]);
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

	public int minLeft(final int r, final IntPredicate tester) {
		if (pendingCount > 0) build();
		if (r == 0) return 0;
		final int[] t = tree;
		final int ls = leafStart;
		final IntBinaryOperator op = operator;
		int ans = identity;
		int cr = r + ls - 1;
		do {
			while (cr > 0 && (cr & 1) == 0) cr = (cr - 2) >> 1;
			int combined = op.applyAsInt(t[cr], ans);
			if (!tester.test(combined)) {
				while (cr < ls) {
					cr = (cr << 1) + 2;
					combined = op.applyAsInt(t[cr], ans);
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
		final int[] t = tree;
		final int ls = leafStart;
		final int[] q = updateQueue;
		final boolean[] pending = isPending;
		final int treeLen = t.length;
		int pc = pendingCount, qh = queueHead;
		while (pc-- > 0) {
			final int pos = qh++ & ls;
			final int left = q[pos], right = left + 1;
			final int parent = left >> 1;
			final int old = t[parent];
			if (right < treeLen) {
				t[parent] = operator.applyAsInt(t[left], t[right]);
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
		final int[] t = tree;
		final int len = t.length;
		for (int i = leafStart - 1; i >= 0; i--) {
			final int l = (i << 1) + 1, r = l + 1;
			if (r < len) {
				t[i] = operator.applyAsInt(t[l], t[r]);
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

	public PrimitiveIterator.OfInt iterator() {
		return new PrimitiveIterator.OfInt() {
			private int idx = 0;

			public boolean hasNext() {
				return idx < elementCount;
			}

			public int nextInt() {
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
