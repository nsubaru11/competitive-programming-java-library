@SuppressWarnings("unused")
public final class FactorialNaive {

	public static long fact(final int n) {
		long ans = 1;
		for (int i = 1; i <= n; i++) ans = ans * i;
		return ans;
	}

	public static long modFact(final int n, final long mod) {
		long ans = 1;
		for (int i = 1; i <= n; i++) ans = ans * i % mod;
		return ans;
	}

	public static long invFact(final int n, final long mod) {
		long ans = 1;
		for (long a = modFact(n, mod), b = mod - 2; b > 0; a = a * a % mod, b >>= 1) {
			if ((b & 1) == 1) ans = ans * a % mod;
		}
		return ans;
	}

}
