package unit.string.approximate;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.string.approximate.*;

import unit.string.Naive;

import static org.junit.jupiter.api.Assertions.*;

final class LevenshteinDPTest {

	@Test
	void examples() {
		assertEquals(3, LevenshteinDP.computeEditDistance("kitten", "sitting"));
		assertEquals(3, LevenshteinDP.computeEditDistance("kitten".toCharArray(), "sitting".toCharArray()));
		assertEquals(3, LevenshteinDP.computeEditDistance(new int[]{1, 2, 3}, new int[]{2, 3, 4, 5}));
		assertEquals(3, LevenshteinDP.computeEditDistance("kitten", "sitting", 3));
		assertEquals(-1, LevenshteinDP.computeEditDistance("kitten", "sitting", 2), "k を超えれば -1");
		assertEquals(0, LevenshteinDP.computeEditDistance("", ""));
		assertEquals(4, LevenshteinDP.computeEditDistance("", "abcd"));
	}

	@RepeatedTest(200)
	void matchesNaive(final RepetitionInfo info) {
		final Random random = new Random(info.getCurrentRepetition());
		final String s = Naive.randomString(random, 10, 3), t = Naive.randomString(random, 10, 3);
		final int d = Naive.editDistance(s, t), k = random.nextInt(0, 12);
		final String at = "s=" + s + ", t=" + t + ", k=" + k;
		assertEquals(d, LevenshteinDP.computeEditDistance(s, t), at);
		assertEquals(d, LevenshteinDP.computeEditDistance(s.toCharArray(), t.toCharArray()), at);
		final int[] si = s.chars().toArray(), ti = t.chars().toArray();
		assertEquals(d, LevenshteinDP.computeEditDistance(si, ti), at);
		final int banded = d <= k ? d : -1;
		assertEquals(banded, LevenshteinDP.computeEditDistance(s, t, k), at);
		assertEquals(banded, LevenshteinDP.computeEditDistance(s.toCharArray(), t.toCharArray(), k), at);
		assertEquals(banded, LevenshteinDP.computeEditDistance(si, ti, k), at);
	}
}
