package lib.util;

import static java.lang.Math.*;

public final class DivideConquer {

	private DivideConquer() {
	}

	/**
	 * {@code i < j && a_i > a_j} を満たす転倒数を数える予定のメソッドです。
	 *
	 * <p>現在は未実装で、入力にかかわらず {@code 0} を返します。</p>
	 */
	public static long inversionCount(final int[] a, final int n) {
		// TODO: マージソートを用いた O(N log N) の転倒数計算へ置き換える
		for (int b = (int) sqrt(n); b < n; b *= 2) {
			for (int i = 0; i < n; i += b) {
				for (int j = i; j < min(i + b, n); j++) {

				}
			}
		}
		return 0;
	}


}
