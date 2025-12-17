@SuppressWarnings("unused")
public final class FactorialTable {
	private final int mod;
	private final int[] fact;
	private final int[] invFact;

	public FactorialTable() {
		this(1_000_000, 998244353);
	}

	public FactorialTable(final int n) {
		this(n, 998244353);
	}

	public FactorialTable(final int n, final int mod) {
		this.mod = mod;
		fact = new int[n + 1];
		invFact = new int[n + 1];
		fact[0] = 1;
		for (int i = 1; i <= n; i++) {
			fact[i] = (int) ((long) i * fact[i - 1] % mod);
		}
		invFact[n] = modPow(fact[n], mod - 2);
		for (int i = n; i > 0; i--) {
			invFact[i - 1] = (int) ((long) invFact[i] * i % mod);
		}
	}

	private int modPow(int a, int b) {
		int ans = 1;
		for (; b > 0; a = (int) ((long) a * a % mod), b >>= 1) {
			if ((b & 1) == 1) ans = (int) ((long) ans * a % mod);
		}
		return ans;
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
}
