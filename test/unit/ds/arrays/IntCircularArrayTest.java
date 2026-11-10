package unit.ds.arrays;

import lib.ds.arrays.*;

import unit.support.*;

final class IntCircularArrayTest extends CircularArrayContract {

	Target create(final int n) {
		return Adapter.of(Target.class, new IntCircularArray(n));
	}

	Target create(final int n, final long v) {
		return Adapter.of(Target.class, new IntCircularArray(n, (int) v));
	}

	Target create(final long[] a) {
		return Adapter.of(Target.class, new IntCircularArray((int[]) Adapter.convert(a, int[].class)));
	}
}
