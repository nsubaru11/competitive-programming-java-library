package unit.sort;

import java.util.*;
import java.util.function.*;

import lib.sort.*;

final class CombSortTest extends SortContract {

	Map<String, Consumer<int[]>> sorters() {
		return Map.ofEntries(
			Map.entry("standardCombSort", (Consumer<int[]>) CombSort::standardCombSort),
			Map.entry("optimizedCombSort", (Consumer<int[]>) CombSort::optimizedCombSort)
		);
	}
}
