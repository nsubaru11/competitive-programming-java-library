package unit.sort;

import java.util.*;
import java.util.function.*;

import lib.sort.*;

final class ShakerSortTest extends SortContract {

	Map<String, Consumer<int[]>> sorters() {
		return Map.ofEntries(
				Map.entry("shakerSort", (Consumer<int[]>) ShakerSort::shakerSort)
		);
	}
}
