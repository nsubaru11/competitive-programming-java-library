package unit.sort;

import java.util.*;
import java.util.function.*;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * int 配列を昇順に並べ替えるソートに共通する振る舞いのテスト。クラスが複数の版を持つ場合は、すべての版を検査する。
 */
abstract class SortContract {

	/**
	 * 版の名前と、配列をその場で昇順に並べ替える処理。
	 */
	abstract Map<String, Consumer<int[]>> sorters();

	@Test
	void edgeCases() {
		for (final Map.Entry<String, Consumer<int[]>> s : sorters().entrySet()) {
			for (final int[] a : List.of(new int[0], new int[]{5}, new int[]{2, 1}, new int[]{3, 3, 3}, new int[]{1, 2, 3, 4}, new int[]{4, 3, 2, 1})) {
				assertSorts(s, a);
			}
		}
	}

	@RepeatedTest(100)
	void matchesArraysSort(final RepetitionInfo info) {
		final Random random = new Random(info.getCurrentRepetition());
		final int n = random.nextInt(info.getCurrentRepetition() % 10 == 0 ? 500 : 40);
		final int[] a = random.ints(n, -20, random.nextBoolean() ? 20 : 1000).toArray();
		for (final Map.Entry<String, Consumer<int[]>> s : sorters().entrySet()) assertSorts(s, a);
	}

	private static void assertSorts(final Map.Entry<String, Consumer<int[]>> sorter, final int[] a) {
		final int[] expected = a.clone(), actual = a.clone();
		Arrays.sort(expected);
		sorter.getValue().accept(actual);
		assertArrayEquals(expected, actual, sorter.getKey() + ": " + Arrays.toString(a));
	}
}
