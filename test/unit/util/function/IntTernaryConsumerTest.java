package unit.util.function;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.util.function.*;

import static org.junit.jupiter.api.Assertions.*;

final class IntTernaryConsumerTest {

	@Test
	void andThenRunsInOrderWithSameArguments() {
		final List<String> log = new ArrayList<>();
		final IntTernaryConsumer first = (a, b, c) -> log.add("first:" + a + "," + b + "," + c);
		final IntTernaryConsumer second = (a, b, c) -> log.add("second:" + a + "," + b + "," + c);
		first.andThen(second).accept(1, 2, 3);
		assertEquals(List.of("first:" + "1,2,3", "second:" + "1,2,3"), log);
	}

	@Test
	void andThenRejectsNull() {
		final IntTernaryConsumer consumer = (a, b, c) -> {
		};
		assertThrows(NullPointerException.class, () -> consumer.andThen(null));
	}
}
