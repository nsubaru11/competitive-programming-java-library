package patterns.math;

public class Catalan {

	public static void main(String[] args) {
		int n = 40;
		System.out.println(catalan(n, new double[n + 1]));
		System.out.println(catalan(n));
	}

	private static double catalan(int n, double[] memo) {
		// 漸化式
		double sum = 0;
		if (memo[n] != 0)
			return memo[n];
		if (n == 0)
			return 1;
		for (int i = 0; i < n; i++) {
			memo[i] = catalan(i, memo);
			memo[n - i - 1] = catalan(n - i - 1, memo);
			sum += memo[i] * memo[n - i - 1];
		}
		return sum;
	}

	private static double catalan(int n) {
		// 一般項 2n! / (n! * n!) / (n + 1)
		double sum = 1;
		double sum2 = 1;
		for (int i = 0; i < n; i++) {
			sum *= 2 * n - i;
			sum2 *= n - i;
		}
		return sum / sum2 / (n + 1);
	}

}
