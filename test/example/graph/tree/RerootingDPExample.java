package example.graph.tree;

import java.util.*;

import lib.graph.tree.*;

public final class RerootingDPExample {

	public static void main(final String[] args) {
		// 木
		int n = 6;
		RootedTree t = new RootedTree(n, 0);
		t.add(0, 1, 1);
		t.add(0, 2, 1);
		t.add(1, 3, 1);
		t.add(1, 4, 1);
		t.add(2, 5, 1);

		// 部分木の大きさと、部分木内の距離の和
		int[] size = new int[n];
		long[] dist = new long[n];
		Arrays.fill(size, 1);

		// 全頂点への距離の和
		RerootingDP.run(t,
				(v, p, e) -> {
					size[p] += size[v];
					dist[p] += dist[v] + size[v];
				},
				(p, c, e) -> dist[c] = dist[p] - size[c] + (n - size[c]));
		System.out.println(Arrays.toString(dist));
	}
}
