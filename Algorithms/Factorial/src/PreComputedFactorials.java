@SuppressWarnings("unused")
public final class PreComputedFactorials {
	private final int n;
	private final long mod;
	private final boolean isPrime;
	private final long[] factorials;
	private final long[] inverseFactorials;

	public PreComputedFactorials() {
		this(1_000_000, 998244353, true);
	}

	public PreComputedFactorials(final int n) {
		this(n, 998244353, true);
	}

	public PreComputedFactorials(final int n, final long mod, final boolean isPrime) {
		this.n = n;
		this.mod = mod;
		this.isPrime = isPrime;
		factorials = new long[n + 1];
		inverseFactorials = new long[n + 1];
		if (isPrime) initializeFactorialsPrime();
		else initializeFactorials();
	}

	private void initializeFactorialsPrime() {
		factorials[0] = 1;
		for (int i = 1; i <= n; i++) {
			factorials[i] = i * factorials[i - 1] % mod;
			inverseFactorials[i] = modPow(factorials[i], mod - 2);
		}
	}

	private void initializeFactorials() {
		factorials[0] = 1;
		for (int i = 1; i <= n; i++) {
			factorials[i] = i * factorials[i - 1] % mod;
			inverseFactorials[i] = modPow(factorials[i], mod - 2);
		}
	}

	private long modPow(long a, long b) {
		long ans = 1;
		for (; b > 0; a = a * a % mod, b >>= 1) {
			if ((b & 1) == 1) ans = ans * a % mod;
		}
		return ans;
	}

	public long factorial(final int n) {
		return factorials[n];
	}

	public long inverseFactorial(final int n) {
		return inverseFactorials[n];
	}

	public long combination(final int n, final int k) {
		return factorials[n] * inverseFactorials[k] % mod * inverseFactorials[n - k] % mod;
	}

	public long permutation(final int n, final int k) {
		return factorials[n] * inverseFactorials[k] % mod;
	}

}
