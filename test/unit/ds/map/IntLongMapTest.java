package unit.ds.map;

import lib.ds.map.*;

final class IntLongMapTest extends MapContract {

	int arity() {
		return 1;
	}

	Object create() {
		return new IntLongMap();
	}

	Object create(final int expectedSize) {
		return new IntLongMap(expectedSize);
	}

	Object create(final int expectedSize, final long defaultValue) {
		return new IntLongMap(expectedSize, defaultValue);
	}

	Object copy(final Object map) {
		return new IntLongMap((IntLongMap) map);
	}
}
