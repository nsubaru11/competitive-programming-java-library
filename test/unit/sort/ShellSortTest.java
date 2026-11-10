package unit.sort;

import java.util.*;
import java.util.function.*;

import lib.sort.*;

final class ShellSortTest extends SortContract {

	Map<String, Consumer<int[]>> sorters() {
		return Map.ofEntries(
				Map.entry("shellSort", (Consumer<int[]>) ShellSort::shellSort)
		);
	}
}
