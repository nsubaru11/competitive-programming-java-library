package unit.ds.arrays;

import lib.ds.arrays.*;

final class LongPrefixModSum3DTest extends PrefixSumContract {

	int dims() {
		return 3;
	}

	long mod() {
		return (1L << 61) - 1;
	}

	Object create(final int[] s, final long[] flat) {
		return new LongPrefixModSum3D(reshape3(s, flat), mod());
	}

	Object createByInit(final int[] s, final long[] flat) {
		return new LongPrefixModSum3D(s[0], s[1], s[2], mod(), (i, j, k) -> flat[(i * s[1] + j) * s[2] + k]);
	}
}
