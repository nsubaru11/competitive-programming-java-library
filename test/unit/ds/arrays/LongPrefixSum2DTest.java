package unit.ds.arrays;

import lib.ds.arrays.*;

final class LongPrefixSum2DTest extends PrefixSumContract {

	int dims() {
		return 2;
	}

	long mod() {
		return 0;
	}

	Object create(final int[] s, final long[] flat) {
		return new LongPrefixSum2D(reshape2(s, flat));
	}

	Object createByInit(final int[] s, final long[] flat) {
		return new LongPrefixSum2D(s[0], s[1], (i, j) -> flat[i * s[1] + j]);
	}
}
