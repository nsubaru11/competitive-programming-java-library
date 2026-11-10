package unit.ds.arrays;

import lib.ds.arrays.*;

import unit.support.*;

final class IntCompressedArrayTest extends CompressedArrayContract {

	Target create(final long[] a) {
		return Adapter.of(Target.class, new IntCompressedArray(ints(a)));
	}

	Target create(final long[] a, final RankType type) {
		return Adapter.of(Target.class, new IntCompressedArray(ints(a), type));
	}

	Target create(final long[] a, final RankType type, final boolean oneBased) {
		return Adapter.of(Target.class, new IntCompressedArray(ints(a), type, oneBased));
	}

	Target createFromCollection(final long[] a, final RankType type, final boolean oneBased) {
		return Adapter.of(Target.class, new IntCompressedArray(new IntArrayDeque(ints(a)), type, oneBased));
	}

	private static int[] ints(final long[] a) {
		return (int[]) Adapter.convert(a, int[].class);
	}
}
