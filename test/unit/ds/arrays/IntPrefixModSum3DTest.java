package unit.ds.arrays;

import lib.ds.arrays.*;

import unit.support.*;

final class IntPrefixModSum3DTest extends PrefixSumContract {

	int dims() {
		return 3;
	}

	long mod() {
		return 998244353;
	}

	Object create(final int[] s, final long[] flat) {
		return new IntPrefixModSum3D((int[][][]) Adapter.convert(reshape3(s, flat), int[][][].class), (int) mod());
	}

	Object createByInit(final int[] s, final long[] flat) {
		return new IntPrefixModSum3D(s[0], s[1], s[2], (int) mod(), (i, j, k) -> (int) flat[(i * s[1] + j) * s[2] + k]);
	}
}
