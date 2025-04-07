import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UnionFind 総グループ数、各グループの総辺数、頂点数を保持します。
 */
@SuppressWarnings("unused")
public class UnionFind {
	private final int[] root, rank, size, path;
	private int cnt;

	public UnionFind(int n) {
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
	 * @param n 頂点
	 * @return 頂点xの代表元
	 */
	public int find(int n) {
		return n != root[n] ? root[n] = find(root[n]) : root[n];
	}

	/**
	 * 引数の二つの頂点が同じグループに属するかどうか。
	 *
	 * @param x 頂点1
	 * @param y 頂点2
	 * @return 同じグループに属するならtrue、そうでなければfalse
	 */
	public boolean isConnected(int x, int y) {
		return find(x) == find(y);
	}

	/**
	 * 引数の二つの頂点を連結する。
	 *
	 * @param x 頂点1
	 * @param y 頂点2
	 * @return すでに連結済みならfalse、そうでなければfalse
	 */
	public boolean union(int x, int y) {
		x = find(x);
		y = find(y);
		path[x]++;
		if (x == y) return false;
		if (rank[x] < rank[y]) {
			int temp = x;
			x = y;
			y = temp;
		}
		if (rank[x] == rank[y]) rank[x]++;
		root[y] = x;
		path[x] += path[y];
		size[x] += size[y];
		cnt--;
		return true;
	}

	/**
	 * このUnionFindのグループ数を返します。
	 *
	 * @return グループ数
	 */
	public int groupCount() {
		return cnt;
	}

	/**
	 * 引数の頂点が属するグループの総辺数を返します。
	 *
	 * @param x 頂点
	 * @return グループの総辺数
	 */
	public int pathCount(int x) {
		return path[find(x)];
	}

	/**
	 * 引数の頂点の属するグループの頂点数を返します。
	 *
	 * @param x 頂点数
	 * @return グループの頂点数
	 */
	public int size(int x) {
		return size[find(x)];
	}

	/**
	 * 頂点i(0 <= i < n)を代表元とする、グループのリストを返します。 iが代表元でないとき、要素を含みません
	 *
	 * @return 全てのグループ
	 */
	public Map<Integer, List<Integer>> groups() {
		Map<Integer, List<Integer>> groups = new HashMap<>(cnt);
		for (int i = 0; i < root.length; i++) {
			groups.computeIfAbsent(find(i), k -> new ArrayList<>()).add(i);
		}
		return groups;
	}
}
