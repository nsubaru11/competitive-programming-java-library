public class Example {

	public static void main(String[] args) {
		Kruskal k = new Kruskal(5);
		k.addEdge(0, 1, 3);
		k.addEdge(0, 4, 1);
		k.addEdge(1, 4, 4);
		k.addEdge(2, 3, 2);
		k.addEdge(3, 4, 7);
		k.addEdge(1, 2, 5);
		k.addEdge(2, 4, 6);
		k.addEdge(3, 1, 2);
		System.out.println(k.solve());

		Prim p = new Prim(5);
		p.addEdge(0, 1, 3);
		p.addEdge(0, 4, 1);
		p.addEdge(1, 4, 4);
		p.addEdge(2, 3, 2);
		p.addEdge(3, 4, 7);
		p.addEdge(1, 2, 5);
		p.addEdge(2, 4, 6);
		p.addEdge(3, 1, 2);
		System.out.println(p.solve());
	}

}
