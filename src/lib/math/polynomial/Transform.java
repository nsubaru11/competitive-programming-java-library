package lib.math.polynomial;

public final class Transform {
	private Transform() {}

	// TODO: 以下の内部変換ロジックはすべて未実装。実装完了まで公開メソッドは正しい結果を返さない
	public static void ntt(final long[] a, final boolean isInverse, final long mod) { /* TODO: NTT（数論変換）の実装 */ }

	public static void ntt(final int[] a, final boolean isInverse, final int mod) { /* TODO: NTT（数論変換）の実装 */ }

	public static void fft(final double[] real, final double[] imag, final boolean isInverse) { /* TODO: FFT（高速フーリエ変換）の実装 */ }

	public static void fwht(final long[] a, final boolean isInverse, final long mod) { /* TODO: FWHT（高速ウォルシュ・アダマール変換）の実装 */ }

	public static void fwht(final long[] a, final boolean isInverse) { /* TODO: FWHT（高速ウォルシュ・アダマール変換）の実装 */ }

	public static void fwht(final int[] a, final boolean isInverse, final int mod) { /* TODO: FWHT（高速ウォルシュ・アダマール変換）の実装 */ }

	public static void fwht(final int[] a, final boolean isInverse) { /* TODO: FWHT（高速ウォルシュ・アダマール変換）の実装 */ }

	public static void subsetZeta(final long[] a, final long mod) {
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

	public static void subsetZeta(final long[] a) {
		final int n = a.length;
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l + i; t < l + j; t++) {
					a[t] += a[t - i];
				}
			}
		}
	}

	public static void subsetZeta(final int[] a, final int mod) {
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

	public static void subsetZeta(final int[] a) {
		final int n = a.length;
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l + i; t < l + j; t++) {
					a[t] += a[t - i];
				}
			}
		}
	}

	public static void subsetMobius(final long[] a, final long mod) {
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

	public static void subsetMobius(final long[] a) {
		final int n = a.length;
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l + i; t < l + j; t++) {
					a[t] -= a[t - i];
				}
			}
		}
	}

	public static void subsetMobius(final int[] a, final int mod) {
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

	public static void subsetMobius(final int[] a) {
		final int n = a.length;
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l + i; t < l + j; t++) {
					a[t] -= a[t - i];
				}
			}
		}
	}

	public static void supersetZeta(final long[] a, final long mod) {
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

	public static void supersetZeta(final long[] a) {
		final int n = a.length;
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l; t < l + i; t++) {
					a[t] += a[t + i];
				}
			}
		}
	}

	public static void supersetZeta(final int[] a, final int mod) {
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

	public static void supersetZeta(final int[] a) {
		final int n = a.length;
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l; t < l + i; t++) {
					a[t] += a[t + i];
				}
			}
		}
	}

	public static void supersetMobius(final long[] a, final long mod) {
		final int n = a.length;
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l; t < l + i; t++) {
					a[t] = (a[t] - a[t + i]) % mod;
				}
			}
		}
		for (int i = 0; i < n; i++) if (a[i] < 0) a[i] += mod;
	}

	public static void supersetMobius(final long[] a) {
		final int n = a.length;
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l; t < l + i; t++) {
					a[t] -= a[t + i];
				}
			}
		}
	}

	public static void supersetMobius(final int[] a, final int mod) {
		final int n = a.length;
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l; t < l + i; t++) {
					a[t] = (a[t] - a[t + i]) % mod;
				}
			}
		}
		for (int i = 0; i < n; i++) if (a[i] < 0) a[i] += mod;
	}

	public static void supersetMobius(final int[] a) {
		final int n = a.length;
		for (int i = 1; i < n; i <<= 1) {
			final int j = i << 1;
			for (int l = 0; l < n; l += j) {
				for (int t = l; t < l + i; t++) {
					a[t] -= a[t + i];
				}
			}
		}
	}

	public static void multipleZeta(final long[] a, final long mod) { /* TODO: 倍数ゼータ変換の実装 */ }

	public static void multipleZeta(final long[] a) { /* TODO: 倍数ゼータ変換の実装 */ }

	public static void multipleZeta(final int[] a, final int mod) { /* TODO: 倍数ゼータ変換の実装 */ }

	public static void multipleZeta(final int[] a) { /* TODO: 倍数ゼータ変換の実装 */ }

	public static void multipleMobius(final long[] a, final long mod) { /* TODO: 倍数メビウス変換の実装 */ }

	public static void multipleMobius(final long[] a) { /* TODO: 倍数メビウス変換の実装 */ }

	public static void multipleMobius(final int[] a, final int mod) { /* TODO: 倍数メビウス変換の実装 */ }

	public static void multipleMobius(final int[] a) { /* TODO: 倍数メビウス変換の実装 */ }

	public static void divisorZeta(final long[] a, final long mod) { /* TODO: 約数ゼータ変換の実装 */ }

	public static void divisorZeta(final long[] a) { /* TODO: 約数ゼータ変換の実装 */ }

	public static void divisorZeta(final int[] a, final int mod) { /* TODO: 約数ゼータ変換の実装 */ }

	public static void divisorZeta(final int[] a) { /* TODO: 約数ゼータ変換の実装 */ }

	public static void divisorMobius(final long[] a, final long mod) { /* TODO: 約数メビウス変換の実装 */ }

	public static void divisorMobius(final long[] a) { /* TODO: 約数メビウス変換の実装 */ }

	public static void divisorMobius(final int[] a, final int mod) { /* TODO: 約数メビウス変換の実装 */ }

	public static void divisorMobius(final int[] a) { /* TODO: 約数メビウス変換の実装 */ }
}
