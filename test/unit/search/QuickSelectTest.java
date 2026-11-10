package unit.search;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.search.*;

import static org.junit.jupiter.api.Assertions.*;

final class QuickSelectTest {

	@Test
	void kthSmallestAndInputUnchanged() {
		final int[] a = {5, 1, 4, 1, 3};
		assertEquals(1, QuickSelect.select(a, 0));
		assertEquals(1, QuickSelect.select(a, 1));
		assertEquals(3, QuickSelect.select(a, 2));
		assertEquals(5, QuickSelect.select(a, 4));
		assertArrayEquals(new int[]{5, 1, 4, 1, 3}, a, "入力配列は変更しない");
	}

	@RepeatedTest(100)
	void matchesSort(final RepetitionInfo info) {
		final Random random = new Random(info.getCurrentRepetition());
		final int[] a = random.ints(1 + random.nextInt(30), -10, 10).toArray();
		final int[] sorted = a.clone();
		Arrays.sort(sorted);
		for (int k = 0; k < a.length; k++) assertEquals(sorted[k], QuickSelect.select(a, k), "a=" + Arrays.toString(a) + ", k=" + k);
	}
}
