package unit.ds.priorityqueue;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.ds.priorityqueue.PriorityQueue;

import unit.support.*;

import static org.junit.jupiter.api.Assertions.*;

final class PriorityQueueTest extends PriorityQueueContract {

	private static Long[] box(final long[] a) {
		return Arrays.stream(a).boxed().toArray(Long[]::new);
	}

	Target create() {
		return Adapter.of(Target.class, new PriorityQueue<Long>());
	}

	Target create(final int initialCapacity) {
		return Adapter.of(Target.class, new PriorityQueue<Long>(initialCapacity));
	}

	Target create(final boolean descending) {
		return Adapter.of(Target.class, new PriorityQueue<Long>(descending));
	}

	Target create(final int initialCapacity, final boolean descending) {
		return Adapter.of(Target.class, new PriorityQueue<Long>(initialCapacity, descending));
	}

	Target createWithComparator(final int initialCapacity) {
		return Adapter.of(Target.class, initialCapacity == 1 ? new PriorityQueue<>(1, CUSTOM) : new PriorityQueue<>(CUSTOM));
	}

	Target create(final long[] a, final boolean descending) {
		final Long[] boxed = box(a);
		return Adapter.of(Target.class, descending ? new PriorityQueue<>(boxed, true) : new PriorityQueue<>(boxed));
	}

	Target createWithComparator(final long[] a) {
		return Adapter.of(Target.class, new PriorityQueue<>(box(a), CUSTOM));
	}

	Target createFromCollection(final long[] a, final boolean descending) {
		final List<Long> c = Arrays.asList(box(a));
		return Adapter.of(Target.class, descending ? new PriorityQueue<>(c, true) : new PriorityQueue<>(c));
	}

	@Test
	void collectionWithComparator() {
		final PriorityQueue<Long> q = new PriorityQueue<>(List.of(13L, 21L, 5L), CUSTOM);
		assertEquals(21L, q.poll());
		assertEquals(13L, q.poll());
	}

	@Test
	void nonComparableValuesWithComparator() {
		final PriorityQueue<int[]> q = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
		q.add(new int[]{3});
		q.add(new int[]{1});
		assertEquals(1, q.poll()[0]);
	}

	@Test
	void addAllFromIterables() {
		final PriorityQueue<Long> q = new PriorityQueue<>();
		assertFalse(q.addAll(List.of()));
		assertTrue(q.addAll(List.of(3L, 1L)));
		final Iterable<Long> plain = () -> List.of(0L).iterator();
		assertTrue(q.addAll(plain));
		final Iterable<Long> empty = Collections::emptyIterator;
		assertFalse(q.addAll(empty));
		assertEquals(0L, q.poll());
	}

	@Test
	void comparatorMustNotBeNull() {
		assertThrows(NullPointerException.class, () -> new PriorityQueue<Long>((Comparator<Long>) null));
	}
}
