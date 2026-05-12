import static java.lang.Math.*;
import static java.util.Arrays.*;

@SuppressWarnings("unused")
public final class Convolution {
	private Convolution() {
	}

	// region public convolution methods
	public static long[] multiplyNtt(long[] a, long[] b, int mod) {
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

	public static long[] multiplyArbitraryMod(long[] a, long[] b, int mod) {
		long[] res1 = multiplyNtt(a, b, 167772161);
		long[] res2 = multiplyNtt(a, b, 469762049);
		long[] res3 = multiplyNtt(a, b, 1224736769);
		return garnerProcess(res1, res2, res3, mod);
	}

	public static double[] multiplyFft(double[] a, double[] b) {
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

	public static long[] convoluteXor(long[] a, long[] b, int mod) {
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

	public static long[] convoluteAnd(long[] a, long[] b, int mod) {
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

	public static long[] convoluteOr(long[] a, long[] b, int mod) {
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

	public static long[] convoluteGcd(long[] a, long[] b, int mod) {
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

	public static long[] convoluteLcm(long[] a, long[] b, int mod) {
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
	// endregion

	// region private transform methods
	private static void transformNtt(long[] a, boolean isInverse, int mod) { /* ... */ }

	private static void transformFft(double[] real, double[] imag, boolean isInverse) { /* ... */ }

	private static void transformFwht(long[] a, boolean isInverse, int mod) { /* ... */ }

	private static void transformSubsetZeta(long[] a, int mod) { /* ... */ }

	private static void transformSubsetMobius(long[] a, int mod) { /* ... */ }

	private static void transformSupersetZeta(long[] a, int mod) { /* ... */ }

	private static void transformSupersetMobius(long[] a, int mod) { /* ... */ }

	private static void transformMultipleZeta(long[] a, int mod) { /* ... */ }

	private static void transformMultipleMobius(long[] a, int mod) { /* ... */ }

	private static void transformDivisorZeta(long[] a, int mod) { /* ... */ }

	private static void transformDivisorMobius(long[] a, int mod) { /* ... */ }
	// endregion

	// region private helper methods
	private static long[] garnerProcess(long[] a, long[] b, long[] c, int mod) {
		long[] d = new long[a.length];
		return d;
	}
	// endregion
}
