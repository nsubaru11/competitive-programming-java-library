package unit.graph;

import java.util.*;

import lib.graph.*;

final class DijkstraTest extends ShortestPathContract {

	Solver solver() {
		return of(Dijkstra::solve, Dijkstra::solve, Dijkstra::dist, Dijkstra::dist, Dijkstra::dist, Dijkstra::path);
	}

	long weight(final Random random) {
		return random.nextInt(10);
	}
}
