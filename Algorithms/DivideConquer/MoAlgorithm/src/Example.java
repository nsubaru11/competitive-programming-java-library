import java.util.*;

import static java.lang.System.*;

public final class Example {

	public static void main(final String[] args) {
		// 各クエリ区間 [l, r]（閉区間, 0-indexed）内で a[i] == a[j] (i < j) となるペアの個数を求める
		final int[] a = {1, 2, 1, 2, 1};
		final int n = a.length;
		final int[][] lr = {{0, 4}, {1, 3}};

		final int[] freq = new int[Arrays.stream(a).max().orElse(0) + 1];
		final long[] cur = {0L};

		final long[] ans = MoAlgorithm.solve(n, lr,
				i -> cur[0] += freq[a[i]]++,
				i -> cur[0] -= --freq[a[i]],
				() -> cur[0]);

		out.println(Arrays.toString(ans)); // [4, 1]
	}

}
