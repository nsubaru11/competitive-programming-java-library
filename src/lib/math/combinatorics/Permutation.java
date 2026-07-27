package lib.math.combinatorics;

import lib.util.ArrayUtils;

import lib.math.numbertheory.*;

/**
 * 順列の辞書順位置と列挙に関するクラスです。
 */
@SuppressWarnings("unused")
public final class Permutation {

	/**
	 * 重複を除いた辞書順における0始まりの位置を返します。
	 *
	 * @param arr 対象の配列
	 * @return 辞書順における位置
	 */
	public static long index(final int[] arr) {
		return index(arr, 0, arr.length);
	}

	/**
	 * 指定範囲の、重複を除いた辞書順における0始まりの位置を返します。
	 *
	 * @param arr     対象の配列
	 * @param fromIdx 始点_include
	 * @param toIdx   終点_exclude
	 * @return 辞書順における位置
	 */
	public static long index(final int[] arr, final int fromIdx, final int toIdx) {
		long permutations = 1;
		for (int i = fromIdx; i < toIdx; i++) {
			int cnt = 1;
			for (int j = fromIdx; j < i; j++) if (arr[j] == arr[i]) cnt++;
			permutations = mulDiv(permutations, i - fromIdx + 1, cnt);
		}
		long index = 0;
		for (int i = fromIdx; i < toIdx; i++) {
			int smaller = 0, same = 0;
			for (int j = i; j < toIdx; j++) {
				if (arr[j] < arr[i]) smaller++;
				else if (arr[j] == arr[i]) same++;
			}
			final int rem = toIdx - i;
			index += mulDiv(permutations, smaller, rem);
			permutations = mulDiv(permutations, same, rem);
		}
		return index;
	}

	/**
	 * 重複を除いた辞書順における0始まりの位置を返します。
	 *
	 * @param arr 対象の配列
	 * @return 辞書順における位置
	 */
	public static long index(final long[] arr) {
		return index(arr, 0, arr.length);
	}

	/**
	 * 指定範囲の、重複を除いた辞書順における0始まりの位置を返します。
	 *
	 * @param arr     対象の配列
	 * @param fromIdx 始点_include
	 * @param toIdx   終点_exclude
	 * @return 辞書順における位置
	 */
	public static long index(final long[] arr, final int fromIdx, final int toIdx) {
		long permutations = 1;
		for (int i = fromIdx; i < toIdx; i++) {
			int cnt = 1;
			for (int j = fromIdx; j < i; j++) if (arr[j] == arr[i]) cnt++;
			permutations = mulDiv(permutations, i - fromIdx + 1, cnt);
		}
		long index = 0;
		for (int i = fromIdx; i < toIdx; i++) {
			int smaller = 0, same = 0;
			for (int j = i; j < toIdx; j++) {
				if (arr[j] < arr[i]) smaller++;
				else if (arr[j] == arr[i]) same++;
			}
			final int rem = toIdx - i;
			index += mulDiv(permutations, smaller, rem);
			permutations = mulDiv(permutations, same, rem);
		}
		return index;
	}

	/**
	 * 重複を除いた辞書順における0始まりの位置を返します。
	 *
	 * @param arr 対象の配列
	 * @return 辞書順における位置
	 */
	public static long index(final char[] arr) {
		return index(arr, 0, arr.length);
	}

	/**
	 * 指定範囲の、重複を除いた辞書順における0始まりの位置を返します。
	 *
	 * @param arr     対象の配列
	 * @param fromIdx 始点_include
	 * @param toIdx   終点_exclude
	 * @return 辞書順における位置
	 */
	public static long index(final char[] arr, final int fromIdx, final int toIdx) {
		long permutations = 1;
		for (int i = fromIdx; i < toIdx; i++) {
			int cnt = 1;
			for (int j = fromIdx; j < i; j++) if (arr[j] == arr[i]) cnt++;
			permutations = mulDiv(permutations, i - fromIdx + 1, cnt);
		}
		long index = 0;
		for (int i = fromIdx; i < toIdx; i++) {
			int smaller = 0, same = 0;
			for (int j = i; j < toIdx; j++) {
				if (arr[j] < arr[i]) smaller++;
				else if (arr[j] == arr[i]) same++;
			}
			final int rem = toIdx - i;
			index += mulDiv(permutations, smaller, rem);
			permutations = mulDiv(permutations, same, rem);
		}
		return index;
	}

	/**
	 * 辞書順で次の順列に並び替えます。
	 *
	 * @param arr 並び替え対象の配列
	 * @return 辞書順で次に当たる配列がある場合はtrue、arrが降順に並んでいるならfalse
	 */
	public static boolean next(final int[] arr) {
		final int len = arr.length;
		for (int i = len - 2; i >= 0; --i) {
			if (arr[i] < arr[i + 1]) {
				for (int j = len - 1; i < j; --j) {
					if (arr[i] < arr[j]) {
						ArrayUtils.swap(arr, i, j);
						ArrayUtils.reverse(arr, i + 1, len);
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 指定した範囲内の要素を、辞書順で次の順列に並び替えます。
	 *
	 * @param arr     並び替え対象の配列
	 * @param fromIdx 始点_include
	 * @param toIdx   終点_exclude
	 * @return 辞書順で次に当たる配列がある場合はtrue、arrが降順に並んでいるならfalse
	 */
	public static boolean next(final int[] arr, final int fromIdx, final int toIdx) {
		for (int i = toIdx - 2; i >= fromIdx; --i) {
			if (arr[i] < arr[i + 1]) {
				for (int j = toIdx - 1; i < j; --j) {
					if (arr[i] < arr[j]) {
						ArrayUtils.swap(arr, i, j);
						ArrayUtils.reverse(arr, i + 1, toIdx);
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 辞書順で前の順列に並び替えます。
	 *
	 * @param arr 並び替え対象の配列
	 * @return 辞書順で前に当たる配列がある場合はtrue、arrが昇順に並んでいるならfalse
	 */
	public static boolean prev(final int[] arr) {
		final int len = arr.length;
		for (int i = len - 2; i >= 0; --i) {
			if (arr[i] > arr[i + 1]) {
				for (int j = len - 1; i < j; --j) {
					if (arr[i] > arr[j]) {
						ArrayUtils.swap(arr, i, j);
						ArrayUtils.reverse(arr, i + 1, len);
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 指定した範囲内の要素を、辞書順で前の順列に並び替えます。
	 *
	 * @param arr     並び替え対象の配列
	 * @param fromIdx 始点_include
	 * @param toIdx   終点_exclude
	 * @return 辞書順で前に当たる配列がある場合はtrue、arrが昇順に並んでいるならfalse
	 */
	public static boolean prev(final int[] arr, final int fromIdx, final int toIdx) {
		for (int i = toIdx - 2; i >= fromIdx; --i) {
			if (arr[i] > arr[i + 1]) {
				for (int j = toIdx - 1; i < j; --j) {
					if (arr[i] > arr[j]) {
						ArrayUtils.swap(arr, i, j);
						ArrayUtils.reverse(arr, i + 1, toIdx);
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 辞書順で次の順列に並び替えます。
	 *
	 * @param arr 並び替え対象の配列
	 * @return 辞書順で次に当たる配列がある場合はtrue、arrが降順に並んでいるならfalse
	 */
	public static boolean next(final long[] arr) {
		final int len = arr.length;
		for (int i = len - 2; i >= 0; --i) {
			if (arr[i] < arr[i + 1]) {
				for (int j = len - 1; i < j; --j) {
					if (arr[i] < arr[j]) {
						ArrayUtils.swap(arr, i, j);
						ArrayUtils.reverse(arr, i + 1, len);
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 指定した範囲内の要素を、辞書順で次の順列に並び替えます。
	 *
	 * @param arr     並び替え対象の配列
	 * @param fromIdx 始点_include
	 * @param toIdx   終点_exclude
	 * @return 辞書順で次に当たる配列がある場合はtrue、arrが降順に並んでいるならfalse
	 */
	public static boolean next(final long[] arr, final int fromIdx, final int toIdx) {
		for (int i = toIdx - 2; i >= fromIdx; --i) {
			if (arr[i] < arr[i + 1]) {
				for (int j = toIdx - 1; i < j; --j) {
					if (arr[i] < arr[j]) {
						ArrayUtils.swap(arr, i, j);
						ArrayUtils.reverse(arr, i + 1, toIdx);
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 辞書順で前の順列に並び替えます。
	 *
	 * @param arr 並び替え対象の配列
	 * @return 辞書順で前に当たる配列がある場合はtrue、arrが昇順に並んでいるならfalse
	 */
	public static boolean prev(final long[] arr) {
		final int len = arr.length;
		for (int i = len - 2; i >= 0; --i) {
			if (arr[i] > arr[i + 1]) {
				for (int j = len - 1; i < j; --j) {
					if (arr[i] > arr[j]) {
						ArrayUtils.swap(arr, i, j);
						ArrayUtils.reverse(arr, i + 1, len);
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 指定した範囲内の要素を、辞書順で前の順列に並び替えます。
	 *
	 * @param arr     並び替え対象の配列
	 * @param fromIdx 始点_include
	 * @param toIdx   終点_exclude
	 * @return 辞書順で前に当たる配列がある場合はtrue、arrが昇順に並んでいるならfalse
	 */
	public static boolean prev(final long[] arr, final int fromIdx, final int toIdx) {
		for (int i = toIdx - 2; i >= fromIdx; --i) {
			if (arr[i] > arr[i + 1]) {
				for (int j = toIdx - 1; i < j; --j) {
					if (arr[i] > arr[j]) {
						ArrayUtils.swap(arr, i, j);
						ArrayUtils.reverse(arr, i + 1, toIdx);
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 辞書順で次の順列に並び替えます。
	 *
	 * @param arr 並び替え対象の配列
	 * @return 辞書順で次に当たる配列がある場合はtrue、arrが降順に並んでいるならfalse
	 */
	public static boolean next(final char[] arr) {
		final int len = arr.length;
		for (int i = len - 2; i >= 0; --i) {
			if (arr[i] < arr[i + 1]) {
				for (int j = len - 1; i < j; --j) {
					if (arr[i] < arr[j]) {
						ArrayUtils.swap(arr, i, j);
						ArrayUtils.reverse(arr, i + 1, len);
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 指定した範囲内の要素を、辞書順で次の順列に並び替えます。
	 *
	 * @param arr     並び替え対象の配列
	 * @param fromIdx 始点_include
	 * @param toIdx   終点_exclude
	 * @return 辞書順で次に当たる配列がある場合はtrue、arrが降順に並んでいるならfalse
	 */
	public static boolean next(final char[] arr, final int fromIdx, final int toIdx) {
		for (int i = toIdx - 2; i >= fromIdx; --i) {
			if (arr[i] < arr[i + 1]) {
				for (int j = toIdx - 1; i < j; --j) {
					if (arr[i] < arr[j]) {
						ArrayUtils.swap(arr, i, j);
						ArrayUtils.reverse(arr, i + 1, toIdx);
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 辞書順で前の順列に並び替えます。
	 *
	 * @param arr 並び替え対象の配列
	 * @return 辞書順で前に当たる配列がある場合はtrue、arrが昇順に並んでいるならfalse
	 */
	public static boolean prev(final char[] arr) {
		final int len = arr.length;
		for (int i = len - 2; i >= 0; --i) {
			if (arr[i] > arr[i + 1]) {
				for (int j = len - 1; i < j; --j) {
					if (arr[i] > arr[j]) {
						ArrayUtils.swap(arr, i, j);
						ArrayUtils.reverse(arr, i + 1, len);
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 指定した範囲内の要素を、辞書順で前の順列に並び替えます。
	 *
	 * @param arr     並び替え対象の配列
	 * @param fromIdx 始点_include
	 * @param toIdx   終点_exclude
	 * @return 辞書順で前に当たる配列がある場合はtrue、arrが昇順に並んでいるならfalse
	 */
	public static boolean prev(final char[] arr, final int fromIdx, final int toIdx) {
		for (int i = toIdx - 2; i >= fromIdx; --i) {
			if (arr[i] > arr[i + 1]) {
				for (int j = toIdx - 1; i < j; --j) {
					if (arr[i] > arr[j]) {
						ArrayUtils.swap(arr, i, j);
						ArrayUtils.reverse(arr, i + 1, toIdx);
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 指定列を基準とする、重複を除いた辞書順における0始まりの位置を返します。
	 *
	 * @param arr 対象の配列
	 * @param idx 比較する列
	 * @return 辞書順における位置
	 */
	public static long index(final int[][] arr, final int idx) {
		return index(arr, idx, 0, arr.length);
	}

	/**
	 * 指定範囲を指定列で比較した、重複を除く辞書順における0始まりの位置を返します。
	 *
	 * @param arr     対象の配列
	 * @param idx     比較する列
	 * @param fromIdx 始点_include
	 * @param toIdx   終点_exclude
	 * @return 辞書順における位置
	 */
	public static long index(final int[][] arr, final int idx, final int fromIdx, final int toIdx) {
		final int[] key = new int[toIdx - fromIdx];
		for (int i = fromIdx; i < toIdx; i++) key[i - fromIdx] = arr[i][idx];
		return index(key);
	}

	/**
	 * 指定列を基準とする、重複を除いた辞書順における0始まりの位置を返します。
	 *
	 * @param arr 対象の配列
	 * @param idx 比較する列
	 * @return 辞書順における位置
	 */
	public static long index(final long[][] arr, final int idx) {
		return index(arr, idx, 0, arr.length);
	}

	/**
	 * 指定範囲を指定列で比較した、重複を除く辞書順における0始まりの位置を返します。
	 *
	 * @param arr     対象の配列
	 * @param idx     比較する列
	 * @param fromIdx 始点_include
	 * @param toIdx   終点_exclude
	 * @return 辞書順における位置
	 */
	public static long index(final long[][] arr, final int idx, final int fromIdx, final int toIdx) {
		final long[] key = new long[toIdx - fromIdx];
		for (int i = fromIdx; i < toIdx; i++) key[i - fromIdx] = arr[i][idx];
		return index(key);
	}

	/**
	 * 指定列を基準とする、重複を除いた辞書順における0始まりの位置を返します。
	 *
	 * @param arr 対象の配列
	 * @param idx 比較する列
	 * @return 辞書順における位置
	 */
	public static long index(final char[][] arr, final int idx) {
		return index(arr, idx, 0, arr.length);
	}

	/**
	 * 指定範囲を指定列で比較した、重複を除く辞書順における0始まりの位置を返します。
	 *
	 * @param arr     対象の配列
	 * @param idx     比較する列
	 * @param fromIdx 始点_include
	 * @param toIdx   終点_exclude
	 * @return 辞書順における位置
	 */
	public static long index(final char[][] arr, final int idx, final int fromIdx, final int toIdx) {
		final char[] key = new char[toIdx - fromIdx];
		for (int i = fromIdx; i < toIdx; i++) key[i - fromIdx] = arr[i][idx];
		return index(key);
	}

	/**
	 * 辞書順で次の順列に並び替えます。
	 *
	 * @param arr 並び替え対象の配列
	 * @return 辞書順で次に当たる配列がある場合はtrue、arrが降順に並んでいるならfalse
	 */
	public static boolean next(final int[][] arr, final int idx) {
		final int len = arr.length;
		for (int i = len - 2; i >= 0; --i) {
			if (arr[i][idx] < arr[i + 1][idx]) {
				for (int j = len - 1; i < j; --j) {
					if (arr[i][idx] < arr[j][idx]) {
						ArrayUtils.swapRow(arr, i, j);
						ArrayUtils.reverse(arr, i + 1, len);
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 指定した範囲内の要素を、辞書順で次の順列に並び替えます。
	 *
	 * @param arr     並び替え対象の配列
	 * @param fromIdx 始点_include
	 * @param toIdx   終点_exclude
	 * @return 辞書順で次に当たる配列がある場合はtrue、arrが降順に並んでいるならfalse
	 */
	public static boolean next(final int[][] arr, final int idx, final int fromIdx, final int toIdx) {
		for (int i = toIdx - 2; i >= fromIdx; --i) {
			if (arr[i][idx] < arr[i + 1][idx]) {
				for (int j = toIdx - 1; i < j; --j) {
					if (arr[i][idx] < arr[j][idx]) {
						ArrayUtils.swapRow(arr, i, j);
						ArrayUtils.reverse(arr, i + 1, toIdx);
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 辞書順で前の順列に並び替えます。
	 *
	 * @param arr 並び替え対象の配列
	 * @return 辞書順で前に当たる配列がある場合はtrue、arrが昇順に並んでいるならfalse
	 */
	public static boolean prev(final int[][] arr, final int idx) {
		final int len = arr.length;
		for (int i = len - 2; i >= 0; --i) {
			if (arr[i][idx] > arr[i + 1][idx]) {
				for (int j = len - 1; i < j; --j) {
					if (arr[i][idx] > arr[j][idx]) {
						ArrayUtils.swapRow(arr, i, j);
						ArrayUtils.reverse(arr, i + 1, len);
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 指定した範囲内の要素を、辞書順で前の順列に並び替えます。
	 *
	 * @param arr     並び替え対象の配列
	 * @param fromIdx 始点_include
	 * @param toIdx   終点_exclude
	 * @return 辞書順で前に当たる配列がある場合はtrue、arrが昇順に並んでいるならfalse
	 */
	public static boolean prev(final int[][] arr, final int idx, final int fromIdx, final int toIdx) {
		for (int i = toIdx - 2; i >= fromIdx; --i) {
			if (arr[i][idx] > arr[i + 1][idx]) {
				for (int j = toIdx - 1; i < j; --j) {
					if (arr[i][idx] > arr[j][idx]) {
						ArrayUtils.swapRow(arr, i, j);
						ArrayUtils.reverse(arr, i + 1, toIdx);
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 辞書順で次の順列に並び替えます。
	 *
	 * @param arr 並び替え対象の配列
	 * @return 辞書順で次に当たる配列がある場合はtrue、arrが降順に並んでいるならfalse
	 */
	public static boolean next(final long[][] arr, final int idx) {
		final int len = arr.length;
		for (int i = len - 2; i >= 0; --i) {
			if (arr[i][idx] < arr[i + 1][idx]) {
				for (int j = len - 1; i < j; --j) {
					if (arr[i][idx] < arr[j][idx]) {
						ArrayUtils.swapRow(arr, i, j);
						ArrayUtils.reverse(arr, i + 1, len);
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 指定した範囲内の要素を、辞書順で次の順列に並び替えます。
	 *
	 * @param arr     並び替え対象の配列
	 * @param fromIdx 始点_include
	 * @param toIdx   終点_exclude
	 * @return 辞書順で次に当たる配列がある場合はtrue、arrが降順に並んでいるならfalse
	 */
	public static boolean next(final long[][] arr, final int idx, final int fromIdx, final int toIdx) {
		for (int i = toIdx - 2; i >= fromIdx; --i) {
			if (arr[i][idx] < arr[i + 1][idx]) {
				for (int j = toIdx - 1; i < j; --j) {
					if (arr[i][idx] < arr[j][idx]) {
						ArrayUtils.swapRow(arr, i, j);
						ArrayUtils.reverse(arr, i + 1, toIdx);
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 辞書順で前の順列に並び替えます。
	 *
	 * @param arr 並び替え対象の配列
	 * @return 辞書順で前に当たる配列がある場合はtrue、arrが昇順に並んでいるならfalse
	 */
	public static boolean prev(final long[][] arr, final int idx) {
		final int len = arr.length;
		for (int i = len - 2; i >= 0; --i) {
			if (arr[i][idx] > arr[i + 1][idx]) {
				for (int j = len - 1; i < j; --j) {
					if (arr[i][idx] > arr[j][idx]) {
						ArrayUtils.swapRow(arr, i, j);
						ArrayUtils.reverse(arr, i + 1, len);
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 指定した範囲内の要素を、辞書順で前の順列に並び替えます。
	 *
	 * @param arr     並び替え対象の配列
	 * @param fromIdx 始点_include
	 * @param toIdx   終点_exclude
	 * @return 辞書順で前に当たる配列がある場合はtrue、arrが昇順に並んでいるならfalse
	 */
	public static boolean prev(final long[][] arr, final int idx, final int fromIdx, final int toIdx) {
		for (int i = toIdx - 2; i >= fromIdx; --i) {
			if (arr[i][idx] > arr[i + 1][idx]) {
				for (int j = toIdx - 1; i < j; --j) {
					if (arr[i][idx] > arr[j][idx]) {
						ArrayUtils.swapRow(arr, i, j);
						ArrayUtils.reverse(arr, i + 1, toIdx);
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 辞書順で次の順列に並び替えます。
	 *
	 * @param arr 並び替え対象の配列
	 * @return 辞書順で次に当たる配列がある場合はtrue、arrが降順に並んでいるならfalse
	 */
	public static boolean next(final char[][] arr, final int idx) {
		final int len = arr.length;
		for (int i = len - 2; i >= 0; --i) {
			if (arr[i][idx] < arr[i + 1][idx]) {
				for (int j = len - 1; i < j; --j) {
					if (arr[i][idx] < arr[j][idx]) {
						ArrayUtils.swapRow(arr, i, j);
						ArrayUtils.reverse(arr, i + 1, len);
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 指定した範囲内の要素を、辞書順で次の順列に並び替えます。
	 *
	 * @param arr     並び替え対象の配列
	 * @param fromIdx 始点_include
	 * @param toIdx   終点_exclude
	 * @return 辞書順で次に当たる配列がある場合はtrue、arrが降順に並んでいるならfalse
	 */
	public static boolean next(final char[][] arr, final int idx, final int fromIdx, final int toIdx) {
		for (int i = toIdx - 2; i >= fromIdx; --i) {
			if (arr[i][idx] < arr[i + 1][idx]) {
				for (int j = toIdx - 1; i < j; --j) {
					if (arr[i][idx] < arr[j][idx]) {
						ArrayUtils.swapRow(arr, i, j);
						ArrayUtils.reverse(arr, i + 1, toIdx);
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 辞書順で前の順列に並び替えます。
	 *
	 * @param arr 並び替え対象の配列
	 * @return 辞書順で前に当たる配列がある場合はtrue、arrが昇順に並んでいるならfalse
	 */
	public static boolean prev(final char[][] arr, final int idx) {
		final int len = arr.length;
		for (int i = len - 2; i >= 0; --i) {
			if (arr[i][idx] > arr[i + 1][idx]) {
				for (int j = len - 1; i < j; --j) {
					if (arr[i][idx] > arr[j][idx]) {
						ArrayUtils.swapRow(arr, i, j);
						ArrayUtils.reverse(arr, i + 1, len);
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 指定した範囲内の要素を、辞書順で前の順列に並び替えます。
	 *
	 * @param arr     並び替え対象の配列
	 * @param fromIdx 始点_include
	 * @param toIdx   終点_exclude
	 * @return 辞書順で前に当たる配列がある場合はtrue、arrが昇順に並んでいるならfalse
	 */
	public static boolean prev(final char[][] arr, final int idx, final int fromIdx, final int toIdx) {
		for (int i = toIdx - 2; i >= fromIdx; --i) {
			if (arr[i][idx] > arr[i + 1][idx]) {
				for (int j = toIdx - 1; i < j; --j) {
					if (arr[i][idx] > arr[j][idx]) {
						ArrayUtils.swapRow(arr, i, j);
						ArrayUtils.reverse(arr, i + 1, toIdx);
						return true;
					}
				}
			}
		}
		return false;
	}

	private static long mulDiv(long a, long b, long d) {
		final long g = NumberTheoryUtils.fastGcd(b, d);
		b /= g;
		d /= g;
		return a / d * b;
	}
}
