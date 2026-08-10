package verify.util.mo;

import static java.util.Arrays.*;

import java.util.*;
import java.util.stream.*;

import lib.ds.arrays.*;
import lib.ds.fenwick.*;
import lib.io.compat17.*;
import lib.util.*;

// https://judge.yosupo.jp/problem/static_range_inversions_query
public final class Check2 {

	// region < Constants & Globals >
	private static final boolean DEBUG = true;
	private static final int MOD = 998244353;
	// private static final int MOD = 1_000_000_007;
	private static final char[] op = new char[]{'L', 'U', 'R', 'D'};
	private static final int[] di = new int[]{0, -1, 0, 1, -1, -1, 1, 1};
	private static final int[] dj = new int[]{-1, 0, 1, 0, -1, 1, 1, -1};
	private static final FastScanner sc = new FastScanner();
	private static final FastPrinter out = new FastPrinter();
	// endregion

	private static void solve() {
		int n = sc.nextInt(), q = sc.nextInt();
		IntCompressedArray a = new IntCompressedArray(sc.nextInt(n));
		int m = a.uniqueSize();
		IntBIT bit = new IntBIT(m);
		int[][] lr = sc.nextIntMatInv(q, 2);
		long[] ans = {0};
		long[] res = new long[q];
		MoAlgorithm.run(n, lr, i -> {
			int ai = a.get(i);
			ans[0] += bit.sum(ai - 1);
			bit.add(ai, 1);
		}, i -> {
			int ai = a.get(i);
			ans[0] += bit.sumAll() - bit.sum(ai);
			bit.add(ai, 1);
		}, i -> {
			int ai = a.get(i);
			ans[0] -= bit.sum(ai - 1);
			bit.add(ai, -1);
		}, i -> {
			int ai = a.get(i);
			ans[0] -= bit.sumAll() - bit.sum(ai);
			bit.add(ai, -1);
		}, i -> res[i] = ans[0]);
		out.println(res);
	}

	// region < main & debug >
	public static void main(final String[] args) {
		try {
			solve();
		} finally {
			out.close();
		}
	}

	private static void debugln(final Object... args) {
		if (DEBUG) {
			out.flush();
			if (args == null) System.err.println("null");
			else if (args.getClass().getComponentType().isArray()) System.err.println(stringify(args));
			else System.err.println(stream(args).map(Check2::stringify).collect(Collectors.joining("\n", "\n", "")));
		}
	}

	private static void debug(final Object... args) {
		if (DEBUG) {
			out.flush();
			if (args == null) System.err.println("null");
			else if (args.getClass().getComponentType().isArray()) System.err.println(stringify(args));
			else System.err.println(stream(args).map(Check2::stringify).collect(Collectors.joining(", ", "", "")));
		}
	}

	private static String stringify(final Object obj) {
		if (obj == null) return "null";
		else if (obj instanceof int[][] arr)
			return "\n" + stream(arr).map(Arrays::toString).collect(Collectors.joining("\n"));
		else if (obj instanceof long[][] arr)
			return "\n" + stream(arr).map(Arrays::toString).collect(Collectors.joining("\n"));
		else if (obj instanceof char[][] arr)
			return "\n" + stream(arr).map(String::valueOf).collect(Collectors.joining("\n"));
		else if (obj instanceof Object[][] arr)
			return "\n" + stream(arr).map(Arrays::deepToString).collect(Collectors.joining("\n"));
		else if (obj instanceof int[] arr) return Arrays.toString(arr);
		else if (obj instanceof long[] arr) return Arrays.toString(arr);
		else if (obj instanceof double[] arr) return Arrays.toString(arr);
		else if (obj instanceof char[] arr) return Arrays.toString(arr);
		else if (obj instanceof boolean[] arr) return Arrays.toString(arr);
		else if (obj instanceof Object[] arr) return deepToString(arr);
		else if (obj instanceof Iterable<?> it) {
			final StringJoiner sj = new StringJoiner(", ", "[", "]");
			for (final Object e : it) sj.add(stringify(e));
			return sj.toString();
		}
		return obj.toString();
	}
	// endregion
}
