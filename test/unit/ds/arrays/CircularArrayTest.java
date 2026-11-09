package unit.ds.arrays;

import org.junit.jupiter.api.*;

import lib.ds.arrays.*;
import lib.ds.fenwick.*;

import static org.junit.jupiter.api.Assertions.*;

final class CircularArrayTest {

	@Test
	void intFromCollectionKeepsOrder() {
		assertEquals("3 1 2", new IntCircularArray(new IntBIT(new int[]{3, 1, 2})).toString());
	}

	@Test
	void longFromCollectionKeepsOrder() {
		assertEquals("3000000000 1 2", new LongCircularArray(new LongBIT(new long[]{3_000_000_000L, 1, 2})).toString());
	}
}
