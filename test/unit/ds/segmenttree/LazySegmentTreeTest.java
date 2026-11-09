package unit.ds.segmenttree;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.ds.segmenttree.*;

import static org.junit.jupiter.api.Assertions.*;

final class LazySegmentTreeTest {

	@Test
	void assignByDefault() {
		final LazySegmentTree<Integer> lazy = new LazySegmentTree<>(List.of(5, 1, 4), Math::min, Integer.MAX_VALUE);
		lazy.apply(0, 2, 7);
		assertEquals(3, lazy.size);
		assertEquals(Integer.MAX_VALUE, lazy.identity);
		assertEquals(4, lazy.query(0, 3));
		assertEquals(7, lazy.get(1));
	}

	@Test
	void customMappingAndComposition() {
		final LazySegmentTree<Integer> lazy = new LazySegmentTree<>(List.of(5, 1, 4), Math::min, Integer.MAX_VALUE, (f, x) -> f + x, Integer::sum);
		lazy.apply(1, 3, 10);
		assertEquals(5, lazy.query(0, 3));
		assertEquals(14, lazy.get(2));
	}

	@Test
	void intCopyIsIndependent() {
		final IntLazySegmentTree tree = new IntLazySegmentTree(new int[]{3, 1, 2}, Math::min, Integer.MAX_VALUE);
		final IntLazySegmentTree copy = new IntLazySegmentTree(tree);
		copy.apply(0, 3, 0);
		assertEquals(1, tree.queryAll());
		assertEquals(0, copy.queryAll());
		assertEquals(3, copy.size);
		assertEquals(Integer.MAX_VALUE, tree.identity);
	}

	@Test
	void longCopyKeepsValues() {
		final LongLazySegmentTree tree = new LongLazySegmentTree(new long[]{3, 1, 2}, Math::min, Long.MAX_VALUE);
		assertEquals(3, tree.size);
		assertEquals(Long.MAX_VALUE, tree.identity);
		assertEquals(1, new LongLazySegmentTree(tree).query(1, 3));
	}
}
