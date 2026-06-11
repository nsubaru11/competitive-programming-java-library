import java.util.function.*;

/**
 * 競技プログラミング向け Range Update Range Query (区間加算・区間和取得) を提供する long 型特化 BIT。
 * <p>
 * 2つの BIT を内部で管理することで、区間加算と区間和の計算を共に O(log N) で行う。
 */
@SuppressWarnings("unused")
public final class LongRangeBIT {
	private final int n;
	private final long[] bit1, bit2;

	/**
	 * サイズ n の BIT を構築する。初期値はすべて 0。
	 *
	 * @param n 要素数
	 */
	public LongRangeBIT(final int n) {
		this.n = n;
		bit1 = new long[n + 1];
		bit2 = new long[n + 1];
	}

	/**
	 * 初期値関数を用いて BIT を O(N) で構築する。
	 *
	 * @param n    要素数
	 * @param init 初期値関数 (index -> value)
	 */
	public LongRangeBIT(final int n, final IntToLongFunction init) {
		this(n);
		setAll(init);
	}

	/**
	 * BIT の全要素を再構築する。
	 *
	 * @param init 初期値関数
	 */
	public void setAll(final IntToLongFunction init) {
		bit1[0] = bit2[0] = 0;
		long prev = 0;
		for (int i = 0; i < n; i++) {
			long cur = init.applyAsLong(i);
			long diff = cur - prev;
			bit1[i + 1] = diff;
			bit2[i + 1] = diff * i;
			prev = cur;
		}
		for (int i = 1; i <= n; i++) {
			int j = i + (i & -i);
			if (j <= n) {
				bit1[j] += bit1[i];
				bit2[j] += bit2[i];
			}
		}
	}

	/**
	 * 閉区間 [l, r] に v を加算する。
	 *
	 * @param l 左端の境界 (includes)
	 * @param r 右端の境界 (includes)
	 * @param v 加算する値
	 */
	public void apply(final int l, final int r, final long v) {
		if (l > r) return;
		add(bit1, l, v);
		add(bit1, r + 1, -v);
		add(bit2, l, v * l);
		add(bit2, r + 1, -v * (r + 1));
	}

	/**
	 * インデックス i (0-indexed) の要素に v を加算する。
	 *
	 * @param i インデックス
	 * @param v 加算する値
	 */
	public void apply(final int i, final long v) {
		apply(i, i, v);
	}

	/**
	 * インデックス i (0-indexed) の現在の値を取得する。
	 *
	 * @param i インデックス
	 * @return 現在の値
	 */
	public long get(final int i) {
		return sum(bit1, i + 1);
	}

	/**
	 * 閉区間 [0, r] の和を計算する。
	 *
	 * @param r 右端の境界 (includes)
	 * @return [0, r] の和
	 */
	public long query(final int r) {
		if (r < 0) return 0;
		return sum(bit1, r + 1) * (r + 1) - sum(bit2, r + 1);
	}

	/**
	 * 閉区間 [l, r] の和を計算する。
	 *
	 * @param l 左端の境界 (includes)
	 * @param r 右端の境界 (includes)
	 * @return [l, r] の和
	 */
	public long query(final int l, final int r) {
		if (l > r) return 0;
		return query(r) - query(l - 1);
	}

	/**
	 * BIT のサイズを返す。
	 *
	 * @return サイズ
	 */
	public int size() {
		return n;
	}

	private void add(final long[] bit, int i, final long v) {
		for (i++; i <= n; i += i & -i) bit[i] += v;
	}

	private long sum(final long[] bit, int r) {
		long s = 0;
		for (; r > 0; r -= r & -r) s += bit[r];
		return s;
	}
}
