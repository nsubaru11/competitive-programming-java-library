@SuppressWarnings("unused")
public final class FactorialTable {
	private final int mod;
	private final int[] fact, invFact;

	public FactorialTable() {
		this(1_000_000, 998244353);
	}

	public FactorialTable(final int n) {
		this(n, 998244353);
	}

	public FactorialTable(final int n, final int mod) {
		this.mod = mod;
		final int len = n << 1 | 1;
		fact = new int[len];
		invFact = new int[len];
		fact[0] = 1;
		for (int i = 1; i < len; i++) {
			fact[i] = (int) ((long) i * fact[i - 1] % mod);
		}
		int invFactN = 1;
		for (int a = fact[len - 1], b = mod - 2; b > 0; a = (int) ((long) a * a % mod), b >>= 1) {
			if ((b & 1) == 1) invFactN = (int) ((long) invFactN * a % mod);
		}
		invFact[len - 1] = invFactN;
		for (int i = len - 1; i > 0; i--) {
			invFact[i - 1] = (int) ((long) invFact[i] * i % mod);
		}
	}

	public int fact(final int n) {
		return fact[n];
	}

	public int invFact(final int n) {
		return invFact[n];
	}

	public int nCr(final int n, final int r) {
		if (r < 0 || r > n) return 0;
		return (int) ((long) fact[n] * invFact[r] % mod * invFact[n - r] % mod);
	}

	public int nPr(final int n, final int r) {
		if (r < 0 || r > n) return 0;
		return (int) ((long) fact[n] * invFact[n - r] % mod);
	}

	/**
	 * `n` 種類のものから、重複を許して `r` 個選ぶ組み合わせ数
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
	 * 正しい括弧列の個数
	 * グリッド上で対角線を越えずに `(0,0)` から `(n,n)` へ移動する経路数
	 *
	 * @param n n組
	 * @return カタラン数
	 */
	public int catalan(final int n) {
		if (n < 0) return 0;
		return (nCr(2 * n, n) - nCr(2 * n, n - 1) + mod) % mod;
	}

	/**
	 * `n` 種類のものを、 `k` 個の「列（並び順に意味があるグループ）」に分割する組み合わせ数
	 *
	 * @param n 種類数
	 * @param k 列数
	 * @return lah number
	 */
	public int lah(final int n, final int k) {
		if (n < k || k < 0) return 0;
		if (n == 0) return 1;
		long res = (long) nCr(n - 1, k - 1) * fact[n] % mod;
		return (int) (res * invFact[k] % mod);
	}

	/**
	 * 長さ `2n` の正しい括弧列のうち、`()` という山（ピーク）がちょうど `k` 個あるものの数
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
	 * グリッド上で対角線を越えずに `(0,0)` から `(n,k)` (n &ge; k) へ移動する経路数
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
