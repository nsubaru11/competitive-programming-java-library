package unit.ds.priorityqueue;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.ds.arrays.*;
import lib.ds.priorityqueue.*;
import lib.util.function.*;

import unit.support.*;

import static org.junit.jupiter.api.Assertions.*;

final class IntPriorityQueueTest extends PriorityQueueContract {

	private static final IntComparator COMPARATOR = (x, y) -> CUSTOM.compare((long) x, (long) y);

	Target create() {
		return Adapter.of(Target.class, new IntPriorityQueue());
	}

	Target create(final int initialCapacity) {
		return Adapter.of(Target.class, new IntPriorityQueue(initialCapacity));
	}

	Target create(final boolean descending) {
		return Adapter.of(Target.class, new IntPriorityQueue(descending));
	}

	Target create(final int initialCapacity, final boolean descending) {
		return Adapter.of(Target.class, new IntPriorityQueue(initialCapacity, descending));
	}

	Target createWithComparator(final int initialCapacity) {
		return Adapter.of(Target.class, initialCapacity == 1 ? new IntPriorityQueue(1, COMPARATOR) : new IntPriorityQueue(COMPARATOR));
	}

	Target create(final long[] a, final boolean descending) {
		return Adapter.of(Target.class, descending ? new IntPriorityQueue((int[]) Adapter.convert(a, int[].class), true) : new IntPriorityQueue((int[]) Adapter.convert(a, int[].class)));
	}

	Target createWithComparator(final long[] a) {
		return Adapter.of(Target.class, new IntPriorityQueue((int[]) Adapter.convert(a, int[].class), COMPARATOR));
	}

	Target createFromCollection(final long[] a, final boolean descending) {
		final IntArrayDeque c = new IntArrayDeque((int[]) Adapter.convert(a, int[].class));
		return Adapter.of(Target.class, descending ? new IntPriorityQueue(c, true) : new IntPriorityQueue(c));
	}

	@Test
	void collectionWithComparatorAndDefaultComparator() {
		final IntPriorityQueue q = new IntPriorityQueue(new IntArrayDeque(new int[]{13, 21, 5}), COMPARATOR);
		assertEquals(21, q.poll());
		assertEquals(13, q.poll());
		assertEquals(5, q.poll());
		final IntPriorityQueue r = new IntPriorityQueue(COMPARATOR);
		r.add(13);
		r.add(21);
		assertEquals(21, r.peek());
	}

	@Test
	void addAllFromIterables() {
		final IntPriorityQueue q = new IntPriorityQueue();
		assertFalse(q.addAll(List.of()));
		assertTrue(q.addAll(List.of(3, 1)), "Collection");
		assertTrue(q.addAll(new IntArrayDeque(new int[]{2})), "IntCollection");
		assertFalse(q.addAll(new IntArrayDeque()));
		final Iterable<Integer> plain = () -> List.of(0).iterator();
		assertTrue(q.addAll(plain), "Collection でない Iterable");
		final Iterable<Integer> empty = Collections::emptyIterator;
		assertFalse(q.addAll(empty));
		assertEquals(4, q.size());
		assertEquals(0, q.poll());
		assertEquals(1, q.poll());
	}

	@Test
	void comparatorMustNotBeNull() {
		assertThrows(NullPointerException.class, () -> new IntPriorityQueue((IntComparator) null));
	}
}
