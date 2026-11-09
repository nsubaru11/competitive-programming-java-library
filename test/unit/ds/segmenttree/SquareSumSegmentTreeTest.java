package unit.ds.segmenttree;

import org.junit.jupiter.api.*;

import lib.ds.segmenttree.*;

import static org.junit.jupiter.api.Assertions.*;

final class SquareSumSegmentTreeTest {

	@Test
	void sumAndSquareSum() {
		final IntSquareSumSegmentTree tree = new IntSquareSumSegmentTree(new int[]{1, 2, 3}, 7);
		assertEquals(3, tree.size);
		assertEquals(7, tree.mod);
		// 1 + 4 + 9 = 14 ≡ 0 (mod 7)
		assertEquals(0, tree.query2(0, 3));
		assertEquals(6, new IntSquareSumSegmentTree(tree).query(0, 3));
	}
}
