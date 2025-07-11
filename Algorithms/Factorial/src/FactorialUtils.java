public final class FactorialUtils {

	public static long factorial(int n) {
		long ans = n;
		while (--n > 0) {
			ans = ans * n;
		}
		return ans;
	}

	public static long inverseFactorial(int n, int mod) {
		long ans = n;
		while (--n > 0) {
			ans = ans * n % mod;
		}
		return ans;
	}

}