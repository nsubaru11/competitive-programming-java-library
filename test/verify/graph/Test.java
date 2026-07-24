package verify.graph;

import lib.graph.*;
import lib.io.compat17.*;

import java.util.*;

/**
 * 各アルゴリズム（Dijkstra, Bellman-Ford, Warshall-Floyd）による最短経路計算の例。
 * 標準入力からグラフの頂点数と辺情報を読み込み、それぞれのアルゴリズムで計算した
 * 全点対の最短経路を出力する。
 * <p>
 * 入力例:
 * 5 10
 * 1 3 2
 * 4 2 1
 * 3 2 1
 * 4 3 2
 * 5 2 10
 * 3 2 2
 * 3 3 8
 * 2 5 9
 * 4 1 7
 * 1 2 7
 * <p>
 * 出力例:
 * 0 3 2 INF 12
 * INF 0 INF INF 9
 * INF 1 0 INF 10
 * 7 1 2 0 10
 * INF 10 INF INF 0
 */
public class Test {

	public static void main(String[] args) {
		FastScanner sc = new FastScanner();
		int n = sc.nextInt(), m = sc.nextInt();
		DirectedGraph graph = new DirectedGraph(n, m);
		graph.setAll(sc::nextInt0, sc::nextInt0, sc::nextLong);
		solveDijkstra(graph);
		System.out.println();
		solveBellmanFord(graph);
		System.out.println();
		solveWarshallFloyd(graph);
	}

	private static void solveDijkstra(DirectedGraph graph) {
		int n = graph.n;
		StringJoiner sj = new StringJoiner("\n");
		for (int i = 0; i < n; i++) {
			StringJoiner sj2 = new StringJoiner(" ");
			var result = Dijkstra.solve(graph, i);
			for (int j = 0; j < n; j++) {
				long ans = result.distTo(j);
				String str = ans == Long.MAX_VALUE ? "INF" : Long.toString(ans);
				sj2.add(str);
			}
			sj.add(sj2.toString());
		}
		System.out.println(sj);
	}

	private static void solveBellmanFord(DirectedGraph graph) {
		int n = graph.n;
		StringJoiner sj = new StringJoiner("\n");
		for (int i = 0; i < n; i++) {
			StringJoiner sj2 = new StringJoiner(" ");
			var result = BellmanFord.solve(graph, i);
			for (int j = 0; j < n; j++) {
				long ans = result.distTo(j);
				String ans2 = ans == Long.MAX_VALUE ? "INF" : Long.toString(ans);
				sj2.add(ans2);
			}
			sj.add(sj2.toString());
		}
		System.out.println(sj);
	}

	private static void solveWarshallFloyd(DirectedGraph graph) {
		int n = graph.n;
		StringJoiner sj = new StringJoiner("\n");
		var result = WarshallFloyd.solve(graph);
		for (int i = 0; i < n; i++) {
			StringJoiner sj2 = new StringJoiner(" ");
			for (int j = 0; j < n; j++) {
				long ans = result.dist(i, j);
				String ans2 = ans == Long.MAX_VALUE ? "INF" : Long.toString(ans);
				sj2.add(ans2);
			}
			sj.add(sj2.toString());
		}
		System.out.println(sj);
	}
}
