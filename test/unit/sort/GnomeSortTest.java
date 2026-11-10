package unit.sort;

import java.util.*;
import java.util.function.*;

import lib.sort.*;

final class GnomeSortTest extends SortContract {

	Map<String, Consumer<int[]>> sorters() {
		return Map.ofEntries(
			Map.entry("basicGnomeSort", (Consumer<int[]>) GnomeSort::basicGnomeSort),
			Map.entry("gnomeSort", (Consumer<int[]>) GnomeSort::gnomeSort)
		);
	}
}
