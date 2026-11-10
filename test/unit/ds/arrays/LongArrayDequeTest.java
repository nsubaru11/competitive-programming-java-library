package unit.ds.arrays;

import java.util.*;

import lib.ds.arrays.*;

import unit.support.*;

final class LongArrayDequeTest extends ArrayDequeContract {

	Target create() {
		return Adapter.of(Target.class, new LongArrayDeque());
	}

	Target create(final int capacity) {
		return Adapter.of(Target.class, new LongArrayDeque(capacity));
	}

	Target create(final long[] a) {
		return Adapter.of(Target.class, new LongArrayDeque(a));
	}
}
