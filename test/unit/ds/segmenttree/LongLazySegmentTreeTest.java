package unit.ds.segmenttree;

import java.util.function.*;

import lib.ds.arrays.*;
import lib.ds.segmenttree.*;

import unit.support.*;

final class LongLazySegmentTreeTest extends LazySegmentTreeContract {

	// 単位元（最大値）には加算しない
	private static final LongBinaryOperator MAPPING = (f, x) -> x == Long.MAX_VALUE ? x : f + x;
	private static final LongBinaryOperator COMPOSITION = Long::sum;

	Target create(final int n, final Act act) {
		return Adapter.of(Target.class, act == Act.ADD ? new LongLazySegmentTree(n, Math::min, Long.MAX_VALUE, MAPPING, COMPOSITION) : new LongLazySegmentTree(n, Math::min, Long.MAX_VALUE));
	}

	Target create(final long[] a, final Act act) {
		final long[] b = a;
		return Adapter.of(Target.class, act == Act.ADD ? new LongLazySegmentTree(b, Math::min, Long.MAX_VALUE, MAPPING, COMPOSITION) : new LongLazySegmentTree(b, Math::min, Long.MAX_VALUE));
	}

	Target createFromCollection(final long[] a, final Act act) {
		final LongArrayDeque c = new LongArrayDeque(a);
		return Adapter.of(Target.class, act == Act.ADD ? new LongLazySegmentTree(c, Math::min, Long.MAX_VALUE, MAPPING, COMPOSITION) : new LongLazySegmentTree(c, Math::min, Long.MAX_VALUE));
	}

	long inf() {
		return Long.MAX_VALUE;
	}
}
