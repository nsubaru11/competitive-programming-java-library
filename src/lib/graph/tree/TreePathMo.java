package lib.graph.tree;

import static java.lang.Math.*;
import static java.util.Arrays.*;

import java.util.function.*;

/**
 * Euler Tour 上の区間に変換した木上の静的パスクエリを Mo's Algorithm でオフライン処理する。
 * 木の頂点数を {@code N}、クエリ数を {@code Q}、区間移動1回の処理時間を {@code C} とすると、
 * ブロック幅 {@code B} に対する計算量は概ね {@code O(Q log Q + (QB + N^2 / B) C)}、
 * 空間計算量は {@code O(N + Q)}。
 */
@SuppressWarnings("unused")
public final class TreePathMo {
	private static final int IDX_BITS = 20;
	private static final long IDX_MASK = (1L << IDX_BITS) - 1;

	private TreePathMo() {}

	public static void run(RootedTree tree, final int[][] uv, final IntConsumer addNode, final IntConsumer removeNode, final IntConsumer query) {
		final int[] from = uv[0], to = uv[1];
		final int q = from.length;
		if (q == 0) return;
		final EulerTour eulerTour = new EulerTour(tree);
		final int[] left = new int[q], right = new int[q], lcas = new int[q], event = eulerTour.event;
		fill(lcas, -1);
		final long[] order = new long[q];
		final int len = eulerTour.length(), blockSize = defaultBlockSize(len, q);
		final LCA lca = new LCA(tree);
		for (int i = 0; i < q; i++) {
			int u = from[i], v = to[i];
			if (eulerTour.enter(u) > eulerTour.enter(v)) {
				final int temp = u;
				u = v;
				v = temp;
			}
			left[i] = eulerTour.enter(u);
			right[i] = eulerTour.enter(v) + 1;
			if (!tree.isAncestor(u, v)) {
				left[i] = eulerTour.exit(u);
				lcas[i] = lca.lca(u, v);
			}
			int lb = left[i] / blockSize, r = right[i];
			if ((lb & 1) == 1) r = len - r;
			order[i] = ((long) lb << (IDX_BITS << 1)) | ((long) r << IDX_BITS) | i;
		}
		sort(order);
		int curL = 0, curR = 0;
		final boolean[] contain = new boolean[tree.n];
		for (final long entry : order) {
			final int id = (int) (entry & IDX_MASK);
			final int ql = left[id], qr = right[id];
			while (curL > ql) {
				final int u = event[--curL];
				contain[u] = !contain[u];
				if (contain[u]) addNode.accept(u);
				else removeNode.accept(u);
			}
			while (curR < qr) {
				final int u = event[curR++];
				contain[u] = !contain[u];
				if (contain[u]) addNode.accept(u);
				else removeNode.accept(u);
			}
			while (curL < ql) {
				final int u = event[curL++];
				contain[u] = !contain[u];
				if (contain[u]) addNode.accept(u);
				else removeNode.accept(u);
			}
			while (curR > qr) {
				final int u = event[--curR];
				contain[u] = !contain[u];
				if (contain[u]) addNode.accept(u);
				else removeNode.accept(u);
			}
			if (lcas[id] != -1) addNode.accept(lcas[id]);
			query.accept(id);
			if (lcas[id] != -1) removeNode.accept(lcas[id]);
		}
	}

	private static int defaultBlockSize(final int n, final int q) {
		return max(1, (int) (n / sqrt(q * 2.0 / 3.0)));
	}
}
