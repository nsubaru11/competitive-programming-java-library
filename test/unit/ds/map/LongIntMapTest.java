package unit.ds.map;

import java.util.*;

import lib.ds.map.*;

final class LongIntMapTest extends MapContract {

	int arity() {
		return 1;
	}

	Object create() {
		return new LongIntMap();
	}

	Object create(final int expectedSize) {
		return new LongIntMap(expectedSize);
	}

	Object create(final int expectedSize, final long defaultValue) {
		return new LongIntMap(expectedSize, Math.toIntExact(defaultValue));
	}

	Object copy(final Object map) {
		return new LongIntMap((LongIntMap) map);
	}

	// int に収まらないキーも混ぜる
	long keyComponent(final Random random) {
		final long k = random.nextInt(-20, 20);
		return random.nextBoolean() ? k : k << 33;
	}
}
