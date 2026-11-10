package unit.ds.arrays;

import lib.ds.arrays.*;

final class LongPrefixModSum2DTest extends PrefixSumContract {

	int dims() {
		return 2;
	}

	long mod() {
		return (1L << 61) - 1;
	}

	Object create(final int[] s, final long[] flat) {
		return new LongPrefixModSum2D(reshape2(s, flat), mod());
	}

	Object createByInit(final int[] s, final long[] flat) {
		return new LongPrefixModSum2D(s[0], s[1], mod(), (i, j) -> flat[i * s[1] + j]);
	}
}
