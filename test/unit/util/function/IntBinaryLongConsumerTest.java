package unit.util.function;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.util.function.*;

import static org.junit.jupiter.api.Assertions.*;

final class IntBinaryLongConsumerTest {

	@Test
	void andThenRunsInOrderWithSameArguments() {
		final List<String> log = new ArrayList<>();
		final IntBinaryLongConsumer first = (a, b, c) -> log.add("first:" + a + "," + b + "," + c);
		final IntBinaryLongConsumer second = (a, b, c) -> log.add("second:" + a + "," + b + "," + c);
		first.andThen(second).accept(1, 2, 3);
		assertEquals(List.of("first:" + "1,2,3", "second:" + "1,2,3"), log);
	}

	@Test
	void andThenRejectsNull() {
		final IntBinaryLongConsumer consumer = (a, b, c) -> {
		};
		assertThrows(NullPointerException.class, () -> consumer.andThen(null));
	}
}
