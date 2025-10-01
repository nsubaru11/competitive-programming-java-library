import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UnionFind 総グループ数、各グループの総辺数、頂点数を保持します。
 */
@SuppressWarnings("unused")
public final class UnionFind {
	private final int[] root, rank, size, path;
	private int cnt;

	/**
	 * 大きさ n の UnionFind を構築します。
	 *
	 * @param n 頂点数
	 */
	public UnionFind(final int n) {
		cnt = n;
		root = new int[n];
		rank = new int[n];
		size = new int[n];
		path = new int[n];
		for (int i = 0; i < n; i++) {
			size[i] = 1;
			root[i] = i;
		}
	}

	/**
	 * 引数の頂点の代表元を取得します。
	 *
	 * @param i 頂点
	 * @return 頂点iの代表元
	 */
	public int find(final int i) {
		return i != root[i] ? root[i] = find(root[i]) : i;
	}

	/**
	 * 引数の二つの頂点が同じグループに属するかどうかを判定します。
	 *
	 * @param x 頂点1
	 * @param y 頂点2
	 * @return 同じグループに属するならtrue、そうでなければfalse
	 */
	public boolean isConnected(final int x, final int y) {
		return find(x) == find(y);
	}

	/**
	 * 引数の二つの頂点を連結します。
	 * 既に連結済みの場合も辺の数はカウントされます（多重辺・自己ループ対応）。
	 *
	 * @param x 頂点1
	 * @param y 頂点2
	 * @return 新たに連結したならtrue、すでに連結済みならfalse
	 */
	public boolean union(final int x, final int y) {
		int rx = find(x);
		int ry = find(y);
		path[rx]++;
		if (rx == ry) return false;
		if (rank[rx] < rank[ry]) {
			int temp = rx;
			rx = ry;
			ry = temp;
		}
		if (rank[rx] == rank[ry]) rank[rx]++;
		root[ry] = rx;
		path[rx] += path[ry];
		size[rx] += size[ry];
		cnt--;
		return true;
	}

	/**
	 * このUnionFindのグループ数を返します。
	 *
	 * @return グループ数（連結成分の数）
	 */
	public int groupCount() {
		return cnt;
	}

	/**
	 * 引数の頂点が属するグループの総辺数を返します。
	 * 多重辺や自己ループも含みます。
	 *
	 * @param i 頂点番号
	 * @return グループの総辺数
	 */
	public int pathCount(final int i) {
		return path[find(i)];
	}

	/**
	 * 引数の頂点の属するグループの頂点数を返します。
	 *
	 * @param i 頂点番号
	 * @return グループの頂点数
	 */
	public int size(final int i) {
		return size[find(i)];
	}

	/**
	 * 全てのグループを返します。
	 * 頂点i(0 <= i < n)を代表元とする、グループのリストを返します。
	 * iが代表元でないとき、マップのキーに含まれません。
	 *
	 * @return 代表元をキー、グループに属する頂点のリストを値とするマップ
	 */
	public Map<Integer, List<Integer>> groups() {
		Map<Integer, List<Integer>> groups = new HashMap<>(cnt);
		for (int i = 0; i < root.length; i++) {
			groups.computeIfAbsent(find(i), k -> new ArrayList<>()).add(i);
		}
		return groups;
	}
}
