package patterns.dp;

import static java.lang.Math.*;

import java.io.*;
import java.util.*;

/**
 * 連続部分列を 1 つ選んで c 倍したときの総和の最大値を、Kadane 法で求める。
 */
public class MaximumSubarray {

	private static void solve(final Scanner sc, final PrintWriter out) {
		int n = sc.nextInt();
		long c = sc.nextLong();
		long[] arr = new long[n];
		long sum = 0;
		for (int i = 0; i < n; i++) {
			arr[i] = sc.nextLong();
			sum += arr[i];
		}
		long ans = c <= 0 ? Long.MAX_VALUE : Long.MIN_VALUE;
		long s = 0;
		for (long a : arr) {
			s = c <= 0 ? min(s + a, a) : max(s + a, a);
			ans = c <= 0 ? min(s, ans) : max(s, ans);
		}
		out.println(max(sum + (c - 1) * ans, sum));
	}

	public static void main(final String[] args) {
		try (Scanner sc = new Scanner(System.in); PrintWriter out = new PrintWriter(System.out)) {
			solve(sc, out);
		}
	}
}
