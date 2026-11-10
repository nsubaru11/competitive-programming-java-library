package unit.ds.arrays;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * IntArray2D・LongArray2D の四則演算の更新を、Array2DContract に加えて検査する。
 */
abstract class NumericArray2DContract extends Array2DContract {

	interface Numeric {
		long add(int i, int j, long d);

		long multiply(int i, int j, long m);

		long affine(int i, int j, long m, long d);

		long get(int i, int j);

		Object rotateRight();
	}

	abstract Numeric createNumeric(long[][] a);

	@Nested
	class ArithmeticUpdates {

		@Test
		void returnUpdatedValues() {
			final Numeric a = createNumeric(SAMPLE);
			assertEquals(7L, a.add(1, 1, 2), "add は更新後の値");
			assertEquals(14L, a.multiply(1, 1, 2));
			assertEquals(29L, a.affine(1, 1, 2, 1), "affine は 現在値 * m + d");
			assertEquals(29L, a.get(1, 1));
		}

		@Test
		void followOrientation() {
			final Numeric a = createNumeric(SAMPLE);
			a.rotateRight();
			// 回転後の (0, 1) は元の (0, 0)
			assertEquals(11L, a.add(0, 1, 10));
			assertThrows(IndexOutOfBoundsException.class, () -> a.add(0, 2, 1));
		}
	}
}
