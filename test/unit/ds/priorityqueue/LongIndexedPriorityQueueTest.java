package unit.ds.priorityqueue;

import org.junit.jupiter.api.*;

import lib.ds.arrays.*;
import lib.ds.priorityqueue.*;
import lib.util.function.*;

import unit.support.*;

import static org.junit.jupiter.api.Assertions.*;

final class LongIndexedPriorityQueueTest extends IndexedPriorityQueueContract {

	private static final LongComparator COMPARATOR = (x, y) -> CUSTOM.compare((long) x, (long) y);

	Target create(final int n) {
		return Adapter.of(Target.class, new LongIndexedPriorityQueue(n));
	}

	Target create(final int n, final boolean descending) {
		return Adapter.of(Target.class, new LongIndexedPriorityQueue(n, descending));
	}

	Target createWithComparator(final int n) {
		return Adapter.of(Target.class, new LongIndexedPriorityQueue(n, COMPARATOR));
	}

	Target create(final long[] a, final boolean descending) {
		return Adapter.of(Target.class, descending ? new LongIndexedPriorityQueue(a, true) : new LongIndexedPriorityQueue(a));
	}

	Target createWithComparator(final long[] a) {
		return Adapter.of(Target.class, new LongIndexedPriorityQueue(a, COMPARATOR));
	}

	Target createFromCollection(final long[] a, final boolean descending) {
		final LongArrayDeque c = new LongArrayDeque(a);
		return Adapter.of(Target.class, descending ? new LongIndexedPriorityQueue(c, true) : new LongIndexedPriorityQueue(c));
	}

	@Test
	void collectionWithComparator() {
		final LongIndexedPriorityQueue q = new LongIndexedPriorityQueue(new LongArrayDeque(new long[]{13, 21, 5}), COMPARATOR);
		assertEquals(1, q.pollIndex());
		assertEquals(0, q.pollIndex());
	}

	@Test
	void comparatorMustNotBeNull() {
		assertThrows(NullPointerException.class, () -> new LongIndexedPriorityQueue(1, (LongComparator) null));
	}
}
