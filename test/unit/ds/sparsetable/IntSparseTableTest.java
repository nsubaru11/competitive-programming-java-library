package unit.ds.sparsetable;

import lib.ds.sparsetable.*;

import unit.support.*;

final class IntSparseTableTest extends SparseTableContract {

	Target create(final long[] a, final boolean max) {
		return Adapter.of(Target.class, new IntSparseTable((int[]) Adapter.convert(a, int[].class), max ? Math::max : Math::min));
	}
}
