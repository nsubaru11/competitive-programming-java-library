package unit.sort;

import java.util.*;
import java.util.function.*;

import lib.sort.*;

final class HeapSortTest extends SortContract {

	Map<String, Consumer<int[]>> sorters() {
		return Map.ofEntries(
				Map.entry("heapSort", (Consumer<int[]>) HeapSort::heapSort)
		);
	}
}
