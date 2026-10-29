package unit.ds.segmenttree;

import static java.util.Arrays.*;

import java.io.*;
import java.util.*;

import lib.ds.segmenttree.*;

public final class Test {

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
		assert iTree.size == n && iTree.identity == 0 && lTree.size == n && lTree.identity == 0 && tree.size == n && tree.identity == 0;
		assert new IntSegmentTree(iTree).query(3, 10) == iTree.query(3, 10);

		final SegmentTree<Integer> fromList = new SegmentTree<>(List.of(5, 1, 4), Math::min, Integer.MAX_VALUE);
		assert fromList.size == 3 && fromList.identity == Integer.MAX_VALUE && fromList.query(0, 3) == 1;
		final LazySegmentTree<Integer> lazy = new LazySegmentTree<>(List.of(5, 1, 4), Math::min, Integer.MAX_VALUE);
		lazy.apply(0, 2, 7);
		assert lazy.size == 3 && lazy.identity == Integer.MAX_VALUE && lazy.query(0, 3) == 4 && lazy.get(1) == 7;
		final LazySegmentTree<Integer> lazyAdd = new LazySegmentTree<>(List.of(5, 1, 4), Math::min, Integer.MAX_VALUE, (f, x) -> f + x, Integer::sum);
		lazyAdd.apply(1, 3, 10);
		assert lazyAdd.query(0, 3) == 5 && lazyAdd.get(2) == 14;

		final IntLazySegmentTree il = new IntLazySegmentTree(new int[]{3, 1, 2}, Math::min, Integer.MAX_VALUE);
		final IntLazySegmentTree ilCopy = new IntLazySegmentTree(il);
		ilCopy.apply(0, 3, 0);
		assert il.queryAll() == 1 && ilCopy.queryAll() == 0 && ilCopy.size == 3 && il.identity == Integer.MAX_VALUE;
		final LongLazySegmentTree ll = new LongLazySegmentTree(new long[]{3, 1, 2}, Math::min, Long.MAX_VALUE);
		assert ll.size == 3 && ll.identity == Long.MAX_VALUE && new LongLazySegmentTree(ll).query(1, 3) == 1;

		final IntSquareSumSegmentTree sq = new IntSquareSumSegmentTree(new int[]{1, 2, 3}, 7);
		assert sq.size == 3 && sq.mod == 7 && sq.query2(0, 3) == 0 && new IntSquareSumSegmentTree(sq).query(0, 3) == 6;
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
