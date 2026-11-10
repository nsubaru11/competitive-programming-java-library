package unit.sort;

import java.util.*;
import java.util.function.*;

import lib.sort.*;

final class OddEvenSortTest extends SortContract {

	Map<String, Consumer<int[]>> sorters() {
		return Map.ofEntries(
			Map.entry("oddEvenSort", (Consumer<int[]>) OddEvenSort::oddEvenSort)
		);
	}
}
