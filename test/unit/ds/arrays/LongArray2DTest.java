package unit.ds.arrays;

import org.junit.jupiter.api.*;

import lib.ds.arrays.*;

import unit.support.*;

import static org.junit.jupiter.api.Assertions.*;

final class LongArray2DTest extends NumericArray2DContract {

	Target create(final int h, final int w) {
		return Adapter.of(Target.class, new LongArray2D(h, w));
	}

	Target create(final int h, final int w, final long v) {
		return Adapter.of(Target.class, new LongArray2D(h, w, v));
	}

	Target create(final int h, final int w, final long[] a) {
		return Adapter.of(Target.class, new LongArray2D(h, w, a));
	}

	Target create(final long[][] a) {
		return Adapter.of(Target.class, new LongArray2D(a));
	}

	Numeric createNumeric(final long[][] a) {
		return Adapter.of(Numeric.class, new LongArray2D(a));
	}

	@Test
	void toStringSeparatesWithSpaceAndNewline() {
		assertEquals("1 2 3\n4 5 6", new LongArray2D(new long[][]{{1, 2, 3}, {4, 5, 6}}).toString());
		assertEquals("", new LongArray2D(0, 0).toString());
	}
}
