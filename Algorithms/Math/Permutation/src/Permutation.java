/**
 * 順列列挙に関するクラスです。
 */
@SuppressWarnings("unused")
public final class Permutation {

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
						swap(arr, i, j);
						reverseRange(arr, i + 1, len);
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
						swap(arr, i, j);
						reverseRange(arr, i + 1, toIdx);
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
						swap(arr, i, j);
						reverseRange(arr, i + 1, len);
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
						swap(arr, i, j);
						reverseRange(arr, i + 1, toIdx);
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
						swap(arr, i, j);
						reverseRange(arr, i + 1, len);
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
						swap(arr, i, j);
						reverseRange(arr, i + 1, toIdx);
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
						swap(arr, i, j);
						reverseRange(arr, i + 1, len);
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
						swap(arr, i, j);
						reverseRange(arr, i + 1, toIdx);
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
						swap(arr, i, j);
						reverseRange(arr, i + 1, len);
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
						swap(arr, i, j);
						reverseRange(arr, i + 1, toIdx);
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
						swap(arr, i, j);
						reverseRange(arr, i + 1, len);
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
						swap(arr, i, j);
						reverseRange(arr, i + 1, toIdx);
						return true;
					}
				}
			}
		}
		return false;
	}

	private static void swap(final int[] a, final int i, final int j) {
		int swap = a[i];
		a[i] = a[j];
		a[j] = swap;
	}

	private static void swap(final long[] a, final int i, final int j) {
		long swap = a[i];
		a[i] = a[j];
		a[j] = swap;
	}

	private static void swap(final char[] a, final int i, final int j) {
		char swap = a[i];
		a[i] = a[j];
		a[j] = swap;
	}

	private static void reverseRange(final int[] a, int i, int j) {
		while (i < j) swap(a, i++, --j);
	}

	private static void reverseRange(final long[] a, int i, int j) {
		while (i < j) swap(a, i++, --j);
	}

	private static void reverseRange(final char[] a, int i, int j) {
		while (i < j) swap(a, i++, --j);
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
						swap(arr, i, j);
						reverseRange(arr, i + 1, len);
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
						swap(arr, i, j);
						reverseRange(arr, i + 1, toIdx);
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
						swap(arr, i, j);
						reverseRange(arr, i + 1, len);
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
						swap(arr, i, j);
						reverseRange(arr, i + 1, toIdx);
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
						swap(arr, i, j);
						reverseRange(arr, i + 1, len);
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
						swap(arr, i, j);
						reverseRange(arr, i + 1, toIdx);
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
						swap(arr, i, j);
						reverseRange(arr, i + 1, len);
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
						swap(arr, i, j);
						reverseRange(arr, i + 1, toIdx);
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
						swap(arr, i, j);
						reverseRange(arr, i + 1, len);
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
						swap(arr, i, j);
						reverseRange(arr, i + 1, toIdx);
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
						swap(arr, i, j);
						reverseRange(arr, i + 1, len);
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
						swap(arr, i, j);
						reverseRange(arr, i + 1, toIdx);
						return true;
					}
				}
			}
		}
		return false;
	}

	private static void swap(final int[][] a, int i, int j) {
		final int[] swap = a[i];
		a[i] = a[j];
		a[j] = swap;
	}

	private static void swap(final long[][] a, int i, int j) {
		final long[] swap = a[i];
		a[i] = a[j];
		a[j] = swap;
	}

	private static void swap(final char[][] a, int i, int j) {
		final char[] swap = a[i];
		a[i] = a[j];
		a[j] = swap;
	}

	private static void reverseRange(final int[][] a, int i, int j) {
		while (i < j) swap(a, i++, --j);
	}

	private static void reverseRange(final long[][] a, int i, int j) {
		while (i < j) swap(a, i++, --j);
	}

	private static void reverseRange(final char[][] a, int i, int j) {
		while (i < j) swap(a, i++, --j);
	}

}
