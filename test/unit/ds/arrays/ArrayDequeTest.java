package unit.ds.arrays;

import org.junit.jupiter.api.*;

import lib.ds.arrays.*;

import static org.junit.jupiter.api.Assertions.*;

final class ArrayDequeTest {

	@Test
	void intCopyIsIndependent() {
		final IntArrayDeque deque = new IntArrayDeque(4);
		deque.addLast(1);
		deque.addLast(2);
		deque.addFirst(0);
		final IntArrayDeque copy = new IntArrayDeque(deque);
		copy.addLast(3);
		copy.set(0, 9);
		assertEquals("0 1 2", deque.toString());
		assertEquals("9 1 2 3", copy.toString());
	}

	@Test
	void longCopyIsIndependent() {
		final LongArrayDeque deque = new LongArrayDeque(2);
		deque.addLast(5);
		deque.addFirst(4);
		final LongArrayDeque copy = new LongArrayDeque(deque);
		assertEquals(4, copy.removeFirst());
		assertEquals(2, deque.size());
		assertEquals("5", copy.toString());
	}
}
