package unit.ds.segmenttree;

import lib.ds.arrays.*;
import lib.ds.segmenttree.*;

import unit.support.*;

final class LongSegmentTreeTest extends NumericSegmentTreeContract {

	Target create(final int n, final Op op) {
		return Adapter.of(Target.class, new LongSegmentTree(n, op == Op.SUM ? Long::sum : Math::min, op == Op.SUM ? 0 : Long.MAX_VALUE));
	}

	Target create(final long[] a, final Op op) {
		return Adapter.of(Target.class, new LongSegmentTree(a, op == Op.SUM ? Long::sum : Math::min, op == Op.SUM ? 0 : Long.MAX_VALUE));
	}

	Target createFromCollection(final long[] a, final Op op) {
		return Adapter.of(Target.class, new LongSegmentTree(new LongArrayDeque(a), op == Op.SUM ? Long::sum : Math::min, op == Op.SUM ? 0 : Long.MAX_VALUE));
	}

	long inf() {
		return Long.MAX_VALUE;
	}

	Numeric createNumeric(final long[] a) {
		return Adapter.of(Numeric.class, new LongSegmentTree(a, Long::sum, 0));
	}
}
