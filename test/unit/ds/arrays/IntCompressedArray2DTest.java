package unit.ds.arrays;

import lib.ds.arrays.*;

import unit.support.*;

final class IntCompressedArray2DTest extends CompressedArray2DContract {

	Target create(final long[][] a) {
		return Adapter.of(Target.class, new IntCompressedArray2D((int[][]) Adapter.convert(a, int[][].class)));
	}

	Target create(final long[][] a, final RankType type) {
		return Adapter.of(Target.class, new IntCompressedArray2D((int[][]) Adapter.convert(a, int[][].class), type));
	}

	Target create(final long[][] a, final RankType type, final boolean oneBased) {
		return Adapter.of(Target.class, new IntCompressedArray2D((int[][]) Adapter.convert(a, int[][].class), type, oneBased));
	}
}
