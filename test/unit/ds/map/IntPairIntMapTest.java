package unit.ds.map;

import lib.ds.map.*;

final class IntPairIntMapTest extends MapContract {

	int arity() {
		return 2;
	}

	Object create() {
		return new IntPairIntMap();
	}

	Object create(final int expectedSize) {
		return new IntPairIntMap(expectedSize);
	}

	Object create(final int expectedSize, final long defaultValue) {
		return new IntPairIntMap(expectedSize, Math.toIntExact(defaultValue));
	}

	Object copy(final Object map) {
		return new IntPairIntMap((IntPairIntMap) map);
	}
}
