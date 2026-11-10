package unit.util.function;

import org.junit.jupiter.api.*;

import lib.util.function.*;

import static org.junit.jupiter.api.Assertions.*;

final class LongComparatorTest {

	@Test
	void naturalAndReverseOrder() {
		assertTrue(LongComparator.naturalOrder().compare(1L, 2L) < 0);
		assertTrue(LongComparator.naturalOrder().compare(2L, 1L) > 0);
		assertEquals(0, LongComparator.naturalOrder().compare(3L, 3L));
		assertTrue(LongComparator.reverseOrder().compare(1L, 2L) > 0);
		assertEquals(0, LongComparator.reverseOrder().compare(3L, 3L));
	}

	@Test
	void reversedSwapsArguments() {
		final LongComparator byLastDigit = (x, y) -> Long.compare(x % 10, y % 10);
		assertTrue(byLastDigit.compare(21L, 19L) < 0);
		assertTrue(byLastDigit.reversed().compare(21L, 19L) > 0);
		assertTrue(LongComparator.naturalOrder().reversed().compare(1L, 2L) > 0);
	}
}
