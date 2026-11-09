package unit.util;

import java.util.*;

import org.junit.jupiter.api.*;

import lib.util.*;

import static org.junit.jupiter.api.Assertions.*;

final class MoAlgorithmTest {

	// 左右の追加・削除を分けると、区間の転倒数を数えられる
	@Test
	void inversionsWithFourCallbacks() {
		final int[] a = {3, 1, 2, 1};
		final int[][] lr = {{0, 1, 0, 2}, {4, 3, 1, 3}};
		assertArrayEquals(new long[]{4, 0, 0, 0}, inversions(a, lr));
	}

	@Test
	void distinctCountWithBlockSize() {
		final int[] a = {1, 2, 1, 3};
		final int[][] lr = {{0, 1}, {3, 4}};
		assertArrayEquals(new int[]{2, 3}, distinct(a, lr, 2));
	}

	// 愚直解との照合: 各区間を直接数えた値と比べる
	@RepeatedTest(50)
	void matchesNaive(final RepetitionInfo info) {
		final long seed = info.getCurrentRepetition();
		final Random random = new Random(seed);
		final int n = 1 + random.nextInt(40);
		final int q = 1 + random.nextInt(40);
		final int[] a = new int[n];
		for (int i = 0; i < n; i++) a[i] = random.nextInt(5);
		final int[][] lr = new int[2][q];
		for (int i = 0; i < q; i++) {
			final int l = random.nextInt(n + 1);
			final int r = random.nextInt(n + 1);
			lr[0][i] = Math.min(l, r);
			lr[1][i] = Math.max(l, r);
		}
		final long[] actualInversions = inversions(a, lr);
		final int[] actualDistinct = distinct(a, lr, 1 + random.nextInt(n));
		for (int i = 0; i < q; i++) {
			final int l = lr[0][i], r = lr[1][i];
			final String at = "seed=" + seed + ", l=" + l + ", r=" + r;
			long inv = 0;
			for (int x = l; x < r; x++) for (int y = x + 1; y < r; y++) if (a[x] > a[y]) inv++;
			assertEquals(inv, actualInversions[i], at);
			assertEquals((int) Arrays.stream(a, l, r).distinct().count(), actualDistinct[i], at);
		}
	}

	private static long[] inversions(final int[] a, final int[][] lr) {
		final int max = Arrays.stream(a).max().orElse(0) + 1;
		final int[] count = new int[max];
		final long[] inversions = {0};
		final long[] answer = new long[lr[0].length];
		MoAlgorithm.run(a.length, lr, i -> {
			for (int v = 0; v < a[i]; v++) inversions[0] += count[v];
			count[a[i]]++;
		}, i -> {
			for (int v = a[i] + 1; v < max; v++) inversions[0] += count[v];
			count[a[i]]++;
		}, i -> {
			count[a[i]]--;
			for (int v = 0; v < a[i]; v++) inversions[0] -= count[v];
		}, i -> {
			count[a[i]]--;
			for (int v = a[i] + 1; v < max; v++) inversions[0] -= count[v];
		}, i -> answer[i] = inversions[0]);
		return answer;
	}

	private static int[] distinct(final int[] a, final int[][] lr, final int blockSize) {
		final int[] freq = new int[Arrays.stream(a).max().orElse(0) + 1];
		final int[] distinct = {0};
		final int[] answer = new int[lr[0].length];
		MoAlgorithm.run(a.length, lr, blockSize, i -> {
			if (++freq[a[i]] == 1) distinct[0]++;
		}, i -> {
			if (--freq[a[i]] == 0) distinct[0]--;
		}, i -> answer[i] = distinct[0]);
		return answer;
	}
}
