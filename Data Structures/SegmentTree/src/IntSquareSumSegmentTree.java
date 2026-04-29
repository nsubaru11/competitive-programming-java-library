import java.util.*;
import java.util.function.*;

@SuppressWarnings("unused")
public final class IntSquareSumSegmentTree implements Iterable<Integer> {
	private static final long IDENTITY_LAZY = 1L << 32;
	private static final int MOD = 998244353;
	private final int n, size, mod, log;
	private final int[] tree, tree2;
	private final long[] lazy;

	public IntSquareSumSegmentTree(final int n) {
		this(n, MOD);
	}

	public IntSquareSumSegmentTree(final int n, final int mod) {
		this.n = n;
		size = n <= 1 ? 1 : Integer.highestOneBit(n - 1) << 1;
		this.mod = mod;
		log = Integer.numberOfTrailingZeros(size);
		tree = new int[size << 1];
		tree2 = new int[size << 1];
		lazy = new long[size << 1];
		Arrays.fill(lazy, IDENTITY_LAZY);
	}

	public IntSquareSumSegmentTree(final int[] data) {
		this(data, MOD);
	}

	public IntSquareSumSegmentTree(final int[] data, final int mod) {
		this(data.length, mod);
		for (int i = 0; i < n; i++) {
			final int v = data[i] % mod;
			tree[size + i] = v;
			tree2[size + i] = (int) ((long) v * v % mod);
		}
		buildAll();
	}

	public int get(final int i) {
		final int idx = size + i;
		pushPath(idx);
		final int v = tree[idx];
		return v < 0 ? v + mod : v;
	}

	public void add(final int i, final int v) {
		apply(i, i + 1, 1, v);
	}

	public void add(int l, int r, final int v) {
		apply(l, r, 1, v);
	}

	public void multiply(final int i, final int v) {
		apply(i, i + 1, v, 0);
	}

	public void multiply(final int l, final int r, final int v) {
		apply(l, r, v, 0);
	}

	public void set(final int i, final int v) {
		apply(i, i + 1, 0, v);
	}

	public void set(final int l, final int r, final int v) {
		apply(l, r, 0, v);
	}

	public void apply(final int i, final int a, final int b) {
		apply(i, i + 1, a, b);
	}

	public void apply(int l, int r, final int a, final int b) {
		if (l >= r) return;
		l += size;
		r += size;
		pushBoundaries(l, r);
		final int l2 = l, r2 = r;
		final int ma = a % mod, mb = b % mod, ma2 = (int) ((long) ma * ma % mod), mb2 = (int) ((long) mb * mb % mod), mab2 = (int) (((long) ma * mb << 1) % mod);
		for (int len = 1; l < r; l >>= 1, r >>= 1, len <<= 1) {
			if ((l & 1) == 1) allApply(l++, ma, mb, ma2, mb2, mab2, len);
			if ((r & 1) == 1) allApply(--r, ma, mb, ma2, mb2, mab2, len);
		}
		updateBoundaries(l2, r2);
	}

	public void fill(final int val) {
		final int v = val % mod;
		Arrays.fill(tree, size, size + n, v);
		Arrays.fill(tree2, size, size + n, (int) ((long) v * v % mod));
		buildAll();
	}

	public void setAll(final IntUnaryOperator func) {
		for (int i = 0, idx = size; i < n; i++, idx++) {
			final int v = func.applyAsInt(i) % mod;
			tree[idx] = v;
			tree2[idx] = (int) ((long) v * v % mod);
		}
		buildAll();
	}

	public int query(int l, int r) {
		if (l >= r) return 0;
		l += size;
		r += size;
		pushBoundaries(l, r);
		long ans = 0;
		for (; l < r; l >>= 1, r >>= 1) {
			if ((l & 1) == 1) ans += tree[l++];
			if ((r & 1) == 1) ans += tree[--r];
		}
		final int v = (int) (ans % mod);
		return v < 0 ? v + mod : v;
	}

	public int query2(int l, int r) {
		if (l >= r) return 0;
		l += size;
		r += size;
		pushBoundaries(l, r);
		long ans = 0;
		for (; l < r; l >>= 1, r >>= 1) {
			if ((l & 1) == 1) ans += tree2[l++];
			if ((r & 1) == 1) ans += tree2[--r];
		}
		final int v = (int) (ans % mod);
		return v < 0 ? v + mod : v;
	}

	public int queryAll() {
		final int v = tree[1];
		return v < 0 ? v + mod : v;
	}

	public int query2All() {
		final int v = tree2[1];
		return v < 0 ? v + mod : v;
	}

	public int size() {
		return n;
	}

	private void buildAll() {
		for (int i = size - 1; i > 0; i--) tree[i] = (int) (((long) tree[i << 1] + tree[i << 1 | 1]) % mod);
		for (int i = size - 1; i > 0; i--) tree2[i] = (int) (((long) tree2[i << 1] + tree2[i << 1 | 1]) % mod);
		Arrays.fill(lazy, IDENTITY_LAZY);
	}

	private void allApply(final int i, final int a, final int b, final int a2, final int b2, final int ab2, final int len) {
		final int before = tree[i];
		tree2[i] = (int) (((long) a2 * tree2[i] + (long) ab2 * before + (long) b2 * len) % mod);
		tree[i] = (int) (((long) a * before + (long) b * len) % mod);
		if (i >= size) return;
		lazy[i] = compose(a, b, lazy[i]);
	}

	private void pushPath(final int idx) {
		for (int i = log; i >= 1; i--) push(idx >> i, 1 << (i - 1));
	}

	private void pushBoundaries(final int l, final int r) {
		for (int i = log; i >= 1; i--) {
			final int len = 1 << (i - 1);
			if (((l >> i) << i) != l) push(l >> i, len);
			if (((r >> i) << i) != r) push((r - 1) >> i, len);
		}
	}

	private void push(final int i, final int len) {
		final long f = lazy[i];
		final int a = (int) (f >>> 32), b = (int) f;
		if (a == 1 && b == 0) return;
		final int a2 = (int) ((long) a * a % mod), b2 = (int) ((long) b * b % mod), ab2 = (int) (((long) a * b << 1) % mod);
		allApply(i << 1, a, b, a2, b2, ab2, len);
		allApply(i << 1 | 1, a, b, a2, b2, ab2, len);
		lazy[i] = IDENTITY_LAZY;
	}

	private void updateBoundaries(final int l, final int r) {
		for (int i = 1; i <= log; i++) {
			if (((l >> i) << i) != l) update(l >> i);
			if (((r >> i) << i) != r) update((r - 1) >> i);
		}
	}

	private void update(final int i) {
		tree[i] = (int) (((long) tree[i << 1] + tree[i << 1 | 1]) % mod);
		tree2[i] = (int) (((long) tree2[i << 1] + tree2[i << 1 | 1]) % mod);
	}

	private long compose(final int newA, final int newB, final long older) {
		final int oldA = (int) (older >>> 32), oldB = (int) older;
		final int composedA = (int) ((long) newA * oldA % mod);
		final int composedB = (int) (((long) newA * oldB + newB) % mod);
		return ((long) composedA << 32) | (composedB & 0xFFFFFFFFL);
	}

	public PrimitiveIterator.OfInt iterator() {
		return new PrimitiveIterator.OfInt() {
			private int idx = 0;

			public boolean hasNext() {
				return idx < n;
			}

			public int nextInt() {
				if (!hasNext()) throw new NoSuchElementException();
				final int v = tree[size + idx++];
				return v < 0 ? v + mod : v;
			}
		};
	}

	@Override
	public String toString() {
		final StringBuilder s = new StringBuilder();
		int v = tree[size];
		s.append(v < 0 ? v + mod : v);
		for (int i = size + 1; i < size + n; i++) {
			v = tree[i];
			s.append(' ').append(v < 0 ? v + mod : v);
		}
		return s.toString();
	}
}
