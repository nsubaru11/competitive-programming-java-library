package unit.ds.map;

import lib.ds.map.*;

final class IntPairLongMapTest extends MapContract {

	int arity() {
		return 2;
	}

	Object create() {
		return new IntPairLongMap();
	}

	Object create(final int expectedSize) {
		return new IntPairLongMap(expectedSize);
	}

	Object create(final int expectedSize, final long defaultValue) {
		return new IntPairLongMap(expectedSize, defaultValue);
	}

	Object copy(final Object map) {
		return new IntPairLongMap((IntPairLongMap) map);
	}
}
