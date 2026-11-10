package unit.ds.set;

import java.util.*;

import lib.ds.set.*;

import unit.support.*;

final class IntAVLSetTest extends SortedSetContract {

	Target create() {
		return Adapter.of(Target.class, new IntAVLSet());
	}

	Collection<?> collection(final long... values) {
		return Arrays.stream(values).mapToObj(Math::toIntExact).toList();
	}
}
