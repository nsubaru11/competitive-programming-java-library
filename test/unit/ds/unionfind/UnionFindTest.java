package unit.ds.unionfind;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.ds.unionfind.*;

import unit.support.*;

import static org.junit.jupiter.api.Assertions.*;

final class UnionFindTest {

	@RepeatedTest(100)
	void matchesNaive(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final int n = 1 + random.nextInt(30);
		final UnionFind uf = new UnionFind(n);
		final int[] label = new int[n], edges = new int[n];
		for (int i = 0; i < n; i++) label[i] = i;
		new RandomOperations()
				.add("union", 3, (r, at) -> {
					final int x = r.nextInt(n), y = r.nextInt(n), lx = label[x], ly = label[y];
					assertEquals(lx != ly, uf.union(x, y), at);
					edges[lx]++;
					if (lx == ly) return;
					edges[lx] += edges[ly];
					for (int i = 0; i < n; i++) if (label[i] == ly) label[i] = lx;
				})
				.add("query", 2, (r, at) -> {
					final int x = r.nextInt(n), y = r.nextInt(n);
					assertEquals(label[x] == label[y], uf.isConnected(x, y), at);
					assertEquals(Arrays.stream(label).filter(l -> l == label[x]).count(), uf.size(x), at);
					assertEquals(edges[label[x]], uf.pathCount(x), at);
					assertEquals(Arrays.stream(label).distinct().count(), uf.groupCount(), at);
				})
				.run(seed, random, 200);
	}

	@Nested
	class State {

		@Test
		void initiallySingletons() {
			final UnionFind uf = new UnionFind(4);
			assertEquals(4, uf.groupCount());
			for (int i = 0; i < 4; i++) {
				assertEquals(i, uf.find(i));
				assertEquals(1, uf.size(i));
				assertEquals(0, uf.pathCount(i));
			}
		}
	}

	@Nested
	class Updates {

		@Test
		void unionReturnsWhetherMerged() {
			final UnionFind uf = new UnionFind(5);
			assertTrue(uf.union(0, 1));
			assertTrue(uf.union(1, 2));
			assertFalse(uf.union(0, 2), "連結済みなら false");
			assertEquals(3, uf.groupCount());
			assertEquals(3, uf.size(2));
			assertTrue(uf.isConnected(0, 2));
			assertFalse(uf.isConnected(0, 3));
			assertEquals(uf.find(0), uf.find(2));
		}

		@Test
		void pathCountIncludesMultiEdgesAndSelfLoops() {
			final UnionFind uf = new UnionFind(4);
			uf.union(0, 1);
			uf.union(1, 0);
			uf.union(2, 2);
			uf.union(1, 2);
			assertEquals(4, uf.pathCount(0));
			assertEquals(0, uf.pathCount(3));
		}
	}

	@Nested
	class Conversion {

		@Test
		void groupsAreKeyedByRepresentative() {
			final UnionFind uf = new UnionFind(5);
			uf.union(0, 3);
			uf.union(4, 3);
			final Map<Integer, List<Integer>> groups = uf.groups();
			assertEquals(3, groups.size());
			assertEquals(List.of(0, 3, 4), groups.get(uf.find(0)));
			assertEquals(List.of(1), groups.get(1));
			assertEquals(List.of(2), groups.get(2));
		}
	}
}
