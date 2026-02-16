import java.util.function.*;

import static java.util.Arrays.*;

/**
 * 競技プログラミング向け Binary Indexed Tree（Fenwick Tree）
 * <p>
 * 1点更新と閉区間 [0, r] の累積和（演算適用結果）を O(log N) で提供。
 */
@SuppressWarnings({"unused", "unchecked"})
public final class BIT<T> {
	private final int n;
	private final T identity;
	private final T[] tree, raw;
	private final BinaryOperator<T> op, inv;

	/**
	 * サイズ n の BIT を構築する。
	 *
	 * @param n        要素数
	 * @param op       演算 (例: (a, b) -> a + b)
	 * @param inv      逆演算 (例: (a, b) -> a - b)
	 * @param identity 単位元 (例: 0)
	 */
	public BIT(final int n, final BinaryOperator<T> op, final BinaryOperator<T> inv, final T identity) {
		this.n = n;
		tree = (T[]) new Object[n + 1];
		raw = (T[]) new Object[n];
		fill(tree, identity);
		fill(raw, identity);
		this.op = op;
		this.inv = inv;
		this.identity = identity;
	}

	/**
	 * 初期値関数を用いて BIT を O(N) で構築する。
	 *
	 * @param n        要素数
	 * @param op       演算
	 * @param inv      逆演算
	 * @param init     初期値関数 (index -> value)
	 * @param identity 単位元
	 */
	public BIT(final int n, final BinaryOperator<T> op, final BinaryOperator<T> inv, final IntFunction<T> init, final T identity) {
		this.n = n;
		tree = (T[]) new Object[n + 1];
		raw = (T[]) new Object[n];
		this.op = op;
		this.inv = inv;
		this.identity = identity;
		setAll(init);
	}

	public void setAll(final IntFunction<T> init) {
		tree[0] = identity;
		for (int i = 0; i < n; i++) raw[i] = tree[i + 1] = init.apply(i);
		for (int i = 1; i <= n; i++) {
			int j = i + (i & -i);
			if (j <= n) tree[j] = op.apply(tree[j], tree[i]);
		}
	}

	/**
	 * インデックス i (0-indexed) の要素に v を作用させる。
	 *
	 * @param i インデックス
	 * @param v 作用させる値
	 */
	public void apply(final int i, final T v) {
		raw[i] = op.apply(raw[i], v);
		for (int cur = i + 1; cur <= n; cur += cur & -cur) tree[cur] = op.apply(tree[cur], v);
	}

	/**
	 * インデックス i (0-indexed) の要素を v に更新する。
	 * 逆演算 inv が提供されている必要がある。
	 *
	 * @param i インデックス
	 * @param v 更新後の値
	 */
	public void set(final int i, final T v) {
		apply(i, inv.apply(v, raw[i]));
	}

	/**
	 * インデックス i (0-indexed) の現在の値を取得する。
	 *
	 * @param i インデックス
	 * @return 現在の値
	 */
	public T get(final int i) {
		return raw[i];
	}

	/**
	 * 閉区間 [0, r] の和を計算する。
	 *
	 * @param r 右端の境界 (includes)
	 * @return [0, r] の和
	 */
	public T query(int r) {
		T res = identity;
		for (r++; r > 0; r -= r & -r) res = op.apply(res, tree[r]);
		return res;
	}

	/**
	 * 閉区間 [l, r] の和を計算する。
	 * 逆演算 inv が提供されている必要がある。
	 *
	 * @param l 左端の境界 (includes)
	 * @param r 右端の境界 (includes)
	 * @return [l, r] の和
	 */
	public T query(int l, int r) {
		if (l > r) return identity;
		return inv.apply(query(r), query(l - 1));
	}

	public int size() {
		return n;
	}
}
