package unit.sort;

import java.util.*;
import java.util.function.*;

import org.junit.jupiter.api.*;

import lib.sort.*;

import static org.junit.jupiter.api.Assertions.*;

final class MergeSortTest extends SortContract {

	Map<String, Consumer<int[]>> sorters() {
		return Map.ofEntries(
				Map.entry("mergeSort", (Consumer<int[]>) a -> assertSame(a, MergeSort.mergeSort(a, a.length)))
		);
	}

	@Test
	void sortsOnlyFirstNElements() {
		final int[] a = {3, 1, 2, 0};
		MergeSort.mergeSort(a, 3);
		assertArrayEquals(new int[]{1, 2, 3, 0}, a);
	}
}
