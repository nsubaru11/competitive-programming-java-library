package unit.ds.arrays;

import lib.ds.arrays.*;

import unit.support.*;

final class LongCompressedArrayTest extends CompressedArrayContract {

	Target create(final long[] a) {
		return Adapter.of(Target.class, new LongCompressedArray(a));
	}

	Target create(final long[] a, final RankType type) {
		return Adapter.of(Target.class, new LongCompressedArray(a, type));
	}

	Target create(final long[] a, final RankType type, final boolean oneBased) {
		return Adapter.of(Target.class, new LongCompressedArray(a, type, oneBased));
	}

	Target createFromCollection(final long[] a, final RankType type, final boolean oneBased) {
		return Adapter.of(Target.class, new LongCompressedArray(new LongArrayDeque(a), type, oneBased));
	}
}
