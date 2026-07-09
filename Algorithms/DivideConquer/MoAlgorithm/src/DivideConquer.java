import static java.lang.Math.*;
import static java.lang.Math.sqrt;

public final class DivideConquer {

	private DivideConquer() {
	}

	/**
	 * i < j && a_i > a_j
	 */
	public static long inversionCount(final int[] a, final int n) {
		for (int b = (int) sqrt(n); b < n; b *= 2) {
			for (int i = 0; i < n; i += b) {
				for (int j = i; j < min(i + b, n); j++) {

				}
			}
		}
		return 0;
	}


}
