package lib.math.polynomial;

import static java.lang.Math.*;
import static java.util.Arrays.*;

@SuppressWarnings("unused")
public final class Convolution {
	private Convolution() {
	}

	public static long[] convolveNtt(final long[] a, final long[] b, final long mod) {
		final int len = a.length + b.length - 1;
		final long[] pa = Transform.ntt(a, len, false, mod);
		final long[] pb = Transform.ntt(b, len, false, mod);
		final int n = pa.length;
		final long[] c = new long[n];
		for (int i = 0; i < n; i++) c[i] = (pa[i] * pb[i]) % mod;
		return copyOf(Transform.ntt(c, len, true, mod), len);
	}

	public static int[] convolveNtt(final int[] a, final int[] b, final int mod) {
		final int len = a.length + b.length - 1;
		final int[] pa = Transform.ntt(a, len, false, mod);
		final int[] pb = Transform.ntt(b, len, false, mod);
		final int n = pa.length;
		final int[] c = new int[n];
		for (int i = 0; i < n; i++) c[i] = (int) (((long) pa[i] * pb[i]) % mod);
		return copyOf(Transform.ntt(c, len, true, mod), len);
	}

	public static long[] convolveArbitraryMod(final long[] a, final long[] b, final long mod) {
		final long[] res1 = convolveNtt(a, b, 167772161);
		final long[] res2 = convolveNtt(a, b, 469762049);
		final long[] res3 = convolveNtt(a, b, 1224736769);
		return garnerProcess(res1, res2, res3, mod);
	}

	public static int[] convolveArbitraryMod(final int[] a, final int[] b, final int mod) {
		final int[] res1 = convolveNtt(a, b, 167772161);
		final int[] res2 = convolveNtt(a, b, 469762049);
		final int[] res3 = convolveNtt(a, b, 1224736769);
		return garnerProcess(res1, res2, res3, mod);
	}

	public static double[] convolveFft(final double[] a, final double[] b) {
		final int len = a.length + b.length - 1;
		final double[][] fft1 = Transform.fft(a, len, false);
		final double[][] fft2 = Transform.fft(b, len, false);
		final int n = fft1[0].length;
		final double[] cr = new double[n], ci = new double[n];
		for (int i = 0; i < n; i++) {
			cr[i] = fft1[0][i] * fft2[0][i] - fft1[1][i] * fft2[1][i];
			ci[i] = fft1[0][i] * fft2[1][i] + fft1[1][i] * fft2[0][i];
		}
		final double[][] fft3 = Transform.fft(cr, ci, true);
		return copyOf(fft3[0], len);
	}

	public static long[] convolveFft(final long[] a, final long[] b) {
		final int len = a.length + b.length - 1;
		final double[][] fft1 = Transform.fft(a, len, false);
		final double[][] fft2 = Transform.fft(b, len, false);
		final int n = fft1[0].length;
		final double[] cr = new double[n], ci = new double[n];
		for (int i = 0; i < n; i++) {
			cr[i] = fft1[0][i] * fft2[0][i] - fft1[1][i] * fft2[1][i];
			ci[i] = fft1[0][i] * fft2[1][i] + fft1[1][i] * fft2[0][i];
		}
		final double[][] fft3 = Transform.fft(cr, ci, true);
		final long[] res = new long[len];
		for (int i = 0; i < len; i++) res[i] = round(fft3[0][i]);
		return res;
	}

	public static long[] convolveFft(final int[] a, final int[] b) {
		final int len = a.length + b.length - 1;
		final double[][] fft1 = Transform.fft(a, len, false);
		final double[][] fft2 = Transform.fft(b, len, false);
		final int n = fft1[0].length;
		final double[] cr = new double[n], ci = new double[n];
		for (int i = 0; i < n; i++) {
			cr[i] = fft1[0][i] * fft2[0][i] - fft1[1][i] * fft2[1][i];
			ci[i] = fft1[0][i] * fft2[1][i] + fft1[1][i] * fft2[0][i];
		}
		final double[][] fft3 = Transform.fft(cr, ci, true);
		final long[] res = new long[len];
		for (int i = 0; i < len; i++) res[i] = round(fft3[0][i]);
		return res;
	}

	public static long[] convolveXor(final long[] a, final long[] b, final long mod) {
		final int len = max(a.length, b.length);
		final long[] pa = Transform.fwht(a, len, false, mod);
		final long[] pb = Transform.fwht(b, len, false, mod);
		final int n = pa.length;
		final long[] c = new long[n];
		for (int i = 0; i < n; i++) c[i] = (pa[i] * pb[i]) % mod;
		return Transform.fwht(c, len, true, mod);
	}

	public static long[] convolveXor(final long[] a, final long[] b) {
		final int len = max(a.length, b.length);
		final long[] pa = Transform.fwht(a, len, false);
		final long[] pb = Transform.fwht(b, len, false);
		final int n = pa.length;
		final long[] c = new long[n];
		for (int i = 0; i < n; i++) c[i] = pa[i] * pb[i];
		return Transform.fwht(c, len, true);
	}

	public static int[] convolveXor(final int[] a, final int[] b, final int mod) {
		final int len = max(a.length, b.length);
		final int[] pa = Transform.fwht(a, len, false, mod);
		final int[] pb = Transform.fwht(b, len, false, mod);
		final int n = pa.length;
		final int[] c = new int[n];
		for (int i = 0; i < n; i++) c[i] = (int) (((long) pa[i] * pb[i]) % mod);
		return Transform.fwht(c, len, true, mod);
	}

	public static long[] convolveXor(final int[] a, final int[] b) {
		final int len = max(a.length, b.length);
		final long[] pa = Transform.fwht(a, len, false);
		final long[] pb = Transform.fwht(b, len, false);
		final int n = pa.length;
		final long[] c = new long[n];
		for (int i = 0; i < n; i++) c[i] = pa[i] * pb[i];
		return Transform.fwht(c, len, true);
	}

	public static long[] convolveAnd(final long[] a, final long[] b, final long mod) {
		final int len = max(a.length, b.length);
		final long[] pa = Transform.supersetZeta(a, len, mod);
		final long[] pb = Transform.supersetZeta(b, len, mod);
		final int n = pa.length;
		final long[] c = new long[n];
		for (int i = 0; i < n; i++) c[i] = (pa[i] * pb[i]) % mod;
		return copyOf(Transform.supersetMobius(c, len, mod), len);
	}

	public static long[] convolveAnd(final long[] a, final long[] b) {
		final int len = max(a.length, b.length);
		final long[] pa = Transform.supersetZeta(a, len);
		final long[] pb = Transform.supersetZeta(b, len);
		final int n = pa.length;
		final long[] c = new long[n];
		for (int i = 0; i < n; i++) c[i] = pa[i] * pb[i];
		return copyOf(Transform.supersetMobius(c, len), len);
	}

	public static int[] convolveAnd(final int[] a, final int[] b, final int mod) {
		final int len = max(a.length, b.length);
		final int[] pa = Transform.supersetZeta(a, len, mod);
		final int[] pb = Transform.supersetZeta(b, len, mod);
		final int n = pa.length;
		final int[] c = new int[n];
		for (int i = 0; i < n; i++) c[i] = (int) (((long) pa[i] * pb[i]) % mod);
		return copyOf(Transform.supersetMobius(c, len, mod), len);
	}

	public static long[] convolveAnd(final int[] a, final int[] b) {
		final int len = max(a.length, b.length);
		final long[] pa = Transform.supersetZeta(a, len);
		final long[] pb = Transform.supersetZeta(b, len);
		final int n = pa.length;
		final long[] c = new long[n];
		for (int i = 0; i < n; i++) c[i] = pa[i] * pb[i];
		return copyOf(Transform.supersetMobius(c, len), len);
	}

	public static long[] convolveOr(final long[] a, final long[] b, final long mod) {
		final int len = max(a.length, b.length);
		final long[] pa = Transform.subsetZeta(a, len, mod);
		final long[] pb = Transform.subsetZeta(b, len, mod);
		final int n = pa.length;
		final long[] c = new long[n];
		for (int i = 0; i < n; i++) c[i] = (pa[i] * pb[i]) % mod;
		return copyOf(Transform.subsetMobius(c, len, mod), len);
	}

	public static long[] convolveOr(final long[] a, final long[] b) {
		final int len = max(a.length, b.length);
		final long[] pa = Transform.subsetZeta(a, len);
		final long[] pb = Transform.subsetZeta(b, len);
		final int n = pa.length;
		final long[] c = new long[n];
		for (int i = 0; i < n; i++) c[i] = pa[i] * pb[i];
		return copyOf(Transform.subsetMobius(c, len), len);
	}

	public static int[] convolveOr(final int[] a, final int[] b, final int mod) {
		final int len = max(a.length, b.length);
		final int[] pa = Transform.subsetZeta(a, len, mod);
		final int[] pb = Transform.subsetZeta(b, len, mod);
		final int n = pa.length;
		final int[] c = new int[n];
		for (int i = 0; i < n; i++) c[i] = (int) (((long) pa[i] * pb[i]) % mod);
		return copyOf(Transform.subsetMobius(c, len, mod), len);
	}

	public static long[] convolveOr(final int[] a, final int[] b) {
		final int len = max(a.length, b.length);
		final long[] pa = Transform.subsetZeta(a, len);
		final long[] pb = Transform.subsetZeta(b, len);
		final int n = pa.length;
		final long[] c = new long[n];
		for (int i = 0; i < n; i++) c[i] = pa[i] * pb[i];
		return copyOf(Transform.subsetMobius(c, len), len);
	}

	public static long[] convolveGcd(final long[] a, final long[] b, final long mod) {
		final int len = max(a.length, b.length);
		final long[] ta = Transform.multipleZeta(a, len, mod);
		final long[] tb = Transform.multipleZeta(b, len, mod);
		final long[] c = new long[len];
		for (int i = 0; i < len; i++) c[i] = (ta[i] * tb[i]) % mod;
		return Transform.multipleMobius(c, len, mod);
	}

	public static long[] convolveGcd(final long[] a, final long[] b) {
		final int len = max(a.length, b.length);
		final long[] ta = Transform.multipleZeta(a, len);
		final long[] tb = Transform.multipleZeta(b, len);
		final long[] c = new long[len];
		for (int i = 0; i < len; i++) c[i] = ta[i] * tb[i];
		return Transform.multipleMobius(c, len);
	}

	public static int[] convolveGcd(final int[] a, final int[] b, final int mod) {
		final int len = max(a.length, b.length);
		final int[] ta = Transform.multipleZeta(a, len, mod);
		final int[] tb = Transform.multipleZeta(b, len, mod);
		final int[] c = new int[len];
		for (int i = 0; i < len; i++) c[i] = (int) (((long) ta[i] * tb[i]) % mod);
		return Transform.multipleMobius(c, len, mod);
	}

	public static long[] convolveGcd(final int[] a, final int[] b) {
		final int len = max(a.length, b.length);
		final long[] ta = Transform.multipleZeta(a, len);
		final long[] tb = Transform.multipleZeta(b, len);
		final long[] c = new long[len];
		for (int i = 0; i < len; i++) c[i] = ta[i] * tb[i];
		return Transform.multipleMobius(c, len);
	}

	public static long[] convolveLcm(final long[] a, final long[] b, final long mod) {
		final int len = max(a.length, b.length);
		final long[] ta = Transform.divisorZeta(a, len, mod);
		final long[] tb = Transform.divisorZeta(b, len, mod);
		final long[] c = new long[len];
		for (int i = 0; i < len; i++) c[i] = (ta[i] * tb[i]) % mod;
		return Transform.divisorMobius(c, len, mod);
	}

	public static long[] convolveLcm(final long[] a, final long[] b) {
		final int len = max(a.length, b.length);
		final long[] ta = Transform.divisorZeta(a, len);
		final long[] tb = Transform.divisorZeta(b, len);
		final long[] c = new long[len];
		for (int i = 0; i < len; i++) c[i] = ta[i] * tb[i];
		return Transform.divisorMobius(c, len);
	}

	public static int[] convolveLcm(final int[] a, final int[] b, final int mod) {
		final int len = max(a.length, b.length);
		final int[] ta = Transform.divisorZeta(a, len, mod);
		final int[] tb = Transform.divisorZeta(b, len, mod);
		final int[] c = new int[len];
		for (int i = 0; i < len; i++) c[i] = (int) (((long) ta[i] * tb[i]) % mod);
		return Transform.divisorMobius(c, len, mod);
	}

	public static long[] convolveLcm(final int[] a, final int[] b) {
		final int len = max(a.length, b.length);
		final long[] ta = Transform.divisorZeta(a, len);
		final long[] tb = Transform.divisorZeta(b, len);
		final long[] c = new long[len];
		for (int i = 0; i < len; i++) c[i] = ta[i] * tb[i];
		return Transform.divisorMobius(c, len);
	}

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
}
