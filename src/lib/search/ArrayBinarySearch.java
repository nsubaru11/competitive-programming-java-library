package lib.search;

import static java.util.Arrays.*;

import lib.ds.arrays.*;

/**
 * 昇順ソート済み配列向けの二分探索ユーティリティです。
 * プリミティブ配列、Comparable配列、IntArray、LongArrayに対して
 * 下限探索・上限探索・包含判定・カウントを提供します。
 * 探索失敗時は -(挿入位置 + 1) を返します。
 */
@SuppressWarnings("unused")
public final class ArrayBinarySearch {

	private ArrayBinarySearch() {
	}

	// region int[]
	public static int lowerBoundSearch(final int[] arr, final int target) {
		return binarySearchLowerBound(arr, 0, arr.length - 1, target);
	}

	public static int upperBoundSearch(final int[] arr, final int target) {
		return binarySearchUpperBound(arr, 0, arr.length - 1, target);
	}

	public static boolean contains(final int[] arr, final int target) {
		return binarySearch(arr, 0, arr.length, target) >= 0;
	}

	public static int count(final int[] arr, final int target) {
		final int up = upperBoundSearch(arr, target);
		if (up < 0) return 0;
		return up - lowerBoundSearch(arr, target) + 1;
	}

	public static int lowerBoundSearch(final int[] arr, final int l, final int r, final int target) {
		return binarySearchLowerBound(arr, l, r - 1, target);
	}

	public static int upperBoundSearch(final int[] arr, final int l, final int r, final int target) {
		return binarySearchUpperBound(arr, l, r - 1, target);
	}

	public static boolean contains(final int[] arr, final int l, final int r, final int target) {
		return binarySearch(arr, l, r, target) >= 0;
	}

	public static int count(final int[] arr, final int l, final int r, final int target) {
		final int up = upperBoundSearch(arr, l, r, target);
		if (up < 0) return 0;
		return up - lowerBoundSearch(arr, l, r, target) + 1;
	}
	// endregion

	// region long[]
	public static int lowerBoundSearch(final long[] arr, final long target) {
		return binarySearchLowerBound(arr, 0, arr.length - 1, target);
	}

	public static int upperBoundSearch(final long[] arr, final long target) {
		return binarySearchUpperBound(arr, 0, arr.length - 1, target);
	}

	public static boolean contains(final long[] arr, final long target) {
		return binarySearch(arr, 0, arr.length, target) >= 0;
	}

	public static int count(final long[] arr, final long target) {
		final int up = upperBoundSearch(arr, target);
		if (up < 0) return 0;
		return up - lowerBoundSearch(arr, target) + 1;
	}

	public static int lowerBoundSearch(final long[] arr, final int l, final int r, final long target) {
		return binarySearchLowerBound(arr, l, r - 1, target);
	}

	public static int upperBoundSearch(final long[] arr, final int l, final int r, final long target) {
		return binarySearchUpperBound(arr, l, r - 1, target);
	}

	public static boolean contains(final long[] arr, final int l, final int r, final long target) {
		return binarySearch(arr, l, r, target) >= 0;
	}

	public static int count(final long[] arr, final int l, final int r, final long target) {
		final int up = upperBoundSearch(arr, l, r, target);
		if (up < 0) return 0;
		return up - lowerBoundSearch(arr, l, r, target) + 1;
	}
	// endregion

	// region double[]
	public static int lowerBoundSearch(final double[] arr, final double target) {
		return binarySearchLowerBound(arr, 0, arr.length - 1, target);
	}

	public static int upperBoundSearch(final double[] arr, final double target) {
		return binarySearchUpperBound(arr, 0, arr.length - 1, target);
	}

	public static boolean contains(final double[] arr, final double target) {
		return binarySearch(arr, 0, arr.length, target) >= 0;
	}

	public static int count(final double[] arr, final double target) {
		final int up = upperBoundSearch(arr, target);
		if (up < 0) return 0;
		return up - lowerBoundSearch(arr, target) + 1;
	}

	public static int lowerBoundSearch(final double[] arr, final int l, final int r, final double target) {
		return binarySearchLowerBound(arr, l, r - 1, target);
	}

	public static int upperBoundSearch(final double[] arr, final int l, final int r, final double target) {
		return binarySearchUpperBound(arr, l, r - 1, target);
	}

	public static boolean contains(final double[] arr, final int l, final int r, final double target) {
		return binarySearch(arr, l, r, target) >= 0;
	}

	public static int count(final double[] arr, final int l, final int r, final double target) {
		final int up = upperBoundSearch(arr, l, r, target);
		if (up < 0) return 0;
		return up - lowerBoundSearch(arr, l, r, target) + 1;
	}
	// endregion

	// region T[]
	public static <T extends Comparable<? super T>> int lowerBoundSearch(final T[] arr, final T target) {
		return binarySearchLowerBound(arr, 0, arr.length - 1, target);
	}

	public static <T extends Comparable<? super T>> int upperBoundSearch(final T[] arr, final T target) {
		return binarySearchUpperBound(arr, 0, arr.length - 1, target);
	}

	public static <T extends Comparable<? super T>> boolean contains(final T[] arr, final T target) {
		return binarySearch(arr, 0, arr.length, target) >= 0;
	}

	public static <T extends Comparable<? super T>> int count(final T[] arr, final T target) {
		final int up = upperBoundSearch(arr, target);
		if (up < 0) return 0;
		return up - lowerBoundSearch(arr, target) + 1;
	}

	public static <T extends Comparable<? super T>> int lowerBoundSearch(final T[] arr, final int l, final int r, final T target) {
		return binarySearchLowerBound(arr, l, r - 1, target);
	}

	public static <T extends Comparable<? super T>> int upperBoundSearch(final T[] arr, final int l, final int r, final T target) {
		return binarySearchUpperBound(arr, l, r - 1, target);
	}

	public static <T extends Comparable<? super T>> boolean contains(final T[] arr, final int l, final int r, final T target) {
		return binarySearch(arr, l, r, target) >= 0;
	}

	public static <T extends Comparable<? super T>> int count(final T[] arr, final int l, final int r, final T target) {
		final int up = upperBoundSearch(arr, l, r, target);
		if (up < 0) return 0;
		return up - lowerBoundSearch(arr, l, r, target) + 1;
	}
	// endregion

	// region IntArray
	public static int lowerBoundSearch(final IntArray arr, final int target) {
		return binarySearchLowerBound(arr, 0, arr.size() - 1, target);
	}

	public static int upperBoundSearch(final IntArray arr, final int target) {
		return binarySearchUpperBound(arr, 0, arr.size() - 1, target);
	}

	public static boolean contains(final IntArray arr, final int target) {
		return binarySearchNormal(arr, 0, arr.size() - 1, target) >= 0;
	}

	public static int count(final IntArray arr, final int target) {
		final int up = upperBoundSearch(arr, target);
		if (up < 0) return 0;
		return up - lowerBoundSearch(arr, target) + 1;
	}

	public static int lowerBoundSearch(final IntArray arr, final int l, final int r, final int target) {
		return binarySearchLowerBound(arr, l, r - 1, target);
	}

	public static int upperBoundSearch(final IntArray arr, final int l, final int r, final int target) {
		return binarySearchUpperBound(arr, l, r - 1, target);
	}

	public static boolean contains(final IntArray arr, final int l, final int r, final int target) {
		return binarySearchNormal(arr, l, r - 1, target) >= 0;
	}

	public static int count(final IntArray arr, final int l, final int r, final int target) {
		final int up = upperBoundSearch(arr, l, r, target);
		if (up < 0) return 0;
		return up - lowerBoundSearch(arr, l, r, target) + 1;
	}
	// endregion

	// region LongArray
	public static int lowerBoundSearch(final LongArray arr, final long target) {
		return binarySearchLowerBound(arr, 0, arr.size() - 1, target);
	}

	public static int upperBoundSearch(final LongArray arr, final long target) {
		return binarySearchUpperBound(arr, 0, arr.size() - 1, target);
	}

	public static boolean contains(final LongArray arr, final long target) {
		return binarySearchNormal(arr, 0, arr.size() - 1, target) >= 0;
	}

	public static int count(final LongArray arr, final long target) {
		final int up = upperBoundSearch(arr, target);
		if (up < 0) return 0;
		return up - lowerBoundSearch(arr, target) + 1;
	}

	public static int lowerBoundSearch(final LongArray arr, final int l, final int r, final long target) {
		return binarySearchLowerBound(arr, l, r - 1, target);
	}

	public static int upperBoundSearch(final LongArray arr, final int l, final int r, final long target) {
		return binarySearchUpperBound(arr, l, r - 1, target);
	}

	public static boolean contains(final LongArray arr, final int l, final int r, final long target) {
		return binarySearchNormal(arr, l, r - 1, target) >= 0;
	}

	public static int count(final LongArray arr, final int l, final int r, final long target) {
		final int up = upperBoundSearch(arr, l, r, target);
		if (up < 0) return 0;
		return up - lowerBoundSearch(arr, l, r, target) + 1;
	}
	// endregion

	// region private binary search methods
	private static int binarySearchUpperBound(final int[] arr, int l, int r, final int t) {
		int ans = -1;
		while (l <= r) {
			final int m = (l + r) >>> 1;
			if (arr[m] > t) {
				r = m - 1;
			} else {
				if (arr[m] == t) ans = m;
				l = m + 1;
			}
		}
		return ans == -1 ? ~l : ans;
	}

	private static int binarySearchLowerBound(final int[] arr, int l, int r, final int t) {
		int ans = -1;
		while (l <= r) {
			final int m = (l + r) >>> 1;
			if (arr[m] < t) {
				l = m + 1;
			} else {
				if (arr[m] == t) ans = m;
				r = m - 1;
			}
		}
		return ans == -1 ? ~l : ans;
	}

	private static int binarySearchUpperBound(final long[] arr, int l, int r, final long t) {
		int ans = -1;
		while (l <= r) {
			final int m = (l + r) >>> 1;
			if (arr[m] > t) {
				r = m - 1;
			} else {
				if (arr[m] == t) ans = m;
				l = m + 1;
			}
		}
		return ans == -1 ? ~l : ans;
	}

	private static int binarySearchLowerBound(final long[] arr, int l, int r, final long t) {
		int ans = -1;
		while (l <= r) {
			final int m = (l + r) >>> 1;
			if (arr[m] < t) {
				l = m + 1;
			} else {
				if (arr[m] == t) ans = m;
				r = m - 1;
			}
		}
		return ans == -1 ? ~l : ans;
	}

	private static int binarySearchUpperBound(final double[] arr, int l, int r, final double t) {
		int ans = -1;
		while (l <= r) {
			final int m = (l + r) >>> 1;
			if (arr[m] > t) {
				r = m - 1;
			} else {
				if (arr[m] == t) ans = m;
				l = m + 1;
			}
		}
		return ans == -1 ? ~l : ans;
	}

	private static int binarySearchLowerBound(final double[] arr, int l, int r, final double t) {
		int ans = -1;
		while (l <= r) {
			final int m = (l + r) >>> 1;
			if (arr[m] < t) {
				l = m + 1;
			} else {
				if (arr[m] == t) ans = m;
				r = m - 1;
			}
		}
		return ans == -1 ? ~l : ans;
	}

	private static <T extends Comparable<? super T>> int binarySearchUpperBound(final T[] arr, int l, int r, final T target) {
		int ans = -1;
		while (l <= r) {
			final int m = (l + r) >>> 1;
			final T t = arr[m];
			final int c = t.compareTo(target);
			if (c > 0) {
				r = m - 1;
			} else {
				if (c == 0) ans = m;
				l = m + 1;
			}
		}
		return ans == -1 ? ~l : ans;
	}

	private static <T extends Comparable<? super T>> int binarySearchLowerBound(final T[] arr, int l, int r, final T target) {
		int ans = -1;
		while (l <= r) {
			final int m = (l + r) >>> 1;
			final T t = arr[m];
			final int c = t.compareTo(target);
			if (c < 0) {
				l = m + 1;
			} else {
				if (c == 0) ans = m;
				r = m - 1;
			}
		}
		return ans == -1 ? ~l : ans;
	}

	private static int binarySearchNormal(final IntArray arr, int l, int r, final int target) {
		while (l <= r) {
			final int m = (l + r) >>> 1;
			final int t = arr.get(m);
			if (t > target) r = m - 1;
			else if (t < target) l = m + 1;
			else return m;
		}
		return ~l;
	}

	private static int binarySearchUpperBound(final IntArray arr, int l, int r, final int target) {
		int ans = -1;
		while (l <= r) {
			final int m = (l + r) >>> 1;
			final int t = arr.get(m);
			if (t > target) {
				r = m - 1;
			} else {
				if (t == target) ans = m;
				l = m + 1;
			}
		}
		return ans == -1 ? ~l : ans;
	}

	private static int binarySearchLowerBound(final IntArray arr, int l, int r, final int target) {
		int ans = -1;
		while (l <= r) {
			final int m = (l + r) >>> 1;
			final int t = arr.get(m);
			if (t < target) {
				l = m + 1;
			} else {
				if (t == target) ans = m;
				r = m - 1;
			}
		}
		return ans == -1 ? ~l : ans;
	}

	private static int binarySearchNormal(final LongArray arr, int l, int r, final long target) {
		while (l <= r) {
			final int m = (l + r) >>> 1;
			final long t = arr.get(m);
			if (t > target) r = m - 1;
			else if (t < target) l = m + 1;
			else return m;
		}
		return ~l;
	}

	private static int binarySearchUpperBound(final LongArray arr, int l, int r, final long target) {
		int ans = -1;
		while (l <= r) {
			final int m = (l + r) >>> 1;
			final long t = arr.get(m);
			if (t > target) {
				r = m - 1;
			} else {
				if (t == target) ans = m;
				l = m + 1;
			}
		}
		return ans == -1 ? ~l : ans;
	}

	private static int binarySearchLowerBound(final LongArray arr, int l, int r, final long target) {
		int ans = -1;
		while (l <= r) {
			final int m = (l + r) >>> 1;
			final long t = arr.get(m);
			if (t < target) {
				l = m + 1;
			} else {
				if (t == target) ans = m;
				r = m - 1;
			}
		}
		return ans == -1 ? ~l : ans;
	}
	// endregion
}
