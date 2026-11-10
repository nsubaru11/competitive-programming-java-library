package unit.math.linearalgebra;

import lib.math.linearalgebra.*;

import unit.support.*;

final class IntMatrixUtilsTest extends MatrixUtilsContract {

	Class<?> utils() {
		return IntMatrixUtils.class;
	}

	Object matrix(final long[][] a) {
		return Adapter.convert(a, int[][].class);
	}

	Object vector(final long[] v) {
		return Adapter.convert(v, int[].class);
	}
}
