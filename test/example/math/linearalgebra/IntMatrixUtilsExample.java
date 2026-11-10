package example.math.linearalgebra;

import java.util.*;

import lib.math.linearalgebra.*;

public final class IntMatrixUtilsExample {

	public static void main(final String[] args) {
		// 行列
		int[][] a = {{1, 2}, {3, 4}};
		int[][] b = {{0, 1}, {1, 0}};

		// 和・差・積
		System.out.println(Arrays.deepToString(IntMatrixUtils.add(a, b)));
		System.out.println(Arrays.deepToString(IntMatrixUtils.sub(a, b)));
		System.out.println(Arrays.deepToString(IntMatrixUtils.mul(a, b)));
		System.out.println();

		// ベクトルとの積
		System.out.println(Arrays.toString(IntMatrixUtils.mul(a, new int[]{1, 1})));
		System.out.println();

		// 単位行列・累乗
		System.out.println(Arrays.deepToString(IntMatrixUtils.identity(3)));
		int[][] fib = {{1, 1}, {1, 0}};
		System.out.println(Arrays.deepToString(IntMatrixUtils.pow(fib, 10)));
		System.out.println(Arrays.deepToString(IntMatrixUtils.modPow(fib, 100, 1_000_000_007)));
		System.out.println();

		// スカラー倍（破壊的）
		int[][] c = IntMatrixUtils.copy(a);
		IntMatrixUtils.mul(c, 3);
		System.out.println(Arrays.deepToString(c));
		System.out.println(Arrays.deepToString(a));
	}
}
