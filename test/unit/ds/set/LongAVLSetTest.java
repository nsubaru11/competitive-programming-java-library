package unit.ds.set;

import java.util.*;

import lib.ds.set.*;

import unit.support.*;

final class LongAVLSetTest extends SortedSetContract {

	Target create() {
		return Adapter.of(Target.class, new LongAVLSet());
	}

	Collection<?> collection(final long... values) {
		return Arrays.stream(values).boxed().toList();
	}
}
