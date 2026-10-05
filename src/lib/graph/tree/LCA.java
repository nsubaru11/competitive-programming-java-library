package lib.graph.tree;

import static java.lang.Math.*;
import static java.util.Arrays.*;

import lib.math.*;

/**
 * ±1 RMQ（Farach-Colton and Bender のアルゴリズム）を用いた最小共通祖先（LCA）クエリ。
 * <p>
 * オイラーツアーにより木上の LCA を隣接要素の深さの差が ±1 の静的区間最小値（RMQ）に帰着します。
 * 前処理 {@code O(N)}、クエリ {@code O(1)}。
 */
@SuppressWarnings("unused")
public final class LCA {
	private final RootedTree tree;
	private final int shift, mask;
	private final int[][] table;
	private final int[] position, order, depth, patterns, lookup, logTable;

	/**
	 * 根付き木から LCA クエリ用の構造を前処理します。
	 *
	 * @param tree 根付き木
	 */
	public LCA(final RootedTree tree) {
		tree.ensureBuild();
		this.tree = tree;
		depth = tree.depth;
		final int n = tree.n, m = (n << 1) - 1;
		final int[] dest = tree.dest(), next = tree.next(), edgeIter = tree.first().clone();
		final int[] stack = new int[n];
		position = new int[n];
		order = new int[m];
		stack[0] = tree.root;
		outer:
		for (int len = 1, i = 0; len > 0; i++) {
			final int u = stack[len - 1];
			order[i] = u;
			if (position[u] == 0) position[u] = i;
			while (edgeIter[u] != -1) {
				final int e = edgeIter[u], v = dest[e];
				edgeIter[u] = next[e];
				if (tree.parent[u] == v) continue;
				stack[len++] = v;
				continue outer;
			}
			len--;
		}
		position[tree.root] = 0;

		final int blockSize = Integer.highestOneBit(max(1, MathUtils.ceilLog2(m) >> 1));
		mask = blockSize - 1;
		shift = Integer.numberOfTrailingZeros(blockSize);
		final int blockCnt = ceilDiv(m, blockSize);
		patterns = new int[blockCnt];
		final int[] table0 = new int[blockCnt];
		final int patternCnt = 1 << (blockSize - 1);
		lookup = new int[patternCnt * blockSize * blockSize];
		fill(lookup, -1);

		for (int i = 0, j = 0; i < blockCnt; i++, j += blockSize) {
			table0[i] = order[j];
			int pattern = 0;
			final int lenI = min(blockSize, m - j);
			for (int ki = 1; ki < lenI; ki++) {
				final int u = order[j + ki];
				if (depth[u] < depth[table0[i]]) table0[i] = u;
				if (depth[order[j + ki - 1]] < depth[u]) pattern |= 1 << (ki - 1);
			}
			patterns[i] = pattern;
			final int base = pattern * blockSize * blockSize;
			if (lookup[base] != -1) continue;
			for (int l = 0; l < lenI; l++) {
				int lr = base + l * blockSize + l;
				lookup[lr++] = l;
				for (int r = l + 1; r < lenI; r++, lr++) {
					final int prev = lookup[lr - 1];
					lookup[lr] = depth[order[j + r]] < depth[order[j + prev]] ? r : prev;
				}
			}
		}

		final int k = MathUtils.floorLog2(blockCnt);
		table = new int[k + 1][];
		table[0] = table0;
		for (int ki = 1; ki <= k; ki++) {
			final int width = 1 << ki, half = width >> 1;
			table[ki] = new int[blockCnt - width + 1];
			for (int i = 0; i + width <= blockCnt; i++) {
				final int u1 = table[ki - 1][i], u2 = table[ki - 1][i + half];
				table[ki][i] = minNode(u1, u2);
			}
		}

		logTable = new int[blockCnt + 1];
		for (int i = 2; i <= blockCnt; i++) logTable[i] = logTable[i >> 1] + 1;
	}

	/**
	 * 2頂点 {@code u} と {@code v} の最小共通祖先（LCA）を返します。
	 *
	 * @param u 頂点番号
	 * @param v 頂点番号
	 * @return 最小共通祖先
	 */
	public int lca(final int u, final int v) {
		int i = position[u], j = position[v];
		if (i > j) {
			final int t = i;
			i = j;
			j = t;
		}
		int bi = i >> shift, bj = j >> shift;
		final int di = i & mask, dj = j & mask, s2 = shift << 1;

		if (bi == bj) return order[(bi << shift) + lookup[(patterns[bi] << s2) + (di << shift) + dj]];
		final int l = order[(bi << shift) + lookup[(patterns[bi] << s2) + (di << shift) + mask]];
		final int r = order[(bj << shift) + lookup[(patterns[bj] << s2) + dj]];
		int ans = minNode(l, r);
		if (++bi < bj) {
			final int k = logTable[bj - bi];
			ans = minNode(ans, minNode(table[k][bi], table[k][bj - (1 << k)]));
		}
		return ans;
	}

	/**
	 * 根からの重み付き距離とLCAから2頂点間の距離を返します。
	 *
	 * @param u 始点
	 * @param v 終点
	 * @return 2頂点間の重み付き距離
	 */
	public long distance(final int u, final int v) {
		final long[] rootDistance = tree.rootDistance;
		return rootDistance[u] + rootDistance[v] - (rootDistance[lca(u, v)] << 1);
	}

	private int minNode(final int u, final int v) {
		return depth[u] <= depth[v] ? u : v;
	}
}
