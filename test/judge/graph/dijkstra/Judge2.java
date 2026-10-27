package judge.graph.dijkstra;

import static java.util.Arrays.*;

import java.io.*;
import java.util.*;
import java.util.stream.*;

import lib.graph.*;

// https://onlinejudge.u-aizu.ac.jp/courses/library/5/GRL/1/GRL_1_A
public final class Judge2 {

	// region < Constants & Globals >
	private static final boolean DEBUG = true;
	private static final int MOD = 998244353;
	// private static final int MOD = 1_000_000_007;
	private static final char[] op = new char[]{'L', 'U', 'R', 'D'};
	private static final int[] di = new int[]{0, -1, 0, 1, -1, -1, 1, 1};
	private static final int[] dj = new int[]{-1, 0, 1, 0, -1, 1, 1, -1};
	private static final Scanner sc = new Scanner(System.in);
	private static final PrintWriter out = new PrintWriter(System.out, false);
	// endregion

	private static void solve() {
		int v = sc.nextInt(), e = sc.nextInt();
		int r = sc.nextInt();
		DirectedGraph graph = new DirectedGraph(v, e);
		graph.setAll(sc::nextInt, sc::nextInt, sc::nextInt);
		long[] dist = Dijkstra.dist(graph, r);
		for (int i = 0; i < v; i++) {
			out.println(dist[i] == Long.MAX_VALUE ? "INF" : dist[i]);
		}
	}

	// region < main & debug >
	public static void main(final String[] args) {
		try {
			solve();
		} finally {
			sc.close();
			out.close();
		}
	}

	private static void debugln(final Object... args) {
		if (DEBUG) {
			out.flush();
			if (args == null) System.err.println("null");
			else if (args.getClass().getComponentType().isArray()) System.err.println(stringify(args));
			else System.err.println(stream(args).map(Judge2::stringify).collect(Collectors.joining("\n", "\n", "")));
		}
	}

	private static void debug(final Object... args) {
		if (DEBUG) {
			out.flush();
			if (args == null) System.err.println("null");
			else if (args.getClass().getComponentType().isArray()) System.err.println(stringify(args));
			else System.err.println(stream(args).map(Judge2::stringify).collect(Collectors.joining(", ", "", "")));
		}
	}

	private static String stringify(final Object obj) {
		if (obj == null) return "null";
		else if (obj instanceof int[][] arr) {
			return "\n" + stream(arr).map(Arrays::toString).collect(Collectors.joining("\n"));
		} else if (obj instanceof long[][] arr) {
			return "\n" + stream(arr).map(Arrays::toString).collect(Collectors.joining("\n"));
		} else if (obj instanceof char[][] arr) {
			return "\n" + stream(arr).map(String::valueOf).collect(Collectors.joining("\n"));
		} else if (obj instanceof Object[][] arr) {
			return "\n" + stream(arr).map(Arrays::deepToString).collect(Collectors.joining("\n"));
		} else if (obj instanceof int[] arr) {
			return Arrays.toString(arr);
		} else if (obj instanceof long[] arr) {
			return Arrays.toString(arr);
		} else if (obj instanceof double[] arr) {
			return Arrays.toString(arr);
		} else if (obj instanceof char[] arr) {
			return Arrays.toString(arr);
		} else if (obj instanceof boolean[] arr) {
			return Arrays.toString(arr);
		} else if (obj instanceof Object[] arr) {
			return deepToString(arr);
		} else if (obj instanceof Iterable<?> it) {
			final StringJoiner sj = new StringJoiner(", ", "[", "]");
			for (final Object e : it) sj.add(stringify(e));
			return sj.toString();
		}
		return obj.toString();
	}
	// endregion
}
