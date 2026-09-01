package verify.util.mo;

import static java.util.Arrays.*;

import java.util.*;
import java.util.stream.*;

import lib.ds.arrays.*;
import lib.ds.priorityqueue.*;
import lib.io.compat17.*;
import lib.util.*;

// https://judge.yosupo.jp/problem/static_range_mode_query
public final class Check3 {

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
		int n = sc.nextInt();
		int q = sc.nextInt();
		int[] a = sc.nextInt(n);
		IntCompressedArray c = new IntCompressedArray(a);
		IntIndexedPriorityQueue freq = new IntIndexedPriorityQueue(c.uniqueSize, true);
		int[][] lr = sc.nextIntMatInv(q, 2);
		int[][] res = new int[q][2];
		MoAlgorithm.run(n, lr, i -> freq.set(c.get(i), freq.getOrDefault(c.get(i), 0) + 1), i -> freq.set(c.get(i), freq.get(c.get(i)) - 1), i -> {
			res[i][0] = c.valueOfRank(freq.peekIndex());
			res[i][1] = freq.peek();
		});
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
			else System.err.println(stream(args).map(Check3::stringify).collect(Collectors.joining("\n", "\n", "")));
		}
	}

	private static void debug(final Object... args) {
		if (DEBUG) {
			out.flush();
			if (args == null) System.err.println("null");
			else if (args.getClass().getComponentType().isArray()) System.err.println(stringify(args));
			else System.err.println(stream(args).map(Check3::stringify).collect(Collectors.joining(", ", "", "")));
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
