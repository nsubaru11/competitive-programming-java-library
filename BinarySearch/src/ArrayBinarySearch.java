/**
 * 整数配列、長整数配列、倍精度浮動小数点数配列、および比較可能な型の配列に対して
 * 二分探索を行うためのユーティリティクラスです。
 * 通常の二分探索、上限探索(Upper Bound)、下限探索(Lower Bound)を提供します。
 * 探索に失敗した際の戻り値は-(挿入位置 + 1)となっています。
 */
@SuppressWarnings("unused")
public class ArrayBinarySearch {

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
	public static int normalSearch(int[] arr, int target) {
		validateRange(arr, 0, arr.length);
		return binarySearch(arr, 0, arr.length - 1, target, SearchType.NORMAL);
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
	public static int lowerBoundSearch(int[] arr, int target) {
		validateRange(arr, 0, arr.length);
		return binarySearch(arr, 0, arr.length - 1, target, SearchType.LOWER_BOUND);
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
	public static int upperBoundSearch(int[] arr, int target) {
		validateRange(arr, 0, arr.length);
		return binarySearch(arr, 0, arr.length - 1, target, SearchType.UPPER_BOUND);
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
	public static int normalSearch(int[] arr, int l, int r, int target) {
		validateRange(arr, l, r);
		return binarySearch(arr, l, r - 1, target, SearchType.NORMAL);
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
	public static int lowerBoundSearch(int[] arr, int l, int r, int target) {
		validateRange(arr, l, r);
		return binarySearch(arr, l, r - 1, target, SearchType.LOWER_BOUND);
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
	public static int upperBoundSearch(int[] arr, int l, int r, int target) {
		validateRange(arr, l, r);
		return binarySearch(arr, l, r - 1, target, SearchType.UPPER_BOUND);
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
	public static int normalSearch(long[] arr, long target) {
		validateRange(arr, 0, arr.length);
		return binarySearch(arr, 0, arr.length - 1, target, SearchType.NORMAL);
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
	public static int lowerBoundSearch(long[] arr, long target) {
		validateRange(arr, 0, arr.length);
		return binarySearch(arr, 0, arr.length - 1, target, SearchType.LOWER_BOUND);
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
	public static int upperBoundSearch(long[] arr, long target) {
		validateRange(arr, 0, arr.length);
		return binarySearch(arr, 0, arr.length - 1, target, SearchType.UPPER_BOUND);
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
	public static int normalSearch(long[] arr, int l, int r, long target) {
		validateRange(arr, l, r);
		return binarySearch(arr, l, r - 1, target, SearchType.NORMAL);
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
	public static int lowerBoundSearch(long[] arr, int l, int r, long target) {
		validateRange(arr, l, r);
		return binarySearch(arr, l, r - 1, target, SearchType.LOWER_BOUND);
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
	public static int upperBoundSearch(long[] arr, int l, int r, long target) {
		validateRange(arr, l, r);
		return binarySearch(arr, l, r - 1, target, SearchType.UPPER_BOUND);
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
	public static int normalSearch(double[] arr, double target) {
		validateRange(arr, 0, arr.length);
		return binarySearch(arr, 0, arr.length - 1, target, SearchType.NORMAL);
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
	public static int lowerBoundSearch(double[] arr, double target) {
		validateRange(arr, 0, arr.length);
		return binarySearch(arr, 0, arr.length - 1, target, SearchType.LOWER_BOUND);
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
	public static int upperBoundSearch(double[] arr, double target) {
		validateRange(arr, 0, arr.length);
		return binarySearch(arr, 0, arr.length - 1, target, SearchType.UPPER_BOUND);
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
	public static int normalSearch(double[] arr, int l, int r, double target) {
		validateRange(arr, l, r);
		return binarySearch(arr, l, r - 1, target, SearchType.NORMAL);
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
	public static int lowerBoundSearch(double[] arr, int l, int r, double target) {
		validateRange(arr, l, r);
		return binarySearch(arr, l, r - 1, target, SearchType.LOWER_BOUND);
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
	public static int upperBoundSearch(double[] arr, int l, int r, double target) {
		validateRange(arr, l, r);
		return binarySearch(arr, l, r - 1, target, SearchType.UPPER_BOUND);
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
	public static <T extends Comparable<? super T>> int normalSearch(T[] arr, T target) {
		validateRange(arr, 0, arr.length, target);
		return binarySearch(arr, 0, arr.length - 1, target, SearchType.NORMAL);
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
	public static <T extends Comparable<? super T>> int lowerBoundSearch(T[] arr, T target) {
		validateRange(arr, 0, arr.length, target);
		return binarySearch(arr, 0, arr.length - 1, target, SearchType.LOWER_BOUND);
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
	public static <T extends Comparable<? super T>> int upperBoundSearch(T[] arr, T target) {
		validateRange(arr, 0, arr.length, target);
		return binarySearch(arr, 0, arr.length - 1, target, SearchType.UPPER_BOUND);
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
	public static <T extends Comparable<? super T>> int normalSearch(T[] arr, int l, int r, T target) {
		validateRange(arr, l, r, target);
		return binarySearch(arr, l, r - 1, target, SearchType.NORMAL);
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
	public static <T extends Comparable<? super T>> int lowerBoundSearch(T[] arr, int l, int r, T target) {
		validateRange(arr, l, r, target);
		return binarySearch(arr, l, r - 1, target, SearchType.LOWER_BOUND);
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
	public static <T extends Comparable<? super T>> int upperBoundSearch(T[] arr, int l, int r, T target) {
		validateRange(arr, l, r, target);
		return binarySearch(arr, l, r - 1, target, SearchType.UPPER_BOUND);
	}

	/* -------------------------- 内部コア処理メソッド -------------------------- */

	/**
	 * 整数配列に対する二分探索の内部実装です。
	 *
	 * @param arr    探索対象の整数配列
	 * @param l      探索開始位置（この位置を含む）
	 * @param r      探索終了位置（この位置を含む）
	 * @param target 探索対象の整数値
	 * @param type   探索の種類
	 * @return 条件にちょうど当てはまるインデックス。探索に失敗した際の戻り値は-(挿入位置 + 1)となっています。
	 */
	private static int binarySearch(int[] arr, int l, int r, int target, SearchType type) {
		Integer ans = null;
		while (l <= r) {
			int m = l + ((r - l) >>> 1);
			switch (Integer.compare(arr[m], target)) {
				case 1:
					r = m - 1;
					break;
				case 0:
					switch (type) {
						case UPPER_BOUND:
							l = m + 1;
							break;
						case LOWER_BOUND:
							r = m - 1;
							break;
						case NORMAL:
							return m;
					}
					ans = m;
					break;
				case -1:
					l = m + 1;
					break;
			}
		}
		return ans == null ? ~l : ans;
	}

	/**
	 * 長整数配列に対する二分探索の内部実装です。
	 *
	 * @param arr    探索対象の長整数配列
	 * @param l      探索開始位置（この位置を含む）
	 * @param r      探索終了位置（この位置を含む）
	 * @param target 探索対象の長整数値
	 * @param type   探索の種類
	 * @return 条件にちょうど当てはまるインデックス。探索に失敗した際の戻り値は-(挿入位置 + 1)となっています。
	 */
	private static int binarySearch(long[] arr, int l, int r, long target, SearchType type) {
		int ans = -1;
		while (l <= r) {
			int m = l + ((r - l) >>> 1);
			switch (Long.compare(arr[m], target)) {
				case 1:
					r = m - 1;
					break;
				case 0:
					switch (type) {
						case UPPER_BOUND:
							l = m + 1;
							break;
						case LOWER_BOUND:
							r = m - 1;
							break;
						case NORMAL:
							return m;
					}
					ans = m;
					break;
				case -1:
					l = m + 1;
					break;
			}
		}
		return ans == -1 ? ~l : ans;
	}

	/**
	 * 倍精度浮動小数点数配列に対する二分探索の内部実装です。
	 *
	 * @param arr    探索対象の倍精度浮動小数点数配列
	 * @param l      探索開始位置（この位置を含む）
	 * @param r      探索終了位置（この位置を含む）
	 * @param target 探索対象の倍精度浮動小数点数値
	 * @param type   探索の種類
	 * @return 条件にちょうど当てはまるインデックス。探索に失敗した際の戻り値は-(挿入位置 + 1)となっています。
	 */
	private static int binarySearch(double[] arr, int l, int r, double target, SearchType type) {
		int ans = -1;
		while (l <= r) {
			int m = l + ((r - l) >>> 1);
			switch (Double.compare(arr[m], target)) {
				case 1:
					r = m - 1;
					break;
				case 0:
					switch (type) {
						case UPPER_BOUND:
							l = m + 1;
							break;
						case LOWER_BOUND:
							r = m - 1;
							break;
						case NORMAL:
							return m;
					}
					ans = m;
					break;
				case -1:
					l = m + 1;
					break;
			}
		}
		return ans == -1 ? ~l : ans;
	}

	/**
	 * 比較可能な型の配列に対する二分探索の内部実装です。
	 *
	 * @param arr    探索対象の配列
	 * @param l      探索開始位置（この位置を含む）
	 * @param r      探索終了位置（この位置を含む）
	 * @param target 探索対象の値
	 * @param type   探索の種類
	 * @return 条件にちょうど当てはまるインデックス。探索に失敗した際の戻り値は-(挿入位置 + 1)となっています。
	 * @throws BSException 配列要素が {@code null} の場合
	 */
	private static <T extends Comparable<? super T>> int binarySearch(T[] arr, int l, int r, T target, SearchType type) {
		int ans = -1;
		while (l <= r) {
			int m = l + ((r - l) >>> 1);
			T element = arr[m];
			if (element == null) {
				throw new BSException(BSException.ErrorType.NULL_ELEMENT, m);
			}
			switch (element.compareTo(target)) {
				case 1:
					r = m - 1;
					break;
				case 0:
					switch (type) {
						case UPPER_BOUND:
							l = m + 1;
							break;
						case LOWER_BOUND:
							r = m - 1;
							break;
						case NORMAL:
							return m;
					}
					ans = m;
					break;
				case -1:
					l = m + 1;
					break;
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
	private static void validateRange(int[] arr, int l, int r) {
		if (arr == null) {
			throw new BSException(BSException.ErrorType.NULL_ARRAY);
		}
		if (l < 0 || r > arr.length) {
			throw new BSException(BSException.ErrorType.INVALID_RANGE, l, r, arr.length);
		}
		if (l >= r) {
			throw new BSException(BSException.ErrorType.INVALID_BOUNDS, l, r);
		}
	}

	/**
	 * 配列の範囲が有効かどうかを検証します。
	 *
	 * @param arr 検証対象の長整数配列
	 * @param l   開始位置
	 * @param r   終了位置
	 * @throws BSException 検証条件を満たさない場合
	 */
	private static void validateRange(long[] arr, int l, int r) {
		if (arr == null) {
			throw new BSException(BSException.ErrorType.NULL_ARRAY);
		}
		if (l < 0 || r > arr.length) {
			throw new BSException(BSException.ErrorType.INVALID_RANGE, l, r, arr.length);
		}
		if (l >= r) {
			throw new BSException(BSException.ErrorType.INVALID_BOUNDS, l, r);
		}
	}

	/**
	 * 配列の範囲が有効かどうかを検証します。
	 *
	 * @param arr 検証対象の倍精度浮動小数点数配列
	 * @param l   開始位置
	 * @param r   終了位置
	 * @throws BSException 検証条件を満たさない場合
	 */
	private static void validateRange(double[] arr, int l, int r) {
		if (arr == null) {
			throw new BSException(BSException.ErrorType.NULL_ARRAY);
		}
		if (l < 0 || r > arr.length) {
			throw new BSException(BSException.ErrorType.INVALID_RANGE, l, r, arr.length);
		}
		if (l >= r) {
			throw new BSException(BSException.ErrorType.INVALID_BOUNDS, l, r);
		}
	}

	/**
	 * 配列の範囲が有効かどうかを検証します。
	 *
	 * @param arr 検証対象の配列
	 * @param l   開始位置
	 * @param r   終了位置
	 * @throws BSException 検証条件を満たさない場合
	 */
	private static void validateRange(Object[] arr, int l, int r, Object target) {
		if (arr == null) {
			throw new BSException(BSException.ErrorType.NULL_ARRAY);
		}
		if (l < 0 || r > arr.length) {
			throw new BSException(BSException.ErrorType.INVALID_RANGE, l, r, arr.length);
		}
		if (l >= r) {
			throw new BSException(BSException.ErrorType.INVALID_BOUNDS, l, r);
		}
	}

	/* -------------------------- 探索種別 (内部用 Enum 定義) -------------------------- */

	/**
	 * 内部的に利用される探索種別を示す列挙型
	 */
	private enum SearchType {
		NORMAL, UPPER_BOUND, LOWER_BOUND
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
		private BSException(ErrorType type, Object... args) {
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

			ErrorType(String messageFormat) {
				this.messageFormat = messageFormat;
			}

			public String format(Object... args) {
				return String.format(messageFormat, args);
			}
		}
	}
}