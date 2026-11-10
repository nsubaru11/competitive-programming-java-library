package unit.ds.priorityqueue;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.ds.priorityqueue.*;

import unit.support.*;

import static org.junit.jupiter.api.Assertions.*;

final class IndexedPriorityQueueTest extends IndexedPriorityQueueContract {

	private static Long[] box(final long[] a) {
		return Arrays.stream(a).boxed().toArray(Long[]::new);
	}

	Target create(final int n) {
		return Adapter.of(Target.class, new IndexedPriorityQueue<Long>(n));
	}

	Target create(final int n, final boolean descending) {
		return Adapter.of(Target.class, new IndexedPriorityQueue<Long>(n, descending));
	}

	Target createWithComparator(final int n) {
		return Adapter.of(Target.class, new IndexedPriorityQueue<>(n, CUSTOM));
	}

	Target create(final long[] a, final boolean descending) {
		final Long[] boxed = box(a);
		return Adapter.of(Target.class, descending ? new IndexedPriorityQueue<>(boxed, true) : new IndexedPriorityQueue<>(boxed));
	}

	Target createWithComparator(final long[] a) {
		return Adapter.of(Target.class, new IndexedPriorityQueue<>(box(a), CUSTOM));
	}

	Target createFromCollection(final long[] a, final boolean descending) {
		final List<Long> c = Arrays.asList(box(a));
		return Adapter.of(Target.class, descending ? new IndexedPriorityQueue<>(c, true) : new IndexedPriorityQueue<>(c));
	}

	@Test
	void collectionWithComparator() {
		final IndexedPriorityQueue<Long> q = new IndexedPriorityQueue<>(List.of(13L, 21L, 5L), CUSTOM);
		assertEquals(1, q.pollIndex());
		assertEquals(0, q.pollIndex());
	}

	@Test
	void comparatorMustNotBeNull() {
		assertThrows(NullPointerException.class, () -> new IndexedPriorityQueue<Long>(1, (Comparator<Long>) null));
	}
}
