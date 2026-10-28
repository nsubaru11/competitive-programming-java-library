package unit.ds.fenwick;

import lib.ds.fenwick.*;

public final class Test {

	public static void main(final String[] args) {
		testIntBITBounds();
		testLongBITBounds();
	}

	private static void testIntBITBounds() {
		final IntBIT bit = new IntBIT(new int[]{2, 0, 3, 1});
		check(bit.lowerBound(1) == 0);
		check(bit.lowerBound(2) == 0);
		check(bit.lowerBound(3) == 2);
		check(bit.lowerBound(6) == 3);
		check(bit.lowerBound(7) == 4);
		check(bit.upperBound(-1) == 0);
		check(bit.upperBound(0) == 0);
		check(bit.upperBound(2) == 2);
		check(bit.upperBound(5) == 3);
		check(bit.upperBound(6) == 4);

		final IntBIT zeroHead = new IntBIT(new int[]{0, 0, 1});
		check(zeroHead.lowerBound(0) == 0);
		check(zeroHead.upperBound(0) == 2);
	}

	private static void testLongBITBounds() {
		final LongBIT bit = new LongBIT(new long[]{2, 0, 3, 1});
		check(bit.lowerBound(1L) == 0);
		check(bit.lowerBound(2L) == 0);
		check(bit.lowerBound(3L) == 2);
		check(bit.lowerBound(6L) == 3);
		check(bit.lowerBound(7L) == 4);
		check(bit.upperBound(-1L) == 0);
		check(bit.upperBound(0L) == 0);
		check(bit.upperBound(2L) == 2);
		check(bit.upperBound(5L) == 3);
		check(bit.upperBound(6L) == 4);

		final LongBIT zeroHead = new LongBIT(new long[]{0, 0, 1});
		check(zeroHead.lowerBound(0L) == 0);
		check(zeroHead.upperBound(0L) == 2);
	}

	private static void check(final boolean condition) {
		if (!condition) throw new AssertionError();
	}
}
