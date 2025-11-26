import java.util.*;
import java.util.function.*;

@SuppressWarnings({"unused", "unchecked"})
public final class SegmentTree<T> implements Iterable<T> {
	private final int elementCount, leafStart;
	private final T identity;
	private final BiFunction<T, T, T> operator;
	private final T[] tree;
	private final int[] updateQueue;
	private final boolean[] isPending;
	private int queueHead, pendingCount;

	public SegmentTree(final int elementCount, final BiFunction<T, T, T> operator, final T identity) {
		this.elementCount = elementCount;
		leafStart = elementCount == 1 ? 0 : Integer.highestOneBit((elementCount - 1) << 1) - 1;
		this.identity = identity;
		this.operator = operator;
		tree = (T[]) new Object[leafStart + elementCount];
		if (identity != null) Arrays.fill(tree, leafStart, leafStart + elementCount, identity);
		updateQueue = new int[leafStart + 1];
		isPending = new boolean[leafStart];
	}

	public SegmentTree(final T[] data, final BiFunction<T, T, T> operator, final T identity) {
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
		final int idx = leafStart + i;
		final T prev = tree[idx];
		tree[idx] = e;
		final int p = (idx - 1) >> 1;
		if (!Objects.equals(prev, e) && p >= 0 && !isPending[p]) {
			isPending[p] = true;
			updateQueue[(queueHead + pendingCount++) & leafStart] = idx - ((idx & 1) ^ 1);
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
		for (int i = 0, idx = leafStart; i < elementCount; i++, idx++) tree[idx] = func.apply(tree[idx]);
		buildAll();
	}

	public T query(int l, int r) {
		if (pendingCount > 0) build();
		l += leafStart;
		r += leafStart;
		T ans = identity;
		while (l <= r) {
			if ((l & 1) == 0) ans = operator.apply(ans, tree[l]);
			if ((r & 1) == 1) ans = operator.apply(ans, tree[r]);
			l >>= 1;
			r = (r - 2) >> 1;
		}
		return ans;
	}

	public T queryAll() {
		if (pendingCount > 0) build();
		return tree[0];
	}

	public int maxRight(int l, final Predicate<T> tester) {
		if (pendingCount > 0) build();
		if (l == elementCount) return elementCount;
		T ans = identity;
		l += leafStart;
		do {
			while ((l & 1) == 1) l = (l - 1) >> 1;
			T combined = operator.apply(ans, tree[l]);
			if (!tester.test(combined)) {
				while (l < leafStart) {
					l = (l << 1) + 1;
					combined = operator.apply(ans, tree[l]);
					if (tester.test(combined)) {
						ans = combined;
						l++;
					}
				}
				return l - leafStart;
			}
			ans = combined;
			l++;
		} while ((l & (l - 1)) != 0);
		return elementCount;
	}

	public int minLeft(int r, final Predicate<T> tester) {
		if (pendingCount > 0) build();
		if (r == 0) return 0;
		T ans = identity;
		r += leafStart - 1;
		do {
			while (r > 0 && (r & 1) == 0) r = (r - 2) >> 1;
			T combined = operator.apply(tree[r], ans);
			if (!tester.test(combined)) {
				while (r < leafStart) {
					r = (r << 1) + 2;
					combined = operator.apply(tree[r], ans);
					if (tester.test(combined)) {
						ans = combined;
						r--;
					}
				}
				return r - leafStart + 1;
			}
			ans = combined;
			r--;
		} while (((r + 1) & r) != 0);
		return 0;
	}

	public int size() {
		return elementCount;
	}

	private void build() {
		while (pendingCount-- > 0) {
			final int pos = queueHead++ & leafStart;
			final int left = updateQueue[pos];
			final int right = left + 1;
			final int parent = left >> 1;
			final T old = tree[parent];
			if (right < tree.length) {
				tree[parent] = operator.apply(tree[left], tree[right]);
			} else {
				tree[parent] = tree[left];
			}
			isPending[parent] = false;
			if (parent > 0 && !Objects.equals(old, tree[parent])) {
				final int p = (parent - 1) >> 1;
				if (!isPending[p]) {
					isPending[p] = true;
					updateQueue[(queueHead + pendingCount++) & leafStart] = parent - ((parent & 1) ^ 1);
				}
			}
		}
		pendingCount = 0;
	}

	private void buildAll() {
		final int len = tree.length;
		for (int i = leafStart - 1; i >= 0; i--) {
			final int l = (i << 1) + 1, r = l + 1;
			if (r < len) {
				tree[i] = operator.apply(tree[l], tree[r]);
			} else if (l < len) {
				tree[i] = tree[l];
			} else {
				tree[i] = identity;
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
