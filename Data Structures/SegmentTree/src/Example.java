import java.io.*;
import java.util.*;

import static java.util.Arrays.*;

public final class Example {

	// region initialization
	// ------------------------ 定数 ------------------------
	private static final boolean DEBUG;
	private static final Scanner sc;
	private static final PrintWriter out;

	static {
		DEBUG = true;
		sc = new Scanner(System.in);
		out = new PrintWriter(System.out);
	}
	// endregion

	// ------------------------ メインロジック ------------------------
	private static void solve() {
		int n = 100;
		IntSegmentTree iTree = new IntSegmentTree(n, Integer::sum, 0);
		iTree.setAll(i -> i);
		LongSegmentTree lTree = new LongSegmentTree(n, Long::sum, 0);
		lTree.setAll(i -> i);
		SegmentTree<Integer> tree = new SegmentTree<>(n, Integer::sum, 0);
		tree.setAll(i -> i);
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				assert iTree.query(i, j) == lTree.query(i, j) && iTree.query(i, j) == tree.query(i, j);
			}
		}
	}

	// region main() and debug() methods
	// ------------------------ main() 関数 ------------------------
	public static void main(final String[] args) {
		try {
			solve();
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			sc.close();
			out.close();
		}
	}

	// ------------------------ デバッグ用 ------------------------
	private static void debug(final Object... args) {
		if (DEBUG) {
			out.flush();
			System.err.println(deepToString(args));
		}
	}
	// endregion
}
