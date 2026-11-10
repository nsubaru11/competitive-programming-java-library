package unit.util.function;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.util.function.*;

import static org.junit.jupiter.api.Assertions.*;

final class IntQuaternaryConsumerTest {

	@Test
	void andThenRunsInOrderWithSameArguments() {
		final List<String> log = new ArrayList<>();
		final IntQuaternaryConsumer first = (a, b, c, d) -> log.add("first:" + a + "," + b + "," + c + "," + d);
		final IntQuaternaryConsumer second = (a, b, c, d) -> log.add("second:" + a + "," + b + "," + c + "," + d);
		first.andThen(second).accept(1, 2, 3, 4);
		assertEquals(List.of("first:" + "1,2,3,4", "second:" + "1,2,3,4"), log);
	}

	@Test
	void andThenRejectsNull() {
		final IntQuaternaryConsumer consumer = (a, b, c, d) -> {
		};
		assertThrows(NullPointerException.class, () -> consumer.andThen(null));
	}
}
