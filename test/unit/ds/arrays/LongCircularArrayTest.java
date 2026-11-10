package unit.ds.arrays;

import lib.ds.arrays.*;

import unit.support.*;

final class LongCircularArrayTest extends CircularArrayContract {

	Target create(final int n) {
		return Adapter.of(Target.class, new LongCircularArray(n));
	}

	Target create(final int n, final long v) {
		return Adapter.of(Target.class, new LongCircularArray(n, v));
	}

	Target create(final long[] a) {
		return Adapter.of(Target.class, new LongCircularArray(a));
	}
}
