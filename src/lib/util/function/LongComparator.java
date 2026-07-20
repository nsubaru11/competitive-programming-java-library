package lib.util.function;

/**
 * 2つのlong値を比較する関数です。
 */
@FunctionalInterface
public interface LongComparator {

	/**
	 * 2つの値を比較します。
	 *
	 * @param a 1つ目の値
	 * @param b 2つ目の値
	 * @return aが先なら負、同順位なら0、bが先なら正
	 */
	int compare(long a, long b);

	/**
	 * 比較順序を反転したComparatorを返します。
	 *
	 * @return 反転したComparator
	 */
	default LongComparator reversed() {
		return (a, b) -> compare(b, a);
	}

	/**
	 * 昇順のComparatorを返します。
	 *
	 * @return 昇順のComparator
	 */
	static LongComparator naturalOrder() {
		return Long::compare;
	}

	/**
	 * 降順のComparatorを返します。
	 *
	 * @return 降順のComparator
	 */
	static LongComparator reverseOrder() {
		return (a, b) -> Long.compare(b, a);
	}
}
