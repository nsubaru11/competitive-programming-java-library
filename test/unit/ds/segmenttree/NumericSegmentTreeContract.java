package unit.ds.segmenttree;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * IntSegmentTree・LongSegmentTree の四則演算の更新を、SegmentTreeContract に加えて検査する。
 */
abstract class NumericSegmentTreeContract extends SegmentTreeContract {

	abstract Numeric createNumeric(long[] a);

	interface Numeric {
		long add(int i, long d);

		long multiply(int i, long m);

		long affine(int i, long m, long d);

		long queryAll();

		long[] toArray();
	}

	@Nested
	class ArithmeticUpdates {

		@Test
		void returnUpdatedValues() {
			final Numeric t = createNumeric(SAMPLE);
			assertEquals(9L, t.add(2, 2), "add は更新後の値");
			assertEquals(18L, t.multiply(2, 2));
			assertEquals(37L, t.affine(2, 2, 1), "affine は 現在値 * m + d");
			assertEquals(48L, t.queryAll());
			assertArrayEquals(new long[]{5, 2, 37, 1, 3}, t.toArray());
		}

		@Test
		void toArrayIsIndependent() {
			final Numeric t = createNumeric(SAMPLE);
			t.toArray()[0] = 100;
			assertEquals(18L, t.queryAll());
		}
	}
}
