package unit.string.suffix;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.string.suffix.*;

import unit.string.*;

import static org.junit.jupiter.api.Assertions.*;

final class SuffixArrayTest {

	@Test
	void example() {
		final SuffixArray sa = new SuffixArray("banana");
		assertTrue(sa.contains("nan"));
		assertTrue(sa.contains("banana"));
		assertTrue(sa.contains("a"));
		assertTrue(sa.contains(""));
		assertFalse(sa.contains("nab"));
		assertFalse(sa.contains("bananas"));
	}

	@RepeatedTest(100)
	void matchesStringContains(final RepetitionInfo info) {
		final Random random = new Random(info.getCurrentRepetition());
		final String s = Naive.randomString(random, 30, 3);
		final SuffixArray sa = new SuffixArray(s);
		for (int q = 0; q < 50; q++) {
			final String p = Naive.randomString(random, 5, 3);
			assertEquals(s.contains(p), sa.contains(p), "s=" + s + ", p=" + p);
		}
	}
}
