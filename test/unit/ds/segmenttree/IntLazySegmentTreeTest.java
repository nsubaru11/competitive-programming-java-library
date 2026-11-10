package unit.ds.segmenttree;

import java.util.function.*;

import lib.ds.arrays.*;
import lib.ds.segmenttree.*;

import unit.support.*;

final class IntLazySegmentTreeTest extends LazySegmentTreeContract {

	// 単位元（最大値）には加算しない
	private static final IntBinaryOperator MAPPING = (f, x) -> x == Integer.MAX_VALUE ? x : f + x;
	private static final IntBinaryOperator COMPOSITION = Integer::sum;

	Target create(final int n, final Act act) {
		return Adapter.of(Target.class, act == Act.ADD ? new IntLazySegmentTree(n, Math::min, Integer.MAX_VALUE, MAPPING, COMPOSITION) : new IntLazySegmentTree(n, Math::min, Integer.MAX_VALUE));
	}

	Target create(final long[] a, final Act act) {
		final int[] b = (int[]) Adapter.convert(a, int[].class);
		return Adapter.of(Target.class, act == Act.ADD ? new IntLazySegmentTree(b, Math::min, Integer.MAX_VALUE, MAPPING, COMPOSITION) : new IntLazySegmentTree(b, Math::min, Integer.MAX_VALUE));
	}

	Target createFromCollection(final long[] a, final Act act) {
		final IntArrayDeque c = new IntArrayDeque((int[]) Adapter.convert(a, int[].class));
		return Adapter.of(Target.class, act == Act.ADD ? new IntLazySegmentTree(c, Math::min, Integer.MAX_VALUE, MAPPING, COMPOSITION) : new IntLazySegmentTree(c, Math::min, Integer.MAX_VALUE));
	}

	long inf() {
		return Integer.MAX_VALUE;
	}
}
