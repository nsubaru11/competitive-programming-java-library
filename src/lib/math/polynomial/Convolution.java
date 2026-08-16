package lib.math.polynomial;

import static java.lang.Math.*;
import static java.util.Arrays.*;

@SuppressWarnings("unused")
public final class Convolution {
	private Convolution() {
	}

	// region public convolution methods
	public static long[] multiplyNtt(final long[] a, final long[] b, final long mod) {
		int len = a.length + b.length - 1;
		int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1;
		long[] pa = new long[n];
		long[] pb = new long[n];
		System.arraycopy(a, 0, pa, 0, a.length);
		System.arraycopy(b, 0, pb, 0, b.length);
		transformNtt(pa, false, mod);
		transformNtt(pb, false, mod);
		long[] c = new long[n];
		for (int i = 0; i < n; i++) {
			c[i] = (pa[i] * pb[i]) % mod;
		}
		transformNtt(c, true, mod);
		return copyOf(c, len);
	}

	public static int[] multiplyNtt(final int[] a, final int[] b, final int mod) {
		int len = a.length + b.length - 1;
		int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1;
		int[] pa = new int[n];
		int[] pb = new int[n];
		System.arraycopy(a, 0, pa, 0, a.length);
		System.arraycopy(b, 0, pb, 0, b.length);
		transformNtt(pa, false, mod);
		transformNtt(pb, false, mod);
		int[] c = new int[n];
		for (int i = 0; i < n; i++) {
			c[i] = (int) (((long) pa[i] * pb[i]) % mod);
		}
		transformNtt(c, true, mod);
		return copyOf(c, len);
	}

	public static long[] multiplyArbitraryMod(final long[] a, final long[] b, final long mod) {
		long[] res1 = multiplyNtt(a, b, 167772161);
		long[] res2 = multiplyNtt(a, b, 469762049);
		long[] res3 = multiplyNtt(a, b, 1224736769);
		return garnerProcess(res1, res2, res3, mod);
	}

	public static int[] multiplyArbitraryMod(final int[] a, final int[] b, final int mod) {
		int[] res1 = multiplyNtt(a, b, 167772161);
		int[] res2 = multiplyNtt(a, b, 469762049);
		int[] res3 = multiplyNtt(a, b, 1224736769);
		return garnerProcess(res1, res2, res3, mod);
	}

	public static double[] multiplyFft(final double[] a, final double[] b) {
		int len = a.length + b.length - 1;
		int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1;
		double[] par = new double[n], pai = new double[n];
		double[] pbr = new double[n], pbi = new double[n];
		System.arraycopy(a, 0, par, 0, a.length);
		System.arraycopy(b, 0, pbr, 0, b.length);
		transformFft(par, pai, false);
		transformFft(pbr, pbi, false);
		double[] cr = new double[n], ci = new double[n];
		for (int i = 0; i < n; i++) {
			cr[i] = par[i] * pbr[i] - pai[i] * pbi[i];
			ci[i] = par[i] * pbi[i] + pai[i] * pbr[i];
		}
		transformFft(cr, ci, true);
		return copyOf(cr, len);
	}

	public static long[] convoluteXor(final long[] a, final long[] b, final long mod) {
		int len = max(a.length, b.length);
		int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1;
		long[] pa = new long[n];
		long[] pb = new long[n];
		System.arraycopy(a, 0, pa, 0, a.length);
		System.arraycopy(b, 0, pb, 0, b.length);
		transformFwht(pa, false, mod);
		transformFwht(pb, false, mod);
		long[] c = new long[n];
		for (int i = 0; i < n; i++) {
			c[i] = (pa[i] * pb[i]) % mod;
		}
		transformFwht(c, true, mod);
		return copyOf(c, len);
	}

	public static int[] convoluteXor(final int[] a, final int[] b, final int mod) {
		int len = max(a.length, b.length);
		int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1;
		int[] pa = new int[n];
		int[] pb = new int[n];
		System.arraycopy(a, 0, pa, 0, a.length);
		System.arraycopy(b, 0, pb, 0, b.length);
		transformFwht(pa, false, mod);
		transformFwht(pb, false, mod);
		int[] c = new int[n];
		for (int i = 0; i < n; i++) {
			c[i] = (int) (((long) pa[i] * pb[i]) % mod);
		}
		transformFwht(c, true, mod);
		return copyOf(c, len);
	}

	public static long[] convoluteAnd(final long[] a, final long[] b, final long mod) {
		int len = max(a.length, b.length);
		int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1;
		long[] pa = new long[n];
		long[] pb = new long[n];
		System.arraycopy(a, 0, pa, 0, a.length);
		System.arraycopy(b, 0, pb, 0, b.length);
		transformSupersetZeta(pa, mod);
		transformSupersetZeta(pb, mod);
		long[] c = new long[n];
		for (int i = 0; i < n; i++) {
			c[i] = (pa[i] * pb[i]) % mod;
		}
		transformSupersetMobius(c, mod);
		return copyOf(c, len);
	}

	public static int[] convoluteAnd(final int[] a, final int[] b, final int mod) {
		int len = max(a.length, b.length);
		int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1;
		int[] pa = new int[n];
		int[] pb = new int[n];
		System.arraycopy(a, 0, pa, 0, a.length);
		System.arraycopy(b, 0, pb, 0, b.length);
		transformSupersetZeta(pa, mod);
		transformSupersetZeta(pb, mod);
		int[] c = new int[n];
		for (int i = 0; i < n; i++) {
			c[i] = (int) (((long) pa[i] * pb[i]) % mod);
		}
		transformSupersetMobius(c, mod);
		return copyOf(c, len);
	}

	public static long[] convoluteOr(final long[] a, final long[] b, final long mod) {
		int len = max(a.length, b.length);
		int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1;
		long[] pa = new long[n];
		long[] pb = new long[n];
		System.arraycopy(a, 0, pa, 0, a.length);
		System.arraycopy(b, 0, pb, 0, b.length);
		transformSubsetZeta(pa, mod);
		transformSubsetZeta(pb, mod);
		long[] c = new long[n];
		for (int i = 0; i < n; i++) {
			c[i] = (pa[i] * pb[i]) % mod;
		}
		transformSubsetMobius(c, mod);
		return copyOf(c, len);
	}

	public static int[] convoluteOr(final int[] a, final int[] b, final int mod) {
		int len = max(a.length, b.length);
		int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1;
		int[] pa = new int[n];
		int[] pb = new int[n];
		System.arraycopy(a, 0, pa, 0, a.length);
		System.arraycopy(b, 0, pb, 0, b.length);
		transformSubsetZeta(pa, mod);
		transformSubsetZeta(pb, mod);
		int[] c = new int[n];
		for (int i = 0; i < n; i++) {
			c[i] = (int) (((long) pa[i] * pb[i]) % mod);
		}
		transformSubsetMobius(c, mod);
		return copyOf(c, len);
	}

	public static long[] convoluteGcd(final long[] a, final long[] b, final long mod) {
		int n = max(a.length, b.length);
		long[] pa = new long[n];
		long[] pb = new long[n];
		System.arraycopy(a, 0, pa, 0, a.length);
		System.arraycopy(b, 0, pb, 0, b.length);
		transformMultipleZeta(pa, mod);
		transformMultipleZeta(pb, mod);
		long[] c = new long[n];
		for (int i = 0; i < n; i++) {
			c[i] = (pa[i] * pb[i]) % mod;
		}
		transformMultipleMobius(c, mod);
		return c;
	}

	public static int[] convoluteGcd(final int[] a, final int[] b, final int mod) {
		int n = max(a.length, b.length);
		int[] pa = new int[n];
		int[] pb = new int[n];
		System.arraycopy(a, 0, pa, 0, a.length);
		System.arraycopy(b, 0, pb, 0, b.length);
		transformMultipleZeta(pa, mod);
		transformMultipleZeta(pb, mod);
		int[] c = new int[n];
		for (int i = 0; i < n; i++) {
			c[i] = (int) (((long) pa[i] * pb[i]) % mod);
		}
		transformMultipleMobius(c, mod);
		return c;
	}

	public static long[] convoluteLcm(final long[] a, final long[] b, final long mod) {
		int n = max(a.length, b.length);
		long[] pa = new long[n];
		long[] pb = new long[n];
		System.arraycopy(a, 0, pa, 0, a.length);
		System.arraycopy(b, 0, pb, 0, b.length);
		transformDivisorZeta(pa, mod);
		transformDivisorZeta(pb, mod);
		long[] c = new long[n];
		for (int i = 0; i < n; i++) {
			c[i] = (pa[i] * pb[i]) % mod;
		}
		transformDivisorMobius(c, mod);
		return c;
	}

	public static int[] convoluteLcm(final int[] a, final int[] b, final int mod) {
		int n = max(a.length, b.length);
		int[] pa = new int[n];
		int[] pb = new int[n];
		System.arraycopy(a, 0, pa, 0, a.length);
		System.arraycopy(b, 0, pb, 0, b.length);
		transformDivisorZeta(pa, mod);
		transformDivisorZeta(pb, mod);
		int[] c = new int[n];
		for (int i = 0; i < n; i++) {
			c[i] = (int) (((long) pa[i] * pb[i]) % mod);
		}
		transformDivisorMobius(c, mod);
		return c;
	}
	// endregion

	// region public transform methods
	// TODO: 以下の内部変換ロジックはすべて未実装。実装完了まで公開メソッドは正しい結果を返さない
	public static void transformNtt(final long[] a, final boolean isInverse, final long mod) { /* TODO: NTT（数論変換）の実装 */ }

	public static void transformNtt(final int[] a, final boolean isInverse, final int mod) { /* TODO: NTT（数論変換）の実装 */ }

	public static void transformFft(final double[] real, final double[] imag, final boolean isInverse) { /* TODO: FFT（高速フーリエ変換）の実装 */ }

	public static void transformFwht(final long[] a, final boolean isInverse, final long mod) { /* TODO: FWHT（高速ウォルシュ・アダマール変換）の実装 */ }

	public static void transformFwht(final int[] a, final boolean isInverse, final int mod) { /* TODO: FWHT（高速ウォルシュ・アダマール変換）の実装 */ }

	public static void transformSubsetZeta(final long[] a, final long mod) {
		final int n = a.length;
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l + i; t < l + j; t++) {
					a[t] = (a[t] + a[t - i]) % mod;
				}
			}
		}
	}

	public static void transformSubsetZeta(final int[] a, final int mod) {
		final int n = a.length;
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l + i; t < l + j; t++) {
					a[t] = (a[t] + a[t - i]) % mod;
				}
			}
		}
	}

	public static void transformSubsetMobius(final long[] a, final long mod) {
		final int n = a.length;
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l + i; t < l + j; t++) {
					a[t] = (a[t] - a[t - i]) % mod;
				}
			}
		}
		for (int i = 0; i < n; i++) if (a[i] < 0) a[i] += mod;
	}

	public static void transformSubsetMobius(final int[] a, final int mod) {
		final int n = a.length;
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l + i; t < l + j; t++) {
					a[t] = (a[t] - a[t - i]) % mod;
				}
			}
		}
		for (int i = 0; i < n; i++) if (a[i] < 0) a[i] += mod;
	}

	public static void transformSupersetZeta(final long[] a, final long mod) {
		final int n = a.length;
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l; t < l + i; t++) {
					a[t] = (a[t] + a[t + i]) % mod;
				}
			}
		}
	}

	public static void transformSupersetZeta(final int[] a, final int mod) {
		final int n = a.length;
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l; t < l + i; t++) {
					a[t] = (a[t] + a[t + i]) % mod;
				}
			}
		}
	}

	public static void transformSupersetMobius(final long[] a, final long mod) {
		final int n = a.length;
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l; t < l + i; t++) {
					a[t] = (a[t] - a[t + i] + mod) % mod;
				}
			}
		}
		for (int i = 0; i < n; i++) if (a[i] < 0) a[i] += mod;
	}

	public static void transformSupersetMobius(final int[] a, final int mod) {
		final int n = a.length;
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l; t < l + i; t++) {
					a[t] = (a[t] - a[t + i] + mod) % mod;
				}
			}
		}
		for (int i = 0; i < n; i++) if (a[i] < 0) a[i] += mod;
	}

	public static void transformMultipleZeta(final long[] a, final long mod) { /* TODO: 倍数ゼータ変換の実装 */ }

	public static void transformMultipleZeta(final int[] a, final int mod) { /* TODO: 倍数ゼータ変換の実装 */ }

	public static void transformMultipleMobius(final long[] a, final long mod) { /* TODO: 倍数メビウス変換の実装 */ }

	public static void transformMultipleMobius(final int[] a, final int mod) { /* TODO: 倍数メビウス変換の実装 */ }

	public static void transformDivisorZeta(final long[] a, final long mod) { /* TODO: 約数ゼータ変換の実装 */ }

	public static void transformDivisorZeta(final int[] a, final int mod) { /* TODO: 約数ゼータ変換の実装 */ }

	public static void transformDivisorMobius(final long[] a, final long mod) { /* TODO: 約数メビウス変換の実装 */ }

	public static void transformDivisorMobius(final int[] a, final int mod) { /* TODO: 約数メビウス変換の実装 */ }
	// endregion

	// region private helper methods
	private static long[] garnerProcess(long[] a, long[] b, long[] c, long mod) {
		// TODO: Garnerのアルゴリズムによる3素数CRT復元の実装
		long[] d = new long[a.length];
		return d;
	}

	private static int[] garnerProcess(int[] a, int[] b, int[] c, int mod) {
		// TODO: Garnerのアルゴリズムによる3素数CRT復元の実装
		int[] d = new int[a.length];
		return d;
	}
	// endregion
}
