import java.util.Arrays;
import java.util.Scanner;
import java.util.StringJoiner;

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
public class Example {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int v = sc.nextInt();
		int e = sc.nextInt();
		int[][] edges = new int[e][3];
		for (int i = 0; i < e; i++) {
			edges[i][0] = sc.nextInt() - 1;
			edges[i][1] = sc.nextInt() - 1;
			edges[i][2] = sc.nextInt();
		}
//		solveDijkstra(v, edges);
		System.out.println();
		solveBellmanFord(v, e, edges);
		System.out.println();
//		solveWarshallfroyd(v, edges);
	}

	private static void solveDijkstra(int v, int[][] edges) {
		Dijkstra dijkstra = new Dijkstra(v);
		for (int[] edge : edges) {
			dijkstra.addEdge(edge[0], edge[1], edge[2]);
		}
		StringJoiner sj = new StringJoiner("\n");
		for (int i = 0; i < v; i++) {
			StringJoiner sj2 = new StringJoiner(" ");
			for (int j = 0; j < v; j++) {
				long ans = dijkstra.getShortestPathWeight(i, j);
				String ans2 = ans == Long.MAX_VALUE ? "INF" : Long.toString(ans);
				sj2.add(ans2);
			}
			sj.add(sj2.toString());
		}
		System.out.println(sj);
	}

	private static void solveBellmanFord(int v, int e, int[][] edges) {
		BellmanFord bellmanFord = new BellmanFord(v, e);
		for (int[] edge : edges) {
			bellmanFord.addEdge(edge[0], edge[1], edge[2]);
		}
		StringJoiner sj = new StringJoiner("\n");
		for (int i = 0; i < v; i++) {
			StringJoiner sj2 = new StringJoiner(" ");
			for (int j = 0; j < v; j++) {
				long ans = bellmanFord.getShortestPathWeight(i, j);
				String ans2 = ans == Long.MAX_VALUE ? "INF" : Long.toString(ans);
				sj2.add(ans2);
			}
			sj.add(sj2.toString());
		}
		System.out.println(sj);
	}

	private static void solveWarshallfroyd(int v, int[][] edges) {
		Warshallfroyd warshallfroyd = new Warshallfroyd(v);
		for (int[] edge : edges) {
			warshallfroyd.addEdge(edge[0], edge[1], edge[2]);
		}
		StringJoiner sj = new StringJoiner("\n");
		for (int i = 0; i < v; i++) {
			StringJoiner sj2 = new StringJoiner(" ");
			for (int j = 0; j < v; j++) {
				long ans = warshallfroyd.getShortestPathWeight(i, j);
				String ans2 = ans == Long.MAX_VALUE ? "INF" : Long.toString(ans);
				sj2.add(ans2);
			}
			sj.add(sj2.toString());
		}
		System.out.println(sj.toString());
	}
}
