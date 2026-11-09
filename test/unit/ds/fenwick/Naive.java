package unit.ds.fenwick;

/**
 * fenwick のテストで照合に使う愚直解。区間はすべて閉区間で、{@code i > j} なら0を返す。
 */
final class Naive {

	private Naive() {
	}

	static long sum(final long[] a, final int i, final int j) {
		long s = 0;
		for (int k = i; k <= j; k++) s += a[k];
		return s;
	}

	static long sum(final long[][] a, final int i1, final int j1, final int i2, final int j2) {
		long s = 0;
		for (int i = i1; i <= i2; i++) for (int j = j1; j <= j2; j++) s += a[i][j];
		return s;
	}

	/**
	 * 累積和が w 以上になる最初の添字。なければ要素数。
	 */
	static int lowerBound(final long[] a, final long w) {
		long s = 0;
		for (int k = 0; k < a.length; k++) {
			s += a[k];
			if (s >= w) return k;
		}
		return a.length;
	}

	/**
	 * 累積和が w を超える最初の添字。なければ要素数。
	 */
	static int upperBound(final long[] a, final long w) {
		long s = 0;
		for (int k = 0; k < a.length; k++) {
			s += a[k];
			if (s > w) return k;
		}
		return a.length;
	}
}
