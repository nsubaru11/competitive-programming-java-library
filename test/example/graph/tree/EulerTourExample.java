package example.graph.tree;

import lib.graph.tree.*;

public final class EulerTourExample {

	public static void main(final String[] args) {
		// 木
		RootedTree t = new RootedTree(5, 0);
		t.add(0, 1, 1);
		t.add(0, 2, 1);
		t.add(1, 3, 1);
		t.add(1, 4, 1);
		EulerTour et = new EulerTour(t);

		// イベント列
		System.out.println(et.length());
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < et.length(); i++) {
			sb.append(et.eventAt(i)).append(' ');
		}
		System.out.println(sb.toString().trim());
		System.out.println();

		// 入場・退場の位置
		for (int u = 0; u < t.n; u++) {
			System.out.println(u + " " + et.enter(u) + " " + et.exit(u));
		}
	}
}
