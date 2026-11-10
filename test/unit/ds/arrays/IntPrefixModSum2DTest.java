package unit.ds.arrays;

import lib.ds.arrays.*;

import unit.support.*;

final class IntPrefixModSum2DTest extends PrefixSumContract {

	int dims() {
		return 2;
	}

	long mod() {
		return 998244353;
	}

	Object create(final int[] s, final long[] flat) {
		return new IntPrefixModSum2D((int[][]) Adapter.convert(reshape2(s, flat), int[][].class), (int) mod());
	}

	Object createByInit(final int[] s, final long[] flat) {
		return new IntPrefixModSum2D(s[0], s[1], (int) mod(), (i, j) -> (int) flat[i * s[1] + j]);
	}
}
