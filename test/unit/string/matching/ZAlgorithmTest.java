package unit.string.matching;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.string.matching.*;

import unit.string.*;

import static org.junit.jupiter.api.Assertions.*;

final class ZAlgorithmTest {

	@Test
	void example() {
		assertArrayEquals(new int[]{7, 0, 1, 0, 3, 0, 1}, ZAlgorithm.getZArray("abacaba"));
		assertArrayEquals(new int[]{3, 2, 1}, ZAlgorithm.getZArray("aaa".toCharArray()));
	}

	@RepeatedTest(200)
	void matchesNaive(final RepetitionInfo info) {
		final String s = "a" + Naive.randomString(new Random(info.getCurrentRepetition()), 29, 3);
		final int[] expected = new int[s.length()];
		for (int i = 0; i < s.length(); i++) {
			while (i + expected[i] < s.length() && s.charAt(expected[i]) == s.charAt(i + expected[i])) expected[i]++;
		}
		assertArrayEquals(expected, ZAlgorithm.getZArray(s), s);
		assertArrayEquals(expected, ZAlgorithm.getZArray(s.toCharArray()), s);
	}
}
