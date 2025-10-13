import java.io.*;
import java.util.*;

public class PartialSumProblem {

	private static void solve(final Scanner sc, final PrintWriter out) {
		int n = sc.nextInt();
		int k = sc.nextInt();
		int[] a = new int[n];
		int sum = 0;
		for (int i = 0; i < n; i++) sum += a[i] = sc.nextInt();
		boolean f = partialSumProblem(n, a, k, sum);
		out.println("複数回使用なし" + (f ? "Yes" : "No"));
		boolean f2 = partialSumProblem(n, a, k);
		out.println("複数回使用あり" + (f2 ? "Yes" : "No"));
	}

	public static boolean partialSumProblem(int n, int[] a, int k) {
		boolean[] dp = new boolean[k + 1];
		dp[0] = true;
		for (int i = 0; i < n; i++) {
			for (int j = a[i]; j <= k; j++) dp[j] |= dp[j - a[i]];
		}
		return dp[k];
	}

	public static boolean partialSumProblem(int n, int[] a, int k, int sum) {
		if (sum < k) return false;
		boolean[] dp = new boolean[k + 1];
		dp[0] = true;
		for (int i = 0; i < n; i++) {
			for (int j = k; j >= a[i]; j--) dp[j] |= dp[j - a[i]];
		}
		return dp[k];
	}

	public static void main(String[] args) {
		try (final Scanner sc = new Scanner(System.in);
			 final PrintWriter out = new PrintWriter(System.out)) {
			solve(sc, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
