import java.util.*;
import java.util.function.*;

@SuppressWarnings({"unused", "unchecked"})
public final class SegmentTree<T> implements Iterable<T> {
	private final int elementCount, leafStart;
	private final T identity;
	private final BinaryOperator<T> operator;
	private final T[] tree;
	private final int[] updateQueue;
	private final boolean[] isPending;
	private int queueHead, pendingCount;

	public SegmentTree(final int elementCount, final BinaryOperator<T> operator, final T identity) {
		this.elementCount = elementCount;
		leafStart = elementCount == 1 ? 0 : Integer.highestOneBit((elementCount - 1) << 1) - 1;
		this.identity = identity;
		this.operator = operator;
		tree = (T[]) new Object[leafStart + elementCount];
		if (identity != null) Arrays.fill(tree, leafStart, leafStart + elementCount, identity);
		updateQueue = new int[leafStart + 1];
		isPending = new boolean[leafStart];
	}

	public SegmentTree(final T[] data, final BinaryOperator<T> operator, final T identity) {
		this.elementCount = data.length;
		leafStart = elementCount == 1 ? 0 : Integer.highestOneBit((elementCount - 1) << 1) - 1;
		this.identity = identity;
		this.operator = operator;
		tree = (T[]) new Object[leafStart + elementCount];
		System.arraycopy(data, 0, tree, leafStart, elementCount);
		updateQueue = new int[leafStart + 1];
		isPending = new boolean[leafStart];
		buildAll();
	}

	public T get(final int i) {
		return tree[leafStart + i];
	}

	public void set(final int i, final T e) {
		final int ls = leafStart;
		final int idx = ls + i;
		final T[] t = tree;
		final T prev = t[idx];
		t[idx] = e;
		final int p = (idx - 1) >> 1;
		final boolean[] pending = isPending;
		if (!Objects.equals(prev, e) && p >= 0 && !pending[p]) {
			pending[p] = true;
			updateQueue[(queueHead + pendingCount++) & ls] = idx - ((idx & 1) ^ 1);
		}
	}

	public void update(final int i, final UnaryOperator<T> func) {
		set(i, func.apply(tree[leafStart + i]));
	}

	public void fill(final T val) {
		Arrays.fill(tree, leafStart, leafStart + elementCount, val);
		buildAll();
	}

	public void setAll(final UnaryOperator<T> func) {
		final T[] t = tree;
		for (int i = 0, idx = leafStart; i < elementCount; i++, idx++) t[idx] = func.apply(t[idx]);
		buildAll();
	}

	public T query(final int l, final int r) {
		final T id = identity;
		if (l > r) return id;
		if (pendingCount > 0) build();
		final T[] t = tree;
		final int ls = leafStart;
		if (l == r) return t[ls + l];
		int cl = l + ls, cr = r + ls;
		T ans = id;
		final BinaryOperator<T> op = operator;
		while (cl <= cr) {
			if ((cl & 1) == 0) ans = op.apply(ans, t[cl]);
			if ((cr & 1) == 1) ans = op.apply(ans, t[cr]);
			cl >>= 1;
			cr = (cr - 2) >> 1;
		}
		return ans;
	}

	public T queryAll() {
		if (pendingCount > 0) build();
		return tree[0];
	}

	public int maxRight(final int l, final Predicate<T> tester) {
		if (pendingCount > 0) build();
		final int n = elementCount;
		if (l == n) return n;
		final T[] t = tree;
		final int ls = leafStart;
		final BinaryOperator<T> op = operator;
		T ans = identity;
		int cl = l + ls;
		do {
			while ((cl & 1) == 1) cl = (cl - 1) >> 1;
			final T combined = op.apply(ans, t[cl]);
			if (!tester.test(combined)) {
				while (cl < ls) {
					cl = (cl << 1) + 1;
					final T combinedLeft = op.apply(ans, t[cl]);
					if (tester.test(combinedLeft)) {
						ans = combinedLeft;
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

	public int minLeft(final int r, final Predicate<T> tester) {
		if (pendingCount > 0) build();
		if (r == 0) return 0;
		final T[] t = tree;
		final int ls = leafStart;
		final BinaryOperator<T> op = operator;
		T ans = identity;
		int cr = r + ls - 1;
		do {
			while (cr > 0 && (cr & 1) == 0) cr = (cr - 2) >> 1;
			final T combined = op.apply(t[cr], ans);
			if (!tester.test(combined)) {
				while (cr < ls) {
					cr = (cr << 1) + 2;
					final T combinedRight = op.apply(t[cr], ans);
					if (tester.test(combinedRight)) {
						ans = combinedRight;
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
		final T[] t = tree;
		final int ls = leafStart;
		final int[] q = updateQueue;
		final boolean[] pending = isPending;
		final int treeLen = t.length;
		int pc = pendingCount, qh = queueHead;
		while (pc-- > 0) {
			final int pos = qh++ & ls;
			final int left = q[pos], right = left + 1;
			final int parent = left >> 1;
			final T old = t[parent];
			if (right < treeLen) {
				t[parent] = operator.apply(t[left], t[right]);
			} else {
				t[parent] = t[left];
			}
			pending[parent] = false;
			if (parent > 0 && !Objects.equals(old, t[parent])) {
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
		final T[] t = tree;
		final int len = t.length;
		for (int i = leafStart - 1; i >= 0; i--) {
			final int l = (i << 1) + 1, r = l + 1;
			if (r < len) {
				t[i] = operator.apply(t[l], t[r]);
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

	public Iterator<T> iterator() {
		return new Iterator<>() {
			private int idx = 0;

			public boolean hasNext() {
				return idx < elementCount;
			}

			public T next() {
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
