package unit.sort;

import java.util.*;
import java.util.function.*;

import lib.sort.*;

final class InsertionSortTest extends SortContract {

	Map<String, Consumer<int[]>> sorters() {
		return Map.ofEntries(
				Map.entry("insertionSort", (Consumer<int[]>) InsertionSort::insertionSort)
		);
	}
}
