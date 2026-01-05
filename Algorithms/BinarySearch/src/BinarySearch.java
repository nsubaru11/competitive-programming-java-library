/**
 * 柔軟な二分探索アルゴリズムを提供するためのユーティリティクラスです。
 * <p>利用者はカスタムの比較ロジックを {@code CompareFunction} インターフェースによって実装する必要があります。</p>
 *
 * @see CompareFunction
 */
@SuppressWarnings("unused")
public final class BinarySearch {

	private BinarySearch() {
		throw new UnsupportedOperationException("このクラスはインスタンス化できません");
	}

	/* -------------------------- 通常の二分探索メソッド -------------------------- */

	/**
	 * 整数範囲での通常の二分探索を行います。{@code comparator} が 0 を返した時点で探索を終了します。
	 *
	 * @param l          下限値 (この数を含む)
	 * @param r          上限値 (この数を含まない)
	 * @param comparator 比較ロジックを提供する関数インターフェース。
	 *                   条件にちょうど当てはまる場合は 0 、超過する場合は正の値、そうでない場合は負の値を返してください。
	 * @return 条件にちょうど当てはまる整数。探索に失敗した際の戻り値は-(挿入位置(境界値) + 1)となっています。
	 * @throws BSException {@code CompareFunction} が {@code null} の場合、または範囲が不正な場合
	 */
	public static int normalSearch(final int l, final int r, final CompareFunction comparator) {
		validateRange(l, r, comparator);
		return (int) binarySearchNormal(l, r - 1, comparator);
	}

	/**
	 * 長整数範囲での通常の二分探索を行います。{@code comparator} が 0 を返した時点で探索を終了します。
	 *
	 * @param l          下限値 (この数を含む)
	 * @param r          上限値 (この数を含まない)
	 * @param comparator 比較ロジックを提供する関数インターフェース。
	 *                   条件にちょうど当てはまる場合は 0 、超過する場合は正の値、そうでない場合は負の値を返してください。
	 * @return 条件にちょうど当てはまる整数。探索に失敗した際の戻り値は-(挿入位置(境界値) + 1)となっています。
	 * @throws BSException {@code CompareFunction} が {@code null} の場合、または範囲が不正な場合
	 */
	public static long normalSearch(final long l, final long r, final CompareFunction comparator) {
		validateRange(l, r, comparator);
		return binarySearchNormal(l, r - 1, comparator);
	}

	/* -------------------------- 上限探索 (Upper Bound) メソッド -------------------------- */

	/**
	 * 整数範囲での上限探索(Upper Bound)を行います。条件を満たす最大の整数を返します。
	 *
	 * @param l          下限値 (この数を含む)
	 * @param r          上限値 (この数を含まない)
	 * @param comparator 比較ロジックを提供する関数インターフェース。
	 *                   条件にちょうど当てはまる場合は 0 、超過する場合は正の値、そうでない場合は負の値を返してください。
	 * @return 条件にちょうど当てはまる整数。探索に失敗した際の戻り値は-(挿入位置(境界値) + 1)となっています。
	 * @throws BSException {@code CompareFunction} が {@code null} の場合、または範囲が不正な場合
	 */
	public static int upperBoundSearch(final int l, final int r, final CompareFunction comparator) {
		validateRange(l, r, comparator);
		return (int) binarySearchUpperBound(l, r - 1, comparator);
	}

	/**
	 * 長整数範囲での上限探索(Upper Bound)を行います。条件を満たす最大の長整数を返します。
	 *
	 * @param l          下限値 (この数を含む)
	 * @param r          上限値 (この数を含まない)
	 * @param comparator 比較ロジックを提供する関数インターフェース
	 *                   条件にちょうど当てはまる場合は 0 、超過する場合は正の値、そうでない場合は負の値を返してください。
	 * @return 条件にちょうど当てはまる長整数。探索に失敗した際の戻り値は-(挿入位置(境界値) + 1)となっています。
	 * @throws BSException {@code CompareFunction} が {@code null} の場合、または範囲が不正な場合
	 */
	public static long upperBoundSearch(final long l, final long r, final CompareFunction comparator) {
		validateRange(l, r, comparator);
		return binarySearchUpperBound(l, r - 1, comparator);
	}

	/* -------------------------- 下限探索 (Lower Bound) メソッド -------------------------- */

	/**
	 * 整数範囲での下限探索(Lower Bound)を行います。条件を満たす最小の整数を返します。
	 *
	 * @param l          下限値 (この数を含む)
	 * @param r          上限値 (この数を含まない)
	 * @param comparator 比較ロジックを提供する関数インターフェース
	 *                   条件にちょうど当てはまる場合は 0 、超過する場合は正の値、そうでない場合は負の値を返してください。
	 * @return 条件にちょうど当てはまる整数。探索に失敗した際の戻り値は-(挿入位置(境界値) + 1)となっています。
	 * @throws BSException {@code CompareFunction} が {@code null} の場合、または範囲が不正な場合
	 */
	public static int lowerBoundSearch(final int l, final int r, final CompareFunction comparator) {
		validateRange(l, r, comparator);
		return (int) binarySearchLowerBound(l, r - 1, comparator);
	}

	/**
	 * 長整数範囲での下限探索(Lower Bound)を行います。条件を満たす最小の長整数を返します。
	 *
	 * @param l          下限値 (この数を含む)
	 * @param r          上限値 (この数を含まない)
	 * @param comparator 比較ロジックを提供する関数インターフェース
	 *                   条件にちょうど当てはまる場合は 0 、超過する場合は正の値、そうでない場合は負の値を返してください。
	 * @return 条件にちょうど当てはまる長整数。探索に失敗した際の戻り値は-(挿入位置(境界値) + 1)となっています。
	 * @throws BSException {@code CompareFunction} が {@code null} の場合、または範囲が不正な場合
	 */
	public static long lowerBoundSearch(final long l, final long r, final CompareFunction comparator) {
		validateRange(l, r, comparator);
		return binarySearchLowerBound(l, r - 1, comparator);
	}

	/* -------------------------- 内部コア処理メソッド -------------------------- */

	/**
	 * 通常の二分探索アルゴリズムを実行する内部メソッドです。
	 *
	 * @param l          下限値（この数を含む）
	 * @param r          上限値（この数を含む）
	 * @param comparator 比較ロジックを提供する関数インターフェース
	 * @return 探索結果のインデックス。または -(挿入位置 + 1) を返します。
	 */
	private static long binarySearchNormal(long l, long r, final CompareFunction comparator) {
		while (l <= r) {
			final long m = l + ((r - l) >>> 1);
			final long c;
			try {
				c = comparator.compare(m);
			} catch (Exception e) {
				throw new BSException(BSException.ErrorType.COMPARATOR_ERROR, e);
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

	/**
	 * 上限探索(Upper Bound)の二分探索アルゴリズムを実行する内部メソッドです。
	 *
	 * @param l          下限値（この数を含む）
	 * @param r          上限値（この数を含む）
	 * @param comparator 比較ロジックを提供する関数インターフェース
	 * @return 探索結果のインデックス。または -(挿入位置 + 1) を返します。
	 */
	private static long binarySearchUpperBound(long l, long r, final CompareFunction comparator) {
		Long ans = null;
		while (l <= r) {
			final long m = l + ((r - l) >>> 1);
			final long c;
			try {
				c = comparator.compare(m);
			} catch (Exception e) {
				throw new BSException(BSException.ErrorType.COMPARATOR_ERROR, e);
			}
			if (c > 0) {
				r = m - 1;
			} else {
				if (c == 0) ans = m;
				l = m + 1;
			}
		}
		return ans != null ? ans : ~l;
	}

	/**
	 * 下限探索(Lower Bound)の二分探索アルゴリズムを実行する内部メソッドです。
	 *
	 * @param l          下限値（この数を含む）
	 * @param r          上限値（この数を含む）
	 * @param comparator 比較ロジックを提供する関数インターフェース
	 * @return 探索結果のインデックス。または -(挿入位置 + 1) を返します。
	 */
	private static long binarySearchLowerBound(long l, long r, final CompareFunction comparator) {
		Long ans = null;
		while (l <= r) {
			final long m = l + ((r - l) >>> 1);
			final long c;
			try {
				c = comparator.compare(m);
			} catch (Exception e) {
				throw new BSException(BSException.ErrorType.COMPARATOR_ERROR, e);
			}
			if (c < 0) {
				l = m + 1;
			} else {
				if (c == 0) ans = m;
				r = m - 1;
			}
		}
		return ans != null ? ans : ~l;
	}


	/* -------------------------- 内部エラーハンドリング処理 -------------------------- */

	/**
	 * 配列の範囲が有効かどうかを検証します。
	 *
	 * @param l          開始位置
	 * @param r          終了位置
	 * @param comparator 比較関数
	 * @throws BSException 検証条件を満たさない場合
	 */
	private static void validateRange(final long l, final long r, final CompareFunction comparator) {
		if (comparator == null) throw new BSException(BSException.ErrorType.NULL_COMPARATOR);
		if (l > r) throw new BSException(BSException.ErrorType.INVALID_BOUNDS, l, r);
	}

	/* -------------------------- 比較用関数型インターフェース CompareFunction -------------------------- */

	/**
	 * 条件に基づいて値を比較するための関数型インターフェースです。
	 *
	 * <p>この関数は、二分探索アルゴリズムにて比較結果を表すために以下のルールに従う必要があります。</p>
	 *
	 * <ul>
	 *   <li> 正の値: 対象の値が条件を超過する場合。</li>
	 *   <li> 0: 対象の値が条件にちょうど当てはまる場合。</li>
	 *   <li> 負の値: 対象の値が条件より小さい場合。</li>
	 * </ul>
	 *
	 * <p>このルールを遵守することで、正しい探索結果が得られます。</p>
	 */
	@FunctionalInterface
	public interface CompareFunction {

		/**
		 * 指定されたインデックスの値を条件に基づいて比較します。
		 *
		 * @param index 現在比較中のインデックス位置。
		 * @return 比較結果:
		 * <ul>
		 *   <li>正の値: 対象の値が条件を超過する場合。</li>
		 *   <li>0: 対象の値が条件に一致する場合。</li>
		 *   <li>負の値: 対象の値が条件より小さい場合。</li>
		 * </ul>
		 */
		long compare(long index);
	}

	/* -------------------------- 二分探索専用例外クラス BSException -------------------------- */

	/**
	 * 二分探索で発生する例外を表すクラスです。
	 * {@code CompareFunction} が null の場合や、探索範囲が不正な場合にスローされます。
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
		 * 指定されたエラーの種類と原因例外から例外を構築します。
		 *
		 * @param type  エラーの種類
		 * @param cause 原因となる例外
		 */
		private BSException(final ErrorType type, final Throwable cause) {
			super(type.format(cause.getMessage()), cause);
		}

		/**
		 * エラーの種類を表す列挙型
		 */
		private enum ErrorType {
			NULL_COMPARATOR("ComparatorFunction is null."),
			INVALID_BOUNDS("Invalid bounds: left=%d, right=%d."),
			COMPARATOR_ERROR("Error occurred in comparator function: %s");

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
