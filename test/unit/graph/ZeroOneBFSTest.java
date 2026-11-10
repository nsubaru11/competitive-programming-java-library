package unit.graph;

import java.util.*;

import lib.graph.*;

final class ZeroOneBFSTest extends ShortestPathContract {

	Solver solver() {
		return of(ZeroOneBFS::solve, ZeroOneBFS::solve, ZeroOneBFS::dist, ZeroOneBFS::dist, ZeroOneBFS::dist, ZeroOneBFS::path);
	}

	long weight(final Random random) {
		return random.nextInt(2);
	}
}
