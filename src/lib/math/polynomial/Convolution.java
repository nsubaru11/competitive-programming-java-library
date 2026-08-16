package lib.math.polynomial;

import static java.lang.Math.*;
import static java.util.Arrays.*;

@SuppressWarnings("unused")
public final class Convolution {
	private Convolution() {
	}

	public static long[] multiplyNtt(final long[] a, final long[] b, final long mod) {
		int len = a.length + b.length - 1;
		int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1;
		long[] pa = new long[n];
		long[] pb = new long[n];
		System.arraycopy(a, 0, pa, 0, a.length);
		System.arraycopy(b, 0, pb, 0, b.length);
		Transform.ntt(pa, false, mod);
		Transform.ntt(pb, false, mod);
		long[] c = new long[n];
		for (int i = 0; i < n; i++) {
			c[i] = (pa[i] * pb[i]) % mod;
		}
		Transform.ntt(c, true, mod);
		return copyOf(c, len);
	}

	public static int[] multiplyNtt(final int[] a, final int[] b, final int mod) {
		int len = a.length + b.length - 1;
		int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1;
		int[] pa = new int[n];
		int[] pb = new int[n];
		System.arraycopy(a, 0, pa, 0, a.length);
		System.arraycopy(b, 0, pb, 0, b.length);
		Transform.ntt(pa, false, mod);
		Transform.ntt(pb, false, mod);
		int[] c = new int[n];
		for (int i = 0; i < n; i++) {
			c[i] = (int) (((long) pa[i] * pb[i]) % mod);
		}
		Transform.ntt(c, true, mod);
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
		Transform.fft(par, pai, false);
		Transform.fft(pbr, pbi, false);
		double[] cr = new double[n], ci = new double[n];
		for (int i = 0; i < n; i++) {
			cr[i] = par[i] * pbr[i] - pai[i] * pbi[i];
			ci[i] = par[i] * pbi[i] + pai[i] * pbr[i];
		}
		Transform.fft(cr, ci, true);
		return copyOf(cr, len);
	}

	public static long[] convoluteXor(final long[] a, final long[] b, final long mod) {
		int len = max(a.length, b.length);
		int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1;
		long[] pa = new long[n];
		long[] pb = new long[n];
		System.arraycopy(a, 0, pa, 0, a.length);
		System.arraycopy(b, 0, pb, 0, b.length);
		Transform.fwht(pa, false, mod);
		Transform.fwht(pb, false, mod);
		long[] c = new long[n];
		for (int i = 0; i < n; i++) {
			c[i] = (pa[i] * pb[i]) % mod;
		}
		Transform.fwht(c, true, mod);
		return copyOf(c, len);
	}

	public static long[] convoluteXor(final long[] a, final long[] b) {
		int len = max(a.length, b.length);
		int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1;
		long[] pa = new long[n];
		long[] pb = new long[n];
		System.arraycopy(a, 0, pa, 0, a.length);
		System.arraycopy(b, 0, pb, 0, b.length);
		Transform.fwht(pa, false);
		Transform.fwht(pb, false);
		long[] c = new long[n];
		for (int i = 0; i < n; i++) {
			c[i] = pa[i] * pb[i];
		}
		Transform.fwht(c, true);
		return copyOf(c, len);
	}

	public static int[] convoluteXor(final int[] a, final int[] b, final int mod) {
		int len = max(a.length, b.length);
		int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1;
		int[] pa = new int[n];
		int[] pb = new int[n];
		System.arraycopy(a, 0, pa, 0, a.length);
		System.arraycopy(b, 0, pb, 0, b.length);
		Transform.fwht(pa, false, mod);
		Transform.fwht(pb, false, mod);
		int[] c = new int[n];
		for (int i = 0; i < n; i++) {
			c[i] = (int) (((long) pa[i] * pb[i]) % mod);
		}
		Transform.fwht(c, true, mod);
		return copyOf(c, len);
	}

	public static long[] convoluteXor(final int[] a, final int[] b) {
		int len = max(a.length, b.length);
		int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1;
		int[] pa = new int[n];
		int[] pb = new int[n];
		System.arraycopy(a, 0, pa, 0, a.length);
		System.arraycopy(b, 0, pb, 0, b.length);
		Transform.fwht(pa, false);
		Transform.fwht(pb, false);
		long[] c = new long[n];
		for (int i = 0; i < n; i++) {
			c[i] = (long) pa[i] * pb[i];
		}
		Transform.fwht(c, true);
		return copyOf(c, len);
	}

	public static long[] convoluteAnd(final long[] a, final long[] b, final long mod) {
		int len = max(a.length, b.length);
		int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1;
		long[] pa = new long[n];
		long[] pb = new long[n];
		System.arraycopy(a, 0, pa, 0, a.length);
		System.arraycopy(b, 0, pb, 0, b.length);
		Transform.supersetZeta(pa, mod);
		Transform.supersetZeta(pb, mod);
		long[] c = new long[n];
		for (int i = 0; i < n; i++) {
			c[i] = (pa[i] * pb[i]) % mod;
		}
		Transform.supersetMobius(c, mod);
		return copyOf(c, len);
	}

	public static long[] convoluteAnd(final long[] a, final long[] b) {
		int len = max(a.length, b.length);
		int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1;
		long[] pa = new long[n];
		long[] pb = new long[n];
		System.arraycopy(a, 0, pa, 0, a.length);
		System.arraycopy(b, 0, pb, 0, b.length);
		Transform.supersetZeta(pa);
		Transform.supersetZeta(pb);
		long[] c = new long[n];
		for (int i = 0; i < n; i++) {
			c[i] = pa[i] * pb[i];
		}
		Transform.supersetMobius(c);
		return copyOf(c, len);
	}

	public static int[] convoluteAnd(final int[] a, final int[] b, final int mod) {
		int len = max(a.length, b.length);
		int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1;
		int[] pa = new int[n];
		int[] pb = new int[n];
		System.arraycopy(a, 0, pa, 0, a.length);
		System.arraycopy(b, 0, pb, 0, b.length);
		Transform.supersetZeta(pa, mod);
		Transform.supersetZeta(pb, mod);
		int[] c = new int[n];
		for (int i = 0; i < n; i++) {
			c[i] = (int) (((long) pa[i] * pb[i]) % mod);
		}
		Transform.supersetMobius(c, mod);
		return copyOf(c, len);
	}

	public static long[] convoluteAnd(final int[] a, final int[] b) {
		int len = max(a.length, b.length);
		int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1;
		int[] pa = new int[n];
		int[] pb = new int[n];
		System.arraycopy(a, 0, pa, 0, a.length);
		System.arraycopy(b, 0, pb, 0, b.length);
		Transform.supersetZeta(pa);
		Transform.supersetZeta(pb);
		long[] c = new long[n];
		for (int i = 0; i < n; i++) {
			c[i] = (long) pa[i] * pb[i];
		}
		Transform.supersetMobius(c);
		return copyOf(c, len);
	}

	public static long[] convoluteOr(final long[] a, final long[] b, final long mod) {
		int len = max(a.length, b.length);
		int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1;
		long[] pa = new long[n];
		long[] pb = new long[n];
		System.arraycopy(a, 0, pa, 0, a.length);
		System.arraycopy(b, 0, pb, 0, b.length);
		Transform.subsetZeta(pa, mod);
		Transform.subsetZeta(pb, mod);
		long[] c = new long[n];
		for (int i = 0; i < n; i++) {
			c[i] = (pa[i] * pb[i]) % mod;
		}
		Transform.subsetMobius(c, mod);
		return copyOf(c, len);
	}

	public static long[] convoluteOr(final long[] a, final long[] b) {
		int len = max(a.length, b.length);
		int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1;
		long[] pa = new long[n];
		long[] pb = new long[n];
		System.arraycopy(a, 0, pa, 0, a.length);
		System.arraycopy(b, 0, pb, 0, b.length);
		Transform.subsetZeta(pa);
		Transform.subsetZeta(pb);
		long[] c = new long[n];
		for (int i = 0; i < n; i++) {
			c[i] = pa[i] * pb[i];
		}
		Transform.subsetMobius(c);
		return copyOf(c, len);
	}

	public static int[] convoluteOr(final int[] a, final int[] b, final int mod) {
		int len = max(a.length, b.length);
		int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1;
		int[] pa = new int[n];
		int[] pb = new int[n];
		System.arraycopy(a, 0, pa, 0, a.length);
		System.arraycopy(b, 0, pb, 0, b.length);
		Transform.subsetZeta(pa, mod);
		Transform.subsetZeta(pb, mod);
		int[] c = new int[n];
		for (int i = 0; i < n; i++) {
			c[i] = (int) (((long) pa[i] * pb[i]) % mod);
		}
		Transform.subsetMobius(c, mod);
		return copyOf(c, len);
	}

	public static long[] convoluteOr(final int[] a, final int[] b) {
		int len = max(a.length, b.length);
		int n = len <= 1 ? 1 : Integer.highestOneBit(len - 1) << 1;
		int[] pa = new int[n];
		int[] pb = new int[n];
		System.arraycopy(a, 0, pa, 0, a.length);
		System.arraycopy(b, 0, pb, 0, b.length);
		Transform.subsetZeta(pa);
		Transform.subsetZeta(pb);
		long[] c = new long[n];
		for (int i = 0; i < n; i++) {
			c[i] = (long) pa[i] * pb[i];
		}
		Transform.subsetMobius(c);
		return copyOf(c, len);
	}

	public static long[] convoluteGcd(final long[] a, final long[] b, final long mod) {
		int n = max(a.length, b.length);
		long[] pa = new long[n];
		long[] pb = new long[n];
		System.arraycopy(a, 0, pa, 0, a.length);
		System.arraycopy(b, 0, pb, 0, b.length);
		Transform.multipleZeta(pa, mod);
		Transform.multipleZeta(pb, mod);
		long[] c = new long[n];
		for (int i = 0; i < n; i++) {
			c[i] = (pa[i] * pb[i]) % mod;
		}
		Transform.multipleMobius(c, mod);
		return c;
	}

	public static long[] convoluteGcd(final long[] a, final long[] b) {
		int n = max(a.length, b.length);
		long[] pa = new long[n];
		long[] pb = new long[n];
		System.arraycopy(a, 0, pa, 0, a.length);
		System.arraycopy(b, 0, pb, 0, b.length);
		Transform.multipleZeta(pa);
		Transform.multipleZeta(pb);
		long[] c = new long[n];
		for (int i = 0; i < n; i++) {
			c[i] = pa[i] * pb[i];
		}
		Transform.multipleMobius(c);
		return c;
	}

	public static int[] convoluteGcd(final int[] a, final int[] b, final int mod) {
		int n = max(a.length, b.length);
		int[] pa = new int[n];
		int[] pb = new int[n];
		System.arraycopy(a, 0, pa, 0, a.length);
		System.arraycopy(b, 0, pb, 0, b.length);
		Transform.multipleZeta(pa, mod);
		Transform.multipleZeta(pb, mod);
		int[] c = new int[n];
		for (int i = 0; i < n; i++) {
			c[i] = (int) (((long) pa[i] * pb[i]) % mod);
		}
		Transform.multipleMobius(c, mod);
		return c;
	}

	public static long[] convoluteGcd(final int[] a, final int[] b) {
		int n = max(a.length, b.length);
		int[] pa = new int[n];
		int[] pb = new int[n];
		System.arraycopy(a, 0, pa, 0, a.length);
		System.arraycopy(b, 0, pb, 0, b.length);
		Transform.multipleZeta(pa);
		Transform.multipleZeta(pb);
		long[] c = new long[n];
		for (int i = 0; i < n; i++) {
			c[i] = (long) pa[i] * pb[i];
		}
		Transform.multipleMobius(c);
		return c;
	}

	public static long[] convoluteLcm(final long[] a, final long[] b, final long mod) {
		int n = max(a.length, b.length);
		long[] pa = new long[n];
		long[] pb = new long[n];
		System.arraycopy(a, 0, pa, 0, a.length);
		System.arraycopy(b, 0, pb, 0, b.length);
		Transform.divisorZeta(pa, mod);
		Transform.divisorZeta(pb, mod);
		long[] c = new long[n];
		for (int i = 0; i < n; i++) {
			c[i] = (pa[i] * pb[i]) % mod;
		}
		Transform.divisorMobius(c, mod);
		return c;
	}

	public static long[] convoluteLcm(final long[] a, final long[] b) {
		int n = max(a.length, b.length);
		long[] pa = new long[n];
		long[] pb = new long[n];
		System.arraycopy(a, 0, pa, 0, a.length);
		System.arraycopy(b, 0, pb, 0, b.length);
		Transform.divisorZeta(pa);
		Transform.divisorZeta(pb);
		long[] c = new long[n];
		for (int i = 0; i < n; i++) {
			c[i] = pa[i] * pb[i];
		}
		Transform.divisorMobius(c);
		return c;
	}

	public static int[] convoluteLcm(final int[] a, final int[] b, final int mod) {
		int n = max(a.length, b.length);
		int[] pa = new int[n];
		int[] pb = new int[n];
		System.arraycopy(a, 0, pa, 0, a.length);
		System.arraycopy(b, 0, pb, 0, b.length);
		Transform.divisorZeta(pa, mod);
		Transform.divisorZeta(pb, mod);
		int[] c = new int[n];
		for (int i = 0; i < n; i++) {
			c[i] = (int) (((long) pa[i] * pb[i]) % mod);
		}
		Transform.divisorMobius(c, mod);
		return c;
	}

	public static long[] convoluteLcm(final int[] a, final int[] b) {
		int n = max(a.length, b.length);
		int[] pa = new int[n];
		int[] pb = new int[n];
		System.arraycopy(a, 0, pa, 0, a.length);
		System.arraycopy(b, 0, pb, 0, b.length);
		Transform.divisorZeta(pa);
		Transform.divisorZeta(pb);
		long[] c = new long[n];
		for (int i = 0; i < n; i++) {
			c[i] = (long) pa[i] * pb[i];
		}
		Transform.divisorMobius(c);
		return c;
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
