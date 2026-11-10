package unit.ds.map;

import lib.ds.map.*;

final class IntTripleLongMapTest extends MapContract {

	int arity() {
		return 3;
	}

	Object create() {
		return new IntTripleLongMap();
	}

	Object create(final int expectedSize) {
		return new IntTripleLongMap(expectedSize);
	}

	Object create(final int expectedSize, final long defaultValue) {
		return new IntTripleLongMap(expectedSize, defaultValue);
	}

	Object copy(final Object map) {
		return new IntTripleLongMap((IntTripleLongMap) map);
	}
}
