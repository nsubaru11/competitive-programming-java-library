/**
 * 整数配列、長整数配列、倍精度浮動小数点数配列、および比較可能な型の配列に対して
 * 二分探索を行うためのユーティリティクラスです。
 * 通常の二分探索、上限探索(Upper Bound)、下限探索(Lower Bound)を提供します。
 * 探索に失敗した際の戻り値は-(挿入位置 + 1)となっています。
 * このクラスの提供する上限探索、下限探索はそれぞれ重複する要素の最大、最小indexを返します。
 */
@SuppressWarnings("unused")
public final class ArrayBinarySearch {

	// コンストラクタをprivateにしてインスタンス化を防止
	private ArrayBinarySearch() {
		throw new UnsupportedOperationException("このクラスはインスタンス化できません");
	}

	/* -------------------------- 整数配列（int） -------------------------- */

	/**
	 * 整数配列全体に対して通常の二分探索を行います。
	 * 配列内の要素と目標値が一致する位置を探索します。
	 *
	 * @param arr    探索対象の整数配列
	 * @param target 探索対象の整数値
	 * @return 条件にちょうど当てはまるインデックス。探索に失敗した際の戻り値は-(挿入位置 + 1)となっています。
	 * @throws BSException 配列が {@code null} の場合、または範囲が不正な場合
	 */
	public static int normalSearch(final int[] arr, final int target) {
		validateRange(arr, 0, arr.length);
		return binarySearchNormal(arr, 0, arr.length - 1, target);
	}

	/**
	 * 整数配列の指定範囲に対して下限探索(Lower Bound)を行います。
	 * 配列内の要素で目標値以下の最大の要素の位置を探索します。
	 *
	 * @param arr    探索対象の整数配列
	 * @param target 探索対象の整数値
	 * @return 条件にちょうど当てはまるインデックス。探索に失敗した際の戻り値は-(挿入位置 + 1)となっています。
	 * @throws BSException 配列が {@code null} の場合、または範囲が不正な場合
	 */
	public static int lowerBoundSearch(final int[] arr, final int target) {
		validateRange(arr, 0, arr.length);
		return binarySearchLowerBound(arr, 0, arr.length - 1, target);
	}

	/**
	 * 整数配列の指定範囲に対して上限探索(Upper Bound)を行います。
	 * 配列内の要素で目標値以上の最小の要素の位置を探索します。
	 *
	 * @param arr    探索対象の整数配列
	 * @param target 探索対象の整数値
	 * @return 条件にちょうど当てはまるインデックス。探索に失敗した際の戻り値は-(挿入位置 + 1)となっています。
	 * @throws BSException 配列が {@code null} の場合、または範囲が不正な場合
	 */
	public static int upperBoundSearch(final int[] arr, final int target) {
		validateRange(arr, 0, arr.length);
		return binarySearchUpperBound(arr, 0, arr.length - 1, target);
	}

	/**
	 * 整数配列の指定範囲に対して通常の二分探索を行います。
	 * 配列内の要素と目標値が一致する位置を探索します。
	 *
	 * @param arr    探索対象の整数配列
	 * @param l      探索開始位置（この位置を含む）
	 * @param r      探索終了位置（この位置を含まない）
	 * @param target 探索対象の整数値
	 * @return 条件にちょうど当てはまるインデックス。探索に失敗した際の戻り値は-(挿入位置 + 1)となっています。
	 * @throws BSException 配列が {@code null} の場合、または範囲が不正な場合
	 */
	public static int normalSearch(final int[] arr, final int l, final int r, final int target) {
		validateRange(arr, l, r);
		return binarySearchNormal(arr, l, r - 1, target);
	}

	/**
	 * 整数配列の指定範囲に対して下限探索(Lower Bound)を行います。
	 * 配列内の要素で目標値以下の最大の要素の位置を探索します。
	 *
	 * @param arr    探索対象の整数配列
	 * @param l      探索開始位置（この位置を含む）
	 * @param r      探索終了位置（この位置を含まない）
	 * @param target 探索対象の整数値
	 * @return 条件にちょうど当てはまるインデックス。探索に失敗した際の戻り値は-(挿入位置 + 1)となっています。
	 * @throws BSException 配列が {@code null} の場合、または範囲が不正な場合
	 */
	public static int lowerBoundSearch(final int[] arr, final int l, final int r, final int target) {
		validateRange(arr, l, r);
		return binarySearchLowerBound(arr, l, r - 1, target);
	}

	/**
	 * 整数配列の指定範囲に対して上限探索(Upper Bound)を行います。
	 * 配列内の要素で目標値以上の最小の要素の位置を探索します。
	 *
	 * @param arr    探索対象の整数配列
	 * @param l      探索開始位置（この位置を含む）
	 * @param r      探索終了位置（この位置を含まない）
	 * @param target 探索対象の整数値
	 * @return 条件にちょうど当てはまるインデックス。探索に失敗した際の戻り値は-(挿入位置 + 1)となっています。
	 * @throws BSException 配列が {@code null} の場合、または範囲が不正な場合
	 */
	public static int upperBoundSearch(final int[] arr, final int l, final int r, final int target) {
		validateRange(arr, l, r);
		return binarySearchUpperBound(arr, l, r - 1, target);
	}

	/* -------------------------- 長整数配列（long） -------------------------- */

	/**
	 * 長整数配列全体に対して通常の二分探索を行います。
	 * 配列内の要素と目標値が一致する位置を探索します。
	 *
	 * @param arr    探索対象の長整数配列
	 * @param target 探索対象の長整数値
	 * @return 条件にちょうど当てはまるインデックス。探索に失敗した際の戻り値は-(挿入位置 + 1)となっています。
	 * @throws BSException 配列が {@code null} の場合、または範囲が不正な場合
	 */
	public static int normalSearch(final long[] arr, final long target) {
		validateRange(arr, 0, arr.length);
		return binarySearchNormal(arr, 0, arr.length - 1, target);
	}

	/**
	 * 長整数配列全体に対して下限探索(Lower Bound)を行います。
	 * 配列内の要素で目標値以下の最大の要素の位置を探索します。
	 *
	 * @param arr    探索対象の長整数配列
	 * @param target 探索対象の長整数値
	 * @return 条件にちょうど当てはまるインデックス。探索に失敗した際の戻り値は-(挿入位置 + 1)となっています。
	 * @throws BSException 配列が {@code null} の場合、または範囲が不正な場合
	 */
	public static int lowerBoundSearch(final long[] arr, final long target) {
		validateRange(arr, 0, arr.length);
		return binarySearchLowerBound(arr, 0, arr.length - 1, target);
	}

	/**
	 * 長整数配列全体に対して上限探索(Upper Bound)を行います。
	 * 配列内の要素で目標値以上の最小の要素の位置を探索します。
	 *
	 * @param arr    探索対象の長整数配列
	 * @param target 探索対象の長整数値
	 * @return 条件にちょうど当てはまるインデックス。探索に失敗した際の戻り値は-(挿入位置 + 1)となっています。
	 * @throws BSException 配列が {@code null} の場合、または範囲が不正な場合
	 */
	public static int upperBoundSearch(final long[] arr, final long target) {
		validateRange(arr, 0, arr.length);
		return binarySearchUpperBound(arr, 0, arr.length - 1, target);
	}

	/**
	 * 長整数配列の指定範囲に対して通常の二分探索を行います。
	 * 配列内の要素と目標値が一致する位置を探索します。
	 *
	 * @param arr    探索対象の長整数配列
	 * @param l      探索開始位置（この位置を含む）
	 * @param r      探索終了位置（この位置を含まない）
	 * @param target 探索対象の長整数値
	 * @return 条件にちょうど当てはまるインデックス。探索に失敗した際の戻り値は-(挿入位置 + 1)となっています。
	 * @throws BSException 配列が {@code null} の場合、または範囲が不正な場合
	 */
	public static int normalSearch(final long[] arr, final int l, final int r, final long target) {
		validateRange(arr, l, r);
		return binarySearchNormal(arr, l, r - 1, target);
	}

	/**
	 * 長整数配列の指定範囲に対して下限探索(Lower Bound)を行います。
	 * 配列内の要素で目標値以下の最大の要素の位置を探索します。
	 *
	 * @param arr    探索対象の長整数配列
	 * @param l      探索開始位置（この位置を含む）
	 * @param r      探索終了位置（この位置を含まない）
	 * @param target 探索対象の長整数値
	 * @return 条件にちょうど当てはまるインデックス。探索に失敗した際の戻り値は-(挿入位置 + 1)となっています。
	 * @throws BSException 配列が {@code null} の場合、または範囲が不正な場合
	 */
	public static int lowerBoundSearch(final long[] arr, final int l, final int r, final long target) {
		validateRange(arr, l, r);
		return binarySearchLowerBound(arr, l, r - 1, target);
	}

	/**
	 * 長整数配列の指定範囲に対して上限探索(Upper Bound)を行います。
	 * 配列内の要素で目標値以上の最小の要素の位置を探索します。
	 *
	 * @param arr    探索対象の長整数配列
	 * @param l      探索開始位置（この位置を含む）
	 * @param r      探索終了位置（この位置を含まない）
	 * @param target 探索対象の長整数値
	 * @return 条件にちょうど当てはまるインデックス。探索に失敗した際の戻り値は-(挿入位置 + 1)となっています。
	 * @throws BSException 配列が {@code null} の場合、または範囲が不正な場合
	 */
	public static int upperBoundSearch(final long[] arr, final int l, final int r, final long target) {
		validateRange(arr, l, r);
		return binarySearchUpperBound(arr, l, r - 1, target);
	}

	/* -------------------------- 倍浮動小数点数配列（double） -------------------------- */

	/**
	 * 倍精度浮動小数点数配列全体に対して通常の二分探索を行います。
	 * 配列内の要素と目標値が一致する位置を探索します。
	 *
	 * @param arr    探索対象の倍精度浮動小数点数配列
	 * @param target 探索対象の倍精度浮動小数点数値
	 * @return 条件にちょうど当てはまるインデックス。探索に失敗した際の戻り値は-(挿入位置 + 1)となっています。
	 * @throws BSException 配列が {@code null} の場合、または範囲が不正な場合
	 */
	public static int normalSearch(final double[] arr, final double target) {
		validateRange(arr, 0, arr.length);
		return binarySearchNormal(arr, 0, arr.length - 1, target);
	}

	/**
	 * 倍精度浮動小数点数配列全体に対して下限探索(Lower Bound)を行います。
	 * 配列内の要素で目標値以下の最大の要素の位置を探索します。
	 *
	 * @param arr    探索対象の倍精度浮動小数点数配列
	 * @param target 探索対象の倍精度浮動小数点数値
	 * @return 条件にちょうど当てはまるインデックス。探索に失敗した際の戻り値は-(挿入位置 + 1)となっています。
	 * @throws BSException 配列が {@code null} の場合、または範囲が不正な場合
	 */
	public static int lowerBoundSearch(final double[] arr, final double target) {
		validateRange(arr, 0, arr.length);
		return binarySearchLowerBound(arr, 0, arr.length - 1, target);
	}

	/**
	 * 倍精度浮動小数点数配列全体に対して上限探索(Upper Bound)を行います。
	 * 配列内の要素で目標値以上の最小の要素の位置を探索します。
	 *
	 * @param arr    探索対象の倍精度浮動小数点数配列
	 * @param target 探索対象の倍精度浮動小数点数値
	 * @return 条件にちょうど当てはまるインデックス。探索に失敗した際の戻り値は-(挿入位置 + 1)となっています。
	 * @throws BSException 配列が {@code null} の場合、または範囲が不正な場合
	 */
	public static int upperBoundSearch(final double[] arr, final double target) {
		validateRange(arr, 0, arr.length);
		return binarySearchUpperBound(arr, 0, arr.length - 1, target);
	}

	/**
	 * 倍精度浮動小数点数配列の指定範囲に対して通常の二分探索を行います。
	 * 配列内の要素と目標値が一致する位置を探索します。
	 *
	 * @param arr    探索対象の倍精度浮動小数点数配列
	 * @param l      探索開始位置（この位置を含む）
	 * @param r      探索終了位置（この位置を含まない）
	 * @param target 探索対象の倍精度浮動小数点数値
	 * @return 条件にちょうど当てはまるインデックス。探索に失敗した際の戻り値は-(挿入位置 + 1)となっています。
	 * @throws BSException 配列が {@code null} の場合、または範囲が不正な場合
	 */
	public static int normalSearch(final double[] arr, final int l, final int r, final double target) {
		validateRange(arr, l, r);
		return binarySearchNormal(arr, l, r - 1, target);
	}

	/**
	 * 倍精度浮動小数点数配列の指定範囲に対して下限探索(Lower Bound)を行います。
	 * 配列内の要素で目標値以下の最大の要素の位置を探索します。
	 *
	 * @param arr    探索対象の倍精度浮動小数点数配列
	 * @param l      探索開始位置（この位置を含む）
	 * @param r      探索終了位置（この位置を含まない）
	 * @param target 探索対象の倍精度浮動小数点数値
	 * @return 条件にちょうど当てはまるインデックス。探索に失敗した際の戻り値は-(挿入位置 + 1)となっています。
	 * @throws BSException 配列が {@code null} の場合、または範囲が不正な場合
	 */
	public static int lowerBoundSearch(final double[] arr, final int l, final int r, final double target) {
		validateRange(arr, l, r);
		return binarySearchLowerBound(arr, l, r - 1, target);
	}

	/**
	 * 倍精度浮動小数点数配列の指定範囲に対して上限探索(Upper Bound)を行います。
	 * 配列内の要素で目標値以上の最小の要素の位置を探索します。
	 *
	 * @param arr    探索対象の倍精度浮動小数点数配列
	 * @param l      探索開始位置（この位置を含む）
	 * @param r      探索終了位置（この位置を含まない）
	 * @param target 探索対象の倍精度浮動小数点数値
	 * @return 条件にちょうど当てはまるインデックス。探索に失敗した際の戻り値は-(挿入位置 + 1)となっています。
	 * @throws BSException 配列が {@code null} の場合、または範囲が不正な場合
	 */
	public static int upperBoundSearch(final double[] arr, final int l, final int r, final double target) {
		validateRange(arr, l, r);
		return binarySearchUpperBound(arr, l, r - 1, target);
	}

	/* -------------------------- ジェネリクス配列（T extends Comparable<? super T>） -------------------------- */

	/**
	 * 比較可能な型の配列全体に対して通常の二分探索を行います。
	 * 配列内の要素と目標値が一致する位置を探索します。
	 *
	 * @param arr    探索対象の配列
	 * @param target 探索対象の値
	 * @return 条件にちょうど当てはまるインデックス。探索に失敗した際の戻り値は-(挿入位置 + 1)となっています。
	 * @throws BSException 配列が {@code null} の場合、または範囲が不正な場合
	 */
	public static <T extends Comparable<? super T>> int normalSearch(final T[] arr, final T target) {
		validateRange(arr, 0, arr.length, target);
		return binarySearchNormal(arr, 0, arr.length - 1, target);
	}

	/**
	 * 比較可能な型の配列全体に対して下限探索(Lower Bound)を行います。
	 * 配列内の要素で目標値以下の最大の要素の位置を探索します。
	 *
	 * @param arr    探索対象の配列
	 * @param target 探索対象の値
	 * @return 条件にちょうど当てはまるインデックス。探索に失敗した際の戻り値は-(挿入位置 + 1)となっています。
	 * @throws BSException 配列が {@code null} の場合、または範囲が不正な場合
	 */
	public static <T extends Comparable<? super T>> int lowerBoundSearch(final T[] arr, final T target) {
		validateRange(arr, 0, arr.length, target);
		return binarySearchLowerBound(arr, 0, arr.length - 1, target);
	}

	/**
	 * 比較可能な型の配列全体に対して上限探索(Upper Bound)を行います。
	 * 配列内の要素で目標値以上の最小の要素の位置を探索します。
	 *
	 * @param arr    探索対象の配列
	 * @param target 探索対象の値
	 * @return 条件にちょうど当てはまるインデックス。探索に失敗した際の戻り値は-(挿入位置 + 1)となっています。
	 * @throws BSException 配列が {@code null} の場合、または範囲が不正な場合
	 */
	public static <T extends Comparable<? super T>> int upperBoundSearch(final T[] arr, final T target) {
		validateRange(arr, 0, arr.length, target);
		return binarySearchUpperBound(arr, 0, arr.length - 1, target);
	}

	/**
	 * 比較可能な型の配列の指定範囲に対して通常の二分探索を行います。
	 * 配列内の要素と目標値が一致する位置を探索します。
	 *
	 * @param arr    探索対象の配列
	 * @param l      探索開始位置（この位置を含む）
	 * @param r      探索終了位置（この位置を含まない）
	 * @param target 探索対象の値
	 * @return 条件にちょうど当てはまるインデックス。探索に失敗した際の戻り値は-(挿入位置 + 1)となっています。
	 * @throws BSException 配列が {@code null} の場合、または範囲が不正な場合
	 */
	public static <T extends Comparable<? super T>> int normalSearch(final T[] arr, final int l, final int r, final T target) {
		validateRange(arr, l, r, target);
		return binarySearchNormal(arr, l, r - 1, target);
	}

	/**
	 * 比較可能な型の配列の指定範囲に対して下限探索(Lower Bound)を行います。
	 * 配列内の要素で目標値以下の最大の要素の位置を探索します。
	 *
	 * @param arr    探索対象の配列
	 * @param l      探索開始位置（この位置を含む）
	 * @param r      探索終了位置（この位置を含まない）
	 * @param target 探索対象の値
	 * @return 条件にちょうど当てはまるインデックス。探索に失敗した際の戻り値は-(挿入位置 + 1)となっています。
	 * @throws BSException 配列が {@code null} の場合、または範囲が不正な場合
	 */
	public static <T extends Comparable<? super T>> int lowerBoundSearch(final T[] arr, final int l, final int r, final T target) {
		validateRange(arr, l, r, target);
		return binarySearchLowerBound(arr, l, r - 1, target);
	}

	/**
	 * 比較可能な型の配列の指定範囲に対して上限探索(Upper Bound)を行います。
	 * 配列内の要素で目標値以上の最小の要素の位置を探索します。
	 *
	 * @param arr    探索対象の配列
	 * @param l      探索開始位置（この位置を含む）
	 * @param r      探索終了位置（この位置を含まない）
	 * @param target 探索対象の値
	 * @return 条件にちょうど当てはまるインデックス。探索に失敗した際の戻り値は-(挿入位置 + 1)となっています。
	 * @throws BSException 配列が {@code null} の場合、または範囲が不正な場合
	 */
	public static <T extends Comparable<? super T>> int upperBoundSearch(final T[] arr, final int l, final int r, final T target) {
		validateRange(arr, l, r, target);
		return binarySearchUpperBound(arr, l, r - 1, target);
	}

	/* -------------------------- 内部コア処理メソッド -------------------------- */

	/**
	 * 整数配列に対する通常の二分探索の内部実装です。
	 *
	 * @param arr 探索対象の整数配列
	 * @param l   探索開始位置（この位置を含む）
	 * @param r   探索終了位置（この位置を含む）
	 * @param t   探索対象の整数値
	 * @return 条件にちょうど当てはまるインデックス。探索に失敗した際の戻り値は-(挿入位置 + 1)となっています。
	 */
	private static int binarySearchNormal(final int[] arr, int l, int r, final int t) {
		while (l <= r) {
			final int m = l + ((r - l) >>> 1);
			if (arr[m] > t) {
				r = m - 1;
			} else if (arr[m] < t) {
				l = m + 1;
			} else {
				return m;
			}
		}
		return ~l;
	}

	private static int binarySearchUpperBound(final int[] arr, int l, int r, final int t) {
		int ans = -1;
		while (l <= r) {
			final int m = l + ((r - l) >>> 1);
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
			final int m = l + ((r - l) >>> 1);
			if (arr[m] < t) {
				l = m + 1;
			} else {
				if (arr[m] == t) ans = m;
				r = m - 1;
			}
		}
		return ans == -1 ? ~l : ans;
	}

	/**
	 * 長整数配列に対する通常の二分探索の内部実装です。
	 *
	 * @param arr 探索対象の長整数配列
	 * @param l   探索開始位置（この位置を含む）
	 * @param r   探索終了位置（この位置を含む）
	 * @param t   探索対象の長整数値
	 * @return 条件にちょうど当てはまるインデックス。探索に失敗した際の戻り値は-(挿入位置 + 1)となっています。
	 */
	private static int binarySearchNormal(final long[] arr, int l, int r, final long t) {
		while (l <= r) {
			final int m = l + ((r - l) >>> 1);
			if (arr[m] > t) {
				r = m - 1;
			} else if (arr[m] < t) {
				l = m + 1;
			} else {
				return m;
			}
		}
		return ~l;
	}

	private static int binarySearchUpperBound(final long[] arr, int l, int r, final long t) {
		int ans = -1;
		while (l <= r) {
			final int m = l + ((r - l) >>> 1);
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
			final int m = l + ((r - l) >>> 1);
			if (arr[m] < t) {
				l = m + 1;
			} else {
				if (arr[m] == t) ans = m;
				r = m - 1;
			}
		}
		return ans == -1 ? ~l : ans;
	}

	/**
	 * 倍精度浮動小数点数配列に対する通常の二分探索の内部実装です。
	 *
	 * @param arr 探索対象の倍精度浮動小数点数配列
	 * @param l   探索開始位置（この位置を含む）
	 * @param r   探索終了位置（この位置を含む）
	 * @param t   探索対象の倍精度浮動小数点数値
	 * @return 条件にちょうど当てはまるインデックス。探索に失敗した際の戻り値は-(挿入位置 + 1)となっています。
	 */
	private static int binarySearchNormal(final double[] arr, int l, int r, final double t) {
		while (l <= r) {
			final int m = l + ((r - l) >>> 1);
			if (arr[m] > t) {
				r = m - 1;
			} else if (arr[m] < t) {
				l = m + 1;
			} else {
				return m;
			}
		}
		return ~l;
	}

	private static int binarySearchUpperBound(final double[] arr, int l, int r, final double t) {
		int ans = -1;
		while (l <= r) {
			final int m = l + ((r - l) >>> 1);
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
			final int m = l + ((r - l) >>> 1);
			if (arr[m] < t) {
				l = m + 1;
			} else {
				if (arr[m] == t) ans = m;
				r = m - 1;
			}
		}
		return ans == -1 ? ~l : ans;
	}

	/**
	 * 比較可能な型の配列に対する通常の二分探索の内部実装です。
	 *
	 * @param arr    探索対象の配列
	 * @param l      探索開始位置（この位置を含む）
	 * @param r      探索終了位置（この位置を含む）
	 * @param target 探索対象の値
	 * @return 条件にちょうど当てはまるインデックス。探索に失敗した際の戻り値は-(挿入位置 + 1)となっています。
	 * @throws BSException 配列要素が {@code null} の場合
	 */
	private static <T extends Comparable<? super T>> int binarySearchNormal(final T[] arr, int l, int r, final T target) {
		while (l <= r) {
			final int m = l + ((r - l) >>> 1);
			final T t = arr[m];
			final int c;
			try {
				c = t.compareTo(target);
			} catch (NullPointerException e) {
				throw new BSException(BSException.ErrorType.NULL_TARGET);
			}
			if (c > 0) {
				r = m - 1;
			} else if (c < 0) {
				l = m + 1;
			} else {
				return m;
			}
		}
		return ~l;
	}

	private static <T extends Comparable<? super T>> int binarySearchUpperBound(final T[] arr, int l, int r, final T target) {
		int ans = -1;
		while (l <= r) {
			final int m = l + ((r - l) >>> 1);
			final T t = arr[m];
			final int c;
			try {
				c = t.compareTo(target);
			} catch (NullPointerException e) {
				throw new BSException(BSException.ErrorType.NULL_TARGET);
			}
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
			final int m = l + ((r - l) >>> 1);
			final T t = arr[m];
			final int c;
			try {
				c = t.compareTo(target);
			} catch (NullPointerException e) {
				throw new BSException(BSException.ErrorType.NULL_TARGET);
			}
			if (c < 0) {
				l = m + 1;
			} else {
				if (c == 0) ans = m;
				r = m - 1;
			}
		}
		return ans == -1 ? ~l : ans;
	}

	/* -------------------------- 内部エラーハンドリング処理 -------------------------- */

	/**
	 * 配列の範囲が有効かどうかを検証します。
	 *
	 * @param arr 検証対象の整数配列
	 * @param l   開始位置
	 * @param r   終了位置
	 * @throws BSException 検証条件を満たさない場合
	 */
	private static void validateRange(final int[] arr, final int l, final int r) {
		if (arr == null) throw new BSException(BSException.ErrorType.NULL_ARRAY);
		if (l < 0 || r > arr.length) throw new BSException(BSException.ErrorType.INVALID_RANGE, l, r, arr.length);
		if (l >= r) throw new BSException(BSException.ErrorType.INVALID_BOUNDS, l, r);
	}

	/**
	 * 配列の範囲が有効かどうかを検証します。
	 *
	 * @param arr 検証対象の長整数配列
	 * @param l   開始位置
	 * @param r   終了位置
	 * @throws BSException 検証条件を満たさない場合
	 */
	private static void validateRange(final long[] arr, final int l, final int r) {
		if (arr == null) throw new BSException(BSException.ErrorType.NULL_ARRAY);
		if (l < 0 || r > arr.length) throw new BSException(BSException.ErrorType.INVALID_RANGE, l, r, arr.length);
		if (l >= r) throw new BSException(BSException.ErrorType.INVALID_BOUNDS, l, r);
	}

	/**
	 * 配列の範囲が有効かどうかを検証します。
	 *
	 * @param arr 検証対象の倍精度浮動小数点数配列
	 * @param l   開始位置
	 * @param r   終了位置
	 * @throws BSException 検証条件を満たさない場合
	 */
	private static void validateRange(final double[] arr, final int l, final int r) {
		if (arr == null) throw new BSException(BSException.ErrorType.NULL_ARRAY);
		if (l < 0 || r > arr.length) throw new BSException(BSException.ErrorType.INVALID_RANGE, l, r, arr.length);
		if (l >= r) throw new BSException(BSException.ErrorType.INVALID_BOUNDS, l, r);
	}

	/**
	 * 配列の範囲が有効かどうかを検証します。
	 *
	 * @param arr 検証対象の配列
	 * @param l   開始位置
	 * @param r   終了位置
	 * @param t   探索対象の値
	 * @throws BSException 検証条件を満たさない場合
	 */
	private static void validateRange(final Object[] arr, final int l, final int r, final Object t) {
		if (arr == null) throw new BSException(BSException.ErrorType.NULL_ARRAY);
		if (t == null) throw new BSException(BSException.ErrorType.NULL_TARGET);
		if (l < 0 || r > arr.length) throw new BSException(BSException.ErrorType.INVALID_RANGE, l, r, arr.length);
		if (l >= r) throw new BSException(BSException.ErrorType.INVALID_BOUNDS, l, r);
	}

	/* -------------------------- 二分探索専用例外クラス BSException -------------------------- */

	/**
	 * 二分探索で発生する例外を表すクラスです。
	 * 配列がnullの場合や、探索範囲が不正な場合にスローされます。
	 */
	public static class BSException extends RuntimeException {
		/**
		 * 指定されたエラーの種類と引数から例外を構築します。
		 *
		 * @param type エラーの種類
		 * @param args エラーメッセージの引数
		 */
		private BSException(final ErrorType type, final Object... args) {
			super(type.format(args));
		}

		/**
		 * エラーの種類を表す列挙型
		 */
		private enum ErrorType {
			NULL_ARRAY("Array is null."),
			INVALID_RANGE("Invalid range: left=%d, right=%d, length=%d."),
			INVALID_BOUNDS("Invalid bounds: left=%d, right=%d."),
			NULL_TARGET("Target value is null."),
			NULL_ELEMENT("Array element at index %d is null.");

			private final String messageFormat;

			ErrorType(final String messageFormat) {
				this.messageFormat = messageFormat;
			}

			public String format(final Object... args) {
				return String.format(messageFormat, args);
			}
		}
	}
}
