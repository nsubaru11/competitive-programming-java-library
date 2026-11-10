package unit.util.function;

import org.junit.jupiter.api.*;

import lib.util.function.*;

import static org.junit.jupiter.api.Assertions.*;

final class IntBinaryToLongFunctionTest {

	@Test
	void andThenAppliesAfterToResult() {
		final IntBinaryToLongFunction product = (a, b) -> (long) a * (long) b;
		assertEquals(6L, product.applyAsLong(2, 3));
		assertEquals(6L + 10, product.andThen(x -> x + 10).applyAsLong(2, 3));
	}

	@Test
	void andThenRejectsNull() {
		final IntBinaryToLongFunction f = (a, b) -> 0;
		assertThrows(NullPointerException.class, () -> f.andThen(null));
	}
}
