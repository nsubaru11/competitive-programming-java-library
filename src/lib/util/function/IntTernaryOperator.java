package lib.util.function;

/**
 * 3つの {@code int} 値を受け取り、{@code int} 値を返す演算を表します。
 */
@FunctionalInterface
public interface IntTernaryOperator {
	/**
	 * 指定された値に演算を適用します。
	 *
	 * @param a 1つ目の値
	 * @param b 2つ目の値
	 * @param c 3つ目の値
	 * @return 演算結果
	 */
	int applyAsInt(int a, int b, int c);
}
