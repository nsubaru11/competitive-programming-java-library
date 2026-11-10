package unit.ds.arrays;

import lib.ds.arrays.*;

import unit.support.*;

final class IntPrefixSum2DTest extends PrefixSumContract {

	int dims() {
		return 2;
	}

	long mod() {
		return 0;
	}

	Object create(final int[] s, final long[] flat) {
		return new IntPrefixSum2D((int[][]) Adapter.convert(reshape2(s, flat), int[][].class));
	}

	Object createByInit(final int[] s, final long[] flat) {
		return new IntPrefixSum2D(s[0], s[1], (i, j) -> (int) flat[i * s[1] + j]);
	}
}
