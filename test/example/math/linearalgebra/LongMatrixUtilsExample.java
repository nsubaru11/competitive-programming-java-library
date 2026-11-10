package example.math.linearalgebra;

import java.util.*;

import lib.math.linearalgebra.*;

public final class LongMatrixUtilsExample {

	public static void main(final String[] args) {
		// 行列
		long[][] a = {{1, 2}, {3, 4}};
		long[][] b = {{0, 1}, {1, 0}};

		// 和・差・積
		System.out.println(Arrays.deepToString(LongMatrixUtils.add(a, b)));
		System.out.println(Arrays.deepToString(LongMatrixUtils.sub(a, b)));
		System.out.println(Arrays.deepToString(LongMatrixUtils.mul(a, b)));
		System.out.println();

		// ベクトルとの積
		System.out.println(Arrays.toString(LongMatrixUtils.mul(a, new long[]{1, 1})));
		System.out.println();

		// 単位行列・累乗
		System.out.println(Arrays.deepToString(LongMatrixUtils.identity(3)));
		long[][] fib = {{1, 1}, {1, 0}};
		System.out.println(Arrays.deepToString(LongMatrixUtils.pow(fib, 10)));
		System.out.println(Arrays.deepToString(LongMatrixUtils.modPow(fib, 100, 1_000_000_007)));
		System.out.println();

		// スカラー倍（破壊的）
		long[][] c = LongMatrixUtils.copy(a);
		LongMatrixUtils.mul(c, 3);
		System.out.println(Arrays.deepToString(c));
		System.out.println(Arrays.deepToString(a));
	}
}
