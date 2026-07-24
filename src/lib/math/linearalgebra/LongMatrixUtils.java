package lib.math.linearalgebra;

import static java.util.Arrays.*;

@SuppressWarnings("unused")
public final class LongMatrixUtils {
	private LongMatrixUtils() {
		throw new AssertionError();
	}

	public static long[][] copy(final long[][] a) {
		final int n = a.length, m = a[0].length;
		final long[][] res = new long[n][m];
		for (int i = 0; i < n; i++) System.arraycopy(a[i], 0, res[i], 0, m);
		return res;
	}

	public static long[][] identity(final int n) {
		final long[][] res = new long[n][n];
		for (int i = 0; i < n; i++) res[i][i] = 1;
		return res;
	}

	private static void checkSameSize(final long[][] a, final long[][] b) {
		if (a.length != b.length || a[0].length != b[0].length) throw new ArithmeticException("not defined.");
	}

	private static void checkMulSize(final long[][] a, final long[][] b) {
		if (a[0].length != b.length) throw new ArithmeticException("not defined.");
	}

	private static void checkMulSize(final long[][] a, final long[] b) {
		if (a[0].length != b.length) throw new ArithmeticException("not defined.");
	}

	private static void checkSquare(final long[][] a) {
		if (a.length != a[0].length) throw new ArithmeticException("not defined.");
	}

	public static long[][] add(final long[][] a, final long[][] b) {
		final long[][] res = copy(a);
		addRaw(res, b);
		return res;
	}

	public static void addRaw(final long[][] a, final long[][] b) {
		checkSameSize(a, b);
		for (int i = 0; i < a.length; i++) for (int j = 0; j < a[0].length; j++) a[i][j] += b[i][j];
	}

	public static long[][] sub(final long[][] a, final long[][] b) {
		final long[][] res = copy(a);
		subRaw(res, b);
		return res;
	}

	public static void subRaw(final long[][] a, final long[][] b) {
		checkSameSize(a, b);
		for (int i = 0; i < a.length; i++) for (int j = 0; j < a[0].length; j++) a[i][j] -= b[i][j];
	}

	public static long[][] mul(final long[][] a, final long[][] b) {
		checkMulSize(a, b);
		final int n = a.length, m = a[0].length, l = b[0].length;
		final long[][] res = new long[n][l];
		for (int i = 0; i < n; i++) {
			for (int k = 0; k < m; k++) {
				if (a[i][k] == 0) continue;
				for (int j = 0; j < l; j++) res[i][j] += a[i][k] * b[k][j];
			}
		}
		return res;
	}

	public static void mulRaw(final long[][] a, final long[][] b) {
		checkMulSize(a, b);
		final int m = a[0].length, l = b[0].length;
		if (m != l) throw new ArithmeticException("raw size changes.");
		final long[][] right = a == b ? copy(b) : b;
		final long[] row = new long[l];
		for (long[] ai : a) {
			fill(row, 0);
			for (int k = 0; k < m; k++) {
				if (ai[k] == 0) continue;
				for (int j = 0; j < l; j++) row[j] += ai[k] * right[k][j];
			}
			System.arraycopy(row, 0, ai, 0, l);
		}
	}

	public static long[] mul(final long[][] a, final long[] b) {
		checkMulSize(a, b);
		final int n = a.length, m = a[0].length;
		final long[] res = new long[n];
		for (int i = 0; i < n; i++) {
			long s = 0;
			for (int j = 0; j < m; j++) s += a[i][j] * b[j];
			res[i] = s;
		}
		return res;
	}

	public static void mulRaw(final long[][] a, final long[] b) {
		checkMulSize(a, b);
		final int n = a.length, m = a[0].length;
		if (n != b.length) throw new ArithmeticException("raw size changes.");
		final long[] res = new long[n];
		for (int i = 0; i < n; i++) {
			long s = 0;
			for (int j = 0; j < m; j++) s += a[i][j] * b[j];
			res[i] = s;
		}
		System.arraycopy(res, 0, b, 0, n);
	}

	public static long[][] modAdd(final long[][] a, final long[][] b, final long mod) {
		final long[][] res = copy(a);
		modAddRaw(res, b, mod);
		return res;
	}

	public static void modAddRaw(final long[][] a, final long[][] b, final long mod) {
		checkSameSize(a, b);
		for (int i = 0; i < a.length; i++)
			for (int j = 0; j < a[0].length; j++) a[i][j] = (a[i][j] + b[i][j]) % mod;
	}

	public static long[][] modSub(final long[][] a, final long[][] b, final long mod) {
		final long[][] res = copy(a);
		modSubRaw(res, b, mod);
		return res;
	}

	public static void modSubRaw(final long[][] a, final long[][] b, final long mod) {
		checkSameSize(a, b);
		for (int i = 0; i < a.length; i++)
			for (int j = 0; j < a[0].length; j++) {
				final long v = (a[i][j] - b[i][j]) % mod;
				a[i][j] = v < 0 ? v + mod : v;
			}
	}

	public static long[][] modMul(final long[][] a, final long[][] b, final long mod) {
		checkMulSize(a, b);
		final int n = a.length, m = a[0].length, l = b[0].length;
		final long[][] res = new long[n][l];
		for (int i = 0; i < n; i++) {
			for (int k = 0; k < m; k++) {
				if (a[i][k] == 0) continue;
				for (int j = 0; j < l; j++) res[i][j] = (res[i][j] + a[i][k] % mod * (b[k][j] % mod) % mod) % mod;
			}
		}
		for (int i = 0; i < n; i++) for (int j = 0; j < l; j++) if (res[i][j] < 0) res[i][j] += mod;
		return res;
	}

	public static void modMulRaw(final long[][] a, final long[][] b, final long mod) {
		checkMulSize(a, b);
		final int n = a.length, m = a[0].length, l = b[0].length;
		if (m != l) throw new ArithmeticException("raw size changes.");
		final long[][] right = a == b ? copy(b) : b;
		final long[] row = new long[l];
		for (int i = 0; i < n; i++) {
			fill(row, 0);
			for (int k = 0; k < m; k++) {
				if (a[i][k] == 0) continue;
				for (int j = 0; j < l; j++) row[j] = (row[j] + a[i][k] % mod * (right[k][j] % mod) % mod) % mod;
			}
			for (int j = 0; j < l; j++) a[i][j] = row[j] < 0 ? row[j] + mod : row[j];
		}
	}

	public static long[] modMul(final long[][] a, final long[] b, final long mod) {
		checkMulSize(a, b);
		final int n = a.length, m = a[0].length;
		final long[] res = new long[n];
		for (int i = 0; i < n; i++) {
			long s = 0;
			for (int j = 0; j < m; j++) s = (s + a[i][j] % mod * (b[j] % mod) % mod) % mod;
			res[i] = s;
		}
		return res;
	}

	public static void modMulRaw(final long[][] a, final long[] b, final long mod) {
		checkMulSize(a, b);
		final int n = a.length, m = a[0].length;
		if (n != b.length) throw new ArithmeticException("raw size changes.");
		final long[] res = new long[n];
		for (int i = 0; i < n; i++) {
			long s = 0;
			for (int j = 0; j < m; j++) s = (s + a[i][j] % mod * (b[j] % mod) % mod) % mod;
			res[i] = s;
		}
		System.arraycopy(res, 0, b, 0, n);
	}

	public static void affine(final long[][] a, final long x, final long y) {
		affineRaw(a, x, y);
	}

	public static void affineRaw(final long[][] a, final long x, final long y) {
		for (int i = 0; i < a.length; i++) for (int j = 0; j < a[0].length; j++) a[i][j] = x * a[i][j] + y;
	}

	public static void modAffine(final long[][] a, final long x, final long y, final long mod) {
		modAffineRaw(a, x, y, mod);
	}

	public static void modAffineRaw(final long[][] a, long x, long y, final long mod) {
		x %= mod;
		y %= mod;
		for (int i = 0; i < a.length; i++)
			for (int j = 0; j < a[0].length; j++) {
				final long v = (x * a[i][j] % mod + y) % mod;
				a[i][j] = v < 0 ? v + mod : v;
			}
	}

	public static void add(final long[][] a, final long x) {
		affineRaw(a, 1, x);
	}

	public static void addRaw(final long[][] a, final long x) {
		affineRaw(a, 1, x);
	}

	public static void sub(final long[][] a, final long x) {
		affineRaw(a, 1, -x);
	}

	public static void subRaw(final long[][] a, final long x) {
		affineRaw(a, 1, -x);
	}

	public static void mul(final long[][] a, final long x) {
		affineRaw(a, x, 0);
	}

	public static void mulRaw(final long[][] a, final long x) {
		affineRaw(a, x, 0);
	}

	public static void div(final long[][] a, final long x) {
		divRaw(a, x);
	}

	public static void divRaw(final long[][] a, final long x) {
		for (int i = 0; i < a.length; i++) for (int j = 0; j < a[0].length; j++) a[i][j] /= x;
	}

	public static void modAdd(final long[][] a, final long x, final long mod) {
		modAffineRaw(a, 1, x, mod);
	}

	public static void modAddRaw(final long[][] a, final long x, final long mod) {
		modAffineRaw(a, 1, x, mod);
	}

	public static void modSub(final long[][] a, final long x, final long mod) {
		modAffineRaw(a, 1, -x, mod);
	}

	public static void modSubRaw(final long[][] a, final long x, final long mod) {
		modAffineRaw(a, 1, -x, mod);
	}

	public static void modMul(final long[][] a, final long x, final long mod) {
		modAffineRaw(a, x, 0, mod);
	}

	public static void modMulRaw(final long[][] a, final long x, final long mod) {
		modAffineRaw(a, x, 0, mod);
	}

	public static long[][] pow(final long[][] a, long exp) {
		checkSquare(a);
		if (exp < 0) throw new ArithmeticException("not defined.");
		final int n = a.length;
		long[][] result = identity(n);
		for (long[][] base = copy(a); exp > 0; exp >>= 1) {
			if ((exp & 1) == 1) result = mul(result, base);
			if (exp > 1) base = mul(base, base);
		}
		return result;
	}

	public static void powRaw(final long[][] a, final long exp) {
		final long[][] res = pow(a, exp);
		for (int i = 0; i < a.length; i++) System.arraycopy(res[i], 0, a[i], 0, a.length);
	}

	public static long[][] modPow(final long[][] a, long exp, final long mod) {
		checkSquare(a);
		if (exp < 0) throw new ArithmeticException("not defined.");
		final int n = a.length;
		long[][] result = identity(n);
		for (long[][] base = copy(a); exp > 0; exp >>= 1) {
			if ((exp & 1) == 1) result = modMul(result, base, mod);
			if (exp > 1) base = modMul(base, base, mod);
		}
		return result;
	}

	public static void modPowRaw(final long[][] a, final long exp, final long mod) {
		final long[][] res = modPow(a, exp, mod);
		for (int i = 0; i < a.length; i++) System.arraycopy(res[i], 0, a[i], 0, a.length);
	}
}
