package unit.sort;

import java.util.*;
import java.util.function.*;

import org.junit.jupiter.api.*;

import lib.sort.*;

import static org.junit.jupiter.api.Assertions.*;

final class QuickSortTest extends SortContract {

	Map<String, Consumer<int[]>> sorters() {
		return Map.ofEntries(
			Map.entry("quickSort", (Consumer<int[]>) a -> QuickSort.quickSort(a, 0, a.length - 1))
		);
	}

	@Test
	void sortsOnlyClosedRange() {
		final int[] a = {9, 3, 1, 2, 0};
		QuickSort.quickSort(a, 1, 3);
		assertArrayEquals(new int[]{9, 1, 2, 3, 0}, a);
	}
}
