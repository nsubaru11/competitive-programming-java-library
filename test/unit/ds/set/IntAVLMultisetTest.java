package unit.ds.set;

import java.util.*;

import lib.ds.set.*;

import unit.support.*;

final class IntAVLMultisetTest extends NumericMultisetContract {

	Target create() {
		return Adapter.of(Target.class, new IntAVLMultiset());
	}

	Numeric createNumeric() {
		return Adapter.of(Numeric.class, new IntAVLMultiset());
	}

	Collection<?> collection(final long... values) {
		return Arrays.stream(values).mapToObj(Math::toIntExact).toList();
	}
}
