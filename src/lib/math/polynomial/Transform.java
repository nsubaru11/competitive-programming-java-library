package lib.math.polynomial;

import static java.lang.Math.*;

import java.util.*;

import lib.math.*;

public final class Transform {
	private static PrimeTable primeTable;

	private Transform() {}

	// TODO: FFTの内部ロジックは未実装。該当する公開メソッドは正しい結果を返さない
	// region ntt
	public static int[] ntt(final int[] a, final int len, final boolean isInverse, final int mod) {
		final int n = ceilPowerOfTwo(len);
		final int[] res = Arrays.copyOf(a, n);
		nttInPlace(res, isInverse, mod);
		return res;
	}

	public static int[] ntt(final int[] a, final boolean isInverse, final int mod) {
		return ntt(a, a.length, isInverse, mod);
	}

	public static long[] ntt(final long[] a, final int len, final boolean isInverse, final long mod) {
		final int n = ceilPowerOfTwo(len);
		final long[] res = Arrays.copyOf(a, n);
		nttInPlace(res, isInverse, mod);
		return res;
	}

	public static long[] ntt(final long[] a, final boolean isInverse, final long mod) {
		return ntt(a, a.length, isInverse, mod);
	}

	public static boolean nttInPlace(final int[] a, final boolean isInverse, final int mod) {
		final int n = a.length;
		if (n == 0 || (n & (n - 1)) != 0 || mod <= 1 || ((mod - 1) & (n - 1)) != 0) return false;
		if (n == 1) return true;
		for (int i = 1, j = 0; i < n; i++) {
			j ^= n - (n >> (Integer.numberOfTrailingZeros(i) + 1));
			if (i < j) {
				final int tmp = a[i];
				a[i] = a[j];
				a[j] = tmp;
			}
		}
		int root = primitiveRoot(mod);
		if (isInverse) root = MathUtils.modInv(root, mod);
		for (int w = 1; w < n; w <<= 1) {
			final int block = w << 1;
			final int rootStep = MathUtils.modPow(root, (mod - 1) / block, mod);
			for (int l = 0; l < n; l += block) {
				int rootPow = 1;
				for (int i = 0; i < w; i++) {
					final long x = a[l + i], y = (long) a[l + i + w] * rootPow % mod;
					long value = x + y - mod;
					a[l + i] = (int) (value + ((value >> 63) & mod));
					value = x - y;
					a[l + i + w] = (int) (value + ((value >> 63) & mod));
					rootPow = (int) ((long) rootPow * rootStep % mod);
				}
			}
		}
		if (isInverse) {
			final int invN = MathUtils.modInv(n, mod);
			for (int i = 0; i < n; i++) a[i] = (int) ((long) a[i] * invN % mod);
		}
		return true;
	}

	public static boolean nttInPlace(final long[] a, final boolean isInverse, final long mod) {
		final int n = a.length;
		if (n == 0 || (n & (n - 1)) != 0 || mod <= 1 || ((mod - 1) & (n - 1)) != 0) return false;
		if (n == 1) return true;
		for (int i = 1, j = 0; i < n; i++) {
			j ^= n - (n >> (Integer.numberOfTrailingZeros(i) + 1));
			if (i < j) {
				final long tmp = a[i];
				a[i] = a[j];
				a[j] = tmp;
			}
		}
		long root = primitiveRoot(mod);
		if (isInverse) root = MathUtils.modInv(root, mod);
		for (int w = 1; w < n; w <<= 1) {
			final int block = w << 1;
			final long rootStep = MathUtils.modPow(root, (mod - 1) / block, mod);
			for (int l = 0; l < n; l += block) {
				long rootPow = 1;
				for (int i = 0; i < w; i++) {
					final long x = a[l + i], y = a[l + i + w] * rootPow % mod;
					long value = x + y - mod;
					a[l + i] = value + ((value >> 63) & mod);
					value = x - y;
					a[l + i + w] = value + ((value >> 63) & mod);
					rootPow = rootPow * rootStep % mod;
				}
			}
		}
		if (isInverse) {
			final long invN = MathUtils.modInv(n, mod);
			for (int i = 0; i < n; i++) a[i] = a[i] * invN % mod;
		}
		return true;
	}
	// endregion

	// region fft
	public static double[][] fft(final int[] a, final int len, final boolean isInverse) {
		final int n = ceilPowerOfTwo(len), m = min(a.length, n);
		final double[][] result = new double[2][n];
		for (int i = 0; i < m; i++) result[0][i] = a[i];
		fftInPlace(result[0], result[1], isInverse);
		return result;
	}

	public static double[][] fft(final int[] a, final boolean isInverse) {
		return fft(a, a.length, isInverse);
	}

	public static double[][] fft(final long[] a, final int len, final boolean isInverse) {
		final int n = ceilPowerOfTwo(len), m = min(a.length, n);
		final double[][] result = new double[2][n];
		for (int i = 0; i < m; i++) result[0][i] = a[i];
		fftInPlace(result[0], result[1], isInverse);
		return result;
	}

	public static double[][] fft(final long[] a, final boolean isInverse) {
		return fft(a, a.length, isInverse);
	}

	public static double[][] fft(final double[] real, final double[] imag, final int len, final boolean isInverse) {
		final int n = ceilPowerOfTwo(len);
		final double[][] result = new double[2][];
		result[0] = Arrays.copyOf(real, n);
		result[1] = Arrays.copyOf(imag, n);
		fftInPlace(result[0], result[1], isInverse);
		return result;
	}

	public static double[][] fft(final double[] real, final double[] imag, final boolean isInverse) {
		return fft(real, imag, max(real.length, imag.length), isInverse);
	}

	public static double[][] fft(final double[] a, final int len, final boolean isInverse) {
		final int n = ceilPowerOfTwo(len);
		final double[][] result = new double[2][n];
		System.arraycopy(a, 0, result[0], 0, min(n, a.length));
		fftInPlace(result[0], result[1], isInverse);
		return result;
	}

	public static double[][] fft(final double[] a, final boolean isInverse) {
		return fft(a, a.length, isInverse);
	}

	public static boolean fftInPlace(final double[] real, final double[] imag, final boolean isInverse) {
		final int n = real.length;
		if (n == 0 || n != imag.length || (n & (n - 1)) != 0) return false;
		/* TODO: FFT（高速フーリエ変換）の実装 */
		return true;
	}
	// endregion

	// region fwht
	public static int[] fwht(final int[] a, final int len, final boolean isInverse, final int mod) {
		final int n = ceilPowerOfTwo(len);
		final int[] res = Arrays.copyOf(a, n);
		fwhtInPlace(res, isInverse, mod);
		return res;
	}

	public static int[] fwht(final int[] a, final boolean isInverse, final int mod) {
		return fwht(a, a.length, isInverse, mod);
	}

	public static long[] fwht(final int[] a, final int len, final boolean isInverse) {
		final int n = ceilPowerOfTwo(len), m = min(a.length, n);
		final long[] res = new long[n];
		for (int i = 0; i < m; i++) res[i] = a[i];
		fwhtInPlace(res, isInverse);
		return res;
	}

	public static long[] fwht(final int[] a, final boolean isInverse) {
		return fwht(a, a.length, isInverse);
	}

	public static long[] fwht(final long[] a, final int len, final boolean isInverse, final long mod) {
		final int n = ceilPowerOfTwo(len);
		final long[] res = Arrays.copyOf(a, n);
		fwhtInPlace(res, isInverse, mod);
		return res;
	}

	public static long[] fwht(final long[] a, final boolean isInverse, final long mod) {
		return fwht(a, a.length, isInverse, mod);
	}

	public static long[] fwht(final long[] a, final int len, final boolean isInverse) {
		final int n = ceilPowerOfTwo(len);
		final long[] res = Arrays.copyOf(a, n);
		fwhtInPlace(res, isInverse);
		return res;
	}

	public static long[] fwht(final long[] a, final boolean isInverse) {
		return fwht(a, a.length, isInverse);
	}

	public static boolean fwhtInPlace(final int[] a, final boolean isInverse, final int mod) {
		final int n = a.length;
		if (n == 0 || (n & (n - 1)) != 0) return false;
		for (int w = 1; w < n; w <<= 1) {
			final int block = w << 1;
			for (int l = 0; l < n; l += block) {
				for (int i = 0; i < w; i++) {
					final int x = a[l + i], y = a[l + i + w];
					a[l + i] = (x + y) % mod;
					a[l + i + w] = (x - y + mod) % mod;
				}
			}
		}
		if (isInverse) {
			final int invN = MathUtils.modInv(n, mod);
			for (int i = 0; i < n; i++) a[i] = (int) ((long) a[i] * invN % mod);
		}
		return true;
	}

	public static boolean fwhtInPlace(final int[] a, final boolean isInverse) {
		final int n = a.length;
		if (n == 0 || (n & (n - 1)) != 0) return false;
		for (int w = 1; w < n; w <<= 1) {
			final int block = w << 1;
			for (int l = 0; l < n; l += block) {
				for (int i = 0; i < w; i++) {
					final int x = a[l + i], y = a[l + i + w];
					a[l + i] = x + y;
					a[l + i + w] = x - y;
				}
			}
		}
		if (isInverse) {
			for (int i = 0; i < n; i++) a[i] /= n;
		}
		return true;
	}

	public static boolean fwhtInPlace(final long[] a, final boolean isInverse, final long mod) {
		final int n = a.length;
		if (n == 0 || (n & (n - 1)) != 0) return false;
		for (int w = 1; w < n; w <<= 1) {
			final int block = w << 1;
			for (int l = 0; l < n; l += block) {
				for (int i = 0; i < w; i++) {
					final long x = a[l + i], y = a[l + i + w];
					a[l + i] = (x + y) % mod;
					a[l + i + w] = (x - y + mod) % mod;
				}
			}
		}
		if (isInverse) {
			final long invN = MathUtils.modInv(n, mod);
			for (int i = 0; i < n; i++) a[i] = a[i] * invN % mod;
		}
		return true;
	}

	public static boolean fwhtInPlace(final long[] a, final boolean isInverse) {
		final int n = a.length;
		if (n == 0 || (n & (n - 1)) != 0) return false;
		for (int w = 1; w < n; w <<= 1) {
			final int block = w << 1;
			for (int l = 0; l < n; l += block) {
				for (int i = 0; i < w; i++) {
					final long x = a[l + i], y = a[l + i + w];
					a[l + i] = x + y;
					a[l + i + w] = x - y;
				}
			}
		}
		if (isInverse) {
			for (int i = 0; i < n; i++) a[i] /= n;
		}
		return true;
	}
	// endregion

	// region subset zeta
	public static int[] subsetZeta(final int[] a, final int len, final int mod) {
		final int n = ceilPowerOfTwo(len);
		final int[] res = Arrays.copyOf(a, n);
		subsetZetaInPlace(res, mod);
		return res;
	}

	public static long[] subsetZeta(final int[] a, final int len) {
		final int n = ceilPowerOfTwo(len), m = min(a.length, n);
		final long[] res = new long[n];
		for (int i = 0; i < m; i++) res[i] = a[i];
		subsetZetaInPlace(res);
		return res;
	}

	public static long[] subsetZeta(final int[] a) {
		return subsetZeta(a, a.length);
	}

	public static long[] subsetZeta(final long[] a, final int len, final long mod) {
		final int n = ceilPowerOfTwo(len);
		final long[] res = Arrays.copyOf(a, n);
		subsetZetaInPlace(res, mod);
		return res;
	}

	public static long[] subsetZeta(final long[] a, final int len) {
		final int n = ceilPowerOfTwo(len);
		final long[] res = Arrays.copyOf(a, n);
		subsetZetaInPlace(res);
		return res;
	}

	public static long[] subsetZeta(final long[] a) {
		return subsetZeta(a, a.length);
	}

	public static boolean subsetZetaInPlace(final int[] a, final int mod) {
		final int n = a.length;
		if (n == 0 || (n & (n - 1)) != 0) return false;
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l + i; t < l + j; t++) {
					a[t] = (a[t] + a[t - i]) % mod;
				}
			}
		}
		return true;
	}

	public static boolean subsetZetaInPlace(final int[] a) {
		final int n = a.length;
		if (n == 0 || (n & (n - 1)) != 0) return false;
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l + i; t < l + j; t++) {
					a[t] += a[t - i];
				}
			}
		}
		return true;
	}

	public static boolean subsetZetaInPlace(final long[] a, final long mod) {
		final int n = a.length;
		if (n == 0 || (n & (n - 1)) != 0) return false;
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l + i; t < l + j; t++) {
					a[t] = (a[t] + a[t - i]) % mod;
				}
			}
		}
		return true;
	}

	public static boolean subsetZetaInPlace(final long[] a) {
		final int n = a.length;
		if (n == 0 || (n & (n - 1)) != 0) return false;
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l + i; t < l + j; t++) {
					a[t] += a[t - i];
				}
			}
		}
		return true;
	}
	// endregion

	// region subset mobius
	public static int[] subsetMobius(final int[] a, final int len, final int mod) {
		final int n = ceilPowerOfTwo(len);
		final int[] res = Arrays.copyOf(a, n);
		subsetMobiusInPlace(res, mod);
		return res;
	}

	public static long[] subsetMobius(final int[] a, final int len) {
		final int n = ceilPowerOfTwo(len), m = min(a.length, n);
		final long[] res = new long[n];
		for (int i = 0; i < m; i++) res[i] = a[i];
		subsetMobiusInPlace(res);
		return res;
	}

	public static long[] subsetMobius(final int[] a) {
		return subsetMobius(a, a.length);
	}

	public static long[] subsetMobius(final long[] a, final int len, final long mod) {
		final int n = ceilPowerOfTwo(len);
		final long[] res = Arrays.copyOf(a, n);
		subsetMobiusInPlace(res, mod);
		return res;
	}

	public static long[] subsetMobius(final long[] a, final int len) {
		final int n = ceilPowerOfTwo(len);
		final long[] res = Arrays.copyOf(a, n);
		subsetMobiusInPlace(res);
		return res;
	}

	public static long[] subsetMobius(final long[] a) {
		return subsetMobius(a, a.length);
	}

	public static boolean subsetMobiusInPlace(final int[] a, final int mod) {
		final int n = a.length;
		if (n == 0 || (n & (n - 1)) != 0) return false;
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l + i; t < l + j; t++) {
					a[t] = (a[t] - a[t - i] + mod) % mod;
				}
			}
		}
		return true;
	}

	public static boolean subsetMobiusInPlace(final int[] a) {
		final int n = a.length;
		if (n == 0 || (n & (n - 1)) != 0) return false;
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l + i; t < l + j; t++) {
					a[t] -= a[t - i];
				}
			}
		}
		return true;
	}

	public static boolean subsetMobiusInPlace(final long[] a, final long mod) {
		final int n = a.length;
		if (n == 0 || (n & (n - 1)) != 0) return false;
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l + i; t < l + j; t++) {
					a[t] = (a[t] - a[t - i] + mod) % mod;
				}
			}
		}
		return true;
	}

	public static boolean subsetMobiusInPlace(final long[] a) {
		final int n = a.length;
		if (n == 0 || (n & (n - 1)) != 0) return false;
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l + i; t < l + j; t++) {
					a[t] -= a[t - i];
				}
			}
		}
		return true;
	}
	// endregion

	// region superset zeta
	public static int[] supersetZeta(final int[] a, final int len, final int mod) {
		final int n = ceilPowerOfTwo(len);
		final int[] res = Arrays.copyOf(a, n);
		supersetZetaInPlace(res, mod);
		return res;
	}

	public static long[] supersetZeta(final int[] a, final int len) {
		final int n = ceilPowerOfTwo(len), m = min(a.length, n);
		final long[] res = new long[n];
		for (int i = 0; i < m; i++) res[i] = a[i];
		supersetZetaInPlace(res);
		return res;
	}

	public static long[] supersetZeta(final int[] a) {
		return supersetZeta(a, a.length);
	}

	public static boolean supersetZetaInPlace(final int[] a, final int mod) {
		final int n = a.length;
		if (n == 0 || (n & (n - 1)) != 0) return false;
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l; t < l + i; t++) {
					a[t] = (a[t] + a[t + i]) % mod;
				}
			}
		}
		return true;
	}

	public static long[] supersetZeta(final long[] a, final int len, final long mod) {
		final int n = ceilPowerOfTwo(len);
		final long[] res = Arrays.copyOf(a, n);
		supersetZetaInPlace(res, mod);
		return res;
	}

	public static long[] supersetZeta(final long[] a, final int len) {
		final int n = ceilPowerOfTwo(len);
		final long[] res = Arrays.copyOf(a, n);
		supersetZetaInPlace(res);
		return res;
	}

	public static long[] supersetZeta(final long[] a) {
		return supersetZeta(a, a.length);
	}

	public static boolean supersetZetaInPlace(final int[] a) {
		final int n = a.length;
		if (n == 0 || (n & (n - 1)) != 0) return false;
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l; t < l + i; t++) {
					a[t] += a[t + i];
				}
			}
		}
		return true;
	}

	public static boolean supersetZetaInPlace(final long[] a, final long mod) {
		final int n = a.length;
		if (n == 0 || (n & (n - 1)) != 0) return false;
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l; t < l + i; t++) {
					a[t] = (a[t] + a[t + i]) % mod;
				}
			}
		}
		return true;
	}

	public static boolean supersetZetaInPlace(final long[] a) {
		final int n = a.length;
		if (n == 0 || (n & (n - 1)) != 0) return false;
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l; t < l + i; t++) {
					a[t] += a[t + i];
				}
			}
		}
		return true;
	}
	// endregion

	// region superset mobius
	public static int[] supersetMobius(final int[] a, final int len, final int mod) {
		final int n = ceilPowerOfTwo(len);
		final int[] res = Arrays.copyOf(a, n);
		supersetMobiusInPlace(res, mod);
		return res;
	}

	public static long[] supersetMobius(final int[] a, final int len) {
		final int n = ceilPowerOfTwo(len), m = min(a.length, n);
		final long[] res = new long[n];
		for (int i = 0; i < m; i++) res[i] = a[i];
		supersetMobiusInPlace(res);
		return res;
	}

	public static long[] supersetMobius(final int[] a) {
		return supersetMobius(a, a.length);
	}

	public static boolean supersetMobiusInPlace(final int[] a, final int mod) {
		final int n = a.length;
		if (n == 0 || (n & (n - 1)) != 0) return false;
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l; t < l + i; t++) {
					a[t] = (a[t] - a[t + i] + mod) % mod;
				}
			}
		}
		return true;
	}

	public static long[] supersetMobius(final long[] a, final int len, final long mod) {
		final int n = ceilPowerOfTwo(len);
		final long[] res = Arrays.copyOf(a, n);
		supersetMobiusInPlace(res, mod);
		return res;
	}

	public static long[] supersetMobius(final long[] a, final int len) {
		final int n = ceilPowerOfTwo(len);
		final long[] res = Arrays.copyOf(a, n);
		supersetMobiusInPlace(res);
		return res;
	}

	public static long[] supersetMobius(final long[] a) {
		return supersetMobius(a, a.length);
	}

	public static boolean supersetMobiusInPlace(final int[] a) {
		final int n = a.length;
		if (n == 0 || (n & (n - 1)) != 0) return false;
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l; t < l + i; t++) {
					a[t] -= a[t + i];
				}
			}
		}
		return true;
	}

	public static boolean supersetMobiusInPlace(final long[] a, final long mod) {
		final int n = a.length;
		if (n == 0 || (n & (n - 1)) != 0) return false;
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l; t < l + i; t++) {
					a[t] = (a[t] - a[t + i] + mod) % mod;
				}
			}
		}
		return true;
	}

	public static boolean supersetMobiusInPlace(final long[] a) {
		final int n = a.length;
		if (n == 0 || (n & (n - 1)) != 0) return false;
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l; t < l + i; t++) {
					a[t] -= a[t + i];
				}
			}
		}
		return true;
	}
	// endregion

	// region multiple zeta
	public static int[] multipleZeta(final int[] a, final int len, final int mod) {
		final int[] res = Arrays.copyOf(a, len);
		multipleZetaInPlace(res, mod);
		return res;
	}

	public static long[] multipleZeta(final int[] a, final int len) {
		final long[] res = new long[len];
		final int m = min(len, a.length);
		for (int i = 0; i < m; i++) res[i] = a[i];
		multipleZetaInPlace(res);
		return res;
	}

	public static long[] multipleZeta(final int[] a) {
		return multipleZeta(a, a.length);
	}

	public static long[] multipleZeta(final long[] a, final int len, final long mod) {
		final long[] res = Arrays.copyOf(a, len);
		multipleZetaInPlace(res, mod);
		return res;
	}

	public static long[] multipleZeta(final long[] a, final long mod) {
		return multipleZeta(a, a.length, mod);
	}

	public static long[] multipleZeta(final long[] a, final int len) {
		final long[] res = Arrays.copyOf(a, len);
		multipleZetaInPlace(res);
		return res;
	}

	public static long[] multipleZeta(final long[] a) {
		return multipleZeta(a, a.length);
	}

	public static boolean multipleZetaInPlace(final int[] a, final int mod) {
		final int n = a.length;
		if (n == 0) return false;
		ensurePrimeTable(n);
		final PrimeTable table = primeTable;
		for (int i = 0, count = table.getPrimeCount(); i < count; i++) {
			final int p = (int) table.get(i);
			for (int d = n / p, m = d * p; d >= 1; d--, m -= p) {
				a[d - 1] = (a[d - 1] + a[m - 1]) % mod;
			}
		}
		return true;
	}

	public static boolean multipleZetaInPlace(final int[] a) {
		final int n = a.length;
		if (n == 0) return false;
		ensurePrimeTable(n);
		final PrimeTable table = primeTable;
		for (int i = 0, count = table.getPrimeCount(); i < count; i++) {
			final int p = (int) table.get(i);
			for (int d = n / p, m = d * p; d >= 1; d--, m -= p) {
				a[d - 1] += a[m - 1];
			}
		}
		return true;
	}

	public static boolean multipleZetaInPlace(final long[] a, final long mod) {
		final int n = a.length;
		if (n == 0) return false;
		ensurePrimeTable(n);
		final PrimeTable table = primeTable;
		for (int i = 0, count = table.getPrimeCount(); i < count; i++) {
			final int p = (int) table.get(i);
			for (int d = n / p, m = d * p; d >= 1; d--, m -= p) {
				a[d - 1] = (a[d - 1] + a[m - 1]) % mod;
			}
		}
		return true;
	}

	public static boolean multipleZetaInPlace(final long[] a) {
		final int n = a.length;
		if (n == 0) return false;
		ensurePrimeTable(n);
		final PrimeTable table = primeTable;
		for (int i = 0, count = table.getPrimeCount(); i < count; i++) {
			final int p = (int) table.get(i);
			for (int d = n / p, m = d * p; d >= 1; d--, m -= p) {
				a[d - 1] += a[m - 1];
			}
		}
		return true;
	}
	// endregion

	// region multiple mobius
	public static int[] multipleMobius(final int[] a, final int len, final int mod) {
		final int[] res = Arrays.copyOf(a, len);
		multipleMobiusInPlace(res, mod);
		return res;
	}

	public static long[] multipleMobius(final int[] a, final int len) {
		final long[] res = new long[len];
		final int m = min(len, a.length);
		for (int i = 0; i < m; i++) res[i] = a[i];
		multipleMobiusInPlace(res);
		return res;
	}

	public static long[] multipleMobius(final int[] a) {
		return multipleMobius(a, a.length);
	}

	public static long[] multipleMobius(final long[] a, final int len, final long mod) {
		final long[] res = Arrays.copyOf(a, len);
		multipleMobiusInPlace(res, mod);
		return res;
	}

	public static long[] multipleMobius(final long[] a, final long mod) {
		return multipleMobius(a, a.length, mod);
	}

	public static long[] multipleMobius(final long[] a, final int len) {
		final long[] res = Arrays.copyOf(a, len);
		multipleMobiusInPlace(res);
		return res;
	}

	public static long[] multipleMobius(final long[] a) {
		return multipleMobius(a, a.length);
	}

	public static boolean multipleMobiusInPlace(final int[] a, final int mod) {
		final int n = a.length;
		if (n == 0) return false;
		ensurePrimeTable(n);
		final PrimeTable table = primeTable;
		for (int i = 0, count = table.getPrimeCount(); i < count; i++) {
			final int p = (int) table.get(i);
			for (int d = 1, m = p; m <= n; d++, m += p) {
				a[d - 1] = (a[d - 1] - a[m - 1] + mod) % mod;
			}
		}
		return true;
	}

	public static boolean multipleMobiusInPlace(final int[] a) {
		final int n = a.length;
		if (n == 0) return false;
		ensurePrimeTable(n);
		final PrimeTable table = primeTable;
		for (int i = 0, count = table.getPrimeCount(); i < count; i++) {
			final int p = (int) table.get(i);
			for (int d = 1, m = p; m <= n; d++, m += p) {
				a[d - 1] -= a[m - 1];
			}
		}
		return true;
	}

	public static boolean multipleMobiusInPlace(final long[] a, final long mod) {
		final int n = a.length;
		if (n == 0) return false;
		ensurePrimeTable(n);
		final PrimeTable table = primeTable;
		for (int i = 0, count = table.getPrimeCount(); i < count; i++) {
			final int p = (int) table.get(i);
			for (int d = 1, m = p; m <= n; d++, m += p) {
				a[d - 1] = (a[d - 1] - a[m - 1] + mod) % mod;
			}
		}
		return true;
	}

	public static boolean multipleMobiusInPlace(final long[] a) {
		final int n = a.length;
		if (n == 0) return false;
		ensurePrimeTable(n);
		final PrimeTable table = primeTable;
		for (int i = 0, count = table.getPrimeCount(); i < count; i++) {
			final int p = (int) table.get(i);
			for (int d = 1, m = p; m <= n; d++, m += p) {
				a[d - 1] -= a[m - 1];
			}
		}
		return true;
	}
	// endregion

	// region divisor zeta
	public static int[] divisorZeta(final int[] a, final int len, final int mod) {
		final int[] res = Arrays.copyOf(a, len);
		divisorZetaInPlace(res, mod);
		return res;
	}

	public static long[] divisorZeta(final int[] a, final int len) {
		final long[] res = new long[len];
		final int m = min(len, a.length);
		for (int i = 0; i < m; i++) res[i] = a[i];
		divisorZetaInPlace(res);
		return res;
	}

	public static long[] divisorZeta(final int[] a) {
		return divisorZeta(a, a.length);
	}

	public static long[] divisorZeta(final long[] a, final int len, final long mod) {
		final long[] res = Arrays.copyOf(a, len);
		divisorZetaInPlace(res, mod);
		return res;
	}

	public static long[] divisorZeta(final long[] a, final long mod) {
		return divisorZeta(a, a.length, mod);
	}

	public static long[] divisorZeta(final long[] a, final int len) {
		final long[] res = Arrays.copyOf(a, len);
		divisorZetaInPlace(res);
		return res;
	}

	public static long[] divisorZeta(final long[] a) {
		return divisorZeta(a, a.length);
	}

	public static boolean divisorZetaInPlace(final int[] a, final int mod) {
		final int n = a.length;
		if (n == 0) return false;
		ensurePrimeTable(n);
		final PrimeTable table = primeTable;
		for (int i = 0, count = table.getPrimeCount(); i < count; i++) {
			final int p = (int) table.get(i);
			for (int d = 1, m = p; m <= n; d++, m += p) {
				a[m - 1] = (a[m - 1] + a[d - 1]) % mod;
			}
		}
		return true;
	}

	public static boolean divisorZetaInPlace(final int[] a) {
		final int n = a.length;
		if (n == 0) return false;
		ensurePrimeTable(n);
		final PrimeTable table = primeTable;
		for (int i = 0, count = table.getPrimeCount(); i < count; i++) {
			final int p = (int) table.get(i);
			for (int d = 1, m = p; m <= n; d++, m += p) {
				a[m - 1] += a[d - 1];
			}
		}
		return true;
	}

	public static boolean divisorZetaInPlace(final long[] a, final long mod) {
		final int n = a.length;
		if (n == 0) return false;
		ensurePrimeTable(n);
		final PrimeTable table = primeTable;
		for (int i = 0, count = table.getPrimeCount(); i < count; i++) {
			final int p = (int) table.get(i);
			for (int d = 1, m = p; m <= n; d++, m += p) {
				a[m - 1] = (a[m - 1] + a[d - 1]) % mod;
			}
		}
		return true;
	}

	public static boolean divisorZetaInPlace(final long[] a) {
		final int n = a.length;
		if (n == 0) return false;
		ensurePrimeTable(n);
		final PrimeTable table = primeTable;
		for (int i = 0, count = table.getPrimeCount(); i < count; i++) {
			final int p = (int) table.get(i);
			for (int d = 1, m = p; m <= n; d++, m += p) {
				a[m - 1] += a[d - 1];
			}
		}
		return true;
	}
	// endregion

	// region divisor mobius
	public static int[] divisorMobius(final int[] a, final int len, final int mod) {
		final int[] res = Arrays.copyOf(a, len);
		divisorMobiusInPlace(res, mod);
		return res;
	}

	public static long[] divisorMobius(final int[] a, final int len) {
		final long[] res = new long[len];
		final int m = min(len, a.length);
		for (int i = 0; i < m; i++) res[i] = a[i];
		divisorMobiusInPlace(res);
		return res;
	}

	public static long[] divisorMobius(final int[] a) {
		return divisorMobius(a, a.length);
	}

	public static long[] divisorMobius(final long[] a, final int len, final long mod) {
		final long[] res = Arrays.copyOf(a, len);
		divisorMobiusInPlace(res, mod);
		return res;
	}

	public static long[] divisorMobius(final long[] a, final long mod) {
		return divisorMobius(a, a.length, mod);
	}

	public static long[] divisorMobius(final long[] a, final int len) {
		final long[] res = Arrays.copyOf(a, len);
		divisorMobiusInPlace(res);
		return res;
	}

	public static long[] divisorMobius(final long[] a) {
		return divisorMobius(a, a.length);
	}

	public static boolean divisorMobiusInPlace(final int[] a, final int mod) {
		final int n = a.length;
		if (n == 0) return false;
		ensurePrimeTable(n);
		final PrimeTable table = primeTable;
		for (int i = 0, count = table.getPrimeCount(); i < count; i++) {
			final int p = (int) table.get(i);
			for (int d = n / p, m = d * p; d >= 1; d--, m -= p) {
				a[m - 1] = (a[m - 1] - a[d - 1] + mod) % mod;
			}
		}
		return true;
	}

	public static boolean divisorMobiusInPlace(final int[] a) {
		final int n = a.length;
		if (n == 0) return false;
		ensurePrimeTable(n);
		final PrimeTable table = primeTable;
		for (int i = 0, count = table.getPrimeCount(); i < count; i++) {
			final int p = (int) table.get(i);
			for (int d = n / p, m = d * p; d >= 1; d--, m -= p) {
				a[m - 1] -= a[d - 1];
			}
		}
		return true;
	}

	public static boolean divisorMobiusInPlace(final long[] a, final long mod) {
		final int n = a.length;
		if (n == 0) return false;
		ensurePrimeTable(n);
		final PrimeTable table = primeTable;
		for (int i = 0, count = table.getPrimeCount(); i < count; i++) {
			final int p = (int) table.get(i);
			for (int d = n / p, m = d * p; d >= 1; d--, m -= p) {
				a[m - 1] = (a[m - 1] - a[d - 1] + mod) % mod;
			}
		}
		return true;
	}

	public static boolean divisorMobiusInPlace(final long[] a) {
		final int n = a.length;
		if (n == 0) return false;
		ensurePrimeTable(n);
		final PrimeTable table = primeTable;
		for (int i = 0, count = table.getPrimeCount(); i < count; i++) {
			final int p = (int) table.get(i);
			for (int d = n / p, m = d * p; d >= 1; d--, m -= p) {
				a[m - 1] -= a[d - 1];
			}
		}
		return true;
	}
	// endregion

	private static void ensurePrimeTable(final int n) {
		if (primeTable != null && n <= primeTable.getLimitValue()) return;
		primeTable = new PrimeTable(n);
	}

	private static int primitiveRoot(final int mod) {
		if (mod == 167772161 || mod == 469762049 || mod == 998244353 || mod == 1224736769) return 3;
		final int phi = mod - 1;
		int n = phi, count = 0;
		final int[] factors = new int[32];
		if ((n & 1) == 0) {
			factors[count++] = 2;
			n >>= Integer.numberOfTrailingZeros(n);
		}
		for (int p = 3; (long) p * p <= n; p += 2) {
			if (n % p != 0) continue;
			factors[count++] = p;
			while (n % p == 0) n /= p;
		}
		if (n > 1) factors[count++] = n;
		for (int root = 2; root < mod; root++) {
			int i = 0;
			for (; i < count; i++) {
				if (MathUtils.modPow(root, phi / factors[i], mod) == 1) break;
			}
			if (i == count) return root;
		}
		throw new IllegalArgumentException();
	}

	private static long primitiveRoot(final long mod) {
		if (mod == 167772161 || mod == 469762049 || mod == 998244353 || mod == 1224736769) return 3;
		final long phi = mod - 1;
		long n = phi;
		int count = 0;
		final long[] factors = new long[64];
		if ((n & 1) == 0) {
			factors[count++] = 2;
			n >>= Long.numberOfTrailingZeros(n);
		}
		for (long p = 3; p <= n / p; p += 2) {
			if (n % p != 0) continue;
			factors[count++] = p;
			while (n % p == 0) n /= p;
		}
		if (n > 1) factors[count++] = n;
		for (long root = 2; root < mod; root++) {
			int i = 0;
			for (; i < count; i++) {
				if (MathUtils.modPow(root, phi / factors[i], mod) == 1) break;
			}
			if (i == count) return root;
		}
		throw new IllegalArgumentException();
	}

	private static int ceilPowerOfTwo(final int n) {
		return n <= 1 ? 1 : 1 << -Integer.numberOfLeadingZeros(n - 1);
	}
}
