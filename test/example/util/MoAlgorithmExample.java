package example.util;

import java.util.*;

import lib.util.*;

public final class MoAlgorithmExample {

	public static void main(final String[] args) {
		// 配列・クエリ
		int[] a = {1, 2, 1, 2, 1};
		int[][] lr = {{0, 1, 2}, {5, 4, 3}};

		// 状態
		int[] freq = new int[3];
		long[] cur = {0};
		long[] ans = new long[lr[0].length];

		// 同値ペアの個数
		MoAlgorithm.run(a.length, lr,
				i -> cur[0] += freq[a[i]]++,
				i -> cur[0] -= --freq[a[i]],
				q -> ans[q] = cur[0]);
		System.out.println(Arrays.toString(ans));
	}
}
