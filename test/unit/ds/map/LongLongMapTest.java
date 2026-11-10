package unit.ds.map;

import java.util.*;

import lib.ds.map.*;

final class LongLongMapTest extends MapContract {

	int arity() {
		return 1;
	}

	Object create() {
		return new LongLongMap();
	}

	Object create(final int expectedSize) {
		return new LongLongMap(expectedSize);
	}

	Object create(final int expectedSize, final long defaultValue) {
		return new LongLongMap(expectedSize, defaultValue);
	}

	Object copy(final Object map) {
		return new LongLongMap((LongLongMap) map);
	}

	// int に収まらないキーも混ぜる
	long keyComponent(final Random random) {
		final long k = random.nextInt(-20, 20);
		return random.nextBoolean() ? k : k << 33;
	}
}
