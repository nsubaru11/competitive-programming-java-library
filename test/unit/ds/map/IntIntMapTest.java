package unit.ds.map;

import lib.ds.map.*;

final class IntIntMapTest extends MapContract {

	int arity() {
		return 1;
	}

	Object create() {
		return new IntIntMap();
	}

	Object create(final int expectedSize) {
		return new IntIntMap(expectedSize);
	}

	Object create(final int expectedSize, final long defaultValue) {
		return new IntIntMap(expectedSize, Math.toIntExact(defaultValue));
	}

	Object copy(final Object map) {
		return new IntIntMap((IntIntMap) map);
	}
}
