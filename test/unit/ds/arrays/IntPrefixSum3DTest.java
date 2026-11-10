package unit.ds.arrays;

import lib.ds.arrays.*;

import unit.support.*;

final class IntPrefixSum3DTest extends PrefixSumContract {

	int dims() {
		return 3;
	}

	long mod() {
		return 0;
	}

	Object create(final int[] s, final long[] flat) {
		return new IntPrefixSum3D((int[][][]) Adapter.convert(reshape3(s, flat), int[][][].class));
	}

	Object createByInit(final int[] s, final long[] flat) {
		return new IntPrefixSum3D(s[0], s[1], s[2], (i, j, k) -> (int) flat[(i * s[1] + j) * s[2] + k]);
	}
}
