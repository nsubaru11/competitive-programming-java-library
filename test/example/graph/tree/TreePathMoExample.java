package example.graph.tree;

import java.util.*;

import lib.graph.tree.*;

public final class TreePathMoExample {

	public static void main(final String[] args) {
		// 木と頂点の色
		RootedTree t = new RootedTree(7, 0);
		t.add(0, 1, 1);
		t.add(0, 2, 1);
		t.add(1, 3, 1);
		t.add(1, 4, 1);
		t.add(2, 5, 1);
		t.add(5, 6, 1);
		int[] color = {0, 1, 0, 2, 1, 2, 2};

		// パスのクエリ (u, v)
		int[][] uv = {{3, 4, 6}, {6, 2, 6}};

		// 状態
		int[] cnt = new int[3];
		int[] distinct = {0};
		int[] ans = new int[uv[0].length];

		// パス上の色の種類数
		TreePathMo.run(t, uv,
				v -> {
					if (cnt[color[v]]++ == 0) distinct[0]++;
				},
				v -> {
					if (--cnt[color[v]] == 0) distinct[0]--;
				},
				q -> ans[q] = distinct[0]);
		System.out.println(Arrays.toString(ans));
	}
}
