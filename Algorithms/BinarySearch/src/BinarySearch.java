/**
 * 条件関数ベースの二分探索ユーティリティです。
 * int/long 範囲に対する通常探索・下限探索・上限探索を提供します。
 * 探索失敗時は -(挿入位置 + 1) を返します。
 */
@SuppressWarnings("unused")
public final class BinarySearch {

	private BinarySearch() {
		throw new UnsupportedOperationException("このクラスはインスタンス化できません");
	}

	// region normalSearch
	public static int normalSearch(final int l, final int r, final CompareFunction comparator) {
		return (int) binarySearchNormal(l, r - 1, comparator);
	}

	public static long normalSearch(final long l, final long r, final CompareFunction comparator) {
		return binarySearchNormal(l, r - 1, comparator);
	}
	// endregion

	// region upperBoundSearch
	public static int upperBoundSearch(final int l, final int r, final CompareFunction comparator) {
		return (int) binarySearchUpperBound(l, r - 1, comparator);
	}

	public static long upperBoundSearch(final long l, final long r, final CompareFunction comparator) {
		return binarySearchUpperBound(l, r - 1, comparator);
	}
	// endregion

	// region lowerBoundSearch
	public static int lowerBoundSearch(final int l, final int r, final CompareFunction comparator) {
		return (int) binarySearchLowerBound(l, r - 1, comparator);
	}

	public static long lowerBoundSearch(final long l, final long r, final CompareFunction comparator) {
		return binarySearchLowerBound(l, r - 1, comparator);
	}
	// endregion

	// region binarySearch
	private static long binarySearchNormal(long l, long r, final CompareFunction comparator) {
		while (l <= r) {
			final long m = (l & r) + ((l ^ r) >> 1);
			final int c = comparator.compare(m);
			if (c > 0) r = m - 1;
			else if (c < 0) l = m + 1;
			else return m;
		}
		return ~l;
	}

	private static long binarySearchUpperBound(long l, long r, final CompareFunction comparator) {
		long ans = l - 1;
		while (l <= r) {
			final long m = (l & r) + ((l ^ r) >> 1);
			final int c = comparator.compare(m);
			if (c > 0) {
				r = m - 1;
			} else {
				if (c == 0) ans = m;
				l = m + 1;
			}
		}
		return ans != l - 1 ? ans : ~l;
	}

	private static long binarySearchLowerBound(long l, long r, final CompareFunction comparator) {
		long ans = l - 1;
		while (l <= r) {
			final long m = (l & r) + ((l ^ r) >> 1);
			final int c = comparator.compare(m);
			if (c < 0) {
				l = m + 1;
			} else {
				if (c == 0) ans = m;
				r = m - 1;
			}
		}
		return ans != l - 1 ? ans : ~l;
	}
	// endregion

	@FunctionalInterface
	public interface CompareFunction {
		int compare(long index);
	}
}
