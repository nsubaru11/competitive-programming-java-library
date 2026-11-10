package unit.ds.priorityqueue;

import org.junit.jupiter.api.*;

import lib.ds.arrays.*;
import lib.ds.priorityqueue.*;
import lib.util.function.*;

import unit.support.*;

import static org.junit.jupiter.api.Assertions.*;

final class IntIndexedPriorityQueueTest extends IndexedPriorityQueueContract {

	private static final IntComparator COMPARATOR = (x, y) -> CUSTOM.compare((long) x, (long) y);

	Target create(final int n) {
		return Adapter.of(Target.class, new IntIndexedPriorityQueue(n));
	}

	Target create(final int n, final boolean descending) {
		return Adapter.of(Target.class, new IntIndexedPriorityQueue(n, descending));
	}

	Target createWithComparator(final int n) {
		return Adapter.of(Target.class, new IntIndexedPriorityQueue(n, COMPARATOR));
	}

	Target create(final long[] a, final boolean descending) {
		return Adapter.of(Target.class, descending ? new IntIndexedPriorityQueue((int[]) Adapter.convert(a, int[].class), true) : new IntIndexedPriorityQueue((int[]) Adapter.convert(a, int[].class)));
	}

	Target createWithComparator(final long[] a) {
		return Adapter.of(Target.class, new IntIndexedPriorityQueue((int[]) Adapter.convert(a, int[].class), COMPARATOR));
	}

	Target createFromCollection(final long[] a, final boolean descending) {
		final IntArrayDeque c = new IntArrayDeque((int[]) Adapter.convert(a, int[].class));
		return Adapter.of(Target.class, descending ? new IntIndexedPriorityQueue(c, true) : new IntIndexedPriorityQueue(c));
	}

	@Test
	void collectionWithComparator() {
		final IntIndexedPriorityQueue q = new IntIndexedPriorityQueue(new IntArrayDeque(new int[]{13, 21, 5}), COMPARATOR);
		assertEquals(1, q.pollIndex());
		assertEquals(0, q.pollIndex());
	}

	@Test
	void comparatorMustNotBeNull() {
		assertThrows(NullPointerException.class, () -> new IntIndexedPriorityQueue(1, (IntComparator) null));
	}
}
