package unit.ds.arrays;

import lib.ds.arrays.*;

import unit.support.*;

final class LongCompressedArray2DTest extends CompressedArray2DContract {

	Target create(final long[][] a) {
		return Adapter.of(Target.class, new LongCompressedArray2D(a));
	}

	Target create(final long[][] a, final RankType type) {
		return Adapter.of(Target.class, new LongCompressedArray2D(a, type));
	}

	Target create(final long[][] a, final RankType type, final boolean oneBased) {
		return Adapter.of(Target.class, new LongCompressedArray2D(a, type, oneBased));
	}
}
