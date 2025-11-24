import java.io.*;
import java.util.*;
import java.util.function.*;

import static java.util.Arrays.*;

// https://judge.yosupo.jp/problem/point_add_range_sum
public final class Example {

	// region initialization
	// ------------------------ 定数 ------------------------
	private static final boolean DEBUG;
	private static final Scanner sc;
	private static final PrintWriter out;

	static {
		DEBUG = true;
		sc = new Scanner(System.in);
		out = new PrintWriter(System.out);
	}
	// endregion

	// ------------------------ メインロジック ------------------------
	private static void solve() {
		int n = sc.nextInt();
		int q = sc.nextInt();
		LongSegmentTree tree = new LongSegmentTree(n, Long::sum);
		for (int i = 0; i < n; i++) tree.set(i, sc.nextInt());
		while (q-- > 0) {
			int t = sc.nextInt();
			int a = sc.nextInt();
			int b = sc.nextInt();
			if (t == 0) {
				tree.set(a, tree.get(a) + b);
			} else {
				out.println(tree.query(a, b - 1));
			}
		}
	}

	// region main() and debug() methods
	// ------------------------ main() 関数 ------------------------
	public static void main(final String[] args) {
		try {
			solve();
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			sc.close();
			out.close();
		}
	}

	// ------------------------ デバッグ用 ------------------------
	private static void debug(final Object... args) {
		if (DEBUG) {
			out.flush();
			System.err.println(deepToString(args));
		}
	}
	// endregion

	@SuppressWarnings("unused")
	private static final class LongSegmentTree implements Iterable<Long> {
		private final int n, leafStart;
		private final long[] tree;
		private final LongBinaryOperator func;
		private final int[] updateList;
		private final boolean[] pending;
		private int head, updateCnt;

		public LongSegmentTree(final int n, final LongBinaryOperator func) {
			this.n = n;
			leafStart = n == 1 ? 0 : Integer.highestOneBit((n - 1) << 1) - 1;
			tree = new long[leafStart + n];
			this.func = func;
			updateList = new int[leafStart + 1];
			pending = new boolean[leafStart];
		}

		public long get(final int i) {
			if (i < 0 || i >= n) throw new IndexOutOfBoundsException();
			return tree[leafStart + i];
		}

		public void set(final int i, final long e) {
			int idx = leafStart + i;
			tree[idx] = e;
			int p = (idx - 1) >> 1;
			if (p >= 0 && !pending[p]) {
				pending[p] = true;
				enqueueLeftChild(idx);
			}
		}

		public long query(int l, int r) {
			if (updateCnt > 0) build();
			l += leafStart;
			r += leafStart;
			long ans = 0;
			boolean first = true;
			while (l <= r) {
				if ((l & 1) == 0) {
					if (first) {
						ans = tree[l];
						first = false;
					} else ans = func.applyAsLong(ans, tree[l]);
				}
				if ((r & 1) == 1) {
					if (first) {
						ans = tree[r];
						first = false;
					} else ans = func.applyAsLong(ans, tree[r]);
				}
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
				long old = tree[parent];
				if (right < tree.length) {
					tree[parent] = func.applyAsLong(tree[left], tree[right]);
				} else {
					tree[parent] = tree[left];
				}
				pending[parent] = false;
				if (parent > 0 && tree[parent] != old) {
					int p = (parent - 1) >> 1;
					if (!pending[p]) {
						pending[p] = true;
						enqueueLeftChild(parent);
					}
				}
			}
			updateCnt = 0;
		}

		private void enqueueLeftChild(final int idx) {
			updateList[(head + updateCnt++) & leafStart] = idx - ((idx & 1) ^ 1);
		}

		public PrimitiveIterator.OfLong iterator() {
			return new PrimitiveIterator.OfLong() {
				private int idx = 0;

				public boolean hasNext() {
					return idx < n;
				}

				public long nextLong() {
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
}
