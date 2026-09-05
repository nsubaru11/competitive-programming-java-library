package lib.math.polynomial;

import static java.lang.Math.*;
import static java.util.Arrays.*;

@SuppressWarnings("unused")
public final class Convolution {
	private static final int NTT_MOD1 = 167772161, NTT_MOD2 = 469762049, NTT_MOD3 = 1224736769;
	private static final long NTT_MOD1_MOD2 = 78812994116517889L, NTT_MOD1_INVERSE_MOD2 = 104391568, NTT_MOD1_MOD2_INVERSE_MOD3 = 721017874;

	private Convolution() {
	}

	public static int[] convolveNtt(final int[] a, final int[] b, final int mod) {
		final int len = a.length + b.length - 1;
		final int[] pa = Transform.ntt(a, len, false, mod);
		final int[] pb = Transform.ntt(b, len, false, mod);
		final int n = pa.length;
		for (int i = 0; i < n; i++) pa[i] = (int) (((long) pa[i] * pb[i]) % mod);
		Transform.nttInPlace(pa, true, mod);
		return len == n ? pa : copyOf(pa, len);
	}

	public static long[] convolveNtt(final long[] a, final long[] b, final long mod) {
		final int len = a.length + b.length - 1;
		final long[] pa = Transform.ntt(a, len, false, mod);
		final long[] pb = Transform.ntt(b, len, false, mod);
		final int n = pa.length;
		for (int i = 0; i < n; i++) pa[i] = (pa[i] * pb[i]) % mod;
		Transform.nttInPlace(pa, true, mod);
		return len == n ? pa : copyOf(pa, len);
	}

	public static int[] convolveArbitraryMod(final int[] a, final int[] b, final int mod) {
		final int[] res1 = convolveNtt(remainder(a, NTT_MOD1), remainder(b, NTT_MOD1), NTT_MOD1);
		final int[] res2 = convolveNtt(remainder(a, NTT_MOD2), remainder(b, NTT_MOD2), NTT_MOD2);
		final int[] res3 = convolveNtt(remainder(a, NTT_MOD3), remainder(b, NTT_MOD3), NTT_MOD3);
		return garnerProcess(res1, res2, res3, mod);
	}

	public static long[] convolveArbitraryMod(final long[] a, final long[] b, final long mod) {
		final long[] res1 = convolveNtt(remainder(a, NTT_MOD1), remainder(b, NTT_MOD1), NTT_MOD1);
		final long[] res2 = convolveNtt(remainder(a, NTT_MOD2), remainder(b, NTT_MOD2), NTT_MOD2);
		final long[] res3 = convolveNtt(remainder(a, NTT_MOD3), remainder(b, NTT_MOD3), NTT_MOD3);
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
		Transform.fftInPlace(cr, ci, true);
		return copyOf(cr, len);
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
		Transform.fftInPlace(cr, ci, true);
		final long[] res = new long[len];
		for (int i = 0; i < len; i++) res[i] = round(cr[i]);
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
		Transform.fftInPlace(cr, ci, true);
		final long[] res = new long[len];
		for (int i = 0; i < len; i++) res[i] = round(cr[i]);
		return res;
	}

	public static long[] convolveXor(final long[] a, final long[] b, final long mod) {
		final int len = max(a.length, b.length);
		final long[] pa = Transform.fwht(a, len, false, mod);
		final long[] pb = Transform.fwht(b, len, false, mod);
		final int n = pa.length;
		final long[] res = new long[n];
		for (int i = 0; i < n; i++) res[i] = (pa[i] * pb[i]) % mod;
		Transform.fwhtInPlace(res, true, mod);
		return res;
	}

	public static long[] convolveXor(final long[] a, final long[] b) {
		final int len = max(a.length, b.length);
		final long[] pa = Transform.fwht(a, len, false);
		final long[] pb = Transform.fwht(b, len, false);
		final int n = pa.length;
		final long[] res = new long[n];
		for (int i = 0; i < n; i++) res[i] = pa[i] * pb[i];
		Transform.fwhtInPlace(res, true);
		return res;
	}

	public static int[] convolveXor(final int[] a, final int[] b, final int mod) {
		final int len = max(a.length, b.length);
		final int[] pa = Transform.fwht(a, len, false, mod);
		final int[] pb = Transform.fwht(b, len, false, mod);
		final int n = pa.length;
		final int[] res = new int[n];
		for (int i = 0; i < n; i++) res[i] = (int) (((long) pa[i] * pb[i]) % mod);
		Transform.fwhtInPlace(res, true, mod);
		return res;
	}

	public static long[] convolveXor(final int[] a, final int[] b) {
		final int len = max(a.length, b.length);
		final long[] pa = Transform.fwht(a, len, false);
		final long[] pb = Transform.fwht(b, len, false);
		final int n = pa.length;
		final long[] res = new long[n];
		for (int i = 0; i < n; i++) res[i] = pa[i] * pb[i];
		Transform.fwhtInPlace(res, true);
		return res;
	}

	public static long[] convolveAnd(final long[] a, final long[] b, final long mod) {
		final int len = max(a.length, b.length);
		final long[] pa = Transform.supersetZeta(a, len, mod);
		final long[] pb = Transform.supersetZeta(b, len, mod);
		final int n = pa.length;
		final long[] res = new long[n];
		for (int i = 0; i < n; i++) res[i] = (pa[i] * pb[i]) % mod;
		Transform.supersetMobiusInPlace(res, mod);
		return res;
	}

	public static long[] convolveAnd(final long[] a, final long[] b) {
		final int len = max(a.length, b.length);
		final long[] pa = Transform.supersetZeta(a, len);
		final long[] pb = Transform.supersetZeta(b, len);
		final int n = pa.length;
		final long[] c = new long[n];
		for (int i = 0; i < n; i++) c[i] = pa[i] * pb[i];
		Transform.supersetMobiusInPlace(c);
		return c;
	}

	public static int[] convolveAnd(final int[] a, final int[] b, final int mod) {
		final int len = max(a.length, b.length);
		final int[] pa = Transform.supersetZeta(a, len, mod);
		final int[] pb = Transform.supersetZeta(b, len, mod);
		final int n = pa.length;
		final int[] res = new int[n];
		for (int i = 0; i < n; i++) res[i] = (int) (((long) pa[i] * pb[i]) % mod);
		Transform.supersetMobiusInPlace(res, mod);
		return res;
	}

	public static long[] convolveAnd(final int[] a, final int[] b) {
		final int len = max(a.length, b.length);
		final long[] pa = Transform.supersetZeta(a, len);
		final long[] pb = Transform.supersetZeta(b, len);
		final int n = pa.length;
		final long[] res = new long[n];
		for (int i = 0; i < n; i++) res[i] = pa[i] * pb[i];
		Transform.supersetMobiusInPlace(res);
		return res;
	}

	public static long[] convolveOr(final long[] a, final long[] b, final long mod) {
		final int len = max(a.length, b.length);
		final long[] pa = Transform.subsetZeta(a, len, mod);
		final long[] pb = Transform.subsetZeta(b, len, mod);
		final int n = pa.length;
		final long[] res = new long[n];
		for (int i = 0; i < n; i++) res[i] = (pa[i] * pb[i]) % mod;
		Transform.subsetMobiusInPlace(res, mod);
		return res;
	}

	public static long[] convolveOr(final long[] a, final long[] b) {
		final int len = max(a.length, b.length);
		final long[] pa = Transform.subsetZeta(a, len);
		final long[] pb = Transform.subsetZeta(b, len);
		final int n = pa.length;
		final long[] res = new long[n];
		for (int i = 0; i < n; i++) res[i] = pa[i] * pb[i];
		Transform.subsetMobiusInPlace(res);
		return res;
	}

	public static int[] convolveOr(final int[] a, final int[] b, final int mod) {
		final int len = max(a.length, b.length);
		final int[] pa = Transform.subsetZeta(a, len, mod);
		final int[] pb = Transform.subsetZeta(b, len, mod);
		final int n = pa.length;
		final int[] res = new int[n];
		for (int i = 0; i < n; i++) res[i] = (int) (((long) pa[i] * pb[i]) % mod);
		Transform.subsetMobiusInPlace(res, mod);
		return res;
	}

	public static long[] convolveOr(final int[] a, final int[] b) {
		final int len = max(a.length, b.length);
		final long[] pa = Transform.subsetZeta(a, len);
		final long[] pb = Transform.subsetZeta(b, len);
		final int n = pa.length;
		final long[] res = new long[n];
		for (int i = 0; i < n; i++) res[i] = pa[i] * pb[i];
		Transform.subsetMobiusInPlace(res);
		return res;
	}

	public static long[] convolveGcd(final long[] a, final long[] b, final long mod) {
		final int len = max(a.length, b.length);
		final long[] ta = Transform.multipleZeta(a, len, mod);
		final long[] tb = Transform.multipleZeta(b, len, mod);
		for (int i = 0; i < len; i++) ta[i] = (ta[i] * tb[i]) % mod;
		Transform.multipleMobiusInPlace(ta, mod);
		return ta;
	}

	public static long[] convolveGcd(final long[] a, final long[] b) {
		final int len = max(a.length, b.length);
		final long[] ta = Transform.multipleZeta(a, len);
		final long[] tb = Transform.multipleZeta(b, len);
		for (int i = 0; i < len; i++) ta[i] *= tb[i];
		Transform.multipleMobiusInPlace(ta);
		return ta;
	}

	public static int[] convolveGcd(final int[] a, final int[] b, final int mod) {
		final int len = max(a.length, b.length);
		final int[] ta = Transform.multipleZeta(a, len, mod);
		final int[] tb = Transform.multipleZeta(b, len, mod);
		for (int i = 0; i < len; i++) ta[i] = (int) (((long) ta[i] * tb[i]) % mod);
		Transform.multipleMobiusInPlace(ta, mod);
		return ta;
	}

	public static long[] convolveGcd(final int[] a, final int[] b) {
		final int len = max(a.length, b.length);
		final long[] ta = Transform.multipleZeta(a, len);
		final long[] tb = Transform.multipleZeta(b, len);
		for (int i = 0; i < len; i++) ta[i] *= tb[i];
		Transform.multipleMobiusInPlace(ta);
		return ta;
	}

	public static long[] convolveLcm(final long[] a, final long[] b, final long mod) {
		final int len = max(a.length, b.length);
		final long[] ta = Transform.divisorZeta(a, len, mod);
		final long[] tb = Transform.divisorZeta(b, len, mod);
		for (int i = 0; i < len; i++) ta[i] = (ta[i] * tb[i]) % mod;
		Transform.divisorMobiusInPlace(ta, mod);
		return ta;
	}

	public static long[] convolveLcm(final long[] a, final long[] b) {
		final int len = max(a.length, b.length);
		final long[] ta = Transform.divisorZeta(a, len);
		final long[] tb = Transform.divisorZeta(b, len);
		for (int i = 0; i < len; i++) ta[i] *= tb[i];
		Transform.divisorMobiusInPlace(ta);
		return ta;
	}

	public static int[] convolveLcm(final int[] a, final int[] b, final int mod) {
		final int len = max(a.length, b.length);
		final int[] ta = Transform.divisorZeta(a, len, mod);
		final int[] tb = Transform.divisorZeta(b, len, mod);
		for (int i = 0; i < len; i++) ta[i] = (int) ((long) ta[i] * tb[i] % mod);
		Transform.divisorMobiusInPlace(ta, mod);
		return ta;
	}

	public static long[] convolveLcm(final int[] a, final int[] b) {
		final int len = max(a.length, b.length);
		final long[] ta = Transform.divisorZeta(a, len);
		final long[] tb = Transform.divisorZeta(b, len);
		for (int i = 0; i < len; i++) ta[i] *= tb[i];
		Transform.divisorMobiusInPlace(ta);
		return ta;
	}

	private static long[] garnerProcess(final long[] a, final long[] b, final long[] c, final long mod) {
		final int n = a.length;
		final long[] d = new long[n];
		final long mod1 = NTT_MOD1 % mod, mod12 = NTT_MOD1_MOD2 % mod;
		for (int i = 0; i < n; i++) {
			final long x = (b[i] - a[i] + NTT_MOD2) * NTT_MOD1_INVERSE_MOD2 % NTT_MOD2;
			final long y = (c[i] - (a[i] + NTT_MOD1 * x) % NTT_MOD3 + NTT_MOD3) * NTT_MOD1_MOD2_INVERSE_MOD3 % NTT_MOD3;
			d[i] = (a[i] % mod + mod1 * x % mod + mod12 * y % mod) % mod;
		}
		return d;
	}

	private static int[] garnerProcess(final int[] a, final int[] b, final int[] c, final int mod) {
		final int n = a.length;
		final int[] d = new int[n];
		final long mod1 = NTT_MOD1 % mod, mod12 = NTT_MOD1_MOD2 % mod;
		for (int i = 0; i < n; i++) {
			final long x = (long) (b[i] - a[i] + NTT_MOD2) * NTT_MOD1_INVERSE_MOD2 % NTT_MOD2;
			final long y = (c[i] - (a[i] + NTT_MOD1 * x) % NTT_MOD3 + NTT_MOD3) * NTT_MOD1_MOD2_INVERSE_MOD3 % NTT_MOD3;
			d[i] = (int) ((a[i] + mod1 * x % mod + mod12 * y % mod) % mod);
		}
		return d;
	}

	private static int[] remainder(final int[] a, final int mod) {
		final int[] res = copyOf(a, a.length);
		for (int i = 0; i < res.length; i++) res[i] %= mod;
		return res;
	}

	private static long[] remainder(final long[] a, final long mod) {
		final long[] res = copyOf(a, a.length);
		for (int i = 0; i < res.length; i++) res[i] %= mod;
		return res;
	}
}
