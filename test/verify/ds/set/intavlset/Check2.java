package verify.ds.set.intavlset;

import static java.util.Arrays.*;

import java.util.*;
import java.util.stream.*;

import lib.ds.set.*;
import lib.io.compat17.*;

// https://judge.yosupo.jp/problem/ordered_set
public final class Check2 {

	// region < Constants & Globals >
	private static final boolean DEBUG = true;
	private static final int MOD = 998244353;
	// private static final int MOD = 1_000_000_007;
	private static final char[] op = new char[]{'L', 'U', 'R', 'D'};
	private static final int[] di = new int[]{0, -1, 0, 1, -1, -1, 1, 1};
	private static final int[] dj = new int[]{-1, 0, 1, 0, -1, 1, 1, -1};
	private static final lib.io.compat17.FastScanner sc = new lib.io.compat17.FastScanner();
	private static final lib.io.compat17.FastPrinter out = new FastPrinter();
	// endregion

	private static void solve() {
		int n = sc.nextInt();
		int q = sc.nextInt();
		IntAVLSet s = new IntAVLSet();
		for (int i = 0; i < n; i++) s.add(sc.nextInt());
		while (q-- > 0) {
			int t = sc.nextInt();
			int x = sc.nextInt();
			if (t == 0) {
				s.add(x);
			} else if (t == 1) {
				s.remove(x);
			} else if (t == 2) {
				out.println(s.size() < x ? -1 : s.getByIndex(x - 1));
			} else if (t == 3) {
				int rank = s.rank(x);
				out.println(rank < 0 ? ~rank : rank + 1);
			} else if (t == 4) {
				Integer floor = s.floor(x);
				out.println(floor == null ? -1 : floor);
			} else if (t == 5) {
				Integer ceil = s.ceiling(x);
				out.println(ceil == null ? -1 : ceil);
			}
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
