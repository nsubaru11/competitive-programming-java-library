package unit.ds.segmenttree;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.ds.segmenttree.*;

import static org.junit.jupiter.api.Assertions.*;

final class SegmentTreeTest {

	private static final int N = 100;

	// 汎用・int・long の3実装と愚直解を、全区間で照合する
	@Test
	void sumMatchesAcrossVariants() {
		final IntSegmentTree intTree = new IntSegmentTree(N, Integer::sum, 0);
		intTree.setAll(i -> i);
		final LongSegmentTree longTree = new LongSegmentTree(N, Long::sum, 0);
		longTree.setAll(i -> i);
		final SegmentTree<Integer> tree = new SegmentTree<>(N, Integer::sum, 0);
		tree.setAll(i -> i);
		for (int l = 0; l <= N; l++) {
			for (int r = l; r <= N; r++) {
				final String at = "l=" + l + ", r=" + r;
				final long expected = (long) (l + r - 1) * (r - l) / 2;
				assertEquals(expected, intTree.query(l, r), at);
				assertEquals(expected, longTree.query(l, r), at);
				assertEquals(expected, (long) tree.query(l, r), at);
			}
		}
	}

	@Test
	void fields() {
		final IntSegmentTree intTree = new IntSegmentTree(N, Integer::sum, 0);
		final LongSegmentTree longTree = new LongSegmentTree(N, Long::sum, 0);
		final SegmentTree<Integer> tree = new SegmentTree<>(N, Integer::sum, 0);
		assertEquals(N, intTree.size);
		assertEquals(0, intTree.identity);
		assertEquals(N, longTree.size);
		assertEquals(0, longTree.identity);
		assertEquals(N, tree.size);
		assertEquals(0, tree.identity);
	}

	@Test
	void copyKeepsValues() {
		final IntSegmentTree intTree = new IntSegmentTree(N, Integer::sum, 0);
		intTree.setAll(i -> i);
		assertEquals(intTree.query(3, 10), new IntSegmentTree(intTree).query(3, 10));
	}

	@Test
	void fromCollection() {
		final SegmentTree<Integer> tree = new SegmentTree<>(List.of(5, 1, 4), Math::min, Integer.MAX_VALUE);
		assertEquals(3, tree.size);
		assertEquals(Integer.MAX_VALUE, tree.identity);
		assertEquals(1, tree.query(0, 3));
	}
}
