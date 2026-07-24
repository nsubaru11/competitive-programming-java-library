package verify.io.compat17;

import static java.util.Arrays.*;

import java.util.*;
import java.util.stream.*;

import lib.io.compat17.*;

// https://judge.yosupo.jp/problem/many_aplusb_128bit
public final class Check2 {

	// region < Constants & Globals >
	private static final boolean DEBUG;
	private static final int MOD;
	private static final int[] di;
	private static final int[] dj;
	private static final FastScanner sc;
	private static final FastPrinter out;

	static {
		DEBUG = true;
		MOD = 998244353;
		// MOD = 1_000_000_007;
		di = new int[]{0, -1, 0, 1, -1, -1, 1, 1};
		dj = new int[]{-1, 0, 1, 0, -1, 1, 1, -1};
		sc = new FastScanner();
		out = new FastPrinter();
	}
	// endregion

	private static void solve() {
		int t = sc.nextInt();
		while (t-- > 0) {
			out.println(sc.nextBigInteger().add(sc.nextBigInteger()));
		}
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
