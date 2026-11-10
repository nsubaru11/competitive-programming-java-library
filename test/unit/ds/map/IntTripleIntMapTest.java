package unit.ds.map;

import lib.ds.map.*;

final class IntTripleIntMapTest extends MapContract {

	int arity() {
		return 3;
	}

	Object create() {
		return new IntTripleIntMap();
	}

	Object create(final int expectedSize) {
		return new IntTripleIntMap(expectedSize);
	}

	Object create(final int expectedSize, final long defaultValue) {
		return new IntTripleIntMap(expectedSize, Math.toIntExact(defaultValue));
	}

	Object copy(final Object map) {
		return new IntTripleIntMap((IntTripleIntMap) map);
	}
}
