package patterns.search;

import java.util.*;

public class ExhaustiveSearch {

	public static void main(String[] args) {
		double t0 = System.nanoTime();
//		test();
		time();
		System.out.printf("総時間: %.1fms\n", (System.nanoTime() - t0) / 1_000_000);
	}

	private static void test() {
		int n = 5, k = 10;
		System.out.printf("n: %d, k: %d\n", n, k);
		int[] arr = new int[n];
		Arrays.setAll(arr, i -> (int) (Math.random() * k));
		System.out.println(Arrays.toString(arr) + "\n");
		bitSearch(arr, n, k);
		System.out.println();
		reclusiveSearch(arr, n, k, 0, 0);
		System.out.println();
		stackSearch(arr, n, k);
		System.out.println();
		System.out.println(dpSearch(arr, n, k));
	}

	private static void time() {
		int a = 4;
		double t1 = 0, t2 = 0, t3 = 0, t4 = 0;
		for (int n = 25, k = 1; n <= 30; n++) {
			int[] arr = new int[n];
			for (int j = 0; j < a; j++) {
				Arrays.setAll(arr, i -> (int) (Math.random() * k));

				if ((j & 1) == 1) {
					long t = System.nanoTime();
					bitSearch(arr, n, k);
					t1 += System.nanoTime() - t;

					t = System.nanoTime();
					reclusiveSearch(arr, n, k, 0, 0);
					t2 += System.nanoTime() - t;

					t = System.nanoTime();
					dpSearch(arr, n, k);
					t4 += System.nanoTime() - t;

					t = System.nanoTime();
					stackSearch(arr, n, k);
					t3 += System.nanoTime() - t;
				} else {
					long t = System.nanoTime();
					reclusiveSearch(arr, n, k, 0, 0);
					t2 += System.nanoTime() - t;

					t = System.nanoTime();
					bitSearch(arr, n, k);
					t1 += System.nanoTime() - t;

					t = System.nanoTime();
					stackSearch(arr, n, k);
					t3 += System.nanoTime() - t;

					t = System.nanoTime();
					dpSearch(arr, n, k);
					t4 += System.nanoTime() - t;
				}

			}
			System.out.printf("データ数: %d \n", n);
			System.out.printf("ビット全探索: %.5fms\n", t1 / a / 1_000_000);
			System.out.printf("深さ優先探索(再帰): %.5fms\n", t2 / a / 1_000_000);
			System.out.printf("深さ優先探索(スタック): %.5fms\n", t3 / a / 1_000_000);
			System.out.printf("動的計画法: %.5fms\n", t4 / a / 1_000_000);
			System.out.println();
		}
	}

	private static final boolean dpSearch(int[] arr, int n, int k) {
		boolean[] dp = new boolean[k + 1];
		dp[0] = true;
		for (int i = 0; i < n; i++) {
			for (int j = arr[i]; j <= k; j++) {
				dp[j] |= dp[j - arr[i]];
			}
		}
//		System.out.println(Arrays.toString(dp));
		return dp[k];
	}

	private static final void bitSearch(int[] arr, int n, int k) {
		for (int i = 0; i < 1 << n; i++) {
			int sum = 0;
			for (int j = 0; j < n; j++) {
				if ((i & (1 << j)) != 0) { // j番目の要素を使うか判定
					sum += arr[j];
				}
			}
			if (sum == k) {
//				String s = Integer.toBinaryString(i);
//				System.out.println(new StringBuilder(s).reverse());
			}
		}
	}

	private static final boolean reclusiveSearch(int[] arr, int n, int k, int sum, int flag) {
		if (n == 0) {
			if (sum == k) {
//				String s = Integer.toBinaryString(flag);
//				System.out.println(new StringBuilder(s).reverse());
			}
			return sum == k;
		}
		boolean f = reclusiveSearch(arr, n - 1, k, sum + arr[n - 1], flag | (1 << (n - 1)));
		boolean f2 = reclusiveSearch(arr, n - 1, k, sum, flag);
		return f || f2;
	}

	private static final void stackSearch(int[] arr, int n, int k) {
		ArrayDeque<int[]> stack = new ArrayDeque<>();
		stack.push(new int[]{0, 1, arr[0]});
		stack.push(new int[]{0, 0, 0});
		while (!stack.isEmpty()) {
			int[] current = stack.pop();
			int index = current[0];
			int flag = current[1];
			int sum = current[2];
			if (sum == k) {
//				String s = Integer.toBinaryString(flag);
//				System.out.println(new StringBuilder(s).reverse());
			}
			if (index == n - 1)
				continue;
			stack.push(new int[]{index + 1, flag | (1 << (index + 1)), sum + arr[index + 1]});
			stack.push(new int[]{index + 1, flag, sum});
		}
	}

}
