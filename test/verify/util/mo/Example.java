package verify.util.mo;

import static java.lang.System.*;

import java.util.*;

import lib.util.*;

public final class Example {

	public static void main(final String[] args) {
		// 各クエリ区間 [l, r)（半開区間, 0-indexed）内で a[i] == a[j] (i < j) となるペアの個数を求める
		final int[] a = {1, 2, 1, 2, 1};
		final int n = a.length;
		final int[][] lr = {{0, 1}, {5, 4}};

		final int[] freq = new int[ArrayUtils.max(a) + 1];
		final long[] cur = {0L};

		final long[] ans = new long[lr[0].length];
		MoAlgorithm.run(n, lr, i -> cur[0] += freq[a[i]]++, i -> cur[0] -= --freq[a[i]], i -> ans[i] = cur[0]);

		out.println(Arrays.toString(ans)); // [4, 1]
	}

}
