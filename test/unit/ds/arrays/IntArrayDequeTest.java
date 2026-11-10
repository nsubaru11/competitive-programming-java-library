package unit.ds.arrays;

import java.util.*;

import lib.ds.arrays.*;

import unit.support.*;

final class IntArrayDequeTest extends ArrayDequeContract {

	Target create() {
		return Adapter.of(Target.class, new IntArrayDeque());
	}

	Target create(final int capacity) {
		return Adapter.of(Target.class, new IntArrayDeque(capacity));
	}

	Target create(final long[] a) {
		return Adapter.of(Target.class, new IntArrayDeque(Arrays.stream(a).mapToInt(Math::toIntExact).toArray()));
	}
}
