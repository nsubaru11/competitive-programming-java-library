package unit.ds.segmenttree;

import java.util.*;
import java.util.function.*;

import org.junit.jupiter.api.*;

import lib.ds.segmenttree.*;

import unit.support.*;

import static org.junit.jupiter.api.Assertions.*;

final class LazySegmentTreeTest extends LazySegmentTreeContract {

	// 単位元（最大値）には加算しない
	private static final BiFunction<Long, Long, Long> MAPPING = (f, x) -> x == Long.MAX_VALUE ? x : f + x;
	private static final BinaryOperator<Long> COMPOSITION = Long::sum;

	Target create(final int n, final Act act) {
		return Adapter.of(Target.class, act == Act.ADD ? new LazySegmentTree<>(n, Math::min, Long.MAX_VALUE, MAPPING, COMPOSITION) : new LazySegmentTree<>(n, Math::min, Long.MAX_VALUE));
	}

	Target create(final long[] a, final Act act) {
		final Long[] b = Arrays.stream(a).boxed().toArray(Long[]::new);
		return Adapter.of(Target.class, act == Act.ADD ? new LazySegmentTree<>(b, Math::min, Long.MAX_VALUE, MAPPING, COMPOSITION) : new LazySegmentTree<>(b, Math::min, Long.MAX_VALUE));
	}

	Target createFromCollection(final long[] a, final Act act) {
		final List<Long> c = Arrays.stream(a).boxed().toList();
		return Adapter.of(Target.class, act == Act.ADD ? new LazySegmentTree<>(c, Math::min, Long.MAX_VALUE, MAPPING, COMPOSITION) : new LazySegmentTree<>(c, Math::min, Long.MAX_VALUE));
	}

	long inf() {
		return Long.MAX_VALUE;
	}

	@Test
	void isEmpty() {
		assertTrue(new LazySegmentTree<Long>(0, Math::min, Long.MAX_VALUE).isEmpty());
		assertFalse(new LazySegmentTree<Long>(1, Math::min, Long.MAX_VALUE).isEmpty());
	}

	// 区間長を値に持たせれば、区間加算と区間和も扱える
	@Test
	void rangeAddRangeSumWithLength() {
		final long[][] init = {{1, 1}, {2, 1}, {3, 1}, {4, 1}};
		final LazySegmentTree<long[]> t = new LazySegmentTree<>(init, (x, y) -> new long[]{x[0] + y[0], x[1] + y[1]}, new long[]{0, 0},
				(f, x) -> new long[]{x[0] + f[0] * x[1], x[1]}, (f, g) -> new long[]{f[0] + g[0], 0});
		t.apply(1, 3, new long[]{10, 0});
		assertEquals(26L, t.query(0, 3)[0]);
		assertEquals(30L, t.queryAll()[0]);
	}
}
