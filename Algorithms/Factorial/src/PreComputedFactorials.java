public class PreComputedFactorials {
	private final int maxNumber;
	private final long mod;
	private final long[] factorials;
	private final long[] inverseFactorials;

	public PreComputedFactorials() {
		this(1_000_000, 998244353);
	}

	public PreComputedFactorials(int maxNumber) {
		this(maxNumber, 998244353);
	}

	public PreComputedFactorials(long mod) {
		this(1_000_000, mod);
	}

	public PreComputedFactorials(int maxNumber, long mod) {
		this.maxNumber = maxNumber;
		this.mod = mod;
		factorials = new long[maxNumber + 1];
		inverseFactorials = new long[maxNumber + 1];
		initializeFactorials();
	}

	private void initializeFactorials() {
		factorials[0] = 1;
		for (int i = 1; i <= maxNumber; i++) {
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

	public long factorial(int n) {
		return factorials[n];
	}

	public long inverseFactorial(int n) {
		return inverseFactorials[n];
	}

	public long combination(int n, int k) {
		return factorials[n] * inverseFactorials[k] % mod * inverseFactorials[n - k] % mod;
	}

	public long permutation(int n, int k) {
		return factorials[n] * inverseFactorials[k] % mod;
	}

}