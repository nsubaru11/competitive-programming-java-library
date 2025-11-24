import java.util.*;
import java.util.function.*;

@SuppressWarnings("unused")
public final class SegmentTree<T> implements Iterable<T> {
	private final int n, leafStart;
	private final T[] tree;
	private final T identity;
	private final BiFunction<T, T, T> func;
	private final int[] updateList;
	private final boolean[] pending;
	private int head, updateCnt;

	public SegmentTree(final int n, final BiFunction<T, T, T> func, final T identity) {
		this.n = n;
		leafStart = n == 1 ? 0 : Integer.highestOneBit((n - 1) << 1) - 1;
		tree = (T[]) new Object[leafStart + n];
		this.func = func;
		this.identity = identity;
		updateList = new int[leafStart + 1];
		pending = new boolean[leafStart];
	}

	public T get(final int i) {
		if (i < 0 || i >= n) throw new IndexOutOfBoundsException();
		return tree[leafStart + i];
	}

	public void set(final int i, final T e) {
		int idx = leafStart + i;
		tree[idx] = e;
		int p = (idx - 1) >> 1;
		if (p >= 0 && !pending[p]) {
			pending[p] = true;
			updateList[(head + updateCnt++) & leafStart] = idx - ((idx & 1) ^ 1);
		}
	}

	public T query(int l, int r) {
		if (updateCnt > 0) build();
		l += leafStart;
		r += leafStart;
		T ans = identity;
		while (l <= r) {
			if ((l & 1) == 0) ans = func.apply(ans, tree[l]);
			if ((r & 1) == 1) ans = func.apply(ans, tree[r]);
			l >>= 1;
			r = (r - 2) >> 1;
		}
		return ans;
	}

	public void build() {
		while (updateCnt-- > 0) {
			int pos = head++ & leafStart;
			int left = updateList[pos];
			int right = left + 1;
			int parent = left >> 1;
			T old = tree[parent];
			if (right < tree.length) {
				tree[parent] = func.apply(tree[left], tree[right]);
			} else {
				tree[parent] = tree[left];
			}
			pending[parent] = false;
			if (parent > 0 && !Objects.equals(old, tree[parent])) {
				int p = (parent - 1) >> 1;
				if (!pending[p]) {
					pending[p] = true;
					updateList[(head + updateCnt++) & leafStart] = parent - ((parent & 1) ^ 1);
				}
			}
		}
		updateCnt = 0;
	}

	public Iterator<T> iterator() {
		return new Iterator<>() {
			private int idx = 0;

			public boolean hasNext() {
				return idx < n;
			}

			public T next() {
				if (!hasNext()) throw new NoSuchElementException();
				return tree[leafStart + idx++];
			}
		};
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		for (int i = 1, idx = 0; i < leafStart; i <<= 1) {
			s.append('[').append(tree[i - 1]);
			for (int j = 0; j < i - 1; j++) {
				s.append(", ").append(tree[i + j]);
			}
			s.append("]\n");
		}
		s.append('[').append(tree[leafStart]);
		for (int i = 1; i < n; i++) {
			s.append(", ").append(tree[leafStart + i]);
		}
		s.append("]");
		return s.toString();
	}

}
