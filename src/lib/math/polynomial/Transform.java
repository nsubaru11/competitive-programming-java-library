package lib.math.polynomial;

import static java.lang.Math.*;

public final class Transform {
	private Transform() {}

	// TODO: 以下の内部変換ロジックはすべて未実装。実装完了まで公開メソッドは正しい結果を返さない
	// region ntt
	public static long[] ntt(final long[] a, final int len, final boolean isInverse, final long mod) {
		final int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1, m = min(a.length, n);
		final long[] res = new long[n];
		for (int i = 0; i < m; i++) {
			final long v = a[i] % mod;
			res[i] = v >= 0 ? v : v + mod;
		}
		/* TODO: NTT（数論変換）の実装 */
		return res;
	}

	public static long[] ntt(final long[] a, final boolean isInverse, final long mod) {
		return ntt(a, a.length, isInverse, mod);
	}

	public static int[] ntt(final int[] a, final int len, final boolean isInverse, final int mod) {
		final int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1, m = min(a.length, n);
		final int[] res = new int[n];
		for (int i = 0; i < m; i++) {
			final int v = a[i] % mod;
			res[i] = v >= 0 ? v : v + mod;
		}
		/* TODO: NTT（数論変換）の実装 */
		return res;
	}

	public static int[] ntt(final int[] a, final boolean isInverse, final int mod) {
		return ntt(a, a.length, isInverse, mod);
	}
	// endregion

	// region fft
	public static double[][] fft(final double[] real, final double[] imag, final int len, final boolean isInverse) {
		final int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1;
		final double[][] result = new double[2][n];
		System.arraycopy(real, 0, result[0], 0, min(n, real.length));
		System.arraycopy(imag, 0, result[1], 0, min(n, imag.length));
		/* TODO: FFT（高速フーリエ変換）の実装 */
		return result;
	}

	public static double[][] fft(final double[] real, final double[] imag, final boolean isInverse) {
		return fft(real, imag, max(real.length, imag.length), isInverse);
	}

	public static double[][] fft(final double[] a, final int len, final boolean isInverse) {
		final int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1;
		final double[][] result = new double[2][n];
		System.arraycopy(a, 0, result[0], 0, min(n, a.length));
		/* TODO: FFT（高速フーリエ変換）の実装 */
		return result;
	}

	public static double[][] fft(final double[] a, final boolean isInverse) {
		return fft(a, a.length, isInverse);
	}

	public static double[][] fft(final long[] a, final int len, final boolean isInverse) {
		final int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1, m = min(a.length, n);
		final double[][] result = new double[2][n];
		for (int i = 0; i < m; i++) result[0][i] = a[i];
		/* TODO: FFT（高速フーリエ変換）の実装 */
		return result;
	}

	public static double[][] fft(final long[] a, final boolean isInverse) {
		return fft(a, a.length, isInverse);
	}

	public static double[][] fft(final int[] a, final int len, final boolean isInverse) {
		final int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1, m = min(a.length, n);
		final double[][] result = new double[2][n];
		for (int i = 0; i < m; i++) result[0][i] = a[i];
		/* TODO: FFT（高速フーリエ変換）の実装 */
		return result;
	}

	public static double[][] fft(final int[] a, final boolean isInverse) {
		return fft(a, a.length, isInverse);
	}
	// endregion

	// region fwht
	public static long[] fwht(final long[] a, final int len, final boolean isInverse, final long mod) {
		final int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1, m = min(a.length, n);
		final long[] res = new long[n];
		for (int i = 0; i < m; i++) {
			final long v = a[i] % mod;
			res[i] = v >= 0 ? v : v + mod;
		}
		/* TODO: FWHT（高速ウォルシュ・アダマール変換）の実装 */
		return res;
	}

	public static long[] fwht(final long[] a, final boolean isInverse, final long mod) {
		return fwht(a, a.length, isInverse, mod);
	}

	public static long[] fwht(final long[] a, final int len, final boolean isInverse) {
		final int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1;
		final long[] res = new long[n];
		System.arraycopy(a, 0, res, 0, min(n, a.length));
		/* TODO: FWHT（高速ウォルシュ・アダマール変換）の実装 */
		return res;
	}

	public static long[] fwht(final long[] a, final boolean isInverse) {
		return fwht(a, a.length, isInverse);
	}

	public static int[] fwht(final int[] a, final int len, final boolean isInverse, final int mod) {
		final int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1, m = min(a.length, n);
		final int[] res = new int[n];
		for (int i = 0; i < m; i++) {
			final int v = a[i] % mod;
			res[i] = v >= 0 ? v : v + mod;
		}
		/* TODO: FWHT（高速ウォルシュ・アダマール変換）の実装 */
		return res;
	}

	public static int[] fwht(final int[] a, final boolean isInverse, final int mod) {
		return fwht(a, a.length, isInverse, mod);
	}

	public static long[] fwht(final int[] a, final int len, final boolean isInverse) {
		final int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1, m = min(a.length, n);
		final long[] res = new long[n];
		for (int i = 0; i < m; i++) res[i] = a[i];
		/* TODO: FWHT（高速ウォルシュ・アダマール変換）の実装 */
		return res;
	}

	public static long[] fwht(final int[] a, final boolean isInverse) {
		return fwht(a, a.length, isInverse);
	}
	// endregion

	// region subset zeta
	public static long[] subsetZeta(final long[] a, final int len, final long mod) {
		final int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1, m = min(a.length, n);
		final long[] res = new long[n];
		for (int i = 0; i < m; i++) {
			final long v = a[i] % mod;
			res[i] = v >= 0 ? v : v + mod;
		}
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l + i; t < l + j; t++) {
					res[t] += res[t - i];
					if (res[t] >= mod) res[t] -= mod;
				}
			}
		}
		return res;
	}

	public static long[] subsetZeta(final long[] a, final int len) {
		final int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1, m = min(a.length, n);
		final long[] res = new long[n];
		System.arraycopy(a, 0, res, 0, m);
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l + i; t < l + j; t++) {
					res[t] += res[t - i];
				}
			}
		}
		return res;
	}

	public static long[] subsetZeta(final long[] a) {
		return subsetZeta(a, a.length);
	}

	public static int[] subsetZeta(final int[] a, final int len, final int mod) {
		final int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1, m = min(a.length, n);
		final int[] res = new int[n];
		for (int i = 0; i < m; i++) {
			final int v = a[i] % mod;
			res[i] = v >= 0 ? v : v + mod;
		}
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l + i; t < l + j; t++) {
					res[t] += res[t - i];
					if (res[t] >= mod) res[t] -= mod;
				}
			}
		}
		return res;
	}

	public static long[] subsetZeta(final int[] a, final int len) {
		final int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1, m = min(a.length, n);
		final long[] res = new long[n];
		for (int i = 0; i < m; i++) res[i] = a[i];
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l + i; t < l + j; t++) {
					res[t] += res[t - i];
				}
			}
		}
		return res;
	}

	public static long[] subsetZeta(final int[] a) {
		return subsetZeta(a, a.length);
	}
	// endregion

	// region subset mobius
	public static long[] subsetMobius(final long[] a, final int len, final long mod) {
		final int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1, m = min(a.length, n);
		final long[] res = new long[n];
		for (int i = 0; i < m; i++) {
			final long v = a[i] % mod;
			res[i] = v >= 0 ? v : v + mod;
		}
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l + i; t < l + j; t++) {
					res[t] -= res[t - i];
					if (res[t] < 0) res[t] += mod;
				}
			}
		}
		return res;
	}

	public static long[] subsetMobius(final long[] a, final int len) {
		final int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1, m = min(a.length, n);
		final long[] res = new long[n];
		System.arraycopy(a, 0, res, 0, m);
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l + i; t < l + j; t++) {
					res[t] -= res[t - i];
				}
			}
		}
		return res;
	}

	public static long[] subsetMobius(final long[] a) {
		return subsetMobius(a, a.length);
	}

	public static int[] subsetMobius(final int[] a, final int len, final int mod) {
		final int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1, m = min(a.length, n);
		final int[] res = new int[n];
		for (int i = 0; i < m; i++) {
			final int v = a[i] % mod;
			res[i] = v >= 0 ? v : v + mod;
		}
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l + i; t < l + j; t++) {
					res[t] -= res[t - i];
					if (res[t] < 0) res[t] += mod;
				}
			}
		}
		return res;
	}

	public static long[] subsetMobius(final int[] a, final int len) {
		final int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1, m = min(a.length, n);
		final long[] res = new long[n];
		for (int i = 0; i < m; i++) res[i] = a[i];
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l + i; t < l + j; t++) {
					res[t] -= res[t - i];
				}
			}
		}
		return res;
	}

	public static long[] subsetMobius(final int[] a) {
		return subsetMobius(a, a.length);
	}
	// endregion

	// region superset zeta
	public static long[] supersetZeta(final long[] a, final int len, final long mod) {
		final int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1, m = min(a.length, n);
		final long[] res = new long[n];
		for (int i = 0; i < m; i++) {
			final long v = a[i] % mod;
			res[i] = v >= 0 ? v : v + mod;
		}
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l; t < l + i; t++) {
					res[t] += res[t + i];
					if (res[t] >= mod) res[t] -= mod;
				}
			}
		}
		return res;
	}

	public static long[] supersetZeta(final long[] a, final int len) {
		final int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1, m = min(a.length, n);
		final long[] res = new long[n];
		System.arraycopy(a, 0, res, 0, m);
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l; t < l + i; t++) {
					res[t] += res[t + i];
				}
			}
		}
		return res;
	}

	public static long[] supersetZeta(final long[] a) {
		return supersetZeta(a, a.length);
	}

	public static int[] supersetZeta(final int[] a, final int len, final int mod) {
		final int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1, m = min(a.length, n);
		final int[] res = new int[n];
		for (int i = 0; i < m; i++) {
			final int v = a[i] % mod;
			res[i] = v >= 0 ? v : v + mod;
		}
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l; t < l + i; t++) {
					res[t] += res[t + i];
					if (res[t] >= mod) res[t] -= mod;
				}
			}
		}
		return res;
	}

	public static long[] supersetZeta(final int[] a, final int len) {
		final int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1, m = min(a.length, n);
		final long[] res = new long[n];
		for (int i = 0; i < m; i++) res[i] = a[i];
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l; t < l + i; t++) {
					res[t] += res[t + i];
				}
			}
		}
		return res;
	}

	public static long[] supersetZeta(final int[] a) {
		return supersetZeta(a, a.length);
	}
	// endregion

	// region superset mobius
	public static long[] supersetMobius(final long[] a, final int len, final long mod) {
		final int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1, m = min(a.length, n);
		final long[] res = new long[n];
		for (int i = 0; i < m; i++) {
			final long v = a[i] % mod;
			res[i] = v >= 0 ? v : v + mod;
		}
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l; t < l + i; t++) {
					res[t] -= res[t + i];
					if (res[t] < 0) res[t] += mod;
				}
			}
		}
		return res;
	}

	public static long[] supersetMobius(final long[] a, final int len) {
		final int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1, m = min(a.length, n);
		final long[] res = new long[n];
		System.arraycopy(a, 0, res, 0, m);
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l; t < l + i; t++) {
					res[t] -= res[t + i];
				}
			}
		}
		return res;
	}

	public static long[] supersetMobius(final long[] a) {
		return supersetMobius(a, a.length);
	}

	public static int[] supersetMobius(final int[] a, final int len, final int mod) {
		final int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1, m = min(a.length, n);
		final int[] res = new int[n];
		for (int i = 0; i < m; i++) {
			final int v = a[i] % mod;
			res[i] = v >= 0 ? v : v + mod;
		}
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l; t < l + i; t++) {
					res[t] -= res[t + i];
					if (res[t] < 0) res[t] += mod;
				}
			}
		}
		return res;
	}

	public static long[] supersetMobius(final int[] a, final int len) {
		final int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1, m = min(a.length, n);
		final long[] res = new long[n];
		for (int i = 0; i < m; i++) res[i] = a[i];
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l; t < l + i; t++) {
					res[t] -= res[t + i];
				}
			}
		}
		return res;
	}

	public static long[] supersetMobius(final int[] a) {
		return supersetMobius(a, a.length);
	}
	// endregion

	// region multiple zeta
	public static long[] multipleZeta(final long[] a, final int len, final long mod) {
		final long[] res = new long[len];
		final int m = min(len, a.length);
		for (int i = 0; i < m; i++) {
			final long v = a[i] % mod;
			res[i] = v >= 0 ? v : v + mod;
		}
		/* TODO: 倍数ゼータ変換の実装 */
		return res;
	}

	public static long[] multipleZeta(final long[] a, final long mod) {
		return multipleZeta(a, a.length, mod);
	}

	public static long[] multipleZeta(final long[] a, final int len) {
		final long[] res = new long[len];
		System.arraycopy(a, 0, res, 0, min(len, a.length));
		/* TODO: 倍数ゼータ変換の実装 */
		return res;
	}

	public static long[] multipleZeta(final long[] a) {
		return multipleZeta(a, a.length);
	}

	public static int[] multipleZeta(final int[] a, final int len, final int mod) {
		final int[] res = new int[len];
		final int m = min(len, a.length);
		for (int i = 0; i < m; i++) {
			final int v = a[i] % mod;
			res[i] = v >= 0 ? v : v + mod;
		}
		/* TODO: 倍数ゼータ変換の実装 */
		return res;
	}

	public static long[] multipleZeta(final int[] a, final int len) {
		final long[] res = new long[len];
		for (int i = 0; i < min(len, a.length); i++) res[i] = a[i];
		/* TODO: 倍数ゼータ変換の実装 */
		return res;
	}

	public static long[] multipleZeta(final int[] a) {
		return multipleZeta(a, a.length);
	}
	// endregion

	// region multiple mobius
	public static long[] multipleMobius(final long[] a, final int len, final long mod) {
		final long[] res = new long[len];
		final int m = min(len, a.length);
		for (int i = 0; i < m; i++) {
			final long v = a[i] % mod;
			res[i] = v >= 0 ? v : v + mod;
		}
		/* TODO: 倍数メビウス変換の実装 */
		return res;
	}

	public static long[] multipleMobius(final long[] a, final long mod) {
		return multipleMobius(a, a.length, mod);
	}

	public static long[] multipleMobius(final long[] a, final int len) {
		final long[] res = new long[len];
		System.arraycopy(a, 0, res, 0, min(len, a.length));
		/* TODO: 倍数メビウス変換の実装 */
		return res;
	}

	public static long[] multipleMobius(final long[] a) {
		return multipleMobius(a, a.length);
	}

	public static int[] multipleMobius(final int[] a, final int len, final int mod) {
		final int[] res = new int[len];
		final int m = min(len, a.length);
		for (int i = 0; i < m; i++) {
			final int v = a[i] % mod;
			res[i] = v >= 0 ? v : v + mod;
		}
		/* TODO: 倍数メビウス変換の実装 */
		return res;
	}

	public static long[] multipleMobius(final int[] a, final int len) {
		final long[] res = new long[len];
		for (int i = 0; i < min(len, a.length); i++) res[i] = a[i];
		/* TODO: 倍数メビウス変換の実装 */
		return res;
	}

	public static long[] multipleMobius(final int[] a) {
		return multipleMobius(a, a.length);
	}
	// endregion

	// region divisor zeta
	public static long[] divisorZeta(final long[] a, final int len, final long mod) {
		final long[] res = new long[len];
		final int m = min(len, a.length);
		for (int i = 0; i < m; i++) {
			final long v = a[i] % mod;
			res[i] = v >= 0 ? v : v + mod;
		}
		/* TODO: 約数ゼータ変換の実装 */
		return res;
	}

	public static long[] divisorZeta(final long[] a, final long mod) {
		return divisorZeta(a, a.length, mod);
	}

	public static long[] divisorZeta(final long[] a, final int len) {
		final long[] res = new long[len];
		System.arraycopy(a, 0, res, 0, min(len, a.length));
		/* TODO: 約数ゼータ変換の実装 */
		return res;
	}

	public static long[] divisorZeta(final long[] a) {
		return divisorZeta(a, a.length);
	}

	public static int[] divisorZeta(final int[] a, final int len, final int mod) {
		final int[] res = new int[len];
		final int m = min(len, a.length);
		for (int i = 0; i < m; i++) {
			final int v = a[i] % mod;
			res[i] = v >= 0 ? v : v + mod;
		}
		/* TODO: 約数ゼータ変換の実装 */
		return res;
	}

	public static long[] divisorZeta(final int[] a, final int len) {
		final long[] res = new long[len];
		for (int i = 0; i < min(len, a.length); i++) res[i] = a[i];
		/* TODO: 約数ゼータ変換の実装 */
		return res;
	}

	public static long[] divisorZeta(final int[] a) {
		return divisorZeta(a, a.length);
	}
	// endregion

	// region divisor mobius
	public static long[] divisorMobius(final long[] a, final int len, final long mod) {
		final long[] res = new long[len];
		final int m = min(len, a.length);
		for (int i = 0; i < m; i++) {
			final long v = a[i] % mod;
			res[i] = v >= 0 ? v : v + mod;
		}
		/* TODO: 約数メビウス変換の実装 */
		return res;
	}

	public static long[] divisorMobius(final long[] a, final long mod) {
		return divisorMobius(a, a.length, mod);
	}

	public static long[] divisorMobius(final long[] a, final int len) {
		final long[] res = new long[len];
		System.arraycopy(a, 0, res, 0, min(len, a.length));
		/* TODO: 約数メビウス変換の実装 */
		return res;
	}

	public static long[] divisorMobius(final long[] a) {
		return divisorMobius(a, a.length);
	}

	public static int[] divisorMobius(final int[] a, final int len, final int mod) {
		final int[] res = new int[len];
		final int m = min(len, a.length);
		for (int i = 0; i < m; i++) {
			final int v = a[i] % mod;
			res[i] = v >= 0 ? v : v + mod;
		}
		/* TODO: 約数メビウス変換の実装 */
		return res;
	}

	public static long[] divisorMobius(final int[] a, final int len) {
		final long[] res = new long[len];
		for (int i = 0; i < min(len, a.length); i++) res[i] = a[i];
		/* TODO: 約数メビウス変換の実装 */
		return res;
	}

	public static long[] divisorMobius(final int[] a) {
		return divisorMobius(a, a.length);
	}
	// endregion
}
