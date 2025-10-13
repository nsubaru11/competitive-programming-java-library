import java.io.*;
import java.util.*;

import static java.lang.Math.*;

public class Knapsack {

	private static void solve(final Scanner sc, final PrintWriter out) {
		int n = sc.nextInt();
		int W = sc.nextInt();
		int[] dp = new int[W + 1];
		for (int i = 0; i < n; i++) {
			int w = sc.nextInt();
			int c = sc.nextInt();
			for (int j = W; j >= w; j--) {
				if (j > 0) dp[j] = max(dp[j], dp[j - w] + c);
			}
		}
		for (int i = 0; i <= W; i++) out.println(dp[i]);
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
