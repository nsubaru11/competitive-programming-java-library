package unit.ds.set;

import java.util.*;

import lib.ds.set.*;

import unit.support.*;

final class LongAVLMultisetTest extends NumericMultisetContract {

	Target create() {
		return Adapter.of(Target.class, new LongAVLMultiset());
	}

	Numeric createNumeric() {
		return Adapter.of(Numeric.class, new LongAVLMultiset());
	}

	Collection<?> collection(final long... values) {
		return Arrays.stream(values).boxed().toList();
	}
}
