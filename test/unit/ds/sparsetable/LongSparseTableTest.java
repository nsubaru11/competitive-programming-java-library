package unit.ds.sparsetable;

import lib.ds.sparsetable.*;

import unit.support.*;

final class LongSparseTableTest extends SparseTableContract {

	Target create(final long[] a, final boolean max) {
		return Adapter.of(Target.class, new LongSparseTable(a.clone(), max ? Math::max : Math::min));
	}
}
