package lib.util.function;

import java.util.*;

/**
 * 2つの {@code int} 値と1つの {@code long} 値を受け取る処理を表します。
 */
@FunctionalInterface
public interface IntBinaryLongConsumer {
	/**
	 * 指定された値を使って処理を実行します。
	 *
	 * @param a 1つ目の {@code int} 値
	 * @param b 2つ目の {@code int} 値
	 * @param c {@code long} 値
	 */
	void accept(int a, int b, long c);

	/**
	 * この処理の後に指定された処理を実行する合成処理を返します。
	 *
	 * @param after 後に実行する処理
	 * @return 合成された処理
	 */
	default IntBinaryLongConsumer andThen(final IntBinaryLongConsumer after) {
		Objects.requireNonNull(after);
		return (a, b, c) -> {
			accept(a, b, c);
			after.accept(a, b, c);
		};
	}
}
