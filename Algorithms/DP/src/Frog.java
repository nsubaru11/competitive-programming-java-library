import static java.lang.Math.*;

import java.io.*;
import java.util.*;

public class Frog {

	private static void solve(final Scanner sc, final PrintWriter out) {
		int n = sc.nextInt();
		int k = sc.nextInt();
		int[] h = new int[n];
		for (int i = 0; i < n; i++) h[i] = sc.nextInt();
		out.println(frogProblem(n, k, h));
	}

	public static int frogProblem(int n, int k, int[] h) {
		int[] dp = new int[k];
		for (int i = 1; i < min(n, k); i++) dp[i] = abs(h[i] - h[0]);
		for (int i = k; i < n; i++) {
			int idx = i % k;
			int best = dp[idx] + abs(h[i] - h[i - k]);
			for (int j = 1; j < k; j++) {
				int idx2 = idx - j;
				if (idx2 < 0) idx2 += k;
				best = min(best, dp[idx2] + abs(h[i] - h[i - j]));
			}
			dp[idx] = best;
		}
		return dp[(n - 1) % k];
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
