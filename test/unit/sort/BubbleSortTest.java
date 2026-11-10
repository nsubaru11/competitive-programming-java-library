package unit.sort;

import java.util.*;
import java.util.function.*;

import lib.sort.*;

final class BubbleSortTest extends SortContract {

	Map<String, Consumer<int[]>> sorters() {
		return Map.ofEntries(
			Map.entry("basicBubbleSort", (Consumer<int[]>) BubbleSort::basicBubbleSort),
			Map.entry("bubbleSort", (Consumer<int[]>) BubbleSort::bubbleSort)
		);
	}
}
