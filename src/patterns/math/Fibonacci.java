package patterns.math;

public class Fibonacci {

	public static void main(String[] args) {
		int n = 1000;
		System.out.println(fibonacci(n, new double[n + 1]));
		System.out.println(fibonacci(n));
	}

	private static double fibonacci(int n, double[] memo) {
		// Fn = Fn-1 + Fn-2
		if (memo[n] != 0)
			return memo[n];
		if (n == 0) {
			memo[n] = 0;
			return memo[n];
		}
		if (n == 1 || n == 2) {
			memo[n] = 1;
			return memo[n];
		}
		memo[n] = fibonacci(n - 1, memo) + fibonacci(n - 2, memo);
		return memo[n];
	}

//	private static double fibonacci(int n) {
//		double rt5 = sqrt(5);
//		return (pow((1 + rt5) / 2, n) - pow((1 - rt5) / 2, n)) / rt5;
//	}

	private static double fibonacci(int n) {
		if (n == 0 || n == 1)
			return 1;
		return fibonacci(n - 1) + fibonacci(n - 2);
	}

}
