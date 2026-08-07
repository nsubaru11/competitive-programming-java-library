package lib.util.function;

import java.util.*;
import java.util.function.*;

/**
 * 3つの {@code int} 値を受け取り、{@code long} 値を返す関数を表します。
 */
@FunctionalInterface
public interface IntTernaryToLongFunction {
	/**
	 * 指定された値に関数を適用します。
	 *
	 * @param a 1つ目の値
	 * @param b 2つ目の値
	 * @param c 3つ目の値
	 * @return 関数の結果
	 */
	long applyAsLong(int a, int b, int c);

	/**
	 * この関数の結果に指定された関数を適用する合成関数を返します。
	 *
	 * @param after 後に適用する関数
	 * @return 合成された関数
	 */
	default IntTernaryToLongFunction andThen(final LongUnaryOperator after) {
		Objects.requireNonNull(after);
		return (a, b, c) -> after.applyAsLong(applyAsLong(a, b, c));
	}
}
