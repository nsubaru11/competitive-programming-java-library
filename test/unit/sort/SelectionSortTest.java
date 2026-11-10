package unit.sort;

import java.util.*;
import java.util.function.*;

import lib.sort.*;

final class SelectionSortTest extends SortContract {

	Map<String, Consumer<int[]>> sorters() {
		return Map.ofEntries(
				Map.entry("selectionSort", (Consumer<int[]>) SelectionSort::selectionSort),
				Map.entry("doubleSelectionSort", (Consumer<int[]>) SelectionSort::doubleSelectionSort)
		);
	}
}
