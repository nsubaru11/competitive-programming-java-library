package unit.ds.segmenttree;

import lib.ds.arrays.*;
import lib.ds.segmenttree.*;

import unit.support.*;

final class IntSegmentTreeTest extends NumericSegmentTreeContract {

	Target create(final int n, final Op op) {
		return Adapter.of(Target.class, new IntSegmentTree(n, op == Op.SUM ? Integer::sum : Math::min, op == Op.SUM ? 0 : Integer.MAX_VALUE));
	}

	Target create(final long[] a, final Op op) {
		return Adapter.of(Target.class, new IntSegmentTree((int[]) Adapter.convert(a, int[].class), op == Op.SUM ? Integer::sum : Math::min, op == Op.SUM ? 0 : Integer.MAX_VALUE));
	}

	Target createFromCollection(final long[] a, final Op op) {
		return Adapter.of(Target.class, new IntSegmentTree(new IntArrayDeque((int[]) Adapter.convert(a, int[].class)), op == Op.SUM ? Integer::sum : Math::min, op == Op.SUM ? 0 : Integer.MAX_VALUE));
	}

	long inf() {
		return Integer.MAX_VALUE;
	}

	Numeric createNumeric(final long[] a) {
		return Adapter.of(Numeric.class, new IntSegmentTree((int[]) Adapter.convert(a, int[].class), Integer::sum, 0));
	}
}
