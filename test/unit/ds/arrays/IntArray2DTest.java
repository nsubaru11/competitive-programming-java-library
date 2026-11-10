package unit.ds.arrays;

import org.junit.jupiter.api.*;

import lib.ds.arrays.*;

import unit.support.*;

import static org.junit.jupiter.api.Assertions.*;

final class IntArray2DTest extends NumericArray2DContract {

	Target create(final int h, final int w) {
		return Adapter.of(Target.class, new IntArray2D(h, w));
	}

	Target create(final int h, final int w, final long v) {
		return Adapter.of(Target.class, new IntArray2D(h, w, Math.toIntExact(v)));
	}

	Target create(final int h, final int w, final long[] a) {
		return Adapter.of(Target.class, new IntArray2D(h, w, (int[]) Adapter.convert(a, int[].class)));
	}

	Target create(final long[][] a) {
		return Adapter.of(Target.class, new IntArray2D((int[][]) Adapter.convert(a, int[][].class)));
	}

	Numeric createNumeric(final long[][] a) {
		return Adapter.of(Numeric.class, new IntArray2D((int[][]) Adapter.convert(a, int[][].class)));
	}

	@Test
	void toStringSeparatesWithSpaceAndNewline() {
		assertEquals("1 2 3\n4 5 6", new IntArray2D(new int[][]{{1, 2, 3}, {4, 5, 6}}).toString());
		assertEquals("", new IntArray2D(0, 0).toString());
	}
}
