/**
 * 二分探索のテンプレート
 * 配列に対する二分探索を行います。
 */
@SuppressWarnings("unused")
public class ArrayBinarySearch {

	public static int normalSearch(int[] arr, int target) {
		return normalSearch(arr, 0, arr.length, target);
	}

	public static int normalSearch(int[] arr, int l, int r, int target) {
		r--;
		while (l <= r) {
			int m = (l + r) >>> 1L;
			switch (Integer.compare(arr[m], target)) {
				case 1:
					r = m - 1;
					break;
				case 0:
					return m;
				case -1:
					l = m + 1;
					break;
			}
		}
		return ~l;
	}

	public static int lowerBoundSearch(int[] arr, int target) {
		return lowerBoundSearch(arr, 0, arr.length, target);
	}

	public static int lowerBoundSearch(int[] arr, int l, int r, int target) {
		r--;
		Integer k = null;
		while (l <= r) {
			int m = (l + r) >>> 1L;
			switch (Integer.compare(arr[m], target)) {
				case 1:
					r = m - 1;
					break;
				case 0:
					r = m - 1;
					k = m;
					break;
				case -1:
					l = m + 1;
					break;
			}
		}
		return k != null ? k : ~l;
	}

	public static int upperBoundSearch(int[] arr, int target) {
		return upperBoundSearch(arr, 0, arr.length, target);
	}

	public static int upperBoundSearch(int[] arr, int l, int r, int target) {
		r--;
		Integer k = null;
		while (l <= r) {
			int m = (l + r) >>> 1L;
			switch (Integer.compare(arr[m], target)) {
				case 1:
					r = m - 1;
					break;
				case 0:
					l = m + 1;
					k = m;
					break;
				case -1:
					l = m + 1;
					break;
			}
		}
		return k != null ? k : ~l;
	}

}