package unit.ds.segmenttree;

import java.util.*;
import java.util.function.*;

import org.junit.jupiter.api.*;

import lib.ds.segmenttree.*;

import unit.support.*;

import static org.junit.jupiter.api.Assertions.*;

final class SegmentTreeTest extends SegmentTreeContract {

	private static BinaryOperator<Long> operator(final Op op) {
		return op == Op.SUM ? Long::sum : Math::min;
	}

	private static Long identity(final Op op) {
		return op == Op.SUM ? 0L : Long.MAX_VALUE;
	}

	Target create(final int n, final Op op) {
		return Adapter.of(Target.class, new SegmentTree<>(n, operator(op), identity(op)));
	}

	Target create(final long[] a, final Op op) {
		return Adapter.of(Target.class, new SegmentTree<>(Arrays.stream(a).boxed().toArray(Long[]::new), operator(op), identity(op)));
	}

	Target createFromCollection(final long[] a, final Op op) {
		return Adapter.of(Target.class, new SegmentTree<>(Arrays.stream(a).boxed().toList(), operator(op), identity(op)));
	}

	long inf() {
		return Long.MAX_VALUE;
	}

	@Test
	void stateOfGenericVersion() {
		final SegmentTree<Long> t = new SegmentTree<>(List.of(1L, 2L), Long::sum, 0L);
		assertEquals(2, t.size);
		assertFalse(t.isEmpty());
		assertTrue(new SegmentTree<>(0, Long::sum, 0L).isEmpty());
	}

	// 結合則だけを満たす演算（文字列の連結）でも左から順に集計する
	@Test
	void nonCommutativeOperator() {
		final SegmentTree<String> t = new SegmentTree<>(new String[]{"a", "b", "c", "d", "e"}, String::concat, "");
		assertEquals("bcd", t.query(1, 4));
		assertEquals("abcde", t.queryAll());
		t.set(2, "X");
		assertEquals("abXd", t.query(0, 4));
		assertEquals(4, t.maxRight(1, s -> s.length() <= 3));
		assertEquals(2, t.minLeft(5, s -> s.length() <= 3));
	}
}
