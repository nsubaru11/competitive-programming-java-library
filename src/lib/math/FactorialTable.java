package lib.math;

import static java.lang.Math.*;
import static java.util.Arrays.*;

/**
 * 素数 mod 上の階乗・階乗逆元テーブルです。
 *
 * <p>テーブルは必要に応じて拡張されます。{@code invFact}、{@code nCr}、{@code nPr}
 * など逆元を利用するメソッドは、mod が素数であり、参照する添字が mod 未満であることを
 * 前提とします。Lucas の定理による {@code n >= mod} の計算は提供しません。
 * {@link #fact(int)} は {@code n >= mod} のとき 0 を返しますが、{@link #invFact(int)}
 * はその範囲では定義されません。
 */
public final class FactorialTable {
	public final int mod;
	private int capacity;
	private int[] inv, fact, invFact;

	/**
	 * 既定容量1024、法998244353で作成します。
	 */
	public FactorialTable() {
		this(1024, 998244353);
	}

	/**
	 * 指定容量、法998244353で作成します。
	 * {@code 0 <= n < 998244353} を前提とします。
	 *
	 * @param n 初期構築する最大添字
	 */
	public FactorialTable(final int n) {
		this(n, 998244353);
	}

	/**
	 * 指定容量と法で作成します。
	 * {@code 0 <= n < mod} かつ {@code mod} が素数であることを前提とします。
	 *
	 * @param n   初期構築する最大添字
	 * @param mod 素数である法
	 */
	public FactorialTable(final int n, final int mod) {
		this.mod = mod;
		capacity = max(2, n + 1);
		inv = new int[capacity];
		fact = new int[capacity];
		invFact = new int[capacity];
		fact[0] = fact[1] = 1;
		inv[1] = 1;
		invFact[0] = invFact[1] = 1;
		build(2);
	}

	private void ensureCapacity(final int n) {
		if (n < capacity) return;
		int oldCapacity = capacity;
		capacity = max(n + 1, capacity * 2);
		if (capacity > mod) capacity = mod;
		inv = copyOf(inv, capacity);
		fact = copyOf(fact, capacity);
		invFact = copyOf(invFact, capacity);
		build(oldCapacity);
	}

	private void build(final int oldCapacity) {
		for (int i = oldCapacity; i < capacity; i++) {
			inv[i] = (int) (mod - (long) (mod / i) * inv[mod % i] % mod);
			fact[i] = (int) ((long) fact[i - 1] * i % mod);
			invFact[i] = (int) ((long) invFact[i - 1] * inv[i] % mod);
		}
	}

	/**
	 * {@code n} の乗法逆元を返します。
	 * {@code 1 <= n < mod} を前提とします。
	 *
	 * @param n 逆元を求める値
	 * @return {@code n^(-1) mod mod}
	 */
	public int inv(final int n) {
		ensureCapacity(n);
		return inv[n];
	}

	/**
	 * {@code n! mod mod} を返します。
	 * {@code n >= 0} を前提とし、{@code n >= mod} の場合は 0 を返します。
	 *
	 * @param n 階乗の引数
	 * @return {@code n! mod mod}
	 */
	public int fact(final int n) {
		if (n >= mod) return 0;
		ensureCapacity(n);
		return fact[n];
	}

	/**
	 * {@code n!} の乗法逆元を返します。
	 * {@code 0 <= n < mod} を前提とします。{@code n >= mod} では定義されません。
	 *
	 * @param n 階乗の引数
	 * @return {@code (n!)^(-1) mod mod}
	 */
	public int invFact(final int n) {
		ensureCapacity(n);
		return invFact[n];
	}

	/**
	 * 二項係数 {@code nCr mod mod} を返します。
	 * 参照される最大添字が mod 未満であることを前提とします。
	 *
	 * @param n 全体の個数
	 * @param r 選択する個数
	 * @return {@code nCr mod mod}
	 */
	public int nCr(final int n, final int r) {
		if (r < 0 || r > n) return 0;
		ensureCapacity(n);
		return (int) ((long) fact[n] * invFact[r] % mod * invFact[n - r] % mod);
	}

	/**
	 * 順列 {@code nPr mod mod} を返します。
	 * 参照される最大添字が mod 未満であることを前提とします。
	 *
	 * @param n 全体の個数
	 * @param r 選択する個数
	 * @return {@code nPr mod mod}
	 */
	public int nPr(final int n, final int r) {
		if (r < 0 || r > n) return 0;
		ensureCapacity(n);
		return (int) ((long) fact[n] * invFact[n - r] % mod);
	}

	/**
	 * {@code n} 種類のものから、重複を許して {@code r} 個選ぶ組み合わせ数を返します。
	 * 参照される最大添字が mod 未満であることを前提とします。
	 *
	 * @param n 種類数
	 * @param r 選択個数
	 * @return 重複組み合わせ
	 */
	public int nHr(final int n, final int r) {
		if (n < 0 || r < 0) return 0;
		if (n == 0 && r == 0) return 1;
		return nCr(n + r - 1, r);
	}

	/**
	 * 正しい括弧列の個数を返します。
	 * グリッド上で対角線を越えずに {@code (0, 0)} から {@code (n, n)} へ移動する経路数と同値です。
	 * 参照される最大添字 {@code 2 * n} が mod 未満であることを前提とします。
	 *
	 * @param n n組
	 * @return カタラン数
	 */
	public int catalan(final int n) {
		if (n < 0) return 0;
		return (nCr(2 * n, n) - nCr(2 * n, n - 1) + mod) % mod;
	}

	/**
	 * {@code n} 種類のものを、{@code k} 個の列へ分割する Lah 数を返します。
	 * 参照される最大添字が mod 未満であることを前提とします。
	 *
	 * @param n 種類数
	 * @param k 列数
	 * @return lah number
	 */
	public int lah(final int n, final int k) {
		if (n < k || k < 0) return 0;
		if (n == 0) return 1;
		ensureCapacity(n);
		long res = (long) nCr(n - 1, k - 1) * fact[n] % mod;
		return (int) (res * invFact[k] % mod);
	}

	/**
	 * 長さ {@code 2n} の正しい括弧列のうち、ピークがちょうど {@code k} 個あるものの数を返します。
	 * 参照される最大添字が mod 未満であることを前提とします。
	 *
	 * @param n n組
	 * @param k 山の数
	 * @return ナラヤナ数
	 */
	public int narayana(final int n, final int k) {
		if (k < 1 || k > n) return 0;
		long res = (long) nCr(n, k) * nCr(n, k - 1) % mod;
		long invN = (long) fact[n - 1] * invFact[n] % mod;
		return (int) (res * invN % mod);
	}

	/**
	 * グリッド上で対角線を越えずに {@code (0, 0)} から {@code (n, k)} へ移動する経路数を返します。
	 * {@code n >= k} を前提とします。
	 * 参照される最大添字が mod 未満であることを前提とします。
	 *
	 * @param n 候補者Aの票数
	 * @param k 候補者Bの票数
	 * @return Ballot Theorem / カタランの三角形
	 */
	public int ballotTheorem(final int n, final int k) {
		if (n < k || k < 0) return 0;
		if (n == 0) return 1;
		return (nCr(n + k, k) - nCr(n + k, k - 1) + mod) % mod;
	}
}
