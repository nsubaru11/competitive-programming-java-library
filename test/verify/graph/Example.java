package verify.graph;

import lib.graph.*;

public final class Example {

	public static void main(String[] args) {
		UndirectedGraph graph = new UndirectedGraph(5, 10);
		graph.add(0, 1, 3);
		graph.add(0, 4, 1);
		graph.add(1, 4, 4);
		graph.add(2, 3, 2);
		graph.add(3, 4, 7);
		graph.add(1, 2, 5);
		graph.add(2, 4, 6);
		graph.add(3, 1, 2);

		System.out.println("Minimum");
		System.out.println(Kruskal.minimum(graph));
		System.out.println(Prim.minimum(graph));

		System.out.println("\nMaximum");
		System.out.println(Kruskal.maximum(graph));
		System.out.println(Prim.maximum(graph));
	}

}
