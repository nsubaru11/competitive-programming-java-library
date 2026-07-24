package lib.math.linearalgebra;

import static java.util.Arrays.*;

@SuppressWarnings("unused")
public final class IntMatrixUtils {
	private IntMatrixUtils() {
		throw new AssertionError();
	}

	public static int[][] copy(final int[][] a) {
		final int n = a.length, m = a[0].length;
		final int[][] res = new int[n][m];
		for (int i = 0; i < n; i++) System.arraycopy(a[i], 0, res[i], 0, m);
		return res;
	}

	public static int[][] identity(final int n) {
		final int[][] res = new int[n][n];
		for (int i = 0; i < n; i++) res[i][i] = 1;
		return res;
	}

	private static void checkSameSize(final int[][] a, final int[][] b) {
		if (a.length != b.length || a[0].length != b[0].length) throw new ArithmeticException("not defined.");
	}

	private static void checkMulSize(final int[][] a, final int[][] b) {
		if (a[0].length != b.length) throw new ArithmeticException("not defined.");
	}

	private static void checkMulSize(final int[][] a, final int[] b) {
		if (a[0].length != b.length) throw new ArithmeticException("not defined.");
	}

	private static void checkSquare(final int[][] a) {
		if (a.length != a[0].length) throw new ArithmeticException("not defined.");
	}

	public static int[][] add(final int[][] a, final int[][] b) {
		final int[][] res = copy(a);
		addRaw(res, b);
		return res;
	}

	public static void addRaw(final int[][] a, final int[][] b) {
		checkSameSize(a, b);
		for (int i = 0; i < a.length; i++) for (int j = 0; j < a[0].length; j++) a[i][j] += b[i][j];
	}

	public static int[][] sub(final int[][] a, final int[][] b) {
		final int[][] res = copy(a);
		subRaw(res, b);
		return res;
	}

	public static void subRaw(final int[][] a, final int[][] b) {
		checkSameSize(a, b);
		for (int i = 0; i < a.length; i++) for (int j = 0; j < a[0].length; j++) a[i][j] -= b[i][j];
	}

	public static int[][] mul(final int[][] a, final int[][] b) {
		checkMulSize(a, b);
		final int n = a.length, m = a[0].length, l = b[0].length;
		final int[][] res = new int[n][l];
		for (int i = 0; i < n; i++) {
			for (int k = 0; k < m; k++) {
				if (a[i][k] == 0) continue;
				for (int j = 0; j < l; j++) res[i][j] += a[i][k] * b[k][j];
			}
		}
		return res;
	}

	public static void mulRaw(final int[][] a, final int[][] b) {
		checkMulSize(a, b);
		final int m = a[0].length, l = b[0].length;
		if (m != l) throw new ArithmeticException("raw size changes.");
		final int[][] right = a == b ? copy(b) : b;
		final int[] row = new int[l];
		for (int[] ai : a) {
			fill(row, 0);
			for (int k = 0; k < m; k++) {
				if (ai[k] == 0) continue;
				for (int j = 0; j < l; j++) row[j] += ai[k] * right[k][j];
			}
			System.arraycopy(row, 0, ai, 0, l);
		}
	}

	public static int[] mul(final int[][] a, final int[] b) {
		checkMulSize(a, b);
		final int n = a.length, m = a[0].length;
		final int[] res = new int[n];
		for (int i = 0; i < n; i++) {
			int s = 0;
			for (int j = 0; j < m; j++) s += a[i][j] * b[j];
			res[i] = s;
		}
		return res;
	}

	public static void mulRaw(final int[][] a, final int[] b) {
		checkMulSize(a, b);
		final int n = a.length, m = a[0].length;
		if (n != b.length) throw new ArithmeticException("raw size changes.");
		final int[] res = new int[n];
		for (int i = 0; i < n; i++) {
			int s = 0;
			for (int j = 0; j < m; j++) s += a[i][j] * b[j];
			res[i] = s;
		}
		System.arraycopy(res, 0, b, 0, n);
	}

	public static int[][] modAdd(final int[][] a, final int[][] b, final int mod) {
		final int[][] res = copy(a);
		modAddRaw(res, b, mod);
		return res;
	}

	public static void modAddRaw(final int[][] a, final int[][] b, final int mod) {
		checkSameSize(a, b);
		for (int i = 0; i < a.length; i++)
			for (int j = 0; j < a[0].length; j++) a[i][j] = (a[i][j] + b[i][j]) % mod;
	}

	public static int[][] modSub(final int[][] a, final int[][] b, final int mod) {
		final int[][] res = copy(a);
		modSubRaw(res, b, mod);
		return res;
	}

	public static void modSubRaw(final int[][] a, final int[][] b, final int mod) {
		checkSameSize(a, b);
		for (int i = 0; i < a.length; i++)
			for (int j = 0; j < a[0].length; j++) {
				final int v = (a[i][j] - b[i][j]) % mod;
				a[i][j] = v < 0 ? v + mod : v;
			}
	}

	public static int[][] modMul(final int[][] a, final int[][] b, final int mod) {
		checkMulSize(a, b);
		final int n = a.length, m = a[0].length, l = b[0].length;
		final int[][] res = new int[n][l];
		for (int i = 0; i < n; i++) {
			for (int k = 0; k < m; k++) {
				if (a[i][k] == 0) continue;
				for (int j = 0; j < l; j++) res[i][j] = (int) ((res[i][j] + (long) a[i][k] * b[k][j] % mod) % mod);
			}
		}
		for (int i = 0; i < n; i++) for (int j = 0; j < l; j++) if (res[i][j] < 0) res[i][j] += mod;
		return res;
	}

	public static void modMulRaw(final int[][] a, final int[][] b, final int mod) {
		checkMulSize(a, b);
		final int n = a.length, m = a[0].length, l = b[0].length;
		if (m != l) throw new ArithmeticException("raw size changes.");
		final int[][] right = a == b ? copy(b) : b;
		final int[] row = new int[l];
		for (int i = 0; i < n; i++) {
			fill(row, 0);
			for (int k = 0; k < m; k++) {
				if (a[i][k] == 0) continue;
				for (int j = 0; j < l; j++) row[j] = (int) ((row[j] + (long) a[i][k] * right[k][j] % mod) % mod);
			}
			for (int j = 0; j < l; j++) a[i][j] = row[j] < 0 ? row[j] + mod : row[j];
		}
	}

	public static int[] modMul(final int[][] a, final int[] b, final int mod) {
		checkMulSize(a, b);
		final int n = a.length, m = a[0].length;
		final int[] res = new int[n];
		for (int i = 0; i < n; i++) {
			long s = 0;
			for (int j = 0; j < m; j++) s = (s + (long) a[i][j] * b[j] % mod) % mod;
			res[i] = (int) (s % mod);
		}
		return res;
	}

	public static void modMulRaw(final int[][] a, final int[] b, final int mod) {
		checkMulSize(a, b);
		final int n = a.length, m = a[0].length;
		if (n != b.length) throw new ArithmeticException("raw size changes.");
		final int[] res = new int[n];
		for (int i = 0; i < n; i++) {
			long s = 0;
			for (int j = 0; j < m; j++) s = (s + (long) a[i][j] * b[j] % mod) % mod;
			res[i] = (int) (s % mod);
		}
		System.arraycopy(res, 0, b, 0, n);
	}

	public static void affine(final int[][] a, final int x, final int y) {
		affineRaw(a, x, y);
	}

	public static void affineRaw(final int[][] a, final int x, final int y) {
		for (int i = 0; i < a.length; i++) for (int j = 0; j < a[0].length; j++) a[i][j] = x * a[i][j] + y;
	}

	public static void modAffine(final int[][] a, final int x, final int y, final int mod) {
		modAffineRaw(a, x, y, mod);
	}

	public static void modAffineRaw(final int[][] a, final int x, final int y, final int mod) {
		for (int i = 0; i < a.length; i++)
			for (int j = 0; j < a[0].length; j++) {
				final int v = (int) (((long) x * a[i][j] % mod + y % mod) % mod);
				a[i][j] = v < 0 ? v + mod : v;
			}
	}

	public static void add(final int[][] a, final int x) {
		affineRaw(a, 1, x);
	}

	public static void addRaw(final int[][] a, final int x) {
		affineRaw(a, 1, x);
	}

	public static void sub(final int[][] a, final int x) {
		affineRaw(a, 1, -x);
	}

	public static void subRaw(final int[][] a, final int x) {
		affineRaw(a, 1, -x);
	}

	public static void mul(final int[][] a, final int x) {
		affineRaw(a, x, 0);
	}

	public static void mulRaw(final int[][] a, final int x) {
		affineRaw(a, x, 0);
	}

	public static void div(final int[][] a, final int x) {
		divRaw(a, x);
	}

	public static void divRaw(final int[][] a, final int x) {
		for (int i = 0; i < a.length; i++) for (int j = 0; j < a[0].length; j++) a[i][j] /= x;
	}

	public static void modAdd(final int[][] a, final int x, final int mod) {
		modAffineRaw(a, 1, x, mod);
	}

	public static void modAddRaw(final int[][] a, final int x, final int mod) {
		modAffineRaw(a, 1, x, mod);
	}

	public static void modSub(final int[][] a, final int x, final int mod) {
		modAffineRaw(a, 1, -x, mod);
	}

	public static void modSubRaw(final int[][] a, final int x, final int mod) {
		modAffineRaw(a, 1, -x, mod);
	}

	public static void modMul(final int[][] a, final int x, final int mod) {
		modAffineRaw(a, x, 0, mod);
	}

	public static void modMulRaw(final int[][] a, final int x, final int mod) {
		modAffineRaw(a, x, 0, mod);
	}

	public static int[][] pow(final int[][] a, long exp) {
		checkSquare(a);
		if (exp < 0) throw new ArithmeticException("not defined.");
		final int n = a.length;
		int[][] result = identity(n);
		for (int[][] base = copy(a); exp > 0; exp >>= 1) {
			if ((exp & 1) == 1) result = mul(result, base);
			if (exp > 1) base = mul(base, base);
		}
		return result;
	}

	public static void powRaw(final int[][] a, final long exp) {
		final int[][] res = pow(a, exp);
		for (int i = 0; i < a.length; i++) System.arraycopy(res[i], 0, a[i], 0, a.length);
	}

	public static int[][] modPow(final int[][] a, long exp, final int mod) {
		checkSquare(a);
		if (exp < 0) throw new ArithmeticException("not defined.");
		final int n = a.length;
		int[][] result = identity(n);
		for (int[][] base = copy(a); exp > 0; exp >>= 1) {
			if ((exp & 1) == 1) result = modMul(result, base, mod);
			if (exp > 1) base = modMul(base, base, mod);
		}
		return result;
	}

	public static void modPowRaw(final int[][] a, final long exp, final int mod) {
		final int[][] res = modPow(a, exp, mod);
		for (int i = 0; i < a.length; i++) System.arraycopy(res[i], 0, a[i], 0, a.length);
	}
}
