package unit.graph;

import java.util.*;

import lib.graph.*;

final class BFSTest extends ShortestPathContract {

	Solver solver() {
		return of(BFS::solve, BFS::solve, BFS::dist, BFS::dist, BFS::dist, BFS::path);
	}

	long weight(final Random random) {
		return 1;
	}
}
