package unit.util.function;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.util.function.*;

import static org.junit.jupiter.api.Assertions.*;

final class IntBinaryConsumerTest {

	@Test
	void andThenRunsInOrderWithSameArguments() {
		final List<String> log = new ArrayList<>();
		final IntBinaryConsumer first = (a, b) -> log.add("first:" + a + "," + b);
		final IntBinaryConsumer second = (a, b) -> log.add("second:" + a + "," + b);
		first.andThen(second).accept(1, 2);
		assertEquals(List.of("first:" + "1,2", "second:" + "1,2"), log);
	}

	@Test
	void andThenRejectsNull() {
		final IntBinaryConsumer consumer = (a, b) -> {
		};
		assertThrows(NullPointerException.class, () -> consumer.andThen(null));
	}
}
