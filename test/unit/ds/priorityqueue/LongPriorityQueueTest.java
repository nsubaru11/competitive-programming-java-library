package unit.ds.priorityqueue;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.ds.arrays.*;
import lib.ds.priorityqueue.*;
import lib.util.function.*;

import unit.support.*;

import static org.junit.jupiter.api.Assertions.*;

final class LongPriorityQueueTest extends PriorityQueueContract {

	private static final LongComparator COMPARATOR = (x, y) -> CUSTOM.compare((long) x, (long) y);

	Target create() {
		return Adapter.of(Target.class, new LongPriorityQueue());
	}

	Target create(final int initialCapacity) {
		return Adapter.of(Target.class, new LongPriorityQueue(initialCapacity));
	}

	Target create(final boolean descending) {
		return Adapter.of(Target.class, new LongPriorityQueue(descending));
	}

	Target create(final int initialCapacity, final boolean descending) {
		return Adapter.of(Target.class, new LongPriorityQueue(initialCapacity, descending));
	}

	Target createWithComparator(final int initialCapacity) {
		return Adapter.of(Target.class, initialCapacity == 1 ? new LongPriorityQueue(1, COMPARATOR) : new LongPriorityQueue(COMPARATOR));
	}

	Target create(final long[] a, final boolean descending) {
		return Adapter.of(Target.class, descending ? new LongPriorityQueue(a, true) : new LongPriorityQueue(a));
	}

	Target createWithComparator(final long[] a) {
		return Adapter.of(Target.class, new LongPriorityQueue(a, COMPARATOR));
	}

	Target createFromCollection(final long[] a, final boolean descending) {
		final LongArrayDeque c = new LongArrayDeque(a);
		return Adapter.of(Target.class, descending ? new LongPriorityQueue(c, true) : new LongPriorityQueue(c));
	}

	@Test
	void collectionWithComparatorAndDefaultComparator() {
		final LongPriorityQueue q = new LongPriorityQueue(new LongArrayDeque(new long[]{13L, 21L, 5L}), COMPARATOR);
		assertEquals(21, q.poll());
		assertEquals(13, q.poll());
		assertEquals(5, q.poll());
		final LongPriorityQueue r = new LongPriorityQueue(COMPARATOR);
		r.add(13);
		r.add(21);
		assertEquals(21, r.peek());
	}

	@Test
	void addAllFromIterables() {
		final LongPriorityQueue q = new LongPriorityQueue();
		assertFalse(q.addAll(List.of()));
		assertTrue(q.addAll(List.of(3L, 1L)), "Collection");
		assertTrue(q.addAll(new LongArrayDeque(new long[]{2L})), "LongCollection");
		assertFalse(q.addAll(new LongArrayDeque()));
		final Iterable<Long> plain = () -> List.of(0L).iterator();
		assertTrue(q.addAll(plain), "Collection でない Iterable");
		final Iterable<Long> empty = Collections::emptyIterator;
		assertFalse(q.addAll(empty));
		assertEquals(4, q.size());
		assertEquals(0, q.poll());
		assertEquals(1, q.poll());
	}

	@Test
	void comparatorMustNotBeNull() {
		assertThrows(NullPointerException.class, () -> new LongPriorityQueue((LongComparator) null));
	}
}
