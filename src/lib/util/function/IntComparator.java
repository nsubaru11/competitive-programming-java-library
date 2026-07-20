package lib.util.function;

/**
 * 2つのint値を比較する関数です。
 */
@FunctionalInterface
public interface IntComparator {

	/**
	 * 2つの値を比較します。
	 *
	 * @param a 1つ目の値
	 * @param b 2つ目の値
	 * @return aが先なら負、同順位なら0、bが先なら正
	 */
	int compare(int a, int b);

	/**
	 * 比較順序を反転したComparatorを返します。
	 *
	 * @return 反転したComparator
	 */
	default IntComparator reversed() {
		return (a, b) -> compare(b, a);
	}

	/**
	 * 昇順のComparatorを返します。
	 *
	 * @return 昇順のComparator
	 */
	static IntComparator naturalOrder() {
		return Integer::compare;
	}

	/**
	 * 降順のComparatorを返します。
	 *
	 * @return 降順のComparator
	 */
	static IntComparator reverseOrder() {
		return (a, b) -> Integer.compare(b, a);
	}
}
