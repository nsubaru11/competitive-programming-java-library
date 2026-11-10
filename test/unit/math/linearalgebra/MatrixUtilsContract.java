package unit.math.linearalgebra;

import java.util.*;

import org.junit.jupiter.api.*;

import unit.support.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * IntMatrixUtils・LongMatrixUtils に共通する振る舞いのテスト。行列は long[][] で受け渡し、static メソッドを
 * {@link Adapter#callStatic} で呼び出す。Raw 付きのメソッドは第1引数を書き換え、付かないものは新しい行列を返す。
 */
abstract class MatrixUtilsContract {

	static final long MOD = 998244353;

	abstract Class<?> utils();

	/**
	 * 対象の要素型の行列（int[][]・long[][]）へ変換する。
	 */
	abstract Object matrix(long[][] a);

	abstract Object vector(long[] v);

	long[][] call(final String name, final Object... args) {
		return Adapter.callStatic(long[][].class, utils(), name, args);
	}

	long[] callVector(final String name, final Object... args) {
		return Adapter.callStatic(long[].class, utils(), name, args);
	}

	long[][] back(final Object m) {
		return (long[][]) Adapter.convert(m, long[][].class);
	}

	static final long[][] A = {{1, 2}, {3, 4}}, B = {{5, 6}, {7, 8}};

	@Nested
	class Construction {

		@Test
		void copyAndIdentity() {
			final Object a = matrix(A);
			final long[][] c = call("copy", a);
			assertArrayEquals(A, c);
			assertNotSame(a, Adapter.callStatic(Object.class, utils(), "copy", a));
			assertArrayEquals(new long[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}}, call("identity", 3));
		}
	}

	@Nested
	class Arithmetic {

		@Test
		void matrixOperations() {
			assertArrayEquals(new long[][]{{6, 8}, {10, 12}}, call("add", matrix(A), matrix(B)));
			assertArrayEquals(new long[][]{{-4, -4}, {-4, -4}}, call("sub", matrix(A), matrix(B)));
			assertArrayEquals(new long[][]{{19, 22}, {43, 50}}, call("mul", matrix(A), matrix(B)));
			assertArrayEquals(new long[]{17, 39}, callVector("mul", matrix(A), vector(new long[]{5, 6})));
			assertArrayEquals(new long[][]{{7, 10}, {15, 22}}, call("pow", matrix(A), 2L));
			assertArrayEquals(new long[][]{{1, 0}, {0, 1}}, call("pow", matrix(A), 0L));
			assertArrayEquals(new long[][]{{5, 8, 11}}, call("mul", matrix(new long[][]{{1, 2}}), matrix(new long[][]{{1, 2, 3}, {2, 3, 4}})), "非正方行列");
		}

		@Test
		void rawVariantsUpdateFirstArgument() {
			Object a = matrix(A);
			Adapter.callStatic(Void.class, utils(), "addRaw", a, matrix(B));
			assertArrayEquals(new long[][]{{6, 8}, {10, 12}}, back(a));
			a = matrix(A);
			Adapter.callStatic(Void.class, utils(), "subRaw", a, matrix(B));
			assertArrayEquals(new long[][]{{-4, -4}, {-4, -4}}, back(a));
			a = matrix(A);
			Adapter.callStatic(Void.class, utils(), "mulRaw", a, matrix(B));
			assertArrayEquals(new long[][]{{19, 22}, {43, 50}}, back(a));
			a = matrix(A);
			Adapter.callStatic(Void.class, utils(), "mulRaw", a, a);
			assertArrayEquals(new long[][]{{7, 10}, {15, 22}}, back(a), "自分自身との積");
			final Object v = vector(new long[]{5, 6});
			Adapter.callStatic(Void.class, utils(), "mulRaw", matrix(A), v);
			assertArrayEquals(new long[]{17, 39}, (long[]) Adapter.convert(v, long[].class));
			a = matrix(A);
			Adapter.callStatic(Void.class, utils(), "powRaw", a, 3L);
			assertArrayEquals(new long[][]{{37, 54}, {81, 118}}, back(a));
		}

		@Test
		void scalarOperationsUpdateInPlace() {
			final String[] names = {"affine", "affineRaw", "add", "addRaw", "sub", "subRaw", "mul", "mulRaw", "div", "divRaw"};
			final long[][][] expected = {
				{{3, 5}, {7, 9}}, {{3, 5}, {7, 9}}, {{3, 4}, {5, 6}}, {{3, 4}, {5, 6}}, {{-1, 0}, {1, 2}}, {{-1, 0}, {1, 2}},
				{{2, 4}, {6, 8}}, {{2, 4}, {6, 8}}, {{0, 1}, {1, 2}}, {{0, 1}, {1, 2}},
			};
			for (int k = 0; k < names.length; k++) {
				final Object a = matrix(A);
				if (names[k].startsWith("affine")) Adapter.callStatic(Void.class, utils(), names[k], a, 2L, 1L);
				else Adapter.callStatic(Void.class, utils(), names[k], a, 2L);
				assertArrayEquals(expected[k], back(a), names[k]);
			}
		}

		@Test
		void sizeMismatchThrows() {
			assertThrows(ArithmeticException.class, () -> call("add", matrix(A), matrix(new long[][]{{1, 2, 3}})));
			assertThrows(ArithmeticException.class, () -> call("mul", matrix(new long[][]{{1, 2, 3}}), matrix(A)));
			assertThrows(ArithmeticException.class, () -> call("pow", matrix(new long[][]{{1, 2}}), 2L));
			assertThrows(ArithmeticException.class, () -> call("pow", matrix(A), -1L));
		}
	}

	@Nested
	class ModArithmetic {

		@RepeatedTest(30)
		void matchNaive(final RepetitionInfo info) {
			final Random random = new Random(info.getCurrentRepetition());
			final int n = 1 + random.nextInt(4);
			final long[][] a = randomMatrix(random, n), b = randomMatrix(random, n);
			final long[] v = random.longs(n, 0, MOD).toArray();
			final String at = "seed=" + info.getCurrentRepetition();
			assertArrayEquals(naiveMul(a, b), call("modMul", matrix(a), matrix(b), MOD), at);
			assertArrayEquals(elementwise(a, b, 1), call("modAdd", matrix(a), matrix(b), MOD), at);
			assertArrayEquals(elementwise(a, b, -1), call("modSub", matrix(a), matrix(b), MOD), at);
			final long[] mv = new long[n];
			for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) mv[i] = (mv[i] + a[i][j] * v[j] % MOD) % MOD;
			assertArrayEquals(mv, callVector("modMul", matrix(a), vector(v), MOD), at);
			final int e = random.nextInt(20);
			long[][] p = new long[n][n];
			for (int i = 0; i < n; i++) p[i][i] = 1;
			for (int k = 0; k < e; k++) p = naiveMul(p, a);
			assertArrayEquals(p, call("modPow", matrix(a), (long) e, MOD), at + ", e=" + e);
			final Object raw = matrix(a);
			Adapter.callStatic(Void.class, utils(), "modPowRaw", raw, (long) e, MOD);
			assertArrayEquals(p, back(raw), at);
			final Object raw2 = matrix(a);
			Adapter.callStatic(Void.class, utils(), "modMulRaw", raw2, matrix(b), MOD);
			assertArrayEquals(naiveMul(a, b), back(raw2), at);
			final Object raw3 = matrix(a);
			Adapter.callStatic(Void.class, utils(), "modAffine", raw3, 3L, -5L, MOD);
			final long[][] aff = new long[n][n];
			for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) aff[i][j] = Math.floorMod(3 * a[i][j] - 5, MOD);
			assertArrayEquals(aff, back(raw3), at + ", 結果を [0, mod) に正規化する");
		}
	}

	static long[][] randomMatrix(final Random random, final int n) {
		final long[][] m = new long[n][];
		for (int i = 0; i < n; i++) m[i] = random.longs(n, 0, MOD).toArray();
		return m;
	}

	static long[][] naiveMul(final long[][] a, final long[][] b) {
		final int n = a.length, m = b[0].length;
		final long[][] res = new long[n][m];
		for (int i = 0; i < n; i++) for (int k = 0; k < b.length; k++) for (int j = 0; j < m; j++) res[i][j] = (res[i][j] + a[i][k] * b[k][j]) % MOD;
		return res;
	}

	static long[][] elementwise(final long[][] a, final long[][] b, final int sign) {
		final long[][] res = new long[a.length][a[0].length];
		for (int i = 0; i < a.length; i++) for (int j = 0; j < a[0].length; j++) res[i][j] = Math.floorMod(a[i][j] + sign * b[i][j], MOD);
		return res;
	}
}
