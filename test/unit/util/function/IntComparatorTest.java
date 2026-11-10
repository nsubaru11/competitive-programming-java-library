package unit.util.function;

import org.junit.jupiter.api.*;

import lib.util.function.*;

import static org.junit.jupiter.api.Assertions.*;

final class IntComparatorTest {

	@Test
	void naturalAndReverseOrder() {
		assertTrue(IntComparator.naturalOrder().compare(1, 2) < 0);
		assertTrue(IntComparator.naturalOrder().compare(2, 1) > 0);
		assertEquals(0, IntComparator.naturalOrder().compare(3, 3));
		assertTrue(IntComparator.reverseOrder().compare(1, 2) > 0);
		assertEquals(0, IntComparator.reverseOrder().compare(3, 3));
	}

	@Test
	void reversedSwapsArguments() {
		final IntComparator byLastDigit = (x, y) -> Integer.compare(x % 10, y % 10);
		assertTrue(byLastDigit.compare(21, 19) < 0);
		assertTrue(byLastDigit.reversed().compare(21, 19) > 0);
		assertTrue(IntComparator.naturalOrder().reversed().compare(1, 2) > 0);
	}
}
