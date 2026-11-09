package unit.ds.arrays;

import org.junit.jupiter.api.*;

import lib.ds.arrays.*;
import lib.ds.fenwick.*;

import static org.junit.jupiter.api.Assertions.*;

final class PrefixSumTest {

	@Test
	void intFromCollection() {
		final IntBIT src = new IntBIT(new int[]{3, 1, 2});
		final IntPrefixSum sum = new IntPrefixSum(src);
		assertEquals(6, sum.sumAll());
		assertEquals(4, sum.sum(1));
		assertEquals(2, sum.get(2));
		final IntPrefixModSum modSum = new IntPrefixModSum(src, 5);
		assertEquals(1, modSum.sumAll());
		assertEquals(3, modSum.sum(1, 2));
	}

	@Test
	void longFromCollection() {
		final LongPrefixSum sum = new LongPrefixSum(new LongBIT(new long[]{3_000_000_000L, 1, 2}));
		assertEquals(3_000_000_003L, sum.sumAll());
		assertEquals(3, sum.sum(1, 2));
		final LongPrefixModSum modSum = new LongPrefixModSum(new LongBIT(new long[]{4, 1, 2}), 5);
		assertEquals(2, modSum.sumAll());
		assertEquals(4, modSum.get(0));
	}

	@Test
	void fromArray() {
		final LongPrefixSum sum = new LongPrefixSum(new long[]{5, 7});
		assertEquals(12, sum.sumAll());
		assertEquals(7, sum.get(1));
		assertEquals(2, new IntPrefixModSum(new int[]{4, 3}, 5).sumAll());
		assertEquals(2, new LongPrefixModSum(new long[]{4, 3}, 5).sumAll());
	}

	@Test
	void emptyArray() {
		assertEquals(0, new LongPrefixSum(new long[0]).sumAll());
	}
}
