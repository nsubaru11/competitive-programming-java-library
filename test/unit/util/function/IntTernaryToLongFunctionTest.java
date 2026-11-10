package unit.util.function;

import org.junit.jupiter.api.*;

import lib.util.function.*;

import static org.junit.jupiter.api.Assertions.*;

final class IntTernaryToLongFunctionTest {

	@Test
	void andThenAppliesAfterToResult() {
		final IntTernaryToLongFunction product = (a, b, c) -> (long) a * (long) b * (long) c;
		assertEquals(24L, product.applyAsLong(2, 3, 4));
		assertEquals(24L + 10, product.andThen(x -> x + 10).applyAsLong(2, 3, 4));
	}

	@Test
	void andThenRejectsNull() {
		final IntTernaryToLongFunction f = (a, b, c) -> 0;
		assertThrows(NullPointerException.class, () -> f.andThen(null));
	}
}
