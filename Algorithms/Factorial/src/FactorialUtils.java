@SuppressWarnings("unused")
public final class FactorialUtils {

	public static long factorial(final int n) {
		long ans = 1;
		for (int i = 1; i <= n; i++) {
			ans = ans * i;
		}
		return ans;
	}

	public static long inverseFactorial(final int n, final int mod) {
		long ans = 1;
		for (int i = 1; i <= n; i++) {
			ans = ans * i % mod;
		}
		return ans;
	}

}
