import java.util.*;
import java.util.function.*;

@SuppressWarnings("unused")
public final class LongSquareSumSegmentTree implements Iterable<Long> {
	private static final long MOD = 998244353;
	private final int n, size, log;
	private final long mod;
	private final long[] tree, tree2, lazyA, lazyB;

	public LongSquareSumSegmentTree(final int n) {
		this(n, MOD);
	}

	public LongSquareSumSegmentTree(final int n, final long mod) {
		this.n = n;
		size = n <= 1 ? 1 : Integer.highestOneBit(n - 1) << 1;
		this.mod = mod;
		log = Integer.numberOfTrailingZeros(size);
		tree = new long[size << 1];
		tree2 = new long[size << 1];
		lazyA = new long[size << 1];
		lazyB = new long[size << 1];
		Arrays.fill(lazyA, 1);
	}

	public LongSquareSumSegmentTree(final long[] data) {
		this(data, MOD);
	}

	public LongSquareSumSegmentTree(final long[] data, final long mod) {
		this(data.length, mod);
		for (int i = 0; i < n; i++) {
			final long v = data[i] % mod;
			tree[size + i] = v;
			tree2[size + i] = v * v % mod;
		}
		buildAll();
	}

	public long get(final int i) {
		final int idx = size + i;
		pushPath(idx);
		final long v = tree[idx];
		return v < 0 ? v + mod : v;
	}

	public void add(final int i, final long v) {
		apply(i, i + 1, 1, v);
	}

	public void add(int l, int r, final long v) {
		apply(l, r, 1, v);
	}

	public void multiply(final int i, final long v) {
		apply(i, i + 1, v, 0);
	}

	public void multiply(final int l, final int r, final long v) {
		apply(l, r, v, 0);
	}

	public void set(final int i, final long v) {
		apply(i, i + 1, 0, v);
	}

	public void set(final int l, final int r, final long v) {
		apply(l, r, 0, v);
	}

	public void apply(final int i, final long a, final long b) {
		apply(i, i + 1, a, b);
	}

	public void apply(int l, int r, final long a, final long b) {
		if (l >= r) return;
		l += size;
		r += size;
		pushBoundaries(l, r);
		final int l2 = l, r2 = r;
		final long ma = a % mod, mb = b % mod, ma2 = ma * ma % mod, mb2 = mb * mb % mod, mab2 = (ma * mb << 1) % mod;
		for (int len = 1; l < r; l >>= 1, r >>= 1, len <<= 1) {
			if ((l & 1) == 1) allApply(l++, ma, mb, ma2, mb2, mab2, len);
			if ((r & 1) == 1) allApply(--r, ma, mb, ma2, mb2, mab2, len);
		}
		updateBoundaries(l2, r2);
	}

	public void fill(final long val) {
		final long v = val % mod;
		Arrays.fill(tree, size, size + n, v);
		Arrays.fill(tree2, size, size + n, v * v % mod);
		buildAll();
	}

	public void setAll(final LongUnaryOperator func) {
		for (int i = 0, idx = size; i < n; i++, idx++) {
			final long v = func.applyAsLong(i) % mod;
			tree[idx] = v;
			tree2[idx] = v * v % mod;
		}
		buildAll();
	}

	public long query(int l, int r) {
		if (l >= r) return 0;
		l += size;
		r += size;
		pushBoundaries(l, r);
		long ans = 0;
		for (; l < r; l >>= 1, r >>= 1) {
			if ((l & 1) == 1) ans += tree[l++];
			if ((r & 1) == 1) ans += tree[--r];
		}
		ans %= mod;
		return ans < 0 ? ans + mod : ans;
	}

	public long query2(int l, int r) {
		if (l >= r) return 0;
		l += size;
		r += size;
		pushBoundaries(l, r);
		long ans = 0;
		for (; l < r; l >>= 1, r >>= 1) {
			if ((l & 1) == 1) ans += tree2[l++];
			if ((r & 1) == 1) ans += tree2[--r];
		}
		ans %= mod;
		return ans < 0 ? ans + mod : ans;
	}

	public long queryAll() {
		final long v = tree[1];
		return v < 0 ? v + mod : v;
	}

	public long query2All() {
		final long v = tree2[1];
		return v < 0 ? v + mod : v;
	}

	public int size() {
		return n;
	}

	private void buildAll() {
		for (int i = size - 1; i > 0; i--) tree[i] = (tree[i << 1] + tree[i << 1 | 1]) % mod;
		for (int i = size - 1; i > 0; i--) tree2[i] = (tree2[i << 1] + tree2[i << 1 | 1]) % mod;
		Arrays.fill(lazyA, 1);
		Arrays.fill(lazyB, 0);
	}

	private void allApply(final int i, final long a, final long b, final long a2, final long b2, final long ab2, final int len) {
		final long before = tree[i];
		tree2[i] = (a2 * tree2[i] % mod + ab2 * before % mod + b2 * len % mod) % mod;
		tree[i] = (a * before % mod + b * len % mod) % mod;
		if (i >= size) return;
		lazyB[i] = (a * lazyB[i] + b) % mod;
		lazyA[i] = (a * lazyA[i]) % mod;
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
		final long a = lazyA[i], b = lazyB[i];
		if (a == 1 && b == 0) return;
		final long a2 = a * a % mod, b2 = b * b % mod, ab2 = (a * b << 1) % mod;
		allApply(i << 1, a, b, a2, b2, ab2, len);
		allApply(i << 1 | 1, a, b, a2, b2, ab2, len);
		lazyA[i] = 1;
		lazyB[i] = 0;
	}

	private void updateBoundaries(final int l, final int r) {
		for (int i = 1; i <= log; i++) {
			if (((l >> i) << i) != l) update(l >> i);
			if (((r >> i) << i) != r) update((r - 1) >> i);
		}
	}

	private void update(final int i) {
		tree[i] = (tree[i << 1] + tree[i << 1 | 1]) % mod;
		tree2[i] = (tree2[i << 1] + tree2[i << 1 | 1]) % mod;
	}

	public PrimitiveIterator.OfLong iterator() {
		return new PrimitiveIterator.OfLong() {
			private int idx = 0;

			public boolean hasNext() {
				return idx < n;
			}

			public long nextLong() {
				if (!hasNext()) throw new NoSuchElementException();
				final long v = tree[size + idx++];
				return v < 0 ? v + mod : v;
			}
		};
	}

	@Override
	public String toString() {
		final StringBuilder s = new StringBuilder();
		long v = tree[size];
		s.append(v < 0 ? v + mod : v);
		for (int i = size + 1; i < size + n; i++) {
			v = tree[i];
			s.append(' ').append(v < 0 ? v + mod : v);
		}
		return s.toString();
	}
}
