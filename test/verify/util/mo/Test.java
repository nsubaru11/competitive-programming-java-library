package verify.util.mo;

import java.util.*;

import lib.util.*;

public final class Test {

	private static void check(final boolean condition) {
		if (!condition) throw new AssertionError();
	}

	private static void testFourDirections() {
		final int[] a = {3, 1, 2, 1};
		final int[][] lr = {{0, 1, 0, 2}, {4, 3, 1, 3}};
		final int[] count = new int[4], size = {0};
		final long[] inversions = {0}, answer = new long[4];
		MoAlgorithm.run(a.length, lr, i -> {
			for (int v = 0; v < a[i]; v++) inversions[0] += count[v];
			count[a[i]]++;
			size[0]++;
		}, i -> {
			for (int v = a[i] + 1; v < count.length; v++) inversions[0] += count[v];
			count[a[i]]++;
			size[0]++;
		}, i -> {
			count[a[i]]--;
			size[0]--;
			for (int v = 0; v < a[i]; v++) inversions[0] -= count[v];
		}, i -> {
			count[a[i]]--;
			size[0]--;
			for (int v = a[i] + 1; v < count.length; v++) inversions[0] -= count[v];
		}, i -> answer[i] = inversions[0]);
		check(Arrays.equals(answer, new long[]{4, 0, 0, 0}));
		check(size[0] == 1);
	}

	public static void main(final String[] args) {
		testFourDirections();
	}
}
