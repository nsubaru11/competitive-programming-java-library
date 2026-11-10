package unit.util;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * クエリは lr[0][i] = l, lr[1][i] = r の半開区間 [l, r)。4方向版は左右で別の追加・削除処理を、2方向版は共通の処理を受け取る。
 */
final class MoAlgorithmTest {

	@Nested
	class Search {

		// 左右の追加・削除を分けると、区間の転倒数を数えられる
		@Test
		void inversionsWithFourCallbacks() {
			final int[] a = {3, 1, 2, 1};
			final int[][] lr = {{0, 1, 0, 2}, {4, 3, 1, 3}};
			assertArrayEquals(new long[]{4, 0, 0, 0}, inversions(a, lr, -1));
		}

		@Test
		void distinctCountWithTwoCallbacks() {
			final int[] a = {1, 2, 1, 3};
			final int[][] lr = {{0, 1, 2}, {1, 4, 2}};
			assertArrayEquals(new int[]{1, 3, 0}, distinct(a, lr, -1), "空区間は何も追加しない");
			assertArrayEquals(new int[]{1, 3, 0}, distinct(a, lr, 2));
		}

		@Test
		void noQueries() {
			assertArrayEquals(new int[0], distinct(new int[]{1, 2}, new int[][]{{}, {}}, -1));
		}
	}

	@RepeatedTest(50)
	void matchesNaive(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final int n = 1 + random.nextInt(40), q = 1 + random.nextInt(40);
		final int[] a = random.ints(n, 0, 5).toArray();
		final int[][] lr = new int[2][q];
		for (int i = 0; i < q; i++) {
			final int l = random.nextInt(n + 1), r = random.nextInt(n + 1);
			lr[0][i] = Math.min(l, r);
			lr[1][i] = Math.max(l, r);
		}
		final int block = 1 + random.nextInt(n);
		final long[] inv = inversions(a, lr, -1), invBlock = inversions(a, lr, block);
		final int[] dist = distinct(a, lr, -1), distBlock = distinct(a, lr, block);
		for (int i = 0; i < q; i++) {
			final int l = lr[0][i], r = lr[1][i];
			final String at = "seed=" + seed + ", l=" + l + ", r=" + r + ", block=" + block;
			long expected = 0;
			for (int x = l; x < r; x++) for (int y = x + 1; y < r; y++) if (a[x] > a[y]) expected++;
			assertEquals(expected, inv[i], at);
			assertEquals(expected, invBlock[i], at);
			final int d = (int) Arrays.stream(a, l, r).distinct().count();
			assertEquals(d, dist[i], at);
			assertEquals(d, distBlock[i], at);
		}
	}

	/**
	 * 4方向版で各区間の転倒数を求める。blockSize が負なら幅を指定しない版を使う。
	 */
	private static long[] inversions(final int[] a, final int[][] lr, final int blockSize) {
		final int max = Arrays.stream(a).max().orElse(0) + 1;
		final int[] count = new int[max];
		final long[] inversions = {0};
		final long[] answer = new long[lr[0].length];
		final java.util.function.IntConsumer addLeft = i -> {
			for (int v = 0; v < a[i]; v++) inversions[0] += count[v];
			count[a[i]]++;
		}, addRight = i -> {
			for (int v = a[i] + 1; v < max; v++) inversions[0] += count[v];
			count[a[i]]++;
		}, removeLeft = i -> {
			count[a[i]]--;
			for (int v = 0; v < a[i]; v++) inversions[0] -= count[v];
		}, removeRight = i -> {
			count[a[i]]--;
			for (int v = a[i] + 1; v < max; v++) inversions[0] -= count[v];
		}, query = i -> answer[i] = inversions[0];
		if (blockSize < 0) MoAlgorithm.run(a.length, lr, addLeft, addRight, removeLeft, removeRight, query);
		else MoAlgorithm.run(a.length, lr, blockSize, addLeft, addRight, removeLeft, removeRight, query);
		return answer;
	}

	/**
	 * 2方向版で各区間の異なる値の数を求める。blockSize が負なら幅を指定しない版を使う。
	 */
	private static int[] distinct(final int[] a, final int[][] lr, final int blockSize) {
		final int[] freq = new int[Arrays.stream(a).max().orElse(0) + 1];
		final int[] distinct = {0};
		final int[] answer = new int[lr[0].length];
		final java.util.function.IntConsumer add = i -> {
			if (++freq[a[i]] == 1) distinct[0]++;
		}, remove = i -> {
			if (--freq[a[i]] == 0) distinct[0]--;
		}, query = i -> answer[i] = distinct[0];
		if (blockSize < 0) MoAlgorithm.run(a.length, lr, add, remove, query);
		else MoAlgorithm.run(a.length, lr, blockSize, add, remove, query);
		return answer;
	}
}
