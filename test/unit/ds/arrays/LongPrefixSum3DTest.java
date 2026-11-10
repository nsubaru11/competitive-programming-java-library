package unit.ds.arrays;

import lib.ds.arrays.*;

final class LongPrefixSum3DTest extends PrefixSumContract {

	int dims() {
		return 3;
	}

	long mod() {
		return 0;
	}

	Object create(final int[] s, final long[] flat) {
		return new LongPrefixSum3D(reshape3(s, flat));
	}

	Object createByInit(final int[] s, final long[] flat) {
		return new LongPrefixSum3D(s[0], s[1], s[2], (i, j, k) -> flat[(i * s[1] + j) * s[2] + k]);
	}
}
