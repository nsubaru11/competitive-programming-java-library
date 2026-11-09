package unit.util;

import org.junit.jupiter.api.*;

import lib.util.*;

import static org.junit.jupiter.api.Assertions.*;

final class ArrayUtilsTest {

	@Test
	void reverse() {
		final int[] ints = {5, 6, 7, 8};
		ArrayUtils.reverse(ints);
		assertArrayEquals(new int[]{8, 7, 6, 5}, ints);

		final char[] chars = {'1', '2', '3'};
		ArrayUtils.reverse(chars);
		assertArrayEquals(new char[]{'3', '2', '1'}, chars);

		final long[] longs = {1, 2, 3, 4, 5};
		ArrayUtils.reverse(longs);
		assertArrayEquals(new long[]{5, 4, 3, 2, 1}, longs);
	}
}
